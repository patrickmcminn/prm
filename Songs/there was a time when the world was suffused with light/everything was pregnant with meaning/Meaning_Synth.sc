/*
Thursday, April 19th 2018
Meaning_Synth.sc
prm

everything was pregnant with meaning
*/

Meaning_Synth : IM_Module {

  var <isLoaded;
  var server;

  var <subtractive, <eq;

  var <chord1IsPlaying, <chord2IsPlaying, <chord3IsPlaying, <chord4IsPlaying;

  *new { |outBus = 0, relGroup = nil, addAction = \addToHead |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      subtractive = Subtractive.new(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

      subtractive.makeSequence(\chord1);
      subtractive.makeSequence(\chord2);
      subtractive.makeSequence(\chord3);

      subtractive.makeSequence(\chord4);

      server.sync;

      this.prMakeSequences;

      isLoaded = true;
    }
  }

  prSetInitialParameters {

    chord1IsPlaying = false;
    chord2IsPlaying = false;
    chord3IsPlaying = false;
    chord4IsPlaying = false;

    eq.setHighPassCutoff(12500);
    //eq.setLowPassCutoff(150);

    subtractive.setAllParameters([ 1, 1, 4, 5.3, 0.5, 0, 0.05, 0.05, 1, 0.5, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 2, 0.5, 0.5, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 3, 0.5, 0.25, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 1, 1.5, 0.53, 1, 0.05, 8.42, 10, false, 0.75, 1.5, 1, 1, 0, 1.1, 0, 0, 1, 1300, 0, 0, 1, 1, 1, 1, 0.7, 0.351, 0.84, 1, 0, -1, 1, 0, 0 ]);
  }

  prMakeSequences {
    subtractive.addKey(\chord1, \note, Prand([
      [-13, -8, -4, 3, 11, 15, 20, 22],
      [-13, -8, -4, 3, 11, 15, 20, 22],
      [-13, -8, -4, 3, 11, 15, 20, 22],
      [-13, -8, -4, 3, 11, 15, 20, 22],
      [-13, -8, -4, 3, 11, 15, 20, 22],
      [-13, -8, -4, 3, 11, 15, 20, 22],
      3, 15, 16, 6, -4, -8, -13, 20, 22, 23, 20, 22, 23,
  ], inf));
    subtractive.addKey(\chord1, \amp, Pwhite(0.5, 0.9, inf));
    subtractive.addKey(\chord1, \dur, Pwhite(0.7, 1.2, inf));
    subtractive.addKey(\chord1, \legato, 1);


    subtractive.addKey(\chord2, \note, Prand([
      [-11, 1, 6, 17, 20],
      [-11, 1, 6, 17, 20],
      [-11, 1, 6, 17, 20],
      [-11, 1, 6, 17, 20],
      [-11, 1, 6, 17, 20],
      [-11, 1, 6, 17, 20],
      1, 5, 17, 20, 6, -11, -13, -14, -12, -18, -30
    ], inf));
    subtractive.addKey(\chord2, \amp, Pwhite(0.5, 0.9, inf));
    subtractive.addKey(\chord2, \dur, Pwhite(0.7, 1.2, inf));
    subtractive.addKey(\chord2, \legato, 1);

    subtractive.addKey(\chord3, \note, Prand([3, 13, 20, 25, 5, 1, -18, -30], inf));
    subtractive.addKey(\chord3, \amp, Pwhite(0.5, 0.9, inf));
    subtractive.addKey(\chord3, \dur, Pwhite(0.5, 0.8, inf));
    subtractive.addKey(\chord3, \legato, 1.3);

    subtractive.addKey(\chord4, \note, Pseq([
      [-23, -11, 8, 10, 12, 20, 22, 24],
      [-23, -11, 8, 10, 12, 20, 22, 24],
      [-27, -15, 12, 24],
      [-27, -15, 12, 24]
    ], inf));
    subtractive.addKey(\chord4, \amp, 0.4);
    subtractive.addKey(\chord4, \dur, 2);
    subtractive.addKey(\chord4, \legato, 0.75);
  }

  //////// public functions:

  free {
    eq.free;
    subtractive.free;
    this.freeModule;
    isLoaded = false;
  }

  playChord1 {
    subtractive.playSequence(\chord1);
    chord1IsPlaying = true;
  }

  releaseChord1 {
    subtractive.stopSequence(\chord1);
    chord1IsPlaying = false;
  }

  tglChord1 { if(chord1IsPlaying == false,
    { this.playChord1 }, { this.releaseChord1 });
  }

  playChord2 {
    subtractive.playSequence(\chord2);
    chord2IsPlaying = true;
  }

  releaseChord2 {
    subtractive.stopSequence(\chord2);
    chord2IsPlaying = false;
  }

  tglChord2 { if(chord2IsPlaying == false,
    { this.playChord2 }, { this.releaseChord2 });
  }

  playChord3 {
    subtractive.playSequence(\chord3);
    chord3IsPlaying = true;
  }

  releaseChord3 {
    subtractive.stopSequence(\chord3);
    chord3IsPlaying = false;
  }

  tglChord3 { if(chord3IsPlaying == false,
    { this.playChord3 }, { this.releaseChord3 });
  }

  playChord4 {
    subtractive.playSequence(\chord4);
    chord4IsPlaying = true;
  }

  releaseChord4 {
    subtractive.stopSequence(\chord4);
    chord4IsPlaying = false;
  }

  tglChord4 { if(chord4IsPlaying == false,
    { this.playChord4 }, { this.releaseChord4 });
  }

}

/*
sketch:
x.setAttackTime(0.7);
x.setReleaseTime(5);
x.setDecayTime(0.351);
x.setSustainLevel(0.84);

x.setLFO1Waveform('sampleAndHold');
x.setLFO1Freq(5.3);

x.setPanLFO1Bottom(-1);
x.setPanLFO1Top(1);

x.setFilterEnvAttackTime(0.05);
x.setFilterEnvDecayTime(8.42);
x.setFilterEnvSustainRatio(0.53);
x.setFilterEnvReleaseTime(0.25);

x.setFilterEnvAttackRatio(1);
x.setFilterEnvPeakRatio(2);

x.setFilterCutoffLFO1BottomRatio(0.75);
x.setFilterCutoffLFO1TopRatio(2);
x.setFilterResLFO1Bottom(0);
x.setFilterResLFO1Top(1.1);

x.playNote(220);
x.releaseNote(220);

x.setOsc1SubVol(-inf);
x.setOsc2SubVol(-inf);

x.setOsc2Octave(3);
x.setFilterEnvReleaseTime(10);


*/
