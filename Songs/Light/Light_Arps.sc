/*
Saturday, April 22nd 2017
Light_Arps.sc
prm
*/

Light_Arps : IM_Module {

  var server, <isLoaded;
  var <sampler;

  *new { | outBus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    var path, sampleArray;
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/Light/Samples/Arp/";
      sampleArray = (path ++ "*").pathMatch;

      sampler = Sampler.newStereo(mixer.chanStereo(0), sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    sampler.free;
    this.freeModule;
    isLoaded = false;
  }

  playArps {
    sampler.playSampleOneShot(0, 0);
    sampler.playSampleOneShot(1, 0);
  }

  setFilterCutoff { | cutoff = 150 |
    sampler.setFilterCutoff(cutoff);
  }

}