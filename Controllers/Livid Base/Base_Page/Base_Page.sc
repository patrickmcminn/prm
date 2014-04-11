
Base_Page {

  var noteOnFuncArray, noteOffFuncArray, controlFuncArray, colorArray;

  var gridSubBankArray, controlSubBankArray, touchFaderSubBankArray, toucButtonSubBankArray;
  var <activeGridSubBank, <activeControlSubBank, <activeTouchFaderSubBank, <activeTouchButtonSubBank;

  *new { ^super.new.prInit }

  prInit {
    this.prMakeResponders;
    this.prMakeColorArray;
    this.prMakeNoteSubBanks;
    this.prMakeControlSubBanks;
  }

}