/*
Tuesday, May 20th 2014
IM_GrainFreeze.sc
prm
*/

IM_GrainFreeze : IM_Processor {
  var <isLoaded;
  var <synth;
  var <freezeActive, <attack, <release, <lowPassCutoff, <highPassCutoff, <postDistortionCutoff, <distortion;
  var server;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    isLoaded = false;

    server.waitForBoot {
      this.prAddSynthDef;
      server.sync;

      freezeActive = false;
      attack = 0.05;
      release = 0.05;
      lowPassCutoff = 7000;
      highPassCutoff = 100;
      postDistortionCutoff = 15000;
      distortion = 1;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait });
      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\IM_GrainFreeze, {
      | inBus = 0, outBus = 0, amp = 1,
      trigRate = 60, grainDur = 0.35, pos = 0.3, rate = 1,
      atk = 0.5, dec = 0, sus = 1, rel = 2, gate = 1,
      cutoff = 7000, hiPassCutoff = 100, postDistCutoff = 18000, distAmt = 1 |

      var input, record, localBuf;
      var rateMod, grainTrig, granulation, env, lpf, hpf, dist, sig;

      // Record (into a 1 second, mono buffer)
      input = In.ar(inBus, 1);
      localBuf = LocalBuf.new(SampleRate.ir * 1, 1).clear;
      record = RecordBuf.ar(input, localBuf, loop: 0);

      // Granulate
      grainTrig = Dust.ar(trigRate);
      granulation = GrainBuf.ar(1, grainTrig, grainDur, localBuf, rate, pos);

      // Filter and distort
      lpf = LPF.ar(granulation, cutoff);
      hpf = HPF.ar(lpf, hiPassCutoff);
      dist = (hpf * distAmt).distort;
      dist = LPF.ar(dist, postDistCutoff);

      // Envelope
      env = EnvGen.kr(Env.dadsr(1, atk, dec, sus, rel), gate, amp, doneAction: 2);
      sig = dist * env;

      // Output
      sig = Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:
  free {
    if( freezeActive == true, { synth.free; synth = nil; });
    this.freeProcessor;
  }

  freeze { | playRate = 1, freezeAmp = 1  |
      if( freezeActive == true, { synth.set(\gate, 0); });
    synth = Synth(\IM_GrainFreeze, [\inBus, inBus, \outBus, mixer.chanMono(0), \atk, attack, \rate, playRate, \amp, freezeAmp,
      \cutoff, lowPassCutoff, \hiPassCutoff, highPassCutoff, \distAmt, distortion], group, \addToHead);
      freezeActive = true;
  }

  releaseFreeze {
    synth.set(\rel, release);
    synth.set(\gate, 0);
    freezeActive = false;
  }

  freeFreeze { synth.free; }

  setPlayRate { | playRate = 1 |
    if( freezeActive == true, { synth.set(\rate, playRate); });  }
  mapPlayRate { | bus = 0 |  if( freezeActive == true, { synth.set(\rate, bus.asMap); }, { "freeze not active".postln; }); }
  setAttack { | atk = 0.05 | attack = atk; }
  setRelease { | rel = 0.05 | release = rel; }
  setLowPassCutoff { | cutoff = 7000 |
    lowPassCutoff = cutoff;
    if ( freezeActive == true, { synth.set(\cutoff, lowPassCutoff); });
  }
  setHighPassCutoff { | cutoff = 100 |
    highPassCutoff = cutoff;
    if( freezeActive == true, { synth.set(\hiPassCutoff, cutoff); });
  }
  setPostDistortionCutoff { | cutoff = 18000 |
    postDistortionCutoff = cutoff;
    if( freezeActive == true, { synth.set(\postDistCutoff, postDistortionCutoff); });
  }
  setDistortion { | dist = 1 |
    distortion = dist;
    if( freezeActive == true, { synth.set(\distAmt, distortion); });
  }
}