/*
Thursday, January 23rd 2014
IM_Elephant_Looper.sc
*/

IM_Elephant_Looper : IM_Processor {
  var <isLoaded;
  var buffer;
  var <looperSynth;
  var recRoutine, playing;

  *new { | outBus = 0, bufLength = 10, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead|

    ^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(bufLength);
  }

  prInit { | bufLength = 10|
    var server = Server.default;

    server.waitForBoot{
      isLoaded = false;
      this.prAddSynthDef;
      buffer = Buffer.alloc(server, server.sampleRate * bufLength, 1);
      server.sync;
      //while( { try { mixer.chan(0).synth } == nil }, { 0.01.wait } );
      //0.1.wait;
      while( { mixer.isLoaded.not }, { 0.001.wait; });
      looperSynth = Synth(\IM_EleLooper, [\inBus1, inBus.subBus(0), \inBus2, inBus.subBus(1),
        \outBus, mixer.chanMono(0), \amp, 1, \buffer, buffer], group, \addToHead);
      this.prMakeRecRoutine;
      while( { looperSynth == nil }, { 0.001.wait; });
      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\IM_EleLooper, {
      | inBus1 = 0, inBus2 = 1, outBus = 0, amp = 1, buffer, t_recTrig = 0, t_playTrig = 0, t_stopTrig = 0, t_reset = 1 |

      var input1, input2, sum, firstTrig, recGate, recTrigger, playGate, playTrigger, time;
      var recEnv, playEnv, recorder, player;
      var sig;

      input1 = In.ar(inBus1);
      input2 = In.ar(inBus2);
      sum = Mix.ar([input1, input2]);


      firstTrig = Trig.kr(SetResetFF.kr(t_recTrig, t_reset), 0.05);
      recGate = PulseCount.kr(t_recTrig, t_reset) > 1;
      time = Latch.kr(Timer.kr(t_recTrig), recGate);
      recTrigger = TDuty.kr(time, recGate, 1) * recGate + firstTrig;
      playGate = PulseCount.kr(t_playTrig, t_stopTrig);
      playTrigger = TDuty.kr(time, playGate, 1) * playGate;

      recEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), PulseCount.kr(t_recTrig, t_reset) % 2);
      recorder = RecordBuf.ar(sum, buffer, 0, recLevel: recEnv, preLevel: 1, loop: 1, trigger: recTrigger);
      playEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), playGate);
      player = PlayBuf.ar(1, buffer, BufRateScale.kr(buffer), playTrigger;, loop: 1);

      sig = player * playEnv;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  prMakeRecRoutine {
    recRoutine = r {
      looperSynth.set(\t_recTrig, 1).yield;

      looperSynth.set(\t_recTrig, 1, \t_playTrig, 1);
      (playing = true).yield;

      loop {
        if( playing == true,
          { looperSynth.set(\t_recTrig, 1).yield },
          { looperSynth.set(\t_playTrig, 1, \t_recTrig, 1);
            playing = true }
        );
      };

    };
  }

  toggleRecord { recRoutine.next }

  togglePlay {
    if (playing == false,
      { looperSynth.set(\t_playTrig, 1); playing = true; },
      { looperSynth.set(\t_stopTrig, 1, \t_playTrig, 0); playing = false; }
    );
  }

  reset { looperSynth.set(\t_reset, 1) }

  free {
    isLoaded = false;
    mixer.masterChan.mute;
    looperSynth.free;
    buffer.free;
    looperSynth = nil;
    buffer = nil;

    recRoutine = nil;
    playing = nil;

    super.freeModule;
  }
}