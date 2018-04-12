IM_FicusBlossom : IM_Module {
  var <isLoaded;
  var hardBus, sustainBus0, sustainBus1;
  var hardIn, sustainSynth0, sustainSynth1;

  *new { |inBus = 3,
    outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead|

    ^super.new(2, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(inBus);
  }

  prInit { |inBus|
    var server = Server.default;

    server.waitForBoot {
      isLoaded = false;
      hardBus = Bus.audio(server, 1);
      sustainBus0 = Bus.audio(server, 1);
      sustainBus1 = Bus.audio(server, 1);
      this.prAddSynthDef;
      server.sync;
      while( { mixer.isLoaded.not }, { 0.001.wait; });
      // default arg for inBus is 3, which should be guitar
      hardIn = IM_HardwareIn(inBus, hardBus, group, \addToHead);
      server.sync;
      while({ hardIn.isLoaded.not }, { 0.001.wait; });
      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\IM_FicusBlossom_Sustainer, {
      | outBus = 0, inBus = 1, amp = 1,
      trigRate = 60, grainDur = 0.35, pos = 0.3,
      rateModFreq = 3, rateModLow = 0.99, rateModHigh = 1.01,
      atk = 0.5, dec = 0, sus = 1, rel = 2, gate = 1,
      cutoff = 2000, hiPassCutoff = 100, distAmt = 1 |

      var input, record, localBuf;
      var rateMod, grainTrig, granulation, env, lpf, hpf, dist, sig;

      // Record (into a 1 second, mono buffer)
      input = In.ar(inBus, 1);
      localBuf = LocalBuf.new(SampleRate.ir * 1, 1).clear;
      record = RecordBuf.ar(input, localBuf, loop: 0);

      // Granulate
      rateMod = LFNoise0.kr(rateModFreq).range(rateModLow, rateModHigh);
      grainTrig = Dust.ar(trigRate);
      granulation = GrainBuf.ar(1, grainTrig, grainDur, localBuf, rateMod, pos);

      // Filter and distort
      lpf = LPF.ar(granulation, cutoff);
      hpf = HPF.ar(lpf, hiPassCutoff);
      dist = (hpf * distAmt).distort;

      // Envelope
      env = EnvGen.kr(Env.adsr(atk, dec, sus, rel), gate, amp, doneAction: 2);
      sig = dist * env;

      // Output
      sig = Out.ar(outBus, sig);
    }).add;
  }

  makeSustainers { |mix = 0.5|
    this.setMix(mix);

    if( sustainSynth0 != nil, { sustainSynth0.set(\gate, 0); sustainSynth0 = nil });
    sustainSynth0 = Synth(\IM_FicusBlossom_Sustainer, [\outBus, mixer.chanMono(0), \inBus, hardBus, \rateModFreq, 3, \rateModLow, 0.99, \rateModHigh, 1.01], mixer.group, \addBefore);

    if( sustainSynth1 != nil, { sustainSynth1.set(\gate, 0); sustainSynth1 = nil });
    sustainSynth1 = Synth(\IM_FicusBlossom_Sustainer, [\outBus, mixer.chanMono(1), \inBus, hardBus, \rateModFreq, 3.1, \rateModLow, 0.495, \rateModHigh, 0.505], mixer.group, \addBefore);
  }

  setMix { |mix = 0.5|
    mixer.setVol(0, mix.ampdb);
    mixer.setVol(1, (1 - mix).ampdb);
  }

  setVol { |vol = 0|
    mixer.masterChan.setVol(vol);
  }

  freeSustainers {
    sustainSynth0.set(\gate, 0);
    sustainSynth1.set(\gate, 0);
  }

  free {
    isLoaded = false;
    mixer.muteMaster;

    sustainSynth0.free;
    sustainSynth1.free;
    hardIn.free;

    sustainBus0.free;
    sustainBus1.free;
    hardBus.free;

    sustainSynth0 = nil;
    sustainSynth1 = nil;
    hardIn = nil;

    sustainBus0 = nil;
    sustainBus1 = nil;
    hardBus = nil;

    super.freeModule;
  }
}