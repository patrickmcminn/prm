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

  addMixerEncodersBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addMixerEncodersBanks(numBanks);
  }

  addDeviceButtonsBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addDeviceButtonsBanks(numBanks);
  }

  addDeviceEncodersBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addDeviceEncodersBanks(numBanks);
  }

  addControlButtonsBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addControlButtonsBanks(numBanks);
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
    pageDict[page].setActiveMixerBank(bank);
    this.prSetAllFaderFuncs;
    this.prSetAllTrackSelectFuncs;
    this.prSetAllTrackActivatorFuncs;
    this.prSetAllCrossfaderSelectFuncs;
    this.prSetAllSoloButtonFuncs;
    this.prSetAllRecordEnableButtonFuncs;

    this.prSetAllMixerColors;
  }

  setActiveMixerEncodersBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveMixerEncodersBank(bank);
    this.prSetAllMixerEncoderFuncs;
    this.prSetAllMixerEncoderValues;
  }

  setActiveDeviceButtonsBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveDeviceButtonsBank(bank);
    this.prSetAllDeviceButtonFuncs;
    this.prSetAllDeviceButtonColors;
  }

  setActiveDeviceEncodersBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveDeviceEncodersBank(bank);
    this.prSetAllDeviceEncoderFuncs;
    this.prSetAllDeviceEncoderValues;
  }

  setActiveControlButtonsBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveControlButtonsBank(bank);
    this.prSetAllControlButtonFuncs;
    this.prSetAllControlButtonColors;
  }

}