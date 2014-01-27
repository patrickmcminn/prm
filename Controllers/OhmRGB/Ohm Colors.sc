+ OhmRGB {

  //////// Color Functions:

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

  turnGridColor { | column = 0, row = 0, color = \off |
    var num = (column * 8) + row;
    this.turnColor(num, color);
  }

  turnGridOff { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \off);
  }

  turnGridRed { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \red);
  }

  turnGridGreen { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \green);
  }

  turnGridBlue { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \blue);
  }

  turnGridYellow { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \yellow);
  }

  turnGridPurple { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \purple);
  }

  turnGridCyan { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \cyan);
  }

  turnGridWhite { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \white);
  }

  turnAllGrid { | color = \off |
   switch(color,
      { \off }, { 64.do({ | num | this.turnColor(num, \off)}); },
      { \red }, { 64.do({ | num | this.turnColor(num, \red)}); },
      { \green }, { 64.do({ | num | this.turnColor(num, \green)}); },
      { \blue }, { 64.do({ | num | this.turnColor(num, \blue)}); },
      { \yellow }, { 64.do({ | num | this.turnColor(num, \yellow)}); },
      { \purple }, { 64.do({ | num | this.turnColor(num, \purple)}); },
      { \cyan }, { 64.do({ | num | this.turnColor(num, \cyan)}); },
      { \white }, { 64.do({ | num | this.turnColor(num, \white)}); }
    );
  }

  turnGridRandom {
    var color = [\red, \green, \blue, \yellow, \purple, \cyan, \white].choose;
    this.turnGrid(color);
  }

  turnLeftButtonColor { | num = 0, color = \off |
    var buttonArray = [65, 73, 66, 74];
    this.turnColor(buttonArray[num], color);
  }

  turnLeftButtonOff { | num |
    this.turnLeftButtonColor(num, \off);
  }

  turnLeftButtonRed { | num |
    this.turnLeftButtonColor(num, \red);
  }

  turnLeftButtonGreen { | num |
    this.turnLeftButtonColor(num, \green);
  }

  turnLeftButtonBlue { | num |
    this.turnLeftButtonColor(num, \blue);
  }

  turnLeftButtonYellow { | num |
    this.turnLeftButtonColor(num, \yellow);
  }

  turnLeftButtonPurple { | num |
    this.turnLeftButtonColor(num, \purple);
  }

  turnLeftButtonCyan { | num |
    this.turnLeftButtonColor(num, \cyan);
  }

  turnLeftButtonWhite { | num |
    this.turnLeftButtonColor(num, \white);
  }

  turnAllLeftButtonsColor { | color = \off |
    var buttonArray = [65, 73, 66, 74];
    buttonArray.do({ | num | this.turnColor(num, color); });
  }

  turnAllLeftButtonsOff {
    this.turnAllLeftButtonsColor(\off);
  }

  turnAllLeftButtonsRed {
    this.turnAllLeftButtonsColor(\red);
  }

  turnAllLeftButtonsGreen {
    this.turnAllLeftButtonsColor(\green);
  }

  turnAllLeftButtonsBlue {
    this.turnAllLeftButtonsColor(\blue);
  }

  turnAllLeftButtonsYellow {
    this.turnAllLeftButtonsColor(\yellow);
  }

  turnAllLeftButtonsPurple {
    this.turnAllLeftButtonsColor(\purple);
  }

  turnAllLeftButtonsCyan {
    this.turnAllLeftButtonsColor(\cyan);
  }

  turnAllLeftButtonsWhite {
    this.turnAllLeftButtonsColor(\white);
  }

  turnRightButtonColor { | num = 0, color = \off |
    var buttonArray = [67, 75, 68, 76];
    this.turnColor(buttonArray[num], color);
  }

  turnRightButtonOff { | num |
    this.turnRightButtonColor(num, \off);
  }

  turnRightButtonRed { | num |
    this.turnRightButtonColor(num, \red);
  }

  turnRightButtonGreen { | num |
    this.turnRightButtonColor(num, \green);
  }

  turnRightButtonBlue { | num |
    this.turnRightButtonColor(num, \blue);
  }

  turnRightButtonYellow { | num |
    this.turnRightButtonColor(num, \yellow);
  }

  turnRightButtonPurple { | num |
    this.turnRightButtonColor(num, \purple);
  }

  turnRightButtonCyan { | num |
    this.turnRightButtonColor(num, \cyan);
  }

  turnRightButtonWhite { | num |
    this.turnRightButtonColor(num, \white);
  }

  turnAllRightButtonsColor { | color = \off |
    var buttonArray = [67, 75, 68, 76];
    buttonArray.do({ | num | this.turnColor(num, color); });
  }

  turnAllRightButtonsOff {
    this.turnAllRightButtonsColor(\off);
  }

  turnAllRightButtonsRed {
    this.turnAllRightButtonsColor(\red);
  }

  turnAllRightButtonsGreen {
    this.turnAllRightButtonsColor(\green);
  }

  turnAllRightButtonsBlue {
    this.turnAllRightButtonsColor(\blue);
  }

  turnAllRightButtonsYellow {
    this.turnAllRightButtonsColor(\yellow);
  }

  turnAllRightButtonsPurple {
    this.turnAllRightButtonsColor(\purple);
  }

  turnAllRightButtonsCyan {
    this.turnAllRightButtonsColor(\cyan);
  }

  turnAllRightButtonsWhite {
    this.turnAllRightButtonsColor(\white);
  }

  turnCrossfaderButton { | num = 0, color = \off |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], color);
  }

  turnCrossfaderButtonOff { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \off);
  }

  turnCrossfaderButtonRed { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \red);
  }

  turnCrossfaderButtonGreen { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \green);
  }

  turnCrossfaderButtonBlue { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \blue);
  }

  turnCrossfaderButtonYellow { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \yellow);
  }

  turnCrossfaderButtonPurple { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \purple);
  }

  turnCrossfaderButtonCyan { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \cyan);
  }

  turnCrossfaderButtonWhite { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \white);
  }

  turnAllCrossfaderButtonsColor { | color = \off |
    var buttonArray = [64, 72];
    buttonArray.do({ | index | this.turnColor(index, color); });
  }

  turnAllCrossfaderButtonsOff {
    this.turnAllCrossfaderButtonsColor(\off);
  }

  turnAllCrossfaderButtonsRed {
    this.turnAllCrossfaderButtonsColor(\red);
  }

  turnAllCrossfaderButtonsGreen {
    this.turnAllCrossfaderButtonsColor(\green);
  }

  turnAllCrossfaderButtonsBlue {
    this.turnAllCrossfaderButtonsColor(\blue);
  }

  turnAllCrossfaderButtonsYellow {
    this.turnAllCrossfaderButtonsColor(\yellow);
  }

  turnAllCrossfaderButtonsPurple {
    this.turnAllCrossfaderButtonsColor(\purple);
  }

  turnAllCrossfaderButtonsCyan {
    this.turnAllCrossfaderButtonsColor(\cyan);
  }

  turnAllCrossfaderButtonsWhite {
    this.turnAllCrossfaderButtonsColor(\white);
  }

  turnControlButtonColor { | num = 0, color = \off |
    var buttonArray = [69, 77, 70, 78, 71, 79];
    this.turnColor(buttonArray[num], color);
  }

  turnControlButtonOff { | num |
    this.turnControlButtonColor(num, \off);
  }

  turnControlButtonRed { | num |
    this.turnControlButtonColor(num, \red);
  }

  turnControlButtonGreen { | num |
    this.turnControlButtonColor(num, \green);
  }

  turnControlButtonBlue { | num |
    this.turnControlButtonColor(num, \blue);
  }

  turnControlButtonYellow { | num |
    this.turnControlButtonColor(num, \yellow);
  }

  turnControlButtonPurple { | num |
    this.turnControlButtonColor(num, \purple);
  }

  turnControlButtonCyan { | num |
    this.turnControlButtonColor(num, \cyan);
  }

  turnControlButtonWhite { | num |
    this.turnControlButtonColor(num, \white);
  }

  turnAllControlButtonsColor { | color = \off |
    var buttonArray = [69, 77, 70, 78, 71, 79];
    buttonArray.do({ | index | this.turnColor(index, color) });
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

  turnAllOff {
    // this.turnAllGridOff;
    this.turnAllGrid(\off);
    this.turnAllLeftButtonsOff;
    this.turnAllRightButtonsOff;
    this.turnAllCrossfaderButtonsOff;
    this.turnAllControlButtonsOff;
    this.turnMasterButtonOff;
  }

}