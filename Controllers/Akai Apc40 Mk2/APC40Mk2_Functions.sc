/*
Monday, May 28th 2018
APC40Mk2_Functions.sc
prm
*/

+ APC40Mk2 {

  setGridFunc { | column = 0, row = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    var num = ((row * 8) + column);
    if( page == 'active', { page = activePageKey });
    pageDict[page].setGridFunc(column, row, func, type, bank);
    this.setFunc(num, activePage.getFunc(num));
  }



}