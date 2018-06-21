/*
Monday, May 21st 2018
APC40Mk2.sc
prm
*/

APC40Mk2 {

  var midiInPort, midiOutPort;
  var gridFuncArray, sceneLaunchFuncArray, clipStopFuncArray;
  var trackSelectFuncArray, trackActivatorFuncArray, crossfadeSelectFuncArray;
  var soloFuncArray, recordEnableFuncArray,  deviceFuncArray, controlFuncArray;
  var controlFuncArray;
  var slidersFuncArray, knobsFuncArray;
  var <pageDict, <activePage, <activePageKey, <storageDict, <previousPage;
  var <gridColorArray, <gridLaunchColorArray, <gridStopColorArray;
  var <selectColorArray, <mixerColorArray;
  var <activeGridBank, <activeMixerBank, <activeMixerEncoderBank;
  var <activeDeviceEncoderBank, <activeControlButtonsBank;

  *new { | deviceName = "APC40 mkII", portName = "APC40 mkII" |
    ^super.new.prInit(deviceName, portName);
  }

  prInit { | deviceName = "APC40 mkII", portName = "APC40 mkII" |
    this.prInitMIDI(deviceName, portName);
    this.prMakeResponders;
    //this.prMakeColorArray;
    this.prMakePageDictionary;
    storageDict = IdentityDictionary.new(know: true);
  }

  prInitMIDI { | deviceName = "APC40 mkII", portName = "APC40 mkII" |
    MIDIIn.connectAll;
    midiInPort = MIDIIn.findPort(deviceName, portName);
    midiOutPort = MIDIOut.newByName(deviceName, portName);
    midiOutPort.latency = 0;

    /////// set to control mode:
    midiOutPort.sysex(Int8Array[0xf0, 0x47, 0x01, 0x29, 0x06, 0x60, 0x00, 0x04, 0x42, 0x01, 0x01, 0x01, 0xf7]);
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
    this.prMakeCrossfadeSelectFuncArray;
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
    this.prFreeCrossfadeSelectFuncArray;
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

  prMakeCrossfadeSelectFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    crossfadeSelectFuncArray = Array.fill2D(2, 8, { nil });
    8.do({ | num |
      crossfadeSelectFuncArray[0][num] = MIDIFunc({ }, 66, num, \noteOn, midiInPort.uid).fix;
    });
    8.do({ | num |
      crossfadeSelectFuncArray[1][num] = MIDIFunc({ }, 66, num, \noteOff, midiInPort.uid).fix;
    });
  }

  prFreeCrossfadeSelectFuncArray { crossfadeSelectFuncArray.do({ | f | f.free; }); }

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

  }

  prMakeColorArrays {
    // grid color arrays:
    gridColorArray = Array.fill(40, { | num | num });
    gridLaunchColorArray = Array.fill(5, { | num | num });
    gridStopColorArray = Array.fill(9, { | num | num });

    // mixer color arrays:
    selectColorArray = Array.fill(9, { | num | num });
    mixerColorArray = Array.fill(8, { Array.fill(4, { | num | num }); });

    // control color arrays:

  }

  prMakePageDictionary {
    pageDict = IdentityDictionary.new;
    this.makePage('main');
    activePage = pageDict['main'];
    this.setPage('main');
  }

  /////// Setting Functions:
/*
  prSetGridFunc { | num = 0, type = 'noteOn', func = nil |
    switch(type,
      { \noteOn }, { noteOnGridFuncArray[num].prFunc_(func); },
      { \noteOff }, { noteOffGridFuncArray[num].prFunc_(func); }
    );
  }

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
  */

}