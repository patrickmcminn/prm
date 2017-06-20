/*
Saturday, June 17th 2017
Foundation_ChordSynth.sc
prm
*/

Foundation_ChordSynth : IM_Module {

  var <isLoaded, server;
  var chordSynth, buffer;
  var <synthDict, <eq;

  var <filterCutoff, <filterRQ, <pan;
  var <index, <lfoFreq;
  var <sawAttackTime, <sawDecayTime, <sawSustainLevel, <sawReleaseTime;
  var <tableAttackTime, <tableDecayTime, tableSustainLevel, <tableReleaseTime;

  var <sequencerDict, <sequencerClock;

  var <mainSequenceIsPlaying = false;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      this.prAddSynthDef;
      this.prMakeBuffer;

      this.prSetInitialParameters;
      synthDict = IdentityDictionary.new;

      sequencerDict = IdentityDictionary.new;
      sequencerClock = TempoClock.new;

      server.sync;

      this.makeSequence(\main);
      server.sync;
      this.prMakeSequence;

      mixer.setPreVol(-3);

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_foundation_chordSynth, {
      |
      outBus = 0, buffer, amp = 1, freq = 220, pan = 0, index = 100,
      lfoFreq = 30, filterCutoff = 5500, rq = 0.7, gate = 1,
      sawAttackTime = 0.01, sawDecayTime = 0, sawSustainLevel = 1, sawReleaseTime = 1.81
      tableAttackTime = 0.016, tableDecayTime = 0.131, tableSustainLevel= 0.5, tableReleaseTime = 1.24
      |
      var saw, sawEnv, wavetable, wavetableEnv, lfo, filter, sig;
      lfo = LFDNoise0.ar(lfoFreq).range(0, 1);
      wavetable = Saw.ar(freq/2);
      //wavetable = Osc.ar(freq+150).range(index.neg, index);
      wavetableEnv = EnvGen.kr(Env.adsr(tableAttackTime, tableDecayTime, tableSustainLevel, tableReleaseTime), gate);
      wavetable = wavetable * wavetableEnv;
      wavetable = wavetable * lfo;
      wavetable = wavetable * index;

      saw = Saw.ar(freq + wavetable);
      sawEnv = EnvGen.kr(Env.adsr(sawAttackTime, sawDecayTime, sawSustainLevel, sawReleaseTime), gate, doneAction: 2);
      saw = saw * sawEnv;

      filter = RLPF.ar(saw, filterCutoff, rq);

      sig = Pan2.ar(filter, pan);
      sig = sig * amp;

      Out.ar(outBus, sig);
    }).add
  }

  prMakeBuffer {
    var wavetable = Wavetable.sineFill(64, [1, 0, 0, 0, 1, 0, 0.75, 0, 0, 0, 0, 0.8, 0, 0, 0, 0, 0, 0, 0, 0.25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.8, 0.85, 0, 0, 0.9, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0.8, 0, 0, 0, 0.8, 0, 0, 0, 0, 0, 0, 0.9, 0]);
    buffer = Buffer.sendCollection(server, wavetable);
  }

  prSetInitialParameters {
    eq.setHighPassCutoff(400);
    filterCutoff = 4500;
    filterRQ = 0.7;
    pan = 0;
    index = 100;
    lfoFreq = 30;
    sawAttackTime = 0.01;
    sawDecayTime = 0;
    sawSustainLevel = 1;
    sawReleaseTime = 1.81;
    tableAttackTime = 0.05;
    tableDecayTime = 0.131;
    tableSustainLevel= 0.5;
    tableReleaseTime = 1.24
  }

  prMakeSequence {
    var durArray = [
      8, 8, 8, 4, 2,
      4, 4, 4, 4, 3, 3, 2, 3,
      2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1.5, 1.5,
      2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
      16
    ];
    var noteArray =
    [
      [2, 9, 17, 24], [5, 9, 15, 24], [2, 9, 17, 24], [5, 9, 15, 24], [17, 21, 27, 36],
      [2, 9, 17, 24], [19, 23, 26, 28], [5, 9, 12, 16], [12, 16, 19, 29], [2, 9, 17, 24], [9, 12, 19, 26], [5, 9, 12, 16],
      [17, 21, 24, 28],
      [2, 9, 17, 24], [14, 17, 21, 24], [19, 23, 26, 28], [5, 9, 12, 16], [17, 21, 24, 26], [0, 7, 11, 16], [12, 16, 23, 29],
      [2, 9, 17, 24], [14, 17, 21, 24], [21, 24, 28, 31], [4, 11, 14, 19], [21, 24, 28, 31], [4, 11, 14, 19],
      [2, 9, 17, 24], [14, 17, 21, 24], [4, 11, 14, 19], [5, 9, 12, 16], [17, 21, 24, 26], [0, 7, 11, 16],
      [2, 9, 17, 24], [14, 17, 21, 24], [21, 24, 28, 31], [4, 11, 14, 19], [12, 16, 23, 31], [12, 19, 28, 35],
      [14, 17, 21, 24]
    ];
    //var ampArray = [0.01, 0.02, 0.05, 0.1, 0.15];
    this.addKey(\main, \octave, 5);
    this.addKey(\main, \legato, 1);
    this.addKey(\main, \amp, Pseq([0.01, 0.02, 0.05, 0.1, 0.15, Pseq([0.2], 34)], 1));
    this.addKey(\main, \dur, Pseq(durArray, 1));
    this.addKey(\main, \note, Pseq(noteArray, 1));
  }

  //////// public functions:

  free {
    buffer.free;
    synthDict.do({ | synth | synth.free; });
    this.freeModule;
    isLoaded = false;
  }

  playNote { | freq = 220, amp = 1 |
    synthDict[freq.asSymbol] = Synth(\prm_foundation_chordSynth,
      [\outBus, eq.inBus,
        \freq, freq, \amp, amp, \filterCutoff, filterCutoff, \filterRQ, filterRQ,
        \index, index, \pan, pan, \lfoFreq, lfoFreq, \sawAttackTime, sawAttackTime,
        \sawDecayTime, sawDecayTime, \sawSustainLevel, sawSustainLevel, \sawReleaseTime, sawReleaseTime,
        \tableAttackTime, tableAttackTime, \tableDecayTime, tableDecayTime, \tableSustainLevel, tableSustainLevel,
        \tableReleaseTime, tableReleaseTime
    ], group, \addToHead);
  }

  releaseNote { | freq = 220 |
    synthDict[freq.asSymbol].set(\gate, 0);
  }

  setFilterCutoff { | cutoff = 5000 |
    filterCutoff = cutoff;
    synthDict.do({ | synth | synth.set(\filterCutoff, filterCutoff); });
  }

    ///////// Pattern Sequencer:
  makeSequence { | name |
    fork {
      sequencerDict[name] = IM_PatternSeq.new(name, group, \addToHead);
      server.sync;
      sequencerDict[name].stop;
      sequencerDict[name].addKey(\instrument, \prm_foundation_chordSynth);
      sequencerDict[name].addKey(\outBus, eq.inBus);
      sequencerDict[name].addKey(\filterCutoff, Pfunc( { filterCutoff }));
      sequencerDict[name].addKey(\sawAttackTime, Pfunc( { sawAttackTime}));
      sequencerDict[name].addKey(\sawDecayTime, Pfunc( { sawDecayTime}));
      sequencerDict[name].addKey(\sawSustainLevel, Pfunc( { sawSustainLevel }));
      sequencerDict[name].addKey(\sawReleaseTime, Pfunc({ sawReleaseTime }));
      sequencerDict[name].addKey(\tableAttackTime, Pfunc( { tableAttackTime}));
      sequencerDict[name].addKey(\tableDecayTime, Pfunc( { tableDecayTime}));
      sequencerDict[name].addKey(\tableSustainLevel, Pfunc( { tableSustainLevel }));
      sequencerDict[name].addKey(\tableReleaseTime, Pfunc({ tableReleaseTime }));
      sequencerDict[name].addKey(\index, Pfunc({ index }));
      sequencerDict[name].addKey(\octave, 4);
      sequencerDict[name].addKey(\amp, 0.3);
    };
  }

  addKey { | name, key, action |
    sequencerDict[name].addKey(key, action);
  }

  playSequence { | name, clock = 'internal', quant = 'nil' |
    var playClock;
    if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
    sequencerDict[name].play(playClock);
  }

  resetSequence { | name | sequencerDict[name].reset; }
  stopSequence { | name | sequencerDict[name].stop; }
  pauseSequence { | name | sequencerDict[name].pause }
  resumeSequence { | name | sequencerDict[name].resume; }
  isSequencePlaying { | name | ^sequencerDict[name].isPlaying }
  setSequenceQuant { | name, quant = 0 | sequencerDict[name].setQuant(quant) }

  setSequencerClockTempo { | bpm = 60 |
    var bps = bpm/60;
    sequencerClock.tempo = bps;
  }

  playMainSequence { | clock |
    this.playSequence(\main, clock);
    mainSequenceIsPlaying = true;
  }



}