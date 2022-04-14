/*
Monday, April 4th 2022
Years_Ensemble.sc
prm
*/

Years_Ensemble : IM_Module {

	var <isLoaded, server;
	var <moog, <synths, <subtractive;
	var <delayNetwork1, <delayNetwork2, <splitter1, <splitter2;
	var <granulator, <distortion, <eq;

	*new {
		| moogInBus, synthsInBus, outBus = 0, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(moogInBus, synthsInBus);
	}

	prInit { | moogInBus, synthsInBus |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			distortion = Decapitator.new(eq.inBus, relGroup: group, addAction: \addToHead);
			while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

			granulator = GranularDelay2.new(distortion.inBus, relGroup: group, addAction: \addToHead);

			delayNetwork1 = DelayNetwork.newMono(granulator.inBus, 3, maxDelay: 2, relGroup: group, addAction: \addToHead);
			while({ try { delayNetwork1.isLoaded } != true }, { 0.001.wait; });

			delayNetwork2 = DelayNetwork.newStereo(granulator.inBus, 3, maxDelay: 4, relGroup: group, addAction: \addToHead);
			while({ try { delayNetwork2.isLoaded } != true }, { 0.001.wait; });

			splitter2 = Splitter.newStereo(2, [granulator.inBus, delayNetwork2.inBus], false, group, \addToHead);
			while({ try { splitter2.isLoaded } != true }, { 0.001.wait; });

			subtractive = Subtractive.new(splitter2.inBus, relGroup: group, addAction: \addToHead);
			while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });

			splitter1 = Splitter.newStereo(2, [granulator.inBus, delayNetwork1.inBus], false, group, \addToHead);
			while({ try { splitter1.isLoaded } != true }, { 0.001.wait; });

			synths = IM_HardwareIn.new(synthsInBus, splitter1.inBus, group, \addToHead);
			while({ try { synths.isLoaded } != true }, { 0.001.wait; });

			moog = IM_HardwareIn.new(moogInBus, granulator.inBus, group, \addToHead);
			while({ try { moog.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			server.sync;

			distortion.setMix(0);

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		mixer.setPreVol(-3);
		granulator.mixer.setPreVol(-12);

		eq.setHighPassCutoff(100);

		distortion.loadPreset('yearsEns');
		distortion.setMix(0);

		granulator.setMix(0.3);
		granulator.setGrainEnvelope('gabWide');
		granulator.setGrainDur(0.9, 1.8);
		granulator.setTrigRate(18);
		granulator.setDelayLevel(0);

		delayNetwork1.mixer.setPreVol(-9);
		delayNetwork2.mixer.setPreVol(-9);

		delayNetwork1.mixer.setVol(-70);

		delayNetwork1.setDelayTime(0, 1.5);
		delayNetwork1.setDelayTime(1, 1);
		delayNetwork1.setDelayTime(2, 2);

		delayNetwork1.setPan(0, -1);
		delayNetwork1.setPan(1, 0);
		delayNetwork1.setPan(2, 1);

		delayNetwork1.setFeedback(0, 0.21);
		delayNetwork1.setFeedback(1, 0.46);
		delayNetwork1.setFeedback(2, 0.24);

		delayNetwork1.setFilterType(0, 3);
		delayNetwork1.setFilterType(1, 3);
		delayNetwork1.setFilterType(2, 3);

		delayNetwork1.setCutoff(0, 436);
		delayNetwork1.setCutoff(1, 138);
		delayNetwork1.setCutoff(2, 611);

		delayNetwork2.mixer.setVol(-70);

		delayNetwork2.setDelayTime(0, 1.25);
		delayNetwork2.setDelayTime(1, 1.5);
		delayNetwork2.setDelayTime(2, 1);

		delayNetwork2.setPan(0, -1);
		delayNetwork2.setPan(1, 0);
		delayNetwork2.setPan(2, 1);

		delayNetwork2.setFeedback(0, 0.36);
		delayNetwork2.setFeedback(1, 0.46);
		delayNetwork2.setFeedback(2, 0.43);

		delayNetwork2.setFilterType(0, 3);
		delayNetwork2.setFilterType(1, 3);
		delayNetwork2.setFilterType(2, 3);

		delayNetwork2.setCutoff(0, 1200);
		delayNetwork2.setCutoff(1, 449);
		delayNetwork2.setCutoff(2, 918);

		subtractive.mixer.setPanBal(-0.3);
		subtractive.mixer.setPreVol(6);
		subtractive.readPreset('yearsStrings');
	}

	////////
	free {
		moog.free; synths.free; subtractive.free;
		delayNetwork1.free; delayNetwork2.free; splitter1.free; splitter2.free;
		granulator.free; distortion.free; eq.free;
		this.freeModule;
	}

} 