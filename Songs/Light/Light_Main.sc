/*
Saturday, April 22nd 2017
Light_Main.sc
prm
*/

Light_Main : IM_Module {

  var server, <isLoaded;
  var <sampler;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: \addToHead).prInit;
  }

  prInit {
    var path, sampleArray;
    server = Server.default;
    server.waitForBoot{
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/Light/Samples/Loops/";
      sampleArray = (path ++ "*").pathMatch;

      sampler = Sampler.newStereo(mixer.chanStereo(0), sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      isLoaded = true;
    }
  }

  ///////// public functions:

  free {
    sampler.free;
    this.freeModule;
    isLoaded = false;
  }

  playLoop1 { | vol = 0 | sampler.playSampleSustaining(\loop1, 0, vol); }
  releaseLoop1 { sampler.releaseSampleSustaining(\loop1); }

  playLoop2 { | vol = 0 | sampler.playSampleSustaining(\loop2, 1, vol); }
  releaseLoop2 { sampler.releaseSampleSustaining(\loop2); }

  playLoop3 { | vol = 0 | sampler.playSampleSustaining(\loop3, 2, vol); }
  releaseLoop3 { sampler.releaseSampleSustaining(\loop3); }

  playLoop4 { | vol = 0 | sampler.playSampleSustaining(\loop4, 3, vol); }
  releaseLoop4 { sampler.releaseSampleSustaining(\loop4); }

  setFilterCutoff { | cutoff = 150 | sampler.setFilterCutoff(cutoff); }

}