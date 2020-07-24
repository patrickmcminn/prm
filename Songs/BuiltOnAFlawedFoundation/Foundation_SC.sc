/*
Monday, June 19th 2017
Foundation_SC.sc
prm
*/

Foundation_SC : IM_Module {

	var server, <isLoaded;
	var <sampler;
	var <tremIsPlaying, <chordsIsPlaying;
	var <sequencer, <channel;
	var tremOn, tremOff, chordsOn, chordsOff;

	*new { | outBus = 0, seq, chan = 11, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(seq, chan);
	}

	prInit { | seq, chan |
		server = Server.default;
		server.waitForBoot {
			var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/BuiltOnAFlawedFoundation/samples/SC/";
			var sampleArray = (path ++ "*").pathMatch;

			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			sampler = Sampler.newStereo(mixer.chanStereo(0), sampleArray, relGroup: group, addAction: \addToHead);
			while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			sampler.setAttackTime(7);
			sampler.setReleaseTime(7);

			tremIsPlaying = false;
			chordsIsPlaying = false;

			sequencer = seq.uid;
			channel = chan;
			this.prMakeMIDIFuncs;

			mixer.setPreVol(6);

			isLoaded = true;
		}
	}

	prMakeMIDIFuncs {
		tremOn = MIDIFunc.noteOn({ this.playTremolo }, 60, channel, sequencer);
		tremOff = MIDIFunc.noteOff({ this.releaseTremolo }, 60, channel, sequencer);
		chordsOn = MIDIFunc.noteOn({ this.playChords }, 61, channel, sequencer);
		chordsOff = MIDIFunc.noteOff({ this.releaseChords }, 61, channel, sequencer);
	}

	prFreeMIDIFuncs {
		tremOn.free; tremOff.free;
		chordsOn.free; chordsOff.free;
	}

	//////// public functions:
	free {
		sampler.free;
		this.prFreeMIDIFuncs;
		this.freeModule;
		isLoaded = false;
	}

	playTremolo {
		sampler.playSampleSustaining(\trem, 1, -3);
		tremIsPlaying = true;
	}
	releaseTremolo {
		sampler.releaseSampleSustaining(\trem);
		tremIsPlaying = false;
	}

	playChords {
		sampler.playSampleSustaining(\chords, 0);
		chordsIsPlaying = true;
	}
	releaseChords {
		sampler.releaseSampleSustaining(\chords);
		chordsIsPlaying = false;
	}
}