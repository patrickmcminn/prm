/*
Friday, March 17th 2017
Habit_TrumpetLoopers.sc
prm
*/

Habit_TrumpetLoopers :IM_Processor {

  var <isLoaded;
  var server;
  var <looper1, <looper2, <looper3, <looper4;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 4, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;

      looper1 = Looper.newMono(mixer.chanStereo(0), 15, 1, relGroup: group, addAction: 'addToHead');
      while({ try { looper1.isLoaded } != true }, { 0.001.wait; });

      looper2 = Looper.newMono(mixer.chanStereo(1), 15, 1, relGroup: group, addAction: 'addToHead');
      while({ try { looper2.isLoaded } != true }, { 0.001.wait; });

      looper3 = Looper.newMono(mixer.chanStereo(2), 15, 1, relGroup: group, addAction: 'addToHead');
      while({ try { looper3.isLoaded } != true }, { 0.001.wait; });

      looper4 = Looper.newMono(mixer.chanStereo(3), 15, 1, relGroup: group, addAction: 'addToHead');
      while({ try { looper4.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      isLoaded = true;

    }
  }




}