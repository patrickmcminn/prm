/*
Tuesday, July 7th 2015
Connections_Chords.sc
prm
*/

Connections_Chords :IM_Module {

  var <isLoaded, server;
  var <chordSumBufferArray;
  var <sumBusArray;
  var <bufferGranulator;
  var <eq, <reverb, <granulator;
  var myChordBufferArray;
  var <randomChordsIsPlaying, <chordProgressionIsPlaying;

  *new { | outBus = 0, chordBufferArray, relGroup = nil, addAction = 'addToTail' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(chordBufferArray);
  }

  prInit { | chordBufferArray |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      randomChordsIsPlaying = false;
      chordProgressionIsPlaying = false;
      this.prAddSynthDefs;
      myChordBufferArray = chordBufferArray;
      chordSumBufferArray = Buffer.allocConsecutive(4, server, server.sampleRate, 1);
      sumBusArray = Array.fill(4, { Bus.audio; });
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      reverb = IM_Reverb.new(granulator.inBus, amp: 1, mix: 1, roomSize: 0.7, damp: 0.7,
        relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(reverb.inBus, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      bufferGranulator = BufferGranulator.newMono(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { bufferGranulator.isLoaded }  != true }, { 0.001.wait; });

      server.sync;
      this.prMakePatterns;

      eq.setHighFreq(2500);
      eq.setHighGain(-6);
      granulator.setDelayMix(0);
      granulator.setGranulatorCrossfade(-0.25);
      granulator.setGrainDur(0.1, 0.2);
      granulator.setTrigRate(25);
      granulator.mixer.setVol(-16);

      server.sync;
      this.prMakePatternParameters;

      mixer.setPreVol(6);
      isLoaded = true;
    };
  }

  prAddSynthDefs {
    SynthDef(\prm_Connections_Chords_RecordBuf, {
      |inBus, buffer, loop = 0, preLevel = 0 |
      var input, record, sig;
      input = In.ar(inBus);
      record = RecordBuf.ar(input, buffer, preLevel: preLevel, loop: loop, trigger: 1, doneAction: 2);
    }).add;

    SynthDef(\prm_Connections_Chords_PlayBuf, {
      | outBus = 0, buffer, amp = 0.5 |
      var playBuf, envelope, sig;
      playBuf = PlayBuf.ar(1, buffer, 1, 1, 0, 0, 0);
      envelope = EnvGen.kr(Env.linen(0.1, 0.8, 0.1, 1), 1, doneAction: 2);
      sig = playBuf * envelope;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  prMakePatterns {
    bufferGranulator.makeSequence(\randomChords);
    bufferGranulator.makeSequence(\chordProgression);
  }

  prMakePatternParameters {

    bufferGranulator.addKey(\randomChords, \buffer, Pxrand([chordSumBufferArray[0], chordSumBufferArray[1],
      chordSumBufferArray[2], chordSumBufferArray[3]], inf));
    bufferGranulator.addKey(\randomChords, \dur, Pwhite(0.125, 1, inf));
    bufferGranulator.addKey(\randomChords, \attackTime, 0.05);
    bufferGranulator.addKey(\randomChords, \amp, 0.2);
    bufferGranulator.addKey(\randomChords, \legato, 1);

    bufferGranulator.addKey(\chordProgression, \buffer, Pseq([chordSumBufferArray[0], chordSumBufferArray[1],
      chordSumBufferArray[2], chordSumBufferArray[3]], inf));
    bufferGranulator.addKey(\chordProgression, \dur, 4);
    bufferGranulator.addKey(\chordProgression, \attackTime, 0.01);
    bufferGranulator.addKey(\chordProgression, \releaseTime, 0.01);
    bufferGranulator.addKey(\chordProgression, \legato, 1);
    bufferGranulator.addKey(\chordProgression, \amp, -7.dbamp);

  }

  //////// public methods:

  free {
    granulator.free;
    reverb.free;
    eq.free;
    bufferGranulator.free;
    this.freeModule;
  }

  recordChords {
    //// F# Minor:
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[0], \buffer, myChordBufferArray[0]],
      group, \addToTail);
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[0], \buffer, myChordBufferArray[1]],
      group, \addToTail);
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[0], \buffer, myChordBufferArray[2]],
      group, \addToTail);
    //record:
    Synth(\prm_Connections_Chords_RecordBuf, [\inBus, sumBusArray[0], \buffer, chordSumBufferArray[0]],
      group, \addToTail);


    //// A Major:
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[1], \buffer, myChordBufferArray[1]],
      group, \addToTail);
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[1], \buffer, myChordBufferArray[2]],
      group, \addToTail);
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[1], \buffer, myChordBufferArray[5]],
      group, \addToTail);
    //record:
    Synth(\prm_Connections_Chords_RecordBuf, [\inBus, sumBusArray[1], \buffer, chordSumBufferArray[1]],
      group, \addToTail);


    //// C# Minor:
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[2], \buffer, myChordBufferArray[2]],
      group, \addToTail);
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[2], \buffer, myChordBufferArray[4]],
      group, \addToTail);
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[2], \buffer, myChordBufferArray[5]],
      group, \addToTail);
    //record:
    Synth(\prm_Connections_Chords_RecordBuf, [\inBus, sumBusArray[2], \buffer, chordSumBufferArray[2]],
      group, \addToTail);

    //// E Major:
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[3], \buffer, myChordBufferArray[3]],
      group, \addToTail);
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[3], \buffer, myChordBufferArray[4]],
      group, \addToTail);
    Synth(\prm_Connections_Chords_PlayBuf, [\outBus, sumBusArray[3], \buffer, myChordBufferArray[5]],
      group, \addToTail);
    //record:
    Synth(\prm_Connections_Chords_RecordBuf, [\inBus, sumBusArray[3], \buffer, chordSumBufferArray[3]],
      group, \addToTail);
  }

  playRandomChords { | clock |
    bufferGranulator.playSequence(\randomChords, clock);
    randomChordsIsPlaying = true;
  }
  stopRandomChords {
    bufferGranulator.stopSequence(\randomChords);
    randomChordsIsPlaying = false;
  }
  togglePlayRandomChords { | clock |
    if( randomChordsIsPlaying == false,
      { this.playRandomChords(clock); }, { this.stopRandomChords });
  }

  playChordProgression { | clock |
    clock.playNextBar(bufferGranulator.playSequence(\chordProgression, clock));
    chordProgressionIsPlaying = true;
  }
  stopChordProgression {
    bufferGranulator.stopSequence(\chordProgression);
    chordProgressionIsPlaying = false;
  }
  togglePlayChordProgression { | clock |
    if( chordProgressionIsPlaying == false,
      { this.playChordProgression(clock); }, { this.stopChordProgression; });
  }

}