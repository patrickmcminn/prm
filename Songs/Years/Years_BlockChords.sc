/*
Monday, April 4th 2022
Years_BlockChords.sc
prm
*/

Years_BlockChords : IM_Module {

	var <isLoaded, server;
	var <subtractive, <distortion, <splitter, <filter, <delay;

	*new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			delay = DelayNetwork.newStereo(mixer.chanStereo, 2, 1, 1.2, 1.5, relGroup: group, addAction: \addToHead);
			while({ try { delay.isLoaded } != true }, { 0.001.wait; });

			splitter = Splitter.newStereo(2, [delay.inBus, mixer.chanStereo(0)], relGroup: group, addAction: \addToHead);
			while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

			filter = LowPassFilter.newStereo(splitter.inBus, 5000, 1, relGroup: group, addAction: \addToHead);
			while({ try { filter.isLoaded } != true }, { 0.001.wait; });

			distortion = Decapitator.newStereo(filter.inBus, relGroup: group, addAction: \addToHead);
			while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

			subtractive = Subtractive.new(distortion.inBus, relGroup: group, addAction: \addToHead);
			while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		subtractive.mixer.setPreVol(-9);
		mixer.setPreVol(-9);

		delay.mixer.setVol(-70);

		subtractive.readPreset('yearsBlockChords');
		distortion.loadPreset('yearsBlockChords');

		filter.setCutoff(800);
		filter.lfo.setWaveform('sampleAndHold');
		filter.lfo.setFrequency(4.5);
		filter.setCutoffLFOBottomRatio(0.8);
		filter.setCutoffLFOTopRatio(3);

		delay.setDelayTime(0, 1);
		delay.setFeedback(0, 0.84);
		delay.setDelayTime(1, 1.5);
		delay.setFeedback(1, 0.84);
	}

	/////// public functions:

	free {
		subtractive.free; distortion.free; filter.free;
		delay.free;
		this.freeModule;
	}

} 