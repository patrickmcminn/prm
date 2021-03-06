/*

*/

+ OhmRGB {

  //////// Note Functions:

  setGridFunc {  | column = 0, row = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var num = (column * 8) + row;
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeGridBnk; });
    pageDict[page].setGridFunc(column, row, func, type, bank);
    this.setFunc(num, type, activePage.getFunc(num, type));
  }

  setGridMonitorFunc { | column = 0, row = 0, func, bank = 'active', page = 'active' |
    var num = (column * 8) + row;
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeGridBnk; });
    pageDict[page].setGridMonitorFunc(column, row, func, bank);
    activePage.gridBankArray[activePage.activeGridBnk][num][4].reset.play;
  }

  setLeftButtonFunc { | num = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var buttonArray = [65, 73, 66, 74];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeLeftButtonsBnk; });
    pageDict[page].setLeftButtonFunc(num, func, type, bank);
    this.setFunc(buttonArray[num], type, activePage.getFunc(buttonArray[num], type));
  }

  setLeftButtonMonitorFunc { | num = 0, func, bank = 'active', page = 'active' |
    var buttonArray = [65, 73, 66, 74];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeLeftButtonsBnk; });
    pageDict[page].setLeftButtonMonitorFunc(num, func, bank);
    activePage.leftButtonsBankArray[activePage.activeLeftButtonsBnk][num][4].reset.play;
  }

  setRightButtonFunc { | num = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var buttonArray = [67, 75, 68, 76];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeRightButtonsBnk; });
    pageDict[page].setRightButtonFunc(num, func, type, bank);
    this.setFunc(buttonArray[num], type, activePage.getFunc(buttonArray[num], type));
  }

  setRightButtonMonitorFunc { | num = 0, func, bank = 'active', page = 'active' |
    var buttonArray = [67, 75, 68, 76];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeRightButtonsBnk; });
    pageDict[page].setRightButtonMonitorFunc(num, func, bank);
    activePage.rightButtonsBankArray[activePage.activeRightButtonsBnk][num][4].reset.play;
  }

  setCrossfaderButtonFunc { | num = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var buttonArray = [64, 72];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeCrossfaderButtonsBnk; });
    pageDict[page].setCrossfaderButtonFunc(num, func, type, bank);
    this.setFunc(buttonArray[num], type, activePage.getFunc(buttonArray[num], type));
  }

  setCrossfaderButtonMonitorFunc { | num = 0, func, bank = 'active', page = 'active' |
    var buttonArray = [64, 72];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeCrossfaderButtonsBnk; });
    pageDict[page].setCrossfaderButtonMonitorFunc(num, func, bank);
    activePage.crossfaderButtonsBankArray[activePage.activeCrossfaderBnk][num][4].reset.play;
  }

  setControlButtonFunc { | column = 0, row = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    var num = (column * 2) + row;
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeControlButtonsBnk; });
    pageDict[page].setControlButtonFunc(column, row, func, type, bank);
    this.setFunc(buttonArray[num], type, activePage.getFunc(buttonArray[num], type));
  }

  setControlButtonMonitorFunc { | column = 0, row = 0, func, bank = 'active', page = 'active' |
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    var num = (column * 2) + row;
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeControlButtonsBnk; });
    pageDict[page].setControlButtonMonitorFunc(column, row, func, bank);
    activePage.controlButtonsBankArray[activePage.activeControlButtonsBnk][num][4].reset.play;
  }

  setLeftSliderFunc { | num = 0, func, bank = 'active', page = 'active' |
    var sliderArray = [23, 22, 15, 14];
    if( page == 'active', { page = activePageKey;  });
    if( bank == 'active', { bank = pageDict[page].activeLeftSlidersBnk;  });
    pageDict[page].setLeftSliderFunc(num, func, bank);
    this.setCCFunc(sliderArray[num], activePage.getCCFunc(sliderArray[num]));
  }

  setRightSliderFunc { | num = 0, func, bank = 'active', page = 'active' |
    var sliderArray = [5, 7, 6, 4];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeRightSlidersBnk; });
    pageDict[page].setRightSliderFunc(num, func, bank);
    this.setCCFunc(sliderArray[num], activePage.getCCFunc(sliderArray[num]));
  }

  setLeftKnobFunc { | column = 0, row = 0, func, bank = 'active', page = 'active' |
    var num = (column * 3) + row;
    var knobArray = [17, 19, 21, 16, 18, 20, 9, 11, 13, 8, 10, 12];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeLeftKnobsBnk; });
    pageDict[page].setLeftKnobFunc(column, row, func, bank);
    this.setCCFunc(knobArray[num], activePage.getCCFunc(knobArray[num]));
  }

  setRightKnobFunc { | num, func, bank = 'active', page = 'active' |
    var knobArray = [3, 1, 0, 2];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeRightKnobsBnk; });
    pageDict[page].setRightKnobFunc(num, func, bank);
    this.setCCFunc(knobArray[num], activePage.getCCFunc(knobArray[num]));
  }

  setCrossfaderFunc { | func, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeCrossfaderBnk; });
    pageDict[page].setCrossfaderFunc(func, bank);
    this.setCCFunc(24, activePage.getCCFunc(24));
  }
}