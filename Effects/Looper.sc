/*
Thursday, August 14th 2014
Looper.sc
prm
*/

Looper : IM_Processor {

  var isPlaying, isRecording, looper;
  var buffer, server;

  * new { | outBus = 0, bufferSize = 1, relGroup = nil, addAction = 'addToHead' |
    ^super.new(2, 1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(bufferSize);
  }

  prInit { | bufferSize = 1 |
    server = Server.default;
    server.waitForBoot {
      this.prAddSynthDef;
      server.sync;
      isPlaying = 0;
      isRecording = 0;
      buffer = Buffer.alloc(server, server.sampleRate * bufferSize, 2);
      while( { try { mixer.chanStereo(0) } == nil }, { 0.01.wait } );
      looper = Synth(\prm_looper, [\inBus, inBus, \outBus, mixer.chanStereo(0), \buffer, buffer],  group, \addToHead);
    }
  }

  prAddSynthDef {
    SynthDef(\prm_looper, {
      | inBus = 0, outBus = 0, amp = 2, buffer, t_recTrig = 0, t_playTrig = 0, t_stopTrig = 0, t_reset = 1 |

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
      player = PlayBuf.ar(2, buffer, BufRateScale.kr(buffer), playTrigger;, loop: 1);

      sig = player * playEnv;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  // public functions:

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

  clearLoop { | newBufLength = 1 |
    this.stopLoop;
    looper.free;
    buffer.free;
    buffer = Buffer.alloc(server, server.sampleRate * newBufLength);
     looper = Synth(\prm_looper, [\inBus, inBus, \outBus, mixer.chanStereo(0), \buffer, buffer],  group, \addToHead);
  }
}