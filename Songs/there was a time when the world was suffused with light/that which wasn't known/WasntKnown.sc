/*
Wednesday, January 10th 2018
WasntKnown.sc
prm

main song file for "That Which Wasn't Known"
*/

WasntKnown : Song {

  var server, <isLoaded;

  var <sampler;

  *new {
     | mixAOutBus, mixBOutBus, mixCOutBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(mixAOutBus, 1, mixBOutBus, 1, mixCOutBus, 1, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixerC.isLoaded } != true }, { 0.001.wait; });

      sampler = WasntKnown_Sampler.new(mixerA.chanStereo(0), group, \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      mixerA.setSendVol(0, 0, -6);
      mixerA.setVol(0, -6);

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    sampler.free;
    this.freeSong;
  }
}