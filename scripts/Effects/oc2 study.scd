Tartini(
	a= SynthDef("testtartini",{arg infreq=440;
		var in, freq, hasFreq, out;

		in=SinOsc.ar(infreq);

		# freq, hasFreq = Tartini.kr(in);

		freq.poll;

		Out.ar(0,[SinOsc.ar(freq,0.1),in]);
	}).play(s);
)

SynthDef(\prm_oc2_ampMod_stereo, {
	| inBus, outBus, dryAmp = 0.5, oct1Amp = 0.5, oct2Amp = 0.2, amp = 1 |
	var input,stereoSum, freq, hasFreq, saw1, saw2, mod1, mod2, filter1, filter2, sig;
	input = In.ar(inBus, 2);
	stereoSum = Mix.new([input[0], input[1]]);
	# freq, hasFreq =  Tartini.kr(stereoSum);
	saw1 = Pulse.ar(freq/2, 0.5);
	filter1 = LPF.ar(saw1, ((freq/2)*3/2));
	saw2 = Pulse.ar(freq/4, 0.5);
	filter2 = LPF.ar(saw2, ((freq/4)*3/2));
	mod1 = input * (filter1 * 0.5);
	mod2 = input * (filter2 * 0.5);
	sig = (input * dryAmp) + (mod1 * oct1Amp) + (mod2*oct2Amp);
	sig = sig * amp;
	Out.ar(outBus, sig);
}).add;


EnvFollow

// nope:
/*

SynthDef(\prm_oc2_ff_stereo, {
	|
	inBus, outBus, dryAmp = 0.5, oct1Amp = 0.5, oct2Amp = 0.2, amp = 1
	inFilterCutoff = 500, filterCutoff = 2500, res = 0
	|
	var input, ampTrack, inFilter, oct1, oct2, octSum, sig;
	input = In.ar(inBus, 2);
	input = LeakDC.ar(input, 0.995);
	ampTrack = Amplitude.kr(input);
	inFilter = LPF.ar(input, inFilterCutoff);
	oct1 = ToggleFF.ar(inFilter);
	oct2 = ToggleFF.ar(oct1);
	octSum = (oct1 * oct1Amp) + (oct2 * oct2Amp);
	octSum = octSum * ampTrack;
	//octSum = octSum.tanh;
	octSum = DFM1.ar(octSum, filterCutoff, res);
	sig = (input*dryAmp) + octSum;
	sig = sig * amp;
	Out.ar(outBus, sig);
}).add;
*/


z.set(\filterCutoff, 1500);
z.set(\inFilterCutoff, 3500);
z.set(\oct1Amp, 6.dbamp);
z.set(\oct2Amp, 6.dbamp);
z.set(\dryAmp, 0);d



s.scope

~bus = Bus.audio(s, 2);

y = Synth(\prm_oc2_ampMod_stereo, [\inBus, ~bus, \outBus, ~prm.audioIn], ~prm.procGroup, \addToHead);
z.free;
x.free;

z = Synth(\prm_oc2_ff_stereo, [\inBus, ~bus, \outBus, ~prm.audioIn], ~prm.procGroup, \addToHead);

s.scope

z.set(\dryAmp, 0);
z.set(\oct2Amp, 1);

x = { Out.ar(~bus, SinOsc.ar([220, 330])) }.play

x.free;

x = IM_HardwareIn.newStereo(18, ~bus);

x = IM_HardwareIn.newStereo(18, ~prm.audioIn);

x.free;
y.set(\dryAmp, 1);
y.set(\oct2Amp, 0.75);
y.set(\oct1Amp, 1);
z.free;

x.free;
s.meter

Pitch

SynthDef(\prm_oc2_ff_stereo, {
	| inBus, outBus, dryAmp = 0.5, oct1Amp = 1, oct2Amp = 1, amp = 1
	inFilterCutoff = 500 |
	var input, proc, octave1, octave2, sig;
	input = In.ar(inBus, 2);
	proc = LeakDC.ar(input, 0.999);
	proc = LPF.ar(proc, inFilterCutoff);

	octave1 = ToggleFF.ar(proc);
	octave1 = octave1 * proc;
	octave1 = octave1 * oct1Amp;

	octave2 = ToggleFF.ar(octave1);
	octave2 = octave2 * proc;
	octave2 = octave2 * oct2Amp;

	sig = Mix.new([(input * dryAmp), octave1, octave2]);

	Out.ar(outBus, sig);
}).add;


~bus = Bus.audio(s, 2);
x = Synth(\prm_oc2_ff_stereo, [\inBus, ~bus, \outBus, ~prm.audioIn], ~prm.procGroup, \addToHead);

y = IM_Mixer_1Ch.newNoSends(~bus, false, ~prm.procGroup, \addToHead);

y = IM_HardwareIn.newStereo(18, ~bus, ~prm.procGroup, \addToHead);
z = IM_HardwareIn.new(1, y.chanMono, ~prm.procGroup, \addToHead);
x.free;
y.free;
z.free;

x.free

y.free;

x.set(\dryAmp, 1);
x.set(\oct1Amp, 1);
x.set(\oct2Amp, 0);

y.free;
z.free;

{ Out.ar(~prm.audioIn, SoundIn.ar(1)) }.play

s.meter



//// original code
(
// an emulation of the Boss OC-2 pedal
SynthDef("oc2", { arg out=0;

	var a, b, c, in, oct1, oct2, direct;
	in = LeakDC.ar(SoundIn.ar(1), 0.999); // substitute input of choice
	a = LPF.ar(in, 400); // lowpass filter input - experiment with cutoff freq
	b = ToggleFF.ar(a); // use flip-flop to generate square wave
	//an octave below input
	c = ToggleFF.ar(b); // square wave two octaves below input

	// the lowpass filtered signal is multiplied by the suboctave
	// square wave, which results in a signal an octave (or two octaves)
	// below the input signal, but with the dynamics of the input signal,
	// and a nice, semi-sinusoidal tone
	// oct1, oct2 and direct control the mix of the various suboctaves and
	// input signal

	oct1 = 1.0;
	oct2 = 1.0;
	direct = 1.0;

	Out.ar(out, in+(a*c) + (a*b));

	/*
	Out.ar(out,
	a*b*oct1 + a*c*oct2 + in*direct
	)
	*/
}).play;
)

s.meter