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
      this.prSetSequencer1ButtonFunc(func, 'noteOn');
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
      this.prSetSequencer2ButtonFunc(func, 'noteOn');
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

  prSetAllControlButtonFuncs {
    16.do({ | func |
      this.prSetControlButtonFunc(func, 'noteOn');
      this.prSetControlButtonFunc(func, 'noteOff');
    });
  }
  prSetControlButtonFunc { | num = 0, type = 'noteOn' |
    switch(type,
      { 'noteOn' },
      { controlButtonFuncArray[0][num].prFunc_(activePage.getControlButtonFunc(num, 'noteOn')) },
      { 'noteOff' },
      { controlButtonFuncArray[1][num].prFunc_(activePage.getControlButtonFunc(num, 'noteOff')) }
    );
  }
  setControlButtonFunc { | num = 0, func, type = 'noteOn', bank = 'active', page = 'active' |
    if( page == 'active', { page = activePageKey });
    pageDict[page].setControlButtonFunc(num, func, type, bank);
    this.prSetControlButtonFunc(num, type);
  }

  prSetAllControlEncoderFuncs {
    16.do({ | func | this.prSetControlEncoderFunc(func) });
  }
  prSetControlEncoderFunc { |num = 0, func |

  }

}
