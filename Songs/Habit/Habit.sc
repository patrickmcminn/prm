/*
Sunday, March 19th 2017
Habit.sc
prm
Baltimore, MD
1:23 am on a Saturday night, listening to Oren Ambarchi
*/

Habit : Song {

  var <isLoaded;
  var server;
  var <moog, <modular, <modularInput, <trumpetInput, <trumpet, <trumpetLoopers, <trumpetLoopersInput, <delay;
  var <clock;

  *new {
    |
    outBus, micIn = 1, modularIn = 2, moogIn = 3, moogDeviceName, moogPortName, mixAOutBus, mixBOutBus, mixCOutBus,
    send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead'
    |
    ^super.new(5, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(outBus, micIn, modularIn, moogIn, moogDeviceName, moogPortName, send0Bus, send1Bus, send2Bus, send3Bus);
  }

  prInit {
    | outBus, micIn = 1, modularIn = 2, moogIn = 3, moogDeviceName, moogPortName, send0Bus, send1Bus, send2Bus, send3Bus |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      clock = TempoClock.new(1);
      server.sync;

      // delay:
      delay = SimpleDelay.newStereo(outBus, 2, 0.6, 5, send0Bus, send1Bus, send2Bus, send3Bus,
        relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      // trumpet:
      trumpet = Habit_LiveTrumpet.new(mixer.chanStereo(0), send0Bus, send1Bus, send2Bus, delay.inBus,
        group, \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });
      trumpetInput = IM_HardwareIn.new(micIn, trumpet.inBus, group, \addToHead);
      while({ try { trumpetInput.isLoaded } != true }, { 0.001.wait; });

      // moog:
      moog = Habit_Moog.new(moogIn, mixer.chanStereo(1), send0Bus, send1Bus, send2Bus, delay.inBus,
        group, \addToHead, moogDeviceName, moogPortName);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });

      // modular:
      modular = IM_Mixer_1Ch.new(mixer.chanStereo(2), send0Bus, send1Bus, send2Bus, delay.inBus, false,
        group, 'addToHead');
      while({ try { modular.isLoaded } != true }, { 0.001.wait; });
      modularInput = IM_HardwareIn.new(modularIn, modular.chanMono, group, \addToHead);
      while({ try { modularInput.isLoaded} != true }, { 0.001.wait; });


      // trumpet loopers:
      trumpetLoopers = Habit_TrumpetLoopers.new(mixer.chanStereo(3), send0Bus, send1Bus, send2Bus, delay.inBus,
        group, \addToHead);
      while({ try { trumpetLoopers.isLoaded } != true}, { 0.001.wait; });
      trumpetLoopersInput = IM_HardwareIn.new(micIn, trumpetLoopers.inBus, group, \addToHead);
      while({ try { trumpetLoopersInput.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      delay.mixer.setSendVol(0, -12);
      delay.setMix(1);

      // modular:
      mixer.setSendVol(2, 0, -13);
      mixer.setSendVol(2, 3, -23.5);
      mixer.setVol(2, -6);

      // moog:
      mixer.setSendVol(1, 0, -14.6);
      mixer.mixer.setSendVol(1, 3, -25.3);
      mixer.mixer.setSendVol(1, 2, 0);
      mixer.setVol(1, -6);

      // trumpet
      mixer.mute(0);
      mixer.setSendVol(0, 0, -3);
      mixer.setSendVol(0, 3, 0);
      mixer.setSendVol(0, 2, -inf);
      mixer.setVol(0, -6);

      // trumpet loopers:
      mixer.setSendVol(3, 0, -3);
      mixer.setSendVol(3, 3, 0);
      mixer.setSendVol(3, 2, -inf);
      mixer.setVol(3, -9);


      server.sync;

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    trumpetInput.free;
    trumpetLoopersInput.free;
    trumpet.free;
    trumpetInput.free;
    modular.free;
    modularInput.free;
    moog.free;
    delay.free;
    clock.free;
    this.freeSong;
  }

  setClockTempo { | tempo = 60 |
    var bps = tempo/60;
    clock.tempo = bps;
  }


}