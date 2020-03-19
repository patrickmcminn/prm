b = Buffer.read(s,"sounds/a11wlk01.wav");
b = Buffer.read(s, "/Users/patrickmcminn/Music/Cora Sone/Black Hole Sample 2.wav");

//match only on amplitude from audio in
(
SynthDef(\concat1, {arg bufnum, matchlength=0.01, freeze=0;
	var concat, control, input;

	control= SoundIn.ar;

	input=PlayBuf.ar(2,bufnum,BufRateScale.kr(bufnum), loop:1);

	concat= Concat.ar(control,input,1.0,1.0,1.0,matchlength,freeze,0.0,1.0,0.0,0.0);

	Out.ar(0,Pan2.ar(concat,0.0))}).add;
)

a=Synth(\concat1,[\bufnum, b.bufnum]);

a.set(\freeze, 0);

a.set(\matchlength, 0.01);


Gendy3

Concat
Concat2

Concat2.ar(control, source, storesize, seektime, seekdur, matchLength)


~buf = Buffer.read(s, ~path);

b = Bus.audio(s, 2);

z = { Out.ar(b, PlayBuf.ar(2, ~buf, loop: 1)) }.play

z = { Out.ar(0, PlayBuf.ar(2, ~buf, loop: 1)) }.play


{ Gendy3.ar(freq: 110) }.play

SynthDef(\test, { | inBus, buffer, outBus |
	var concat, control, input;
	control = PlayBuf.ar(2, buffer);
	input = SoundIn.ar(inBus);
	concat = Concat2.ar(control,input,4.0,4.0,4.0,MouseX.kr(0.0,0.5),0,0.5,1.0,0.5,0.0);
	Out.ar(outBus, Pan2.ar(concat, 0));
}).add;


x = Synth(\test, [\inBus, 0, \buffer, ~buf, \outBus, 0]);


(
{
	var concat, control, input;

	control= b;

	input=SoundIn.ar;

	concat= Concat2.ar(control,input,4.0,4.0,4.0,MouseX.kr(0.0,0.5),0,0.5,1.0,0.5,0.0);

	Out.ar(0,Pan2.ar(concat,0.0)*0.05)}.play
)


~path = "/Users/patrickmcminn/Music/Cora Sone/Wild Kindness/Bounced Files/Wild Kindness rev2.wav";





(
{
	var concat, control, input, amp;

	var freq;


	control=SoundIn.ar;

	freq = Tartini.kr(control);

	amp=Amplitude.ar(control);

	input= SinOsc.ar(2)*
	Mix(Gendy3.ar(
		3,
		5,
		1.0,
		1.0,
		[freq, freq/2],
		MouseY.kr(0.01,0.05),
		MouseY.kr(0.001,0.016),
		5,mul:0.1));

	concat= Concat.ar(control,input,1.0,1.0,1.0,MouseX.kr(0.0,0.1),0,1.0,0.0,1.0,1.0);

	Out.ar(0,Pan2.ar(10*concat*amp,0.0))}.play
)
Gendy3.ar(









	