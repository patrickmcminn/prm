/*
Tuesday, March 17th 2020
LittlePlate.sc
prm

wrapper class for Little Plate reverb
by Soundtoys

in the midst of the COVID-19 outbreak
*/

LittlePlate : IM_Processor {

	var server, <isLoaded;
	var <synth, bus;
	var path;
	var <mix;

	*new {
		| outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
	}

	*newStereo {
		| outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
		^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo;
	}

	prInit {
		server = Server.default;
		path = "~/Library/Application Support/SuperCollider/Extensions/prm/Effects/Reverb/Little Plate Presets/";
		server.waitForBoot {
			isLoaded = false;

			mix = 1.0;

			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDef;

			server.sync;

			synth = VSTPluginController(Synth(\prm_littlePlate,
				[\inBus, inBus, \outBus, mixer.chanStereo(0)], group, \addToHead)).open("LittlePlate");
			while({ try { synth.loaded } != true }, { 0.001.wait; });

			server.sync;

			mixer.setPreVol(6);

			isLoaded = true;
		}
	}

	prInitStereo {
		server = Server.default;
		path = "~/Library/Application Support/SuperCollider/Extensions/prm/Effects/Reverb/Little Plate Presets/";
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDef;

			server.sync;

			synth = VSTPluginController(Synth(\prm_littlePlate_stereo,
				[\inBus, inBus, \outBus, mixer.chanStereo(0)], group, \addToHead)).open("LittlePlate");
			while({ try { synth.loaded } != true }, { 0.001.wait; });

			server.sync;

			mixer.setPreVol(6);

			isLoaded = true;
		}
	}

	prAddSynthDef {
		SynthDef(\prm_littlePlate, { | inBus = 0, outBus = 0, amp = 1 |
			var input, sig;
			input = In.ar(inBus, 1);
			sig = VSTPlugin.ar(input, 2);
			sig = sig * amp;
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_littlePlate_stereo, { | inBus = 0, outBus = 0, amp = 1 |
			var input, sig;
			input = In.ar(inBus, 2);
			sig = VSTPlugin.ar(input, 2);
			sig = sig * amp;
			Out.ar(outBus, sig);
		}).add;
	}

	free {
		synth.free;
		this.freeProcessor;
	}


	loadPreset { | name = \default |
		{
			synth.readProgram(path ++ name ++ ".fxp");
			server.sync;
			synth.get(\Mix, { | i | mix = i });
		}.fork;
	}

	savePreset { | name = \default |
		synth.writeProgram(path++ name ++ ".fxp");
	}

	makeGUI { ^synth.gui }

	setMix { | m = 1.0 |
		mix = m;
		synth.set(\Mix, mix);
	}

}

