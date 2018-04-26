/*
Sorrow_Trumpets.sc
Sunday, April 22nd 2018
prm

there is a sorrow in beginning to understand
*/

Sorrow_Trumpets : IM_Module {

  var server;
  var <isLoaded;

  var splitter, <trumpet2, <trumpet3;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(3, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      trumpet2 = GrainFreeze2.newMono(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { trumpet2.isLoaded } != true }, { 0.001.wait; });

      trumpet3 = GrainFreeze2.newMono(mixer.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { trumpet3.isLoaded } != true }, { 0.001.wait; });

      // trumpet 1 goes right to the mixer.
      splitter = Splitter.newMono(3, [mixer.chanMono(0), trumpet2.inBus, trumpet3.inBus],
        relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitializeParameters;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    trumpet2.setAttackTime(0.3);
    trumpet2.setReleaseTime(2.5);
    trumpet2.setLowPassCutoff(12000);

    trumpet3.setAttackTime(0.3);
    trumpet3.setReleaseTime(2.5);
    trumpet3.setLowPassCutoff(12000);
  }

  //////// public functions:

  inBus { ^splitter.inBus }

  free {
    trumpet3.free;
    trumpet2.free;
    splitter.free;
  }

}