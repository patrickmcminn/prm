/*
Sunday, January 19th 2014
prm
*/

OhmRGB_Page : OhmRGB {

  //var midiInPort, midiOutPort;
  var noteOnFuncArray, noteOffFuncArray, controlFuncArray, colorArray, animationArray;

  var leftSlidersBankArray, rightSlidersBankArray, leftKnobsBankArray, rightKnobsBankArray, crossfaderBankArray;
  var <activeLeftSlidersBnk, <activeRightSlidersBnk, <activeLeftKnobsBnk, <activeRightKnobsBnk, <activeCrossfaderBnk;

  var <gridBankArray, <leftButtonsBankArray, <rightButtonsBankArray, <crossfaderButtonsBankArray, <controlButtonsBankArray;
  var <activeGridBnk, <activeLeftButtonsBnk, <activeRightButtonsBnk, <activeCrossfaderButtonsBnk, <activeControlButtonsBnk;

  var <loadFunctionDict, <offLoadFunctionDict;

  *new {
    ^super.new.prInit;
  }

  prInit {
    //this.prInitMIDI;
    loadFunctionDict = IdentityDictionary.new;
    offLoadFunctionDict = IdentityDictionary.new;
    this.prMakeResponders;
    this.prMakeColorArray;
    this.prMakeAnimationArray;
    this.prMakeNoteBanks;
    this.prMakeControlBanks;
  }

  /*
  prInitMIDI {
    MIDIIn.connectAll;
    midiInPort = MIDIIn.findPort("OhmRGB", "Controls");
    midiOutPort = MIDIOut.newByName("OhmRGB", "Controls");
    midiOutPort.latency = 0;
  }
  */

  prMakeResponders {
    this.prMakeNoteResponders;
    this.prMakeControlResponders;
  }

  prFreeResponders {
    this.prFreeNoteResponders;
    this.prFreeControlResponders;
  }

  prMakeNoteResponders {
    noteOnFuncArray = Array.fill(81, { {} });
    noteOffFuncArray = Array.fill(81, { {} });
  }

  prFreeNoteResponders {
    noteOnFuncArray.do({ | func | func.free; });
  }

  prMakeControlResponders {
    controlFuncArray = Array.fill(25, { {} });
  }

  prFreeControlResponders {
    controlFuncArray.do({ | func | func.free; });
  }

  prMakeColorArray {
    colorArray = Array.fill(81, { 'off' });
  }

  prMakeAnimationArray {
    animationArray = Array.fill(81, { TaskProxy.new; });
  }

  setFunc { | num = 0, type = \noteOn, func = nil |
    switch(type,
      { \noteOn }, { noteOnFuncArray[num] = func; },
      { \noteOff }, { noteOffFuncArray[num] = func; },
      { \control }, { controlFuncArray[num] = func; }
    );
  }

  clearFunc { | num = 0, type = \noteOn |
    switch(type,
      { \noteOn }, { noteOnFuncArray[num] = { }; },
      { \noteOff }, { noteOffFuncArray[num] = { }; },
      { \control }, { controlFuncArray[num] = { } }
    );
  }

  getFunc { | num = 0, type = \noteOn |
    switch(type,
      { \noteOn }, { ^noteOnFuncArray[num] },
      { \noteOff }, { ^noteOffFuncArray[num] },
      { \control }, { ^controlFuncArray[num] }
    );
  }

  setNoteOnFunc { | num, func |
    this.setFunc(num, \noteOn, func);
  }

  clearNoteOnFunc { | num |
    this.clearFunc(num, \noteOn);
  }

  setNoteOffFunc { | num, func |
    this.setFunc(num, \noteOff, func);
  }

  clearNoteOffFunc { | num |
    this.clearFunc(num, \noteOff);
  }

  setCCFunc { | num, func |
    this.setFunc(num, \control, func);
  }

  clearCCFunc { | num |
    this.clearFunc(num, \control);
  }

  getNoteOnFunc { | num = 0 | ^this.getFunc(num, 'noteOn'); }

  getNoteOffFunc { | num = 0 | ^this.getFunc(num, 'noteOff'); }

  getCCFunc { | num = 0 | ^this.getFunc(num, 'control'); }

  addLoadFunction { | name, func |
    loadFunctionDict[name] = func;
  }

  addOffLoadFunction { | name, func |
    offLoadFunctionDict[name] = func;
  }

  //////// Convenience Methods:

  setGridFunc { | column = 0, row = 0, func, type = \noteOn, bank = 'active' |
    var bankSet;
    var num = (column * 8) + row;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSet = activeGridBnk }, { bankSet = bank });
    switch(type,
      { \noteOn }, { gridBankArray[bankSet][num][0] = func; if( bank == 'active', { this.setNoteOnFunc(num, func); }) },
      { \noteOff }, { gridBankArray[bankSet][num][1] = func; if( bank == 'active', { this.setNoteOffFunc(num, func); }) }
    );
  }

  setGridMonitorFunc { | column = 0, row = 0, func, bank = 'active' |
    var bankSet;
    var num = (column * 8) + row;
    if( bank == activeGridBnk, { bank = 'active' });
    if( bank == 'active', { bankSet = activeGridBnk }, { bankSet = bank });
    gridBankArray[bankSet][num][4].stop;
    gridBankArray[bankSet][num][4] = r {
      loop {
        func.value;
        0.05.wait;
      }
    };
  }

  setLeftButtonFunc { | num, func, type = \noteOn, bank = 'active' |
    var bankSet;
    var buttonArray = [65, 73, 66, 74];
    if( bank == activeLeftButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSet = activeLeftButtonsBnk }, { bankSet = bank });
    if( num < 5,
      { switch(type,
        { \noteOn},
        {
          leftButtonsBankArray[bankSet][num][0] = func;
          if( bank == 'active', {this.setNoteOnFunc(buttonArray[num], func); })
        },
        { \noteOff },
        {
          leftButtonsBankArray[bankSet][num][1] = func;
          if( bank == 'active', { this.setNoteOffFunc(buttonArray[num], func); });
        }
        );
      },
      { ^"not that many left buttons"; }
    );
  }

  setLeftButtonMonitorFunc {  | num, func, bank = 'active' |
    var bankSet;
    var buttonArray = [65, 73, 66, 74];
    if( bank == activeLeftButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSet = activeLeftButtonsBnk }, { bankSet = bank });
    if( num < 5, {
      leftButtonsBankArray[bankSet][num][4].stop;
      leftButtonsBankArray[bankSet][num][4] = r {
        loop {
          func.value;
          0.05.wait;
        }
      };
    }, { ^"not that many left buttons"; });
  }

  setRightButtonFunc { | num, func, type = \noteOn, bank = 'active' |
    var bankSet;
    var buttonArray = [67, 75, 68, 76];
    if( bank == activeRightButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSet = activeRightButtonsBnk }, { bankSet = bank });
    if( num < 5,
      { switch(type,
        { \noteOn},
        {
          rightButtonsBankArray[bankSet][num][0] = func;
          if( bank == 'active', {this.setNoteOnFunc(buttonArray[num], func); })
        },
        { \noteOff },
        {
          rightButtonsBankArray[bankSet][num][1] = func;
          if( bank == 'active', { this.setNoteOffFunc(buttonArray[num], func); });
        }
        );
      },
      { ^"not that many right buttons"; }
    );
  }

  setRightButtonMonitorFunc { | num, func, bank = 'active' |
    var bankSet;
    var buttonArray = [67, 75, 68, 76];
    if( bank == activeRightButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSet = activeRightButtonsBnk }, { bankSet = bank });
    if( num < 5, {
      rightButtonsBankArray[bankSet][num][4].stop;
      rightButtonsBankArray[bankSet][num][4] = r {
        loop {
          func.value;
          0.05.wait;
        };
      };
    }, { ^"not that many right buttons"; });
  }


  setCrossfaderButtonFunc { | num, func, type = \noteOn, bank = 'active' |
    var bankSet;
    var buttonArray = [64, 72];
    if( bank == activeCrossfaderButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSet = activeCrossfaderButtonsBnk }, { bankSet = bank });
    if ( num < 3,
      { switch(type,
        { \noteOn },
        {
          crossfaderButtonsBankArray[bankSet][num][0] = func;
          if( bank == 'active', { this.setNoteOnFunc(buttonArray[num], func); });
        },
        { \noteOff },
        {
          crossfaderButtonsBankArray[bankSet][num][1] = func;
          if( bank == 'active', { this.setNoteOffFunc(buttonArray[num], func); })
        }
        );
      },
      { ^"not that many crossfader buttons"; }
    );
  }

  setCrossfaderButtonMonitorFunc { | num, func, bank = 'active' |
    var bankSet;
    var buttonArray = [64, 72];
    if( bank == activeCrossfaderButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSet = activeCrossfaderButtonsBnk }, { bankSet = bank });
    if ( num < 3,
      {
        crossfaderButtonsBankArray[bankSet][num][4].stop;
        crossfaderButtonsBankArray[bankSet][num][4] = r {
          loop {
            func.value;
            0.05.wait;
          };
        };
    }, { ^"not that many crossfader buttons"; });
  }


  setControlButtonFunc { | column = 0, row = 0, func, type = \noteOn, bank = 'active' |
    var bankSet;
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    var num = (column * 2) + row;
    if( bank == activeControlButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSet = activeControlButtonsBnk }, { bankSet = bank });
    if( num < 7,
      { switch(type,
        { \noteOn },
        {
          controlButtonsBankArray[bankSet][num][0] = func;
          if( bank == 'active', { this.setNoteOnFunc(buttonArray[num], func); });
        },
        { \noteOff },
        {
          controlButtonsBankArray[bankSet][num][1] = func;
          if( bank == 'active', { this.setNoteOffFunc(buttonArray[num], func); });
        }
        );
      },
      { ^"not that many control buttons" }
    );
  }

  setControlButtonMonitorFunc { | column = 0, row = 0, func, bank = 'active' |
    var bankSet;
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    var num = (column * 2) + row;
    if( bank == activeControlButtonsBnk, { bank = 'active' });
    if( bank == 'active', { bankSet = activeControlButtonsBnk }, { bankSet = bank });
    if( num < 7, {
      controlButtonsBankArray[bankSet][num][4].stop;
      controlButtonsBankArray[bankSet][num][4] = r {
        loop {
          func.value;
          0.05.wait;
        };
      };
    }, { ^"not that many control buttons" });
  }


  // don't use?
  setMasterButtonFunc { | func, type = \noteOn |
    switch(type,
      { \noteOn }, { this.setNoteOnFunc(80, func); },
      { \noteOff }, { this.setNoteOffFunc(80, func); }
    );
  }

  //////// Sliders:

  setLeftSliderFunc { | num = 0, func, bank = 'active' |
    var bankSet;
    var sliderArray = [23, 22, 15, 14];
     if( num < 4,
      {
        if( bank == activeLeftSlidersBnk, { bank = 'active'});
        if( bank == 'active', { bankSet = activeLeftSlidersBnk }, { bankSet = bank });
        leftSlidersBankArray[bankSet][num] = func;
        if( bank == 'active', { this.setCCFunc(sliderArray[num], func); });
      },
      { ^"not that many left sliders"; }
    );
  }

  setRightSliderFunc { | num = 0, func, bank = 'active' |
    var bankSet;
    var sliderArray = [5, 7, 6, 4];
     if( num < 4,
      {
        if( bank == activeRightSlidersBnk, { bank = 'active'});
        if( bank == 'active', { bankSet = activeRightSlidersBnk }, { bankSet = bank });
        rightSlidersBankArray[bankSet][num] = func;
        if( bank == 'active', { this.setCCFunc(sliderArray[num], func); });
      },
      { ^"not that many right sliders"; }
    );
  }

  //////// Knobs:

  setLeftKnobFunc { | column = 0, row = 0, func, bank = 'active' |
    var bankSet;
    var num = (column * 3) + row;
    var knobArray = [17, 19, 21, 16, 18, 20, 9, 11, 13, 8, 10, 12];
    if( num < 12,
      {
        if( bank == activeLeftKnobsBnk, { bank = 'active' });
        if( bank == 'active', { bankSet = activeLeftKnobsBnk }, { bankSet = bank });
        leftKnobsBankArray[bankSet][num] = func;
        if( bank == 'active', { this.setCCFunc(knobArray[num], func); });
      },
      { ^"not that many left knobs" }
    );
  }

  setRightKnobFunc { | num = 0, func, bank = 'active' |
    var bankSet;
    var knobArray = [3, 1, 0, 2];
    if ( num < 4,
      {
        if( bank == activeRightKnobsBnk, { bank = 'active' });
        if( bank == 'active', { bankSet = activeRightKnobsBnk }, { bankSet = bank });
        rightKnobsBankArray[bankSet][num] = func;
        if( bank == 'active', { this.setCCFunc(knobArray[num], func); });
      },
      { ^"not that many right knobs"; }
    );
  }

  setCrossfaderFunc { | func, bank = 'active' |
    var bankSet;
    if( bank == activeCrossfaderBnk, { bank = 'active' });
    if( bank == 'active', { bankSet = activeCrossfaderBnk }, { bankSet = bank });
    crossfaderBankArray[bankSet] = func;
    if( bank == 'active', { this.setCCFunc(24, func); });

  }

}