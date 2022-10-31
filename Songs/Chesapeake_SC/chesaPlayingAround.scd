MarkovSetN

z = FakeGuitar.new(~prm.audioIn);

a = SaturSynth.new(~prm.audioIn);

x = Chesa_CrisisSynth.new(0);
GranularDelay2
y = Chesa_Algae.new;

y.synth.playSequence(\algae);

y.isLoaded

y.synth.playNote(220);
y.synth.releaseNote(220);

y.eq.setLowPassCutoff(5000);

y.synth.makeSequence(\algae);

y.synth.addKey(\algae, \dur, Pwhite(0.05, 0.5, inf));
y.synth.addKey(\algae, \note, Prand([7, 9, 10, 5, 0, 2, 14, \r, \r, \r, \r], inf));

y.synth.playSequence(\algae);

y.granulator.setMix(1);
y.granulator.setTrigRate(10);
y.granulator.setTrigRate(1);
y.granulator.setGrainDur(3, 5);
y.granulator.setRate(0.5, 0.5);
y.granulator.setGrainEnvelope(\revPerc);

GranularDelay2

x.isLoaded

x.synth.playSequence(\bassline);

x.tremolo.setVolLFOFreq(0.2);
x.tremolo.setVolLFODepth(0.2);

x.filter.setCutoff(5000);

s.meter

x.mixer.setPreVol(-18);

x.synth.mixer.setPreVol(-12);

a.playNote(36.midicps);
a.releaseNote(36.midicps);

~fb = FeedbackSynth.new(0);
~fb.playNote(36.midicps);
b.playNote(60.midicps);
b.playNote(55.midicps);
x.synth.setFilterCutoff(750);

Darkness_Spuds

x = Subtractive.new(0);
x.readPreset('darkness');

x.playNote(660);
x.releaseNote(660);

x.writePreset('chesaAlgae');


~fb.sweepFilter(5720, 1000, 5);
{ SinOsc.ar(220) }.play
~fb.playNote(50.midicps);

~fb.releaseNote(50.midicps);

~fb.free;

[55, 53, 51, 50]





~fb.setAttackTime(10);
~fb.setReleaseTime(10);
~fb.mixer.setVol(-24);

b.releaseNote(60.midicps);

b.mixer.setVol(-12);

z.isLoaded

z.playNote(24.midicps);

y = IM_Mixer_1Ch.new(~prm.audioIn, ~prm.reverb.inBus);


y.setPreVol(-12);

y.setSendVol(0, -15);

x = Chesa_Tide.new(y.chanStereo(0));



~rout = {
	loop {
		x.playMarkovChord;
		30.wait;
	}
}.fork;

s.hasShmInterface;

~busTry = {
	loop {
		~bus.get({ | val |
			var durLow = if( val > 0.5,
				{ val.lincurve(0, 1, 1, 0.05, -1.5) },
				{ val.lincurve(0, 1, 1, 0.05, 1.5); });
			var durHigh = if( val > 0.5,
				{ val.lincurve(0, 1, 3, 0.3, -1.5) },
				{ val.lincurve(0, 1, 3, 0.3, 1.5); });
			var trigLow = if( val > 0.5,
				{ val.lincurve(0, 1, 0.5, 45, -1.5); },
				{ val.lincurve(0, 1, 0.5, 45, 1.5); });
			var trigHigh = if( val > 0.5,
				{ val.lincurve(0, 1, 3, 60, -1.5); },
				{ val.lincurve(0, 1, 3, 60, 1.5); });
			var cutoffLow = val.linexp(0, 1, 400, 4500);
			var cutoffHigh = val.linexp(0, 1, 900, 9000);
			var tremSpeed = val.linlin(0, 1, 0.1, 8);
			var verb = val.linlin(0, 1, -24.dbamp, -9.dbamp);
			x.cloud.setSustain(durLow, durHigh);
			x.cloud.setTrigRate(trigLow, trigHigh);
			x.cloud.setCutoff(cutoffLow, cutoffHigh);
			x.tremolo.setVolLFOFreq(tremSpeed);
			y.setSendVol(0, 0, verb);

		});
		0.001.wait;
	}
}.fork;

~bus.getSynchronous({ | val | val.postln; });


~busTry.stop;

x.cloud.setRelease(3);
x.cloud.setSustain(1, 3);
x.cloud.setTrigRate(0.5, 3);
x.cloud.setCutoff(5000, 10000);

{ Line.ar(-1, 5, 0.1).lincurve(0, 3, -1, 1, curve: ) }.plot(0.1);

0.1.lincurve(0, 1, 0, 127, 0);
0.1.linlin(0, 1, 0, 127);

x = { | val |
	if( val >0.5,
		{ val.lincurve(0, 1, 0, 127, -1.5); },
		{ val.lincurve(0, 1, 0, 127, 1.5); });
};

x.value(0.8);

0.8.linlin(0, 1, 0, 127);

curve

0.9.lincurve(0, 1, 0, 127, 2);
0.9.linlin(0, 1, 0, 127);

{ Line.ar(-2, 2, 2).lincurve(-1, 1, -1, 1, 0) }.plot(2);



