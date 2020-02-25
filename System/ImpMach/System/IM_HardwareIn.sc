/*
Thursday, January 9th 2013

prm

JAB 7/22/2014

PRM 2/25/2020
*/

IM_HardwareIn {
	var <isLoaded;
	var synth;
	var <isMuted;

	*new { |inBus = 0, outBus = 0, relGroup = nil, addAction = \addToHead|
		^super.new.prInit(inBus, outBus, relGroup, addAction);
	}

	*newStereo { | inBus = 0, outBus = 0, relGroup = nil, addAction = \addToHead |
		^super.new.prInitStereo(inBus, outBus, relGroup, addAction)
	}

	prInit { |inBus = 0, outBus = 0, relGroup = nil, addAction = \addToHead|
		var server = Server.default;

		server.waitForBoot {
			isLoaded = false;

			this.prAddSynthDef;
			server.sync;

			this.prMakeInputs(inBus, outBus, relGroup, addAction);

			while({ synth == nil }, { 0.001.wait });

			isMuted = false;
			isLoaded = true;
		};
	}

	prInitStereo { | inBus = 0, outBus = 0, relGroup = nil, addAction = \addToHead|
		var server = Server.default;
		server.waitForBoot {
			isLoaded = false;

			this.prAddSynthDef;
			server.sync;
			this.prMakeStereoInputs(inBus, outBus, relGroup, addAction);

			while({ synth == nil }, { 0.001.wait });

			isMuted = false;
			isLoaded = true;
		};
	}



	prAddSynthDef {
		SynthDef(\IM_extIn, { |inBus = 0, outBus = 0, mute = 1|
			var sig, out;
			sig = SoundIn.ar(inBus, 1);
			sig = sig * mute;
			out = Out.ar(outBus, sig);
		}).add;

		SynthDef(\IM_extIn_Stereo, { | inBus = 0, outBus = 0, mute = 1 |
			var sig, out;
			sig = SoundIn.ar([inBus, (inBus+1)]);
			sig = sig * mute;
			out = Out.ar(outBus, sig);
		}).add;
	}

	prMakeInputs { | inBus = 0, outBus = 0, relGroup = nil, addAction = \addToHead |
		synth = Synth(\IM_extIn, [\inBus, inBus, \outBus, outBus], relGroup, addAction);
	}

	prMakeStereoInputs { | inBus = 0, outBus = 0, relGroup = nil, addAction = \addToHead |
		synth = Synth(\IM_extIn_Stereo, [\inBus, inBus, \outBus, outBus], relGroup, addAction);
	}

	free {
		isLoaded = false;
		synth.free;
		synth = nil;
	}

	mute {
		isMuted = true;
		synth.set(\mute, 0);
	}

	unMute {
		isMuted = false;
		synth.set(\mute, 1);
	}

	tglMute {
		if( isMuted == false, { this.mute }, { this.unMute });
	}

}