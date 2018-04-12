/*
Thursday, January 30th 2014
IM_SerpentGang.sc
Beram and McMinn

Updated as IM_Song subclass 5/21/2014 JAB
*/

IM_SerpentGang : IM_Module {
  var <isLoaded;
  var synthsLoaded;
  var buffer, recRoutine;
  var trumpetBus, flugelBus;
  var trumpetInput, flugelInput, trumpetMultiShift, flugelMultiShift, looper;
  var server;

  var recording, playing;

  *new { |trumpetInBus = 1, flugelInBus = 2, bufLengthSecs = 10,
    outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead|

    ^super.new(3, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(trumpetInBus, flugelInBus, bufLengthSecs);
  }

  prInit { |trumpetInBus, flugelInBus, bufLengthSecs|
    server = Server.default;

    server.waitForBoot {
      isLoaded = false;
      trumpetBus = Bus.audio(server, 2);
      flugelBus = Bus.audio(server, 2);
      this.prAddSynthDef;
      buffer =  Buffer.alloc(server, server.sampleRate * bufLengthSecs, 2);
      server.sync;

      //while( { try { mixer.chanMono(2) } == nil }, { 0.01.wait });
      while({ try { mixer.isLoaded } != true }, { 0.001.wait });

      this.prMakeSynths(trumpetInBus, flugelInBus,
        mixer.chanMono(0), mixer.chanMono(1), mixer.chanMono(2) );
      server.sync;

      while({ synthsLoaded.not }, { 0.001.wait; });
      recording = 0;
      playing = 0;

      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\IM_Serpent_Looper, {
      | inBus1 = 0, inBus2 = 1, outBus = 0, amp = 2, buffer, t_recTrig = 0, t_playTrig = 0, t_stopTrig = 0, t_reset = 1 |

      var input1, input2, sum, firstTrig, recGate, recTrigger, playGate, playTrigger, time;
      var recEnv, playEnv, recorder, player;
      var sig;

      input1 = In.ar(inBus1, 2);
      input2 = In.ar(inBus2, 2);
      sum = Mix.ar([input1 + input2]);

      firstTrig = Trig.kr(SetResetFF.kr(t_recTrig, t_reset), 0.05);
      recGate = PulseCount.kr(t_recTrig, t_reset) > 1;
      time = Latch.kr(Timer.kr(t_recTrig), recGate);
      recTrigger = TDuty.kr(time, recGate, 1) * recGate + firstTrig;
      playGate = PulseCount.kr(t_playTrig, t_stopTrig);
      playTrigger = TDuty.kr(time, playGate, 1) * playGate;

      recEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), PulseCount.kr(t_recTrig, t_reset) % 2);
      recorder = RecordBuf.ar(sum, buffer, 0, recLevel: recEnv, preLevel: 1, loop: 1, trigger: recTrigger);
      playEnv = EnvGen.kr(Env.asr(0.05, 1, 0.05), playGate);
      player = PlayBuf.ar(2, buffer, BufRateScale.kr(buffer), playTrigger;, loop: 1);

      sig = player * playEnv;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  prMakeSynths { |trumpetInBus, flugelInBus, trumpetOutBus, flugelOutBus, loopOutBus|
    fork {
      synthsLoaded = false;

      trumpetMultiShift = IM_MultiShift.new(trumpetOutBus, [1, 3, 5, 7], 0, group, \addToHead);
      server.sync;
      while({ trumpetMultiShift.isLoaded.not }, { 0.001.wait; });

      flugelMultiShift = IM_MultiShift.new(flugelOutBus, [2, 3, 5, 7], 0, group, \addToHead);
      server.sync;
      while({ flugelMultiShift.isLoaded.not }, { 0.001.wait; });

      trumpetInput = IM_HardwareIn.new(trumpetInBus, trumpetMultiShift.inBus,
        group, \addToHead);
      server.sync;
      while({ trumpetInput.isLoaded.not }, { 0.001.wait; });

      flugelInput = IM_HardwareIn.new(flugelInBus, flugelMultiShift.inBus,
        group, \addToHead);
      server.sync;
      while({ flugelInput.isLoaded.not }, { 0.001.wait; });

      looper = Synth(\IM_Serpent_Looper, [\inBus1, trumpetOutBus, \inBus2, flugelOutBus,
          \outBus, loopOutBus, \buffer, buffer], mixer.group, \addBefore);
      while({ looper == nil }, { 0.001.wait; });

      synthsLoaded = true;
    };
  }

  //////// Public Functions:

  toggleRecordLoop {
    if( recording == 0,
      { looper.set(\t_recTrig, 1); recording = 1; },
      { looper.set(\t_recTrig, 1); recording = 0; }
    );
  }

  togglePlayLoop {
    if( playing == 0, { this.playLoop }, { this.stopLoop } );
  }

  playLoop {
    looper.set(\t_playTrig, 1);
    playing = 1;
  }

  stopLoop {
    looper.set(\t_stopTrig, 1, \t_playTrig, 0);
    playing = 0;
  }

  free {
    mixer.masterChan.mute;

    trumpetMultiShift.free;
    flugelMultiShift.free;
    looper.free;
    trumpetInput.free;
    flugelInput.free;

    buffer.free;

    trumpetBus.free;
    flugelBus.free;

    trumpetMultiShift = nil;
    flugelMultiShift = nil;
    looper = nil;
    trumpetInput = nil;
    flugelInput = nil;

    buffer = nil;

    trumpetBus = nil;
    flugelBus = nil;

    recording = nil;
    playing = nil;

    super.freeModule;
  }
}