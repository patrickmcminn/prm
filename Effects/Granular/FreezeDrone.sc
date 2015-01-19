/*
Thursday, September 11th 2014
FreezeDrone.sc
prm
*/

FreezeDrone : IM_Processor {

  var <isLoaded = false;
  var <highFreeze, <lowFreeze, <granularDelay, splitter, <distortion, distBus;
  var <attackTime, <releaseTime;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      attackTime = 5;
      releaseTime = 10;
      while({ try { mixer.isLoaded } != true}, { 0.01.wait; });

      this.prAddSynthDefs;
      server.sync;
      distBus = Bus.audio(server, 2);
      server.sync;
      //distortion = Synth(\prm_droneFreeze_Distortion, [\inBus, distBus, \outBus, mixer.chanStereo(0)], group, \addToHead);
      server.sync;
      //granularDelay = GranularDelay.new(distBus, group, \addToHead);
      granularDelay = GranularDelay.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { granularDelay.isLoaded } != true}, {0.001.wait; });

      highFreeze = IM_GrainFreeze.new(granularDelay.inBus, group, \addToHead);
      while({ try { highFreeze.isLoaded } != true}, { 0.001.wait; });
      lowFreeze = IM_GrainFreeze.new(granularDelay.inBus, group, \addToHead);
      while({ try { lowFreeze.isLoaded } !=true}, { 0.001.wait; });

      granularDelay.setDelayTime(1.5);
      highFreeze.setAttack(attackTime);
      highFreeze.setRelease(releaseTime);
      lowFreeze.setAttack(attackTime);
      lowFreeze.setRelease(releaseTime);

      splitter = Synth(\prm_droneFreeze_Splitter, [\inBus, inBus, \outBus1, highFreeze.inBus, \outBus2, lowFreeze.inBus],
        group, \addToHead);

      highFreeze.setLowPassCutoff(2000);
      highFreeze.setHighPassCutoff(25);
      lowFreeze.setLowPassCutoff(1500);
      lowFreeze.setHighPassCutoff(25);

      isLoaded = true;
    };
  }

  prAddSynthDefs {
    SynthDef(\prm_droneFreeze_Splitter, {
      | inBus = 0, outBus1 = 0, outBus2 = 0 |
      var sig = In.ar(inBus);
      sig = LPF.ar(sig, 2500);
      Out.ar(outBus1, sig);
      Out.ar(outBus2, sig);
    }).add;

    SynthDef(\prm_droneFreeze_Distortion, {
      | inBus = 0, outBus = 0, gainAmp = 1, postAmp = 1, postCutoff = 20000, lowGain = 6, highGain = 6, amp = 1 |
      var input, lowEq, highEq, distortion, filter, sig;
      input = In.ar(inBus, 2);
      lowEq = BLowShelf.ar(input, 300, 1, lowGain);
      highEq = BHiShelf.ar(lowEq, 1200, 1, highGain);
      distortion = (highEq * gainAmp).distort;
      distortion = distortion * postAmp;
      filter = LPF.ar(distortion, postCutoff);
      sig = filter * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:

  free {
    splitter.free;
    splitter = nil;
    lowFreeze.free;
    lowFreeze = nil;
    highFreeze.free;
    highFreeze = nil;
    granularDelay.free;
    granularDelay = nil;
    distortion.free;
    distortion = nil;
    distBus.free;
    distBus = nil;
    this.freeModule;
  }

  freeze { | highFreezeRate = 0.5, lowFreezeRate = 0.25 |
    highFreeze.freeze(highFreezeRate, 0.75);
    lowFreeze.freeze(lowFreezeRate, 1);
  }

  releaseFreeze {
    highFreeze.releaseFreeze;
    lowFreeze.releaseFreeze;
  }

  /*
  setPostDistortionCutoff { | cutoff = 20000 | distortion.set(\postCutoff, cutoff); }
  setDistortionGainAmp { | gainAmp = 1 | distortion.set(\gainAmp, gainAmp); }
  setDistortionPostAmp { | postAmp = 1 |  distortion.set(\postAmp, postAmp); }
  setDistortionLowGain { | gain = 6 | distortion.set(\lowGain, gain); }
  setDistortionHighGain { | gain = 6 | distortion.set(\highGain, gain); }
  */

}