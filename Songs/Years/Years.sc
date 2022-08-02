/*
Tuesday, April 5th 2022
Years.sc
prm
*/

Years : IM_Module {

	var <isLoaded, server;
	var <drone, <glock, <ensemble, <bass;
	var <drums, <freezeTpts, <tpt, <endTpts;
	var <freezeTptsInput, <endTptsInput;
	var <blockChords;
	var <modIn, <noiseIn;

	var droneCutoffRout, droneTremRout, <glockNoteRout1;
	var <droneCutoffRoutIsPlaying, <droneTremRoutIsPlaying, <glockNoteRout1IsPlaying;

	var <sequencer;

	var <midiDict, <midiEnabled;

	var <click;

	*new {
		|
		outBus, micIn, modularInArray, digiArray, seq,
		send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead'
		|
		^super.new(11, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(micIn,
			modularInArray, digiArray, seq);
	}

	prInit { | micIn, modularInArray, digiArray, seq |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			sequencer = seq.uid;
			midiDict = IdentityDictionary.new;
			midiEnabled = false;
			click = false;

			server.sync;
			9.do({ | chan | mixer.mute(chan); });
			this.prSetMixerParameters;

			server.sync;

			drone = Years_Drone.new(mixer.chanStereo(0), group, \addToHead);
			while({ try { drone.isLoaded } != true }, { 0.001.wait; });

			glock = Years_Glock.new(mixer.chanStereo(1), group, \addToHead);
			while({ try { glock.isLoaded } != true }, { 0.001.wait; });

			ensemble = Years_Ensemble.new(modularInArray[0], digiArray, mixer.chanStereo(2), group, \addToHead);
			while({ try { ensemble.isLoaded } != true }, { 0.001.wait; });

			bass = SaturSynth.new(mixer.chanStereo(3), relGroup: group, addAction: \addToHead);
			while({ try { bass.isLoaded } != true }, { 0.001.wait; });

			drums = Years_Drums.new(mixer.chanStereo(4), group, \addToHead);
			while({ try { drums.isLoaded } != true }, { 0.001.wait; });


			freezeTpts = Years_FreezeTpts.new(mixer.chanStereo(5), group, \addToHead);
			while({ try { freezeTpts.isLoaded } != true }, { 0.001.wait; });
			freezeTptsInput = IM_HardwareIn.new(micIn, freezeTpts.inBus, group, \addToHead);
			while({ try { freezeTptsInput.isLoaded } != true }, { 0.001.wait; });

			tpt = IM_HardwareIn.new(micIn, mixer.chanMono(6), group, \addToHead);
			while({ try { tpt.isLoaded } != true }, { 0.001.wait; });

			endTpts = Years_EndHorns.new(mixer.chanStereo(7), group, \addToHead);
			while({ try { endTpts.isLoaded } != true }, { 0.001.wait; });
			endTptsInput = IM_HardwareIn.new(micIn, endTpts.inBus, group, \addToHead);
			while({ try { endTptsInput.isLoaded } != true }, { 0.001.wait; });

			blockChords = Years_BlockChords.new(mixer.chanStereo(8), group, \addToHead);
			while({ try { blockChords.isLoaded } != true }, { 0.001.wait; });

			modIn = IM_HardwareIn.new(modularInArray[1], mixer.chanMono(9), group, \addToHead);
			while({ try { modIn.isLoaded } != true }, { 0.001.wait; });

			noiseIn = IM_HardwareIn.new(modularInArray[3], mixer.chanMono(10), group, \addToHead);
			while({ try { noiseIn.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;


			9.do({ | chan | mixer.unMute(chan); });

			isLoaded = true;
		}
	}

	prSetMixerParameters {
		11.do({ | chan | mixer.setPreVol(chan, -9) });
		mixer.setPreVol(6, -3);

		// 1 - drone:
		mixer.setVol(0, -6);
		mixer.setSendVol(0, 0, -18);
		mixer.setSendVol(0, 3, -24);
		// 2 - glock:
		mixer.setVol(1, -9);
		mixer.setSendVol(1, 0, -24);
		// 3 - ensemble:
		mixer.setVol(2, -3);
		mixer.setSendVol(2, 0, -15);
		mixer.setSendVol(2, 3, -18);
		// 4 - bass:
		mixer.setVol(3, -1.5);
		mixer.setSendVol(3, 0, -18);
		// 5 - drums:
		mixer.setVol(4, -9);
		mixer.setSendVol(4, 0, -15);
		mixer.setSendVol(3, 0, -24);
		// 6 - freeze trumpets:
		mixer.setVol(5, -15);
		mixer.setSendVol(5, 0, -3);
		mixer.setSendVol(5, 3, -12);
		// 7 - trumpet:
		mixer.setVol(6, -5);
		mixer.setSendVol(6, 0, -3);
		mixer.setSendVol(6, 3, -12);
		// 8 - end Trumpets:
		mixer.setVol(7, -6);
		mixer.setSendVol(7, 0, -6);
		mixer.setSendVol(7, 3, -9);
		// 9 - block chords:
		mixer.setVol(8, -6);
		mixer.setSendVol(8, 0, -9);
		// 10 - modIn:
		mixer.mute(9);
		mixer.setVol(9, -70);
		mixer.setSendVol(9, 0, -24);
		//mixer.setSendVol(9, 3, -);

		// 11 - noiseIn:
		mixer.mute(10);
		mixer.setVol(10, -70);
		mixer.setSendVol(10, 0, -12);
		mixer.setSendVol(10, 3, -9);

	}

	prSetInitialParameters {
		this.prMakeRoutines;
		//freezeTptsInput.mute;
		tpt.mute;
		endTptsInput.mute;

		bass.setAttackTime(0.25);
		bass.setReleaseTime(1);
	}


	prMakeRoutines {
		droneCutoffRoutIsPlaying = false;
		droneTremRoutIsPlaying = false;
		glockNoteRout1IsPlaying = false;
		droneTremRout = r {
			loop {
				var freq = exprand(6, 12).lag(2);
				drone.tremolo.setVolLFOFreq(freq);
				rrand(2, 5).wait;
			};
		};

		droneCutoffRout = r {
			loop {
				var cutoff = exprand(400, 1500).lag(2);
				drone.eq.setLowPassCutoff(cutoff);
				rrand(2, 5).wait;
			}
		};
		glockNoteRout1 = r {
			loop {
				var note = [0, 2, 4, 5, 7, 9].choose;
				note = note + 73;
				glock.glock.playNote(note.midicps);
				rrand(1, 5).wait;
			}
		};
	}

	//////// public functions:

	free {
		this.freeMIDIFuncs;
		this.stopDroneCutoffRout; this.stopDroneTremRout; this.stopGlockNoteRout1;
		drone.free; glock.free; ensemble.free; bass.free;
		drums.free; freezeTpts.free; tpt.free; endTpts.free;
		freezeTptsInput.free; endTptsInput.free; blockChords.free;
		modIn.free; noiseIn.free;

		this.freeModule;
	}

	toggleMIDIFuncs { if(midiEnabled == false, { this.makeMIDIFuncs }, { this.freeMIDIFuncs; }); }

	makeMIDIFuncs {

		// ensemble:
		midiDict[\ensSubOn] = Array.fill(128, { | note |
			MIDIFunc.noteOn({ | vel | ensemble.subtractive.playNote(note.midicps, vel.ccdbfs) },
				note, 0, sequencer);
		});
		midiDict[\ensSubOff] = Array.fill(128, { | note |
			MIDIFunc.noteOff({ ensemble.subtractive.releaseNote(note.midicps) }, note, 0, sequencer);
		});
		midiDict[\bassOn] = Array.fill(128, { | note |
			MIDIFunc.noteOn({ | vel | bass.playNote(note.midicps, vel.ccdbfs); }, note, 1, sequencer); });
		midiDict[\bassOff] = Array.fill(128, { | note |
			MIDIFunc.noteOff({ bass.releaseNote(note.midicps); }, note, 1, sequencer); });

		midiDict[\drums] = Array.fill(16, { | note |
			MIDIFunc.noteOn({ | vel | drums.drums.playSample(note, vel.ccdbfs); }, note+60, 2, sequencer); });

		midiDict[\blockChordsOn] = Array.fill(128, { | note |
			MIDIFunc.noteOn({ | vel | blockChords.subtractive.playNote(note.midicps, vel.ccdbfs) }, note, 3, sequencer); });
		midiDict[\blockChordsOff] = Array.fill(128, { | note |
			MIDIFunc.noteOff({ blockChords.subtractive.releaseNote(note.midicps); }, note, 3, sequencer); });

		midiDict[\clickOn] = MIDIFunc.noteOn({ click = true }, 60, 9, sequencer);
		midiDict[\clickOff] = MIDIFunc.noteOff({ click = false }, 60, 9, sequencer);


		midiEnabled = true;
	}

	freeMIDIFuncs {
		midiDict.do({ | func | func.free; });
		midiEnabled = false;
	}

	toggleDroneCutoffRout { if(droneCutoffRoutIsPlaying == false,
		{ this.playDroneCutoffRout }, { this.stopDroneCutoffRout });
	}
	playDroneCutoffRout { droneCutoffRout.play; droneCutoffRoutIsPlaying = true; }
	stopDroneCutoffRout { droneCutoffRout.stop.reset; droneCutoffRoutIsPlaying = false; }

	toggleDroneTremRout { if(droneTremRoutIsPlaying == false,
		{ this.playDroneTremRout }, { this.stopDroneTremRout });
	}
	playDroneTremRout { droneTremRout.play; droneTremRoutIsPlaying = true; }
	stopDroneTremRout { droneTremRout.stop.reset; droneTremRoutIsPlaying = false; }


	toggleGlockNoteRout1 { if(glockNoteRout1IsPlaying == false,
		{ this.playGlockNoteRout1 }, { this.stopGlockNoteRout1 });
	}
	playGlockNoteRout1 { glockNoteRout1.play; glockNoteRout1IsPlaying = true; }
	stopGlockNoteRout1 { glockNoteRout1.stop; glockNoteRout1IsPlaying = false; }
	resetGlockNoteRout1 { glockNoteRout1.reset; }



} 