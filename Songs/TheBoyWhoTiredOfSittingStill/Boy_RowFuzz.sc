/*
Tuesday, July 25th 2017
Boy_RowFuzz.sc
prm
*/

Boy_RowFuzz : IM_Module {

  var <isLoaded;
  var server;

  var <hammond;
  var <eq, <reverb, <distortion, <nebula, <delay;

  var delayBus, nebulaBus;

  var <mainRowFuzzIsPlaying, <lowRowFuzzIsPlaying;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      delayBus = Bus.audio(server, 2);
      nebulaBus = Bus.audio(server, 2);

      this.prAddSynthDefs;

      server.sync;

      delay = Synth(\prm_pingPongDelayStereo, [\inBus, delayBus, \outBus, mixer.chanStereo(0),
        \delay, 0.915, \decay, 6, \centerFreq, 1000, \bw, 12, \mix, 0.49], group, \addToHead);
      while({ try { delay == nil }}, { 0.001.wait; });

      nebula = Synth(\prm_nebula8, [\inBus, nebulaBus, \outBus, delayBus,
        \depth, 50, \activity, 40], group, \addToHead);
      while({ try { nebula == nil }}, { 0.001.wait; });

      distortion = Distortion.newStereo(nebulaBus, 5.5, relGroup: group, addAction: \addToHead);
      while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

      reverb = IM_Reverb.new(distortion.inBus, mix: 0.86, roomSize: 0.8, damp: 0,
        relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(reverb.inBus, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      hammond = Hammond.new(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { hammond.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

      hammond.makeSequence(\rowFuzzMain);
      hammond.makeSequence(\rowFuzzLow);

      server.sync;

      this.prMakePatternParameters;

      isLoaded = true;
    }
  }

  prAddSynthDefs {
    SynthDef(\prm_nebula8, {
      | inBus = 0, outBus = 0 trig, depth = 25, amp = 1, activity = 50 |
      var input, range, offset, rate, trigger, nebulaLeft, nebulaRight, left, right, sig;

      input = In.ar(inBus);
      range = depth * 0.01;
      offset = 0.945 - range + (range/5);
      rate = ((activity/100) -1*(-500)+11)/1000;
      trigger = Impulse.kr(1/rate);

      nebulaLeft = Mix.fill(4, {
        (TRand.kr(0, range, trigger) + offset).linexp(0, 1.2, 0.0001, 1.2)
      });
      nebulaLeft = nebulaLeft/4;

      nebulaRight = Mix.fill(4, {
        (TRand.kr(0, range, trigger) + offset).linexp(0, 1.2, 0.0001, 1.2)
      });
      nebulaRight = nebulaRight/4;


      left = input * Lag2.kr(nebulaLeft, 0.7);
      right = input * Lag2.kr(nebulaRight, 0.7);

      sig = [left, right];
      sig = sig * amp;
      sig = Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_pingPongDelayStereo, {
      | inBus, outBus, delay = 0.7, decay = 6, centerFreq = 660, bw = 4, mix = 0.5, amp = 1 |

      var input, initDelay, leftDelay, rightDelay, sum, filter, dry, sig;

      input = In.ar(inBus, 2);
      initDelay = DelayN.ar(input, 5, delay);
      leftDelay = CombC.ar(initDelay, 5, delay * 2, decay);

      rightDelay = CombC.ar(input, 5, delay * 2, decay);

      sum = [initDelay + leftDelay, rightDelay];

      filter = BBandPass.ar(sum, centerFreq, bw);

      dry = input * (1-mix);
      sig = filter * mix;
      sig = sig + dry;
      sig = sig * amp;
      sig = Out.ar(outBus, sig);
    }).add;
  }

  prSetInitialParameters {
    mainRowFuzzIsPlaying = false;
    lowRowFuzzIsPlaying = false;

    distortion.postEQ.setLowPassCutoff(4500);
    distortion.postEQ.setHighPassCutoff(150);

    eq.setHighFreq(2500);
    eq.setHighGain(15);
    eq.setLowPassCutoff(4500);
    eq.setHighPassCutoff(100);
  }

  prMakePatternParameters {

    hammond.addKey(\rowFuzzMain, \root, -5);
    hammond.addKey(\rowFuzzMain, \octave, 6);
    hammond.addKey(\rowFuzzMain, \amp, 0.5);
    hammond.addKey(\rowFuzzMain, \subAmp, 0.7);
    hammond.addKey(\rowFuzzMain, \bassAmp, 0.5);
    hammond.addKey(\rowFuzzMain, \partial2Amp, 0.1);
    hammond.addKey(\rowFuzzMain, \partial3Amp, 0.05);
    hammond.addKey(\rowFuzzMain, \partial4Amp, 0.05);
    hammond.addKey(\rowFuzzMain, \partial5Amp, 0.05);
    hammond.addKey(\rowFuzzMain, \partial6Amp, 0.01);
    hammond.addKey(\rowFuzzMain, \partial8Amp, 0);
    hammond.addKey(\rowFuzzMain, \note, Pseq([[-1, 0, 4, 6, 7, 9, 12]], inf));
    hammond.addKey(\rowFuzzMain, \dur, 8);
    hammond.addKey(\rowFuzzMain, \cutoff, 15000);
    hammond.addKey(\rowFuzzMain, \dist, 0.3);
    hammond.addKey(\rowFuzzMain, \noise, 0.1);
    hammond.addKey(\rowFuzzMain, \attackTime, 0.01);
    hammond.addKey(\rowFuzzMain, \releaseTime, 0.01);
    hammond.addKey(\rowFuzzMain, \legato, 1);

    hammond.addKey(\rowFuzzLow, \root, -5);
    hammond.addKey(\rowFuzzLow, \octave, 4);
    hammond.addKey(\rowFuzzLow, \subAmp, 0);
    hammond.addKey(\rowFuzzLow, \bassAmp, 0);
    hammond.addKey(\rowFuzzLow, \partial2Amp, 0.1);
    hammond.addKey(\rowFuzzLow, \partial3Amp, 0.1);
    hammond.addKey(\rowFuzzLow, \partial4Amp, 0.05);
    hammond.addKey(\rowFuzzLow, \partial5Amp, 0.05);
    hammond.addKey(\rowFuzzLow, \partial6Amp, 0);
    hammond.addKey(\rowFuzzLow, \partial8Amp, 0);
    hammond.addKey(\rowFuzzLow, \amp, 0.07);
    hammond.addKey(\rowFuzzLow, \note, Pseq([[-1, 0, 4, 6, 7, 9, 12]], inf));
    hammond.addKey(\rowFuzzLow, \dur, 8);
    hammond.addKey(\rowFuzzLow, \cutoff, 15000);
    hammond.addKey(\rowFuzzLow, \dist, 1);
    hammond.addKey(\rowFuzzLow, \noise, 0.03);
    hammond.addKey(\rowFuzzLow, \attackTime, 0.01);
    hammond.addKey(\rowFuzzLow, \releaseTime, 0.01);
    hammond.addKey(\rowFuzzLow, \legato, 1);
  }

}