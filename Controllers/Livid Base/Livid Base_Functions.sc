+ Base {

  setGridFunc { | column = 0, row = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var num = ((row * 8) + column);
    var midiNum = num + 36;
    var midiType;
    if( page == 'active', { page = activePageKey });
    if( type == 'pressure', { midiType = 'control' }, { midiType = type });
    //if( bank == 'active', { bank = pageDict[page].activeGridBank; });
    pageDict[page].setGridFunc(column, row, func, type, bank);
    this.setFunc(midiNum, midiType, activePage.getFunc(midiNum, midiType));
  }

  setGridMonitorFunc { | column = 0, row = 0, func, bank = 'active', page = 'active' |
    var num = ((row * 8) + column);
    var midiType;
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeGridBnk });
    pageDict[page].setGridMonitorFunc(column, row, func, bank);
    activePage.gridBankArray[activePage.activeGridBnk][num][5].reset.play;
  }

  setControlButtonFunc { | button = 1, func, type = 'noteOn', bank = 'active', page = 'active' |
    var num = button + 17;
    if( page == 'active', { page = activePageKey });
    //if( bank == 'active', { bank = pageDict[page].activeControlButtonsBank; });
    pageDict[page].setControlButtonFunc(button, func, type);
    this.setFunc(num, type, activePage.getFunc(num, type));
  }

  setControlButtonMonitorFunc { | button = 1, func, bank = 'active', page = 'active' |
    var num = button - 1;
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeControlButtonsBnk; });
    pageDict[page].setControlButtonMonitorFunc(button, func, bank);
    activePage.controlButtonsBankArray[activePage.activeControlButtonsBnk][4][num].reset.play;
  }

  setTouchButtonFunc { | button = 1, func, type = 'noteOn', bank = 'active', page = 'active' |
    var num = button + 9;
    if( page == 'active', { page = activePageKey });
    //if( bank == 'active', { bank = pageDict[page].activeTouchButtonsBank; });
    pageDict[page].setTouchButtonFunc(button, func, type, bank);
    this.setFunc(num, type, activePage.getFunc(num, type));
  }

  setTouchButtonMonitorFunc { | button = 1, func, bank = 'active', page = 'active' |
    var num = button - 1;
    bank.postln;
    page.postln;
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeTouchButtonsBnk });
    //bank.postln;
    //page.postln;
    pageDict[page].setTouchButtonMonitorFunc(button, func, bank);
    activePage.touchButtonsBankArray[activePage.activeTouchButtonsBnk][4][num].reset.play;
  }

  setFaderFunc { | fader = 1, func, bank = 'active' page = 'active' |
    //var num = fader + 1;
    if( page == 'active', { page = activePageKey });
    //if( bank == 'active', { bank = pageDict[page].activeFadersBank; });
    pageDict[page].setFaderFunc(fader, func, bank);
    this.setFunc(fader, 'control', activePage.getFunc(fader, 'control'));
  }

  setFaderMonitorFunc { | fader = 1, func, bank = 'active', page = 'active' |
    var faderIndex = fader - 1;
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeFadersBnk; });
    pageDict[page].setFaderMonitorFunc(fader, func, bank);
    activePage.fadersBankArray[activePage.activeFadersBnk][faderIndex][4].reset.play;
  }

}