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
  */

  //////// Control Banks:
  /*
  Array Slot 0: Control Function
  Array Slot 1: Mode
  Array Slot 2: Animation Function
  */

  prMakeSubBanks { | numBanks = 1 |
    gridSubBankArray = Array.new;
    this.addGridSubBanks(numBanks);
    controlButtonsSubBankArray = Array.new;
    this.addControlButtonSubBanks(numBanks);
    touchButtonsSubBankArray = Array.new;
    this.addTouchButtonSubBanks(numBanks);
    faderSubBankArray = Array.new;
    this.addFaderSubBanks(numBanks);

    activeGridSubBank = 0;
    activeControlButtonsSubBank = 0;
    activeTouchButtonsSubBank = 0;
    activeFaderSubBank = 0;
  }

  addGridSubBanks { | numBanks = 1 |
    numBanks.do({
      gridSubBankArray = gridSubBankArray.add(Array.fill2D(32, 4, nil));
      gridSubBankArray[gridSubBankArray.size -1].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = \off;
        item[3] = TaskProxy.new;
      });
    });
  }

  addControlButtonSubBanks { | numBanks = 1 |
    numBanks.do({
      controlButtonsSubBankArray = controlButtonsSubBankArray.add(Array.fill2D(8, 4, nil));
      controlButtonsSubBankArray[controlButtonsSubBankArray.size - 1].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = \off;
        item[3] = TaskProxy.new;
      });
    });
  }

  addTouchButtonSubBanks { | numBanks = 1 |
    numBanks.do({
      touchButtonsSubBankArray = touchButtonsSubBankArray.add(Array.fill2D(8, 4, nil));
      touchButtonsSubBankArray[touchButtonsSubBankArray.size - 1].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = \off;
        item[3] = TaskProxy.new;
      });
    });
  }

  addFaderSubBanks { | numBanks = 1 |
    numBanks.do({
      faderSubBankArray = faderSubBankArray.add(Array.fill2D(9, 3, nil));
      faderSubBankArray[faderSubBankArray.size - 1].do({ | item, index |
        item[0] = { };
        item[1] = 'redFill';
        item[2] = TaskProxy.new;
      });
    });
  }

  //////// Setting SubBanks:

  setActiveGridSubBank { | subBank = 0 |
    activeGridSubBank = subBank;
    32.do({ | index |
      this.setNoteOnFunc(index + 36, gridSubBankArray[activeGridSubBank][index][0]);
      this.setNoteOffFunc(index + 36, gridSubBankArray[activeGridSubBank][index][1]);
      this.turnColor(index + 36, gridSubBankArray[activeGridSubBank][index][2]);
    });
  }

  setActiveControlSubBank { | subBank = 0 |
    activeControlButtonsSubBank = subBank;
    8.do({ | index |
      this.setNoteOnFunc(index, controlButtonsSubBankArray[activeControlButtonsSubBank][index][0]);
      this.setNoteOffFunc(index, controlButtonsSubBankArray[activeControlButtonsSubBank][index][1]);
      // not working yet:
      //this.turnColor(index, controlButtonsSubBankArray[activeControlButtonsSubBank][index][2]);
    });
  }

  setActiveTouchButtonsSubBank { | subBank = 0 |
    activeTouchButtonsSubBank = subBank;
    8.do({ | index |
      this.setNoteOnFunc(index, touchButtonsSubBankArray[activeTouchButtonsSubBank][index][0]);
      this.setNoteOffFunc(index, touchButtonsSubBankArray[activeTouchButtonsSubBank][index][1]);
      // not working yet:
      //this.turnColor(index, touchButtonsSubBankArray[activeTouchButtonsSubBank][index][2]);
    });
  }

  setActiveFaderSubBank { | subBank = 0 |
    activeFaderSubBank = subBank;
    8.do({ | index |
      this.setControlFunc(index, faderSubBankArray[activeFaderSubBank][index][0]);
      this.setFaderMode(index + 10, faderSubBankArray[activeFaderSubBank][index][1]);
    });
  }

  numSubBanks { | type = 'grid' |
    switch(type,
      { 'grid' }, { ^gridSubBankArray.size; },
      { 'controlButtons' }, { ^controlButtonsSubBankArray.size; },
      { 'touchButtons' }, { ^touchButtonsSubBankArray.size; },
      { 'faders' }, { ^faderSubBankArray.size; }
    );
  }

}

+ Base_Page {

  //////// convenience functions for getting number of banks:

  numGridSubBanks { this.numSubBanks('grid'); }
  numControlButtonsSubBanks { this.numSubBanks('controlButtons'); }
  numTouchButtonsSubBanks { this.numSubBanks('touchButtons'); }
  numFaderSubBanks { this.numSubBanks('faders'); }

}