+ OhmRGB {

   ///////// Banks:

  //////// Note Banks:

  /*
  Array Slot 0: Note On Function
  Array Slot 1: Note Off Function
  Array Slot 2: Color
  Array Slot 3: Animation Function (not implemented yet)
  */

  prMakeNoteBanks { | numBanks = 1 |
    this.prMakeGridBanks(numBanks);
    this.prMakeLeftButtonBanks(numBanks);
    this.prMakeRightButtonBanks(numBanks);
    this.prMakeCrossfaderButtonBanks(numBanks);
    this.prMakeControlButtonBanks(numBanks);
  }

  prMakeGridBanks { | numBanks = 1 |
    gridBankArray = Array.new;
    this.addGridBanks(numBanks);
  }

  /*
  prMakeLeftButtonBanks { | numBanks = 1 |
    leftButtonsBankArray = Array.fill2D(4, 4, nil);
    leftButtonsBankArray.do({ | item, index |
      item[0] = { (midiInPort.device + "noteOn" + index + "has no function assigned").postln; };
      item[1] = { };
      item[2] = \off;
      item[3] = nil;
    });
  }

  prMakeRightButtonBanks { | numBanks = 1 |
    rightButtonsBankArray = Array.fill2D(4, 4, nil);
    rightButtonsBankArray.do({ | item, index |
      item[0] = { (midiInPort.device + "noteOn" + index + "has no function assigned").postln; };
      item[1] = { };
      item[2] = \off;
      item[3] = nil;
    });
  }

  prMakeCrossfaderButtonsBanks { | numBanks = 1 |
    crossfaderButtonsBankArray = Array.fill2D(2, 4, nil);
    crossfaderButtonsBankArray.do({ | item, index |
      item[0] = { (midiInPort.device + "noteOn" + index + "has no function assigned").postln; };
      item[1] = { };
      item[2] = \off;
      item[3] = nil;
    });
  }

  prMakeControlButtonsBanks { | numBanks = 1 |
    controlButtonsBankArray = Array.fill2D(7, 4, nil);
    controlButtonsBankArray.do({ | item, index |
      item[0] = { (midiInPort.device + "noteOn" + index + "has no function assigned").postln; };
      item[1] = { };
      item[2] = \off;
      item[3] = nil;
    });
  }
  */

  //////// Control Banks:

  prMakeControlBanks { | numBanks = 1 |
    leftSlidersBankArray = Array.new;
    rightSlidersBankArray = Array.new;
    leftKnobsBankArray = Array.new;
    rightKnobsBankArray = Array.new;
    crossfaderBankArray = Array.new;

    this.addLeftSlidersBanks(numBanks);
    this.addRightSlidersBanks(numBanks);
    this.addLeftKnobsBanks(numBanks);
    this.addRightKnobsBanks(numBanks);
    this.addCrossfaderBanks(numBanks);

    activeLeftSlidersBank = 0;
    activeRightSlidersBank = 0;
    activeLeftKnobsBank = 0;
    activeRightKnobsBank = 0;
    activeCrossfaderBank = 0;
  }

  //////// Public Bank Functions:


  //////// Public Note Bank Functions:

  /*
  addNoteBanks { | num = 1, type = 'grid' |
    switch(type,
      { 'grid' }, { num.do({
  */

  addGridBanks { | num = 1 |
    var newGridBank;
    num.do({
      gridBankArray = gridBankArray.add(Array.fill2D(64, 4, nil));
      gridBankArray[(gridBankArray.size)-1].do({ | item, index |
        item[0] = { (midiInPort.device + "noteOn" + index + "has no function assigned").postln; };
        item[1] = { };
        item[2] = \off;
        item[3] = nil;
      });
    });
  }

  //////// Public Control Bank Functions:

  addControlBanks { | num = 1, type = \leftSliders |
    switch(type,
      { \leftSliders }, { num.do({ leftSlidersBankArray = leftSlidersBankArray.add(Array.fill(4, { nil; });); }); },
      { \rightSliders }, { num.do({ rightSlidersBankArray = rightSlidersBankArray.add(Array.fill(4, { nil; });); }) },
      { \leftKnobs }, { num.do({ leftKnobsBankArray = leftKnobsBankArray.add(Array.fill(12, { nil; });) }) },
      { \rightKnobs }, { num.do({ rightKnobsBankArray = rightKnobsBankArray.add(Array.fill(4, { nil; });) }) },
      { \crossfader }, { num.do({ crossfaderBankArray = crossfaderBankArray.add( { nil } ); }) }
    );
  }

  addLeftSlidersBanks { | num = 1 | this.addControlBanks(num, 'leftSliders'); }

  addRightSlidersBanks { | num = 1 | this.addControlBanks(num, 'rightSliders'); }

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