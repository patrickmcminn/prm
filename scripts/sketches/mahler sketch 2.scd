z = Distortion.newStereo(0, 16);

d = SimpleDelay.newStereo(z.inBus, 0.4, 0.7);

x = "/Users/patrickmcminn/Music/Patrick McMinn/_Sketches/mahlampler/sample/mahler.wav"
y = SamplePlayer.newStereo(d.inBus, x);



(

z.postEQ.setLowPassCutoff(5000);
z.postEQ.setLowFreq(350);
z.postEQ.setLowGain(12);

z.mixer.setVol(0, -12);

y.setReleaseTime(0.5);
y.setSustainTime(0.05);
y.setFilterCutoff(10000);
);


// tremolo?
y.setTremoloDepth(1);
y.setTremoloRate(8);
y.setTremoloWaveform('revSaw');
y.setTremoloWaveform('rect');
y.setTremoloWaveform('tri');
y.setTremoloWaveform('saw');


y.makeSequence('ok', 'oneShot');

(
y.addKey(\ok, \dur, Pseq([0.25, 0.25, 0.125, 0.125], inf));
y.addKey(\ok, \startPos, Pwhite(0.2, 0.202));
y.addKey(\ok, \endPos, Pkey(\startPos) + Pwhite(0.001, 0.1));
y.addKey(\ok, \rate, Prand([0.5, 0.75, 1, 1.75, 1.5, 9/8, 8/9, 2, 4, 4/3, 1.5, 9/8], inf));
)


y.addKey(\ok, \rate, Prand([0.1, 0.15, 0.001], inf));

y.playSequence('ok');
y.stopSequence('ok');


// maybe:
//y.addKey(\ok, \tremFreq, Prand([8, 8, 8, 8, 4, 16, 8.1, 8.1, 8.1, 8.1, 16, 16, 16, 16, 12, 12, 12, 12], inf));
y.addKey(\ok, \tremFreq, Pseq([Pseq([8], 8), Pseq([12], 8), Pseq([16], 4), Pseq([8.1], 8)], inf));
y.setTremoloDepth(1);


// chords
(
y.addKey(\ok, \rate, Pseq([Pseq([[0.5, 1, 1.5, 0.75, 6/5]], 8), Pseq([[9/8, 1, 8/9]], 8)], inf));
z.setDistortionGain(6);
);

s.meter;

y.addKey(\ok, \startPos, Pwhite(0, 1));
y.addKey(\ok, \endPos, Pwhite(0, 1));
d.setFeedback(0.7);

d.setDelayTime(0.5);
d.setFeedback(0.7);
y.free;

z.free;
y.free;
s.meter;

s.makeWindow



d = FakeGuitar.new(0);
d.mixer.setVol(0);
d.playNote(1080, 750);
d.releaseNote(3);

d.playNote(1, 1000);


d.playNote(27.midicps, 5000);


d.setFlangerWaveform('saw');
d.setFlangerFeedback(0);
d.setFlangerSpeed(3);

s.meter

d.free;