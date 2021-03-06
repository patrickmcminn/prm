Base_Page {

  var noteOnFuncArray, noteOffFuncArray, controlFuncArray, touchFuncArray, bendFuncArray;
  var buttonColorArray, <faderModeArray, <faderValueArray;

  var <gridBankArray, <controlButtonsBankArray, <fadersBankArray, <touchButtonsBankArray;
  var <activeGridBnk, <activeControlButtonsBnk, <activeFadersBnk, <activeTouchButtonsBnk;

  var <loadFunctionDict, <offLoadFunctionDict;

  *new { ^super.new.prInit }

  prInit {
    loadFunctionDict = IdentityDictionary.new;
    offLoadFunctionDict = IdentityDictionary.new;
    this.prMakeResponders;
    this.prMakeButtonColorArray;
    this.prMakeFaderModeArray;
    this.prMakeFaderValueArray;
    this.prMakeBanks;
  }

  prMakeResponders {
    this.prMakeNoteResponders;
    this.prMakeControlResponders;
    //this.prMakeAftertouchResponders;
    //this.prMakeBendResponders;
  }

  prFreeResponders {
    this.prFreeNoteResponders;
    this.prFreeControlResponders;
    //this.prFreeAftertouchResponders;
    //this.prFreeBendResponders;
  }

  prMakeNoteResponders {
    noteOnFuncArray = Array.fill(72, { });
    noteOffFuncArray = Array.fill(72, { });
  }

  prFreeNoteResponders { noteOnFuncArray.do({ | func | func.free; }); }
  prMakeControlResponders { controlFuncArray = Array.fill(72, { }); }
  prFreeControlResponders { controlFuncArray.do({ | func | func.free; }); }

  /*
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
  */

  prMakeButtonColorArray { buttonColorArray = Array.fill(81, { 'off' }); }

  prMakeFaderModeArray { faderModeArray = Array.fill(9, { 'redFill' }); }
  prMakeFaderValueArray { faderValueArray = Array.fill(9, { 0 });}

  ////////

  setFunc { | num = 0, type = 'noteOn', func = nil |
    switch(type,
      { \noteOn }, { noteOnFuncArray[num] = func },
      { \noteOff }, { noteOffFuncArray[num] = func },
      { \control }, { controlFuncArray[num] = func },
      //{ \polyTouch }, { touchFuncArray[num] = func },
      //{ \bend }, { bendFuncArray[num] = func }
    );
  }

  clearFunc { | num = 0, type = 'noteOn' |
    switch(type,
      { \noteOn }, { noteOnFuncArray[num] = { } },
      { \noteOff }, { noteOffFuncArray[num] = { } },
      { \control }, { controlFuncArray[num] = { } },
      //{ \polyTouch }, { touchFuncArray[num] = { } },
      //{ \bend }, { bendFuncArray[num] = { } }
    );
  }

  getFunc { | num = 0, type = 'noteOn' |
    switch(type,
      { \noteOn }, { ^noteOnFuncArray[num]; },
      { \noteOff }, { ^noteOffFuncArray[num]; },
      { \control }, { ^controlFuncArray[num]; },
      //{ \polyTouch }, { ^touchFuncArray[num]; },
      //{ \bend }, { bendFuncArray[num] = { } }
    );
  }

  getFaderValue { | num = 0 | ^faderValueArray[num]; }
  getFaderMode { | num = 0 | ^faderModeArray[num]; }

}

//////// convenience note functions:

+ Base_Page {

  setNoteOnFunc { | num = 0, func = nil |
    this.setFunc(num, \noteOn, func);
  }
  clearNoteOnFunc { | num = 0 |
    this.clearFunc(num, \noteOn);
  }
  getNoteOnFunc { | num = 0 |
    ^this.getFunc(num, \noteOn);
  }

  setNoteOffFunc { | num = 0, func = nil |
    this.setFunc(num, \noteOff, func);
  }
  clearNoteOffFunc { | num = 0 |
    this.clearFunc(num, \noteOff);
  }
  getNoteOffFunc { | num = 0 |
    ^this.getFunc(num, 'noteOff');
  }

  setControlFunc { | num = 0, func = nil |
    this.setFunc(num, \control, func);
  }
  clearControlFunc { | num = 0 |
    this.clearFunc(num, \control);
  }
  getControlFunc { | num = 0 |
    ^this.getFunc(num, \control);
  }

  addLoadFunction { | name, func |
    loadFunctionDict[name] = func;
  }

  addOffLoadFunction { | name, func |
    offLoadFunctionDict[name] = func;
  }


  /*
  setPolyTouchFunc { | num = 0, func = nil|
    this.setFunc(num, \polyTouch, func);
  }
  clearPolyTouchFunc { | num = 0 |
    this.clearFunc(num, \polyTouch);
  }
  getPolyTouchFunc { | num = 0 |
    this.getFunc(num, 'polyTouch');
  }

  setBendFunc { | num = 0, func = nil |
    this.setFunc(num, \bend, func);
  }
  clearBendFunc { | num = 0 |
    this.clearFunc(num, \bend);
  }
  getBendFunc { | num = 0 |
    this.getFunc(num, 'bend');
  }
  */


}

// convenience functions w/ bank arguments:

