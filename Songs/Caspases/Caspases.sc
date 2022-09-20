/*
Caspases.sc
Tuesday, August 9th 2022
prm
*/

Caspases : IM_Module {

	var <isLoaded, server;
	var <texture, <lead,  <synthMel;
	var <tpt, <shiftTpt, <distTpt, <scTpt, <tptDelays, <tptDelaysInput;
	var <prickles, <pricklesEQ, <pad, <padEQ, <kick;
	var <midiDict, <midiEnabled;

	var <sequencer;

	*new {
		|
		outBus, micIn, pickupIn, modInArray, digiArray, seq,
		send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead'
		|
		^super.new(9, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(micIn, pickupIn, modInArray, digiArray, seq);

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

			pricklesEQ = Equalizer.newStereo(mixer.chanStereo(3), group, \addToHead);
			while( { try { pricklesEQ.isLoaded } != true }, { 0.001.wait; });

			prickles = Subtractive.new(mixer.chanStereo(3), relGroup: group, addAction: \addToHead);
			while({ try { prickles.isLoaded } != true }, { 0.001.wait; });

			padEQ = Equalizer.newStereo(mixer.chanStereo(4), group, \addToHead);
			while({ try { padEQ.isLoaded } != true }, { 0.001.wait; });

			pad = SubJuno.new(padEQ.inBus, relGroup: group, addAction: \addToHead);
			while({ try { pad.isLoaded } != true }, { 0.001.wait; });

			tptDelays = DelayNetwork.newMono(mixer.chanStereo(5), 4, 1, 1, 10, relGroup: group, addAction: \addToHead);
			while({ try { tptDelays.isLoaded } != true }, { 0.001.wait; });

			tptDelaysInput = IM_HardwareIn.new(micIn, tptDelays.inBus, group, \addToHead);
			while({ try { tptDelaysInput.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			tptDelaysInput.mute;

			distTpt = Caspases_DistTpt.new(mixer.chanStereo(6), pickupIn, group, \addToHead);
			while({ try { distTpt.isLoaded } != true }, { 0.001.wait; });

			scTpt = Caspases_SCTPT.new(mixer.chanStereo(6), pickupIn, group, \addToHead);
			while({ try { scTpt.isLoaded } != true }, { 0.001.wait; });

			shiftTpt = Caspases_ShiftTpt.new(mixer.chanStereo(6), pickupIn, group, \addToHead);
			while({ try { shiftTpt.isLoaded } != true }, { 0.001.wait; });

			tpt = IM_HardwareIn.new(micIn, mixer.chanStereo(7), group, \addToHead);
			while({ try { tpt.isLoaded } != true }, { 0.001.wait; });

			kick = IM_HardwareIn.newStereo(digiArray[1], mixer.chanStereo(8), group, \addToHead);
			while({ try { kick.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialParameters {

		9.do({ | chan | mixer.setPreVol(chan, -9); });

		// texture:
		texture.readPreset('caspasesTextures');
		//mixer.setPreVol(0, -12);
		mixer.setPreVol(0, -3);
		mixer.setVol(0, -6);
		mixer.setSendVol(0, 0, -9);

		// lead:
		//mixer.setPreVol(1, -6);
		mixer.setVol(1, -inf);
		mixer.setSendVol(1, 0, -24);

		// synth mel:
		mixer.setVol(2, -inf);
		mixer.setSendVol(2, 0, -5);

		// prickles:
		mixer.setPreVol(3, -3);
		mixer.setVol(3, -inf);
		mixer.setSendVol(3, 0, -18);
		mixer.setSendVol(3, 1, -9);
		prickles.readPreset('caspasesPrickles');
		pricklesEQ.setPeak2Freq(250);
		pricklesEQ.setPeak2Gain(-3);
		pricklesEQ.setPeak3Freq(1300);
		pricklesEQ.setPeak3Gain(-3);
		pricklesEQ.setHighPassCutoff(200);

		// synth pad:
		mixer.setPreVol(4, -24);
		mixer.setVol(4, -inf);
		mixer.setSendVol(4, -6);
		padEQ.setPeak2Freq(300);
		padEQ.setPeak2Gain(-3);
		padEQ.setPeak3Freq(1100);
		padEQ.setPeak3Gain(-3);
		padEQ.setLowFreq(180);
		padEQ.setLowGain(3);

		pad.readPreset('caspasesJuno');
		//pad.readPreset('caspasesArrival');
		pad.chorus.setDepth(0.35);
		pad.chorus.setTimeMul(0.05);

		// trumpet delay:
		mixer.setPreVol(5, -3);
		mixer.setSendVol(5, -24);
		mixer.setVol(5, -6);

		tptDelays.setDelayTime(0, 3.636363636363636);
		tptDelays.setFeedback(0, 0.55);
		tptDelays.setPan(0, -0.90);
		tptDelays.setFilterType(0, 2);
		tptDelays.setCutoff(0, 300);

		tptDelays.setDelayTime(1, 5);
		tptDelays.setFeedback(1, 0.6);
		tptDelays.setPan(1, 0.8);
		tptDelays.setFilterType(1, 2);
		tptDelays.setCutoff(400);

		tptDelays.setDelayTime(2, 4.090909090909091);
		tptDelays.setFeedback(2, 0.6);
		tptDelays.setPan(2, -0.42);
		tptDelays.setFilterType(2, 2);
		tptDelays.setCutoff(2, 300);

		tptDelays.setDelayTime(3, 2.727272727272727);
		tptDelays.setFeedback(3, 0.5);
		tptDelays.setPan(3, 0.68);
		tptDelays.setFilterType(3, 2);
		tptDelays.setCutoff(3, 300);

		mixer.setVol(6, -6);
		mixer.setSendVol(6, 0, -12);

		// tpt distortion:
		distTpt.distortion.loadPreset('caspasesTpt');
		distTpt.mixer.setVol(-18);
		distTpt.mixer.setPreVol(-12);

		// sc tpt:
		scTpt.mixer.setVol(-5);

		// trumpet:
		tpt.mute;
		mixer.setPreVol(7, 0);
		mixer.setVol(7, -3);
		mixer.setSendVol(7, 0, -6);

		// kick:
		kick.mute;
		mixer.setVol(8, -9);
		mixer.setPreVol(8, 0);
		mixer.setSendVol(8, 0, -12);


	}

	//////// public functions:

	free {
		this.freeMIDIFuncs;
		texture.free; lead.free; synthMel.free;
		tpt.free; shiftTpt.free; distTpt.free; scTpt.free;
		tptDelays.free; tptDelaysInput.free;
		prickles.free; pricklesEQ.free; pad.free;
		padEQ.free;
		kick.free;
		midiDict.free;

		this.freeModule;
	}

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