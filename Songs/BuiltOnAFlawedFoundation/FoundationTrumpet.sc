/*
Tuesday, June 6th 2017
FoundationTrumpet.sc
prm
*/

FoundationTrumpet : IM_Module {

	var server, <isLoaded;

	var <input, <multiShift, <granulator, <distortion;

	var sequencer, channel, arm;

	*new {
		| outBus = 0, hardwareInBus = 0, seq, chan = 3,
		send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(hardwareInBus, seq, chan);
	}

	prInit { | hardwareInBus = 0, seq, chan |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			distortion = Distortion.newStereo(mixer.chanStereo, 100, relGroup: group, addAction: \addToHead);
			while({ try { distortion.isLoaded } != true }, { 0.001.wait });

			granulator = GranularDelay.new(distortion.inBus, group, \addToHead);
			while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

			multiShift = IM_MultiShift.new(granulator.inBus, [-12, 7, 12, 19, 24], 1, group, \addToHead);
			while({ try { multiShift.isLoaded } != true }, { 0.001.wait; });

			input = IM_HardwareIn.new(hardwareInBus, multiShift.inBus, group, \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			sequencer = seq.uid;
			channel = chan;
			this.prMakeMIDIFuncs;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prMakeMIDIFuncs {
		arm = MIDIFunc.cc({ | val | if( val == 0, { input.mute; }, { input.unMute }); }, 31, channel, sequencer);
	}

	prFreeMIDIFuncs { arm.free; }

	prSetInitialParameters {
		input.mute;

		// Distortion:
		distortion.postEQ.setLowPassCutoff(3910);
		distortion.postEQ.setHighPassCutoff(150);

		// Granulator:
		granulator.granulator.setCrossfade(0.348.linlin(0, 1, -1, 1));
		granulator.setGrainDur(0.1, 0.2);
		granulator.setTrigRate(18);
		granulator.setDelayTime(0.2);
		granulator.setFeedback(0);
		granulator.setDelayMix(0.25);

		mixer.setPreVol(-12);

	}


	//////// public functions:

	free {
		input.free;
		multiShift.free;
		granulator.free;
		distortion.free;
		this.prFreeMIDIFuncs;
		this.freeModule;
		isLoaded = false;
	}


}