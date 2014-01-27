/*
Sunday, January 19th 2014
prm
*/

OhmRGB {

  var midiInPort, <midiOutPort;
  var noteOnFuncArray, noteOffFuncArray, controlFuncArray;
  var leftSlidersBankArray, rightSlidersBankArray;
  var leftKnobsBankArray, rightKnobsBankArray;
  var crossfaderBankArray;

  var activeLeftSlidersBank, activeRightSlidersBank, activeLeftKnobsBank, activeRightKnobsBank, activeCrossfaderBank;

  *new {
    ^super.new.prInit;
  }

  prInit {
    this.prInitMIDI;
    this.prMakeResponders;
    this.prMakeControlBanks;
  }

  prInitMIDI {
    MIDIIn.connectAll;
    midiInPort = MIDIIn.findPort("OhmRGB", "Controls");
    midiOutPort = MIDIOut.newByName("OhmRGB", "Controls");
    midiOutPort.latency = 0;
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
    noteOnFuncArray = Array.fill(80, { | index |
      MIDIFunc({ | val, num, chn, src | (midiInPort.device + "noteOn" + num + "has no function assigned").postln; },
        index, nil, \noteOn, midiInPort.uid).fix;
    });
    noteOnFuncArray = noteOnFuncArray.add(
      MIDIFunc({ | val, num, chn, src | (midiInPort.device + "noteOn" + num + "has no function assigned").postln; },
        87, nil, \noteOn, midiInPort.uid).fix;
    );

    noteOffFuncArray = Array.fill(80, { | index |
      MIDIFunc({ | val, num, chn, src |  }, index, nil, \noteOff, midiInPort.uid).fix;
    });
    noteOffFuncArray = noteOffFuncArray.add(MIDIFunc({ | val, num, chn, src | }, 87, nil, \noteOff, midiInPort.uid).fix;);
  }

  prFreeNoteResponders {
    noteOnFuncArray.do({ | func | func.free; });
  }

  prMakeControlResponders {
    controlFuncArray = Array.fill(25, { | index |
      MIDIFunc({ | val, num, chn, src | },
        index, nil, \control, midiInPort.uid).fix;
    });
  }

  prFreeControlResponders {
    controlFuncArray.do({ | func | func.free; });
  }

  ///////// Banks:

  prMakeControlBanks { | numBanks = 1 |
    leftSlidersBankArray = Array.fill(numBanks, { Array.fill(4, { nil; }); });
    rightSlidersBankArray = Array.fill(numBanks, { Array.fill(4, { nil; }); });
    leftKnobsBankArray = Array.fill(numBanks, { Array.fill(12, { nil; }); });
    rightKnobsBankArray = Array.fill(numBanks, { Array.fill(4, { nil; }); });
    crossfaderBankArray = Array.fill(numBanks, { { nil }; });

    activeLeftSlidersBank = 0;
    activeRightSlidersBank = 0;
    activeLeftKnobsBank = 0;
    activeRightKnobsBank = 0;
    activeCrossfaderBank = 0;
  }

  //////// Public Bank Functions:

  addControlBank { | type = \leftSliders |
    switch(type,
      { \leftSlider }, { leftSlidersBankArray.add(Array.fill(4, { nil; });); },
      { \rightSliders }, { rightSlidersBankArray.add(Array.fill(4, { nil; });); },
      { \leftKnobs }, { leftKnobsBankArray.add(Array.fill(12, { nil; });) },
      { \rightKnobs }, { rightKnobsBankArray.add(Array.fill(4, { nil; });) },
      { \crossfader }, { crossfaderBankArray.add( { nil } ); }
    );
  }

  activeControlBank { | type = \leftSliders |
    switch(type,
      { \leftSlider }, { ^activeLeftSlidersBank },
      { \rightSliders }, { ^activeRightSlidersBank },
      { \leftKnobs }, { ^activeLeftKnobsBank },
      { \rightKnobs }, { ^activeRightKnobsBank },
      { \crossfader }, { ^activeCrossfaderBank }
    );
  }

  setActiveLeftSlidersBank { | bank = 0 |
    var sliderArray = [23, 22, 15, 14];
    activeLeftSlidersBank = bank;
    sliderArray.do({ | item, index |
      this.setCCFunc(item, leftSlidersBankArray[activeLeftSlidersBank][index]);
    });
  }

  setActiveControlBank { | type = \leftSliders, bank = 0 |
    switch(type,
      { \leftSliders }, { this.setActiveLeftSlidersBank(bank); },
      { \rightSliders }, { activeRightSlidersBank = bank },
      { \leftKnobs }, { activeLeftKnobsBank = bank },
      { \rightKnobs }, { activeRightKnobsBank = bank },
      { \crossfader }, { activeCrossfaderBank = bank }
    );
  }

  setFunc { | num = 0, type = \noteOn, func = nil |
    switch(type,
      { \noteOn }, { noteOnFuncArray[num].prFunc_(func); },
      { \noteOff }, { noteOffFuncArray[num].prFunc_(func); },
      { \control }, { controlFuncArray[num].prFunc_(func); }
    );
  }

  clearFunc { | num = 0, type = \noteOn |
    switch(type,
      { \noteOn }, { noteOnFuncArray[num].prFunc_(
        { | val, num, chn, src | (midiInPort.device + "noteOn" + num + "has no function assigned").postln; }) },
      { \noteOff }, { noteOffFuncArray[num].prFunc_(
         { | val, num, chn, src | (midiInPort.device + "noteOn" + num + "has no function assigned").postln; }) },
      { \control }, { controlFuncArray[num].prFunc_({}); }
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

  setGridFunc { | column = 0, row = 0, func, type = \noteOn |
    var num = (column * 8) + row;
    switch(type,
      { \noteOn }, { this.setNoteOnFunc(num, func); },
      { \noteOff }, { this.setNoteOffFunc(num, func); }
    );
  }

  setLeftButtonFunc { | num, func, type = \noteOn |
    var buttonArray = [65, 73, 66, 74];
    if( num < 5,
      { switch(type,
        { \noteOn}, { this.setNoteOnFunc(buttonArray[num], func); },
        { \noteOff }, { this.setNoteOffFunc(buttonArray[num], func); }
        );
      },
      { ^"not that many left buttons"; }
    );
  }

  setRightButtonFunc { | num, func, type = \noteOn |
    var buttonArray = [67, 75, 68, 76];
    if( num < 5,
      { switch(type,
        { \noteOn }, { this.setNoteOnFunc(buttonArray[num], func); },
        { \noteOff }, { this.setNoteOffFunc(buttonArray[num], func); }
        );
      },
      { ^"not that many right buttons"; }
    );
  }

  setCrossfaderButton { | num, func, type = \noteOn |
    var buttonArray = [64, 72];
    if ( num < 3,
      { switch(type,
        { \noteOn }, { this.setNoteOnFunc(buttonArray[num], func); },
        { \noteOff }, { this.setNoteOffFunc(buttonArray[num], func); }
        );
      },
      { ^"not that many crossfader buttons"; }
    );
  }

  setControlButtonFunc { | column = 0, row = 0, func, type = \noteOn |
    var buttonArray = [69, 77, 70, 78, 71, 79];
    var num = (column * 2) + row;
    if( num < 6,
      { switch(type,
        { \noteOn }, { this.setNoteOnFunc(buttonArray[num], func); },
        { \noteOff }, { this.setNoteOffFunc(buttonArray[num], func); }
        );
      },
      { ^"not that many control buttons" }
    );
  }

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
        if( bank == 'active',
          {
            bankSet = activeLeftSlidersBank;
            leftSlidersBankArray[bankSet][num] = func;
            this.setCCFunc(sliderArray[num], func);
          },
          {
            bankSet = bank;
            leftSlidersBankArray[bankSet][num] = func;
          }
        );
      },
      { ^"not that many left sliders"; }
    );

  }

  setRightSliderFunc { | num = 0, func |
    var sliderArray = [5, 7, 6, 4];
    if( num < 4,
      { this.setCCFunc(sliderArray[num], func); },
      { ^"not that many right sliders";}
    );
  }

  //////// Knobs:

  setLeftKnobFunc { | column = 0, row = 0, func |
    var num = (column * 3) + row;
    var knobArray = [17, 19, 21, 16, 18, 20, 9, 11, 13, 8, 10, 12];
    if( num < 12,
      { this.setCCFunc(knobArray[num], func); },
      { ^"not that many right knobs" }
    );
  }

  setRightKnobFunc { | num = 0, func |
    var knobArray = [3, 1, 0, 2];
    if ( num < 4,
      { this.setCCFunc(knobArray[num], func); },
      { ^"not that many left knobs"; }
    );
  }

  setCrossfaderFunc { | func |
    this.setCCFunc(24, func);
  }


  //////// Color Functions:

  turnColor { | num, color = \off |
    switch(color,
      { \off }, { midiOutPort.noteOn(16, num, 0) },
      { \red }, { midiOutPort.noteOn(16, num, 16) },
      { \green }, { midiOutPort.noteOn(16, num, 127) },
      { \blue }, { midiOutPort.noteOn(16, num, 32) },
      { \yellow }, { midiOutPort.noteOn(16, num, 64) },
      { \purple }, { midiOutPort.noteOn(16, num, 8) },
      { \cyan }, { midiOutPort.noteOn(16, num, 4) },
      { \white }, { midiOutPort.noteOn(16, num, 1) },
    );
  }

  turnOff { | num |
    this.turnOff(num, \off);
  }

  turnRed { | num |
    this.turnColor(num, \red);
  }

  turnGreen { | num |
    this.turnColor(num, \green);
  }

  turnBlue { | num |
    this.turnColor(num, \blue);
  }

  turnYellow { | num |
    this.turnColor(num, \yellow);
  }

  turnPurple { | num |
    this.turnColor(num, \purple);
  }

  turnCyan { | num |
    this.turnColor(num, \cyan);
  }

  turnWhite { | num |
    this.turnColor(num, \white);
  }

  turnRandomColor { | num |
    var color;
    color = [\red, \green, \blue, \yellow, \purple, \cyan, \white].choose;
    this.turnColor(num, color);
  }

  turnGridColor { | column = 0, row = 0, color = \off |
    var num = (column * 8) + row;
    this.turnColor(num, color);
  }

  turnGridOff { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \off);
  }

  turnGridRed { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \red);
  }

  turnGridGreen { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \green);
  }

  turnGridBlue { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \blue);
  }

  turnGridYellow { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \yellow);
  }

  turnGridPurple { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \purple);
  }

  turnGridCyan { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \cyan);
  }

  turnGridWhite { | column = 0, row = 0 |
    var num = (column * 8) + row;
    this.turnColor(num, \white);
  }

  turnAllGrid { | color = \off |
   switch(color,
      { \off }, { 64.do({ | num | this.turnColor(num, \off)}); },
      { \red }, { 64.do({ | num | this.turnColor(num, \red)}); },
      { \green }, { 64.do({ | num | this.turnColor(num, \green)}); },
      { \blue }, { 64.do({ | num | this.turnColor(num, \blue)}); },
      { \yellow }, { 64.do({ | num | this.turnColor(num, \yellow)}); },
      { \purple }, { 64.do({ | num | this.turnColor(num, \purple)}); },
      { \cyan }, { 64.do({ | num | this.turnColor(num, \cyan)}); },
      { \white }, { 64.do({ | num | this.turnColor(num, \white)}); }
    );
  }

  turnGridRandom {
    var color = [\red, \green, \blue, \yellow, \purple, \cyan, \white].choose;
    this.turnGrid(color);
  }

  turnLeftButtonColor { | num = 0, color = \off |
    var buttonArray = [65, 73, 66, 74];
    this.turnColor(buttonArray[num], color);
  }

  turnLeftButtonOff { | num |
    this.turnLeftButtonColor(num, \off);
  }

  turnLeftButtonRed { | num |
    this.turnLeftButtonColor(num, \red);
  }

  turnLeftButtonGreen { | num |
    this.turnLeftButtonColor(num, \green);
  }

  turnLeftButtonBlue { | num |
    this.turnLeftButtonColor(num, \blue);
  }

  turnLeftButtonYellow { | num |
    this.turnLeftButtonColor(num, \yellow);
  }

  turnLeftButtonPurple { | num |
    this.turnLeftButtonColor(num, \purple);
  }

  turnLeftButtonCyan { | num |
    this.turnLeftButtonColor(num, \cyan);
  }

  turnLeftButtonWhite { | num |
    this.turnLeftButtonColor(num, \white);
  }

  turnAllLeftButtonsColor { | color = \off |
    var buttonArray = [65, 73, 66, 74];
    buttonArray.do({ | num | this.turnColor(num, color); });
  }

  turnAllLeftButtonsOff {
    this.turnAllLeftButtonsColor(\off);
  }

  turnAllLeftButtonsRed {
    this.turnAllLeftButtonsColor(\red);
  }

  turnAllLeftButtonsGreen {
    this.turnAllLeftButtonsColor(\green);
  }

  turnAllLeftButtonsBlue {
    this.turnAllLeftButtonsColor(\blue);
  }

  turnAllLeftButtonsYellow {
    this.turnAllLeftButtonsColor(\yellow);
  }

  turnAllLeftButtonsPurple {
    this.turnAllLeftButtonsColor(\purple);
  }

  turnAllLeftButtonsCyan {
    this.turnAllLeftButtonsColor(\cyan);
  }

  turnAllLeftButtonsWhite {
    this.turnAllLeftButtonsColor(\white);
  }

  turnRightButtonColor { | num = 0, color = \off |
    var buttonArray = [67, 75, 68, 76];
    this.turnColor(buttonArray[num], color);
  }

  turnRightButtonOff { | num |
    this.turnRightButtonColor(num, \off);
  }

  turnRightButtonRed { | num |
    this.turnRightButtonColor(num, \red);
  }

  turnRightButtonGreen { | num |
    this.turnRightButtonColor(num, \green);
  }

  turnRightButtonBlue { | num |
    this.turnRightButtonColor(num, \blue);
  }

  turnRightButtonYellow { | num |
    this.turnRightButtonColor(num, \yellow);
  }

  turnRightButtonPurple { | num |
    this.turnRightButtonColor(num, \purple);
  }

  turnRightButtonCyan { | num |
    this.turnRightButtonColor(num, \cyan);
  }

  turnRightButtonWhite { | num |
    this.turnRightButtonColor(num, \white);
  }

  turnAllRightButtonsColor { | color = \off |
    var buttonArray = [67, 75, 68, 76];
    buttonArray.do({ | num | this.turnColor(num, color); });
  }

  turnAllRightButtonsOff {
    this.turnAllRightButtonsColor(\off);
  }

  turnAllRightButtonsRed {
    this.turnAllRightButtonsColor(\red);
  }

  turnAllRightButtonsGreen {
    this.turnAllRightButtonsColor(\green);
  }

  turnAllRightButtonsBlue {
    this.turnAllRightButtonsColor(\blue);
  }

  turnAllRightButtonsYellow {
    this.turnAllRightButtonsColor(\yellow);
  }

  turnAllRightButtonsPurple {
    this.turnAllRightButtonsColor(\purple);
  }

  turnAllRightButtonsCyan {
    this.turnAllRightButtonsColor(\cyan);
  }

  turnAllRightButtonsWhite {
    this.turnAllRightButtonsColor(\white);
  }

  turnCrossfaderButton { | num = 0, color = \off |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], color);
  }

  turnCrossfaderButtonOff { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \off);
  }

  turnCrossfaderButtonRed { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \red);
  }

  turnCrossfaderButtonGreen { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \green);
  }

  turnCrossfaderButtonBlue { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \blue);
  }

  turnCrossfaderButtonYellow { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \yellow);
  }

  turnCrossfaderButtonPurple { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \purple);
  }

  turnCrossfaderButtonCyan { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \cyan);
  }

  turnCrossfaderButtonWhite { | num |
    var buttonArray = [64, 72];
    this.turnColor(buttonArray[num], \white);
  }

  turnAllCrossfaderButtonsColor { | color = \off |
    var buttonArray = [64, 72];
    buttonArray.do({ | index | this.turnColor(index, color); });
  }

  turnAllCrossfaderButtonsOff {
    this.turnAllCrossfaderButtonsColor(\off);
  }

  turnAllCrossfaderButtonsRed {
    this.turnAllCrossfaderButtonsColor(\red);
  }

  turnAllCrossfaderButtonsGreen {
    this.turnAllCrossfaderButtonsColor(\green);
  }

  turnAllCrossfaderButtonsBlue {
    this.turnAllCrossfaderButtonsColor(\blue);
  }

  turnAllCrossfaderButtonsYellow {
    this.turnAllCrossfaderButtonsColor(\yellow);
  }

  turnAllCrossfaderButtonsPurple {
    this.turnAllCrossfaderButtonsColor(\purple);
  }

  turnAllCrossfaderButtonsCyan {
    this.turnAllCrossfaderButtonsColor(\cyan);
  }

  turnAllCrossfaderButtonsWhite {
    this.turnAllCrossfaderButtonsColor(\white);
  }

  turnControlButtonColor { | num = 0, color = \off |
    var buttonArray = [69, 77, 70, 78, 71, 79];
    this.turnColor(buttonArray[num], color);
  }

  turnControlButtonOff { | num |
    this.turnControlButtonColor(num, \off);
  }

  turnControlButtonRed { | num |
    this.turnControlButtonColor(num, \red);
  }

  turnControlButtonGreen { | num |
    this.turnControlButtonColor(num, \green);
  }

  turnControlButtonBlue { | num |
    this.turnControlButtonColor(num, \blue);
  }

  turnControlButtonYellow { | num |
    this.turnControlButtonColor(num, \yellow);
  }

  turnControlButtonPurple { | num |
    this.turnControlButtonColor(num, \purple);
  }

  turnControlButtonCyan { | num |
    this.turnControlButtonColor(num, \cyan);
  }

  turnControlButtonWhite { | num |
    this.turnControlButtonColor(num, \white);
  }

  turnAllControlButtonsColor { | color = \off |
    var buttonArray = [69, 77, 70, 78, 71, 79];
    buttonArray.do({ | index | this.turnColor(index, color) });
  }

  turnAllControlButtonsOff {
    this.turnAllControlButtonsColor(\off);
  }

  turnAllControlButtonsRed {
    this.turnAllControlButtonsColor(\red);
  }

  turnAllControlButtonsGreen {
    this.turnAllControlButtonsColor(\green);
  }

  turnAllControlButtonsBlue {
    this.turnAllControlButtonsColor(\blue);
  }

  turnAllControlButtonsYellow {
    this.turnAllControlButtonsColor(\yellow);
  }

  turnAllControlButtonsPurple {
    this.turnAllControlButtonsColor(\purple);
  }

  turnAllControlButtonsCyan {
    this.turnAllControlButtonsColor(\cyan);
  }

  turnAllControlButtonsWhite {
    this.turnAllControlButtonsColor(\white);
  }

  turnMasterButtonColor { | color = \off |
    this.turnColor(87);
  }

  turnMasterButtonOff {
    this.turnMasterButtonColor(\off);
  }

  turnMasterButtonRed {
    this.turnMasterButtonColor(\red);
  }

  turnMasterButtonGreen {
    this.turnMasterButtonColor(\green);
  }

  turnMasterButtonBlue {
    this.turnMasterButtonColor(\blue);
  }

  turnMasterButtonYellow {
    this.turnMasterButtonColor(\yellow);
  }

  turnMasterButtonPurple {
    this.turnMasterButtonColor(\purple);
  }

  turnMasterButtonCyan {
    this.turnMasterButtonColor(\cyan);
  }

  turnMasterButtonWhite {
    this.turnMasterButtonColor(\white);
  }

  turnAllOff {
    // this.turnAllGridOff;
    this.turnAllGrid(\off);
    this.turnAllLeftButtonsOff;
    this.turnAllRightButtonsOff;
    this.turnAllCrossfaderButtonsOff;
    this.turnAllControlButtonsOff;
    this.turnMasterButtonOff;
  }

}