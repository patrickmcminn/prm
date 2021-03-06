/*
Wednesday, June 3rd 2015
Wash.sc
Salzburg, Austria
Based on the SA.Wash Max for Live Effect
*/

Wash : IM_Processor {

  var server, <isLoaded;
  var synth;

  var <mix, <highPassCutoff, <lowPassCutoff;
  var <delayCoefficient, <feedbackCoefficient;
  var <modulatorFrequency, <modulatorDepth, <jitterRange, <jitterDepth;

  *newStereo { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo;
  }

  *newMono { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono;
  }

  prInitStereo {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      this.prInitializeParameters;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait });
      server.sync;
      mixer.setPreVol(3);
      synth = Synth(\prm_Wash_Stereo, [\inBus, inBus, \outBus, mixer.chanStereo(0), \amp, 1, \mix, mix,
        \highPassCutoff, highPassCutoff, \lowPassCutoff, lowPassCutoff,
        \delayCoefficient, delayCoefficient, \feedbackCoefficient, feedbackCoefficient,
        \modulatorFrequency, modulatorFrequency, \modulatorDepth, modulatorDepth,
        \jitterRange, jitterRange, \jitterDepth, jitterDepth],
        group, \addToHead);
      server.sync;
      isLoaded = true;
    };
  }

  prInitMono {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      this.prInitializeParameters;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait });
      server.sync;
      synth = Synth(\prm_Wash_Mono, [\inBus, inBus, \outBus, mixer.chanMono(0), \amp, 1, \mix, mix,
        \highPassCutoff, highPassCutoff, \lowPassCutoff, lowPassCutoff,
        \delayCoefficient, delayCoefficient, \feedbackCoefficient, feedbackCoefficient,
        \modulatorFrequency, modulatorFrequency, \modulatorDepth, modulatorDepth,
        \jitterRange, jitterRange, \jitterDepth, jitterDepth],
        group, \addToHead);
      server.sync;
      isLoaded = true;
    };
  }

  prInitializeParameters {
    mix = 0;
    highPassCutoff = 150;
    lowPassCutoff = 4375;
    delayCoefficient = 0.3;
    feedbackCoefficient = 0.7;
    jitterRange = 0.5;
    jitterDepth = 0.01;
  }

  prAddSynthDefs {
    SynthDef(\prm_Wash_Stereo, {
      |
      delayCoefficient = 0.3,
      inBus = 0, outBus = 0, amp = 1, mix = 0, highPassCutoff = 20, lowPassCutoff = 4375,
      feedbackCoefficient = 0.7, baseDecayTime = 20
      modulatorFrequency = 0.5, modulatorDepth = 0.02,
      jitterRange = 0.5, jitterDepth = 0.01
      |

      var input, initHighPass, preLowPass, preHighPass;
      var modulator, jitter, modulation;
      var comb1, comb2, comb3, comb4, comb5, comb6, comb7, comb8, combSum;
      var postLowPass, postHighPass;
      var crossfade, sig;

      input = In.ar(inBus, 2);
      initHighPass = HPF.ar(input, 100);
      preLowPass = LPF.ar(initHighPass, lowPassCutoff);
      preHighPass = HPF.ar(preLowPass, highPassCutoff);

      modulator = SinOsc.kr(modulatorFrequency) * modulatorDepth * 0.0001;
      jitter = LPF.ar(WhiteNoise.ar, jitterRange) * jitterDepth;
      modulation = modulator + jitter;
      //modulation.poll;

      comb1 = CombL.ar(preHighPass, 1, (0.0562 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb2 = CombL.ar(preHighPass, 1, (0.061 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb3 = CombL.ar(preHighPass, 1, (0.0215 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb4 = CombL.ar(preHighPass, 1, (0.0272 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb5 = CombL.ar(preHighPass, 1, (0.0785 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb6 = CombL.ar(preHighPass, 1, (0.07023 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb7 = CombL.ar(preHighPass, 1, (0.0461 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb8 = CombL.ar(preHighPass, 1, (0.0738 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      combSum = Mix.ar([comb1, comb2, comb3, comb4, comb5, comb6, comb7, comb8]);

      postLowPass = LPF.ar(combSum, lowPassCutoff);
      postHighPass = HPF.ar(postLowPass, highPassCutoff);
      crossfade = XFade2.ar(input, postHighPass, mix);
      sig = crossfade * amp;

      Out.ar(outBus, sig);

    }, [0.1]).add;

    SynthDef(\prm_Wash_Mono, {
      |
      delayCoefficient = 0.3,
      inBus = 0, outBus = 0, amp = 1, mix = 0, highPassCutoff = 20, lowPassCutoff = 4375,
       feedbackCoefficient = 0.7, baseDecayTime = 20
      modulatorFrequency = 0.5, modulatorDepth = 0.02,
      jitterRange = 0.5, jitterDepth = 0.01
      |

      var input, initHighPass, preLowPass, preHighPass;
      var modulator, jitter, modulation;
      var comb1, comb2, comb3, comb4, comb5, comb6, comb7, comb8, combSum;
      var postLowPass, postHighPass;
      var crossfade, sig;

      input = In.ar(inBus, 1);
      initHighPass = HPF.ar(input, 100);
      preLowPass = LPF.ar(initHighPass, lowPassCutoff);
      preHighPass = HPF.ar(preLowPass, highPassCutoff);

      modulator = SinOsc.kr(modulatorFrequency) * modulatorDepth * 0.05;
      jitter = LPF.ar(WhiteNoise.ar, jitterRange) * jitterDepth;
      modulation = modulator + jitter;
      //modulation.poll;

      comb1 = CombL.ar(preHighPass, 1, (0.0562 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb2 = CombL.ar(preHighPass, 1, (0.061 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb3 = CombL.ar(preHighPass, 1, (0.0215 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb4 = CombL.ar(preHighPass, 1, (0.0272 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb5 = CombL.ar(preHighPass, 1, (0.0785 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb6 = CombL.ar(preHighPass, 1, (0.07023 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb7 = CombL.ar(preHighPass, 1, (0.0461 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      comb8 = CombL.ar(preHighPass, 1, (0.0738 * delayCoefficient) + modulation,
        feedbackCoefficient * baseDecayTime);
      combSum = Mix.ar([comb1, comb2, comb3, comb4, comb5, comb6, comb7, comb8]);

      postLowPass = LPF.ar(combSum, lowPassCutoff);
      postHighPass = HPF.ar(postLowPass, highPassCutoff);
      crossfade = XFade2.ar(input, postHighPass, mix);
      sig = crossfade * amp;

      Out.ar(outBus, sig);

    }, [0.1]).add;
  }

  ///////// Public Methods:

  free {
    synth.free;
    synth = nil;
    this.freeProcessor;
  }

  setMix { | m = 0 |
    mix = m;
    synth.set(\mix, mix);
  }
  setHighPassCutoff { | cutoff = 20 |
    highPassCutoff = cutoff;
    synth.set(\highPassCutoff, highPassCutoff);
  }
  setLowPassCutoff { | cutoff = 4375 |
    lowPassCutoff = cutoff;
    synth.set(\lowPassCutoff, lowPassCutoff);
  }
  setDelayCoefficient { | coeff = 0.3 |
    delayCoefficient = coeff;
    synth.set(\delayCoefficient, delayCoefficient);
  }
  setFeedbackCoefficient { | coeff = 0.7 |
    feedbackCoefficient = coeff;
    synth.set(\feedbackCoefficient, feedbackCoefficient);
  }
  setModulatorFrequency { | freq = 0.5 |
    modulatorFrequency = freq;
    synth.set(\modulatorFrequency, modulatorFrequency);
  }
  setModulatorDepth { | depth = 0.02 |
    modulatorDepth = depth;
    synth.set(\modulatorDepth, modulatorDepth);
  }
  setJitterRange { | range = 0.5 |
    jitterRange = range;
    synth.set(\jitterRange, jitterRange);
  }
  setJitterDepth { | depth = 0.01 |
    jitterDepth = depth;
    synth.set(\jitterDepth, jitterDepth);
  }
}