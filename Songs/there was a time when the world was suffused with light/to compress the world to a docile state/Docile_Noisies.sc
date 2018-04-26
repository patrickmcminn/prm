/*
Monday, April 23rd 2018
Docile_Noisies.sc
prm

to compress the world to a docile state
*/

Docile_Noisies : IM_Module {

  var <isLoaded;
  var server;

  var <sampler, <delayLeft, <delayRight, splitter, <eq;

  *new { | outBus = 0, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path, sampleArray;

      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/to compress the world to a docile state/samples/noisies/";
      sampleArray = (path ++ "*").pathMatch;


      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      delayLeft = SimpleDelay.newStereo(eq.inBus, 0.14, 0.48, 0.2, relGroup: group, addAction: \addToHead);
      while({ try { delayLeft.isLoaded } != true }, { 0.001.wait; });

      delayRight = SimpleDelay.newStereo(eq.inBus, 0.28571428571429, 0.48, 0.3, relGroup: group, addAction: \addToHead);
      while({ try { delayRight.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newStereo(2, [delayLeft.inBus, delayRight.inBus], false, group, \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newStereo(splitter.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitializeParameters;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    eq.setHighPassCutoff(90);
    delayLeft.mixer.setPanBal(-1);
    delayRight.mixer.setPanBal(1);
  }

  //////// public functions:

  free {
    eq.free;
    splitter.free;
    delayLeft.free;
    delayRight.free;
    sampler.free;
    this.freeModule;
    isLoaded = false;
  }

}