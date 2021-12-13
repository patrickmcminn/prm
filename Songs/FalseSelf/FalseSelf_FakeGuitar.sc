/*
Wednesday, March 15th 2017
FalseSelf_FakeGuitar.sc
prm
*/

FalseSelf_FakeGuitar : IM_Module {

  var server, <isLoaded;
  var <section1, <section2;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(2, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    var path1, path2;
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      path1 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/FalseSelf/samples/Fake Guitar/Fake Guitar section 1.wav";
      path2 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/FalseSelf/samples/Fake Guitar/Fake Guitar section 2.wav";

      server.sync;

      section1 = SamplePlayer.newStereo(mixer.chanStereo(0), path1, relGroup: group, addAction: \addToHead);
      while({ try { section1.isLoaded } != true }, { 0.001.wait; });

      section2 = SamplePlayer.newStereo(mixer.chanStereo(1), path2, relGroup: group, addAction: \addToHead);
      while({ try { section2.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      section2.setFilterCutoff(40);

      isLoaded = true;
    }
  }

  free {
    section1.free;
    section2.free;
    this.freeModule;
    isLoaded = false;
  }

  playSection1 { section1.playSampleSustaining('gtr', -3); }
  releaseSection1 { section1.releaseSampleSustaining('gtr'); }
  playSection2 { section2.playSampleSustaining('gtr', -3); }
  releaseSection2 { section2.releaseSampleSustaining('gtr'); }

  panic { section1

  }

}