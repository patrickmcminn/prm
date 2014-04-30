+ Base {

  turnButtonColor { | num, color = \off, bank = 'active' |
    var bankSelect;
    if( bank == 'active', { bankSelect = currentBank; }, { bankSelect = bank });
    bankSelect = bankSelect - 1;
    switch(color,
      { \off }, { midiOutPort.noteOn(bankSelect, colorArray[num], 0) },
      { \red }, { midiOutPort.noteOn(bankSelect, colorArray[num], 16) },
      { \green }, { midiOutPort.noteOn(bankSelect, colorArray[num], 127) },
      { \blue }, { midiOutPort.noteOn(bankSelect, colorArray[num], 32) },
      { \yellow }, { midiOutPort.noteOn(bankSelect, colorArray[num], 64) },
      { \magenta }, { midiOutPort.noteOn(bankSelect, colorArray[num], 8) },
      { \cyan }, { midiOutPort.noteOn(bankSelect, colorArray[num], 4) },
      { \white }, { midiOutPort.noteOn(bankSelect, colorArray[num], 1) },
    );
  }

  turnButtonRed { | num = 0, bank = 'active' | this.turnButtonColor(num, \red, bank); }

  turnButtonGreen { | num = 0, bank = 'active' | this.turnButtonColor(num, \green, bank); }

  turnButtonBlue { | num = 0, bank = 'active' | this.turnButtonColor(num, \blue, bank); }

  turnButtonYellow { | num = 0, bank = 'active' | this.turnButtonColor(num, \yellow, bank); }

  turnButtonMagenta { | num = 0, bank = 'active' | this.turnButtonColor(num, \magenta, bank); }

  turnButtonCyan { | num = 0, bank = 'active' | this.turnButtonColor(num, \cyan, bank); }

  turnButtonWhite { | num = 0, bank = 'active' | this.turnButtonColor(num, \white, bank); }

  setFaderMode { | num = 10, mode = 'blueFill' |
    switch(mode,
      { 'invertedWalk' }, { midiOutPort.control(16, num, 64); },
      { 'invertedFill' }, { midiOutPort.control(16, num, 65); },
      { 'invertedEQ' }, { midiOutPort.control(16, num, 66); },
      { 'invertedSpread' }, { midiOutPort.control(16, num, 67); },
      { 'redWalk' }, { midiOutPort.control(16, num, 68); },
      { 'redFill' }, { midiOutPort.control(16, num, 69); },
      { 'redEQ' }, { midiOutPort.control(16, num, 70); },
      { 'redSpread' }, { midiOutPort.control(16, num, 71); },
      { 'greenWalk' }, { midiOutPort.control(16, num, 72); },
      { 'greenFill' }, { midiOutPort.control(16, num, 73); },
      { 'greenEQ' }, { midiOutPort.control(16, num, 74); },
      { 'greenSpread' }, { midiOutPort.control(16, num, 75); },
      { 'yellowWalk' }, { midiOutPort.control(16, num, 76); },
      { 'yellowFill' }, { midiOutPort.control(16, num, 77); },
      { 'yellowEQ' }, { midiOutPort.control(16, num, 78); },
      { 'yellowSpread' }, { midiOutPort.control(16, num, 79); },
      { 'blueWalk' }, { midiOutPort.control(16, num, 80); },
      { 'blueFill' }, { midiOutPort.control(16, num, 81); },
      { 'blueEQ' }, { midiOutPort.control(16, num, 82); },
      { 'blueSpread' }, { midiOutPort.control(16, num, 83); },
      { 'magentaWalk' }, { midiOutPort.control(16, num, 84); },
      { 'magentaFill' }, { midiOutPort.control(16, num, 85); },
      { 'magentaEQ' }, { midiOutPort.control(16, num, 86); },
      { 'magentaSpread' }, { midiOutPort.control(16, num, 87); },
      { 'cyanWalk' }, { midiOutPort.control(16, num, 88); },
      { 'cyanFill' }, { midiOutPort.control(16, num, 89); },
      { 'cyanEQ' }, { midiOutPort.control(16, num, 90); },
      { 'cyanSpread' }, { midiOutPort.control(16, num, 91); },
      { 'whiteWalk' }, { midiOutPort.control(16, num, 92); },
      { 'whiteFill' }, { midiOutPort.control(16, num, 93); },
      { 'whiteEQ' }, { midiOutPort.control(16, num, 94); },
      { 'whiteSpread' }, { midiOutPort.control(16, num, 95); },
    );
  }

  // convenience colors (for paging):

  turnGridColor { | column = 0, row = 0, color = 'off', subBank = 'active', page = 'active' |
    if( column > 8 || row > 4, { "out of range".postln; },
      {
        var num = ((column * 8) + row) + 36;
        if( page == 'active', { page = activePageKey });
        if( subBank == 'active', { subBank = pageDict[page].activeGridBank });
        pageDict[page].turnGridColor(column, row, color, subBank);
        this.turnColor(num, activePage.getColor(num));
      }
    );
  }

  turnGridOff { | column = 0, row = 0, subBank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'off', subBank, page);
  }

  turnGridRed { | column = 0, row = 0, subBank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'red', subBank, page);
  }

  turnGridGreen { | column = 0, row = 0, subBank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'green', subBank, page);
  }

  turnGridBlue { | column = 0, row = 0, subBank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'blue', subBank, page);
  }

  turnGridYellow { | column = 0, row = 0, subBank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'yellow', subBank, page);
  }

  turnGridMagenta { | column = 0, row = 0, subBank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'magenta', subBank, page);
  }

  turnGridCyan { | column = 0, row = 0, subBank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'cyan', subBank, page);
  }

  turnGridWhite { | column = 0, row = 0, subBank = 'active', page = 'active' |
    this.turnGridColor(column, row, 'white', subBank, page);
  }

  turnAllGridColor { | color = 'off', subBank = 'active', page = 'active' |
    8.do({ | column | 4.do({ | row | this.turnGridColor(column, row, color, subBank, page); }); });
  }

  turnAllGridOff { | subBank = 'active', page = 'active' |
    this.turnAllGridColor('off', subBank, page);
  }

  turnAllGridRed { | subBank = 'active', page = 'active' |
    this.turnAllGridColor('red', subBank, page);
  }

  turnAllGridGreen { | subBank = 'active', page = 'active' |
    this.turnAllGridColor('green', subBank, page);
  }

  turnAllGridBlue { | subBank = 'active', page = 'active' |
    this.turnAllGridColor('blue', subBank, page);
  }

  turnAllGridYellow { | subBank = 'active', page = 'active' |
    this.turnAllGridColor('yellow', subBank, page);
  }

  turnAllGridMagenta { | subBank = 'active', page = 'active' |
    this.turnAllGridColor('magenta', subBank, page);
  }

  turnAllGridCyan { | subBank = 'active', page = 'active' |
    this.turnAllGridColor('cyan', subBank, page);
  }

  turnAllGridWhite { | subBank = 'active', page = 'active' |
    this.turnAllGridColor('white', subBank, page);
  }

  turnControlButtonColor { | button = 1, led = 'left', color = 'off' |

  }

}