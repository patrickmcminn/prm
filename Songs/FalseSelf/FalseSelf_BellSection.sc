/*
Tuesday, December 1st 2015
FalseSelf_BellSection.sc
prm
*/

FalseSelf_BellSection : IM_Module {

  var <isLoaded, server;
  var <synth, <bells;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead |
    ^super.new(2, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      synth = GlockSynth.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { synth.isLoaded } != true }, { 0.001.wait; });
      bells = FalseSelf_Bells.new(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { bells.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    }
  }

  //////// public functions:
  free {
    bells.free;
    synth.free;
    this.freeModule;
  }

  playNote { | freq = 220, synthVol = -3, bellsVol = 0 |
    {
      synth.playNote(freq, synthVol);
      bells.playNote(freq, bellsVol);
      0.05.wait;
      synth.releaseNote(freq);
    }.fork;
  }
}
