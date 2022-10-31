/*
Friday, October 14th 2022
Chesa_Algae.sc
prm
*/

Chesa_Algae : IM_Module {

	var <isLoaded, server;
	var <synth, <granulator, <eq;
	var pitchSets;

	*new { | outBus, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			pitchSets = Array.newClear(6);

			eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			granulator = GranularDelay2.new(eq.inBus, relGroup: group, addAction: \addToHead);
			while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

			synth = Subtractive.new(granulator.inBus, relGroup: group, addAction: \addToHead);
			while({ try { synth.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			synth.makeSequence(\algae);
			synth.makeSequence(\bloom);

			server.sync;

			this.prDefinePitchSets;
			this.prMakePatterns;
			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		synth.readPreset('chesaAlgae');

		eq.setHighPassCutoff(150);
		eq.setPeak1Freq(300);
		eq.setPeak1Gain(-3);
		eq.setPeak2Freq(650);
		eq.setPeak2Gain(-3);
		eq.setLowPassCutoff(1500);

		granulator.setMix(1);
		granulator.setTrigRate(10);
		granulator.setGrainDur(3, 5);
		granulator.setRate(0.5, 0.5);
		granulator.setGrainEnvelope(\revPerc);
	}

	prMakePatterns {
		synth.addKey(\algae, \dur, Pwhite(0.05, 0.5, inf));
		synth.addKey(\algae, \note, Prand([7, 9, 10, 5, 0, 2, 14, \r, \r, \r, \r], inf));
		synth.addKey(\algae, \octave, Prand([5, 5, 6], inf));

		synth.addKey(\bloom, \dur, Prand([0.125, 0.25, 0.5, 0.0125], inf));
		synth.addKey(\bloom, \note,Prand([-5, 7, 2, 3], inf));
		synth.addKey(\bloom, \octave, Prand([4, 5, 5, 6], inf));

	}

	prDefinePitchSets {
		pitchSets[0] = Prand([7, 9, 10, 5, 0, 2, 14, \r, \r, \r, \r], inf);
		pitchSets[1] = Prand([0, 2, 3, -2, \r], inf);
		pitchSets[2] = Prand([[7, 2, 0, 12], [-2, 0, 2, 3], [7, 2, -2, 5]],inf);
		pitchSets[3] = Prand([7, 2, 0, 12,  \r, \r, -2, 3], inf);
		pitchSets[4] = Prand([[3, -3, -2, 2], [-2, 0, 2, 3], [7, 2, -2, 5], \r, \r, \r, \r, \r, \r], inf);
		pitchSets[5] = Prand([7, 9, 10, 5, 0, 2, 14], inf);
	}

	/////// public functions:

	free {
		eq.free;
		granulator.free;
		synth.free;
		this.freeModule;
	}

	changePitchSet {
		var set = 6.rand;
		synth.addKey(\algae, \note, pitchSets[set]);
	}
} 