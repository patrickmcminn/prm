/*
Saturday, April 22nd 2017
Light Ohm.scd
prm
*/

var page = 'light';



var leftBank1 = 0;
var leftBank2 = 1;

var activeLeftBank = leftBank1;

~ohm.makePage(page);
~ohm.addLeftButtonsBanks(1, page);
~ohm.addLeftSlidersBanks(1, page);
~ohm.addLeftKnobsBanks(1, page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);

~ohm.turnAllGridMagenta(page: page);


// play Arps:
~ohm.turnGridBlue(0, 0, page: page);
~ohm.setGridFunc(0, 0, {
  ~light.arps.playArps;
  ~ohm.turnGridGreen(0, 0);
}, page: page);

// switch banks:
~ohm.setCrossfaderButtonMonitorFunc(0, {
  if( activeLeftBank == leftBank1, { ~ohm.turnCrossfaderButtonMagenta(0) }, { ~ohm.turnCrossfaderButtonCyan(0) });
}, page: page);
~ohm.setCrossfaderButtonFunc(0, {
  if( activeLeftBank == leftBank1, { activeLeftBank = leftBank2 }, { activeLeftBank = leftBank1 });
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  ~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, page: page);

//////// Left Bank 1:

////// Main:
// Button:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~light.mixerA.isMuted(0), { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, bank: leftBank1, page: page);
~ohm.setLeftButtonFunc(0, { ~light.mixerA.tglMute(0) }, bank: leftBank1, page: page);
// Slider:
~ohm.setLeftSliderFunc(0, {  | val | ~light.mixerA.setVol(0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(0, 2, { | val | ~light.mixerA.setSendVol(0, 0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// Delays:
~ohm.setLeftKnobFunc(0, 1, { | val | ~light.mixerA.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// Modular:
~ohm.setLeftKnobFunc(0, 0, { | val | ~light.mixerA.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);

//// Bass:
// Button:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~light.mixerB.isMuted(0), { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, bank: leftBank1, page: page);
~ohm.setLeftButtonFunc(1, { ~light.mixerB.tglMute(0) }, bank: leftBank1, page: page);
// Slider:
~ohm.setLeftSliderFunc(1, {  | val | ~light.mixerB.setVol(0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(1, 2, { | val | ~light.mixerB.setSendVol(0, 0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// Delays:
~ohm.setLeftKnobFunc(1, 1, { | val | ~light.mixerB.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// Modular:
~ohm.setLeftKnobFunc(1, 0, { | val | ~light.mixerB.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);


//// Arps:
// Button:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~light.mixerA.isMuted(1), { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, bank: leftBank1, page: page);
~ohm.setLeftButtonFunc(2, { ~light.mixerA.tglMute(1) }, bank: leftBank1, page: page);
// Slider:
~ohm.setLeftSliderFunc(2, {  | val | ~light.mixerA.setVol(1, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(2, 2, { | val | ~light.mixerA.setSendVol(1, 0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// Delays:
~ohm.setLeftKnobFunc(2, 1, { | val | ~light.mixerA.setSendVol(1, 1, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// Modular:
~ohm.setLeftKnobFunc(2, 0, { | val | ~light.mixerA.setSendVol(1, 2, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);

/////// Main Filter Cutoff:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var cutoff = val.linexp(0, 127, 150, 20000);
  ~light.main.setFilterCutoff(cutoff);
}, bank: leftBank1, page: page);
/////// Arps Filter Cutoff:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var cutoff = val.linexp(0, 127, 150, 20000);
  ~light.arps.setFilterCutoff(cutoff);
}, bank: leftBank1, page: page);

//////// Left Bank 2:

////// Light Reverb:
// Button:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~lightSend.mixerC.isMuted(0), { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, bank: leftBank2, page: page);
~ohm.setLeftButtonFunc(0, { ~lightSend.mixerC.tglMute(0) }, bank: leftBank2, page: page);
// Slider:
~ohm.setLeftSliderFunc(0, {  | val | ~lightSend.mixerC.setVol(0, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(0, 2, { | val | ~lightSend.mixerC.setSendVol(0, 0, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// Delays:
~ohm.setLeftKnobFunc(0, 1, { | val | ~lightSend.mixerC.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// Modular:
~ohm.setLeftKnobFunc(0, 0, { | val | ~lightSend.mixerC.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);


////// Light Delays:
// Button:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~lightSend.mixerC.isMuted(1), { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, bank: leftBank2, page: page);
~ohm.setLeftButtonFunc(1, { ~lightSend.mixerC.tglMute(1) }, bank: leftBank2, page: page);
// Slider:
~ohm.setLeftSliderFunc(1, {  | val | ~lightSend.mixerC.setVol(1, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(1, 2, { | val | ~lightSend.mixerC.setSendVol(1, 0, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// Delays:
~ohm.setLeftKnobFunc(1, 1, { | val | ~lightSend.mixerC.setSendVol(1, 1, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// Modular:
~ohm.setLeftKnobFunc(1, 0, { | val | ~lightSend.mixerC.setSendVol(1, 2, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);

////// Light Multishift:
// Button:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~lightSend.mixerC.isMuted(2), { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, bank: leftBank2, page: page);
~ohm.setLeftButtonFunc(2, { ~lightSend.mixerC.tglMute(2) }, bank: leftBank2, page: page);
// Slider:
~ohm.setLeftSliderFunc(2, {  | val | ~lightSend.mixerC.setVol(2, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(2, 2, { | val | ~lightSend.mixerC.setSendVol(2, 0, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// Delays:
~ohm.setLeftKnobFunc(2, 1, { | val | ~lightSend.mixerC.setSendVol(2, 1, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// Modular:
~ohm.setLeftKnobFunc(2, 0, { | val | ~lightSend.mixerC.setSendVol(2, 2, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
