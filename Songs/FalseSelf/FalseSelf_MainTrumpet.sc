/*
Wednesday, March 15th 2017
FalseSelf_MainTrumpet.sc
prm
*/

FalseSelf_MainTrumpet : IM_Processor {

  var <isLoaded;
  var server;
  var <distortion, <delay, <eq, <reverb;
  var <buffer, recordBus;
  var <splitter;

  *new { |outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      recordBus = Bus.audio(server, 1);
      buffer = Buffer.alloc(server, server.sampleRate * 29.5);

      server.sync;

      this.prAddSynthDefs;

      reverb = IM_Reverb.new(mixer.chanStereo(0), mix: 0.4, roomSize: 0.9, damp: 0.2,
        relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(reverb.inBus, relGroup: group, addAction: \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      delay = SimpleDelay.newStereo(eq.inBus, 0.375, 0.22, 5, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      distortion = Distortion.newMono(delay.inBus, 1000, relGroup: group, addAction: \addToHead);
      while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newMono(2, [distortion.inBus, recordBus], false, group, \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      //input = IM_HardwareIn.new(inBus, distortion.inBus, relGroup: group, addAction: \addToHead);

      server.sync;

      this.prInitializeReverb;
      this.prInitializeEQ;
      this.prInitializeDelay;
      this.prInitializeDistortion;

      server.sync;

      isLoaded = true;
    }

  }

  inBus { ^splitter.inBus }

  prAddSynthDefs {
    SynthDef(\prm_falseSelf_TrumpetRecorder, {
      | inBus, buffer |
      var input, record;
      input = In.ar(inBus, 1);
      record = RecordBuf.ar(input, buffer, 0, 1, 0, 1, 0, 1, 2);
    }).add;

    SynthDef(\prm_falseSelf_trumpetWarp, {
      | outBus = 0, buffer, time = 19.5, amp = 1 |
      var warp, sig;
      warp = Warp1.ar(1, buffer, Line.kr(0, 1, time, doneAction: 2), 1);
      sig = warp * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  prInitializeReverb {
    reverb.setLowPassFreq(5500);
  }

  prInitializeEQ {
    eq.setLowPassCutoff(9500);
    eq.setHighPassCutoff(173);
    eq.setPeak1Freq(1000);
    eq.setPeak1Gain(3.1);
    eq.mixer.setPreVol(-6);
  }

  prInitializeDelay {
    delay.setMix(0.35);
  }

  prInitializeDistortion {
    distortion.preEQ.setHighPassCutoff(100);
    distortion.postEQ.setHighPassCutoff(100);
    distortion.postEQ.setLowPassCutoff(7500);
    distortion.mixer.setPreVol(0);
  }

  //////// public functions:

  free {
    splitter.free;
    distortion.free;
    delay.free;
    eq.free;
    reverb.free;
    recordBus.free;
    this.freeProcessor;
    isLoaded = false;
  }

  recordLoop { Synth(\prm_falseSelf_TrumpetRecorder, [\inBus, recordBus, \buffer, buffer], splitter.group, 'addAfter'); }

  playWarpedLoop { | time = 19.5 |
    Synth(\prm_falseSelf_trumpetWarp, [\outBus, distortion.inBus, \buffer, buffer, \time, time],
      group, \addToHead);
  }

  fadeVolume { | start = -inf, end = 0, time = 10 |
    {
      var bus = Bus.control;
      server.sync;
      { Out.kr(bus, Line.kr(start.dbamp, end.dbamp, time, doneAction: 2)) }.play;
      mixer.mapAmp(bus);
      { bus.free }.defer(time);
      { mixer.setVol(end) }.defer(time);
    }.fork;
  }



}