/*
Monday, June 19th 2017
Foundation_Warps.sc
prm
*/

Foundation_Warps :IM_Module {
  var server, <isLoaded;
  var <warp1, <warp2, <warp3, <warp4, <warp5;
  var <warp1IsPlaying, <warp2IsPlaying, <warp3IsPlaying, <warp4IsPlaying, <warp5IsPlaying;

  *new { | outBus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(5, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path1 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/BuiltOnAFlawedFoundation/samples/Warps/Warp1.wav";
      var path2 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/BuiltOnAFlawedFoundation/samples/Warps/Warp2.wav";
      var path3 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/BuiltOnAFlawedFoundation/samples/Warps/Warp3.wav";
      var path4 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/BuiltOnAFlawedFoundation/samples/Warps/Warp4.wav";
      var path5 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/BuiltOnAFlawedFoundation/samples/Warps/Warp5.wav";

      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      warp1 = SamplePlayer.newStereo(mixer.chanStereo(0), path1, relGroup: group, addAction: \addToHead);
      while({ try { warp1.isLoaded } != true }, { 0.001.wait; });
      warp2 = SamplePlayer.newStereo(mixer.chanStereo(0), path2, relGroup: group, addAction: \addToHead);
      while({ try { warp2.isLoaded } != true }, { 0.001.wait; });
      warp3 = SamplePlayer.newStereo(mixer.chanStereo(0), path3, relGroup: group, addAction: \addToHead);
      while({ try { warp3.isLoaded } != true }, { 0.001.wait; });
      warp4 = SamplePlayer.newStereo(mixer.chanStereo(0), path4, relGroup: group, addAction: \addToHead);
      while({ try { warp4.isLoaded } != true }, { 0.001.wait; });
      warp5 = SamplePlayer.newStereo(mixer.chanStereo(0), path5, relGroup: group, addAction: \addToHead);
      while({ try { warp5.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      mixer.setVol(3, -6);
      mixer.setVol(4, -12);

      warp1.setAttackTime(10);
      warp2.setAttackTime(10);
      warp3.setAttackTime(10);
      warp4.setAttackTime(10);
      warp5.setAttackTime(10);

      warp1IsPlaying = false;
      warp2IsPlaying = false;
      warp3IsPlaying = false;
      warp4IsPlaying = false;
      warp5IsPlaying = false;

      isLoaded = true;
    }
  }

  //////// public functions:
  free {
    warp1.free;
    warp2.free;
    warp3.free;
    warp4.free;
    warp5.free;
    this.freeModule;
    isLoaded = false;
  }

  playWarp1 {
    warp1.playSampleSustaining(\warp);
    warp1IsPlaying = true;
  }
  releaseWarp1 {
    warp1.releaseSampleSustaining(\warp);
    warp1IsPlaying = false;
  }
  playWarp2 {
    warp2.playSampleSustaining(\warp);
    warp2IsPlaying = true;
  }
  releaseWarp2 {
    warp2.releaseSampleSustaining(\warp);
    warp2IsPlaying = false;
  }
  playWarp3 {
    warp3.playSampleSustaining(\warp);
    warp3IsPlaying = true;
  }
  releaseWarp3 {
    warp3.releaseSampleSustaining(\warp);
    warp3IsPlaying = false;
  }
  playWarp4 {
    warp4.playSampleSustaining(\warp);
    warp4IsPlaying = true;
  }
  releaseWarp4 {
    warp4.releaseSampleSustaining(\warp);
    warp4IsPlaying = false;
  }
  playWarp5 {
    warp5.playSampleSustaining(\warp);
    warp5IsPlaying = true;
  }
  releaseWarp5 {
    warp5.releaseSampleSustaining(\warp);
    warp5IsPlaying = false;
  }
}