/*
Saturday, August 20th 2022
Caspases_FXTPT.sc
prm
*/

Caspases_ShiftTpt : IM_Module {

	var <isLoaded, server ;

	var <input, <delay, <microsynth, dummyMixer;

	*new { | outBus, inBus, relGroup, addAction = 'addToHead' |
		^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(inBus);
	}

	prInit { | inBus |
		server =  Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			delay = SimpleDelay.newStereo(mixer.chanStereo(0), 0.454545, 0.5, 0.5, relGroup: group, addAction: \addToHead);
			while({ try { delay.isLoaded } != true }, { 0.001.wait; });

			microsynth = MicroSynth.newStereo(delay.inBus, relGroup: group, addAction: \addToHead);
			while({ try { microsynth.isLoaded } != true }, { 0.001.wait; });

			dummyMixer = IM_Mixer_1Ch.newNoSends(microsynth.inBus, false, group, \addToHead);
			while({ try { dummyMixer.isLoaded } != true }, { 0.001.wait; });

			input = IM_HardwareIn.new(inBus, dummyMixer.chanMono, group, \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });


			server.sync;
			this.prSetInitialParameters;
			isLoaded = true;
		}
	}

	prSetInitialParameters {
		input.mute;

		delay.setMix(0.5);

		microsynth.setSubVol(-inf);
		microsynth.setSquareVol(-inf);
		microsynth.setDryVol(-inf);
		microsynth.setOctVol(0);
		microsynth.setStartFrequency(5000);
		microsynth.setEndFrequency(5000);
	}

	//////// public functions:

	free {
		input.free;
		microsynth.free;
		this.freeModule;
	}


} 