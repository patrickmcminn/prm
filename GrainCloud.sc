/*
Monday, November 25th 2013
GrainCloud.sc
prm
Howard Community College, Columbia, MD
before teaching a Sound Design Class
*/

GrainCloud {

  var server;
  var instArray, noteArray, octaveArray;
  var trigRateLow, trigRateHigh, sustainLow, sustainHigh, ampLow, ampHigh, cutoffLow, cutoffHigh, rqLow, rqHigh, panLow, panHigh;
  var cloudPattern;
  var faderBus, faderSynth, output;

  *new {
    ^super.new.prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot{
      this.prAddSynthDefs;
      server.sync;
      this.prMakeParameters;
      this.prMakeBus;
      server.sync;
      this.prMakeSynth;
      server.sync;
      this.prMakePattern;
    }

  }

  //////// Private Functions:

  prAddSynthDefs{

    // SynthDefs based on Microsound chapter in SuperCollider book by
    // by Alberto de Campo

   	// a gabor (approx. gaussian-shaped) grain
      SynthDef(\gaborSine, { | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0 |
        var snd = FSinOsc.ar(freq);
        var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
        var env = EnvGen.ar(Env.sine(sustain, amp2), doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

      // wider, quasi-gaussian envelope, with a hold time in the middle.
      SynthDef(\gaborWideSine, { |outBus, amp=0.1, freq=440, sustain=0.01, pan, width=0.5|
        var holdT = sustain * width;
        var fadeT = 1 - width * sustain * 0.5;
        var snd = FSinOsc.ar(freq);
        var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
        var env = EnvGen.ar(Env([0, 1, 1, 0], [fadeT, holdT, fadeT], \sin),
          levelScale: amp2,
          doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

      // a simple percussive envelope
      SynthDef(\percSin, { |outBus, amp=0.1, freq=440, sustain=0.01, pan|
        var snd = FSinOsc.ar(freq);
        var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
        var env = EnvGen.ar(
          Env.perc(0.1, 0.9, amp2),
          timeScale: sustain,
          doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

      // a reversed  percussive envelope
      SynthDef(\percSinRev, { |outBus, amp=0.1, freq=440, sustain=0.01, pan|
        var snd = FSinOsc.ar(freq);
        var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
        var env = EnvGen.ar(
          Env.perc(0.9, 0.1, amp2),
          timeScale: sustain,
          doneAction: 2
        );
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

      // an exponential decay envelope
      SynthDef(\expodecSine, { |outBus, amp=0.1, freq=440, sustain=0.01, pan|
        var snd = FSinOsc.ar(freq);
        var amp2 = AmpComp.ir(freq.max(50)) * 0.5 * amp;
        var env = XLine.ar(amp2, amp2 * 0.001, sustain, doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

      // a reversed exponential decay envelope
      SynthDef(\rexpodecSine, { |outBus, amp=0.1, freq=440, sustain=0.01, pan|
        var snd = FSinOsc.ar(freq);
        var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
        var env = XLine.ar(amp2 * 0.001, amp2, sustain, doneAction: 2)
        * (AmpComp.ir(freq) * 0.5);
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

    SynthDef(\gaborSaw, {
      | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0, cutoff = 5000, rq = 1 |
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
      var env = EnvGen.ar(Env.sine(sustain, amp2), doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

    SynthDef(\gaborWideSaw, {
      | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0, width = 0.5, cutoff = 5000, rq = 1 |
      var holdT = sustain * width;
      var fadeT = 1 - width * sustain * 0.5;
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
      var env = EnvGen.ar(Env([0, 1, 1, 0], [fadeT, holdT, fadeT], \sin),
          levelScale: amp2,
          doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

    SynthDef(\percSaw, {
      | outBus, amp=0.1, freq=440, sustain=0.01, pan = 0, cutoff = 5000, rq = 1  |
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
      var env = EnvGen.ar(
          Env.perc(0.1, 0.9, amp2),
          timeScale: sustain,
          doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

      // a reversed  percussive envelope
      SynthDef(\percRevSaw, {
      | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0, cutoff = 5000, rq = 1 |
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
      var env = EnvGen.ar(
          Env.perc(0.9, 0.1, amp2),
          timeScale: sustain,
          doneAction: 2
        );
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

      // an exponential decay envelope
      SynthDef(\expodecSaw, {
      | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0, cutoff = 5000, rq = 1 |
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = AmpComp.ir(freq.max(50)) * 0.5 * amp;
      var env = XLine.ar(amp2, amp2 * 0.001, sustain, doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

      // a reversed exponential decay envelope
      SynthDef(\rexpodecSaw, {
      | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0, cutoff = 5000, rq = 1 |
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
      var env = XLine.ar(amp2 * 0.001, amp2, sustain, doneAction: 2)
        * (AmpComp.ir(freq) * 0.5);
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

    SynthDef(\PRM_stereoFader, {
      | inBus, outBus, amp = 0.6, balance = 0, mute = 1 |
      var input, bal, sig;
      var lagTime = 0.05;
      input = In.ar(inBus, 2);
      bal = Balance2.ar(input[0], input[1], balance, amp.lag(lagTime));
      sig = input * amp;
      sig = sig * mute;
      sig = Out.ar(outBus, sig);
    }).add;

  }

  prMakeParameters {
   // |
   // durLow, durHigh, sustainLow, sustainHigh, ampLow, ampHigh,
   // cutoffLow, cutoffHigh, rqLow, rqHigh, panLow, panHigh
   // |
    instArray = ['gaborSine'];
    noteArray = [0, 4, 7, 12];
    octaveArray = [4, 5];
    trigRateLow = 200;
    trigRateHigh = 1000;
    sustainLow = 0.01;
    sustainHigh = 0.05;
    ampLow = 0.1;
    ampHigh = 0.2;
    cutoffLow = 3000;
    cutoffHigh = 5000;
    rqLow = 1;
    rqHigh = 0.3;
    panLow = -1;
    panHigh = 1;
  }

  prMakeBus {
    faderBus = Bus.audio(server, 2);
  }

  prFreeBus {
    faderBus.free;
  }

  prMakeSynth { | outBus, amp = 0.5, balance = 0, group = nil, addAction = 'addToTail' |
    faderSynth = Synth(\PRM_stereoFader, [\inBus, faderBus, \outBus, outBus, \amp, amp, \balance, balance], group, addAction);

  }

  prFreeSynth {
    faderSynth.free;
  }

  prMakePattern {

    cloudPattern = Pbind(
      \outBus, faderBus,
      \group, faderSynth,
      \addAction, \addBefore,
      \instrument, Prand(instArray, inf),
      \note, Prand(noteArray, inf),
      \octave, Prand(octaveArray, inf),
      \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).play;

  }

  //////// Public Functions:

  setAmp { | amp |
    faderSynth.set(\amp, amp);
  }

  setVol { | vol |
    faderSynth.set(\amp, vol.dbamp);
  }

  setBalance { | balance = 0 |
    faderSynth.set(\balance, balance);
  }

  setNoteArray { | array |
    noteArray = array;
    cloudPattern.stream = Pbind(
      \outBus, faderBus,
      \group, faderSynth,
      \addAction, \addBefore,
      \instrument, Prand(instArray, inf),
      \note, Prand(noteArray, inf),
      \octave, Prand(octaveArray, inf),
      \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).asStream;
  }

  setOctaveArray { | array |
    octaveArray = array;
    cloudPattern.stream = Pbind(
      \outBus, faderBus,
      \group, faderSynth,
      \addAction, \addBefore,
      \instrument, Prand(instArray, inf),
      \note, Prand(noteArray, inf),
      \octave, Prand(octaveArray, inf),
      \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).asStream;
  }

  setInstArray { | array |
    instArray = array;
    cloudPattern.stream = Pbind(
      \outBus, faderBus,
      \group, faderSynth,
      \addAction, \addBefore,
      \instrument, Prand(instArray, inf),
      \note, Prand(noteArray, inf),
      \octave, Prand(octaveArray, inf),
      \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).asStream;
  }

  setTrigRate { | low, high |
    trigRateLow = low;
    trigRateHigh = high;
    cloudPattern.stream = Pbind(
      \outBus, faderBus,
      \group, faderSynth,
      \addAction, \addBefore,
      \instrument, Prand(instArray, inf),
      \note, Prand(noteArray, inf),
      \octave, Prand(octaveArray, inf),
      \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).asStream;
  }

  setSustain { | low, high |
    sustainLow = low;
    sustainHigh = high;
    cloudPattern.stream = Pbind(
      \outBus, faderBus,
      \group, faderSynth,
      \addAction, \addBefore,
      \instrument, Prand(instArray, inf),
      \note, Prand(noteArray, inf),
      \octave, Prand(octaveArray, inf),
      \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).asStream;
  }

  setPatternAmp { | low, high |
    ampLow = low;
    ampHigh = high;
    cloudPattern.stream = Pbind(
      \outBus, faderBus,
      \group, faderSynth,
      \addAction, \addBefore,
      \instrument, Prand(instArray, inf),
      \note, Prand(noteArray, inf),
      \octave, Prand(octaveArray, inf),
      \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).asStream;
  }

  setCutoff { | low, high |
    cutoffLow = low;
    cutoffHigh = high;
    cloudPattern.stream = Pbind(
      \outBus, faderBus,
      \group, faderSynth,
      \addAction, \addBefore,
      \instrument, Prand(instArray, inf),
      \note, Prand(noteArray, inf),
      \octave, Prand(octaveArray, inf),
      \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).asStream;
  }

  setRQ { | low, high |
    rqLow = low;
    rqHigh = high;
    cloudPattern.stream = Pbind(
      \outBus, faderBus,
      \group, faderSynth,
      \addAction, \addBefore,
      \instrument, Prand(instArray, inf),
      \note, Prand(noteArray, inf),
      \octave, Prand(octaveArray, inf),
      \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).asStream;
  }

  setPan { | low, high |
    panLow = low;
    panHigh = high;
    cloudPattern.stream = Pbind(
      \outBus, faderBus,
      \group, faderSynth,
      \addAction, \addBefore,
      \instrument, Prand(instArray, inf),
      \note, Prand(noteArray, inf),
      \octave, Prand(octaveArray, inf),
      \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).asStream;
  }


}
