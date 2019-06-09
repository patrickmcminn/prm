/*
Saturday, June 8th 2019
WhereTheBirds_Mic.sc
prm
*/

WhereTheBirds_Mic : IM_Module {

  var <isLoaded, server;
  var <input, <delay, <eq;

  *new { | outBus = 0, micIn, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(micIn);
  }

  prInit { | micIn |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      delay = SimpleDelay.newMono(eq.inBus, 2, 0.3, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      input = IM_HardwareIn.new(micIn, delay.inBus, group, \addToHead);
      while({ try { input.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      delay.setMix(0.15);
      eq.setHighPassCutoff(200);
      eq.setHighGain(3);
      eq.setLowPassCutoff(15000);

      isLoaded = true;
    }
  }

  free {
    input.free;
    delay.free;
    this.freeModule;
  }

}