/*
Tuesday, September 25th 2018
False Self APC.scd
prm

updated 12/10/2021
*/


var page = 'falseSelf';

var main = 0;
var secondary = 1;

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var send10 = 4;
var send11 = 5;
var send12 = 6;
var send13 = 7;

var sequenceReset;

~apc.makePage(page);
~apc.addPageLoadFunction(\masterFunc, { ~apc.storageDict[\loadMasterFunc].value; }, page);


~apc.addMixerEncodersBanks(7, page: page);
~apc.addMixerBanks(1, page);
~apc.addDeviceButtonsBanks(1, page);

~apc.setUserButtonFunc({
  ~prm.reverb.loadPreset('falseSelf');
  ~apc.turnUserButtonOn;
}, bank: main, page: page);

~apc.setGridMonitorFunc(\muted, {
  //if( ~falseSelf.mixer.isMutedMaster, { ~apc.turnBankButtonOn }, { ~apc.turnBankButtonOff });
  if( ~falseSelf.songIsMuted, { ~apc.turnBankButtonOn }, { ~apc.turnBankButtonOff });
}, main, page);
~apc.setBankButtonFunc({ ~falseSelf.tglMuteSong; }, bank: main, page: page);

~apc.setSceneLaunchMonitorFunc(\midi, {
  var midi = ~falseSelf.midiEnabled;
  if( midi == true, { ~apc.turnSceneLaunchButtonGreen(0) }, { ~apc.turnSceneLaunchButtonWhite(0) });
}, bank: main, page: page);
~apc.setSceneLaunchFunc(0, { ~falseSelf.toggleMIDIFuncs }, bank: main, page: page);

//////////////////////
//// Mixer: /////////
////////////////////

~apc.mapMixer("~falseSelf.mixer", 8, 0, 0, send0, send1, send2, send3, main, page);
~apc.mapSendBanks(\main, send0, send1, send2, send3, main, page);


~apc.mapMixer("~falseSelf.mixer", 8, 8, 0, send10, send11, send12, send13, secondary, page);
~apc.mapSendBanks(\page2, send10, send11, send12, send13, secondary, page);

/*
~apc.setDeviceButtonsMonitorFunc(\switcher, {
  if( ~apc.activeDeviceButtonsBank == main,
    { ~apc.turnDeviceButtonOn(5); });
}, main, page);
~apc.setDeviceButtonsMonitorFunc(\switcher2, {
  if( ~apc.activeDeviceButtonsBank == secondary, { ~apc.turnDeviceButtonOn(6); });
*/
~apc.turnDeviceButtonOn(5, main, page);
~apc.turnDeviceButtonOn(6, secondary, page);
~apc.setDeviceButtonFunc(6, {
  ~apc.setActiveDeviceButtonsBank(secondary, page);
  ~apc.setActiveMixerBank(secondary, page);
}, bank: main, page: page);
~apc.setDeviceButtonFunc(5, {
  ~apc.setActiveDeviceButtonsBank(main, page);
  ~apc.setActiveMixerBank(main, page);
}, bank: secondary, page: page);

// input mutes:
~apc.setMixerMonitorFunc(\inputMutes, {
  var mainTrumpet = ~falseSelf.mainTrumpetInput.isMuted;
  if( mainTrumpet,
    { ~apc.turnRecordEnableButtonOff(6, main, page); },
    { ~apc.turnRecordEnableButtonOn(6, main, page); });
}, main, page);
~apc.setRecordEnableButtonFunc(6, { ~falseSelf.mainTrumpetInput.tglMute },
  bank: main, page: page);
~apc.setMixerMonitorFunc(\inputMutesSec, {
  var trumpetMel = ~falseSelf.trumpetMelodyInput.isMuted;
  var modular = ~falseSelf.modular.isMuted;
  var modNoise = ~falseSelf.modNoise.isMuted;
  if( trumpetMel,
    { ~apc.turnRecordEnableButtonOff(1, secondary, page); },
    { ~apc.turnRecordEnableButtonOn(1, secondary, page); });
  if( modular,
    { ~apc.turnRecordEnableButtonOff(7, secondary, page); },
    { ~apc.turnRecordEnableButtonOn(7, secondary, page) });
  if( modNoise,
    { ~apc.turnRecordEnableButtonOff(8, secondary, page); },
    { ~apc.turnRecordEnableButtonOn(8, secondary, page) });
}, secondary, page);
~apc.setRecordEnableButtonFunc(1, { ~falseSelf.trumpetMelodyInput.tglMute },
  bank: secondary, page: page);
~apc.setRecordEnableButtonFunc(7, { ~falseSelf.modular.tglMute },
  bank: secondary, page: page);
~apc.setRecordEnableButtonFunc(8, { ~falseSelf.modNoise.tglMute },
  bank: secondary, page: page);

//////////////////////
//// Grid: //////////
////////////////////

~apc.turnAllGridWhite(1, main, page);
~apc.setGridMonitorFunc(\twoBars, {
  if( ~falseSelf.twoBarWarning == true,
    { ~apc.turnGridGreen(0, 4, 2) }, { ~apc.turnGridPurple(0, 4, 2); });
}, main, page);

