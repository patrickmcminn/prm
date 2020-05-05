/*
Monday, May 4th 2020
SubJuno.sc
prm

a class to run Subtractive into a
poor-man's Juno filter
*/

SubJuno : Subtractive {

	var <chorus;

	*new { | outBus, send1Bus = nil, send2Bus = nil, send3Bus = nil, send4Bus = nil, relGroup = nil, addAction = 'addToHead' |
		^super.new(outBus, send1Bus, send2Bus, send3Bus, send4Bus, relGroup, addAction).prInitJuno;
	}

	prInitJuno {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			chorus = StereoChorus.new(mixer.chanStereo(0), group, \addToHead);
			while({ try { chorus.isLoaded } != true }, { 0.001.wait; });

			server.sync;
			isLoaded = true;
		}
	}

	//////// public functions:

	free {
		sequencerDict.do({ | sequence | sequence.stop; });
		sequencerDict.do({ | sequence | sequence.free; });
		sequencerClock.clear;
		sequencerDict.stop;
		sequencerDict = nil;
		synthDict.do({ | voice | voice.free; });
		lfo.free;
		lfo = nil;
		lfoBus.free;
		lfoBus = nil;
		synthGroup.free;
		synthGroup = nil;
		chorus.free;

		this.freeModule;
	}

	playNote { | freq = 220, vol = -12 |
		this.playNoteCustomOut(chorus.inBus, freq, vol);
	}

}