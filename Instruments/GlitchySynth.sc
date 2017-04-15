GlitchySynth : IM_Module {

  var server, <isLoaded;

  var synth, buffer;
  var <attackTime, <sustainLevel, <releaseTime;
  var <glitchLooper;

  var procBus;

  var noteDict, <numVoices;
  var <sequencerDict, <sequencerClock, <tempo;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = 'addToTail' |
    ^super.new(2, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      this.prInitializeParameters;
      this.prAddSynthDefs;
      server.sync;

      mixer.tglMute(0);
      mixer.tglMute(1);

      glitchLooper = GlitchLooper.newStereo(mixer.chanStereo(1), 1, relGroup: group, addAction: \addToHead);
      while({ try { glitchLooper.isLoaded } != true }, { 0.001.wait; });

      procBus = Bus.audio(server, 2);
      this.prMakeSynth(mixer.chanStereo(0), glitchLooper.inBus);

      sequencerDict = IdentityDictionary.new;
      sequencerClock = TempoClock.new;

      server.sync;

      this.playNote(0);
      this.releaseNote(0);
      server.sync;
      mixer.tglMute(0);
      mixer.tglMute(1);

      mixer.setVol(1, -6);

      isLoaded = true;
    }
  }

  prAddSynthDefs {
    SynthDef(\prm_glitchySynth_osc, {
      |
      outBus = 0, freq = 220, amp = 1,
      attack = 0.05, sustain = 1, release = 0.05, gate = 1,
      cutoff = 5309, rq = 0.3
      |
      var saw, env, filter, sig;
      saw = Saw.ar(freq);
      filter = RLPF.ar(saw, cutoff, rq);
      env = EnvGen.kr(Env.asr(attack, sustain, release), gate + Impulse.ar(0), doneAction: 2);
      sig = filter * env;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;


    SynthDef(\prm_glitchySynth_proc, {
      |
      inBus = 0, outBus1 = 0, outBus2 = 0, amp = 1, pan = 0,
      attack = 0.05, susLevel = 1, release = 0.05, gate = 0,
      distAmp = 0.1, distGain = 10, bitDepth = 6, noiseAmp = 0,
      cutoff = 3000, rq = 1, waveLossAmount = 0
      |

      var input, crush, dist, noise, distSum, env;
      var buffer, playHead, recordHead, recorder, looper, sum, filter, panner, waveLoss, highPass, sig;

      input = In.ar(inBus);

      crush = Decimator.ar(input, 44100, bitDepth);
      dist = (crush * distGain).distort;
      dist = dist * distAmp;
      noise = WhiteNoise.ar(noiseAmp);
      distSum = dist + noise;
      sum = distSum;

      filter = RLPF.ar(sum, cutoff, rq);
      panner = Pan2.ar(filter, pan);
      waveLoss = WaveLoss.ar(panner, waveLossAmount, 100, 2);

      highPass = HPF.ar(waveLoss, 50);

      env = EnvGen.kr(Env.asr(attack, susLevel, release), gate);

      sig = highPass * env;
      sig = sig * amp;
      Out.ar(outBus1, sig);
      Out.ar(outBus2, sig);
    }).add;

  }

  prInitializeParameters {
    noteDict = IdentityDictionary.new;
    numVoices = 0;
    attackTime = 0.05;
    releaseTime = 0.05;
  }

  prMakeSynth { | outBus1 = 0, outBus2 = 0 |
    synth = Synth(\prm_glitchySynth_proc, [\inBus, procBus, \outBus1, outBus1, \outBus2, outBus2, \amp, 1, \pan, 0],
      group, \addToHead);
  }


  prFreeSynth { synth.free; }

  //////// Public Functions:

  free {
    this.freeAllNotes;
    this.prFreeSynth;
    this.prFreeBus;
    this.prFreeGroups;
  }


  playNote { | freq = 220, amp = 1, cutoff = 5309, rq = 0.3 |
    if( noteDict[freq.asSymbol].notNil, { this.releaseNote(freq); });
    if( numVoices == 0,
      {
        synth.set(\attack, attackTime, \gate, 1);
        noteDict[freq.asSymbol] = Synth(\prm_glitchySynth_osc, [\outBus, procBus, \freq, freq, \amp, amp,
          \cutoff, cutoff, \rq, rq], group, \addToHead);
      },
      {
        noteDict[freq.asSymbol] = Synth(\prm_glitchySynth_osc, [\outBus, procBus, \freq, freq, \amp, amp,
          \attack, attackTime, \release, releaseTime, \cutoff, cutoff, \rq, rq], group, \addToHead);
    });
    numVoices = numVoices + 1;
  }

  releaseNote { | freq = 220 |
    var released;
    if( noteDict[freq.asSymbol].notNil, {
      released = noteDict[freq.asSymbol];
      released.set(\release, releaseTime, \gate, 0);
      noteDict[freq.asSymbol] = nil;
      }
    );
      numVoices = numVoices - 1;
  }

  freeNote { | freq = 220 |
    noteDict[freq.asSymbol].free;
    noteDict[freq.asSymbol] = nil;
    numVoices = numVoices - 1;
  }

  // releaseAllNotes { noteDict.do({ | synth | synth.set(\gate, 0); }); }

  freeAllNotes { noteDict.keysValuesDo({ | key | this.freeNote(key); }); }

  setAttackTime { | attack = 0.05 |
    attackTime = attack;
    noteDict.do({ | synth | synth.set(\attack, attack) });
  }

  setReleaseTime { | release = 0.05 |
    releaseTime = release;
    noteDict.do({ | synth | synth.set(\release, release); });
  }


  setNoteFilterCutoff { | freq = 220, cutoff = 5309 |
    noteDict[freq.asSymbol].set(\cutoff, cutoff);
  }

  setNoteFilterRQ { | freq = 220, rq = 0.3 |
    noteDict[freq.asSymbol].set(\rq, rq);
  }

  setFilterCutoff { | cutoff = 3580 |
    synth.set(\cutoff, cutoff);
  }

  setFilterRQ { | rq = 1 |
    synth.set(\rq, rq);
  }

  setBitDepth { | bitDepth = 8 |
    synth.set(\bitDepth, bitDepth);
  }

  setWaveLossAmount { | amount = 0 | synth.set(\waveLossAmount, amount); }

  ///////// Pattern Sequencer:
  makeSequence { | name |
    fork {
      sequencerDict[name] = IM_PatternSeq.new(name, group, \addToHead);
      sequencerDict[name].stop;
      server.sync;
      sequencerDict[name].addKey(\instrument, \prm_glitchySynth_osc);
      sequencerDict[name].addKey(\outBus, procBus);
      //sequencerDict[name].addKey(\attack, Pfunc({ attackTime }));
      //sequencerDict[name].addKey(\sustainLevel, Pfunc({ sustainLevel }));
      //sequencerDict[name].addKey(\release, Pfunc({ releaseTime }));
      sequencerDict[name].addKey(\amp, 1);

    };
  }

  addKey {  | name, key, action |
    sequencerDict[name].addKey(key, action);
  }

  playSequence { | name, clock = 'internal', quant = 'nil' |
    var playClock;
    if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
    sequencerDict[name].play(playClock);
  }

  resetSequence { | name | sequencerDict[name].reset; }
  stopSequence { | name | sequencerDict[name].stop; }
  pauseSequence { | name | sequencerDict[name].pause }
  resumeSequence { | name | sequencerDict[name].resume; }
  isSequencePlaying { | name | ^sequencerDict[name].isPlaying }
  setSequenceQuant { | name, quant = 0 | sequencerDict[name].setQuant(quant) }

  setSequencerClockTempo { | bpm = 60 |
    var bps = bpm/60;
    tempo = bps;
    sequencerClock.tempo = tempo;
  }
}
