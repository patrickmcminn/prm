/*
Saturday, April 22nd, 2017
Light_Delays.sc
prm
*/

Light_Delays : IM_Processor {

  var server, <isLoaded;
  var <mainSplitter, <auxSplitter;
  var <filterDelay1, <filterDelay2, <filterDelay3;
  var <delayLineInput, <delayLine1, <delayLine2, <delayLine3;
  var <normalDelay1, <normalDelay2;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      filterDelay1 = SimpleDelay.newStereo(mixer.chanStereo(0), 0.6, 0.65, 1, relGroup: group, addAction: \addToHead);
      while({ try { filterDelay1.isLoaded } != true }, { 0.001.wait; });
      filterDelay2 = SimpleDelay.newStereo(mixer.chanStereo(0), 0.8, 0.56, 1, relGroup: group, addAction: \addToHead);
      while({ try { filterDelay2.isLoaded } != true }, { 0.001.wait; });
      filterDelay3 = SimpleDelay.newStereo(mixer.chanStereo(0), 1.2, 0.67, 1.2, relGroup: group, addAction: \addToHead);
      while({ try { filterDelay3.isLoaded } != true }, { 0.001.wait; });

      delayLine1 = SimpleDelay.newStereo(mixer.chanStereo(0), 0.6, 0.21, 0.6,
        filterDelay1.inBus, filterDelay2.inBus, filterDelay3.inBus,
        relGroup: group, addAction: \addToHead);
      while({ try { delayLine1.isLoaded } != true }, { 0.001.wait; });
      delayLine2 = SimpleDelay.newStereo(mixer.chanStereo(0), 0.2, 0.46, 0.5,
        filterDelay1.inBus, filterDelay2.inBus, filterDelay3.inBus,
        relGroup: group, addAction: \addToHead);
      while({ try { delayLine2.isLoaded } != true }, { 0.001.wait; });
      delayLine3 = SimpleDelay.newStereo(mixer.chanStereo(0), 1, 0.24, 1,
        filterDelay1.inBus, filterDelay2.inBus, filterDelay3.inBus,
        relGroup: group, addAction: \addToHead);
      while({ try { delayLine3.isLoaded } != true }, { 0.001.wait; });

      auxSplitter = Splitter.newStereo(3, [delayLine1.inBus, delayLine2.inBus, delayLine3.inBus],
        relGroup: group, addAction: \addToHead);
      while({ try { auxSplitter.isLoaded } != true }, { 0.001.wait; });

      delayLineInput = SimpleDelay.newStereo(auxSplitter.inBus, 3.2, 0.6, 4, relGroup: group, addAction: \addToHead);
      while({ try { delayLineInput.isLoaded } != true }, { 0.001.wait; });

      normalDelay1 = SimpleDelay.newStereo(mixer.chanStereo(0), 1.6, 0.69, 1,
        auxSplitter.inBus, filterDelay1.inBus, filterDelay2.inBus, filterDelay3.inBus,
        relGroup: group, addAction: \addToHead);
      while({ try { normalDelay1.isLoaded } != true }, { 0.001.wait; });
      normalDelay2 = SimpleDelay.newStereo(mixer.chanStereo(0), 3.2, 0.69, 3.5,
        auxSplitter.inBus, filterDelay1.inBus, filterDelay2.inBus, filterDelay3.inBus,
        relGroup: group, addAction: \addToHead);
      while({ try { normalDelay2.isLoaded } != true }, { 0.001.wait; });

      mainSplitter = Splitter.newStereo(6,
        [normalDelay1.inBus, normalDelay2.inBus, delayLineInput.inBus,
          filterDelay1.inBus, filterDelay2.inBus, filterDelay3.inBus],
        relGroup: group, addAction: \addToHead);
      while({ try { mainSplitter.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitializeParamters;

      isLoaded = true;
    }
  }

  prInitializeParamters {
    mixer.setPreVol(-9);

    // filter delay 1:
    filterDelay1.setMix(1);
    filterDelay1.mixer.setPanBal(-1);
    filterDelay1.setFilterType('bandPass');
    filterDelay1.setFilterCutoff(18000);
    filterDelay1.setFilterRQ(1/9);

    // filter delay 2:
    filterDelay2.setMix(1);
    filterDelay2.setFilterType('bandPass');
    filterDelay2.setFilterCutoff(3320);
    filterDelay2.setFilterRQ(1/3);

    // filter delay 3:
    filterDelay3.setMix(1);
    filterDelay3.mixer.setPanBal(1);
    filterDelay3.setFilterType('bandPass');
    filterDelay3.setFilterCutoff(75);
    filterDelay3.setFilterRQ(1/7.25);

    // delay line 1:
    delayLine1.setMix(1);
    delayLine1.mixer.setPanBal(-1);
    delayLine1.mixer.setSendVol(0, -6);
    delayLine1.mixer.setSendVol(1, -6);
    delayLine1.mixer.setSendVol(2, -6);
    delayLine1.setFilterType('bandPass');
    delayLine1.setFilterCutoff(704);
    delayLine1.setFilterRQ(1/4);

    // delay line 2:
    delayLine2.setMix(1);
    delayLine2.mixer.setSendVol(0, -6);
    delayLine2.mixer.setSendVol(1, -6);
    delayLine2.mixer.setSendVol(2, -6);
    delayLine2.setFilterType('bandPass');
    delayLine2.setFilterCutoff(449);
    delayLine2.setFilterRQ(1/4);

    // delay line 3:
    delayLine3.setMix(1);
    delayLine3.mixer.setSendVol(0, -6);
    delayLine3.mixer.setSendVol(1, -6);
    delayLine3.mixer.setSendVol(2, -6);
    delayLine3.mixer.setPanBal(1);
    delayLine3.setFilterType('bandPass');
    delayLine3.setFilterCutoff(918);
    delayLine3.setFilterRQ(1/4);

    // delay line input:
    delayLineInput.setMix(1);
    delayLineInput.setFilterType('bandPass');
    delayLineInput.setFilterCutoff(1000);
    delayLineInput.setFilterRQ(1/4);

    //// simple delays:
    // 1:
    normalDelay1.setMix(1);
    normalDelay1.mixer.setVol(-3);
    normalDelay1.mixer.setPanBal(-1);
    normalDelay1.mixer.setSendVol(0, -6);
    normalDelay1.mixer.setSendVol(1, -17);
    normalDelay1.mixer.setSendVol(2, -17);
    normalDelay1.mixer.setSendVol(2, -17);

    // 2:
    normalDelay2.setMix(1);
    normalDelay2.mixer.setVol(-3);
    normalDelay2.mixer.setPanBal(1);
    normalDelay2.mixer.setSendVol(0, -6);
    normalDelay2.mixer.setSendVol(1, -17);
    normalDelay2.mixer.setSendVol(2, -17);
    normalDelay2.mixer.setSendVol(2, -17);

  }

  //////// public functions:

  free {
    mainSplitter.free; auxSplitter.free;
    filterDelay1.free; filterDelay2.free; filterDelay3.free;
    delayLineInput.free; delayLine1.free; delayLine2.free; delayLine3.free;
    normalDelay1.free; normalDelay2.free;
    this.freeProcessor;
    isLoaded = false;
  }

  inBus { ^mainSplitter.inBus }

}