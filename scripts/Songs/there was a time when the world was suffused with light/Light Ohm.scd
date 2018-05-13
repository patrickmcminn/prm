/*
Saturday, April 22nd 2017
Light Ohm.scd
prm

edited Thursday May 3rd 2018
*/

var page = 'light';


var leftBank1 = 0;
var leftBank2 = 1;
var leftBank3 = 2;

var activeLeftBank = leftBank1;

~ohm.makePage(page);
~ohm.addLeftButtonsBanks(2, page);
~ohm.addLeftSlidersBanks(2, page);
~ohm.addLeftKnobsBanks(2, page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);

~ohm.turnAllGridMagenta(page: page);


////////////////
//// Grid: ////
//////////////

//// play chorale:
~ohm.setGridMonitorFunc(0, 0, {
  if( ~light.chorale.choraleIsPlaying,
    { ~ohm.turnGridGreen(0, 0) }, { ~ohm.turnGridBlue(0, 0) });
}, page: page);
~ohm.setGridFunc(0, 0, { ~light.chorale.tglChorale; }, page: page);


/*
// play Arps:
~ohm.turnGridBlue(0, 0, page: page);
~ohm.setGridFunc(0, 0, {
  ~light.arps.playArps;
  ~ohm.turnGridGreen(0, 0);
}, page: page);
*/

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
  ~light.arps.lowPass.setCutoff(cutoff);
}, bank: leftBank1, page: page);


//////// Left Bank 2:
////// chorale:
//// Mute:
~ohm.setLeftButtonMonitorFunc(0, { if( ~light.mixerC.isMuted(0),
  { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) }); },
  bank: leftBank2, page: page);
~ohm.setLeftButtonFunc(0, { ~light.mixerC.tglMute(0) },
  bank: leftBank2, page: page);
//// Fader:
~ohm.setLeftSliderFunc(0, { | val |
  ~light.mixerC.setVol(0, val.ccdbfs);
}, bank: leftBank2, page: page);
//// Sends:
// verb:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~light.mixerC.setSendVol(0, 0, val.ccdbfs);
}, leftBank2, page);
// delay:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~light.mixerC.setSendVol(0, 1, val.ccdbfs);
}, leftBank2, page);
// modular:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~light.mixerC.setSendVol(0, 2, val.ccdbfs);
}, leftBank2, page);

////// trumpet:
//// Mute:
~ohm.setLeftButtonMonitorFunc(1, { if( ~light.mixerC.isMuted(1),
  { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) }); },
  bank: leftBank2, page: page);
~ohm.setLeftButtonFunc(1, { ~light.mixerC.tglMute(1) },
  bank: leftBank2, page: page);
//// Fader:
~ohm.setLeftSliderFunc(1, { | val |
  ~light.mixerC.setVol(1, val.ccdbfs);
}, bank: leftBank2, page: page);
//// Sends:
// verb:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~light.mixerC.setSendVol(1, 0, val.ccdbfs);
}, leftBank2, page);
// delay:
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~light.mixerC.setSendVol(1, 1, val.ccdbfs);
}, leftBank2, page);
// modular:
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~light.mixerC.setSendVol(1, 2, val.ccdbfs);
}, leftBank2, page);



/*
//////// Left Bank 3:

////// Light Reverb:
// Button:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~lightSend.mixerC.isMuted(0), { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, bank: leftBank3, page: page);
~ohm.setLeftButtonFunc(0, { ~lightSend.mixerC.tglMute(0) }, bank: leftBank2, page: page);
// Slider:
~ohm.setLeftSliderFunc(0, {  | val | ~lightSend.mixerC.setVol(0, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(0, 2, { | val | ~lightSend.mixerC.setSendVol(0, 0, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);
// Delays:
~ohm.setLeftKnobFunc(0, 1, { | val | ~lightSend.mixerC.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);
// Modular:
~ohm.setLeftKnobFunc(0, 0, { | val | ~lightSend.mixerC.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);


////// Light Delays:
// Button:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~lightSend.mixerC.isMuted(1), { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, bank: leftBank3, page: page);
~ohm.setLeftButtonFunc(1, { ~lightSend.mixerC.tglMute(1) }, bank: leftBank2, page: page);
// Slider:
~ohm.setLeftSliderFunc(1, {  | val | ~lightSend.mixerC.setVol(1, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(1, 2, { | val | ~lightSend.mixerC.setSendVol(1, 0, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);
// Delays:
~ohm.setLeftKnobFunc(1, 1, { | val | ~lightSend.mixerC.setSendVol(1, 1, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);
// Modular:
~ohm.setLeftKnobFunc(1, 0, { | val | ~lightSend.mixerC.setSendVol(1, 2, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);

////// Light Multishift:
// Button:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~lightSend.mixerC.isMuted(2), { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, bank: leftBank3, page: page);
~ohm.setLeftButtonFunc(2, { ~lightSend.mixerC.tglMute(2) }, bank: leftBank2, page: page);
// Slider:
~ohm.setLeftSliderFunc(2, {  | val | ~lightSend.mixerC.setVol(2, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(2, 2, { | val | ~lightSend.mixerC.setSendVol(2, 0, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);
// Delays:
~ohm.setLeftKnobFunc(2, 1, { | val | ~lightSend.mixerC.setSendVol(2, 1, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);
// Modular:
~ohm.setLeftKnobFunc(2, 0, { | val | ~lightSend.mixerC.setSendVol(2, 2, val.ccdbfs(-70, 0));
}, bank: leftBank3, page: page);
*/