~apc.turnGridCyan(0, 3, 2, main, page);
~apc.setGridFunc(0, 3, {
  ~falseSelf.trumpetMelody.recordBuffer;
  ~apc.turnGridRed(0, 3);
}, bank: main, page: page);

sequenceReset = { 7.do({ | i |  ~apc.turnGridBlue(i, 1, bank: main, page: page); }); };
sequenceReset.value;


// play sequences:
6.do({ | i |
  ~apc.setGridFunc(i, 1, {
    ~pyrOut.control(0, 69, i+9);
    ~apc.turnGridGreen(i, 1);
  }, bank: main, page: page);
});

~apc.setGridFunc(6, 1, {
  ~pyrOut.control(0, 69, 16);
  ~apc.turnGridGreen(6, 1);
}, bank: main, page: page);

~apc.turnSceneLaunchButtonBlue(3, 2, main, page);
~apc.setSceneLaunchFunc(3, { sequenceReset.value; }, bank: main, page: page);

//// tpt feedback:
~apc.setGridMonitorFunc(\tpt, {
  var feedback = ~falseSelf.mainTrumpet.delay.feedback;
  if( feedback == 1,
    { ~apc.turnGridYellow(0, 0) }, { ~apc.turnGridPink(0, 0) });
}, main, page);

~apc.setGridFunc(0, 0, {
  var feedback = ~falseSelf.mainTrumpet.delay.feedback;
  if( feedback != 1,
    { ~falseSelf.mainTrumpet.delay.setFeedback(1); },
    { ~falseSelf.mainTrumpet.delay.setFeedback(0.22); });
}, bank: main, page: page);

//////////////////////////
//// device encoders: ///
////////////////////////

