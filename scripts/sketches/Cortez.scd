// implied chords:
// F# m - EM - Bm7 (add 9 or 11?) - C#m
// but VERY open voicings right now -- could be other chords too
// borrowed the initial idea from Cortez the Killer by Neil Young

//x = AudioSystem.new(2);
(
r {

  // clock:
  z = TempoClock.new;
  z.tempo = 60/60;

  // song parameters:
  ~prm.granulator.setGrainEnvelope('rexpodec');
  ~prm.granulator.setGrainDur(0.05, 0.08);
  ~prm.granulator.setTrigRate(60);

  // synths:
  y = Subtractive.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus);
  b = Subtractive.new(~prm.submixB, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus);
  c = Subtractive.new(~prm.submixC, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus);

  ~shift = PitchEnvGenerator.newMono(~prm.submixB, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus);
  while({ try { ~shift.isLoaded } != true }, { 0.001.wait; });
  d = IM_HardwareIn.new(2, ~shift.inBus, nil, \addToHead);
  while({ try { d.isLoaded } != true }, { 0.001.wait; });

  ~trumpetShift = FreezeEnvGenerator.newMono(~prm.submixA,
    ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus);
  while({ try { ~trumpetShift.isLoaded } != true }, { 0.001.wait; });
  e = IM_HardwareIn.new(0, ~trumpetShift.inBus);
  while({ try { e.isLoaded } != true }, { 0.001.wait; });

  ~trumpetShift.envGenerator.setSustainTime(0.1);
  ~trumpetShift.envGenerator.setReleaseTime(0.09);

  ~tptSeq = FunctionSequencer.new(27, z);
  while({ try { ~tptSeq.isLoaded } !=true }, { 0.001.wait; });

  ~tptSeq.addFunction(0, { ~trumpetShift.makeSynthOneShot('seq0', -12) });
  ~tptSeq.addFunction(1, { ~trumpetShift.makeSynthOneShot('seq1', -7) });
  ~tptSeq.addFunction(2, { ~trumpetShift.makeSynthOneShot('seq2', -5) });
  ~tptSeq.addFunction(3, { ~trumpetShift.makeSynthOneShot('seq3', 2) });
  ~tptSeq.addFunction(4, { ~trumpetShift.makeSynthOneShot('seq4', 0) });

  ~tptSeq.addFunction(5, { ~trumpetShift.makeSynthOneShot('seq0', -12) });
  ~tptSeq.addFunction(6, { ~trumpetShift.makeSynthOneShot('seq1', -7) });
  ~tptSeq.addFunction(7, { ~trumpetShift.makeSynthOneShot('seq2', -5) });
  ~tptSeq.addFunction(8, { ~trumpetShift.makeSynthOneShot('seq3', 2) });
  ~tptSeq.addFunction(9, { ~trumpetShift.makeSynthOneShot('seq4', 0) });

  ~tptSeq.addFunction(10, { ~trumpetShift.makeSynthOneShot('seq0', -12) });
  ~tptSeq.addFunction(11, { ~trumpetShift.makeSynthOneShot('seq1', -7) });
  ~tptSeq.addFunction(12, { ~trumpetShift.makeSynthOneShot('seq2', -5) });
  ~tptSeq.addFunction(13, { ~trumpetShift.makeSynthOneShot('seq3', 2) });
  ~tptSeq.addFunction(14, { ~trumpetShift.makeSynthOneShot('seq4', 0) });

  ~tptSeq.addFunction(15, { ~trumpetShift.makeSynthOneShot('seq5', -12) });
  ~tptSeq.addFunction(16, { ~trumpetShift.makeSynthOneShot('seq6', -7) });
  ~tptSeq.addFunction(17, { ~trumpetShift.makeSynthOneShot('seq7', -5) });
  ~tptSeq.addFunction(18, { ~trumpetShift.makeSynthOneShot('seq8', -2) });
  ~tptSeq.addFunction(19, { ~trumpetShift.makeSynthOneShot('seq9', 0) });
  ~tptSeq.addFunction(20, { ~trumpetShift.makeSynthOneShot('seq10', 2) });

  ~tptSeq.addFunction(21, { ~trumpetShift.makeSynthOneShot('seq5', -12) });
  ~tptSeq.addFunction(22, { ~trumpetShift.makeSynthOneShot('seq6', -7) });
  ~tptSeq.addFunction(23, { ~trumpetShift.makeSynthOneShot('seq7', -5) });
  ~tptSeq.addFunction(24, { ~trumpetShift.makeSynthOneShot('seq8', -2) });
  ~tptSeq.addFunction(25, { ~trumpetShift.makeSynthOneShot('seq9', 0) });
  ~tptSeq.addFunction(26, { ~trumpetShift.makeSynthOneShot('seq10', 2) });


}.play;
);

