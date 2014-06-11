+ Base_Page {

  // color functions:

  getColor { | num = 0 | ^colorArray[num]; }
  turnColor { | num = 0, color = 'off' | colorArray[num] = color }

  // functions for use with SubBanks:

  turnGridColor { | column = 0, row = 0, color = 'off', subBank = 'active' |
    var num = ((column * 8) + row);
    if( subBank == 'active', { subBank = activeGridSubBank });
    gridSubBankArray[subBank][num][2] = color;
    this.turnColor(num + 36, gridSubBankArray[activeGridSubBank][num][2]);
  }

  turnControlButtonColor { | button = 0, led = 'left', color = 'off', subBank = 'active' |
    var num = button + switch(led, { 'left' }, { 0 }, { 'right' }, { 8 });
    if( subBank == 'active', { subBank = activeControlButtonsSubBank });
    controlButtonsSubBankArray[subBank][num][2] = color;
    this.turnColor(num + 18, controlButtonsSubBankArray[activeControlButtonsSubBank][num][2]);
  }

  turnTouchButtonColor { | button = 0, led = 'middle', color = 'off', subBank = 'active' |
    var num = button + switch(led, { 'middle' }, { 0 }, { 'top' }, { 8 });
    var midi = button + switch(led, { 'middle' }, { 10 }, { 'top' }, { 68 });
    if( subBank = 'active', { subBank = activeTouchButtonsSubBank });
    touchButtonsSubBankArray[subBank][num][2] = color;
    this.turnColor(midi, touchButtonsSubBankArray[activeTouchButtonsSubBank][num][2]);
  }

  setFaderMode { | fader = 0, mode = 'redFill', subBank = 'active' |
    if( subBank = 'active', { subBank = activeFadersSubBank });
    fadersSubBankArray[subBank][fader][1] = mode;
    faderModeArray[fader] = fadersSubBankArray[activeFadersSubBank][fader][1];
  }

}

// convenience functions:

+ Base_Page {

  turnGridOff { | column = 0, row = 0, subBank = 'active' |
    this.turnGridColor(column, row, 'off', subBank);
  }
  turnGridRed { | column = 0, row = 0, subBank = 'active' |
    this.turnGridColor(column, row, 'red', subBank);
  }
  turnGridGreen { | column = 0, row = 0, subBank = 'active' |
    this.turnGridColor(column, row, 'green', subBank);
  }
  turnGridBlue { | column = 0, row = 0, subBank = 'active' |
    this.turnGridColor(column, row, 'blue', subBank);
  }
  turnGridYellow { | column = 0, row = 0, subBank = 'active' |
    this.turnGridColor(column, row, 'yellow', subBank);
  }
  turnGridMagenta { | column = 0, row = 0, subBank = 'active' |
    this.turnGridColor(column, row, 'magenta', subBank);
  }
  turnGridCyan { | column = 0, row = 0, subBank = 'active' |
    this.turnGridColor(column, row, 'cyan', subBank);
  }
  turnGridWhite { | column = 0, row = 0, subBank = 'active' |
    this.turnGridColor(column, row, 'white', subBank);
  }

  turnAllGridColor { | color = 'off', subBank = 'active' |
    8.do({ | column |
      4.do({ | row |
        this.turnGridColor(column, row, color, subBank);
      });
    });
  }
  turnAllGridOff { | subBank = 'active' |
    this.turnAllGridColor('off', subBank);
  }
  turnAllGridRed { | subBank = 'active' |
    this.turnAllGridColor('red', subBank);
  }
  turnAllGridGreen { | subBank = 'active' |
    this.turnAllGridColor('green', subBank);
  }
  turnAllGridBlue { | subBank = 'active' |
    this.turnAllGridColor('blue', subBank);
  }
  turnAllGridYellow { | subBank = 'active' |
    this.turnAllGridColor('yellow', subBank);
  }
  turnAllGridMagenta { | subBank = 'active' |
    this.turnAllGridColor('magenta', subBank);
  }
  turnAllGridCyan { | subBank = 'active' |
    this.turnAllGridColor('cyan', subBank);
  }
  turnAllGridWhite { | subBank = 'active' |
    this.turnAllGridColor('white', subBank);
  }

  turnControlButtonOff { | button = 0, led = 'left', subBank = 'active' |
    this.turnControlButtonColor(button, led, 'off', subBank);
  }
  turnControlButtonRed { | button = 0, led = 'left', subBank = 'active' |
    this.turnControlButtonColor(button, led, 'red', subBank);
  }
  turnControlButtonGreen { | button = 0, led = 'left', subBank = 'active' |
    this.turnControlButtonColor(button, led, 'green', subBank);
  }
  turnControlButtonBlue { | button = 0, led = 'left', subBank = 'active' |
    this.turnControlButtonColor(button, led, 'blue', subBank);
  }
  turnControlButtonYellow { | button = 0, led = 'left', subBank = 'active' |
    this.turnControlButtonColor(button, led, 'yellow', subBank);
  }
  turnControlButtonMagenta { | button = 0, led = 'left', subBank = 'active' |
    this.turnControlButtonColor(button, led, 'magenta', subBank);
  }
  turnControlButtonCyan { | button = 0, led = 'left', subBank = 'active' |
    this.turnControlButtonColor(button, led, 'cyan', subBank);
  }
  turnControlButtonWhite { | button = 0, led = 'left', subBank = 'active' |
    this.turnControlButtonColor(button, led, 'white', subBank);
  }

  turnAllControlButtonsColor { | color = 'off', subBank = 'active' |
    8.do({ | index | this.turnControlButtonColor(index, 'left', color, 'subBank'); });
    8.do({ | index | this.turnControlButtonColor(index, 'right', color, 'subBank'); });
  }
  turnAllControlButtonsOff { | subBank = 'active' |
    this.turnAllControlButtonsColor('off', subBank);
  }
  turnAllControlButtonsRed { | subBank = 'active' |
    this.turnAllControlButtonsColor('red', subBank);
  }
  turnAllControlButtonsGreen { | subBank = 'active' |
    this.turnAllControlButtonsColor('green', subBank);
  }
  turnAllControlButtonsBlue { | subBank = 'active' |
    this.turnAllControlButtonsColor('blue', subBank);
  }
  turnAllControlButtonsYellow { | subBank = 'active' |
    this.turnAllControlButtonsColor('yellow', subBank);
  }
  turnAllControlButtonsMagenta { | subBank = 'active' |
    this.turnAllControlButtonsColor('magenta', subBank);
  }
  turnAllControlButtonsCyan { | subBank = 'active' |
    this.turnAllControlButtonsColor('cyan', subBank);
  }
  turnAllControlButtonsWhite { | subBank = 'active' |
    this.turnAllControlButtonsColor('white', subBank);
  }

  turnTouchButtonOff { | button = 0, led = 'middle', subBank = 'active' |
    this.turnTouchButtonColor(button, led, 'off', subBank);
  }
  turnTouchButtonRed { | button = 0, led = 'middle', subBank = 'active' |
    this.turnTouchButtonColor(button, led, 'red', subBank);
  }
  turnTouchButtonGreen { | button = 0, led = 'middle', subBank = 'active' |
    this.turnTouchButtonColor(button, led, 'green', subBank);
  }
  turnTouchButtonBlue { | button = 0, led = 'middle', subBank = 'active' |
    this.turnTouchButtonColor(button, led, 'blue', subBank);
  }
  turnTouchButtonYellow { | button = 0, led = 'middle', subBank = 'active' |
    this.turnTouchButtonColor(button, led, 'yellow', subBank);
  }
  turnTouchButtonMagenta { | button = 0, led = 'middle', subBank = 'active' |
    this.turnTouchButtonColor(button, led, 'magenta', subBank);
  }
  turnTouchButtonCyan { | button = 0, led = 'middle', subBank = 'active' |
    this.turnTouchButtonColor(button, led, 'cyan', subBank);
  }
  turnTouchButtonWhite { | button = 0, led = 'middle', subBank = 'active' |
    this.turnTouchButtonColor(button, led, 'white', subBank);
  }

  turnAllTouchButtonsColor { | color = 'off', subBank = 'active' |
    8.do({ | index | this.turnTouchButtonColor(index, 'middle', color, subBank); });
    8.do({ | index | this.turnTouchButtonColor(index, 'top', color, subBank); });
  }
  turnAllTouchButtonsOff { | subBank = 'active' |
    this.turnAllTouchButtonsColor('off', subBank);
  }
  turnAllTouchButtonsRed { | subBank = 'active' |
    this.turnAllTouchButtonsColor('red', subBank);
  }
  turnAllTouchButtonsGreen { | subBank = 'active' |
    this.turnAllTouchButtonsColor('green', subBank);
  }
  turnAllTouchButtonsBlue { | subBank = 'active' |
    this.turnAllTouchButtonsColor('blue', subBank);
  }
  turnAllTouchButtonsYellow { | subBank = 'active' |
    this.turnAllTouchButtonsColor('yellow', subBank);
  }
  turnAllTouchButtonsMagenta { | subBank = 'active' |
    this.turnAllTouchButtonsColor('magenta', subBank);
  }
  turnAllTouchButtonsCyan { | subBank = 'active' |
    this.turnAllTouchButtonsColor('cyan', subBank);
  }
  turnAllTouchButtonsWhite { | subBank = 'active' |
    this.turnAllTouchButtonsColor('white', subBank);
  }

  setMasterFaderMode { | mode = 'redFill', subBank = 'active' |
    this.setFaderMode(8, mode, subBank);
  }
  setAllFaderModes { | mode = 'redFill', subBank = 'active' |
    9.do({ | index | this.setFaderMode(index, mode, subBank); });
  }


}