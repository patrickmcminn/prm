+ OhmRGB_Page {

   ///////// Banks:

  //////// Note Banks:

  /*
  Array Slot 0: Note On Function
  Array Slot 1: Note Off Function
  Array Slot 2: Color
  Array Slot 3: Animation Function
  Array Slot 4: Monitor Routine
  */

  prMakeNoteBanks { | numBanks = 1 |
    this.prMakeGridBanks(numBanks);
    this.prMakeLeftButtonsBanks(numBanks);
    this.prMakeRightButtonsBanks(numBanks);
    this.prMakeCrossfaderButtonsBanks(numBanks);
    this.prMakeControlButtonsBanks(numBanks);

    activeGridBnk = 0;
    activeLeftButtonsBnk = 0;
    activeRightButtonsBnk = 0;
    activeCrossfaderButtonsBnk = 0;
    activeControlButtonsBnk = 0;
  }

  prMakeGridBanks { | numBanks = 1 |
    gridBankArray = Array.new;
    this.addGridBanks(numBanks);
  }

  prMakeLeftButtonsBanks { | numBanks = 1 |
    leftButtonsBankArray = Array.new;
    this.addLeftButtonsBanks(numBanks);
  }

  prMakeRightButtonsBanks { | numBanks = 1 |
    rightButtonsBankArray = Array.new;
    this.addRightButtonsBanks(numBanks);
  }

  prMakeCrossfaderButtonsBanks { | numBanks = 1 |
    crossfaderButtonsBankArray = Array.new;
    this.addCrossfaderButtonsBanks(numBanks);
  }

  prMakeControlButtonsBanks { | numBanks = 1 |
    controlButtonsBankArray = Array.new;
    this.addControlButtonsBanks(numBanks);
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

    activeLeftSlidersBnk = 0;
    activeRightSlidersBnk = 0;
    activeLeftKnobsBnk = 0;
    activeRightKnobsBnk = 0;
    activeCrossfaderBnk = 0;
  }

  //////// Public Bank Functions:


  //// Public Note Bank Functions:

  addGridBanks { | num = 1 |
    num.do({
      gridBankArray = gridBankArray.add(Array.fill2D(64, 5, nil));
      gridBankArray[(gridBankArray.size-1)].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = \off;
        item[3] = TaskProxy.new;
        item[4] = r { };
      });
    });
  }

  addLeftButtonsBanks { | num = 1 |
    num.do({
      leftButtonsBankArray = leftButtonsBankArray.add(Array.fill2D(4, 5, nil));
      leftButtonsBankArray[(leftButtonsBankArray.size-1)].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = \off;
        item[3] = TaskProxy.new;
        item[4] = r { };
      });
    });
  }

  addRightButtonsBanks { | num = 1 |
    num.do({
      rightButtonsBankArray = rightButtonsBankArray.add(Array.fill2D(4, 5, nil));
      rightButtonsBankArray[(rightButtonsBankArray.size-1)].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = \off;
        item[3] = TaskProxy.new;
        item[4] = r { };
      });
    });
  }

  addCrossfaderButtonsBanks { | num = 1 |
    num.do({
      crossfaderButtonsBankArray = crossfaderButtonsBankArray.add(Array.fill2D(2, 5, nil));
      crossfaderButtonsBankArray[(crossfaderButtonsBankArray.size-1)].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = \off;
        item[3] = TaskProxy.new;
        item[4] = r { };
      });
    });
  }

  addControlButtonsBanks { | num = 1 |
    num.do({
      controlButtonsBankArray = controlButtonsBankArray.add(Array.fill2D(7, 5, nil));
      controlButtonsBankArray[(controlButtonsBankArray.size-1)].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = \off;
        item[3] = TaskProxy.new;
        item[4] = r { };
      });
    });
  }

  addNoteBanks { | numBanks = 1, type = 'grid' |
    switch(type,
      { 'grid' }, { this.addGridBanks(numBanks); },
      { 'leftButtons' }, { this.addLeftButtonsBanks(numBanks); },
      { 'rightButtons' }, { this.addRightButtonsBanks(numBanks); },
      { 'crossfaderButtons' }, { this.addCrossfaderButtonsBanks(numBanks); },
      { 'controlButtons' }, { this.addControlButtonsBanks(numBanks); }
    );
  }

  setActiveGridBank { | bank = 0 |
    activeGridBnk = bank;
    64.do({ | index |
      this.setNoteOnFunc(index, gridBankArray[activeGridBnk][index][0]);
      this.setNoteOffFunc(index, gridBankArray[activeGridBnk][index][1]);
      this.turnColor(index, gridBankArray[activeGridBnk][index][2]);
    });
  }

  setActiveLeftButtonsBank { | bank = 0 |
    var buttonArray = [65, 73, 66, 74];
    activeLeftButtonsBnk = bank;
    buttonArray.do({ | item, index |
      this.setNoteOnFunc(item, leftButtonsBankArray[activeLeftButtonsBnk][index][0]);
      this.setNoteOffFunc(item, leftButtonsBankArray[activeLeftButtonsBnk][index][1]);
      this.turnColor(item, leftButtonsBankArray[activeLeftButtonsBnk][index][2]);
    });
  }

  setActiveRightButtonsBank { | bank = 0 |
    var buttonArray = [67, 75, 68, 76];
    activeRightButtonsBnk = bank;
    buttonArray.do({ | item, index |
      this.setNoteOnFunc(item, rightButtonsBankArray[activeRightButtonsBnk][index][0]);
      this.setNoteOffFunc(item, rightButtonsBankArray[activeRightButtonsBnk][index][1]);
      this.turnColor(item, rightButtonsBankArray[activeRightButtonsBnk][index][2]);
    });
  }

  setActiveCrossfaderButtonsBank { | bank = 0 |
    var buttonArray = [64, 72];
    activeCrossfaderButtonsBnk = bank;
    buttonArray.do({ | item, index |
      this.setNoteOnFunc(item, crossfaderButtonsBankArray[activeCrossfaderButtonsBnk][index][0]);
      this.setNoteOffFunc(item, crossfaderButtonsBankArray[activeCrossfaderButtonsBnk][index][1]);
      this.turnColor(item, crossfaderButtonsBankArray[activeCrossfaderButtonsBnk][index][2]);
    });
  }

  setActiveControlButtonsBank { | bank = 0 |
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    activeControlButtonsBnk = bank;
    buttonArray.do({ | item, index |
      this.setNoteOnFunc(item, controlButtonsBankArray[activeControlButtonsBnk][index][0]);
      this.setNoteOffFunc(item, controlButtonsBankArray[activeControlButtonsBnk][index][1]);
      this.turnColor(item, controlButtonsBankArray[activeControlButtonsBnk][index][2]);
    });
  }

  startActiveBankMonitorRoutines {
    this.startActiveGridBankMonitorRoutines;
    this.startActiveLeftButtonsBankMonitorRoutines;
    this.startActiveRightButtonsBankMonitorRoutines;
    this.startActiveControlButtonsBankMonitorRoutines;
    this.startActiveCrossfaderButtonsBankMonitorRoutines;
    /*
    64.do({ | index | gridBankArray[activeGridBnk][index][4].reset.play; });
    4.do({ | index | leftButtonsBankArray[activeLeftButtonsBnk][index][4].reset.play; });
    4.do({ | index | rightButtonsBankArray[activeRightButtonsBnk][index][4].reset.play; });
    2.do({ | index | crossfaderButtonsBankArray[activeCrossfaderButtonsBnk][index][4].reset.play; });
    7.do({ | index | controlButtonsBankArray[activeControlButtonsBnk][index][4].reset.play; });
    */
  }

  stopActiveBankMonitorRoutines {
    this.stopActiveGridBankMonitorRoutines;
    this.stopActiveLeftButtonsBankMonitorRoutines;
    this.stopActiveRightButtonsBankMonitorRoutines;
    this.stopActiveControlButtonsBankMonitorRoutines;
    this.stopActiveCrossfaderButtonsBankMonitorRoutines;
    /*
    64.do({ | index | gridBankArray[activeGridBnk][index][4].stop; });
    4.do({ | index | leftButtonsBankArray[activeLeftButtonsBnk][index][4].stop; });
    4.do({ | index | rightButtonsBankArray[activeRightButtonsBnk][index][4].stop; });
    2.do({ | index | crossfaderButtonsBankArray[activeCrossfaderButtonsBnk][index][4].stop; });
    7.do({ | index | controlButtonsBankArray[activeControlButtonsBnk][index][4].stop; });
    */
  }

  startActiveGridBankMonitorRoutines {
    64.do({ | index | gridBankArray[activeGridBnk][index][4].reset.play; });
  }
  stopActiveGridBankMonitorRoutines {
    64.do({ | index | gridBankArray[activeGridBnk][index][4].stop; });
  }

  startActiveLeftButtonsBankMonitorRoutines {
    4.do({ | index | leftButtonsBankArray[activeLeftButtonsBnk][index][4].reset.play; });
  }
  stopActiveLeftButtonsBankMonitorRoutines {
    4.do({ | index | leftButtonsBankArray[activeLeftButtonsBnk][index][4].stop; });
  }

  startActiveRightButtonsBankMonitorRoutines {
    4.do({ | index | rightButtonsBankArray[activeRightButtonsBnk][index][4].reset.play; });
  }
  stopActiveRightButtonsBankMonitorRoutines {
    4.do({ | index | rightButtonsBankArray[activeRightButtonsBnk][index][4].stop; });
  }

  startActiveControlButtonsBankMonitorRoutines {
    7.do({ | index | controlButtonsBankArray[activeControlButtonsBnk][index][4].reset.play; });
  }
  stopActiveControlButtonsBankMonitorRoutines {
    7.do({ | index | controlButtonsBankArray[activeControlButtonsBnk][index][4].stop; });
  }

  startActiveCrossfaderButtonsBankMonitorRoutines {
    2.do({ | index | crossfaderButtonsBankArray[activeCrossfaderButtonsBnk][index][4].reset.play; });
  }
  stopActiveCrossfaderButtonsBankMonitorRoutines {
    2.do({ | index | crossfaderButtonsBankArray[activeCrossfaderButtonsBnk][index][4].stop; });
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

  setActiveLeftSlidersBank { | bank = 0 |
    var sliderArray = [23, 22, 15, 14];
    activeLeftSlidersBnk = bank;
    sliderArray.do({ | item, index |
      this.setCCFunc(item, leftSlidersBankArray[activeLeftSlidersBnk][index]);
    });
  }

  setActiveRightSlidersBank { | bank = 0 |
    var sliderArray = [5, 7, 6, 4];
    activeRightSlidersBnk = bank;
    sliderArray.do({ | item, index |
      this.setCCFunc(item, rightSlidersBankArray[activeRightSlidersBnk][index]);
    });
  }

  setActiveLeftKnobsBank { | bank = 0 |
    var knobArray = [17, 19, 21, 16, 18, 20, 9, 11, 13, 8, 10, 12];
    activeLeftKnobsBnk = bank;
    knobArray.do({ | item, index |
      this.setCCFunc(item, leftKnobsBankArray[activeLeftKnobsBnk][index]);
    });
  }

  setActiveRightKnobsBank { | bank = 0 |
    var knobArray = [3, 1, 0, 2];
    activeRightKnobsBnk = bank;
    knobArray.do({ | item, index |
      this.setCCFunc(item, rightKnobsBankArray[activeRightKnobsBnk][index]);
    });
  }

  setActiveCrossfaderBank { | bank = 0 |
    activeCrossfaderBnk = bank;
    this.setCCFunc(24, crossfaderBankArray[activeCrossfaderBnk]);

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