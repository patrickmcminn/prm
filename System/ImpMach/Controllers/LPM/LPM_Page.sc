// An LPM_Page is a collection of Lists, storing banks, plus ways to organize them
LPM_Page {
  var topBankList, sideBankList, gridBankList;

  *new { |isEnabled = true|
    ^super.new.prInit(isEnabled);
  }

  prInit { |isEnabled|
    topBankList = LPM_BankList.new(\top, isEnabled);
    sideBankList = LPM_BankList.new(\side, isEnabled);
    gridBankList = LPM_BankList.new(\grid, isEnabled);
  }

  // NEED FUNCTIONS FOR ADD/REMOVE BANK, TOO
  setTop { |index = 0| topBankList.setBank(index) }
  setSide { |index = 0| sideBankList.setBank(index) }
  setGrid { |index = 0| gridBankList.setBank(index) }

  setAllBanks { |topIndex = 0, sideIndex = 0, gridIndex = 0|
    this.setTop(topIndex);
    this.setSide(sideIndex);
    this.setGrid(gridIndex);
  }

  addTop { topBankList.addBank }
  addSide { sideBankList.addBank }
  addGrid { gridBankList.addBank }

  removeTop { |index| topBankList.removeBank(index) }
  removeSide { |index| sideBankList.removeBank(index) }
  removeGrid { |index| gridBankList.removeBank(index) }

  // Bank access methods
  top { ^topBankList.active }
  side { ^sideBankList.active }
  grid { ^gridBankList.active }

  // Allows numerical access to banks, used to access inactive banks
  topBank { |index = 0| ^topBankList.bank(index) }
  sideBank { |index = 0| ^sideBankList.bank(index) }
  gridBank { |index = 0| ^gridBankList.bank(index) }

  disable {
    topBankList.disable;
    sideBankList.disable;
    gridBankList.disable;
  }

  enable {
    topBankList.enable;
    sideBankList.enable;
    gridBankList.enable;
  }

  pause {
    topBankList.pause;
    sideBankList.pause;
    gridBankList.pause;
  }

  play {
    topBankList.play;
    sideBankList.play;
    gridBankList.play;
  }

  free {
    topBankList.free;
    sideBankList.free;
    gridBankList.free;
  }
}