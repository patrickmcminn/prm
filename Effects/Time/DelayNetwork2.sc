/*
Wednesday, December 4th 2019
DelayNetwork2.sc
prm

Like DelayNetwork except using Comb delays
with decay times instead of feedback loops
*/

DelayNetwork2 : IM_Module {

	var <isLoaded, server;
	var delayArray, shiftArray;
	var <subMixer, <dry, splitter;
	var delayBus;
	var isStereo;

	*newMono {
		|
		outBus = 0, numDelays = 5, delayTimeLow = 0.35, delayTimeHigh = 1.2, maxDelay = 3,
		send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead'
		|
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono(numDelays, delayTimeLow, delayTimeHigh, maxDelay);
	}

	*newStereo {
		|
		outBus = 0, numDelays = 5, delayTimeLow = 0.35, delayTimeHigh = 1.2, maxDelay = 3,
		send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead'
		|
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo(numDelays, delayTimeLow, delayTimeHigh, maxDelay);
	}

	prInitMono { | numDelays, delayTimeLow, delayTimeHigh, maxDelay |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			isStereo = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			dry = IM_Mixer_1Ch.new(mixer.chanMono(0), relGroup: group, addAction: \addToHead);
			while({ try { dry.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDefs;
			shiftArray = Array.fill(1, 0);
			delayBus = Bus.audio(server, 1);
			server.sync;

			// subMixer for delays:
			subMixer = IM_Mixer.new(numDelays, mixer.chanStereo, relGroup: group, addAction: \addToHead);
			while({ try { subMixer.isLoaded } != true }, { 0.001.wait; });

			this.prMakeMonoSynths(numDelays, maxDelay, delayTimeLow, delayTimeHigh);

			server.sync;

			splitter = Splitter.newMono(2, [dry.inBus, delayBus], relGroup: group, addAction: \addToHead);
			while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

			isLoaded = true;
		};
	}

	prInitStereo { | numDelays, delayTimeLow, delayTimeHigh, maxDelay |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			isStereo = true;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			dry = IM_Mixer_1Ch.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
			while({ try { dry.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDefs;
			shiftArray = Array.fill(1, 0);
			delayBus = Bus.audio(server, 2);
			server.sync;

			// subMixer for delays:
			subMixer = IM_Mixer.new(numDelays, mixer.chanStereo, relGroup: group, addAction: \addToHead);
			while({ try { subMixer.isLoaded } != true }, { 0.001.wait; });

			this.prMakeStereoSynths(numDelays, maxDelay, delayTimeLow, delayTimeHigh);

			server.sync;

			splitter = Splitter.newStereo(2, [dry.inBus, delayBus], relGroup: group, addAction: \addToHead);
			while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

			isLoaded = true;
		};
	}

	prAddSynthDefs {

		SynthDef(\prm_delayNetwork2_mono, {
			|
			inBus = 0, outBus = 0, amp = 1, delayTime = 0.3, maxDelayTime = 5, decayTime = 1,
			filterType = 0, cutoff = 20000, res = 0, shiftAmount = 0
			|

			var input, delay, filter, pitchShift, sig;

			input = In.ar(inBus, 1);
			delay = CombC.ar(input, maxDelayTime, delayTime, decayTime);
			filter = BMoog.ar(delay, cutoff, res, filterType);
			pitchShift = PitchShift.ar(filter, 0.1, shiftAmount.midiratio, 0, 0.05);
			sig = pitchShift * amp;
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_delayNetwork2_stereo, {
			|
			inBus = 0, outBus = 0, amp = 1, delayTime = 0.3, maxDelayTime = 5, decayTime = 1,
			filterType = 0, cutoff = 20000, res = 0, shiftAmount = 0
			|

			var input, delay, filter, pitchShift, sig;

			input = In.ar(inBus, 2);
			delay = CombC.ar(input, maxDelayTime, delayTime, decayTime);
			filter = BMoog.ar(delay, cutoff, res, filterType);
			pitchShift = PitchShift.ar(filter, 0.1, shiftAmount.midiratio, 0, 0.05);
			sig = pitchShift * amp;
			Out.ar(outBus, sig);
		}).add;

	}


	prMakeMonoSynths { | numDelays, maxDelay, delayTimeLow, delayTimeHigh |
		delayArray = Array.fill(numDelays, { | i |
			var shift;
			shift = shiftArray.choose;
			Synth(\prm_delayNetwork2_mono,
				[
					\inBus, delayBus, \outBus, subMixer.chanMono(i),
					\maxDelay, maxDelay, \delayTime, rrand(delayTimeLow, delayTimeHigh),
					\decayTime, 1, \shiftAmount, shift
			], group, \addToHead);
		});
	}

	prMakeStereoSynths { | numDelays, maxDelay, delayTimeLow, delayTimeHigh |
		delayArray = Array.fill(numDelays, { | i |
			var shift;
			shift = shiftArray.choose;
			Synth(\prm_delayNetwork2_stereo,
				[
					\inBus, delayBus, \outBus, subMixer.chanStereo(i),
					\maxDelay, maxDelay, \delayTime, rrand(delayTimeLow, delayTimeHigh),
					\decayTime, 1, \shiftAmount, shift
			], group, \addToHead);
		});
	}


	//////// Public Functions:

	inBus { ^splitter.inBus; }

	free {
		isLoaded = false;
		subMixer.free;
		delayArray.do({ | synth | synth.free; });
		dry.free;
		delayBus.free;
		this.freeModule;
	}

	numDelays { ^delayArray.size }

	addDelay { | delayTime = 0.1, decayTime = 1, maxDelay = 3, filterType = 0,
		cutoff = 20000, rq = 1, shiftAmount = 0 |
		subMixer.addStrip;
		if( isStereo == false,
			{
				delayArray = delayArray.add(Synth(\prm_delayNetwork2_mono,
					[\outBus, mixer.chanMono(delayArray.size),\delayTime, delayTime,
						\decayTime, decayTime, \maxDelay, maxDelay,
						\filterType, filterType, \cutoff, cutoff, \rq, rq, \shiftAmount, shiftAmount],
					group, \addToHead));
			},
			{
				delayArray = delayArray.add(Synth(\prm_delayNetwork2_stereo,
					[\outBus, mixer.chanStereo(delayArray.size),\delayTime, delayTime,
						\decayTime, decayTime, \maxDelay, maxDelay,
						\filterType, filterType, \cutoff, cutoff, \rq, rq, \shiftAmount, shiftAmount],
					group, \addToHead));
			}
		);
	}

	/////// addressing individual delays:

	setDelayTime { | delayNum = 0, delayTime = 1 |
		if( delayArray.size > delayNum,
			{ delayArray.at(delayNum).set(\delayTime, delayTime); },
			{ ^"Delay Synth Does Not Exist at This Index"; });
	}

	setDecayTime { | delayNum = 0, decayTime = 1 |
		if( delayArray.size > delayNum,
			{ delayArray.at(delayNum).set(\decayTime, decayTime); },
			{ ^"Delay Synth Does Not Exist at This Index"; });
	}

	setFilterType { | delayNum = 0, type = 0 |
		if( delayArray.size > delayNum,
			{ delayArray.at(delayNum).set(\filterType, type); },
			{ ^"Delay Synth Does Not Exist at This Index"; });
	}

	setCutoff { | delayNum = 0, cutoff = 1000 |
		if( delayArray.size > delayNum,
			{ delayArray.at(delayNum).set(\cutoff, cutoff); },
			{ ^"Delay Synth Does Not Exist at This Index"; });
	}

	setRes { | delayNum = 0, res = 0 |
		if( delayArray.size > delayNum,
			{ delayArray.at(delayNum).set(\res, res); },
			{ ^"Delay Synth Does Not Exist at This Index"; });
	}

	setPan { | delayNum = 0, pan = 0 |
		if( delayArray.size > delayNum,
			{ subMixer.setPanBal(delayNum, pan); },
			{ ^"Delay Synth Does Not Exist at This Index"; });
	}

	setPitchShift { | delayNum = 0, shiftAmount = 0 |
		if( delayArray.size > delayNum,
			{ delayArray.at(delayNum).set(\shiftAmount, shiftAmount); },
			{ ^"Delay Synth Does Not Exist at This Index"; });
	}

	///// addressing the entire delay array:

	randomizeParameters {
		|
		delayTimeLow = 0.1, delayTimeHigh = 1, decayTimeLow = 0.5, decayTimeHigh = 1,
		cutoffLow = 1300, cutoffHigh = 20000, resLow = 0, resHigh = 0.7, panLow = -1, panHigh = 1,
		shiftArray = 0
		|
		delayArray.do({ | synth |
			var shift;
			if( shiftArray.isArray, { shift = shiftArray.choose; }, { shift = shiftArray });
			synth.set(
				\delayTime, rrand(delayTimeLow, delayTimeHigh),
				\decayTime, rrand(decayTimeLow, decayTimeHigh),
				\cutoff, rrand(cutoffLow, cutoffHigh),
				\res, rrand(resLow, resHigh),
				\shiftAmount, shift
			);
		});
		^"parameters randomized";
	}

	randomizeDelayTime { | delayTimeLow = 0.15, delayTimeHigh = 1 |
		delayArray.do({ | synth |
			synth.set(\delayTime, rrand(delayTimeLow, delayTimeHigh));
		});
	}

	randomizeDecayTime { | decayTimeLow = 0.5, decayTimeHigh = 1 |
		delayArray.do({ | synth |
			synth.set(\decayTime, rrand(decayTimeLow, decayTimeHigh));
		});
	}

	randomizeCutoff { | cutoffLow = 700, cutoffHigh = 2500 |
		delayArray.do({ | synth |
			synth.set(\cutoff, exprand(cutoffLow, cutoffHigh));
		});
	}

	randomizeRes { | resLow = 0, resHigh = 0.5 |
		delayArray.do({ | synth |
			synth.set(\res, rrand(resLow, resHigh));
		});
	}

	randomizePan { | panLow = -1, panHigh = 1 |
		delayArray.size.do({ | chan |
			subMixer.setPanBal(chan, rrand(panLow, panHigh));
		});
	}

	setPitchShiftArray { | shiftArray |
		delayArray.do({ | synth | synth.set(\shiftAmount, shiftArray.choose;) });
	}

}