~apc.setDeviceEncodersMonitorFunc(\main, {
  var tptFeedback, gtrCutoff1, gtrCutoff2, fbCutoff, kickCutoff;
  tptFeedback = ~falseSelf.mainTrumpet.delay.feedback.linlin(0, 1, 0, 127);
  gtrCutoff1 = ~falseSelf.fakeGuitar.section1.filterCutoff.explin(40, 20000, 0, 127);
  gtrCutoff2 = ~falseSelf.fakeGuitar.section2.filterCutoff.explin(40, 20000, 0, 127);
  fbCutoff = ~falseSelf.bassSection.feedback.filterCutoff.explin(40, 6500, 0, 127);
  kickCutoff = ~falseSelf.drums.eq.highPassCutoff.explin(40, 3500, 0, 127);
  ~apc.setDeviceEncoderValue(1, tptFeedback);
  ~apc.setDeviceEncoderValue(2, gtrCutoff1);
  ~apc.setDeviceEncoderValue(3, gtrCutoff2);
  ~apc.setDeviceEncoderValue(4, fbCutoff);
  ~apc.setDeviceEncoderValue(5, kickCutoff);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(1, { | val |
  var feedback = val.linlin(0, 127, 0, 1);
  ~falseSelf.mainTrumpet.delay.setFeedback(feedback);
}, main, page);
~apc.setDeviceEncoderFunc(2, { | val |
  var cutoff = val.linexp(0, 127, 40, 20000);
  ~falseSelf.fakeGuitar.section1.setFilterCutoff(cutoff);
}, main, page);
~apc.setDeviceEncoderFunc(3, { | val |
  var cutoff = val.linexp(0, 127, 40, 20000);
  ~falseSelf.fakeGuitar.section2.setFilterCutoff(cutoff);
}, main, page);
~apc.setDeviceEncoderFunc(4, { | val |
  var cutoff = val.linexp(0, 127, 40, 6500);
  ~falseSelf.bassSection.feedback.setFilterCutoff(cutoff);
}, main, page);
~apc.setDeviceEncoderFunc(5, { | val |
  var cutoff = val.linexp(0, 127, 40, 3500);
  ~falseSelf.drums.eq.setHighPassCutoff(cutoff);
}, main, page);







/*
var page = 'falseSelf';

var activeMixerBank = mixerBank1;

var mixerBank1 = 0;
var mixerBank2 = 1;

var sendMod = 0;

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var send4 = 4;
var send5 = 5;
var send6 = 6;
var send7 = 7;



~apc.makePage(page);
~apc.addGridBanks(1, page);
~apc.addMixerBanks(1, page);
~apc.addMixerEncodersBanks(7, page);
~apc.addClipStopBanks(1, page: page);
~apc.addSceneLaunchBanks(1, page: page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);


//////// Metronome:
~apc.addPageLoadFunction(\metronome, {
  ~falseSelf.clock.schedAbs(~falseSelf.clock.beats.ceil, { | beat |
    //~falseSelf.clock.beatInBar.postln;
    ~falseSelf.clock.playNextBar({
      ~apc.turnSendsButtonOn(page: page);
      { ~apc.turnSendsButtonOff(page: page) }.defer(0.1);
    });
    ~apc.turnMetronomeButtonOn(page: page);
    { ~apc.turnMetronomeButtonOff(page: page); }.defer(0.1);
    1;
  });
}, page: page);

// TO DO:
/////// BANK CHANGE


/////////////////////
//// main grid: ////
////////////////////

//// Bar Numbers:

8.do({ | button | ~apc.turnGridBlue(button, 4, 0, mixerBank1, page); });
8.do({ | button | ~apc.turnGridBlue(button, 4, 0, mixerBank2, page); });
~apc.setGridMonitorFunc(\bars, {
  if( ~falseSelf.bar >= 1, { ~apc.turnGridMagenta(0, 4); }, { ~apc.turnGridBlue(0, 4) });
  if( ~falseSelf.bar >= 13, { ~apc.turnGridMagenta(1, 4); }, { ~apc.turnGridBlue(1, 4) });
  if( ~falseSelf.bar >= 21, { ~apc.turnGridMagenta(2, 4); }, { ~apc.turnGridBlue(2, 4) });
  if( ~falseSelf.bar >= 25, { ~apc.turnGridMagenta(3, 4); }, { ~apc.turnGridBlue(3, 4) });
  if( ~falseSelf.bar >= 53, { ~apc.turnGridMagenta(4, 4); }, { ~apc.turnGridBlue(4, 4) });
  if( ~falseSelf.bar >= 55, { ~apc.turnGridMagenta(5, 4); }, { ~apc.turnGridBlue(5, 4) });
  if( ~falseSelf.bar >= 68, { ~apc.turnGridMagenta(6, 4); }, { ~apc.turnGridBlue(6, 4) });
  if( ~falseSelf.bar >= 81, { ~apc.turnGridMagenta(7, 4); }, { ~apc.turnGridBlue(7, 4) });
}, bank: mixerBank1, page: page);
~apc.setGridMonitorFunc(\bars, {
  if( ~falseSelf.bar >= 1, { ~apc.turnGridMagenta(0, 4); }, { ~apc.turnGridBlue(0, 4) });
  if( ~falseSelf.bar >= 13, { ~apc.turnGridMagenta(1, 4); }, { ~apc.turnGridBlue(1, 4) });
  if( ~falseSelf.bar >= 21, { ~apc.turnGridMagenta(2, 4); }, { ~apc.turnGridBlue(2, 4) });
  if( ~falseSelf.bar >= 25, { ~apc.turnGridMagenta(3, 4); }, { ~apc.turnGridBlue(3, 4) });
  if( ~falseSelf.bar >= 53, { ~apc.turnGridMagenta(4, 4); }, { ~apc.turnGridBlue(4, 4) });
  if( ~falseSelf.bar >= 55, { ~apc.turnGridMagenta(5, 4); }, { ~apc.turnGridBlue(5, 4) });
  if( ~falseSelf.bar >= 68, { ~apc.turnGridMagenta(6, 4); }, { ~apc.turnGridBlue(6, 4) });
  if( ~falseSelf.bar >= 81, { ~apc.turnGridMagenta(7, 4); }, { ~apc.turnGridBlue(7, 4) });
}, bank: mixerBank2, page: page);



//// countdowns:
/*
//// Metronome:
~ohm.turnGridRed(7, 1, bank: mainGridBank, page: page);
~ohm.setGridFunc(7, 1, {
~falseSelf.clock.schedAbs(~falseSelf.clock.beats.ceil, { | beat |
//~falseSelf.clock.beatInBar.postln;
if( ~falseSelf.clock.beatInBar.round == 0,
{ ~falseSelf.metronome.playDownbeat },
{ ~falseSelf.metronome.playBeat }
);
1;
},page);
~ohm.turnGridGreen(7, 1);
}, bank: mainGridBank, page: page);
*/


// grid play funcs:

~apc.setGridMonitorFunc(\isPlaying, {
  if( ~falseSelf.section1IsPlaying == true, { ~apc.turnGridGreen(0, 3) }, { ~apc.turnGridBlue(0, 3, 1); });
  if( ~falseSelf.chorus1IsPlaying == true, { ~apc.turnGridGreen(1, 3) }, { ~apc.turnGridBlue(1, 3, 1); });
  if( ~falseSelf.chorus2IsPlaying == true, { ~apc.turnGridGreen(2, 3) }, { ~apc.turnGridBlue(2, 3, 1);});
  if( ~falseSelf.canonIsPlaying == true, { ~apc.turnGridGreen(3, 3) }, { ~apc.turnGridBlue(3, 3, 1); });
  if( ~falseSelf.limboIsPlaying == true, { ~apc.turnGridGreen(4, 3) }, { ~apc.turnGridBlue(4, 3, 1); });
  if(~falseSelf.melodyIsPlaying == true, { ~apc.turnGridGreen(5, 3) }, { ~apc.turnGridBlue(5, 3, 1); });
  if( ~falseSelf.endIsPlaying == true, { ~apc.turnGridGreen(6, 3) }, { ~apc.turnGridBlue(6, 3, 1); });
}, bank: mixerBank1, page: page);
~apc.setGridMonitorFunc(\isPlaying, {
  if( ~falseSelf.section1IsPlaying == true, { ~apc.turnGridGreen(0, 3) }, { ~apc.turnGridBlue(0, 3, 1); });
  if( ~falseSelf.chorus1IsPlaying == true, { ~apc.turnGridGreen(1, 3) }, { ~apc.turnGridBlue(1, 3, 1); });
  if( ~falseSelf.chorus2IsPlaying == true, { ~apc.turnGridGreen(2, 3) }, { ~apc.turnGridBlue(2, 3, 1);});
  if( ~falseSelf.canonIsPlaying == true, { ~apc.turnGridGreen(3, 3) }, { ~apc.turnGridBlue(3, 3, 1); });
  if( ~falseSelf.limboIsPlaying == true, { ~apc.turnGridGreen(4, 3) }, { ~apc.turnGridBlue(4, 3, 1); });
  if(~falseSelf.melodyIsPlaying == true, { ~apc.turnGridGreen(5, 3) }, { ~apc.turnGridBlue(5, 3, 1); });
  if( ~falseSelf.endIsPlaying == true, { ~apc.turnGridGreen(6, 3) }, { ~apc.turnGridBlue(6, 3, 1); });
}, bank: mixerBank2, page: page);

///////////////////////
//// Mixer Bank 1: ///
/////////////////////

// start song:
~apc.turnSceneLaunchButtonWhite(0, 2, mixerBank1, page);
~apc.setSceneLaunchFunc(0, { ~falseSelf.startSong; ~apc.turnSceneLaunchButtonGreen(0); },
  bank: mixerBank1, page: page);

// Bells:
~apc.setClipStopFunc(1, { ~falseSelf.bellSection.free;  ~apc.turnClipStopButtonOff(1); },
  bank: mixerBank1, page: page);

// Fake Guitar:
~apc.setClipStopFunc(2, { ~falseSelf.fakeGuitar.free; ~apc.turnClipStopButtonOff(2); },
  bank: mixerBank1, page: page);

// Melody Synth:
~apc.setClipStopFunc(3, { ~falseSelf.melodySynth.free; ~apc.turnClipStopButtonOff(3); },
  bank: mixerBank1, page: page);

//// Basses:
~apc.setClipStopFunc(4, { ~falseSelf.bassSection.free; ~apc.turnClipStopButtonOff(4); },
  bank: mixerBank1, page: page);
/*
~apc.setGridMonitorFunc(\bass, {
if( ~falseSelf.bassSection.preChorusIsPlaying,
{ ~apc.turnGridGreen(3,

///patterns:
// pre-chorus:
~ohm.setGridMonitorFunc(5, 6, {
if( ~falseSelf.bassSection.preChorusIsPlaying,
{ ~ohm.turnGridGreen(5, 6) }, { ~ohm.turnGridRed(5, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 6, { if( ~falseSelf.bassSection.preChorusIsPlaying,
{ ~falseSelf.bassSection.stopPreChorus }, { ~falseSelf.bassSection.playPreChorus });
}, bank: mainGridBank, page: page);
// chorus:
~ohm.setGridMonitorFunc(6, 6, {
if( ~falseSelf.bassSection.chorusIsPlaying,
{ ~ohm.turnGridGreen(6, 6,) }, { ~ohm.turnGridRed(6, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 6, {
if( ~falseSelf.bassSection.chorusIsPlaying,
{ ~falseSelf.bassSection.stopChorus }, { ~falseSelf.bassSection.playChorus });
}, bank: mainGridBank, page: page);
// post-Chorus:
~ohm.setGridMonitorFunc(7, 6, {
if( ~falseSelf.bassSection.postChorusIsPlaying,
{ ~ohm.turnGridGreen(7, 6) }, { ~ohm.turnGridRed(7, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(7, 6, {
if( ~falseSelf.bassSection.postChorusIsPlaying,
{ ~falseSelf.bassSection.stopPostChorus }, { ~falseSelf.bassSection.playPostChorus });
}, bank: mainGridBank, page: page);
// chorale:
~ohm.setGridMonitorFunc(5, 7, {
if( ~falseSelf.bassSection.endIsPlaying,
{ ~ohm.turnGridGreen(5, 7) }, { ~ohm.turnGridRed(5, 7) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 7, {
if( ~falseSelf.bassSection.endIsPlaying,
{ ~falseSelf.bassSection.stopEnd }, { ~falseSelf.bassSection.playEnd });
}, bank: mainGridBank, page: page);
// end:
~ohm.setGridMonitorFunc(6, 7, {
if( ~falseSelf.bassSection.codaIsPlaying,
{ ~ohm.turnGridGreen(6, 7) }, { ~ohm.turnGridRed(6, 7) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 7, {
if( ~falseSelf.bassSection.codaIsPlaying,
{ ~falseSelf.bassSection.stopCoda }, { ~falseSelf.bassSection.playCoda });
}, bank: mainGridBank, page: page);
*/


// Main Trumpet:
~apc.setClipStopFunc(5, { ~falseSelf.mainTrumpet.free; ~apc.turnClipStopButtonOff(5) },
  bank: mixerBank1, page: page);

//// Drums:
~apc.setClipStopFunc(6, { ~falseSelf.drums.free; ~apc.turnClipStopButtonOff(6); },
  bank: mixerBank1, page: page);

~apc.setGridMonitorFunc(\drums, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\canon),
    { ~apc.turnGridGreen(5, 2); }, { ~apc.turnGridPurple(5, 2, 1); });
  if( ~falseSelf.drums.drums.isSequencePlaying(\ending),
    { ~apc.turnGridGreen(5, 1); }, { ~apc.turnGridCyan(5, 1, 1); });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(5, 2, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\canon),
    { ~falseSelf.drums.stopCanon }, { ~falseSelf.drums.playCanon(~falseSelf.clock); });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(5, 1, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\ending),
    { ~falseSelf.drums.stopEnding }, { ~falseSelf.drums.playEnding(~falseSelf.clock) });
}, bank: mixerBank1, page: page);