(
r {

  // set parameters:

  // y:
  y.mixer.setSendVol(0, -24);
  y.setAllParameters([ 1, 1, 1, 5, 0.5, 0, 0.05, 0.05, 0.6, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0.5, 1, 1, 1, 0, 0, 0.5, 0, 1.2, 0.5, 0.50118723362727, 0, 1, -12, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0.5, 1, 1, 1, 0, 0, 0, 0, 1, 0.5, 0.25, 0.25118864315096, 1, 0, 1, 1, 1, 1, 1, 1, 1.122018454302e-50, 1000, 1, 1, 0.7, 1, 0.25, 0.5, 0.05, false, 0.5, 1, 0.6, 1.4, 0, 0, 0, 0, 10, 850, 0, 0, 1, 1, 1, 1, 0.05, 0.5, 0.7, 0.1, 0, 0, 0, 0, 0 ]);

  // b:
  b.mixer.setSendVol(0, -9);
  b.mixer.setSendVol(1, -99);
  b.mixer.setSendPre;
  b.mixer.setVol(-6);
  b.setAllParameters([ 1, 1, 0, 1, 0.5, 0, 0.05, 0.05, 0.1, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0.4, 1, 0.5, 0.5, 0, 0.5, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0.5, 0.15848931924611, 1.122018454302e-50, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 0, 1, 0.5, 0, 0.1, 2.5, 3, false, 1, 1, 0.7, 1, 0, 0, 0, 0, 1, 2200, 0, 0, 1, 1, 1, 1, 0.05, 0.05, 0.5, 0.1, 0, 0, 0, 0, 0 ]);

  //c:
  // perhaps vary the reverb sends
  c.mixer.setSendVol(0, -18);
  c.mixer.setSendVol(1, -16);
  c.setAllParameters([ 1, 1, 0, 1, 0.5, 0, 0.05, 0.05, 0.2, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 2, 0.5, 0.5, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, -0.8, 0.15, 1, 1, 1, 1, 0, 0, 0, 0, 3, 0.8, 0.25, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 1, 1, 1, 1, 0.05, 0, 0.05, false, 1, 1, 1.3, 0.6, 0, 0, -0.7, 0.5, 1, 1500, 0.1, 0, 1, 1, 1, 1, 0.05, 0.01, 0, 0.05, 0, 0, 0, 0, 0 ]);


  // make sequences:
  y.makeSequence('cortez');
  b.makeSequence(\cortezHi);
  b.makeSequence(\cortezHi2);
  c.makeSequence(\cortezPhase);
  c.makeSequence(\cortezPhase2);
  c.makeSequence(\cortezPhase3);
  c.makeSequence(\cortezPhase4);
  c.makeSequence(\cortezPhase5);




  s.sync;


  // add sequences parameters:
  //y:
  y.addKey(\cortez, \root, 6);
  y.addKey(\cortez, \note, Pseq([[0, 7], [0, 3], [-2, 5], [-2, 2], [5, 0], [-7, 0], [-5, 2]], inf));
  y.addKey(\cortez, \legato, 1);
  y.addKey(\cortez, \octave, 4);
  y.addKey(\cortez, \dur, Pseq([3, 1, 3, 2, 3, 1.5, 3], inf));
  y.addKey(\cortez, \amp, 1);
  y.addKey(\cortez, \lfo1Freq, Pseq([15/3, 2.5, 9/3, 4/2, 5.5, 3, 3], inf));
  y.addKey(\cortez, \osc1Waveform, Pbrown(0.8, 1.3, 0.05, inf));
  y.addKey(\cortez, \filterDrive, Pbrown(3, 12, 0.125));

  //b:
  b.addKey(\cortezHi, \root, 6);
  b.addKey(\cortezHi, \note, Pseq([Pseq([2, 0, 5, 0, 2], 7), Pseq([2, 0, 5, 0, 2, 3], 7)], inf));
  b.addKey(\cortezHi, \dur, Pseq([0.15], inf));
  b.addKey(\cortezHi, \legato, 1);
  b.addKey(\cortezHi, \amp, 0.3);
  b.addKey(\cortezHi, \pan, -0.3);

  b.addKey(\cortezHi2, \pan, 0.3);
  b.addKey(\cortezHi2, \root, 6);
  b.addKey(\cortezHi2, \note, Pseq([2, 0, 5, 0, 2, -2], inf));
  b.addKey(\cortezHi2, \dur, Pseq([0.16], inf));
  b.addKey(\cortezHi2, \legato, 1);
  b.addKey(\cortezHi2, \amp, 0.3);

  // c:
  c.addKey(\cortezPhase, \root, 6);
  c.addKey(\cortezPhase, \note, Pseq([0], inf));
  c.addKey(\cortezPhase, \dur, 3/20);
  c.addKey(\cortezPhase, \legato, Pbrown(0.1, 0.4, 0.125, inf));
  c.addKey(\cortezPhase, \octave, 5);
  c.addKey(\cortezPhase, \amp, 0.3);
  c.addKey(\cortezPhase, \pan, Pbrown(0, 1, 0.125, inf));


  c.addKey(\cortezPhase2, \root, 6);
  c.addKey(\cortezPhase2, \note, Pseq([2], inf));
  c.addKey(\cortezPhase2, \dur, 3/20.1);
  c.addKey(\cortezPhase2, \legato, Pbrown(0.1, 0.4, 0.125, inf));
  c.addKey(\cortezPhase2, \amp, 0.3);
  c.addKey(\cortezPhase2, \pan, Pbrown(-1, 0, 0.125, inf));


  c.addKey(\cortezPhase3, \root, 6);
  c.addKey(\cortezPhase3, \note, Pseq([3], inf));
  c.addKey(\cortezPhase3, \dur, 3/20.2);
  c.addKey(\cortezPhase3, \legato, Pbrown(0.1, 0.4, 0.125, inf));
  c.addKey(\cortezPhase3, \amp, 0.3);
  c.addKey(\cortezPhase3, \pan, Pbrown(-0.2, 0.2, 0.025, inf));


  c.addKey(\cortezPhase4, \root, 6);
  c.addKey(\cortezPhase4, \note, Pseq([5], inf));
  c.addKey(\cortezPhase4, \dur, 3/20.3);
  c.addKey(\cortezPhase4, \legato, Pbrown(0.1, 0.4, 0.125, inf));
  c.addKey(\cortezPhase4, \amp, 0.3);
  c.addKey(\cortezPhase4, \pan, Pbrown(-0.3, 1, 0.125, inf));


  c.addKey(\cortezPhase5, \root, 6);
  c.addKey(\cortezPhase5, \note, Pseq([7], inf));
  c.addKey(\cortezPhase5, \dur, 3/20.4);
  c.addKey(\cortezPhase5, \legato, Pbrown(0.1, 0.4, 0.125, inf));
  c.addKey(\cortezPhase5, \amp, 0.3);
  c.addKey(\cortezPhase5, \pan, Pbrown(-1, 0.3, 0.125, inf));



}.play;
);













