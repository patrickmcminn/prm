/*
Monday, June 19th 2017
Foundation_EndTrumpets.sc
prm
*/

Foundation_EndTrumpets :IM_Module {

	var server, <isLoaded;
	var <trumpet;
	var <endSequenceIsPlaying;
	var <sequencer, <channel1, <channel2, <channel3;
	var tpt1OnArray, tpt1OffArray, tpt2OnArray, tpt2OffArray, tpt3OnArray, tpt3OffArray;

	*new { |outBus, aBuffer, seq, chanIndex = 8, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(aBuffer, seq, chanIndex);
	}

	prInit { | aBuffer, seq, chanIndex |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			trumpet = SamplePlayer.newMono(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
			while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			trumpet.setAttackTime(1);
			trumpet.setReleaseTime(5);
			trumpet.setDecayTime(1);
			trumpet.setSustainLevel(0.5);

			trumpet.makeSequence(\trumpet1);
			trumpet.makeSequence(\trumpet2);
			trumpet.makeSequence(\trumpet3);
			//trumpet.setFilterCutoff(12000);

			trumpet.setBuffer(aBuffer);

			server.sync;

			this.prMakePatterns;

			sequencer = seq.uid;
			channel1 = chanIndex;
			channel2 = chanIndex + 1;
			channel3 = chanIndex + 2;

			this.prMakeMIDIFuncs;

			endSequenceIsPlaying = false;

			isLoaded = true;
		}
	}

	prMakeMIDIFuncs {
		tpt1OnArray = Array.fill(128, { | i | MIDIFunc.noteOn({
			var note = (i -60).midiratio;
			trumpet.playSampleSustaining(("chan1" ++ i).asSymbol, 0, note, 0, 1, rrand(-0.2, 0.2));
		}, i, channel1, sequencer); });
		tpt1OffArray = Array.fill(128, { | i |
			MIDIFunc.noteOff({ trumpet.releaseSampleSustaining(("chan1" ++ i).asSymbol) }, i, channel1, sequencer); });
		tpt2OnArray = Array.fill(128, { | i | MIDIFunc.noteOn({
			var note = (i -60).midiratio;
			trumpet.playSampleSustaining(("chan2" ++ i).asSymbol, 0, note, 0, 1, rrand(-1, -0.2));
		}, i, channel2, sequencer); });
		tpt2OffArray = Array.fill(128, { | i |
			MIDIFunc.noteOff({ trumpet.releaseSampleSustaining(("chan2" ++ i).asSymbol) }, i, channel2, sequencer); });
		tpt3OnArray = Array.fill(128, { | i | MIDIFunc.noteOn({
			var note = (i -60).midiratio;
			trumpet.playSampleSustaining(("chan2" ++ i).asSymbol, 0, note, 0, 1, rrand(0.2, 1));
		}, i, channel3, sequencer); });
		tpt3OffArray = Array.fill(128, { | i |
			MIDIFunc.noteOff({ trumpet.releaseSampleSustaining(("chan2" ++ i).asSymbol) }, i, channel3, sequencer); });
	}

	prFreeMIDIFuncs {
		tpt1OnArray.do({ | i | i.free; });
		tpt1OffArray.do({ | i | i.free; });
		tpt2OnArray.do({ | i | i.free; });
		tpt2OffArray.do({ | i | i.free; });
		tpt3OnArray.do({ | i | i.free; });
		tpt3OffArray.do({ | i | i.free; });
	}

	prMakePatterns {
		this.prMakeTrumpet1Patterns;
		this.prMakeTrumpet2Patterns;
		this.prMakeTrumpet3Patterns;
	}

	prMakeTrumpet1Patterns {
		var squareNoteArray, squareDurArray;
		var driftNoteArray, driftDurArray;
		squareNoteArray = Pseq([
			-7.midiratio, -7.midiratio, Rest(), -10.midiratio, Rest(), -4.midiratio, Rest(), -12.midiratio, -12.midiratio,
			Rest(), -5.midiratio, Rest(), -4.midiratio, Rest(), -9.midiratio, Rest()
		], 1);
		squareDurArray = Pseq([6, 3, 4, 1.5, 5.5, 1.5, 2.5, 6, 3, 4, 2, 7, 1.5, 2.5, 1.5, 4.5]/2, 1);
		driftNoteArray = Pseq([
			-7.midiratio, -7.midiratio, Rest(), -10.midiratio, Rest(), -4.midiratio, Rest(), -12.midiratio, -12.midiratio,
			Rest(), -5.midiratio, Rest(), -4.midiratio, Rest(), -9.midiratio, Rest()
		], inf);
		driftDurArray = Pseq([6, 3, 4, 1.5, 5.5, 1.5, 2.5, 6, 3, 4, 2, 7, 1.5, 2.5, 1.5, 3.5]/2, inf);

		trumpet.addKey(\trumpet1, \legato, 1);
		trumpet.addKey(\trumpet1, \dur, Pseq([squareDurArray, driftDurArray], 1));
		trumpet.addKey(\trumpet1, \rate, Pseq([squareNoteArray, driftNoteArray], 1));
		trumpet.addKey(\trumpet1, \pan, Pwhite(-1, 1));
	}

	prMakeTrumpet2Patterns {
		var squareNoteArray, squareDurArray;
		var driftNoteArray, driftDurArray;

		squareNoteArray = Pseq([
			Rest(), -4.midiratio, Rest(), -12.midiratio, -12.midiratio, Rest(), -5.midiratio, Rest(),
			-9.midiratio, Rest(), -7.midiratio, 0.midiratio, Rest(), -5.midiratio, Rest(), -10.midiratio, Rest()
		], 1);
		squareDurArray = Pseq([4, 1.5, 2.5, 6, 3, 4, 1.5, 5.5, 1.5, 2.5, 6, 6, 3, 1.5, 2.5, 1.5, 3.5]/2, 1);
		driftNoteArray = Pseq([
			Rest(), -4.midiratio, Rest(), -12.midiratio, -12.midiratio, Rest(), -5.midiratio, Rest(),
			-9.midiratio, Rest(), -7.midiratio, 0.midiratio, Rest(), -5.midiratio, Rest(), -10.midiratio, Rest()
		], inf);
		driftDurArray = Pseq([4, 1.5, 2.5, 6, 3, 4, 1.5, 5.5, 1.5, 2.5, 6, 6, 3, 1.5, 2.5, 1.5, 4]/2, inf);

		trumpet.addKey(\trumpet2, \legato, 1);
		trumpet.addKey(\trumpet2, \dur, Pseq([squareDurArray, driftDurArray], 1));
		trumpet.addKey(\trumpet2, \rate, Pseq([squareNoteArray, driftNoteArray], 1));
		trumpet.addKey(\trumpet2, \pan, Pwhite(-1, 1));
	}

	prMakeTrumpet3Patterns {
		var squareNoteArray, squareDurArray;
		var driftNoteArray, driftDurArray;

		squareNoteArray = Pseq([
			Rest(), -5.midiratio, Rest(), -9.midiratio, Rest(), -7.midiratio, -7.midiratio, Rest(),
			-10.midiratio, Rest(), -4.midiratio, Rest(), -12.midiratio, -7.midiratio, -12.midiratio
		], 1);
		squareDurArray = Pseq([5, 1.5, 5.5, 1.5, 2.5, 6, 3, 4, 1.5, 5.5, 1.5, 4.5, 6, 4, 4]/2, 1);
		driftNoteArray = Pseq([
			Rest(), -5.midiratio, Rest(), -9.midiratio, Rest(), -7.midiratio, -7.midiratio, Rest(),
			-10.midiratio, Rest(), -4.midiratio, Rest(), -12.midiratio, -7.midiratio, -12.midiratio
		], inf);
		driftDurArray = Pseq([5, 1.5, 5.5, 1.5, 2.5, 6, 3, 4, 1.5, 5.5, 1.5, 4.5, 6, 4, 2]/2, inf);

		trumpet.addKey(\trumpet3, \legato, 1);
		trumpet.addKey(\trumpet3, \dur, Pseq([squareDurArray, driftDurArray], 1));
		trumpet.addKey(\trumpet3, \rate, Pseq([squareNoteArray, driftNoteArray], 1));
		trumpet.addKey(\trumpet3, \pan, Pwhite(-1, 1));
	}

	//////// public functions:
	free {
		trumpet.free;
		this.prFreeMIDIFuncs;
		this.freeModule;
		isLoaded = false;
	}

	playEndSequence { | clock |
		trumpet.playSequence(\trumpet1, clock);
		trumpet.playSequence(\trumpet2, clock);
		trumpet.playSequence(\trumpet3, clock);
		endSequenceIsPlaying = true;
	}

	stopEndSequence {
		trumpet.stopSequence(\trumpet1);
		trumpet.stopSequence(\trumpet2);
		trumpet.stopSequence(\trumpet3);
		endSequenceIsPlaying = false;
	}

}