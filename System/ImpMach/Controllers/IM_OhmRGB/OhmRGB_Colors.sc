
+ IM_OhmRGB {
  //////// colors:

  turnColor { | num, color = \off |
    switch(color,
      { \off }, { midiOutPort.noteOn(16, colorArray[num], 0) },
      { \red }, { midiOutPort.noteOn(16, colorArray[num], 16) },
      { \green }, { midiOutPort.noteOn(16, colorArray[num], 127) },
      { \blue }, { midiOutPort.noteOn(16, colorArray[num], 32) },
      { \yellow }, { midiOutPort.noteOn(16, colorArray[num], 64) },
      { \purple }, { midiOutPort.noteOn(16, colorArray[num], 8) },
      { \cyan }, { midiOutPort.noteOn(16, colorArray[num], 4) },
      { \white }, { midiOutPort.noteOn(16, colorArray[num], 1) },
    );
  }

  turnOff { | num |
    this.turnColor(num, \off);
  }

  turnRed { | num |
    this.turnColor(num, \red);
  }

  turnGreen { | num |
    this.turnColor(num, \green);
  }

  turnBlue { | num |
    this.turnColor(num, \blue);
  }

  turnYellow { | num |
    this.turnColor(num, \yellow);
  }

  turnPurple { | num |
    this.turnColor(num, \purple);
  }

  turnCyan { | num |
    this.turnColor(num, \cyan);
  }

  turnWhite { | num |
    this.turnColor(num, \white);
  }

  turnRandomColor { | num |
    var color;
    color = [\red, \green, \blue, \yellow, \purple, \cyan, \white].choose;
    this.turnColor(num, color);
  }

  //////// Convenience Colors (for paging):

  turnGridColor { | column = 0, row = 0, color = 'off', bank = 'active', page = 'active' |
    var num = (column * 8) + row;
    if( page == 'active', { page = activePageKey; });
    if( bank == 'active', { bank = pageDict[page].activeGridBank });
    pageDict[page].turnGridColor(column, row, color, bank);
    this.turnColor(num, activePage.getColor(num));
  }

  turnGridOff { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'off', bank, page);
  }

  turnGridRed { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'red', bank, page);
  }

  turnGridGreen { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'green', bank, page);
  }

  turnGridBlue { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'blue', bank, page);
  }

  turnGridYellow { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'yellow', bank, page);
  }

  turnGridPurple { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'purple', bank, page);
  }

  turnGridCyan { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'cyan', bank, page);
  }

  turnGridWhite { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'white', bank, page);
  }

  turnAllGridColor { | color = 'off', bank = 'active', page = 'active' |
    8.do({ | column | 8.do({ | row | this.turnGridColor(column, row, color, bank, page); }); });
  }

  turnAllGridOff { |  bank = 'active', page = 'active' |
    8.do({ | column | 8.do({ | row | this.turnGridColor(column, row, 'off', bank, page); }); });
  }

  turnAllGridRed { |  bank = 'active', page = 'active' |
    8.do({ | column | 8.do({ | row | this.turnGridColor(column, row, 'red', bank, page); }); });
  }

  turnAllGridGreen { |  bank = 'active', page = 'active' |
    8.do({ | column | 8.do({ | row | this.turnGridColor(column, row, 'green', bank, page); }); });
  }

  turnAllGridBlue { |  bank = 'active', page = 'active' |
    8.do({ | column | 8.do({ | row | this.turnGridColor(column, row, 'blue', bank, page); }); });
  }

  turnAllGridYellow { |  bank = 'active', page = 'active' |
    8.do({ | column | 8.do({ | row | this.turnGridColor(column, row, 'yellow', bank, page); }); });
  }

  turnAllGridPurple { |  bank = 'active', page = 'active' |
    8.do({ | column | 8.do({ | row | this.turnGridColor(column, row, 'purple', bank, page); }); });
  }

  turnAllGridCyan { |  bank = 'active', page = 'active' |
    8.do({ | column | 8.do({ | row | this.turnGridColor(column, row, 'cyan', bank, page); }); });
  }

  turnAllGridWhite { |  bank = 'active', page = 'active' |
    8.do({ | column | 8.do({ | row | this.turnGridColor(column, row, 'white', bank, page); }); });
  }

  turnLeftButtonColor { | num, color = 'off', bank = 'active', page = 'active' |
    var buttonArray = [65, 73, 66, 74];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeLeftButtonsBank; });
    pageDict[page].turnLeftButtonColor(num, color, bank);
    this.turnColor(buttonArray[num], activePage.getColor(buttonArray[num]));
  }

  turnLeftButtonOff { | num, bank = 'active', page = 'active' |
    this.turnLeftButtonColor(num, 'off', bank, page);
  }

  turnLeftButtonRed { | num, bank = 'active', page = 'active' |
    this.turnLeftButtonColor(num, 'red', bank, page);
  }

  turnLeftButtonGreen { | num, bank = 'active', page = 'active' |
    this.turnLeftButtonColor(num, 'green', bank, page);
  }

  turnLeftButtonBlue { | num, bank = 'active', page = 'active' |
    this.turnLeftButtonColor(num, 'blue', bank, page);
  }

  turnLeftButtonYellow { | num, bank = 'active', page = 'active' |
    this.turnLeftButtonColor(num, 'yellow', bank, page);
  }

  turnLeftButtonPurple { | num, bank = 'active', page = 'active' |
    this.turnLeftButtonColor(num, 'purple', bank, page);
  }

  turnLeftButtonCyan { | num, bank = 'active', page = 'active' |
    this.turnLeftButtonColor(num, 'cyan', bank, page);
  }

  turnLeftButtonWhite { | num, bank = 'active', page = 'active' |
    this.turnLeftButtonColor(num, 'white', bank, page);
  }

  turnRightButtonColor { | num, color = 'off', bank = 'active', page = 'active' |
    var buttonArray = [67, 75, 68, 76];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeRightButtonsBank; });
    pageDict[page].turnRightButtonColor(num, color, bank);
    this.turnColor(buttonArray[num], activePage.getColor(buttonArray[num]));
  }

  turnRightButtonOff { | num, bank = 'active', page = 'active' |
    this.turnRightButtonColor(num, 'off', bank, page);
  }

  turnRightButtonRed { | num, bank = 'active', page = 'active' |
    this.turnRightButtonColor(num, 'red', bank, page);
  }

  turnRightButtonGreen { | num, bank = 'active', page = 'active' |
    this.turnRightButtonColor(num, 'green', bank, page);
  }

  turnRightButtonBlue { | num, bank = 'active', page = 'active' |
    this.turnRightButtonColor(num, 'blue', bank, page);
  }

  turnRightButtonYellow { | num, bank = 'active', page = 'active' |
    this.turnRightButtonColor(num, 'yellow', bank, page);
  }

  turnRightButtonPurple { | num, bank = 'active', page = 'active' |
    this.turnRightButtonColor(num, 'purple', bank, page);
  }

  turnRightButtonCyan { | num, bank = 'active', page = 'active' |
    this.turnRightButtonColor(num, 'cyan', bank, page);
  }

  turnRightButtonWhite { | num, bank = 'active', page = 'active' |
    this.turnRightButtonColor(num, 'white', bank, page);
  }

  turnCrossfaderButtonColor { | num, color = 'off', bank = 'active', page = 'active' |
    var buttonArray = [64, 72];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeCrossfaderButtonsBank; });
    pageDict[page].turnCrossfaderButtonColor(num, color, bank);
    this.turnColor(buttonArray[num], activePage.getColor(buttonArray[num]));
  }

  turnCrossfaderButtonOff { | num = 0, bank = 'active', page = 'active' |
    this.turnCrossfaderButtonColor(num, 'off', bank, page);
  }

  turnCrossfaderButtonRed { | num = 0, bank = 'active', page = 'active' |
    this.turnCrossfaderButtonColor(num, 'red', bank, page);
  }

  turnCrossfaderButtonGreen { | num = 0, bank = 'active', page = 'active' |
    this.turnCrossfaderButtonColor(num, 'green', bank, page);
  }

  turnCrossfaderButtonBlue { | num = 0, bank = 'active', page = 'active' |
    this.turnCrossfaderButtonColor(num, 'blue', bank, page);
  }

  turnCrossfaderButtonYellow { | num = 0, bank = 'active', page = 'active' |
    this.turnCrossfaderButtonColor(num, 'yellow', bank, page);
  }

  turnCrossfaderButtonPurple { | num = 0, bank = 'active', page = 'active' |
    this.turnCrossfaderButtonColor(num, 'purple', bank, page);
  }

  turnCrossfaderButtonCyan { | num = 0, bank = 'active', page = 'active' |
    this.turnCrossfaderButtonColor(num, 'cyan', bank, page);
  }

  turnCrossfaderButtonWhite { | num = 0, bank = 'active', page = 'active' |
    this.turnCrossfaderButtonColor(num, 'white', bank, page);
  }

  turnControlButtonColor { | column = 0, row = 0, color = 'off', bank = 'active', page = 'active' |
    var num = (column * 2) + row;
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    if( page == 'active', { page = activePageKey });
    if( bank == 'active', { bank = pageDict[page].activeControlButtonsBank; });
    pageDict[page].turnControlButtonColor(column, row, color, bank);
    this.turnColor(buttonArray[num], activePage.getColor(buttonArray[num]));
  }

  turnControlButtonOff { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnControlButtonColor(column, row, 'off', bank, page);
  }

  turnControlButtonRed { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnControlButtonColor(column, row, 'red', bank, page);
  }

  turnControlButtonGreen { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnControlButtonColor(column, row, 'green', bank, page);
  }

  turnControlButtonBlue { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnControlButtonColor(column, row, 'blue', bank, page);
  }

  turnControlButtonYellow { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnControlButtonColor(column, row, 'yellow', bank, page);
  }

  turnControlButtonPurple { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnControlButtonColor(column, row, 'purple', bank, page);
  }

  turnControlButtonCyan { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnControlButtonColor(column, row, 'cyan', bank, page);
  }

  turnControlButtonWhite { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnControlButtonColor(column, row, 'white', bank, page);
  }

}