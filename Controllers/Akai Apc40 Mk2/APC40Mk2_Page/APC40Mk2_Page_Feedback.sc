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
  getCrossfaderSelectButtonColor { | num | ^crossfaderSelectColorArray[num] }
  getSoloButtonColor { | num | ^soloColorArray[num] }
  getRecordEnableButtonColor { | num | ^recordEnableColorArray[num] }

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
    if( bank == 'active', { bank = activeGridBnk });
    clipStopBankArray[bank][num][2] = colorVal;
    this.prTurnClipStopButtonColor(num, clipStopBankArray[activeClipStopBnk][num][2]);
  }
  turnClipStopButtonOn { | num, bank | this.turnClipStopButtonColor(num, 1, bank) }
  turnClipStopButtonOff { | num, bank | this.turnClipStopButtonColor(num, 0, bank); }

  prTurnTrackSelectButtonColor { | num, colorVal | selectColorArray[num] = colorVal }

  turnTrackSelectButtonColor { | num = 1, colorVal = 0, bank = 'active' |
    if( bank == 'active', { bank = activeGridBnk });
    mixerBankArray[bank][1][num][2] = colorVal;
    this.prTurnTrackSelectButtonColor(num, mixerBankArray[activeMixerBnk][1][num][2]);
  }
  turnTrackSelectButtonOn { | num, bank | this.turnTrackSelectButtonColor(num, 1, bank); }
  turnTrackSelectButtonOff { | num, bank | this.turnTrackSelectButtonColor(num, 0, bank); }

  prTurnTrackActivatorButtonColor { | num, colorVal | trackActivatorColorArray[num] = colorVal }

  turnTrackActivatorButtonColor { | num = 1, colorVal = 0, bank = 'active' |
    if( bank == 'active', { bank = activeGridBnk });
    mixerBankArray[bank][2][num][2] = colorVal;
    this.prTurnTrackActivatorButtonColor(num, mixerBankArray[activeMixerBnk][2][num][2]);
  }
  turnTrackActivatorButtonOn { | num, bank | this.turnTrackActivatorButtonColor(num, 1, bank); }
  turnTrackActivatorButtonOff { | num, bank | this.turnTrackActivatorButtonColor(num, 0, bank); }

  prTurnCrossfaderSelectButtonColor { | num, colorVal | crossfaderSelectColorArray[num] = colorVal }
  turnCrossfaderSelectButtonColor { | num = 1, colorVal = 0, bank = 'active' |
    if( bank == 'active', { bank = activeGridBnk });
    mixerBankArray[bank][3][num][2] = colorVal;
    this.prTurnCrossfaderSelectButtonColor(num, mixerBankArray[activeMixerBnk][3][num][2]);
  }
  turnCrossfaderSelectButtonOn { | num, bank | this.turnCrossfaderSelectButtonColor(num, 1, bank) }
  turnCrossfaderSelectButtonOff { | num, bank | this.turnCrossfaderSelectButtonColor(num, 0, bank) }

  prTurnSoloButtonColor { | num, colorVal | soloColorArray[num] = colorVal }
  turnSoloButtonColor { | num = 1, colorVal = 0, bank = 'active' |
    if( bank == 'active', { bank = activeGridBnk });
    mixerBankArray[bank][4][num][2] = colorVal;
    this.prTurnSoloButtonColor(num, mixerBankArray[activeMixerBnk][4][num][2]);
  }
  turnSoloButtonOn { | num, bank | this.turnSoloButtonColor(num, 1, bank); }
  turnSoloButtonOff { | num, bank | this.turnSoloButtonColor(num, 0, bank); }

  prTurnRecordEnableButtonColor { | num, colorVal | recordEnableColorArray[num] = colorVal; }
  turnRecordEnableButtonColor { | num = 0, colorVal = 0, bank = 'active' |
    if( bank == 'active', { bank = activeGridBnk });
    mixerBankArray[bank][5][num][2] = colorVal;
    this.prTurnRecordEnableButtonColor(num, mixerBankArray[activeMixerBnk][5][num][2]);
  }
  turnRecordEnableButtonOn { | num, bank | this.turnRecordEnableButtonColor(num, 1, bank); }
  turnRecordEnableButtonOff { | num, bank | this.turnRecordEnableButtonColor(num, 0, bank); }

  prTurnDeviceButtonColor { | num, colorVal | deviceColorArray[num] = colorVal }
  turnDeviceButtonColor { | num = 0, colorVal = 0, bank = 'active' |
    if( bank == 'active', { bank = activeGridBnk });
    deviceButtonsBankArray[bank][num][2] = colorVal;
    this.prTurnDeviceButtonColor(num, deviceButtonsBankArray[activeDeviceButtonsBnk][num][2]);
  }
  turnDeviceButtonOn { | num, bank | this.turnDeviceButtonColor(num, 1, bank); }
  turnDeviceButtonOff { | num, bank | this.turnDeviceButtonColor(num, 0, bank); }

  prTurnControlButtonColor { | num, colorVal | controlColorArray[num] = colorVal }
  turnControlButtonColor { | num = 0, colorVal = 0, bank = 'active' |
    if( bank == 'active', { bank = activeGridBnk });
    controlButtonsBankArray[bank][num][2] = colorVal;
    this.prTurnControlButtonColor(num, controlButtonsBankArray[activeControlButtonsBnk][num][2]);
  }
  turnControlButtonOn { | num, bank | this.turnControlButtonColor(num, 1, bank); }
  turnControlButtonOff { | num, bank | this.turnControlButtonColor(num, 0, bank); }

  /////// values:
  prSetMixerEncoderValue { | num, val | mixerEncodersValueArray[num] = val; }
  setMixerEncoderValue { | encoder = 0, val = 0, bank = 'active' |
    if( bank == 'active', { bank = activeMixerEncodersBnk });
    mixerEncodersBankArray[bank][encoder][1] = val;
    this.prSetMixerEncoderValue(encoder, val);
  }

  prSetDeviceEncoderValue { | num, val | deviceEncodersValueArray[num] = val }
  setDeviceEncoderValue { | encoder = 0, val = 0, bank = 'active' |
    if( bank == 'active', { bank = activeDeviceEncodersBnk });
    deviceEncodersBankArray[bank][encoder][1] = val;
    this.prSetDeviceEncoderValue(encoder, val);
  }
}