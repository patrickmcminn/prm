/*
Sunday, April 3rd 2022
Years_Drums.sc
prm
*/

Years_Drums : IM_Module {

	var <isLoaded, server;
	var <drums, <granulator, <distortion, <eq;

	*new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			distortion = Decapitator.new(eq.inBus, relGroup: group, addAction: \addToHead);
			while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

			granulator = GranularDelay2.new(distortion.inBus, relGroup: group, addAction: \addToHead);
			while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

			drums = SampleGrid.new(granulator.inBus, relGroup: group, addAction: \addToHead);
			while({ try { drums.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		drums.readPreset('years1');
		drums.mixer.setPreVol(-9);
		granulator.setMix(0.6);
		granulator.setGrainDur(0.9, 2.2);
		granulator.setTrigRate(16);
		granulator.setDelayLevel(0);
		granulator.setGrainEnvelope('percRev');
	}


	//////// public functions:
	free {
		drums.free;
		granulator.free;
		distortion.free;
		eq.free;
		this.freeModule;
	}

} 