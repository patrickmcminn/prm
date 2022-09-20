/*
Tuesday, August 9th 2022
Caspases_SCTPT.sc
prm
*/

Caspases_SCTPT : IM_Module {

	var <isLoaded, server;
	var <concat, <lpf, <granulator, <eq;
	var <input;

	*new { | outBus, inBus, relGroup, addAction |
		^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(inBus);
	}

	prInit { | inBus |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			eq = Equalizer.newStereo(mixer.chanStereo, group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			granulator = GranularDelay2.new(eq.inBus, relGroup: group, addAction: \addToHead);
			while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

			lpf = LowPassFilter.newStereo(granulator.inBus, 3500, 0.3, relGroup: group, addAction: \addToHead);
			while({ try { lpf.isLoaded } != true }, { 0.001.wait; });

			concat = ConcatGlitch.newMono(lpf.inBus, relGroup: group, addAction: \addToHead);
			while({ try{ concat.isLoaded } != true }, { 0.001.wait; });

			input = IM_HardwareIn.new(inBus, concat.inBus, group, \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			server.sync;
			input.mute;

			this.prSetInitialParameters;

			isLoaded = true;
		}

	}

	prSetInitialParameters {
		mixer.setPreVol(0, -18);

		lpf.mixer.setPreVol(0, -9);

		eq.setHighPassCutoff(300);

		concat.setMix(0.9);

		granulator.setGrainDur(0.2, 0.5);
		granulator.setTrigRate(16);
		granulator.setMix(0.5);
		granulator.setDelayLevel(0.9);
		granulator.setDelayTime(2.5);
		granulator.setFeedback(0.6);

		lpf.lfo.setFrequency(0.7);
		lpf.lfo.setWaveform('noise');
		lpf.setCutoffLFOBottomRatio(1/7);
		lpf.setCutoffLFOTopRatio(1.2);

	}

} 