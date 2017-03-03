+ OhmRGB {

  //////// add notes banks:

  addGridBanks { | num = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addGridBanks(num);
  }

  addLeftButtonsBanks { | num = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addLeftButtonsBanks(num);
  }

  addRightButtonsBanks { | num = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addRightButtonsBanks(num);
  }

  addCrossfaderButtonsBanks { | num = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addCrossfaderButtonsBanks(num);
  }

  addControlButtonsBanks { | num = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addControlButtonsBanks(num);
  }

  //////// add control banks:

  addLeftSlidersBanks { | num = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addLeftSlidersBanks(num);
  }

  addRightSlidersBanks { | num = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addRightSlidersBanks(num);
  }

  addLeftKnobsBanks { | num = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addLeftKnobsBanks(num);
  }

  addRightKnobsBanks { | num = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addRightKnobsBanks(num);
  }

  addCrossfaderBanks { | num = 1, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].addCrossfaderBanks(num);
  }

  /////////  note bank set:

  setActiveGridBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    activePage.stopActiveGridBankMonitorRoutines;
    pageDict[page].setActiveGridBank(bank);
    64.do({ | num, index |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
    activePage.startActiveGridBankMonitorRoutines;
    this.prSetCurrentBanks;
  }

  setActiveLeftButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [65, 73, 66, 74];
    if( page == 'active', { page = activePageKey });
    activePage.stopActiveLeftButtonsBankMonitorRoutines;
    pageDict[page].setActiveLeftButtonsBank(bank);
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
    activePage.startActiveLeftButtonsBankMonitorRoutines;
    this.prSetCurrentBanks;
  }

  setActiveRightButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [67, 75, 68, 76];
    if( page == 'active', { page = activePageKey });
    activePage.stopActiveRightButtonsBankMonitorRoutines;
    pageDict[page].setActiveRightButtonsBank(bank);
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
    activePage.startActiveRightButtonsBankMonitorRoutines;
    this.prSetCurrentBanks;
  }

  setActiveCrossfaderButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [64, 72];
    if( page == 'active', { page = activePageKey });
    activePage.stopActiveCrossfaderButtonsBankMonitorRoutines;
    pageDict[page].setActiveCrossfaderButtonsBank(bank);
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
    activePage.startActiveCrossfaderButtonsBankMonitorRoutines;
    this.prSetCurrentBanks;
  }

  setActiveControlButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    if( page == 'active', { page = activePageKey });
    activePage.stopActiveControlButtonsBankMonitorRoutines;
    pageDict[page].setActiveControlButtonsBank(bank);
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
    activePage.startActiveBankMonitorRoutines;
    this.prSetCurrentBanks;
  }

  /////// control bank set:

  setActiveLeftSlidersBank { | bank = 0, page = 'active' |
    var sliderArray = [23, 22, 15, 14];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveLeftSlidersBank(bank);
    sliderArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
    this.prSetCurrentBanks;
  }

  setActiveRightSlidersBank { | bank = 0, page = 'active' |
    var sliderArray = [5, 7, 6, 4];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveRightSlidersBank(bank);
    sliderArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
    this.prSetCurrentBanks;
  }

  setActiveLeftKnobsBank { | bank = 0, page = 'active' |
    var knobArray = [17, 19, 21, 16, 18, 20, 9, 11, 13, 8, 10, 12];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveLeftKnobsBank(bank);
    knobArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
    this.prSetCurrentBanks;
  }

  setActiveRightKnobsBank { | bank = 0, page = 'active' |
    var knobArray = [3, 1, 0, 2];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveRightKnobsBank(bank);
    knobArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
    this.prSetCurrentBanks;
  }

  setActiveCrossfaderBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveCrossfaderBank(bank);
    this.setCCFunc(24, activePage.getCCFunc(24));
    this.prSetCurrentBanks;
  }

  prSetCurrentBanks {
    activeGridBank = pageDict[activePageKey].activeGridBnk;
    activeLeftButtonsBank = pageDict[activePageKey].activeLeftButtonsBnk;
    activeRightButtonsBank = pageDict[activePageKey].activeRightButtonsBnk;
    activeControlButtonsBank = pageDict[activePageKey].activeControlButtonsBnk;
    activeLeftSlidersBank = pageDict[activePageKey].activeLeftSlidersBnk;
    activeRightSlidersBank = pageDict[activePageKey].activeRightSlidersBnk;
    activeLeftKnobsBank = pageDict[activePageKey].activeLeftKnobsBnk;
    activeRightKnobsBank = pageDict[activePageKey].activeRightKnobsBnk;
    activeCrossfaderBank = pageDict[activePageKey].activeCrossfaderBnk;
    activeCrossfaderButtonsBank = pageDict[activePageKey].activeCrossfaderButtonsBnk;
  }

}