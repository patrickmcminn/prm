~ohmFunc = ( );

~ohmFunc.addMasterFaders = { | func, page, bank = 'active' |

  // Slider for Master Volume
  ~ohm.setRightSliderFunc(3, { | val |
    ~im.systemMixer.setVol(0, val.ccdbfs(-140, 0));
  }, page: page, bank: bank);

  // Slider for Delay Volume:
  ~ohm.setRightSliderFunc(0, { | val |
    ~im.delay.setVol(0, val.ccdbfs(-70, 0));
  }, page: page, bank: bank);

  // Slider for Reverb Volume:
  ~ohm.setRightSliderFunc(1, { | val |
    ~im.reverb.setVol(0, val.ccdbfs(-70, 0));
  }, page: page, bank: bank);

  // Slider for Granulator Volume:
  ~ohm.setRightSliderFunc(2, { | val |
    ~im.granulator.setVol(0, val.ccdbfs(-70, 6));
  }, page: page, bank: bank);

  // buttons for muting:
  //4.do({ | index | ~ohm.turnRightButtonGreen(index) }, page: page, bank: bank);
  ~ohm.setRightButtonFunc(0, {
    if( ~im.delay.isMuted,
      { ~im.delay.unMute; ~ohm.turnRightButtonGreen(0); },
      { ~im.delay.mute; ~ohm.turnRightButtonRed(0); }
    );
  }, page: page, bank: bank);

  ~ohm.setRightButtonFunc(1, {
    if( ~im.reverb.isMuted,
      { ~im.reverb.unMute; ~ohm.turnRightButtonGreen(1); },
      { ~im.reverb.mute; ~ohm.turnRightButtonRed(1); }
    );
  }, page: page, bank: bank);

  ~ohm.setRightButtonFunc(2, {
    if( ~im.granulator.isMuted,
      { ~im.granulator.unMute; ~ohm.turnRightButtonGreen(2); },
      { ~im.granulator.mute; ~ohm.turnRightButtonRed(2); }
    );
  }, page: page, bank: bank);
};


~ohmFunc.addDryFaders = { | func, page, bank = 'active' |

  4.do({ | index | ~ohm.setLeftSliderFunc(index, { | val |
    ~directIns.setVol(index, val.ccdbfs(-70, 0), 0.1);
    }, page: page, bank: bank);
  });

  // Buttons for Input Muting:
  4.do({ | index | ~ohm.turnLeftButtonGreen(index, page: page, bank: bank) });
  4.do({ | index | ~ohm.setLeftButtonFunc(index, {
    if( ~directIns.isMuted(index),
      { ~directIns.unMute(index); ~ohm.turnLeftButtonGreen(index); },
      { ~directIns.mute(index); ~ohm.turnLeftButtonRed(index); }
    );
    }, page: page, bank: bank);
  });

  // Delay Sends for Input Channels:
  4.do({ | index | ~ohm.setLeftKnobFunc(index, 2, { | val |
    ~directIns.mixer.setSendVol(index, 0, val.ccdbfs(-70, 0));
    }, page: page, bank: bank);
  });

  // Reverb Sends for Input Channels:
  4.do({ | index | ~ohm.setLeftKnobFunc(index, 1, { | val |
    ~directIns.mixer.setSendVol(index, 1, val.ccdbfs(-70, 0));
    }, page: page, bank: bank);
  });

  // Granulator Sends for Input Channels:
  4.do({ | index | ~ohm.setLeftKnobFunc(index, 0, { | val |
    ~directIns.mixer.setSendVol(index, 2, val.ccdbfs(-70, 0));
    }, page: page, bank: bank);
  });
};

/*
~ohmFunc.addHomeButton = { | page = 'active', bank = 'active' |
  ~ohm.setControlButtonFunc(3, 0, { ~ohm.setPage('main'); }, bank: bank, page: page);
};
*/


