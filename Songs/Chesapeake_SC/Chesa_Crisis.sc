/*
Friday, 10/14/2022
Chesa_CrisisSynth.sc
prm
*/

Chesa_Crisis : IM_Module {

	var server, <isLoaded;
	var <synth, <filter, <tremolo;

	*new { | outBus, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			tremolo = Tremolo.newStereo(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
			while({ try { tremolo.isLoaded } != true }, { 0.001.wait; });

			filter = LowPassFilter.newStereo(tremolo.inBus, 600, relGroup: group, addAction: \addToHead);
			while({ try { filter.isLoaded } != true }, { 0.001.wait; });

			synth = FeedbackSynth.new(filter.inBus, relGroup: group, addAction: \addToHead);
			while({ try { synth.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			synth.makeSequence(\bassline);
			synth.makeSequence(\dieOff);

			server.sync;
			this.prMakePatterns;
			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prMakePatterns {
		synth.addKey(\bassline, \legato, 1);
		synth.addKey(\bassline, \dur, Prand([
			Pseq([4, 8, 4, 12], 1),
			Pseq([6, 12, 6, 18], 1),
			Pseq([6, 12, 6, 18], 1),
			Pseq([8, 16, 8, 16], 1);
		], inf);
		);
		synth.addKey(\bassline, \note,
			Prand([Pseq([7, 5, 3, 2],  1), Pseq([-5], 1), Pseq([-5], 1), Pseq([3, 2], 1)], inf));
		synth.addKey(\bassline, \octave, Prand([
			Pseq([4, 4, 4, 4], 1),
			Pseq([3, 3, 4, 4], 1),
			Pseq([4, 4, 4, 4], 1),
			Pseq([4, 4, 5, 5], 1),
			Pseq([4, 4, 4, 4], 1)], inf));

		synth.addKey(\dieOff, \legato, 1);
		synth.addKey(\dieOff, \dur, Pseq([16, 8], inf));
		synth.addKey(\dieOff, \note, Pseq([-5], inf));
		synth.addKey(\dieOff, \octave, 4);
		synth.addKey(\dieOff, \amp, 0.5);

	}

	prSetInitialParameters {
		mixer.setPreVol(-12);
		tremolo.setVolLFODepth(0.2);
		tremolo.setVolLFOWaveform('noise');
		tremolo.setVolLFOFreq(0.2);
		synth.mixer.setPreVol(-12);
		synth.setAttackTime(5);
		synth.setReleaseTime(5);

		filter.lfo.setWaveform('noise');
		filter.lfo.setFrequency(3);
	}

	//////// public functions:

	free {
		synth.free;
		this.freeModule;
	}

} 