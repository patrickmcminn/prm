/*
Tuesday, July 7th 2015
Connections.sc
prm

update 5/27/2019

originally written Spring of 2009
*/

Connections : IM_Module {

	var <isLoaded, server;
	var <noteRecord, <inlet;

	var <airSputters, <droner, <bass;
	var <trumpetGran, <chords;

	var <airSputtersInput, <dronerInput;
	var noteRecordInput, <trumpetGranInput;
	var <mic, <micInput;

	var <modularClock;

	var <moog,<modular, <sampler;

	var micIn, pickupIn, moogIn, moogDevice, moogPort;
	//var impulseResponse;

	var <midiEnabled = false;
	var midiFuncs, midiDict;

	var <clock;

	*new {
		|
		outBus = 0, micInBus, pickupInBus, moogInBus, modInBus, samplerInBus,
		send0Bus, send1Bus, send2Bus, send3Bus,
		relGroup, addAction = 'addToHead'
		|
		^super.new(11, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
			relGroup, addAction).prInit(micInBus, pickupInBus, moogInBus, modInBus, samplerInBus);
	}

	prInit {
		|
		micInBus, pickupInBus, moogInBus, modInBus, samplerInBus
		|

		server = Server.default;
		micIn = micInBus; pickupIn = pickupInBus; moogIn = moogInBus;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
			mixer.masterChan.mute;

			clock = TempoClock.new;
			server.sync;
			clock.tempo = 75/60;
			//impulseResponse = ir;

			midiDict = IdentityDictionary.new;

			/*
			modularClock = ModularClock.new(clockOutBus, 75, 24, group, \addToHead);
			while({ try { modularClock.isLoaded } != true }, { 0.001.wait; });
			*/

			noteRecord = Connections_NoteRecord.new(group, \addToHead);
			while({ try { noteRecord.isLoaded } != true }, { 0.001.wait; });
			noteRecordInput = IM_HardwareIn.new(pickupInBus, noteRecord.inBus, group, \addToHead);
			while({ try { noteRecordInput.isLoaded } != true }, { 0.001.wait; });

			airSputters = Connections_AirSputters.new(mixer.chanStereo(0), clock, group, \addToHead);
			while({ try { airSputters.isLoaded } != true }, { 0.001.wait; });
			airSputtersInput = IM_HardwareIn.new(pickupIn, airSputters.inBus, group, \addToHead);
			while({ try { airSputtersInput.isLoaded } != true }, { 0.001.wait; });

			droner = Droner.newMono(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
			//droner = Droner.newMono(mixer.chanStereo(1), impulseResponse,  relGroup: group, addAction: \addToHead);
			while({ try { droner.isLoaded } != true }, { 0.001.wait; });
			dronerInput = IM_HardwareIn.new(pickupIn, droner.inBus, group, \addToHead);
			while({ try { dronerInput.isLoaded } != true }, { 0.001.wait; });

			bass = Connections_Bassline.new(mixer.chanStereo(2), noteRecord.noteBufferArray, group, \addToHead);
			while({ try { bass.isLoaded } != true }, { 0.001.wait; });

			trumpetGran = Connections_TrumpetGran.new(mixer.chanStereo(3), group, \addToHead);
			while({ try { trumpetGran.isLoaded } != true }, { 0.001.wait; });
			trumpetGranInput = IM_HardwareIn.new(pickupIn, trumpetGran.inBus, group, \addToHead);
			while({ try { trumpetGranInput.isLoaded } != true }, { 0.001.wait; });

			inlet = Connections_Inlet.new(mixer.chanStereo(4), noteRecord.noteBufferArray,
				noteRecord.cascadeBufferArray,  group, \addToHead);
			while({ try { inlet.isLoaded } != true }, { 0.001.wait; });

			chords = Connections_Chords.new(mixer.chanStereo(5), noteRecord.chordBufferArray,
				group, \addToHead);
			while( { try { chords.isLoaded } != true }, { 0.00.wait; });

			mic = Connections_Mic.new(mixer.chanStereo(7), group, \addToHead);
			while({ try { mic.isLoaded } != true }, { 0.001.wait; });
			micInput = IM_HardwareIn.new(micIn, mic.inBus, group, \addToHead);
			while({ try { micInput.isLoaded } != true }, { 0.001.wait; });

			moog = IM_HardwareIn.new(moogInBus, mixer.chanMono(8), group, \addToHead);
			while({ try { moog.isLoaded } != true }, { 0.001.wait; });

			modular = IM_HardwareIn.new(modInBus, mixer.chanMono(9), group, \addToHead);
			while({ try { modular.isLoaded } != true }, { 0.001.wait; });

			sampler = IM_HardwareIn.new(samplerInBus, mixer.chanMono(10), group, \addToHead);
			while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;
			mixer.masterChan.unMute;

			server.sync;
			isLoaded = true;
		}
	}

	prSetInitialParameters {
		// mixer parameters:
		mixer.setMasterVol(-9);

		11.do({ | chan | mixer.setPreVol(chan, -6); });

		// air sputters:
		airSputtersInput.mute;
		mixer.setPreVol(0, -9);
		mixer.setSendVol(0, 0, -36);

		// droner:
		dronerInput.mute;
		mixer.setSendVol(1, 0, -18);
		mixer.setVol(1, -9);

		// bass:
		mixer.setSendVol(2, 0, -10);
		mixer.setVol(2, -9);

		// trumpet gran:
		trumpetGranInput.mute;
		mixer.setSendVol(3, 0, -3);
		mixer.setVol(3, -12);
		mixer.setPreVol(3, -7.5);

		// inlet:
		//mixer.setPreVol(4, -3);
		//mixer.setSendVol(4, 0, -18);

		// chords:
		mixer.setSendVol(5, 0, -9);
		mixer.setPreVol(5, -6);

		// mic:
		mixer.setPreVol(7, -9);
		mixer.setSendVol(7, 0, -6);
		mixer.setVol(7, -6);
		micInput.mute;

		// moog:
		mixer.setPreVol(8, 0);
		mixer.setVol(8, -140);
		mixer.setSendVol(8, 0, -10);
		mixer.mute(8);

		// modular:
		mixer.setPreVol(9, 0);
		mixer.setVol(9, -140);
		mixer.setSendVol(9, 0, -12);
		mixer.mute(9);

		// sampler:
		mixer.setPreVol(10, 0);
		mixer.setVol(10, -140);
		mixer.setSendVol(10, 0, -45);
		mixer.mute(10);



	}

	/////// public functions:

	free {
		noteRecord.free;
		try { inlet.free; };
		try { airSputters.free; };
		try { droner.free; };
		try { bass.free; };
		try { trumpetGran.free; };
		try { chords.free; };
		try { modularClock.free;};
		try { mic.free; micInput.free; };
		//this.freeSong;
		try { this.freeMIDIFuncs; };
		this.freeModule;
	}

	toggleMIDIFuncs { if(midiEnabled == false, { this.makeMIDIFuncs }, { this.freeMIDIFuncs }); }

	makeMIDIFuncs {
		//// bass:

		midiDict[\bass1On] = MIDIFunc.noteOn({ bass.sampler.playSampleSustaining(\bass1, 0, 0, 0.25, 0.1, 1, 0) },
			60, 8);
		midiDict[\bass1Off] = MIDIFunc.noteOff({ bass.sampler.releaseSampleSustaining(\bass1); },
			60, 8);
		midiDict[\bass2On] = MIDIFunc.noteOn({ bass.sampler.playSampleSustaining(\bass2, 4, 0, 0.25, 0.1, 1, 0) },
			61, 8);
		midiDict[\bass2Off] = MIDIFunc.noteOff({ bass.sampler.releaseSampleSustaining(\bass2); },
			61, 8);
		midiDict[\bass3On] = MIDIFunc.noteOn({ bass.sampler.playSampleSustaining(\bass3, 2, 0, 0.25, 0.1, 1, 0) },
			62, 8);
		midiDict[\bass3Off] = MIDIFunc.noteOff({ bass.sampler.releaseSampleSustaining(\bass3); },
			62, 8);
		midiDict[\bass4On] = MIDIFunc.noteOn({ bass.sampler.playSampleSustaining(\bass4, 3, 0, 0.25, 0.1, 1, 0) },
			63, 8);
		midiDict[\bass4Off] = MIDIFunc.noteOff({ bass.sampler.releaseSampleSustaining(\bass4); },
			63, 8);
		midiDict[\bass5On] = MIDIFunc.noteOn({ bass.sampler.playSampleSustaining(\bass5, 1, 0, 0.25, 0.1, 1, 0) },
			64, 8);
		midiDict[\bass5Off] = MIDIFunc.noteOff({ bass.sampler.releaseSampleSustaining(\bass5); },
			64, 8);

		midiDict[\highBass1On] = MIDIFunc.noteOn({ bass.sampler.playSampleSustaining(\highBass1, 2, -3, 0.5, 0.1, 1, 0); },
			72, 8);
		midiDict[\highBass1Off] = MIDIFunc.noteOff({ bass.sampler.releaseSampleSustaining(\highBass1); },
			72, 8);
		midiDict[\highBass2On] = MIDIFunc.noteOn({ bass.sampler.playSampleSustaining(\highBass2, 3, -3, 0.5, 0.1, 1, 0); },
			73, 8);
		midiDict[\highBass2Off] = MIDIFunc.noteOff({ bass.sampler.releaseSampleSustaining(\highBass2); },
			73, 8);
		midiDict[\highBass3On] = MIDIFunc.noteOn({ bass.sampler.playSampleSustaining(\highBass3, 0, -3, 0.5, 0.1, 1, 0); },
			74, 8);
		midiDict[\highBass3Off] = MIDIFunc.noteOff({ bass.sampler.releaseSampleSustaining(\highBass3); },
			74, 8);
		midiDict[\highBass4On] = MIDIFunc.noteOn({ bass.sampler.playSampleSustaining(\highBass4, 1, -3, 0.5, 0.1, 1, 0); },
			75, 8);
		midiDict[\highBass4Off] = MIDIFunc.noteOff({ bass.sampler.releaseSampleSustaining(\highBass4); },
			75, 8);
		midiDict[\highBass5On] = MIDIFunc.noteOn({ bass.sampler.playSampleSustaining(\highBass5, 4, -3, 0.5, 0.1, 1, 0); },
			76, 8);
		midiDict[\highBass5Off] = MIDIFunc.noteOff({ bass.sampler.releaseSampleSustaining(\highBass5); },
			76, 8);


    midiDict[\basslineFade] = MIDIFunc.noteOn({ mixer.setVol(2, -140, 30); },
      60, 7);
    midiDict[\chordsFade] = MIDIFunc.noteOn({ mixer.setVol(5, -140, 30); },
      62, 7);


		midiEnabled = true;


	}

	freeMIDIFuncs { midiDict.do({ | func | func.free; }); midiEnabled = false; }

}