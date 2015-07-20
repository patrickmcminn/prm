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
controlButtonResetFunc =  {
  controlButtonColorArray.do({ | color, i | ~base.turnControlButtonColor(i, 'right', color, page: page); });
  8.do({ | i | ~base.turnControlButtonOff(i+1, 'left', page: page); });
};

controlButtonResetFunc.value;
~base.turnControlButtonGreen(1, 'left', page: page);

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

3.do({ | i | ~base.setFaderMode(i+1, 'cyanFill', bank: mainBank, page: page); });
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
  var fb = val.linlin(0, 127, 0.7, 1.2);
  ~connections.droner.delay.setFeedback(fb);
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
  ~base.setFaderValue(5, val);
}, bank: mainBank, page: page);

// gran send:
~base.setFaderFunc(6, { | val |
  ~prm.microphone.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(6, val);
}, bank: mainBank, page: page);

// modular send:
~base.setFaderFunc(7, { | val |
  ~prm.microphone.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(7, val);
}, bank: mainBank, page: page);

// modular volume:
~base.setFaderMode(8, 'redFill', bank: mainBank, page: page);
~base.setFaderFunc(8, { | val |
  ~prm.modular.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(8, val);
}, bank: mainBank, page: page);


//////// grid:
~base.turnAllGridMagenta(bank: mainBank, page: page);

// droner erosion:
~base.turnGridYellow(0, 3, bank: mainBank, page: page);
~base.setGridFunc(0, 3, { | val |
  var mix = val.linlin(0, 127, 0.02, 0.3);
  ~connections.droner.setErosionMix(mix);
}, 'pressure', bank: mainBank, page: page);




////////////////////////////
///// Air Sputters Bank: ////
////////////////////////////


//////// Faders:
~base.setFaderMode(1, 'whiteFill', bank: airSputtersBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 2, 'magentaFill', bank: airSputtersBank, page: page); });

