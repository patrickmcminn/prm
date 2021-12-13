/*
Tuesday, December 1st 2015
FalseSelf_Bells.scd
prm
*/

FalseSelf_Bells : IM_Module {

  var server, <isLoaded;
  var bells, <delay;
  var <granulator;

  *new {
    | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    var path, sampleArray;

    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      granulator = GranularDelay2.new(mixer.chanStereo(0), relGroup: group, addAction: 'addToHead');
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      delay = SimpleDelay.newStereo(granulator.inBus, 0.1875, 0.5, 0.5, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      bells = MidBells.new(delay.inBus, relGroup: group, addAction: \addToHead);
      while({ try { bells.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      // initial parameters:
      delay.setMix(0.5);

      bells.mixer.setPreVol(-9);

      granulator.setMix(0.45);
      granulator.setGrainDur(0.05, 0.2);
      granulator.setTrigRate(20);

      mixer.setPreVol(-12);

      isLoaded = true;
    }
  }

  //////// public functions:
  free {
    bells.free;
    delay.free;
    granulator.free;
    this.freeModule;
    isLoaded = false;
  }

  playNote { | freq = 220, vol = 0 |
    bells.playNote(freq, vol);
  }

}