d
~busTry.stop;
~bus
~rout.stop;

x.cloud.trigRateLow;
x.cloud.trigRateHigh
x.cloud.sustainLow
x.cloud.sustainHigh

0.67372065782547.linlin(0, 1, 3, 0.005);
0.1.linlin(0, 1, 3, 0.005);

~bus.getSynchronous({ | val | val.postln; });

~ftBuf.plot

x.tremolo.volLFO.setFrequency(8);
x.tremolo.setVolLFODepth(0.3);

x.cloud.setCutoff(1500, 3500);

x.cloud.setInstArray([\gaborGendy]);

x.cloud.setInstArray([\expodecSaw, \gaborSaw, \percRevRect]);

x.cloud.setInstArray([\gaborSaw, \gaborRect, \percRevRect, \rexpodecRect, \expodecRect, \rexpodecSaw, \rexpodecSine]);

x.cloud.setWidth(0.05, 0.95);

d

GrainCloud2

x.


x.cloud.setInstArray([\gaborRect, \gaborSaw, \gaborWideSaw, \percSaw, \percRevRect, \expodecSaw]);

x.cloud.setWidth(0.01, 0.99);
x.cloud.setRQ(0.07, 0.3);

x.cloud.setPan(-1, 1);

x.cloud.setCutoff(400, 3500);

s
x.cloud.setInstArray([\rexpodecSaw, \expodecSaw, \gaborSaw, \gaborSaw, \gaborGendy]);
x.setCutoffLow(600);
x.setCutoffHigh(2500);

x.setTrigRateLow(0.5);
x.setTrigRateHigh(11);

x.setSustain(1, 3);

x.setGrain


(
m = MarkovSetN([
    [[100, 120], [120, 130]],
    [[120, 100], [100, 1900]]
], 2);
)


m = MarkovSetN([
	[[1, 2], [2]],
	[[2, 1], [1]]], 2)

m.next([1, 2], 2);


~p = 0;
~c = 1;

m = MarkovSetN([
	[[0, 1], [2]],
	[[1, 2], [3]]], 2);

(
var previous = ~p;
var current = ~c;
~p = ~c;
~c = m.next([previous, current]);
)

~p
~c

x = [0.75, 0.1, 0.1, 0.1];

y = x.normalizeSum

normalize


~progression = MarkovSetN([
		[[0, 1], [1], [1],
			[[1, 1],[2, 3, 4, 6], [0.6, 0.15, 0.15, 0.1]],
	[[1, 2], [1, 4, 5, 6], [0.4, 0.4, 0.1, 0.1]],
	[[1, 3], [4, 5, 9, 8], [0.75, 0.2, 0.025, 0.025]],
	[[1, 4], [3, 5, 6], [0.8, 0.1, 0.1]],
	[[1, 6], [1, 4], [0.9, 0.1]],
	[[2, 1], [2, 3], [0.2, 0.8]],
	[[2, 4], [5, 6, 7], [0.5, 0.25, 0.25]],
	[[2, 5], [9], [1]],
	[[2, 6], [6, 10], [0.5, 0.5]],
	[[3, 4], [3, 1], [0.9, 0.1]],
	[[3, 5], [6, 9, 2], [0.8, 0.1, 0.1]],
	[[3, 9], [9, 10, 5], [0.9, 0.05, 0.05]],
	[[3, 8], [7, 3], [0.5, 0.5]],
	[[3, 7], [4], [1]],
	[[3, 6], [9], [1]],
	[[4, 3], [4, 7, 6], [0.3, 0.35, 0.35]],
	[[4, 5], [9], [1]],
	[[4, 7], [5, 2], [0.5, 0.5]],
	[[4, 6], [6, 10], [0.75, 0.25]],
	[[4, 1], [2], [1]],
	[[5, 9], [9], [1]],
	[[5, 6], [6, 10], [0.75, 0.25]],
	[[5, 1], [6], [1]],
	[[5, 10], [6], [1]],
	[[6, 1], [2], [1]],
	[[6, 4], [6, 3], [0.3, 0.7]],
	[[6, 6], [6, 9], [0.5, 0.5]],
	[[6, 10], [10], [1]],
	[[6, 9], [9], [1]],
	[[6, 3], [6, 4], [0.5, 0.5]],
	[[7, 5], [6, 9], [0.5, 0.5]],
	[[7, 2], [4], [1]],
	[[7, 3], [4], [1]],
	[[7, 4], [3], [1]],
	[[8, 7], [3], [1]],
	[[8, 3], [4], [1]],
	[[9, 9], [9, 6, 10], [1/3, 1/3, 1/3]],
	[[9, 10], [10], [1]],
	[[9, 5], [10], [1]],
	[[9, 6], [9, 3], [0.3, 0.7]],
	[[10, 10], [10, 1], [0.5, 0.5]],
	[[10, 1], [2, 3, 4, 6], [0.6, 0.15, 0.15, 0.1]],
	[[10, 6], [9, 10], [0.25, 0.75]]
], 2);


~p = 0;
~c = 1;

(
var previous = ~p;
var current = ~c;
~p = ~c;
~c = ~progression.next([previous, current]);
)
