/*
Saturday, April 22nd, 2017
Light_Bass.sc
prm
*/

Light_Bass : IM_Module {

  var server, <isLoaded;
  var <bass;

  *new { | outBus = 0, moogDevice = "iConnectAudio4+", moogPort = "DIN", relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(moogDevice, moogPort);
  }

  prInit { | moogDevice = "iConnectAudio4+", moogPort = "DIN" |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      bass = Mother32.new(3, mixer.chanStereo(0), moogDevice, moogPort, relGroup: group, addAction: \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    bass.free;
    this.freeModule;
    isLoaded = false;
  }

  playNote { | freq = 220 | bass.playNote(freq); }
  releaseNote { | freq = 220 | bass.releaseNote(freq); }
}