// vol:
~base.setFaderFunc(1, { | val |
  ~connections.mixerA.setVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: airSputtersBank, page: page);
// reverb send:
~base.setFaderFunc(2, { | val |
  ~connections.mixerA.setSendVol(0, 0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: airSputtersBank, page: page);
// granulator send:
~base.setFaderFunc(3, { | val |
  ~connections.mixerA.setSendVol(0, 1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: airSputtersBank, page: page);
// modular send:
~base.setFaderFunc(4, { | val |
  ~connections.mixerA.setSendVol(0, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: airSputtersBank, page: page);


/////// Grid:
~base.turnAllGridWhite(bank: airSputtersBank, page: page);



////////////////////////////
///// Droner Bank: ////
////////////////////////////

//////// Faders:
~base.setFaderMode(1, 'cyanFill', bank: dronerBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 2, 'magentaFill', bank: dronerBank, page: page); });

// vol:
~base.setFaderFunc(1, { | val |
  ~connections.mixerA.setVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: dronerBank, page: page);
// reverb send:
~base.setFaderFunc(2, { | val |
  ~connections.mixerA.setSendVol(1, 0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: dronerBank, page: page);
// granulator send:
~base.setFaderFunc(3, { | val |
  ~connections.mixerA.setSendVol(1, 1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: dronerBank, page: page);
// modular send:
~base.setFaderFunc(4, { | val |
  ~connections.mixerA.setSendVol(1, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: dronerBank, page: page);

//////// Grid:
~base.turnAllGridCyan(dronerBank, page: page);



////////////////////////////
///// Bassline Bank: ////
////////////////////////////

//////// Faders:
~base.setFaderMode(1, 'blueFill', bank: basslineBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 2, 'magentaFill', bank: basslineBank, page: page); });

// vol:
~base.setFaderFunc(1, { | val |
  ~connections.mixerB.setVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: basslineBank, page: page);
// reverb send:
~base.setFaderFunc(2, { | val |
  ~connections.mixerB.setSendVol(0, 0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: basslineBank, page: page);
// granulator send:
~base.setFaderFunc(3, { | val |
  ~connections.mixerB.setSendVol(0, 1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: basslineBank, page: page);
// modular send:
~base.setFaderFunc(4, { | val |
  ~connections.mixerB.setSendVol(0, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: basslineBank, page: page);

//////// Grid:
~base.turnAllGridBlue(basslineBank, page: page);


////////////////////////////
///// Trumpet Gran Bank: ////
////////////////////////////

//////// Faders:
~base.setFaderMode(1, 'yellowFill', bank: trumpetGranBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 2, 'magentaFill', bank: trumpetGranBank, page: page); });

// vol:
~base.setFaderFunc(1, { | val |
  ~connections.mixerA.setVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: trumpetGranBank, page: page);
// reverb send:
~base.setFaderFunc(2, { | val |
  ~connections.mixerA.setSendVol(2, 0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: trumpetGranBank, page: page);
// granulator send:
~base.setFaderFunc(3, { | val |
  ~connections.mixerA.setSendVol(2, 1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: trumpetGranBank, page: page);
// modular send:
~base.setFaderFunc(4, { | val |
  ~connections.mixerA.setSendVol(2, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: trumpetGranBank, page: page);

//////// Grid:
~base.turnAllGridYellow(trumpetGranBank, page: page);

////////////////////////////
///// Inlet Bank: ////
////////////////////////////

//////// Faders:
~base.setFaderMode(1, 'whiteFill', bank: inletBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 2, 'magentaFill', bank: inletBank, page: page); });
~base.setFaderMode(5, 'greenFill', bank: inletBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 6, 'magentaFill', bank: inletBank, page: page); });

//// attack Randomizer:
// vol:
~base.setFaderFunc(1, { | val |
  ~connections.inlet.attackRandomizer.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: inletBank, page: page);
// reverb send:
~base.setFaderFunc(2, { | val |
  ~connections.mixerB.setSendVol(1, 0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: inletBank, page: page);
// granulator send:
~base.setFaderFunc(3, { | val |
  ~connections.mixerB.setSendVol(1, 1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: inletBank, page: page);
// modular send:
~base.setFaderFunc(4, { | val |
  ~connections.mixerB.setSendVol(1, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: inletBank, page: page);
//// Cascade:
// vol:
~base.setFaderFunc(5, { | val |
  ~connections.inlet.cascade.mixer.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(5, val);
}, bank: inletBank, page: page);

//////// Grid:
~base.turnAllGridYellow(inletBank, page: page);


////////////////////////////
///// Chord Bank: ////
////////////////////////////

//////// Faders:
~base.setFaderMode(1, 'cyan', bank: chordsBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 2, 'magentaFill', bank: chordsBank, page: page); });

// vol:
~base.setFaderFunc(1, { | val |
  ~connections.mixerC.setVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: chordsBank, page: page);
// reverb send:
~base.setFaderFunc(2, { | val |
  ~connections.mixerC.setSendVol(0, 0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: chordsBank, page: page);
// granulator send:
~base.setFaderFunc(3, { | val |
  ~connections.mixerC.setSendVol(0, 1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: chordsBank, page: page);
// modular send:
~base.setFaderFunc(4, { | val |
  ~connections.mixerC.setSendVol(0, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: chordsBank, page: page);

//////// Grid:
~base.turnAllGridCyan(chordsBank, page: page);


////////////////////////////////////////
///// Microphone Bank: /////
////////////////////////////////////////

//////// Faders:
~base.setFaderMode(1, 'cyan', bank: micBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 2, 'magentaFill', bank: micBank, page: page); });

// vol:
~base.setFaderFunc(1, { | val |
  ~prm.microphone.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: micBank, page: page);
// reverb send:
~base.setFaderFunc(2, { | val |
  ~prm.microphone.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: micBank, page: page);
// granulator send:
~base.setFaderFunc(3, { | val |
  ~prm.microphone.setSendVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: micBank, page: page);
// modular send:
~base.setFaderFunc(4, { | val |
 ~prm.microphone.setSendVol(2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: micBank, page: page);

//////// Grid:
~base.turnAllGridWhite(micBank, page: page);

