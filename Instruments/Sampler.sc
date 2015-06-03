/*
Sampler.sc
*/

// Multi-Sample version of SamplePlayer to allow for multiple sample layers
// velocity layers not even close to implemented here.

Sampler : IM_Module {

  var <isLoaded;
  var server;
  var samplerDict, <bufferArray;
  var <attackTime, <decayTime, <sustainLevel, <releaseTime, sustainTime;
  var <filterCutoff;
  var <tremoloRate, <tremoloDepth, <tremoloWaveform;
  var monoOrStereo;
  var sequencerDict, <sequencerClock;
  var <tempo;

  *newMono {
    |
    outBus = 0, pathArray = nil, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = 'addToTail'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono(pathArray);
  }

  *newStereo {
    |
    outBus = 0, pathArray = nil, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = 'addToTail'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo(pathArray);
  }

  prInitStereo { | pathArray |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      monoOrStereo = 'stereo';
      this.prAddSynthDefs;
      samplerDict = IdentityDictionary.new;
      bufferArray = Array.fill(pathArray.size, { | i |
        Buffer.read(server, pathArray[i])
      });
      server.sync;
      //// parameters:
      // envelope:
      attackTime = 0.05;
      decayTime = 0;
      sustainLevel = 1;
      releaseTime = 0.05;
      // filter:
      filterCutoff = 20000;
      // tremolo:
      tremoloRate = 0;
      tremoloDepth = 0;
      tremoloWaveform = 0;

      //sustainTime = buffer.numFrames.postln;
      //sustainTime = buffer.numFrames * server.sampleRate - (attackTime + releaseTime);

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      sequencerDict = IdentityDictionary.new;
      sequencerClock = TempoClock.new;
      server.sync;

      isLoaded = true;
    };
  }

  prInitMono { | pathArray |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      monoOrStereo = 'mono';
      this.prAddSynthDefs;
      samplerDict = IdentityDictionary.new;
      bufferArray = Array.fill(pathArray.size, { | i |
        Buffer.read(server, pathArray[i])
      });
      server.sync;
      //// parameters:
      // envelope:
      attackTime = 0.05;
      decayTime = 0;
      sustainLevel = 1;
      releaseTime = 0.05;
      // filter:
      filterCutoff = 20000;
      // tremolo:
      tremoloRate = 0;
      tremoloDepth = 0;
      tremoloWaveform = 0;

      //sustainTime = buffer.numFrames.postln;
      //sustainTime = buffer.numFrames * server.sampleRate - (attackTime + releaseTime);

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;

      isLoaded = true;
    };
  }

  prAddSynthDefs {
    SynthDef(\prm_Sampler_Stereo_ADSR, {
      |
      outBus = 0, amp = 1, buffer, rate = 1, loop = 1,
      startPos = 0, endPos = 1,
      tremFreq = 7, tremDepth = 0, tremWaveform = 0, tremPulseWidth = 0.5,
      attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05, gate = 1,
      filterCutoff = 20000, pan = 0
      |
      var tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold;
      var tremolo, playHead, player, filter, env, sig;
      tremSine = SinOsc.ar(tremFreq).range((1-tremDepth), 1);
      tremSaw = LFSaw.ar(tremFreq, 1).range((1-tremDepth), 1);
      tremRevSaw = LFSaw.ar(tremFreq, 1).range(-1, (1-tremDepth).neg) * -1;
      tremRect = LFPulse.ar(tremFreq, 0, tremPulseWidth).range((1-tremDepth), 1);
      tremNoise = LFDNoise1.ar(tremFreq).range((1-tremDepth), 1);
      tremSampleAndHold = LFDNoise0.ar(tremFreq).range((1-tremDepth), 1);
      tremolo = SelectX.ar(tremWaveform, [tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold]);
      playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
        BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
      player = BufRd.ar(2, buffer, playHead, loop);
      filter = LPF.ar(player, filterCutoff);
      env = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4), gate, doneAction: 2);
      sig = tremolo * env;
      sig = sig * filter;
      sig = sig * amp;
      sig = Balance2.ar(sig[0], sig[1], pan);
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_Sampler_Stereo_OneShot, {
      |
      outBus = 0, amp = 1, buffer, rate = 1, loop = 0,
      startPos = 0, endPos = 1,
      tremFreq = 0, tremDepth = 0, tremWaveform = 0, tremPulseWidth = 0.5,
      attackTime = 0.05, sustainTime = 1, releaseTime = 0.05,
      filterCutoff = 20000, pan = 0
      |
      var tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold;
      var tremolo, playHead, player, filter, env, sig;
      tremSine = SinOsc.ar(tremFreq).range((1-tremDepth), 1);
      tremSaw = LFSaw.ar(tremFreq, 1).range((1-tremDepth), 1);
      tremRevSaw = LFSaw.ar(tremFreq, 1).range(-1, (1-tremDepth).neg) * -1;
      tremRect = LFPulse.ar(tremFreq, 0, tremPulseWidth).range((1-tremDepth), 1);
      tremNoise = LFDNoise1.ar(tremFreq).range((1-tremDepth), 1);
      tremSampleAndHold = LFDNoise0.ar(tremFreq).range((1-tremDepth), 1);
      tremolo = SelectX.ar(tremWaveform, [tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold]);
      playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
        BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
      player = BufRd.ar(2, buffer, playHead, loop);
      filter = LPF.ar(player, filterCutoff);
      env = EnvGen.kr(Env.linen(attackTime, sustainTime, releaseTime, 1, -4), 1, doneAction: 2);
      sig = tremolo * env;
      sig = sig * filter;
      sig = sig * amp;
      sig = Balance2.ar(sig[0], sig[1], pan);
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_Sampler_Mono_ADSR, {
      |
      outBus = 0, amp = 1, buffer, rate = 1, loop = 0,
      startPos = 0, endPos = 1,
      tremFreq = 0, tremDepth = 0, tremWaveform = 0, tremPulseWidth = 0.5,
      attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05, gate = 1,
      filterCutoff = 20000, pan = 0
      |
      var tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold;
      var tremolo, playHead, player, filter, env, sig;
      tremSine = SinOsc.ar(tremFreq).range((1-tremDepth), 1);
      tremSaw = LFSaw.ar(tremFreq, 1).range((1-tremDepth), 1);
      tremRevSaw = LFSaw.ar(tremFreq, 1).range(-1, (1-tremDepth).neg) * -1;
      tremRect = LFPulse.ar(tremFreq, 0, tremPulseWidth).range((1-tremDepth), 1);
      tremNoise = LFDNoise1.ar(tremFreq).range((1-tremDepth), 1);
      tremSampleAndHold = LFDNoise0.ar(tremFreq).range((1-tremDepth), 1);
      tremolo = SelectX.ar(tremWaveform, [tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold]);
      playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
        BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
      player = BufRd.ar(1, buffer, playHead, loop);
      filter = LPF.ar(player, filterCutoff);
      env = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4), gate, doneAction: 2);
      sig = tremolo * env;
      sig = sig * filter;
      sig = sig * amp;
      sig = Pan2.ar(sig, pan);
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_Sampler_Mono_OneShot, {
      |
      outBus = 0, amp = 1, buffer, rate = 1, loop = 0,
      startPos = 0, endPos = 1,
      tremFreq = 0, tremDepth = 0, tremWaveform = 0, tremPulseWidth = 0.5,
      attackTime = 0.05, sustainTime = 1, releaseTime = 0.05,
      filterCutoff = 20000, pan = 0
      |
      var tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold;
      var tremolo, playHead, player, filter, env, sig;
      tremSine = SinOsc.ar(tremFreq).range((1-tremDepth), 1);
      tremSaw = LFSaw.ar(tremFreq, 1).range((1-tremDepth), 1);
      tremRevSaw = LFSaw.ar(tremFreq, 1).range(-1, (1-tremDepth).neg) * -1;
      tremRect = LFPulse.ar(tremFreq, 0, tremPulseWidth).range((1-tremDepth), 1);
      tremNoise = LFDNoise1.ar(tremFreq).range((1-tremDepth), 1);
      tremSampleAndHold = LFDNoise0.ar(tremFreq).range((1-tremDepth), 1);
      tremolo = SelectX.ar(tremWaveform, [tremSine, tremSaw, tremRevSaw, tremRect, tremNoise, tremSampleAndHold]);
      playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
        BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
      player = BufRd.ar(1, buffer, playHead, loop);
      filter = LPF.ar(player, filterCutoff);
      env = EnvGen.kr(Env.linen(attackTime, sustainTime, releaseTime, 1, -4), 1, doneAction: 2);
      sig = tremolo * env;
      sig = sig * filter;
      sig = sig * amp;
      sig = Pan2.ar(sig, pan);
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:
  free {
    bufferArray.do({ | buf | buf.free; });
    bufferArray = nil;
    samplerDict.do({ | synth | synth.free; });
    samplerDict = nil;
    this.freeModule;
  }

  playSampleSustaining { | name, sampleNum, vol = 0, rate = 1, startPos = 0, endPos = 1, pan = 0 |
    var amp = vol.dbamp;
    if( monoOrStereo == 'stereo',
      {
        samplerDict[name] = Synth(\prm_Sampler_Stereo_ADSR,
          [
            \outBus, mixer.chanStereo(0), \buffer, bufferArray[sampleNum], \rate, rate, \startPos, startPos, \endPos, endPos,
            \filterCutoff, filterCutoff, \tremFreq, tremoloRate, \tremDepth, tremoloDepth,
            \tremWaveform, tremoloWaveform,
            \attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel, \releaseTime, releaseTime,
            \amp, amp, \pan, pan
          ],
          group, \addToHead);
      },
      {
        samplerDict[name] = Synth(\prm_Sampler_Mono_ADSR,
          [
            \outBus, mixer.chanStereo(0), \buffer, bufferArray[sampleNum], \rate, rate, \startPos, startPos, \endPos, endPos,
            \filterCutoff, filterCutoff, \tremFreq, tremoloRate, \tremDepth, tremoloDepth,
            \tremWaveform, tremoloWaveform,
            \attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel, \releaseTime, releaseTime,
            \amp, amp, \pan, pan
          ],
          group, \addToHead);
    });
  }

  releaseSampleSustaining { | name |
    samplerDict[name].set(\releaseTime, releaseTime);
    samplerDict[name].set(\gate, 0);
  }

  playSampleOneShot { | sampleNum, vol = 0, rate = 1, startPos = 0, endPos = 1, pan = 0 |
    var amp = vol.dbamp;
    if( bufferArray.size > sampleNum, {
      sustainTime = (((bufferArray[sampleNum].numFrames * (endPos-startPos))* (1/rate)) / server.sampleRate) - (attackTime + releaseTime);
      if( monoOrStereo == 'stereo',
        {
          Synth(\prm_Sampler_Stereo_OneShot,
            [
              \outBus, mixer.chanStereo(0), \buffer, bufferArray[sampleNum],
              \rate, rate, \loop, 0, \startPos, startPos, \endPos, endPos,
              \filterCutoff, filterCutoff, \tremFreq, tremoloRate, \tremDepth, tremoloDepth,
              \tremWaveform, tremoloWaveform,
              \attackTime, attackTime, \releaseTime, releaseTime, \sustainTime, sustainTime,
              \amp, amp, \pan, pan
            ],
            group, \addToHead);
        },
        {
          Synth(\prm_Sampler_Mono_OneShot,
            [
              \outBus, mixer.chanStereo(0), \buffer, bufferArray[sampleNum],
              \rate, rate, \loop, 0, \startPos, startPos, \endPos, endPos,
              \filterCutoff, filterCutoff, \tremFreq, tremoloRate, \tremDepth, tremoloDepth,
              \tremWaveform, tremoloWaveform,
              \attackTime, attackTime, \releaseTime, releaseTime, \sustainTime, sustainTime,
              \amp, amp, \pan, pan
            ],
            group, \addToHead);

        }
      );
      },
      { "outside of range".postln; });

  }


  // envelope:
  setAttackTime { | attack = 0.05 | attackTime = attack; }
  setDecayTime { | decay = 0 | decayTime = decay; }
  setSustainLevel { | sustain = 1 | sustainLevel = sustain; }
  setReleaseTime { | release = 0.05 | releaseTime = release; }

  // filter:
  setFilterCutoff { | cutoff = 20000 |
    filterCutoff = cutoff;
    samplerDict.do({ | synth | synth.set(\filterCutoff, filterCutoff); });
  }

  // tremolo:
  setTremoloRate { | rate = 0 |
    tremoloRate = rate;
    samplerDict.do({ | synth | synth.set(\tremFreq, tremoloRate); });
  }
  setTremoloDepth { | depth = 0 |
    tremoloDepth = depth;
    samplerDict.do({ | synth | synth.set(\tremDepth, tremoloDepth); });
  }
  setTremoloWaveform { | waveform = 'sine' |
    if( waveform.isInteger || waveform.isFloat, { tremoloWaveform = waveform },
      {
        switch (waveform,
          'sine', { tremoloWaveform = 0 },
          'saw', { tremoloWaveform = 1 },
          'revSaw', { tremoloWaveform = 2 },
          'rect', { tremoloWaveform = 3 },
          'noise', { tremoloWaveform = 4 },
          'sampleAndHold', { tremoloWaveform = 5 }
        );
    });
    samplerDict.do({ | synth | synth.set(\tremWaveform, tremoloWaveform);});
  }

  bufferArraySize { ^bufferArray.size; }

  //////// Pattern Sequencer:

  makeSequence { | name, type = 'sustaining' |
    fork {
      sequencerDict[name] = IM_PatternSeq.new(name, group, \addToHead);
      sequencerDict[name].stop;
      server.sync;
      //sequencerDict[name].addKey(\instrument, \prm_Sampler_Stereo_OneShot);
      if( type == 'sustaining',
        {
          if( monoOrStereo == 'stereo',
            { sequencerDict[name].addKey(\instrument, \prm_Sampler_Stereo_ADSR) },
            { sequencerDict[name].addKey(\instrument, \prm_Sampler_Mono_ADSR) });
        },
        {
          if( monoOrStereo == 'stereo',
            { sequencerDict[name].addKey(\instrument, \prm_Sampler_Stereo_OneShot); },
            { sequencerDict[name].addKey(\instrument, \prm_Sampler_Mono_OneShot); }
          );
      });
      sequencerDict[name].addKey(\outBus, if( monoOrStereo == 'stereo', { mixer.chanStereo(0) }, { mixer.chanMono(0) }));
      sequencerDict[name].addKey(\attackTime, attackTime);
      sequencerDict[name].addKey(\decayTime, decayTime);
      sequencerDict[name].addKey(\sustainLevel, sustainLevel);
      sequencerDict[name].addKey(\releaseTime, releaseTime);
      sequencerDict[name].addKey(\filterCutoff, filterCutoff);
      sequencerDict[name].addKey(\tremFreq, tremoloRate);
      sequencerDict[name].addKey(\tremDepth, tremoloDepth);
      sequencerDict[name].addKey(\tremWaveform, tremoloWaveform);
      sequencerDict[name].addKey(\amp, 1);
      sequencerDict[name].addKey(\freq, 1);
    };
  }

  addKey {  | name, key, action |
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
    tempo = bps;
    sequencerClock.tempo = tempo;
  }

}