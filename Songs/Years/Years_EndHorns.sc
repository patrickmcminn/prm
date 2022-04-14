/*
Monday, April 4th 2022
Years_EndHorns.sc
prm
*/

Years_EndHorns : IM_Module {

	var <isLoaded, server;
	var <delay, <granulator, <eq, <input, <splitter;

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

			delay = DelayNetwork.newStereo(eq.inBus, 7, 3, 8, 8, relGroup: group, addAction: \addToHead);
			while({ try { delay.isLoaded } != true }, { 0.001.wait; });

			granulator = GranularDelay2.new(eq.inBus, relGroup: group, addAction: \addToHead);
			while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

			splitter = Splitter.newStereo(2, [granulator.inBus, delay.inBus], false, group, \addToHead);
			while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

			input = IM_Mixer.new(1, splitter.inBus, relGroup: group, addAction: \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		mixer.setPreVol(-9);

		eq.setHighPassCutoff(150);

		delay.mixer.setPreVol(-9);
		delay.randomizeDelayTime(4, 8);
		delay.randomizeFeedback(0.1, 0.3);
		delay.randomizePan(-1, 1);

		granulator.setBufferLength(10);
		granulator.setGrainDur(6, 8);
		granulator.setRate(0.5, 0.5);
		granulator.setTrigRate(1);
	}

	/////// public functions:

	free {
		input.free;
		splitter.free;
		granulator.free;
		delay.free;
		eq.free;
		this.freeModule;
	}

	inBus { ^input.chanMono; }
} 