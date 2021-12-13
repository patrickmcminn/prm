/*
Tuesday, December 1st 2015
FalseSelf_PlaneNoise.sc
prm
*/

FalseSelf_PlaneNoise : IM_Module {

  var <isLoaded, server;
  var <samplePlayer;
  var <isPlaying;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToTail |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/FalseSelf/samples/plane noise.wav";
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      samplePlayer = SamplePlayer.newStereo(mixer.chanStereo(0), path, relGroup: group, addAction: \addToHead);
      while({ try { samplePlayer.isLoaded } != true }, { 0.001.wait; });

      samplePlayer.setFilterCutoff(9490);
      samplePlayer.setReleaseTime(15);
      mixer.setPreVol(12);
      isPlaying = false;
      isLoaded = true;
    };
  }

  //////// public functions:
  free {
    samplePlayer.free;
    this.freeModule;
    isLoaded = false;
  }

  playSample { samplePlayer.playSampleSustaining('planeNoise', 0, 1, 0, 1, 0); isPlaying = true; }
  releaseSample { samplePlayer.releaseSampleSustaining('planeNoise'); isPlaying = false;  }

  setFilterCutoff { | cutoff = 9490 | samplePlayer.setFilterCutoff(cutoff); }


}