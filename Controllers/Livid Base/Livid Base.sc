/*
Saturday, April 12th, 2014
prm
*/

Base {

  var midiInPort, midiOutPort;
  var noteOnFuncArray, noteOffFuncArray, controlFuncArray, touchFuncArray, bendFuncArray;
  var <pageDict, <activePage, activePageKey;
  var colorArray;

  *new { | localControl = 'allOff' |
    ^super.new.prInit(localControl);
  }

  prInit { | localControl = 'allOff' |
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
    //this.prMakeAftertouchResponders;
    //this.prMakeBendResponders;
  }

  prFreeResponders {
    this.prFreeNoteResponders;
    this.prFreeControlResponders;
    //this.prFreeAftertouchResponders;
    //this.prFreeBendResponders;
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
    noteOnFuncArray = Array.fill(72, { | num |
      MIDIFunc({ }, num, nil, \noteOn, midiInPort.uid).fix;
    });
    noteOffFuncArray = Array.fill(72, { | num |
      MIDIFunc({ }, num, nil, \noteOff, midiInPort.uid).fix;
    });

  }

  prFreeNoteResponders {
    noteOnFuncArray.do({ | func | func.free; });
    noteOffFuncArray.do({ | func | func.free; });
  }

  // Control Responders:

  prMakeControlResponders {
    controlFuncArray = Array.fill(72, { | num |
      MIDIFunc({ }, num, nil, \control, midiInPort.uid).fix;
    });
  }

  prFreeControlResponders { controlFuncArray.do({ | func | func.free; }); }

  // Polyphonic Aftertouch Responders:

  /*
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
  */

  // pages:

  prMakePageDictionary {
    pageDict = IdentityDictionary.new;
    this.makePage('main');
    this.setPage('main');
  }

  // Settings:

  setFunc { | num = 0, type = \noteOn, func = nil |
    switch(type,
      { \noteOn }, { noteOnFuncArray[num].prFunc_(func); },
      { \noteOff }, { noteOffFuncArray[num].prFunc_(func) },
      { \control }, { controlFuncArray[num].prFunc_(func); },
      //{ \polyTouch }, { touchFuncArray[bankSelect][num].prFunc_(func); },
      //{ \bend }, { bendFuncArray[bankSelect][num].prFunc_(func); }
    );
  }

  clearFunc { | num = 0, type = 'noteOn' |
    switch(type,
      { \noteOn }, { noteOnFuncArray[num].prFunc_({ }) },
      { \noteOff }, { noteOffFuncArray[num].prFunc_({ }) },
      { \control }, { controlFuncArray[num].prFunc_({ }) },
      //{ \polyTouch }, { touchFuncArray[bankSelect][num].prFunc_({ }) },
      //{ \bend }, { bendFuncArray[bankSelect][num].prFunc_({ }) },
    );
  }

  setNoteOnFunc { | num = 0, func = nil |
    this.setFunc(num, 'noteOn', func);
  }

  clearNoteOnFunc { | num = 0 |
    this.clearFunc(num, 'noteOn');
  }

  setNoteOffFunc { | num = 0, func = nil |
    this.setFunc(num, 'noteOff', func);
  }

  clearNoteOffFunc { | num = 0 |
    this.clearFunc(num, 'noteOff');
  }

  setControlFunc { | num = 0, func = nil |
    this.setFunc(num, 'control', func);
  }

  clearControlFunc { | num = 0 |
    this.clearFunc(num, 'control');
  }

  setPolyTouchFunc { | num = 0, func = nil |
    this.setFunc(num, 'polyTouch', func);
  }

  clearPolyTouchFunc { | num = 0 |
    this.clearFunc(num, 'polyTouch');
  }

  setBendFunc { | num = 0, func = nil |
    this.setFunc(num, 'bend', func);
  }

  clearBendFunc { | num = 0 |
    this.clearFunc(num, 'bend');
  }

  ///////// Page Functions:

  makePage { | name = 'newPage' |
    pageDict[name] = Base_Page.new;
  }

  setPage { | name = 'page' |
    activePage.offLoadFunction.value;
    activePageKey = name;
    activePage = pageDict[activePageKey];
    72.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
    });
    68.do({ | num | this.setControlFunc(num, activePage.getControlFunc(num)); });
    //72.do({ | num | this.setPolyTouchFunc(num, activePage.getPolyTouchFunc(num)) });
    //72.do({ | num | this.setBendFunc(num, activePage.getBendFunc(num)) });
    9.do({ | num |
      this.prSetFaderValue(num + 1, activePage.getFaderValue(num));
      this.prSetFaderMode(num + 10, activePage.getFaderMode(num));
    });
    76.do({ | num | this.turnButtonColor(num, activePage.getButtonColor(num)); });
    activePage.loadFunction.value;
  }

  setLoadFunction { | func, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setLoadFunction(func);
  }

  setOffLoadFunction { | func, page = 'active' |
    if( page == 'active', { page = activePageKey; });
    pageDict[page].setOffLoadFunction(func);
  }

}