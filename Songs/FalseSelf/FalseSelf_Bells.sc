/*
Tuesday, December 1st 2015
FalseSelf_Bells.scd
prm
*/

FalseSelf_Bells : IM_Module {

  var server, <isLoaded;
  var bells, <reverb, <delay;
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
      granulator = GranularDelay.new(mixer.chanStereo(0), group, 'addToHead');
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      reverb = IM_Reverb.new(granulator.inBus, mix: 0.55, roomSize: 0.8, damp: 0.3,
        relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      delay = SimpleDelay.newStereo(reverb.inBus, 0.1875, 0.5, 0.5, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      bells = MidBells.new(delay.inBus, relGroup: group, addAction: \addToHead);
      while({ try { bells.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      // initial parameters:
      delay.setMix(0.5);

      granulator.setGranulatorCrossfade(-0.1);
      granulator.setGrainDur(0.05, 0.2);
      granulator.setTrigRate(20);

      mixer.setPreVol(-3);

      isLoaded = true;
    }
  }

  //////// public functions:
  free {
    bells.free;
    delay.free;
    reverb.free;
    granulator.free;
    this.freeModule;
  }

  playNote { | freq = 220, vol = 0 |
    bells.playNote(freq, vol);
  }

}