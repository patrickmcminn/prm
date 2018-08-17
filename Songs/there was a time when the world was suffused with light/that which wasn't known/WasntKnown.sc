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
     | outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      sampler = WasntKnown_Sampler.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      mixer.setSendVol(0, 0, -6);
      mixer.setPreVol(0, 0, -9);
      mixer.setVol(0, -6);

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    sampler.free;
    this.freeSong;
  }
}