/*
Friday, April 14th 2017
FalseSelf_Orchestra.sc
prm
*/

FalseSelf_Orchestra : IM_Module {

  var server, <isLoaded;
  var <sampler;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path;

      isLoaded = false;
      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/FalseSelf/samples/mahler/Mahler Phrase.wav";

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      sampler = SamplePlayer.newStereo(mixer.chanStereo, path, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    sampler.free;
    this.freeModule;
  }

  playMahlerPhrase { sampler.playSampleOneShot(0, 1, 0, 1);}

}