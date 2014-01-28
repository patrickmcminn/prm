+ OhmRGB_Page {

  //////// Color Functions:

  /*
  turnColor { | num, color = \off |
    switch(color,
      { \off }, { midiOutPort.noteOn(16, num, 0) },
      { \red }, { midiOutPort.noteOn(16, num, 16) },
      { \green }, { midiOutPort.noteOn(16, num, 127) },
      { \blue }, { midiOutPort.noteOn(16, num, 32) },
      { \yellow }, { midiOutPort.noteOn(16, num, 64) },
      { \purple }, { midiOutPort.noteOn(16, num, 8) },
      { \cyan }, { midiOutPort.noteOn(16, num, 4) },
      { \white }, { midiOutPort.noteOn(16, num, 1) },
    );
  }

  turnOff { | num |
    this.turnOff(num, \off);
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

  */

  getColor { | num = 0 |
    ^colorArray[num];
  }

  turnColor { | num = 0, color = 'off' |
    colorArray[num] = color;
  }

  getColor { | num = 0 |
    ^colorArray[num];
  }

  turnGridColor { | column = 0, row = 0, color = \off, bank = 'active' |
    var num = (column * 8) + row;
    //var bankSet;
    if( bank == 'active', { bank = activeGridBank });
    gridBankArray[bank][num][2] = color;
    this.turnColor(num, gridBankArray[activeGridBank][num][2]);
    /*
    if ( bank == activeGridBank, { bank = 'active' });
    if( bank == 'active',
      {
        bankSet = activeGridBank;
        gridBankArray[bankSet][num][2] = color;
        this.turnColor(num, color);
      },
      {
        bankSet = bank;
        gridBankArray[bankSet][num][2] = color;
      }
    );
  */
  }

  turnGridOff { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, \off, bank);
  }

  turnGridRed { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, \red, bank);
  }

  turnGridGreen { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, \green, bank);
  }

  turnGridBlue { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, \blue, bank);
  }

  turnGridYellow { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, \yellow, bank);
  }

  turnGridPurple { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, \purple, bank);
  }

  turnGridCyan { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, \cyan, bank);
  }

  turnGridWhite { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, \white, bank);
  }

  turnAllGrid { | color = \off, bank = 'active' |
    8.do({ | column |
      8.do({ | row |
        this.turnGridColor(column, row, color, bank);
      });
    });
  }

  turnAllGridOff { | bank = 'active' |
    this.turnAllGrid('off', bank);
  }

  turnAllGridRed { | bank = 'active' |
    this.turnAllGrid('red', bank);
  }

  turnAllGridGreen { | bank = 'active' |
    this.turnAllGrid('green', bank);
  }

  turnAllGridBlue { | bank = 'active' |
    this.turnAllGrid('blue', bank);
  }

  turnAllGridYellow { | bank = 'active' |
    this.turnAllGrid('yellow', bank);
  }

  turnAllGridPurple { | bank = 'active' |
    this.turnAllGrid('purple', bank);
  }

  turnAllGridCyan { | bank = 'active' |
    this.turnAllGrid('cyan', bank);
  }

  turnAllGridWhite { | bank = 'active' |
    this.turnAllGrid('white', bank);
  }

  turnAllGridRandom { | bank = 'active' |
    var color = [\red, \green, \blue, \yellow, \purple, \cyan, \white].choose;
    this.turnAllGrid(color, bank);
  }

  turnLeftButtonColor { | num = 0, color = \off, bank = 'active' |
    //var bankSet;
    var buttonArray = [65, 73, 66, 74];
    if( bank == 'active', { bank = activeLeftButtonsBank });
    leftButtonsBankArray[bank][num][2] = color;
    this.turnColor(buttonArray[num], leftButtonsBankArray[activeLeftButtonsBank][num][2]);
    /*
    if( bank == activeLeftButtonsBank, { bank = 'active' });
    if( bank == 'active',
      { bankSet = activeLeftButtonsBank; this.turnColor(buttonArray[num], color); },
      { bankSet = bank }
    );
    leftButtonsBankArray[bankSet][num][2] = color;
    */
  }

  turnLeftButtonOff { | num, bank = 'active' |
    this.turnLeftButtonColor(num, \off, bank);
  }

  turnLeftButtonRed { | num, bank = 'active' |
    this.turnLeftButtonColor(num, \red, bank);
  }

  turnLeftButtonGreen { | num, bank = 'active' |
    this.turnLeftButtonColor(num, \green, bank);
  }

  turnLeftButtonBlue { | num, bank = 'active' |
    this.turnLeftButtonColor(num, \blue, bank);
  }

  turnLeftButtonYellow { | num, bank = 'active' |
    this.turnLeftButtonColor(num, \yellow, bank);
  }

  turnLeftButtonPurple { | num, bank = 'active' |
    this.turnLeftButtonColor(num, \purple, bank);
  }

  turnLeftButtonCyan { | num, bank = 'active' |
    this.turnLeftButtonColor(num, \cyan, bank);
  }

  turnLeftButtonWhite { | num, bank = 'active' |
    this.turnLeftButtonColor(num, \white, bank);
  }

  turnAllLeftButtonsColor { | color = \off, bank = 'active' |
    4.do({ | num | this.turnLeftButtonColor(num, color, bank); });
  }

  turnAllLeftButtonsOff { | bank = 'active' |
    this.turnAllLeftButtonsColor(\off, bank);
  }

  turnAllLeftButtonsRed { | bank = 'active' |
    this.turnAllLeftButtonsColor(\red, bank);
  }

  turnAllLeftButtonsGreen { | bank = 'active' |
    this.turnAllLeftButtonsColor(\green, bank);
  }

  turnAllLeftButtonsBlue { | bank = 'active' |
    this.turnAllLeftButtonsColor(\blue, bank);
  }

  turnAllLeftButtonsYellow { | bank = 'active' |
    this.turnAllLeftButtonsColor(\yellow, bank);
  }

  turnAllLeftButtonsPurple { | bank = 'active' |
    this.turnAllLeftButtonsColor(\purple, bank);
  }

  turnAllLeftButtonsCyan { | bank = 'active' |
    this.turnAllLeftButtonsColor(\cyan, bank);
  }

  turnAllLeftButtonsWhite { | bank = 'active' |
    this.turnAllLeftButtonsColor(\white, bank);
  }

  turnRightButtonColor { | num = 0, color = \off, bank = 'active' |
    //var bankSet;
    var buttonArray = [67, 75, 68, 76];
    if( bank == 'active', { bank = activeRightButtonsBank });
    rightButtonsBankArray[bank][num][2] = color;
    this.turnColor(buttonArray[num], rightButtonsBankArray[activeRightButtonsBank][num][2]);
    /*
    if( bank == activeRightButtonsBank, { bank = 'active' });
    if( bank == 'active',
      { bankSet = activeRightButtonsBank; this.turnColor(buttonArray[num], color); },
      { bankSet = bank }
    );
    rightButtonsBankArray[bankSet][num][2] = color;
    */
  }

  turnRightButtonOff { | num = 0, bank = 'active' |
    this.turnRightButtonColor(num, \off, bank);
  }

  turnRightButtonRed { | num = 0, bank = 'active' |
    this.turnRightButtonColor(num, \red, bank);
  }

  turnRightButtonGreen { | num = 0, bank = 'active' |
    this.turnRightButtonColor(num, \green, bank);
  }

  turnRightButtonBlue { | num = 0, bank = 'active' |
    this.turnRightButtonColor(num, \blue, bank);
  }

  turnRightButtonYellow { | num = 0, bank = 'active' |
    this.turnRightButtonColor(num, \yellow, bank);
  }

  turnRightButtonPurple { | num = 0, bank = 'active' |
    this.turnRightButtonColor(num, \purple, bank);
  }

  turnRightButtonCyan { | num = 0, bank = 'active' |
    this.turnRightButtonColor(num, \cyan, bank);
  }

  turnRightButtonWhite { | num = 0, bank = 'active' |
    this.turnRightButtonColor(num, \white, bank);
  }

  turnAllRightButtonsColor { | color = \off, bank = 'active' |
   4.do({ | num | this.turnRightButtonColor(num, color, bank); });
  }

  turnAllRightButtonsOff { | bank = 'active' |
    this.turnAllRightButtonsColor(\off, bank);
  }

  turnAllRightButtonsRed { | bank = 'active' |
    this.turnAllRightButtonsColor(\red, bank);
  }

  turnAllRightButtonsGreen { | bank = 'active' |
    this.turnAllRightButtonsColor(\green, bank);
  }

  turnAllRightButtonsBlue { | bank = 'active' |
    this.turnAllRightButtonsColor(\blue, bank);
  }

  turnAllRightButtonsYellow { | bank = 'active' |
    this.turnAllRightButtonsColor(\yellow, bank);
  }

  turnAllRightButtonsPurple { | bank = 'active' |
    this.turnAllRightButtonsColor(\purple, bank);
  }

  turnAllRightButtonsCyan { | bank = 'active' |
    this.turnAllRightButtonsColor(\cyan, bank);
  }

  turnAllRightButtonsWhite { | bank = 'active' |
    this.turnAllRightButtonsColor(\white, bank);
  }

  turnCrossfaderButtonColor { | num = 0, color = \off, bank = 'active' |
    //var bankSet;
    var buttonArray = [64, 72];
    if( bank == 'active', { bank = activeCrossfaderButtonsBank; });
    crossfaderButtonsBankArray[bank][num][2] = color;
    this.turnColor(buttonArray[num], crossfaderButtonsBankArray[activeCrossfaderButtonsBank][num][2]);
    /*
    if( bank == activeCrossfaderButtonsBank, { bank = 'active' });
    if( bank == 'active',
      { bankSet = activeCrossfaderButtonsBank; this.turnColor(buttonArray[num], color); },
      { bankSet = bank; }
    );
    crossfaderButtonsBankArray[bankSet][num][2] = color;
    */
  }

  turnCrossfaderButtonOff { | num, bank = 'active' |
    this.turnCrossfaderButtonColor(num, 'off', bank);
  }

  turnCrossfaderButtonRed { | num, bank = 'active' |
    this.turnCrossfaderButtonColor(num, 'red', bank);
  }

  turnCrossfaderButtonGreen { | num, bank = 'active' |
    this.turnCrossfaderButtonColor(num, 'green', bank);
  }

  turnCrossfaderButtonBlue { | num, bank = 'active' |
    this.turnCrossfaderButtonColor(num, 'blue', bank);
  }

  turnCrossfaderButtonYellow { | num, bank = 'active' |
    this.turnCrossfaderButtonColor(num, 'yellow', bank);
  }

  turnCrossfaderButtonPurple { | num, bank = 'active' |
    this.turnCrossfaderButtonColor(num, 'purple', bank);
  }

  turnCrossfaderButtonCyan { | num, bank = 'active' |
    this.turnCrossfaderButtonColor(num, 'cyan', bank);
  }

  turnCrossfaderButtonWhite { | num, bank = 'active' |
    this.turnCrossfaderButtonColor(num, 'white', bank);
  }

  turnAllCrossfaderButtonsColor { | color = \off, bank = 'active' |
    2.do({ | num | this.turnCrossfaderButtonColor(num, color, bank); });
  }

  turnAllCrossfaderButtonsOff { | bank = 'active' |
    this.turnAllCrossfaderButtonsColor(\off, bank);
  }

  turnAllCrossfaderButtonsRed { | bank = 'active' |
    this.turnAllCrossfaderButtonsColor(\red, bank);
  }

  turnAllCrossfaderButtonsGreen { | bank = 'active' |
    this.turnAllCrossfaderButtonsColor(\green, bank);
  }

  turnAllCrossfaderButtonsBlue { | bank = 'active' |
    this.turnAllCrossfaderButtonsColor(\blue, bank);
  }

  turnAllCrossfaderButtonsYellow { | bank = 'active' |
    this.turnAllCrossfaderButtonsColor(\yellow, bank);
  }

  turnAllCrossfaderButtonsPurple { | bank = 'active' |
    this.turnAllCrossfaderButtonsColor(\purple, bank);
  }

  turnAllCrossfaderButtonsCyan { | bank = 'active' |
    this.turnAllCrossfaderButtonsColor(\cyan, bank);
  }

  turnAllCrossfaderButtonsWhite { | bank = 'active' |
    this.turnAllCrossfaderButtonsColor(\white, bank);
  }

  turnControlButtonColor { | column = 0, row = 0, color = \off, bank = 'active' |
    //var bankSet;
    var num = (column * 2) + row;
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    if( bank == 'active', { bank = activeControlButtonsBank });
    controlButtonsBankArray[bank][num][2] = color;
    this.turnColor(buttonArray[num], controlButtonsBankArray[activeControlButtonsBank][2]);
    /*
    if( bank == activeControlButtonsBank, { bank = 'active' });
    if( bank == 'active',
      { bankSet = activeControlButtonsBank; this.turnColor(buttonArray[num], color); },
      { bankSet = bank; }
    );
    controlButtonsBankArray[bankSet][num][2] = color;
    */
  }

  turnControlButtonOff { | column = 0, row = 0, bank = 'active' |
    this.turnControlButtonColor(row, column, \off, bank);
  }

  turnControlButtonRed { | column = 0, row = 0, bank = 'active' |
    this.turnControlButtonColor(row, column, \red, bank);
  }

  turnControlButtonGreen { | column = 0, row = 0, bank = 'active' |
    this.turnControlButtonColor(row, column, \green, bank);
  }

  turnControlButtonBlue { | column = 0, row = 0, bank = 'active' |
    this.turnControlButtonColor(row, column, \blue, bank);
  }

  turnControlButtonYellow { | column = 0, row = 0, bank = 'active' |
    this.turnControlButtonColor(row, column, \yellow, bank);
  }

  turnControlButtonPurple { | column = 0, row = 0, bank = 'active' |
    this.turnControlButtonColor(row, column, \purple, bank);
  }

  turnControlButtonCyan { | column = 0, row = 0, bank = 'active' |
    this.turnControlButtonColor(row, column, \cyan, bank);
  }

  turnControlButtonWhite { | column = 0, row = 0, bank = 'active' |
    this.turnControlButtonColor(row, column, \white, bank);
  }

  /*
  turnAllControlButtonsColor { | color = \off, bank = 'active' |
  }

  turnAllControlButtonsOff {
    this.turnAllControlButtonsColor(\off);
  }

  turnAllControlButtonsRed {
    this.turnAllControlButtonsColor(\red);
  }

  turnAllControlButtonsGreen {
    this.turnAllControlButtonsColor(\green);
  }

  turnAllControlButtonsBlue {
    this.turnAllControlButtonsColor(\blue);
  }

  turnAllControlButtonsYellow {
    this.turnAllControlButtonsColor(\yellow);
  }

  turnAllControlButtonsPurple {
    this.turnAllControlButtonsColor(\purple);
  }

  turnAllControlButtonsCyan {
    this.turnAllControlButtonsColor(\cyan);
  }

  turnAllControlButtonsWhite {
    this.turnAllControlButtonsColor(\white);
  }

  turnMasterButtonColor { | color = \off |
    this.turnColor(87);
  }

  turnMasterButtonOff {
    this.turnMasterButtonColor(\off);
  }

  turnMasterButtonRed {
    this.turnMasterButtonColor(\red);
  }

  turnMasterButtonGreen {
    this.turnMasterButtonColor(\green);
  }

  turnMasterButtonBlue {
    this.turnMasterButtonColor(\blue);
  }

  turnMasterButtonYellow {
    this.turnMasterButtonColor(\yellow);
  }

  turnMasterButtonPurple {
    this.turnMasterButtonColor(\purple);
  }

  turnMasterButtonCyan {
    this.turnMasterButtonColor(\cyan);
  }

  turnMasterButtonWhite {
    this.turnMasterButtonColor(\white);
  }
  */

  turnAllOff { | bank = 'active' |
    // this.turnAllGridOff;
    this.turnAllGrid(\off, bank);
    this.turnAllLeftButtonsOff(bank);
    this.turnAllRightButtonsOff(bank);
    this.turnAllCrossfaderButtonsOff(bank);
    //this.turnAllControlButtonsOff(bank);
    //this.turnMasterButtonOff;
  }

}