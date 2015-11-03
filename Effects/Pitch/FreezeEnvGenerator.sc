/*
Wednesday, July 1st 2015
FreezeEnvGenerator.sc
prm
*/

FreezeEnvGenerator : IM_Module {

  var <isLoaded;
  var <freeze, <envGenerator;
  var <inBus;
  var server;

  *newMono { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono;
  }

  prInitMono {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      envGenerator = PitchEnvGenerator.newStereo(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { envGenerator.isLoaded } != true }, { 0.001.wait; });

      freeze = IM_GrainFreeze.new(envGenerator.inBus, group, 'addToHead');
      while({ try { freeze.isLoaded } != true }, { 0.001.wait; });

      inBus = freeze.inBus;
      server.sync;
      isLoaded = true;
    };
  }

  // public methods:

  free {
    freeze.free;
    envGenerator.free;
    this.freeModule;

    freeze = nil;
    envGenerator = nil;
  }


}

+ FreezeEnvGenerator {

  // convenience functions:

  freeze { freeze.freeze }
  releaseFreeze { freeze.releaseFreeze }

  makeSynthSustaining { | name = 'synth', pitchShift = 0, vol = 0, cutoff = 16000 |
    envGenerator.makeSynthSustaining(name, pitchShift, vol, cutoff);
  }

  releaseSynthSustaining { | name = 'synth' | envGenerator.releaseSynthSustaining(name); }

  makeSynthOneShot { | name = 'synth', pitchShift = 0, vol = 0, cutoff = 16000 |
    envGenerator.makeSynthOneShot(name, pitchShift, vol, cutoff);
  }

  setSynthVol { | name = 'synth',  vol = 0 | envGenerator.setSynthVol(name, vol); }
}