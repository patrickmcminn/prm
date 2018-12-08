/*
Saturday, December 8th 2018
MIDISequencer.sc
prm

class for using Patterns to interact
with other software via MIDI
*/


MIDISequencer {

	var <isLoaded, server;
	var  <midiOutPort;
	var <channel;

	var <sequencerDict, <sequencerClock, <tempo, <beats;

	*new {
		| deviceName = "IAC Driver", portName ="Bus 1", chan = 0 |
		^super.new.prInit(deviceName, portName, chan);
	}

	prInit {
		| deviceName = "IAC Driver", portName = "Bus 1", chan = 0 |

		server = Server.default;
		server.waitForBoot {
			isLoaded = false;

			this.prInitMIDI(deviceName, portName);

			channel = chan;

			sequencerDict = IdentityDictionary.new;
			sequencerClock = TempoClock.new;
			tempo = sequencerClock.tempo;

			server.sync;

			isLoaded = true;
		}
	}

	prInitMIDI { | deviceName, portName |
		MIDIIn.connectAll;
		midiOutPort = MIDIOut.newByName(deviceName, portName);
		midiOutPort.latency = 0;
	}

	makeSequence { | name |
		{
			sequencerDict[name] = IM_PatternSeq.new;
			sequencerDict[name].stop;
			server.sync;
			sequencerDict[name].addKey(\type, \midi);
			sequencerDict[name].addKey(\midicmd, \noteOn);
			sequencerDict[name].addKey(\midiout, midiOutPort);
			sequencerDict[name].addKey(\chan, channel);
		}.fork;
	}

	addKey {  | name, key, action |
		sequencerDict[name].addKey(key, action);
	}

	playSequence { | name, clock = 'internal', quant = 0 |
		var playClock;
		if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
		sequencerDict[name].play(playClock, quant);
	}

	resetSequence { | name | sequencerDict[name].reset; }
	stopSequence { | name | sequencerDict[name].stop; }
	pauseSequence { | name | sequencerDict[name].pause }
	resumeSequence { | name | sequencerDict[name].resume; }
	isSequencePlaying { | name | ^sequencerDict[name].isPlaying }
	setSequenceQuant { | name, quant = 0 | sequencerDict[name].setQuant(quant) }

	setSequencerClockTempo { | bpm = 60 |
		var bps = bpm/60;
		tempo = bps;
		sequencerClock.tempo = tempo;
	}

}

