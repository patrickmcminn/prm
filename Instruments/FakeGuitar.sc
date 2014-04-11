/*
Tuesday, March 11th 2014
Guitar.sc
prm
*/

FakeGuitar {

  var server, <group, noteGroup;
  var procBus, synth;
  var attack, sustain, release;

  var noteDict;


  *new { | outBus = 0, amp = 1, pan = 0, relGroup = nil, addAction = 'addToTail' |
    ^super.new.prInit(outBus, amp, pan, relGroup, addAction);
  }

  prInit { | outBus = 0, amp = 1, pan = 0, relGroup = nil, addAction = 'addToTail' |
    server = Server.default;
    server.waitForBoot {
      this.prInitializeParameters;
      this.prAddSynthDefs;
      server.sync;
      this.prMakeGroup(relGroup, addAction);
      this.prMakeBus;
      server.sync;
      this.prMakeSynth(outBus, amp, pan);
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
      vib = SinOsc.ar(vibratoSpeed).range(vibratoAmp.neg/3, vibratoAmp/3);
      osc = Saw.ar(freq + vib);
      env = EnvGen.kr(Env.asr(attack, sustain, release), gate, doneAction: 2);
      sig = osc * env;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_fakeGuitarProc, {
      |
      inBus = 0, outBus = 0, amp = 1, pan = 0,
      delayTime = 0.006347, delayMix = 0.5,
      flangerMix = 0.25, flangerFeedback = 0.5, flangerSpeed = 0.4,
      flangerRangeLow = 0.0001, flangerRangeHigh = 0.0032125,
      preDistortionCutoff = 793, preDistortionRQ = 0.35,
      distAmp = 0.1, distGain = 1000,
      reverbMix = 1, reverbGain = 0.1, roomSize = 1, damp = 0.3,
      lowGain = 6, highGain = 3,
      threshhold = 0.3555, slope = 0.333,
      filterType = 0, cutoff = 5364, rq = 1,
      waveLossAmount = 0
      |

      var input, delay, flanger, flangerSum, mix, preFilter, distortion, panner, reverb, sig;
      var lowShelf, highShelf, compressor, lowpass, bandpass, highpass, filter, hipass, waveLoss;

      input = In.ar(inBus);
      delay = DelayL.ar(input, 0.1, delayTime, 0.5);
      delay = delay + (input * 0.5);
      flanger = LocalIn.ar(1) + delay;
      flanger = DelayN.ar(flanger, 0.2, SinOsc.ar(flangerSpeed).range(flangerRangeLow, flangerRangeHigh) - ControlRate.ir);

      LocalOut.ar(flanger * flangerFeedback);

      flangerSum = (delay * (1-flangerMix)) + (flanger * flangerMix);

      preFilter = RLPF.ar(flangerSum, preDistortionCutoff, preDistortionRQ);
      distortion = preFilter * distGain;
      distortion = distortion.distort * distAmp;
      panner = Pan2.ar(distortion, pan);
      reverb = FreeVerb2.ar(panner[0], panner[1], reverbMix, roomSize, damp);
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
    attack = 0.05;
    sustain = 1.0;
    release = 0.05;
  }

  prMakeGroup { | relGroup = nil, addAction = 'addToTail' |
    group = Group.new(relGroup, addAction);
    noteGroup = Group.new(group, \addToHead);
  }

  prFreeGroup { group.free; noteGroup.free; }

  prMakeBus { procBus = Bus.audio; }

  prFreeBus { procBus.free; }

  prMakeSynth { | outBus = 0, amp = 1, pan = 0 |
    synth = Synth(\prm_fakeGuitarProc, [\inBus, procBus, \outBus, outBus, \amp, amp, \pan, pan], group, \addToTail);
  }

  prFreeSynth { synth.free; }

  /////// public functions:

  free {
    this.prFreeSynth;
    this.prFreeBus;
    this.prFreeGroup
  }

  playNote { | freq = 220, amp = 1 |
    if( noteDict[freq.asSymbol].notNil, { this.releaseNote(freq); });
    noteDict[freq.asSymbol] = Synth(\prm_fakeGuitarOsc,
      [\outBus, procBus, \freq, freq, \amp, amp, \attack, attack, \sustain, sustain, \release, release],
      noteGroup, \addToTail);
  }

  releaseNote { | freq = 220 |
    var released;
    if ( noteDict[freq.asSymbol].notNil, {
      released = noteDict[freq.asSymbol];
      released.set(\gate, 0, \release, release);
      noteDict[freq.asSymbol] = nil;
    });
  }

  setAttack { | atk = 0.05 |
    attack = atk;
    noteDict.do({ | synth | synth.set(\attack, attack); });
  }

  setRelease { | rel = 0.05 |
    release = rel;
    noteDict.do({ | synth | synth.set(\release, release); });
  }

  freeNote { | freq = 220 |
    var freed;
    if ( noteDict[freq.asSymbol].notNil, {
      freed = noteDict[freq.asSymbol];
      freed.free;
      noteDict[freq.asSymbol] = nil;
    });
  }

  setNoteAmp { | freq, amp = 1 | noteDict[freq.asSymbol].set(\amp, amp); }

  setAmp { | amp = 1 | synth.set(\amp, amp); }

  setVol { | vol = 0 | this.setAmp(vol.dbamp); }

  setPan { | pan = 0 | synth.set(\pan, pan); }

  setOutput { | out = 0 | synth.set(\outBus, out); }

  setFlangerMix { | mix = 0.25 | synth.set(\flangerMix, mix); }

  setFlangerFeedback { | feedback = 0.5 | synth.set(\flangerFeedback, feedback); }

  setFlangerRangeLow { | rangeLow = 0.0001 | synth.set(\flangerRangeLow, rangeLow); }

  setFlangerRangeHigh { | rangeHigh = 0.0032125 | synth.set(\flangerRangeHigh, rangeHigh); }

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

  setCutoff { | cutoff = 5364 | synth.set(\cutoff, cutoff); }

  setRQ { | rq = 1 | synth.set(\rq, rq); }

  setFilterType { | type = 0 | synth.set(\filterType, type); }

  setWaveLossAmount { | amount = 0 | synth.set(\waveLossAmount, amount); }

}

  