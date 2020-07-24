/*
Wednesday, April 22nd 2020
Darkness_Noise.sc
prm
*/

Darkness_Noise : IM_Module {

	var server, <isLoaded;
	var <input, <lowPass;

	*new { | outBus, noiseInBus, relGroup, addAction |
		^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(noiseInBus);
	}

	prInit { | noiseInBus |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			lowPass = LowPassFilter.newMono(mixer.chanStereo(0), 4500, relGroup: group, addAction: \addToHead);
			while({ try { lowPass.isLoaded } != true }, { 0.001.wait; });

			input = IM_HardwareIn.new(noiseInBus, lowPass.inBus, group, \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			isLoaded = true;
		}
	}

	free {
		input.free;
		lowPass.free;
		this.freeModule;
		isLoaded = false;
	}

}