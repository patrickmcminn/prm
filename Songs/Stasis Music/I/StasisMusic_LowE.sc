/*
Thursday, August 20th 2015
StasisMusic_LowE.sc
prm
*/

StasisMusic_LowTonic :IM_Module {

  var <server, <isLoaded;
  var sampler;
  var <attackTime, <decayTime, <sustainLevel, <releaseTime;

  *new { | outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/Stasis Music/I/Samples/Low E.wav";
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      sampler = SamplePlayer.newMono(mixer.chanStereo(0), path, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      attackTime = 3;
      decayTime = 0;
      sustainLevel = 1;
      releaseTime = 7;

      sampler.setAttackTime(attackTime);
      sampler.setDecayTime(decayTime);
      sampler.setSustainLevel(sustainLevel);
      sampler.setReleaseTime(releaseTime);
      sampler.setFilterCutoff(450);
      sampler.setTremoloDepth(1);

      mixer.setVol(6);

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    sampler.free;
    sampler = nil;
    this.freeModule;
  }

  playSample { | note = 4, name = 'lowTonic', tremRate = 0.1 |
    sampler.setTremoloRate(tremRate);
    sampler.playSampleSustaining('lowTonic', 0, (note-4).midiratio, 0, 0.5, 0);
  }

  releaseSample { | name = 'lowTonic' | sampler.releaseSampleSustaining(name); }
}