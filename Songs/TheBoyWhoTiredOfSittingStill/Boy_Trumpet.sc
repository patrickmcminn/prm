/*
Tuesday, July 25th 2017
Boy_Trumpet.sc
prm
*/

Boy_Trumpet : IM_Processor {
  var <isLoaded;
  var server;

  var <nebula, <looper, <splitter, <input;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      nebula = TrumpetNebula.newStereo(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { nebula.isLoaded } != true }, { 0.001.wait; });

      looper = Looper.newStereo(nebula.inBus, 60, 1, relGroup: group, addAction: \addToHead);
      while({ try { looper.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newMono(2, [looper.inBus, nebula.inBus], relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      input = IM_Mixer_1Ch.new(splitter.inBus, relGroup: group, addAction: \addToHead);
      while({ try { input.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      looper.mixer.setVol(-6);

      isLoaded = true;
    }
  }

  //////// public functions:

  inBus { ^input.chanStereo(0) }

  free {
    nebula.free;
    looper.free;
    splitter.free;
    input.free;
    this.freeModule;
    isLoaded = false;
  }


}