/*
// beginning:
~ohm.setGridMonitorFunc(5, 3, {
if( ~falseSelf.drums.drums.isSequencePlaying(\section1),
{ ~ohm.turnGridGreen(5, 3) }, { ~ohm.turnGridRed(5, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 3, {
if( ~falseSelf.drums.drums.isSequencePlaying(\section1),
{ ~falseSelf.drums.stopSection1 }, { ~falseSelf.drums.playSection1(~falseSelf.clock) });
}, bank: mainGridBank, page: page);
// chorus 1:
~ohm.setGridMonitorFunc(6, 3, {
if( ~falseSelf.drums.drums.isSequencePlaying(\chorus1),
{ ~ohm.turnGridGreen(6, 3) }, { ~ohm.turnGridRed(6, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 3, {
if( ~falseSelf.drums.drums.isSequencePlaying(\chorus1),
{ ~falseSelf.drums.stopChorus1 }, { ~falseSelf.drums.playChorus1(~falseSelf.clock); });
}, bank: mainGridBank, page: page);
// chorus 2:
~ohm.setGridMonitorFunc(7, 3, {
if( ~falseSelf.drums.drums.isSequencePlaying(\chorus2),
{ ~ohm.turnGridGreen(7, 3) }, { ~ohm.turnGridRed(7, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(7, 3, {
if( ~falseSelf.drums.drums.isSequencePlaying(\chorus2),
{ ~falseSelf.drums.stopChorus2 }, { ~falseSelf.drums.playChorus2(~falseSelf.clock) });
}, bank: mainGridBank, page: page);
// canon:
~ohm.setGridMonitorFunc(4, 4, {
if( ~falseSelf.drums.drums.isSequencePlaying(\canon),
{ ~ohm.turnGridGreen(4, 4) }, { ~ohm.turnGridRed(4, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 4, {
if( ~falseSelf.drums.drums.isSequencePlaying(\canon),
{ ~falseSelf.drums.stopCanon }, { ~falseSelf.drums.playCanon(~falseSelf.clock) });
}, bank: mainGridBank, page: page);
// end:
~ohm.setGridMonitorFunc(5, 4, {
if( ~falseSelf.drums.drums.isSequencePlaying(\ending),
{ ~ohm.turnGridGreen(5, 4) }, { ~ohm.turnGridRed(5, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 4, {
if( ~falseSelf.drums.drums.isSequencePlaying(\ending),
{ ~falseSelf.drums.stopEnding }, { ~falseSelf.drums.playEnding(~falseSelf.clock) });
}, bank: mainGridBank, page: page);

*/

