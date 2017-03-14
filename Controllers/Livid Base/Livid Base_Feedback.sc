+ Base {

  turnButtonColor { | note, color = \off |
    switch(color,
      { \off }, { midiOutPort.noteOn(0, colorArray[note], 0) },
      { \red }, { midiOutPort.noteOn(0, colorArray[note], 16) },
      { \green }, { midiOutPort.noteOn(0, colorArray[note], 127) },
      { \blue }, { midiOutPort.noteOn(0, colorArray[note], 32) },
      { \yellow }, { midiOutPort.noteOn(0, colorArray[note], 64) },
      { \magenta }, { midiOutPort.noteOn(0, colorArray[note], 8) },
      { \cyan }, { midiOutPort.noteOn(0, colorArray[note], 4) },
      { \white }, { midiOutPort.noteOn(0, colorArray[note], 1) },
    );
  }

  prSetFaderValue { | cc = 1, val = 64 | midiOutPort.control(0, cc, val); }

  prSetFaderMode { | cc = 10, mode = 'blueFill' |
    switch(mode,
      { 'invertedWalk' }, { midiOutPort.control(15, cc, 64); },
      { 'invertedFill' }, { midiOutPort.control(15, cc, 65); },
      { 'invertedEQ' }, { midiOutPort.control(15, cc, 66); },
      { 'invertedSpread' }, { midiOutPort.control(15, cc, 67); },
      { 'redWalk' }, { midiOutPort.control(15, cc, 68); },
      { 'redFill' }, { midiOutPort.control(15, cc, 69); },
      { 'redEQ' }, { midiOutPort.control(15, cc, 70); },
      { 'redSpread' }, { midiOutPort.control(15, cc, 71); },
      { 'greenWalk' }, { midiOutPort.control(15, cc, 72); },
      { 'greenFill' }, { midiOutPort.control(15, cc, 73); },
      { 'greenEQ' }, { midiOutPort.control(15, cc, 74); },
      { 'greenSpread' }, { midiOutPort.control(15, cc, 75); },
      { 'yellowWalk' }, { midiOutPort.control(15, cc, 76); },
      { 'yellowFill' }, { midiOutPort.control(15, cc, 77); },
      { 'yellowEQ' }, { midiOutPort.control(15, cc, 78); },
      { 'yellowSpread' }, { midiOutPort.control(15, cc, 79); },
      { 'blueWalk' }, { midiOutPort.control(15, cc, 80); },
      { 'blueFill' }, { midiOutPort.control(15, cc, 81); },
      { 'blueEQ' }, { midiOutPort.control(15, cc, 82); },
      { 'blueSpread' }, { midiOutPort.control(15, cc, 83); },
      { 'magentaWalk' }, { midiOutPort.control(15, cc, 84); },
      { 'magentaFill' }, { midiOutPort.control(15, cc, 85); },
      { 'magentaEQ' }, { midiOutPort.control(15, cc, 86); },
      { 'magentaSpread' }, { midiOutPort.control(15, cc, 87); },
      { 'cyanWalk' }, { midiOutPort.control(15, cc, 88); },
      { 'cyanFill' }, { midiOutPort.control(15, cc, 89); },
      { 'cyanEQ' }, { midiOutPort.control(15, cc, 90); },
      { 'cyanSpread' }, { midiOutPort.control(15, cc, 91); },
      { 'whiteWalk' }, { midiOutPort.control(15, cc, 92); },
      { 'whiteFill' }, { midiOutPort.control(15, cc, 93); },
      { 'whiteEQ' }, { midiOutPort.control(15, cc, 94); },
      { 'whiteSpread' }, { midiOutPort.control(15, cc, 95); },
    );
  }

}

