/*
Saturday, April 15th 2017
FalseSelf.sc
prm
*/

FalseSelf : Song {

  var <server, <isLoaded;

  var <clock;

  var <fakeGuitar, <bellSection, <melodySynth;

  *new { | mixAOutBus, mixBOutBus, mixCOutBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(mixAOutBus, 8, mixBOutBus, 8, mixCOutBus, 8, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixerC.isLoaded } != true }, { 0.001.wait; });

      clock = TempoClock.new;
      server.sync;
      clock.tempo = 160/60;

      fakeGuitar = FalseSelf_FakeGuitar.new(mixerA.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { fakeGuitar.isLoaded } != true }, { 0.001.wait; });

      bellSection = FalseSelf_BellSection.new(mixerA.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { bellSection.isLoaded } != true }, { 0.001.wait; });

      melodySynth = FalseSelf_MelodySynth.new(mixerB.chanStereo(0), group, \addToHead);
      while({ try { melodySynth.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    }
  }

}