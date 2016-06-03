x = AudioSystem.new;

x.submixA;

~path = "/Users/patrickmcminn/Music/Instrument:Sample Library/Drums/by drum/Snare/chunkier-snare.aif";
y = SamplePlayer.newMono(x.submixA, ~path, x.reverb.inBus, relGroup: x.procGroup, addAction: \addToHead);

y.playSampleOneShot(0, 1);
y.mixer.setSendVol(0, -inf);
y.makeSequence('test1', 'oneShot')

y.addKey(\test1, \dur, Pseq([1/8, 1/8, 1/2, 1/4, 1/4, Pseq([1/32], 16), Pseq([1/16], 4)], inf));
y.addKey(\test1, \freq, Pseq([Prand([0], inf)], inf));
y.addKey(\test1, \rate, Pwhite(0.8, 1.2));
y.addKey(\test1, \loop, 0);

y.playSequence(\test1);
y.stopSequence(\test1);
y.addKey(\test1, \sustainTime, Pwhite(0.005, 0.009));
y.addKey(\test1, \attackTime, 0);
y.addKey(\test1, \releaseTime, 0);
y.addKey(\test1, \instrument, \prm_SamplePlayer_Mono_OneShot);


y.sequencerDict
y.mixer.setVol(-15);
y.mixer.tglMute;


~path2 = "/Users/patrickmcminn/Music/Instrument:Sample Library/Drums/3rd party sample packs/iAmDrum-MS20-Free/Drums/BD_MS20_808_2.wav";
z = SamplePlayer.newMono(x.submixA, ~path2, x.reverb.inBus, relGroup: x.procGroup, addAction: \addToHead);
z.playSampleOneShot;
z.mixer.setVol(3);


z.makeSequence(\four);
z.addKey(\four, \dur, 0.5);
z.addKey(\four, \rate, [0.75, 1, 1.5, 1.6]);
z.stopSequence(\four);
(
z.playSequence(\four);
y.playSequence(\test1);
)

