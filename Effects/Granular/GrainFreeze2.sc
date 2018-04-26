/*
Sunday, April 22nd 2018
GrainFreeze2.sc
prm
*/

GrainFreeze2 : IM_Processor {

  var <isLoaded;
  var server;

  var isStereo;

  var <buffer, freezeDict;

  var <attackTime, <decayTime, <sustainLevel, <releaseTime;

  var <lowPassCutoff, <highPassCutoff, <distortion;


  *newMono { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono;
  }

  *newStereo { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo;
  }

  prInitMono {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      this.prAddSynthDefs;

      isStereo = false;
      buffer = Buffer.alloc(server, server.sampleRate * 1, 1);

      server.sync;

      freezeDict = IdentityDictionary.new;

      this.prInitializeParameters;

      isLoaded = true;
    }
  }

  prInitStereo {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      this.prAddSynthDefs;

      isStereo = true;
      buffer = Buffer.alloc(server, server.sampleRate * 1, 1);

      server.sync;

      freezeDict = IdentityDictionary.new;

      this.prInitializeParameters;

      isLoaded = true;
    }
  }


  prAddSynthDefs {
    SynthDef(\prm_grainFreeze2_freeze_stereo, {
      |  outBus = 0, buffer, amp = 1,
      trigRate = 60, grainDur = 0.35, pos = 0.3, rate = 1,
      attackTime = 0.5, decayTime = 0, sustainLevel = 1, releaseTime = 2, gate = 1,
      lowPassCutoff = 18000, highPassCutoff = 100, distAmt = 1 |

      var rateMod, grainTrig, granulation, env, lpf, hpf, dist, sig;

      // Granulate
      grainTrig = Dust.ar(trigRate);
      granulation = GrainBuf.ar(2, grainTrig, grainDur, buffer, rate, pos);

      // Filter and distort

      dist = (granulation * distAmt).distort;
      lpf = LPF.ar(dist, lowPassCutoff);
      hpf = HPF.ar(lpf, highPassCutoff);

      // Envelope
      env = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime), gate, amp, doneAction: 2);
      sig = hpf * env;

      // Output
      sig = Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_grainFreeze2_freeze_mono, {
      |  outBus = 0, buffer, amp = 1,
      trigRate = 60, grainDur = 0.35, pos = 0.3, rate = 1,
      attackTime = 0.5, decayTime = 0, sustainLevel = 1, releaseTime = 2, gate = 1,
      lowPassCutoff = 18000, highPassCutoff = 100, distAmt = 1 |

      var input, record;
      var rateMod, grainTrig, granulation, env, lpf, hpf, dist, sig;

      // Granulate
      grainTrig = Dust.ar(trigRate);
      granulation = GrainBuf.ar(1, grainTrig, grainDur, buffer, rate, pos);

      // Filter and distort

      dist = (granulation * distAmt).distort;
      lpf = LPF.ar(dist, lowPassCutoff);
      hpf = HPF.ar(lpf, highPassCutoff);

      // Envelope
      env = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime), gate, amp, doneAction: 2);
      sig = hpf * env;

      // Output
      sig = Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_grainFreeze2_recordBuf_mono, {
      | inBus = 0, buffer |
      var input, record;
      input = In.ar(inBus, 1);
      record = RecordBuf.ar(input, buffer, loop: 0, doneAction: 2);
    }).add;

    SynthDef(\prm_grainFreeze2_recordBuf_stereo, {
      | inBus = 0, buffer |
      var input, record;
      input = In.ar(inBus, 2);
      input = (input[0] + input[1])/2;
      record = RecordBuf.ar(input, buffer, loop: 0, doneAction: 2);
    }).add;

  }

  prInitializeParameters {
    attackTime = 0.05;
    decayTime = 0;
    sustainLevel = 1;
    releaseTime = 0.05;

    lowPassCutoff = 18000;
    highPassCutoff = 80;

    distortion = 1;
  }

  //////// public functions:

  free {
    freezeDict.do({ | synth | synth.free; });
    buffer.free;
    this.freeProcessor;
    isLoaded = false;
  }

  recordBuffer {
    var record;
    if( isStereo == true,
      { record = Synth(\prm_grainFreeze2_recordBuf_stereo, [\inBus, inBus, \buffer, buffer], group, \addToHead); },
      { record = Synth(\prm_grainFreeze2_recordBuf_mono, [\inBus, inBus, \buffer, buffer], group, \addToHead); }
    );
  }

  clearBuffer { buffer.clear; }

  playNote { | name, note = 0, vol = -3 |
    if( isStereo == true,
      { freezeDict[name] = Synth(\prm_grainFreeze2_freeze_stereo, [
          \buffer, buffer, \outBus, mixer.chanStereo(0), \rate, note.midiratio, \amp, vol.dbamp,
          \attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel, \releaseTime, releaseTime,
          \lowPassCutoff, lowPassCutoff, \highPassCutoff, highPassCutoff, \distortion, distortion],
          group, \addToHead);
      },
      {
        freezeDict[name] = Synth(\prm_grainFreeze2_freeze_mono, [
          \buffer, buffer, \outBus, mixer.chanMono(0), \rate, note.midiratio, \amp, vol.dbamp,
          \attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel, \releaseTime, releaseTime,

          \lowPassCutoff, lowPassCutoff, \highPassCutoff, highPassCutoff, \distortion, distortion
          ],
          group, \addToHead);
      }
    );
  }

  releaseNote { | name | freezeDict[name].set(\gate, 0); }

  setAttackTime { | attack = 0.05 |
    attackTime = attack;
    freezeDict.do({ | synth | synth.set(\attackTime, attackTime); });
  }

  setDecayTime { | decay = 0.05 |
    decayTime = decay;
    freezeDict.do({ | synth | synth.set(\decayTime, decayTime); });
  }

  setSustainLevel { | sustain = 1 |
    sustainLevel = sustain;
    freezeDict.do({ | synth | synth.set(\sustainLevel, sustainLevel); });
  }

  setReleaseTime { | release = 0.85 |
    releaseTime = release;
    freezeDict.do({ | synth | synth.set(\releaseTime, releaseTime); });
  }

  setLowPassCutoff { | cutoff = 18000 |
    lowPassCutoff = cutoff;
    freezeDict.do({ | synth | synth.set(\lowPassCutoff, lowPassCutoff); });
  }

  setHighPassCutoff { | cutoff = 80 |
    highPassCutoff = cutoff;
    freezeDict.do({ | synth | synth.set(\highPassCutoff, highPassCutoff); });
  }

  setDistortion { | dist = 1 |
    distortion = dist;
    freezeDict.do({ | synth | synth.set(\distortion, distortion); });
  }


}