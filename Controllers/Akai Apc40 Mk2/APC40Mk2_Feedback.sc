/*
Monday, May 28th 2018
APC40Mk2_Feedback.sc
prm
*/

+ APC40Mk2 {

  /////// Colors:

  ////// Notes:

  ///////////////  BASIC COLOR FUNCTIONS ///////////////

  prTurnButtonColor { | num, colorVal | midiOutPort.noteOn(0, num, colorVal); }
  prTurnClipStopButtonColor { | num, color = 0 | midiOutPort.noteOn(num, 52, color); }
  prTurnStopAllClipsButtonColor { | color | midiOutPort.noteOn(0, 81, color); }
  prTurnTrackSelectButtonColor { | num, color = 0 | midiOutPort.noteOn(num, 51, color); }
  prTurnMasterTrackSelectButtonColor { | color | midiOutPort.noteOn(0, 80, color); }
  prTurnTrackActivatorButtonColor { | num, color = 0 | midiOutPort.noteOn(num, 50, color); }
  prTurnCrossfaderSelectButtonColor { | num, color = 0 | midiOutPort.noteOn(num, 66, color); }
  prTurnSoloButtonColor { | num = 0, color = 0 | midiOutPort.noteOn(num, 49, color); }
  prTurnRecordEnableButtonColor { | num = 0, color = 0 | midiOutPort.noteOn(num, 48, color); }
  prTurnDeviceButtonColor { | num = 0, color = 0 | midiOutPort.noteOn(0, num+58, color); }
  prTurnBankButtonColor { | color | midiOutPort.noteOn(0, 103, color); }
  prTurnPanButtonColor { | color | midiOutPort.noteOn(0, 87, color); }
  prTurnSendsButtonColor { | color | midiOutPort.noteOn(0, 88, color); }
  prTurnUserButtonColor { | color | midiOutPort.noteOn(0, 89, color); }
  prTurnMetronomeButtonColor { | color | midiOutPort.noteOn(0, 90, color); }
  prTurnPlayButtonColor { | color | midiOutPort.noteOn(0, 91, color); }
  prTurnRecordButtonColor { | color | midiOutPort.noteOn(0, 93, color); }
  prTurnSessionButtonColor { | color | midiOutPort.noteOn(0, 102, color); }

  //////// for changing banks:

  prSetAllGridColors {
    40.do({ | num | this.prTurnButtonColor(num, activePage.gridColorArray[num]) });
  }
  prSetAllSceneLaunchColors {
    5.do({ | num | this.prTurnButtonColor(82+num, activePage.sceneLaunchColorArray[num]); });
  }
  prSetAllClipStopColors {
    9.do({ | num | this.prTurnClipStopButtonColor(num, activePage.clipStopColorArray[num]) });
  }
  prSetAllMixerColors {
    9.do({ | num | this.prTurnTrackSelectButtonColor(num, activePage.selectColorArray[num]) });
    8.do({ | num | this.prTurnTrackActivatorButtonColor(num, activePage.trackActivatorColorArray[num]) });
    8.do({ | num | this.prTurnCrossfaderSelectButtonColor(num, activePage.crossfaderSelectColorArray[num]) });
    8.do({ | num | this.prTurnSoloButtonColor(num, activePage.soloColorArray[num]) });
    8.do({ | num | this.prTurnRecordButtonColor(num, activePage.recordEnableColorArray[num]) });
  }
  prSetAllDeviceButtonColors {
    8.do({ | num | this.prTurnDeviceButtonColor(num, activePage.deviceColorArray[num]) });
    this.prTurnBankButtonColor(activePage.deviceColorArray[13]);
  }
  prSetAllControlButtonColors { | num |
    this.prTurnPanButtonColor(activePage.controlColorArray[0]);
    this.prTurnSendsButtonColor(activePage.controlColorArray[1]);
    this.prTurnUserButtonColor(activePage.controlColorArray[2]);
    this.prTurnMetronomeButtonColor(activePage.controlColorArray[6]);
    this.prTurnPlayButtonColor(activePage.controlColorArray[3]);
    this.prTurnRecordButtonColor(activePage.controlColorArray[4]);
    this.prTurnSessionButtonColor(activePage.controlColorArray[5]);
  }


  //////// Values:

  prSetAllMixerEncoderValues {
    8.do({ | num | this.prSetMixerEncoderValue(num, activePage.mixerEncodersValueArray[num]); });
  }
  prSetMixerEncoderValue { | num = 0, val = 127 |
    midiOutPort.control(0, num+48, val);
  }
  prSetAllDeviceEncoderValues {
    8.do({ | num | this.prSetDeviceEncoderValue(num, activePage.deviceEncodersValueArray[num]); });
  }
  prSetDeviceEncoderValue {| num = 0, val = 127 |
    midiOutPort.control(0, num+16, val);
  }

  ////////////////////////////////////
  //////// Color Functions //////////
  //////////////////////////////////


  /////////////// grid colors:

  turnGridColor { | column = 0, row = 0, colorVal = 0, bank = 'active', page = 'active' |
    var num = ((row * 8) + column);
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      colorVal != activePage.getGridButtonColor(num),
      { this.prTurnButtonColor(num, colorVal); }); });
    pageDict[page].turnGridColor(column, row, colorVal, bank);
  }

  turnGridWhite { | column = 0, row = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 1 + brightness;
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnGridColor(column, row, colorVal, bank, page); });
  }

  turnGridRed { | column = 0, row = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 5 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnGridColor(column, row, colorVal, bank, page); });
  }

  turnGridYellow { | column = 0, row = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 13 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnGridColor(column, row, colorVal, bank, page); });
  }

  turnGridYellowGreen { | column = 0, row = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 17 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnGridColor(column, row, colorVal, bank, page); });
  }

  turnGridGreen { | column = 0, row = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 21 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnGridColor(column, row, colorVal, bank, page); });
  }

  turnGridCyan { | column = 0, row = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 33 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnGridColor(column, row, colorVal, bank, page); });
  }

  turnGridLightBlue { | column = 0, row = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 37 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnGridColor(column, row, colorVal, bank, page); });
  }

  turnGridBlue { | column = 0, row = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 45 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnGridColor(column, row, colorVal, bank, page); });
  }

  turnGridPurple { | column = 0, row = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 49 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnGridColor(column, row, colorVal, bank, page); });
  }

  turnGridMagenta { | column = 0, row = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 53 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnGridColor(column, row, colorVal, bank, page); });
  }

  turnGridPink { | column = 0, row = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 57 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnGridColor(column, row, colorVal, bank, page); });
  }


  ////////// launch button colors:
  turnSceneLaunchButtonColor { | button = 0, colorVal = 0, bank = 'active', page = 'active' |
    var num = button + 82;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      colorVal != activePage.getSceneLaunchButtonColor(num),
      { this.prTurnButtonColor(num, colorVal); }); });
    pageDict[page].turnSceneLaunchButtonColor(button, colorVal, bank);
  }

  turnSceneLaunchButtonWhite { | button = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 1 + brightness;
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnSceneLaunchButtonColor(button, colorVal, bank, page); });
  }

  turnSceneLaunchButtonRed { | button = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 5 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnSceneLaunchButtonColor(button, colorVal, bank, page); });
  }

  turnSceneLaunchButtonYellow { | button = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 13 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnSceneLaunchButtonColor(button, colorVal, bank, page); });
  }

  turnSceneLaunchButtonYellowGreen { | button = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 17 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnSceneLaunchButtonColor(button, colorVal, bank, page); });
  }

  turnSceneLaunchButtonGreen { | button = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 21 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnSceneLaunchButtonColor(button, colorVal, bank, page); });
  }

  turnSceneLaunchButtonCyan { | button = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 33 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnSceneLaunchButtonColor(button, colorVal, bank, page); });
  }

  turnSceneLaunchLightBlue { | button = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 37 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnSceneLaunchButtonColor(button, colorVal, bank, page); });
  }

  turnSceneLaunchButtonBlue { | button = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 45 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnSceneLaunchButtonColor(button, colorVal, bank, page); });
  }

  turnSceneLaunchButtonPurple { | button = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 49 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnSceneLaunchButtonColor(button, colorVal, bank, page); });
  }

  turnSceneLaunchButtonMagenta { | button = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 53 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnSceneLaunchButtonColor(button, colorVal, bank, page); });
  }

  turnSceneLaunchButtonPink { | button = 0, brightness = 2, bank = 'active', page = 'active' |
    var colorVal = 57 + (2-brightness);
    if( brightness > 2, { "brightness out of range".postln; },
      { this.turnSceneLaunchButtonColor(button, colorVal, bank, page); });
  }

  //////////////////////////////////
  ///// Other color functions: ////
  ////////////////////////////////


  turnClipStopButtonOn { | button = 1, bank = 'active', page = 'active' |
    var index = button - 1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getClipStopButtonColor(index) != 1,
      if( button == 9,
        { this.prTurnStopAllClipsButtonColor(1) },
        { this.prTurnClipStopButtonColor(index, 1) });
    )});
    pageDict[page].turnClipStopButtonOn(index, bank);
  }
  turnClipStopButtonOff { | button = 1, bank = 'active', page = 'active' |
    var index = button -1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getClipStopButtonColor(index) != 0,
      if( button == 9,
        { this.prTurnStopAllClipsButtonColor(index, 0) },
        { this.prTurnClipStopButtonColor(index, 0) }); );
    });
    pageDict[page].turnClipStopButtonOff(index, bank);
  }

  turnTrackSelectButtonOn { | button = 1, bank = 'active', page = 'active' |
    var index = button-1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackSelectButtonColor(index) != 1,
      if( button == 9,
        { this.prTurnMasterTrackSelectButtonColor(1) },
        { this.prTurnTrackSelectButtonColor(index, 1) }); );
    });
    pageDict[page].turnTrackSelectButtonOn(index, bank);
  }
  turnTrackSelectButtonOff { | button = 1, bank = 'active', page = 'active' |
    var index = button-1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackSelectButtonColor(index) != 0,
      if( button == 9,
        { this.prTurnMasterTrackSelectButtonColor(0) },
        { this.prTurnTrackSelectButtonColor(index, 0) }); );
      });
    pageDict[page].turnTrackSelectButtonOff(index, bank);
  }

  turnTrackActivatorButtonOn { | button = 1, bank = 'active', page = 'active' |
    var index = button-1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackActivatorButtonColor(index) != 1,
      { this.prTurnTrackActivatorButtonColor(index, 1) }); });
    pageDict[page].turnTrackActivatorButtonOn(index, bank);
  }
  turnTrackActivatorButtonOff { | button = 1, bank = 'active', page = 'active' |
    var index = button-1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackActivatorButtonColor(index) != 0,
      { this.prTurnTrackActivatorButtonColor(index, 0) }); });
    pageDict[page].turnTrackActivatorButtonOff(index, bank);
  }

  turnCrossfaderSelectButtonOn { | button = 1, bank = 'active', page = 'active' |
    var index = button-1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getCrossfaderSelectButtonColor(index) != 1,
      { this.prTurnCrossfaderSelectButtonColor(index, 1) }); });
    pageDict[page].turnCrossfaderSelectButtonOn(index, bank);
  }
  turnCrossfaderSelectButtonOff { | button = 1, bank = 'active', page = 'active' |
    var index = button-1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getCrossfaderSelectButtonColor(index) != 0,
      { this.prTurnCrossfaderSelectButtonColor(index, 0) }); });
    pageDict[page].turnCrossfaderSelectButtonOff(index, bank);
  }

  turnSoloButtonOn { | button = 1, bank = 'active', page = 'active' |
    var index = button-1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSoloButtonColor(index) != 1,
      { this.prTurnSoloButtonColor(index, 1) }); });
    pageDict[page].turnSoloButtonOn(index, bank);
  }
  turnSoloButtonOff { | button = 1, bank = 'active', page = 'active' |
    var index = button-1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSoloButtonColor(index) != 0,
      { this.prTurnSoloButtonColor(index, 0) }); });
    pageDict[page].turnSoloButtonOff(index, bank);
  }

  turnRecordEnableButtonOn { | button = 1, bank = 'active', page = 'active' |
    var index = button-1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getRecordEnableButtonColor(index) != 1,
      { this.prTurnRecordEnableButtonColor(index, 1) }); });
    pageDict[page].turnRecordEnableButtonOn(index, bank);
  }
  turnRecordEnableButtonOff { | button = 1, bank = 'active', page = 'active' |
    var index = button-1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getRecordEnableButtonColor(index) != 0,
      { this.prTurnRecordEnableButtonColor(index, 0) }); });
    pageDict[page].turnRecordEnableButtonOff(index, bank);
  }


  turnDeviceButtonOn { | button = 1, bank = 'active', page = 'active' |
    var index = button - 1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getDeviceButtonColor(index) != 1,
      { this.prTurnDeviceButtonColor(index, 1) }); });
    pageDict[page].turnDeviceButtonOn(index, bank);
  }
  turnDeviceButtonOff { | button = 1, bank = 'active', page = 'active' |
    var index = button-1;
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getDeviceButtonColor(index) != 0,
      { this.prTurnDeviceButtonColor(index, 0) }); });
    pageDict[page].turnDeviceButtonOff(index, bank);
  }

  turnBankButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getDeviceButtonColor(13) != 1,
      { this.prTurnBankButtonColor(1) }); });
    pageDict[page].turnDeviceButtonColor(13, 1, bank);
  }
  turnBankButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getDeviceButtonColor(13) != 0,
      { this.prTurnBankButtonColor(0) }); });
    pageDict[page].turnDeviceButtonColor(13, 0, bank);
  }

  turnPanButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(0) != 1,
      { this.prTurnPanButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(0, 1, bank);
  }
  turnPanButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(0) != 0,
      { this.prTurnPanButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(0, 0, bank);
  }

  turnSendsButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(1) != 1,
      { this.prTurnSendsButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(1, 1, bank);
  }
  turnSendsButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(1) != 0,
      { this.prTurnSendsButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(1, 0, bank);
  }

  turnUserButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(2) != 1,
      { this.prTurnUserButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(2, 1, bank);
  }
  turnUserButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(2) != 0,
      { this.prTurnUserButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(2, 0, bank);
  }

  turnMetronomeButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(6) != 1,
      { this.prTurnMetronomeButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(6, 1, bank);
  }
  turnMetronomeButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(6) != 0,
      { this.prTurnMetronomeButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(6, 0, bank);
  }

  turnPlayButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(3) != 1,
      { this.prTurnPlayButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(3, 1, bank);
  }
  turnPlayButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(3) != 0,
      { this.prTurnPlayButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(3, 0, bank);
  }

  turnRecordButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(4) != 1,
      { this.prTurnRecordButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(4, 1, bank);
  }
  turnRecordButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(4) != 0,
      { this.prTurnRecordButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(4, 0, bank);
  }

  turnSessionButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getControlButtonColor(5) != 1,
      { this.prTurnSessionButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(5, 1, bank);
  }
  turnSessionButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.ggetControlButtonColor(5) != 0,
      { this.prTurnSessionButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(5, 0, bank);
  }

  ////////////////////////
  //////// CC Values: ///
  //////////////////////

  //// Mixer Encoders:
  setMixerEncoderValue { | encoder = 1, value = 127, bank = 'active', page = 'active' |
    var index = encoder-1;
    if( encoder > 8, { "out of range!".postln; },
      {
        if( page == 'active', { page = activePageKey });
        if( page == activePageKey, { if(
          value != activePage.mixerEncodersValueArray[index],
          { this.prSetMixerEncoderValue(index, value) });
        });
        pageDict[page].setMixerEncoderValue(index, value, bank);
      }
    );
  }

  setDeviceEncoderValue { | encoder = 1, value = 127, bank = 'active', page = 'active' |
    var index = encoder-1;
    if( encoder > 8, { "out of range!".postln; },
      {
        if( page == 'active', { page = activePageKey });
        if( page == activePageKey, { if(
          value != activePage.deviceEncodersValueArray[index],
          { this.prSetDeviceEncoderValue(index, value) });
        });
        pageDict[page].setDeviceEncoderValue(index, value, bank);
      }
    );
  }


}