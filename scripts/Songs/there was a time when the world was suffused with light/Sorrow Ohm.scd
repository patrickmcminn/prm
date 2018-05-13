/*
Thursday, April 26th 2018
Sorrow Ohm.scd
prm

there is a sorrow in beginning to understand
*/

var page = 'sorrow';

~ohm.makePage(page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);


//////////////////////
//// Grid: /////
////////////////////

~ohm.turnAllGridMagenta(page: page);

//// Record Buffer:
~ohm.turnGridWhite(0, 0, page: page);
~ohm.setGridFunc(0, 0, {
  ~sorrow.trumpets.recordBuffer;
  ~ohm.turnGridGreen(0, 0)
}, page: page);

//////////////////////
//// Left Side: /////
////////////////////

//////// Live Trumpet:

//// Mute:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~sorrow.trumpets.mixer.isMuted(0),
    { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, page: page);
~ohm.setLeftButtonFunc(0, { ~sorrow.trumpets.mixer.tglMute(0) }, page: page);

//// Volume:
~ohm.setLeftSliderFunc(0, { | val |
  ~sorrow.trumpets.mixer.setVol(0, val.ccdbfs)
}, page: page);

//////// Trumpet 2:
//// Mute:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~sorrow.trumpets.mixer.isMuted(1),
    { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, page: page);
~ohm.setLeftButtonFunc(1, { ~sorrow.trumpets.mixer.tglMute(1) }, page: page);

//// Volume:
~ohm.setLeftSliderFunc(1, { | val |
  ~sorrow.trumpets.mixer.setVol(1, val.ccdbfs)
}, page: page);

//////// Trumpet 3:
//// Mute:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~sorrow.trumpets.mixer.isMuted(2),
    { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, page: page);
~ohm.setLeftButtonFunc(2, { ~sorrow.trumpets.mixer.tglMute(2) }, page: page);

//// Volume:
~ohm.setLeftSliderFunc(2, { | val |
  ~sorrow.trumpets.mixer.setVol(2, val.ccdbfs)
}, page: page);

/////// Trumpet Sends:
// reverb:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~sorrow.mixer.setSendVol(0, 0, val.ccdbfs);
}, page: page);
// granulator:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~sorrow.mixer.setSendVol(0, 1, val.ccdbfs);
}, page: page);
// modular:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~sorrow.mixer.setSendVol(0, 2, val.ccdbfs);
}, page: page);


//////// Moog:
//// Mute:
~ohm.setLeftButtonMonitorFunc(3, {
  if( ~sorrow.mixer.isMuted(1),
    { ~ohm.turnLeftButtonRed(3) }, { ~ohm.turnLeftButtonGreen(3) });
}, page: page);
~ohm.setLeftButtonFunc(3, { ~sorrow.mixer.tglMute(1) }, page: page);
//// Volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~sorrow.mixer.setVol(1, val.ccdbfs);
}, page: page);
//// Sends:
// reverb:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~sorrow.mixer.setSendVol(1, 0, val.ccdbfs);
}, page: page);
// granulator:
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~sorrow.mixer.setSendVol(1, 1, val.ccdbfs);
}, page: page);
// modular:
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~sorrow.mixer.setSendVol(1, 2, val.ccdbfs);
}, page: page);


