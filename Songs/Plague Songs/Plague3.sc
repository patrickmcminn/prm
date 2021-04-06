/*
Tuesday, March 9th 2021
Plague3.sc
prm
*/

Plague3 : IM_Module  {

	var server, <isLoaded;

	var <tptSendOut, <arbRecordOut, <arbLayerOut, <mimMixOut, <guitarMixOut;
	var <moogInput, <terrInput, <plaitsInput, <tptReturnInput;

	var <trumpet, <hornInput, <horn;
	var <moog, <terrarium, <plaits, <subtractive;

	var <seq, midiDict, subOnArray, subOffArray;

	var <arbRecord, <arbLayer, <mimeophonMix, <guitarMix;

	var <cvDict;

	var midiFuncsLoaded;

	*new {
		|
		outBus = 0, micInBus, pickupInBus, sequencer, inArray, outArray,
		send0Bus, send1Bus, send2Bus, send3Bus,
		relGroup, addAction = 'addToHead'
		|
		^super.new(6, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(micInBus, pickupInBus, inArray, outArray, sequencer);
	}

	prInit { | micInBus, pickupInBus, inArray, outArray, sequencer |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			midiFuncsLoaded = false;

			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			mixer.muteMaster;

			server.sync;

			seq = sequencer.uid;

			tptSendOut = outArray[1];
			arbRecordOut = outArray[2];
			arbLayerOut = outArray[3];
			mimMixOut = outArray[4];
			guitarMixOut = outArray[5];

			moogInput = inArray[0];
			terrInput = inArray[1];
			plaitsInput = inArray[2];
			tptReturnInput = inArray[3];

			trumpet = IM_HardwareIn.new(micInBus, mixer.chanMono(0), group, \addToHead);
			while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

			horn = IM_HardwareIn.new(tptReturnInput, mixer.chanMono(1), group, \addToHead);
			while({ try { horn.isLoaded } != true }, { 0.001.wait; });
			hornInput = IM_HardwareIn.new(pickupInBus, tptSendOut, group, \addToHead);
			while({ try { hornInput.isLoaded } != true }, { 0.001.wait; });

			moog = IM_HardwareIn.new(moogInput, mixer.chanMono(2), group, \addToHead);
			while({ try { moog.isLoaded } != true }, { 0.001.wait; });

			terrarium = IM_HardwareIn.new(terrInput, mixer.chanMono(3), group, \addToHead);
			while({ try { terrarium.isLoaded } != true }, { 0.001.wait; });

			plaits = IM_HardwareIn.new(plaitsInput, mixer.chanMono(4), group, \addToHead);
			while({ try { plaits.isLoaded } != true }, { 0.001.wait; });

			subtractive = Subtractive.new(mixer.chanStereo(5), nil, nil, nil, nil, group, \addToHead);
			while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			arbRecord = CV_Gate.new(arbRecordOut, group, \addToHead);
			while({ try { arbRecord.isLoaded } != true }, { 0.001.wait; });
			arbLayer = CV_Constant.new(arbLayerOut, 0, group, \addToHead);
			while({ try { arbLayer.isLoaded } != true }, { 0.001.wait; });
			mimeophonMix = CV_Constant.new(mimMixOut, 0, group, \addToHead);
			while({ try { mimeophonMix.isLoaded } != true }, { 0.001.wait; });
			guitarMix = CV_Constant.new(guitarMixOut, 0, group, \addToHead);
			while({ try { guitarMix.isLoaded } != true }, { 0.001.wait; });

			this.prSetInitialParameters;
			this.prMakeMIDIFuncs;

			mixer.unMuteMaster;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		6.do({ | chan | mixer.setPreVol(chan, -9); });

		subtractive.readPreset('plague3-2');

		// trumpets no, no, no:
		mixer.mute(0); mixer.mute(1);

		// moog:
		mixer.mute(2);
		mixer.setVol(2, -6);

		// terrarium:
		mixer.setVol(3, -70);

		// plaitsDrone:
		mixer.setPreVol(4, 0);
		mixer.setVol(4, -70);

		// subtractive:
		mixer.setVol(5, -9);

	}

	prMakeMIDIFuncs {
		midiDict = IdentityDictionary.new;

		subOnArray = Array.fill(128, { | i |
			MIDIFunc.noteOn({ | vel | subtractive.playNote(i.midicps, vel.ccdbfs);
		}, i, 13, seq); });
		subOffArray = Array.fill(128, { | i |
			MIDIFunc.noteOff({ subtractive.releaseNote(i.midicps); }, i, 13, seq); });

		cvDict = IdentityDictionary.new;
		cvDict[\arbharRecOn] = MIDIFunc.noteOn({ arbRecord.makeGate(0.5) }, 1, 0, seq);
		cvDict[\arbharRecOff] = MIDIFunc.noteOff({ arbRecord.releaseGate }, 1, 0, seq);
		cvDict[\arbharLayer] = MIDIFunc.cc({ | val |
			var layer = val.linlin(0, 127, 0, 0.25);
			arbLayer.setValue(layer);
		}, 1, 0, seq);
		cvDict[\mimeophonMix] = MIDIFunc.cc({ | val |
			var mix = val.linlin(0, 127, 0, 1);
			mimeophonMix.setValue(mix);
		}, 2, 0, seq);
		cvDict[\guitarMix] = MIDIFunc.cc({ | val |
			var mix = val.linlin(0, 127, 0, 1);
			guitarMix.setValue(mix);
		}, 3, 0, seq);

		midiFuncsLoaded = true;

	}

	prFreeMIDIFuncs {
		midiDict.do({ | i | i.free; });
		subOnArray.do({ | i | i.free; });
		subOffArray.do({ | i | i.free; });
		cvDict.do({ | i | i.free; });
	}

	//////// PUBLIC FUNCTIONS:
	free {
		this.prFreeMIDIFuncs;
		trumpet.free; horn.free; moog.free; terrarium.free; plaits.free; subtractive.free;
		arbRecord.free; arbLayer.free; mimeophonMix.free; guitarMix.free;
		this.freeModule;
	}


}

/*
Arbhar Values:
0 - 1
0.05 -2
0.075 - 3
0.11 - 4
0.15 - 5
0.19 - 6

0.23 - 1

seems to wrap without hitting omega buffr
*/

/*
WHAT DO I NEED for CV?

- send (just for old time's sake)
- tpt send
- Arbhar Record
- Arbhar Layer
- Mimeophon Mix
- Guitar dry/wet

on the table:
- arbhar reverb
- arbhar length?
- something plaits related?

MIXER:
- trumpet mic
- trumpet pickup
- subtractive
- moog might as well
- E352
- Plaits Drone (hopefully more interesting later? eek!)
	((( - trumpet return. )))

*/

