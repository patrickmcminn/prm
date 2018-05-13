/*
Saturday, April 22nd, 2017
Light.sc
prm
*/

Light : Song {

  var server, <isLoaded;

  var <main, <bass, <arps;
  var <chorale, <trumpet;

  *new { | mixAOutBus, mixBOutBus, mixCOutBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(mixAOutBus, 2, mixBOutBus, 1, mixCOutBus, 2, send0Bus, send1Bus, send2Bus, send3Bus, false,
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
      bass = Light_Bass.new(mixerB.chanStereo(0), "UMC404HD 192k", "UMC404HD 192k", group, \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });

      // Mixer C;
      chorale = Light_Chorale.new(mixerC.chanStereo(0), group, \addToHead);
      while({ try { chorale.isLoaded } != true }, { 0.001.wait;});
      trumpet = IM_HardwareIn.new(0, mixerC.chanMono(1), group, \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });


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

    mixerA.masterChan.setVol(-6);
    mixerB.masterChan.setVol(-6);
    mixerC.masterChan.setVol(-6);

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

    // chorale:
    mixerC.setPreVol(0, 6);
    mixerC.setVol(0, -15);
    mixerC.setSendVol(0, 0, 0);
    mixerC.setSendVol(0, 1, -12);

    // trumpet:
    mixerC.tglMute(1);
    mixerC.setPreVol(1, 6);
    mixerC.setSendVol(1, 0, 0);
    mixerC.setSendVol(1, 1, -5);
  }

  free {
    mixerA.masterChan.tglMute;
    mixerB.masterChan.tglMute;
    mixerC.masterChan.tglMute;
    trumpet.free;
    chorale.free;
    main.free;
    arps.free;
    bass.free;
    isLoaded = false;
  }

}