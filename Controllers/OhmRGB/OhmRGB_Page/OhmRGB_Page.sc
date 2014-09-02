/*
Sunday, January 19th 2014
prm
*/

OhmRGB_Page : OhmRGB {

  //var midiInPort, midiOutPort;
  var noteOnFuncArray, noteOffFuncArray, controlFuncArray, colorArray, animationArray;

  var leftSlidersBankArray, rightSlidersBankArray, leftKnobsBankArray, rightKnobsBankArray, crossfaderBankArray;
  var <activeLeftSlidersBank, <activeRightSlidersBank, <activeLeftKnobsBank, <activeRightKnobsBank, <activeCrossfaderBank;

  var <gridBankArray, <leftButtonsBankArray, <rightButtonsBankArray, <crossfaderButtonsBankArray, <controlButtonsBankArray;
  var <activeGridBank, <activeLeftButtonsBank, <activeRightButtonsBank, <activeCrossfaderButtonsBank, <activeControlButtonsBank;

  var <loadFunction, <offLoadFunction;

  *new {
    ^super.new.prInit;
  }

  prInit {
    //this.prInitMIDI;
    loadFunction = { };
    offLoadFunction = { };
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

   setLoadFunction { | func |
    loadFunction = func;
  }

  setOffLoadFunction { | func |
    offLoadFunction = func;
  }

  //////// Convenience Methods:

  setGridFunc { | column = 0, row = 0, func, type = \noteOn, bank = 'active' |
    var bankSet;
    var num = (column * 8) + row;
    if( bank == activeGridBank, { bank = 'active' });
    if( bank == 'active', { bankSet = activeGridBank }, { bankSet = bank });
    switch(type,
      { \noteOn }, { gridBankArray[bankSet][num][0] = func; if( bank == 'active', { this.setNoteOnFunc(num, func); }) },
      { \noteOff }, { gridBankArray[bankSet][num][1] = func; if( bank == 'active', { this.setNoteOffFunc(num, func); }) }
    );
  }

  setLeftButtonFunc { | num, func, type = \noteOn, bank = 'active' |
    var bankSet;
    var buttonArray = [65, 73, 66, 74];
    if( bank == activeLeftButtonsBank, { bank = 'active' });
    if( bank == 'active', { bankSet = activeLeftButtonsBank }, { bankSet = bank });
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

  setRightButtonFunc { | num, func, type = \noteOn, bank = 'active' |
    var bankSet;
    var buttonArray = [67, 75, 68, 76];
    if( bank == activeRightButtonsBank, { bank = 'active' });
    if( bank == 'active', { bankSet = activeRightButtonsBank }, { bankSet = bank });
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

  setCrossfaderButtonFunc { | num, func, type = \noteOn, bank = 'active' |
    var bankSet;
    var buttonArray = [64, 72];
    if( bank == activeCrossfaderButtonsBank, { bank = 'active' });
    if( bank == 'active', { bankSet = activeCrossfaderButtonsBank }, { bankSet = bank });
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

  setControlButtonFunc { | column = 0, row = 0, func, type = \noteOn, bank = 'active' |
    var bankSet;
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    var num = (column * 2) + row;
    if( bank == activeControlButtonsBank, { bank = 'active' });
    if( bank == 'active', { bankSet = activeControlButtonsBank }, { bankSet = bank });
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
        if( bank == activeLeftSlidersBank, { bank = 'active'});
        if( bank == 'active', { bankSet = activeLeftSlidersBank }, { bankSet = bank });
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
        if( bank == activeRightSlidersBank, { bank = 'active'});
        if( bank == 'active', { bankSet = activeRightSlidersBank }, { bankSet = bank });
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
        if( bank == activeLeftKnobsBank, { bank = 'active' });
        if( bank == 'active', { bankSet = activeLeftKnobsBank }, { bankSet = bank });
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
        if( bank == activeRightKnobsBank, { bank = 'active' });
        if( bank == 'active', { bankSet = activeRightKnobsBank }, { bankSet = bank });
        rightKnobsBankArray[bankSet][num] = func;
        if( bank == 'active', { this.setCCFunc(knobArray[num], func); });
      },
      { ^"not that many right knobs"; }
    );
  }

  setCrossfaderFunc { | func, bank = 'active' |
    var bankSet;
    if( bank == activeCrossfaderBank, { bank = 'active' });
    if( bank == 'active', { bankSet = activeCrossfaderBank }, { bankSet = bank });
    crossfaderBankArray[bankSet] = func;
    if( bank == 'active', { this.setCCFunc(24, func); });

  }

}