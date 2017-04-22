/*
Saturday, April 22nd, 2017
Light_MultiShift.sc
prm
*/

Light_MultiShift : IM_Processor {

  var server, <isLoaded;
  var <multiShift, <lfo;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, relGroup: nil, addAction: \addToHead).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      lfo = LFO.new(10, 'noise', 0, 1, group, 'addToHead');
      while({ try { lfo.isLoaded } != true }, { 0.001.wait; });

      mixer.mapAmp(lfo.outBus);

      multiShift = IM_MultiShift.newStereo(mixer.chanStereo, [0.5, 3/2, 2, 2.5, 3, 3.5, 4], 0, group, 'addToHead');
      while({ try { multiShift.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    multiShift.free;
    lfo.free;
    this.freeProcessor;
    isLoaded = false;
  }

  inBus { ^multiShift.inBus }

}