/*
Saturday, June 17th 2017
Foundation_Cellos.sc
prm
*/

Foundation_Cellos : IM_Module {

  var server, <isLoaded;
  var <subtractive;
  var <mainSequenceIsPlaying;

  *new { | outBus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      subtractive = Subtractive.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitParameters;

      subtractive.makeSequence(\cello1Main);
      subtractive.makeSequence(\cello2Main);
      subtractive.makeSequence(\cello3Main);

      server.sync;

      this.prMakePatterns;

      server.sync;

      mixer.setPreVol(3);

      mainSequenceIsPlaying = false;

      isLoaded = true;
    }
  }

  prInitParameters {
    subtractive.setOsc2Octave(3);
    subtractive.setOsc1Octave(3);
    subtractive.setOsc2Waveform('saw');
    subtractive.setOsc1Waveform('rect');

    subtractive.setOsc1PulseWidth(0.5);
    subtractive.setOsc1PulseWidthLFO1Bottom(-0.2);
    subtractive.setOsc1PulseWidthLFO1Top(0.2);

    subtractive.setOsc1PulseWidthLFO2Bottom(-0.3);
    subtractive.setOsc1PulseWidthLFO2Top(0.3);

    subtractive.setLFO1Waveform('noise');
    subtractive.setLFO1Freq(0.3);

    subtractive.setLFO2Freq(0.7);
    subtractive.setLFO2Waveform('noise');

    subtractive.setOsc1SubVol(-18);

    subtractive.setFilterCutoff(2000);
    subtractive.setFilterCutoffLFO1BottomRatio(0.6);
    subtractive.setFilterCutoffLFO1TopRatio(1.2);

    subtractive.setFilterCutoffLFO2BottomRatio(0.6);
    subtractive.setFilterCutoffLFO2TopRatio(1.2);

    subtractive.setOsc2DetuneCents(7.5);

    subtractive.setAttackTime(0.25);
    subtractive.setReleaseTime(0.5);
  }

  prMakePatterns {
    this.prMakeCello1Pattern;
    this.prMakeCello2Pattern;
    this.prMakeCello3Pattern;
  }

  prMakeCello3Pattern {
    var iter2NoteArray, iter2DurArray;
    var iter3NoteArray, iter3DurArray, iter4NoteArray, iter4DurArray;
    iter2NoteArray = Pseq([2, 7, 5, -7, 0, 2, 9, 5, -7, 0], 1);
    iter2DurArray = Pseq([6, 2, 2, 4, 2, 4, 2, 2, 1.5, 1.5], 1);
    iter3NoteArray = Pseq([2, 7, 5, -7, 0, 2, 9, 4, -8, 0], 1);
    iter3DurArray = Pseq([4, 2, 2, 4, 2, 4, 2, 2, 1.5, 1.5], 1);
    iter4NoteArray = Pseq([2, 7, 5, -7, 0, 2, 9, 4, -8, -12], 1);
    iter4DurArray = Pseq([4, 2, 2, 2, 2, 4, 2, 2, 2, 2], 1);

    subtractive.addKey(\cello3Main, \legato, 1);
    subtractive.addKey(\cello3Main, \octave, 4);
    subtractive.addKey(\cello3Main, \dur, Pseq([30, iter2DurArray, iter3DurArray, iter4DurArray, 12], 1));
    subtractive.addKey(\cello3Main, \note, Pseq([Rest, iter2NoteArray, iter2NoteArray, iter3NoteArray, -10], 1));

  }

  prMakeCello2Pattern {
    var iter3NoteArray, iter3DurArray;
    var iter4NoteArray, iter4DurArray;
    iter3NoteArray = Pseq([-3, 0, 2, 0, -1, -3, 4, 2, 0, -1], 1);
    iter3DurArray = Pseq([4, 2, 2, 4, 2, 4, 2, 2, 1.5, 1.5], 1);
    iter4NoteArray = Pseq([5, 4, 2, 4, 7, 5, 4, 5, 4, 7], 1);
    iter4DurArray = Pseq([4, 2, 2, 2, 2, 4, 2, 2, 2, 2], 1);

    subtractive.addKey(\cello2Main, \legato, 1);
    subtractive.addKey(\cello2Main, \octave, 3);
    subtractive.addKey(\cello2Main, \dur, Pseq([30, 27, iter3DurArray, iter4DurArray, 12], 1));
    subtractive.addKey(\cello2Main, \note, Pseq([Rest, Rest, iter3NoteArray, iter4NoteArray, 5], 1));
  }

  prMakeCello1Pattern {
    var iter3NoteArray, iter3DurArray;
    var iter4NoteArray, iter4DurArray;
    iter3NoteArray = Pseq([-3, 0, 2, 0, -1, -3, 4, 2, 0, -1], 1);
    iter3DurArray = Pseq([4, 2, 2, 4, 2, 4, 2, 2, 1.5, 1.5], 1);
    iter4NoteArray = Pseq([-3, -1, -3, 0, 4, 2, 4, 2, 0, -1], 1);
   iter4DurArray = Pseq([4, 2, 2, 2, 2, 4, 2, 2, 2, 2], 1);

    subtractive.addKey(\cello1Main, \legato, 1);
    subtractive.addKey(\cello1Main, \octave, 4);
    subtractive.addKey(\cello1Main, \dur, Pseq([30, 27, iter3DurArray, iter4DurArray, 12], 1));
    subtractive.addKey(\cello1Main, \note, Pseq([Rest, Rest, iter3NoteArray, iter4NoteArray, -3], 1));
  }

  //////// public functions:

  free {
    subtractive.free;
    this.freeModule;
    isLoaded = false;
  }

  playMainSequence { | clock |
    subtractive.playSequence(\cello3Main, clock);
    subtractive.playSequence(\cello2Main, clock);
    subtractive.playSequence(\cello1Main, clock);
    mainSequenceIsPlaying = true;
  }
}