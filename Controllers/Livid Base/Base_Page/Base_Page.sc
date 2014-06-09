Base_Page : Base {

  var noteOnFuncArray, noteOffFuncArray, controlFuncArray, touchFuncArray, bendFuncArray;
  var buttonColorArray, faderModeArray;

  var gridSubBankArray, controlButtonSubBankArray, faderSubBankArray, touchButtonSubBankArray;
  var <activeGridSubBank, <activeControlButtonSubBank, <activeFaderSubBank, <activeTouchButtonSubBank, <activeBank;

  *new { ^super.new.prInit }

  prInit {
    this.prMakeResponders;
    this.prMakeButtonColorArray;
    this.prMakeFaderModeArray;
    this.prMakeSubBanks;
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

  prMakeButtonColorArray {
    buttonColorArray = Array.fill(64, { 'off' });
  }

  prMakeFaderModeArray {
    faderModeArray = Array.fill(9, { 'redFill' });
  }

  ////////

  setFunc { | num = 0, type = 'noteOn', func = nil, bank = 'active' |
    var bankSelect;
    if( bank == 'active', { bankSelect = super.activeBank; }, { bankSelect = bank; });
    switch(type,
      { \noteOn }, { noteOnFuncArray[bankSelect][num] = func },
      { \noteOff }, { noteOffFuncArray[bankSelect][num] = func },
      { \control }, { controlFuncArray[bankSelect][num] = func },
      { \polyTouch }, { touchFuncArray[bankSelect][num] = func },
      { \bend }, { bendFuncArray[bankSelect][num] = func }
    );
  }

  clearFunc { | num = 0, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == 'active', { bankSelect = super.activeBank; }, { bankSelect = bank; });
    switch(type,
      { \noteOn }, { noteOnFuncArray[bankSelect][num] = { } },
      { \noteOff }, { noteOffFuncArray[bankSelect][num] = { } },
      { \control }, { controlFuncArray[bankSelect][num] = { } },
      { \polyTouch }, { touchFuncArray[bankSelect][num] = { } },
      { \bend }, { bendFuncArray[bankSelect][num] = { } }
    );
  }

  getFunc { | num = 0, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == 'active', { bankSelect = super.activeBank; }, { bankSelect = bank; });
    switch(type,
      { \noteOn }, { ^noteOnFuncArray[bankSelect][num]; },
      { \noteOff }, { ^noteOffFuncArray[bankSelect][num]; },
      { \control }, { ^controlFuncArray[bankSelect][num]; },
      { \polyTouch }, { ^touchFuncArray[bankSelect][num]; },
      { \bend }, { bendFuncArray[bankSelect][num] = { } }
    );
  }

  //prMakeSubBanks { }

}

//////// convenience note functions:

+ Base_Page {

  setNoteOnFunc { | num = 0, func = nil, bank = 'active' |
    this.setFunc(num, \noteOn, func, bank);
  }
  clearNoteOnFunc { | num = 0, bank = 'active' |
    this.clearFunc(num, \noteOn, bank);
  }
  getNoteOnFunc { | num = 0, bank = 'active' |
    this.getFunc(num, \noteOn, bank);
  }

  setNoteOffFunc { | num = 0, func = nil, bank = 'active' |
    this.setFunc(num, \noteOff, func, bank);
  }
  clearNoteOffFunc { | num = 0, bank = 'active' |
    this.clearFunc(num, \noteOff, bank);
  }
  getNoteOffFunc { | num = 0, bank = 'active' |
    this.getFunc(num, 'noteOff', bank);
  }

  setControlFunc { | num = 0, func = nil, bank = 'active' |
    this.setFunc(num, \control, func, bank);
  }
  clearControlFunc { | num = 0, bank = 'active' |
    this.clearFunc(num, \control, bank);
  }
  getControlFunc { | num = 0, bank = 'active' |
    this.getFunc(num, \control, bank);
  }

  setPolyTouchFunc { | num = 0, func = nil, bank = 'active' |
    this.setFunc(num, \polyTouch, func, bank);
  }
  clearPolyTouchFunc { | num = 0, bank = 'active' |
    this.clearFunc(num, \polyTouch, bank);
  }
  getPolyTouchFunc { | num = 0, bank = 'active' |
    this.getFunc(num, 'polyTouch', bank);
  }

  setBendFunc { | num = 0, func = nil, bank = 'active' |
    this.setFunc(num, \bend, func, bank);
  }
  clearBendFunc { | num = 0, bank = 'active' |
    this.clearFunc(num, \bend, bank);
  }
  getBendFunc { | num = 0, bank = 'active' |
    this.getFunc(num, 'bend', bank);
  }

}

