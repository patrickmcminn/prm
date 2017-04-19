/*
Tuesday, December 3rd 2015
FalseSelf_BassSection.sc
prm
driving from Portland, ME to Ithaca, NY
*/

FalseSelf_BassSection :IM_Module {

  var server, <isLoaded;
  var <satur, <feedback, <guitar, <moog;
  var <saturFadeBus, <feedbackFadeBus, <moogFadeBus;
  var <preChorusIsPlaying, <chorusIsPlaying, <postChorusIsPlaying;
  var <endIsPlaying, <codaIsPlaying;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead',
    moogDeviceName = "iConnectAudio4+", moogPortName = "DIN" |
    ^super.new(4, outBus, send0Bus, send1Bus, send3Bus, send3Bus, false, relGroup, addAction).prInit(
      moogDeviceName, moogPortName);
  }

  prInit { | moogDeviceName = "iConnectAudio4+", moogPortName = "DIN" |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      satur = SaturSynth.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { satur.isLoaded } != true }, { 0.001.wait; });

      feedback = FeedbackSynth.new(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { feedback.isLoaded } != true },  { 0.001.wait; });

      guitar = FakeGuitar.new(mixer.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { guitar.isLoaded } != true }, { 0.001.wait; });

      moog = Mother32.new(3, mixer.chanStereo(3), moogDeviceName, moogPortName, 1, 1,
        relGroup: group, addAction: \addToHead);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });


      saturFadeBus = Bus.control;
      feedbackFadeBus = Bus.control;
      moogFadeBus = Bus.control;

      server.sync;
      moog.makeSequence('preChorus');
      moog.makeSequence('chorus');
      moog.makeSequence('postChorus');


      server.sync;

      satur.makeSequence('preChorus');
      satur.makeSequence('chorus');
      satur.makeSequence('postChorus');
      satur.makeSequence('end');
      satur.makeSequence('coda');

      server.sync;

      feedback.makeSequence('preChorus');
      feedback.makeSequence('preChorusOctave');
      feedback.makeSequence('preChorusSecondOctave');

      feedback.makeSequence('chorus');
      feedback.makeSequence('chorusOctave');
      feedback.makeSequence('chorusSecondOctave');

      //feedback.makeSequence('postChorus');
      //feedback.makeSequence('preEnd');
      feedback.makeSequence('end');
      feedback.makeSequence('endOctave');

      server.sync;

      guitar.makeSequence('chorus');

      server.sync;

      this.prMakeGuitarPatterns;
      this.prMakeSaturSynthPatterns;
      this.prMakeMoogPatterns;
      this.prMakeFeedbackSynthPatterns;

      guitar.setFilterCutoff(1200);
      feedback.setFilterCutoff(30);

      satur.mixer.setVol(-6);

      server.sync;

      mixer.setMasterVol(-9);

      preChorusIsPlaying = false;
      chorusIsPlaying = false;
      postChorusIsPlaying = false;
      endIsPlaying = false;
      codaIsPlaying = false;

      isLoaded = true;
    }
  }

  //////// public:
  free {
    guitar.free;
    feedback.free;
    satur.free;
    moog.free;
    feedbackFadeBus.free;
    saturFadeBus.free;
    moogFadeBus.free;
    this.freeModule;
  }

  prMakeGuitarPatterns {
    var note, dur;
    note = Pseq([[6, 18], [5, 17], [6, 18], [5, 17], [6, 18], [8, 20], [6, 18], [13, 25], [6, 18],  [5, 17]], 2);
    dur = Pseq([6, 8, 6, 6, 6, 8, 2, 2, 2, 6], 2);

    guitar.addKey(\chorus, \octave, 3);
    guitar.addKey(\chorus, \legato, 1);
    guitar.addKey(\chorus, \note, note);
    guitar.addKey(\chorus, \dur, dur);

  }

  prMakeFeedbackSynthPatterns {
    var chorusNote, chorusDur;
    var endDur, endNote;

    chorusNote = Pseq([6, 5, 6, 5, 6, 8, 6, 13, 6, 5], 2);
    chorusDur = Pseq([6, 8, 6, 6, 6, 8, 2, 2, 2, 6], 2);

    endDur = Pseq([8, 8, 6, 10, 8, 8, 6, 6, 8, 6, 8], 1);
    endNote = Pseq([1, 0, 1, -4, -3, 1, -3, -4, -6, -8, -13], 1);


    feedback.addKey(\preChorus, \dur, Pseq([56], 1));
    feedback.addKey(\preChorus, \octave, 3);
    feedback.addKey(\preChorus, \legato, 1);
    feedback.addKey(\preChorus, \note, 1);

    feedback.addKey(\preChorusOctave, \dur, Pseq([56], 1));
    feedback.addKey(\preChorusOctave, \octave, 3);
    feedback.addKey(\preChorusOctave, \legato, 1);
    feedback.addKey(\preChorusOctave, \note, 13);

    feedback.addKey(\preChorusSecondOctave, \dur, Pseq([56], 1));
    feedback.addKey(\preChorusSecondOctave, \octave, 3);
    feedback.addKey(\preChorusSecondOctave, \legato, 1);
    feedback.addKey(\preChorusSecondOctave, \note, 25);

    feedback.addKey(\chorus, \octave, 3);
    feedback.addKey(\chorus, \legato, 1);
    feedback.addKey(\chorus, \releaseTime, 0.1);
    feedback.addKey(\chorus, \dur, chorusDur);
    feedback.addKey(\chorus, \note, chorusNote);
    feedback.addKey(\chorus, \filterCutoff, 5720);
    feedback.addKey(\chorusOctave, \octave, 4);
    feedback.addKey(\chorusOctave, \legato, 1);
    feedback.addKey(\chorusOctave, \releaseTime, 0.1);
    feedback.addKey(\chorusOctave, \dur, chorusDur);
    feedback.addKey(\chorusOctave, \note, chorusNote);
    feedback.addKey(\chorusOctave, \filterCutoff, 5720);
    feedback.addKey(\chorusSecondOctave, \octave, 5);
    feedback.addKey(\chorusSecondOctave, \legato, 1);
    feedback.addKey(\chorusSecondOctave, \releaseTime, 0.1);
    feedback.addKey(\chorusSecondOctave, \dur, chorusDur);
    feedback.addKey(\chorusSecondOctave, \note, Pseq([Pseq([\r], 10), chorusNote], 1));
    feedback.addKey(\chorusSecondOctave, \filterCutoff, 5720);
    feedback.addKey(\chorusSecondOctave, \amp, 0.5);

    /*
    feedback.addKey(\postChorus, \octave, 3);
    feedback.addKey(\postChorus, \legato, 1);
    feedback.addKey(\postChorus, \note, Pseq([1], inf));
    feedback.addKey(\postChorus, \dur, Pseq([4], inf));
    */
    /*
    feedback.addKey(\preEnd, \octave, 4);
    feedback.addKey(\preEnd, \legato, 1);
    feedback.addKey(\preEnd, \dur, 8);
    feedback.addKey(\preEnd, \note, Pseq([[1, 13]], 1));
    */

    feedback.addKey(\end, \octave, 4);
    feedback.addKey(\end, \legato, 1);
    feedback.addKey(\end, \dur, endDur);
    feedback.addKey(\end, \note, endNote);
    feedback.addKey(\end, \filterCutoff, 500);
    feedback.addKey(\endOctave, \octave, 5);
    feedback.addKey(\endOctave, \legato, 1);
    feedback.addKey(\endOctave, \dur, endDur);
    feedback.addKey(\endOctave, \note, endNote);
    feedback.addKey(\endOctave, \filterCutoff, 950);

  }

  prMakeSaturSynthPatterns {
    var chorus1Note, chorus2Note, chorusDur;
    var endDur, endNote;

    chorus1Note = Pseq([[6, 18], [5, 17], [6, 18], [5, 17], [6, 18], [8, 20], [6, 18], [13, 25], [6, 18],  [5, 17]], 1);
    chorus2Note = Pseq(
      [
        [6, 18, 30], [5, 17, 29], [6, 18, 30], [5, 17, 29], [6, 18, 30], [8, 20, 32], [6, 18, 30], [13, 25, 37],
        [6, 18, 30],  [5, 17, 29]
    ], 1);
    chorusDur = Pseq([6, 8, 6, 6, 6, 8, 2, 2, 2, 6], 2);

    endDur = Pseq([8, 8, 6, 10, 8, 8, 6, 6, 8, 6, 8], 1);
    endNote = Pseq([1, 0, 1, -4, -3, 1, -3, -4, -6, -8, -13], 1);


    satur.addKey(\preChorus, \dur, Pseq([56], 1));
    satur.addKey(\preChorus, \octave, 3);
    satur.addKey(\preChorus, \legato, 1);
    satur.addKey(\preChorus, \note, [1, 13, 25]);

    satur.addKey(\chorus, \octave, 3);
    satur.addKey(\chorus, \legato, 1);
    satur.addKey(\chorus, \releaseTime, 0.1);
    satur.addKey(\chorus, \dur, chorusDur);
    satur.addKey(\chorus, \note, Pseq([chorus1Note, chorus2Note], 1));

    satur.addKey(\postChorus, \octave, 3);
    satur.addKey(\postChorus, \legato, 1);
    satur.addKey(\postChorus, \note, Pseq([[1, 13, 20]], inf));
    satur.addKey(\postChorus, \dur, Pseq([4], inf));
    satur.addKey(\postChorus, \releaseTime, 0.5);
    satur.addKey(\postChorus, \attackTime, 0.25);

    satur.addKey(\end, \octave, [3, 4, 5]);
    satur.addKey(\end, \legato, 1);
    satur.addKey(\end, \dur, endDur);
    satur.addKey(\end, \note, endNote);

    satur.addKey(\coda, \octave, [3, 4, 5]);
    satur.addKey(\coda, \legato, 1);
    satur.addKey(\coda, \dur, Pseq([16], inf));
    satur.addKey(\coda, \note, Pseq([8], inf));
  }

  prMakeMoogPatterns {
    var chorusNote, chorusDur;

    chorusNote = Pseq([6, 5, 6, 5, 6, 8, 6, 13, 6, 5], 2);
    chorusDur = Pseq([6, 8, 6, 6, 6, 8, 2, 2, 2, 6], 2);

    moog.addKey(\preChorus, \octave, 2);
    moog.addKey(\preChorus, \legato, 0.99);
    moog.addKey(\preChorus, \note, 1);
    moog.addKey(\preChorus, \dur, Pseq([56], 1));

    moog.addKey(\chorus, \octave, 2);
    moog.addKey(\chorus, \legato, 0.99);
    moog.addKey(\chorus, \dur, chorusDur);
    moog.addKey(\chorus, \note, chorusNote);

    moog.addKey(\postChorus, \octave, 2);
    moog.addKey(\postChorus, \legato, 0.99);
    moog.addKey(\postChorus, \dur, Pseq([4], inf));
    moog.addKey(\postChorus, \note, Pseq([1], inf));
  }


  playBassNote { | freq, saturVol = -6, feedbackVol = -6, guitarVol = -6 |
    satur.playNote(freq, saturVol);
    feedback.playNote(freq, feedbackVol);
    guitar.playNote(freq, guitarVol);
  }

  releaseBassNote { | freq |
    satur.releaseNote(freq);
    feedback.releaseNote(freq);
    guitar.releaseNote(freq);
  }

  playPreChorus { | clock |
    moog.playSequence(\preChorus, clock);
    satur.playSequence(\preChorus, clock);
    feedback.playSequence(\preChorus, clock);
    feedback.playSequence(\preChorusOctave, clock);
    feedback.playSequence(\preChorusSecondOctave, clock);
    preChorusIsPlaying = true;
  }

  stopPreChorus {
    moog.stopSequence(\preChorus);
    satur.stopSequence(\preChorus);
    feedback.stopSequence(\preChorus);
    feedback.stopSequence(\preChorusOctave);
    feedback.stopSequence(\preChorusSecondOctave);
    preChorusIsPlaying = false;
  }

  playChorus { | clock |
    moog.playSequence(\chorus, clock);
    satur.playSequence(\chorus, clock);
    feedback.playSequence(\chorus, clock);
    feedback.playSequence(\chorusOctave, clock);
    feedback.playSequence(\chorusSecondOctave, clock);
    guitar.playSequence(\chorus, clock);
    chorusIsPlaying = true;
  }

  stopChorus {
    moog.stopSequence(\chorus);
    satur.stopSequence(\chorus);
    feedback.stopSequence(\chorus);
    feedback.stopSequence(\chorusOctave);
    feedback.stopSequence(\chorusSecondOctave);
    guitar.stopSequence(\chorus);
    chorusIsPlaying = false;
  }

  playPostChorus { | clock |
    moog.playSequence(\postChorus, clock);
    satur.playSequence(\postChorus, clock);
    feedback.playNote(47.midicps);
   // feedback.playNote(35.midicps);
    postChorusIsPlaying = true;
  }


  stopPostChorus {
    moog.stopSequence(\postChorus);
    satur.stopSequence(\postChorus);
    feedback.releaseNote(49.midicps);
    //feedback.releaseNote(35.midicps);
    postChorusIsPlaying = false;
  }

  playEnd { | clock |
    satur.playSequence(\end, clock);
    feedback.playSequence(\end, clock);
    feedback.playSequence(\endOctave, clock);
    endIsPlaying = true;
  }

  stopEnd {
    satur.stopSequence(\end);
    feedback.stopSequence(\end);
    feedback.stopSequence(\endOctave);
    endIsPlaying = false;
  }

  playCoda { | clock |
    satur.playSequence(\coda, clock);
    codaIsPlaying = true;
  }

  stopCoda {
    satur.stopSequence(\coda);
    codaIsPlaying = false;
  }

  fadeSaturSynth { | start = 0, end = 0.5, time = 21 |
    { Out.kr(saturFadeBus, Line.kr(start, end, time, doneAction: 2)); }.play;
    satur.mixer.mapAmp(saturFadeBus);
  }

  fadeFeedbackSynth { | start = 0, end = 1, time = 21 |
    { Out.kr(feedbackFadeBus, Line.kr(start, end, time, doneAction: 2)); }.play;
    feedback.mixer.mapAmp(feedbackFadeBus);
  }

  fadeMoog { | start = 0, end = 1, time = 21 |
    { Out.kr(moogFadeBus, Line.kr(start, end, time, doneAction: 2)); }.play;
    moog.mixer.mapAmp(moogFadeBus);
  }
}

