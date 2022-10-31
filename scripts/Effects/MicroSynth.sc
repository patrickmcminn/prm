/*
Friday, August 5th 2022
MicroSynth.sc
prm
*/

MicroSynth : IM_Processor {

	var <isLoaded, server;
	var synth;

	var <threshold,  inFilterFreq;
	var	<dryVol, <subVol, <octVol, <squareVol;
	var	<startFrequency, <endFrequency, <filterTime, <attackTime, <filterRes;

	*newStereo { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
		^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo;
	}

	*newMono { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
		^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono;
	}

	prInitStereo {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDefs;

			server.sync;

			synth = Synth(\prm_microsynth_stereo, [\inBus, inBus, \outBus, mixer.chanStereo(0)], group, \addToHead);
			while({ try { synth.isNil }== true } , { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prInitMono {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDefs;

			server.sync;

			synth = Synth(\prm_microsynth_mono, [\inBus, inBus, \outBus, mixer.chanMono(0)], group, \addToHead);
			while({ try { synth.isNil }== true } , { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prAddSynthDefs {

		SynthDef(\prm_microsynth_mono, {
			|
			inBus, outBus, amp = 1, thresh = 0.05, inFilterFreq = 500,
			dryAmp = 0.5, subAmp = 0.2, octAmp = 0.2, squareAmp = 0.2,
			startFreq = 4500, endFreq = 4500, filterTime = 1, attackTime = 0, filterRes = 0
			|
			var input, onset, amplitude, freqSum, freq, hasFreq;
			var inFilter, subOctave, octave, square, waveSum;
			var filter, filterEnv, envelope;
			var sig;

			input = In.ar(inBus);
			onset = Coyote.kr(input, thresh: thresh, minDur: 0.05);
			//freqSum = Mix.new([input[0], input[1]]);
			amplitude = Amplitude.ar(input);
			# freq, hasFreq = Tartini.kr(input);

			// sub octave:
			inFilter = LeakDC.ar(input, 0.999);
			inFilter = LPF.ar(inFilter, inFilterFreq);
			subOctave = ToggleFF.ar(inFilter);
			subOctave = subOctave * inFilter;
			subOctave = subOctave * subAmp;


			// octave up:
			octave = PitchShift.ar(input, 0.05, 2.0, 0.0, 0.01);
			octave = octave.abs;
			octave = LeakDC.ar(octave);
			octave = octave * octAmp;

			square = Pulse.ar(freq);
			square = LPF.ar(square, 2500) * amplitude;
			square = square * squareAmp;

			waveSum = Mix.new([(input*dryAmp), subOctave, octave, square]);
			//waveSum = Mix.new([(input*dryAmp), subOctave, octave, dist]);

			filterEnv = EnvGen.kr(Env([endFreq, startFreq, endFreq], [0, filterTime], \exp), onset);
			envelope = EnvGen.kr(Env([1, 0, 1], [0, attackTime]), onset);

			filter = DFM1.ar(waveSum, filterEnv, filterRes);

			sig = filter * envelope;
			sig = sig * amp;

			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_microsynth_stereo, {
			|
			inBus, outBus, amp = 1, thresh = 0.05, inFilterFreq = 500,
			dryAmp = 0.5, subAmp = 0.2, octAmp = 0.2, squareAmp = 0.2,
			startFreq = 4500, endFreq = 4500, filterTime = 1, attackTime = 0, filterRes = 0
			|
			var input, onset, amplitude, freqSum, freq, hasFreq;
			var inFilter, subOctave, octave, square, waveSum;
			var filter, filterEnv, envelope;
			var sig;

			input = In.ar(inBus, 2);
			onset = Coyote.kr(input, thresh: thresh, minDur: 0.05);
			freqSum = Mix.new([input[0], input[1]]);
			amplitude = Amplitude.ar(freqSum);
			# freq, hasFreq = Tartini.kr(freqSum);

			// sub octave:
			inFilter = LeakDC.ar(input, 0.999);
			inFilter = LPF.ar(inFilter, inFilterFreq);
			subOctave = ToggleFF.ar(inFilter);
			subOctave = subOctave * inFilter;
			subOctave = subOctave * subAmp;


			// octave up:
			octave = PitchShift.ar(input, 0.05, 2.0, 0.0, 0.01);
			octave = octave.abs;
			octave = LeakDC.ar(octave);
			octave = octave * octAmp;

			square = Pulse.ar(freq);
			square = LPF.ar(square, 2500) * amplitude;
			square = square * squareAmp;

			waveSum = Mix.new([(input*dryAmp), subOctave, octave, square]);
			//waveSum = Mix.new([(input*dryAmp), subOctave, octave, dist]);

			filterEnv = EnvGen.kr(Env([endFreq, startFreq, endFreq], [0, filterTime], \exp), onset);
			envelope = EnvGen.kr(Env([1, 0, 1], [0, attackTime]), onset);

			filter = DFM1.ar(waveSum, filterEnv, filterRes);

			sig = filter * envelope;
			sig = sig * amp;

			Out.ar(outBus, sig);
		}).add;
	}

	prSetInitialParameters {
		threshold = 0.05; inFilterFreq = 500;
		dryVol = -6; subVol = -9; octVol = -24; squareVol = -18;
		startFrequency = 4500; endFrequency = 4500; filterTime = 1; attackTime = 0; filterRes = 0;
	}

	/////// public functions:

	free {
		synth.free;
		this.freeProcessor;
	}

	dontuse {
		var threshold,  inFilterFreq;
		var	dryVol, subVol, octVol, squareVol;
		var	startFrequency, endFrequency, filterTime, attackTime, filterRes;
	}

	setThreshold { | thresh = 0.05 |
		threshold = thresh;
		synth.set(\thresh, threshold);
	}

	setInFilterFreq { | freq = 500 |
		inFilterFreq = freq;
		synth.set(\inFilterFreq, inFilterFreq);
	}

	setDryVol { | vol = -6 |
		dryVol = vol;
		synth.set(\dryAmp, dryVol.dbamp);
	}
	setSubVol { | vol = -9 |
		subVol = vol;
		synth.set(\subAmp, subVol.dbamp);
	}
	setOctVol { | vol = -24 |
		octVol = vol;
		synth.set(\octAmp, octVol.dbamp);
	}
	setSquareVol { | vol = -18 |
		squareVol = vol;
		synth.set(\squareAmp, squareVol.dbamp);
	}

	setFilterTime { | time = 1 |
		filterTime = time;
		synth.set(\filterTime, filterTime);
	}
	setStartFrequency { | freq = 4500 |
		startFrequency = freq;
		synth.set(\startFreq, startFrequency);
	}
	setEndFrequency { | freq = 4500 |
		endFrequency = freq;
		synth.set(\endFreq, endFrequency);
	}
	setFilterRes { | res = 0 |
		filterRes = res;
		synth.set(\res, filterRes);
	}
	setAttackTime { | time = 0 |
		attackTime = time;
		synth.set(\attackTime, attackTime);
	}


} 