// convenience functions w/ bank arguments:

+ Base_Page {

  setGridFunc { | column = 0, row = 0, func, type = 'noteOn', bank = 'active', subBank = 'active' |
    var bankSelect, subBankSelect;
    // row is from bottom up
    // column is from left to right
    var num = ((row * 8) + column);
    var midiNum = num + 36;
    if( bank == 'active', { bankSelect = super.activeBank; }, { bankSelect = bank; });
    if( subBank == 'active', { subBankSelect = activeGridSubBank }, { subBankSelect = subBank });
    if( (row >3) || (column > 7), { "out of range!"}, {
      switch(type,
        { \noteOn }, {
          gridSubBankArray[subBankSelect][num][0] = func;
          if( subBank == 'active', { this.setNoteOnFunc(midiNum, func, bank); }); },
        { \noteOff }, {
          gridSubBankArray[subBankSelect][num][1] = func;
          if( subBank == 'active', { this.setNoteOffFunc(midiNum, func, bank); }); }
      );
    });

  }

  setControlButtonFunc { | button = 1, func, type = 'noteOn', bank = 'active', subBank = 'active' |
    var bankSelect, subBankSelect;
    var num = button + 17;
    if ( bank == 'active', { bankSelect = super.activeBank; }, { bankSelect = bank });
    if( subBank == 'active', { subBankSelect = activeControlButtonSubBank }, { subBankSelect = subBank });
    if( button > 8, { "out of range!" }, {
      switch(type,
        { \noteOn }, {
          controlButtonSubBankArray[subBankSelect][button][0] = func;
          if( subBank == 'active', { this.setNoteOnFunc(num, func, bank); }); },
        { \noteOff }, {
          controlButtonSubBankArray[subBankSelect][button][1] = func;
          if( subBank == 'active', { this.setNoteOffFunc(num, func, bank); }); }
      );
    });
  }

  setTouchButtonFunc { | button = 0, func, type = 'noteOn', bank = 'active', subBank = 'active' |
    var bankSelect, subBankSelect;
    var num = button + 10;
    if ( bank == 'active', { bankSelect = super.activeBank; }, { bankSelect = bank });
    if( subBank == 'active', { subBankSelect = activeTouchButtonSubBank }, { subBankSelect = subBank });
    if( button > 8, { "out of range!" }, {
      switch(type,
        { \noteOn }, {
          touchButtonSubBankArray[subBankSelect][button][0] = func;
          if( subBank == 'active', { this.setNoteOnFunc(num, func, bank); }); },
        { \noteOff }, {
          touchButtonSubBankArray[subBankSelect][button][1] = func;
          if( subBank == 'active', { this.setNoteOffFunc(num, func, bank); }); }
      );
    });
  }

  setFaderFunc { | fader = 0, func, bank = 'active', subBank = 'active' |
    var bankSelect, subBankSelect;
    var num = fader + 1;
    if ( bank == 'active', { bankSelect = super.activeBank; }, { bankSelect = bank });
    if( subBank == 'active', { subBankSelect = activeFaderSubBank }, { subBankSelect = subBank });
    if( fader > 9, { "out of range!" }, {
      faderSubBankArray[subBankSelect][fader][0] = func;
      if( subBank == 'active', { this.setControlFunc(num, func, bank); }); });
  }

  setMasterFaderFunc { | func, bank = 'active', subBank = 'active' |
    var bankSelect, subBankSelect;
    if ( bank == 'active', { bankSelect = super.activeBank; }, { bankSelect = bank });
    if( subBank == 'active', { subBankSelect = activeFaderSubBank }, { subBankSelect = subBank });
    faderSubBankArray[subBankSelect][8][0] = func;
    if( subBank == 'active', { this.setControlFunc(9, func, bank); });
  }
}
