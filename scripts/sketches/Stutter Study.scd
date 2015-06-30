x = Subtractive.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus);

x.setFilterType('lowPass');

x.setOsc2Vol(-inf);
x.setOsc1Waveform('rect');
x.setOsc1PulseWidth(0.5);
x.setLFO1Waveform('rect');
x.setLFO1Freq(5);
x.setOsc1PulseWidthLFO1Bottom(-0.3);
x.setOsc1PulseWidthLFO1Top(0.3);
x.setSustainLevel(0);
x.setDecayTime(0.1);
x.setFilterCutoff(5500);
x.setFilterEnvAttackRatio(0);
x.setFilterEnvAttackTime(0.05);
x.setFilterCutoffLFO2BottomRatio(0.3);
x.setFilterCutoffLFO2TopRatio(1.4);
x.setLFO2Waveform('sampleAndHold');
x.setLFO2Freq(15);
x.setOsc1FreqEnvStartRatio(1);
x.setOsc1FreqEnvEndRatio(1);
x.setOsc1FreqEnvTime(0.05);

x.mixer.setSendVol(0, -9);

(
x.makeSequence('stutter');
x.makeSequence('stutter2');
)

x.addKey(\stutter, \dur, Pseq([Pwhite(0.07, 0.15, 15), 0.15], inf));
x.addKey(\stutter, \note, Pseq([Pseq([13], 15), 11], inf));
x.addKey(\stutter, \legato, 1);
x.addKey(\stutter, \amp, Pwhite(0.4, 0.65));

x.addKey(\stutter2, \dur, Pseq([Pwhite(0.07, 0.15, 15), 0.1, 0.05], inf));
x.addKey(\stutter2, \note, Pseq([Pseq([15], 11), 13, 25], inf));
x.addKey(\stutter2, \legato, 1);
x.addKey(\stutter2, \amp, 0.6);
x.addKey(\stutter2, \decayTime, 0.05);
x.addKey(\stutter2, \pan, Pwhite(-0.5, 0.5));

(
x.playSequence('stutter');
x.playSequence('stutter2');
)


x.stopSequence('stutter2');


~prm.submixerA.mixer.tglMute;
~prm.reverb.mixer.tglMute;
x.mixer.tglMute;