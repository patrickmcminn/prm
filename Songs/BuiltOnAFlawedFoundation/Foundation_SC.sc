/*
Monday, June 19th 2017
Foundation_SC.sc
prm
*/

Foundation_SC : IM_Module {

  var server, <isLoaded;
  var <sampler;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/BuiltOnAFlawedFoundation/samples/SC/";
      var sampleArray = (path ++ "*").pathMatch;

      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newStereo(mixer.chanStereo(0), sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      sampler.setAttackTime(7);
      sampler.setReleaseTime(7);

      isLoaded = true;
    }
  }

  //////// public functions:
  free {
    sampler.free;
    this.freeModule;
    isLoaded = false;
  }

  playTremolo { sampler.playSampleSustaining(\trem, 1, -3); }
  releaseTremolo { sampler.releaseSampleSustaining(\trem) }

  playChords { sampler.playSampleSustaining(\chords, 0); }
  releaseChords { sampler.releaseSampleSustaining(\chords); }
}