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

  addMixerBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addMixerBanks(numBanks);
  }

  addDeviceBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addDeviceBanks(numBanks);
  }

  addControlBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addDeviceBanks(numBanks);
  }

  ///// setting banks:


}
