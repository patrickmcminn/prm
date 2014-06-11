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
    fadersSubBankArray = Array.new;
    this.addFaderSubBanks(numBanks);

    activeGridSubBank = 0;
    activeControlButtonsSubBank = 0;
    activeTouchButtonsSubBank = 0;
    activeFadersSubBank = 0;
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
      controlButtonsSubBankArray = controlButtonsSubBankArray.add(Array.newClear(4));
      controlButtonsSubBankArray[controlButtonsSubBankArray.size - 1][0] = Array.fill(8, { });
      controlButtonsSubBankArray[controlButtonsSubBankArray.size - 1][1] = Array.fill(8, { });
      controlButtonsSubBankArray[controlButtonsSubBankArray.size - 1][2] = Array.fill(16, \off);
      controlButtonsSubBankArray[controlButtonsSubBankArray.size - 1][3] = Array.fill(8, { TaskProxy.new });
    });
  }

  addTouchButtonSubBanks { | numBanks = 1 |
    numBanks.do({
      touchButtonsSubBankArray = touchButtonsSubBankArray.add(Array.newClear(4));
      touchButtonsSubBankArray[touchButtonsSubBankArray.size - 1][0] = Array.fill(8, { });
      touchButtonsSubBankArray[touchButtonsSubBankArray.size - 1][1] = Array.fill(8, { });
      touchButtonsSubBankArray[touchButtonsSubBankArray.size - 1][2] = Array.fill(16, \off);
      touchButtonsSubBankArray[touchButtonsSubBankArray.size - 1][3] = Array.fill(8, { TaskProxy.new });
    });
  }

  addFadersSubBanks { | numBanks = 1 |
    numBanks.do({
      fadersSubBankArray = fadersSubBankArray.add(Array.fill2D(9, 3, nil));
      fadersSubBankArray[fadersSubBankArray.size - 1].do({ | item, index |
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
      this.setNoteOnFunc(index + 18, controlButtonsSubBankArray[activeControlButtonsSubBank][index][0]);
      this.setNoteOffFunc(index + 18, controlButtonsSubBankArray[activeControlButtonsSubBank][index][1]);
    });
    16.do({ | index |
      this.turnColor(index + 18, controlButtonsSubBankArray[activeControlButtonsSubBank][index][2]);
    });
  }

  setActiveTouchButtonsSubBank { | subBank = 0 |
    activeTouchButtonsSubBank = subBank;
    8.do({ | index |
      this.setNoteOnFunc(index + 10, touchButtonsSubBankArray[activeTouchButtonsSubBank][index][0]);
      this.setNoteOffFunc(index + 10, touchButtonsSubBankArray[activeTouchButtonsSubBank][index][1]);
    });
    8.do({ | index |
      this.turnColor(index + 10, touchButtonsSubBankArray[activeTouchButtonsSubBank][index][2]);
    });
    8.do({ | index |
      this.turnColor(index + 68, touchButtonsSubBankArray[activeTouchButtonsSubBank][index + 8][2]);
    });
  }

  setactiveFadersSubBank { | subBank = 0 |
    activeFadersSubBank = subBank;
    8.do({ | index |
      this.setControlFunc(index, fadersSubBankArray[activeFadersSubBank][index][0]);
      this.setFaderMode(index + 10, fadersSubBankArray[activeFadersSubBank][index][1]);
    });
  }

  numSubBanks { | type = 'grid' |
    switch(type,
      { 'grid' }, { ^gridSubBankArray.size; },
      { 'controlButtons' }, { ^controlButtonsSubBankArray.size; },
      { 'touchButtons' }, { ^touchButtonsSubBankArray.size; },
      { 'faders' }, { ^fadersSubBankArray.size; }
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