IM_DonkeyJaw_Interlude : IM_Module {
  var <isLoaded;
  var clock;

  *new { |outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead|

    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit;
  }

  prInit {
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDef;
      clock = TempoClock.new;
      server.sync;
      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\IM_DonkeyJaw_InterludeSynth, { |outBus = 0, freq = 880, amp = 0.5, attack = 1, dur = 7|
      var op1, op2, op3, op4, voice1, voice2;
      var seq_durs, seq_vals, seq_out;
      var env, sig;

      freq = Lag3.kr(freq);

      // Value sequencer
      seq_durs = Dseq([2, 7, 1, 0.3, 0.2, 0.4, 9, 11], inf);
      seq_vals = Dseq([1.0, 0, 0.3, 0.8], inf);
      seq_out = Duty.ar(seq_durs, 0, seq_vals);

      // Voice 1
      op1 = SinOsc.ar(freq / (1 + Rand(0.0, 0.005)), 0, LFTri.kr(900 + SinOsc.kr(1.075), 0, 1));
      op2 = SinOsc.ar((op1 * 300) + Rand(0, 25), SinOsc.kr(5 ** LFNoise0.kr(0.5), 0, 0.1));
      voice1 = op2;

      // Voice 2
      op3 = SinOsc.ar(freq, 0, 1);
      op4 = SinOsc.ar(op3 * 1200);
      voice2 = op4;

      // Cross fade voice mod:
      // Add in cross faded version of voices (change depth of effect with LFSaw mul parameter)
      voice1 = (voice1 + (voice1 * LFSaw.ar(0.1, 0, 0.2))) / 2;
      voice2 = (voice2 + (voice2 * (1 - LFSaw.ar(0.1, 0, 0.1)))) / 2;

      // Mix voices
      sig = Mix([voice1, voice2]) / 3;

      // Filter mixed voices (modulate filter cutoff frequency with a decreasing ramp and
      // fade in smoothed sequence of offsets to cutoff frequency)
      sig = LPF.ar(sig, freq * (3 - LFSaw.kr(0.02, 0)) + (Line.kr(0, 3) * Lag.kr( DC.kr(seq_out * 200))));

      // Envelope total sound
      env = EnvGen.ar(Env.linen(attack, dur, 5 + Rand(0, 1), 1, -2), 1, amp, doneAction: 2);
      sig = sig * env;

      // Output
      Out.ar(outBus, sig);
    }).add;
  }

  playInterlude {
    fork {
      this.playPart1;
      37.wait;
      this.playPart2;
    };
  }

  playPart1 {
    var tempSynth;
    var outBus = mixer.chanMono(0);

    r {
      "Playing 'Donkey Jaw' synth interlude, part 1A.".postln;

      tempSynth = Synth(\IM_DonkeyJaw_InterludeSynth, [\outBus, outBus, \freq, 800 + 400.rand], group, \addToHead);
      5.wait;
      tempSynth.set(\freq, 1000 + rand(-20, 20));
      2.wait;
      tempSynth.set(\freq, 1000 + rand(-20, 20));
      2.wait;

      tempSynth = Synth(\IM_DonkeyJaw_InterludeSynth, [\outBus, outBus, \freq, 800 + 400.rand], group, \addToHead);
      1.wait;
      tempSynth.set(\freq, 1000 + rand(-20, 20));
      1.wait;

      tempSynth = Synth(\IM_DonkeyJaw_InterludeSynth, [\outBus, outBus, \freq, 800 + 400.rand], group, \addToHead);
      1.wait;

      tempSynth = Synth(\IM_DonkeyJaw_InterludeSynth, [\outBus, outBus, \freq, 1200 + 400.rand], group, \addToHead);
      6.wait;
      tempSynth.set(\freq, 1000 + rand(-20, 20));
      3.wait;
      tempSynth.set(\freq, 1000 + rand(-20, 20));
      (5.rand).wait;

      group.set(\freq, 2000 + rand(-200, 200));
      1.wait;


      "Playing 'Donkey Jaw' synth interlude, part 1B.".postln;

      tempSynth = Synth(\IM_DonkeyJaw_InterludeSynth, [\outBus, outBus, \freq, 400 + 400.rand], group, \addToHead);
      1.wait;
      tempSynth.set(\freq, 1000 + rand(-20, 20));
      1.wait;

      tempSynth = Synth(\IM_DonkeyJaw_InterludeSynth, [\outBus, outBus, \freq, 800 + 400.rand], group, \addToHead);
      1.wait;

      group.set(\freq, 2000 + rand(-200, 200));
      1.wait;

      group.set(\freq, 2000 + rand(-200, 200));
      9.wait;

      group.set(\freq, 2000 + rand(-200, 200));
      1.wait;
    }.play(clock);
  }

  playPart2 {
    var outBus = mixer.chanMono(0);

    r {
      "Playing 'Donkey Jaw' synth interlude, 2.".postln;

      Synth(\IM_DonkeyJaw_InterludeSynth, [\outBus, outBus, \freq, 220, \dur, 10 + 2.rand, \amp, 1/3], group, \addToHead);
      (2 + 1.rand).wait;

      Synth(\IM_DonkeyJaw_InterludeSynth, [\outBus, outBus, \freq, 330, \dur, 10 + 6.rand, \amp, 1/3], group, \addToHead);
      (3 + 2.rand).wait;

      Synth(\IM_DonkeyJaw_InterludeSynth, [\outBus, outBus, \freq, 495, \dur, 10 + 6.rand, \amp, 1/3], group, \addToHead);
      (2 + 9.rand).wait;
    }.play(clock);
  }

  free {
    isLoaded = false;
    mixer.masterChan.mute;

    clock.clear;
    clock.free;

    clock = nil;

    super.freeModule;
  }
}