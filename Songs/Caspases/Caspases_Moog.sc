/*
Thursday, August 11th 2022
Caspases_Moog.sc
prm
*/

Caspases_Moog : IM_Module {

	var <isLoaded, server;
	var <input, <delay;

	*new { | outBus, inBus, group, addAction = 'addToHead' |
		^super.new(1, outBus, nil, nil, nil, nil, false, group, addAction).prInit(inBus);
	}

	prInit { | inBus |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			delay = SimpleDelay.newMono(mixer.chanStereo(0), 1.363636363636364, 0.3, 1.5,
				relGroup: group, addAction: \addToHead);
			while({ try { delay.isLoaded } != true }, { 0.001.wait; });

			input = IM_HardwareIn.new(inBus, delay.inBus, group, \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		input.mute;
		delay.setMix(0.42);
	}

	/////// public functions:

	free {
		input.free;
		delay.free;
		this.freeModule;
	}


} 