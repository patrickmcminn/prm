/*
Tuesday, October 26th 2021
Meaning_SimpleHiss.sc
prm

bug huntin'
*/

Meaning_SimpleHiss : IM_Module {

  var <isLoaded, server;

  var sampler;
  var <isPlaying;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path;
      isLoaded = false;
      isPlaying = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/everything was pregnant with meaning/samples/hiss/simpleHiss.aiff";

      sampler = SamplePlayer.newStereo(mixer.chanStereo(0), path, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

      isLoaded = true;
    };

  }

  prSetInitialParameters {
    sampler.setAttackTime(6);
    sampler.setReleaseTime(10);
  }

  free {
    try {  sampler.releaseSampleSustaining('hiss'); };
    sampler.free;
    this.freeModule;
    isLoaded = false;
  }

  playSample { sampler.playSampleSustaining('hiss', -3, 1, -0, 1, 0); isPlaying = true; }
  releaseSample { sampler.releaseSampleSustaining('hiss'); isPlaying = false; }
}