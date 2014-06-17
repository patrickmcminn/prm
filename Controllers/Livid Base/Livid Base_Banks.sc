/*
*/

+ Base {

  //////// adding banks:
  addGridBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addGridBanks(numBanks);
  }

  addControlButtonsBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addControlButtonsBanks(numBanks);
  }

  addTouchButtonsBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addTouchButtonsBanks(numBanks);
  }

  addFadersBanks { | numBanks = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addFadersBanks(numBanks);
  }

  /////// setting banks:

  setActiveGridBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveGridBank(bank);
    32.do({ | index |
      var num = index + 36;
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.setControlFunc(num, activePage.getControlFunc(num));
      this.turnButtonColor(num, activePage.getButtonColor(num));
    });
  }

  setActiveControlButtonsBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveControlButtonsBank(bank);
    8.do({ | index |
      var num = index + 18;
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
    });
    16.do({ | index |
      var num = index + 18;
      this.turnButtonColor(num, activePage.getButtonColor(num));
    });
  }

  setActiveTouchButtonsBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveTouchButtonsBank(bank);
    8.do({ | index |
      var num = index + 10;
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnButtonColor(num, activePage.getButtonColor(num));
    });
    8.do({ | index |
      var num = index + 68;
      this.turnButtonColor(num, activePage.getButtonColor(num));
    });
  }

  setActiveFadersBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveFadersBank(bank);
    9.do({ | index |
      var num = index;
      var valNum = index + 1;
      var modeNum = index + 10;
      this.setControlFunc(index + 1, activePage.getControlFunc(index + 1));
      this.prSetFaderValue(valNum, activePage.getFaderValue(index));
      this.prSetFaderMode(modeNum, activePage.getFaderMode(index));
    });
  }

}