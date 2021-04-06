/*
Monday, April 5th 2021
PlagueSong4.sc
prm
*/

Plague4 : IM_Module {

	var server, <isLoaded;

	var <kick, <shard, <terr, <gran, <decapitator, <subtractive;
	var kickIn, shardIn, terrIn, granIn;

	var <seq, midiDict, subOnArray, subOffArray;

	var <shardEnv, <tremolo, <envelope, <arbharEnv, <lubadhEnv;
	var shardEnvOut, tremoloOut, envelopeOut, arbharEnvOut, lubadhEnvOut;

	var <cvDict;

	var midiFuncsLoaded;

	*new {
		|
		outBus = 0, sequencer, inArray, outArray,
		send0Bus, send1Bus, send2Bus, send3Bus,
		relGroup, addAction = 'addToHead'
		|
		^super.new(5, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(inArray, outArray, sequencer);
	}

	prInit { | inArray, outArray, sequencer |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			midiFuncsLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			mixer.muteMaster;
			server.sync;
			seq = sequencer.uid;

			kickIn = inArray[0];
			shardIn = inArray[1];
			terrIn = inArray[2];
			granIn = inArray[3];

			shardEnvOut = outArray[1];
			tremoloOut = outArray[2];
			envelopeOut = outArray[3];
			arbharEnvOut = outArray[4];
			lubadhEnvOut = outArray[5];

			kick = IM_HardwareIn.new(kickIn, mixer.chanMono(0), group, \addToHead);
			while({ try { kick.isLoaded } != true }, { 0.001.wait; });
			shard = IM_HardwareIn.new(shardIn, mixer.chanMono(1), group, \addToHead);
			while({ try { shard.isLoaded } != true }, { 0.001.wait; });
			terr = IM_HardwareIn.new(terr, mixer.chanMono(2), group, \addToHead);
			while({ try { terr.isLoaded } != true }, { 0.001.wait; });
			gran = IM_HardwareIn.new(shardIn, mixer.chanMono(3), group, \addToHead);
			while({ try { gran.isLoaded } != true }, { 0.001.wait; });

			decapitator = Decapitator.newStereo(mixer.chanStereo(4), nil, nil, nil, nil, group, \addToHead);
			while({ try { decapitator.isLoaded } != true }, { 0.001.wait; });
			subtractive = Subtractive.new(decapitator.inBus, nil, nil, nil, nil, group, \addToHead);
			while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			shardEnv = CV_EnvADSR.new(shardEnvOut, group, \addToHead);
			while( { try { shardEnv.isLoaded } != true }, { 0.001.wait; });
			tremolo = CV_LFO.new(tremoloOut, 1, 'revSaw', 0, 1, group, \addtoHead);
			while({ try { tremolo.isLoaded } != true }, { 0.001.wait; });
			envelope = CV_EnvADSR.new(envelopeOut, group, \addToHead);
			while( { try { envelope.isLoaded } != true }, { 0.001.wait; });
			arbharEnv = CV_EnvADSR.new(arbharEnvOut, group, \addToHead);
			while( { try { arbharEnv.isLoaded } != true }, { 0.001.wait; });
			lubadhEnv = CV_EnvADSR.new(lubadhEnvOut, group, \addToHead);
			while( { try { lubadhEnv.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;
			this.prMakeMIDIFuncs;

			mixer.unMuteMaster;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		5.do({ | chan | mixer.setPreVol(chan, -9); });

		//subtractive.readPreset('plague3-2'
	}

	prMakeMIDIFuncs {
		midiDict = IdentityDictionary.new;

		subOnArray = Array.fill(128, { | i |
			MIDIFunc.noteOn({ | vel | subtractive.playNote(i.midicps, vel.ccdbfs);
		}, i, 13, seq); });
		subOffArray = Array.fill(128, { | i |
			MIDIFunc.noteOff({ subtractive.releaseNote(i.midicps); }, i, 13, seq); });

		cvDict = IdentityDictionary.new;
		cvDict[\shardOn] = MIDIFunc.noteOn({ shardEnv.trigger; }, 1, 0, seq);
		cvDict[\shardOff] = MIDIFunc.noteOff({ shardEnv.release; }, 1, 0, seq);
		cvDict[\envOn] = MIDIFunc.noteOn({ envelope.trigger; }, 2, 0, seq);
		cvDict[\envOff] = MIDIFunc.noteOff({ envelope.release }, 2, 0, seq);

		cvDict[\tremolo1] = MIDIFunc.noteOn({ tremolo.setFrequency(1); }, 3, 0, seq);
		cvDict[\tremolo2] = MIDIFunc.noteOn({ tremolo.setFrequency(1); }, 4, 0, seq);
		cvDict[\tremolo3] = MIDIFunc.noteOn({ tremolo.setFrequency(1); }, 5, 0, seq);
		cvDict[\tremolo4] = MIDIFunc.noteOn({ tremolo.setFrequency(1); }, 6, 0, seq);
		cvDict[\tremolo5] = MIDIFunc.noteOn({ tremolo.setFrequency(1); }, 7, 0, seq);
		cvDict[\tremolo6] = MIDIFunc.noteOn({ tremolo.setFrequency(1); }, 8, 0, seq);

		cvDict[\arbharEnv] = MIDIFunc.noteOn({ arbharEnv.trigger; }, 9, 0, seq);
		cvDict[\arbharEnv] = MIDIFunc.noteOfF({ arbharEnv.release; }, 9, 0, seq);
		cvDict[\lubadhEnv] = MIDIFunc.noteOn({ lubadhEnv.trigger; }, 10, 0, seq);
		cvDict[\lubadhEnv] = MIDIFunc.noteOff({ lubadhEnv.relase; }, 10, 0, seq);

		midiFuncsLoaded = true;

	}

	prFreeMIDIFuncs {
		midiDict.do({ | i | i.free; });
		subOnArray.do({ | i | i.free; });
		subOffArray.do({ | i | i.free; });
		cvDict.do({ | i | i.free; });
	}

	free {
		this.prFreeMIDIFuncs;
		kick.free; shard.free; terr.free; gran.free; decapitator.free; subtractive.free;
		kickIn.free; shardIn.free; terrIn.free; granIn.free;
		this.freeModule;
	}

}