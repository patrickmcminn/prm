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
      { 'noteOn' }, { gridFuncArray[0][num].prFunc_(activePage.getGridFunc(num, 'noteOn')) },
      { 'noteOff' },{ gridFuncArray[1][num].prFunc_(activePage.getGridFunc(num, 'noteOff')) }
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
      \noteOn, { sceneLaunchFuncArray[0][num].prFunc_(activePage.getSceneLaunchFunc(num, 'noteOn')) },
      \noteOff, { sceneLaunchFuncArray[1][num].prFunc_(activePage.getSceneLaunchFunc(num, 'noteOff')) }
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
      \noteOn, { clipStopFuncArray[0][num].prFunc_(activePage.getClipStopFunc(num, 'noteOn')) },
      \noteOff, { clipStopFuncArray[1][num].prFunc_(activePage.getClipStopFunc(num, 'noteOff')) }
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
      \noteOn, { trackSelectFuncArray[0][num].prFunc_(activePage.getTrackSelectFunc(num, 'noteOn')) },
      \noteOff, { trackSelectFuncArray[1][num].prFunc_(activePage.getTrackSelectFunc(num, 'noteOff')) });
  }
  setTrackSelectFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num - 1;
    if( num < 10, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setTrackSelectFunc(number, func, type, bank);
      this.prSetTrackSelectFunc(number, type);
    }, { "out of range!" });
  }

  ////// track activator:
  prSetAllTrackActivatorFuncs {
    8.do({ | func | this.prSetTrackActivatorFunc(func, 'noteOn'); this.prSetTrackActivatorFunc(func, 'noteOff'); });
  }
  prSetTrackActivatorFunc { | num, type |
    switch(type,
      \noteOn, { trackActivatorFuncArray[0][num].prFunc_(activePage.getTrackActivatorFunc(num, 'noteOn')) },
      \noteOff, { trackActivatorFuncArray[1][num].prFunc_(activePage.getTrackActivatorFunc(num, 'noteOff')) });
  }
  setTrackActivatorFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num-1;
    if( num < 9, {
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
      \noteOn, { crossfaderSelectFuncArray[0][num].prFunc_(activePage.getCrossfaderSelectFunc(num, 'noteOn')) },
      \noteOff, { crossfaderSelectFuncArray[1][num].prFunc_(activePage.getCrossfaderSelectFunc(num, 'noteOff' )) });
  }
  setCrossfaderSelectFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num - 1;
    if( num < 9, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setCrossfaderSelectFunc(number, func, type, bank);
      this.prSetCrossfaderSelectFunc(number, type);
    }, { "out of range".postln; });
  }

  /////// solo funcs:
  prSetAllSoloButtonFuncs {
    8.do({ | func | this.prSetSoloButtonFunc(func, 'noteOn'); this.prSetSoloButtonFunc(func, 'noteOff'); });
  }
  prSetSoloButtonFunc { | num, type |
    switch(type,
      \noteOn, { soloButtonFuncArray[0][num].prFunc_(activePage.getSoloButtonFunc(num, 'noteOn')) },
      \noteOff, { soloButtonFuncArray[1][num].prFunc_(activePage.getSoloButtonFunc(num, 'noteOff')) });
  }
  setSoloButtonFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num - 1;
    if( num < 9, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setSoloButtonFunc(number, func, type, bank);
      this.prSetSoloButtonFunc(number, type);
    }, { "out of range!".postln });
  }

  ////// record enable funcs:
  prSetAllRecordEnableButtonFuncs {
    8.do({ | func | this.prSetRecordEnableButtonFunc(func, 'noteOn');
      this.prSetRecordEnableButtonFunc(func, 'noteOff'); });
  }
  prSetRecordEnableButtonFunc { | num, type |
    switch(type,
      \noteOn, { recordEnableButtonFuncArray[0][num].prFunc_(activePage.getRecordEnableButtonFunc(num, 'noteOn')) },
      \noteOff, { recordEnableButtonFuncArray[1][num].prFunc_(activePage.getRecordEnableButtonFunc(num, 'noteOff'))
    });
  }
  setRecordEnableButtonFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num - 1;
    if( num < 9, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setRecordEnableButtonFunc(number, func, type, bank);
      this.prSetRecordEnableButtonFunc(number, type);
    }, { "out of range!".postln });
  }

  ////// device buttons:
  prSetAllDeviceButtonFuncs {
    14.do({ | func | this.prSetDeviceButtonFunc(func, 'noteOn'); this.prSetDeviceButtonFunc(func, 'noteOff'); });
  }
  prSetDeviceButtonFunc { | num, type |
    switch(type,
      \noteOn, { deviceFuncArray[0][num].prFunc_(activePage.getDeviceButtonFunc(num, 'noteOn')) },
      \noteOff, { deviceFuncArray[1][num].prFunc_(activePage.getDeviceButtonFunc(num, 'noteOff')) });
  }
  setDeviceButtonFunc { | num = 1, func = nil, type = 'noteOn', bank = 'active', page = 'active' |
    var number = num - 1;
    if( num < 9, {
      if( page == 'active', { page = activePageKey });
      pageDict[page].setDeviceButtonFunc(number, func, type, bank);
      this.prSetDeviceButtonFunc(number, type);
    }, { "out of range!.postln" });
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

  prSetAllControlButtonFuncs {
    10.do({ | func | this.prSetControlButtonFunc(func, 'noteOn'); this.prSetControlButtonFunc(func, 'noteOff'); });
  }
  prSetControlButtonFunc { | num, type |
    switch(type,
      \noteOn, { controlFuncArray[0][num].prFunc_(activePage.getControlButtonFunc(num, 'noteOn')) },
      \noteOff, { controlFuncArray[1][num].prFunc_(activePage.getControlButtonFunc(num, 'noteOff')) });
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
  ////// Control Functions: //////
  ///////////////////////////////


  prSetAllFaderFuncs {
    9.do({ | func | this.prSetFaderFunc(func); });
  }
  prSetFaderFunc { | num |
    mixerFadersArray[num].prFunc_(activePage.getFaderFunc(num));
  }
  setFaderFunc { | fader = 1, func = nil, bank = 'active', page = 'active' |
    var faderIndex = fader-1;
    if( page == 'active', { page = activePageKey });
    pageDict[page].setFaderFunc(faderIndex, func, bank);
    this.prSetFaderFunc(faderIndex);
  }
  setMasterFaderFunc { | func = nil, bank = 'active', page = 'active' |
    this.setFaderFunc(9, func, bank, page);
  }

  prSetAllMixerEncoderFuncs {
    9.do({ | func | this.prSetMixerEncoderFunc(func); });
  }
  prSetMixerEncoderFunc { | num |
    mixerEncodersArray[num].prFunc_(activePage.getMixerEncoderFunc(num));
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
    deviceEncodersArray[num].prFunc_(activePage.getDeviceEncoderFunc(num));
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
  //////  Monitor Funcs: //////
  ////////////////////////////////

  setGridMonitorFunc { | name, func, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeGridBnk });
    pageDict[page].setGridMonitorFunc(name, func, bank);
    activePage.gridBankMonitorFuncArray[activePage.activeGridBnk][name].reset.play;
  }

  setSceneLaunchMonitorFunc { | name, func, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeSceneLaunchBnk });
    pageDict[page].setSceneLaunchMonitorFunc(name, func, bank);
    activePage.sceneLaunchBankMonitorFuncArray[activePage.activeSceneLaunchBnk][name].reset.play;
  }

  setClipStopMonitorFunc { | name, func, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeClipStopBnk });
    pageDict[page].setClipStopMonitorFunc(name, func, bank);
    activePage.clipStopBankMonitorFuncArray[activePage.activeClipStopBnk][name].reset.play;
  }

  setMixerMonitorFunc { | name, func, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeMixerBnk });
    pageDict[page].setMixerMonitorFunc(name, func, bank);
    activePage.mixerBankMonitorFuncArray[activePage.activeMixerBnk][name].reset.play;
  }

  setDeviceButtonsMonitorFunc { | name, func, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeDeviceButtonsBnk });
    pageDict[page].setDeviceButtonsMonitorFunc(name, func, bank);
    activePage.deviceButtonsBankMonitorFuncArray[activePage.activeDeviceButtonsBnk][name].reset.play;
  }

  setControlButtonsMonitorFunc { | name, func, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeControlButtonsBnk });
    pageDict[page].setControlButtonsMonitorFunc(name, func, bank);
    activePage.controlButtonsBankMonitorFuncArray[activePage.activeControlButtonsBnk][name].reset.play;
  }

  setMixerEncodersMonitorFunc { | name, func, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeMixerEncodersBnk });
    pageDict[page].setMixerEncodersMonitorFunc(name, func, bank);
    activePage.mixerEncodersBankMonitorFuncArray[activePage.activeMixerEncodersBnk][name].reset.play;
  }

  setDeviceEncodersMonitorFunc { | name, func, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeDeviceEncodersBnk });
    pageDict[page].setDeviceEncodersMonitorFunc(name, func, bank);
    activePage.deviceEncodersBankMonitorFuncArray[activePage.activeDeviceEncodersBnk][name].reset.play;
  }

}

