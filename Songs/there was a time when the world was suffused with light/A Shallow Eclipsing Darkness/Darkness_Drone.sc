/*
Sunday, April 8th 2018
Darkness_Drone.sc
prm

A Shallow Eclipsing Darkness
*/

Darkness_Drone : IM_Module {

  var <isLoaded;
  var server;
  var <synth, <eq;

  *new { | outBus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      synth = Subtractive.new(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { synth.isLoaded } != true }, { 0.001.wait; });

      this.prInitSynth;
      this.prInitEQ;

      server.sync;

      synth.makeSequence(\cSharp);
      synth.makeSequence(\dSharp);
      synth.makeSequence(\E);
      synth.makeSequence(\fSharp);
      synth.makeSequence(\gSharp);
      synth.makeSequence(\A);
      synth.makeSequence(\B);
      synth.makeSequence(\cSharp2);
      synth.makeSequence(\cSharp0);
      synth.makeSequence(\c);

      server.sync;

      this.prMakePatterns;
      mixer.setPreVol(-12);

      isLoaded = true;
    }
  }

  prInitSynth {
    // see sketch "swampmusic variation.scd" for actual patch and info
    synth.setAllParameters([ 1, 1, 5, 0.5, 0.5, 0, 0.05, 0.05, 0.11, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0.1, 1, 1, 1, -2, 2, 0, 0, 3, 0.5, 0.5, 0, 2, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0.2, 1, 1, 2, -2, 0, 0, 3, 0.5, 0.25, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 1, 1, 1, 1, 0.05, 0, 0.05, false, 0.1, 1.5, 1, 1, 0, 0, 0, 0, 50, 1500, 0.7, 0, 1, 1, 1, 1, 10, 0.05, 1, 10, 0, -1, 1, 0, 0 ]);
  }

  prInitEQ {
    // from Ableton file of mix
    eq.setHighPassCutoff(350);
    eq.setLowFreq(250);
    eq.setLowGain(-6);
    eq.setHighFreq(2500);
    eq.setHighGain(3);
    eq.setLowPassCutoff(3500);
  }

  prMakePatterns {
    synth.addKey(\cSharp, \note, Pseq([1, \r], inf));
    synth.addKey(\cSharp, \dur, Pwhite(15, 25));
    synth.addKey(\cSharp, \amp, Pwhite(0.2, 0.32));
    synth.addKey(\cSharp, \legato, 1);
    synth.addKey(\cSharp, \osc2OctaveMul, Prand([0.5, 1, 1, 2, 4], inf));

    synth.addKey(\dSharp, \note, Pseq([3, \r], inf));
    synth.addKey(\dSharp, \dur, Pwhite(17, 27));
    synth.addKey(\dSharp, \amp, Pwhite(0.2, 0.32));
    synth.addKey(\dSharp, \legato, 1);

    synth.addKey(\E, \note, Pxrand([4, \r], inf));
    synth.addKey(\E, \dur, Pwhite(21, 30));
    synth.addKey(\E, \amp, Pwhite(0.2, 0.32));
    synth.addKey(\E, \legato, 1);

    synth.addKey(\fSharp, \note, Prand([6, \r], inf));
    synth.addKey(\fSharp, \dur, Pwhite(11, 20));
    synth.addKey(\fSharp, \amp, Pwhite(0.2, 0.32));
    synth.addKey(\fSharp, \legato, 1);
    synth.addKey(\fSharp, \osc2DetuneCents, Pwhite(0, 20));

    synth.addKey(\gSharp, \note, Pxrand([8, \r], inf));
    synth.addKey(\gSharp, \dur, Pwhite(15, 25));
    synth.addKey(\gSharp, \amp, Pwhite(0.2, 0.32));
    synth.addKey(\gSharp, \legato, 1);
    synth.addKey(\gSharp, \osc1SubAmp, Prand([0, 0, Pwhite(0.1, 0.6)], inf));

    synth.addKey(\A, \note, Prand([9, \r, \r], inf));
    synth.addKey(\A, \dur, Pwhite(15, 25));
    synth.addKey(\A, \amp, Pwhite(0.2, 0.32));
    synth.addKey(\A, \legato, 1);

    synth.addKey(\B, \note, Prand([11, \r, \r, -1], inf));
    synth.addKey(\B, \dur, Pwhite(15, 25));
    synth.addKey(\B, \amp, Pwhite(0.2, 0.32));
    synth.addKey(\B, \legato, 1);

    synth.addKey(\cSharp2, \note, Pseq([13, \r], inf));
    synth.addKey(\cSharp2, \dur, Pwhite(15, 25));
    synth.addKey(\cSharp2, \amp, Pwhite(0.2, 0.32));
    synth.addKey(\cSharp2, \legato, 1);

    synth.addKey(\cSharp0, \note, Pseq([-11, \r, \r, \r], inf));
    synth.addKey(\cSharp0, \dur, Pwhite(15, 25));
    synth.addKey(\cSharp0, \amp, Pwhite(0.2, 0.32));
    synth.addKey(\cSharp0, \legato, 1);

    synth.addKey(\c, \note, Pseq([0, \r], inf));
    synth.addKey(\c, \dur, Pwhite(5, 30));
    synth.addKey(\c, \amp, Pwhite(0.2, 0.32));
    synth.addKey(\c, \legato, 1);
  }

  //////// public functions:

  free {
    synth.free;
    eq.free;
    this.freeModule;
    isLoaded = false;
  }

}