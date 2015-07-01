/*
Tuesday, June 30th 2015
Cortez.scd
Script for Cortez Sketch

*/

var page = 'cortez';
var controlButtonColorArray = ['blue', 'magenta', 'cyan', 'yellow', 'blue', 'magenta', 'cyan', 'white'];
var controlButtonResetFunc;
var mainBank = 0;
var yBank = 1;
var bBank = 2;
var cBank = 3;
var masterBank = 7;


var modVolControlFunc;

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

// y bank:
~base.setControlButtonFunc(2, {
  ~base.setActiveGridBank(yBank, page);
  ~base.setActiveTouchButtonsBank(yBank, page);
  ~base.setActiveFadersBank(yBank, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(2, 'left', page: page);
}, page: page);

// b bank:
~base.setControlButtonFunc(3, {
  ~base.setActiveGridBank(bBank, page);
  ~base.setActiveTouchButtonsBank(bBank, page);
  ~base.setActiveFadersBank(bBank, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(3, 'left', page: page);
}, page: page);

// c bank:
~base.setControlButtonFunc(4, {
  ~base.setActiveGridBank(cBank, page);
  ~base.setActiveTouchButtonsBank(cBank, page);
  ~base.setActiveFadersBank(cBank, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(4, 'left', page: page);
}, page: page);

// master bank:
~base.setControlButtonFunc(8, {
  ~base.setActiveGridBank(masterBank, page);
  ~base.setActiveFadersBank(masterBank, page);
  ~base.setActiveTouchButtonsBank(masterBank, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(8, 'left', page: page);
}, page: page);



//////////////////////////////
/////////////////////////////
/////// Main Bank: //////////
///////////////////////////
/////////////////////////
~base.setFaderMode(1, 'magentaFill', bank: mainBank, page: page);
~base.setFaderMode(2, 'cyanFill', bank: mainBank, page: page);
~base.setFaderMode(3, 'yellowFill', bank: mainBank, page: page);

3.do({ | i | ~base.turnTouchButtonGreen(i+1, 'middle', bank: mainBank, page: page); });


//////// Sub y:

// Sub y Vol:
~base.setFaderFunc(1, { | val |
  y.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
  ~base.setFaderValue(1, val, bank: yBank);
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
  ~base.setFaderValue(1, val, bank: bBank);
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
  ~base.setFaderValue(1, val, bank: cBank);
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

//////// Modular Sends:

// reverb:
~base.setFaderMode(5, 'magentaFill', bank: mainBank, page: page);
~base.setFaderFunc(5, { | val |
  ~prm.modular.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(5, val);
}, bank: mainBank, page: page);

// granular delay:
~base.setFaderMode(6, 'magnetaFill', bank: mainBank, page: page);
~base.setFaderFunc(6, { | val |
  ~prm.modular.mixer.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(6, val);
}, bank: mainBank, page: page);

// modular (CAREFUL!):
~base.setFaderMode(7, 'magentaFill', bank: mainBank, page: page);
~base.setFaderFunc(7, { | val |
  ~prm.modular.mixer.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(7, val);
}, bank: mainBank, page: page);

////////////////////
///// GRID //////////
////////////////////


//////// Sequences:

// bassline 1:
~base.turnGridRed(0, 0, bank: mainBank, page: page);
~base.setGridFunc(0, 0, r {
  loop{
    y.playSequence(\cortez);
    ~base.turnGridGreen(0, 0).yield;

    y.stopSequence(\cortez);
    ~base.turnGridRed(0, 0).yield;
  };
}, bank: mainBank, page: page);

// reverb seq:
~base.turnGridRed(1, 0, bank: mainBank, page: page);
~base.setGridFunc(1, 0, r {
  loop {
    b.playSequence(\cortezHi);
    b.playSequence(\cortezHi2);
    ~base.turnGridGreen(1, 0).yield;

    b.stopSequence(\cortezHi);
    b.stopSequence(\cortezHi2);
    ~base.turnGridRed(1, 0).yield;
  };
}, bank: mainBank, page: page);

// phase seq:
~base.turnGridRed(2, 0, bank: mainBank, page: page);
~base.setGridFunc(2, 0, r {
  loop {
    c.playSequence(\cortezPhase, z);
    c.playSequence(\cortezPhase2, z);
    c.playSequence(\cortezPhase3, z);
    c.playSequence(\cortezPhase4, z);
    c.playSequence(\cortezPhase5, z);
    ~base.turnGridGreen(2, 0).yield;

    c.stopSequence(\cortezPhase);
    c.stopSequence(\cortezPhase2);
    c.stopSequence(\cortezPhase3);
    c.stopSequence(\cortezPhase4);
    c.stopSequence(\cortezPhase5);
    ~base.turnGridRed(2, 0).yield;
  }
}, bank: mainBank, page: page);


//////// Modular Volume Control:

~base.turnGridRed(0, 3, bank: mainBank, page: page);
~base.turnGridCyan(1, 3, bank: mainBank, page: page);

// Func On/Off:
~base.setGridFunc(0, 3, r {
  loop {
    ~base.setGridFunc(1, 3, { | val | modVolControlFunc.value(val); },
      'pressure', bank: mainBank, page: page);
    ~base.turnGridGreen(0, 3).yield;

    ~base.setGridFunc(1, 3, { }, 'pressure', bank: mainBank, page: page);
    ~base.turnGridRed(1, 3).yield;
  };
}, bank: mainBank, page: page);

// Volume Control via Touch Function:

modVolControlFunc = { | val |
  ~prm.modular.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val, bank: mainBank, page: page);
  if( val < 25, { ~base.turnGridCyan(1, 3); });
  if( (val > 24) && (val < 50), { ~base.turnGridBlue(1, 3); });
  if( (val > 49) && (val < 95), { ~base.turnGridMagenta(1, 3); });
  if( (val > 94) && (val < 115), { ~base.turnGridYellow(1, 3); });
  if( val > 114, { ~base.turnGridWhite(1, 3); });
};


//////////////////////////////
/////////////////////////////
// Synth Y Bank: ////////////
////////////////////////////
//////////////////////////////


//////// Faders:
~base.setFaderMode(1, 'magentaFill', bank: yBank, page: page);
~base.setFaderMode(2, 'cyanFill', bank: yBank, page: page);
~base.setFaderMode(3, 'whiteFill', bank: yBank, page: page);
~base.setFaderMode(4, 'yellowFill', bank: yBank, page: page);

// Synth Volume:
~base.setFaderFunc(1, { | val |
  y.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
  ~base.setFaderValue(1, val, bank: mainBank);
}, bank: yBank, page: page);
// Mute:
if( y.mixer.isMuted,
  { ~base.turnTouchButtonRed(1, bank: yBank, page: page); },
  { ~base.turnTouchButtonGreen(1, bank: yBank, page: page); });
~base.setTouchButtonFunc(1, {
  y.mixer.tglMute;
  if( y.mixer.isMuted, { ~base.turnTouchButtonRed(1); }, { ~base.turnTouchButtonGreen(1) });
}, bank: yBank, page: page);

// Reverb Send:
~base.setFaderFunc(2, { | val |
  y.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: yBank, page: page);

// Granulator Send:
~base.setFaderFunc(3, { | val |
  y.mixer.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: yBank, page: page);

// Modular Send:
~base.setFaderFunc(4, { | val |
  y.mixer.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: yBank, page: page);

//////// Grid:
~base.turnGridMagenta(0, 3, bank: yBank, page: page);
~base.setGridFunc(0, 3, r {
  loop {
    y.mixer.setSendPre;
    ~base.turnGridCyan(0, 3).yield;

    y.mixer.setSendPost;
    ~base.turnGridMagenta(0, 3).yield;
  };
}, bank: yBank, page: page);




//////////////////////////////
/////////////////////////////
// Synth B Bank: ////////////
////////////////////////////
//////////////////////////////

//////// Faders:
~base.setFaderMode(1, 'cyanFill', bank: bBank, page: page);
~base.setFaderMode(2, 'magentaFill', bank: bBank, page: page);
~base.setFaderMode(3, 'whiteFill', bank: bBank, page: page);
~base.setFaderMode(4, 'yellowFill', bank: bBank, page: page);

// Synth Volume:
~base.setFaderFunc(1, { | val |
  b.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
  ~base.setFaderValue(2, val, bank: mainBank);
}, bank: bBank, page: page);
// Mute:
if( b.mixer.isMuted,
  { ~base.turnTouchButtonRed(1, bank: bBank, page: page); },
  { ~base.turnTouchButtonGreen(1, bank: bBank, page: page); });
~base.setTouchButtonFunc(1, {
  b.mixer.tglMute;
  if( b.mixer.isMuted, { ~base.turnTouchButtonRed(1); }, { ~base.turnTouchButtonGreen(1) });
}, bank: bBank, page: page);

// Reverb Send:
~base.setFaderFunc(2, { | val |
  b.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: bBank, page: page);

// Granulator Send:
~base.setFaderFunc(3, { | val |
  b.mixer.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: bBank, page: page);

// Modular Send:
~base.setFaderFunc(4, { | val |
  b.mixer.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: bBank, page: page);

//////// Grid:
~base.turnGridMagenta(0, 3, bank: bBank, page: page);
~base.setGridFunc(0, 3, r {
  loop {
    b.mixer.setSendPre;
    ~base.turnGridCyan(0, 3).yield;

    b.mixer.setSendPost;
    ~base.turnGridMagenta(0, 3).yield;
  };
}, bank: bBank, page: page);




//////////////////////////////
/////////////////////////////
// Synth C Bank: ////////////
////////////////////////////
//////////////////////////////

//////// Faders:
~base.setFaderMode(1, 'yellowFill', bank: cBank, page: page);
~base.setFaderMode(2, 'magentaFill', bank: cBank, page: page);
~base.setFaderMode(3, 'whiteFill', bank: cBank, page: page);
~base.setFaderMode(4, 'cyanFill', bank: cBank, page: page);

// Synth Volume:
~base.setFaderFunc(1, { | val |
  c.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
  ~base.setFaderValue(3, val, bank: mainBank);
}, bank: cBank, page: page);
// Mute:
if( c.mixer.isMuted,
  { ~base.turnTouchButtonRed(1, bank: cBank, page: page); },
  { ~base.turnTouchButtonGreen(1, bank: cBank, page: page); });
~base.setTouchButtonFunc(1, {
  c.mixer.tglMute;
  if( c.mixer.isMuted, { ~base.turnTouchButtonRed(1); }, { ~base.turnTouchButtonGreen(1) });
}, bank: cBank, page: page);

// Reverb Send:
~base.setFaderFunc(2, { | val |
  c.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: cBank, page: page);

// Granulator Send:
~base.setFaderFunc(3, { | val |
  c.mixer.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: cBank, page: page);

// Modular Send:
~base.setFaderFunc(4, { | val |
  c.mixer.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: cBank, page: page);

//////// Grid:

// fader pre/post:
~base.turnGridMagenta(0, 3, bank: cBank, page: page);
~base.setGridFunc(0, 3, r {
  loop {
    c.mixer.setSendPre;
    ~base.turnGridCyan(0, 3).yield;

    c.mixer.setSendPost;
    ~base.turnGridMagenta(0, 3).yield;
  };
}, bank: cBank, page: page);

// sequences:
5.do({ | i | ~base.turnGridYellow(i, 0, bank: cBank, page: page); });

//1
~base.setGridFunc(0, 0, r {
  loop {
    c.playSequence(\cortezPhase, z);
    ~base.turnGridGreen(0, 0).yield;

    c.stopSequence(\cortezPhase, z);
    ~base.turnGridRed(0, 0).yield;
  };
}, bank: cBank, page: page);

//2
~base.setGridFunc(1, 0, r {
  loop {
    c.playSequence(\cortezPhase2, z);
    ~base.turnGridGreen(1, 0).yield;

    c.stopSequence(\cortezPhase2, z);
    ~base.turnGridRed(1, 0).yield;
  };
}, bank: cBank, page: page);

//3
~base.setGridFunc(2, 0, r {
  loop {
    c.playSequence(\cortezPhase3, z);
    ~base.turnGridGreen(2, 0).yield;

    c.stopSequence(\cortezPhase3, z);
    ~base.turnGridRed(2, 0).yield;
  };
}, bank: cBank, page: page);

//4
~base.setGridFunc(3, 0, r {
  loop {
    c.playSequence(\cortezPhase4, z);
    ~base.turnGridGreen(3, 0).yield;

    c.stopSequence(\cortezPhase4, z);
    ~base.turnGridRed(3, 0).yield;
  };
}, bank: cBank, page: page);

//5
~base.setGridFunc(4, 0, r {
  loop {
    c.playSequence(\cortezPhase5, z);
    ~base.turnGridGreen(4, 0).yield;

    c.stopSequence(\cortezPhase5, z);
    ~base.turnGridRed(4, 0).yield;
  };
}, bank: cBank, page: page);








//////////////////////////////
/////////////////////////////
// Master Bank: ////////////
////////////////////////////
//////////////////////////////


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



3.do({ | i | ~base.setFaderMode(i + 1, 'magentaFill', bank: masterBank, page: page); });
//////// Reverb Volume:
~base.setFaderFunc(1, { | val |
  ~prm.reverb.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: masterBank, page: page);

//////// Granulator Volume:
~base.setFaderFunc(2, { | val |
  ~prm.granulator.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: masterBank, page: page);

//////// modular send volume:
~base.setFaderFunc(3, { | val |
  ~prm.modularSend.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: masterBank, page: page);

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