~prm = AudioSystem.new(2);
~prm.submixerA.mixer.setVol(3);


x = Subtractive.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);
y = GlitchySynth.new(~prm.submixB, ~prm.reverb.inBus, ~prm.granulator.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);


x.mixer.setSendVol(0, -9);
x.mixer.setSendVol(1, -6);

y.mixer.setSendVol(0, -3);

x.setOsc1Waveform('tri');
x.setOsc2Waveform('rect');
x.setOsc2PulseWidthLFO2Bottom(0.5);
x.setOsc2PulseWidthLFO2Bottom(-0.5);
x.setFilterCutoff(3500);
x.setFilterCutoffLFO2BottomRatio(3);
x.setFilterCutoffLFO2TopRatio(0.75);
x.setOsc2Vol(-12);
x.setReleaseTime(0.05);
x.setLFO2Freq(0.3);
x.setOsc1WaveformLFO2Bottom(0);
x.setOsc1WaveformLFO2Top(3);
x.mixer.setVol(12);
x.sequencerClock.tempo = 104/60;

y.setAttackTime(3);
y.setReleaseTime(5);
y.glitchLooper.setTriggerFrequency(1.5);
y.glitchLooper.setRepeats(64);
y.mixer.setVol(1, 3);
y.mixer.setSendVol(0, 1, -9);
y.mixer.setSendVol(0, 0, -3);
y.mixer.setMasterVol(-3);


x.printAllParameters;
x.saveCurrentAsPreset('perc');


x.makeSequence('ping');
x.makeSequence('ding');

y.makeSequence('mel');


x.addKey(\ping, \dur, Pseq([0.25, 0.25, 0.25, 1/6, 1/6, 0.125, 0.125], inf));
x.addKey(\ping, \note, Pseq([0, -3, 2, 4], inf));
x.addKey(\ping, \octave, Prand([4, 5, 5, 5, 6, 6], inf));
x.addKey(\ping, \legato, 0.1);
x.addKey(\ping, \pan, 0.5);


x.addKey(\ding, \dur, Pseq([0.25, 0.25, 0.25, 1/6, 1/6, 0.125], inf));
x.addKey(\ding, \note, Pseq([0, -3, 2, 4, 5], inf));
x.addKey(\ding, \octave, Prand([4, 5, 5, 5, 6, 6], inf));
x.addKey(\ding, \legato, 0.1);
x.addKey(\ding, \pan, -0.5);


y.addKey(\mel, \dur, Pseq([6.5, 1.5, 3, 4, 6], inf));
y.addKey(\mel, \note, Pseq([0, -3, 0, 2, -5], inf));
y.addKey(\mel, \legato, 1);
y.addKey(\mel, \octave, [4, 5]);
//y.addKey(\mel, \octave, [4, 5, 6]);


x.playSequence(\ding);
x.playSequence(\ping);
y.playSequence(\mel, x.sequencerClock);
z.playSequence(\bassline, x.sequencerClock);



x.stopSequence(\ding);
x.stopSequence(\ping);
y.stopSequence(\mel);
z.stopSequence(\bassline);

y.playNote(220);

y.mixer.unMute
y.mixer.setSendVol(0, 0, -3);
y.releaseNote(220);
y.setWaveLossAmount(0);

y.setFilterCutoff(5500);

y.mixer.tglMute(0);

z = SaturSynth.new(~prm.submixC, ~prm.reverb.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);

z.makeSequence(\bassline);

z.addKey(\bassline, \dur, 1/6);
z.addKey(\bassline, \note, Pseq([Pseq([0, -3, 5, -3, 0, 2], 8), Pseq([-3, 0, -3, -5, 4, 0], 8)], inf));
z.addKey(\bassline, \octave, [3, 4, 5]);
z.addKey(\bassline, \legato, 0.8);

z.setFilterCutoff(330);











