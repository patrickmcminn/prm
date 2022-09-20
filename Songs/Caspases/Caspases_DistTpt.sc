/*
Tuesday, August 9th 2022
Caspases_DistTpt.sc
prm
*/

Caspases_DistTpt : IM_Module {

	var <isLoaded, server;
	var <eq, <distortion, <delay, <input;

	*new { | outBus, inBus, relGroup, addAction = 'addToHead' |
		^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(inBus);
	}

	prInit { | inBus |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait;});

			delay = SimpleDelay.newStereo(eq.inBus, ((60/132)*8), 0.8, 4, relGroup: group, addAction: \addToHead);
			while({ try { delay.isLoaded } != true }, { 0.001.wait; });

			distortion = Decapitator.new(delay.inBus, relGroup: group, addAction: \addToHead);
			while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

			input = IM_HardwareIn.new(inBus, distortion.inBus, group, \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			input.mute;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialParameters {

		delay.setMix(0.75);
		eq.setLowPassCutoff(2500);
		eq.setHighPassCutoff(200);
		eq.setPeak1Freq(1740);
		eq.setPeak1Gain(3);

	}

	/////// public functions:

	free {
		input.free;
		delay.free;
		distortion.free;
		eq.free;
		this.freeModule;
	}

} 