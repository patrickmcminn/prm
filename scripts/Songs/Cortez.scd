/*
Tuesday, June 30th 2015
Cortez.scd
Script for Cortez Sketch

*/

var page = 'cortez';
var controlButtonColorArray = ['blue', 'magenta', 'cyan', 'yellow', 'blue', 'magenta', 'cyan', 'white'];
var controlButtonResetFunc;
var masterBank = 7;

~base.makePage(page);
~base.addGridBanks(7, page);
~base.addFadersBanks(7, page);
~base.addTouchButtonsBanks(7, page);


//////// control button functions:
controlButtonResetFunc = {
  controlButtonColorArray.do({ | color, i |
    ~base.turnControlButtonColor(i+1, 'left', 'off', page: page);
    ~base.turnControlButtonColor(i+1, 'right', color, page: page);
  });
};

// initial state:
controlButtonResetFunc.value;
~base.turnControlButtonGreen(1, 'left', page: page);

// functions:

// "master" control:

~base.setControlButtonFunc(8, {
  ~base.setActiveGridBank(masterBank, page);
  ~base.setActiveFadersBank(masterBank, page);
  ~base.setActiveTouchButtonsBank(masterBank, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(8, 'left', page: page);
}, page: page);



///////////////////
// Master Bank: //
//////////////////

3.do({ | i | ~base.setFaderMode(i + 6, 'greenFill', bank: masterBank, page: page); });
if( ~prm.submixerA.mixer.isMuted,
  { ~base.turnTouchButtonRed(6, bank: masterBank, page: page); },
  { ~base.turnTouchButtonGreen(6, bank: masterBank, page: page); }
);
if( ~prm.submixerB.mixer.isMuted,
  { ~base.turnTouchButtonRed(7, bank: masterBank, page: page); },
  { ~base.turnTouchButtonGreen(7, bank: masterBank, page: page); }
);
if( ~prm.submixerC.mixer.isMuted,
  { ~base.turnTouchButtonRed(8, bank: masterBank, page: page); },
  { ~base.turnTouchButtonGreen(8, bank: masterBank, page: page); }
);



//////// Submix A:

// volume:
~base.setFaderFunc(6, { | val |
  ~prm.submixerA.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(6, val);
}, bank: masterBank, page: page);
// mute:
~base.setTouchButtonFunc(6, {
  ~prm.submixerA.mixer.tglMute;
  if( ~prm.submixerA.mixer.isMuted,
  { ~base.turnTouchButtonRed(6, bank: masterBank, page: page); },
  { ~base.turnTouchButtonGreen(6, bank: masterBank, page: page); }
  );
}, bank: masterBank, page: page);

//////// Submix B:

// volume:
~base.setFaderFunc(7, { | val |
  ~prm.submixerB.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(7, val);
}, bank: masterBank, page: page);
// mute:
~base.setTouchButtonFunc(7, {
  ~prm.submixerB.mixer.tglMute;
  if( ~prm.submixerB.mixer.isMuted,
  { ~base.turnTouchButtonRed(7, bank: masterBank, page: page); },
  { ~base.turnTouchButtonGreen(7, bank: masterBank, page: page); }
  );
}, bank: masterBank, page: page);

//////// Submix C:

// volume:
~base.setFaderFunc(8, { | val |
  ~prm.submixerC.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(8, val);
}, bank: masterBank, page: page);
// mute:
~base.setTouchButtonFunc(8, {
  ~prm.submixerC.mixer.tglMute;
  if( ~prm.submixerC.mixer.isMuted,
  { ~base.turnTouchButtonRed(8, bank: masterBank, page: page); },
  { ~base.turnTouchButtonGreen(8, bank: masterBank, page: page); }
  );
}, bank: masterBank, page: page);