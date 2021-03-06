/*
Tuesday, January 28th 2014
prm
*/
/*

IM_OhmRGB {

  var midiInPort, midiOutPort;
  var noteOnFuncArray, noteOffFuncArray, controlFuncArray;
  var <pageDict, <activePage, <activePageKey;
  var colorArray;

  *new {
    ^super.new.prInit;
  }

  prInit {
    this.prInitMIDI;
    this.prMakeResponders;
    this.prMakeColorArray;
    this.prMakePageDictionary;
  }

  prInitMIDI {
    MIDIIn.connectAll;
    midiInPort = MIDIIn.findPort("OhmRGB", "Controls");
    midiOutPort = MIDIOut.newByName("OhmRGB", "Controls");
    midiOutPort.latency = 0;
  }

  prMakeResponders {
    this.prMakeNoteResponders;
    this.prMakeControlResponders;
  }

  prFreeResponders {
    this.prFreeNoteResponders;
    this.prFreeControlResponders;
  }

  prMakeColorArray {
    colorArray = Array.fill(80, { | num | num });
    colorArray = colorArray.add(87);
  }

  //////// Note Responders:
  prMakeNoteResponders {
    noteOnFuncArray = Array.fill(80, { | index |
      MIDIFunc({ | val, num, chn, src | (midiInPort.device + "noteOn" + num + "has no function assigned").postln; },
        index, nil, \noteOn, midiInPort.uid).fix;
    });
    noteOnFuncArray = noteOnFuncArray.add(
      MIDIFunc({ | val, num, chn, src | (midiInPort.device + "noteOn" + num + "has no function assigned").postln; },
        87, nil, \noteOn, midiInPort.uid).fix;
    );

    noteOffFuncArray = Array.fill(80, { | index |
      MIDIFunc({ | val, num, chn, src |  }, index, nil, \noteOff, midiInPort.uid).fix;
    });
    noteOffFuncArray = noteOffFuncArray.add(MIDIFunc({ | val, num, chn, src | }, 87, nil, \noteOff, midiInPort.uid).fix;);
  }

  prFreeNoteResponders {
    noteOnFuncArray.do({ | func | func.free; });
  }

  //////// CC Responders:

  prMakeControlResponders {
    controlFuncArray = Array.fill(25, { | index |
      MIDIFunc({ | val, num, chn, src | },
        index, nil, \control, midiInPort.uid).fix;
    });
  }

  prFreeControlResponders {
    controlFuncArray.do({ | func | func.free; });
  }

  prMakePageDictionary {
    pageDict = IdentityDictionary.new;
    this.makePage('main');
    this.setPage('main');
  }


  /////// Setting Functions:

  setFunc { | num = 0, type = \noteOn, func = nil |
    switch(type,
      { \noteOn }, { noteOnFuncArray[num].prFunc_(func); },
      { \noteOff }, { noteOffFuncArray[num].prFunc_(func); },
      { \control }, { controlFuncArray[num].prFunc_(func); }
    );
  }

  clearFunc { | num = 0, type = \noteOn |
    switch(type,
      { \noteOn }, { noteOnFuncArray[num].prFunc_(
        { | val, num, chn, src | (midiInPort.device + "noteOn" + num + "has no function assigned").postln; }) },
      { \noteOff }, { noteOffFuncArray[num].prFunc_(
         { | val, num, chn, src | (midiInPort.device + "noteOn" + num + "has no function assigned").postln; }) },
      { \control }, { controlFuncArray[num].prFunc_({}); }
    );
  }

  setNoteOnFunc { | num, func |
    this.setFunc(num, \noteOn, func);
  }

  clearNoteOnFunc { | num |
    this.clearFunc(num, \noteOn);
  }

  setNoteOffFunc { | num, func |
    this.setFunc(num, \noteOff, func);
  }

  clearNoteOffFunc { | num |
    this.clearFunc(num, \noteOff);
  }

  setCCFunc { | num, func |
    this.setFunc(num, \control, func);
  }

  clearCCFunc { | num |
    this.clearFunc(num, \control);
  }

  /////// Page:

  makePage { | name = 'newPage' |
    pageDict[name] = OhmRGB_Page.new;
  }

  setPage { | name = 'page' |
    activePageKey = name;
    activePage = pageDict[activePageKey];
    81.do({ | num | this.setNoteOnFunc(num, activePage.getNoteOnFunc(num)); });
    81.do({ | num | this.setNoteOffFunc(num, activePage.getNoteOffFunc(num)); });
    25.do({ | num | this.setCCFunc(num, activePage.getCCFunc(num)); });
    81.do({ | num | this.turnColor(num, activePage.getColor(num)); });
  }

}
*/