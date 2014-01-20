/*
Sunday, January 19th 2014
prm
*/

OhmRGB {

  var midiInPort, midiOutPort;
  var noteFuncArray, controlFuncArray;

  *new {
    ^super.new.prInit;
  }

  prInit {
    this.prInitMIDI;
    this.prMakeResponders;

  }

  prInitMIDI {
    MIDIIn.connectAll;
    midiInPort = MIDIIn.findPort("OhmRGB", "Controls");
    midiOutPort = MIDIOut.findPort("OhmRGB", "Controls");
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
    noteFuncArray = Array.fill(80, { | index |
      MIDIFunc({ | val, num, chn, src | (midiInPort.device + "note" + num + "has no function assigned").postln; },
        index, nil, \noteOn, midiInPort.uid).fix;
    });
    noteFuncArray = noteFuncArray.add(
      MIDIFunc({ | val, num, chn, src | (midiInPort.device + "note" + num + "has no function assigned").postln; },
        87, nil, \noteOn, midiInPort.uid).fix;
    );
  }

  prFreeNoteResponders {
    noteFuncArray.do({ | func | func.free; });
  }

  prMakeControlResponders {
    controlFuncArray = Array.fill(25, { | index |
      MIDIFunc({ | val, num, chn, src | (midiInPort.device + "controller" + num + "has no function assigned").postln; },
        index, nil, \cc, midiInPort.uid).fix;
    });
  }

  prFreeControlResponders {
    controlFuncArray.do({ | func | func.free; });
  }

  setFunc { | num = 0, type = \noteOn, func = nil |
    switch(type,
      { \noteOn }, { noteFuncArray[num].prFunc_(func); },
      { \control }, { controlFuncArray[num].prFunc_(func); }
    );
  }

  setNoteFunc { | num, func |
    this.setFunc(num, \noteOn, func);
  }

  setCCFunc { | num, func |
    this.setFunc(num, \control, func);
  }

  setGridFunc { | column = 0, row = 0, func |
    var num = (column * 8) + row;
    this.setNoteFunc(num, func);
  }

  setLeftButtonFunc { | num, func |
    var buttonArray = [65, 73, 66, 74];
    if( num < 5,
      { this.setNoteFunc(buttonArray[num], func); },
      { ^"not that many left buttons"; }
    );
  }

  setRightButtonFunc { | num, func |
    var buttonArray = [67, 75, 68, 76];
    if( num < 5,
      { this.setNoteFunc(buttonArray[num], func); },
      { ^"not that many right buttons"; }
    );
  }

  setCrossfaderButton { | num, func |
    var buttonArray = [64, 72];
    if ( num < 3,
      { this.setNoteFunc(buttonArray[num], func); },
      { ^"not that many crossfader buttons"; }
    );
  }

  setControlButtonFunc { | column = 0, row = 0, func |
    var buttonArray = [69, 77, 70, 78, 71, 79];
    var num = (column * 2) + row;
    if( num < 6,
      { this.setNoteFunc(buttonArray[num], func); },
      { ^"not that many control buttons" }
    );
  }

  setMasterButtonFunc { | func |
    this.setNoteFunc(87, func);
  }

  setLeftSliderFunc { | num = 0, func |
    var sliderArray = [23, 22, 15, 14];
    if( num < 4,
      { this.setCCFunc(sliderArray[num], func); },
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
    case(color,
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

  turnGrid { | num = 0, color = \off |
   case(color,
      { \off }, { 63.do({ | num | this.turnColor(num, \off)}); },
      { \red }, { 63.do({ | num | this.turnColor(num, \red)}); },
      { \green }, { 63.do({ | num | this.turnColor(num, \green)}); },
      { \blue }, { 63.do({ | num | this.turnColor(num, \blue)}); },
      { \yellow }, { 63.do({ | num | this.turnColor(num, \yellow)}); },
      { \purple }, { 63.do({ | num | this.turnColor(num, \purple)}); },
      { \cyan }, { 63.do({ | num | this.turnColor(num, \cyan)}); },
      { \white }, { 63.do({ | num | this.turnColor(num, \white)}); }
    );
  }

}