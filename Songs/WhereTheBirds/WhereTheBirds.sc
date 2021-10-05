/*
Sunday, June 9th 2019
WhereTheBirds.sc
prm


*/

WhereTheBirds : IM_Module {

  var <isLoaded, server;
  var <clock, <modularClock;
  var <bed, <chords, <distChords;
  var <bass, <noiseSynth;
  var <noise, <noiseFilter, <mic;

	var <midiDict, sequencer, <midiEnabled;

  *new {
    |
    outBus = 0, micIn, moogIn, chordsIn, noiseIn,
    pitchOut, chordOut, invOut, clockOut, moogDevice, moogPort, seq,
    send0Bus, send1Bus, send2Bus, send3Bus,
    relGroup, addAction = 'addToHead'
    |
    ^super.new(7, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(
      micIn, moogIn, chordsIn, noiseIn,pitchOut, chordOut, invOut, clockOut, moogDevice, moogPort, seq);
  }

  prInit { | micIn, moogIn, chordsIn, noiseIn,pitchOut, chordOut, invOut, clockOut, moogDevice, moogPort, seq |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      mixer.masterChan.mute;

      clock = TempoClock.new(60/60);

      modularClock = ModularClock.new(clockOut, 60, 24, group, 'addToHead');
      while({ try { modularClock.isLoaded } != true }, { 0.001.wait; });

      // 1 -- bed:
      bed = WhereTheBirds_Bed.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { bed.isLoaded } != true }, { 0.001.wait; });

      // 2 -- chords:
      chords = WhereTheBirds_Chords.new(mixer.chanStereo(1), pitchOut, chordOut, invOut, chordsIn,
        group, \addToHead);
      while({ try { chords.isLoaded } != true }, { 0.001.wait; });

      // 3 -- Dist Chords:
      distChords = WhereTheBirds_DistChords.new(mixer.chanStereo(2), chordsIn, group, \addToHead);
      while({ try { distChords.isLoaded } != true }, { 0.001.wait; });

      // 4 -- Bass:
      bass = WhereTheBirds_Bass.new(mixer.chanStereo(3), moogIn, moogDevice, moogPort, group, \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });

      // 5 -- Noise Synth:
      noiseSynth = WhereTheBirds_NoiseSynth.new(mixer.chanStereo(4), group, \addToHead);
      while({ try { noiseSynth.isLoaded } != true }, { 0.001.wait; });

      // 6 -- Noise:
      noiseFilter = LowPassFilter.newMono(mixer.chanStereo(5), relGroup: group,
        addAction: \addToHead);
      while({ try { noiseFilter.isLoaded } != true }, { 0.001.wait; });
      noise = IM_HardwareIn.new(noiseIn, noiseFilter.inBus, group, \addToHead);
      while({ try { noise.isLoaded } != true }, { 0.001.wait; });

      // 7 -- mic:
      mic = WhereTheBirds_Mic.new(mixer.chanStereo(6), micIn, group, \addToHead);
      while({ try { mic.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

			server.sync;

			sequencer = seq.uid;
			//this.prMakeMIDIFuncs;
			midiDict = IdentityDictionary.new;
			midiEnabled = false;

      mixer.masterChan.unMute;
      isLoaded = true;
    }
  }

  prSetInitialParameters {
		// prevent clipping:
		7.do({ | chan | mixer.setPreVol(chan, -12) });

    // bed:
    mixer.setVol(0, -6);
    mixer.setSendVol(0, 0, 0);
    // chords:
    mixer.mute(1);
    mixer.setVol(1, -15);
    mixer.setSendVol(1, 0, -9);
    // noise chords:
    mixer.mute(2);
    mixer.setVol(1, -inf);
    mixer.setSendVol(2, 0, -15);
    // bass:
		mixer.mute(3);
    mixer.setVol(3, -9);
    mixer.setSendVol(3, 0, -24);
    mixer.setPreVol(3, -9);
    // noise synth:
    mixer.setVol(4, -3);
    mixer.setSendVol(4, 0, -6);
    // noise:
    mixer.mute(5);
    mixer.setVol(5, -inf);
    mixer.setSendVol(5, 0, -12);
    // mic:
    //mixer.mute(6);
    mic.input.mute;
    mixer.setPreVol(6, -9);
    mixer.setSendVol(6, 0, -3);

  }

	makeMIDIFuncs {
		midiDict[\drone] = MIDIFunc.noteOn({ chords.setDrone }, 42, 0, sequencer);

		midiDict[\chord1] = MIDIFunc.noteOn({ chords.setChord1 }, 66, 0, sequencer);
		midiDict[\chord2] = MIDIFunc.noteOn({ chords.setChord2 }, 69, 0, sequencer);
		midiDict[\chord3] = MIDIFunc.noteOn({ chords.setChord3 }, 61, 0, sequencer);
		midiDict[\chord4] = MIDIFunc.noteOn({ chords.setChord4 }, 64, 0, sequencer);

		midiDict[\turnaround1] = MIDIFunc.noteOn({ chords.setTurnaroundChord1 }, 78, 0, sequencer);
		midiDict[\turnaround2] = MIDIFunc.noteOn({ chords.setTurnaroundChord2 }, 81, 0, sequencer);
		midiDict[\turnaround3] = MIDIFunc.noteOn({ chords.setTurnaroundChord3 }, 73, 0, sequencer);
		midiDict[\turnaround4] = MIDIFunc.noteOn({ chords.setTurnaroundChord4 }, 76, 0, sequencer);

		midiDict[\endDrone] = MIDIFunc.noteOn({ chords.setEndDrone }, 53, 0, sequencer);

		midiEnabled = true;

	}

	freeMIDIFuncs {
		midiDict.do({ | func | func.free; });
		midiEnabled = false;
	}

  free {
    clock.stop;
    clock.free;
    modularClock.stop;
    modularClock.free;
    bed.free;
    chords.free;
    distChords.free;
    bass.free;
    noiseSynth.free;
    noiseFilter.free;
    noise.free;
    mic.free;
    this.freeModule;
    isLoaded = false;
  }
}