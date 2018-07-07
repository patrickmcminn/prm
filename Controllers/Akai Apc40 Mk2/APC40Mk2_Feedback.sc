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

  prTurnStopClipButtonOn { | num = 1| midiOutPort.noteOn(num-1, 52, 1); }
  prTurnStopClipButtonOff { | num = 1| midiOutPort.noteOn(num-1, 52, 0); }

  prTurnTrackSelectButtonOn { | num = 1| midiOutPort.noteOn(num-1, 51, 1); }
  prTurnTrackSelectButtonOff { | num = 1| midiOutPort.noteOn(num-1, 51, 0); }

  prTurnTrackActivatorButtonOn { | num = 1| midiOutPort.noteOn(num-1, 50, 1); }
  prTurnTrackActivatorButtonOff { | num = 1| midiOutPort.noteOn(num-1, 50, 0); }

  prTurnCrossfadeSelectButtonOn { | num = 1| midiOutPort.noteOn(num-1, 66, 1); }
  prTurnCrossfadeSelectButtonOff { | num = 1| midiOutPort.noteOn(num-1, 66, 0); }

  prTurnSoloButtonOn { | num = 1| midiOutPort.noteOn(num-1, 49, 1); }
  prTurnSoloButtonOff { | num = 1| midiOutPort.noteOn(num-1, 49, 0); }

  prTurnRecordEnableButtonOn { | num = 1| midiOutPort.noteOn(num-1, 48, 1); }
  prTurnRecordEnableButtonOff { | num = 1| midiOutPort.noteOn(num-1, 48, 0); }

  prTurnDeviceButtonOn { | num = 1 |midiOutPort.noteOn(0, num+57, 1); }
  prTurnDeviceButtonOff { | num = 1 | midiOutPort.noteOn(0, num+57, 0); }

  prTurnBankButtonOn { midiOutPort.noteOn(0, 103, 1); }
  prTurnBankButtonOff { midiOutPort.noteOn(0, 103, 0); }

  prTurnPanButtonOn { midiOutPort.noteOn(0, 87, 1); }
  prTurnPanButtonOff { midiOutPort.noteOn(0, 87, 0); }

  prTurnSendsButtonOn { midiOutPort.noteOn(0, 88, 1); }
  prTurnSendsButtonOff { midiOutPort.noteOn(0, 88, 0); }

  prTurnUserButtonOn { midiOutPort.noteOn(0, 89, 1); }
  prTurnUserButtonOff { midiOutPort.noteOn(0, 89, 0); }

  prTurnMetronomeButtonOn { midiOutPort.noteOn(0, 90, 1); }
  prTurnMetronomeButtonOff { midiOutPort.noteOn(0, 90, 0); }

  prTurnPlayButtonOn { midiOutPort.noteOn(0, 91, 1); }
  prTurnPlayButtonOff { midiOutPort.noteOn(0, 91, 0); }

  prTurnRecordButtonOn { midiOutPort.noteOn(0, 93, 1); }
  prTurnRecordButtonOff { midiOutPort.noteOn(0, 93, 0); }

  prTurnSessionButtonOn { midiOutPort.noteOn(0, 102, 1); }
  prTurnSessionButtonOff { midiOutPort.noteOn(0, 102, 0); }

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
      { this.prturnButtonColor(num, colorVal); }); });
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
      activePage.getStopClipButtonColor(button) != 'on',
      { this.prTurnStopClipButtonOn(button) }); });
    pageDict[page].turnStopClipButtonOn(button, bank);
  }
  turnStopClipButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getStopClipButtonColor(button) != 'off',
      { this.prTurnStopClipButtonOff(button) }); });
    pageDict[page].turnStopClipButtonOff(button, bank);
  }

  turnTrackSelectButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackSelectButtonColor(button) != 'on',
      { this.prTurnTrackSelectButtonOn(button) }); });
    pageDict[page].turnTrackSelectButtonOn(button, bank);
  }
  turnTrackSelectButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackSelectButtonColor(button) != 'off',
      { this.prTurnTrackSelectButtonOff(button) }); });
    pageDict[page].turnTrackSelectButtonOff(button, bank);
  }

  turnTrackActivatorButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackActivatorButtonColor(button) != 'on',
      { this.prTurnTrackActivatorButtonOn(button) }); });
    pageDict[page].turnTrackActivatorButtonOn(button, bank);
  }
  turnTrackActivatorButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getTrackActivatorButtonColor(button) != 'off',
      { this.prTurnTrackActivatorButtonOff(button) }); });
    pageDict[page].turnTrackActivatorButtonOff(button, bank);
  }

  turnCrossfadeSelectButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getCrossfadeSelectButtonColor(button) != 'on',
      { this.prTurnCrossfadeSelectButtonOn(button) }); });
    pageDict[page].turnCrossfadeSelectButtonOn(button, bank);
  }
  turnCrossfadeSelectButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getCrossfadeSelectButtonColor(button) != 'off',
      { this.prTurnCrossfadeSelectButtonOff(button) }); });
    pageDict[page].turnCrossfadeSelectButtonOff(button, bank);
  }

  turnSoloButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSoloButtonColor(button) != 'on',
      { this.prTurnSoloButtonOn(button) }); });
    pageDict[page].turnSoloButtonOn(button, bank);
  }
  turnSoloButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSoloButtonColor(button) != 'off',
      { this.prTurnSoloButtonOff(button) }); });
    pageDict[page].turnSoloButtonOff(button, bank);
  }

  turnRecordEnableButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getRecordEnableButtonColor(button) != 'on',
      { this.prTurnRecordButtonOn(button) }); });
    pageDict[page].turnRecordEnableButtonOn(button, bank);
  }
  turnRecordEnableButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getRecordEnableButtonColor(button) != 'off',
      { this.prTurnRecordButtonOff(button) }); });
    pageDict[page].turnRecordEnableButtonOff(button, bank);
  }

  turnDeviceButtonOn { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getDeviceButtonColor(button) != 'on',
      { this.prTurnDeviceButtonOn(button) }); });
    pageDict[page].turnDeviceButtonOn(button, bank);
  }
  turnDeviceButtonOff { | button = 1, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getDeviceButtonColor(button) != 'off',
      { this.prTurnDeviceButtonOff(button) }); });
    pageDict[page].turnDeviceButtonOff(button, bank);
  }

  turnBankButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getBankButtonColor != 'on',
      { this.prTurnBankButtonOn }); });
    pageDict[page].turnBankButtonOn(bank);
  }
  turnBankButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getBankButtonColor != 'off',
      { this.prTurnBankButtonOff }); });
    pageDict[page].turnBankButtonOff(bank);
  }

  turnPanButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getPanButtonColor != 'on',
      { this.prTurnPanButtonOn }); });
    pageDict[page].turnPanButtonOn(bank);
  }
  turnPanButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getPanButtonColor != 'off',
      { this.prTurnPanButtonOff }); });
    pageDict[page].turnPanButtonOff(bank);
  }

  turnSendsButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSendsButtonColor != 'on',
      { this.prTurnSendsButtonOn }); });
    pageDict[page].turnSendsButtonOn(bank);
  }
  turnSendsButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSendsButtonColor != 'off',
      { this.prTurnSendsButtonOff }); });
    pageDict[page].turnSendsButtonOff(bank);
  }

  turnUserButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getUserButtonColor != 'on',
      { this.prTurnUserButtonOn }); });
    pageDict[page].turnUserButtonOn(bank);
  }
  turnUserButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getUserButtonColor != 'off',
      { this.prTurnUserButtonOff }); });
    pageDict[page].turnUserButtonOff(bank);
  }

  turnMetronomeButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getMetronomeButtonColor != 'on',
      { this.prTurnMetronomeButtonOn }); });
    pageDict[page].turnMetronomeButtonOn(bank);
  }
  turnMetronomeButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getMetronomeButtonColor != 'off',
      { this.prTurnMetronomeButtonOff }); });
    pageDict[page].turnMetronomeButtonOff(bank);
  }

  turnPlayButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getPlayButtonColor != 'on',
      { this.prTurnPlayButtonOn }); });
    pageDict[page].turnPlayButtonOn(bank);
  }
  turnPlayButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getPlayButtonColor != 'off',
      { this.prTurnPlayButtonOff }); });
    pageDict[page].turnPlayButtonOff(bank);
  }

  turnRecordButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getRecordButtonColor != 'on',
      { this.prTurnRecordButtonOn }); });
    pageDict[page].turnRecordButtonOn(bank);
  }
  turnRecordButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getRecordButtonColor != 'off',
      { this.prTurnRecordButtonOff }); });
    pageDict[page].turnRecordButtonOff(bank);
  }

  turnSessionButtonOn { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSessionButtonColor != 'on',
      { this.prTurnSessionButtonOn }); });
    pageDict[page].turnSessionButtonOn(bank);
  }
  turnSessionButtonOff { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( page == activePageKey, { if(
      activePage.getSessionButtonColor != 'off',
      { this.prTurnSessionButtonOff }); });
    pageDict[page].turnSessionButtonOff(bank);
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