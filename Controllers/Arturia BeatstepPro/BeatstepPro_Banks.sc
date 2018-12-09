/*
Monday, November 26th 2018
BeatstepPro_Banks.sc
prm
*/

+ BeatstepPro {

  //////// adding banks:

  addSequencer1Banks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addSequencer1Banks(numBanks);
  }

  addSequencer2Banks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addSequencer2Banks(numBanks);
  }

  addDrumBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addDrumBanks(numBanks);
  }

  addControlBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addControlBanks(numBanks);
  }

  //////// setting banks:
  setActiveSequencer1Bank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveSequencer1Bank(bank);
    this.prSetAllSequencer1ButtonFuncs;
    activeSequencer1Bank = activePage.activeSequencer1Bnk;
  }

  setActiveSequencer2Bank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveSequencer2Bank(bank);
    this.prSetAllSequencer2ButtonFuncs;
    activeSequencer2Bank = activePage.activeSequencer2Bnk;
  }

  setActiveDrumBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveDrumBank(bank);
    this.prSetAllDrumButtonFuncs;
    activeDrumBank = activePage.activeDrumBnk;
  }

  setActiveControlBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveControlBank(bank);
    this.prSetAllControlButtonFuncs;
    this.prSetAllControlEncoderFuncs;
    activeControlBank = activePage.activeControlBnk;
  }

  //////// num banks:
  numSequencer1Banks { ^activePage.numSequencer1Banks }
  numSequencer2Banks { ^activePage.numSequencer2Banks }
  numDrumBanks { ^activePage.numDrumBanks }
  numControlBanks { ^activePage.numControlBanks }

}