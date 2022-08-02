/*
Monday, April 4th 2022
Years_Ensemble.sc
prm
*/

Years_Ensemble : IM_Module {

	var <isLoaded, server;
	var <moog, <synths, <outlier;
	var <delayNetwork1, <delayNetwork2, <splitter;
	var <granulator, <distortion, <eq;
	var <digiIn1, <digiIn2, <digiIn3, <digiIn4;

	var <ensembleIsMuted = true;

	*new {
		| moogInBus, digiArray, outBus = 0, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(moogInBus, digiArray);
	}

	prInit { | moogInBus, digiArray |
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

			/*
			splitter2 = Splitter.newStereo(2, [granulator.inBus, delayNetwork2.inBus], false, group, \addToHead);
			while({ try { splitter2.isLoaded } != true }, { 0.001.wait; });

			subtractive = Subtractive.new(splitter2.inBus, relGroup: group, addAction: \addToHead);
			while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });
			*/

			splitter = Splitter.newStereo(3, [granulator.inBus, delayNetwork1.inBus, delayNetwork2.inBus],
				false, group, \addToHead);
			while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

			/*
			synths = IM_HardwareIn.new(synthsInBus, splitter1.inBus, group, \addToHead);
			while({ try { synths.isLoaded } != true }, { 0.001.wait; });
			*/

			synths = IM_Mixer.newNoSends(3, splitter.inBus, false, group, \addToHead);
			while({ try { synths.isLoaded } != true }, { 0.001.wait; });

			outlier = IM_Mixer_1Ch.newNoSends(granulator.inBus, false, group, \addToHead);
			while({ try { outlier.isLoaded } != true }, { 0.001.wait; });

			moog = IM_HardwareIn.new(moogInBus, granulator.inBus, group, \addToHead);
			while({ try { moog.isLoaded } != true }, { 0.001.wait; });

			digiIn1 = IM_HardwareIn.newStereo(digiArray[0], synths.chanStereo(0), group, \addToHead);
			while( { try { digiIn1.isLoaded } != true }, { 0.001.wait; });
			digiIn2 = IM_HardwareIn.newStereo(digiArray[1], synths.chanStereo(1), group, \addToHead);
			while( { try { digiIn2.isLoaded } != true }, { 0.001.wait; });
			digiIn3 = IM_HardwareIn.newStereo(digiArray[2], synths.chanStereo(2), group, \addToHead);
			while( { try { digiIn3.isLoaded } != true }, { 0.001.wait; });

			digiIn4 = IM_HardwareIn.newStereo(digiArray[3], outlier.chanStereo, group, \addToHead);
			while( { try { digiIn4.isLoaded } != true }, { 0.001.wait; });

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

		3.do({ | i | synths.setPreVol(i, 3); });
		outlier.setPreVol(3);

		this.muteEnsembleInputs;

		eq.setHighPassCutoff(100);

		distortion.loadPreset('yearsEns');
		distortion.setMix(0);

		granulator.setMix(0.3);
		granulator.setGrainEnvelope('gabWide');
		granulator.setGrainDur(0.9, 1.8);
		granulator.setTrigRate(18);
		granulator.setDelayLevel(0);

		delayNetwork1.mixer.setPreVol(-3);
		delayNetwork2.mixer.setPreVol(-3);

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

		/*
		subtractive.mixer.setPanBal(-0.3);
		subtractive.mixer.setPreVol(6);
		subtractive.readPreset('yearsStrings');
		*/
	}

	////////
	free {
		moog.free; synths.free; outlier.free;
		delayNetwork1.free; delayNetwork2.free; splitter.free;
		granulator.free; distortion.free; eq.free;
		digiIn1.free; digiIn2.free; digiIn3.free; digiIn4.free;
		this.freeModule;
	}

	tglMuteEnsembleInputs {
		if( ensembleIsMuted == true, { this.unMuteEnsembleInputs }, { this.muteEnsembleInputs; });
	}

	muteEnsembleInputs {
		moog.mute; digiIn1.mute; digiIn2.mute; digiIn3.mute; digiIn4.mute;
		ensembleIsMuted = true;
	}

	unMuteEnsembleInputs {
		moog.unMute; digiIn1.unMute; digiIn2.unMute; digiIn3.unMute; digiIn4.unMute;
		ensembleIsMuted = false;
	}

} 