/*
Thursday, July 9th 2015
Cortez Base.scd
prm
*/

var page = 'cortez';
var controlButtonColorArray = ['blue', 'magenta', 'cyan', 'yellow', 'blue', 'magenta', 'cyan', 'white'];
var controlButtonResetFunc;
var mainBank = 0;
var yBank = 1;
var bBank = 2;
var cBank = 3;
var performanceBank = 4;
var bellsAndBass = 5;
var masterBank = 7;
var seqVisRoutine;

var bellsNoteArray = [54, 56, 57, 59, 61, 62, 64, 66.01];
var bassNoteArray = [30, 32, 33, 35, 37, 38, 40, 42.01];

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

// ~cortez.chordsSynth bank:
~base.setControlButtonFunc(2, {
  ~base.setActiveGridBank(yBank, page);
  ~base.setActiveTouchButtonsBank(yBank, page);
  ~base.setActiveFadersBank(yBank, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(2, 'left', page: page);
}, page: page);

// ~cortez.washSynth bank:
~base.setControlButtonFunc(3, {
  ~base.setActiveGridBank(bBank, page);
  ~base.setActiveTouchButtonsBank(bBank, page);
  ~base.setActiveFadersBank(bBank, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(3, 'left', page: page);
}, page: page);

// ~cortez.phaseSynth bank:
~base.setControlButtonFunc(4, {
  ~base.setActiveGridBank(cBank, page);
  ~base.setActiveTouchButtonsBank(cBank, page);
  ~base.setActiveFadersBank(cBank, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(4, 'left', page: page);
}, page: page);

// performanceBank:
~base.setControlButtonFunc(5, {
  ~base.setActiveGridBank(performanceBank, page);
  ~base.setActiveTouchButtonsBank(performanceBank, page);
  ~base.setActiveFadersBank(performanceBank, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(5, 'left', page: page);
}, page: page);

// Bells and Bass:
~base.setControlButtonFunc(6, {
  ~base.setActiveGridBank(bellsAndBass, page);
  ~base.setActiveTouchButtonsBank(bellsAndBass, page);
  ~base.setActiveFadersBank(bellsAndBass, page);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(6, 'left', page: page);
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


//////// Sub ~cortez.chordsSynth:

// Sub ~cortez.chordsSynth Vol:
~base.setFaderFunc(1, { | val |
  ~cortez.chordsSynth.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
  ~base.setFaderValue(1, val, bank: yBank);
}, bank: mainBank, page: page);

// Sub ~cortez.chordsSynth Mute:
~base.setTouchButtonFunc(1, {
  ~cortez.chordsSynth.mixer.tglMute;
  if( ~cortez.chordsSynth.mixer.isMuted, { ~base.turnTouchButtonRed(1) }, { ~base.turnTouchButtonGreen(1) });
}, bank: mainBank, page: page);


//////// Sub ~cortez.washSynth:
// Sub ~cortez.washSynth Vol:
~base.setFaderFunc(2, { | val |
  ~cortez.washSynth.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
  ~base.setFaderValue(1, val, bank: bBank);
}, bank: mainBank, page: page);

// Sub ~cortez.washSynth Mute:
~base.setTouchButtonFunc(2, {
  ~cortez.washSynth.mixer.tglMute;
  if( ~cortez.washSynth.mixer.isMuted, { ~base.turnTouchButtonRed(2) }, { ~base.turnTouchButtonGreen(2) });
}, bank: mainBank, page: page);


//////// Sub ~cortez.phaseSynth:
// Sub ~cortez.phaseSynth Vol:
~base.setFaderFunc(3, { | val |
  ~cortez.phaseSynth.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
  ~base.setFaderValue(1, val, bank: cBank);
}, bank: mainBank, page: page);

// Sub ~cortez.phaseSynth Mute:
~base.setTouchButtonFunc(3, {
  ~cortez.phaseSynth.mixer.tglMute;
  if( ~cortez.phaseSynth.mixer.isMuted, { ~base.turnTouchButtonRed(3) }, { ~base.turnTouchButtonGreen(3) });
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
  ~prm.modular.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(5, val);
}, bank: mainBank, page: page);

// granular delay:
~base.setFaderMode(6, 'magnetaFill', bank: mainBank, page: page);
~base.setFaderFunc(6, { | val |
  ~prm.modular.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(6, val);
}, bank: mainBank, page: page);

// modular (CAREFUL!):
~base.setFaderMode(7, 'magentaFill', bank: mainBank, page: page);
~base.setFaderFunc(7, { | val |
  ~prm.modular.setSendVol(2, val.ccdbfs(-70, 0));
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
    ~cortez.chordsSynth.playSequence(\cortez);
    ~base.turnGridGreen(0, 0).yield;

    ~cortez.chordsSynth.stopSequence(\cortez);
    ~base.turnGridRed(0, 0).yield;
  };
}, bank: mainBank, page: page);

// reverb seq:
~base.turnGridRed(1, 0, bank: mainBank, page: page);
~base.setGridFunc(1, 0, r {
  loop {
    ~cortez.washSynth.playSequence(\cortezHi);
    ~cortez.washSynth.playSequence(\cortezHi2);
    ~base.turnGridGreen(1, 0).yield;

    ~cortez.washSynth.stopSequence(\cortezHi);
    ~cortez.washSynth.stopSequence(\cortezHi2);
    ~base.turnGridRed(1, 0).yield;
  };
}, bank: mainBank, page: page);

// phase seq:
~base.turnGridRed(2, 0, bank: mainBank, page: page);
~base.setGridFunc(2, 0, r {
  loop {
    ~cortez.phaseSynth.playSequence(\cortezPhase, ~cortez.clock);
    ~cortez.phaseSynth.playSequence(\cortezPhase2, ~cortez.clock);
    ~cortez.phaseSynth.playSequence(\cortezPhase3, ~cortez.clock);
    ~cortez.phaseSynth.playSequence(\cortezPhase4, ~cortez.clock);
    ~cortez.phaseSynth.playSequence(\cortezPhase5, ~cortez.clock);
    ~base.turnGridGreen(2, 0).yield;

    ~cortez.phaseSynth.stopSequence(\cortezPhase);
    ~cortez.phaseSynth.stopSequence(\cortezPhase2);
    ~cortez.phaseSynth.stopSequence(\cortezPhase3);
    ~cortez.phaseSynth.stopSequence(\cortezPhase4);
    ~cortez.phaseSynth.stopSequence(\cortezPhase5);
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

// Shifter:

// colors:
4.do({ | i | ~base.turnGridMagenta(i + 4, 0, bank: mainBank, page: page); });
4.do({ | i | ~base.turnGridMagenta(i + 4, 1, bank: mainBank, page: page); });
4.do({ | i | ~base.turnGridMagenta(i + 4, 2, bank: mainBank, page: page); });
4.do({ | i | ~base.turnGridMagenta(i + 4, 3, bank: mainBank, page: page); });

//  Octave Down:
~base.setGridFunc(4, 3, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downOctave', -12, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(4, 3);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(4, 3, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downOctave');
  ~base.turnGridMagenta(4, 3);
}, 'noteOff', bank: mainBank, page: page);



//  Seventh Down:
~base.setGridFunc(5, 3, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downSeventh', -10, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(5, 3);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(5, 3, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downSeventh');
  ~base.turnGridMagenta(5, 3);
}, 'noteOff', bank: mainBank, page: page);


// fifth Down:
~base.setGridFunc(6, 3, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downFifth', -7, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(6, 3);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(6, 3, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downFifth');
  ~base.turnGridMagenta(6, 3);
}, 'noteOff', bank: mainBank, page: page);


// fourth down:
~base.setGridFunc(7, 3, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downFourth', -5, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(7, 3);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(7, 3, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downFourth');
  ~base.turnGridMagenta(7, 3);
}, 'noteOff', bank: mainBank, page: page);

