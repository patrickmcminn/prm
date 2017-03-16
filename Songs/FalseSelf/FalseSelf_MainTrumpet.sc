/*
Wednesday, March 15th 2017
FalseSelf_MainTrumpet.sc
prm
*/

FalseSelf_MainTrumpet : IM_Processor {

  var <isLoaded;
  var server;
  var <distortion, <delay, <eq, <reverb;

  *new { |outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      reverb = IM_Reverb.new(mixer.chanStereo(0), mix: 0.5, roomSize: 0.8, damp: 0.45,
        relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(reverb.inBus, relGroup: group, addAction: \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      delay = SimpleDelay.newStereo(eq.inBus, 0.375, 0.22, 5, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      distortion = Distortion.newMono(delay.inBus, 100, relGroup: group, addAction: \addToHead);
      while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

      //input = IM_HardwareIn.new(inBus, distortion.inBus, relGroup: group, addAction: \addToHead);

      server.sync;

      this.prInitializeReverb;
      this.prInitializeEQ;
      this.prInitializeDelay;
      this.prInitializeDistortion;

      server.sync;

      isLoaded = true;
    }

  }

  inBus { ^distortion.inBus }

  prInitializeReverb {
    reverb.setLowPassFreq(1040);
  }

  prInitializeEQ {
    eq.setLowPassCutoff(3880);
    eq.setHighPassCutoff(173);
    eq.setPeak1Freq(1000);
    eq.setPeak1Gain(3.1);
  }

  prInitializeDelay {
    delay.setMix(0.45);
  }

  prInitializeDistortion {
    distortion.preEQ.setHighPassCutoff(200);
    distortion.postEQ.setHighPassCutoff(200);
    distortion.postEQ.setLowPassCutoff(4000);
  }

  //////// public functions:

  free {
    distortion.free;
    delay.free;
    eq.free;
    reverb.free;
    this.freeProcessor;
  }

}