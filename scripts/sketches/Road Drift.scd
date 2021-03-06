x = AudioSystem.new(2);
y = Subtractive.new(x.submixA, x.reverb.inBus, x.granulator.inBus);

y.mixer.setSendVol(0, -16);
y.mixer.setSendVol(1, -70);


y.setFilterCutoff(950);
y.setLFO2Freq(0.3);
y.setLFO2Waveform('tri');
y.setFilterCutoffLFO2BottomRatio(0.8);
y.setFilterCutoffLFO2TopRatio(1.2);
y.setOsc2Vol(-12);
y.setOsc2Octave(2);
y.setOsc1Octave(2);
y.setOsc2PulseWidth(0.9);
y.setOsc1SubVol(-70);
y.setOsc2PulseWidthLFO2Bottom(-1);
y.setOsc2PulseWidthLFO2Top(1);


y.printAllParameters;

y.savePreset('drift', [ 1, 1, 0, 1, 0.5, 0, 0.05, 0.05, 0.3, 0.5, 0, 0.5, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 2, 0.5, 0.5, 0.00031622776601684, 0.5, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, -1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 3, 0.9, 0.25118864315096, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 1, 1, 1, 1, 0.05, 0, 0.05, false, 1, 1, 0.8, 1.2, 0, 0, 0, 0, 1, 950, 0, 0, 1, 1, 1, 1, 0.05, 0.05, 1, 0.05, 0, 0, 0, 0, 0 ]);
y.loadPreset('drift');

~chord1 = [0, 4, 7];
~chord2 = [4, 9, 11];
~chord3 = [5, 9, 12, 16];
~chord4 = [-2, 2, 5, 9];


y.makeSequence('grift');
y.sequencerClock.tempo = 1.75;


y.addKey(\grift, \dur, Pseq([Pseq([0.25], 16), Pseq([0.1875], 24), Pseq([0.25], 16), Pseq([1/3], 16)], inf));
y.addKey(\grift, \root, 1);
y.addKey(\grift, \note, Pseq([Pseq([~chord1], 16), Pseq([~chord2], 24), Pseq([~chord3], 16), Pseq([[9, 12, 14, 19]], 16)], inf));
y.addKey(\grift, \legato, 0.9);
y.addKey(\grift, \amp, 1);



y.playSequence(\grift);
y.stopSequence(\grift);




// section 1: ~chord 1, ~chord2, ~chord 3

// final section: ~chord 4 added
s.meter;

z = MidBells.new(x.submixB,x.reverb.inBus, x.granulator.inBus);
z.setAttackTime(0.4);
z.playNote(61.midicps, 6);

z.mixer.setSendVol(1, 0);

x.systemMixer.setVol(0, 12);
s.meter;

x.granulator.granulator.setTrigRate(1);
x.granulator.granulator.setTrigRate(66);
x.granulator.setGrainDur(5, 7);