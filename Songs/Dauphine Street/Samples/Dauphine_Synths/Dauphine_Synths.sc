/*
Sunday, July 5th 2015
Dauphine_Synths.sc
prm
*/

Dauphine_Synths : IM_Module {

  var server, <isLoaded;

  var samplePlayer;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/Dauphine Street/Samples/Dauphine_Synths/Samples/DauphineStreetBacking.wav";
      isLoaded = false;
      while( { try { mixer.isLoaded } != true }, { 0.001.wait; });

      samplePlayer = SamplePlayer.newStereo(mixer.chanStereo(0), path, relGroup: group, addAction: \addToHead);
      while({ try{ samplePlayer.isLoaded } != true }, { 0.001.wait; });

      mixer.setPreVol(6);

      isLoaded = true;
    };
  }

  //////// public functions:

  free {
    samplePlayer.free;
    this.freeModule;
    samplePlayer = nil;
    isLoaded = false;
  }

  playBackingTrack { | vol = 0 |
    samplePlayer.playSampleSustaining('dauphineStreet', vol, 1, 0, 1, 0);
  }

  stopBackingTrack { samplePlayer.releaseSampleSustaining('dauphineStreet'); }

}