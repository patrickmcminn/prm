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
    if( page == 'active', { page = activePageKey; });
    pageDict[page].setActiveGridBank(bank);
    activeGridBank = pageDict[page].activeGridBank;
    64.do({ | num, index |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
  }

  setActiveLeftButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [65, 73, 66, 74];
    if( page == 'active', { page = activePageKey; activeLeftButtonsBank = bank; });
    pageDict[page].setActiveLeftButtonsBank(bank);
    activeLeftButtonsBank = pageDict[page].activeLeftButtonsBank;
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
  }

  setActiveRightButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [67, 75, 68, 76];
    if( page == 'active', { page = activePageKey; activeRightButtonsBank = bank; });
    pageDict[page].setActiveRightButtonsBank(bank);
    activeRightButtonsBank = pageDict[page].activeRightButtonsBank;
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
  }

  setActiveCrossfaderButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [64, 72];
    if( page == 'active', { page = activePageKey; activeCrossfaderButtonsBank = bank; });
    pageDict[page].setActiveCrossfaderButtonsBank(bank);
    activeCrossfaderButtonsBank = pageDict[page].activeCrossfaderButtonsBank;
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
  }

  setActiveControlButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    if( page == 'active', { page = activePageKey; activeControlButtonsBank = bank; });
    pageDict[page].setActiveControlButtonsBank(bank);
    activeControlButtonsBank = pageDict[page].activeControlButtonsBank;
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
  }

  /////// control bank set:

  setActiveLeftSlidersBank { | bank = 0, page = 'active' |
    var sliderArray = [23, 22, 15, 14];
    if( page == 'active', { page = activePageKey; activeLeftSlidersBank = bank; });
    pageDict[page].setActiveLeftSlidersBank(bank);
    activeLeftSlidersBank = pageDict[page].activeLeftSlidersBank;
    sliderArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
  }

  setActiveRightSlidersBank { | bank = 0, page = 'active' |
    var sliderArray = [5, 7, 6, 4];
    if( page == 'active', { page = activePageKey; activeRightSlidersBank = bank; });
    pageDict[page].setActiveRightSlidersBank(bank);
    activeRightSlidersBank = pageDict[page].activeRightSlidersBank;
    sliderArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
  }

  setActiveLeftKnobsBank { | bank = 0, page = 'active' |
    var knobArray = [17, 19, 21, 16, 18, 20, 9, 11, 13, 8, 10, 12];
    if( page == 'active', { page = activePageKey; activeLeftKnobsBank = bank; });
    pageDict[page].setActiveLeftKnobsBank(bank);
    activeLeftKnobsBank = pageDict[page].activeLeftKnobsBank;
    knobArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
  }

  setActiveRightKnobsBank { | bank = 0, page = 'active' |
    var knobArray = [3, 1, 0, 2];
    if( page == 'active', { page = activePageKey; activeRightKnobsBank = bank; });
    pageDict[page].setActiveRightKnobsBank(bank);
    activeRightKnobsBank = pageDict[page].activeRightKnobsBank;
    knobArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
  }

  setActiveCrossfaderBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey; activeCrossfaderBank = bank; });
    pageDict[page].setActiveCrossfaderBank(bank);
    activeCrossfaderBank = pageDict[page].activeCrossfaderBank;
    this.setCCFunc(24, activePage.getCCFunc(24));
  }

}