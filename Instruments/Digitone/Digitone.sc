/*
Monday, August 1st 2022
Digitone.sc
prm

wrapper class for Digitone VST
*/

Digitone : IM_Module {

	var server, <isLoaded;
	var <synth, bus;
	var path;

	*new {
		| outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
	}

	prInit {
		server = Server.default;
		path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Instruments/Digitone/presets/";
		server.waitForBoot {
			isLoaded = false;
			bus = Bus.audio(server, 2);
			while({ try { mixer.isLoaded } != true }, { 0.001.wait });
			mixer.mute(0);

			this.prAddSynthDef;

			server.sync;

			synth = VSTPluginController(Synth(\prm_valhalla,
				[\inBus, bus, \outBus, mixer.chanStereo(0)], group, \addToHead)).open("Digitone");
			while({ try { synth.loaded } != true }, { 0.001.wait; });

			server.sync;

			isLoaded = true;
		}
	}

	prAddSynthDef {
		SynthDef(\prm_digitone, { | outBus = 0, amp = 1 |
			var input, sig;
			sig = VSTPlugin.ar(nil, 2);
			sig = sig * amp;
			Out.ar(outBus, sig);
		}).add;
	}

	free {
		synth.free;
		this.freeModule
	}

	makeEditor { ^synth.editor }

} 