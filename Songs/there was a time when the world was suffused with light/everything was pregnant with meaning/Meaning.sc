/*
Meaing.sc
Saturday, April 21st 2018
prm

everything was pregnant with meaning
*/

Meaning : IM_Module {

  var <isLoaded;
  var server;

  var <hiss, <main, <synth;
  var <reverb, <delay;

  var <mainMixer, <returns;

  *new { | outBus, modularBus, reverbBuffer, relGroup, addAction = 'addToHead' |
    ^super.new(2, outBus, relGroup: relGroup, addAction: addAction).prInit(modularBus, reverbBuffer);
  }

  prInit { | modularBus, reverbBuffer |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      /////// Send Returns:
      returns = IM_Mixer.new(2, mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { returns.isLoaded } != true }, { 0.001.wait; });

      reverb = Meaning_Reverb.new(returns.chanStereo(0), reverbBuffer, group, \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      delay = Meaning_Delay.new(returns.chanStereo(1), group, \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      //////// Main Mixer:
      mainMixer = IM_Mixer.new(3, mixer.chanStereo(1), reverb.inBus, delay.inBus, modularBus,
        relGroup: group, addAction: \addToHead);
      while({ try { mainMixer.isLoaded } != true }, { 0.001.wait; });

      hiss = Meaning_Hiss.new(mainMixer.chanStereo(0), group, \addToHead);
      while({ try { hiss.isLoaded } != true }, { 0.001.wait; });

      main = Meaning_Main.new(mainMixer.chanStereo(1), group, \addToHead);
      while({ try { main.isLoaded } != true }, { 0.001.wait; });

      synth = Meaning_Synth.new(mainMixer.chanStereo(2), group, \addToHead);
      while({ try { synth.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitializeParameters;

      server.sync;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    //////// Mixer:

    // hiss:
    mainMixer.setVol(0,0);
    mainMixer.setSendVol(0, 0, -20);
    mainMixer.setSendVol(0, 1, 0);

    // main:
    mainMixer.setVol(1, 0);
    mainMixer.setSendVol(1, 0, 0);
    mainMixer.setSendVol(1, 1, -12);

    // synth:
    mainMixer.setVol(2, -17);
    mainMixer.setSendVol(2, 0, -15);
    mainMixer.setSendVol(2, 1, -3);
  }

  //////// public functions:

  free {
    synth.free;
    main.free;
    hiss.free;

    reverb.free;
    delay.free;

    mainMixer.free;
    returns.free;

    this.freeModule;

    isLoaded = false;
  }

}