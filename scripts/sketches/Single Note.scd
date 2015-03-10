x = TrumpetNebula.new(0);
y = Subtractive.new(x.inBus);

y.setOsc1SubVol(-6);

y.setFilterDrive(1);
y.setFilterRes(0.4);
y.setFilterCutoff(3500);

y.setFilterCutoffLFO2BottomRatio(0.75);
y.setFilterCutoffLFO2TopRatio(2);

y.setLFO2Freq(0.1);
y.setPanLFO2Bottom(-1);
y.setPanLFO2Top(1);

y.setOsc2Waveform('rect');
y.setOsc2Octave(3);
y.setOsc2Vol(-12);

x.setFeedback(1);
x.setReverbRoom(0.7);

x.setLowGain(-3);
x.setHighGain(-6);

x.setCutoff(2500);

x.setLeftDelayTime(0.7);
x.setRightDelayTime(0.9);

y.setLFO1Freq(12);
y.setFilterCutoffLFOTopRatio(2);
y.setFilterCutoffLFOBottomRatio(0.75);
y.setLFO1Waveform('rect');

y.makeSequence('y');
y.makeSequence(\z);
y.makeSequence(\3);

(
y.addKey(\y, \note, Pseq([[13, 20], \r, \r, \r, \r, 13, \r, \r, \r, \r, 20, \r, \r, \r], inf));
y.addKey(\y, \dur, 1);
)

y.playSequence(\y);


y.addKey(\z, \note, Pseq([15, \r, \r, \r, \r, 10, \r, \r, [15, 17], \r, \r], inf));
y.addKey(\z, \dur, 8/9);
y.addKey(\z, \attackTime, 0.75);

y.playSequence(\z);


y.addKey(\3, \note, Pseq([11, \r, \r, \r, \r], inf));
y.addKey(\3, \dur, 9/8);
y.addKey(\3, \attackTime, 1);
y.addKey(\3, \legato, Pwhite(0.75, 1));
y.playSequence(\3);


y.makeSequence(\4);
y.addKey(\4, \note, Pseq([18, \r, \r, \r, \r, \r], inf));
y.addKey(\4, \dur, 3/4);
y.addKey(\4, \attackTime, 1.5);
y.addKey(\4, \releaseTime, 3);
y.addKey(\4, \legato, Pwhite(0.5, 1));
y.playSequence(\4);


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

~bassSynth.addKey(\bass, \note, Pseq([[-11, 1], \r, \r, [-13, -1], \r, \r], inf));
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
~bassSynth.playSequence(\bass);

~bassSynth.setFilterCutoff(1000);

x.mixer.setVol(-3);
s.meter;

s.makeWindow;

~fbRoutine.stop;
~freqRoutine.stop;
~bassSynth.stopSequence(\bass);

x.setDelayFilterCenterFreq(750);
x.setFeedback(1);


y.stopSequence(\4);
y.stopSequnce(\3);
y.stopSequence(\z);
y.stopSequence(\y);

y.mixer.fadeOut(10);