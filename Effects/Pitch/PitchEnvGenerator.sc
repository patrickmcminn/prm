/*
Wednesday, July 1st 2015
PitchEnvGenerator
prm
*/

// external audio envelope generator and pitch shifter
// hopefully this makes sense
// oh well

PitchEnvGenerator : IM_Processor {

  var <isLoaded;
  var server;
  var synthDict;
  var monoOrStereo;

  var attackTime, decayTime, sustainLevel, releaseTime, sustainTime;

  *newStereo {
    | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    feedback = false, relGroup = nil, addAction = 'addToHead' |
    ^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitStereo;
  }


  *newMono {
  | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    feedback = false, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitMono;
  }

  prInitStereo {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      monoOrStereo = 'stereo';
      synthDict = IdentityDictionary.new;
      this.prAddSynthDefs;

      attackTime = 0.05;
      decayTime = 0;
      sustainLevel = 1;
      releaseTime = 0.05;
      sustainTime = 1;

      while({ try { mixer.isLoaded } !=true }, { 0.001.wait; });
      server.sync;

      isLoaded = true;
    }

  }

  prInitMono {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      monoOrStereo = 'mono';
      synthDict = IdentityDictionary.new;
      this.prAddSynthDefs;

      attackTime = 0.05;
      decayTime = 0;
      sustainLevel = 1;
      releaseTime = 0.05;
      sustainTime = 1;

      while({ try { mixer.isLoaded } !=true }, { 0.001.wait; });
      server.sync;

      isLoaded = true;
    }
  }

  prAddSynthDefs {
    SynthDef(\prm_PitchEnvGenerator_Sustaining_Stereo, {
      |
      inBus = 0, outBus = 0, amp = 1, pitchShift = 0, cutoff = 16000,
      attackTime = 0.05, decayTime = 0, sustainLevel = 1, releaseTime = 0.05,
      gate = 1
      |
      var input, interval, shift, filter, envelope, sig;
      input = In.ar(inBus, 2);
      interval = pitchShift.midiratio;
      shift = PitchShift.ar(input, 0.05, interval, 0.01, 0.04);
      filter = LPF.ar(shift, cutoff);
      envelope = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4),
        gate + Impulse.kr(0), doneAction: 2);
      sig = filter * envelope;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_PitchEnvGenerator_Sustaining_Mono, {
      |
      inBus = 0, outBus = 0, amp = 1, pitchShift = 0, cutoff = 16000,
      attackTime = 0.05, decayTime = 0, sustainLevel = 1, releaseTime = 0.05,
      gate = 1
      |
      var input, interval, shift, filter, envelope, sig;
      input = In.ar(inBus, 1);
      interval = pitchShift.midiratio;
      shift = PitchShift.ar(input, 0.05, interval, 0.01, 0.04);
      filter = LPF.ar(shift, cutoff);
      envelope = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4),
        gate + Impulse.kr(0), doneAction: 2);
      sig = filter * envelope;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_PitchEnvGenerator_OneShot_Stereo, {
      |
      inBus = 0, outBus = 0, amp = 1, pitchShift = 0, cutoff = 16000,
      attackTime = 0.05, sustainTime = 1, releaseTime = 0.05
      |
      var input, interval, shift, filter, envelope, sig;
      input = In.ar(inBus, 2);
      interval = pitchShift.midiratio;
      shift = PitchShift.ar(input, 0.05, interval, 0.01, 0.04);
      filter = LPF.ar(shift, cutoff);
      envelope = EnvGen.kr(Env.linen(attackTime, sustainTime, releaseTime, 1, -4), 1, doneAction: 2);
      sig = filter * envelope;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_PitchEnvGenerator_OneShot_Mono, {
      |
      inBus = 0, outBus = 0, amp = 1, pitchShift = 0, cutoff = 16000,
      attackTime = 0.05, sustainTime = 1, releaseTime = 0.05
      |
      var input, interval, shift, filter, envelope, sig;
      input = In.ar(inBus, 2);
      interval = pitchShift.midiratio;
      shift = PitchShift.ar(input, 0.05, interval, 0.01, 0.04);
      filter = LPF.ar(shift, cutoff);
      envelope = EnvGen.kr(Env.linen(attackTime, sustainTime, releaseTime, 1, -4), 1, doneAction: 2);
      sig = filter * envelope;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:

  free {
    synthDict.do({ | synth | synth.free; });
    this.freeModule;
  }

  makeSynthSustaining { | name = 'synth', pitchShift = 0, vol = 0, cutoff = 16000 |
    if( monoOrStereo == 'stereo',
      {
        synthDict[name] = Synth(\prm_PitchEnvGenerator_Sustaining_Stereo,
          [\inBus, inBus, \outBus, mixer.chanStereo(0), \amp, vol.dbamp,
            \attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel,
            \releaseTime, releaseTime, \pitchShift, pitchShift, \cutoff, cutoff],
          group, \addToHead);
      },
      {
        synthDict[name] = Synth(\prm_PitchEnvGenerator_Sustaining_Mono,
          [\inBus, inBus, \outBus, mixer.chanMono(0), \amp, vol.dbamp,
            \attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel,
            \releaseTime, releaseTime, \pitchShift, pitchShift, \cutoff, cutoff],
          group, \addToHead);
    });
  }


  releaseSynthSustaining { | name = 'synth' |
    synthDict[name].set(\gate, 0);
  }

  makeSynthOneShot { | name = 'synth', pitchShift = 0, vol = 0, cutoff = 16000 |
    if( monoOrStereo == 'stereo',
      {
        synthDict[name] = Synth(\prm_PitchEnvGenerator_OneShot_Stereo,
          [\inBus, inBus, \outBus, mixer.chanStereo(0), \amp, vol.dbamp,
            \attackTime, attackTime, \sustainTime, sustainTime,
            \releaseTime, releaseTime, \pitchShift, pitchShift, \cutoff, cutoff],
          group, \addToHead);
      },
      {
        synthDict[name] = Synth(\prm_PitchEnvGenerator_OneShot_Mono,
          [\inBus, inBus, \outBus, mixer.chanMono(0), \amp, vol.dbamp,
            \attackTime, attackTime, \sustainTime, sustainTime,
            \releaseTime, releaseTime, \pitchShift, pitchShift, \cutoff, cutoff],
          group, \addToHead);
    });
  }

  setAttackTime { | attack = 0.05 | attackTime = attack; }
  setDecayTime { | decay = 0 | decayTime = decay; }
  setSustainLevel { | sustain = 1 | sustainLevel = sustain; }
  setReleaseTime { | release = 0.05 | releaseTime = release; }
  setSustainTime{ | sustain = 1 | sustainTime = sustain; }

  setSynthVol { | name = 'synth', vol = 0 | synthDict[name].set(\amp, vol.dbamp); }
  setSynthCutoff {| name = 'synth', cutoff = 16000 | synthDict[name].set(\cutoff, cutoff); }

}