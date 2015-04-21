/*
Thursday, August 14th 2014
Looper.sc
prm
*/

Looper : IM_Module {

  var <isLoaded;
  var <isPlaying, <isRecording;
  var server, buffer, <eq, <looper, <inBus;
  var isStereo;
  var prLooperRoutine;
  var <mix;

  *newStereo {
    |
    outBus = 0, bufferSize = 1, loopMix = 0,
    send0Bus, send1Bus, send2Bus, send3Bus,
    relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).
    prInitStereo(bufferSize, loopMix);
  }

  *newMono {
    |
    outBus = 0, bufferSize = 1, loopMix = 0,
    send0Bus, send1Bus, send2Bus, send3Bus,
    relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).
    prInitMono(bufferSize, loopMix);
  }


  prInitStereo { | bufferSize = 1, loopMix = 0 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while( {  try { mixer.isLoaded } != true }, { 0.001.wait; });
      mix = loopMix;
      this.prAddSynthDef;
      server.sync;
      isPlaying = false;
      isRecording = false;
      isStereo = true;

      buffer = Buffer.alloc(server, server.sampleRate * bufferSize, 2);
      inBus = Bus.audio(server, 2);
      server.sync;

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while( { try { eq.isLoaded } != true }, { 0.001.wait; });
      server.sync;
      looper = Synth(\prm_looper, [\inBus, inBus, \outBus, eq.inBus, \buffer, buffer, \mix, mix],
        group, \addToHead);
      server.sync;
      while( { try { looper } == nil }, { 0.001.wait; });

      this.prMakeLooperRoutine;
      isLoaded = true;
    }
  }

  prInitMono { | bufferSize = 1, loopMix = 0 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while( {  try { mixer.isLoaded } != true }, { 0.001.wait; });
      mix = loopMix;
      this.prAddSynthDef;
      server.sync;
      isPlaying = false;
      isRecording = false;
      isStereo = true;
      buffer = Buffer.alloc(server, server.sampleRate * bufferSize, 1);
      inBus = Bus.audio(server);
      server.sync;

      eq = Equalizer.newMono(mixer.chanMono(0), group, 'addToHead');
      while( { try { eq.isLoaded } != true }, { 0.001.wait; });
      looper = Synth(\prm_looperMono, [\inBus, inBus, \outBus, eq.inBus, \buffer, buffer, \mix, mix],
        group, \addToHead);
      server.sync;
      while( { try { looper } == nil }, { 0.001.wait; });

      this.prMakeLooperRoutine;
      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_looper, {
      |
      loopRate = 1, loopDiv = 1, loopPos = 0,
      inBus = 0, outBus = 0, amp = 1, mix = 0,
      buffer, t_recTrig = 0, t_playTrig = 0, t_stopTrig = 0, t_reset = 1, t_resetPlayTrig = 1,
      waveLossAmount = 0, waveLossMode = 2
      |
      var input,  sum, playReset, firstTrig, recGate, recTrigger, playPhasor, playGate, playTrigger, time, loopSamples;
      var recEnv, playEnv, recInput, recorder, player, recPhasor;
      var sig;

      input = In.ar(inBus, 2);

      firstTrig = Trig.kr(SetResetFF.kr(t_recTrig, t_reset), 0.05);
      recGate = PulseCount.kr(t_recTrig, t_reset) > 1;
      time = Latch.kr(Timer.kr(t_recTrig), recGate);
      playReset = Trig.kr(t_resetPlayTrig);
      loopSamples = Latch.kr(time * SampleRate.ir, recGate);
      recTrigger = TDuty.kr(time, recGate, 1) * recGate + firstTrig;
      playGate = PulseCount.kr(t_playTrig, t_stopTrig);

      playPhasor = Phasor.ar(playReset, BufRateScale.kr(buffer) * loopRate,
        loopPos * loopSamples, (loopSamples/loopDiv) + (loopPos * loopSamples), loopPos * loopSamples);

      recEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), PulseCount.kr(t_recTrig, t_reset) % 2);
      playEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), playGate);
      player = BufRd.ar(2, buffer, playPhasor, 1, 2);
      recPhasor = Select.ar(PulseCount.kr(t_playTrig.clip(0, 1), t_reset),
        [Phasor.ar(recTrigger, BufRateScale.kr(buffer) * loopRate, 0, BufSamples.kr(buffer), 0), playPhasor]);
      recInput = player + (input * recEnv);
      recorder = BufWr.ar(recInput, buffer, recPhasor, 1);

      sig = player * playEnv;
      sig = XFade2.ar(input, sig, mix);
      sig = WaveLoss.ar(sig, waveLossAmount, 100, waveLossMode);
      sig = sig * amp;
      Out.ar(outBus, sig);
    }, [0.005, 0.05, 0.05]).add;


    SynthDef(\prm_looperMono, {
      |
      loopRate = 1, loopDiv = 1, loopPos = 0,
      inBus = 0, outBus = 0, amp = 1, mix = 0,
      buffer, t_recTrig = 0, t_playTrig = 0, t_stopTrig = 0, t_reset = 1, t_resetPlayTrig = 1,
      waveLossAmount = 0, waveLossMode = 2
      |
      var input,  sum, playReset, firstTrig, recGate, recTrigger, playPhasor, playGate, playTrigger, time, loopSamples;
      var recEnv, playEnv, recInput, recorder, player, recPhasor;
      var sig;

      input = In.ar(inBus);

      firstTrig = Trig.kr(SetResetFF.kr(t_recTrig, t_reset), 0.05);
      recGate = PulseCount.kr(t_recTrig, t_reset) > 1;
      time = Latch.kr(Timer.kr(t_recTrig), recGate);
      playReset = Trig.kr(t_resetPlayTrig);
      loopSamples = Latch.kr(time * SampleRate.ir, recGate);
      recTrigger = TDuty.kr(time, recGate, 1) * recGate + firstTrig;
      playGate = PulseCount.kr(t_playTrig, t_stopTrig);

      playPhasor = Phasor.ar(playReset, BufRateScale.kr(buffer) * loopRate,
        loopPos * loopSamples, (loopSamples/loopDiv) + (loopPos * loopSamples), loopPos * loopSamples);

      recEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), PulseCount.kr(t_recTrig, t_reset) % 2);
      playEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), playGate);
      player = BufRd.ar(2, buffer, playPhasor, 1, 2);
      recPhasor = Select.ar(PulseCount.kr(t_playTrig.clip(0, 1), t_reset),
        [Phasor.ar(recTrigger, BufRateScale.kr(buffer) * loopRate, 0, BufSamples.kr(buffer), 0), playPhasor]);
      recInput = player + (input * recEnv);
      recorder = BufWr.ar(recInput, buffer, recPhasor, 1);

      sig = player * playEnv;
      sig = XFade2.ar(input, sig, mix);
      sig = WaveLoss.ar(sig, waveLossAmount, 100, waveLossMode);
      sig = sig * amp;
      Out.ar(outBus, sig);
    }, [0.005, 0.05, 0.05]).add;

  }

  //////// public functions:

  prMakeLooperRoutine {
    prLooperRoutine = r {
      this.toggleRecordLoop.yield;

      this.toggleRecordLoop;
      this.playLoop.yield;

      loop {
        this.toggleRecordLoop.yield;
        this.toggleRecordLoop.yield;
      };
    };
  }

  // public functions:

  free {
    {
      this.stopLoop;
      server.sync;
      looper.free;
      looper = nil;
      buffer.free;
      buffer = nil;
      eq.free;
      eq = nil;
      this.freeModule;
    }.fork;
  }

  loop {
    prLooperRoutine.next;
  }

  toggleRecordLoop {
    if( isRecording == false,
      { looper.set(\t_recTrig, 1); isRecording = true; },
      { looper.set(\t_recTrig, 1); isRecording = false; }
    );
  }

  togglePlayLoop {
    if( isPlaying == false, { this.playLoop }, { this.stopLoop } );
  }

  playLoop {
    looper.set(\t_playTrig, 1);
    isPlaying = true;
  }

  stopLoop {
    looper.set(\t_stopTrig, 1, \t_playTrig, 0);
    isPlaying = false;
  }

  clearLoop { | newBufLength = 1 |
    {
      this.stopLoop;
      //looper.free;
      looper.set(\loopDiv, 1, \loopPos, 0, \loopRate, 1);
      buffer.free;
      server.sync;
      buffer = Buffer.alloc(server, server.sampleRate * newBufLength, 2);
      server.sync;
      looper.set(\buffer, buffer);
      looper.set(\t_reset, 1);
      server.sync;
      prLooperRoutine.reset;
    }.fork;
  }

  setMix { | loopMix = 0 |
    mix = loopMix;
    looper.set(\mix, mix);
  }

  setLoopRate { | loopRate = 1 |
    //looper.set(\t_playTrig, 1);
    looper.set(\loopRate, loopRate, \t_resetPlayTrig, 1);
  }

  setLoopDivison { | division = 1 |
    //looper.set(\t_playTrig, 1);
    looper.set(\loopDiv, division,  \t_resetPlayTrig, 1);
  }

  setLoopPosition { | pos = 0 |
    //looper.set(\t_playTrig, 1);
    looper.set(\loopPos, pos,  \t_resetPlayTrig, 1);
  }

  setWaveLossAmount { | amount = 0 |
    looper.set(\waveLossAmount, amount);
  }

  setWaveLossMode { | mode = 2 |
    looper.set(\waveLossMode, mode);
  }
}