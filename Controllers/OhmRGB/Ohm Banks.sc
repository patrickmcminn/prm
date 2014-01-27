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

  addControlBank { | type = \leftSliders |
    switch(type,
      { \leftSliders }, { leftSlidersBankArray.add(Array.fill(4, { nil; });); },
      { \rightSliders }, { rightSlidersBankArray.add(Array.fill(4, { nil; });); },
      { \leftKnobs }, { leftKnobsBankArray.add(Array.fill(12, { nil; });) },
      { \rightKnobs }, { rightKnobsBankArray.add(Array.fill(4, { nil; });) },
      { \crossfader }, { crossfaderBankArray.add( { nil } ); }
    );
  }

  activeControlBank { | type = \leftSliders |
    switch(type,
      { \leftSliders }, { ^activeLeftSlidersBank },
      { \rightSliders }, { ^activeRightSlidersBank },
      { \leftKnobs }, { ^activeLeftKnobsBank },
      { \rightKnobs }, { ^activeRightKnobsBank },
      { \crossfader }, { ^activeCrossfaderBank }
    );
  }

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

  setActiveRightKnobsBank { }

  setActiveCrossfaderBank { }

  setActiveControlBank { | type = \leftSliders, bank = 0 |
    switch(type,
      { \leftSliders }, { this.setActiveLeftSlidersBank(bank); },
      { \rightSliders }, { activeRightSlidersBank = bank },
      { \leftKnobs }, { activeLeftKnobsBank = bank },
      { \rightKnobs }, { activeRightKnobsBank = bank },
      { \crossfader }, { activeCrossfaderBank = bank }
    );
  }

}