/*
Sunday, July 5th 2015
Dauphine_Bass
prm
*/

Dauphine_Bass : IM_Module {

  var <isLoaded;
  var server;
  var sampler;
  var <granulator;

  *new { | outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {

    server = Server.default;
    server.waitForBoot {
      var path = "~/Library/Application Support/SuperCollider/Extensions/prm/Songs/Dauphine Street/Samples/DauphineBass/Samples/";
      var sampleArray = (path ++ "*").pathMatch;

      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newStereo(granulator.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      granulator.setDelayMix(0.3);
      granulator.granulator.setCrossfade(1);
      granulator.setGrainDur(0.2, 0.5);
      granulator.setTrigRate(25);

      isLoaded = true;
    }
  }


  //////// public methods:

  free {
    sampler.free;
    this.freeModule;

    sampler = nil;
    isLoaded = false;
  }

  playC { | vol = -6 | sampler.playSampleOneShot(0, vol, 1, 0, 1, 0); }
  playG { | vol = -6 | sampler.playSampleOneShot(1, vol); }
  playD { | vol = -6 | sampler.playSampleOneShot(2, vol); }
  playA { | vol = -6 | sampler.playSampleOneShot(3, vol); }
  playF { | vol = -6 | sampler.playSampleOneShot(4, vol); }
  playE { | vol = -6 | sampler.playSampleOneShot(5, vol); }
}