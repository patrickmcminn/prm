/*
Saturday, April 2nd 2022
Years_Glock.sc
prm
*/

Years_Glock : IM_Module {

	var <isLoaded, server;

	var <glock, <granulator;

	*new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			granulator = GranularDelay2.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
			while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

			glock = MidBells.new(granulator.inBus, relGroup: group, addAction: \addToHead);
			while({ try { glock.isLoaded } != true }, { 0.001.wait; });

			this.prSetInitialParameters;

			server.sync;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		glock.mixer.setPreVol(-12);
		mixer.setPreVol(-12);

		granulator.setMix(0.85);
		granulator.setDelayLevel(0.8);
		granulator.setDelayTime(2);
		granulator.setFeedback(0.5);
		granulator.setGrainDur(0.3, 1.2);
		granulator.setSync(1);
		granulator.setTrigRate(40);

	}

	///// public functions:

	free {
		glock.free;
		granulator.free;
		this.freeModule;
	}

}

