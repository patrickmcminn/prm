/*
Tuesday, November 11th 2014
Mixer_1Ch_Looper.sc
prm

1Ch mixer from IM_Mixer_1Ch
with Looper included
until ImpMach can be forked
*/

Mixer_1Ch_Looper : IM_Processor {

  var <isLoaded, <looper, <mixer1Ch;
  var <inputSplitter, <outputMixer;
  var <mixerBus, <looperBus;

  *new {
    |
    outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, feedback = false,
    relGroup = nil, addAction = \addToHead
    |
    ^super.new(1, 1,outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInit;
  }

  prInit {
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      this.prAddSynthDefs;
      server.sync;

      mixerBus = Bus.audio(server, 2);
      looperBus = Bus.audio(server, 2);
      server.sync;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      outputMixer = Synth(\prm_mixerLooper_OutputMixer, [\inBus1, mixerBus, \inBus2, looperBus,
        \outBus, mixer.chanStereo(0), \mix, -1], group, \addToHead);
      server.sync;

      while ( { try { outputMixer } == nil }, { 0.001.wait } );
      mixer1Ch = IM_Mixer_1Ch.new(mixerBus, relGroup:group, addAction:\addToHead);
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      looper = Looper.newStereo(looperBus, 30, relGroup: group, addAction: \addToHead);
      while({ try { looper.isLoaded } != true }, { 0.001.wait; });

      inputSplitter = Synth(\prm_mixerLooper_InputSplitter, [\inBus, inBus,
        \outBus1, mixer1Ch.inBus, \outBus2, looper.inBus], group, \addToHead);
      server.sync;
      while ( { try { inputSplitter } == nil }, { 0.001.wait } );

      isLoaded = true;

    };
  }

  prAddSynthDefs {
    SynthDef(\prm_mixerLooper_InputSplitter, {
      | inBus = 0, outBus1 = 0, outBus2 = 1 |
      var sig;
      sig = In.ar(inBus, 2);
      Out.ar(outBus1, sig);
      Out.ar(outBus2, sig);
    }).add;

    SynthDef(\prm_mixerLooper_OutputMixer, {
      | inBus1 = 0, inBus2 = 1, outBus = 0, mix = -1 |
      var input1, input2, sig;
      input1 = In.ar(inBus1, 2);
      input2 = In.ar(inBus2, 2);
      sig = XFade2.ar(input1, input2, mix);
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public methods:

  free {
    looper.free;
    mixer1Ch.free;
    inputSplitter.free;
    outputMixer.free;
    mixerBus.free;
    looperBus.free;


    looper = nil;
    mixer1Ch = nil;
    inputSplitter = nil;
    outputMixer = nil;
    mixerBus = nil;
    looperBus = nil;

    this.freeProcessor;
  }

  setMix { | mix = -1 | outputMixer.set(\mix, mix); }
}

+ Mixer_1Ch_Looper { }