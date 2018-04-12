IM_NanoKontrolStrip {
  var myKnobNum, myFaderNum, myTopBtnNum, myBtmBtnNum;
  var knobFunc, faderFunc, topBtnOnFunc, topBtnOffFunc, btmBtnOnFunc, btmBtnOffFunc;

  *new { |faderNum, knobNum, btmBtnNum, topBtnNum|
    ^super.new.prInit(faderNum, knobNum, btmBtnNum, topBtnNum);
  }

  prInit { |faderNum, knobNum, btmBtnNum, topBtnNum|
    myKnobNum = knobNum;
    myFaderNum = faderNum;
    myTopBtnNum = topBtnNum;
    myBtmBtnNum = btmBtnNum;

    this.prMakeMIDIResponders;
  }

  prMakeMIDIResponders {
    var midiPort;

    MIDIIn.connectAll;  // will this overwrite previous connections?
    midiPort = MIDIIn.findPort("nanoKONTROL", "SLIDER/KNOB");

    knobFunc = MIDIFunc( { |val, num, chan, src|
      ("nanoKontrol cc" + num + "has no function assigned.").postln;
    }, myKnobNum, nil, \control, midiPort.uid).fix;

    faderFunc = MIDIFunc( { |val, num, chan, src|
      ("nanoKontrol cc" + num + "has no function assigned.").postln;
    }, myFaderNum, nil, \control, midiPort.uid).fix;

    topBtnOnFunc = MIDIFunc( { |val, num, chan, src|
      ("nanoKontrol note on" + num + "has no function assigned.").postln;
    }, myTopBtnNum, nil, \noteOn, midiPort.uid).fix;

    topBtnOffFunc = MIDIFunc( { |val, num, chan, src|
      ("nanoKontrol note off" + num + "has no function assigned.").postln;
    }, myTopBtnNum, nil, \noteOff, midiPort.uid).fix;

    btmBtnOnFunc = MIDIFunc( { |val, num, chan, src|
      ("nanoKontrol note on" + num + "has no function assigned.").postln;
    }, myBtmBtnNum, nil, \noteOn, midiPort.uid).fix;

    btmBtnOffFunc = MIDIFunc( { |val, num, chan, src|
      ("nanoKontrol noteOff" + num + "has no function assigned.").postln;
    }, myBtmBtnNum, nil, \noteOff, midiPort.uid).fix;
  }

  knobFunc_ { |func| knobFunc.prFunc_(func) }
  faderFunc_ { |func| faderFunc.prFunc_(func) }
  topBtnOnFunc_ { |func| topBtnOnFunc.prFunc_(func) }
  topBtnOffFunc_ { |func| topBtnOffFunc.prFunc_(func) }
  btmBtnOnFunc_ { |func| btmBtnOnFunc.prFunc_(func) }
  btmBtnOffFunc_ { |func| btmBtnOffFunc.prFunc_(func) }

  resetKnobFunc {
    this.knobFunc_({ |val, num, chan, src|
      ("nanoKontrol cc" + num + "has no function assigned.").postln;
    });
  }

  resetFaderFunc {
    this.faderFunc_({ |val, num, chan, src|
      ("nanoKontrol cc" + num + "has no function assigned.").postln;
    });
  }

  resetTopBtnOnFunc {
    this.topBtnOnFunc_({ |val, num, chan, src|
      ("nanoKontrol note on" + num + "has no function assigned.").postln;
    });
  }

  resetTopBtnOffFunc {
    this.topBtnOffFunc_({ |val, num, chan, src|
      ("nanoKontrol note off" + num + "has no function assigned.").postln;
    });
  }

  resetTopBtnFuncs { this.resetTopBtnOnFunc; this.resetBtmBtnOffFunc; }

  resetBtmBtnOnFunc {
    this.btmBtnOnFunc_({ |val, num, chan, src|
      ("nanoKontrol note on" + num + "has no function assigned.").postln;
    });
  }

  resetBtmBtnOffFunc {
    this.btmBtnOffFunc_({ |val, num, chan, src|
      ("nanoKontrol note off" + num + "has no function assigned.").postln;
    });
  }

  resetBtmBtnFuncs { this.resetBtmBtnOnFunc; this.resetBtmBtnOffFunc; }

  resetAllFuncs {
    this.resetKnobFunc;
    this.resetFaderFunc;
    this.resetTopBtnFuncs;
    this.resetBtmBtnFuncs;
  }

  free {
    knobFunc.free;
    knobFunc = nil;
    myKnobNum = nil;

    faderFunc.free;
    faderFunc = nil;
    myFaderNum = nil;

    topBtnOnFunc.free;
    topBtnOnFunc = nil;

    topBtnOffFunc.free;
    topBtnOffFunc = nil;
    myTopBtnNum = nil;

    btmBtnOnFunc.free;
    btmBtnOnFunc = nil;

    btmBtnOffFunc.free;
    btmBtnOffFunc = nil;
    myBtmBtnNum = nil;
 }
}