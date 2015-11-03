a = Distortion.newStereo(0);
x = Wash.newStereo(a.inBus);
y = Subtractive.new(x.inBus);

y.playNote(220);
y.playNote(330);

y.releaseNote(330);
x.setMix(0);


a.setDistortionGain(100);
a.postEQ.setLowPassCutoff(1200);
a.postEQ.setLowGain(-12);
a.postEQ.setLowFreq(500);


y.playSequence('yes');

y.makeSequence('yes');
y.addKey(\yes, \root, -1);
y.addKey(\yes, \dur, 1);
y.addKey(\yes, \note, Pseq([[0, 2, 7, 12]], inf));
y.addKey(\yes, \octave, Pxrand([5, 5, 5, 6], inf));
y.addKey(\yes, \legato, 1);
y.addKey(\yes, \amp, 1);

y.setFilterCutoffLFO2BottomRatio(0.2);
y.setLFO2Waveform('sampleAndHold');
y.setLFO2Freq(15);
y.setReleaseTime(0.4);
y.playSequence('yes');
y.setOsc1Waveform('rect');
y.setOsc1PulseWidth(0.2);
y.setOsc1PulseWidthLFO2Bottom(-1);
y.setOsc1PulseWidthLFO2Top(1);
y.stopSequence('yes');
y.setOsc2Vol(-70);
y.setOsc1AmpLFO2Bottom(1);
x.setFeedbackCoefficient(0.2);
x.setDelayCoefficient(0.9);


z = SaturSynth.new(x.inBus);

z.playNote(110, 0);
z.playNote(220);
z.playNote(330);
z.releaseNote(330);
z.setFilterCutoff(200);
z.releaseNote(110);