// Trumpet Canon:
~apc.setClipStopFunc(7, { ~falseSelf.trumpetCanon.free; ~apc.turnClipStopButtonOff(7) },
  bank: mixerBank1, page: page);

//// Orchestra:
~apc.setClipStopFunc(8, { ~falseSelf.orchestra.free; ~apc.turnClipStopButtonOff(8) },
  bank: mixerBank1, page: page);
// play:
~apc.turnGridCyan(7, 2, 2, mixerBank1, page);
~apc.setGridFunc(7, 2, { ~falseSelf.orchestra.playMahlerPhrase }, bank: mixerBank1, page: page);


///////////////////////
//// Mixer Bank 2: ///
/////////////////////

//// Tempo Change:
~apc.setSceneLaunchMonitorFunc(\tempo, {
  if( ~falseSelf.clock.tempo == 2.37,
    { ~apc.turnSceneLaunchButtonGreen(0) }, { ~apc.turnSceneLaunchButtonBlue(0) });
}, bank: mixerBank2, page: page);
~apc.setSceneLaunchFunc(0, { ~falseSelf.clock.tempo = 142.2/60; }, bank: mixerBank2, page: page);

//// End Sequence:
~apc.setSceneLaunchMonitorFunc(\endSequence, {
  if( ~falseSelf.melodyIsPlaying == true,
    { ~apc.turnSceneLaunchButtonGreen(1) }, { ~apc.turnSceneLaunchButtonMagenta(1) });
}, bank: mixerBank2, page: page);
// start sequence:
~apc.setSceneLaunchFunc(1, { ~falseSelf.startMelody; }, bank: mixerBank2, page: page);

//// Trumpet Melody:
~apc.setClipStopFunc(1, { ~falseSelf.trumpetMelody.free; ~apc.turnClipStopButtonOff(1); },
  bank: mixerBank2, page: page);
