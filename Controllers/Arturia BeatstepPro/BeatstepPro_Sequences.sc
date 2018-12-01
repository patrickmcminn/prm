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

  resetSeq1Sequence { | name | sequencer1Dict[name].reset; }
  stopSeq1Sequence { | name | sequencer1Dict[name].stop; }
  pauseSeq1Sequence { | name | sequencer1Dict[name].pause }
  resumeSeq1Sequence { | name | sequencer1Dict[name].resume; }
  isSeq1SequencePlaying { | name | ^sequencer1Dict[name].isPlaying }
  setSeq1SequenceQuant { | name, quant = 0 | sequencer1Dict[name].setQuant(quant) }

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

  resetSeq2Sequence { | name | sequencer2Dict[name].reset; }
  stopSeq2Sequence { | name | sequencer2Dict[name].stop; }
  pauseSeq2Sequence { | name | sequencer2Dict[name].pause }
  resumeSeq2Sequence { | name | sequencer2Dict[name].resume; }
  isSeq2SequencePlaying { | name | ^sequencer2Dict[name].isPlaying }
  setSeq2SequenceQuant { | name, quant = 0 | sequencer2Dict[name].setQuant(quant) }

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

  resetSeq1Sequence { | name | sequencer1Dict[name].reset; }
  stopSeq1Sequence { | name | sequencer1Dict[name].stop; }
  pauseSeq1Sequence { | name | sequencer1Dict[name].pause }
  resumeSeq1Sequence { | name | sequencer1Dict[name].resume; }
  isSeq1SequencePlaying { | name | ^sequencer1Dict[name].isPlaying }
  setSeq1SequenceQuant { | name, quant = 0 | sequencer1Dict[name].setQuant(quant) }

	addDrumKey { | name, key, action |
		drumSequencerDict[name].addKey(key, action);
	}

	playDrumSequence { | name, clock = 'internal', quant = nil |
		var playClock = sequencerClock;
		if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
		drumSequencerDict[name].play(playClock, quant);
	}

  resetDrumSequence { | name | drumSequencerDict[name].reset; }
  stopDrumSequence { | name | drumSequencerDict[name].stop; }
  pauseDrumSequence { | name | drumSequencerDict[name].pause }
  resumeDrumSequence { | name | drumSequencerDict[name].resume; }
  isDrumSequencePlaying { | name | ^drumSequencerDict[name].isPlaying }
  setDrumSequenceQuant { | name, quant = 0 | drumSequencerDict[name].setQuant(quant) }

	setSequencerClockTempo { | bpm = 60 |
		var bps = bpm/60;
		tempo = bps;
		sequencerClock.tempo = tempo;
	}


}
