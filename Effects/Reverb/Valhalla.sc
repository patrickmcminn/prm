/*
Wednesday, February 26th 2020
Valhalla.sc
prm

Wrapper class for Valhalla Reverb
*/

Valhalla : IM_Module {

	var server, <isLoaded;
	var <synth, bus;
	var <preEQ, <postEQ;
	var path;

	var <highCut, <decayTime, <mix, <preDelay;

	*new {
		| outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
	}

	*newStereo {
		| outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo;
	}

	prInit {
		server = Server.default;
		path = "~/Library/Application Support/SuperCollider/Extensions/prm/Effects/Reverb/Valhalla Presets/";
		server.waitForBoot {
			isLoaded = false;
			bus = Bus.audio(server, 2);
			while({ try { mixer.isLoaded } != true }, { 0.001.wait });

			this.prAddSynthDef;

			server.sync;

			postEQ = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { postEQ.isLoaded } != true }, { 0.001.wait; });

			synth = VSTPluginController(Synth(\prm_valhalla,
				[\inBus, bus, \outBus, postEQ.inBus], group, \addToHead)).open("ValhallaRoom_x64");
			while({ try { synth.loaded } != true }, { 0.001.wait; });

			preEQ = Equalizer.newMono(bus, group, \addToHead);
			while({ try { preEQ.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prPopulateParameters;

			mixer.setPreVol(6);
			//preEQ.mixer.setPreVol(3);
			postEQ.mixer.setPreVol(3);

			isLoaded = true;
		}
	}

	prInitStereo {
		server = Server.default;
		path = "~/Library/Application Support/SuperCollider/Extensions/prm/Effects/Reverb/Valhalla Presets/";
		server.waitForBoot {
			isLoaded = false;
			bus = Bus.audio(server, 2);
			while({ try { mixer.isLoaded } != true }, { 0.001.wait });

			this.prAddSynthDef;

			server.sync;

			postEQ = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { postEQ.isLoaded } != true }, { 0.001.wait; });

			synth = VSTPluginController(Synth(\prm_valhalla_stereo,
				[\inBus, bus, \outBus, postEQ.inBus], group, \addToHead)).open("ValhallaRoom_x64");
			while({ try { synth.loaded } != true }, { 0.001.wait; });

			preEQ = Equalizer.newStereo(bus, group, \addToHead);
			while({ try { preEQ.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prPopulateParameters;

			mixer.setPreVol(6);
			//preEQ.mixer.setPreVol(3);
			postEQ.mixer.setPreVol(3);

			isLoaded = true;
		};
	}

	prAddSynthDef {
		SynthDef(\prm_valhalla, { | inBus = 0, outBus = 0, amp = 1 |
			var input, sig;
			input = In.ar(inBus, 1);
			sig = VSTPlugin.ar(input, 2);
			sig = sig * amp;
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_valhalla_stereo, { | inBus = 0, outBus = 0, amp = 1 |
			var input, sig;
			input = In.ar(inBus, 2);
			sig = VSTPlugin.ar(input, 2);
			sig = sig * amp;
			Out.ar(outBus, sig);
		}).add;
	}

	// really not working yet for some reason
	prPopulateParameters {
		synth.get(\HighCut, { | val |
			highCut = val.linlin(0.0, 1.0, 100, 15000);
		});
		synth.get(\decay, { | val |
			decayTime = val.linlin(0.0, 1.0, 0.1, 100);
		});
		synth.get(\mix, { | val |
			mix = val;
		});
		synth.get(\predelay, { | val |
			preDelay = val.linlin(0.0, 1.0, 0, 500);
		});
	}

	//////// public functions:

	free {
		synth.free;
		preEQ.free;
		postEQ.free;
		bus.free;
		this.freeModule
	}

	inBus { ^preEQ.inBus }

	loadPreset { | name = \default |
		synth.readProgram(path ++ name ++ ".fxp");
		this.prPopulateParameters;
	}

	savePreset { | name = \default |
		synth.writeProgram(path++ name ++ ".fxp");
	}

	makeGUI { ^synth.gui }
	makeEditor { ^synth.editor }

	setMix { | m = 1.0 |
		mix = m;
		synth.set(\mix, mix);
	}

	setPreDelay { | delay = 0.0 |
		var del;
		preDelay = delay;
		del = preDelay.linlin(0, 500, 0.0, 1.0);
		synth.set(\predelay, del);
	}

	setDecayTime { | decay = 1.5 |
		var dec;
		decayTime = decay;
		dec = decayTime.linlin(0.1, 100, 0.0, 1.0);
		synth.set(\decay, dec);
	}

	setHighCut { | cut = 4500 |
		var c;
		highCut = cut;
		c = highCut.linlin(100, 15000, 0.0, 1.0);
		c = c.lag(0.1);
		synth.set(\HighCut, c);
	}



}