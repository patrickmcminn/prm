/*
Monday, July 10th 2017
Hammond.cd
prm

shit emulation of a hammond organ, ok?
*/

Hammond : IM_Module {

  var <isLoaded;
  var server;

  var <subVol, <bassVol, <fundamentalVol;
  var <partial2Vol, <partial3Vol, <partial4Vol, <partial5Vol;
  var <partial6Vol, <partial8Vol;
  var <filterCutoff, <filterRes, <distortionAmount;
  var <attackTime, <sustainLevel, <releaseTime, <pan;

  var <tremoloSpeed, <tremoloDepth;

  var noteDict;

  var <sequencerDict, <sequencerClock, <tempo;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      this.prAddSynthDef;
      this.prSetInitialParameters;

      noteDict = IdentityDictionary.new;
      sequencerDict = IdentityDictionary.new;
      sequencerClock = TempoClock.new;

      server.sync;
      isLoaded = true;
    }
  }

  prAddSynthDef {

    SynthDef(\prm_hammond, {
      | outBus = 0, freq = 220, amp = 0.5,
      subAmp = 0.1, bassAmp = 0.1, fundAmp = 1,
      partial2Amp = 0.25, partial3Amp = 0.125, partial4Amp = 0.1, partial5Amp = 0.09, partial6Amp = 0.08, partial8Amp = 0.05,
      cutoff = 20000, res = 0.1, dist = 0.1, noise = 0.0003,
      attackTime = 0.01, sustainLevel = 1, releaseTime = 0.01, gate = 1
      pan = 0, tremoloSpeed = 0, tremoloDepth = 0
      |

      var subOctave, bass, fund, octave, octaveFifth, twoOct, twoOctThird, twoOctFifth, threeOct;
      var sum, env, filter, tremolo, sig;

      subOctave = SinOsc.ar(freq/2) * subAmp;
      bass = SinOsc.ar(freq * (3/4)) * bassAmp;
      fund = SinOsc.ar(freq) * fundAmp;
      octave = SinOsc.ar(freq * 2) * partial2Amp;
      octaveFifth = SinOsc.ar(freq * 3) * partial3Amp;
      twoOct = SinOsc.ar(freq * 4) * partial4Amp;
      twoOctThird = SinOsc.ar(freq * 5) * partial5Amp;
      twoOctFifth = SinOsc.ar(freq * 6) * partial6Amp;
      threeOct = SinOsc.ar(freq * 8) * partial8Amp;

      sum = Mix.new([ subOctave, bass, fund, octave, octaveFifth, twoOct, twoOctThird, twoOctFifth, threeOct]);
      filter = DFM1.ar(sum, cutoff, res, dist, 0, noise);
      env = EnvGen.kr(Env.asr(attackTime, sustainLevel, releaseTime), gate, doneAction: 2);
      sig = filter * env;
      sig = sig * amp;
      sig = Pan2.ar(sig, pan);
      Out.ar(outBus, sig);
    }).add;
  }

  prSetInitialParameters {
    subVol = -20;
    bassVol = -20;
    fundamentalVol = 0;
    partial2Vol = -12;
    partial3Vol = -18;
    partial4Vol = -20;
    partial5Vol = -21;
    partial6Vol = -22;
    partial8Vol = -26;
    filterCutoff = 20000;
    filterRes = 0;
    distortionAmount = 0.2;
    attackTime = 0.05;
    sustainLevel = 1;
    releaseTime = 0.05;
    pan = 0;
  }

  //////// public functions:

  free {
    this.freeModule;
    noteDict.do({ | synth | synth.free; });
    isLoaded = false;
  }

  playNote { | freq = 220, vol = -6 |
    noteDict[freq.asSymbol] = Synth(\prm_hammond,
      [
        \outBus, mixer.chanStereo(0), \freq, freq, \amp, vol.dbamp,
        \subAmp, subVol.dbamp, \bassAmp, bassVol.dbamp, \fundAmp, fundamentalVol.dbamp,
        \partial2Amp, partial2Vol.dbamp, \partial3Amp, partial3Vol.dbamp,
        \partial4Amp, partial4Vol.dbamp, \partial5Amp, partial5Vol.dbamp,
        \partial6Amp, partial6Vol.dbamp, \partial8Amp, partial8Vol.dbamp,
        \cutoff, filterCutoff, \res, filterRes, \dist, distortionAmount,
        \attackTime, attackTime, \sustainLevel, sustainLevel, \releaseTime, releaseTime,
        \pan, pan
      ], group, \addToHead);
  }

  releaseNote { | freq = 220 |
    noteDict[freq.asSymbol].set(\gate, 0);
  }

  setAttackTime { | attack = 0.05 |
    attackTime = attack;
    noteDict.do({ | synth | synth.set(\attackTime, attackTime) });
  }

  setSustainLevel { |sustain = 1 |
    sustainLevel = sustain;
    noteDict.do({ | synth | synth.set(\sustainLevel, sustainLevel); });
  }

  setReleaseTime { | release = 0.05 |
    releaseTime = release;
    noteDict.do({ | synth | synth.set(\releaseTime, releaseTime); });
  }

  setFilterCutoff { | cutoff = 20000 |
    filterCutoff = cutoff;
    noteDict.do({ | synth | synth.set(\cutoff, filterCutoff) });
  }

  setFilterRest { | res = 0 |
    filterRes = res;
    noteDict.do({ | synth | synth.set(\res, filterRes);  });
  }

  setDistortionAmount { | dist = 0.2 |
    distortionAmount = dist;
    noteDict.do({ | synth | synth.set(\dist, distortionAmount) });
  }

  //////// partials:

  setSubVol { | vol = -20 |
    subVol = vol;
    noteDict.do({ | synth | synth.set(\subAmp, subVol.dbamp); });
  }

  setBassVol { | vol = -20 |
    bassVol = vol;
    noteDict.do({ | synth | synth.set(\bassAmp, bassVol.dbamp); });
  }

  setFundamentalVol { | vol = 0 |
    fundamentalVol = vol;
    noteDict.do({ | synth | synth.set(\fundAmp, fundamentalVol.dbamp) });
  }

  setPartial2Vol {  | vol = -12 |
    partial2Vol = vol;
    noteDict.do({ | synth | synth.set(\partial2Amp, partial2Vol.dbamp) });
  }

  setPartial3Vol { | vol = -18 |
    partial3Vol = vol;
    noteDict.do({ | synth | synth.set(\partial3Amp, partial3Vol.dbamp) });
  }

  setPartial4Vol { | vol = -20 |
    partial4Vol = vol;
    noteDict.do({ | synth | synth.set(\partial4Amp, partial4Vol.dbamp) });
  }

  setPartial5Vol { | vol = -21 |
    partial5Vol = vol;
    noteDict.do({ | synth | synth.set(\partial5Amp, partial5Vol.dbamp) });
  }

  setPartial6Vol { | vol = -22 |
    partial6Vol = vol;
    noteDict.do({ | synth | synth.set(\partial6Amp, partial6Vol.dbamp) });
  }

  setPartial8Vol { | vol = -26 |
    partial8Vol = vol;
    noteDict.do({ | synth | synth.set(\partial8Amp, partial8Vol.dbamp) });
  }

  ///////// Pattern Sequencer:
  makeSequence { | name |
    fork {
      sequencerDict[name] = IM_PatternSeq.new(name, group, \addToHead);
      sequencerDict[name].stop;
      server.sync;
      sequencerDict[name].addKey(\instrument, \prm_hammond);
      sequencerDict[name].addKey(\outBus, mixer.chanStereo(0));
      sequencerDict[name].addKey(\attackTime, Pfunc({ attackTime }));
      sequencerDict[name].addKey(\sustainLevel, Pfunc({ sustainLevel }));
      sequencerDict[name].addKey(\releaseTime, Pfunc({ releaseTime }));
      sequencerDict[name].addKey(\amp, 0.5);
      sequencerDict[name].addKey(\dist, Pfunc({ distortionAmount }));
      sequencerDict[name].addKey(\subAmp, Pfunc({ subVol.dbamp }));
      sequencerDict[name].addKey(\bassAmp, Pfunc({ bassVol.dbamp }));
      sequencerDict[name].addKey(\fundAmp, Pfunc({ fundamentalVol.dbamp }));
      sequencerDict[name].addKey(\partial2Amp, Pfunc( { partial2Vol.dbamp }));
      sequencerDict[name].addKey(\partial3Amp, Pfunc( { partial3Vol.dbamp }));
      sequencerDict[name].addKey(\partial4Amp, Pfunc( { partial4Vol.dbamp }));
      sequencerDict[name].addKey(\partial5Amp, Pfunc( { partial5Vol.dbamp }));
      sequencerDict[name].addKey(\partial6Amp, Pfunc( { partial6Vol.dbamp }));
      sequencerDict[name].addKey(\partial8Amp, Pfunc( { partial8Vol.dbamp }));
      sequencerDict[name].addKey(\cutoff, Pfunc({ filterCutoff }));
      sequencerDict[name].addKey(\res, Pfunc({ filterRes }));
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