~apc.setGridMonitorFunc(\tptShift, {
  if( ~falseSelf.trumpetMelody.shift1.mixer.isMuted,
    { ~apc.turnGridRed(0, 2, 1); }, { ~apc.turnGridGreen(0, 2); });
  if(~falseSelf.trumpetMelody.shift2.mixer.isMuted,
    { ~apc.turnGridRed(0, 1, 1); }, { ~apc.turnGridGreen(0, 1); });
  if( ~falseSelf.trumpetMelody.dry.isMuted,
    { ~apc.turnGridRed(0, 0, 1); }, { ~apc.turnGridGreen(0, 0); });
}, bank: mixerBank2, page: page);
// shift 1:
~apc.setGridFunc(0, 2, { ~falseSelf.trumpetMelody.shift1.mixer.tglMute; },
  bank: mixerBank2, page: page);
// shift 2:
~apc.setGridFunc(0, 1, { ~falseSelf.trumpetMelody.shift2.mixer.tglMute; },
  bank: mixerBank2, page: page);
// dry:
~apc.setGridFunc(0, 0, { ~falseSelf.trumpetMelody.dry.tglMute; },
  bank: mixerBank2, page: page);

//// Crude Drones:
~apc.setClipStopFunc(2, { ~falseSelf.drones.free; ~apc.turnClipStopButtonOff(2) },
  bank: mixerBank2, page: page);
/// voices:
~apc.setGridMonitorFunc(\drones, {
  if( ~falseSelf.drones.sampler.isSequencePlaying(\voice1),
    { ~apc.turnGridGreen(1, 2); }, { ~apc.turnGridRed(1, 2, 1); });
  if( ~falseSelf.drones.sampler.isSequencePlaying(\voice2),
    { ~apc.turnGridGreen(1, 1); }, { ~apc.turnGridRed(1, 1, 1); });
  if( ~falseSelf.drones.sampler.isSequencePlaying(\voice3),
    { ~apc.turnGridGreen(1, 0); }, { ~apc.turnGridRed(1, 0, 1); });
}, bank: mixerBank2, page: page);
// voice 1:
~apc.setGridFunc(1, 2, {
  if( ~falseSelf.drones.sampler.isSequencePlaying(\voice1),
    { ~falseSelf.drones.stopVoice1Sequence }, { ~falseSelf.drones.playVoice1Sequence(~falseSelf.clock); });
}, bank: mixerBank2, page: page);
// voice 2:
~apc.setGridFunc(1, 1, { if( ~falseSelf.drones.sampler.isSequencePlaying(\voice2),
  { ~falseSelf.drones.stopVoice2Sequence }, { ~falseSelf.drones.playVoice2Sequence(~falseSelf.clock) });
}, bank: mixerBank2, page: page);
// voice 3:
~apc.setGridFunc(1, 0, { if( ~falseSelf.drones.sampler.isSequencePlaying(\voice3),
  { ~falseSelf.drones.stopVoice3Sequence }, { ~falseSelf.drones.playVoice3Sequence(~falseSelf.clock) });
}, bank: mixerBank2, page: page);


//// 16th Drones:
~apc.setClipStopFunc(3, { ~falseSelf.sixteenthDrones.free; ~apc.turnClipStopButtonOff(3) },
  bank: mixerBank2, page: page);
/// voices:
~apc.setGridMonitorFunc(\sixteenthDrones, {
  if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice1),
    { ~apc.turnGridGreen(2, 2); }, { ~apc.turnGridRed(2, 2, 1); });
  if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice2),
    { ~apc.turnGridGreen(2, 1); }, { ~apc.turnGridRed(2, 1, 1); });
  if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice3),
    { ~apc.turnGridGreen(2, 0); }, { ~apc.turnGridRed(2, 0, 1); });
}, bank: mixerBank2, page: page);
// voice 1:
~apc.setGridFunc(2, 2, { if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice1),
  { ~falseSelf.sixteenthDrones.stopVoice1Sequence }, { ~falseSelf.sixteenthDrones.playVoice1Sequence(~falseSelf.clock); });
}, bank: mixerBank2, page: page);
// voice 2:
~apc.setGridFunc(2, 1, { if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice2),
  { ~falseSelf.sixteenthDrones.stopVoice2Sequence }, { ~falseSelf.sixteenthDrones.playVoice2Sequence(~falseSelf.clock) });
}, bank: mixerBank2, page: page);
// voice 3:
~apc.setGridFunc(2, 0, { if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice3),
  { ~falseSelf.sixteenthDrones.stopVoice3Sequence }, { ~falseSelf.sixteenthDrones.playVoice3Sequence(~falseSelf.clock) });
}, bank: mixerBank2, page: page);


//// Plane Noise:
~apc.setClipStopFunc(4, { ~falseSelf.planeNoise.free; ~apc.turnClipStopButtonOff(4); },
  bank: mixerBank2, page: page);
// playing:
~apc.setGridMonitorFunc(\planeNoise, {
  if( ~falseSelf.planeNoise.isPlaying,
    { ~apc.turnGridGreen(3, 2) }, { ~apc.turnGridRed(3, 2, 1); });
}, bank: mixerBank2, page: page);
~apc.setGridFunc(3, 2, {
  if( ~falseSelf.planeNoise.isPlaying,
    { ~falseSelf.planeNoise.releaseSample }, { ~falseSelf.planeNoise.playSample; });
}, bank: mixerBank2, page: page);