+ Base {

  turnButtonRed { | num = 0 | this.turnButtonColor(num, \red); }
  turnButtonGreen { | num = 0 | this.turnButtonColor(num, \green); }
  turnButtonBlue { | num = 0 | this.turnButtonColor(num, \blue); }
  turnButtonYellow { | num = 0 | this.turnButtonColor(num, \yellow); }
  turnButtonMagenta { | num = 0  | this.turnButtonColor(num, \magenta); }
  turnButtonCyan { | num = 0 | this.turnButtonColor(num, \cyan); }
  turnButtonWhite { | num = 0  | this.turnButtonColor(num, \white); }

  // convenience colors (for paging):

  turnGridColor { | column = 0, row = 0, color = 'off', bank = 'active', page = 'active' |
    if( column > 8 || row > 4, { "out of range".postln; },
      {
        var num = ((row * 8) + column) + 36;
        if( page == 'active', { page = activePageKey });
        if( page == activePageKey, { if(
          color != activePage.getButtonColor(num),
          { this.turnButtonColor(num, color); }); });
        pageDict[page].turnGridColor(column, row, color, bank);
      }
    );
  }

  turnControlButtonColor { | button = 1, led = 'left', color = 'off', bank = 'active', page = 'active' |
    if ( button > 8, { "Out of Range".postln; },
      {
        var num = (button - 1) + switch(led, { 'left' }, { 0 }, { 'right' }, { 8 });
        var midinum = num + 18;
        if( page == 'active', { page = activePageKey });
        if( page == activePageKey, { if(
          color != activePage.getButtonColor(midinum),
          { this.turnButtonColor(midinum, color); });
        });
        pageDict[page].turnControlButtonColor(button, led, color, bank);
      }
    );
  }

  turnTouchButtonColor { | button = 1, led = 'middle', color = 'off', bank = 'active', page = 'active' |
    if( button > 8, { "Out of Range".postln; },
      {
        var num = (button-1) + switch(led, { 'middle' }, { 10 }, { 'top' }, { 68 });
        if( page == 'active', { page = activePageKey });
        if( page == activePageKey, { if(
          color != activePage.getButtonColor(num),
          { this.turnButtonColor(num, color); });
        });
        pageDict[page].turnTouchButtonColor((button-1), led, color, bank);
      }
    );
  }

  setFaderValue { | fader = 1, value = 0, bank = 'active', page = 'active' |
    if(fader > 9, { "Out of Range".postln; },
      {
        if( page == 'active', { page = activePageKey });
        if( page == activePageKey, { if(
          value != activePage.faderValueArray[fader-1],
          { this.prSetFaderValue(fader, value); });
        });
        pageDict[page].setFaderValue(fader, value, bank);
      }
    );
  }

  setFaderMode { | fader = 1, mode = 'redFill', bank = 'active', page = 'active' |
    if(fader > 9, { "Out of Range".postln; },
      {
        if( page == 'active', { page = activePageKey });
        if( page == activePageKey, { if(
          mode != activePage.faderModeArray[fader-1],
          { this.prSetFaderMode(fader + 9, mode); });
        });
        pageDict[page].setFaderMode(fader, mode, bank);
      }
    );
  }


}


// even more convenience:
+ Base {

  // grid functions:
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
  turnGridMagenta { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'magenta', bank, page);
  }
  turnGridCyan { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'cyan', bank, page);
  }
  turnGridWhite { | column = 0, row = 0, bank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'white', bank, page);
  }

  // all grids:
   turnAllGridColor { | color = 'off', bank = 'active', page = 'active' |
    8.do({ | column | 4.do({ | row | this.turnGridColor(column, row, color, bank, page); }); });
  }

  turnAllGridOff { | bank = 'active', page = 'active' |
    this.turnAllGridColor('off', bank, page);
  }
  turnAllGridRed { | bank = 'active', page = 'active' |
    this.turnAllGridColor('red', bank, page);
  }
  turnAllGridGreen { | bank = 'active', page = 'active' |
    this.turnAllGridColor('green', bank, page);
  }
  turnAllGridBlue { | bank = 'active', page = 'active' |
    this.turnAllGridColor('blue', bank, page);
  }
  turnAllGridYellow { | bank = 'active', page = 'active' |
    this.turnAllGridColor('yellow', bank, page);
  }
  turnAllGridMagenta { | bank = 'active', page = 'active' |
    this.turnAllGridColor('magenta', bank, page);
  }
  turnAllGridCyan { | bank = 'active', page = 'active' |
    this.turnAllGridColor('cyan', bank, page);
  }
  turnAllGridWhite { | bank = 'active', page = 'active' |
    this.turnAllGridColor('white', bank, page);
  }

  // control buttons:
  turnControlButtonOff { | button = 1, led = 'left', bank = 'active', page = 'active' |
    this.turnControlButtonColor(button, led, 'off', bank, page);
  }
  turnControlButtonRed { | button = 1, led = 'left', bank = 'active', page = 'active' |
    this.turnControlButtonColor(button, led, 'red', bank, page);
  }
  turnControlButtonGreen { | button = 1, led = 'left', bank = 'active', page = 'active' |
    this.turnControlButtonColor(button, led, 'green', bank, page);
  }
  turnControlButtonBlue { | button = 1, led = 'left', bank = 'active', page = 'active' |
    this.turnControlButtonColor(button, led, 'blue', bank, page);
  }
  turnControlButtonYellow { | button = 1, led = 'left', bank = 'active', page = 'active' |
    this.turnControlButtonColor(button, led, 'yellow', bank, page);
  }
  turnControlButtonMagenta { | button = 1, led = 'left', bank = 'active', page = 'active' |
    this.turnControlButtonColor(button, led, 'magenta', bank, page);
  }
  turnControlButtonCyan { | button = 1, led = 'left', bank = 'active', page = 'active' |
    this.turnControlButtonColor(button, led, 'cyan', bank, page);
  }
  turnControlButtonWhite { | button = 1, led = 'left', bank = 'active', page = 'active' |
    this.turnControlButtonColor(button, led, 'white', bank, page);
  }

  turnAllControlButtonsColor { | color = 'off', bank = 'active', page = 'active' |
    8.do({ | button | this.turnControlButtonColor(button + 1, 'left', color, bank, page); });
    8.do({ | button | this.turnControlButtonColor(button + 1, 'right', color, bank, page); });
  }
  turnAllControlButtonsOff { | bank = 'active', page = 'active' |
    this.turnAllControlButtonsColor('off', bank, page);
  }
  turnAllControlButtonsRed { | bank = 'active', page = 'active' |
    this.turnAllControlButtonsColor('red', bank, page);
  }
  turnAllControlButtonsGreen { | bank = 'active', page = 'active' |
    this.turnAllControlButtonsColor('green', bank, page);
  }
  turnAllControlButtonsBlue { | bank = 'active', page = 'active' |
    this.turnAllControlButtonsColor('blue', bank, page);
  }
  turnAllControlButtonsYellow { | bank = 'active', page = 'active' |
    this.turnAllControlButtonsColor('yellow', bank, page);
  }
  turnAllControlButtonsMagenta { | bank = 'active', page = 'active' |
    this.turnAllControlButtonsColor('magenta', bank, page);
  }
  turnAllControlButtonsCyan { | bank = 'active', page = 'active' |
    this.turnAllControlButtonsColor('cyan', bank, page);
  }
  turnAllControlButtonsWhite { | bank = 'active', page = 'active' |
    this.turnAllControlButtonsColor('white', bank, page);
  }

  turnTouchButtonOff { | button = 0, led = 'middle', bank = 'active', page = 'active' |
    this.turnTouchButtonColor(button, led, 'off', bank, page);
  }
  turnTouchButtonRed { | button = 0, led = 'middle', bank = 'active', page = 'active' |
    this.turnTouchButtonColor(button, led, 'red', bank, page);
  }
  turnTouchButtonGreen { | button = 0, led = 'middle', bank = 'active', page = 'active' |
    this.turnTouchButtonColor(button, led, 'green', bank, page);
  }
  turnTouchButtonBlue { | button = 0, led = 'middle', bank = 'active', page = 'active' |
    this.turnTouchButtonColor(button, led, 'blue', bank, page);
  }
  turnTouchButtonYellow { | button = 0, led = 'middle', bank = 'active', page = 'active' |
    this.turnTouchButtonColor(button, led, 'yellow', bank, page);
  }
  turnTouchButtonMagenta { | button = 0, led = 'middle', bank = 'active', page = 'active' |
    this.turnTouchButtonColor(button, led, 'magenta', bank, page);
  }
  turnTouchButtonCyan { | button = 0, led = 'middle', bank = 'active', page = 'active' |
    this.turnTouchButtonColor(button, led, 'cyan', bank, page);
  }
  turnTouchButtonWhite { | button = 0, led = 'middle', bank = 'active', page = 'active' |
    this.turnTouchButtonColor(button, led, 'white', bank, page);
  }

  turnAllTouchButtonsColor { | color = 'off', bank = 'active', page = 'active' |
    8.do({ | button | this.turnTouchButtonColor(button + 1, 'middle', color, bank, page); });
    8.do({ | button | this.turnTouchButtonColor(button + 1, 'top', color, bank, page); });
  }
  turnAllTouchButtonsOff { | bank = 'active', page = 'active' |
    this.turnAllTouchButtonsColor('off', bank, page);
  }
  turnAllTouchButtonsRed { | bank = 'active', page = 'active' |
    this.turnAllTouchButtonsColor('red', bank, page);
  }
  turnAllTouchButtonsGreen { | bank = 'active', page = 'active' |
    this.turnAllTouchButtonsColor('green', bank, page);
  }
  turnAllTouchButtonsBlue { | bank = 'active', page = 'active' |
    this.turnAllTouchButtonsColor('blue', bank, page);
  }
  turnAllTouchButtonsYellow { | bank = 'active', page = 'active' |
    this.turnAllTouchButtonsColor('yellow', bank, page);
  }
  turnAllTouchButtonsCyan { | bank = 'active', page = 'active' |
    this.turnAllTouchButtonsColor('cyan', bank, page);
  }
  turnAllTouchButtonsMagenta { | bank = 'active', page = 'active' |
    this.turnAllTouchButtonsColor('magenta', bank, page);
  }
  turnAllTouchButtonsWhite { | bank = 'active', page = 'active' |
    this.turnAllTouchButtonsColor('white', bank, page);
  }

  setAllFaderModes { | mode = 'redFill', bank = 'active', page = 'active' |
    9.do({ | index | this.setFaderMode(index + 1, mode, bank, page); });
  }
  setAllFaderValues { | value = 0, bank = 'active', page = 'active' |
    9.do({ | index | this.setFaderValue(index + 1, value, bank, page); });
  }
}