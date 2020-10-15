
a = Bus.audio(s, 2);
b = Bus.control(s, 1);

~lfo = LFO.new(1);
~input = { Out.ar(a, Pan2.ar(SoundIn.ar(1)) ) }.play;

~lfo.setWaveform(\noise);
~lfo.setFrequency(2);

~cGendy1.set(\baseFreq, 1100);
~cGendy1.set(\baseModFreq, 12);
~cGendy2.free
~cGendy1.set(\durScale, 0.5);

~cGlitch = Synth(\prm_concatGlitch,
	[\inBus, a, \outBus, 0, \matchBus, ~lfo.outBus], ~lfo.synth, \addAfter);
~cGlitch.free;

~cGendy1 = Synth(\prm_concatGendyUnpitched, [\inBus, a, \outBus, 0, \matchBus, ~lfo.outBus], ~lfo.synth, \addAfter);
~cGendy2 = Synth(\prm_concatGendyPitched, [\inBus, a, \outBus, 0, \matchBus, ~lfo.outBus], ~lfo.synth, \addAfter);
~cGendy2.free;
~cGlitch.set(\matchLength, 0.3, \matchLengthRange, 0.25);

~cGendy2.set(\matchLength, 0.1, \matchLengthRange, 0.05, \ampModFreq, 1);
~cGendy2.set(\freqMul1, 2, \freqMul2, 4, \freqMul3, 0.5, \freqMul4, 0.5, \freqMul5, 0.25);
~cGendy2.set(\drift, 0.1);
~cGlitch.set(\ampTrack, 0, \ampTrackMod, 15);
s.meter

~cGendy2.set(\matchLengthRange, 0, \ampTrack, 0);

SynthDef(\prm_concatGlitch, {
	|
	inBus, outBus, amp = 1, matchBus,
	storeSize = 4, seekTime = 4, seekDur = 4, matchLength = 0.3,
	freeze = 0, controlAmpModFreq = 0.5, matchLengthRange = 0.1,
	ampTrack = 0, ampTrackMod = 15
	|
	var input, matchInput, control, controlAmpMod, amplitude, concat, sig;
	input = In.ar(inBus, 2);
	matchInput = In.kr(matchBus).range(matchLengthRange.neg, matchLengthRange);
	amplitude = Amplitude.ar(input);
	controlAmpMod = SinOsc.ar(controlAmpModFreq);
	control = Saw.ar(SinOsc.kr(LFNoise0.kr(LFNoise1.kr(1).range(0.01, 1), 3, 4.5), 0, 50, LFNoise1.kr(1.5).range(120, 500)));
	control = control * controlAmpMod;
	concat = Concat2.ar(control, input, storeSize, seekTime, seekDur, matchLength + matchInput, freeze, 0.5, 1.0, 0.5, 0.0);
	sig = ((concat * amplitude * ampTrackMod) * ampTrack) + (concat * (1-ampTrack));
	//sig = concat * amp;
	sig = sig * amp;
	Out.ar(outBus, sig);
}).add;
~cGlitch.free;


SynthDef(\prm_concatGendyUnpitched, {
	|
	inBus, outBus, amp = 1, matchBus,
	inputAmpModFreq = 2, baseFreq = 400, baseModFreq = 1,
	matchLength = 0.05, matchLengthRange = 0.01, freeze = 0,
	ampScale = 0.03, durScale = 0.007
	ampTrackMod = 10,
	storeSize = 1.0, seekTime = 1.0, seekDur = 1.0
	|

	var input,  control, matchInput, concat,  inputAmpMod, amplitude, sig;

	control= In.ar(inBus, 2);
	amplitude = Amplitude.ar(control) * ampTrackMod;
	matchInput = In.kr(matchBus).range(matchLengthRange.neg, matchLengthRange);

	inputAmpMod = SinOsc.ar(inputAmpModFreq);
	input = Mix(
		Gendy3.ar(3,5,1.0,1.0,
			(Array.fill(5,{LFNoise0.kr(rrand(0.1, 2)*baseModFreq).range(0.1, 2)}) * baseFreq),
			amplitude.range(0.01, 0.05), amplitude.range(0.001, 0.016),
			//LFNoise2.kr(1.1*baseModFreq).range(0.01,0.05),
			//LFNoise2.kr(1.3*baseModFreq).range(0.001,0.016),
			//ampScale, durScale,
			12,mul:0.1));
	input = input * inputAmpMod;

	concat= Concat.ar(control,input, storeSize, seekTime, seekDur, matchLength + matchInput, freeze, 1.0,0.0,1.0,1.0);

	sig = concat * amplitude;
	sig = sig * amp;

	Out.ar(outBus, sig);
}).add;


SynthDef(\prm_concatGendyPitched, {
	|
	inBus, outBus, amp = 1, matchBus,
	freqMul1 = 1, freqMul2 = 1, freqMul3 = 1, freqMul4 = 0.5, freqMul5 = 2,
	drift = 0.005,
	inputAmpModFreq = 2.1, baseModFreq = 1,
	matchLength = 0.1, matchLengthRange = 0.05, freeze = 0,
	ampTrackMod = 6,
	storeSize = 1, seekTime = 1, seekDur = 1
	|

	var input, control, matchInput, concat, inputAmpMod;
	var amplitude, frequency, hasFrequency, sig;
	var gendy1, gendy2, gendy3, gendy4, gendy5;

	control= In.ar(inBus, 2);
	amplitude = Amplitude.ar(control) * ampTrackMod;
	amplitude = amplitude.lag(0.2);
	# frequency, hasFrequency = Tartini.kr(control[0]);
	matchInput = In.kr(matchBus).range(matchLengthRange.neg, matchLengthRange);

	//inputAmpMod = SinOsc.ar(inputAmpModFreq);
	gendy1 = Gendy3.ar(3, 5, 1.0, 1.0, (frequency * freqMul1),
		amplitude.range(0.01, 0.03), amplitude.range(0.001, 0.014), 12);
		//0.5, 0.5, 5);
	gendy2 = Gendy3.ar(3, 5, 1.0, 1.0, (frequency * freqMul2 * (1+drift)),
		amplitude.range(0.02, 0.04), amplitude.range(0.0009, 0.01), 12);
		//0.5, 0.5, 5);
	gendy3 = Gendy3.ar(3, 5, 1.0, 1.0, (frequency * freqMul2 * (1-drift)),
		amplitude.range(0.02, 0.04), amplitude.range(0.001, 0.012), 12);
		//0.5, 0.5, 5);
	gendy4 = Gendy3.ar(3, 5, 1.0, 1.0, (frequency * freqMul2 * (1+(drift*2))),
		amplitude.range(0.03, 0.02), amplitude.range(0.0001, 0.011), 12);
		//0.5, 0.5, 5);
	gendy5 = Gendy3.ar(3, 5, 1.0, 1.0, (frequency * freqMul2 * (1-(drift*2))),
		amplitude.range(0.01, 0.02), amplitude.range(0.001, 0.0101), 12);
		//0.5, 0.5, 5);
	input = Mix([gendy1, gendy2, gendy3, gendy4, gendy5]);
	input = input * 0.1;
	//input = input * inputAmpMod;

	concat= Concat.ar(control ,input, storeSize, seekTime, seekDur, matchLength + matchInput, freeze, 1.0,0.0,1.0,1.0);
	concat = HPF.ar(concat, 40);

	sig = concat * amplitude;
	sig = sig * amp;

	Out.ar(outBus, sig);
}).add;

s.meter

~cGendy1.free;

~cGendy2.free;


