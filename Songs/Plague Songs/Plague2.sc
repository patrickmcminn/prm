/*
Saturday, February 6th 2021
Plague2.sc
prm

class for shortsong that doesn't have much of a name yet
and also isn't done
*/

Plague2 : IM_Module {

	var server, <isLoaded;
	var <trumpet;
	var <tptEQ, <tptShift, <tptInput;
	var <texture, <moog;
	var <subtractive, <subSend, <subReturn;
	var <terrarium;
	var <seq, midiDict, subOnArray, subOffArray;
	var <cvDict;
	var <activeChord;
	var midiFuncsLoaded;

	var <arbGate, <plaitsVol, <delayMix, <distMix;

	*new {
		|
		outBus = 0, micInBus, pickupInBus, sequencer, modularOutBus, subOutBus,
		arbGateOut, plaitsVolOut, delayMixOut, distMixOut,
		moogInBus, subInBus, textureInBus, terrInBus,
		send0Bus, send1Bus, send2Bus, send3Bus,
		relGroup, addAction = 'addToHead'
		|
		^super.new(6, outBus, send0Bus, send1Bus, send2Bus, send3Bus,  false, relGroup, addAction).prInit(micInBus, pickupInBus, modularOutBus, subOutBus, arbGateOut, plaitsVolOut, delayMixOut, distMixOut, sequencer, moogInBus, subInBus, textureInBus, terrInBus);
	}

	prInit {
		|
		micInBus, pickupInBus, modularOutBus, subOutBus, arbGateOut, plaitsVolOut, delayMixOut,
		distMixOut, sequencer, moogInBus, subInBus, textureInBus, terrInBus
		|
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true },  { 0.001.wait; });

			server.sync;

			mixer.muteMaster;

			seq = sequencer.uid;

			trumpet = IM_HardwareIn.new(micInBus, mixer.chanMono(0), group, \addToHead);
			while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

			tptEQ = Equalizer.newStereo(mixer.chanStereo(1), group, \addToHead);
			while({ try { tptEQ.isLoaded } != true }, { 0.001.wait; });
			tptShift = IM_MultiShift.new(tptEQ.inBus, [-14, -7, 7], 0.9, group, \addToHead);
			while({ try { tptShift.isLoaded } != true }, { 0.001.wait; });
			tptInput = IM_HardwareIn.new(pickupInBus, tptShift.inBus, group, \addToHead);
			while({ try { tptInput.isLoaded } != true }, { 0.001.wait; });

			texture = IM_HardwareIn.new(textureInBus, mixer.chanMono(2), group, \addToHead);
			while({ try { texture.isLoaded } != true }, { 0.001.wait });

			moog = IM_HardwareIn.new(moogInBus, mixer.chanMono(3), group, \addToHead);
			while({ try { moog.isLoaded } != true }, { 0.001.wait });

			subReturn = IM_HardwareIn.new(subInBus, mixer.chanMono(4), group, \addToHead);
			while({ try { subReturn.isLoaded } != true }, { 0.001.wait });
			subSend = MonoHardwareSend.new(subOutBus, nil, nil, nil, nil, false, group, \addToHead);
			while({ try { subSend.isLoaded } != true }, { 0.001.wait; });
			subtractive = Subtractive.new(subSend.inBus, nil, nil, nil, nil, group, \addToHead);
			while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });


			terrarium = IM_HardwareIn.new(terrInBus, mixer.chanMono(5), group, \addToHead);
			while({ try { terrarium.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			arbGate = CV_Gate.new(arbGateOut, group, \addToHead);
			while({ try { arbGate.isLoaded } != true }, { 0.001.wait; });
			plaitsVol = CV_Constant.new(plaitsVolOut, 0, group, \addToHead);
			while({ try{ plaitsVol.isLoaded } != true }, { 0.001.wait; });
			delayMix = CV_Constant.new(delayMixOut, 0.5, group, \addToHead);
			while({ try { delayMix.isLoaded } != true }, { 0.001.wait; });
			distMix = CV_Constant.new(distMixOut, 0, group, \addToHead);
			while({ try { distMix.isLoaded } != true }, { 0.001.wait; });

			this.prSetInitialParameters;
			this.prMakeMIDIFuncs;

			mixer.unMuteMaster;

			isLoaded = true;
		};
	}

	prSetInitialParameters {
		///// don't forget to set the reverb and granulator on the control surface!

		subtractive.readPreset('plague2-4');
		tptEQ.setLowPassCutoff(4500);
		tptEQ.setHighPassCutoff(200);
		activeChord = 1;

		//// pre vol:
		6.do({ | chan | mixer.setPreVol(chan, -9); });
		//// and then a little boost for my texture friend:
		mixer.setPreVol(2, 0);
		//// and another little boost for the tpt shift.
		// Making Exceptions all over the place, aren't we?
		mixer.setPreVol(1, -6);

		//// trumpet:
		mixer.setVol(0, -6);
		mixer.setSendVol(0, 0, -3);
		mixer.setSendVol(0, 1, -3);
		mixer.setSendVol(0, 2, 0);
		mixer.mute(0);


		//// tpt shift:
		mixer.setVol(1, -9);
		mixer.setSendVol(1, 0, -9);
		mixer.mute(1);

		//// texture:
		mixer.setVol(2, -inf);
		//mixer.setSendVol(2, 0, -3);
		mixer.setSendVol(2, 0, -12);

		//// moog:
		mixer.setVol(3, -6);
		mixer.setSendVol(3, 0, -9);

		//// subtractive:
		mixer.setVol(4, -inf);
		mixer.setSendVol(4, 0, -6);
		mixer.setSendVol(4, 1, -3);

		//// terrarium:
		mixer.setVol(5, -inf);
		mixer.setSendVol(5, 0, -3);
		mixer.setSendVol(5, 3, -3);

	}

	prMakeMIDIFuncs {
		midiDict = IdentityDictionary.new;
		midiDict[\shift1] = MIDIFunc.cc({ | val | tptShift.setShiftVol(0, val.ccdbfs) }, 1, 0, seq);
		midiDict[\shift2] = MIDIFunc.cc({ | val | tptShift.setShiftVol(1, val.ccdbfs) }, 2, 0, seq);
		midiDict[\shift3] = MIDIFunc.cc({ | val | tptShift.setShiftVol(2, val.ccdbfs) }, 3, 0, seq);
		midiDict[\shift4] = MIDIFunc.cc({ | val | tptShift.setDryVol(val.ccdbfs) }, 4, 0, seq);

		midiDict[\tptVol] = MIDIFunc.cc({ | val | mixer.setVol(0, val.ccdbfs); }, 1, 1, seq);
		midiDict[\shiftVol] = MIDIFunc.cc({ | val | mixer.setVol(1, val.ccdbfs); }, 2, 1, seq);
		midiDict[\textureVol] = MIDIFunc.cc({ | val | mixer.setVol(2, val.ccdbfs); }, 3, 1, seq);
		midiDict[\moogVol] = MIDIFunc.cc({ | val | mixer.setVol(3, val.ccdbfs); }, 4, 1, seq);
		midiDict[\subVol] = MIDIFunc.cc({ | val | mixer.setVol(4, val.ccdbfs); }, 5, 1, seq);
		midiDict[\terrVol] = MIDIFunc.cc({ | val | mixer.setVol(5, val.ccdbfs); }, 6, 1, seq);

		midiDict[\tptGranSend] = MIDIFunc.cc({ | val | mixer.setSendVol(0, 1, val.ccdbfs); }, 7, 1, seq);
		midiDict[\subGranSend] = MIDIFunc.cc({ | val | mixer.setSendVol(4, 1, val.ccdbfs); }, 8, 1, seq);
		midiDict[\shiftGranSend] = MIDIFunc.cc({ | val | mixer.setSendVol(1, 1, val.ccdbfs); }, 9, 1, seq);

		subOnArray = Array.fill(128, { | i |
			MIDIFunc.noteOn({ | vel | subtractive.playNote(i.midicps, vel.ccdbfs);
		}, i, 13, seq); });
		subOffArray = Array.fill(128, { | i |
			MIDIFunc.noteOff({ subtractive.releaseNote(i.midicps); }, i, 13, seq); });

		cvDict = IdentityDictionary.new;
		cvDict[\gateOn] = MIDIFunc.noteOn({ arbGate.makeGate(0.5) }, 1, 0, seq);
		cvDict[\gateOff] = MIDIFunc.noteOff({ arbGate.releaseGate; }, 1, 0, seq);
		cvDict[\plaitsVol] = MIDIFunc.cc({ | val |
			var cv = val.linlin(0, 127, 0, 0.5);
			plaitsVol.setValue(cv);
		}, 5, 0, seq);
		cvDict[\delayMix] = MIDIFunc.cc({ | val |
			var cv = val.linlin(0, 127, 0, 0.5);
			delayMix.setValue(cv);
		}, 6, 0, seq);
		cvDict[\distMix] = MIDIFunc.cc({ | val |
			var cv = val.linlin(0, 127, 0, 1);
			distMix.setValue(cv);
		}, 7, 0, seq);

		midiFuncsLoaded = true;
	}

	prFreeMIDIFuncs {
		midiDict.do({ | func | func.free; });
		subOnArray.do({ | func | func.free; });
		subOffArray.do({ | func | func.free; });
		cvDict.do({ | func | func.free; });
		midiFuncsLoaded = false;
	}

	//////// public functions:

	free {
		trumpet.free;
		tptEQ.free; tptShift.free; tptInput.free;
		texture.free; moog.free;
		subtractive.free; subSend.free; subReturn.free;
		terrarium.free;

		arbGate.free; plaitsVol.free; delayMix.free;

		this.prFreeMIDIFuncs;

		this.freeModule;
	}

	setGranulatorPreset { | granulator |
		granulator.setRate(0.25, 0.25);
		granulator.setGrainDur(1, 1.75);
		granulator.setGrainEnvelope('hanning');
		granulator.setDelayTime(1);
		granulator.setDelayLevel(0.7);
		granulator.setFeedback(0.5);
	}

	resetGranulator { | granulator |
		granulator.setRate(1, 1);
		granulator.setGrainDur(0.05, 0,15);
		granulator.setGrainEnvelope(-1);
	}

	setChoralePreset { | reverb |
		mixer.setPanBal(3, -0.25);
		mixer.setPanBal(4, 0.5);
		mixer.setPanBal(5, -0.75);
		reverb.setDecayTime(8);
	}

	setChord1Shift { activeChord = 1; tptShift.setShiftArray([7, -7, -14]); }
	setChord2Shift { activeChord = 2; tptShift.setShiftArray([-10, -3, 7]); }
	setChord3Shift { activeChord = 3; tptShift.setShiftArray([-7, -3, 4]); }
	setClusterShift { activeChord = 'cluster'; tptShift.setShiftArray([-2, 2, 3]); }
	setMinorChordShift { activeChord = 'minor'; tptShift.setShiftArray([-2, 3, 7]); }

}
