/*
Wednesday, October 14th 2020
Concaterwaul.sc
prm
*/

Concaterwaul : IM_Module {

	var server, <isLoaded;
	var <subMixer;
	var <dry, <glitch, <unPitch, <pitch;
	var <preEQ, <postEQ;
	var dryBus, glitchBus, unPitchBus, pitchBus;
	var <lfo, lfoGroup, <splitter;

	var <isFrozen, <glitchAmpIsTracking;
	var <freqMul1, <freqMul2, <freqMul3, <freqMul4, <freqMul5;
	var <detune;
	var <glitchLength, <glitchModRange;
	var <unPitchLength, <unPitchModRange, <pitchLength, <pitchModRange;

	var <baseFreq, <baseModFreq, <glitchAmpMod, <unPitchAmpMod;

	*newMono { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, feedback = false, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitMono;
	}

	*newStereo {
		| outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, feedback = false, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitStereo;
	}

	prInitMono {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDefs;

			dryBus = Bus.audio(server, 2);
			glitchBus = Bus.audio(server, 2);
			unPitchBus = Bus.audio(server, 2);
			pitchBus = Bus.audio(server, 2);

			lfoGroup = Group.new(group, \addBefore);

			server.sync;

			postEQ = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { postEQ.isLoaded } != true }, { 0.001.wait; });

			subMixer = IM_Mixer.newNoSends(4, postEQ.inBus, false, group, \addToHead);
			while({ try { subMixer.isLoaded } != true }, { 0.001.wait; });

			lfo = LFO.new(1, 'noise', relGroup: lfoGroup, addAction: \addToHead);
			while({ try { lfo.isLoaded } != true }, { 0.001.wait; });
			/*
			glitchEQ = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { glitchEQ.isLoaded } != true }, { 0.001.wait; });

			unPitchEQ = Equalizer.newStereo(mixer.chanStereo(1), group, \addToHead);
			while({ try { unPitchEQ.isLoaded } != true }, { 0.001.wait; });

			pitchEQ = Equalizer.newStereo(mixer.chanStereo(2), group, \addToHead);
			while({ try { pitchEQ.isLoaded } != true }, { 0.001.wait; });

			dryEQ = Equalizer.newStereo(mixer.chanStereo(3), group, \addToHead);
			while({ try { dryEQ.isLoaded } != true }, { 0.001.wait; });
			*/

			glitch = Synth(\prm_concatGlitch,
				[\inBus, glitchBus, \outBus, subMixer.chanStereo(0), \matchBus, lfo.outBus],
				group, \addToHead);
			unPitch = Synth(\prm_concatGendyUnpitched, [\inBus, unPitchBus, \outBus, subMixer.chanStereo(1), \matchBus, lfo.outBus],
				group, \addToHead);
			pitch = Synth(\prm_concatGendyPitched, [\inBus, pitchBus, \outBus, subMixer.chanStereo(2), \matchBus, lfo.outBus],
				group, \addToHead);
			while( { try { pitch.isNil } == true }, { 0.001.wait; });

			splitter = Splitter.newStereo(4, [glitchBus, unPitchBus, pitchBus, subMixer.chanStereo(3)], false,
				group, \addToHead);
			while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

			preEQ = Equalizer.newMono(splitter.inBus, group, \addToHead);
			while({ try { preEQ.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;
			isLoaded = true;

		}
	}

	prInitStereo {
	server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDefs;

			dryBus = Bus.audio(server, 2);
			glitchBus = Bus.audio(server, 2);
			unPitchBus = Bus.audio(server, 2);
			pitchBus = Bus.audio(server, 2);

			lfoGroup = Group.new(group, \addBefore);

			server.sync;

			postEQ = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { postEQ.isLoaded } != true }, { 0.001.wait; });

			subMixer = IM_Mixer.newNoSends(4, postEQ.inBus, false, group, \addToHead);
			while({ try { subMixer.isLoaded } != true }, { 0.001.wait; });

			lfo = LFO.new(3, 'noise', relGroup: lfoGroup, addAction: \addToHead);
			while({ try { lfo.isLoaded } != true }, { 0.001.wait; });
			/*
			glitchEQ = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { glitchEQ.isLoaded } != true }, { 0.001.wait; });

			unPitchEQ = Equalizer.newStereo(mixer.chanStereo(1), group, \addToHead);
			while({ try { unPitchEQ.isLoaded } != true }, { 0.001.wait; });

			pitchEQ = Equalizer.newStereo(mixer.chanStereo(2), group, \addToHead);
			while({ try { pitchEQ.isLoaded } != true }, { 0.001.wait; });

			dryEQ = Equalizer.newStereo(mixer.chanStereo(3), group, \addToHead);
			while({ try { dryEQ.isLoaded } != true }, { 0.001.wait; });
			*/

			glitch = Synth(\prm_concatGlitch,
				[\inBus, glitchBus, \outBus, subMixer.chanStereo(0), \matchBus, lfo.outBus],
				group, \addToHead);
			unPitch = Synth(\prm_concatGendyUnpitched, [\inBus, unPitchBus, \outBus, subMixer.chanStereo(1), \matchBus, lfo.outBus],
				group, \addToHead);
			pitch = Synth(\prm_concatGendyPitched, [\inBus, pitchBus, \outBus, subMixer.chanStereo(2), \matchBus, lfo.outBus],
				group, \addToHead);
			while( { try { pitch.isNil } == true }, { 0.001.wait; });

			splitter = Splitter.newStereo(4, [glitchBus, unPitchBus, pitchBus, subMixer.chanStereo(3)], false,
				group, \addToHead);
			while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

			preEQ = Equalizer.newMono(splitter.inBus, group, \addToHead);
			while({ try { preEQ.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;
			isLoaded = true;

		}
	}

	prSetInitialParameters {

		isFrozen = false; glitchAmpIsTracking = false;
		freqMul1 = 1; freqMul2 = 1; freqMul3 = 1; freqMul4 = 0.5; freqMul5 = 2;
		detune = 0.05;
		glitchLength = 0.3; glitchModRange = 0.1;
		unPitchLength = 0.05; unPitchModRange = 0.01;
		pitchLength = 0.1; pitchModRange = 0.05;

		baseFreq = 400; baseModFreq = 1;
		glitchAmpMod = 0.5; unPitchAmpMod = 2;

		subMixer.setPreVol(0, -12);
		subMixer.setPreVol(1, -12);
		subMixer.setPreVol(2, -12);
		subMixer.setPreVol(3, -12);
		preEQ.setHighPassCutoff(100);
		subMixer.setVol(0, -21);
		subMixer.setVol(1, -6);
		subMixer.setVol(2, -15);
		subMixer.setVol(3, -12);
		subMixer.mute(3);
	}

	prAddSynthDefs {
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
			control = Saw.ar(
				SinOsc.kr(LFNoise0.kr(LFNoise1.kr(1).range(0.01, 1), 3, 4.5), 0, 50, LFNoise1.kr(1.5).range(120, 500)));
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
			gendy3 = Gendy3.ar(3, 5, 1.0, 1.0, (frequency * freqMul3 * (1-drift)),
				amplitude.range(0.02, 0.04), amplitude.range(0.001, 0.012), 12);
			//0.5, 0.5, 5);
			gendy4 = Gendy3.ar(3, 5, 1.0, 1.0, (frequency * freqMul4 * (1+(drift*2))),
				amplitude.range(0.03, 0.02), amplitude.range(0.0001, 0.011), 12);
			//0.5, 0.5, 5);
			gendy5 = Gendy3.ar(3, 5, 1.0, 1.0, (frequency * freqMul5 * (1-(drift*2))),
				amplitude.range(0.01, 0.02), amplitude.range(0.001, 0.0101), 12);
			//0.5, 0.5, 5);
			input = Mix([gendy1, gendy2, gendy3, gendy4, gendy5]);
			input = input * 0.5;
			//input = input * inputAmpMod;
			concat= Concat.ar(control ,input, storeSize, seekTime, seekDur, matchLength + matchInput, freeze, 1.0,0.0,1.0,1.0);
			concat = HPF.ar(concat, 40);
			sig = concat * amplitude;
			sig = sig * amp;

			Out.ar(outBus, sig);
		}).add;
	}

	//////// public functions:

	free {
		dry.free; glitch.free; unPitch.free; pitch.free;
		//dryEQ.free; glitchEQ.free; unPitchEQ.free; pitchEQ.free;
		preEQ.free; postEQ.free; subMixer.free;
		dryBus.free; glitchBus.free; unPitchBus.free; pitchBus.free;
		lfo.free; lfoGroup.free; splitter.free;
		this.freeModule;

		isLoaded = false;
	}

	inBus { ^preEQ.inBus }

	freeze {
		isFrozen = true;
		glitch.set(\freeze, 1);
		unPitch.set(\freeze, 1);
		pitch.set(\freeze, 1);
	}
	unFreeze {
		isFrozen = false;
		glitch.set(\freeze, 0);
		unPitch.set(\freeze, 0);
		pitch.set(\freeze, 0);
	}
	toggleFreeze { if( isFrozen == true, { this.unFreeze }, { this.freeze }); }

	glitchTrackAmp { glitchAmpIsTracking = true; glitch.set(\ampTrack, 1); }
	glitchUnTrackAmp { glitchAmpIsTracking = false; glitch.set(\ampTrack, 0); }
	toggleGlitchTrackAmp { if( glitchAmpIsTracking == true, { this.glitchUnTrackAmp }, { this.glitchTrackAmp }); }

	setFreqMul1 { | mul = 1 | freqMul1 = mul; pitch.set(\freqMul1, freqMul1); }
	setFreqMul2 { | mul = 1 | freqMul2 = mul; pitch.set(\freqMul2, freqMul2); }
	setFreqMul3 { | mul = 1 | freqMul3 = mul; pitch.set(\freqMul3, freqMul3); }
	setFreqMul4 { | mul = 1 | freqMul4 = mul; pitch.set(\freqMul4, freqMul4); }
	setFreqMul5 { | mul = 1 | freqMul5 = mul; pitch.set(\freqMul5, freqMul5); }

	setDetune { | d = 0.005 | detune = d; pitch.set(\drift, d); }

	setGlitchLength { | length = 0.3 | glitchLength = length; glitch.set(\matchLength, glitchLength); }
	setGlitchModRange { | range = 0.1 | glitchModRange = range; glitch.set(\matchLengthRange, glitchModRange); }

	setUnPitchLength { | length = 0.05 | unPitchLength = length; unPitch.set(\matchLength, unPitchLength); }
	setUnPitchModRange { | range = 0.01 | unPitchModRange = range; unPitch.set(\matchLengthRange, unPitchModRange); }

	setPitchLength { | length = 0.1 | pitchLength = length; pitch.set(\matchLength, pitchLength); }
	setPitchModRange { | range = 0.05 | pitchModRange = range; pitch.set(\matchLengthRange, pitchModRange); }

	setBaseFreq { | freq = 400 | baseFreq = freq; unPitch.set(\baseFreq, baseFreq); }
	setBaseModFreq { | freq = 1 | baseModFreq = freq; unPitch.set(\baseModFreq, baseModFreq); }

	setGlitchAmpMod { | freq = 0.5 | glitchAmpMod = freq; glitch.set(\controlAmpModFreq, glitchAmpMod); }
	setUnPitchAmpMod { | freq = 2 | unPitchAmpMod = freq; unPitch.set(\inputAmpModFreq, unPitchAmpMod); }

}