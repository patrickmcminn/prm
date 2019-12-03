/*
5/20/2014
Droner.sc
prm

picked up again 7/6/2015

updated 6/5/2019
-- erosion sounds terrible -- replacing with Distortion
*/

Droner : IM_Processor {

  var <isLoaded, server;
  var demand, <input, <delay, <granulator, erosion, <reverb, <eq;
  var <distortion;
  var <inBus;
  var demandBus, erosionBus;

  *newMono { | outBus = 0, ir, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono(ir);
  }

  prInitMono { | ir |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      demandBus = Bus.control;
      erosionBus = Bus.audio(server, 2);
      server.sync;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });


      /*
      reverb = IM_Reverb.new(eq.inBus, mix: 0.75, roomSize: 1, damp: 0.85, relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });
      */

      reverb = IM_Reverb.newConvolution(eq.inBus, bufName: ir, relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(reverb.inBus, group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });
      /*
      erosion = Synth(\prm_Droner_Erosion, [\inBus, erosionBus, \outBus, granulator.inBus, \freq, 500, \rangeLow, 0.002,
      \rangeHigh, 0.009, \mix, 0.02], group, \addToHead);
      server.sync;
      */

      distortion = Distortion.newStereo(granulator.inBus, 1, relGroup: group, addAction: \addToHead);
      while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

      delay = SimpleDelay.newStereo(distortion.inBus, 1.305, 0.99, 3, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      input = IM_Mixer_1Ch.new(delay.inBus, relGroup: group, addAction: \addToHead);
      while({ try { input.isLoaded } != true }, { 0.001.wait; });

      inBus = input.chanMono(0);

      demand = Synth(\prm_Droner_DemandRand7, [\outBus, demandBus, \freq, 12, \d1, 0.5, \d2, 0.5, \d3, 0.5, \d4, 0.5,
        \d5, 0.5, \d6, 0.25, \d7, 0.25], group, \addToHead);
      server.sync;

      granulator.mapGranulatorParameter(\rateLow, demandBus);
      granulator.mapGranulatorParameter(\rateHigh, demandBus);
      granulator.setGrainDur(0.1, 0.33);
      granulator.setTrigRate(17);
      granulator.setPan(-0.03, 0.03);
      granulator.setGranulatorCrossfade(1);
      granulator.setDelayMix(0);
      granulator.mixer.setVol(3);

      reverb.setMix(0.75);
      reverb.mixer.setPreVol(-3);
      reverb.postEQ.setHighGain(6);
      reverb.preEQ.setPeak1Freq(300);
      reverb.postEQ.setPeak2Freq(650);
      reverb.postEQ.setPeak2Gain(-6);
      reverb.preEQ.setPeak1Gain(-3);
      reverb.postEQ.setHighPassCutoff(20);

      reverb.setMix(0.75);

      eq.setLowFreq(120);
      eq.setLowGain(0);
      eq.setHighFreq(2637);
      eq.setHighGain(-3);

      mixer.setPreVol(3);
      mixer.setVol(0);

      isLoaded = true;
    };
  }

  prAddSynthDefs {

    SynthDef(\prm_Droner_DemandRand7, {
      | outBus, freq = 1, d1 = 1, d2 = 2, d3 = 3, d4 = 4, d5 = 5, d6 = 6, d7 = 7 |
      var rand, trig, demand, sig;
      rand = Drand([d1, d2, d3, d4, d5, d6, d7], inf);
      trig = Dust.kr(freq);
      demand = Demand.kr(trig, 0, rand);
      sig = Out.kr(outBus, demand);
    }).add;

    SynthDef(\prm_Droner_Erosion, {
      | inBus, outBus, freq = 100, rangeLow = 0.002, rangeHigh = 0.1, decayTime = 1.5, amp = 1, mix = 1 |
      var input, dry, noise, delay, sig;
      input = In.ar(inBus);
      dry = input * (1-mix);
      noise = LFNoise1.ar(freq).range(rangeLow, rangeHigh);
      delay = CombN.ar(input, 1, noise, decayTime);
      delay = delay*mix;
      sig = (dry + delay) * amp;
      sig = Out.ar(outBus, sig);
    }).add;

  }

  //////// public functions:

  free {
    demand.free;
    input.free;
    delay.free;
    //erosion.free;
    distortion.free;
    granulator.free;
    reverb.free;
    eq.free;
    demandBus.free;
    erosionBus.free;
    this.freeModule;

    demand = nil;
    input = nil;
    delay = nil;
    erosion = nil;
    granulator = nil;
    reverb = nil;
    eq = nil;
    demandBus = nil;
    erosionBus = nil;

    isLoaded = false;
  }

  setErosionMix { | mix = 0.02 | erosion.set(\mix, mix); }
  setErosionFreq { | freq = 500 | erosion.set(\freq, freq); }
  setErosionRangeLow { | rangeLow = 0.002 | erosion.set(\rangeLow, rangeLow); }
  setErosionRangeHigh { | rangeHigh = 0.009 | erosion.set(\rangeHigh, rangeHigh); }

  setGrainRates { | array |
    demand.set(\d1, array[0], \d2, array[1], \d3, array[2], \d4, array[3],
      \d5, array[4], \d6, array[5], \d7, array[6]);
  }

}