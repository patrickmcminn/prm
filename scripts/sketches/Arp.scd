x = AudioSystem.new(2);
y = Subtractive.new(x.submixA, x.reverb.inBus, x.granulator.inBus);

y.mixer.setSendVol(0, -9);
y.mixer.setSendVol(1, -12);

~phraselet1 = Pseq([[0, 4], -5, -3, [-7, 5]], 3);
~phraselet2 = Pseq([[7, 5], -5, -3, [-7, 5]], 1);

y.makeSequence('sad');
y.sequencerClock.tempo = 60/60;

y.addKey(\sad, \root, -1);
y.addKey(\sad, \note, Pseq([~phraselet1, ~phraselet2], inf));
y.addKey(\sad, \amp, Pseq([Pwhite(0.65, 0.75), Pwhite(0.8, 1), Pwhite(0.76, 0.95), Pwhite(0.5, 0.6)], inf));
y.addKey(\sad, \dur, Pseq([1, 1, 1, 1], inf));
y.addKey(\sad, \legato, 1);
y.addKey(\sad, \attackTime, Pwhite(0.2, 0.45));
y.addKey(\sad, \filterCutoff, Pbrown(900, 1200, 50));
y.addKey(\sad, \filterRes, Pwhite(0.4, 0.9));


y.playSequence(\sad);

y.setOsc1Waveform(1.3);
y.setOsc2Octave(4);
y.setOsc2Waveform('tri');
y.setOsc2Vol(-12);

y.setAttackTime(0.35);
y.setReleaseTime(0.55);
y.setSustainLevel(1);
y.setFilterEnvAttackRatio(0);
y.setFilterEnvAttackTime(0.35);
y.setFilterEnvSustainRatio(0.8);
y.setFilterEnvDecayTime(0.2);
