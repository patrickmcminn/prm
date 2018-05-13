/*
Thursday, April 12th 2018
Darkness_Returns.sc
prm
A Shallow Eclipsing Darkness
*/

Darkness_Returns : IM_Module {

  var <isLoaded;
  var server;

  var <delay, <granulator;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(2, outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      delay = SimpleDelay.newStereo(mixer.chanStereo(0), 0.3, 0.7, 1, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      granulator = Darkness_Gran.new(mixer.chanStereo(1), group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      server.sync;
      this.prSetInitialParameters;

      isLoaded = true;
    }
  }

  prSetInitialParameters {
    delay.setMix(1);

    mixer.setSendVol(0, 0, -9);
    mixer.setSendVol(1, 0, -3);
    mixer.setSendVol(1, 1, -12);

    // granulator starts quiet:
    mixer.setVol(1, -inf);
  }

  //////// public functions:
  free {
    delay.free;
    granulator.free;
    this.freeModule;
    isLoaded = false;
  }

}