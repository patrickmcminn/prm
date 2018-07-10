/*
Monday, May 28th 2018
APC40Mk2_Page.sc
prm
*/

APC40Mk2_Page {

  var gridFuncArray, sceneLaunchFuncArray, clipStopFuncArray;
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
    gridFuncArray = Array.fill2D(2, 40, { nil });
    sceneLaunchFuncArray = Array.fill2D(2, 5, { nil });
    clipStopFuncArray = Array.fill2D(2, 9, { nil });
    trackSelectFuncArray = Array.fill2D(2, 9, { nil });
    trackActivatorFuncArray = Array.fill2D(2, 8, { nil });
    crossfaderSelectFuncArray = Array.fill2D(2, 8, { nil });
    soloFuncArray = Array.fill2D(2, 8, { nil });
    recordEnableFuncArray = Array.fill2D(2, 8, { nil });
    deviceFuncArray = Array.fill2D(2, 14, { nil });
    controlFuncArray = Array.fill2D(2, 10, { nil });
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
    mixerFaderArray = Array.fill(9, { nil });
    mixerEncoderArray = Array.fill(9, { nil });
    deviceEncoderArray = Array.fill(10, { nil });
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

  /////// note funcs:

  prSetGridFunc { | num = 0, type = 'noteOn', func = nil |
    switch(type,
      \noteOn, { gridFuncArray[0][num].prFunc_(func) },
      \noteOff,{ gridFuncArray[1][num].prFunc_(func) }
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
      \noteOn, { sceneLaunchFuncArray[0][num].prFunc_(func) },
      \noteOff, { sceneLaunchFuncArray[1][num].prFunc_(func) }
    );
  }

  setSceneLaunchFunc { | num = 0, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeGridBnk }, { bankSelect = bank });
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
      \noteOn, { clipStopFuncArray[0][num].prFunc_(func) },
      \noteOff, { clipStopFuncArray[1][num].prFunc_(func) }
    );
  }

  setClipStopFunc { | num = 1, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeGridBnk }, { bankSelect = bank });
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
      \noteOn, { trackSelectFuncArray[0][num].prFunc_(func) },
      \noteOff, { trackSelectFuncArray[1][num].prFunc_(func) });
  }

  /*
  setTrackSelectFunc { | num = 1, func, type = 'noteOn', bank = 'active' |
  var bankSelect;
  if( bank == activeGridBnk, { bank = 'active' });
  if( bank == 'active', { bankSelect = activeGridBnk }, { bankSelect = bank });
  if( num >9, { "out of range!"}, {
  switch(type,
  \noteOn, {
  trackSelectBankArray[bankSelect][num][0] = func;
  if( bank == 'active', { this.prSetTrackSelectFunc(num, type, func); }); },
  \noteOff, {
  trackSelectBankArray[bankSelect][num][1] = func;
  if( bank == 'active', { this.prSetTrackSelectFunc(num, type, func); }); }
  );
  });
  }
  */

  prSetTrackActivatorFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { trackActivatorFuncArray[0][num].prFunc_(func) },
      \noteOff, { trackActivatorFuncArray[1][num].prFunc_(func) });
  }

  prSetCrossfaderSelectFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { crossfaderSelectFuncArray[0][num].prFunc_(nil) },
      \noteOff, { crossfaderSelectFuncArray[1][num].prFunc_(nil) });
  }

  prSetSoloFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { soloFuncArray[0][num].prFunc_(func) },
      \noteOff, { soloFuncArray[1][num].prFunc_(func) });
  }

  prSetRecordEnableFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { recordEnableFuncArray[0][num].prFunc_(func) },
      \noteOff, { recordEnableFuncArray[1][num].prFunc_(func) });
  }

  prSetDeviceButtonFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { deviceFuncArray[0][num].prFunc_(func) },
      \noteOff, { deviceFuncArray[1][num].prFunc_(func) });
  }

  prSetControlButtonFunc { | num, type, func = nil |
    switch(type,
      \noteOn, { controlFuncArray[0][num].prFunc_(func) },
      \noteOff, { controlFuncArray[1][num].prFunc_(func) });
  }

  //////// control funcs:
  prSetFaderFunc { | num, func = nil|
    mixerFaderArray[num].prFunc_(nil);
  }

  prSetMixerEncoderFunc { | num, func = nil |
    mixerEncoderArray[num].prFunc_(func);
  }

  prSetDeviceEncoderFunc { | num, func = nil |
    deviceEncoderArray[num].prFunc_(func);
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