+ Base_Page {

  setGridFunc { | column = 0, row = 0, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    // row is from bottom up
    // column is from left to right
    var num = ((row * 8) + column);
    var midiNum = num + 36;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeGridBnk }, { bankSelect = bank });
    if( (row >3) || (column > 7), { "out of range!"}, {
      switch(type,
        { \noteOn }, {
          gridBankArray[bankSelect][num][0] = func;
          if( bank == 'active', { this.setNoteOnFunc(midiNum, func); });},
        { \noteOff }, {
          gridBankArray[bankSelect][num][1] = func;
          if( bank == 'active', { this.setNoteOffFunc(midiNum, func); }); },
        { \pressure }, {
          gridBankArray[bankSelect][num][2] = func;
          if( bank == 'active', { this.setControlFunc(midiNum, func); }); }
      );
    });
  }

  setGridMonitorFunc { | column = 0, row = 0, func, bank = 'active' |
    var bankSelect;
    var num = ((row * 8) + column);
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeGridBnk }, { bankSelect = bank });
    if( ( row > 3) || (column >7), { "out of range!"}, {
      gridBankArray[bankSelect][num][5].stop;
      gridBankArray[bankSelect][num][5] = r {
        loop {
          func.value;
          0.05.wait;
        }
      };
      //if( bank == 'active', { gridBankArray[bankSelect][num][5].reset.play; });
    });
  }

  setControlButtonFunc { | button = 1, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    var num = button - 1;
    var midinum = button + 17;
    if( bank == activeControlButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeControlButtonsBnk }, { bankSelect = bank });
    if( num > 7, { "out of range!" }, {
      switch(type,
        { \noteOn }, {
          controlButtonsBankArray[bankSelect][0][num] = func;
          if( bank == 'active', { this.setNoteOnFunc(midinum, func); }); },
        { \noteOff }, {
          controlButtonsBankArray[bankSelect][1][num] = func;
          if( bank == 'active', { this.setNoteOffFunc(midinum, func); }); }
      );
    });
  }

  setControlButtonMonitorFunc { | button = 1, func, bank = 'active' |
    var bankSelect;
    var num = button -1;
    if( bank == activeControlButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeControlButtonsBnk }, { bankSelect = bank });
    if( num > 7, { "Out of Range!" }, {
      controlButtonsBankArray[bankSelect][4][num].stop;
      controlButtonsBankArray[bankSelect][4][num] = r {
        loop {
          func.value;
          0.05.wait;
        }
      };
      //if( bank == 'active', { controlButtonsBankArray[bankSelect][4][num].reset.play; });
    });
  }


  setTouchButtonFunc { | button = 1, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    var num = button -1;
    var midinum = button + 9;
    if( bank == activeTouchButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeTouchButtonsBnk }, { bankSelect = bank });
    if( num > 7, { "out of range!".postln; }, {
      switch(type,
        { \noteOn }, {
          touchButtonsBankArray[bankSelect][0][num] = func;
          if( bank == 'active', { this.setNoteOnFunc(midinum, func); }); },
        { \noteOff }, {
          touchButtonsBankArray[bankSelect][1][num] = func;
          if( bank == 'active', { this.setNoteOffFunc(midinum, func); }); }
      );
    });
  }

  setTouchButtonMonitorFunc { | button = 1, func, bank = 'active' |
    var bankSelect;
    var num = button - 1;
    //button.postln;
    //func.postln;
    //bank.postln;
    if( bank == activeTouchButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeTouchButtonsBnk }, { bankSelect = bank });
    //bankSelect.postln;
    //num.postln;
    if( num > 7, { "out of range!".postln; }, {
      touchButtonsBankArray[bankSelect][4][num].stop;
      touchButtonsBankArray[bankSelect][4][num] = r {
        loop {
          func.value;
          0.05.wait;
        };
      };
      //if( bank == 'active', { touchButtonsBankArray[bankSelect][4][num].reset.play; });
    });
  }

  setFaderFunc { | fader = 1, func, bank = 'active' |
    var bankSelect;
    var faderIndex = fader - 1;
    var num = fader;
    if( bank == activeFadersBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeFadersBnk }, { bankSelect = bank });
    if( fader > 9, { "out of range!" }, {
      fadersBankArray[bankSelect][faderIndex][0] = func;
      if( bank == 'active', { this.setControlFunc(num, func); }); });
  }

  setFaderMonitorFunc { | fader = 1, func, bank = 'active' |
    var bankSelect;
    var faderIndex = fader - 1;
    var num = fader;
    if( bank == activeFadersBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeFadersBnk }, { bankSelect = bank });
    if( fader > 9, { "out of range!" }, {
      fadersBankArray[bankSelect][faderIndex][4].stop;
      fadersBankArray[bankSelect][faderIndex][4] = r {
        loop {
          func.value;
          0.05.wait;
        };
      };
      //if( bank == 'active', { fadersBankArray[bankSelect][faderIndex][4].reset.play; });
    });
  }

  setMasterFaderFunc { | func, bank = 'active' |
    var bankSelect;
    if( bank == activeFadersBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeFadersBnk }, { bankSelect = bank });
    fadersBankArray[bankSelect][8][0] = func;
    if( bank == 'active', { this.setControlFunc(9, func); });
  }
}
