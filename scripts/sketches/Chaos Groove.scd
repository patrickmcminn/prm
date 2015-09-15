x = Subtractive.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus);

x.makeSequence(\dist);

x.addKey(\dist, \root, 1);
x.addKey(\dist, \note, Pseq([[2, 9, 10], 0, [7, 14], [4, 12], [-12, 12], \r], inf));
x.addKey(\dist, \octave, Pseq([4, 4, 5, 4, 5, 4, 4], inf));
x.addKey(\dist, \dur, Pseq([0.25, 0.25, 0.25, 0.25, 0.5], inf));
x.addKey(\dist, \legato, 1);
x.addKey(\dist, \osc1FreqEnvStartRatio, Pseq([Pwhite(0.3, 2), 1, 1, 1], inf));
x.addKey(\dist, \osc1FreqEnvTime, Pwhite(0.1, 0.15));
x.addKey(\dist, \osc2FreqEnvStartRatio, Pseq([Pwhite(0.3, 0.9), 1, 1, 1], inf));
x.addKey(\dist, \osc2FreqEnvTime, Pwhite(0.05, 0.2));
x.addKey(\dist, \amp, 0.3);
x.addKey(\dist, \noiseOscAmp, Pwhite(0, 0.45));
x.addKey(\dist, \noiseOscCutoff, Pwhite(400, 3500));
x.addKey(\dist, \filterCutoff, Pwhite(1500, 2500));


x.playSequence(\dist);

x.setFilterDrive(1000);

x.setOsc2FreqEnvStartRatio(0.2);
x.setOsc2FreqEnvEndRatio(1);
x.setOsc2FreqEnvTime(1);

x.setOsc1FreqEnvStartRatio(5);
x.setOsc1FreqEnvTime(0.3);

x.setOsc1FreqLFO2BottomRatio(1);

x.setOsc2FreqLFO1BottomRatio(0.5);
x.setOsc2FreqLFO1TopRatio(2);

x.setLFO1Waveform('rect');
x.setLFO1Freq(7);

x.setOsc2Vol(-6);
x.setOsc2Octave(3);
x.setOsc2Waveform('saw')

x.setOsc1Vol(0);


x.setOsc1FreqLFO2BottomRatio(1);
x.setOsc1FreqLFO2TopRatio(1);

x.setLFO2Waveform('sampleAndHold');
x.setFilterCutoffLFO2BottomRatio(0.4);
x.setFilterCutoffLFO2TopRatio(2);
x.setFilterResLFO2Bottom(0);
x.setFilterResLFO2Top(0);
x.setLFO2Freq(7);

x.setReleaseTime(0.05);

x.setOsc1SubVol(-12);
x.setOsc2SubVol(-18);

x.setFilterEnvAttackRatio(4);
x.setFilterEnvAttackTime(0.3);
x.setFilterEnvReleaseRatio(3);
x.setFilterEnvReleaseTime(0.4);


x.mixer.tglMute;
x.mixer.setSendVol(0, -18);
x.mixer.setSendVol(1, -6);
x.mixer.setSendPre;
x.mixer.setVol(0);

~prm.granulator.setGrainEnvelope('expodec');
~prm.granulator.setGrainDur(0.2, 0.2);
~prm.granulator.setTrigRate(8);
~prm.granulator.granulator.setSync(1);
~prm.granulator.setPan(0, 0);
~prm.granulator.setPos(0, 1);
~prm.granulator.setRate(0.5, 0.5);

~prm.submixerA.setWaveLossAmount(0);
~prm.submixerA.setWaveLossMode(1);

x.stopSequence(\dist);

s.makeWindow;