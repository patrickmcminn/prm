/*
Monday, October 3rd 2022
Chesa_Tide.sc
prm
*/

Chesa_Tide : IM_Module {

	var server, <isLoaded;
	var <cloud, <tremolo, <eq;
	var <progression, <chordArray;
	var <previousChord, <currentChord;

	*new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			tremolo = Tremolo.newStereo(eq.inBus, relGroup: group, addAction: \addToHead);
			while({ try { tremolo.isLoaded } != true }, { 0.001.wait; });

			cloud = GrainCloud2.new(tremolo.inBus, relGroup: group, addAction: \addToHead);
			while({ try { cloud.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		previousChord = 0;
		currentChord = 1;
		tremolo.setVolLFODepth(0);
		eq.mixer.setPreVol(-18);
		eq.setPeak2Freq(600);
		eq.setPeak2Gain(-5);
		this.prMakeMarkovChain;
		this.prDefineChords;
		this.prSetInitialCloud;
	}

	prSetInitialCloud {
		cloud.setAttack(0.5);
		cloud.setRelease(1.5);
		cloud.setInstArray([\gaborSine, \gaborSine, \gaborSine, \percRevSine, \gaborSaw]);
		cloud.setTrigRate(0.5, 3);
		cloud.setSustain(1, 3);
		cloud.setCutoff(500, 1500);
	}

	prDefineChords {
		chordArray = Array.newClear(10);
		chordArray[0] = [43, 50, 53, 60];
		chordArray[1] = [39, 46, 53, 60];
		chordArray[2] = [43, 50, 58, 65];
		chordArray[3] = [39, 46, 58, 65];
		chordArray[4] = [55, 60, 62, 65];
		chordArray[5] = [58, 60, 62, 63];
		chordArray[6] = [50, 57, 58, 65];
		chordArray[7] = [51, 57, 58, 62];
		chordArray[8] = [55, 62];
		chordArray[9] = [62];

	}

	prMakeMarkovChain {
		progression = MarkovSetN([
			[[0, 1], [1], [1]],
			[[1, 1], [2, 3, 4, 6], [0.6, 0.15, 0.15, 0.1]],
			[[1, 2], [1, 4, 5, 6], [0.4, 0.4, 0.1, 0.1]],
			[[1, 3], [4, 5, 9, 8], [0.75, 0.2, 0.025, 0.025]],
			[[1, 4], [3, 5, 6], [0.8, 0.1, 0.1]],
			[[1, 6], [1, 4], [0.9, 0.1]],
			[[2, 1], [2, 3], [0.2, 0.8]],
			[[2, 4], [5, 6, 7], [0.5, 0.25, 0.25]],
			[[2, 5], [9], [1]],
			[[2, 6], [6, 10], [0.5, 0.5]],
			[[3, 4], [3, 1], [0.9, 0.1]],
			[[3, 5], [6, 9, 2], [0.8, 0.1, 0.1]],
			[[3, 9], [9, 10, 5], [0.5, 0.25, 0.25]],
			[[3, 8], [7, 3], [0.5, 0.5]],
			[[3, 7], [4], [1]],
			[[3, 6], [9], [1]],
			[[4, 3], [4, 7, 6], [0.3, 0.35, 0.35]],
			[[4, 5], [9], [1]],
			[[4, 7], [5, 2], [0.5, 0.5]],
			[[4, 6], [6, 10], [0.75, 0.25]],
			[[4, 1], [2], [1]],
			[[5, 9], [9], [1]],
			[[5, 6], [6, 10], [0.75, 0.25]],
			[[5, 1], [6], [1]],
			[[5, 10], [6], [1]],
			[[6, 1], [2], [1]],
			[[6, 4], [6, 3], [0.3, 0.7]],
			[[6, 6], [6, 9], [0.5, 0.5]],
			[[6, 10], [10], [1]],
			[[6, 9], [9], [1]],
			[[6, 3], [6, 4], [0.5, 0.5]],
			[[7, 5], [6, 9], [0.5, 0.5]],
			[[7, 2], [4], [1]],
			[[7, 3], [4], [1]],
			[[7, 4], [3], [1]],
			[[8, 7], [3], [1]],
			[[8, 3], [4], [1]],
			[[9, 9], [9, 6, 10], [0.35, 0.3, 0.35]],
			[[9, 10], [10], [1]],
			[[9, 5], [10], [1]],
			[[9, 6], [9, 3], [0.2, 0.8]],
			[[10, 10], [10, 1], [0.2, 0.8]],
			[[10, 1], [2, 3, 4, 6], [0.6, 0.15, 0.15, 0.1]],
			[[10, 6], [9, 10], [0.25, 0.75]]
		], 2);
	}

	//////// public functions:

	free {
		eq.free; tremolo.free; cloud.free;
		this.freeModule;
	}

	playMarkovChord {
		{
			var newChord = progression.next([previousChord, currentChord]);
			newChord = newChord.asInteger;
			newChord.postln;
			this.releaseChord(currentChord);
			//cloud.allNotesOff;
			server.sync;
			previousChord = currentChord;
			currentChord = newChord;
			this.playChord(currentChord);
		}.fork;
	}

	playChord { | chord |
		if( chord != 0,
			{ chordArray[chord-1].do({ | note | cloud.addNote(note); }); });
	}

	releaseChord { | chord |
		if( chord != 0,
			{ chordArray[chord-1].do({ | note | cloud.removeNote(note); }); });
	}

	releaseCurrentChord { releaseChord(currentChord) }

} 