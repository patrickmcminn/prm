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

  prTurnStopClipOn { | num = 1| midiOutPort.noteOn(num-1, 52, 1); }
  prTurnStopClipOff { | num = 1| midiOutPort.noteOn(num-1, 52, 0); }

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

}