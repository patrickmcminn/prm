/*
Monday, November 26th 2018
BeatstepPro_Page.sc
prm
*/

BeatstepPro_Page {


  var <activeSequencer1Bnk = 0, <activeSequencer2Bnk = 0, <activeDrumBnk = 0, <activeControlBnk = 0;

  var <sequencer1Dict, <sequencer2Dict, <drumSequencerDict;
  var <sequencerClock, <tempo, <beats;

  var <sequencer1ButtonFuncArray, <sequencer2ButtonFuncArray, <drumButtonFuncArray, <controlButtonFuncArray;
  var <controlEncoderFuncArray;

  var <sequencer1BankArray, <sequencer2BankArray, <drumBankArray, <controlBankArray;

  var <loadFunctionDict, <offLoadFunctionDict;

  *new { ^super.new.prInit; }

  prInit {
    loadFunctionDict = IdentityDictionary.new;
    offLoadFunctionDict = IdentityDictionary.new;
    this.prMakeResponders;
    this.prMakeBanks;
  }

  prMakeResponders {
    sequencer1ButtonFuncArray = Array.fill2D(2, 84, { });
    sequencer2ButtonFuncArray = Array.fill2D(2, 84, { });
    drumButtonFuncArray = Array.fill2D(2, 16, { });
    controlButtonFuncArray = Array.fill2D(2, 16, { });
    controlEncoderFuncArray = Array.fill(16, { });
  }

  prFreeResponders {
    sequencer1ButtonFuncArray.do({ | f | f.free; });
    sequencer2ButtonFuncArray.do({ | f | f.free; });
    drumButtonFuncArray.do({ | f | f.free; });
    controlButtonFuncArray.do({ | f | f.free; });
    controlEncoderFuncArray.do({ | f | f.free; });
  }

  addLoadFunction { | name, func |
    loadFunctionDict[name] = func;
  }
  addOffLoadFunction { | name, func |
    offLoadFunctionDict[name] = func;
  }

  ////////////////////////////////////
  /////// Function Setting: /////////
  //////////////////////////////////

  ////////////////////////////////
  ///// Function Reporting: /////
  //////////////////////////////

  getSequencer1ButtonFunc { | num, type |
    switch(type,
      \noteOn, { ^sequencer1ButtonFuncArray[0][num] },
      \noteOff, { ^sequencer1ButtonFuncArray[1][num] },
    );
  }

  getSequencer2ButtonFunc { | num, type |
    switch(type,
      \noteOn, { ^sequencer2ButtonFuncArray[0][num] },
      \noteOff, { ^sequencer2ButtonFuncArray[1][num] },
    );
  }

  getDrumButtonFunc { | num, type |
    switch(type,
      \noteOn, { ^drumButtonFuncArray[0][num] },
      \noteOff, { ^drumButtonFuncArray[1][num] },
    );
  }

  getControlButtonFunc { | num, type |
    switch(type,
      \noteOn, { ^controlButtonFuncArray[0][num] },
      \noteOff, { ^controlButtonFuncArray[1][num] },
    );
  }

  getControlEncoderFunc { | num |
    ^controlButtonFuncArray[num];
  }

  ////////////////////////////////
  ///////////// Functions: //////
  //////////////////////////////

  prSetSequencer1Func { | num = 0, type = 'noteOn', func |
    switch(type,
      \noteOn, { sequencer1ButtonFuncArray[0][num] = func },
      \noteOff, { sequencer1ButtonFuncArray[1][num] = func }
    );
  }

  setSequencer1Func { | num = 24, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    var index = num-24;
    if( bank == activeSequencer1Bnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeSequencer1Bnk }, { bankSelect = bank });
    if( index > 84, { ^"Out of Range!" }, {
      switch(type,
        \noteOn, { sequencer1BankArray[bankSelect][index][0] = func; },
        \noteOff, { sequencer1BankArray[bankSelect][index][1] = func; }
      );
      if( bank == 'active', { this.prSetSequencer1Func(index, type, func); });
    });
  }

  prSetSequencer2Func { | num = 0, type = 'noteOn', func |
    switch(type,
      \noteOn, { sequencer2ButtonFuncArray[0][num] = func },
      \noteOff, { sequencer2ButtonFuncArray[1][num] = func }
    );
  }

  setSequencer2Func { | num = 24, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    var index = num-24;
    if( bank == activeSequencer2Bnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeSequencer2Bnk }, { bankSelect = bank });
    if( index > 84, { ^"Out of Range!" }, {
      switch(type,
        \noteOn, { sequencer2BankArray[bankSelect][index][0] = func; },
        \noteOff, { sequencer2BankArray[bankSelect][index][1] = func; }
      );
      if( bank == 'active', { this.prSetSequencer2Func(index, type, func); });
    });
  }

  prSetDrumButtonFunc { | num = 0, type = 'noteOn', func |
    switch(type,
      \noteOn, { drumButtonFuncArray[0][num] = func },
      \noteOff, { drumButtonFuncArray[1][num] = func }
    );
  }

  setDrumFunc { | num = 1, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    var index = num-1;
    if( bank == activeDrumBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeDrumBnk }, { bankSelect = bank });
    bankSelect.postln;
    if( index > 15, { ^"Out of Range!" }, {
      switch(type,
        \noteOn, { drumBankArray[bankSelect][index][0] = func; num.postln; },
        \noteOff, { drumBankArray[bankSelect][index][1] = func; }
      );
      if( bank == 'active', { this.prSetDrumButtonFunc(index, type, func); "this posted".postln; });
    });
  }

  prSetControlButtonFunc { | num = 0, type = 'noteOn', func |
    switch(type,
      \noteOn, { controlButtonFuncArray[0][num] = func },
      \noteOff, { controlButtonFuncArray[1][num] = func }
    );
  }

  setControlButtonFunc { | num = 1, func, type = 'noteOn', bank = 'active' |
    var bankSelect;
    var index = num-1;
    if( bank == activeControlBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeControlBnk }, { bankSelect = bank });
    if( index > 15, { ^"Out of Range!" }, {
      switch(type,
        \noteOn, { controlBankArray[bankSelect][0][index][0] = func; },
        \noteOff, { controlBankArray[bankSelect][0][index][1] = func; }
      );
      if( bank == 'active', { this.prSetControlButtonFunc(index, func); });
    });
  }

  prSetControlEncoderFunc { | num = 0, func |
    controlEncoderFuncArray[num] = func;
  }

  setControlEncoderFunc { | num = 1, func, bank = 'active' |
    var bankSelect;
    var index = num-1;
    if( bank == activeControlBnk, { bank = 'active' });
    if( bank == 'active', { bankSelect = activeControlBnk }, { bankSelect = bank });
    if( index > 15, { ^"Out of Range!" }, {
      controlBankArray[bankSelect][1][index][0] = func;
      if( bank == 'active', { this.prSetControlEncoderFunc(index, func); });
    });
  }

}