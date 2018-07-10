/*
Sunday, July 8th 2018
APC40Mk2_Page_Feedback.sc
prm
*/

+ APC40Mk2_Page {

  getGridButtonColor { | num | ^gridColorArray[num] }
  getSceneLaunchButtonColor { | num | ^sceneLaunchColorArray[num] }
  getClipStopButtonColor { | num | ^clipStopColorArray[num] }

  getTrackSelectButtonColor { | num | ^selectColorArray[num] }
  getTrackActivatorButtonColor { | num | ^trackActivatorColorArray[num] }
  getCrossfadeSelectButtonColor { | num | ^crossfadeSelectColorArray[num] }
  getSoloButtonColor { | num | ^soloColorArray[num] }
  getRecordEnableColor { | num | ^recordEnableColorArray[num] }

  getDeviceButtonColor { | num | ^deviceColorArray[num] }
  getControlButtonColor { | num | ^controlColorArray[num] }

  // functions for use with Banks:

  /////// colors:
  prTurnGridColor { | num, colorVal = 0 | gridColorArray[num] = colorVal; }

  turnGridColor { | column = 0, row = 0, colorVal = 0, bank = 'active' |
    var num = (row * 8) + column;
    if( bank == 'active', { bank = activeGridBnk });
    gridBankArray[bank][num][2] = colorVal;
    this.prTurnGridColor(num, gridBankArray[activeGridBnk][num][2]);
  }

  prTurnSceneLaunchButtonColor { | num, colorVal = 0 | sceneLaunchColorArray[num] = colorVal; }

  turnSceneLaunchButtonColor { | num = 0, colorVal = 0, bank = 'active' |
    if( bank == 'active', { bank = activeGridBnk });
    sceneLaunchBankArray[bank][num][2] = colorVal;
    this.prTurnSceneLaunchButtonColor(num, sceneLaunchBankArray[activeSceneLaunchBnk][num][2]);
  }

  prTurnClipStopButtonColor { | num, colorVal | clipStopColorArray[num] = colorVal; }

  turnClipStopButtonColor { | num = 1, colorVal = 0, bank = 'active' |
    var index = num-1;
    if( bank == 'active', { bank = activeGridBnk });
    clipStopBankArray[bank][index][2] = colorVal;
    this.prTurnClipStopButtonColor(index, clipStopBankArray[activeClipStopBnk][index][2]);
  }
  turnClipStopButtonOn { | num, bank | this.turnClipStopButtonColor(num, 1, bank) }
  turnClipStopButtonOff { | num, bank | this.turnClipStopButtonColor(num, 0, bank); }

  prTurnTrackSelectButtonColor { | num, colorVal | selectColorArray[num] = colorVal }

  turnTrackSelectButtonColor { | num = 1, colorVal = 0, bank = 'active' |
    var index = num-1;
    if( bank == 'active', { bank = activeGridBnk });
    mixerBankArray[bank][1][index][2] = colorVal;
    this.prTurnTrackSelectButtonColor(index, mixerBankArray[activeMixerBnk][1][index][2]);
  }
  turnTrackSelectButtonOn { | num, bank | this.turnTrackSelectButtonColor(num, 1, bank); }
  turnTrackSelectButtonOff { | num, bank | this.turnTrackSelectButtonOff(num, 0, bank); }

  prTurnTrackActivatorButtonColor { | num, colorVal | trackActivatorColorArray[num] = colorVal }

  turnTrackActivatorButtonColor { | num = 1, colorVal = 0, bank = 'active' |
    var index = num-1;
    if( bank == 'active', { bank = activeGridBnk });
    mixerBankArray[bank][2][index][2] = colorVal;
    this.prTurnTrackActivatorButtonColor(index, mixerBankArray[activeMixerBnk][2][index][2]);
  }
  turnTrackActivatorButtonOn { | num, bank | this.turnTrackActivatorButtonColor(num, 1, bank); }
  turnTrackActivatorButtonOff { | num, bank | this.turnTrackActivatorButtonColor(num, 0, bank); }

  prTurnCrossfadeSelectButtonColor { | num, colorVal | crossfadeSelectColorArray[num] = colorVal }
  turnCrossfadeSelectButtonColor { | num = 1, colorVal = 0, bank = 'active' |
    var index = num-1;
    if( bank == 'active', { bank = activeGridBnk });
    mixerBankArray[bank][3][index][2] = colorVal;
    this.prTurnCrossfadeSelectButtonColor(index, mixerBankArray[activeMixerBnk][3][index][2]);
  }
  turnCrossfadeSelectButtonOn { | num, bank | this.turnCrossfadeSelectButtonColor(num, 1, bank) }
  turnCrossfadeSelectButtonOff { | num, bank | this.turnCrossfadeSelectButtonColor(num, 0, bank) }

  prTurnSoloButtonColor { | num, colorVal | soloColorArray[num] = colorVal }
  turnSoloButtonColor { | num = 1, colorVal = 0, bank = 'active' |
    var index = num-1;
    if( bank == 'active', { bank = activeGridBnk });
    mixerBankArray[bank][4][index][2] = colorVal;
    this.prTurnSoloButtonColor(index, mixerBankArray[activeMixerBnk][4][index][2]);
  }
  turnSoloButtonOn { | num, bank | this.turnSoloButtonColor(num, 1, bank); }
  turnSoloButtonOff { | num, bank | this.turnSoloButtonColor(num, 0, bank); }

  prTurnRecordEnableButtonColor { | num, colorVal | recordEnableColorArray[num] = colorVal; }
  turnRecordEnableButtonColor { | num = 1, colorVal = 0, bank = 'active' |
    var index = num-1;
    if( bank == 'active', { bank = activeGridBnk });
    mixerBankArray[bank][5][index][2] = colorVal;
    this.prTurnRecordEnableButtonColor(index, mixerBankArray[activeMixerBnk][5][index][2]);
  }
  turnRecordEnableButtonOn { | num, bank | this.turnRecordEnableButtonColor(num, 1, bank); }
  turnRecordEnableButtonOff { | num, bank | this.turnRecordEnableButtonColor(num, 0, bank); }

  prTurnDeviceButtonColor { | num, colorVal | deviceColorArray[num] = colorVal }
  turnDeviceButtonColor { | num = 1, colorVal = 0, bank = 'active' |
    var index = num-1;
    if( bank == 'active', { bank = activeGridBnk });
    deviceButtonsBankArray[bank][index][2] = colorVal;
    this.prTurnDeviceButtonColor(num, deviceColorArray[activeDeviceButtonsBnk][index][2]);
  }
  turnDeviceButtonOn { | num, bank | this.turnDeviceButtonColor(num, 1, bank); }
  turnDeviceButtonOff { | num, bank | this.turnDeviceButtonColor(num, 0, bank); }

  prTurnControlButtonColor { | num, colorVal | controlColorArray[num] = colorVal }
  turnControlButtonColor { | num = 0, colorVal = 0, bank = 'active' |
    if( bank == 'active', { bank = activeGridBnk });
    controlButtonsBankArray[bank][num][2] = colorVal;
    this.prTurnControlButtonColor(num, controlColorArray[activeControlButtonsBnk][num][2]);
  }
  turnControlButtonOn { | num, bank | this.turnControlButtonColor(num, 1, bank); }
  turnControlButtonOff { | num, bank | this.turnControlButtonColor(num, 0, bank); }

  /////// values:
  prSetMixerEncoderValue { | num, val | mixerEncoderValueArray[num] = val; }
  setMixerEncoderValue { | button = 1, val = 0, bank = 'active' |
    var index = button-1;
    if( bank == 'active', { bank = activeMixerEncoderBnk });
    mixerEncoderBankArray[bank][1][index] = val;
    this.prSetMixerEncoderValue(index, val);
  }

  prSetDeviceEncoderValue { | num, val | deviceEncoderValueArray[num] = val }
  setDeviceEncoderValue { | button = 1, val = 0, bank = 'active' |
    var index = button-1;
    if( bank == 'active', { bank = activeDeviceEncoderBnk });
    deviceEncoderBankArray[bank][1][index] = val;
    this.prSetDeviceEncoderValue(index, val);
  }
}