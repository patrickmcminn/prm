// JAB 7/23/2014
IM_Sirens_FXChain : IM_Module {
  var inputSplitter, <dopplerShift, <freezeBend1, <freezeBend2;
  var <isLoaded;

  *new { |inBus = 0,
    outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead|

    ^super.new(3, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(inBus);
  }

  prInit { |inBus, dopplerMix, dopplerXRate, dopplerYRate|
    var server = Server.default;
    isLoaded = false;

    server.waitForBoot {
      this.prAddSynthDef;
      server.sync;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait });

      dopplerShift = IM_DopplerShift(mixer.chanMono(0), relGroup: group, addAction: \addToHead);
      freezeBend1 = IM_FreezeBend(mixer.chanMono(1), group, \addToHead);
      freezeBend2 = IM_FreezeBend(mixer.chanMono(2), group, \addToHead);

      [dopplerShift, freezeBend1, freezeBend2].do { |processor|
        while({ try { processor.isLoaded } != true }, { 0.001.wait });
      };

      inputSplitter = Synth(\IM_sirens_inputSplitter, [\inBus, inBus, \outBus0, dopplerShift.inBus, \outBus1, freezeBend1.inBus, \outBus2, freezeBend2.inBus], group, \addToHead);
      server.sync;

      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\IM_sirens_inputSplitter, { |inBus = 0, outBus0 = 0, outBus1 = 1, outBus2 = 2|
      var sig = SoundIn.ar(inBus);
      Out.ar(outBus0, sig);
      Out.ar(outBus1, sig);
      Out.ar(outBus2, sig);
    }).add;
  }

  free {
    fork {
      mixer.mute;

      inputSplitter.free;
      dopplerShift.free;
      freezeBend1.free;
      freezeBend2.free;

      inputSplitter = nil;
      dopplerShift = nil;
      freezeBend1 = nil;
      freezeBend2 = nil;

      isLoaded = nil;

      this.freeModule;
    };
  }
}