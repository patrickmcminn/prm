x = AudioSystem.new(2);

y = Subtractive.new(x.submixA, x.reverb.inBus, x.granulator.inBus);

y.mixer.setSendVol(0, -18);
y.mixer.setSendVol(1, -70);

y.setFilterCutoff(750);
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
y.addKey(\grift, \amp, 2);



y.playSequence(\grift);
y.stopSequence(\grift);




// section 1: ~chord 1, ~chord2, ~chord 3

// final section: ~chord 4 added
s.meter;

