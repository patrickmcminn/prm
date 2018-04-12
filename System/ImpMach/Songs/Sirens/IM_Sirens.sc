/*
Thursday, May 22nd 2014
Sirens.sc
prm
*/

IM_Sirens : IM_Module {
  var <isLoaded;
  var <trumpet, <flugel, <drumMachine;

  *new { |outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus,
    relGroup = nil, addAction = \addToHead|

    ^super.new(3, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(send0Bus, send1Bus, send2Bus, send3Bus);
  }

  prInit { | send0Bus, send1Bus, send2Bus, send3Bus |
    var server = Server.default;

    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait });

      trumpet = IM_Sirens_FXChain(1, mixer.chanMono(0), send0Bus, send1Bus, send2Bus, send3Bus, relGroup: group);

      while({ try { trumpet.isLoaded } != true }, { 0.001.wait });
      trumpet.dopplerShift.setDopplerMix(1);
      trumpet.dopplerShift.setXRate(0.07);
      trumpet.dopplerShift.setYRate(0.1);

      flugel = IM_Sirens_FXChain(2, mixer.chanMono(1), send0Bus, send1Bus, send2Bus, send3Bus, relGroup: group);

      while({ try { flugel.isLoaded } != true }, { 0.001.wait });
      flugel.dopplerShift.setDopplerMix(1);
      flugel.dopplerShift.setXRate(0.09);
      flugel.dopplerShift.setYRate(0.15);

      this.prMakeDrumMachine(mixer.chanStereo(2));
      mixer.setSendPre(2);
      mixer.setVol(2, -70);
      //mixer.setSendVol(2, 1, -20);

      isLoaded = true;
    }
  }

  prMakeDrumMachine { |output = nil|
    fork{
      drumMachine = IM_DrumMachine.new(output, relGroup: group, addAction: \addToHead);
      while({ try { drumMachine.isLoaded } != true }, { 0.001.wait });

      drumMachine.setBPM(53);
      drumMachine.setPreset(0, 0.35, 6.5, 2, 2, 20, 16, 44100, 3900, 0.9, 4, 4.5);
      drumMachine.setPreset(1, 0.3, 50, 2, 2, 220, 8, 19500, 10000, 0.4, 10, -9);
      drumMachine.setPreset(2, 0.6, 0, 2, 2, 20, 12, 44100, 22000, 0.1, 1.5, -6);
      drumMachine.voice0Seq = "e...5...";
      drumMachine.voice1Seq = "|...e...";
      drumMachine.voice2Seq = "f.1.e.6.";
      drumMachine.setVolRange(-48, -12);
    }
  }

  //////// public functions:
  free {
    isLoaded = false;
    mixer.mute;

    trumpet.free;
    flugel.free;
    drumMachine.free;

    trumpet = nil;
    flugel = nil;
    drumMachine = nil;

    this.freeModule;
  }
}