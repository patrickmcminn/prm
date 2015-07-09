/*
Thursday, July 9th 2015
Cortez.sc
prm
*/

Cortez {

  var <isLoaded, server, group;

  var <chordsSynth, <washSynth, <phaseSynth;
  var <modularShift, <trumpetShift, <trumpetSeq;
  var <bells, <bass;
  var shiftInput, trumpetInput;
  var <clock;

  *new {
    | submixA, submixB, submixC, send0Bus, send1Bus, send2Bus, send3Bus, relGroup  = nil, addAction = 'addToHead' |
    ^super.new.prInit(submixA, submixB, submixC, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction);
  }

  prInit { | submixA, submixB, submixC, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      clock = TempoClock.new;
      clock.tempo = 60/60;
      group = Group.new(relGroup, addAction);
      server.sync;

      // synths:
      chordsSynth = Subtractive.new(submixA, send0Bus, send1Bus, send2Bus, relGroup: group, addAction: \addToHead);
      while({ try { chordsSynth.isLoaded } != true }, { 0.001.wait; });
      washSynth = Subtractive.new(submixA, send0Bus, send1Bus, send2Bus, relGroup: group, addAction: \addToHead);
      while({ try { washSynth.isLoaded } != true }, { 0.001.wait; });
      phaseSynth = Subtractive.new(submixA, send0Bus, send1Bus, send2Bus, relGroup: group, addAction: \addToHead);
      while({ try { phaseSynth.isLoaded } != true }, { 0.001.wait; });

      chordsSynth.makeSequence('cortez');
      washSynth.makeSequence(\cortezHi);
      washSynth.makeSequence(\cortezHi2);
      phaseSynth.makeSequence(\cortezPhase);
      phaseSynth.makeSequence(\cortezPhase2);
      phaseSynth.makeSequence(\cortezPhase3);
      phaseSynth.makeSequence(\cortezPhase4);
      phaseSynth.makeSequence(\cortezPhase5);

      // modular shift:
      modularShift = PitchEnvGenerator.newMono(submixB, send0Bus, send1Bus, send2Bus,
        relGroup: group, addAction: \addToHead);
      while({ try { modularShift.isLoaded } != true }, { 0.001.wait; });
      shiftInput = IM_HardwareIn.new(2, modularShift.inBus, group, \addToHead);
      while({ try { shiftInput.isLoaded } != true }, { 0.001.wait; });

      // trumpet shift:
      trumpetShift = FreezeEnvGenerator.newMono(submixB, send0Bus, send1Bus, send2Bus,
        relGroup: group, addAction: \addToHead);
      while({ try { trumpetShift.isLoaded } != true }, { 0.001.wait; });
      trumpetInput = IM_HardwareIn.new(0, trumpetShift.inBus, group, \addToHead);
      while({ try { trumpetInput.isLoaded } != true }, { 0.001.wait; });
      trumpetShift.envGenerator.setSustainTime(0.1);
      trumpetShift.envGenerator.setReleaseTime(0.09);

      // trumpet sequencer:
      trumpetSeq = FunctionSequencer.new(27, clock);
      while({ try { trumpetSeq.isLoaded } !=true }, { 0.001.wait; });

      bells = MidBells.new(submixC, send0Bus, send1Bus, send2Bus, relGroup: group,
        addAction: \addToHead);
      while({ try { bells.isLoaded } != true }, { 0.001.wait; });
      bells.setReleaseTime(7);

      bass = SaturSynth.new(submixC, send0Bus, send1Bus, send2Bus, relGroup: group,
        addAction: \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });
      bass.setAttackTime(0.1);
      bass.setDecayTime(0.5);
      bass.setSustainLevel(0.5);
      bass.setReleaseTime(7);

      server.sync;

      this.prSetSynthParameters;
      this.prMakePatternParameters;
      this.prMakeTrumpetSequence;

      server.sync;

      isLoaded = true;
    };
  }

  prSetSynthParameters {
    // set parameters:

    // chordsSynth:
    chordsSynth.mixer.setSendVol(0, -24);
    chordsSynth.setAllParameters([ 1, 1, 1, 5, 0.5, 0, 0.05, 0.05, 0.6, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0.5, 1, 1, 1, 0, 0, 0.5, 0, 1.2, 0.5, 0.50118723362727, 0, 1, -12, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0.5, 1, 1, 1, 0, 0, 0, 0, 1, 0.5, 0.25, 0.25118864315096, 1, 0, 1, 1, 1, 1, 1, 1, 1.122018454302e-50, 1000, 1, 1, 0.7, 1, 0.25, 0.5, 0.05, false, 0.5, 1, 0.6, 1.4, 0, 0, 0, 0, 10, 850, 0, 0, 1, 1, 1, 1, 0.05, 0.5, 0.7, 0.1, 0, 0, 0, 0, 0 ]);
    chordsSynth.setReleaseTime(0.5);

    // washSynth:
    washSynth.mixer.setSendVol(0, -15);
    washSynth.mixer.setSendVol(1, -99);
    washSynth.mixer.setSendPre;
    washSynth.mixer.setVol(-12);
    washSynth.setAllParameters([ 1, 1, 0, 1, 0.5, 0, 0.05, 0.05, 0.1, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0.4, 1, 0.5, 0.5, 0, 0.5, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0.5, 0.15848931924611, 1.122018454302e-50, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 0, 1, 0.5, 0, 0.1, 2.5, 3, false, 1, 1, 0.7, 1, 0, 0, 0, 0, 1, 2200, 0, 0, 1, 1, 1, 1, 0.05, 0.05, 0.5, 0.1, 0, 0, 0, 0, 0 ]);

    //phaseSynth:
    // perhaps vary the reverb sends
    phaseSynth.mixer.setSendVol(0, -18);
    phaseSynth.mixer.setSendVol(1, -16);
    phaseSynth.setAllParameters([ 1, 1, 0, 1, 0.5, 0, 0.05, 0.05, 0.2, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 2, 0.5, 0.5, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, -0.8, 0.15, 1, 1, 1, 1, 0, 0, 0, 0, 3, 0.8, 0.25, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 1, 1, 1, 1, 0.05, 0, 0.05, false, 1, 1, 1.3, 0.6, 0, 0, -0.7, 0.5, 1, 1500, 0.1, 0, 1, 1, 1, 1, 0.05, 0.01, 0, 0.05, 0, 0, 0, 0, 0 ]);
  }

  prMakePatternParameters {
    // add sequences parameters:
    //chordsSynth:
    chordsSynth.addKey(\cortez, \root, 6);
    chordsSynth.addKey(\cortez, \note, Pseq([[0, 7], [0, 3], [-2, 5], [-2, 2], [5, 0], [-7, 0], [-5, 2]], inf));
    chordsSynth.addKey(\cortez, \legato, 1);
    chordsSynth.addKey(\cortez, \octave, 4);
    chordsSynth.addKey(\cortez, \dur, Pseq([3, 1, 3, 2, 3, 1.5, 3], inf));
    chordsSynth.addKey(\cortez, \amp, 1);
    chordsSynth.addKey(\cortez, \lfo1Freq, Pseq([15/3, 2.5, 9/3, 4/2, 5.5, 3, 3], inf));
    chordsSynth.addKey(\cortez, \osc1Waveform, Pbrown(0.8, 1.3, 0.05, inf));
    chordsSynth.addKey(\cortez, \filterDrive, Pbrown(3, 12, 0.125));
    chordsSynth.addKey(\cortez, \releaseTime, 0.1);

    //washSynth:
    washSynth.addKey(\cortezHi, \root, 6);
    washSynth.addKey(\cortezHi, \note, Pseq([Pseq([2, 0, 5, 0, 2], 7), Pseq([2, 0, 5, 0, 2, 3], 7)], inf));
    washSynth.addKey(\cortezHi, \dur, Pseq([0.15], inf));
    washSynth.addKey(\cortezHi, \legato, 1);
    washSynth.addKey(\cortezHi, \amp, 0.3);
    washSynth.addKey(\cortezHi, \pan, -0.3);

    washSynth.addKey(\cortezHi2, \pan, 0.3);
    washSynth.addKey(\cortezHi2, \root, 6);
    washSynth.addKey(\cortezHi2, \note, Pseq([2, 0, 5, 0, 2, -2], inf));
    washSynth.addKey(\cortezHi2, \dur, Pseq([0.16], inf));
    washSynth.addKey(\cortezHi2, \legato, 1);
    washSynth.addKey(\cortezHi2, \amp, 0.3);

    // phaseSynth:
    phaseSynth.addKey(\cortezPhase, \root, 6);
    phaseSynth.addKey(\cortezPhase, \note, Pseq([0], inf));
    phaseSynth.addKey(\cortezPhase, \dur, 3/20);
    phaseSynth.addKey(\cortezPhase, \legato, Pbrown(0.1, 0.4, 0.125, inf));
    phaseSynth.addKey(\cortezPhase, \octave, 5);
    phaseSynth.addKey(\cortezPhase, \amp, 0.3);
    phaseSynth.addKey(\cortezPhase, \pan, Pbrown(0, 1, 0.125, inf));


    phaseSynth.addKey(\cortezPhase2, \root, 6);
    phaseSynth.addKey(\cortezPhase2, \note, Pseq([2], inf));
    phaseSynth.addKey(\cortezPhase2, \dur, 3/20.1);
    phaseSynth.addKey(\cortezPhase2, \legato, Pbrown(0.1, 0.4, 0.125, inf));
    phaseSynth.addKey(\cortezPhase2, \amp, 0.3);
    phaseSynth.addKey(\cortezPhase2, \pan, Pbrown(-1, 0, 0.125, inf));


    phaseSynth.addKey(\cortezPhase3, \root, 6);
    phaseSynth.addKey(\cortezPhase3, \note, Pseq([3], inf));
    phaseSynth.addKey(\cortezPhase3, \dur, 3/20.2);
    phaseSynth.addKey(\cortezPhase3, \legato, Pbrown(0.1, 0.4, 0.125, inf));
    phaseSynth.addKey(\cortezPhase3, \amp, 0.3);
    phaseSynth.addKey(\cortezPhase3, \pan, Pbrown(-0.2, 0.2, 0.025, inf));


    phaseSynth.addKey(\cortezPhase4, \root, 6);
    phaseSynth.addKey(\cortezPhase4, \note, Pseq([5], inf));
    phaseSynth.addKey(\cortezPhase4, \dur, 3/20.3);
    phaseSynth.addKey(\cortezPhase4, \legato, Pbrown(0.1, 0.4, 0.125, inf));
    phaseSynth.addKey(\cortezPhase4, \amp, 0.3);
    phaseSynth.addKey(\cortezPhase4, \pan, Pbrown(-0.3, 1, 0.125, inf));


    phaseSynth.addKey(\cortezPhase5, \root, 6);
    phaseSynth.addKey(\cortezPhase5, \note, Pseq([7], inf));
    phaseSynth.addKey(\cortezPhase5, \dur, 3/20.4);
    phaseSynth.addKey(\cortezPhase5, \legato, Pbrown(0.1, 0.4, 0.125, inf));
    phaseSynth.addKey(\cortezPhase5, \amp, 0.3);
    phaseSynth.addKey(\cortezPhase5, \pan, Pbrown(-1, 0.3, 0.125, inf));

  }

  prMakeTrumpetSequence {
    trumpetSeq.addFunction(0, { ~trumpetShift.makeSynthOneShot('seq0', -12) });
    trumpetSeq.addFunction(1, { ~trumpetShift.makeSynthOneShot('seq1', -7) });
    trumpetSeq.addFunction(2, { ~trumpetShift.makeSynthOneShot('seq2', -5) });
    trumpetSeq.addFunction(3, { ~trumpetShift.makeSynthOneShot('seq3', 2) });
    trumpetSeq.addFunction(4, { ~trumpetShift.makeSynthOneShot('seq4', 0) });

    trumpetSeq.addFunction(5, { ~trumpetShift.makeSynthOneShot('seq0', -12) });
    trumpetSeq.addFunction(6, { ~trumpetShift.makeSynthOneShot('seq1', -7) });
    trumpetSeq.addFunction(7, { ~trumpetShift.makeSynthOneShot('seq2', -5) });
    trumpetSeq.addFunction(8, { ~trumpetShift.makeSynthOneShot('seq3', 2) });
    trumpetSeq.addFunction(9, { ~trumpetShift.makeSynthOneShot('seq4', 0) });

    trumpetSeq.addFunction(10, { ~trumpetShift.makeSynthOneShot('seq0', -12) });
    trumpetSeq.addFunction(11, { ~trumpetShift.makeSynthOneShot('seq1', -7) });
    trumpetSeq.addFunction(12, { ~trumpetShift.makeSynthOneShot('seq2', -5) });
    trumpetSeq.addFunction(13, { ~trumpetShift.makeSynthOneShot('seq3', 2) });
    trumpetSeq.addFunction(14, { ~trumpetShift.makeSynthOneShot('seq4', 0) });

    trumpetSeq.addFunction(15, { ~trumpetShift.makeSynthOneShot('seq5', -12) });
    trumpetSeq.addFunction(16, { ~trumpetShift.makeSynthOneShot('seq6', -7) });
    trumpetSeq.addFunction(17, { ~trumpetShift.makeSynthOneShot('seq7', -5) });
    trumpetSeq.addFunction(18, { ~trumpetShift.makeSynthOneShot('seq8', -2) });
    trumpetSeq.addFunction(19, { ~trumpetShift.makeSynthOneShot('seq9', 0) });
    trumpetSeq.addFunction(20, { ~trumpetShift.makeSynthOneShot('seq10', 2) });

    trumpetSeq.addFunction(21, { ~trumpetShift.makeSynthOneShot('seq5', -12) });
    trumpetSeq.addFunction(22, { ~trumpetShift.makeSynthOneShot('seq6', -7) });
    trumpetSeq.addFunction(23, { ~trumpetShift.makeSynthOneShot('seq7', -5) });
    trumpetSeq.addFunction(24, { ~trumpetShift.makeSynthOneShot('seq8', -2) });
    trumpetSeq.addFunction(25, { ~trumpetShift.makeSynthOneShot('seq9', 0) });
    trumpetSeq.addFunction(26, { ~trumpetShift.makeSynthOneShot('seq10', 2) });
  }

  free {
    chordsSynth.free;
    washSynth.free;
    phaseSynth.free;

    modularShift.free;
    trumpetShift.free;
    trumpetSeq.free;

    bells.free;
    bass.free;

    trumpetInput.free;
    shiftInput.free;

    clock.free;
  }
}