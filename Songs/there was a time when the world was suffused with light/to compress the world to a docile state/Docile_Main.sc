/*
Tuesday, April 24th 2018
Docile_Main.sc
prm

to compress the world to a docile state
*/

Docile_Main : IM_Module {

  var server;
  var <isLoaded;

  var <sampler, modularOut, modularIn;
  var isPlaying;

  *new {  | outBus = 0, modularInput = 2, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(modularInput);
  }

  prInit { | modularInput = 2 |
    server = Server.default;
    server.waitForBoot {
      var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/to compress the world to a docile state/samples/main/mainSample.wav";
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      isPlaying = false;

      modularIn = IM_HardwareIn.new(modularInput, mixer.chanMono(0), group, \addToHead);
      while ({ try { modularIn.isLoaded } != true }, { 0.001.wait; });

      modularOut = MonoHardwareSend.new(2, relGroup: group, addAction: \addToHead);
      while ({ try { modularOut.isLoaded } != true }, { 0.001.wait; });

      sampler = SamplePlayer.newStereo(modularOut.inBus, path, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      mixer.setPreVol(6);

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    sampler.free;
    modularOut.free;
    modularIn.free;
    this.freeModule;
    isLoaded = false;
  }

  playLoop {
    sampler.playSampleSustaining('loop');
    isPlaying = true;
  }

  stopLoop {
    sampler.releaseSampleSustaining('loop');
    isPlaying = false;
  }

  tglLoop {
    if( isPlaying == false, { this.playLoop }, { this.stopLoop });
  }
}