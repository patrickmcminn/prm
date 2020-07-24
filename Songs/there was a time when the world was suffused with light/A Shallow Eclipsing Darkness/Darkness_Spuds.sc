/*
Wednesday, April 22nd 2020
Darkness_Spuds.sc
prm

not sure why they're spuds, but they are
*/

Darkness_Spuds : IM_Module {

	var server, <isLoaded;

	var <synth, <granulator;

	*new { | outBus, relGroup, addAction = 'addToHead' |
		^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			granulator = GranularDelay.new(mixer.chanStereo(0), group, \addToHead);
			while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

			synth = Subtractive.new(granulator.inBus, relGroup: group, addAction: \addToHead);
			while({ try { synth.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			server.sync;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		synth.readPreset('darkness');

		granulator.setGranulatorCrossfade(0.5);
		granulator.setGrainEnvelope('expodec');
		granulator.setDelayMix(0.5);
		granulator.setDelayTime(2.1);
		granulator.setFeedback(0.3);
	}

	free {
		synth.free;
		granulator.free;
		this.freeModule;
		isLoaded = false;
	}

}

