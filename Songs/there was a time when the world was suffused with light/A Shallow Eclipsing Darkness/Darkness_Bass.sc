/*
Monday, April 9th 2018
Darkness_Bass.sc
prm
*/

Darkness_Bass : IM_Module {

  var <isLoaded;
  var server;

  var <moog, <eq;

  *new { | inBus = 3, outBus = 0, moogDeviceName, moogPortName, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(inBus, moogDeviceName, moogPortName);
  }

  prInit { | inBus = 3, moogDeviceName, moogPortName |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });


      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      moog = Mother32.new(inBus, eq.inBus, moogDeviceName, moogPortName,
        relGroup: group, addAction: \addToHead);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });


      moog.makeSequence(\darkness);

      server.sync;

      this.prSetInitialParameters;
      this.prMakePatternParameters;

      isLoaded = true;
    }
  }

  prSetInitialParameters {
    eq.setLowFreq(120);
    eq.setLowGain(6);

  }

  prMakePatternParameters {
    var p1, p2, p3, p4, p5, p6, p7, p8;
    p1 = Pseq([7, 5, 7, 0]+1, 1);
    p2 = Pseq([7, 5, 7]+1, 1);
    p3 = Pseq([7, 5, 7, 0, 7, 5]+1, 1);
    p4 = Pseq([7, 0]+1, 1);
    p5 = Pseq([7, 5, 10, 12, 0]+1, 1);
    p6 = Pseq([7, 5]+1, 1);
    p7 = Pseq([10, 12, 0]+1, 1);
    p8 = Pseq([\r], 1);

    moog.addKey(\darkness, \note, Prand([
      p1, p1, p1, p1, p1, p1, p1, p1, p1, p1,
      p2, p2, p2, p2,
      p3, p3, p3, p3, p3,
      p4, p4, p4,
      p5, p5,
      p6, p6, p6,
      p7, p7,
      p8
    ], inf));
    moog.addKey(\darkness, \dur, Pseq([2, 3, 2, 8], inf));
    moog.addKey(\darkness, \legato, 0.9);
    moog.addKey(\darkness, \octave, 3);
    moog.sequencerClock.tempo = 120/60;

  }


  /////// public functions:
  free {
    moog.free;
    eq.free;
    this.freeModule;
    isLoaded = false;
  }

}