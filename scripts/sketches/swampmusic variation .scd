~prm = AudioSystem.new(2);
x = Subtractive.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus);

x.mixer.setSendVol(0, -inf);
x.mixer.setSendVol(1, -inf);

x.setOsc1Waveform('rect');
x.setOsc2Waveform('rect');
x.setOsc2Octave(4);


x.setAttackTime(10);
x.setReleaseTime(10);

x.setLFO1Waveform('noise');
x.setLFO1Freq(0.5);
x.setLFO2Freq(0.3);

x.setPanLFO1Bottom(-1);
x.setPanLFO1Top(1);

x.setFilterType('highPass');

x.setFilterCutoff(1500);
x.setFilterCutoffLFO1BottomRatio(0.1);
x.setFilterCutoffLFO1TopRatio(1.5);
x.setLFO2Freq(0.11);
x.setLFO2Waveform('noise');

x.setFilterDrive(50);
x.setFilterRes(0.7);

x.setOsc1AmpLFO1Bottom(0.1);
x.setOsc2AmpLFO1Top(0.2);

x.setOsc1WaveformLFO1Bottom(-2);
x.setOsc1WaveformLFO1Top(2);

x.setOsc2WaveformLFO1Bottom(2);
x.setOsc2WaveformLFO1Top(-2);


x.setOsc1PulseWidthLFO2Bottom(-1);
x.setOsc1PulseWidthLFO2Bottom(1);
x.setOsc1PulseWidthLFO1Bottom(-1);
x.setOsc1PulseWidthLFO1Bottom(1);

x.setOsc2PulseWidthLFO2Bottom(-1);
x.setOsc2PulseWidthLFO2Bottom(1);
x.setOsc2PulseWidthLFO1Bottom(-1);
x.setOsc2PulseWidthLFO1Bottom(1);

//x.setFilterType('lowPass');
x.setFilterType('highPass');


x.makeSequence(\cSharp);
x.makeSequence(\dSharp);
x.makeSequence(\E);
x.makeSequence(\fSharp);
x.makeSequence(\gSharp);
x.makeSequence(\A);
x.makeSequence(\B);
x.makeSequence(\cSharp2);
x.makeSequence(\cSharp0);
x.makeSequence(\c);

(
x.addKey(\cSharp, \note, Pseq([1, \r], inf));
x.addKey(\cSharp, \dur, Pwhite(15, 25));
x.addKey(\cSharp, \amp, Pwhite(0.2, 0.32));
x.addKey(\cSharp, \legato, 1);
x.addKey(\cSharp, \osc2OctaveMul, Prand([0.5, 1, 1, 2, 4], inf));

x.addKey(\dSharp, \note, Pseq([3, \r], inf));
x.addKey(\dSharp, \dur, Pwhite(17, 27));
x.addKey(\dSharp, \amp, Pwhite(0.2, 0.32));
x.addKey(\dSharp, \legato, 1);

x.addKey(\E, \note, Pxrand([4, \r], inf));
x.addKey(\E, \dur, Pwhite(21, 30));
x.addKey(\E, \amp, Pwhite(0.2, 0.32));
x.addKey(\E, \legato, 1);

x.addKey(\fSharp, \note, Prand([6, \r], inf));
x.addKey(\fSharp, \dur, Pwhite(11, 20));
x.addKey(\fSharp, \amp, Pwhite(0.2, 0.32));
x.addKey(\fSharp, \legato, 1);
x.addKey(\fSharp, \osc2DetuneCents, Pwhite(0, 20));

x.addKey(\gSharp, \note, Pxrand([8, \r], inf));
x.addKey(\gSharp, \dur, Pwhite(15, 25));
x.addKey(\gSharp, \amp, Pwhite(0.2, 0.32));
x.addKey(\gSharp, \legato, 1);
x.addKey(\gSharp, \osc1SubAmp, Prand([0, 0, Pwhite(0.1, 0.6)], inf));

x.addKey(\A, \note, Prand([9, \r, \r], inf));
x.addKey(\A, \dur, Pwhite(15, 25));
x.addKey(\A, \amp, Pwhite(0.2, 0.32));
x.addKey(\A, \legato, 1);

x.addKey(\B, \note, Prand([11, \r, \r, -1], inf));
x.addKey(\B, \dur, Pwhite(15, 25));
x.addKey(\B, \amp, Pwhite(0.2, 0.32));
x.addKey(\B, \legato, 1);

x.addKey(\cSharp2, \note, Pseq([13, \r], inf));
x.addKey(\cSharp2, \dur, Pwhite(15, 25));
x.addKey(\cSharp2, \amp, Pwhite(0.2, 0.32));
x.addKey(\cSharp2, \legato, 1);

x.addKey(\cSharp0, \note, Pseq([-11, \r, \r, \r], inf));
x.addKey(\cSharp0, \dur, Pwhite(15, 25));
x.addKey(\cSharp0, \amp, Pwhite(0.2, 0.32));
x.addKey(\cSharp0, \legato, 1);

x.addKey(\c, \note, Pseq([0, \r], inf));
x.addKey(\c, \dur, Pwhite(5, 30));
x.addKey(\c, \amp, Pwhite(0.2, 0.32));
x.addKey(\c, \legato, 1);


);

x.playSequence(\cSharp);
x.playSequence(\dSharp);
x.playSequence(\E);
x.playSequence(\fSharp);
x.playSequence(\gSharp);
x.playSequence(\A);
x.playSequence(\B);
x.playSequence(\cSharp2);
x.playSequence(\cSharp0);
x.playSequence(\c);


s.meter;
x.setOsc1SubVol(-15);
x.setOsc2SubVol(-21);



y = SaturSynth.new(~prm.submixB);
y.mixer.setVol(6);
y.playNote(38.midicps);
y.playNote(50.midicps);
y.releaseNote(36.midicps);
y.setFilterCutoff(450);

y.setTremoloDepth(0);
y.setTremoloRate(33)
y.setTremoloWaveform('revSaw');
s.meter;


x.stopSequence(\cSharp);
x.stopSequence(\dSharp);
x.stopSequence(\E);
x.stopSequence(\fSharp);
x.stopSequence(\gSharp);
x.stopSequence(\A);
x.stopSequence(\B);
x.stopSequence(\cSharp2);
x.stopSequence(\cSharp0);

s.makeWindow;