b.printAllParameters;






c.printAllParameters;



y.setOsc1Waveform(1.2);
y.setOsc2Waveform('tri');
y.setOsc2SubVol(-12);
y.setOsc2Octave(3);
y.setOsc1Vol(-6);
y.setFilterCutoff(850);
y.setAttackTime(0.05);
y.setDecayTime(0.5);
y.setSustainLevel(0.7);
y.setReleaseTime(0.1);
y.setFilterEnvAttackRatio(1);
y.setFilterEnvAttackTime(0.25);
y.setFilterEnvDecayTime(0.5);
y.setFilterEnvSustainRatio(0.7);
y.setFilterCutoffLFO2BottomRatio(0.6);
y.setFilterCutoffLFO2TopRatio(1.4);
y.setLFO2Freq(0.6);
y.setLFO2Waveform('noise');
y.setOsc1WaveformLFO2Bottom(0.5);
y.setFilterDrive(10);
y.setFilterCutoffLFO1BottomRatio(0.5);
y.setFilterCutoffLFO1TopRatio(1);
y.setOsc1AmpLFO1Top(1);
y.setOsc1AmpLFO1Bottom(0.5);
y.setOsc2AmpLFO1Top(1);
y.setOsc2AmpLFO1Bottom(0.5);
y.setNoiseOscAmpLFO1Bottom(1);
y.setNoiseOscAmpLFO1Top(0);
y.setNoiseOscVol(-999);
y.setNoiseOscCutoff(1000);
y.setLFO1Freq(5);
y.setLFO1Waveform('saw');
y.setFilterType('lowPass');
y.setOsc2DetuneCents(-12);




