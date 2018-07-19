/*
Monday, May 28th 2018
APC40Mk2_Page.sc
prm
*/

APC40Mk2_Page {

  var <gridFuncArray, sceneLaunchFuncArray, clipStopFuncArray;
  var trackSelectFuncArray, trackActivatorFuncArray, crossfaderSelectFuncArray;
  var soloFuncArray, recordEnableFuncArray,  deviceFuncArray, controlFuncArray;
  var mixerFaderArray, mixerEncoderArray, deviceEncoderArray;

  var <gridColorArray, <sceneLaunchColorArray, <clipStopColorArray;
  var <selectColorArray, <trackActivatorColorArray, <crossfadeSelectColorArray;
  var <soloColorArray, <recordEnableColorArray, <deviceColorArray, <controlColorArray;
  var <mixerEncoderValueArray, <deviceEncoderValueArray;

  var <gridBankArray, <sceneLaunchBankArray, <clipStopBankArray, <mixerBankArray;
  var <mixerEncoderBankArray, <deviceEncoderBankArray, <deviceButtonsBankArray, <controlButtonsBankArray;

  var <gridBankMonitorFuncArray, <sceneLaunchBankMonitorFuncArray, <clipStopBankMonitorFuncArray;
  var <mixerBankMonitorFuncArray, <mixerEncoderBankMonitorFuncArray, <deviceEncoderBankMonitorFuncArray;
  var <deviceButtonsBankMonitorFuncArray, <controlButtonsBankMonitorFuncArray;

  var <activeGridBnk, <activeSceneLaunchBnk, <activeClipStopBnk, <activeMixerBnk;
  var <activeMixerEncoderBnk, <activeDeviceEncoderBnk, <activeDeviceButtonsBnk, <activeControlButtonsBnk;

  var <loadFunctionDict, <offLoadFunctionDict;

  *new { ^super.new.prInit }

  prInit {
    loadFunctionDict = IdentityDictionary.new;
    offLoadFunctionDict = IdentityDictionary.new;
    this.prMakeResponders;
    this.prMakeColorArrays;
    this.prMakeValueArrays;
    this.prMakeBanks;
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
    gridFuncArray = Array.fill2D(2, 40, { { }; });
    sceneLaunchFuncArray = Array.fill2D(2, 5, { { }; });
    clipStopFuncArray = Array.fill2D(2, 9, { { }; });
    trackSelectFuncArray = Array.fill2D(2, 9, { { }; });
    trackActivatorFuncArray = Array.fill2D(2, 8, { { }; });
    crossfaderSelectFuncArray = Array.fill2D(2, 8, { { }; });
    soloFuncArray = Array.fill2D(2, 8, { { }; });
    recordEnableFuncArray = Array.fill2D(2, 8, { { }; });
    deviceFuncArray = Array.fill2D(2, 14, { { }; });
    controlFuncArray = Array.fill2D(2, 10, { { }; });
  }

  prFreeNoteResponders {
    gridFuncArray.do({ | f | f.free; });
    sceneLaunchFuncArray.do({ | f | f.free; });
    clipStopFuncArray.do({ | f | f.free; });
    trackSelectFuncArray.do({ | f | f.free; });
    trackActivatorFuncArray.do({ | f | f.free; });
    crossfaderSelectFuncArray.do({ | f | f.free; });
    soloFuncArray.do({ | f | f.free; });
    recordEnableFuncArray.do({ | f | f.free; });
    deviceFuncArray.do({ | f | f.free; });
    controlFuncArray.do({ | f | f.free; });
  }

  prMakeControlResponders {
    mixerFaderArray = Array.fill(9, { { }; });
    mixerEncoderArray = Array.fill(9, { { }; });
    deviceEncoderArray = Array.fill(10, { { }; });
  }

  prFreeControlResponders {
    mixerFaderArray.do({ | f | f.free; });
    mixerEncoderArray.do({ | f | f.free; });
    deviceEncoderArray.do({ | f | f.free; });
  }

  prMakeColorArrays {
    // grid color arrays:
    gridColorArray = Array.fill(40, { 0 });
    sceneLaunchColorArray = Array.fill(5, { 0 });
    clipStopColorArray = Array.fill(9, { 0 });

    // mixer color arrays:
    selectColorArray = Array.fill(9, { 0 });
    trackActivatorColorArray = Array.fill(9, {  0 });
    crossfadeSelectColorArray = Array.fill(9, { 0 });
    soloColorArray = Array.fill(9, { 0 });
    recordEnableColorArray = Array.fill(9, { 0 });

    // device and control:
    deviceColorArray = Array.fill(14, { 0 });
    controlColorArray = Array.fill(10, {0 });
  }

  prMakeValueArrays {
    mixerEncoderValueArray = Array.fill(8, { nil });
    deviceEncoderValueArray = Array.fill(8, { nil });
  }

  prFreeValueArrays {
    mixerEncoderValueArray.do({ |f | f.free; });
    deviceEncoderValueArray.do({ | f | f.free; });
  }

  ////////////////////////////////////
  //// Function Setting: ////
  //////////////////////////////////

  ////////////////////////////////
  ///// Function Reporting: /////
  //////////////////////////////

  getGridFunc { | num, type |
    switch(type,
      \noteOn, { ^gridFuncArray[0][num] },
      \noteOff, { ^gridFuncArray[1][num] });
  }
  getSceneLaunchFunc { | num, type |
    switch(type,
      \noteOn, { ^sceneLaunchFuncArray[0][num] },
      \noteOff, { ^sceneLaunchFuncArray[1][num] });
  }
  getClipStopFunc { | num, type |
    switch(type,
      \noteOn, { ^clipStopFuncArray[0][num] },
      \noteOff, { ^clipStopFuncArray[1][num] });
  }
  getTrackSelectFunc { | num, type |
    switch(type,
      \noteOn, { ^trackSelectFuncArray[0][num] },
      \noteOff, { ^trackSelectFuncArray[1][num] });
  }
  getTrackActivatorFunc { | num, type |
    switch(type,
      \noteOn, { ^trackActivatorFuncArray[0][num] },
      \noteOff, { ^trackActivatorFuncArray[1][num] });
  }
  getCrossfaderSelectFunc { | num, type |
    switch(type,
      \noteOn, { ^crossfaderSelectFuncArray[0][num] },
      \noteOff, { ^crossfaderSelectFuncArray[1][num] });
  }
  getSoloFunc { | num, type |
    switch(type,
      \noteOn, { ^soloFuncArray[0][num] },
      \noteOff, { ^soloFuncArray[1][num] });
  }
  getRecordEnableFunc { | num, type |
    switch(type,
      \noteOn, { ^recordEnableFuncArray[0][num] },
      \noteOff, { ^recordEnableFuncArray[1][num] });
  }
  getDeviceButtonFunc { | num, type |
    switch(type,
      \noteOn, {^deviceFuncArray[0][num] },
      \noteOff, { ^deviceFuncArray[1][num] });
  }
  getControlButtonFunc { | num, type |
    switch(type,
      \noteOn, { ^controlFuncArray[0][num] },
      \noteOff, { ^controlFuncArray[1][num] });
  }
  getFaderFunc { | num | ^mixerFaderArray[num] }
  getMixerEncoderFunc { | num | ^mixerEncoderArray[num] }
  getMixerEncoderValue { | num | ^mixerEncoderValueArray[num] }
  getDeviceEncoderFunc{ | num | ^deviceEncoderArray[num] }
  getDeviceEncoderValue { | num | ^deviceEncoderValueArray[num] }

  /////// note funcs:

  prSetGridFunc { | num = 0, type = 'noteOn', func = nil |
    switch(type,
      \noteOn, { gridFuncArray[0][num] = func },
      \noteOff,{ gridFuncArray[1][num] = func }
    );
  }

  setGridFunc { | column = 0, row = 0, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    var num = ((row * 8) + column);
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeGridBnk }, { bankSelect = bank });
    if( (row >5) || (column > 7), { "out of range!"}, {
      switch(type,
        \noteOn, {
          gridBankArray[bankSelect][num][0] = func;
          if( bank == 'active', { this.prSetGridFunc(num, type, func); }); },
        \noteOff, {
          gridBankArray[bankSelect][num][1] = func;
          if( bank == 'active', { this.prSetGridFunc(num, type, func); }); }
      );
    });
  }

  prSetSceneLaunchFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { sceneLaunchFuncArray[0][num] = func },
      \noteOff, { sceneLaunchFuncArray[1][num] = func }
    );
  }

  setSceneLaunchFunc { | num = 0, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == activeSceneLaunchBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeSceneLaunchBnk }, { bankSelect = bank });
    if( num >5, { "out of range!"}, {
      switch(type,
        \noteOn, {
          sceneLaunchBankArray[bankSelect][num][0] = func;
          if( bank == 'active', { this.prSetSceneLaunchFunc(num, type, func); }); },
        \noteOff, {
          sceneLaunchBankArray[bankSelect][num][1] = func;
          if( bank == 'active', { this.prSetSceneLaunchFunc(num, type, func); }); }
      );
    });
  }

  prSetClipStopFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { clipStopFuncArray[0][num] = func},
      \noteOff, { clipStopFuncArray[1][num] = func }
    );
  }

  setClipStopFunc { | num = 0, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == activeClipStopBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeClipStopBnk }, { bankSelect = bank });
    if( num >10, { "out of range!"}, {
      switch(type,
        \noteOn, {
          clipStopBankArray[bankSelect][num][0] = func;
          if( bank == 'active', { this.prSetClipStopFunc(num, type, func); }); },
        \noteOff, {
          clipStopBankArray[bankSelect][num][1] = func;
          if( bank == 'active', { this.prSetClipStopFunc(num, type, func); }); }
      );
    });
  }

  prSetTrackSelectFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { trackSelectFuncArray[0][num] = func },
      \noteOff, { trackSelectFuncArray[1][num] = func });
  }

  setTrackSelectFunc { | num = 0, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == activeMixerBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeMixerBnk }, { bankSelect = bank });
    if( num > 9, { "out of range!" }, {
      switch(type,
        \noteOn, {
          mixerBankArray[bankSelect][1][num][0] = func;
          if( bank == 'active', { this.prSetTrackSelectFunc(num, 'noteOn', func); }); },
        \noteOff, {
          mixerBankArray[bankSelect][1][num][1] = func;
          if( bank == 'active', { this.prSetTrackSelectFunc(num, 'noteOff', func);}); }
      );
    });
  }

  prSetTrackActivatorFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { trackActivatorFuncArray[0][num] = func },
      \noteOff, { trackActivatorFuncArray[1][num] = func });
  }
  setTrackActivatorFunc { | num = 1, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == activeMixerBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeMixerBnk }, { bankSelect = bank });
    if( num > 9, { "out of range!" }, {
      switch(type,
        \noteOn, {
          mixerBankArray[bankSelect][2][num][0] = func;
          if( bank == 'active', { this.prSetTrackActivatorFunc(num, 'noteOn', func); }); },
        \noteOff, {
          mixerBankArray[bankSelect][2][num][1] = func;
          if( bank == 'active', { this.prSetTrackActivatorFunc(num, 'noteOff', func); }); }
      );
    });
  }

  prSetCrossfaderSelectFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { crossfaderSelectFuncArray[0][num] = func },
      \noteOff, { crossfaderSelectFuncArray[1][num] = func });
  }
  setCrossfaderSelectFunc { | num = 1, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == activeMixerBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeMixerBnk }, { bankSelect = bank });
    if( num > 9, { "out of range!" }, {
      switch(type,
        \noteOn, {
          mixerBankArray[bankSelect][3][num][0] = func;
          if( bank == 'active', { this.prSetCrossfaderSelectFunc(num, 'noteOn', func); }); },
        \noteOff, {
          mixerBankArray[bankSelect][3][num][1] = func;
          if( bank == 'active', { this.prSetCrossfaderSelectFunc(num, 'noteOff', func); }); }
      );
    });
  }

  prSetSoloFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { soloFuncArray[0][num] = func },
      \noteOff, { soloFuncArray[1][num] = func });
  }
  setSoloFunc { | num = 1, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == activeMixerBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeMixerBnk }, { bankSelect = bank });
    if( num > 9, { "out of range!" }, {
      switch(type,
        \noteOn, {
          mixerBankArray[bankSelect][4][num][0] = func;
          if( bank == 'active', { this.prSetSoloFunc(num, 'noteOn', func); }); },
        \noteOff, {
          mixerBankArray[bankSelect][4][num][1] = func;
          if( bank == 'active', { this.prSetSoloFunc(num, 'noteOff', func); }); }
      );
    });
  }

  prSetRecordEnableFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { recordEnableFuncArray[0][num] = func },
      \noteOff, { recordEnableFuncArray[1][num] = func });
  }
  setRecordEnableFunc { | num = 1, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == activeMixerBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeMixerBnk }, { bankSelect = bank });
    if( num > 9, { "out of range!" }, {
      switch(type,
        \noteOn, {
          mixerBankArray[bankSelect][5][num][0] = func;
          if( bank == 'active', { this.prSetRecordEnableFunc(num, 'noteOn', func); }); },
        \noteOff, {
          mixerBankArray[bankSelect][5][num][1] = func;
          if( bank == 'active', { this.prSetRecordEnableFunc(num, 'noteOff', func); }); }
      );
    });
  }

  prSetDeviceButtonFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { deviceFuncArray[0][num] = func },
      \noteOff, { deviceFuncArray[1][num] = func });
  }
  setDeviceButtonFunc { | num = 1, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == activeMixerBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeDeviceButtonsBnk }, { bankSelect = bank });
    switch(type,
      \noteOn, {
        deviceButtonsBankArray[bankSelect][num][0] = func;
        if( bank == 'active', { this.prSetDeviceButtonFunc(num, 'noteOn', func); }); },
      \noteOff, {
        deviceButtonsBankArray[bankSelect][num][1] = func;
        if( bank == 'active', { this.prSetDeviceButtonFunc(num, 'noteOff', func); }); }
    );
  }

  prSetControlButtonFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { controlFuncArray[0][num] = func },
      \noteOff, { controlFuncArray[1][num] = func });
  }
  setControlButtonFunc { | num = 1, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == activeMixerBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeControlButtonsBnk }, { bankSelect = bank });
    switch(type,
      \noteOn, {
        controlButtonsBankArray[bankSelect][num][0] = func;
        if( bank == 'active', { this.prSetControlButtonFunc(num, 'noteOn', func); }); },
      \noteOff, {
        controlButtonsBankArray[bankSelect][num][1] = func;
        if( bank == 'active', { this.prSetControlButtonFunc(num, 'noteOff', func); }); }
    );
  }

  //////// control funcs:
  prSetFaderFunc { | num, func = nil|
    mixerFaderArray[num] = func;
  }
  setFaderFunc { | num = 1, func, bank = 'active' |
    var bankSelect;
    if( bank == activeMixerBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeMixerBnk }, { bankSelect = bank });
    mixerBankArray[bankSelect][0][num][0] = func;
    if( bank == 'active', { this.prSetFaderFunc(num, func) });
  }

  prSetMixerEncoderFunc { | num, func = nil |
    mixerEncoderArray[num] = func;
  }
  setMixerEncoderFunc { | num = 1, func, bank = 'active' |
    var bankSelect;
    if( bank == activeMixerEncoderBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeMixerEncoderBnk }, { bankSelect = bank });
    mixerEncoderBankArray[bankSelect][num][0] = func;
    if( bank == 'active', { this.prSetMixerEncoderFunc(num, func); });
  }

  prSetDeviceEncoderFunc { | num, func = nil |
    deviceEncoderArray[num] = func;
  }
  setDeviceEncoderFunc { | num = 1, func, bank = 'active' |
    var bankSelect;
    if( bank == activeMixerEncoderBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeDeviceEncoderBnk }, { bankSelect = bank });
    deviceEncoderBankArray[bankSelect][num][0] = func;
    if( bank == 'active', { this.prSetDeviceEncoderFunc(num, func); });
  }

  /////////////////////////////////
  ///////// Monitor Funcs ////////
  ///////////////////////////////

  setGridMonitorFunc { | name, func, bank = 'active' |
    var bankSelect;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeGridBnk }, { bankSelect = bank });
    if( gridBankMonitorFuncArray[bankSelect][name].isNil == false, { gridBankMonitorFuncArray[bankSelect][name].stop; });
    gridBankMonitorFuncArray[bankSelect][name] = r {
      loop {
        func.value;
        0.05.wait;
      }
    };
  }

  setSceneLaunchMonitorFunc { | name, func, bank = 'active' |
    var bankSelect;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeSceneLaunchBnk }, { bankSelect = bank });
    if(
      sceneLaunchBankMonitorFuncArray[bankSelect][name].isNil == false,
      { sceneLaunchBankMonitorFuncArray[bankSelect][name].stop; });
    sceneLaunchBankMonitorFuncArray[bankSelect][name] = r {
      loop {
        func.value;
        0.05.wait;
      }
    };
  }

  setClipStopMonitorFunc { | name, func, bank = 'active' |
    var bankSelect;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeClipStopBnk }, { bankSelect = bank });
    if( clipStopBankMonitorFuncArray[bankSelect][name].isNil == false, { clipStopBankMonitorFuncArray[bankSelect][name].stop });
    clipStopBankMonitorFuncArray[bankSelect][name] = r {
      loop {
        func.value;
        0.05.wait;
      }
    };
  }

  setMixerMonitorFunc { | name, func, bank = 'active' |
    var bankSelect;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeMixerBnk }, { bankSelect = bank });
    if( mixerBankMonitorFuncArray[bankSelect][name].isNil == false, { mixerBankMonitorFuncArray[bankSelect][name].stop; });
    mixerBankMonitorFuncArray[bankSelect][name] = r {
      loop {
        func.value;
        0.05.wait;
      }
    };
  }

  setDeviceButtonsMonitorFunc { | name, func, bank = 'active' |
    var bankSelect;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeDeviceButtonsBnk }, { bankSelect = bank });
    if( deviceButtonsBankMonitorFuncArray[bankSelect][name].isNil == false,
      { deviceButtonsBankMonitorFuncArray[bankSelect][name].stop; });
    deviceButtonsBankMonitorFuncArray[bankSelect][name] = r {
      loop {
        func.value;
        0.05.wait;
      }
    };
  }

  setControlButtonsMonitorFunc { | name, func, bank = 'active' |
    var bankSelect;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeControlButtonsBnk }, { bankSelect = bank });
    if( controlButtonsBankMonitorFuncArray[bankSelect][name].isNil == false,
      { controlButtonsBankMonitorFuncArray[bankSelect][name].stop });
    controlButtonsBankMonitorFuncArray[bankSelect][name] = r {
      loop {
        func.value;
        0.05.wait;
      }
    };
  }

  setMixerEncodersMonitorFunc  { | name, func, bank = 'active' |
    var bankSelect;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeMixerEncoderBnk }, { bankSelect = bank });
    if( mixerEncoderBankMonitorFuncArray[bankSelect][name].isNil == false,
      { mixerEncoderBankMonitorFuncArray[bankSelect][name].stop; });
    mixerEncoderBankMonitorFuncArray[bankSelect][name] = r {
      loop {
        func.value;
        0.05.wait;
      }
    };
  }

  setDeviceEncodersMonitorFunc { | name, func, bank = 'active' |
    var bankSelect;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeDeviceEncoderBnk }, { bankSelect = bank });
    if ( deviceEncoderBankMonitorFuncArray[bankSelect][name].isNil == false,
      { deviceEncoderBankMonitorFuncArray[bankSelect][name].stop; });
    deviceEncoderBankMonitorFuncArray[bankSelect][name] = r {
      loop {
        func.value;
        0.05.wait;
      }
    };
  }
}