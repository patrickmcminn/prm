x = TrumpetNebula.new(0);
y = Subtractive.new(x.inBus);

y.setOsc1SubVol(-6);

y.setFilterDrive(1);
y.setFilterRes(0.4);
y.setFilterCutoff(3500);

y.setFilterCutoffLFO2BottomRatio(0.75);
y.setFilterCutoffLFO2TopRatio(2);

y.setLFO2Freq(0.1);
y.setPanLFO2Bottom(-0.5);
y.setPanLFO2Top(0.5);

y.setOsc2Waveform('rect');
y.setOsc2Octave(3);
y.setOsc2Vol(-12);

x.setFeedback(1);
x.setReverbRoom(0.7);

x.setLowGain(-3);
x.setHighGain(-3);

x.setCutoff(3500);

x.setLeftDelayTime(0.7);
x.setRightDelayTime(0.9);

y.setLFO1Freq(12);
y.setFilterCutoffLFO1TopRatio(2);
y.setFilterCutoffLFO1BottomRatio(0.75);
y.setLFO1Waveform('revSaw');

y.makeSequence('y');
y.makeSequence(\z);
y.makeSequence(\3);
y.makeSequence(\4);
y.makeSequence(\5);

(
y.addKey(\y, \note,
  Pseq([[13, 20, 25], \r, \r, \r, \r, 13, \r, \r, \r, \r, 20, \r, \r, \r, [13, 20, 25, 27], \r, \r, \r, \r, 13, \r, \r, \r, \r, 18, \r, \r, \r], inf));
y.addKey(\y, \dur, 1);
y.addKey(\y, \attackTime, 1.5);
y.addKey(\y, \legato, 1.5);
y.addKey(\y, \releaseTime, 2);
y.addKey(\z, \lfoEnvType, 1);
y.addKey(\z, \lfo1AttackTime, 1);

y.addKey(\z, \note, Pseq([15, \r, \r, \r, \r, 13, \r, \r, \r, 12, \r, \r], inf));
y.addKey(\z, \dur, 16/9);
y.addKey(\z, \attackTime, 3);
y.addKey(\z, \legato, 0.7);
y.addKey(\z, \releaseTime, 3.5);
y.addKey(\z, \lfoEnvType, 1);
y.addKey(\z, \lfo1AttackTime, 0.6);

y.addKey(\3, \note, Pseq([12, \r, \r, \r, \r], inf));
y.addKey(\3, \dur, 9/8);
y.addKey(\3, \attackTime, 1);
y.addKey(\3, \legato, Pwhite(0.75, 1));

y.addKey(\4, \note, Pseq([18, \r, \r, \r, \r, \r], inf));
y.addKey(\4, \dur, 3/4);
y.addKey(\4, \attackTime, 1.5);
y.addKey(\4, \releaseTime, 3);
y.addKey(\4, \legato, Pwhite(0.5, 1));

y.addKey(\5, \note, Pseq([Pseq([[20, 27]], 16), Pseq([[25, 27]], 15), Pseq([[25, 32]], 17)], inf));
y.addKey(\5, \dur, 0.25);
y.addKey(\5, \amp, 0.2);
y.addKey(\5, \legato, 0.05);
y.addKey(\5, \filterCutoff, 4000);
)

y.playSequence(\y);
y.playSequence(\z);
y.playSequence(\3);
y.playSequence(\4);
y.playSequence(\5);


~fbRoutine = r {
  x.setFeedback(1);
  rrand(3.0, 10.0).wait;

  x.setFeedback(1.5);
  rrand(1.5, 5).wait;
}.loop.play;

~freqRoutine = r {
  x.setDelayFilterCenterFreq(exprand(850, 2500));
  rrand(10.0, 60.0).wait;
}.loop.play;


~bassSynth = Subtractive.new(x.inBus);
~bassSynth.makeSequence(\bass);

~bassSynth.addKey(\bass, \note, Pseq([Pseq([[-11, 1], \r, \r, [-13, -1], \r, \r], 4), Pseq([[-14, -2], \r, \r])], inf));
~bassSynth.addKey(\bass, \dur, Prand([5, 5.5, 6, 6.5], inf));
~bassSynth.addKey(\bass, \legato, 1);
~bassSynth.addKey(\bass, \amp, 0.24);

~bassSynth.setAttackTime(2);
~bassSynth.setReleaseTime(7);
~bassSynth.setDecayTime(3);
~bassSynth.setSustainLevel(0.5);

~bassSynth.setFilterEnvPeakRatio(1);
~bassSynth.setFilterEnvAttackRatio(0.2);
~bassSynth.setFilterEnvAttackTime(3);
~bassSynth.setFilterRes(0.8);
~bassSynth.setFilterCutoffLFO2BottomRatio(0.75);
~bassSynth.setFilterCutoffLFO2TopRatio(1.5);
~bassSynth.setLFO2Freq(0.2);
~bassSynth.setLFO2Waveform('noise');


~bassSynth.setOsc2Waveform('tri');
~bassSynth.setOsc2SubVol(-12);
~bassSynth.setOsc2Octave(2);
~bassSynth.setOsc2Vol(0);
~bassSynth.setFilterCutoff(1000);

~bassSynth.playSequence(\bass);

x.mixer.setVol(-3);
s.meter;

s.makeWindow;

~fbRoutine.stop;
~freqRoutine.stop;
~bassSynth.stopSequence(\bass);

x.setDelayFilterCenterFreq(750);
x.setFeedback(1);


y.stopSequence(\4);
y.stopSequence(\3);
y.stopSequence(\z);
y.stopSequence(\y);
y.stopSequence(\5);
~bassSynth.stopSequence(\bass);


y.mixer.fadeOut(10);



// bass change:
~bassSynth.addKey(\bass, \note, Pseq([[-11, 1], [-13, -1], [-14, -2], \r], inf));



x.setFeedback(0.9);
x.setLeftDelayTime(0.25);
x.setRightDelayTime(0.27);
x.setDelayFilterBW(1);
x.mixer.setVol(6);
x.setDistortionAmount(10);
x.setLowGain(3);