IM_DonkeyJaw_ChordSeq : IM_Module {
  var <isLoaded;
  var chordDict, chordRout;
  var trumpetSynth, flugelSynth, guitarSynth;

  *new { |trumpetInBus = 1, flugelInBus = 2, guitarInBus = 3,
    outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead|

    ^super.new(3, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(trumpetInBus, flugelInBus, guitarInBus);
  }

  prInit { |trumpetInBus, flugelInBus, guitarInBus|
    var server = Server.default;

    server.waitForBoot {
      fork {
        isLoaded = false;
        this.prAddSynthDef;
        this.prMakeChordDict;
        this.prMakeChordRout;

        server.sync;
        //while( { try { mixer.chanMono(0) } == nil }, { 0.01.wait });
        while( { mixer.isLoaded.not }, { 0.001.wait; });

        this.prMakeSynths(trumpetInBus, flugelInBus, guitarInBus);
        server.sync;
        while({ guitarSynth == nil }, { 0.001.wait; });
        // Does the sequence start with \cM6? If so, this should be rolled into
        // the chord routine and then resetChordSeq will work properly
        this.setAllChords(\cM6);
        isLoaded = true;
      };
    };
  }

  prAddSynthDef {
    SynthDef(\IM_DonkeyJaw_ChordHarmonizer, {
      | outBus = 0, inBus = 1, amp = 1, trigRate = 5.58659, filtFreq = 20000,
      int0 = -12, int1 = 12, int2 = 7, int3 = -5, int4 = 4, int5 = 0, int6 = 0, int7 = 0,
      int8 = 0, int9 = 0, int10 = 0, int11 = 0, int12 = 0, int13 = 0, int14 = 0, int15 = 0 |

      var input, trig;
      var harmList, harmList1, harmList2, ratio0, ratio1, ratio2;
      var harm0, harm1, harm2;
      var mix, filt, sig;

      input = SoundIn.ar(inBus);

      trig = Impulse.kr(trigRate);

      harmList = Dxrand([int0, int1, int2, int3, int4, int5, int6, int7,
        int8, int9, int10, int11, int12, int13, int14, int15], inf);

      ratio0 = Demand.kr(trig, 0, harmList).midiratio;
      ratio1 = Demand.kr(trig, 0, harmList).midiratio;
      ratio2 = Demand.kr(trig, 0, harmList).midiratio;

      harm0 = PitchShift.ar(input, 0.1, ratio0, 0.001, 0.04);
      harm1 = PitchShift.ar(input, 0.1, ratio1, 0.001, 0.04);
      harm2 = PitchShift.ar(input, 0.1, ratio2, 0.001, 0.04);

      mix = Mix.ar([harm0, harm1, harm2]) / 3;

      filt = LPF.ar(mix, filtFreq);

      sig = filt * amp;
      sig = Out.ar(outBus, sig);
    }).add;
  }

  prMakeChordDict {
    chordDict = IdentityDictionary();
    chordDict[\cM6] = [8, 8, 8, 8, 8, 8, 8, 3, 3, 3, 3, -4, -4, -12, 12, 7];
    chordDict[\gM64] = [5, 5, 5, 5, 5, 5, -7, -7, 9, 9, 9, 9, -3, -3, 12, 4];
    chordDict[\cSm] = [7, 7, 7, 7, 7, 7, 3, 3, 3, 3, 3, 12, 12, 12, 12, 10];
    chordDict[\gSm6] = [9, 9, 9, 9, 9, 9, 4, 4, 4, 4, 4, -3, -3, -3, 12, 7];
    chordDict[\aM] = [7, 7, 7, 7, 7, 4, 4, 4, 4, 12, 12, 12, 12, 12,  -12, 11];
    chordDict[\fSm] = [7, 7, 7, 7, 7, 7, 3, 3, 3, 3, 3, 12, 12, 12, 12, 10];
  }

  prMakeChordRout { |chordSymbolArray = #[\gM64, \cSm, \gSm6, \aM, \fSm, \cM6]|
    chordRout = r {
      "Starting harmonized chord sequence.".postln;
      chordSymbolArray.do { |chord|
        this.setAllChords(chord);
        chord.postln;
        0.yield;
      };
      "Harmonized chord sequence complete.".postln;
    }.loop;
  }

  prMakeSynths { |trumpetInBus, flugelInBus, guitarInBus|
    trumpetSynth = Synth(\IM_DonkeyJaw_ChordHarmonizer,
      [\inBus, trumpetInBus, \outBus, mixer.chanMono(0)], group, \addToHead
    );

    flugelSynth = Synth(\IM_DonkeyJaw_ChordHarmonizer,
      [\inBus, flugelInBus, \outBus, mixer.chanMono(1)], group, \addToHead
    );

    guitarSynth = Synth(\IM_DonkeyJaw_ChordHarmonizer,
      [\inBus, guitarInBus, \outBus, mixer.chanMono(2)], group, \addToHead
    );
  }

  setChord { |instrument = \trumpet, chord = \cM6|
    var tempKey;

    switch(instrument,
      \trumpet, {
        chordDict.at(chord).do { |pitch, index|
          tempKey = ("int" ++ index).asSymbol;
          trumpetSynth.set(tempKey, pitch);
        };
      },
      \flugel, {
        chordDict.at(chord).do { |pitch, index|
          tempKey = ("int" ++ index).asSymbol;
          flugelSynth.set(tempKey, pitch);
        };
      },
      \guitar, {
        chordDict.at(chord).do { |pitch, index|
          tempKey = ("int" ++ index).asSymbol;
          guitarSynth.set(tempKey, pitch);
        };
      }
    );
  }

  setTrumpetChord { |chord = \cM6| this.setChord(\trumpet, chord) }
  setFlugelChord { |chord = \cM6| this.setChord(\flugel, chord) }
  setGuitarChord { |chord = \cM6| this.setChord(\guitar, chord) }

  setAllChords { |chord = \cM6|
    this.setTrumpetChord(chord);
    this.setFlugelChord(chord);
    this.setGuitarChord(chord);
  }

  nextChord { chordRout.next }

  resetChordSeq {
    chordRout.reset;
    this.setAllChords(\cM6);
  }

  free {
    mixer.masterChan.mute;

    trumpetSynth.free;
    flugelSynth.free;
    guitarSynth.free;

    trumpetSynth = nil;
    flugelSynth = nil;
    guitarSynth = nil;

    chordDict = nil;
    chordRout = nil;

    super.freeModule;
  }
}