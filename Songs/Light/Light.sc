/*
Saturday, April 22nd, 2017
Light.sc
prm
*/

Light : Song {

  var server, <isLoaded;

  var <main, <bass, <arps;

  *new { | mixAOutBus, mixBOutBus, mixCOutBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(mixAOutBus, 2, mixBOutBus, 1, mixCOutBus, 1, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixerC.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      // Mixer A:
      main = Light_Main.new(mixerA.chanStereo(0), group, \addToHead);
      while({ try { main.isLoaded } != true }, { 0.001.wait; });

      arps = Light_Arps.new(mixerA.chanStereo(1), group, \addToHead);
      while({ try { arps.isLoaded } != true }, { 0.001.wait; });

      // Mixer B:
      bass = Light_Bass.new(mixerB.chanStereo(0), "UMC1820", "UMC1820", group, \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialConditions;

      isLoaded = true;
    }
  }

  prSetInitialConditions {

    // send 1: Light Sends Reverb
    // send 2: Light Sends Delay
    // send 3: Modular
    // send 4: Light Sends multishift

    mixerA.masterChan.setVol(-12);
    mixerB.masterChan.setVol(-12);
    mixerC.masterChan.setVol(-12);

    // main:
    mixerA.setVol(0, -3);
    mixerA.setSendVol(0, 0, 0);
    mixerA.setSendVol(0, 1, 0);
   // mixerA.setSendVol(0, 2, 0);
    mixerA.setSendVol(0, 3, 0);
    main.setFilterCutoff(150);

    // arps:
    mixerA.setVol(1, -6);
    mixerA.setSendVol(1, 0, 0);
    mixerA.setSendVol(1, 1, -9);
    //arps.setFilterCutoff(150);

    // bass:
    mixerB.setVol(0, -inf);
    mixerB.setSendVol(0, 0, -12);
  }

  free {
    mixerA.masterChan.tglMute;
    mixerB.masterChan.tglMute;
    mixerC.masterChan.tglMute;
    main.free;
    arps.free;
    bass.free;
    isLoaded = false;
  }

}