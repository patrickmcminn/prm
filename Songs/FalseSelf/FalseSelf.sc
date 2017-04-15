/*
Saturday, April 15th 2017
FalseSelf.sc
prm
*/

FalseSelf : Song {

  var <server, <isLoaded;

  var <clock;

  var <fakeGuitar, <bellSection, <melodySynth;
  var <bassSection, <modular, <modularInput;
  var <drums, <mainTrumpet, <mainTrumpetInput;
  var <modularRoutine;

  *new { | mixAOutBus, mixBOutBus, mixCOutBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(mixAOutBus, 8, mixBOutBus, 8, mixCOutBus, 8, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixerC.isLoaded } != true }, { 0.001.wait; });

      mixerA.tglMute(2);

      clock = TempoClock.new;
      server.sync;
      clock.tempo = 160/60;

      fakeGuitar = FalseSelf_FakeGuitar.new(mixerA.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { fakeGuitar.isLoaded } != true }, { 0.001.wait; });

      bellSection = FalseSelf_BellSection.new(mixerA.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { bellSection.isLoaded } != true }, { 0.001.wait; });

      mainTrumpet = FalseSelf_MainTrumpet.new(mixerA.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { mainTrumpet.isLoaded } != true }, { 0.001.wait; });
      mainTrumpetInput = IM_HardwareIn.new(0, mainTrumpet.inBus, group, 'addToHead');
      while({ try { mainTrumpetInput.isLoaded } != true }, { 0.001.wait; });

      melodySynth = FalseSelf_MelodySynth.new(mixerB.chanStereo(0), group, \addToHead);
      while({ try { melodySynth.isLoaded } != true }, { 0.001.wait; });

      bassSection = FalseSelf_BassSection.new(mixerB.chanStereo(1), relGroup: group, addAction: \addToHead,
        moogDeviceName: "iConnectAudio4+", moogPortName: "DIN");
      while({ try { bassSection.isLoaded } != true }, { 0.001.wait; });

      drums = FalseSelf_Kick.new(mixerB.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { drums.isLoaded } != true }, { 0.001.wait; });



      modular = IM_Mixer_1Ch.new(mixerC.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { modular.isLoaded } != true }, { 0.001.wait; });
      modularInput = IM_HardwareIn.new(2, modular.chanMono, group, \addToHead);
      while({ try { modularInput.isLoaded } != true }, { 0.001.wait; });


      server.sync;

      this.prMakeModularRoutine;
      this.prSetInitialParameters;

      isLoaded = true;
    }
  }

  prMakeModularRoutine {
    modularRoutine = r {
      {
        bassSection.moog.assignableOut.triggerEnvelopeOneShot;
        ((1/clock.tempo)/4).wait;
      }.loop;
    };
  }

  prSetInitialParameters {
    this.prSetInitialMixerLevels;
    this.prSetAssignableOutParameters;
  }

  prSetInitialMixerLevels {
    // fake guitar:
    mixerA.setVol(0, -3);
    mixerA.setSendVol(0, 2, 0);
    // bells:
    mixerA.setVol(1, -6);
    // trumpet:
    mixerA.setVol(2, -25);

    // melodySynth:
    mixerB.setVol(0, -24);
    // basses:
    mixerB.setVol(1, -6);
    bassSection.satur.mixer.setVol(-inf);
    bassSection.feedback.mixer.setVol(-inf);
    bassSection.moog.mixer.setVol(-inf);
    // drums:
    mixerB.setVol(2, -inf);

    // modular:
    mixerC.setVol(0, -inf);

  }

  prSetAssignableOutParameters {
    bassSection.moog.assignableOut.setStaticValue(0);
    bassSection.moog.assignableOut.setAttackTime(0);
    bassSection.moog.assignableOut.setSustainTime(0);
    bassSection.moog.assignableOut.setReleaseTime((1/clock.tempo)/4);
    bassSection.moog.assignableOut.triggerEnvelopeOneShot;
  }

  //////// public functions:

  free {

  }

  playModularRoutine { modularRoutine.play; }
  stopModularRoutine { modularRoutine.stop.reset; }

}