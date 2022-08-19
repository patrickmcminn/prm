/*
Caspases.sc
Tuesday, August 9th 2022
prm
*/

Caspases : IM_Module {

	var <isLoaded, server;
	var <texture, <lead,  <synthMel;
	var <trumpet, <shiftTrumpet, <distTrumpet, <scTrumpet, <trumpetDelays, <trumpetDelaysInput;
	var <prickles, <pad, <kick;
	var <midiDict, <midiEnabled;

	var <sequencer;

	*new {
		|
		outBus, micIn, pickupIn, modInArray, digiArray, seq,
		send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead'
		|
		^super.new(11, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(micIn, pickupIn, modInArray, digiArray, seq);

	}

	prInit { | micIn, pickupIn, modInArray, digiArray, seq |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			sequencer = seq.uid;
			midiDict = IdentityDictionary.new;
			midiEnabled = false;

			texture = SampleGrid.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
			while({ try { texture.isLoaded } != true }, { 0.001.wait; });

			lead = Caspases_Lead.new(mixer.chanStereo(1), digiArray[0], group, \addToHead);
			while({ try { lead.isLoaded } != true }, { 0.001.wait; });

			synthMel = Caspases_Moog.new(mixer.chanStereo(2), modInArray[0], group, \addToHead);
			while({ try { synthMel.isLoaded } != true }, { 0.001.wait; });
			/*
			distTrumpet = Caspases_DistTpt.new(mixer.chanStereo(4), pickupIn, group, \addToHead);
			while({ try { distTrumpet.isLoaded } != true }, { 0.001.wait; });

			trumpetDelays = DelayNetwork.newMono(mixer.chanStereo(7), 4, 1, 1, 10, relGroup: group, addAction: \addToHead);
			while({ try { trumpetDelays.isLoaded } != true }, { 0.001.wait; });

			trumpetDelaysInput = IM_HardwareIn.new(micIn, trumpetDelays.inBus, group, \addToHead);
			while({ try { trumpetDelaysInput.isLoaded } != true }, { 0.001.wait; });

			*/
			prickles = Subtractive.new(mixer.chanStereo(8), relGroup: group, addAction: \addToHead);
			while({ try { prickles.isLoaded } != true }, { 0.001.wait; });


			pad = SubJuno.new(mixer.chanStereo(9), relGroup: group, addAction: \addToHead);
			while({ try { pad.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialParameters {

		11.do({ | chan | mixer.setPreVol(chan, -9); });

		// texture:
		texture.readPreset('caspasesTextures');
		mixer.setPreVol(0, -12);
		mixer.setVol(0, -6);
		mixer.setSendVol(0, 0, -3);

		// lead:
		//mixer.setPreVol(1, -6);
		mixer.setVol(1, -9);
		mixer.setSendVol(1, 0, -24);

		// synth mel:
		mixer.setVol(2, -9);
		mixer.setSendVol(2, 0, -5);

		// prickles:
		mixer.setVol(8, -9);
		mixer.setSendVol(8, 0, -18);
		mixer.setSendVol(8, 1, -9);


		// synth pad:
		mixer.setPreVol(9, -24);
		mixer.setVol(9, -15);
		mixer.setSendVol(9, -6);

		/*
		trumpetDelays.setDelayTime(0, 3.636363636363636);
		trumpetDelays.setFeedback(0, 0.55);
		trumpetDelays.setPan(0, -0.90);
		trumpetDelays.setFilterType(0, 3);
		trumpetDelays.setCutoff(0, 300);

		trumpetDelays.setDelayTime(1, 5);
		trumpetDelays.setFeedback(1, 0.6);
		trumpetDelays.setPan(1, 0.8);
		trumpetDelays.setFilterType(3);
		trumpetDelays.setCutoff(400);

		trumpetDelays.setDelayTime(2, 4.090909090909091);
		trumpetDelays.setFeedback(2, 0.6);
		trumpetDelays.setPan(2, -0.42);
		trumpetDelays.setFilterType(2, 3);
		trumpetDelays.setCutoff(2, 300);

		trumpetDelays.setDelayTime(3, 2.727272727272727);
		trumpetDelays.setFeedback(3, 0.5);
		trumpetDelays.setPan(3, 0.68);
		trumpetDelays.setFilterType(3, 3);
		trumpetDelays.setCutoff(3, 300);
		*/

		prickles.readPreset('caspasesPrickles');

		pad.readPreset('caspasesJuno');
		pad.chorus.setDepth(0.35);
		pad.chorus.setTimeMul(0.05);

	}

	//////// public functions:

	free { }

	toggleMIDIFuncs {
		if( midiEnabled == false,
			{ this.makeMIDIFuncs }, { this.freeMIDIFuncs });
	}

	makeMIDIFuncs {
		midiDict[\samplerOn] = Array.fill(16, { | note |
			MIDIFunc.noteOn({ | vel | texture.playSample(note, vel.ccdbfs); }, (note+60), 0, sequencer); });
		midiDict[\samplerOff] = Array.fill(16, { | note |
			MIDIFunc.noteOff({ texture.releaseSample(note); }, (note+60), 0, sequencer) });

		// prickles:
		midiDict[\prickles] = Array.fill(128, { | note |
			MIDIFunc.noteOn({ | vel | prickles.playNote(note.midicps, vel.ccdbfs) }, note, 1, sequencer); });
		midiDict[\prickles] = Array.fill(128, { | note |
			MIDIFunc.noteOff({ | vel | prickles.releaseNote(note.midicps) }, note, 1, sequencer); });

		// juno pad:
		midiDict[\junoOn] = Array.fill(128, { | note |
			MIDIFunc.noteOn({ | vel | pad.playNote(note.midicps, vel.ccdbfs) }, note, 2, sequencer); });
		midiDict[\junoOff] = Array.fill(128, { | note |
			MIDIFunc.noteOff({ | vel | pad.releaseNote(note.midicps) }, note, 2, sequencer); });

		midiEnabled = true;
	}

	freeMIDIFuncs {
		midiDict.do({ | func | func.free; });
		midiEnabled = false;
	}




} 