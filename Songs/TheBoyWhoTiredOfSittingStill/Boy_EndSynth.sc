/*
July 25th 2017
Boy_EndSynth.sc
prm
*/

Boy_EndSynth : IM_Module {

  var <isLoaded;
  var server;

  var <granulator, <eq, <reverb, <antenna, <antennaBus, <delay, <hammond;

  var <endLoopIsPlaying;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      endLoopIsPlaying = false;

      antennaBus = Bus.audio(server, 2);

      server.sync;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      this.prAddSynthDef;

      delay = SimpleDelay.newStereo(mixer.chanStereo(0), 0.549, 0.5, 0.6, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } !=  true }, { 0.001.wait; });

      antenna = Synth(\prm_boy_brokenAntenna, [\mix, 0.8, \outputGain, 6.dbamp, \inBus, antennaBus, \outBus, delay.inBus],
        group, 'addToHead');
      while({ try { antenna == nil } }, { 0.001.wait; });

      server.sync;

      reverb = IM_Reverb.new(antennaBus, mix: 0.3, roomSize: 0.75, damp: 0.21, relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(reverb.inBus, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(eq.inBus, group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      hammond = Hammond.new(granulator.inBus, relGroup: group, addAction: \addToHead);
      while({ try { hammond.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

      server.sync;

      this.prMakePatternParameters;

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_boy_brokenAntenna, {
      |
      inBus, outBus, mix = 1,
      lowFreq = 120, lowAtk = 0.00137, lowRel = 0.008
      lowInputGain = 0.7079, lowOutputGain = 1.07152,
      lowThresh = 0.14125, lowBelowRatio = 4, lowAboveRatio = 0.23,
      midAtk = 0.00228, midRel = 0.0096,
      midInputGain = 0.6839, midOutputGain = 0.71614,
      midThresh = 0.14125, midBelowRatio = 4, midAboveRatio = 0.465 ,
      hiFreq = 3250, hiAtk = 0.00042, hiRel = 0.00449,
      hiInputGain = 0.6839, hiOutputGain = 1.92752,
      hiThresh = 0.23041, hiBelowRatio = 4,  hiAboveRatio = 1,
      outputGain = 7.9433,
      amp = 1
      |

      var input, dry, low, mid, hi, sum, sig;
      input = In.ar(inBus, 2);
      dry = input * (1-mix);
      low = LPF.ar(input, lowFreq);
      low = low * lowInputGain;
      low = Compander.ar(low, low, lowThresh, lowBelowRatio, lowAboveRatio, lowAtk, lowRel);
      low = low * lowOutputGain;
      mid = BBandPass.ar(input, 1685, 4);
      mid = mid * midInputGain;
      mid = Compander.ar(mid, mid, midThresh, midBelowRatio, midAboveRatio, midAtk, midRel);
      mid = mid * midOutputGain;
      hi = HPF.ar(input, hiFreq);
      hi = hi * hiInputGain;
      hi = Compander.ar(hi, hi, hiThresh, hiBelowRatio, hiAboveRatio, hiAtk, hiRel);
      hi = hi*hiOutputGain;
      sum = low + mid + hi;
      sig = sum * outputGain;
      sig = sig * mix;
      sig = dry + sig;
      sig = sig * amp;
      sig = sig.softclip;
      sig = Out.ar(outBus, sig);
    }).add;
  }

  prSetInitialParameters {
    // delay:
    delay.setMix(0.3);

    // eq:
    eq.setLowGain(-12.1);

    // granulator:
    granulator.setGrainDur(0.0459, 0.0859);
    granulator.setTrigRate(15);
    granulator.setGranulatorCrossfade(1);
    granulator.setPan(-0.6, 0.6);

    // hammond:
    hammond.makeSequence(\endLoop);
    hammond.setSubVol(0.7.ampdb);
    hammond.setBassVol(-inf);
    hammond.setPartial2Vol(0.1.ampdb);
    hammond.setPartial3Vol(0.01.ampdb);
    hammond.setPartial4Vol(-inf);
    hammond.setPartial5Vol(-inf);
    hammond.setPartial6Vol(0.001.ampdb);
    hammond.setPartial8Vol(0.005.ampdb);
    hammond.setDistortionAmount(0.65);
  }

  prMakePatternParameters {
    hammond.addKey(\endLoop, \subAmp, 0.7);
    hammond.addKey(\endLoop, \bassAmp, 0);
    hammond.addKey(\endLoop, \partial2Amp, 0.1);
    hammond.addKey(\endLoop, \partial3Amp, 0.01);
    hammond.addKey(\endLoop, \partial4Amp, 0);
    hammond.addKey(\endLoop, \partial5Amp, 0);
    hammond.addKey(\endLoop, \partial6Amp, 0.001);
    hammond.addKey(\endLoop, \partial8Amp, 0.005);
    hammond.addKey(\endLoop, \dur, 6);
    hammond.addKey(\endLoop, \dist, 0.65);
    hammond.addKey(\endLoop, \root, 7);
    hammond.addKey(\endLoop, \octave, 6);
    hammond.addKey(\endLoop, \note, Pseq([[-1, 0, 4], [-3, 1, 4]], inf));
    hammond.addKey(\endLoop, \amp, Pseq([0.15, 0.13], inf));
  }

  //////// public functions:

  free {
    this.stopEndLoop;
    granulator.free;
    eq.free;
    reverb.free;
    delay.free;
    antenna.free;
    antennaBus.free;
    hammond.free;
    this.freeModule;
    isLoaded = false;
  }

  playNote { | freq = 220, vol = -6 |
    hammond.playNote(freq, vol);
  }
  releaseNote { | freq = 220 |
    hammond.releaseNote(freq);
  }

  playEndLoop { | clock |
    hammond.playSequence(\endLoop, clock);
    endLoopIsPlaying = true;
  }

  stopEndLoop {
    hammond.stopSequence(\endLoop);
    endLoopIsPlaying = false;
  }
}