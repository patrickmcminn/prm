/*
Saturday, April 22nd, 2017
Light.sc
prm
*/

Light : Song {

  var server, <isLoaded;

  var <main, <bass, <arps;
  var <chorale, <trumpet;

  *new { | outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(5, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      server.sync;


      main = Light_Main.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { main.isLoaded } != true }, { 0.001.wait; });

      arps = Light_Arps.new(mixer.chanStereo(1), group, \addToHead);
      while({ try { arps.isLoaded } != true }, { 0.001.wait; });

      // Mixer B:
      bass = Light_Bass.new(mixer.chanStereo(2), "UMC404HD 192k", "UMC404HD 192k", group, \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });

      // Mixer C;
      chorale = Light_Chorale.new(mixer.chanStereo(3), group, \addToHead);
      while({ try { chorale.isLoaded } != true }, { 0.001.wait;});
      trumpet = IM_HardwareIn.new(0, mixer.chanMono(4), group, \addToHead);
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

    mixer.masterChan.setVol(-6);

    // main:
    mixer.setVol(0, -3);
    mixer.setSendVol(0, 0, 0);
    mixer.setSendVol(0, 1, 0);
   // mixerA.setSendVol(0, 2, 0);
    mixer.setSendVol(0, 3, 0);
    main.setFilterCutoff(150);

    // arps:
    mixer.setVol(1, -6);
    mixer.setSendVol(1, 0, 0);
    mixer.setSendVol(1, 1, -9);
    //arps.setFilterCutoff(150);

    // bass:
    mixer.setVol(2, -inf);
    mixer.setSendVol(2, 0, -12);

    // chorale:
    mixer.setPreVol(3, 6);
    mixer.setVol(3, -15);
    mixer.setSendVol(3, 0, 0);
    mixer.setSendVol(3, 1, -12);

    // trumpet:
    mixer.tglMute(4);
    mixer.setPreVol(4, 6);
    mixer.setSendVol(4, 0, 0);
    mixer.setSendVol(4, 1, -5);
  }

  free {
    mixer.masterChan.mute;
    trumpet.free;
    chorale.free;
    main.free;
    arps.free;
    bass.free;
    isLoaded = false;
  }

}