b.setFilterCutoffLFO2BottomRatio(0.7);
b.setFilterCutoffLFO2TopRatio(1);
b.setLFO2Waveform('noise');
b.setFilterCutoff(2200);
b.setFilterType(\lowPass);
b.setAttackTime(0.05);
b.setReleaseTime(0.1);
b.setSustainLevel(0.5);
b.setDecayTime(0.05);
b.setFilterEnvAttackRatio(0);
b.setFilterEnvAttackTime(0.1);
b.setFilterEnvSustainRatio(0.5);
b.setFilterEnvDecayTime(2.5);
b.setFilterEnvReleaseRatio(0);
b.setFilterEnvReleaseTime(3);
b.setOsc1WaveformLFO2Bottom(0);
b.setOsc1Waveform('tri');
b.setOsc1WaveformLFO2Top(0.4);
b.setLFO2Freq(0.1);
b.setOsc2Vol(-16);
b.setOsc2Octave(2);
b.setOsc2SubVol(-999);
b.setOsc2Waveform('tri');


c.setDecayTime(0.01);
c.setSustainLevel(0);
c.setAttackTime(0.05);
c.setFilterCutoff(1500);
c.setFilterRes(0.1);
c.setFilterResLFO2Bottom(-0.7);
c.setFilterResLFO2Top(0.5);
c.setLFO2Freq(0.2);
c.setLFO2Waveform('noise');
c.setFilterCutoffLFO2BottomRatio(1.3);
c.setFilterCutoffLFO2TopRatio(0.6);
c.setOsc2Octave(3);
c.setOsc2PulseWidth(0.8);
c.setOsc2PulseWidthLFO2Bottom(-0.8);
c.setOsc2PulseWidthLFO2Top(0.15);
c.setFilterDrive(1);

y.stopSequence(\cortez);
c.playSequence(\cortezPhase);
c.playSequence(\cortezPhase2);
c.playSequence(\cortezPhase3);
c.playSequence(\cortezPhase4);
c.playSequence(\cortezPhase5);

c.stopSequence(\cortezPhase);
c.stopSequence(\cortezPhase2);
c.stopSequence(\cortezPhase3);
c.stopSequence(\cortezPhase4);
c.stopSequence(\cortezPhase5);

