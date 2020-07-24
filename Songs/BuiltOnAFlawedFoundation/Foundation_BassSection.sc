/*
Monday, June 19th 2017
Foundation_BassSection.sc
prm

edited 3/3/2020 for Pyramid MIDI
*/

Foundation_BassSection : IM_Module {

	var <isLoaded, server;
	var <eq, <satur, <sub;
	var <sequencer, <channel1, <channel2;
	var saturOnArray, saturOffArray, subOnArray, subOffArray, saturVol, subVol;
	var <arrivalSequenceIsPlaying = false;
	var <mainSequenceIsPlaying= false;



	* new { | outBus = 0, seq = nil, chan1 = 1, chan2 = 2,  relGroup = nil, addAction = 'addToHead' |
		^super.new(2, outBus, relGroup: relGroup, addAction: addAction).prInit(seq, chan1, chan2);
	}

	prInit { | seq, chan1, chan2 |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			satur = SaturSynth.new(eq.inBus, relGroup: group, addAction: \addToHead);
			while({ try { satur.isLoaded } != true }, { 0.001.wait; });

			sub = Subtractive.new(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
			while({ try { sub.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prInitParameters;

			satur.makeSequence(\arrival);
			satur.makeSequence(\main);

			sub.makeSequence(\main);

			server.sync;

			this.prMakePatterns;

			sequencer = seq.uid;
			channel1 = chan1;
			channel2 = chan2;

			this.prMakeMIDIFuncs;

			isLoaded = true;
		}
	}

	prMakeMIDIFuncs {
		saturOnArray = Array.fill(128, { | i |
			MIDIFunc.noteOn({ | vel | satur.playNote(i.midicps, vel.ccdbfs);  }, i, channel1, sequencer); });
		saturOffArray = Array.fill(128, { | i | MIDIFunc.noteOff({ satur.releaseNote(i.midicps) }, i, channel1, sequencer); });
		saturVol = MIDIFunc.cc({ | val | satur.mixer.setVol(val.ccdbfs) }, 7, channel1, sequencer);

		subOnArray = Array.fill(128, { | i |
			MIDIFunc.noteOn({ | vel | sub.playNote(i.midicps, vel.ccdbfs); }, i, channel2, sequencer); });
		subOffArray = Array.fill(128, { | i | MIDIFunc.noteOff({ sub.releaseNote(i.midicps) }, i, channel2, sequencer); });
		subVol = MIDIFunc.cc({ | val | sub.mixer.setVol(val.ccdbfs) }, 7, channel2, sequencer);

	}

	prFreeMIDIFuncs {
		saturOnArray.do({ | i | i.free; });
		saturOffArray.do({ | i | i.free; });
		subOnArray.do({ | i | i.free; });
		subOffArray.do({ | i | i.free; });
		saturVol.free;
		subVol.free;
	}

	prInitParameters {
		sub.mixer.setVol(-3);
		mixer.setPreVol(0, 3);
		mixer.setPreVol(1, 3);
		//// eq:
		//eq.setHighPassCutoff(240);

		//// subtractive:
		sub.setOsc1Waveform('saw');
		sub.setOsc1Octave(3);
		sub.setOsc2Waveform('rect');
		sub.setOsc2Octave(2);
		sub.setFilterCutoff(1000);
		sub.setFilterRes(0.25);
		sub.setAttackTime(0.07);
		sub.setReleaseTime(0.1);
	}

	prMakePatterns {
		var arrivalNoteArray, arrivalDurArray;
		var iter1NoteArray, iter1DurArray;
		var iter2NoteArray, iter2DurArray;
		var iter3NoteArray, iter3DurArray;
		var iter4NoteArray, iter4DurArray;

		arrivalNoteArray = Pseq([5, 17, 5, 9, 4], 1);
		arrivalDurArray = Pseq([8, 8, 8, 8, 16], 1);

		iter1NoteArray = Pseq([2, 5, -7, 0, 2, 9, 5, -7, 0], 1);
		iter1DurArray = Pseq([8, 2, 4, 2, 6, 2, 2, 2, 2], 1);
		iter2NoteArray = Pseq([2, 7, 5, -7, 0, 2, 9, 5, -7, 0], 1);
		iter2DurArray = Pseq([6, 2, 2, 4, 2, 4, 2, 2, 1.5, 1.5], 1);
		iter3NoteArray = Pseq([2, 7, 5, -7, 0, 2, 9, 4, -8, 0], 1);
		iter3DurArray = Pseq([4, 2, 2, 4, 2, 4, 2, 2, 1.5, 1.5], 1);
		iter4NoteArray = Pseq([2, 7, 5, -7, 0, 2, 9, 4, -8, -12], 1);
		iter4DurArray = Pseq([4, 2, 2, 2, 2, 4, 2, 2, 2, 2], 1);

		satur.addKey(\arrival, \legato, 1);
		satur.addKey(\arrival, \dur, arrivalDurArray);
		satur.addKey(\arrival, \note, arrivalNoteArray);

		satur.addKey(\main, \legato, 1);
		satur.addKey(\main, \note, Pseq([iter1NoteArray, iter2NoteArray, iter3NoteArray, iter4NoteArray, 2], 1));
		satur.addKey(\main, \dur, Pseq([iter1DurArray, iter2DurArray, iter3DurArray, iter4DurArray, 24], 1));

		sub.addKey(\main, \legato, 1);
		sub.addKey(\main, \octave, 4);
		sub.addKey(\main, \note, Pseq([iter1NoteArray, iter2NoteArray, iter3NoteArray, iter4NoteArray, 2], 1));
		sub.addKey(\main, \dur, Pseq([iter1DurArray, iter2DurArray, iter3DurArray, iter4DurArray, 24], 1));
	}

	//////// public functions:

	free {
		sub.free;
		satur.free;
		eq.free;
		this.freeModule;
		this.prFreeMIDIFuncs;
		isLoaded = false;
	}

	playArrivalSequence { | clock |
		eq.setHighPassCutoff(240);
		satur.playSequence(\arrival, clock);
		arrivalSequenceIsPlaying = true;
	}

	playMainSequence { | clock |
		eq.setHighPassCutoff(20);
		satur.playSequence(\main, clock);
		sub.playSequence(\main, clock);
		mainSequenceIsPlaying = true;
	}

}