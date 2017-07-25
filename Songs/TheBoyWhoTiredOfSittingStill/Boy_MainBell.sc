/*
Tuesday, July 25th 2017
Boy_MainBell.sc
prm
*/

Boy_MainBell : IM_Module {

  var <isLoaded;
  var server;

  var <midBells, <reverb;

  var <mainBellIsPlaying;

  *new { | outBus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      mainBellIsPlaying = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      reverb = IM_Reverb.new(mixer.chanStereo(0), mix: 0.4, roomSize: 0.9, damp: 0.1,
        relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      midBells = MidBells.new(reverb.inBus, relGroup: group, addAction: \addToHead);
      while({ try { midBells.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      midBells.makeSequence(\mainBell);

      server.sync;
      this.prMakePatternParameters;

      isLoaded = true;
    }
  }

  prMakePatternParameters {
    midBells.addKey(\mainBell, \attackTime, 0);
    midBells.addKey(\mainBell, \releaseTime, 8);
    midBells.addKey(\mainBell, \dur, 8);
    midBells.addKey(\mainBell, \buffer, midBells.sampler.bufferArray[7]);
    midBells.addKey(\mainBell, \amp, 0.75);
  }

  //////// public functions:

  free {
    this.stopMainBell;
    reverb.free;
    midBells.free;
    this.freeModule;
    isLoaded = false;
  }

  playMainBell { | clock |
    midBells.playSequence(\mainBell, clock);
    mainBellIsPlaying = true;
  }
  stopMainBell {
    midBells.stopSequence(\mainBell);
    mainBellIsPlaying = false;
  }
}