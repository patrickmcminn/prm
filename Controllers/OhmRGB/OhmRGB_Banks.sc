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
    pageDict[page].setActiveGridBank(bank);
    64.do({ | num, index |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
  }

  setActiveLeftButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [65, 73, 66, 74];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveLeftButtonsBank(bank);
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
  }

  setActiveRightButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [67, 75, 68, 76];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveRightButtonsBank(bank);
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
  }

  setActiveCrossfaderButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [64, 72];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveCrossfaderButtonsBank(bank);
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
  }

  setActiveControlButtonsBank { | bank = 0, page = 'active' |
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveControlButtonsBank(bank);
    buttonArray.do({ | num |
      this.setNoteOnFunc(num, activePage.getNoteOnFunc(num));
      this.setNoteOffFunc(num, activePage.getNoteOffFunc(num));
      this.turnColor(num, activePage.getColor(num));
    });
  }

  /////// control bank set:

  setActiveLeftSlidersBank { | bank = 0, page = 'active' |
    var sliderArray = [23, 22, 15, 14];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveLeftSlidersBank(bank);
    sliderArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
  }

  setActiveRightSlidersBank { | bank = 0, page = 'active' |
    var sliderArray = [5, 7, 6, 4];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveRightSlidersBank(bank);
    sliderArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
  }

  setActiveLeftKnobsBank { | bank = 0, page = 'active' |
    var knobArray = [17, 19, 21, 16, 18, 20, 9, 11, 13, 8, 10, 12];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveLeftKnobsBank(bank);
    knobArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
  }

  setActiveRightKnobsBank { | bank = 0, page = 'active' |
    var knobArray = [3, 1, 0, 2];
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveRightKnobsBank(bank);
    knobArray.do({ | num, index | this.setCCFunc(num, activePage.getCCFunc(num)) });
  }

  setActiveCrossfaderBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveCrossfaderBank(bank);
    this.setCCFunc(24, activePage.getCCFunc(24));
  }

}