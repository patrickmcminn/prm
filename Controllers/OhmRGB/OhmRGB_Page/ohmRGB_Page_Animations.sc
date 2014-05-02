/*
Wednesday, April 30th 2014
OhmRGB_Animation.sc
*/

+ OhmRGB_Page {

  // Setting Animations:

  blinkGridPlay { | column = 0, row = 0, color = 'red', clock = nil, mul = 1, width = 0.5, bank = 'active' |
    var num = (column * 8) + row;
    if( bank == 'active', { bank = activeGridBank });
    gridBankArray[bank][num][3].source = {
      loop {
        this.turnGridColor(column, row, color, bank);
        (((1.0 * mul)/2)/width).wait;
        this.turnGridOff(column, row, bank);
        (((1.0 * mul)/2)/(1-width)).wait;
      }
    };
    gridBankArray[bank][num][3].play(clock);
  }

  blinkGridStop { | column = 0, row = 0, bank = 'active' |
    var num = (column * 8) + row;
    if( bank == 'active', { bank = activeGridBank });
    gridBankArray[bank][num][3].stop;
  }

  alternateGridPlay { | column = 0, row = 0, color1 = 'red', color2 = 'green', clock = nil, mul = 1, width = 0.5, bank = 'active' |
    var num = (column * 8) + row;
    if( bank == 'active', { bank = activeGridBank });
    gridBankArray[bank][num][3].source = {
      loop {
        this.turnGridColor(column, row, color1, bank);
        (((1.0 * mul)/2)/width).wait;
        this.turnGridColor(column, row, color2, bank);
        ((1.0 * mul)/(1-width)).wait;
      }
    };
    gridBankArray[bank][num][3].play(clock);
  }

  alternateGridStop { | column = 0, row = 0, bank = 'active' |
    var num = (column * 8) + row;
    if( bank == 'active', { bank = activeGridBank });
    gridBankArray[bank][num][3].stop;
  }

  blinkLeftButtonPlay { | num = 0, color = 'red', clock = nil, mul = 1, width = 0.5, bank = 'active' |
    //var buttonArray = [65, 73, 66, 74];
    if( bank == 'active', { bank = activeLeftButtonsBank });
    leftButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnLeftButtonColor(num, color, bank);
        (((1.0 * mul)/2)/width).wait;
        this.turnLeftButtonOff(num, bank);
        (((1.0 * mul)/2)/(1-width)).wait;
      }
    };
    leftButtonsBankArray[bank][num][3].play(clock);
  }

  blinkLeftButtonStop { | num = 0, bank = 'active' |
    if( bank == 'active', { bank = activeLeftButtonsBank });
    leftButtonsBankArray[bank][num][3].stop;
  }

  alternateLeftButtonPlay { | num = 0, color1 = 'red', color2 = 'green', clock = nil, mul = 1, width = 0.5, bank = 'active' |
    if( bank == 'active', { bank = activeLeftButtonsBank });
    leftButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnLeftButtonColor(num, color1, bank);
        (((1.0 * mul)/2)/width).wait;
        this.turnLeftButtonColor(num, color2, bank);
        ((1.0 * mul)/(1-width)).wait;
      }
    };
    leftButtonsBankArray[bank][num][3].play(clock);
  }

  alternateLeftButtonStop { | num = 0, bank = 'active' |
    if( bank == 'active', { bank = activeLeftButtonsBank });
    leftButtonsBankArray[bank][num][3].stop;
  }

  blinkRightButtonPlay { | num = 0, color = 'red', clock = nil, mul = 1, width = 0.5, bank = 'active' |
    if( bank == 'active', { bank = activeRightButtonsBank });
    rightButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnRightButtonColor(num, color, bank);
        (((1.0 * mul)/2)/width).wait;
        this.turnRightButtonOff(num, bank);
        (((1.0 * mul)/2)/(1-width)).wait;
      }
    };
    rightButtonsBankArray[bank][num][3].play(clock);
  }

  blinkRightButtonStop { | num = 0, bank = 'active' |
    if( bank == 'active', { bank = activeRightButtonsBank });
    rightButtonsBankArray[bank][num][3].stop;
  }

  alternateRightButtonPlay { | num = 0, color1 = 'red', color2 = 'green', clock = nil, mul = 1, width = 0.5, bank = 'active' |
    if( bank == 'active', { bank = activeRightButtonsBank });
    rightButtonsBankArray[bank][num][3].source = {
      loop {
        this.turnRightButtonColor(num, color1, bank);
        (((1.0 * mul)/2)/width).wait;
        this.turnRightButtonColor(num, color2, bank);
        ((1.0 * mul)/(1-width)).wait;
      }
    };
    rightButtonsBankArray[bank][num][3].play(clock);
  }

  alternateRightButtonStop { | num = 0, bank = 'active' |
    if( bank == 'active', { bank = activeRightButtonsBank });
    rightButtonsBankArray[bank][num][3].stop;
  }
}