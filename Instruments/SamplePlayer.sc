/*
Thursday, May 14th 2015
SamplePlayer.sc
Minneapolis, MN
*/

SamplePlayer : IM_Module {

  var <isLoaded;
  var server;
  var buffer;
  var samplerDict;
  var <attackTime, <decayTime, <sustainLevel, <releaseTime, sustainTime;
  var monoOrStereo;

  *newMono {
    |
    outBus = 0, path = nil, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = 'addToTail'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono(path);
  }

  *newStereo {
    |
    outBus = 0, path = nil, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = 'addToTail'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo(path);
  }

  prInitStereo { | path |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      monoOrStereo = 'stereo';
      this.prAddSynthDefs;
      samplerDict = IdentityDictionary.new;
      buffer = Buffer.read(server, path);
      attackTime = 0.05;
      decayTime = 0;
      sustainLevel = 1;
      releaseTime = 0.05;
      sustainTime = buffer.numFrames * server.sampleRate - (attackTime + releaseTime);
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;
      isLoaded = true;
    };
  }

  prInitMono { | path |

  }

  prAddSynthDefs {
    SynthDef(\prm_SamplePlayer_Stereo_ADSR, {
      |
      outBus = 0, amp = 1, buffer, rate = 1, loop = 0,
      startPos = 0, endPos = 1,
      tremFreq = 0, tremDepth = 0, tremWaveform = 0, tremPulseWidth = 0.5,
      attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05, gate = 1,
      cutoff = 20000
      |
      var tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold;
      var tremolo, playHead, player, filter, env, sig;
      tremSine = SinOsc.ar(tremFreq).range((1-tremDepth), 1);
      tremSaw = LFSaw.ar(tremFreq, 1).range((1-tremDepth), 1);
      tremRevSaw = LFSaw.ar(tremFreq, 1).range(-1, (1-tremDepth).neg) * -1;
      tremRect = LFPulse.ar(tremFreq, 0, tremPulseWidth).range((1-tremDepth), 1);
      tremNoise = LFNoise2.ar(tremFreq).range((1-tremDepth), 1);
      tremSampleAndHold = LFNoise0.ar(tremFreq).range((1-tremDepth), 1);
      tremolo = SelectX.ar(tremWaveform, [tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold]);
      playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
        BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
      player = BufRd.ar(2, buffer, playHead, loop);
      filter = LPF.ar(player, cutoff);
      env = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4), gate, doneAction: 2);
      sig = filter * env;
      sig = sig * tremolo;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_SamplePlayer_Stereo_OneShot, {
      |
      outBus = 0, amp = 1, buffer, rate = 1, loop = 0,
      startPos = 0, endPos = 1,
      tremFreq = 0, tremDepth = 0, tremWaveform = 0, tremPulseWidth = 0.5,
      attackTime = 0.05, sustainTime = 1, releaseTime = 0.05,
      cutoff = 20000
      |
      var tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold;
      var tremolo, playHead, player, filter, env, sig;
      tremSine = SinOsc.ar(tremFreq).range((1-tremDepth), 1);
      tremSaw = LFSaw.ar(tremFreq, 1).range((1-tremDepth), 1);
      tremRevSaw = LFSaw.ar(tremFreq, 1).range(-1, (1-tremDepth).neg) * -1;
      tremRect = LFPulse.ar(tremFreq, 0, tremPulseWidth).range((1-tremDepth), 1);
      tremNoise = LFNoise2.ar(tremFreq).range((1-tremDepth), 1);
      tremSampleAndHold = LFNoise0.ar(tremFreq).range((1-tremDepth), 1);
      tremolo = SelectX.ar(tremWaveform, [tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold]);
      playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
        BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
      player = BufRd.ar(2, buffer, playHead, loop);
      filter = LPF.ar(player, cutoff);
      env = EnvGen.kr(Env.linen(attackTime, sustainTime, releaseTime, 1, -4), 1, doneAction: 2);
      sig = filter * env;
      sig = sig * tremolo;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_SamplePlayer_Mono_ADSR, {
      |
      outBus = 0, amp = 1, buffer, rate = 1, loop = 0,
      startPos = 0, endPos = 1,
      tremFreq = 0, tremDepth = 0, tremWaveform = 0, tremPulseWidth = 0.5,
      attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05, gate = 1,
      cutoff = 20000
      |
      var tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold;
      var tremolo, playHead, player, filter, env, sig;
      tremSine = SinOsc.ar(tremFreq).range((1-tremDepth), 1);
      tremSaw = LFSaw.ar(tremFreq, 1).range((1-tremDepth), 1);
      tremRevSaw = LFSaw.ar(tremFreq, 1).range(-1, (1-tremDepth).neg) * -1;
      tremRect = LFPulse.ar(tremFreq, 0, tremPulseWidth).range((1-tremDepth), 1);
      tremNoise = LFNoise2.ar(tremFreq).range((1-tremDepth), 1);
      tremSampleAndHold = LFNoise0.ar(tremFreq).range((1-tremDepth), 1);
      tremolo = SelectX.ar(tremWaveform, [tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold]);
      playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
        BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
      player = BufRd.ar(1, buffer, playHead, loop);
      filter = LPF.ar(player, cutoff);
      env = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4), gate, doneAction: 2);
      sig = filter * env;
      sig = sig * tremolo;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_SamplePlayer_Mono_OneShot, {
      |
      outBus = 0, amp = 1, buffer, rate = 1, loop = 0,
      startPos = 0, endPos = 1,
      tremFreq = 0, tremDepth = 0, tremWaveform = 0, tremPulseWidth = 0.5,
      attackTime = 0.05, sustainTime = 1, releaseTime = 0.05,
      cutoff = 20000
      |
      var tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold;
      var tremolo, playHead, player, filter, env, sig;
      tremSine = SinOsc.ar(tremFreq).range((1-tremDepth), 1);
      tremSaw = LFSaw.ar(tremFreq, 1).range((1-tremDepth), 1);
      tremRevSaw = LFSaw.ar(tremFreq, 1).range(-1, (1-tremDepth).neg) * -1;
      tremRect = LFPulse.ar(tremFreq, 0, tremPulseWidth).range((1-tremDepth), 1);
      tremNoise = LFNoise2.ar(tremFreq).range((1-tremDepth), 1);
      tremSampleAndHold = LFNoise0.ar(tremFreq).range((1-tremDepth), 1);
      tremolo = SelectX.ar(tremWaveform, [tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold]);
      playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
        BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
      player = BufRd.ar(1, buffer, playHead, loop);
      filter = LPF.ar(player, cutoff);
      env = EnvGen.kr(Env.linen(attackTime, sustainTime, releaseTime, 1, -4), 1, doneAction: 2);
      sig = filter * env;
      sig = sig * tremolo;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:
  free {
    buffer.free;
    buffer = nil;
    samplerDict.free;
    samplerDict = nil;
    this.freeModule;
  }

  playSampleSustaining { | name, rate = 1, loop = 'false', startPos = 0, endPos = 1 |
    if( monoOrStereo == 'stereo',
      {
        samplerDict[name] = Synth(\prm_SamplePlayer_Stereo_ADSR,
          [\outBus, mixer.chanStereo(0),\rate, rate, \loop, loop, \startPos, startPos, \endPos, endPos

          ],
          group, \addToHead);
      },
      {
        samplerDict[name] = Synth(\prm_SamplePlayer_Mono_ADSR,
          [\outBus, mixer.chanMono(0), \rate, rate, \loop, loop, \startPos, startPos, \endPos, endPos,
          \attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel, \releaseTime, releaseTime
          ],
          group, \addToHead);
    });
  }

  releaseSampleSustaining { | name |
    samplerDict[name].set(\gate, 0);
  }
/*
  playSampleOneShot {
    if( monoOrStereo = 'stereo',
      {
        Synth(\prm_SamplePlayer_Stereo_OneShot,
          [\outBus, mixer.chanStereo(0), \rate, rate, \loop, loop, \startPos, startPos, \endPos, endPos],
          group, \addToHead);
      },
      {
    });

  }
*/

  setAttackTime { | attack = 0.05 | attackTime = attack; }
  setDecayTime { | decay = 0 | decayTime = decay; }
  setSustainLevel { | sustain = 1 | sustainLevel = sustain; }
  setReleaseTime { | release = 0.05 | releaseTime = release; }



}