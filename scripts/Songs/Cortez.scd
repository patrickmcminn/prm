/*
Tuesday, June 30th 2015
Cortez.scd
Script for Cortez Sketch

*/

var page = 'cortez';
var controlButtonColorArray = ['blue', 'magenta', 'cyan', 'yellow', 'blue', 'magenta', 'cyan', 'white'];
var controlButtonResetFunc;
var mainBank = 0;
var masterBank = 7;

~base.makePage(page);
~base.addGridBanks(7, page);
~base.addFadersBanks(7, page);
~base.addTouchButtonsBanks(7, page);

///////////////////////////////////////////
//////// control button functions: ////////
///////////////////////////////////////////
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

// main bank:
~base.setControlButtonFunc(1, {
  ~base.setActiveGridBank(mainBank, page);
  ~base.setActiveTouchButtonsBank(mainBank, page);
  ~base.setActiveFadersBank(mainBank, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(1, 'left', page: page);
}, page: page);

// master bank:

~base.setControlButtonFunc(8, {
  ~base.setActiveGridBank(masterBank, page);
  ~base.setActiveFadersBank(masterBank, page);
  ~base.setActiveTouchButtonsBank(masterBank, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(8, 'left', page: page);
}, page: page);

///////////////////
// Main Bank: /////
/////////////////

~base.setFaderMode(1, 'magentaFill', bank: mainBank, page: page);
~base.setFaderMode(2, 'cyanFill', bank: mainBank, page: page);
~base.setFaderMode(3, 'yellowFill', bank: mainBank, page: page);

3.do({ | i | ~base.turnTouchButtonGreen(i+1, 'middle', bank: mainBank, page: page); });


//////// Sub y:

// Sub y Vol:
~base.setFaderFunc(1, { | val |
  y.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: mainBank, page: page);

// Sub y Mute:
~base.setTouchButtonFunc(1, {
  y.mixer.tglMute;
  if( y.mixer.isMuted, { ~base.turnTouchButtonRed(1) }, { ~base.turnTouchButtonGreen(1) });
}, bank: mainBank, page: page);


//////// Sub b:
// Sub b Vol:
~base.setFaderFunc(2, { | val |
  b.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: mainBank, page: page);

// Sub b Mute:
~base.setTouchButtonFunc(2, {
  b.mixer.tglMute;
  if( b.mixer.isMuted, { ~base.turnTouchButtonRed(2) }, { ~base.turnTouchButtonGreen(2) });
}, bank: mainBank, page: page);


//////// Sub c:
// Sub c Vol:
~base.setFaderFunc(3, { | val |
  c.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: mainBank, page: page);

// Sub c Mute:
~base.setTouchButtonFunc(3, {
  c.mixer.tglMute;
  if( c.mixer.isMuted, { ~base.turnTouchButtonRed(3) }, { ~base.turnTouchButtonGreen(3) });
}, bank: mainBank, page: page);


//////// Modular In:

// vol:
~base.setFaderMode(4, 'blueFill', bank: mainBank, page: page);
~base.setFaderFunc(4, { | val |
  ~prm.modular.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: mainBank, page: page);

// mute:
~base.turnTouchButtonGreen(4, bank: mainBank, page: page);
~base.setTouchButtonFunc(4, {
  ~prm.modular.tglMute;
  if( ~prm.modular.isMuted, { ~base.turnTouchButtonRed(4) }, { ~base.turnTouchButtonGreen(4) });
}, bank: mainBank, page: page);


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