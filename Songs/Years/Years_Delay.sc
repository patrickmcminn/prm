/*
Sunday, April 3rd 2022
Years_Delay.sc
prm
*/

Years_Delay : IM_Module {

	var <isLoaded, server;
	var <delay, <eq;

	*new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			delay = DelayNetwork.newStereo(eq.inBus, 6, maxDelay: 2, relGroup: group, addAction: \addToHead);
			while({ try { delay.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prInitializeParameters;

			isLoaded = true;
		}
	}

	prInitializeParameters {

		delay.mixer.setPreVol(-12);
		mixer.setPreVol(-3);

		eq.setHighPassCutoff(100);
		eq.setPeak1Freq(350);
		eq.setPeak1Gain(-5);
		eq.setHighGain(-3);

		delay.setDelayTime(0, 1.7);
		delay.setDelayTime(1, 2);
		delay.setDelayTime(2, 0.3);
		delay.setDelayTime(3, 0.4);
		delay.setDelayTime(4, 1);
		delay.setDelayTime(5, 2);

		delay.setFeedback(0, 0.75);
		delay.setFeedback(1, 0.75);
		delay.setFeedback(2, 0.71);
		delay.setFeedback(3, 0.71);
		delay.setFeedback(4, 0.42);
		delay.setFeedback(5, 0.42);

		delay.setPan(0, -1);
		delay.setPan(1, 1);
		delay.setPan(2, -1);
		delay.setPan(3, 1);
		delay.setPan(4, -1);
		delay.setPan(5, 1);

		delay.setFilterType(0, 2);
		delay.setFilterType(1, 2);
		delay.setFilterType(2, 0);
		delay.setFilterType(3, 0);
		delay.setFilterType(4, 1);
		delay.setFilterType(5, 1);

		delay.setCutoff(0, 650);
		delay.setCutoff(1, 1100);
		delay.setCutoff(4, 450);
		delay.setCutoff(5, 350);

		delay.dry.mute;
	}

	//////// public functions:

	free {
		delay.free;
		eq.free;
		this.freeModule;
	}

	inBus { ^delay.inBus }
} 