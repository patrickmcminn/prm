/*
Friday, June 16th 2017
Foundation_TrumpetSection.sc
prm
*/

Foundation_TrumpetSection : IM_Module {

	var <isLoaded, server;
	var <trumpet1, <trumpet2, <trumpet3;
	var buffer1, buffer2, buffer3;
	var <arrivalSequenceIsPlaying;
	var <mainSequenceIsPlaying;
	var <sequencer, <channel1, <channel2, <channel3;
	var trumpet1OnArray, trumpet1OffArray, trumpet2OnArray, trumpet2OffArray, trumpet3OnArray, trumpet3OffArray;

	*new { | outBus = 0, lowDBuffer, aBuffer, highDBuffer, seq, chanIndex, relGroup, addAction = 'addToHead' |
		^super.new(3, outBus, relGroup: relGroup, addAction: addAction).prInit(lowDBuffer, aBuffer, highDBuffer, seq, chanIndex);
	}

	prInit { | lowDBuffer, aBuffer, highDBuffer, seq, chanIndex |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			buffer1 = highDBuffer;
			buffer2 = aBuffer;
			buffer3 = lowDBuffer;

			trumpet1 = SamplePlayer.newMono(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
			while({ try { trumpet1.isLoaded } != true }, { 0.001.wait; });

			trumpet2 = SamplePlayer.newMono(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
			while({ try { trumpet2.isLoaded } != true }, { 0.001.wait; });

			trumpet3 = SamplePlayer.newMono(mixer.chanStereo(2), relGroup: group, addAction: \addToHead);
			while({ try { trumpet3.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			trumpet1.setAttackTime(0.1);
			trumpet1.setReleaseTime(0.15);
			trumpet1.setBuffer(buffer1);

			trumpet2.setAttackTime(0.1);
			trumpet2.setReleaseTime(0.15);
			trumpet2.setBuffer(buffer2);

			trumpet3.setAttackTime(0.1);
			trumpet3.setReleaseTime(0.15);
			trumpet3.setBuffer(buffer3);

			mixer.setPanBal(1, -0.5);
			mixer.setPanBal(1, 0.5);

			sequencer = seq.uid;
			channel1 = chanIndex;
			channel2 = chanIndex + 1;
			channel3 = chanIndex + 2;

			this.prMakeMIDIFuncs;

			this.prCreateSequences;
			server.sync;

			this.prMakeArrivalSequences;
			this.prMakeMainSequences;
			server.sync;

			arrivalSequenceIsPlaying = false;
			mainSequenceIsPlaying = false;

			isLoaded = true;
		}
	}

	prMakeMIDIFuncs {
		trumpet1OnArray = Array.fill(128, { | i | MIDIFunc.noteOn({
			var note = (i-60).midiratio;
			trumpet1.playSampleSustaining(i.asSymbol, 0, note) }, i, channel1, sequencer); });
		trumpet1OffArray = Array.fill(128, { | i | MIDIFunc.noteOff({
			trumpet1.releaseSampleSustaining(i.asSymbol); }, i, channel1, sequencer); });
		trumpet2OnArray = Array.fill(128, { | i | MIDIFunc.noteOn({
			var note = (i-60).midiratio;
			trumpet2.playSampleSustaining(i.asSymbol, 0, note) }, i, channel2, sequencer); });
		trumpet2OffArray = Array.fill(128, { | i | MIDIFunc.noteOff({
			trumpet2.releaseSampleSustaining(i.asSymbol); }, i, channel2, sequencer); });
		trumpet3OnArray = Array.fill(128, { | i | MIDIFunc.noteOn({
			var note = (i-60).midiratio;
			trumpet3.playSampleSustaining(i.asSymbol, 0, note) }, i, channel3, sequencer); });
		trumpet3OffArray = Array.fill(128, { | i | MIDIFunc.noteOff({
			trumpet3.releaseSampleSustaining(i.asSymbol); }, i, channel3, sequencer); });
		}

	prFreeMIDIFuncs {
		trumpet1OnArray.do({ | i | i.free; });
		trumpet1OffArray.do({ | i | i.free; });
		trumpet2OnArray.do({ | i | i.free; });
		trumpet2OffArray.do({ | i | i.free; });
		trumpet3OnArray.do({ | i | i.free; });
		trumpet3OffArray.do({ | i | i.free; });
	}

	prCreateSequences {
		////// arrival:
		trumpet1.makeSequence(\arrival, 'sustaining');
		trumpet2.makeSequence(\arrival, 'sustaining');
		trumpet3.makeSequence(\arrival, 'sustaining');

		////// main:
		trumpet1.makeSequence(\main, 'sustaining');
		trumpet2.makeSequence(\main, 'sustaining');
		trumpet3.makeSequence(\main, 'sustaining');

		trumpet1.makeSequence(\test);
	}

	prMakeArrivalSequences {
		this.prMakeTrumpet1ArrivalSequence;
		this.prMakeTrumpet2ArrivalSequence;
		this.prMakeTrumpet3ArrivalSequence;
	}

	prMakeMainSequences {
		this.prMakeTrumpet1MainSequence;
		this.prMakeTrumpet2MainSequence;
		this.prMakeTrumpet3MainSequence;
	}

	prMakeTrumpet1ArrivalSequence {
		var noteLoop = [-2, -3, -10, -2, -3, -12];
		trumpet1.addKey(\arrival, \legato, 1);
		trumpet1.addKey(\arrival, \rate, Pseq([
			Rest(),
			Pseq(noteLoop.midiratio, 4),
			Pseq([-2, -3, -10].midiratio, 1)
		], 1));
		trumpet1.addKey(\arrival, \dur, Pseq([
			4,
			2, 2, 2, 2, 2, 5,
			2, 2, 2, 2, 2, 3,
			1, 1, 1, 1, 1, 2,
			1, 1, 1, 1, 1, 1, 1, 1, 1
		], 1));
	}
	prMakeTrumpet1MainSequence {
		var pattern1, pattern2, pattern3, pattern4;
		pattern1 = Pseq([-2, -3, -10, -2, -3, -12].midiratio, 30);
		pattern2 = Pseq([-2, -3, -5, -2, -3, -5].midiratio, 18);
		pattern3 = Pseq([-2, -3, -2, 0].midiratio, 10);
		pattern4 = Pseq([0, -2, 0, 2, 3, 2].midiratio, 16);
		trumpet1.addKey(\main, \legato, 1);
		trumpet1.addKey(\main, \dur, 0.25);
		trumpet1.addKey(\main, \rate, Pseq([pattern1, pattern2, pattern3, pattern4], 1));
		//trumpet1.addKey(\main, \rate, Pseq([0, -2, 0, 3, 2].midiratio, inf));
	}


	prMakeTrumpet2ArrivalSequence {
		var noteLoop = [0, -4, -9, -7];
		trumpet2.addKey(\arrival, \legato, 1);
		trumpet2.addKey(\arrival, \rate, Pseq([
			Rest(), Rest(),
			Pseq(noteLoop.midiratio, 5),
			0.midiratio
		], 1));
		trumpet2.addKey(\arrival, \dur, Pseq([4, Pseq([2], 22)], 1));
	}

	prMakeTrumpet2MainSequence {
		var pattern1, pattern2, pattern3, pattern4;
		pattern1 = Pseq([-7, -5, -4, -5, -7, -9].midiratio, 24);
		pattern2 = Pseq([-7, -5, -4, -5, -7, -9, -10, -9].midiratio, 13);
		pattern3 = Pseq([-10, -9, -7, -5, -4, -5, -7, -9, -10, -12].midiratio, 8);
		pattern4 = Pseq([0, 2, 3, 2, 0, -2].midiratio, 16);
		trumpet2.addKey(\main, \legato, 1);
		trumpet2.addKey(\main, \dur, 0.25);
		trumpet2.addKey(\main, \rate, Pseq([pattern1, pattern2, pattern3, pattern4], 1));
	}


	prMakeTrumpet3ArrivalSequence {
		var note = [0, 5, 3, 2, 0, 5, 3, 2, -2, 0, 5, 3, 2, 0, 5, 3];
		var dur = [4, 4, 2, 2, 4, 4, 2, 2, 2, 2, 4, 2, 2, 2, 4, 2, 2, 2];
		trumpet3.addKey(\arrival, \legato, 1);
		trumpet3.addKey(\arrival, \rate, Pseq([Rest(), Rest(), Pseq(note.midiratio, 1)], 1));
		trumpet3.addKey(\arrival, \dur, Pseq(dur, 1));
	}

	prMakeTrumpet3MainSequence {
		var pattern1, pattern2, pattern3, pattern4;
		pattern1 = Pseq([7, 5, 7, 10, 9, 10].midiratio, 20);
		pattern2 = Pseq([7, 5, 7, 10, 9, 10, 12].midiratio, 16);
		pattern3 = Pseq([7, 5, 7, 10, 9, 10, 12, 5].midiratio, 12);
		pattern4 = Pseq([3, 5, 3, 5, 3, 2].midiratio, 16);
		trumpet3.addKey(\main, \legato, 1);
		trumpet3.addKey(\main, \dur, 0.25);
		trumpet3.addKey(\main, \rate, Pseq([pattern1, pattern2, pattern3, pattern4], 1));
	}

	//////// public functions:

	free {
		trumpet1.free;
		trumpet2.free;
		trumpet3.free;
		this.prFreeMIDIFuncs;
		this.freeModule;
		isLoaded = false;
	}

	playArrivalSequence { | clock |
		trumpet1.playSequence(\arrival, clock);
		trumpet2.playSequence(\arrival, clock);
		trumpet3.playSequence(\arrival, clock);
		arrivalSequenceIsPlaying = true;
	}

	playMainSequence { | clock |
		trumpet1.playSequence(\main, clock);
		trumpet2.playSequence(\main, clock);
		trumpet3.playSequence(\main, clock);
		mainSequenceIsPlaying = true;
	}

}