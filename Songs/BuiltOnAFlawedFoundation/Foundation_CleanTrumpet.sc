/*
Monday, June 19th 2017
Foundation_IntroTrumpet.sc
prm
*/

Foundation_CleanTrumpet : IM_Module {

  var server, <isLoaded;
  var <trumpet, <eq, <splitter, <delay1, <delay2, <delay3;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(4, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      delay1 = SimpleDelay.newStereo(mixer.chanStereo(1), 2.5, 0.2, 2.5, relGroup: group, addAction: \addToHead);
      while({ try { delay1.isLoaded } != true }, { 0.001.wait; });

      delay2 = SimpleDelay.newStereo(mixer.chanStereo(1), 3.75, 0.2, 3.75, relGroup: group, addAction: \addToHead);
      while({ try { delay2.isLoaded } != true }, { 0.001.wait; });

      delay3 = SimpleDelay.newStereo(mixer.chanStereo(1), 3.4375, 0.2, 3.5, relGroup: group, addAction: \addToHead);
      while({ try { delay3.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newStereo(4, [mixer.chanStereo(0), delay1.inBus, delay2.inBus, delay3.inBus],
        relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newMono(splitter.inBus, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      trumpet = IM_Mixer_1Ch.new(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

      server.sync;
      this.prInitializeParameters;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    // eq:
    eq.setHighPassCutoff(250);
    eq.setLowPassCutoff(3910);

    // delays:
    delay1.setMix(1);
    delay1.setFilterType('highPass');
    delay1.setFilterCutoff(750);
    delay2.setMix(1);
    delay1.setFilterType('highPass');
    delay1.setFilterCutoff(450);
    delay3.setMix(1);
    delay1.setFilterType('highPass');
    delay1.setFilterCutoff(1000);

    // mixer:
    mixer.setVol(1, -3);
    mixer.setPanBal(1, -0.25);
    mixer.setVol(2, -3);
    mixer.setPanBal(2, 0.8);
    mixer.setVol(3, -3);
    mixer.setPanBal(3, -0.8);
  }

  //////// public functions:

  free {
    delay3.free;
    delay2.free;
    delay1.free;
    splitter.free;
    eq.free;
    this.freeModule;
    isLoaded = false;
  }

  inBus { ^trumpet.inBus }

}