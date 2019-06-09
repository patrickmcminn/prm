/*
Sunday, June 14th 2015
SaturSynth.sc
prm
on the road between Luxembourg and Brussels

6/17/2015 - adding tremolo
*/


SaturSynth : IM_Module {

  var <isLoaded, server;
  //var noteIsPlaying;
  var synth;
  var frequency, filterCutoff, attackTime, decayTime, sustainLevel, releaseTime;
  var <sequencerDict, <sequencerClock;

  *new {
    | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      this.prInitializeParameters;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;
      synth = Synth(\prm_SaturSynth, [\outBus, mixer.chanStereo(0),
        \attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel,
        \releaseTime, releaseTime, \gate, 0], group, \addToHead);
      sequencerDict = IdentityDictionary.new;
      sequencerClock = TempoClock.new;
      server.sync;
      isLoaded = true;
    }
  }

  prInitializeParameters {
    filterCutoff = 200;
    attackTime = 0.05;
    decayTime = 0;
    sustainLevel = 1;
    releaseTime = 0.05;
  }

  prAddSynthDefs {
    SynthDef(\prm_SaturSynth, {
      |
      filterCutoff = 200,
      outBus = 0, amp = 0.5, freq = 110,
      oscillatorFilterCutoff = 974, oscillatorAmp = 0.36,
      filterLFOFreq = 2.4, filterLFORatioLow = 0.8, filterLFORatioHigh = 1.2,
      distortionAmount = 63.0957,
      notchFrequency = 100, notchGain = 8.8, notchRQ = 1.43,
      attackTime = 0.05, decayTime = 0, sustainLevel = 1, releaseTime = 0.05,
      gate = 1,
      tremoloFreq = 1, tremoloDepth = 0,
      pan = 0
      |

      var sineOscillator, sawOscillator, oscillatorMix, filterLFO, oscillatorFilter;
      var distortion;
      var notchFilter, lowpassFilter, env, tremolo;
      var sig;

      sineOscillator = SinOsc.ar(freq);
      sawOscillator = Saw.ar(freq/2);
      oscillatorMix = Mix.ar([sineOscillator, sawOscillator]);
      filterLFO = LFNoise2.ar(2.4).range(filterLFORatioLow, filterLFORatioHigh);
      oscillatorFilter = BLowPass4.ar(oscillatorMix, oscillatorFilterCutoff * filterLFO);
      oscillatorFilter = oscillatorFilter * oscillatorAmp;

      distortion = oscillatorFilter * distortionAmount;
      distortion = distortion.clip(-1, 1);

      notchFilter = BPeakEQ.ar(distortion, notchFrequency, notchRQ, notchGain);
      //notchFilter = notchFilter.distort;
      lowpassFilter = BLowPass4.ar(notchFilter, filterCutoff);
      //lowpassFilter = BLowPass4.ar(notchFilter, freq * 2.5);
      lowpassFilter = Limiter.ar(lowpassFilter, 1, 0.01);

      //env = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime), gate, doneAction: 2);
      env = EnvGen.kr(Env.new([0, 0, 1, sustainLevel, 0], [0, attackTime, decayTime, releaseTime],
        curve: -4, releaseNode: 3), gate);
      env = lowpassFilter * env;
      tremolo = LFTri.ar(tremoloFreq).range((1-tremoloDepth), 1);

      sig = tremolo * env;
      //sig = Limiter.ar(sig, 1, 0.01);
      sig = Pan2.ar(sig, pan);
      sig = sig * amp;
      Out.ar(outBus, sig);
    }, [0.1]).add;

    SynthDef(\prm_SaturSynth_Seq, {
      |
      outBus = 0, amp = 0.5, freq = 110,
      oscillatorFilterCutoff = 974, oscillatorAmp = 0.36,
      filterLFOFreq = 2.4, filterLFORatioLow = 0.8, filterLFORatioHigh = 1.2,
      distortionAmount = 63.0957,
      filterCutoff = 200,
      notchFrequency = 100, notchGain = 8.8, notchRQ = 1.43,
      attackTime = 0.05, decayTime = 0, sustainLevel = 1, releaseTime = 0.05,
      gate = 1,
      tremoloFreq = 1, tremoloDepth = 0,
      pan = 0
      |

      var sineOscillator, sawOscillator, oscillatorMix, filterLFO, oscillatorFilter;
      var distortion;
      var notchFilter, lowpassFilter, env, tremolo;
      var sig;

      sineOscillator = SinOsc.ar(freq);
      sawOscillator = Saw.ar(freq/2);
      oscillatorMix = Mix.ar([sineOscillator, sawOscillator]);
      filterLFO = LFNoise2.ar(2.4).range(filterLFORatioLow, filterLFORatioHigh);
      oscillatorFilter = BLowPass4.ar(oscillatorMix, oscillatorFilterCutoff * filterLFO);
      oscillatorFilter = oscillatorFilter * oscillatorAmp;

      distortion = oscillatorFilter * distortionAmount;
      distortion = distortion.clip(-1, 1);

      notchFilter = BPeakEQ.ar(distortion, notchFrequency, notchRQ, notchGain);
      //notchFilter = notchFilter.distort;
      lowpassFilter = BLowPass4.ar(notchFilter, filterCutoff);
      //lowpassFilter = BLowPass4.ar(notchFilter, freq * 2.5);
      lowpassFilter = Limiter.ar(lowpassFilter, 1, 0.01);

      //env = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime), gate, doneAction: 2);
      env = EnvGen.kr(Env.new([0, 0, 1, sustainLevel, 0], [0, attackTime, decayTime, releaseTime],
        curve: -4, releaseNode: 3), gate, doneAction: 2);
      env = lowpassFilter * env;
      tremolo = LFTri.ar(tremoloFreq).range((1-tremoloDepth), 1);

      sig = tremolo * env;
      //sig = Limiter.ar(sig, 1, 0.01);
      sig = Pan2.ar(sig, pan);
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public methods:

  free {
    synth.free;
    this.freeModule;
    isLoaded = false;
  }

  playNote { | freq, vol = -6 |
    frequency = freq;
    synth.set(\gate, 1, \freq, frequency, \amp, vol.dbamp);
  }

  releaseNote { | freq |
    if( freq == frequency, { synth.set(\gate, 0); });
  }

  setAttackTime { | attack = 0.05 |
    attackTime = attack;
    synth.set(\attackTime, attackTime);
  }
  setDecayTime { | decay = 0 |
    decayTime = decay;
    synth.set(\decayTime, decayTime);
  }
  setSustainLevel { | sustain = 1 |
    sustainLevel = sustain;
    synth.set(\sustainLevel, sustainLevel);
  }
  setReleaseTime { | release = 0.05 |
    releaseTime = release;
    synth.set(\releaseTime, releaseTime);
  }
  setFilterCutoff { | cutoff = 200 |
    filterCutoff = cutoff;
    synth.set(\filterCutoff, filterCutoff); }
  setTremoloRate { | freq = 1 |
    synth.set(\tremoloFreq, freq);
  }
  setTremoloDepth { | depth = 0 |
    synth.set(\tremoloDepth, depth);
  }

  mapCutoff { | bus | synth.set(\filterCutoff, bus.asMap); }

  ///////// Pattern Sequencer:
  makeSequence { | name |
    fork {
      sequencerDict[name] = IM_PatternSeq.new(name, group, \addToHead);
      server.sync;
      sequencerDict[name].stop;
      sequencerDict[name].addKey(\instrument, \prm_SaturSynth_Seq);
      sequencerDict[name].addKey(\outBus, mixer.chanStereo(0));
      sequencerDict[name].addKey(\filterCutoff, Pfunc( { filterCutoff }));
      sequencerDict[name].addKey(\attackTime, Pfunc( { attackTime}));
      sequencerDict[name].addKey(\decayTime, Pfunc( { decayTime}));
      sequencerDict[name].addKey(\sustainLevel, Pfunc( { sustainLevel }));
      sequencerDict[name].addKey(\releaseTime, Pfunc({ releaseTime }));
      sequencerDict[name].addKey(\octave, 4);
      sequencerDict[name].addKey(\amp, 0.5);
    };
  }

  addKey { | name, key, action |
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
    sequencerClock.tempo = bps;
  }

}