//// Mid Buzz:
~apc.setClipStopFunc(5, { ~falseSelf.midBuzz.free; ~apc.turnClipStopButtonOff(5) },
  bank: mixerBank2, page: page);
// playing:
~apc.setGridMonitorFunc(\midBuzz, {
  if( ~falseSelf.midBuzz.sampler.isSequencePlaying(\midBuzz),
    { ~apc.turnGridGreen(4, 2) }, { ~apc.turnGridRed(4, 2, 1); });
}, bank: mixerBank2, page: page);
~apc.setGridFunc(4, 2, {
  if( ~falseSelf.midBuzz.sampler.isSequencePlaying(\midBuzz),
    { ~falseSelf.midBuzz.stopSequence }, { ~falseSelf.midBuzz.playSequence(~falseSelf.clock); });
}, bank: mixerBank2, page: page);


//// Guitars:
~apc.setClipStopFunc(6, { ~falseSelf.freezeGuitar.free; ~apc.turnClipStopButtonOff(6) },
  bank: mixerBank2, page: page);

/*
// chord progression:
~ohm.setGridMonitorFunc(5, 4, { if( ~falseSelf.freezeGuitar.sampler.isSequencePlaying(\chordProgression),
{ ~ohm.turnGridGreen(5, 4); }, { ~ohm.turnGridRed(5, 4) });
~ohm.setGridFunc(5, 4, { if( ~falseSelf.freezeGuitar.sampler.isSequencePlaying(\chordProgression),
{ ~falseSelf.freezeGuitar.stopChordProgression }, { ~falseSelf.freezeGuitar.playChordProgression(~falseSelf.clock) });

// end progression:
~ohm.setGridMonitorFunc(6, 4, { if( ~falseSelf.freezeGuitar.sampler.isSequencePlaying(\voice1),
{ ~ohm.turnGridGreen(6, 4) }, { ~ohm.turnGridRed(6, 4) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(6, 4, { if( ~falseSelf.freezeGuitar.sampler.isSequencePlaying(\voice1),
{ ~falseSelf.freezeGuitar.stopEndProgression }, { ~falseSelf.freezeGuitar.playEndProgression(~falseSelf.clock) })
}, bank: endGridBank, page: page);
*/

//// Modular:
~apc.setClipStopFunc(7, { ~falseSelf.modular.free; ~apc.turnClipStopButtonOff(7) },
  bank: mixerBank2, page: page);

//// end trumpet:
~apc.setClipStopFunc(8, { ~falseSelf.endTrumpet.free; ~apc.turnClipStopButtonOff(8) },
  bank: mixerBank2, page: page);


/////////////////////
// Bank Change: ////
///////////////////

//// send bank changes:

~apc.setDeviceButtonsMonitorFunc(\sends, {
  if( ~apc.activeMixerEncodersBank == (send0+sendMod),
    { ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1) });
  if( ~apc.activeMixerEncodersBank  == (send1+sendMod),
    { ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2) });
  if( ~apc.activeMixerEncodersBank  == (send2+sendMod),
    { ~apc.turnDeviceButtonOn(3) }, { ~apc.turnDeviceButtonOff(3) });
  if( ~apc.activeMixerEncodersBank == (send3+sendMod),
    { ~apc.turnDeviceButtonOn(4) }, { ~apc.turnDeviceButtonOff(4) });
}, page: page);


~apc.setDeviceButtonFunc(1, { ~apc.setActiveMixerEncodersBank(send0+sendMod); }, page: page);
~apc.setDeviceButtonFunc(2, { ~apc.setActiveMixerEncodersBank(send1+sendMod);  }, page: page);
~apc.setDeviceButtonFunc(3, { ~apc.setActiveMixerEncodersBank(send2+sendMod); }, page: page);
~apc.setDeviceButtonFunc(4, { ~apc.setActiveMixerEncodersBank(send3+sendMod); }, page: page);


//// Mixer Bank Change:

~apc.turnDeviceButtonOn(5, page: page);
~apc.setDeviceButtonFunc(5, {
  ~apc.turnDeviceButtonOn(5);
  ~apc.turnDeviceButtonOff(6);
  activeMixerBank = mixerBank1;
  ~apc.setActiveClipStopBank(activeMixerBank);
  ~apc.setActiveMixerBank(activeMixerBank);
  ~apc.setActiveGridBank(activeMixerBank);
  ~apc.setActiveSceneLaunchBank(activeMixerBank);
  ~apc.setActiveMixerEncodersBank(~apc.activeMixerEncodersBank%4);
  sendMod = 0;
}, page: page);
~apc.setDeviceButtonFunc(6, {
  ~apc.turnDeviceButtonOff(5);
  ~apc.turnDeviceButtonOn(6);
  activeMixerBank = mixerBank2;
  ~apc.setActiveClipStopBank(activeMixerBank);
  ~apc.setActiveMixerBank(activeMixerBank);
  ~apc.setActiveGridBank(activeMixerBank);
  ~apc.setActiveSceneLaunchBank(activeMixerBank);
  ~apc.setActiveMixerEncodersBank((~apc.activeMixerEncodersBank%4)+4);
  sendMod = 4;
}, page: page);


