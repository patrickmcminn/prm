IM_Elephant_Chimes : IM_Module {
  var <isLoaded;
  var <chimeBufArray;
  var introClock, introPattern;

  *new { |outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead|

    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit;
  }

  prInit {
    var server = Server.default;
    server.waitForBoot {
      fork {
        isLoaded = false;
        this.prAddSynthDefs;
        this.prLoadSamples;
        introClock = TempoClock.new;
        server.sync;
        while( { mixer.isLoaded.not }, { 0.001.wait; });
        isLoaded = true;
      };
    };
  }

  prAddSynthDefs {
    SynthDef(\IM_Elephant_ResoChime, {
      |outBus = 0, inBuf = 0, startTime = 0,
      resoFreq = 200, resoDecay = 25, hpFreq = 20, lpFreq = 22000,
      attackTime = 0.002, sustainTime = 10, releaseTime = 0.002, preAmp = 0.05, amp = 1|

      var sig, input, resoFilt, filt, env;

      input = PlayBuf.ar(1, inBuf, 1, 1, SampleRate.ir * startTime) * preAmp;
      input = HPF.ar(input, hpFreq);

      resoFilt = Klank.ar(`[
        [1, 2.756, 5.404, 8.9, 4.0, 9.2, 2, 3, 4, 5, 6, 7, 8, 9, 10],
        [0.70, 0.5, 0.25, 0.125, 0.5, 0.25, 0.4, 0.25, 0.125, 0.0125, 0.0125, 0.0125, 0.0125, 0.0125, 0.0125].normalizeSum,
        [1, 1.5, 2, 2.5, 2, 2.5, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5, 5.5] / 5.5
      ], input, resoFreq, 0, resoDecay);

      filt = LPF.ar(resoFilt, lpFreq);
      env = filt * EnvGen.ar(Env.linen(attackTime, sustainTime, releaseTime, 1, -4), doneAction: 2);
      sig = env * amp;

      Out.ar(outBus, sig);
      DetectSilence.ar(sig, doneAction: 2);
    }).add;
  }

  prLoadSamples {
    var tempPath = PathName("~/Library/Application Support/SuperCollider/Extensions/Impatience Machine/Generators/Elephant Chimes/Samples");

    chimeBufArray = Array.newClear(tempPath.files.size);

    tempPath.filesDo { |samplePath, index|
      chimeBufArray[index] = Buffer.read(Server.default, samplePath.fullPath);
    };
  }

  returnChimeFreq { |chimeNum = 0|
    ^[62, 68, 64, 70, 66, 72, 68, 74].at(chimeNum).midicps;
  }

  playChime { |chimeNum = 0, resoFreq = 800, attackTime = 0.001, sustainTime = 0.01, releaseTime = 10, vol = 0|
    Synth(\IM_Elephant_ResoChime, [\outBus, mixer.chanMono(0), \inBuf, chimeBufArray[chimeNum], \resoFreq, resoFreq, \amp, vol.dbamp, \attackTime, attackTime, \sustainTime, sustainTime, \releaseTime, releaseTime], group, \addToHead);
  }

  playTunedChime { |chimeNum = 0, attackTime = 0.001, sustainTime = 0.01, releaseTime = 10, vol = 0|
    this.playChime(chimeNum, this.returnChimeFreq(chimeNum), attackTime, sustainTime, releaseTime, vol);
  }

  playTunedChimeOctDown { |chimeNum = 0, attackTime = 0.001, sustainTime = 0.01, releaseTime = 10, vol = 0|
    this.playChime(chimeNum, this.returnChimeFreq(chimeNum) / 2, attackTime, sustainTime, releaseTime, vol);
  }

  setIntroTempo { |tempo = 1|
    introClock.tempo_(tempo);
  }

  playIntro {
    |vol = 0, attackTime = 0.001, sustainTime = 0.01, releaseTime = 9,
    volJitter = 0, attackJitter = 0.002, sustainJitter = 0.02, releaseJitter = 2, timingJitter = 0.125|

    introPattern.stop;
    introPattern = Pbind(*[
      instrument: \IM_Elephant_ResoChime,
      index: Pxrand([0, 1, 2, 3, 4, 5, 6, 7], inf),
      inBuf: Pfunc( { |event| chimeBufArray.at(event[\index]) } ),
      resoFreq: Pfunc( { |event| this.returnChimeFreq(event[\index]); } ),
      attackTime: Pwhite(attackTime, attackTime + attackJitter.abs, inf),
      sustainTime: Pwhite(sustainTime, sustainTime + sustainJitter.abs, inf),
      releaseTime: Pwhite(releaseTime, releaseTime + releaseJitter.abs, inf),
      amp: Pwhite( (vol - volJitter.abs).dbamp, vol.dbamp, inf),
      dur: 4,
      timingOffset: Pwhite(0, timingJitter, inf),
      group: group,
      outBus: mixer.chanMono(0)
    ]).play(introClock);
  }

  pauseIntro {
    introPattern.pause;
  }

  resumeIntro {
    introPattern.play;
  }

  stopIntro {
    introPattern.stop;
  }

  mute {
    mixer.mute(0);
  }

  unMute {
    mixer.unMute(0);
  }

  setVol { |vol = 0|
    mixer.setVol(0, vol);
  }

  free {
    isLoaded = false;
    mixer.masterChan.mute;

    introPattern.stop;
    introPattern.free;

    introClock.clear;
    introClock.free;

    chimeBufArray.do { |item, index| item.free };

    introPattern = nil;
    introClock = nil;
    chimeBufArray = nil;

    super.freeModule;
  }
}