/*
Wednesday, December 4th 2019
LoopDelay.sc
prm

an avenue for improvisation
*/

LoopDelay : IM_Module {

	var <isLoaded, server;
	var <delayNetwork, <looper;

	*new { | outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = \addToHead |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			looper = Looper.newStereo(mixer.chanStereo(0), 10, 0, relGroup: group, addAction: \addToHead);
			while({ try { looper.isLoaded } != true }, { 0.001.wait; });

			delayNetwork = DelayNetwork.newMono(looper.inBus, 10, 2, 4, 10,
				relGroup: group, addAction: \addToHead);
			while({ try { delayNetwork.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		mixer.setSendVol(0, -12);
		delayNetwork.randomizeDelayTime(0.5, 3);
		delayNetwork.randomizeFeedback(0.6, 0.75);
		delayNetwork.randomizePan;

	}

	/////// public functions:

	inBus { ^delayNetwork.inBus }

	free {
		looper.free;
		delayNetwork.free;
		this.freeModule;
	}

}