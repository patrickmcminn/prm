/*
Monday, April 30th 2018
Darkness Ohm.scd
prm

a shallow eclipsing darkness
*/

var page = 'darkness';

var mixerBank = 0;
var songBank = 1;


~ohm.makePage(page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);


~ohm.addLeftKnobsBanks(1, page);


///////////////////
////// Grid //////
/////////////////

/////// intro:
~ohm.setGridMonitorFunc(0, 0, {
  if( ~darkness.introIsPlaying,
    { ~ohm.turnGridGreen(0, 0) }, { ~ohm.turnGridWhite(0, 0) });
}, page: page);
~ohm.setGridFunc(0, 0, { ~darkness.tglIntro; }, page: page);
/////// bassline:
~ohm.setGridMonitorFunc(0, 1, {
  if( ~darkness.basslineIsPlaying == true,
    { ~ohm.turnGridGreen(0, 1) }, { ~ohm.turnGridYellow(0, 1) });
}, page: page);
~ohm.setGridFunc(0, 1, { ~darkness.tglBassline; }, page: page);

//////// drones:

5.do({ | i |
  ~ohm.setGridMonitorFunc(i, 3, {
    if( ("~darkness.drone" ++ (i+1) ++"IsPlaying").interpret,
      { ~ohm.turnGridGreen(i, 3) }, { ~ohm.turnGridBlue(i, 3) });
  }, page: page);
  ~ohm.setGridFunc(i, 3, { ("~darkness.tglDrone" ++ (i+1)).interpret; },
    page: page);
  });

5.do({ | i |
  ~ohm.setGridMonitorFunc(i, 4, {
    if( ("~darkness.drone" ++ (i+6) ++"IsPlaying").interpret,
      { ~ohm.turnGridGreen(i, 4) }, { ~ohm.turnGridBlue(i, 4) });
  }, page: page);
    ~ohm.setGridFunc(i, 4, { ("~darkness.tglDrone" ++ (i+6)).interpret; },
    page: page);
});

//////// trumpet:
// input mute:
~ohm.setGridMonitorFunc(0, 6, {
  if( ~darkness.trumpetInput.isMuted,
    { ~ohm.turnGridRed(0, 6) }, { ~ohm.turnGridGreen(0, 6) });
}, page: page);
~ohm.setGridFunc(0, 6, { ~darkness.trumpetInput.tglMute },
  page: page);
// octave mute/unmute:
~ohm.setGridMonitorFunc(1, 6, {
  if( ~darkness.trumpet.mixer.isMuted(1),
    { ~ohm.turnGridRed(1, 6) }, { ~ohm.turnGridGreen(1, 6) });
}, page: page);
~ohm.setGridFunc(1, 6, { ~darkness.trumpet.mixer.tglMute(1) },
  page: page);
// multiharm:
~ohm.setGridMonitorFunc(2, 6, {
  if( ~darkness.trumpet.multiHarmonizer.isBypassed == true,
    { ~ohm.turnGridRed(2, 6) }, { ~ohm.turnGridGreen(2, 6) });
}, page: page);
~ohm.setGridFunc(2, 6, { ~darkness.trumpet.multiHarmonizer.tglBypass },
  page: page);


///////////////////
//// Left Side ///
/////////////////

//////////// Mixer Bank:

//////// intro sample:
//// Mute:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~darkness.songMixer.isMuted(0),
    { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, mixerBank, page);
~ohm.setLeftButtonFunc(0, { ~darkness.songMixer.tglMute(0) }, bank: mixerBank, page: page);

//// Volume:
~ohm.setLeftSliderFunc(0, { | val |
  ~darkness.songMixer.setVol(0, val.ccdbfs)
}, bank: mixerBank, page: page);

//// Sends:
// reverb:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~darkness.songMixer.setSendVol(0, 0, val.ccdbfs)
}, bank: mixerBank, page: page);
// granulator:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~darkness.songMixer.setSendVol(0, 1, val.ccdbfs);
}, bank: mixerBank, page: page);
// delay:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~darkness.songMixer.setSendVol(0, 2, val.ccdbfs);
}, bank: mixerBank, page: page);

//////// bass:
//// Mute:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~darkness.songMixer.isMuted(1),
    { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, mixerBank, page);
~ohm.setLeftButtonFunc(1, { ~darkness.songMixer.tglMute(1) }, bank: mixerBank, page: page);

//// Volume:
~ohm.setLeftSliderFunc(1, { | val |
  ~darkness.songMixer.setVol(1, val.ccdbfs)
}, bank: mixerBank, page: page);

//// Sends:
// reverb:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~darkness.songMixer.setSendVol(1, 0, val.ccdbfs)
}, bank: mixerBank, page: page);
// granulator:
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~darkness.songMixer.setSendVol(1, 1, val.ccdbfs);
}, bank: mixerBank, page: page);
// delay:
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~darkness.songMixer.setSendVol(1, 2, val.ccdbfs);
}, bank: mixerBank, page: page);

//////// trumpet:
//// Mute:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~darkness.songMixer.isMuted(2),
    { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, mixerBank, page);
~ohm.setLeftButtonFunc(2, { ~darkness.songMixer.tglMute(2) }, bank: mixerBank, page: page);

//// Volume:
~ohm.setLeftSliderFunc(2, { | val |
  ~darkness.songMixer.setVol(2, val.ccdbfs)
}, bank: mixerBank, page: page);

//// Sends:
// reverb:
~ohm.setLeftKnobFunc(2, 2, { | val |
  ~darkness.songMixer.setSendVol(2, 0, val.ccdbfs)
}, bank: mixerBank, page: page);
// granulator:
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~darkness.songMixer.setSendVol(2, 1, val.ccdbfs);
}, bank: mixerBank, page: page);
// delay:
~ohm.setLeftKnobFunc(2, 0, { | val |
  ~darkness.songMixer.setSendVol(2, 2, val.ccdbfs);
}, bank: mixerBank, page: page);


//////// drones:
//// Mute:
~ohm.setLeftButtonMonitorFunc(3, {
  if( ~darkness.songMixer.isMuted(3),
    { ~ohm.turnLeftButtonRed(3) }, { ~ohm.turnLeftButtonGreen(3) });
}, mixerBank, page);
~ohm.setLeftButtonFunc(3, { ~darkness.songMixer.tglMute(3) }, bank: mixerBank, page: page);

//// Volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~darkness.songMixer.setVol(3, val.ccdbfs)
}, bank: mixerBank, page: page);

//// Sends:
// reverb:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~darkness.songMixer.setSendVol(3, 0, val.ccdbfs)
}, bank: mixerBank, page: page);
// granulator:
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~darkness.songMixer.setSendVol(3, 1, val.ccdbfs);
}, bank: mixerBank, page: page);
// delay:
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~darkness.songMixer.setSendVol(3, 2, val.ccdbfs);
}, bank: mixerBank, page: page);

//////////// Song Bank:



