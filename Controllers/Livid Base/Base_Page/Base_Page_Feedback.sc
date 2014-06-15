+ Base_Page {

  // color functions:

  getButtonColor { | num = 0 | ^buttonColorArray[num]; }
  turnButtonColor { | num = 0, color = 'off' | buttonColorArray[num] = color }

  // functions for use with Banks:

  turnGridColor { | column = 0, row = 0, color = 'off', bank = 'active' |
    var num = (row * 8) + column;
    if( bank == 'active', { bank = activeGridBank });
    gridBankArray[bank][num][3] = color;
    this.turnButtonColor(num + 36, gridBankArray[activeGridBank][num][3]);
  }

  turnControlButtonColor { | button = 1, led = 'left', color = 'off', bank = 'active' |
    var num = (button - 1) + switch(led, { 'left' }, { 0 }, { 'right' }, { 8 });
    if( bank == 'active', { bank = activeControlButtonsBank });
    controlButtonsBankArray[bank][2][num] = color;
    this.turnButtonColor(num + 18, controlButtonsBankArray[activeControlButtonsBank][2][num]);
  }

  turnTouchButtonColor { | button = 0, led = 'middle', color = 'off', bank = 'active' |
    var num = button + switch(led, { 'middle' }, { 0 }, { 'top' }, { 8 });
    var midi = button + switch(led, { 'middle' }, { 10 }, { 'top' }, { 68 });
    if( bank == 'active', { bank = activeTouchButtonsBank });
    touchButtonsBankArray[bank][2][num] = color;
    this.turnButtonColor(midi, touchButtonsBankArray[activeTouchButtonsBank][2][num]);
  }

  setFaderMode { | fader = 0, mode = 'redFill', bank = 'active' |
    if( bank == 'active', { bank = activeFadersBank });
    fadersBankArray[bank][fader][2] = mode;
    faderModeArray[fader] = fadersBankArray[activeFadersBank][fader][1];
  }

  setFaderValue { | fader = 0, value = 0, bank = 'active' |
    if( bank == 'active', { bank = activeFadersBank });
    fadersBankArray[bank][fader][1] = value;
    faderValueArray[fader] = fadersBankArray[activeFadersBank][fader][1];
  }

}

// convenience functions:

