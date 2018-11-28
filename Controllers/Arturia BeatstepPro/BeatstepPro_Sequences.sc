/*
Tuesday, 11/27/2018
BeatstepPro Sequences.sc
prm
*/


+BeatstepPro {

	makeSeq1Sequence { |name |
		{
			sequencer1Dict[name] = IM_PatternSeq.new;
			sequencer1Dict[name].stop;
			server.sync;
			sequencer1Dict[name].addKey(\type, \midi);
			sequencer1Dict[name].addKey(\midicmd, \noteOn);
			sequencer1Dict[name].addKey(\midiout, midiOutPort);
			sequencer1Dict[name].addKey(\chan, sequencer1Channel);
		}.fork;
	}

	addSeq1Key { | name, key, action |
		sequencer1Dict[name].addKey(key, action);
	}

	playSeq1Sequence { | name, clock = 'internal', quant = nil |
		var playClock = sequencerClock;
		if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
		sequencer1Dict[name].play(playClock, quant);
	}

	makeSeq2Sequence { |name |
		{
			sequencer2Dict[name] = IM_PatternSeq.new;
			sequencer2Dict[name].stop;
			server.sync;
			sequencer2Dict[name].addKey(\type, \midi);
			sequencer2Dict[name].addKey(\midicmd, \noteOn);
			sequencer2Dict[name].addKey(\midiout, midiOutPort);
			sequencer2Dict[name].addKey(\chan, sequencer2Channel);
		}.fork;
	}

	addSeq2Key { | name, key, action |
		sequencer2Dict[name].addKey(key, action);
	}

	playSeq2Sequence { | name, clock = 'internal', quant = nil |
		var playClock = sequencerClock;
		if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
		sequencer2Dict[name].play(playClock, quant);
	}

	makeDrumSequence { |name |
		{
			drumSequencerDict[name] = IM_PatternSeq.new;
			drumSequencerDict[name].stop;
			server.sync;
			drumSequencerDict[name].addKey(\type, \midi);
			drumSequencerDict[name].addKey(\midicmd, \noteOn);
			drumSequencerDict[name].addKey(\midiout, midiOutPort);
			drumSequencerDict[name].addKey(\chan, drumChannel);
		}.fork;
	}

	addDrumKey { | name, key, action |
		drumSequencerDict[name].addKey(key, action);
	}

	playDrumSequence { | name, clock = 'internal', quant = nil |
		var playClock = sequencerClock;
		if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
		drumSequencerDict[name].play(playClock, quant);
	}

	setSequencerClockTempo { | bpm = 60 |
		var bps = bpm/60;
		tempo = bps;
		sequencerClock.tempo = tempo;
	}


}
