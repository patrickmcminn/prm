/*
Tuesday, March 11th 2014
Guitar.sc
prm
*/

FakeGuitar : IM_Module {

  var <isLoaded;

  var server,  noteGroup;
  var procBus, <synth;
  var attack, sustain, release;
  var vibratoSpeed, vibratoDepth;

  var noteDict, <sequencerDict, <sequencerClock, <tempo;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToTail' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait });
      this.prInitializeParameters;
      this.prAddSynthDefs;
      this.prMakeGroup;
      server.sync;
      this.prMakeBus;
      server.sync;
      this.prMakeSynth;
      while({ synth == nil }, { 0.001.wait; });
      isLoaded = true;
    }
  }


  prAddSynthDefs {

    SynthDef(\prm_fakeGuitarOsc, {
      |
      outBus = 0, freq = 220, amp = 1,
      attack = 0.05, sustain = 1.0, release = 0.05, gate = 1,
      vibratoSpeed = 6.1875, vibratoAmp = 4
      |
      var osc, vib, env, sig;
      vib = SinOsc.ar(vibratoSpeed).range(vibratoAmp.neg/2, vibratoAmp/2);
      osc = LPF.ar(LFSaw.ar(freq+vib) * -1, 20000);
      env = EnvGen.kr(Env.asr(attack, sustain, release), gate, doneAction: 2);
      sig = osc * env;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_fakeGuitarProc, {
      |
      inBus = 0, outBus = 0, amp = 1, pan = 0,
      delayTimeLeft = 0.006347, delayTimeRight = 0.0097656,  delayMix = 0.5,
      flangerWaveform = 1, flangerPulseWidth = 0.5,
      flangerMix = 0.25, flangerFeedback = 0.6, flangerSpeed = 0.4,
      flangerRangeLow = 0.0001, flangerRangeHigh = 0.003125,
      preDistortionCutoff = 793, preDistortionRQ = 0.35,
      distAmp = 0.1, distGain = 1000,
      reverbMix = 1, reverbGain = 0.1, roomSize = 1, damp = 0.9,
      lowGain = 6, highGain = 3,
      threshhold = 0.3555, slope = 0.333,
      filterType = 0, cutoff = 5364, rq = 1,
      waveLossAmount = 0
      |

      var input, delayLeft, delayRight, delay;
      var flangerSine, flangerHSine, flangerSaw, flangerRevSaw, flangerRect, flangerSampleAndHold, flangerNoise, flangerWave;
      var flangerIn, flanger, flangerSum, mix;
      var preFilter, distortion, reverb, sig;
      var lowShelf, highShelf, compressor, lowpass, bandpass, highpass, filter, hipass, waveLoss;

      input = In.ar(inBus);
      delayLeft = DelayL.ar(input, 0.1, delayTimeLeft, 0.5)  * -1;
      delayRight = DelayL.ar(input, 0.1, delayTimeRight, 0.5) * -1;
      delay = [delayLeft + (input * 0.5) , delayRight + (input * 0.5)];

      flangerSine = SinOsc.ar(flangerSpeed).range(flangerRangeLow, flangerRangeHigh);
      flangerHSine = SinOsc.ar(flangerSpeed).abs.range(flangerRangeHigh.neg, flangerRangeHigh);
      flangerSaw = LFSaw.ar(flangerSpeed).range(flangerRangeLow, flangerRangeHigh);
      flangerRevSaw = LFSaw.ar(flangerSpeed).range(flangerRangeLow, flangerRangeHigh) * -1;
      flangerRect = LFPulse.ar(flangerSpeed, 0, flangerPulseWidth).range(flangerRangeLow, flangerRangeHigh);
      flangerSampleAndHold = LFNoise0.ar(flangerSpeed).range(flangerRangeLow, flangerRangeHigh);
      flangerNoise = LFNoise2.ar(flangerSpeed).range(flangerRangeLow, flangerRangeHigh);
      flangerWave = SelectX.ar(flangerWaveform,
        [flangerSine, flangerHSine, flangerSaw, flangerRevSaw, flangerRect, flangerSampleAndHold, flangerNoise]);

      flangerIn = (LocalIn.ar(2) * (flangerFeedback * -1)) + delay;
      flanger = DelayN.ar(flangerIn, 0.2, flangerWave);

      LocalOut.ar(flanger);

      flangerSum = (delay * (1-flangerMix)) + (flanger * flangerMix);

      preFilter = RLPF.ar(flangerSum, preDistortionCutoff, preDistortionRQ);
      distortion = preFilter * distGain;
      distortion = distortion.distort * distAmp;
      reverb = FreeVerb2.ar(distortion[0], distortion[1], reverbMix, roomSize, damp);
      reverb = reverb * reverbGain;

      mix = distortion + reverb;

      lowShelf = BLowShelf.ar(mix, 250, 1, lowGain);
      highShelf = BHiShelf.ar(lowShelf, 1500, 1, highGain);
      compressor = Compander.ar(highShelf, highShelf, threshhold, 1, slope, 0.001, 0.3);
      lowpass = RLPF.ar(compressor, cutoff, rq);
      highpass = RHPF.ar(compressor, cutoff, rq);
      bandpass = BPF.ar(compressor, cutoff, rq);
      filter = Select.ar(filterType, [lowpass, highpass, bandpass]);

      waveLoss = WaveLoss.ar(filter, waveLossAmount, 100, 2);

      sig = waveLoss * amp;

      Out.ar(outBus, sig);
    }).add;

  }

  prInitializeParameters {
    noteDict = IdentityDictionary.new;
    sequencerDict = IdentityDictionary.new;
    tempo = 1;
    sequencerClock = TempoClock.new(tempo);
    attack = 0.05;
    sustain = 1.0;
    release = 0.05;
    vibratoSpeed = 6.1875;
    vibratoDepth = 4;
  }

  prMakeGroup { | relGroup = nil, addAction = 'addToTail' |
    noteGroup = Group.new(group, \addToHead);
  }

  prFreeGroup { noteGroup.free; }

  prMakeBus { procBus = Bus.audio; }

  prFreeBus { procBus.free; }

  prMakeSynth { | outBus = 0, amp = 1, pan = 0 |
    synth = Synth(\prm_fakeGuitarProc, [\inBus, procBus, \outBus, mixer.chanStereo(0), \amp, amp, \pan, pan], group, \addToHead);
  }

  prFreeSynth { synth.free; }

  /////// public functions:

  free {
    this.prFreeSynth;
    this.prFreeBus;
    this.prFreeGroup;
    sequencerClock.clear;
    sequencerClock.stop;
    sequencerClock = nil;
    sequencerDict.do({ | sequence | sequence.free; });
  }

  playNote { | freq = 220, amp = 1 |
    if( noteDict[freq.asSymbol].notNil, { this.releaseNote(freq); });
    noteDict[freq.asSymbol] = Synth(\prm_fakeGuitarOsc,
      [\outBus, procBus, \freq, freq, \amp, amp, \attack, attack, \sustain, sustain, \release, release, \vibratoSpeed,
        vibratoSpeed, \vibratoAmp, vibratoDepth],
      group, \addToHead);
  }

  releaseNote { | freq = 220 |
    var released;
    if ( noteDict[freq.asSymbol].notNil, {
      released = noteDict[freq.asSymbol];
      released.set(\gate, 0, \release, release);
      noteDict[freq.asSymbol] = nil;
    });
  }

  freeNote { | freq = 220 |
    var freed;
    if ( noteDict[freq.asSymbol].notNil, {
      freed = noteDict[freq.asSymbol];
      freed.free;
      noteDict[freq.asSymbol] = nil;
    });
  }

  setAttackTime { | atk = 0.05 |
    attack = atk;
    noteDict.do({ | synth | synth.set(\attack, attack); });
  }
  setReleaseTime { | rel = 0.05 |
    release = rel;
    noteDict.do({ | synth | synth.set(\release, release); });
  }
  setVibratoSpeed { | speed = 6.1875 |
    vibratoSpeed = speed;
    noteDict.do({ | synth | synth.set(\vibratoSpeed, vibratoSpeed) });
  }
  setVibratoDepth { | depth = 4 |
    vibratoDepth = depth;
    noteDict.do({ | synth | synth.set(\vibratoAmp, vibratoDepth); });
  }

  setNoteAmp { | freq, amp = 1 | noteDict[freq.asSymbol].set(\amp, amp); }
  setAmp { | amp = 1 | synth.set(\amp, amp); }
  setVol { | vol = 0 | this.setAmp(vol.dbamp); }
  setPan { | pan = 0 | synth.set(\pan, pan); }
  setOutput { | out = 0 | synth.set(\outBus, out); }
  setFlangerSpeed { | speed = 0.4 | synth.set(\flangerSpeed, speed); }
  setFlangerMix { | mix = 0.25 | synth.set(\flangerMix, mix); }
  setFlangerFeedback { | feedback = 0.5 | synth.set(\flangerFeedback, feedback); }
  setFlangerRangeLow { | rangeLow = 0.0001 | synth.set(\flangerRangeLow, rangeLow); }
  setFlangerRangeHigh { | rangeHigh = 0.0032125 | synth.set(\flangerRangeHigh, rangeHigh); }
  setFlangerWaveform { | waveform = 'sine' |
    if( waveform.isInteger || waveform.isFloat, { synth.set(\flangerWaveform, waveform); },
      {
        switch(waveform,
          { 'sine' }, { synth.set(\flangerWaveform, 0); },
          {'hSine' }, { synth.set(\flangerWaveform, 1); },
          { 'saw' }, { synth.set(\flangerWaveform, 2); },
          { 'revSaw' }, { synth.set(\flangerWaveform, 3); },
          { 'rect' }, { synth.set(\flangerWaveform, 4); },
          { 'sampleAndHold' }, { synth.set(\flangerWaveform, 5); },
          { 'noise' }, { synth.set(\flangerWaveform, 6); },
        );
    });
  }
  setFlangerPulseWidth { | width = 0.5 | synth.set(\flangerPulseWidth, width); }
  setPreDistortionCutoff { | cutoff = 793 | synth.set(\preDistortionCutoff, cutoff); }
  setPreDistortionRQ { | rq = 0.35 | synth.set(\preDistortionRQ, rq); }
  setDistortionAmp { | amp = 0.1 | synth.set(\distAmp, amp); }
  setDistortionGain { | gain = 1000 | synth.set(\distGain, gain); }
  setReverbMix { | mix = 1 | synth.set(\reverbMix, mix); }
  setReverbAmp { | amp = 0.1 | synth.set(\reverbGain, amp); }
  setReverbRoomSize { | size = 1 | synth.set(\roomSize, size); }
  setReverbDamp { | damp = 0.3 | synth.set(\damp, damp); }
  setLowGain { | gain = 6 | synth.set(\lowGain, gain); }
  setHighGain { | gain = 3 | synth.set(\highGain, gain); }
  setFilterCutoff { | cutoff = 5364 | synth.set(\cutoff, cutoff); }
  setFilterRQ { | rq = 1 | synth.set(\rq, rq); }
  setFilterType { | type = 0 | synth.set(\filterType, type); }
  setWaveLossAmount { | amount = 0 | synth.set(\waveLossAmount, amount); }

  // Pattern Sequencer:
  makeSequence { | uniqueName, type = 'note' |
    fork {
      switch(type,
        { 'note' }, {
          sequencerDict[uniqueName] = PatternSequencer.new(uniqueName, group, \addToHead);
          server.sync;
          sequencerDict[uniqueName].addKey(\instrument, \prm_fakeGuitarOsc);
          sequencerDict[uniqueName].addKey(\outBus, procBus);
        },
        { 'processor' }, {
          sequencerDict[uniqueName] = PatternSequencer.new(uniqueName, noteGroup, \addToTail);
          server.sync;
          sequencerDict[uniqueName].addKey(\type, \set);
          sequencerDict[uniqueName].addKey(\id, synth.nodeID);
          sequencerDict[uniqueName].addKey(\instrument, \prm_fakeGuitarProc);
          sequencerDict[uniqueName].addKey(\args, #[]);
          sequencerDict[uniqueName].addKey(\amp, 1);
      });
    }
  }

  addKey {  | uniqueName, key, action |
    sequencerDict[uniqueName].addKey(key, action);
  }

  playSequence { | uniqueName, clock = 'internal' |
    var playClock;
    if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
    sequencerDict[uniqueName].play(clock);
  }

  resetSequence { | uniqueName | sequencerDict[uniqueName].reset; }
  stopSequence { | uniqueName | sequencerDict[uniqueName].stop; }
  pauseSequence { | uniqueName | sequencerDict[uniqueName].pause }
  resumeSequence { | uniqueName | sequencerDict[uniqueName].resume; }
  isSequencePlaying { | uniqueName | ^sequencerDict[uniqueName].isPlaying }
  setSequenceQuant { | uniqueName, quant = 0 | sequencerDict[uniqueName].setQuant(quant) }

  setSequencerClockTempo { | bpm = 60 |
    var bps = bpm/60;
    tempo = bps;
    sequencerClock.tempo =tempo;
  }

}

  