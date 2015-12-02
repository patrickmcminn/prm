/*
Tuesday, December 1st 2015
FalseSelf_Bells.scd
prm
*/

FalseSelf_Bells : IM_Module {

  var server, <isLoaded;
  var bells, <reverb, <delay;
  // var granulator; // let's try it with the granualtor as a send effect as first?

  *new {
    | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      //granulator = IM_Granulator.new();
      reverb = IM_Reverb.new(mixer.chanStereo(0), mix: 0.55, roomSize: 0.9, damp: 0.9,
        relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });
      delay = SimpleDelay.newStereo(reverb.inBus, 0.48, 0.5, 0.5, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });
      bells = MidBells.new(delay.inBus, relGroup: group, addAction: \addToHead);
      while({ try { bells.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      delay.setMix(0.25);

      isLoaded = true;
    }
  }

  //////// public functions:
  free {
    bells.free;
    delay.free;
    reverb.free;
    this.freeModule;
  }

  playNote { | freq = 220, vol = 0 |
    bells.playNote(freq, vol);
  }

}