x = AudioSystem.new;

x.submixA;

~path = "/Users/patrickmcminn/Music/Instrument:Sample Library/Drums/by drum/Snare/chunkier-snare.aif";
y = SamplePlayer.newMono(x.submixA, ~path, x.reverb.inBus, relGroup: x.procGroup, addAction: \addToHead);

y.playSampleOneShot(0, 1);
y.mixer.setSendVol(0, -24);
y.makeSequence('test1', 'oneShot')

y.addKey(\test1, \dur, Pseq([1/8, 1/8, 1/2, 1/4, 1/4, Pseq([1/8], 4), Pseq([1/16], 4)], inf));

y.addKey(\test1, \freq, Pseq([0, 0, 0, 0, 0, Prand([0, \r], 4), Pseq([0], 4), inf], inf));
y.addKey(\test1, \rate, Pwhite(0.98, 1.02));
y.addKey(\test1, \amp, Pwhite(0.8, 1));
y.addKey(\test1, \pan, Pwhite(-0.7, 0.7));

y.playSequence(\test1);
y.stopSequence(\test1);

y.addKey(\test1, \attackTime, 0.05);
y.addKey(\test1, \releaseTime, Pwhite(0.05, 0.7));

y.mixer.setVol(-12);
y.mixer.tglMute;


~path2 = "/Users/patrickmcminn/Music/Instrument:Sample Library/Drums/3rd party sample packs/iAmDrum-MS20-Free/Drums/BD_MS20_808_2.wav";
z = SamplePlayer.newMono(x.submixA, ~path2, x.reverb.inBus, relGroup: x.procGroup, addAction: \addToHead);
z.playSampleOneShot;
z.mixer.setVol(3);


z.makeSequence(\four);
z.addKey(\four, \dur, 0.5);
z.addKey(\four, \rate, [1]);
z.stopSequence(\four);
(
z.playSequence(\four);
y.playSequence(\test1);
a.playSequence(\yeah);
)


a = Subtractive.new(x.submixA, x.reverb.inBus);
a.mixer.setSendVol(0, -18)
a.makeSequence(\yeah);

a.addKey(\yeah, \dur, 0.125);
a.addKey(\yeah, \note, Pseq([0, -5, -7, -9, -12], inf));
a.addKey(\yeah, \amp, 0.25);
a.addKey(\yeah, \legato, 1);
a.addKey(\yeah, \filterCutoff, Pbrown(1000, 3500, 75, inf));

(
a.setFilterCutoff(1200);
a.setSustainLevel(0.15);
a.setDecayTime(0.15);
a.setReleaseTime(0.05);

a.setOsc2Octave(4);


a.setOsc1Waveform('rect');
a.setOsc1SubVol(-12);
a.setOsc1PulseWidth(0.2);
a.setOsc1PulseWidthLFO2Bottom(-1);
a.setOsc1PulseWidthLFO2Top(1);
a.setLFO2Freq(0.7);
a.setLFO1Freq(0);

a.setLFO2Waveform('noise');
a.setLFO1Waveform('revSaw');

a.setFilterCutoffLFO1BottomRatio(0.3);
a.setFilterCutoffLFO1TopRatio(2);
a.setLFO1Freq(1);

a.setFilterResLFO2Bottom(0);
a.setFilterResLFO2Top(0.5);
)