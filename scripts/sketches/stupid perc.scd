x = AudioSystem.new;

(
~path = "/Users/patrickmcminn/Music/Instrument:Sample Library/Drums/by drum/Snare/chunkier-snare.aif";
y = SamplePlayer.newMono(x.submixA, ~path, x.reverb.inBus, x.granulator.inBus, relGroup: x.procGroup, addAction: \addToHead);

~path2 = "/Users/patrickmcminn/Music/Instrument:Sample Library/Drums/3rd party sample packs/iAmDrum-MS20-Free/Drums/BD_MS20_808_2.wav";
z = SamplePlayer.newMono(x.submixA, ~path2, x.reverb.inBus, relGroup: x.procGroup, addAction: \addToHead);

a = Subtractive.new(x.submixA, x.reverb.inBus);
);

(
y.mixer.setSendVol(0, -24);
y.mixer.setVol(-24);

y.setAttackTime(0.05);

z.mixer.setVol(3);


a.mixer.setSendVol(0, -6);

a.setLFO2Freq(0.7);
a.setLFO1Freq(0);
a.setLFO2Waveform('noise');
a.setLFO1Waveform('revSaw');
a.setLFO1Freq(1);

a.setFilterCutoff(1200);
a.setFilterCutoffLFO1BottomRatio(0.3);
a.setFilterCutoffLFO1TopRatio(2);
a.setFilterResLFO2Bottom(0);
a.setFilterResLFO2Top(0.5);

a.setAttackTime(0.15);
a.setSustainLevel(0.15);
a.setDecayTime(0.15);
a.setReleaseTime(0.05);

a.setOsc2Octave(4);
a.setOsc1Waveform('rect');
a.setOsc1SubVol(-12);
a.setOsc1PulseWidth(0.2);
a.setOsc1PulseWidthLFO2Bottom(-1);
a.setOsc1PulseWidthLFO2Top(1);
a.mixer.setVol(-6);
);

(
y.makeSequence('test1', 'oneShot');
z.makeSequence(\four, 'oneShot');
a.makeSequence(\yeah);
);

(
y.addKey(\test1, \dur, Pseq([1/8, 1/8, 1/2, 1/4, 1/4, Pseq([1/8], 4), Pseq([1/16], 4)], inf));
y.addKey(\test1, \freq, Pseq([0, 0, 0, 0, 0, Prand([0, \r], 4), Pseq([0], 4), inf], inf));
y.addKey(\test1, \rate, Pwhite(1, 1));
y.addKey(\test1, \amp, Pwhite(0.8, 1));
y.addKey(\test1, \pan, Pwhite(-0.7, 0.7));
y.addKey(\test1, \attackTime, 0.05);
y.addKey(\test1, \releaseTime, Pwhite(0.05, 0.7));

z.addKey(\four, \dur, 0.5);
z.addKey(\four, \rate, 1);
z.addKey(\four, \legato, 1);
z.addKey(\four, \amp, 0.4);

a.addKey(\yeah, \dur, 1/5);
a.addKey(\yeah, \note, Pseq([Pseq([0, -5, -7, -9, -12], 16), Pseq([0, -2, 3, 5, 7], 16)], inf));
a.addKey(\yeah, \amp, 0.25);
a.addKey(\yeah, \legato, 1);
a.addKey(\yeah, \filterCutoff, Pbrown(1000, 3500, 75, inf));
);


(
z.playSequence(\four);
y.playSequence(\test1);
a.playSequence(\yeah);
);

(
z.stopSequence(\four);
y.stopSequence(\test1);
a.stopSequence(\yeah);
)


y.tglMute
a.mixer.tglMute
z.mixer.tglMute;

y.mixer.setSendVol(1, -9);

x.granulator.setRate(1, 1);
x.granulator.setGrainDur(0.001, 0.03);
x.granulator.setTrigRate(128);
x.granulator.setGrainEnvelope('expodec');

x.granulator.setDelayMix(0.5);
x.granulator.setDelayTime(1);
x.granulator.setFeedback(0.85);