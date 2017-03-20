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
    micIn = 1, modularIn = 2, moogIn = 3, moogDeviceName, moogPortName, mixAOutBus, mixBOutBus, mixCOutBus,
    send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead'
    |
    ^super.new(mixAOutBus, 2, mixBOutBus, 2, mixCOutBus, 1, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(micIn, modularIn, moogIn, moogDeviceName, moogPortName, send0Bus, send1Bus, send2Bus, send3Bus);
  }

  prInit { |micIn = 1, modularIn = 2, moogIn = 3, moogDeviceName, moogPortName, send0Bus, send1Bus, send2Bus, send3Bus |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixerC.isLoaded } != true }, { 0.001.wait; });
      clock = TempoClock.new(1);
      server.sync;

      delay = SimpleDelay.newStereo(mixerC.chanMono(0), 2, 0.6, 5, send0Bus, send1Bus, send2Bus, send3Bus,
        relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      modular = IM_Mixer_1Ch.new(mixerB.chanStereo(0), send0Bus, send1Bus, send2Bus, delay.inBus, false,
        group, 'addToHead');
      while({ try { modular.isLoaded } != true }, { 0.001.wait; });
      modularInput = IM_HardwareIn.new(modularIn, modular.chanMono, group, \addToHead);
      while({ try { modularInput.isLoaded} != true }, { 0.001.wait; });


      moog = Habit_Moog.new(moogIn, mixerB.chanStereo(1), send0Bus, send1Bus, send2Bus, delay.inBus,
        group, \addToHead, moogDeviceName, moogPortName);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });


      trumpet = Habit_LiveTrumpet.new(mixerA.chanStereo(0), send0Bus, send1Bus, send2Bus, delay.inBus,
        group, \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });
      trumpetInput = IM_HardwareIn.new(micIn, trumpet.inBus, group, \addToHead);
      while({ try { trumpetInput.isLoaded } != true }, { 0.001.wait; });

      trumpetLoopers = Habit_TrumpetLoopers.new(mixerA.chanStereo(1), send0Bus, send1Bus, send2Bus, delay.inBus,
        group, \addToHead);
      while({ try { trumpetLoopers.isLoaded } != true}, { 0.001.wait; });
      trumpetLoopersInput = IM_HardwareIn.new(micIn, trumpetLoopers.inBus, group, \addToHead);
      while({ try { trumpetLoopersInput.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      delay.mixer.setSendVol(0, -12);

      modular.setSendVol(0, -13);
      modular.setSendVol(3, -23.5);


      moog.mixer.setSendVol(0, -14.6);
      moog.mixer.setSendVol(3, -25.3);
      moog.mixer.setSendVol(2, 0);

      trumpet.mixer.mute;
      trumpet.mixer.setSendVol(0, -3);
      trumpet.mixer.setSendVol(3, 0);
      trumpet.mixer.setSendVol(2, -inf);

      //trumpetLoopers.mixer.mute;
      trumpetLoopers.mixer.setSendVol(0, -3);
      trumpetLoopers.mixer.setSendVol(3, 0);
      trumpetLoopers.mixer.setSendVol(2, -inf);

      modular.setVol(-6);
      moog.mixer.setVol(-6);
      trumpet.mixer.setVol(-6);
      trumpetLoopers.mixer.setMasterVol(-9);

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
    var bps = tempo/60
    clock.tempo = bps;
  }


}