// third down:
~base.setGridFunc(4, 2, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downThird', -4, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(4, 2);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(4, 2, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downThird');
  ~base.turnGridMagenta(4, 2);
}, 'noteOff', bank: mainBank, page: page);


// second down:
~base.setGridFunc(5, 2, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downSecond', -2, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(5, 2);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(5, 2, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downSecond');
  ~base.turnGridMagenta(5, 2);
}, 'noteOff', bank: mainBank, page: page);


// root:
~base.setGridFunc(6, 2, { | val |
  ~cortez.shiftInput.makeSynthSustaining('root', 0, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(6, 2);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(6, 2, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('root');
  ~base.turnGridMagenta(6, 2);
}, 'noteOff', bank: mainBank, page: page);

// second:
~base.setGridFunc(7, 2, { | val |
  ~cortez.shiftInput.makeSynthSustaining('second', 2, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(7, 2);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(7, 2, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('second');
  ~base.turnGridMagenta(7, 2);
}, 'noteOff', bank: mainBank, page: page);

// third:
~base.setGridFunc(4, 1, { | val |
  ~cortez.shiftInput.makeSynthSustaining('third', 3, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(4, 1);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(4, 1, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('third');
  ~base.turnGridMagenta(4, 1);
}, 'noteOff', bank: mainBank, page: page);

// fourth:
~base.setGridFunc(5, 1, { | val |
  ~cortez.shiftInput.makeSynthSustaining('fourth', 5, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(5, 1);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(5, 1, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('fourth');
  ~base.turnGridMagenta(5, 1);
}, 'noteOff', bank: mainBank, page: page);


// fifth:
~base.setGridFunc(6, 1, { | val |
  ~cortez.shiftInput.makeSynthSustaining('fifth', 7, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(6, 1);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(6, 1, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('fifth');
  ~base.turnGridMagenta(6, 1);
}, 'noteOff', bank: mainBank, page: page);

// sixth:
~base.setGridFunc(7, 1, { | val |
  ~cortez.shiftInput.makeSynthSustaining('sixth', 8, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(7, 1);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(7, 1, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('sixth');
  ~base.turnGridMagenta(7, 1);
}, 'noteOff', bank: mainBank, page: page);

// seventh:
~base.setGridFunc(4, 0, { | val |
  ~cortez.shiftInput.makeSynthSustaining('seventh', 10, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(4, 0);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(4, 0, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('seventh');
  ~base.turnGridMagenta(4, 0);
}, 'noteOff', bank: mainBank, page: page);

// octave:
~base.setGridFunc(5, 0, { | val |
  ~cortez.shiftInput.makeSynthSustaining('oct', 12, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(5, 0);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(5, 0, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('oct');
  ~base.turnGridMagenta(5, 0);
}, 'noteOff', bank: mainBank, page: page);

// 9th:
~base.setGridFunc(6, 0, { | val |
  ~cortez.shiftInput.makeSynthSustaining('ninth', 14, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(6, 0);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(6, 0, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('ninth');
  ~base.turnGridMagenta(6, 0);
}, 'noteOff', bank: mainBank, page: page);


// 10th:
~base.setGridFunc(7, 0, { | val |
  ~cortez.shiftInput.makeSynthSustaining('tenth', 15, val.ccdbfs(-12, 3), 16000);
  ~base.turnGridGreen(7, 0);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(7, 0, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('tenth');
  ~base.turnGridMagenta(7, 0);
}, 'noteOff', bank: mainBank, page: page);



//////////////////////////////
/////////////////////////////
// Synth ~cortez.chordsSynth Bank: ////////////
////////////////////////////
//////////////////////////////


//////// Faders:
~base.setFaderMode(1, 'magentaFill', bank: yBank, page: page);
~base.setFaderMode(2, 'cyanFill', bank: yBank, page: page);
~base.setFaderMode(3, 'whiteFill', bank: yBank, page: page);
~base.setFaderMode(4, 'yellowFill', bank: yBank, page: page);

// Synth Volume:
~base.setFaderFunc(1, { | val |
  ~cortez.chordsSynth.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
  ~base.setFaderValue(1, val, bank: mainBank);
}, bank: yBank, page: page);
// Mute:
if( ~cortez.chordsSynth.mixer.isMuted,
  { ~base.turnTouchButtonRed(1, bank: yBank, page: page); },
  { ~base.turnTouchButtonGreen(1, bank: yBank, page: page); });
~base.setTouchButtonFunc(1, {
  ~cortez.chordsSynth.mixer.tglMute;
  if( ~cortez.chordsSynth.mixer.isMuted, { ~base.turnTouchButtonRed(1); }, { ~base.turnTouchButtonGreen(1) });
}, bank: yBank, page: page);

// Reverb Send:
~base.setFaderFunc(2, { | val |
  ~cortez.chordsSynth.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: yBank, page: page);

// Granulator Send:
~base.setFaderFunc(3, { | val |
  ~cortez.chordsSynth.mixer.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: yBank, page: page);

// Modular Send:
~base.setFaderFunc(4, { | val |
  ~cortez.chordsSynth.mixer.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: yBank, page: page);

//////// Grid:
~base.turnGridMagenta(0, 3, bank: yBank, page: page);
~base.setGridFunc(0, 3, r {
  loop {
    ~cortez.chordsSynth.mixer.setSendPre;
    ~base.turnGridCyan(0, 3).yield;

    ~cortez.chordsSynth.mixer.setSendPost;
    ~base.turnGridMagenta(0, 3).yield;
  };
}, bank: yBank, page: page);




//////////////////////////////
/////////////////////////////
// Synth ~cortez.washSynth Bank: ////////////
////////////////////////////
//////////////////////////////

//////// Faders:
~base.setFaderMode(1, 'cyanFill', bank: bBank, page: page);
~base.setFaderMode(2, 'magentaFill', bank: bBank, page: page);
~base.setFaderMode(3, 'whiteFill', bank: bBank, page: page);
~base.setFaderMode(4, 'yellowFill', bank: bBank, page: page);

// Synth Volume:
~base.setFaderFunc(1, { | val |
  ~cortez.washSynth.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
  ~base.setFaderValue(2, val, bank: mainBank);
}, bank: bBank, page: page);
// Mute:
if( ~cortez.washSynth.mixer.isMuted,
  { ~base.turnTouchButtonRed(1, bank: bBank, page: page); },
  { ~base.turnTouchButtonGreen(1, bank: bBank, page: page); });
~base.setTouchButtonFunc(1, {
  ~cortez.washSynth.mixer.tglMute;
  if( ~cortez.washSynth.mixer.isMuted, { ~base.turnTouchButtonRed(1); }, { ~base.turnTouchButtonGreen(1) });
}, bank: bBank, page: page);

// Reverb Send:
~base.setFaderFunc(2, { | val |
  ~cortez.washSynth.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: bBank, page: page);

// Granulator Send:
~base.setFaderFunc(3, { | val |
  ~cortez.washSynth.mixer.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: bBank, page: page);

// Modular Send:
~base.setFaderFunc(4, { | val |
  ~cortez.washSynth.mixer.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: bBank, page: page);

//////// Grid:
~base.turnGridMagenta(0, 3, bank: bBank, page: page);
~base.setGridFunc(0, 3, r {
  loop {
    ~cortez.washSynth.mixer.setSendPre;
    ~base.turnGridCyan(0, 3).yield;

    ~cortez.washSynth.mixer.setSendPost;
    ~base.turnGridMagenta(0, 3).yield;
  };
}, bank: bBank, page: page);




//////////////////////////////
/////////////////////////////
// Synth ~cortez.phaseSynth Bank: ////////////
////////////////////////////
//////////////////////////////

//////// Faders:
~base.setFaderMode(1, 'yellowFill', bank: cBank, page: page);
~base.setFaderMode(2, 'magentaFill', bank: cBank, page: page);
~base.setFaderMode(3, 'whiteFill', bank: cBank, page: page);
~base.setFaderMode(4, 'cyanFill', bank: cBank, page: page);

// Synth Volume:
~base.setFaderFunc(1, { | val |
  ~cortez.phaseSynth.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
  ~base.setFaderValue(3, val, bank: mainBank);
}, bank: cBank, page: page);
// Mute:
if( ~cortez.phaseSynth.mixer.isMuted,
  { ~base.turnTouchButtonRed(1, bank: cBank, page: page); },
  { ~base.turnTouchButtonGreen(1, bank: cBank, page: page); });
~base.setTouchButtonFunc(1, {
  ~cortez.phaseSynth.mixer.tglMute;
  if( ~cortez.phaseSynth.mixer.isMuted, { ~base.turnTouchButtonRed(1); }, { ~base.turnTouchButtonGreen(1) });
}, bank: cBank, page: page);

// Reverb Send:
~base.setFaderFunc(2, { | val |
  ~cortez.phaseSynth.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: cBank, page: page);

// Granulator Send:
~base.setFaderFunc(3, { | val |
  ~cortez.phaseSynth.mixer.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: cBank, page: page);

// Modular Send:
~base.setFaderFunc(4, { | val |
  ~cortez.phaseSynth.mixer.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: cBank, page: page);

//////// Grid:

// fader pre/post:
~base.turnGridMagenta(0, 3, bank: cBank, page: page);
~base.setGridFunc(0, 3, r {
  loop {
    ~cortez.phaseSynth.mixer.setSendPre;
    ~base.turnGridCyan(0, 3).yield;

    ~cortez.phaseSynth.mixer.setSendPost;
    ~base.turnGridMagenta(0, 3).yield;
  };
}, bank: cBank, page: page);

// sequences:
5.do({ | i | ~base.turnGridYellow(i, 0, bank: cBank, page: page); });

//1
~base.setGridFunc(0, 0, r {
  loop {
    ~cortez.phaseSynth.playSequence(\cortezPhase, ~cortez.clock);
    ~base.turnGridGreen(0, 0).yield;

    ~cortez.phaseSynth.stopSequence(\cortezPhase, ~cortez.clock);
    ~base.turnGridRed(0, 0).yield;
  };
}, bank: cBank, page: page);

//2
~base.setGridFunc(1, 0, r {
  loop {
    ~cortez.phaseSynth.playSequence(\cortezPhase2, ~cortez.clock);
    ~base.turnGridGreen(1, 0).yield;

    ~cortez.phaseSynth.stopSequence(\cortezPhase2, ~cortez.clock);
    ~base.turnGridRed(1, 0).yield;
  };
}, bank: cBank, page: page);

//3
~base.setGridFunc(2, 0, r {
  loop {
    ~cortez.phaseSynth.playSequence(\cortezPhase3, ~cortez.clock);
    ~base.turnGridGreen(2, 0).yield;

    ~cortez.phaseSynth.stopSequence(\cortezPhase3, ~cortez.clock);
    ~base.turnGridRed(2, 0).yield;
  };
}, bank: cBank, page: page);

//4
~base.setGridFunc(3, 0, r {
  loop {
    ~cortez.phaseSynth.playSequence(\cortezPhase4, ~cortez.clock);
    ~base.turnGridGreen(3, 0).yield;

    ~cortez.phaseSynth.stopSequence(\cortezPhase4, ~cortez.clock);
    ~base.turnGridRed(3, 0).yield;
  };
}, bank: cBank, page: page);

//5
~base.setGridFunc(4, 0, r {
  loop {
    ~cortez.phaseSynth.playSequence(\cortezPhase5, ~cortez.clock);
    ~base.turnGridGreen(4, 0).yield;

    ~cortez.phaseSynth.stopSequence(\cortezPhase5, ~cortez.clock);
    ~base.turnGridRed(4, 0).yield;
  };
}, bank: cBank, page: page);



//////////////////////////////
/////////////////////////////
// Performance Bank ////////////
////////////////////////////
//////////////////////////////

/////////////
// faders: //
//////////////

// modes:
~base.setFaderMode(1, 'greenFill', bank: performanceBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 2, 'magentaFill', bank: performanceBank, page: page); });
~base.setFaderMode(5, 'whiteFill', bank: performanceBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 6, 'magentaFill', bank: performanceBank, page: page); });


//////// Trumpet Grain:

// Volume:

~base.setFaderFunc(1, { | val |
  ~cortez.trumpetShift.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: performanceBank, page: page);

// Reverb Send:
~base.setFaderFunc(2, { | val |
  ~cortez.trumpetShift.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: performanceBank, page: page);

// Granulator Send:
~base.setFaderFunc(3, { | val |
  ~cortez.trumpetShift.mixer.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: performanceBank, page: page);

// Modular Send:
~base.setFaderFunc(4, { | val |
  ~cortez.trumpetShift.mixer.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: performanceBank, page: page);

//////// Modular Shift:
~base.setFaderFunc(5, { | val |
  ~cortez.shiftInput.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(5, val);
}, bank: performanceBank, page: page);

// Reverb Send:
~base.setFaderFunc(6, { | val |
  ~cortez.shiftInput.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(6, val);
}, bank: performanceBank, page: page);

// Granulator Send:
~base.setFaderFunc(7, { | val |
  ~cortez.shiftInput.mixer.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(7, val);
}, bank: performanceBank, page: page);

// Modular Send:
~base.setFaderFunc(8, { | val |
  ~cortez.shiftInput.mixer.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(8, val);
}, bank: performanceBank, page: page);

////////////////////////
//////// Grid: ////////
////////////////////////

//////// Trumpet Grain:

//// SequenceRoutine:

// On/Off
~base.turnTouchButtonCyan(2, 'middle', bank: performanceBank, page: page);
~base.turnTouchButtonCyan(2, 'top', bank: performanceBank, page: page);
~base.setTouchButtonFunc(2, r {
  loop {
    ~cortez.trumpetSeq.playSequence(0.17);
    seqVisRoutine.reset;
    seqVisRoutine.play;
    ~base.turnTouchButtonGreen(2, 'middle').yield;

    ~cortez.trumpetSeq.stopSequence;
    seqVisRoutine.stop;
    ~base.turnTouchButtonRed(2, 'middle').yield;
  };
}, bank: performanceBank, page: page);


// Visualiser:
seqVisRoutine = r {
  var columnArray = [1, 2, 3, 3, 2, 1, 2, 3, 3, 2, 1, 2, 3, 3, 2, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3];
  var rowArray = [3, 3, 3, 2, 2, 3, 3, 3, 2, 2, 3, 3, 3, 2, 2, 3, 3, 3, 2, 2, 2, 3, 3, 3, 2, 2, 2];
  var resetFunc = {
    3.do({ | i | ~base.turnGridCyan(i + 1, 3, bank: performanceBank, page: page); });
    3.do({ | i | ~base.turnGridCyan(i + 1, 2, bank: performanceBank, page: page); });
  };
  loop {
    resetFunc.value;
    ~base.turnGridWhite(columnArray[~cortez.trumpetSeq.activeStep], rowArray[~cortez.trumpetSeq.activeStep],
      bank: performanceBank, page: page);
    0.05.wait;
  };
};




// Colors:
4.do({ | i | ~base.turnGridCyan(i, 0, bank: performanceBank, page: page); });
4.do({ | i | ~base.turnGridCyan(i, 1, bank: performanceBank, page: page); });
4.do({ | i | ~base.turnGridCyan(i, 2, bank: performanceBank, page: page); });
4.do({ | i | ~base.turnGridCyan(i, 3, bank: performanceBank, page: page); });

// freeze:
~base.turnGridBlue(0, 3, bank: performanceBank, page: page);
~base.setGridFunc(0, 3, r {
  {
    ~cortez.trumpetShift.freeze;
    ~base.turnGridGreen(0, 3).yield;

    ~cortez.trumpetShift.releaseFreeze;
    ~base.turnGridRed(0, 3).yield;
  }.loop;
}, bank: performanceBank, page: page);

// shift functions:

//  Octave Down:
~base.setGridFunc(1, 3, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('downOctave', -12, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(1, 3);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(1, 3, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('downOctave');
  ~base.turnGridCyan(1, 3);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(1, 3, { | val |
  ~cortez.trumpetShift.setSynthVol('downOctave', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);


// fifth Down:
~base.setGridFunc(2, 3, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('downFifth', -7, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(2, 3);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(2, 3, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('downFifth');
  ~base.turnGridCyan(2, 3);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(2, 3, { | val |
  ~cortez.trumpetShift.setSynthVol('downFifth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);



// fourth down:
~base.setGridFunc(3, 3, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('downFourth', -5, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(3, 3);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(3, 3, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('downFourth');
  ~base.turnGridCyan(3, 3);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(3, 3, { | val |
  ~cortez.trumpetShift.setSynthVol('downFourth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// third down:
~base.setGridFunc(0, 2, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('downThird', -4, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(0, 2);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(0, 2, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('downThird');
  ~base.turnGridCyan(0, 2);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(0, 2, { | val |
  ~cortez.trumpetShift.setSynthVol('downThird', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// second down:
~base.setGridFunc(1, 2, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('downSecond', -2, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(1, 2);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(1, 2, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('downSecond');
  ~base.turnGridCyan(1, 2);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(1, 2, { | val |
  ~cortez.trumpetShift.setSynthVol('downSecond', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);


// root:
~base.setGridFunc(2, 2, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('root', 0, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(2, 2);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(2, 2, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('root');
  ~base.turnGridCyan(2, 2);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(2, 2, { | val |
  ~cortez.trumpetShift.setSynthVol('root', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// second:
~base.setGridFunc(3, 2, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('second', 2, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(3, 2);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(3, 2, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('second');
  ~base.turnGridCyan(3, 2);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(3, 2, { | val |
  ~cortez.trumpetShift.setSynthVol('second', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// third:
~base.setGridFunc(0, 1, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('third', 3, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(0, 1);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(0, 1, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('third');
  ~base.turnGridCyan(0, 1);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(0, 1, { | val |
  ~cortez.trumpetShift.setSynthVol('third', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// fourth:
~base.setGridFunc(1, 1, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('fourth', 5, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(1, 1);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(1, 1, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('fourth');
  ~base.turnGridCyan(1, 1);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(1, 1, { | val |
  ~cortez.trumpetShift.setSynthVol('fourth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// fifth:
~base.setGridFunc(2, 1, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('fifth', 7, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(2, 1);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(2, 1, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('fifth');
  ~base.turnGridCyan(2, 1);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(2, 1, { | val |
  ~cortez.trumpetShift.setSynthVol('fifth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// sixth:
~base.setGridFunc(3, 1, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('sixth', 8, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(3, 1);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(3, 1, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('sixth');
  ~base.turnGridCyan(3, 1);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(3, 1, { | val |
  ~cortez.trumpetShift.setSynthVol('sixth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// seventh:
~base.setGridFunc(0, 0, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('seventh', 10, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(0, 0);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(0, 0, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('seventh');
  ~base.turnGridCyan(0, 0);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(0, 0, { | val |
  ~cortez.trumpetShift.setSynthVol('seventh', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// octave:
~base.setGridFunc(1, 0, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('oct', 12, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(1, 0);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(1, 0, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('oct');
  ~base.turnGridCyan(1, 0);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(1, 0, { | val |
  ~cortez.trumpetShift.setSynthVol('oct', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// 9th:
~base.setGridFunc(2, 0, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('ninth', 14, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(2, 0);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(2, 0, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('ninth');
  ~base.turnGridCyan(2, 0);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(2, 0, { | val |
  ~cortez.trumpetShift.setSynthVol('ninth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);


// 10th:
~base.setGridFunc(3, 0, { | val |
  ~cortez.trumpetShift.makeSynthSustaining('tenth', 15, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(3, 0);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(3, 0, { | val |
  ~cortez.trumpetShift.releaseSynthSustaining('tenth');
  ~base.turnGridCyan(3, 0);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(3, 0, { | val |
  ~cortez.trumpetShift.setSynthVol('tenth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// Shifter:

// colors:
4.do({ | i | ~base.turnGridMagenta(i + 4, 0, bank: performanceBank, page: page); });
4.do({ | i | ~base.turnGridMagenta(i + 4, 1, bank: performanceBank, page: page); });
4.do({ | i | ~base.turnGridMagenta(i + 4, 2, bank: performanceBank, page: page); });
4.do({ | i | ~base.turnGridMagenta(i + 4, 3, bank: performanceBank, page: page); });

//  Octave Down:
~base.setGridFunc(4, 3, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downOctave', -12, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(4, 3);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(4, 3, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downOctave');
  ~base.turnGridMagenta(4, 3);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(4, 3, { | val |
  ~cortez.shiftInput.setSynthVol('downOctave', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);


//  Seventh Down:
~base.setGridFunc(5, 3, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downSeventh', -10, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(5, 3);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(5, 3, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downSeventh');
  ~base.turnGridMagenta(5, 3);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(5, 3, { | val |
  ~cortez.shiftInput.setSynthVol('downSeventh', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// fifth Down:
~base.setGridFunc(6, 3, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downFifth', -7, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(6, 3);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(6, 3, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downFifth');
  ~base.turnGridMagenta(6, 3);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(6, 3, { | val |
  ~cortez.shiftInput.setSynthVol('downFifth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);



// fourth down:
~base.setGridFunc(7, 3, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downFourth', -5, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(7, 3);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(7, 3, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downFourth');
  ~base.turnGridMagenta(7, 3);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(7, 3, { | val |
  ~cortez.shiftInput.setSynthVol('downFourth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// third down:
~base.setGridFunc(4, 2, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downThird', -4, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(4, 2);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(4, 2, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downThird');
  ~base.turnGridMagenta(4, 2);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(4, 2, { | val |
  ~cortez.shiftInput.setSynthVol('downThird', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// second down:
~base.setGridFunc(5, 2, { | val |
  ~cortez.shiftInput.makeSynthSustaining('downSecond', -2, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(5, 2);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(5, 2, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('downSecond');
  ~base.turnGridMagenta(5, 2);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(5, 2, { | val |
  ~cortez.shiftInput.setSynthVol('downSecond', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);


// root:
~base.setGridFunc(6, 2, { | val |
  ~cortez.shiftInput.makeSynthSustaining('root', 0, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(6, 2);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(6, 2, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('root');
  ~base.turnGridMagenta(6, 2);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(6, 2, { | val |
  ~cortez.shiftInput.setSynthVol('root', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// second:
~base.setGridFunc(7, 2, { | val |
  ~cortez.shiftInput.makeSynthSustaining('second', 2, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(7, 2);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(7, 2, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('second');
  ~base.turnGridMagenta(7, 2);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(7, 2, { | val |
  ~cortez.shiftInput.setSynthVol('second', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// third:
~base.setGridFunc(4, 1, { | val |
  ~cortez.shiftInput.makeSynthSustaining('third', 3, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(4, 1);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(4, 1, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('third');
  ~base.turnGridMagenta(4, 1);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(4, 1, { | val |
  ~cortez.shiftInput.setSynthVol('third', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// fourth:
~base.setGridFunc(5, 1, { | val |
  ~cortez.shiftInput.makeSynthSustaining('fourth', 5, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(5, 1);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(5, 1, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('fourth');
  ~base.turnGridMagenta(5, 1);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(5, 1, { | val |
  ~cortez.shiftInput.setSynthVol('fourth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// fifth:
~base.setGridFunc(6, 1, { | val |
  ~cortez.shiftInput.makeSynthSustaining('fifth', 7, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(6, 1);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(6, 1, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('fifth');
  ~base.turnGridMagenta(6, 1);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(6, 1, { | val |
  ~cortez.shiftInput.setSynthVol('fifth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// sixth:
~base.setGridFunc(7, 1, { | val |
  ~cortez.shiftInput.makeSynthSustaining('sixth', 8, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(7, 1);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(7, 1, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('sixth');
  ~base.turnGridMagenta(7, 1);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(7, 1, { | val |
  ~cortez.shiftInput.setSynthVol('sixth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// seventh:
~base.setGridFunc(4, 0, { | val |
  ~cortez.shiftInput.makeSynthSustaining('seventh', 10, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(4, 0);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(4, 0, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('seventh');
  ~base.turnGridMagenta(4, 0);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(4, 0, { | val |
  ~cortez.shiftInput.setSynthVol('seventh', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// octave:
~base.setGridFunc(5, 0, { | val |
  ~cortez.shiftInput.makeSynthSustaining('oct', 12, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(5, 0);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(5, 0, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('oct');
  ~base.turnGridMagenta(5, 0);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(5, 0, { | val |
  ~cortez.shiftInput.setSynthVol('oct', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);

// 9th:
~base.setGridFunc(6, 0, { | val |
  ~cortez.shiftInput.makeSynthSustaining('ninth', 14, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(6, 0);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(6, 0, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('ninth');
  ~base.turnGridMagenta(6, 0);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(6, 0, { | val |
  ~cortez.shiftInput.setSynthVol('ninth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);


// 10th:
~base.setGridFunc(7, 0, { | val |
  ~cortez.shiftInput.makeSynthSustaining('tenth', 15, val.ccdbfs(-70, 0), 16000);
  ~base.turnGridGreen(7, 0);
}, 'noteOn', bank: performanceBank, page: page);
~base.setGridFunc(7, 0, { | val |
  ~cortez.shiftInput.releaseSynthSustaining('tenth');
  ~base.turnGridMagenta(7, 0);
}, 'noteOff', bank: performanceBank, page: page);
~base.setGridFunc(7, 0, { | val |
  ~cortez.shiftInput.setSynthVol('tenth', val.ccdbfs(-36, 6));
}, 'pressure', bank: performanceBank, page: page);


//////////////////////////////
/////////////////////////////
// Bells and Base Bank: ////////////
////////////////////////////
//////////////////////////////

/////////////////////////
//////// Faders: ////////
////////////////////////

//////// Bells:
~base.setFaderMode(1, 'magentaFill', bank: bellsAndBass, page: page);
3.do({ | i | ~base.setFaderMode(i + 2, 'greenFill', bank: bellsAndBass, page: page); });
~base.setFaderValue(1, 127, bank: bellsAndBass, page: page);

~base.setFaderFunc(1, { | val |
  ~cortez.bells.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: bellsAndBass, page: page);

~base.setFaderFunc(2, { | val |
  ~cortez.bells.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: bellsAndBass, page: page);

~base.setFaderFunc(3, { | val |
  ~cortez.bells.mixer.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: bellsAndBass, page: page);

~base.setFaderFunc(4, { | val |
  ~cortez.bells.mixer.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: bellsAndBass, page: page);

/////// Bass:
~base.setFaderMode(5, 'whiteFill', bank: bellsAndBass, page: page);
3.do({ | i | ~base.setFaderMode(i+ 5, 'greenFill', bank: bellsAndBass, page: page); });

// vol:
~base.setFaderFunc(5, { | val |
  ~cortez.bass.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(5, val);
}, bank: bellsAndBass, page: page);

// send 0:
~base.setFaderFunc(6, { | val |
  ~cortez.bass.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(6, val);
}, bank: bellsAndBass, page: page);

// send 1:
~base.setFaderFunc(7, { | val |
  ~cortez.bass.mixer.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(7, val);
}, bank: bellsAndBass, page: page);

// send 2:
~base.setFaderFunc(8, { | val |
  ~cortez.bass.mixer.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(8, val);
}, bank: bellsAndBass, page: page);


/////////////////////////
//////// Grid: ////////
////////////////////////


//////// Bells:

// Colors:
8.do({ | i | ~base.turnGridMagenta(i, 2, bank: bellsAndBass, page: page); });
8.do({ | i | ~base.turnGridMagenta(i, 3, bank: bellsAndBass, page: page); });

// Play Functions:
bellsNoteArray.do({ | note, i |
  ~base.setGridFunc(i, 2, { | val |
    ~cortez.bells.playDriftingNote(note.midicps, val.ccdbfs(-12, 0), rrand(1, 5), 0, 0.2, 0.003, 0.1, rrand(-1, 1));
    ~base.turnGridGreen(i, 2);
  }, 'noteOn', bank: bellsAndBass, page: page);
  ~base.setGridFunc(i, 2, {
    ~cortez.bells.releaseDriftingNote(note.midicps);
    ~base.turnGridMagenta(i, 2);
  }, 'noteOff', bank: bellsAndBass, page: page);
});

bellsNoteArray.do({ | note, i |
  ~base.setGridFunc(i, 3, { | val |
    ~cortez.bells.playDriftingNote((note + 12).midicps, val.ccdbfs(-12, 0), rrand(1, 5), 0, 0.2, 0.003, 0.1, rrand(-1, 1));
  }, 'noteOn', bank: bellsAndBass, page: page);
  ~base.setGridFunc(i, 3, {
    ~cortez.bells.releaseDriftingNote((note + 12).midicps);
    ~base.turnGridMagenta(i, 3);
  }, 'noteOff', bank: bellsAndBass, page: page);
});


//////// Bass Synth:

8.do({ | i | ~base.turnGridWhite(i, 0, bank: bellsAndBass, page: page); });
8.do({ | i | ~base.turnGridWhite(i, 1, bank: bellsAndBass, page: page); });

bassNoteArray.do({ | note, i |
  ~base.setGridFunc(i, 0, { | val |
    ~cortez.bass.playNote(note.midicps, val.ccdbfs(-24, 0));
    ~base.turnGridGreen(i, 0);
  }, 'noteOn', bank: bellsAndBass, page: page);
  ~base.setGridFunc(i, 0, { | val |
    ~cortez.bass.releaseNote(note.midicps);
    ~base.turnGridWhite(i, 0);
  }, 'noteOff', bank: bellsAndBass, page: page);
});
bassNoteArray.do({ | note, i |
  ~base.setGridFunc(i, 1, { | val |
    ~cortez.bass.playNote((note + 12).midicps, val.ccdbfs(-24, 0));
    ~base.turnGridGreen(i, 1);
  }, 'noteOn', bank: bellsAndBass, page: page);
  ~base.setGridFunc(i, 1, { | val |
    ~cortez.bass.releaseNote((note + 12).midicps);
    ~base.turnGridWhite(i, 1);
  }, 'noteOff', bank: bellsAndBass, page: page);
});



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

//////// Submix B;

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