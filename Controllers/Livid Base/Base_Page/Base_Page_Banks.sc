/*
Wednesday, June 4th 2014
prm
Base_Page_Banks.sc
*/

+ Base_Page {

  //////// Note Banks:
  /*
  Array Slot 0: Note On Function
  Array Slot 1: Note Off Function
  Array Slot 2: Color
  Array Slot 3: Animation Function
  Array Slot 4: Monitor Routine
  */

  // Grid Bank:
  /*
  Array Slot 0: Note On Func
  Array Slot 1: Note Off Func
  Array Slot 2: Pressure Func
  Array Slot 3: Color Func
  Array Slot 4: Animation Func
  Array Slot 5: Monitor Routine
  */

  //////// Control Banks:
  /*
  Array Slot 0: Control Function
  Array Slot 1: Fader Value
  Array Slot 2: Mode
  Array Slot 3: Animation Function
  Array Slot 4: Monitor Routine
  */

  prMakeBanks { | numBanks = 1 |
    gridBankArray = Array.new;
    this.addGridBanks(numBanks);
    controlButtonsBankArray = Array.new;
    this.addControlButtonsBanks(numBanks);
    touchButtonsBankArray = Array.new;
    this.addTouchButtonsBanks(numBanks);
    fadersBankArray = Array.new;
    this.addFadersBanks(numBanks);

    activeGridBnk = 0;
    activeControlButtonsBnk = 0;
    activeTouchButtonsBnk = 0;
    activeFadersBnk = 0;
  }

  addGridBanks { | numBanks = 1 |
    numBanks.do({
      gridBankArray = gridBankArray.add(Array.fill2D(32, 6, nil));
      gridBankArray[gridBankArray.size -1].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = { };
        item[3] = \off;
        item[4] = TaskProxy.new;
        item[5] = r { };
      });
    });
  }

  addControlButtonsBanks { | numBanks = 1 |
    numBanks.do({
      controlButtonsBankArray = controlButtonsBankArray.add(Array.newClear(5));
      controlButtonsBankArray[controlButtonsBankArray.size - 1][0] = Array.fill(8, { });
      controlButtonsBankArray[controlButtonsBankArray.size - 1][1] = Array.fill(8, { });
      controlButtonsBankArray[controlButtonsBankArray.size - 1][2] = Array.fill(16, \off);
      controlButtonsBankArray[controlButtonsBankArray.size - 1][3] = Array.fill(8, { TaskProxy.new });
      controlButtonsBankArray[controlButtonsBankArray.size - 1][4] = Array.fill(8, r { });
    });
  }

  addTouchButtonsBanks { | numBanks = 1 |
    numBanks.do({
      touchButtonsBankArray = touchButtonsBankArray.add(Array.newClear(5));
      touchButtonsBankArray[touchButtonsBankArray.size - 1][0] = Array.fill(8, { });
      touchButtonsBankArray[touchButtonsBankArray.size - 1][1] = Array.fill(8, { });
      touchButtonsBankArray[touchButtonsBankArray.size - 1][2] = Array.fill(16, \off);
      touchButtonsBankArray[touchButtonsBankArray.size - 1][3] = Array.fill(8, { TaskProxy.new });
      touchButtonsBankArray[touchButtonsBankArray.size - 1][4] = Array.fill(8, r{ });
    });
  }

  addFadersBanks { | numBanks = 1 |
    numBanks.do({
      fadersBankArray = fadersBankArray.add(Array.fill2D(9, 5, nil));
      fadersBankArray[fadersBankArray.size - 1].do({ | item, index |
        item[0] = { };
        item[1] = 0;
        item[2] = 'redFill';
        item[3] = TaskProxy.new;
        item[4] = r { };
      });
    });
  }

  //////// Setting banks:

  setActiveGridBank { | bank = 0 |
    32.do({ | index | gridBankArray[activeGridBnk][index][5].stop; });
    activeGridBnk = bank;
    32.do({ | index |
      this.setNoteOnFunc(index + 36, gridBankArray[activeGridBnk][index][0]);
      this.setNoteOffFunc(index + 36, gridBankArray[activeGridBnk][index][1]);
      this.setControlFunc(index + 36, gridBankArray[activeGridBnk][index][2]);
      this.turnButtonColor(index + 36, gridBankArray[activeGridBnk][index][3]);
      gridBankArray[activeGridBnk][index][5].reset.play;
    });
  }

  setActiveControlButtonsBank { | bank = 0 |
    8.do({ | index | controlButtonsBankArray[activeControlButtonsBnk][4][index].stop; });
    activeControlButtonsBnk = bank;
    8.do({ | index |
      this.setNoteOnFunc(index + 18, controlButtonsBankArray[activeControlButtonsBnk][0][index]);
      this.setNoteOffFunc(index + 18, controlButtonsBankArray[activeControlButtonsBnk][1][index]);
      controlButtonsBankArray[activeControlButtonsBnk][4][index].reset.play;
    });
    16.do({ | index |
      this.turnButtonColor(index + 18, controlButtonsBankArray[activeControlButtonsBnk][2][index]);
    });
  }

  setActiveTouchButtonsBank { | bank = 0 |
    8.do({ | index | touchButtonsBankArray[activeTouchButtonsBnk][4][index].stop; });
    activeTouchButtonsBnk = bank;
    8.do({ | index |
      this.setNoteOnFunc(index + 10, touchButtonsBankArray[activeTouchButtonsBnk][0][index]);
      this.setNoteOffFunc(index + 10, touchButtonsBankArray[activeTouchButtonsBnk][1][index]);
      touchButtonsBankArray[activeTouchButtonsBnk][4][index].reset.play;
    });
    8.do({ | index |
      this.turnButtonColor(index + 10, touchButtonsBankArray[activeTouchButtonsBnk][2][index]);
    });
    8.do({ | index |
      this.turnButtonColor(index + 68, touchButtonsBankArray[activeTouchButtonsBnk][2][index + 8]);
    });
  }

  setActiveFadersBank { | bank = 0 |
    9.do({ | index | fadersBankArray[activeFadersBnk][index][4].stop; });
    activeFadersBnk = bank;
    9.do({ | index |
      this.setControlFunc(index + 1, fadersBankArray[activeFadersBnk][index][0]);
      this.setFaderValue(index + 1, fadersBankArray[activeFadersBnk][index][1]);
      this.setFaderMode(index + 1, fadersBankArray[activeFadersBnk][index][2]);
      fadersBankArray[activeFadersBnk][index][4].reset.play;
    });
  }

  numBanks { | type = 'grid' |
    switch(type,
      { 'grid' }, { ^gridBankArray.size; },
      { 'controlButtons' }, { ^controlButtonsBankArray.size; },
      { 'touchButtons' }, { ^touchButtonsBankArray.size; },
      { 'faders' }, { ^fadersBankArray.size; }
    );
  }

  stopActiveBankMonitorRoutines {
    32.do({ | index | gridBankArray[activeGridBnk][index][5].stop; });
    8.do({ | index | controlButtonsBankArray[activeControlButtonsBnk][4][index].stop; });
    8.do({ | index | touchButtonsBankArray[activeTouchButtonsBnk][4][index].stop; });
    9.do({ | index | fadersBankArray[activeFadersBnk][index][4].stop; });
  }

  startActiveBankMonitorRoutines {
    32.do({ | index | gridBankArray[activeGridBnk][index][5].reset.play; });
    8.do({ | index | controlButtonsBankArray[activeControlButtonsBnk][4][index].reset.play; });
    8.do({ | index | touchButtonsBankArray[activeTouchButtonsBnk][4][index].reset.play; });
    9.do({ | index | fadersBankArray[activeFadersBnk][index][4].reset.play; });
  }

}

+ Base_Page {

  //////// convenience functions for getting number of banks:

  numGridbanks { this.numBanks('grid'); }
  numControlButtonsbanks { this.numBanks('controlButtons'); }
  numTouchButtonsbanks { this.numBanks('touchButtons'); }
  numFaderbanks { this.numBanks('faders'); }

}