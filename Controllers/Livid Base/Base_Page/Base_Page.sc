Base_Page {

  var noteOnFuncArray, noteOffFuncArray, controlFuncArray, touchFuncArray, bendFuncArray;

  var gridSubBankArray, controlSubBankArray, touchFaderSubBankArray, touchButtonSubBankArray;
  var <activeGridSubBank, <activeControlSubBank, <activeTouchFaderSubBank, <activeTouchButtonSubBank, <activeBank;

  *new { ^super.new.prInit }

  prInit {
    this.prMakeResponders;
    this.prMakeColorArray;
    //this.prMakeSubBanks;
  }

  prMakeResponders {
    this.prMakeNoteResponders;
    this.prMakeControlResponders;
    this.prMakeAftertouchResponders;
    this.prMakeBendResponders;
  }

  prFreeResponders {
    this.prFreeNoteResponders;
    this.prFreeControlResponders;
    this.prFreeAftertouchResponders;
    this.prFreeBendResponders;
  }

  prMakeNoteResponders {
    noteOnFuncArray = Array.fill2D(7, 72, { });
    noteOffFuncArray = Array.fill2D(7, 72, { });
  }

  prFreeNoteResponders {
    7.do({ | rItem, rIndex |
      72.do({ | cItem, cIndex |
        noteOnFuncArray[rIndex][cIndex] = nil;
        noteOffFuncArray[rIndex][cIndex] = nil;
      });
    });
  }

  prMakeControlResponders {
    controlFuncArray = Array.fill2D(7, 72, { });
  }

  prFreeControlResponders {
     7.do({ | rItem, rIndex |
      72.do({ | cItem, cIndex |
        controlFuncArray[rIndex][cIndex] = nil;
      });
    });
  }

  prMakeAftertouchResponders {
    touchFuncArray = Array.fill2D(7, 32, { });
  }

  prFreeAftertouchResponders {
    7.do({ | rItem, rIndex |
      32.do({ | cItem, cIndex |
        touchFuncArray[rIndex][cIndex] = nil;
      });
    });
  }

  prMakeBendResponders {
    bendFuncArray = Array.fill2D(7, 32, { });
  }

  prFreeBendResponders {
    7.do({ | rItem, rIndex |
      32.do({ | cItem, cIndex |
        bendFuncArray[rIndex][cIndex] = nil;
      });
    });
  }

  prMakeColorArray {
    //colorArray = Array.fill(81, { 'off' });
  }

  prMakeSubBanks { }

}
