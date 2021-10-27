/*
Wednesday, April 18th 2018
Meaning_Delay.sc
prm

everything was pregnant with meaning
*/

Meaning_Delay : IM_Module {

  var <isLoaded;
  var server;

  var <delay1, <delay2, <delay3, <granulator, splitter;

  *new { | outBus = 0, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay2.new(mixer.chanStereo(0),
        relGroup: group, addAction: \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      delay3 = SimpleDelay.newStereo(granulator.inBus, maxDelayTime: 0.5,
        relGroup: group, addAction: \addToHead);
      while({ try { delay3.isLoaded } != true }, { 0.001.wait; });

      delay2 = SimpleDelay.newStereo(granulator.inBus, maxDelayTime: 0.5,
        relGroup: group, addAction: \addToHead);
      while({ try { delay2.isLoaded } != true }, { 0.001.wait; });

      delay1 = SimpleDelay.newStereo(granulator.inBus, maxDelayTime: 0.5,
        relGroup: group, addAction: \addToHead);
      while({ try { delay1.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newMono(3, [delay1.inBus, delay2.inBus, delay3.inBus], relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitializeParameters;

      server.sync;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    // granulator:
    granulator.setMix(0.65);
    granulator.setDelayLevel(0.35);
    granulator.setFeedback(0.3);

    // delay 1:
    delay1.setMix(0.75);
    delay1.setDelayTime(0.32);
    delay1.setFeedback(0.21);
    delay1.setFilterType('bandPass');
    delay1.setFilterCutoff(11200);
    delay1.setFilterRQ(0.129);
    delay1.mixer.setPanBal(-1);

    // delay 2:
    delay2.setMix(0.75);
    delay2.setDelayTime(0.107);
    delay2.setFeedback(0.46);
    delay2.setFilterType('bandPass');
    delay2.setFilterCutoff(449);
    delay2.setFilterRQ(0.25);

    // delay 3:
    delay3.setMix(0.75);
    delay3.setDelayTime(0.5);
    delay3.setFeedback(0.24);
    delay3.setFilterType('bandPass');
    delay3.setFilterCutoff(918);
    delay3.setFilterRQ(0.25);
    delay3.mixer.setPanBal(1);

  }

  //////// public functions:

  free {
    granulator.free;
    delay1.free;
    delay2.free;
    delay3.free;
    splitter.free;
    this.freeModule;
    isLoaded = false;
  }

  inBus { ^splitter.inBus }

}