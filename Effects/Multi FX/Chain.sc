/*
Tuesday, July 27th 2021
Chain.sc
prm

fx chain for trumpt improv
hurray
*/


Chain : IM_Module {

	var <isLoaded, server;
	var <freezer, <concat, <multiShift;
	var <granulator, <eq, <delay;
	var splitter;
	var <oct, <microsynth;
	var <input;

	var <looper;

	*new { |outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
			mixer.muteMaster;

			delay = SimpleDelay.newStereo(mixer.chanStereo(0), 0.75, 0.5, 10, relGroup: group, addAction: \addToHead);
			while({ try { delay.isLoaded } != true }, { 0.001.wait; });

			eq = Equalizer.newStereo(delay.inBus, group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			granulator = GranularDelay2.new(eq.inBus, relGroup: group, addAction: \addToHead);
			while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

			multiShift = IM_MultiShift.newStereo(granulator.inBus, [-12, -5, 2, 7, 12], 1, group, \addToHead);
			while({ try { multiShift.isLoaded } != true }, { 0.001.wait; });

			concat = Concaterwaul.newStereo(multiShift.inBus, relGroup: group, addAction: \addToHead);
			while({ try { concat.isLoaded } !=true }, { 0.001.wait; });

			/*
			freezer = GrainFreeze2.newMono(concat.inBus, relGroup: group, addAction: \addToHead);
			while({ try { freezer.isLoaded } != true }, { 0.001.wait; });
			*/
			freezer = IM_GrainFreeze.new(concat.inBus, group, \addToHead);
			while({ try { freezer.isLoaded } != true }, { 0.001.wait; });

			looper = Looper.newStereo(concat.inBus, 30, 1, relGroup: group, addAction: \addToHead);
			while({ try { looper.isLoaded } != true }, { 0.001.wait; });

			splitter = Splitter.newStereo(3, [concat.inBus, freezer.inBus, looper.inBus], false, group, \addToHead);
			while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

			microsynth = MicroSynth.newStereo(splitter.inBus, relGroup: group, addAction: \addToHead);
			while({ try { microsynth.isLoaded } != true }, { 0.001.wait; });

			oct = Octave_OC2.newStereo(microsynth.inBus, relGroup: group, addAction: \addToHead);
			while({ try { oct.isLoaded } != true }, { 0.001.wait; });

			input = IM_Mixer_1Ch.new(oct.inBus, relGroup: group, addAction: \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			mixer.unMuteMaster;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		// microsynth:
		microsynth.setDryVol(0);
		microsynth.setSubVol(-70);
		microsynth.setOctVol(-70);
		microsynth.setSquareVol(-70);
		microsynth.setStartFrequency(20000);
		microsynth.setEndFrequency(20000);

		// oct:
		oct.setDryVol(0);
		oct.setOct1Vol(-70);
		oct.setOct2Vol(-70);

		delay.setMix(0);
		granulator.setMix(0);
		granulator.setDelayLevel(0);
		multiShift.bypass;
		3.do({ | chan | concat.subMixer.mute(chan); });
		concat.subMixer.unMute(3);
		looper.mixer.setPreVol(-9);
	}

	//////// public functions:

	free { }

	inBus { ^input.inBus }

}


/*
freeze
concat
MultiShift
Granulator
EQ
Delay
*/