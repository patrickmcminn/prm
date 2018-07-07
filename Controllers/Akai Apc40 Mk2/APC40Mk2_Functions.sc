/*
Monday, May 28th 2018
APC40Mk2_Functions.sc
prm
*/

+ APC40Mk2 {

  //////////////////////////
  /////// Functions ///////
  ////////////////////////

  prSetAllGridFuncs {
    40.do({ | func | this.prSetGridFunc(func, 'noteOn'); this.prSetGridFunc(func, 'noteOff'); });
  }
  prSetGridFunc { | num = 0, type = 'noteOn' |
    switch(type,
      \noteOn, { gridFuncArray[0][num].prFunc_(activePage.getGridFunc(0, num)) },
      \noteOff,{ gridFuncArray[1][num].prFunc_(activePage.getGridFunc(1, num)) }
    );
  }
  setGridFunc { | column = 0, row = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var num = ((row * 8) + column);
    if( page == 'active', { page = activePageKey });
    pageDict[page].setGridFunc(column, row, func, type, bank);
    //this.setFunc(num, activePage.getFunc(num));
    this.prSetGridFunc(num, type);
  }

  prSetAllSceneLaunchFuncs {
    5.do({ | func | this.prSetSceneLaunchFunc(func, 'noteOn'); this.prSetSceneLaunchFunc(func, 'noteOff'); });
  }
  prSetSceneLaunchFunc { | num, type |
    switch(type,
      \noteOn, { sceneLaunchFuncArray[0][num].prFunc_(activePage.getSceneLaunchFunc(0, num)) },
      \noteOff, { sceneLaunchFuncArray[1][num].prFunc_(activePage.getSceneLaunchFunc(1, num)) }
    );
  }
  setSceneLaunchFunc { | num = 0, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( num < 5, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setSceneLaunchFunc(num, func, type, bank);
      this.prSetSceneLaunchFunc(num, type);
      },
      { "out of range!".postln; });
  }

  prSetAllClipStopFuncs {
    9.do({ | func | this.prSetClipStopFunc(func, 'noteOn'); this.prSetClipStopFunc(func, 'noteOff'); });
  }
  prSetClipStopFunc { | num, type |
    switch(type,
      \noteOn, { clipStopFuncArray[0][num].prFunc_(activePage.getClipStopFunc(0, num)) },
      \noteOff, { clipStopFuncArray[1][num].prFunc_(activePage.getClipStopFunc(1, num)) }
    );
  }

  setClipStopFunc { | num = 1 , func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num-1;
    if( num <= 10, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setClipStopFunc(number, func, type, bank);
      this.prSetClipStopFunc(number, type);

      },
      { "out of range!".postln; });
  }

  ////// track select:
  prSetAllTrackSelectFuncs {
    9.do({ | func | this.prSetTrackSelectFunc(func, 'noteOn'); this.prSetTrackSelectFunc(func, 'noteOn'); });
  }
  prSetTrackSelectFunc { | num, type |
    switch(type,
      \noteOn, { trackSelectFuncArray[0][num].prFunc_(activePage.getTrackSelectFunc(0, num)) },
      \noteOff, { trackSelectFuncArray[1][num].prFunc_(activePage.getTrackSelectFunc(1, num)) });
  }
  setTrackSelectFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num - 1;
    if( num < 9, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setTrackSelectFunc(number, func, type, bank);
      this.prSetTrackSelectFunc(number, type);
    }, { "out of range!" });
  }

  ////// track activator:
  prSetAllTrackActivatorFuncs {
    9.do({ | func | this.prSetTrackActivatorFunc(func, 'noteOn'); this.prSetTrackActivatorFunc(func, 'noteOff'); });
  }
  prSetTrackActivatorFunc { | num, type |
    switch(type,
      \noteOn, { trackActivatorFuncArray[0][num].prFunc_(activePage.getTrackActivatorFunc(0, num)) },
      \noteOff, { trackActivatorFuncArray[1][num].prFunc_(activePage.getTrackActivatorFunc(1, num)) });
  }
  setTrackActivatorFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num-1;
    if( num < 8, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setTrackActivatorFunc(number, func, type, bank);
      this.prSetTrackActivatorFunc(number, type);
    }, { "out of range!".postln; });
  }

  ////// crossfader select:
  prSetAllCrossfaderSelectFuncs {
    8.do({ | func | this.prSetCrossfaderSelectFunc(func, 'noteOn'); this.prSetCrossfaderSelectFunc(func, 'noteOff'); });
  }
  prSetCrossfaderSelectFunc { | num, type |
    switch(type,
      \noteOn, { crossfaderSelectFuncArray[0][num].prFunc_(activePage.getCrossfaderSelectFunc(0, num)) },
      \noteOff, { crossfaderSelectFuncArray[1][num].prFunc_(activePage.getCrossfaderSelectFunc(1, num )) });
  }
  setCrossfaderSelectFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num - 1;
    if( num < 8, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setCrossfaderSelectFunc(number, func, type, bank);
      this.prSetCrossfaderSelectFunc(number, type);
    }, { "out of range".postln; });
  }

  /////// solo funcs:
  prSetAllSoloFuncs {
    8.do({ | func | this.prSetSoloFunc(func, 'noteOn'); this.prSetSoloFunc(func, 'noteOff'); });
  }
  prSetSoloFunc { | num, type |
    switch(type,
      \noteOn, { soloFuncArray[0][num].prFunc_(activePage.getSoloFunc(0, num)) },
      \noteOff, { soloFuncArray[1][num].prFunc_(activePage.getSoloFunc(1, num)) });
  }
  setSoloFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num - 1;
    if( num < 8, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setSoloFunc(number, func, type, bank);
      this.prSetSoloFunc(number, type);
    }, { "out of range!" });
  }

  ////// record enable funcs:
  prSetAllRecordEnableFuncs {
    8.do({ | func | this.prSetRecordEnableFunc(func, 'noteOn'); this.prSetRecordEnableFunc(func, 'noteOff'); });
  }
  prSetRecordEnableFunc { | num, type |
    switch(type,
      \noteOn, { recordEnableFuncArray[0][num].prFunc_(activePage.getRecordEnableFunc(0, num)) },
      \noteOff, { recordEnableFuncArray[1][num].prFunc_(activePage.getRecordEnableFunc(1, num)) });
  }
  setRecordEnableFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num - 1;
    if( num < 8, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setRecordEnableFunc(number, func, type, bank);
      this.prSetRecordEnableFunc(number, type);
    }, { "out of range!" });
  }

  ////// device buttons:
  prSetAllDeviceButtonFuncs {
    14.do({ | func | this.prSetDeviceButtonFunc(func, 'noteOn'); this.prSetDeviceButtonFunc(func, 'noteOff'); });
  }
  prSetDeviceButtonFunc { | num, type |
    switch(type,
      \noteOn, { deviceFuncArray[0][num].prFunc_(activePage.getDeviceButtonFunc(0, num)) },
      \noteOff, { deviceFuncArray[1][num].prFunc_(activePage.getDeviceButtonFunc(1, num)) });
  }
  setDeviceButtonFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num - 1;
    if( num < 8, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setDeviceButtonFunc(number, func, type, bank);
      this.prSetDeviceButtonFunc(number, type);
    }, { "out of range!" });
  }
  setBankSelectFunc { | direction = 'up', func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number;
    switch(direction,
      'up', { number = 8 },
      'down', { number = 9 },
      'right', { number = 10 },
      'left', { number = 11 }
    );
    if( page == 'active', { page = activePageKey });
    pageDict[page].setDeviceButtonFunc(number, func, type, bank);
    this.prSetDeviceButtonFunc(number, type);
  }
  setShiftButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setDeviceButtonFunc(12, func, type, bank);
    this.prSetDeviceButtonFunc(12, type);
  }
  setBankButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setDeviceButtonFunc(13, func, type, bank);
    this.prSetDeviceButtonFunc(13, type);
  }

  ////// control button funcs:
  prSetAllControlButtonsFuncs {
    10.do({ | func | this.prSetControlButtonFunc(func, 'noteOn'); this.prSetControlButtonFunc(func, 'noteOff'); });
  }
  prSetControlButtonFunc { | num, type |
    switch(type,
      \noteOn, { controlFuncArray[0][num].prFunc_(activePage.getControlButtonFunc(0, num)) },
      \noteOff, { controlFuncArray[1][num].prFunc_(activePage.getControlButtonFunc(1, num)) });
  }
  setPanButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(0, func, type, bank);
    this.prSetControlButtonFunc(0, type);
  }
  setSendsButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(1, func, type, bank);
    this.prSetControlButtonFunc(1, type);
  }
  setUserButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(2, func, type, bank);
    this.prSetControlButtonFunc(2, type);
  }
  setPlayButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(3, func, type, bank);
    this.prSetControlButtonFunc(3, type);
  }
  setRecordButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(4, func, type, bank);
    this.prSetControlButtonFunc(4, type);
  }
  setSessionButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(5, func, type, bank);
    this.prSetControlButtonFunc(5, type);
  }
  setMetronomeButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(6, func, type, bank);
    this.prSetControlButtonFunc(6, type);
  }
  setTapTempoButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(7, func, type, bank);
    this.prSetControlButtonFunc(7, type);
  }
  setNudgeDownButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(8, func, type, bank);
    this.prSetControlButtonFunc(8, type);
  }
  setNudgeUpButtonFunc { | func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(9, func, type, bank);
    this.prSetControlButtonFunc(9, type);
  }

  ///////////////////////////////
  ////// Control Funtions: //////
  ///////////////////////////////

  prSetAllFaderFuncs {
    9.do({ | func | this.prSetFaderFunc(func); });
  }
  prSetFaderFunc { | num |
    mixerFaderArray[num].prFunc_(activePage.getFaderFunc(num));
  }
  setFaderFunc { | fader = 1, func = nil, bank = 'active', page = 'active' |
    var faderIndex = fader-1;
    if( page == 'active', { page = activePageKey });
    pageDict[page].setFaderFunc(faderIndex, func, bank);
    this.prSetFaderFunc(faderIndex);
  }

  prSetAllMixerEncoderFuncs {
    9.do({ | func | this.prSetMixerEncoderFunc(func); });
  }
  prSetMixerEncoderFunc { | num |
    mixerEncoderArray[num].prFunc_(activePage.getMixerEncoderFunc(num));
  }
  setMixerEncoderFunc { | encoder = 1, func = nil, bank = 'active', page = 'active' |
    var encoderIndex = encoder-1;
    if( page == 'active', { page = activePageKey });
    pageDict[page].setMixerEncoderFunc(encoderIndex, func, bank);
    this.prSetMixerEncoderFunc(encoderIndex);
  }
  setCueLevelEncoderFunc { | func = nil, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setMixerEncoderFunc(8, func, bank);
    this.prSetMixerEncoderFunc(8);
  }

  prSetAllDeviceEncoderFuncs {
    10.do({ | func | this.prSetDeviceEncoderFunc(func); });
  }
  prSetDeviceEncoderFunc { | num |
    deviceEncoderArray[num].prFunc_(activePage.getDeviceEncoderFunc(num));
  }
  setDeviceEncoderFunc{ | encoder = 1, func = nil, bank = 'active', page = 'active' |
    var encoderIndex = encoder-1;
    if( page == 'active', { page = activePageKey });
    pageDict[page].setDeviceEncoderFunc(encoderIndex, func, bank);
    this.prSetDeviceEncoderFunc(encoderIndex);
  }

  setTempoEncoderFunc { | func = nil, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setDeviceEncoderFunc(8, func, bank);
    this.prSetDeviceEncoderFunc(8);
  }

  setCrossfaderFunc { | func = nil, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setDeviceEncoderFunc(9, func, bank);
    this.prSetDeviceEncoderFunc(9);
  }


  ////////////////////////////////
  ////// Grid Monitor Funcs: //////
  ////////////////////////////////


}

