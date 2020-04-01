/*
Thursday, February 27th 2020
Decapitator.sc
prm

Wrapper Class for Decapitator VST by SoundToys
*/

Decapitator : IM_Processor {

	var <isLoaded;
	var <synth;
	var path;

	var <drive, <mix;

	var server;

	*new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
	}

	*newStereo { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
		^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo;
	}

	prInit {
		server = Server.default;
		path = "~/Library/Application Support/SuperCollider/Extensions/prm/Effects/Timbral/Decapitator Presets/";
		server.waitForBoot {
			isLoaded = false;

			drive = 0.0;
			mix = 1.0;

			this.prAddSynthDefs;
			server.sync;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			synth = VSTPluginController(Synth(\prm_decap_mono,
				[\inBus, inBus, \outBus, mixer.chanMono(0)], group, \addToHead)).open("Decapitator");

			server.sync;

			isLoaded = true;
		}
	}

	prInitStereo {
		server = Server.default;
		path = "~/Library/Application Support/SuperCollider/Extensions/prm/Effects/Timbral/Decapitator Presets/";
		server.waitForBoot {
			isLoaded = false;
			this.prAddSynthDefs;
			server.sync;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			synth = VSTPluginController(Synth(\prm_decap_stereo,
				[\inBus, inBus, \outBus, mixer.chanStereo(0)], group, \addToHead)).open("Decapitator");

			server.sync;

			isLoaded = true;
		}

	}

	prAddSynthDefs {
		SynthDef(\prm_decap_mono, { | inBus = 0, outBus = 0, amp = 1 |
			var input, sig;
			input = In.ar(inBus, 1);
			sig = VSTPlugin.ar(input, 1);
			sig = sig * amp;
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_decap_stereo, { | inBus = 0, outBus = 0, amp = 1 |
			var input, sig;
			input = In.ar(inBus, 2);
			sig = VSTPlugin.ar(input, 2);
			sig = sig * amp;
			Out.ar(outBus, sig);
		}).add;
	}

	//////// public functions:

	free {
		synth.free;
		isLoaded = false;
	}

	loadPreset { | name = \default |
		{
			synth.readProgram(path ++ name ++ ".fxp");
			server.sync;
			synth.get(\Mix, { | i | mix = i });
			synth.get(\Drive, { | i | drive = i; });
		}.fork;
	}

	savePreset { | name = \default |
		synth.writeProgram(path++ name ++ ".fxp");
	}

	makeGUI { ^synth.gui; }

	setMix { | m = 1.0 | mix = m; synth.set(\Mix, mix); }
	setDrive { | d = 0.0 | drive = d; synth.set(\Drive, drive); }
}