/*
Monday, June 19th 2017
Foundation_Moog.sc
prm

edited to accept Pyramid input 3/3/2020
leaving in MIDI file for redundancy
and legacy, whatever legacy actually means
*/

Foundation_Moog : IM_Module {

	var server, <isLoaded;
	var <moog, <delay, <eq;
	var mainMidiFile, endMidiFile, midiOut;
	var <mainSequenceIsPlaying = false;
	var <endSequenceIsPlaying = false;
	var sequencer, channel;
	var mHighPass, mLowPass;

	*new {
		| inBus = 3, outBus = 0, moogDeviceName, moogPortName,
		seq = nil, seqChan = 0,  relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(inBus, moogDeviceName, moogPortName, seq, seqChan);
	}

	prInit { | inBus, moogDeviceName, moogPortName, seq, seqChan |
		server = Server.default;
		server.waitForBoot {
			var path1 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/BuiltOnAFlawedFoundation/samples/MoogMIDI.mid";
			var path2 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/BuiltOnAFlawedFoundation/samples/EndMelody.mid";

			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			sequencer = seq.uid;
			channel = seqChan;
			/*
			mainMidiFile = SimpleMIDIFile.read(path1);
			endMidiFile = SimpleMIDIFile.read(path2);


			server.sync;
			mainMidiFile.tempo = 96;
			endMidiFile.tempo = 96;
			*/

			server.sync;

			eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			delay = SimpleDelay.newStereo(eq.inBus, 0.35, 0.3, 0.35,
				relGroup: group, addAction: \addToHead);
			while({ try { delay.isLoaded } != true }, { 0.001.wait; });

			moog = Mother32.new(inBus, delay.inBus, moogDeviceName, moogPortName, relGroup: group, addAction: \addToHead);
			while({ try { moog.isLoaded } != true }, { 0.001.wait; });

			server.sync;
			midiOut = MIDIOut.newByName(moogDeviceName, moogPortName);

			eq.setHighPassCutoff(1300);
			eq.setLowPassCutoff(3010);

			delay.setMix(0.35);

			this.prMakeMIDIFuncs;

			isLoaded = true;
		}
	}

	prMakeMIDIFuncs {
		/*
		onArray = Array.fill(128, { | i |
			MIDIFunc.noteOn({ moog.playNote(i.midicps) }, i, channel, sequencer); });
		offArray = Array.fill(128, { | i |
			MIDIFunc.noteOff({ | i | moog.releaseNote(i.midicps) }, i, channel, sequencer); });
		*/
		mHighPass = MIDIFunc.cc({ | val |
			var cutoff = val.linexp(0, 127, 20, 1310);
			eq.setHighPassCutoff(cutoff);
		}, 20, channel, sequencer);
		mLowPass = MIDIFunc.cc( { | val |
			var cutoff = val.linexp(0, 127, 3010, 20000);
			eq.setLowPassCutoff(cutoff);
		}, 21, channel, sequencer);
	}

	//////// public functions:

	free {
		midiOut.free;
		moog.free;
		//onArray.do({ | i | i.free; });
		//offArray.do({ | i | i.free; });
		this.freeModule;
		isLoaded = false;
	}

	playMainSection {
		var pattern;
		pattern = Pmul(\sustain, 0.9, mainMidiFile.p <> (type: \midi, midiout: midiOut, chan: 0)).play;
		mainSequenceIsPlaying = true;
	}

	playEndSection {
		var pattern;
		pattern = Pmul(\sustain, 0.9, endMidiFile.p <> (type: \midi, midiout: midiOut, chan: 0)).play;
		endSequenceIsPlaying = true;
	}

}