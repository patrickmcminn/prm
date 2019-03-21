/*
Sunday, April 22nd 2018
Sorrow.sc
prm

there is a sorrow in beginning to understand
*/

Sorrow : IM_Module {

  var <isLoaded;
  var server;

  var <trumpets, <trumpetInput, <moog;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(2, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      trumpets = Sorrow_Trumpets.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { trumpets.isLoaded } != true }, { 0.001.wait; });

      trumpetInput = IM_HardwareIn.new(1, trumpets.inBus, group, \addToHead);
      while({ try { trumpetInput.isLoaded } != true }, { 0.001.wait; });

      moog = IM_HardwareIn.new(3, mixer.chanMono(1), group, \addToHead);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitializeParameters;

      server.sync;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    // trumpets:
    //mixer.setPreVol(0, -3);
    mixer.setVol(0, -3);
    mixer.setSendVol(0, 0, -3);

    // moog:
    //mixer.setPreVol(1, -6);
    mixer.setVol(1, -6);
    mixer.setSendVol(1, 0, -9);
  }

  //////// public functions:

  free {
    moog.free;
    trumpets.free;
    this.freeModule;
  }

}