/*
Friday, March 17th 2017
Habit_TrumpetLoopers.sc
prm
*/

Habit_TrumpetLoopers :IM_Processor {

  var <isLoaded;
  var server;
  var <submixer;
  var <looper1, <looper2, <looper3, <looper4;
  var splitter;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;

      submixer = IM_Mixer.new(4, mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { submixer.isLoaded } != true }, { 0.001.wait; });

      looper1 = Looper.newMono(submixer.chanStereo(0), 15, 1, relGroup: group, addAction: 'addToHead');
      while({ try { looper1.isLoaded } != true }, { 0.001.wait; });

      looper2 = Looper.newMono(submixer.chanStereo(1), 15, 1, relGroup: group, addAction: 'addToHead');
      while({ try { looper2.isLoaded } != true }, { 0.001.wait; });

      looper3 = Looper.newMono(submixer.chanStereo(2), 15, 1, relGroup: group, addAction: 'addToHead');
      while({ try { looper3.isLoaded } != true }, { 0.001.wait; });

      looper4 = Looper.newMono(submixer.chanStereo(3), 15, 1, relGroup: group, addAction: 'addToHead');
      while({ try { looper4.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      splitter = Splitter.newMono(4,
        [looper1.inBus, looper2.inBus, looper3.inBus, looper4.inBus],
        relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;

    }
  }

  inBus { ^splitter.inBus }

  free {
    splitter.free;
    looper1.free;
    looper2.free;
    looper3.free;
    looper4.free;
    splitter.free;
    this.freeProcessor;
    isLoaded = false;
  }
}