+ Base_Page {

  turnGridOff { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, 'off', bank);
  }
  turnGridRed { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, 'red', bank);
  }
  turnGridGreen { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, 'green', bank);
  }
  turnGridBlue { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, 'blue', bank);
  }
  turnGridYellow { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, 'yellow', bank);
  }
  turnGridMagenta { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, 'magenta', bank);
  }
  turnGridCyan { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, 'cyan', bank);
  }
  turnGridWhite { | column = 0, row = 0, bank = 'active' |
    this.turnGridColor(column, row, 'white', bank);
  }

  turnAllGridColor { | color = 'off', bank = 'active' |
    8.do({ | column |
      4.do({ | row |
        this.turnGridColor(column, row, color, bank);
      });
    });
  }
  turnAllGridOff { | bank = 'active' |
    this.turnAllGridColor('off', bank);
  }
  turnAllGridRed { | bank = 'active' |
    this.turnAllGridColor('red', bank);
  }
  turnAllGridGreen { | bank = 'active' |
    this.turnAllGridColor('green', bank);
  }
  turnAllGridBlue { | bank = 'active' |
    this.turnAllGridColor('blue', bank);
  }
  turnAllGridYellow { | bank = 'active' |
    this.turnAllGridColor('yellow', bank);
  }
  turnAllGridMagenta { | bank = 'active' |
    this.turnAllGridColor('magenta', bank);
  }
  turnAllGridCyan { | bank = 'active' |
    this.turnAllGridColor('cyan', bank);
  }
  turnAllGridWhite { | bank = 'active' |
    this.turnAllGridColor('white', bank);
  }

  turnControlButtonOff { | button = 0, led = 'left', bank = 'active' |
    this.turnControlButtonColor(button, led, 'off', bank);
  }
  turnControlButtonRed { | button = 0, led = 'left', bank = 'active' |
    this.turnControlButtonColor(button, led, 'red', bank);
  }
  turnControlButtonGreen { | button = 0, led = 'left', bank = 'active' |
    this.turnControlButtonColor(button, led, 'green', bank);
  }
  turnControlButtonBlue { | button = 0, led = 'left', bank = 'active' |
    this.turnControlButtonColor(button, led, 'blue', bank);
  }
  turnControlButtonYellow { | button = 0, led = 'left', bank = 'active' |
    this.turnControlButtonColor(button, led, 'yellow', bank);
  }
  turnControlButtonMagenta { | button = 0, led = 'left', bank = 'active' |
    this.turnControlButtonColor(button, led, 'magenta', bank);
  }
  turnControlButtonCyan { | button = 0, led = 'left', bank = 'active' |
    this.turnControlButtonColor(button, led, 'cyan', bank);
  }
  turnControlButtonWhite { | button = 0, led = 'left', bank = 'active' |
    this.turnControlButtonColor(button, led, 'white', bank);
  }

  turnAllControlButtonsColor { | color = 'off', bank = 'active' |
    8.do({ | index | this.turnControlButtonColor(index, 'left', color, 'bank'); });
    8.do({ | index | this.turnControlButtonColor(index, 'right', color, 'bank'); });
  }
  turnAllControlButtonsOff { | bank = 'active' |
    this.turnAllControlButtonsColor('off', bank);
  }
  turnAllControlButtonsRed { | bank = 'active' |
    this.turnAllControlButtonsColor('red', bank);
  }
  turnAllControlButtonsGreen { | bank = 'active' |
    this.turnAllControlButtonsColor('green', bank);
  }
  turnAllControlButtonsBlue { | bank = 'active' |
    this.turnAllControlButtonsColor('blue', bank);
  }
  turnAllControlButtonsYellow { | bank = 'active' |
    this.turnAllControlButtonsColor('yellow', bank);
  }
  turnAllControlButtonsMagenta { | bank = 'active' |
    this.turnAllControlButtonsColor('magenta', bank);
  }
  turnAllControlButtonsCyan { | bank = 'active' |
    this.turnAllControlButtonsColor('cyan', bank);
  }
  turnAllControlButtonsWhite { | bank = 'active' |
    this.turnAllControlButtonsColor('white', bank);
  }

  turnTouchButtonOff { | button = 0, led = 'middle', bank = 'active' |
    this.turnTouchButtonColor(button, led, 'off', bank);
  }
  turnTouchButtonRed { | button = 0, led = 'middle', bank = 'active' |
    this.turnTouchButtonColor(button, led, 'red', bank);
  }
  turnTouchButtonGreen { | button = 0, led = 'middle', bank = 'active' |
    this.turnTouchButtonColor(button, led, 'green', bank);
  }
  turnTouchButtonBlue { | button = 0, led = 'middle', bank = 'active' |
    this.turnTouchButtonColor(button, led, 'blue', bank);
  }
  turnTouchButtonYellow { | button = 0, led = 'middle', bank = 'active' |
    this.turnTouchButtonColor(button, led, 'yellow', bank);
  }
  turnTouchButtonMagenta { | button = 0, led = 'middle', bank = 'active' |
    this.turnTouchButtonColor(button, led, 'magenta', bank);
  }
  turnTouchButtonCyan { | button = 0, led = 'middle', bank = 'active' |
    this.turnTouchButtonColor(button, led, 'cyan', bank);
  }
  turnTouchButtonWhite { | button = 0, led = 'middle', bank = 'active' |
    this.turnTouchButtonColor(button, led, 'white', bank);
  }

  turnAllTouchButtonsColor { | color = 'off', bank = 'active' |
    8.do({ | index | this.turnTouchButtonColor(index, 'middle', color, bank); });
    8.do({ | index | this.turnTouchButtonColor(index, 'top', color, bank); });
  }
  turnAllTouchButtonsOff { | bank = 'active' |
    this.turnAllTouchButtonsColor('off', bank);
  }
  turnAllTouchButtonsRed { | bank = 'active' |
    this.turnAllTouchButtonsColor('red', bank);
  }
  turnAllTouchButtonsGreen { | bank = 'active' |
    this.turnAllTouchButtonsColor('green', bank);
  }
  turnAllTouchButtonsBlue { | bank = 'active' |
    this.turnAllTouchButtonsColor('blue', bank);
  }
  turnAllTouchButtonsYellow { | bank = 'active' |
    this.turnAllTouchButtonsColor('yellow', bank);
  }
  turnAllTouchButtonsMagenta { | bank = 'active' |
    this.turnAllTouchButtonsColor('magenta', bank);
  }
  turnAllTouchButtonsCyan { | bank = 'active' |
    this.turnAllTouchButtonsColor('cyan', bank);
  }
  turnAllTouchButtonsWhite { | bank = 'active' |
    this.turnAllTouchButtonsColor('white', bank);
  }

  setMasterFaderMode { | mode = 'redFill', bank = 'active' |
    this.setFaderMode(8, mode, bank);
  }
  setAllFaderModes { | mode = 'redFill', bank = 'active' |
    9.do({ | index | this.setFaderMode(index, mode, bank); });
  }


}