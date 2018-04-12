LPM_Bank {
  classvar topCCs, sideNotes, gridNotes;
  var myType, <bank;

  *initClass {
    topCCs = (104..111);
    sideNotes = [8, 24, 40, 56, 72, 88, 104, 120];
    gridNotes = [(0..7), (16..23), (32..39), (48..55), (64..71), (80..87), (96..103), (112..119)];
  }

  *new { |type = \top, isEnabled = true|
    ^super.new.prInit(type, isEnabled);
  }

  prInit { |type, isEnabled|
    myType = type;
    this.prMakeBank(isEnabled);
  }

  prMakeBank { |isEnabled|
    switch(myType,
      \top, { bank = Array.fill(8, { |index| LPM_Cell.new(topCCs[index], \cc, isEnabled) }); },
      \side, { bank = Array.fill(8, { |index| LPM_Cell.new(sideNotes[index], \note, isEnabled) }); },
      \grid, { bank = Array.fill2D(8, 8, { |rowIndex, colIndex|
          LPM_Cell.new(gridNotes[colIndex][rowIndex], \note, isEnabled)
      }); }
    );
  }

  enable {
    if(myType == \grid,
      { bank.do { |row| row.do { |cell| cell.enable } }; },
      { bank.do { |cell| cell.enable }; }
    );
  }

  disable {
    if(myType == \grid,
      { bank.do { |row| row.do { |cell| cell.disable } }; },
      { bank.do { |cell| cell.disable }; }
    );
  }

  pause {
    if(myType == \grid,
      { bank.do { |row| row.do { |cell| cell.pause } }; },
      { bank.do { |cell| cell.pause }; }
    );
  }

  play {
    if(myType == \grid,
      { bank.do { |row| row.do { |cell| cell.play } }; },
      { bank.do { |cell| cell.play }; }
    );
  }

  free {
    if(myType == \grid,
      { bank.do { |row| row.do { |cell| cell.free } }; },
      { bank.do { |cell| cell.free }; }
    );
  }
}


// Remove doesn't work properly
LPM_BankList {
  var myType;
  var bankList, <activeIndex;

  *new { |type = \top, isEnabled = true|
    ^super.new.prInit(type, isEnabled);
  }

  prInit { |type, isEnabled|
    myType = type;

    bankList = List.new(0);
    bankList.add( LPM_Bank(myType, isEnabled) );

    activeIndex = 0;
  }

  addBank {
    bankList.add( LPM_Bank(myType, false) );
  }

  removeBank { |index = 0|
    if( ((index > 0) && (index < bankList.size)),
      {
        bankList[index].free;
        bankList.removeAt(index);

        if(activeIndex == index, {
          activeIndex = index - 1;
          bankList[activeIndex].enable;
        });
      },
      {
        if( index == 0,
          { "Removal of bank 0 not allowed.".postln },
          { "Index out of bounds.".postln }
        );
      }
    );
  }

  enable { |index = \active|
    if(index == \active,
      { bankList[activeIndex].enable },
      { bankList[index].enable }
    );
  }

  disable { |index = \active|
    if(index == \active,
      { bankList[activeIndex].disable },
      { bankList[index].disable }
    );
  }

  setBank { |index = 0|
    if( ((index >= 0) && (index < bankList.size)),
      {
        this.disable;
        activeIndex = index;
        this.enable;
      },
      { "Index out of bounds.".postln; }
    );
  }

  // Wrappers for deeper access to banks
  active { ^bankList[activeIndex].bank }
  bank { |index = 0| ^bankList[index].bank }

  pause { bankList[activeIndex].pause }
  play { bankList[activeIndex].play }

  free {
    bankList.do { |bank| bank.free };
  }
}