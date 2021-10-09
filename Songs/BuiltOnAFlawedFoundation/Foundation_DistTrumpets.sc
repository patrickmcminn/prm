/*
Monday, June 19th 2017
Foundation_DistTrumpets.sc
prm
*/

Foundation_DistTrumpets : IM_Module {

	var server, <isLoaded;
	var <trumpet;
	var <distortion, <granulator;
	var <arrivalSequenceIsPlaying;
	var <sequencer, <channel;
	var onArray, offArray;

	*new { | outBus, highDBuffer, seq, chan = 7, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: \addToHead).prInit(highDBuffer, seq, chan);
	}

	prInit { | highDBuffer, seq, chan |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			granulator = GranularDelay2.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
			while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

			distortion = Distortion.newStereo(granulator.inBus, 3, relGroup: group, addAction: \addToHead);
			while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

			trumpet = SamplePlayer.newMono(distortion.inBus, relGroup: group, addAction: \addToHead);
			while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

			this.prSetInitialParameters;
			trumpet.setBuffer(highDBuffer);

			server.sync;

			trumpet.makeSequence(\arrival);

			server.sync;

			sequencer = seq.uid;
			channel = chan;
			this.prMakeMIDIFuncs;

			this.prMakeSequence;

			arrivalSequenceIsPlaying = false;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		//granulator.setGranulatorCrossfade(-0.5);
		granulator.setMix(0.15);
		granulator.setDelayLevel(0.1);
		granulator.setDelayTime(1.25);
		granulator.setFeedback(0.2);
		granulator.setGrainDur(0.1, 0.3);
		granulator.setGrainEnvelope('gabWide');

		distortion.preEQ.setHighPassCutoff(400);
		distortion.postEQ.setHighPassCutoff(400);
		//distortion.postEQ.setLowPassCutoff(4500);
		distortion.postEQ.setLowPassCutoff(5500);
		distortion.postEQ.setPeak1Freq(300);
		distortion.postEQ.setPeak1Gain(-5);
		distortion.postEQ.setPeak2Freq(1100);
		distortion.postEQ.setPeak1Gain(3);

		trumpet.setAttackTime(0.25);
		trumpet.setReleaseTime(0.25);
		trumpet.mixer.setPreVol(-9);
	}

	prMakeMIDIFuncs {
		onArray = Array.fill(128, { | i | MIDIFunc.noteOn({
			var note = (i-60).midiratio;
			trumpet.playSampleSustaining(i.asSymbol, 0, note); }, i, channel, sequencer); });
		offArray = Array.fill(128, { | i |  MIDIFunc.noteOff({
			trumpet.releaseSampleSustaining(i.asSymbol); }, i, channel, sequencer); });
	}

	prFreeMIDIFuncs {  onArray.do({ | i | i.free; }); offArray.do({ | i | i.free; }); }

	prMakeSequence {
		trumpet.addKey(\arrival, \legato, 1);
		trumpet.addKey(\arrival, \dur, Pseq([16, 4, 4, 4, 4, 4, 4, 4, 4, 4], 1));
		trumpet.addKey(\arrival, \rate, Pseq([
			[-12, -9, -5],
			[3, 0, 7], [3, 0, 7],[3, 0, 7], [3, 0, 7],
			[-12, -9, -5], [-14, -9, -5], [0, 3, 7], [-2, 3, 7],
			[-2, 3, 7]
		].midiratio, 1));
	}

	//////// public functions:

	free {
		trumpet.free;
		distortion.free;
		granulator.free;
		this.prFreeMIDIFuncs;
		this.freeModule;
		isLoaded = false;
	}

	playArrivalSequence { | clock |
		trumpet.playSequence(\arrival, clock);
		arrivalSequenceIsPlaying = true;
	}
	stopArrivalSequence {
		trumpet.stopSequence(\arrival);
		arrivalSequenceIsPlaying = false;
	}
}
