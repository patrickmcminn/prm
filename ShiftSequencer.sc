/*
ShiftSequencer.sc
prm
*/

ShiftSequencer {

  var <>shiftArray, <>durArray, <>ampArray;
  var shiftPattern;
  var shiftSynth;
  var input, <outBus;
  var <clock;

  *new { | inBus, outBus = 0, amp = 1, tempo = 120, group = nil, addAction = 'addToTail' |
    ^super.new.prInit(inBus, outBus, amp, tempo, group, addAction);
  }

  prInit { | inBus, outBus = 0, amp = 1, tempo = 120, group = nil, addAction = 'addToTail' |
    var server = Server.default;
    server.waitForBoot {

      this.prMakeSynthDefs;
      server.sync;
      this.prMakeClock(tempo);
      server.sync;
      this.prMakeSynth(inBus, outBus, amp, group, addAction);
      server.sync;
      this.prMakePattern;
      server.sync;

    }
  }

  ///////// Private Functions

  prMakeSynthDefs {

    SynthDef(\PRM_pitchShift, {
      | inBus, outBus, shiftAmount = 0, amp = 1 |
      var int, input, sig;
      int = exp(0.057762265 * shiftAmount);
      input = In.ar(inBus);
      sig = PitchShift.ar(input, 0.1, int, 0.001, 0.04);
      sig = sig*amp;
      sig = Out.ar(outBus, sig);
    }).add;

  }

  prMakeClock { | tempo = 120 |
    clock = TempoClock.new(tempo/60);
  }


  prMakeSynth { | inBus = 0, outBus = 0, amp = 1, group, addAction = 'addToTail' |
    shiftSynth = Synth(\PRM_pitchShift, [\inBus, inBus, \outBus, outBus, \amp, 1], group, addAction);
  }

  prFreeSynth {
    shiftSynth.free;
  }

  prMakePattern {
    shiftArray = [0, 2, 4, 5, 7];
    durArray = [0.5, 0.5, 0.5, 0.5, 0.5];
    ampArray = [1, 1, 1, 1, 1];

    shiftPattern = Pbind(
      \type, \set,
      \id, shiftSynth.nodeID,
      //\group, faderSynth,
      //\addAction, \addBefore,
      //\inBus, shiftBus,
      //\outBus, faderBus,
      \args, #[\shiftAmount, \amp],
      \shiftAmount, Pseq(shiftArray, inf),
      \dur, Pseq(durArray, inf),
      \amp, Pseq(ampArray, inf)
    ).play(clock);

  }

  prFreePattern {
    shiftPattern.stop;
    shiftPattern.free;
  }

  //////// Public Methods:

  set { | key, value |
    shiftSynth(key.asSymbol, value);
  }

  setShiftArray { | array |
    shiftArray = array;
    shiftPattern.stream = Pbind(
      \type, \set,
      \id, shiftSynth.nodeID,
      \args, #[\shiftAmount, \amp],
      \shiftAmount, Pseq(shiftArray, inf),
      \dur, Pseq(durArray, inf),
      \amp, Pseq(ampArray, inf)
    ).asStream;
    ^shiftArray;
  }

  setAmpArray { | array |
    ampArray = array;
    shiftPattern.stream = Pbind(
      \type, \set,
      \id, shiftSynth.nodeID,
      \args, #[\shiftAmount, \amp],
      \shiftAmount, Pseq(shiftArray, inf),
      \dur, Pseq(durArray, inf),
      \amp, Pseq(ampArray, inf)
    ).asStream;
    ^ampArray;
  }

  setDurArray { | array |
    durArray = array;
    shiftPattern.stream = Pbind(
      \type, \set,
      \id, shiftSynth.nodeID,
      \args, #[\shiftAmount, \amp],
      \shiftAmount, Pseq(shiftArray, inf),
      \dur, Pseq(durArray, inf),
      \amp, Pseq(ampArray, inf)
    ).asStream;
    ^durArray;
  }

  setAmp { | amp |
    shiftSynth.set(\amp, amp);
  }

  setVol { | vol |
    shiftSynth.set(\amp, vol.dbamp);
  }

  setInput { | inBus |
    shiftSynth.set(\inBus, inBus);
  }

  setTempo { | tempo |
    clock.tempo = tempo/60;
  }

  free {
    this.prFreePattern;
    this.prFreeSynth;
  }

}