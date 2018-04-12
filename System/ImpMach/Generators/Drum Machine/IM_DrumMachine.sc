// Two drum machines sync'ed? TO DO: add a preDelay param to each voice in the sequencer

// JAB 6/14/2014
IM_DrumMachine : IM_Module {
  var <isLoaded;
  var clock, seqRout;
  var <>voice0Seq, <>voice1Seq, <>voice2Seq, <>voice3Seq;
  var <volLow, <volHigh, <>timingJitter;

  var samples, presets;

  *new { |outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead|

    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    var server = Server.default;
    isLoaded = false;
    volLow = -36;
    volHigh = -12;
    timingJitter = 0.001;
    clock = TempoClock(120/60);

    server.waitForBoot {
      fork {
        this.prAddSynthDef;
        this.prLoadSamples;

        //while( { try { mixer.isLoaded } != true }, { 0.01.wait });
        //1.0.wait;
        server.sync;
        while( { mixer.isLoaded.not }, { 0.001.wait; });
        this.prInitPresets;
        isLoaded = true;
      };
    };
  }

  // Add pitch envelope
  // sort out preDist naming and units
  // Add time stretching and how should the envelopes be implemented? Add bitRateMod
  // BUG: releaseTime not working
  prAddSynthDef {
    SynthDef(\IM_DrumMachine_SamplePlayerOneShot, { |outBus = 0, inBuf = 0, rate = 1,
      startTime = 0, attackTime = 0.002, releaseTime = 0.002, hpFreq = 20,
      bitDepth = 24, bitRate = 44100, lpFreq = 22000, lpRes = 0.1, preDistAmp = 1,
      postDistAmp = 1, amp = 1|

      var input, sig, hp, lp, bitRateMod, decimator, filt, sampleDur, sustainTime, env;

      input = PlayBuf.ar(1, inBuf, rate, 1, SampleRate.ir * startTime);
      decimator = Decimator.ar(input, bitRate, bitDepth);
      hp = HPF.ar(decimator, hpFreq);
      lp = DFM1.ar(hp, lpFreq, lpRes, preDistAmp) * postDistAmp;

      sampleDur = BufDur.kr(inBuf);
      sustainTime = sampleDur - startTime - attackTime - releaseTime;
      env = EnvGen.ar(Env.asr(attackTime, 1, releaseTime, -4), Trig.kr(1, attackTime + sustainTime), amp, 0, 1, 2);
      sig = lp * env;

      Out.ar(outBus, sig);
    }).add;
  }

  prLoadSamples {
    var pathString = PathName("~/Library/Application Support/SuperCollider/Extensions/Impatience Machine/Generators/Drum Machine/Samples/").fullPath;

    samples = Array.newClear(4);
    ["KIK.wav", "SNR.wav", "CHH.wav", "PRC.wav"].do { |string, index|
      samples[index] = Buffer.read(Server.default, pathString ++ string);
    };
  }

  prInitPresets {
    presets = Array.fill(4, { |voiceNum|
      IdentityDictionary.newFrom([\outBus, mixer.chanMono(0), \inBuf, voiceNum, \rate, 1,
        \startTime, 0, \attackTime, 0.002, \releaseTime, 0.002, \hpFreq, 20,
        \bitDepth, 24, \bitRate, 44100, \lpFreq, 22000, \lpRes, 0.1,
        \preDistAmp, 1, \postDistAmp, 1]);
    });
  }

  playSamplePreset { |sampleNum = 1, vol = -18|
    Synth(\IM_DrumMachine_SamplePlayerOneShot, presets[sampleNum].asKeyValuePairs ++ [\amp, vol.dbamp], group, \addToHead);
  }

  playSample { |sampleNum = 1, vol = 0, rate = 1,
    startTimeMS = 0, attackTimeMS = 2, releaseTimeMS = 2, hpFreq = 20,
    bitDepth = 24, bitRate = 44100, lpFreq = 22000, lpRes = 0.1,
    preDistAmp = 1, postDistVol = 0|

    Synth(\IM_DrumMachine_SamplePlayerOneShot, [\outBus, mixer.chanMono(0), \inBuf, sampleNum, \rate, rate, \startTime, startTimeMS / 1000, \attackTime, attackTimeMS / 1000, \releaseTime, releaseTimeMS / 1000, \hpFreq, hpFreq, \bitDepth, bitDepth, \bitRate, bitRate, \lpFreq, lpFreq, \lpRes, lpRes, \preDistAmp, preDistAmp, \postDistAmp, postDistVol.dbamp, \amp, vol.dbamp], group, \addToHead);
  }

  // change rout to a pattern proxy to deal with retriggering timing issues
  // change this so there are sequence presets stored in the underlying class
  // (also would it make sense to change so that numbers are used not strings?)
  // Pass a voice a string of numbers, each representing the volume of a 16th note
  // Pass a period for a rest
  playSeq {
    var count = 0, myQuant = 0;

    try { seqRout.stop };

    seqRout = r {
      [voice0Seq, voice1Seq, voice2Seq, voice3Seq].do { |voiceSeq, voiceNum|
        // var val = try { seqArray[voiceNum].wrapAt(count).digit.clip(0, 15) };
        var val = try { voiceSeq.wrapAt(count).digit.clip(0, 15) };
        if( val != nil,
          { this.playSamplePreset(voiceNum, val.linlin(0, 15, volLow, volHigh)) }
        );
      };
      (timingJitter.rand + 0.25).wait;
      count = count + 1;
    }.loop.play(clock, quant: myQuant);
  }

  stopSeq { seqRout.stop }

  getBPM { ^clock.tempo * 60 }
  setBPM { |bpm| clock.tempo_(bpm / 60) }

  setVolRange { |dbLow = -36, dbHigh = -12|
    volLow = dbLow;
    volHigh = dbHigh;
  }

  // BUG: something wonky here, this should be the default preset
  setPreset { |voiceNum = 1, rate = 1, startTimeMS = 0, attackTimeMS = 2, releaseTimeMS = 2,
    hpFreq = 20, bitDepth = 24, bitRate = 44100, lpFreq = 22000, lpRes = 0.1,
    preDistAmp = 1, postDistVol = 0|

    presets[voiceNum] = IdentityDictionary.newFrom(
      [\outBus, mixer.chanMono(0), \inBuf, voiceNum, \rate, rate,
        \startTime, startTimeMS / 1000, \attackTime, attackTimeMS / 1000, \releaseTime, releaseTimeMS / 1000,
        \hpFreq, hpFreq, \bitDepth, bitDepth, \bitRate, bitRate,
        \lpFreq, lpFreq, \lpRes, lpRes, \preDistAmp, preDistAmp, \postDistAmp, postDistVol.dbamp]
    );
  }

  setRate { |voiceNum = 0, rate = 0|
    presets[voiceNum][\rate] = rate;
  }

  setStartTime { |voiceNum = 0, startTimeMS = 0|
    presets[voiceNum][\startTime] = startTimeMS / 1000;
  }

  setAttackTime { |voiceNum = 0, attackTimeMS = 2|
    presets[voiceNum][\attackTime] = attackTimeMS / 1000;
  }

  setReleaseTime { |voiceNum = 0, releaseTimeMS = 2|
      presets[voiceNum][\releaseTime] = releaseTimeMS / 1000;
  }

  setHpFreq { |voiceNum = 0, hpFreq = 20|
    presets[voiceNum][\hpFreq] = hpFreq;
  }

  setBitDepth { |voiceNum = 0, bitDepth = 24|
    presets[voiceNum][\bitDepth] = bitDepth;
  }

  setBitRate { |voiceNum = 0, bitRate = 44100|
    presets[voiceNum][\bitRate] = bitRate;
  }

  setLpFreq { |voiceNum = 0, lpFreq = 22000|
    presets[voiceNum][\lpFreq] = lpFreq;
  }

  setLpRes { |voiceNum = 0, lpRes = 0.1|
    presets[voiceNum][\lpRes] = lpRes;
  }

  setPreDistAmp { |voiceNum = 0, preDistAmp = 1|
    presets[voiceNum][\preDistAmp] = preDistAmp;
  }

  setPostDistVol { |voiceNum = 0, postDistVol = 0|
    presets[voiceNum][\postDistAmp] = postDistVol.dbamp;
  }

  free {
    isLoaded = false;
    mixer.mute;

    try { seqRout.stop };
    clock.free;
    samples.do { |sample| sample.free; sample = nil; };

    clock = nil;
    seqRout = nil;
    presets = nil;
    samples = nil;

    super.freeModule;
  }
}