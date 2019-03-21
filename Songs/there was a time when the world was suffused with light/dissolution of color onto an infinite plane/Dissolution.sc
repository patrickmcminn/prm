/*
Sunday, May 6th 2018
Dissolution.sc
prm

dissolution of light onto an infinite plane
*/

Dissolution : IM_Module {

  var server, <isLoaded;
  var <sampler, <modularOut, <modularIn;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/dissolution of color onto an infinite plane/samples/b04_2.wav";
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      modularIn = IM_HardwareIn.new(2, mixer.chanMono(0), group, \addToHead);
      while ({ try { modularIn.isLoaded } != true }, { 0.001.wait; });

      modularOut = MonoHardwareSend.new(2, relGroup: group, addAction: \addToHead);
      while ({ try { modularOut.isLoaded } != true }, { 0.001.wait; });

      sampler = SamplePlayer.newStereo(modularOut.inBus, path, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      ///// just manage this from the front page of the control surface:
      mixer.mute(0);

      isLoaded = true;
    }
  }

  /////////// public functions:
  free {
    sampler.free;
    modularIn.free;
    modularOut.free;
    this.freeModule;
    isLoaded = false;
  }

  playSample {
    sampler.playSampleOneShot(0);
  }

}