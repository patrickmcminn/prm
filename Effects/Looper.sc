/*
Thursday, August 14th 2014
Looper.sc
prm
*/

Looper : IM_Processor {

  var <isLoaded;
  var <isPlaying, <isRecording, looper;
  var buffer, server;
  var isStereo;

  *newStereo { | outBus = 0, bufferSize = 10, relGroup = nil, addAction = 'addToHead' |
    ^super.new(2, 1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInitStereo(bufferSize);
  }

  *newMono { | outBus = 0, bufferSize = 10, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInitMono(bufferSize);
  }

  prInitStereo { | bufferSize = 1 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      server.sync;
      isPlaying = 0;
      isRecording = 0;
      isStereo = true;
      buffer = Buffer.alloc(server, server.sampleRate * bufferSize, 2);
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      looper = Synth(\prm_looperStereo, [\inBus, inBus, \outBus, mixer.chanStereo(0), \buffer, buffer],  group, \addToHead);
      isLoaded = true;
    }
  }

  prInitMono { | bufferSize = 1 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      server.sync;
      isPlaying = 0;
      isRecording = 0;
      isStereo = false;
      buffer = Buffer.alloc(server, server.sampleRate * bufferSize, 1);
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      looper = Synth(\prm_looperMono, [\inBus, inBus, \outBus, mixer.chanMono(0), \buffer, buffer], group, \addToHead);
      isLoaded = true;
    }
  }

  prAddSynthDefs {
    SynthDef(\prm_looperStereo, {
      | inBus = 0, outBus = 0, amp = 1, buffer, t_recTrig = 0, t_playTrig = 0, t_stopTrig = 0, t_reset = 1 |

      var input,  sum, firstTrig, recGate, recTrigger, playGate, playTrigger, time;
      var recEnv, playEnv, recorder, player;
      var sig;

      input = In.ar(inBus, 2);

      firstTrig = Trig.kr(SetResetFF.kr(t_recTrig, t_reset), 0.05);
      recGate = PulseCount.kr(t_recTrig, t_reset) > 1;
      time = Latch.kr(Timer.kr(t_recTrig), recGate);
      recTrigger = TDuty.kr(time, recGate, 1) * recGate + firstTrig;
      playGate = PulseCount.kr(t_playTrig, t_stopTrig);
      playTrigger = TDuty.kr(time, playGate, 1) * playGate;

      recEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), PulseCount.kr(t_recTrig, t_reset) % 2);
      recorder = RecordBuf.ar(input, buffer, 0, recLevel: recEnv, preLevel: 1, loop: 1, trigger: recTrigger);
      playEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), playGate);
      player = PlayBuf.ar(2, buffer, BufRateScale.kr(buffer), playTrigger, loop: 1);

      sig = player * playEnv;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_looperMono, {
      | inBus = 0, outBus = 0, amp = 1, buffer, t_recTrig = 0, t_playTrig = 0, t_stopTrig = 0, t_reset = 1 |

      var input,  sum, firstTrig, recGate, recTrigger, playGate, playTrigger, time;
      var recEnv, playEnv, recorder, player;
      var sig;

      input = In.ar(inBus);

      firstTrig = Trig.kr(SetResetFF.kr(t_recTrig, t_reset), 0.05);
      recGate = PulseCount.kr(t_recTrig, t_reset) > 1;
      time = Latch.kr(Timer.kr(t_recTrig), recGate);
      recTrigger = TDuty.kr(time, recGate, 1) * recGate + firstTrig;
      playGate = PulseCount.kr(t_playTrig, t_stopTrig);
      playTrigger = TDuty.kr(time, playGate, 1) * playGate;

      recEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), PulseCount.kr(t_recTrig, t_reset) % 2);
      recorder = RecordBuf.ar(input, buffer, 0, recLevel: recEnv, preLevel: 1, loop: 1, trigger: recTrigger);
      playEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), playGate);
      player = PlayBuf.ar(1, buffer, BufRateScale.kr(buffer), playTrigger, loop: 1);

      sig = player * playEnv;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:

  free {
    looper.free;
    looper = nil;
    buffer.free;
    buffer = nil;
    this.freeProcessor;
  }

  toggleRecordLoop {
    if( isRecording == 0,
      { looper.set(\t_recTrig, 1); isRecording = 1; },
      { looper.set(\t_recTrig, 1); isRecording = 0; }
    );
  }

  togglePlayLoop {
    if( isPlaying == 0, { this.playLoop }, { this.stopLoop } );
  }

  playLoop {
    looper.set(\t_playTrig, 1);
    isPlaying = 1;
  }

  stopLoop {
    looper.set(\t_stopTrig, 1, \t_playTrig, 0);
    isPlaying = 0;
  }

  clearLoop { | newBufLength = 10 |
    this.stopLoop;
    looper.free;
    buffer.free;
    if( isStereo == true,
      {
        buffer = Buffer.alloc(server, server.sampleRate * newBufLength, 2);
        looper = Synth(\prm_looperStereo, [\inBus, inBus, \outBus, mixer.chanStereo(0), \buffer, buffer],  group, \addToHead);
      },
      {
        buffer = Buffer.alloc(server, server.sampleRate * newBufLength, 1);
        looper = Synth(\prm_looperMono, [\inBus, inBus, \outBus, mixer.chanMono(0), \buffer, buffer],  group, \addToHead);
      }
    );
  }
}