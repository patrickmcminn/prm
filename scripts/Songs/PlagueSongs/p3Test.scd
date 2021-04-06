
(
~p3 = Plague3.new(~prm.audioIn, ~micIn, ~pickupIn, ~pyr, ~modInput, ~modOutput,
	~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus, ~prm.procGroup, \addToHead);
)

~p3.isLoaded;


~apc.setPage('plague3');
~base.setPage('plague3');


x = { | freq = 220, lag = 0.01 | SinOsc.ar(Lag2.kr(freq, lag)) * 0.1; }.play

x.set(\freq, 220);
x.set(\lag, 3);

SynthDef(\lagTest, {
	| outBus = 0, freq = 220, amp = 0.1, lag = 0.01 |
	var sig = SinOsc.ar(freq);
	sig = sig * Lag2.kr(amp, lag);
	sig = Out.ar(outBus, sig);
}).add;


x = Synth(\lagTest);

x.set(\lag, 10);
x.set(\amp, 0);

x.set(\lag, 10);
x.set(\freq, 50);

s.meter