/*
Monday, June 19th 2017
Foundation_BassSection.sc
prm
*/

Foundation_BassSection : IM_Module {

  var <isLoaded, server;
  var <eq, <satur, <sub;

  * new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(2, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      satur = SaturSynth.new(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { satur.isLoaded } != true }, { 0.001.wait; });

      sub = Subtractive.new(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { sub.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitParameters;

      satur.makeSequence(\arrival);
      satur.makeSequence(\main);

      sub.makeSequence(\main);

      server.sync;

      this.prMakePatterns;

      isLoaded = true;
    }
  }

  prInitParameters {
    sub.mixer.setVol(-6);

    //// eq:
    eq.setHighPassCutoff(240);

    //// subtractive:
    sub.setOsc1Waveform('saw');
    sub.setOsc1Octave(3);
    sub.setOsc2Waveform('rect');
    sub.setOsc2Octave(2);
    sub.setFilterCutoff(1000);
    sub.setFilterRes(0.25);
    sub.setAttackTime(0.07);
    sub.setReleaseTime(0.1);
  }

  prMakePatterns {
    var arrivalNoteArray, arrivalDurArray;
    var iter1NoteArray, iter1DurArray;
    var iter2NoteArray, iter2DurArray;
    var iter3NoteArray, iter3DurArray;
    var iter4NoteArray, iter4DurArray;

    arrivalNoteArray = Pseq([5, 17, 5, 9, 4], 1);
    arrivalDurArray = Pseq([8, 8, 8, 8, 16], 1);

    iter1NoteArray = Pseq([2, 5, -7, 0, 2, 9, 5, -7, 0], 1);
    iter1DurArray = Pseq([8, 2, 4, 2, 6, 2, 2, 2, 2], 1);
    iter2NoteArray = Pseq([2, 7, 5, -7, 0, 2, 9, 5, -7, 0], 1);
    iter2DurArray = Pseq([6, 2, 2, 4, 2, 4, 2, 2, 1.5, 1.5], 1);
    iter3NoteArray = Pseq([2, 7, 5, -7, 0, 2, 9, 4, -8, 0], 1);
    iter3DurArray = Pseq([4, 2, 2, 4, 2, 4, 2, 2, 1.5, 1.5], 1);
    iter4NoteArray = Pseq([2, 7, 5, -7, 0, 2, 9, 4, -8, -12], 1);
    iter4DurArray = Pseq([4, 2, 2, 2, 2, 4, 2, 2, 2, 2], 1);

    satur.addKey(\arrival, \legato, 1);
    satur.addKey(\arrival, \dur, arrivalDurArray);
    satur.addKey(\arrival, \note, arrivalNoteArray);

    satur.addKey(\main, \legato, 1);
    satur.addKey(\main, \note, Pseq([iter1NoteArray, iter2NoteArray, iter3NoteArray, iter4NoteArray, 2], 1));
    satur.addKey(\main, \dur, Pseq([iter1DurArray, iter2DurArray, iter3DurArray, iter4DurArray, 24], 1));

    sub.addKey(\main, \legato, 1);
    sub.addKey(\main, \octave, 4);
    sub.addKey(\main, \note, Pseq([iter1NoteArray, iter2NoteArray, iter3NoteArray, iter4NoteArray, 2], 1));
    sub.addKey(\main, \dur, Pseq([iter1DurArray, iter2DurArray, iter3DurArray, iter4DurArray, 24], 1));
  }

  //////// public functions:

  free {
    sub.free;
    satur.free;
    eq.free;
    this.freeModule;
    isLoaded = false;
  }

  playArrivalSequence { | clock |
    eq.setHighPassCutoff(240);
    satur.playSequence(\arrival, clock);
  }

  playMainSequence { | clock |
    eq.setHighPassCutoff(20);
    satur.playSequence(\main, clock);
    sub.playSequence(\main, clock);
  }

}