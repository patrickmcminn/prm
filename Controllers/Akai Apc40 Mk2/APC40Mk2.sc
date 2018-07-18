/*
Monday, May 21st 2018
APC40Mk2.sc
prm
*/

APC40Mk2 {

  var midiInPort, midiOutPort;

  var <gridFuncArray, sceneLaunchFuncArray, clipStopFuncArray;
  var trackSelectFuncArray, trackActivatorFuncArray, crossfaderSelectFuncArray;
  var soloFuncArray, recordEnableFuncArray,  deviceFuncArray, controlFuncArray;
  var mixerFaderArray, <mixerEncoderArray, deviceEncoderArray;

  var <pageDict, <activePage, <activePageKey, <storageDict, <previousPage;

  var <gridColorArray, <sceneLaunchColorArray, <clipStopColorArray;
  var <selectColorArray, <mixerColorArray;

  var <activeGridBank, <activeSceneLaunchBank, <activeClipStopBank;
  var <activeMixerBank,  <activeMixerEncoderBank;
  var <activeDeviceEncodersBank, <activeDeviceButtonsBank, <activeControlButtonsBank;

  *new { | deviceName = "APC40 mkII", portName = "APC40 mkII" |
    ^super.new.prInit(deviceName, portName);
  }

  prInit { | deviceName = "APC40 mkII", portName = "APC40 mkII" |
    this.prInitMIDI(deviceName, portName);
    this.prMakeResponders;
    this.prMakeColorArrays;
    this.prMakePageDictionary;
    storageDict = IdentityDictionary.new(know: true);
  }

  prInitMIDI { | deviceName = "APC40 mkII", portName = "APC40 mkII" |
    MIDIIn.connectAll;
    midiInPort = MIDIIn.findPort(deviceName, portName);
    midiOutPort = MIDIOut.newByName(deviceName, portName);
    midiOutPort.latency = 0;

    /////// set to control mode:
    midiOutPort.sysex(Int8Array[0xf0, 0x47, 0x01, 0x29,0x60, 0x00, 0x04, 0x42, 0x01, 0x01, 0x01, 0xf7]);
  }

  prMakeResponders {
    this.prMakeNoteResponders;
    this.prMakeControlResponders;
  }

  prFreeResponders {
    this.prFreeNoteResponders;
    this.prFreeControlResponders;
  }

  prMakeNoteResponders {
    this.prMakeGridFuncArray;
    this.prMakeSceneLaunchFuncArray;
    this.prMakeClipStopFuncArray;
    this.prMakeTrackSelectFuncArray;
    this.prMakeTrackActivatorFuncArray;
    this.prMakeCrossfaderSelectFuncArray;
    this.prMakeSoloFuncArray;
    this.prMakeRecordEnableFuncArray;
    this.prMakeDeviceFuncArray;
    this.prMakeControlFuncArray;
  }

  prFreeNoteResponders {
    this.prFreeGridFuncArray;
    this.prFreeSceneLauncFuncArray;
    this.prFreeClipStopFuncArray;
    this.prFreeTrackSelectFuncArray;
    this.prFreeTrackActivatorFuncArray;
    this.prFreeCrossfaderSelectFuncArray;
    this.prFreeSoloFuncArray;
    this.prFreeRecordEnableFuncArray;
    this.prFreeDeviceFuncArray;
    this.prFreeControlFuncArray;
  }

  prMakeGridFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    gridFuncArray = Array.fill2D(2, 40, { nil });
    40.do({ | num |
      gridFuncArray[0][num] = MIDIFunc({ }, num, 0, \noteOn, midiInPort.uid).fix;
    });
    40.do({ | num |
      gridFuncArray[1][num] = MIDIFunc({ }, num, 0, \noteOff, midiInPort.uid).fix;
    });
  }

  prFreeGridFuncArray { gridFuncArray.do({ | f | f.free; }); }

  prMakeSceneLaunchFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    sceneLaunchFuncArray = Array.fill2D(2, 5, { nil });
    5.do({ | num |
      sceneLaunchFuncArray[0][num] = MIDIFunc({ }, num+82, 0, \noteOn, midiInPort.uid).fix;
    });
    5.do({ | num |
      sceneLaunchFuncArray[1][num] = MIDIFunc({ }, num+82, 0, \noteOff, midiInPort.uid).fix;
    });
  }

  prFreeSceneLaunchFuncArray { sceneLaunchFuncArray.do({ | f | f.free; }); }

  prMakeClipStopFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    clipStopFuncArray = Array.fill2D(2, 9, { nil });
    8.do({ | num |
      clipStopFuncArray[0][num] = MIDIFunc({ }, 52, num, \noteOn, midiInPort.uid).fix;
    });
    clipStopFuncArray[0][8] = MIDIFunc({}, 81, 0, \noteOn, midiInPort.uid).fix;
    8.do({ | num |
      clipStopFuncArray[1][num] = MIDIFunc({ }, 52, num, \noteOff, midiInPort.uid).fix;
    });
    clipStopFuncArray[1][8] = MIDIFunc({}, 81, 0, \noteOff, midiInPort.uid).fix;
  }

  prFreeClipStopFuncArray { clipStopFuncArray.do({ | f | f.free; }); }

  prMakeTrackSelectFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    trackSelectFuncArray = Array.fill2D(2, 9, { nil });
    8.do({ | num |
      trackSelectFuncArray[0][num] = MIDIFunc({ }, 51, num, \noteOn, midiInPort.uid).fix;
    });
    trackSelectFuncArray[0][8] = MIDIFunc({ }, 80, 0, \noteOn, midiInPort.uid).fix;
    8.do({ | num |
      trackSelectFuncArray[1][num] = MIDIFunc({ }, 51, num, \noteOff, midiInPort.uid).fix;
    });
    trackSelectFuncArray[1][8] = MIDIFunc({ }, 80, 0, \noteOff, midiInPort.uid).fix;
  }

  prFreeTrackSelectFuncArray { trackSelectFuncArray.do({ | f | f.free; }); }

  prMakeTrackActivatorFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    trackActivatorFuncArray = Array.fill2D(2, 8, { nil });
    8.do({ | num |
      trackActivatorFuncArray[0][num] = MIDIFunc({ }, 50, num, \noteOn, midiInPort.uid).fix;
    });
    8.do({ | num |
      trackActivatorFuncArray[1][num] = MIDIFunc({ }, 50, num, \noteOff, midiInPort.uid).fix;
    });
  }

  prFreeTrackActivatorFuncArray { trackActivatorFuncArray.do({ | f | f.free; }); }

  prMakeCrossfaderSelectFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    crossfaderSelectFuncArray = Array.fill2D(2, 8, { nil });
    8.do({ | num |
      crossfaderSelectFuncArray[0][num] = MIDIFunc({ }, 66, num, \noteOn, midiInPort.uid).fix;
    });
    8.do({ | num |
      crossfaderSelectFuncArray[1][num] = MIDIFunc({ }, 66, num, \noteOff, midiInPort.uid).fix;
    });
  }

  prFreeCrossfaderSelectFuncArray { crossfaderSelectFuncArray.do({ | f | f.free; }); }

  prMakeSoloFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    soloFuncArray = Array.fill2D(2, 8, { nil });
    8.do({ | num |
      soloFuncArray[0][num] = MIDIFunc({ }, 49, num, \noteOn, midiInPort.uid).fix;
    });
    8.do({ | num |
      soloFuncArray[1][num] = MIDIFunc({ }, 49, num, \noteOff, midiInPort.uid).fix;
    });
  }

  prFreeSoloFuncArray { soloFuncArray.do({ | f | f.free; }); }

  prMakeRecordEnableFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    recordEnableFuncArray = Array.fill2D(2, 8, { nil });
    8.do({ | num |
      recordEnableFuncArray[0][num] = MIDIFunc({ }, 48, num, \noteOn, midiInPort.uid).fix;
    });
    8.do({ | num |
      recordEnableFuncArray[1][num] = MIDIFunc({ }, 48, num, \noteOff, midiInPort.uid).fix;
    });
  }

  prFreeRecordEnableFuncArray { recordEnableFuncArray.do({ | f | f.free; }); }

  prMakeDeviceFuncArray {
    ////// buttons in array:
    // 0: 1
    // 1: 2
    // 2: 3
    // 3: 4
    // 4: 5
    // 5: 6
    // 6: 7
    // 7: 8
    // 8: Bank Select Up
    // 9: Bank Select Down
    // 10: Bank Select Right
    // 11: Bank Select Left
    // 12: Shift
    // 13: Bank

    deviceFuncArray = Array.fill2D(2, 14, { nil });

    //// note on:
    8.do({ | num |
      deviceFuncArray[0][num] = MIDIFunc({ }, num + 58, 0, \noteOn, midiInPort.uid).fix;
    });
    4.do({ | num |
      deviceFuncArray[0][num+8] = MIDIFunc({ }, num + 94, 0, \noteOn, midiInPort.uid).fix;
    });
    deviceFuncArray[0][12] = MIDIFunc({ }, 98, 0, \noteOn, midiInPort.uid).fix;
    deviceFuncArray[0][13] = MIDIFunc({ } , 103, 0, \noteOn, midiInPort.uid).fix;

    //// note off:
    8.do({ | num |
      deviceFuncArray[1][num] = MIDIFunc({ }, num + 58, 0, \noteOff, midiInPort.uid).fix;
    });
    4.do({ | num |
      deviceFuncArray[1][num+8] = MIDIFunc({ }, num + 94, 0, \noteOff, midiInPort.uid).fix;
    });
    deviceFuncArray[1][12] = MIDIFunc({ }, 98, 0, \noteOff, midiInPort.uid).fix;
    deviceFuncArray[1][13] = MIDIFunc({ } , 103, 0, \noteOff, midiInPort.uid).fix;
  }

  prFreeDeviceFuncArray { deviceFuncArray.do({ | f | f.free; }); }

  prMakeControlFuncArray {
    controlFuncArray = Array.fill2D(2, 10, { nil });
    ////// buttons in array:
    // 0: pan
    // 1: sends
    // 2: user
    // 3: play
    // 4: record
    // 5: session
    // 6: metronome
    // 7: tap tempo
    // 8: nudge -
    // 9: nudge +

    //// note on:
    3.do({ | num | controlFuncArray[0][num] = MIDIFunc({ }, num + 87, 0, \noteOn, midiInPort.uid).fix; });
    controlFuncArray[0][3] = MIDIFunc({ }, 91, 0, \noteOn, midiInPort.uid).fix;
    controlFuncArray[0][4] = MIDIFunc({ }, 93, 0, \noteOn, midiInPort.uid).fix;
    controlFuncArray[0][5] = MIDIFunc({ }, 102, 0, \noteOn, midiInPort.uid).fix;
    controlFuncArray[0][6] = MIDIFunc({ }, 90, 0, \noteOn, midiInPort.uid).fix;
    controlFuncArray[0][7] = MIDIFunc({ }, 99, 0, \noteOn, midiInPort.uid).fix;
    controlFuncArray[0][8] = MIDIFunc({ }, 100, 0, \noteOn, midiInPort.uid).fix;
    controlFuncArray[0][9] = MIDIFunc({ }, 101, 0, \noteOn, midiInPort.uid).fix;

    //// note off:
    3.do({ | num | controlFuncArray[1][num] = MIDIFunc({ }, num + 87, 0, \noteOff, midiInPort.uid).fix; });
    controlFuncArray[1][3] = MIDIFunc({ }, 91, 0, \noteOff, midiInPort.uid).fix;
    controlFuncArray[1][4] = MIDIFunc({ }, 93, 0, \noteOff, midiInPort.uid).fix;
    controlFuncArray[1][5] = MIDIFunc({ }, 102, 0, \noteOff, midiInPort.uid).fix;
    controlFuncArray[1][6] = MIDIFunc({ }, 90, 0, \noteOff, midiInPort.uid).fix;
    controlFuncArray[1][7] = MIDIFunc({ }, 99, 0, \noteOff, midiInPort.uid).fix;
    controlFuncArray[1][8] = MIDIFunc({ }, 100, 0, \noteOff, midiInPort.uid).fix;
    controlFuncArray[1][9] = MIDIFunc({ }, 101, 0, \noteOff, midiInPort.uid).fix;
  }

  prFreeControlFuncArray { controlFuncArray.do({ | f | f.free; }); }

  prMakeControlResponders {
    this.prMakeMixerFaderArray;
    this.prMakeMixerEncoderArray;
    this.prMakeDeviceEncoderArray;
  }

  prFreeControlResponders {
    this.prFreeMixerFaderArray;
    this.prFreeMixerEncoderArray;
    this.prFreeDeviceEncoderArray;
  }

  prMakeMixerFaderArray {
    mixerFaderArray = Array.fill(9, { nil });
    8.do({ | num | mixerFaderArray[num] = MIDIFunc({ }, 7, num, \control, midiInPort.uid).fix; });
    //// master mixer:
    mixerFaderArray[8] = MIDIFunc({ }, 14, 0, \control, midiInPort.uid).fix;
  }

  prFreeMixerFaderArray { mixerFaderArray.do({ | f | f.free; }); }

  prMakeMixerEncoderArray {
    mixerEncoderArray = Array.fill(9, { nil });
    8.do({ | num | mixerEncoderArray[num] = MIDIFunc({ }, num + 48, 0, \control, midiInPort.uid).fix; });
    //// cue level:
    mixerEncoderArray[8] = MIDIFunc({ }, 47, 0, \control, midiInPort.uid).fix;
  }

  prFreeMixerEncoderArray { mixerEncoderArray.do({ | f | f.free; }); }

  prMakeDeviceEncoderArray {
    deviceEncoderArray = Array.fill(10, { nil });
    8.do({ | num | deviceEncoderArray[num] = MIDIFunc({ }, num + 16, 0, \control, midiInPort.uid).fix; });
    //// tempo:
    deviceEncoderArray[8] = MIDIFunc({ }, 13, 0, \control, midiInPort.uid).fix;
    //// crossfader:
    deviceEncoderArray[9] = MIDIFunc({ }, 15, 0, \control, midiInPort.uid).fix;
  }

  prFreeDeviceEncoderArray { deviceEncoderArray.do({ | f | f.free; }); }

  prMakeColorArrays {
    // grid color arrays:
    gridColorArray = Array.fill(40, { | num | num });
    sceneLaunchColorArray = Array.fill(5, { | num | num });
    clipStopColorArray = Array.fill(9, { | num | num });

    // mixer color arrays:
    selectColorArray = Array.fill(9, { | num | num });
    mixerColorArray = Array.fill(8, { Array.fill(4, { | num | num }); });

  }

  ////// pages:
  prMakePageDictionary {
    pageDict = IdentityDictionary.new;
    this.makePage('main');
    activePage = pageDict['main'];
    this.setPage('main');
  }

  makePage { | name = 'newPage' |
    pageDict[name] = APC40Mk2_Page.new;
  }

  setPage { | name = 'page' |
    // stops routines that update control surface values on active page:
    activePage.stopActiveBankMonitorRoutines;
    activePage.offLoadFunctionDict.do({ | func | func.value; });

    previousPage = activePageKey;

    activePageKey = name;
    activePage = pageDict[activePageKey];
    //////// note functions:
    //// grid:
    this.prSetAllGridFuncs;
    //// scene launch:
    this.prSetAllSceneLaunchFuncs;
    //// clip stop:
    this.prSetAllClipStopFuncs;
    //// Track Select:
    this.prSetAllTrackSelectFuncs;
    //// TrackActivate:
    this.prSetAllTrackActivatorFuncs;
    //// Crossfade Select:
    this.prSetAllCrossfaderSelectFuncs;
    //// Solo:
    this.prSetAllSoloFuncs;
    //// Record Enable:
    this.prSetAllRecordEnableFuncs;
    //// device buttons:
    this.prSetAllDeviceButtonFuncs;
    //// Control Buttons:
    this.prSetAllControlButtonFuncs;

    //////// cc functions:
    //// mixer faders:
    this.prSetAllFaderFuncs;
    //// mixer encoders:
    this.prSetAllMixerEncoderFuncs;

    //// device encoders:
    this.prSetAllDeviceEncoderFuncs;

    // starts routines that update control surface values on active page:
    activePage.startActiveBankMonitorRoutines;
    activePage.loadFunctionDict.do({ | func | func.value; });

  }

  addPageLoadFunction { | name, func, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addLoadFunction(name, func);
  }

  addPageOffLoadFunction { | name func, page = 'active' |
    if( page == 'active', { page = activePageKey; });
    pageDict[page].addOffLoadFunction(name, func);
  }

  setToPreviousPage { this.setPage(previousPage); }


}