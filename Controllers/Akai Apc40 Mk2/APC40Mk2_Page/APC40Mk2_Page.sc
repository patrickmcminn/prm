/*
Monday, May 28th 2018
APC40Mk2_Page.sc
prm
*/

APC40Mk2_Page {

  var noteOnFuncArray, noteOffFuncArray, controlFuncArray;
  var buttonColorArray, knobValueArray;

  var <gridBank, <mixerBank, <encoderBank, <controlBank;
  var <activeGridBnk, <activeMixerBnk, <activeEncoderBnk, <activeControlBnk;

  var <loadFunctionDict, <offLoadFunctionDict;

  *new { ^super.new.prInit }

  prInit {
    loadFunctionDict = IdentityDictionary.new;
    offLoadFunctionDict = IdentityDictionary.new;
    this.prMakeResponders;
    this.prMakeButtonColorArray;
    this.prMakeFaderValueArray;
    this.prMakeBanks;
  }

  prMakeResponders {
    this.prMakeNoteResponders;
    this.prMakeControlResponders;
  }

  prFreeResponders {
    this.prFreeNoteResponders;
    this.prFreeControlResponders;
  }

  prMakeNoteResponders {
    noteOnFuncArray = Array.fill(102, { });
    noteOffFuncArray = Array.fill(102, { });
  }

  prFreeNoteResponders {
    noteOnFuncArray.do({ | func | func.free; });
    noteOffFuncArray.do({ | func | func.free; });
  }

  prMakeControlResponders {
    //controlFuncArray = Array.fill(72, { });
  }

  prFreeControlResponders {
    //controlFuncArray.do({ | func | func.free; });
  }

  //////// colors:
  prMakeButtonColorArray {
    buttonColorArray = Array.fill(81, { 'off' });
  }

}