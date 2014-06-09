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
    controlButtonSubBankArray = Array.new;
    this.addControlButtonSubBanks(numBanks);
    touchButtonSubBankArray = Array.new;
    this.addTouchButtonSubBanks(numBanks);
    faderSubBankArray = Array.new;
    this.addFaderSubBanks(numBanks);

    activeGridSubBank = 0;
    activeControlButtonSubBank = 0;
    activeTouchButtonSubBank = 0;
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
      controlButtonSubBankArray = controlButtonSubBankArray.add(Array.fill2D(8, 4, nil));
      controlButtonSubBankArray[controlButtonSubBankArray.size - 1].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = \off;
        item[3] = TaskProxy.new;
      });
    });
  }

  addTouchButtonSubBanks { | numBanks = 1 |
    numBanks.do({
      touchButtonSubBankArray = touchButtonSubBankArray.add(Array.fill2D(8, 4, nil));
      touchButtonSubBankArray[touchButtonSubBankArray.size - 1].do({ | item, index |
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




}