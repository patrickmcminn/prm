GlitchySynth {

  var server, group, noteGroup;
  var procBus, <synth, buffer;
  var attack, sustain, release;
  var <numVoices;

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
      this.prMakeGroups(relGroup, addAction);
      //this.prMakeBuffer;
      this.prMakeBus;
      server.sync;
      this.prMakeSynth(outBus, amp, pan);
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
      env = EnvGen.kr(Env.asr(attack, sustain, release), gate, doneAction: 2);
      sig = filter * env;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;


    SynthDef(\prm_glitchySynth_proc, {
      |
      inBus = 0, outBus = 0, amp = 1, pan = 0,
      attack = 0.05, susLevel = 1, release = 0.05, gate = 0,
      distAmp = 0.1, distGain = 1000, bitDepth = 8, noiseAmp = 0,
      loopAmp = 0.2, loopChance = 0.8, delayTime = 0.0664, repeats = 26,
      cutoff = 3580, rq = 1, waveLossAmount = 0
      |

      var input, crush, dist, noise, distSum, env;
      var buffer, loopTrig, loopRec, looper, sum, filter, panner, waveLoss, sig;

      input = In.ar(inBus);

      crush = Decimator.ar(input, 44100, bitDepth);
      dist = (crush * distGain).distort;
      dist = dist * distAmp;
      noise = WhiteNoise.ar(noiseAmp);
      distSum = dist + noise;

      //buffer = LocalBuf(server.sampleRate, 1);
      //loopTrig;
      //loopRec;
      //looper;
      //sum = distSum + looper;
      sum = distSum;

      filter = RLPF.ar(sum, cutoff, rq);
      panner = Pan2.ar(filter, pan);
      waveLoss = WaveLoss.ar(panner, waveLossAmount, 100, 2);

      env = EnvGen.kr(Env.asr(attack, susLevel, release), gate);

      sig = waveLoss * env;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;

  }

  prInitializeParameters {
    noteDict = IdentityDictionary.new;
    numVoices = 0;
    attack = 0.05;
    release = 0.05;
  }

  prMakeGroups { | relGroup = nil, addAction = 'addToTail' |
    group = Group.new(relGroup, addAction);
    noteGroup = Group.new(group, \addToHead);
  }

  prFreeGroups {
    group.free;
    noteGroup.free;
  }

  prMakeBus { procBus = Bus.audio; }

  prFreeBus { procBus.free; }

  prMakeSynth { | outBus = 0, amp = 1, pan = 0 |
    synth = Synth(\prm_glitchySynth_proc, [\inBus, procBus, \outBus, outBus, \amp, amp, \pan, pan],
      group, \addToTail);
  }


  prFreeSynth { synth.free; }

  //////// Public Functions:

  free {
    this.freeAllNotes;
    this.prFreeSynth;
    this.prFreeBus;
    this.prFreeGroups;
  }

  setAmp { | amp = 1 |
    synth.set(\amp, amp);
  }

  setVol { | vol = 0 |
    synth.set(\amp, vol.dbamp);
  }

  playNote { | freq = 220, amp = 1, cutoff = 5309, rq = 0.3 |
    if( noteDict[freq.asSymbol].notNil, { this.releaseNote(freq); });
    if( numVoices == 0,
      {
        synth.set(\attack, attack, \gate, 1);
        noteDict[freq.asSymbol] = Synth(\prm_glitchySynth_osc, [\outBus, procBus, \freq, freq, \amp, amp,
          \cutoff, cutoff, \rq, rq], noteGroup, \addToTail);
      },
      {
        noteDict[freq.asSymbol] = Synth(\prm_glitchySynth_osc, [\outBus, procBus, \freq, freq, \amp, amp,
          \attack, attack, \release, release, \cutoff, cutoff, \rq, rq], noteGroup, \addToTail);
    });
    numVoices = numVoices + 1;
  }

  releaseNote { | freq = 220 |
    var released;
    if( noteDict[freq.asSymbol].notNil, {
      released = noteDict[freq.asSymbol];
      released.set(\release, release, \gate, 0);
      noteDict[freq.asSymbol] = nil;
      }
    );
      numVoices = numVoices - 1;
  }

  freeNote { | freq = 220 |
    noteDict[freq.asSymbol].free;
    noteDict[freq.asSymbol] = nil;
    numVoices = numVoices = 1;
  }

  // releaseAllNotes { noteDict.do({ | synth | synth.set(\gate, 0); }); }

  freeAllNotes { noteDict.keysValuesDo({ | key | this.freeNote(key); }); }

  setAttack { | atk = 0.05 |
    attack = atk;
    noteDict.do({ | synth | synth.set(\attack, attack) });
  }

  setRelease { | rel = 0.05 |
    release = rel;
    noteDict.do({ | synth | synth.set(\release, release); });
  }

  setNoteAmp { | freq = 220, amp = 1 |
    noteDict[freq.asSymbol].set(\amp, amp);
  }

  setNoteCutoff { | freq = 220, cutoff = 5309 |
    noteDict[freq.asSymbol].set(\cutoff, cutoff);
  }

  setNoteRQ { | freq = 220, rq = 0.3 |
    noteDict[freq.asSymbol].set(\rq, rq);
  }

  setCutoff { | cutoff = 3580 |
    synth.set(\cutoff, cutoff);
  }

  setRQ { | rq = 1 |
    synth.set(\rq, rq);
  }

  setBitDepth { | bitDepth = 8 |
    synth.set(\bitDepth, bitDepth);
  }

  setWaveLossAmount { | amount = 0 | synth.set(\waveLossAmount, amount); }
}
