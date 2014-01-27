/*
Sunday, January 19th 2014
prm
*/

OhmRGB {

  var midiInPort, <midiOutPort;
  var noteOnFuncArray, noteOffFuncArray, controlFuncArray;

  var leftSlidersBankArray, rightSlidersBankArray, leftKnobsBankArray, rightKnobsBankArray, crossfaderBankArray;
  var activeLeftSlidersBank, activeRightSlidersBank, activeLeftKnobsBank, activeRightKnobsBank, activeCrossfaderBank;

  var gridBankArray, leftButtonsBankArray, rightButtonsBankArray, crossfaderButtonsBankArray, controlButtonsBankArray;
  var activeGridBank, activeLeftButtonsBank, activeRightButtonsBank, activeCrossfaderButtonsBank, activeControlButtonsBank;


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

  //////// Convenience Methods:

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
    var buttonArray = [69, 77, 70, 78, 71, 79, 80];
    var num = (column * 2) + row;
    if( num < 7,
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

  setRightSliderFunc { | num = 0, func, bank = 'active' |
    var bankSet;
    var sliderArray = [5, 7, 6, 4];
    if( num < 4,
      {
        if( bank == 'active',
          {
            bankSet = activeRightSlidersBank;
            rightSlidersBankArray[bankSet][num] = func;
            this.setCCFunc(sliderArray[num], func);
          },
          {
            bankSet = bank;
            rightSlidersBankArray[bankSet][num] = func;
          }
        );
      },
      { ^"not that many right sliders";}
    );
  }

  //////// Knobs:

  setLeftKnobFunc { | column = 0, row = 0, func, bank = 'active' |
    var bankSet;
    var num = (column * 3) + row;
    var knobArray = [17, 19, 21, 16, 18, 20, 9, 11, 13, 8, 10, 12];
    if( num < 12,
      {
        if( bank == 'active',
          {
            bankSet = activeLeftKnobsBank;
            leftKnobsBankArray[bankSet][num] = func;
            this.setCCFunc(knobArray[num], func);
          },
          {
            bankSet = bank;
            leftKnobsBankArray[bankSet][num] = func;
          }
        );
      },
      { ^"not that many left knobs" }
    );
  }

  setRightKnobFunc { | num = 0, func, bank = 'active' |
    var bankSet;
    var knobArray = [3, 1, 0, 2];
    if ( num < 4,
      {
        if( bank == 'active',
          {
            bankSet = activeRightKnobsBank;
            rightKnobsBankArray[bankSet][num] = func;
            this.setCCFunc(knobArray[num], func);
          },
          {
            bankSet = bank;
            rightKnobsBankArray[bankSet][num] = func;
          }
        );
      },
      { ^"not that many right knobs"; }
    );
  }

  setCrossfaderFunc { | func, bank = 'active' |
    var bankSet;
    if ( bank == 'active',
      {
        bankSet = activeCrossfaderBank;
        crossfaderBankArray[bankSet] = func;
        this.setCCFunc(24, func);
      },
      {
        bankSet = bank;
        crossfaderBankArray[bankSet] = func;
      }
    );
  }

}