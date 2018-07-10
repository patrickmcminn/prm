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
  prTurnStopClipButtonColor { | num, color = 0 | midiOutPort.noteOn(num-1, 52, color); }
  prTurnTrackSelectButtonColor { | num, color = 0 | midiOutPort.noteOn(num-1, 51, color); }
  prTurnTrackActivatorButtonColor { | num, color = 0 | midiOutPort.noteOn(num-1, 50, color); }
  prTurnCrossfadeSelectButtonColor { | num, color = 0 | midiOutPort.noteOn(num-1, 66, color); }
  prTurnSoloButtonColor { | num = 1, color = 0 | midiOutPort.noteOn(num-1, 49, color); }
  prTurnRecordEnableButtonColor { | num = 1, color = 0 | midiOutPort.noteOn(num-1, 48, color); }
  prTurnDeviceButtonColor { | num = 1, color = 0 | midiOutPort.noteOn(0, num+57, color); }
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
  prSetAllStopClipColors {
    9.do({ | num | this.prTurnStopClipButtonColor(num, activePage.clipStopColorArray[num]) });
  }
  prSetAllMixerColors {
    9.do({ | num | this.prTurnTrackSelectButtonColor(num, activePage.selectColorArray[num]) });
    8.do({ | num | this.prTurnTrackActivatorButtonColor(num, activePage.trackActivatorColorArray[num]) });
    8.do({ | num | this.prTurnCrossfadeSelectButtonColor(num, activePage.crossfadeSelectColorArray[num]) });
    8.do({ | num | this.prTurnSoloButtonColor(num, activePage.soloColorArray[num]) });
    8.do({ | num | this.prTurnRecordButtonColor(num, activePage.recordEnableColorArray[num]) });
  }
  prSetAllDeviceColors {
    8.do({ | num | this.prTurnDeviceButtonColor(num, activePage.deviceColorArray[num]) });
    this.prTurnBankButtonColor(activePage.deviceColorArray[13]);
  }
  prSetAllControlColors { | num |
    this.prTurnPanButtonColor(activePage.controlColorArray[0]);
    this.prTurnSendsButtonColor(activePage.controlColorArray[1]);
    this.prTurnUserButtonColor(activePage.controlColorArray[2]);
    this.prTurnMetronomeButtonColor(activePage.controlColorArray[6]);
    this.prTurnPlayButtonColor(activePage.controlColorArray[3]);
    this.prTurnRecordButtonColor(activePage.controlColorArray[4]);
    this.prTurnSessionButtonColor(activePage.controlColorArray[5]);
  }


  //////// Values:

  prSetMixerEncoderValue { | num = 1, val = 127 |
    midiOutPort.control(0, num+47, val);
  }

  prSetDeviceEncoderValue {| num = 1, val = 127 |
    midiOutPort.control(0, num+15, val);
  }

  ////////////////////////////////////
  //////// Color Functions //////////
  //////////////////////////////////


  /////////////// grid colors:

  turnGridColor { | column = 0, row = 0, colorVal = 0, bank = 'active', page = 'active' |
    var num = ((row * 8) + column);
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      colorVal != activePage.getGridColor(num),
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


  turnStopClipButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getStopClipButtonColor(button) != 1,
      { this.prTurnStopClipButtonColor(button, 1) }); });
    pageDict[page].turnStopClipButtonOn(button, bank);
  }
  turnStopClipButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getStopClipButtonColor(button) != 0,
      { this.prTurnStopClipButtonColor(button, 0) }); });
    pageDict[page].turnStopClipButtonOff(button, bank);
  }

  turnTrackSelectButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackSelectButtonColor(button) != 1,
      { this.prTurnTrackSelectButtonColor(button, 1) }); });
    pageDict[page].turnTrackSelectButtonOn(button, bank);
  }
  turnTrackSelectButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackSelectButtonColor(button) != 0,
      { this.prTurnTrackSelectButtonColor(button, 0) }); });
    pageDict[page].turnTrackSelectButtonOff(button, bank);
  }

  turnTrackActivatorButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackActivatorButtonColor(button) != 1,
      { this.prTurnTrackActivatorButtonColor(button, 1) }); });
    pageDict[page].turnTrackActivatorButtonOn(button, bank);
  }
  turnTrackActivatorButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackActivatorButtonColor(button) != 0,
      { this.prTurnTrackActivatorButtonColor(button, 0) }); });
    pageDict[page].turnTrackActivatorButtonOff(button, bank);
  }

  turnCrossfadeSelectButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getCrossfadeSelectButtonColor(button) != 1,
      { this.prTurnCrossfadeSelectButtonColor(button, 1) }); });
    pageDict[page].turnCrossfadeSelectButtonOn(button, bank);
  }
  turnCrossfadeSelectButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getCrossfadeSelectButtonColor(button) != 0,
      { this.prTurnCrossfadeSelectButtonColor(button, 0) }); });
    pageDict[page].turnCrossfadeSelectButtonOff(button, bank);
  }

  turnSoloButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSoloButtonColor(button) != 1,
      { this.prTurnSoloButtonColor(button, 1) }); });
    pageDict[page].turnSoloButtonOn(button, bank);
  }
  turnSoloButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSoloButtonColor(button) != 0,
      { this.prTurnSoloButtonColor(button, 0) }); });
    pageDict[page].turnSoloButtonOff(button, bank);
  }

  turnRecordEnableButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getRecordEnableButtonColor(button) != 1,
      { this.prTurnRecordButtonColor(button, 1) }); });
    pageDict[page].turnRecordEnableButtonOn(button, bank);
  }
  turnRecordEnableButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getRecordEnableButtonColor(button) != 0,
      { this.prTurnRecordButtonColor(button, 0) }); });
    pageDict[page].turnRecordEnableButtonOff(button, bank);
  }

  turnDeviceButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getDeviceButtonColor(button) != 1,
      { this.prTurnDeviceButtonColor(button, 1) }); });
    pageDict[page].turnDeviceButtonOn(button, bank);
  }
  turnDeviceButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getDeviceButtonColor(button) != 0,
      { this.prTurnDeviceButtonColor(button, 0) }); });
    pageDict[page].turnDeviceButtonOff(button, bank);
  }

  turnBankButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getBankButtonColor != 1,
      { this.prTurnBankButtonColor(1) }); });
    pageDict[page].turnDeviceButtonColor(13, 1, bank);
  }
  turnBankButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getBankButtonColor != 0,
      { this.prTurnBankButtonColor(0) }); });
    pageDict[page].turnDeviceButtonColor(13, 0, bank);
  }

  turnPanButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getPanButtonColor != 1,
      { this.prTurnPanButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(0, 1, bank);
  }
  turnPanButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getPanButtonColor != 0,
      { this.prTurnPanButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(0, 0, bank);
  }

  turnSendsButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSendsButtonColor != 1,
      { this.prTurnSendsButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(1, 1, bank);
  }
  turnSendsButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSendsButtonColor != 0,
      { this.prTurnSendsButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(1, 0, bank);
  }

  turnUserButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getUserButtonColor != 1,
      { this.prTurnUserButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(2, 1, bank);
  }
  turnUserButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getUserButtonColor != 0,
      { this.prTurnUserButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(2, 0, bank);
  }

  turnMetronomeButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getMetronomeButtonColor != 1,
      { this.prTurnMetronomeButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(6, 1, bank);
  }
  turnMetronomeButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getMetronomeButtonColor != 0,
      { this.prTurnMetronomeButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(6, 0, bank);
  }

  turnPlayButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getPlayButtonColor != 1,
      { this.prTurnPlayButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(3, 1, bank);
  }
  turnPlayButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getPlayButtonColor != 0,
      { this.prTurnPlayButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(3, 0, bank);
  }

  turnRecordButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getRecordButtonColor != 1,
      { this.prTurnRecordButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(4, 1, bank);
  }
  turnRecordButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getRecordButtonColor != 0,
      { this.prTurnRecordButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(4, 0, bank);
  }

  turnSessionButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSessionButtonColor != 1,
      { this.prTurnSessionButtonColor(1) }); });
    pageDict[page].turnControlButtonColor(5, 1, bank);
  }
  turnSessionButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSessionButtonColor != 0,
      { this.prTurnSessionButtonColor(0) }); });
    pageDict[page].turnControlButtonColor(5, 0, bank);
  }

  ////////////////////////
  //////// CC Values: ///
  //////////////////////

  //// Mixer Encoders:
  setMixerEncoderValue { | encoder = 1, value = 127, bank = 'active', page = 'active' |
    if( encoder > 8, { "out of range!".postln; },
      {
        if( page == 'active', { page = activePageKey });
        if( page == activePageKey, { if(
          value != activePage.mixerEncoderValueArray[encoder-1],
          { this.prSetMixerEncoderValue(encoder, value) });
        });
        pageDict[page].setMixerEncoderValue(encoder, value, bank);
      }
    );
  }

  setDeviceEncoderValue { | encoder = 1, value = 127, bank = 'active', page = 'active' |
    if( encoder > 8, { "out of range!".postln; },
      {
        if( page == 'active', { page = activePageKey });
        if( page == activePageKey, { if(
          value != activePage.deviceEncoderValueArray[encoder-1],
          { this.prSetDeviceEncoderValue(encoder, value) });
        });
        pageDict[page].setDeviceEncoderValue(encoder, value, bank);
      }
    );
  }


}