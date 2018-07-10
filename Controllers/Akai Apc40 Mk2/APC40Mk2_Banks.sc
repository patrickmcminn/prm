/*
Monday, May 28th 2018
APC40Mk2_Banks.sc
prm
*/

+ APC40Mk2 {

  ////// adding banks:

  addGridBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addGridBanks(numBanks);
  }

  addSceneLaunchBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addSceneLaunchBanks(numBanks);
  }

  addClipStopBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addClipStopBanks(numBanks);
  }

  addMixerBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addMixerBanks(numBanks);
  }

  addMixerEncoderBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addMixerEncoderBanks(numBanks);
  }

  addDeviceButtonsBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addDeviceBanks(numBanks);
  }

  addDeviceEncodersBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addDeviceBanks(numBanks);
  }

  addControlBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addDeviceBanks(numBanks);
  }

  ///// setting banks:

  setActiveGridBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveGridBank(bank);
    this.prSetAllGridFuncs;
    this.prSetAllGridColors;

  }

  setActiveSceneLaunchBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveSceneLaunchBank(bank);
    this.prSetAllSceneLaunchFuncs;
    this.prSetAllSceneLaunchColors;
  }

  setActiveClipStopBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveClipStopBank(bank);
    this.prSetAllClipStopFuncs;
    this.prSetAllClipStopColors;
  }

  setActiveMixerBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveMixerBank;
    this.prSetAllFaderFuncs;
    this.prSetAllTrackSelectFuncs;
    this.prSetAllTrackActivatorFuncs;
    this.prSetAllCrossfaderSelectFuncs;
    this.prSetAllSoloFuncs;
    this.prSetAllRecordEnableFuncs;

    this.prSetAllMixerColors;
  }

  setActiveMixerEncoderBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveMixerEncoderBank;
    this.prSetAllMixerEncoderFuncs;
    this.prSetAllMixerEncoderValues;
  }

  setActiveDeviceButtonsBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveDeviceButtonsBank;
    this.prSetAllDeviceButtonFuncs;
    this.prSetAllDeviceButtonColors;
  }

  setActiveDeviceEncodersBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveDeviceEncodersBank;
    this.prSetAllDeviceEncoderFuncs;
    this.prSetAllDeviceEncoderValues;
  }

  setActiveControlBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveControlBank;
    this.prSetAllControlButtonsFuncs;
    this.prSetAllControlButtonColors;
  }

}