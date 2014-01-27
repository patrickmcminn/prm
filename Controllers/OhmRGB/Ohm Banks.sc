+ OhmRGB {

   ///////// Banks:

  prMakeControlBanks { | numBanks = 1 |
    leftSlidersBankArray = Array.fill(numBanks, { Array.fill(4, { nil; }); });
    rightSlidersBankArray = Array.fill(numBanks, { Array.fill(4, { nil; }); });
    leftKnobsBankArray = Array.fill(numBanks, { Array.fill(12, { nil; }); });
    rightKnobsBankArray = Array.fill(numBanks, { Array.fill(4, { nil; }); });
    crossfaderBankArray = Array.fill(numBanks, { { nil }; });

    activeLeftSlidersBank = 0;
    activeRightSlidersBank = 0;
    activeLeftKnobsBank = 0;
    activeRightKnobsBank = 0;
    activeCrossfaderBank = 0;
  }

  //////// Public Bank Functions:

  addControlBanks { | num = 1, type = \leftSliders |
    switch(type,
      { \leftSliders }, { num.do({ leftSlidersBankArray.add(Array.fill(4, { nil; });); }); },
      { \rightSliders }, { num.do({ rightSlidersBankArray.add(Array.fill(4, { nil; });); }) },
      { \leftKnobs }, { num.do({ leftKnobsBankArray.add(Array.fill(12, { nil; });) }) },
      { \rightKnobs }, { num.do({ rightKnobsBankArray.add(Array.fill(4, { nil; });) }) },
      { \crossfader }, { num.do({ crossfaderBankArray.add( { nil } ); }) }
    );
  }

  addLeftSlidersBanks { | num = 1 | this.addControlBanks(num, 'leftSliders'); }

  addRightSlidersBanks { | num = 1 | this.addControlBanks(num, 'leftSliders'); }

  addLeftKnobsBanks { | num = 1 | this.addControlBanks(num, 'leftKnobs'); }

  addRightKnobsBanks { | num = 1 | this.addControlBanks(num, 'rightKnobs'); }

  addCrossfaderBanks { | num = 1 | this.addControlBanks(num, 'crossfader'); }

  numControlBanks { | type = \leftSliders |
    switch(type,
      { \leftSliders }, { ^leftSlidersBankArray.size; },
      { \rightSliders }, { ^rightSlidersBankArray.size; },
      { \leftKnobs }, { ^leftKnobsBankArray.size; },
      { \rightKnobs }, { ^rightKnobsBankArray.size },
      { \crossfader }, { ^crossfaderBankArray.size }
    );
  }

  numLeftSlidersBanks { this.numControlBanks('leftSliders') }

  numRightSlidersBanks { this.numControlBanks('RightSliders') }

  numLeftKnobsBanks { this.numControlBanks('leftKnobs') }

  numRightKnobsBanks { this.numControlBanks('rightKnobs') }

  numCrossfaderBanks { this.numControlBanks('crossfader') }

  activeControlBank { | type = \leftSliders |
    switch(type,
      { \leftSliders }, { ^activeLeftSlidersBank },
      { \rightSliders }, { ^activeRightSlidersBank },
      { \leftKnobs }, { ^activeLeftKnobsBank },
      { \rightKnobs }, { ^activeRightKnobsBank },
      { \crossfader }, { ^activeCrossfaderBank }
    );
  }

  activeLeftSlidersBank { this.activeControlBank('leftSliders'); }

  activeRightSlidersBank { this.activeControlBank('rightSliders'); }

  activeLeftKnobsBank { this.activeControlBank('leftKnobs'); }

  activeRightKnobsBank { this.activeControlBank('rightKnobs'); }

  activeCrossfaderBank { this.activeControlBank('crossfader'); }

  setActiveLeftSlidersBank { | bank = 0 |
    var sliderArray = [23, 22, 15, 14];
    activeLeftSlidersBank = bank;
    sliderArray.do({ | item, index |
      this.setCCFunc(item, leftSlidersBankArray[activeLeftSlidersBank][index]);
    });
  }

  setActiveRightSlidersBank { | bank = 0 |
    var sliderArray = [5, 7, 6, 4];
    activeRightSlidersBank = bank;
    sliderArray.do({ | item, index |
      this.setCCFunc(item, rightSlidersBankArray[activeRightSlidersBank][index]);
    });
  }

  setActiveLeftKnobsBank { | bank = 0 |
    var knobArray = [17, 19, 21, 16, 18, 20, 9, 11, 13, 8, 10, 12];
    activeLeftKnobsBank = bank;
    knobArray.do({ | item, index |
      this.setCCFunc(item, leftKnobsBankArray[activeLeftKnobsBank][index]);
    });
  }

  setActiveRightKnobsBank { | bank = 0 |
    var knobArray = [3, 1, 0, 2];
    activeRightKnobsBank = bank;
    knobArray.do({ | item, index |
      this.setCCFunc(item, rightKnobsBankArray[activeRightKnobsBank][index]);
    });
  }

  setActiveCrossfaderBank { | bank = 0 |
    activeCrossfaderBank = bank;
    this.setCCFunc(24, crossfaderBankArray[activeCrossfaderBank]);

  }

  setActiveControlBank { | type = \leftSliders, bank = 0 |
    switch(type,
      { \leftSliders }, { this.setActiveLeftSlidersBank(bank); },
      { \rightSliders }, { this.setActiveRightSlidersBank(bank); },
      { \leftKnobs }, { this.setActiveLeftKnobsBank(bank); },
      { \rightKnobs }, { this.setActiveRightKnobsBank(bank); },
      { \crossfader }, { this.setActiveCrossfaderBank(bank); }
    );
  }

}