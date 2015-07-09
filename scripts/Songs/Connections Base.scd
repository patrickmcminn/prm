/*
Wednesday, July 8th 2015
Connections Base.scd
prm
*/

var page = 'connections';

var mainBank = 0;
var airSputtersBank = 1;
var dronerBank = 2;
var basslineBank = 3;
var trumpetGranBank = 4;
var inletBank = 5;
var chordsBank = 6;
var micBank = 7;

var controlButtonResetFunc, controlButtonColorArray;

~base.makePage(page);

~base.addTouchButtonsBanks(7, page: page);
~base.addFadersBanks(7, page: page);
~base.addGridBanks(7, page: page);

////////////////////////////
///// Control Buttons: ////
////////////////////////////

controlButtonColorArray = ['magenta', 'white', 'cyan', 'blue', 'yellow', 'white', 'cyan', 'magenta'];
controlButtonResetFunc {
  controlButtonColorArray.do({ | color, i | ~base.turnControlButtonColor(i, 'right', color, page: page); });
  8.do({ | i | ~base.turnControlButtonOff(i, 'left', page: page); });
};

// main bank:
~base.setControlButtonFunc(1, {
  ~base.setActiveGridBank(mainBank);
  ~base.setActiveFadersBank(mainBank);
  ~base.setActiveTouchButtonsBank(mainBank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(1);
}, page: page);

// air sputters bank:
~base.setControlButtonFunc(2, {
  ~base.setActiveGridBank(airSputtersBank);
  ~base.setActiveFadersBank(airSputtersBank);
  ~base.setActiveTouchButtonsBank(airSputtersBank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(2);
}, page: page);

// droner bank:
~base.setControlButtonFunc(3, {
  ~base.setActiveGridBank(dronerBank);
  ~base.setActiveFadersBank(dronerBank);
  ~base.setActiveTouchButtonsBank(dronerBank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(3);
}, page: page);

// bassline bank:
~base.setControlButtonFunc(4, {
  ~base.setActiveGridBank(basslineBank);
  ~base.setActiveFadersBank(basslineBank);
  ~base.setActiveTouchButtonsBank(basslineBank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(4);
}, page: page);

// trumpet gran bank:
~base.setControlButtonFunc(5, {
  ~base.setActiveGridBank(trumpetGranBank);
  ~base.setActiveFadersBank(trumpetGranBank);
  ~base.setActiveTouchButtonsBank(trumpetGranBank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(5);
}, page: page);

// inlet bank:
~base.setControlButtonFunc(6, {
  ~base.setActiveGridBank(inletBank);
  ~base.setActiveFadersBank(inletBank);
  ~base.setActiveTouchButtonsBank(inletBank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(6);
}, page: page);

// chords bank:
~base.setControlButtonFunc(7, {
  ~base.setActiveGridBank(chordsBank);
  ~base.setActiveFadersBank(chordsBank);
  ~base.setActiveTouchButtonsBank(chordsBank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(7);
}, page: page);

// mic bank:
~base.setControlButtonFunc(8, {
  ~base.setActiveGridBank(micBank);
  ~base.setActiveFadersBank(micBank);
  ~base.setActiveTouchButtonsBank(micBank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(8);
}, page: page);


//////////////////////
///// Main Bank: ////
/////////////////////

//////// Faders:

//// droner:

3.do({ | i | ~base.setFaderMode(i, 'cyanFill', bank: mainBank, page: page); });
// reverb send:
~base.setFaderFunc(1, { | val |
  ~connections.mixerA.setSendVol(1, 0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: mainBank, page: page);
// modular send:
~base.setFaderFunc(2, { | val |
  ~connections.mixerA.setSendVol(1, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: mainBank, page: page);
// granulator feedback:
~base.setFaderFunc(3, { | val |
  var fb = val.linlin(0, 127, 0.0, 1.0);
  ~connections.droner.granulator.setFeedback(fb);
  ~base.setFaderValue(3, val);
}, bank: mainBank, page: page);

//// microphone:
4.do({ | i | ~base.setFaderMode(i+4, 'magentaFill', bank: mainBank, page: page); });
// volume:
~base.setFaderFunc(4, { | val |
  ~prm.microphone.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(val);
}, bank: mainBank, page: page);
// verb send:
~base.setFaderFunc(5, { | val |
  ~prm.microphone.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(val);
}, bank: mainBank, page: page);

// gran send:
~base.setFaderFunc(6, { | val |
  ~prm.microphone.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(val);
}, bank: mainBank, page: page);

// modular send:
~base.setFaderFunc(7, { | val |
  ~prm.microphone.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(val);
}, bank: mainBank, page: page);


//////// grid:
~base.turnAllGridMagenta(bank: mainBank, page: page);

// droner erosion:
~base.turnGridYellow(0, 3, bank: mainBank, page: page);
~base.setGridFunc(0, 3, { | val |
  var mix = val.linlin(0, 127, 0.02, 1);
  ~connections.droner.setErosionMix(mix);
}, 'pressure', bank: mainBank, page: page);




////////////////////////////
///// Air Sputters Bank: ////
////////////////////////////


//////// Faders:
~base.setFaderMode(1, 'whiteFill', bank: airSputtersBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 2, 'magentaFill', bank: airSputtersBank, page: page); });

// vol:
//~base.setFaderFunc()



/////// Grid:
~base.turnAllGridWhite(bank: airSputtersBank, page: page);

