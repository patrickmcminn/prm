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

  setControlButtonFunc { | button = 1, func, type = 'noteOn', bank = 'active', page = 'active' |
    var num = button + 17;
    if( page == 'active', { page = activePageKey });
    //if( bank == 'active', { bank = pageDict[page].activeControlButtonsBank; });
    pageDict[page].setControlButtonFunc(button, func, type);
    this.setFunc(num, type, activePage.getFunc(num, type));
  }

  setTouchButtonFunc { | button = 1, func, type = 'noteOn', bank = 'active', page = 'active' |
    var num = button + 9;
    if( page == 'active', { page = activePageKey });
    //if( bank == 'active', { bank = pageDict[page].activeTouchButtonsBank; });
    pageDict[page].setTouchButtonFunc(button, func, type, bank);
    this.setFunc(num, type, activePage.getFunc(num, type));
  }

  setFaderFunc { | fader = 1, func, bank = 'active' page = 'active' |
    //var num = fader + 1;
    if( page == 'active', { page = activePageKey });
    //if( bank == 'active', { bank = pageDict[page].activeFadersBank; });
    pageDict[page].setFaderFunc(fader, func, bank);
    this.setFunc(fader, 'control', activePage.getFunc(fader, 'control'));
  }

}