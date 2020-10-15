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
Mix(Gendy3.ar(3,5,1.0,1.0,(Array.fill(5,{LFNoise0.kr(1.3.rand,1,2)})*MouseY.kr(100,3780,'exponential')),MouseY.kr(0.01,0.05),MouseY.kr(0.001,0.016),5,mul:0.1));


Gendy3.ar(3, 5, 1, 1, Array.fill(5, { LFNoise0.kr(1.3.rand, 1, 2)}


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

	input=SoundIn.ar(0);

	concat= Concat2.ar(control,input,4.0,4.0,4.0,MouseX.kr(0.0,0.5),0,0.5,1.0,0.5,0.0);

	Out.ar(0,Pan2.ar(concat,0.0)*0.05)}.play
)


(
{
	var concat, control, input, amp;

	var freq;


	control=SoundIn.ar(1);

	freq = Tartini.kr(control);

	amp=Amplitude.ar(control);

	input= SinOsc.ar(2)*
	Mix(Gendy3.ar(
		3,
		5,
		1.0,
		1.0,
		[freq, freq/2, freq*1.1],
		MouseY.kr(0.01,0.05),
		MouseY.kr(0.001,0.016),
		5,mul:0.1));

	concat= Concat.ar(control,input,1.0,1.0,1.0,MouseX.kr(0.0,0.1),0,1.0,0.0,1.0,1.0);

	Out.ar(0,Pan2.ar(10*concat*amp,0.0))}.play
)



	SynthDef(\prm_concatGlitch, {
		|
		inBus, outBus, amp = 1, matchBus,
		storeSize = 4, seekTime = 4, seekDur = 4, matchLength = 0.3,
		freeze = 0, controlAmpModFreq = 0.5, matchLengthRange = 0.1
		|
		var input, matchInput, control, controlAmpMod, concat, sig;
		input = In.ar(inBus, 2);
		matchInput = In.kr(matchBus).range(matchLengthRange.neg, matchLengthRange);
		controlAmpMod = SinOsc.ar(controlAmpModFreq);
		control = Saw.ar(SinOsc.kr(LFNoise0.kr(LFNoise1.kr(1).range(0.01, 1), 3, 4.5), 0, 50, LFNoise1.kr(1.5).range(120, 500)));
		control = control * controlAmpMod;
		concat = Concat2.ar(control, input, storeSize, seekTime, seekDur, matchLength + matchInput, freeze, 0.5, 1.0, 0.5, 0.0);
		sig = concat * amp;
		Out.ar(outBus, sig);
	}).add;


	SynthDef(\prm_concatGendyUnpitched, {
		|
		inBus, outBus, amp, matchBus,
		storeSize = 1, seekTime = 1, seekDur = 1, matchLength = 0.05,
		freeze = 0, inputAmpModFreq = 2, matchLengthRange = 0.01,
		inputFreqRangeLow = 100, inputFreqRangeHigh = 3780,
		inputFreqChangeLow = 1, inputFreqChangeHigh = 4.6,
		ampScale = 0.05, durScale = 5
		|
		var input, matchInput, control, inputAmpMod, frequency, amplitude, concat, sig;

		control = In.ar(inBus, 2);
		matchInput = In.kr(matchBus).range(matchLengthRange.neg, matchLengthRange);
		frequency = Tartini.kr(control);
		amplitude = Amplitude.kr(control);

		inputAmpMod = SinOsc.ar(inputAmpModFreq);
		input = Mix(Gendy3.ar(3, 5, 1.0, 1.0, (Array.fill(5, {
			LFNoise0.kr(rrand(inputFreqChangeLow, inputFreqChangeHigh)).range(1, 3)
			*
			LFNoise2.kr(rrand(inputFreqChangeLow, inputFreqChangeHigh)).exprange(100, 3780)})), ampScale, durScale));
		input = input * inputAmpMod;
		concat = Concat2.ar(control, input, storeSize, seekTime, seekDur, matchLength + matchInput, freeze, 1, 0, 1, 1);
		sig = concat * amplitude;
		sig = sig * amp;
		Out.ar(outBus, sig);
	}).add;


	SynthDef(\prm_concatGendyPitched, {
		|
		inBus, outBus, amp = 1, matchBus,
		storeSize = 1, seekTime = 1, seekDur = 1, matchLength = 0.05,
		freeze = 0, inputAmpModFreq = 2.1, matchLengthRange = 0.01,
		spread = 0.01, ampScale = 0.05, durScale = 5,
		freqMul1 = 1, freqMul2 = 1, freqMul3 = 1, freqMul4 = 0.5, freqMul5 = 2
		|
		var input, gendy1, gendy2, gendy3, gendy4, gendy5;
		var matchInput, control, inputAmpMod, frequency, amplitude, concat, sig;

		control = In.ar(inBus, 2);
		matchInput = In.kr(matchBus).range(matchLengthRange.neg, matchLengthRange);
		frequency = Tartini.kr(control);
		amplitude = Amplitude.kr(control);

		inputAmpMod = SinOsc.ar(inputAmpModFreq);

		gendy1 = Gendy3.ar(3, 4, 1, 1, frequency * freqMul1, ampScale, durScale);
		gendy2 = Gendy3.ar(3, 4, 1, 1, (frequency * freqMul2 * (1 + (spread*1))), ampScale, durScale);
		gendy3 = Gendy3.ar(3, 4, 1, 1, (frequency * freqMul3 * (1 - (spread*1))), ampScale, durScale);
		gendy4 = Gendy3.ar(3, 4, 1, 1, (frequency * freqMul4 * (1 + (spread*2))), ampScale, durScale);
		gendy5 = Gendy3.ar(3, 4, 1, 1, (frequency * freqMul5 * (1 - (spread*2))), ampScale, durScale);

		input = Mix([gendy1, gendy2, gendy3, gendy4, gendy5]);
		input = input * inputAmpMod;

		concat = Concat2.ar(control, input, storeSize, seekTime, seekDur, matchLength + matchInput, freeze, 1, 0, 1, 1);
		sig = concat * amplitude;
		sig = sig * amp;
		Out.ar(outBus, sig);
	}).add;




		{

Mix(Gendy3.ar(3,5,1.0,1.0,(Array.fill(5,{LFNoise0.kr(1.3.rand,1,2)})*MouseY.kr(100,3780,'exponential')),MouseY.kr(0.01,0.05),MouseY.kr(0.001,0.016),5,mul:0.1));
		}.play

			control=SoundIn.ar;

amp=Amplitude.ar(control);

concat= Concat.ar(control,input,1.0,1.0,1.0,MouseX.kr(0.0,0.1),0,1.0,0.0,1.0,1.0);

Out.ar(0,Pan2.ar(10*concat*amp,0.0))}.play
	