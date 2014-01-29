+ OhmRGB {

  //////// Note Banks:


  /////////  note bank set:

  setActiveGridBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveGridBank(bank);
  }

  setActiveLeftButtonsBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveLeftButtonsBank(bank);
  }

  setActiveRightButtonsBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveRightButtonsBank(bank);
  }

  setActiveCrossfaderButtonsBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveCrossfaderButtonsBank(bank);
  }

  setActiveControlButtonsBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveControlButtonsBank(bank);
  }

  /////// control bank set:

  setActiveLeftSlidersBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveLeftSlidersBank(bank);
  }

  setActiveRightSlidersBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveRightSlidersBank(bank);
  }

  setActiveLeftKnobsBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveLeftKnobsBank(bank);
  }

  setActiveRightKnobsBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveRightKnobsBank(bank);
  }

  setActiveCrossfaderBank { | bank = 0, page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setActiveCrossfaderBank(bank);
  }




}