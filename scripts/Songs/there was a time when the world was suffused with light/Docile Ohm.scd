/*
Tuesday, May 1st 2018
Docile Ohm.scd
prm

to compress the world to a docile state
*/

var page = 'docile';

~ohm.makePage(page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);

/////////////////////
//// Grid:  ////////
///////////////////

/////// main loop:
~ohm.setGridMonitorFunc(0, 0, {
  if( ~docile.mainLoopIsPlaying == true,
    { ~ohm.turnGridGreen(0, 0) }, { ~ohm.turnGridMagenta(0, 0) });
}, page: page);
~ohm.setGridFunc(0, 0, { ~docile.toggleMainLoop; }, page: page);

/////// kicks:
~ohm.turnGridRed(0, 1, page: page);
~ohm.setGridFunc(0, 1, { ~docile.stopAllKicks; }, page: page);
8.do({ | i |
  ~ohm.setGridMonitorFunc(i, 2, {
    if(("~docile.kickSection" ++ (i+1) ++ "IsPlaying").interpret,
      { ~ohm.turnGridGreen(i, 2) }, { ~ohm.turnGridCyan(i, 2) });
  }, page: page);
  ~ohm.setGridFunc(i, 2, {
    ~docile.stopAllKicks;
    ("~docile.playKickSection" ++ (i+1)).interpret;
  }, page: page);
});

/////// noisies:
~ohm.setGridMonitorFunc(0, 4, {
  if( ~docile.noisiesFullLoopIsPlaying == true,
    { ~ohm.turnGridGreen(0, 4) }, { ~ohm.turnGridWhite(0, 4) });
}, page: page);
~ohm.setGridFunc(0, 4, { ~docile.tglNoisiesLoop; }, page: page);

/////// moog:
~ohm.setGridMonitorFunc(0, 6, {
  if( ~docile.moog.arpRevIsPlaying == true,
    { ~ohm.turnGridGreen(0, 6) }, { ~ohm.turnGridYellow(0, 6) });
}, page: page);
~ohm.setGridFunc(0, 6, { ~docile.moog.playArpRev; }, page: page);
~ohm.setGridMonitorFunc(1, 6, {
  if( ~docile.moog.arpIsPlaying == true,
    { ~ohm.turnGridGreen(1, 6) }, { ~ohm.turnGridBlue(1, 6) });
}, page: page);
~ohm.setGridFunc(1, 6, { ~docile.moog.playArp; }, page: page);





/////////////////////
//// Left Side: ////
///////////////////

//////////// Main:
//// Mute:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~docile.mixer.isMuted(0) == true,
    { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, page: page);
~ohm.setLeftButtonFunc(0, { ~docile.mixer.tglMute(0) }, page: page);
//// Volume:
~ohm.setLeftSliderFunc(0, { | val |
  ~docile.mixer.setVol(0, val.ccdbfs);
}, page: page);
//// Sends:
// Verb:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~docile.mixer.setSendVol(0, 0, val.ccdbfs);
}, page: page);
// Gran:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~docile.mixer.setSendVol(0, 1, val.ccdbfs);
}, page: page);
// Modular:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~docile.mixer.setSendVol(0, 2, val.ccdbfs);
}, page: page);

//////////// Kick:
//// Mute:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~docile.mixer.isMuted(1) == true,
    { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, page: page);
~ohm.setLeftButtonFunc(1, { ~docile.mixer.tglMute(1) }, page: page);
//// Volume:
~ohm.setLeftSliderFunc(1, { | val |
  ~docile.mixer.setVol(1, val.ccdbfs);
}, page: page);
//// Sends:
// Verb:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~docile.mixer.setSendVol(1, 0, val.ccdbfs);
}, page: page);
// Gran:
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~docile.mixer.setSendVol(1, 1, val.ccdbfs);
}, page: page);
// Modular:
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~docile.mixer.setSendVol(1, 2, val.ccdbfs);
}, page: page);


//////////// Noisies:
//// Mute:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~docile.mixer.isMuted(2) == true,
    { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, page: page);
~ohm.setLeftButtonFunc(2, { ~docile.mixer.tglMute(2) }, page: page);
//// Volume:
~ohm.setLeftSliderFunc(2, { | val |
  ~docile.mixer.setVol(2, val.ccdbfs);
}, page: page);
//// Sends:
// Verb:
~ohm.setLeftKnobFunc(2, 2, { | val |
  ~docile.mixer.setSendVol(2, 0, val.ccdbfs);
}, page: page);
// Gran:
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~docile.mixer.setSendVol(2, 1, val.ccdbfs);
}, page: page);
// Modular:
~ohm.setLeftKnobFunc(2, 0, { | val |
  ~docile.mixer.setSendVol(2, 2, val.ccdbfs);
}, page: page);


//////////// Moog:
//// Mute:
~ohm.setLeftButtonMonitorFunc(3, {
  if( ~docile.mixer.isMuted(3) == true,
    { ~ohm.turnLeftButtonRed(3) }, { ~ohm.turnLeftButtonGreen(3) });
}, page: page);
~ohm.setLeftButtonFunc(3, { ~docile.mixer.tglMute(3) }, page: page);
//// Volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~docile.mixer.setVol(3, val.ccdbfs);
}, page: page);
//// Sends:
// Verb:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~docile.mixer.setSendVol(3, 0, val.ccdbfs);
}, page: page);
// Gran:
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~docile.mixer.setSendVol(3, 1, val.ccdbfs);
}, page: page);
// Modular:
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~docile.mixer.setSendVol(3, 2, val.ccdbfs);
}, page: page);
