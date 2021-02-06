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

	*new {
		|
		outBus = 0, micInBus, pickupInBus, sequencer, modularOutBus,
		moogInBus, textureInBus, subInBus, terrInBus,
		send0Bus, send1Bus, send2Bus, send3Bus,
		relGroup, addAction = 'addToHead'
		|
		^super.new(6, outBus, send0Bus, send1Bus, send2Bus, send3Bus,  false, relGroup, addAction).prInit(micInBus, pickupInBus, modularOutBus, sequencer, moogInBus, textureInBus, subInBus, terrInBus);
	}

	prInit { | micInBus, pickupInBus, modularOutBus, sequencer, moogInBus, textureInBus, subInBus, terrInBus |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true },  { 0.001.wait; });

			mixer.muteMaster;

			trumpet = IM_HardwareIn.new(micInBus, mixer.chanStereo(0), group, \addToHead);
			while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

			tptEQ = Equalizer.newStereo(mixer.chanStereo(1), group, \addToHead);
			while({ try { tptEQ.isLoaded } != true }, { 0.001.wait; });
			tptShift = IM_MultiShift.new(tptEQ.inBus, [-14, -7, 7], 0.9, group, \addToHead);
			while({ try { tptShift.isLoaded } != true }, { 0.001.wait; });
			tptInput = IM_HardwareIn.new(pickupInBus, tptShift.inBus, group, \addToHead);
			while({ try { tptInput.isLoaded } != true }, { 0.001.wait; });

			texture = IM_HardwareIn.new(textureInBus, mixer.chanStereo(2), group, \addToHead);
			while({ try { texture.isLoaded } != true }, { 0.001.wait });

			moog = IM_HardwareIn.new(moogInBus, mixer.chanStereo(3), group, \addToHead);
			while({ try { moog.isLoaded } != true }, { 0.001.wait });

			subReturn = IM_HardwareIn.new(subInBus, mixer.chanStereo(4), group, \addToHead);
			while({ try { subReturn.isLoaded } != true }, { 0.001.wait });
			subSend = MonoHardwareSend.new(modularOutBus, nil, nil, nil, nil, false, group, \addToHead);
			while({ try { subSend.isLoaded } != true }, { 0.001.wait; });
			subtractive = Subtractive.new(subSend.inBus, nil, nil, nil, nil, group, \addToHead);
			while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });

			terrarium = IM_HardwareIn.new(terrInBus, mixer.chanStereo(5), group, \addToHead);
			while({ try { terrarium.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			mixer.unMuteMaster;

			isLoaded = true;
		};
	}

	prSetInitialParameters {
		///// don't forget to set the reverb and granulator on the control surface!

		subtractive.loadPreset('plague2-3');
		tptEQ.setLowPassCutoff(4500);
		tptEQ.setHighPassCutoff(200);

		//// pre vol:
		6.do({ | chan | mixer.setPreVol(chan, -9); });

		//// trumpet:
		mixer.setVol(0, -6);
		mixer.setSendVol(0, 0, -3);
		mixer.setSendVol(0, 1, -3);
		mixer.mute(0);


		//// tpt shift:
		mixer.setVol(1, -9);
		mixer.setSendVol(1, 0, -9);
		mixer.mute(0);


		//// texture:
		mixer.setVol(2, -inf);
		mixer.setSendVol(2, 0, -3);

		//// moog:
		mixer.setVol(3, -6);
		mixer.setSendVol(3, 0, -9);

		//// subtractive:
		mixer.setVol(4, -3);
		mixer.setSendVol(4, 0, -6);
		mixer.setSendVol(4, 1, -3);

		//// terrarium:
		mixer.setVol(5, -9);
		mixer.setSendVol(5, 0, -3);
		mixer.setSendVol(5, 3, -3);

	}

	//////// public functions:

	free {
		trumpet.free;
		tptEQ.free; tptShift.free; tptInput.free;
		texture.free; moog.free;
		subtractive.free; subSend.free; subReturn.free;
		terrarium.free;

		this.freeModule;
	}

	setChord1Shift { tptShift.setShiftArray([7, -7, -14]); }

	setChord2Shift { tptShift.setShiftArray([-10, -3, 7]); }

	setChord3Shift { }

	setClusterShift { tptShift.setShiftArray([-2, 2, 3]); }

}