//////////////////////////
///////// Mixer /////////
////////////////////////

//////// Mixer Bank 1:
8.do({ | i |
  // free colors:
  ~apc.turnClipStopButtonOn(i+1, mixerBank1, page);

  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~falseSelf.mixer.setVol(i, val.ccdbfs); }, bank: mixerBank1, page: page);

  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~falseSelf.mixer.tglMute(i); }, bank: mixerBank1, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~falseSelf.mixer.isMuted(i),
      { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, bank: mixerBank1, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~falseSelf.mixer.preOrPost(i) == 'post',
      { ~falseSelf.mixer.setSendPre(i); }, { ~falseSelf.mixer.setSendPost(i) });
  }, bank: mixerBank1, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~falseSelf.mixer.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, bank: mixerBank1, page: page);

  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~falseSelf.mixer.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~falseSelf.mixer.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~falseSelf.mixer.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~falseSelf.mixer.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~falseSelf.mixer.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~falseSelf.mixer.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~falseSelf.mixer.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~falseSelf.mixer.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

});


//// main trumpet input mute:
~apc.setMixerMonitorFunc(\mainTpt, {
  if( ~falseSelf.mainTrumpetInput.isMuted,
    { ~apc.turnRecordEnableButtonOff(5) }, { ~apc.turnRecordEnableButtonOn(5) });
}, bank: mixerBank1, page: page);

~apc.setMixerMonitorFunc(\canonTpt, {
  if( ~falseSelf.trumpetCanonInput.isMuted,
    { ~apc.turnRecordEnableButtonOff(7) }, { ~apc.turnRecordEnableButtonOn(7) });
}, bank: mixerBank1, page: page);


//////// Mixer Bank 2:

8.do({ | i |
  // free buttons:
  ~apc.turnClipStopButtonOn(i+1, mixerBank2, page);

  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~falseSelf.mixer.setVol(i+8, val.ccdbfs); }, bank: mixerBank2, page: page);

  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~falseSelf.mixer.tglMute(i+8); }, bank: mixerBank2, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~falseSelf.mixer.isMuted(i+8),
      { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, bank: mixerBank2, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~falseSelf.mixer.preOrPost(i+8) == 'post',
      { ~falseSelf.mixer.setSendPre(i+8); }, { ~falseSelf.mixer.setSendPost(i+8) });
  }, bank: mixerBank2, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~falseSelf.mixer.preOrPost(i+8) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, bank: mixerBank2, page: page);

  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~falseSelf.mixer.setSendVol(i+8, 0, val.ccdbfs); }, bank: send4, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~falseSelf.mixer.setSendVol(i+8, 1, val.ccdbfs); }, bank: send5, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~falseSelf.mixer.setSendVol(i+8, 2, val.ccdbfs); }, bank: send6, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~falseSelf.mixer.setSendVol(i+8, 3, val.ccdbfs); }, bank: send7, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~falseSelf.mixer.sendVol(i+8, 0).dbfsCC); }, bank: send4, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~falseSelf.mixer.sendVol(i+8, 1).dbfsCC); }, bank: send5, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~falseSelf.mixer.sendVol(i+8, 2).dbfsCC); }, bank: send6, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~falseSelf.mixer.sendVol(i+8, 3).dbfsCC); }, bank: send7, page: page);

});

~apc.setMixerMonitorFunc(\melodyTpt, {
  if( ~falseSelf.trumpetMelodyInput.isMuted,
    { ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
}, bank: mixerBank2, page: page);
~apc.setRecordEnableButtonFunc(1, { ~falseSelf.trumpetMelodyInput.tglMute; },
  bank: mixerBank2, page: page);

~apc.setMixerMonitorFunc(\modular, {
  if( ~falseSelf.modularInput.isMuted,
    { ~apc.turnRecordEnableButtonOff(7) }, { ~apc.turnRecordEnableButtonOn(7) });
}, bank: mixerBank2, page: page);
~apc.setRecordEnableButtonFunc(7, { ~falseSelf.modularInput.tglMute; },
  bank: mixerBank2, page: page);

~apc.setMixerMonitorFunc(\endTpt, {
  if( ~falseSelf.endTrumpetInput.isMuted,
    { ~apc.turnRecordEnableButtonOff(8) }, { ~apc.turnRecordEnableButtonOn(8) });
}, bank: mixerBank2, page: page);
~apc.setRecordEnableButtonFunc(8, { ~falseSelf.endTrumpetInput.tglMute; },
  bank: mixerBank2, page: page);



/*



~ohm.setPage('falseSelf');
~base.setPage('falseSelf');


~falseSelf.clock.schedAbs(~falseSelf.clock.beats.ceil, { | beat | beat.postln; 1 });

~falseSelf.clock.tempo = 160/60
//
~falseSelf.clock.schedAbs(~falseSelf.clock.nextBar, { ~falseSelf.clock.beatsPerBar_(4) });

~falseSelf.startSong;

~falseSelf.mixerA.tglMute(0);

~falseSelf.startMelody

*/

*/