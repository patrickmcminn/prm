/*
Saturday, April 12th, 2014
prm
*/

Base {

  var midiInPort, midiOutPort;
  var noteOnFuncArray, noteOffFuncArray, controlFuncArray, touchFuncArray, bendFuncArray;
  var <pageDict, <activePage, activePageKey, currentBank;
  var colorArray;

  *new { | localControl = 'allOff' |
    ^super.new.prInit(localControl);
  }

  prInit { | localControl = 'allOff' |
    currentBank = 1;
    this.prInitMIDI;
    this.setLocalControl(localControl);
    this.prMakeResponders;
    this.prMakeColorArray;
    this.prMakePageDictionary;
  }

  prInitMIDI {
    MIDIIn.connectAll;
    midiInPort = MIDIIn.findPort("Base", "Controls");
    midiOutPort = MIDIOut.newByName("Base", "Controls");
    midiOutPort.latency = 0;
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

  prMakeColorArray {
    colorArray = Array.fill(76, { | num | num });
  }

  // Note Responders:

  prMakeNoteResponders {
    /*
    noteOnFuncArray = Array.fill(72, { | num |
      MIDIFunc({ }, num, nil, \noteOn, midiInPort.uid).fix;
    });
    noteOffFuncArray = Array.fill(72, { | num |
      MIDIFunc({ }, num, nil, \noteOff, midiInPort.uid).fix;
    });
    */
    noteOnFuncArray = Array.fill2D(7, 72, { | bank, num |
      MIDIFunc({ }, num, bank, \noteOn, midiInPort.uid).fix;
    });
    noteOffFuncArray = Array.fill2D(7, 72, { | bank, num |
      MIDIFunc({ }, num, bank, \noteOff, midiInPort.uid).fix;
    });

  }

  prFreeNoteResponders {
    7.do({ | rItem, rIndex |
      72.do({ | cItem, cIndex |
        noteOnFuncArray[rIndex][cIndex].free;
        noteOffFuncArray[rIndex][cIndex].free;
      });
    });
  }

  // Control Responders:

  prMakeControlResponders {
    controlFuncArray = Array.fill2D(7, 72, { | bank, num |
      MIDIFunc({ }, num, bank, \control, midiInPort.uid).fix;
    });
  }

  prFreeControlResponders {
    7.do({ | rItem, rIndex |
      72.do({ | cItem, cIndex |
        controlFuncArray[rIndex][cIndex].free;
      });
    });
  }

  // Polyphonic Aftertouch Responders:

  prMakeAftertouchResponders {
    touchFuncArray = Array.fill2D(7, 32, { | bank, num |
      MIDIFunc({ }, num, bank, \polyTouch, midiInPort.uid).fix;
    });
  }

  prFreeAftertouchResponders {
    7.do({ | rItem, rIndex |
      32.do({ | cItem, cIndex |
        touchFuncArray[rIndex][cIndex].free;
      });
    });
  }

  // Bend Responders
  // untested -- how is bend implemented??

  prMakeBendResponders {
    bendFuncArray = Array.fill2D(7, 32, { | bank, num |
      MIDIFunc({ }, num, bank, \bend, midiInPort.uid).fix;
    });
  }

  prFreeBendResponders {
    7.do({ | rItem, rIndex |
      32.do({ | cItem, cIndex |
        bendFuncArray[rIndex][cIndex].free;
      });
    });
  }

  // pages:

  prMakePageDictionary {
    pageDict = IdentityDictionary.new;
    this.makePage('main');
    this.setPage('main');
  }

  // Settings:

  setFunc { | num = 0, type = \noteOn, func = nil, bank = 'active' |
    var bankSelect;
    if( bank == 'active', { bankSelect = currentBank; }, { bankSelect = bank });
    bankSelect = bankSelect - 1;
    switch(type,
      { \noteOn }, { noteOnFuncArray[bankSelect][num].prFunc_(func); },
      { \noteOff }, { noteOffFuncArray[bankSelect][num].prFunc_(func) },
      { \control }, { controlFuncArray[bankSelect][num].prFunc_(func); },
      { \polyTouch }, { touchFuncArray[bankSelect][num].prFunc_(func); },
      { \bend }, { bendFuncArray[bankSelect][num].prFunc_(func); }
    );
  }

  clearFunc { | num = 0, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == 'active', { bankSelect = currentBank; }, { bankSelect = bank });
    bankSelect = bankSelect - 1;
    switch(type,
      { \noteOn }, { noteOnFuncArray[bankSelect][num].prFunc_({ }) },
      { \noteOff }, { noteOffFuncArray[bankSelect][num].prFunc_({ }) },
      { \control }, { controlFuncArray[bankSelect][num].prFunc_({ }) },
      { \polyTouch }, { touchFuncArray[bankSelect][num].prFunc_({ }) },
      { \bend }, { bendFuncArray[bankSelect][num].prFunc_({ }) },
    );
  }

  setNoteOnFunc { | num = 0, func = nil, bank = 'active' |
    this.setFunc(num, 'noteOn', func, bank);
  }

  clearNoteOnFunc { | num = 0, bank = 'active' |
    this.clearFunc(num, 'noteOn', bank);
  }

  setNoteOffFunc { | num = 0, func = nil, bank = 'active' |
    this.setFunc(num, 'noteOff', func, bank);
  }

  clearNoteOffFunc { | num = 0, bank = 'active' |
    this.clearFunc(num, 'noteOff', bank);
  }

  setControlFunc { | num = 0, func = nil, bank = 'active' |
    this.setFunc(num, 'control', func, bank);
  }

  clearControlFunc { | num = 0, bank = 'active' |
    this.clearFunc(num, 'control', bank);
  }

  setPolyTouchFunc { | num = 0, func = nil, bank = 'active' |
    this.setFunc(num, 'polyTouch', func, bank);
  }

  clearPolyTouchFunc { | num = 0, bank = 'active' |
    this.clearFunc(num, 'polyTouch', bank);
  }

  setBendFunc { | num = 0, func = nil, bank = 'active' |
    this.setFunc(num, 'bend', func, bank);
  }

  clearBendFunc { | num = 0, bank = 'active' |
    this.clearFunc(num, 'bend', bank);
  }

  //////// Bank Functions:

  setBank { | bank = 1, page = 'active' |

    currentBank = bank - 1;
    midiOutPort.program(midiOutPort.uid, currentBank);
  }


  ///////// Page Functions:

  makePage { | name = 'newPage' |
    pageDict[name] = Base_Page.new;
  }

  setPage { | name = 'newPage', bank = 1 |
    activePageKey = name;
    activePage = pageDict[activePageKey];
   // 72.do({ | num | this.setNoteOnFunc(num, activePage.getNoteOnFunc(num)); });
    //72.do({ | num | this.setNoteOffFunc(num, activePage.getNoteOffFunc(num)); });
    //36.do({ | num | this.setCCFunc(num, activePage.getCCFunc(num)); });
    //72.do({ | num | this.setPolyTouchFunc(num, activePage.getPolyTouchFunc(num)) });
    //72.do({ | num | this.setBendFunc(num, activePage.getBendFunc(num)) });
    //81.do({ | num | this.turnColor(num, activePage.getColor(num)); });
  }

}