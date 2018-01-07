/*
Thursday, August 10th 2017
SpectralMap.sc
prm
*/

SpectralMap : IM_Module {

  var <isLoaded;
  var <server;
  var synth;

  var <carrierInBus;
  var <modulatorInBus;

  var <spectralMod, <pitchShiftAmount, <pitchShiftVol;


  *new {
    | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead'|
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      carrierInBus = Bus.audio(server, 2);
      modulatorInBus = Bus.audio(server, 2);

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      mixer.mute;

      this.prAddSynthDef;

      server.sync;

      spectralMod = 0.05;
      pitchShiftAmount = 7;
      pitchShiftVol = -20;

      synth = Synth(\prm_SpectralMap,
        [\outBus, mixer.chanMono(0), \carrierInBus, carrierInBus, \modulatorInBus, modulatorInBus],
        group, \addToHead);
      server.sync;

      1.wait;

      mixer.unMute;
      mixer.setPreVol(6);

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_SpectralMap, {
      |
      carrierInBus = 0, modulatorInBus = 1,
      outBus = 0, amp = 1, spectralMod = 0.05, pitchShiftAmount = 1.5,
      pitchShiftAmp = 0.1, mix = 1
      |
      var carrier, modulator;
      var buf1, buf2, chain1, chain2, dry, wet;
      var freq, hasFreq, sig;

      carrier = In.ar(carrierInBus, 2);
      modulator = In.ar(modulatorInBus, 2);


      buf1 = LocalBuf.new(512);
      buf2 = LocalBuf.new(512);
      chain1 = FFT(buf1, carrier[0]);
      chain2 = FFT(buf2, modulator[0] * 50);

      chain1 = PV_SpectralMap(chain1, chain2, 0, 0, 1, 0.9);
      wet = IFFT(chain1);
      wet = wet * spectralMod;
      wet = Mix.new([wet, PitchShift.ar(wet, 0.2, 1.5)* pitchShiftAmp]);
      wet = wet * mix;

      dry = modulator;
      dry = dry * (1-mix);

      sig = Mix.new([dry, wet]);
      //sig = Mix.new([carrier, modulator]);

      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_fuckOff, {
      | inBus = 0, outBus = 0 |
      var sig = Out.ar(outBus, In.ar(inBus));
    }).add;
  }

  //////// public functions:

  free {
    synth.free;
    this.freeModule;
    isLoaded = false;
  }

  setSpectralMod { | mod = 0.05 |
    synth.set(\spectralMod, mod);
  }

  setPitchShiftAmount { | amount = 7 |
    synth.set(\pitchShiftAmount, 7.midiratio);
  }

  setPitchShiftVol { | vol = -20 |
    synth.set(\pitchShiftAmp, vol.dbamp);
  }

}