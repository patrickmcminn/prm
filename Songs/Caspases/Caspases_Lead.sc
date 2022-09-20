/*
Wednesday, August 3rd 2022
Caspases_Lead.sc
prm
*/

Caspases_Lead : IM_Module {

	var server, <isLoaded;
	var <input, <concat, <distortion, <microsynth, <oc2, <eq;
	var <preset;

	*new { | outBus = 0, synthIn, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(synthIn);
	}

	prInit { | synthIn |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
			mixer.mute;

			eq = Equalizer.newStereo(mixer.chanStereo, group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			distortion = Decapitator.newStereo(eq.inBus, relGroup: group, addAction: \addToHead);

			concat = ConcatGlitch.newStereo(distortion.inBus, relGroup: group, addAction: \addToHead);
			while({ try { concat.isLoaded } != true }, { 0.001.wait; });

			microsynth = MicroSynth.newStereo(concat.inBus, relGroup: group, addAction: \addToHead);
			while({ try { microsynth.isLoaded } != true }, { 0.001.wait; });

			oc2 = Octave_OC2.newStereo(microsynth.inBus, relGroup: group, addAction: \addToHead);
			while({ try { oc2.isLoaded } != true }, { 0.001.wait; });

			input = IM_HardwareIn.newStereo(synthIn, oc2.inBus, group, \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;
			mixer.unMute;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		input.mute;
		microsynth.mixer.setPreVol(0);
		concat.mixer.setPreVol(-3);
		distortion.mixer.setPreVol(0);

		distortion.loadPreset('caspases');

		server.sync;

		this.loadInitial;
	}

	////// public functions:

	free {
		oc2.free; microsynth.free; concat.free;
		distortion.free; eq.free;
		this.freeModule;
	}

	loadInitial {
		concat.setMix(0.5);

		microsynth.setSubVol(-60);
		microsynth.setOctVol(-24);
		microsynth.setSquareVol(-60);
		microsynth.setDryVol(0);
		microsynth.setStartFrequency(18000);
		microsynth.setEndFrequency(18000);

		distortion.setMix(0);
		oc2.setOct1Vol(-60);
		oc2.setOct2Vol(-60);

		preset = 'initial';
	}

	loadCulmination {
		distortion.setMix(1);

		concat.setMix(0);

		microsynth.setSubVol(-9);
		microsynth.setOctVol(0);
		microsynth.setSquareVol(-21);

		oc2.setOct1Vol(0);
		oc2.setOct2Vol(-9);

		preset = 'culmination';
	}

} 