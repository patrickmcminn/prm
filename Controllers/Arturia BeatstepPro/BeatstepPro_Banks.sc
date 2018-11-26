/*
Monday, November 26th 2018
BeatstepPro_Banks.sc
prm
*/

+ BeatstepPro {

  //////// adding banks:

  addSequencer1Banks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addSequencer1Banks;
  }

  addSequencer2Banks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addSequencer2Banks;
  }

  addDrumBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addDrumBanks;
  }

  addControlBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addControlBanks;
  }

  //////// setting banks:
  setActiveSequencer1Bank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveSequencer1Bank(bank);
    this.prSetAllSequencer1ButtonFuncs;
    activeSequencer1Bank = activePage.activeSequencer1Bank;
  }

  setActiveSequencer2Bank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveSequencer2Bank(bank);
    this.prSetAllSequencer2ButtonFuncs;
    activeSequencer2Bank = activePage.activeSequencer2Bank;
  }

}