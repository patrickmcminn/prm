/*
Friday, March 17th 2017
Habit_LiveTrumpet.sc
prm
*/

Habit_LiveTrumpet : IM_Processor {

  var <isLoaded;
  var server;
  var <eq;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      eq = Equalizer.newMono(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      eq.setHighPassCutoff(200);
      eq.setLowPassCutoff(3500);

      server.sync;

      isLoaded = true;
    }
  }

  inBus { ^eq.inBus }

  //////// public functions:

  free {
    eq.free;
    this.freeProcessor;
  }


}