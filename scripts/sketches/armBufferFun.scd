BufWr
(
y = { arg rate=1;
    var in;
    in = SinOsc.ar(LFNoise1.kr(2, 300, 400), 0, 0.1);
    BufWr.ar(in, 0, Phasor.ar(0, BufRateScale.kr(0) * rate, 0, BufFrames.kr(0)));
    0.0 //quiet
}.play;
)


SynthDef(\prm_writer, {
	| inBus, overdubBus, phaseBus, buffer, reset = 0, overdubAmt = 0, rate = 1 |
	var input, overdub, sum, record;
	input = In.ar(inBus, 1);
	overdub = In.ar(overdubBus) * overdubAmt;
	sum = input + overdub;
	record = BufWr.ar(sum, buffer, Phasor.ar(reset, BufRateScale.kr(buffer) * rate, 0, BufFrames.kr(buffer)), 1.0);
}).add;



SynthDef(\prm_longLoop, {
	| outBus = 0, inBus, buffer, reset = 0, overdubAmt = 0, rate = 1 |
	var input, overdub, sum, record, play, writeHead, readHead;
	var output;
	input = In.ar(inBus, 1);
	overdub = LocalIn.ar(1) * overdubAmt;
	sum = input + overdub;

	writeHead = Phasor.ar(reset, BufRateScale.kr(buffer), 0, BufFrames.kr(buffer), 0);
	readHead = Phasor.ar(reset, BufRateScale.kr(buffer) * rate, 0, BufFrames.kr(buffer), 0) - 0.05;

	record = BufWr.ar(sum, buffer, writeHead, 1.0);

	play = BufRd.ar(1, buffer, readHead, 1.0, 2);
	LocalOut.ar(play);

	output = Out.ar(outBus, play);
}).add;



x = Synth(\prm_longLoop, [\outbus, 0, \inBus, ~inBus, \buffer, ~buf, \rate, 1]);
x.free
~inBus = Bus.audio;
~oBus = Bus.audio;

~buf = Buffer.alloc(s, s.sampleRate*30, 1);

x = Synth(\prm_writer, [\inBus, ~inBus, \overdubBus, ~overdubBus, \buffer, ~buf]);

x.set(\overdubAmt, 1);
x.set(\rate, 0.5);

y = { Out.ar(~inBus, SoundIn.ar(0)) }.play

~buf.play

x.set(\rate, 1);

~buf.scopes

{ SoundIn.ar(0) }.play


x = Looper.newMono(0, 30, 0);
x.
y = { Out.ar(x.inBus, SoundIn.ar(0)) }.play

x.toggleRecordLoop
x.togglePlayLoop