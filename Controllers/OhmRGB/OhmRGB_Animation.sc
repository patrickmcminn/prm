/*
Wednesday, April 30th 2014
OhmRGB_Animation.sc
*/

+ OhmRGB {

  // Setting Animations:

  stopGridAnimations { | bank = 'active', page = 'active' |
    //var num = (column * 8) + row;
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    64.do({ | led | pageDict[page].gridBankArray[bank][led][3].stop; });
  }

  playGridAnimations { | bank = 'active', page = 'active' |
    //var num = (column * 8) + row;
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    64.do({ | led | pageDict[page].gridBankArray[bank][led][3].play; });
  }

  blinkGridPlay { | column = 0, row = 0, color = 'red', clock = nil, mul = 1, width = 0.5, bank = 'active', page = 'active' |
    var num = (column * 8) + row;
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    pageDict[page].gridBankArray[bank][num][3].source = {
      loop {
        this.turnGridColor(column, row, color, bank, page);
        (((1.0 * mul)/2)/width).wait;
        this.turnGridOff(column, row, bank, page);
        (((1.0 * mul)/2)/(1-width)).wait;
      }
    };
   pageDict[page].gridBankArray[bank][num][3].play(clock);
  }

  blinkGridStop { | column = 0, row = 0, bank = 'active', page = 'active' |
    var num = (column * 8) + row;
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    pageDict[page].gridBankArray[bank][num][3].stop;
  }

  alternateGridPlay {
    | column = 0, row = 0, color1 = 'red', color2 = 'green', clock = nil, mul = 1, width = 0.5, bank = 'active', page = 'active' |
    var num = (column * 8) + row;
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    pageDict[page].gridBankArray[bank][num][3].source = {
      loop {
        this.turnGridColor(column, row, color1, bank, page);
        ((1.0 * mul)/width).wait;
        this.turnGridColor(column, row, color2, bank, page);
        ((1.0 * mul)/(1-width)).wait;
      }
    };
    pageDict[page].gridBankArray[bank][num][3].play(clock);
  }

  alternateGridStop { | column = 0, row = 0, bank = 'active', page = 'active' |
    var num = (column * 8) + row;
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    pageDict[page].gridBankArray[bank][num][3].stop;
  }

  stopLeftButtonAnimations { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    4.do({ | led | pageDict[page].leftButtonsBankArray[bank][led][3].stop; });
  }

  playLeftButtonAnimations { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    4.do({ | led | pageDict[page].leftButtonsBankArray[bank][led][3].play; });
  }

  blinkLeftButtonPlay { | num = 0, color = 'red', clock = nil, mul = 1, width = 0.5, bank = 'active', page = 'active' |
    //var buttonArray = [65, 73, 66, 74];
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeLeftButtonsBank });
    pageDict[page].leftButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnLeftButtonColor(num, color, bank, page);
        (((1.0 * mul)/2)/width).wait;
        this.turnLeftButtonOff(num, bank, page);
        (((1.0 * mul)/2)/(1-width)).wait;
      }
    };
    pageDict[page].leftButtonsBankArray[bank][num][3].play(clock);
  }

  blinkLeftButtonStop { | num = 0, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeLeftButtonsBank });
    pageDict[page].leftButtonsBankArray[bank][num][3].stop;
  }

  alternateLeftButtonPlay {
    | num = 0, color1 = 'red', color2 = 'green', clock = nil, mul = 1, width = 0.5, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeLeftButtonsBank });
    pageDict[page].leftButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnLeftButtonColor(num, color1, bank, page);
        ((1.0 * mul)/width).wait;
        this.turnLeftButtonColor(num, color2, bank, page);
        ((1.0 * mul)/(1-width)).wait;
      }
    };
    pageDict[page].leftButtonsBankArray[bank][num][3].play(clock);
  }

  stopRightButtonAnimations { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    4.do({ | led | pageDict[page].rightButtonsBankArray[bank][led][3].stop; });
  }

  playRightButtonAnimations { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    4.do({ | led | pageDict[page].rightButtonsBankArray[bank][led][3].play; });
  }

  blinkRightButtonPlay { | num = 0, color = 'red', clock = nil, mul = 1, width = 0.5,bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeRightButtonsBank });
    pageDict[page].rightButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnRightButtonColor(num, color, bank, page);
        (((1.0 * mul)/2)/width).wait;
        this.turnRightButtonOff(num, bank, page);
        (((1.0 * mul)/2)/(1-width)).wait;
      }
    };
    pageDict[page].rightButtonsBankArray[bank][num][3].play(clock);
  }

  blinkRightButtonStop { | num = 0, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeRightButtonsBank });
    pageDict[page].rightButtonsBankArray[bank][num][3].stop;
  }

  alternateRightButtonPlay {
    | num = 0, color1 = 'red', color2 = 'green', clock = nil, mul = 1, width = 0.5, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeRightButtonsBank });
    pageDict[page].rightButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnRightButtonColor(num, color1, bank, page);
        ((1.0 * mul)/width).wait;
        this.turnRightButtonColor(num, color2, bank, page);
        ((1.0 * mul)/(1-width)).wait;
      }
    };
    pageDict[page].rightButtonsBankArray[bank][num][3].play(clock);
  }

  alternateRightButtonStop { | num = 0, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeRightButtonsBank });
    pageDict[page].rightButtonsBankArray[bank][num][3].stop;
  }

  stopCrossfaderButtonAnimations { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    2.do({ | led | pageDict[page].crossfaderButtonsBankArray[bank][led][3].stop; });
  }

  playCrossfaderButtonAnimations { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    2.do({ | led | pageDict[page].crossfaderButtonsBankArray[bank][led][3].play; });
  }

  blinkCrossfaderButtonPlay { | num = 0, color = 'red', clock = nil, mul = 1, width = 0.5, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeCrossfaderButtonsBank });
    pageDict[page].crossfaderButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnCrossfaderButtonColor(num, color, bank, page);
        (((1.0 * mul)/2)/width).wait;
        this.turnCrossfaderButtonOff(num, bank, page);
        (((1.0 * mul)/2)/(1-width)).wait;
      }
    };
    pageDict[page].crossfaderButtonsBankArray[bank][num][3].play(clock);
  }

  blinkCrossfaderButtonStop { | num = 0, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeCrossfaderButtonsBank });
    pageDict[page].crossfaderButtonsBankArray[bank][num][3].stop;
  }

  alternateCrossfaderButtonPlay {
    | num = 0, color1 = 'red', color2 = 'green', clock = nil, mul = 1, width = 0.5, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeCrossfaderButtonsBank });
    pageDict[page].crossfaderButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnCrossfaderButtonColor(num, color1, bank, page);
        ((1.0 * mul)/width).wait;
        this.turnCrossfaderButtonColor(num, color2, bank, page);
        ((1.0 * mul)/(1-width)).wait;
      };
    };
    pageDict[page].crossfaderButtonsBankArray[bank][num][3].play;
  }

  alternateCrossfaderButtonStop { | num = 0, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeCrossfaderButtonsBank });
    pageDict[page].crossfaderButtonsBankArray[bank][num][3].stop;
  }

  stopControlButtonAnimations { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    7.do({ | led | pageDict[page].controlButtonsBankArray[bank][led][3].stop; });
  }

  playControlButtonAnimations { | bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    7.do({ | led | pageDict[page].controlButtonsBankArray[bank][led][3].play; });
  }

  blinkControlButtonPlay { | column = 0, row = 0, color = 'red', clock = nil, mul = 1, width = 0.5, bank = 'active', page = 'active' |
    var num = (column * 2) + row;
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeControlButtonsBank });
    pageDict[page].controlButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnControlButtonColor(column, row, color, bank, page);
        (((1.0 * mul)/2)/width).wait;
        this.turnControlButtonOff(column, row, bank, page);
        (((1.0 * mul)/2)/(1-width)).wait;
      };
    };
    pageDict[page].controlButtonsBankArray[bank][num][3].play;
  }

  blinkControlButtonStop { | column = 0, row = 0, bank = 'active', page = 'active' |
    var num = (column * 2) + row;
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeControlButtonsBank });
    pageDict[page].controlButtonsBankArray[bank][num][3].stop;
  }

  alternateControlButtonPlay {
    | column = 0, row = 0, color1 = 'red', color2 = 'green', clock = nil, mul = 1, width = 0.5, bank = 'active', page = 'active' |
    var num = (column * 2) + row;
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeControlButtonsBank });
    pageDict[page].controlButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnControlButtonColor(column, row, color1, bank, page);
        ((1.0 * mul)/width).wait;
        this.turnControlButtonColor(column, row, color2, bank, page);
        ((1.0 * mul)/(1-width)).wait;
      };
    };
    pageDict[page].controlButtonsBankArray[bank][num][3].play;
  }

  alternateControlButtonStop { | column = 0, row = 0, bank = 'active', page = 'active' |
    var num = (column * 2) + row;
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeControlButtonsBank });
    pageDict[page].controlButtonsBankArray[bank][num][3].stop;
  }
}