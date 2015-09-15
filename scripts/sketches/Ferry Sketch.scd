x = SaturSynth.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus);
y = Subtractive.new(~prm.submixB, ~prm.reverb.inBus, ~prm.granulator.inBus);

y.makeSequence(\ship);
y.setSequencerClockTempo(96);
y.addKey(\ship, \root, 1);
y.addKey(\ship, \dur, Pseq([0.25], inf));
y.addKey(\ship, \note, Pseq([[0, 7], 2, 3, [2, 7]], inf));
y.addKey(\ship, \octave, Prand([4, 5, 6], inf));
y.addKey(\ship, \legato, 1);
y.addKey(\ship, \filterCutoff, Pwhite(550, 1500, inf));
y.addKey(\ship, \amp, 0.1);
y.addKey(\ship, \pan, Pbrown(-1, 1));
y.addKey(\ship, \lfo2Freq, Pbrown(7, 44, 1));
y.playSequence(\ship);

y.setFilterCutoffLFO2BottomRatio(1);
y.setFilterCutoffLFO2TopRatio(3);
y.setLFO2Freq(11);
y.setLFO2Waveform('noise');
y.setOsc2AmpLFO2Bottom(1);
y.setOsc2AmpLFO2Top(0.3);
y.setFilterDrive(12);

x.makeSequence(\ship);
x.setSequencerClockTempo(96);
x.addKey(\ship, \root, 1);
x.addKey(\ship, \dur, Pseq([0.25], inf));
x.addKey(\ship, \note, Pseq([[0, 7], 2, 3, [2, 7]], inf));
x.addKey(\ship, \octave, Prand([4, 5, 6], inf));
x.addKey(\ship, \legato, 1);
x.addKey(\ship, \filterCutoff, Pwhite(170, 880, inf));
x.addKey(\ship, \amp, 0.3);
x.addKey(\ship, \pan, Pbrown(-1, 1));

x.playSequence(\ship);


x.makeSequence(\ground);
x.addKey(\ground, \root, 1);
x.addKey(\ground, \dur, 4);
x.addKey(\ground, \legato, 1);
x.addKey(\ground, \octave, [3, 4]);
x.addKey(\ground, \note, Pseq([Pseq([7, 2, 0], 2), Pseq([7, [10, -2], 2, 0], 1)], inf));
x.addKey(\ground, \filterCutoff, 440);

x.playSequence(\ground);
s.meter;

x.stopSequence(\ground);
x.stopSequence(\ship);
y.stopSequence(\ship);

~prm.submixerA.mixer.setVol(-6);
~prm.submixerA.setMix(0);
~prm.submixerA.mixer.tglMute;

~prm.submixerB.setMix(1);
~prm.submixerB.loop
~prm.submixerB.setLoopRate(0.1);
~prm.submixerB.setLoopDivison(1);
~prm.submixerB.setLoopPosition(0.84);

~prm.submixerB.mixer.setSendVol(0, -70);
~prm.submixerB.mixer.setSendVol(1, -70);

s.makeWindow;