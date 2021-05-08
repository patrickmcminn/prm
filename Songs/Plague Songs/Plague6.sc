/*
Monday, April 26th 2021
PlagueSong6.sc
prm
*/

Plague6 : IM_Module {

	var server, <isLoaded;

	var <concatInput, <multiShiftInput, <delNetInput;
	var <dry, <concat, <multiShift, <delNet;

	var midiDict;

	var seq;

	*new {
		|
		outBus = 0, sequencer, granulator,
		send0Bus, send1Bus, send2Bus, send3Bus,
		relGroup, addAction = 'addToHead'
		|
		^super.new(4, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(sequencer, granulator);
	}

	prInit { | sequencer, granulator |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while ({ try { mixer.isLoaded } != true }, { 0.001.wait; });
			mixer.muteMaster;

			seq = sequencer.uid;

			//dry = IM_HardwareIn.new(1, mixer.chanMono(0), group, \addToHead);
			dry = IM_HardwareIn.newStereo(0, mixer.chanStereo(0), group, \addToHead);
			while({ try { dry.isLoaded} != true }, { 0.001.wait; });

			multiShift = IM_MultiShift.newStereo(mixer.chanStereo(1), [-12, 7, 12], 0.75, group, \addToHead);
			while({ try { multiShift.isLoaded } != true }, { 0.001.wait; });
			//multiShiftInput = IM_HardwareIn.new(1, multiShift.inBus, group, \addToHead);
			multiShiftInput = IM_HardwareIn.newStereo(0, multiShift.inBus, group, \addToHead);
			while({ try { multiShiftInput.isLoaded } != true }, { 0.001.wait; });

			delNet = DelayNetwork.newMono(mixer.chanStereo(2), 5, 0.5, 5, 5, relGroup: group, addAction: \addToHead);
			while({ try { delNet.isLoaded } != true }, { 0.001.wait; });
			delNetInput = IM_HardwareIn.new(0, delNet.inBus, group, \addToHead);
			while({ try { delNetInput.isLoaded } != true }, { 0.001.wait; });

			concat = Concaterwaul.newStereo(mixer.chanStereo(3), relGroup: group, addAction: \addToHead);
			while({ try { concat.isLoaded } != true }, { 0.001.wait; });
			concatInput = IM_HardwareIn.newStereo(0, concat.inBus, group, \addToHead);
			while({ try { concatInput.isLoaded } != true }, { 0.001.wait; });


			server.sync;

			this.prSetInitialParameters(granulator);
			this.prMakeMIDIFuncs(granulator);

			mixer.unMuteMaster;
			server.sync;

			isLoaded = true;
		}
	}

	prSetInitialParameters { | granulator |
		4.do({ | chan | mixer.setPreVol(chan, -9); });
		mixer.setPreVol(2, -12);
		mixer.setPreVol(3, -9);
		//4.do({ | chan | mixer.mute(chan); });
		dry.mute; multiShiftInput.mute; delNetInput.mute; concatInput.mute;

		granulator.mixer.setSendVol(0, -inf);

		mixer.setVol(1, -12);

		// dry:
		mixer.setSendPre(0);
		mixer.mute(0);
		mixer.setSendVol(0, 1, -3);

		// concat:
		mixer.setVol(3, -6);
		mixer.setSendVol(3, 0, -9);
		concat.subMixer.setPreVol(1, -9);
		concat.setGlitchLength(0.5);
		concat.setGlitchModRange(0.45);


		}

	prMakeMIDIFuncs { | granulator |
		midiDict = IdentityDictionary.new;

		midiDict[\section1] = MIDIFunc.noteOn({
			this.triggerSection1(granulator); }, 1, 0, seq);
		midiDict[\section2] = MIDIFunc.noteOn({
			this.triggerSection2(granulator); }, 2, 0, seq);
		midiDict[\section3] = MIDIFunc.noteOn({
			this.triggerSection3(granulator); }, 3, 0, seq);
		midiDict[\section4] = MIDIFunc.noteOn({
			this.triggerSection4(granulator); }, 4, 0, seq);


		midiDict[\granDurLow] = MIDIFunc.cc({ | val |
			var dur = val.linin(0, 127, 0.01, 1.5);
			granulator.setGrainDurLow(dur);
		}, 0, 0, seq);
		midiDict[\granDurHigh] = MIDIFunc.cc({ | val |
			var dur = val.linin(0, 127, 0.01, 1.5);
			granulator.setGrainDurHigh(dur);
		}, 1, 0, seq);
		midiDict[\trigRate] = MIDIFunc.cc({ | val |
			var rate = val.linlin(0, 127, 1, 45);
			granulator.setTrigRate(rate);
		}, 2, 0, seq);
		midiDict[\env] = MIDIFunc.cc({ | val |
			var env;
			switch(val,
				0, { env = 0 },
				1, { env = 'gabor' },
				2, { env = 'gabWide' },
				3, { env = 'perc' },
				4, { env = 'revPerc' },
				5, { env = 'expodec' },
				6 , { env = 'rexpodec' });
			granulator.setGrainEnvelope(env);
		}, 3, 0, seq);
		midiDict[\granRate] = MIDIFunc.cc({ | val |
			var rate;
			switch(val,
				0, { rate = 1 },
				1, { rate = 0.5 },
				2, { rate = 0.25 },
				3, { rate = 2 },
				4, { rate = 1.5 },
				5, { rate = 4 },
				6, { rate = 0.125 },
				7, { rate = 0.00125 });
			granulator.setRate(rate, rate);
		}, 4, 0, seq);

		midiDict[\shiftVol] = MIDIFunc.cc({ | val | mixer.setVol(1, val.ccdbfs); }, 2, 1, seq);
		midiDict[\delayVol] = MIDIFunc.cc({ | val | mixer.setVol(2, val.ccdbfs); }, 3, 1, seq);
		midiDict[\concatVol] = MIDIFunc.cc({ | val | mixer.setVol(3, val.ccdbfs); }, 4, 1, seq);
		midiDict[\concatGlitchVol] = MIDIFunc.cc({ | val | concat.subMixer.setVol(0, val.ccdbfs); }, 5, 1, seq);
		midiDict[\concatUnPitchVol] = MIDIFunc.cc({ | val | concat.subMixer.setVol(1, val.ccdbfs); }, 6, 1, seq);
		midiDict[\concatPitchVol] = MIDIFunc.cc({ | val | concat.subMixer.setVol(2, val.ccdbfs); }, 7, 1, seq);

		midiDict[\dryRec] = MIDIFunc.cc({ | val |
			if( val == 0, { dry.mute }, { dry.unMute; }); }, 1, 2, seq);
		midiDict[\shiftRec] = MIDIFunc.cc({ | val |
			if( val == 0, { multiShiftInput.mute }, { multiShiftInput.unMute; }); }, 2, 2, seq);
		midiDict[\delayRec] = MIDIFunc.cc({ | val |
			if( val == 0, { delNetInput.mute; }, { delNetInput.unMute; }); }, 3, 2, seq);
		midiDict[\concatRec] = MIDIFunc.cc({ | val |
			if( val == 0, { concatInput.mute; }, { concatInput.unMute; }); }, 4, 2, seq);

		midiDict[\concatVerb] = MIDIFunc.cc({ | val | mixer.setSendVol(3, 0, val.ccdbfs); }, 4, 6, seq);

	}

	//////// public funcs:

	free {
		midiDict.do({ | func | func.free; });
		concatInput.free; multiShiftInput.free; delNetInput.free;
		dry.free; concat.free; multiShift.free; delNet.free;
	}

	triggerSection1 { | granulator |
		dry.unMute;
		multiShiftInput.mute;
		delNetInput.mute;
		concatInput.mute;
		granulator.setGrainDur(0.03, 0.3);
		granulator.setTrigRate(32);
		3.do({ | chan | concat.subMixer.setVol(chan, -70); });
		concat.setGlitchLength(0.1);
		concat.setGlitchModRange(0.07);
		mixer.setSendVol(3, 0, -9);
	}

	triggerSection2 { | granulator |
		granulator.setGrainDur(0.5, 1.5);
		granulator.setTrigRate(3);
		concatInput.unMute; delNetInput.unMute; multiShiftInput.mute; dry.unMute;
		concat.setGlitchLength(0.5);
		concat.setGlitchModRange(0.49);
		mixer.setVol(3, -6);
		mixer.setVol(2, -12);
		concat.subMixer.setVol(0, 0);
		concat.subMixer.setVol(1, -18);
		concat.subMixer.setVol(2, -9);
		mixer.setSendVol(3, 0, -9);

	}

	triggerSection3 { | granulator |
		dry.mute; multiShiftInput.mute; delNetInput.mute;
		concatInput.unMute;
		mixer.setVol(3, -3);
		mixer.setSendVol(3, 0, -3);
		mixer.setSendVol(3, 3, -6);
		granulator.setGrainDur(0.04, 0.18);
		granulator.setTrigRate(32);
		granulator.setRate(0.5, 0.5);
		concat.subMixer.setVol(0, -70);
		concat.subMixer.setVol(1, -21);
		concat.subMixer.setVol(2, 0);
	}

	triggerSection4 { | granulator |
		dry.mute;
		mixer.mute(1); mixer.mute(2); mixer.mute(3);
	}


} 