/*

*/

+ OhmRGB {

  //////// Note Functions:

  setGridFunc {  | column = 0, row = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var num = (column * 8) + row;
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeGridBank; });
    pageDict[page].setGridFunc(column, row, func, type, bank);
    this.setFunc(num, type, activePage.getFunc(num, type));
  }

  setLeftButtonFunc { | num = 0, func, type = 'noteOn', bank = 'active', page = 'active ' |
    var buttonArray = [65, 73, 66, 74];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeLeftButtonsBank; });
    pageDict[page].setLeftButtonFunc(num, func, type, bank);
    this.setFunc(buttonArray[num], type, activePage.getFunc(buttonArray[num], type));
  }

  setRightButtonFunc { | num = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var buttonArray = [67, 75, 68, 76];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeRightButtonsBank; });
    pageDict[page].setRightButtonFunc(num, func, type, bank);
    this.setFunc(buttonArray[num], type, activePage.getFunc(buttonArray[num], type));
  }

  setCrossfaderButtonFunc { | num = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var buttonArray = [64, 72];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeCrossfaderButtonsBank; });
    pageDict[page].setCrossfaderButtonFunc(num, func, type, bank);
    this.setFunc(buttonArray[num], type, activePage.getFunc(buttonArray[num], type));
  }

  setControlButtonFunc { | column = 0, row = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    var num = (column * 2) + row;
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeControlButtonsBank; });
    pageDict[page].setControlButtonFunc(num, func, type, bank);
    this.setFunc(buttonArray[num], type, activePage.getFunc(buttonArray[num], type));
  }

}