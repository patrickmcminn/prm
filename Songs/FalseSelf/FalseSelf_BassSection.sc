/*
Tuesday, December 3rd 2015
FalseSelf_BassSection.sc
prm
driving from Portland, ME to Ithaca, NY
*/

FalseSelf_BassSection :IM_Module {

  var server, <isLoaded;
  var <satur, <feedback, <guitar, <moog;

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



      server.sync;
      moog.makeSequence('preChorus');
      moog.makeSequence('chorus');
      moog.makeSequence('postChorus');

      satur.makeSequence('preChorus');
      satur.makeSequence('chorus');
      satur.makeSequence('postChorus');
      satur.makeSequence('end');

      feedback.makeSequence('preChorus');
      feedback.makeSequence('chorus');
      feedback.makeSequence('postChorus');
      feedback.makeSequence('preEnd');
      feedback.makeSequence('end');

      server.sync;

      this.prMakeFeedbackSynthPatterns;
      this.prMakeSaturSynthPatterns;
      this.prMakeMoogPatterns;
      guitar.setFilterCutoff(238);

      server.sync;

      mixer.setMasterVol(-9);

      isLoaded = true;
    }
  }

  //////// public:
  free {
    guitar.free;
    feedback.free;
    satur.free;
    this.freeModule;
  }

  prMakeFeedbackSynthPatterns {
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

    feedback.addKey(\preChorus, \dur, 56);
    feedback.addKey(\preChorus, \octave, 3);
    feedback.addKey(\preChorus, \legato, 1);
    feedback.addKey(\preChorus, \note, Pseq([[1, 13, 25]], 1));

    feedback.addKey(\chorus, \octave, 3);
    feedback.addKey(\chorus, \legato, 1);
    feedback.addKey(\chorus, \releaseTime, 0.1);
    feedback.addKey(\chorus, \dur, chorusDur);
    feedback.addKey(\chorus, \note, Pseq([chorus1Note, chorus2Note], 1));

    feedback.addKey(\postChorus, \octave, 3);
    feedback.addKey(\postChorus, \legato, 1);
    feedback.addKey(\postChorus, \note, Pseq([[1, 13, 20]], inf));
    feedback.addKey(\postChorus, \dur, Pseq([4], inf));

    feedback.addKey(\preEnd, \octave, 4);
    feedback.addKey(\preEnd, \legato, 1);
    feedback.addKey(\preEnd, \dur, 8);
    feedback.addKey(\preEnd, \note, Pseq([[1, 13]], 1));

    feedback.addKey(\end, \octave, [4, 5]);
    feedback.addKey(\end, \legato, 1);
    feedback.addKey(\end, \dur, endDur);
    feedback.addKey(\end, \note, endNote);
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


    satur.addKey(\preChorus, \dur, 56);
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


    feedback.addKey(\end, \octave, [4, 5, 6]);
    feedback.addKey(\end, \legato, 1);
    feedback.addKey(\end, \dur, endDur);
    feedback.addKey(\end, \note, endNote);
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
}

