/*
Thursday, August 10th 2017
RingMod.sc
prm
*/


RingMod : IM_Module {

  var <isLoaded;
  var server;

  var <synth;

  var <carrierInBus, <modulatorInBus;

  *new {
    | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }


  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      carrierInBus = Bus.audio(server, 2);
      modulatorInBus = Bus.audio(server, 2);
      this.prAddSynthDef;
      server.sync;

      synth = Synth(\prm_ringMod, [\outBus, mixer.chanMono(0), \carrierInBus, carrierInBus, \modulatorInBus, modulatorInBus],
        group, \addToHead);

      server.sync;

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_ringMod, {
      | carrierInBus = 0, modulatorInBus = 1, outBus, mix = 1, amp = 1 |
      var carrier, modulator, ringMod, sig, filter;
      carrier = In.ar(carrierInBus, 2);
      modulator = In.ar(modulatorInBus, 2);
      ringMod = carrier * modulator;
      filter = HPF.ar(ringMod, 200);
      sig = Mix.new([(ringMod * mix), (carrier * (1-mix))]);
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;
  }

}