/*
Wednesday, November 7th
BeatstepPro_Functions.sc
prm
*/

+ BeatstepPro {

  //////////////////////////
  /////// Functions ///////
  ////////////////////////

  prSetAllSequencer1ButtonFuncs {
    84.do({ | func |
      this.prSetSequencer1ButtonFunc(func, 'noteOn');
      this.prSetSequencer1ButtonFunc(func, 'noteOff');
    });
  }
  prSetSequencer1ButtonFunc { | num = 24, type = 'noteOn' |
    var index = num-24;
    switch(type,
      { 'noteOn' },
      { sequencer1ButtonFuncArray[0][index].prFunc_(activePage.getSequencer1ButtonFunc(index, 'noteOn')) },
      { 'noteOff' },
      { sequencer1ButtonFuncArray[1][index].prFunc_(activePage.getSequencer1ButtonFunc(index, 'noteOff')) }
    );
  }
  setSequencer1Func { | num = 24, func, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setSequencer1Func(num, func, type, bank);
    this.prSetSequencer1ButtonFunc(num, type);
  }

  prSetAllSequencer2ButtonFuncs {
    84.do({ | func |
      this.prSetSequencer2ButtonFunc(func, 'noteOn');
      this.prSetSequencer2ButtonFunc(func, 'noteOff');
    });
  }
  prSetSequencer2ButtonFunc { | num = 24, type = 'noteOn' |
    var index = num-24;
    switch(type,
      { 'noteOn' },
      { sequencer2ButtonFuncArray[0][index].prFunc_(activePage.getSequencer2ButtonFunc(index, 'noteOn')) },
      { 'noteOff' },
      { sequencer2ButtonFuncArray[1][index].prFunc_(activePage.getSequencer2ButtonFunc(index, 'noteOff')) }
    );
  }
  setSequencer2Func { | num = 24, func, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setSequencer2Func(num, func, type, bank);
    this.prSetSequencer1ButtonFunc(num, type);
  }

  prSetAllDrumButtonFuncs {
    16.do({ | func |
      this.prSetDrumButtonFunc(func, 'noteOn');
      this.prSetDrumButtonFunc(func, 'noteOff');
    });
  }
  prSetDrumButtonFunc { | button = 1, type = 'noteOn' |
    var index = button -1;
    switch(type,
      { 'noteOn' },
      { drumButtonFuncArray[0][index].prFunc_(activePage.getDrumButtonFunc(index, 'noteOn')) },
      { 'noteOff' },
      { drumButtonFuncArray[1][index].prFunc_(activePage.getDrumButtonFunc(index, 'noteOff')) }
    );
  }
  setDrumFunc { | button = 1, func, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setDrumFunc(button, func, type, bank);
    this.prSetDrumButtonFunc(button, type);
  }

  prSetAllControlButtonFuncs {
    16.do({ | func |
      this.prSetControlButtonFunc(func, 'noteOn');
      this.prSetControlButtonFunc(func, 'noteOff');
    });
  }
  prSetControlButtonFunc { | button = 1, type = 'noteOn' |
    var num = button - 1;
    switch(type,
      { 'noteOn' },
      { controlButtonFuncArray[0][num].prFunc_(activePage.getControlButtonFunc(num, 'noteOn')) },
      { 'noteOff' },
      { controlButtonFuncArray[1][num].prFunc_(activePage.getControlButtonFunc(num, 'noteOff')) }
    );
  }
  setControlButtonFunc { | button = 1, func, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(button, func, type, bank);
    this.prSetControlButtonFunc(button, type);
  }

  prSetAllControlEncoderFuncs {
    16.do({ | func | this.prSetControlEncoderFunc(func) });
  }
  prSetControlEncoderFunc { | encoder = 1 |
    var num = encoder - 1;
    controlEncoderFuncArray[num].prFunc_(activePage.getControlEncoderFunc(num));
  }
  setControlEncoderFunc { | encoder = 1, func, bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlEncoderFunc(encoder, func, bank);
    this.prSetControlEncoderFunc(encoder);
  }

}
