/*
Wednesday, August 3rd 2022
ConcatGlitch.sc
prm
*/

ConcatGlitch : IM_Processor {

	var server, <isLoaded;

	var lfoGroup, <input;

	var lfo, synth;

	var <storeSize, <seekTime, <seekDur, <matchLength;
	var <controlAmpModFreq, <matchLengthRange;
	var <ampTrack, <ampTrackMod, <isFrozen;

	var <mix;

	*newMono {
		| outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, feedback = false, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitMono;
	}

	*newStereo {
		| outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, feedback = false, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitStereo;
	}

	prInitMono {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDefs;

			server.sync;

			lfoGroup = Group.new(group, \addBefore);

			server.sync;

			lfo = LFO.new(3, 'noise', relGroup: lfoGroup, addAction: \addToHead);
			while({ try { lfo.isLoaded } != true }, { 0.001.wait; });

			this.prSetInitialParameters;

			synth = Synth(\prm_concatGlitch_stereo,
				[
					\inBus, inBus, \outBus, mixer.chanMono(0), \matchBus, lfo.outBus,
					\storeSize, storeSize, \seekTime, seekTime, \seekDur, seekDur, \matchLength, matchLength,
					\controlAmpModFreq, controlAmpModFreq, \matchLengthRange, matchLengthRange,
					\ampTrack, ampTrack, \ampTrackMod, ampTrackMod, \freeze, 0
				],
				group, \addToHead);

			server.sync;

			isLoaded = true;
		}
	}

	prInitStereo {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDefs;

			server.sync;

			lfoGroup = Group.new(group, \addBefore);

			server.sync;

			lfo = LFO.new(3, 'noise', relGroup: lfoGroup, addAction: \addToHead);
			while({ try { lfo.isLoaded } != true }, { 0.001.wait; });

			this.prSetInitialParameters;

			synth = Synth(\prm_concatGlitch_stereo,
				[
					\inBus, inBus, \outBus, mixer.chanStereo(0), \matchBus, lfo.outBus,
					\storeSize, storeSize, \seekTime, seekTime, \seekDur, seekDur, \matchLength, matchLength,
					\controlAmpModFreq, controlAmpModFreq, \matchLengthRange, matchLengthRange,
					\ampTrack, ampTrack, \ampTrackMod, ampTrackMod, \freeze, 0
				],
				group, \addToHead);

			server.sync;

			isLoaded = true;
		}
	}

	prAddSynthDefs {
		SynthDef(\prm_concatGlitch_stereo, {
			|
			inBus, outBus, amp = 1, matchBus,
			storeSize = 4, seekTime = 4, seekDur = 4, matchLength = 0.3,
			freeze = 0, controlAmpModFreq = 0.5, matchLengthRange = 0.1,
			ampTrack = 0, ampTrackMod = 15, mix = 0.5
			|
			var input, matchInput, control, controlAmpMod, amplitude, concat, sig;
			input = In.ar(inBus, 2);
			matchInput = In.kr(matchBus).range(matchLengthRange.neg, matchLengthRange);
			amplitude = Amplitude.ar(input);
			controlAmpMod = SinOsc.ar(controlAmpModFreq);
			control = Saw.ar(
				SinOsc.kr(LFNoise0.kr(LFNoise1.kr(1).range(0.01, 1), 3, 4.5), 0, 50, LFNoise1.kr(1.5).range(120, 500)));
			control = control * controlAmpMod;
			concat = Concat2.ar(control, input, storeSize, seekTime, seekDur, matchLength + matchInput, freeze, 0.5, 1.0, 0.5, 0.0);
			sig = ((concat * amplitude * ampTrackMod) * ampTrack) + (concat * (1-ampTrack));
			//sig = concat * amp;
			sig = sig * amp;

			sig = (sig*mix) + (input * (1-mix));

			Out.ar(outBus, sig);
		}).add;


		SynthDef(\prm_concatGlitch_mono, {
			|
			inBus, outBus, amp = 1, matchBus,
			storeSize = 4, seekTime = 4, seekDur = 4, matchLength = 0.3,
			freeze = 0, controlAmpModFreq = 0.5, matchLengthRange = 0.1,
			ampTrack = 0, ampTrackMod = 15
			|
			var input, matchInput, control, controlAmpMod, amplitude, concat, sig;
			input = In.ar(inBus);
			matchInput = In.kr(matchBus).range(matchLengthRange.neg, matchLengthRange);
			amplitude = Amplitude.ar(input);
			controlAmpMod = SinOsc.ar(controlAmpModFreq);
			control = Saw.ar(
				SinOsc.kr(LFNoise0.kr(LFNoise1.kr(1).range(0.01, 1), 3, 4.5), 0, 50, LFNoise1.kr(1.5).range(120, 500)));
			control = control * controlAmpMod;
			concat = Concat2.ar(control, input, storeSize, seekTime, seekDur, matchLength + matchInput, freeze, 0.5, 1.0, 0.5, 0.0);
			sig = ((concat * amplitude * ampTrackMod) * ampTrack) + (concat * (1-ampTrack));
			//sig = concat * amp;
			sig = sig * amp;
			Out.ar(outBus, sig);
		}).add;

	}

	prSetInitialParameters {
		storeSize = 4; seekTime = 2; seekDur = 2; matchLength = 0.3;
		controlAmpModFreq = 0.5; matchLengthRange = 0.1;
		ampTrack = 0; ampTrackMod = 15; isFrozen = false;
		mix = 0.5;

	}

	//////// public functions:

	free {
		synth.free;
		lfoGroup.free; lfo.free;

	}

	setMix { | m = 0.5 |
		mix = m;
		synth.set(\mix, mix);
	}



}

