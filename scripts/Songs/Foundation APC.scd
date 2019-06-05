/*
Foundation APC.scd
Tuesday, June 20th 2017
prm
*/

var page = 'foundation';

var activeGridBank = mainGridBank;

var mainGridBank = 0;
var endGridBank = 1;

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

~apc.addGridBanks(3, page: page);
~apc.addMixerEncodersBanks(7, page: page);
~apc.addClipStopBanks(1, page: page);
~apc.addMixerBanks(1, page: page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);

//////// Modular Clock:
~apc.setPanButtonFunc({
  ~foundation.modularClock.togglePlay;
  if( ~foundation.modularClock.isPlaying,
    { ~apc.turnPanButtonOn }, { ~apc.turnPanButtonOff });
}, page: page);


//////// Metronome:
~apc.addPageLoadFunction(\metronome, {
  ~foundation.clock.schedAbs(~foundation.clock.beats.ceil, { | beat |
    ~foundation.clock.playNextBar({
      ~apc.turnSendsButtonOn(page: page);
      { ~apc.turnSendsButtonOff(page: page) }.defer(0.1);
    });
    ~apc.turnMetronomeButtonOn(page: page);
    { ~apc.turnMetronomeButtonOff(page: page); }.defer(0.1);
    1;
  });
}, page: page);



/////////////////////
//// main grid: ////
////////////////////

//////// locations:
//////// buffer record:
// active:
~apc.setGridMonitorFunc(\bufs, {
  if( ~foundation.songBuffers.isLoaded, { ~apc.turnGridGreen(0, 4) }, { ~apc.turnGridRed(0, 4); });
}, bank: mainGridBank, page: page);
~apc.setGridFunc(0, 4, {
  if( ~foundation.songBuffers.isLoaded, { ~foundation.songBuffers.free; });
}, bank: mainGridBank, page: page);

//// buffers:
~apc.turnGridBlue (1, 4, bank: mainGridBank, page: page);
~apc.turnGridCyan(2, 4, bank: mainGridBank, page: page);
~apc.turnGridMagenta(3, 4, bank: mainGridBank, page: page);
// low d:
~apc.setGridFunc(1, 4, { ~foundation.songBuffers.recordLowDBuffer; ~apc.turnGridGreen(1, 4); },
  bank: mainGridBank, page: page);
// a:
~apc.setGridFunc(2, 4, { ~foundation.songBuffers.recordABuffer; ~apc.turnGridGreen(2, 4); },
  bank: mainGridBank, page: page);
// high d:
~apc.setGridFunc(3, 4, { ~foundation.songBuffers.recordHighDBuffer; ~apc.turnGridGreen(3, 4); },
  bank: mainGridBank, page: page);

/////////////////////////
//// BIG GO BUTTON: ////
///////////////////////
~apc.setSceneLaunchMonitorFunc(\arrival, {
  if( ~foundation.arrivalIsPlaying,
    { ~apc.turnSceneLaunchButtonWhite(1) }, { ~apc.turnSceneLaunchButtonGreen(1, 0); });
}, bank: mainGridBank, page: page);
~apc.setSceneLaunchFunc(1, { ~foundation.playMainSequence; }, bank: mainGridBank, page: page);

/////// 1 - moog:
~apc.setClipStopFunc(1, { ~foundation.moog.free; ~apc.turnClipStopButtonOff(1) }, bank: mixerBank1, page: page);

~apc.setGridMonitorFunc(\moogMain, {
  if( ~foundation.moog.mainSequenceIsPlaying == true,
    { ~apc.turnGridGreen(0, 3) }, { ~apc.turnGridYellow(0, 3); });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(0, 3,  { ~foundation.moog.playEndSection }, bank: mixerBank1, page: page);

// end sequence:
~apc.setGridMonitorFunc(\moogEnd,  { if( ~foundation.moog.endSequenceIsPlaying,
  { ~apc.turnGridGreen(0, 2) }, { ~apc.turnGridPurple(0, 2); });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(0, 2, { ~foundation.moog.playEndSection }, bank: mixerBank1, page: page);


/////// 2 - Clean Trumpet:
~apc.setClipStopFunc(2,
  {  ~foundation.cleanTrumpet.free; ~apc.turnClipStopButtonOff(2) }, bank: mixerBank1, page: page);

//////// buffer record:
// active:
~apc.setGridMonitorFunc(\bufs, {
  if( ~foundation.songBuffers.isLoaded, { ~apc.turnGridGreen(1, 4) }, { ~apc.turnGridRed(1, 4); });
}, bank: mainGridBank, page: page);
~apc.setGridFunc(1, 4, {
  if( ~foundation.songBuffers.isLoaded, { ~foundation.songBuffers.free; });
}, bank: mainGridBank, page: page);

//// buffers:
~apc.turnGridBlue (1, 3, bank: mainGridBank, page: page);
~apc.turnGridCyan(1, 2, bank: mainGridBank, page: page);
~apc.turnGridMagenta(1, 1, bank: mainGridBank, page: page);
// low d:
~apc.setGridFunc(1, 3, { ~foundation.songBuffers.recordLowDBuffer; ~apc.turnGridGreen(1, 3); },
  bank: mainGridBank, page: page);
// a:
~apc.setGridFunc(1, 2, { ~foundation.songBuffers.recordABuffer; ~apc.turnGridGreen(1, 2); },
  bank: mainGridBank, page: page);
// high d:
~apc.setGridFunc(1, 1, { ~foundation.songBuffers.recordHighDBuffer; ~apc.turnGridGreen(1, 1); },
  bank: mainGridBank, page: page);


/////// 3 - Distorted Trumpet:
~apc.setClipStopFunc(3, { ~foundation.trumpet.free; ~apc.turnClipStopButtonOff(3) },
  bank: mixerBank1, page: page);

/////// 4 - bass section:
// free:
~apc.setClipStopFunc(4, { ~foundation.bassSection.free; ~apc.turnClipStopButtonOff(4) },
  bank: mixerBank1, page: page);
// arrival:
~apc.setGridMonitorFunc(\bassArrival, { if( ~foundation.bassSection.arrivalSequenceIsPlaying,
  { ~apc.turnGridGreen(3, 3) }, { ~apc.turnGridYellow(3, 3) });
}, bank: mixerBank1, page: page);
// main sequence:
~apc.setGridMonitorFunc(\bassMain, { if( ~foundation.bassSection.mainSequenceIsPlaying,
  { ~apc.turnGridGreen(3, 2) }, { ~apc.turnGridPink(3, 2); });
}, bank: mixerBank1, page: page);

/////// 5 - chord synth:
// free:
~apc.setClipStopFunc(5, { ~foundation.chordSynth.free; ~apc.turnClipStopButtonOff(5) },
  bank: mixerBank1, page: page);
// main:
~apc.setGridMonitorFunc(\chordSynthMain, { if( ~foundation.chordSynth.mainSequenceIsPlaying,
  { ~apc.turnGridGreen(4, 3); }, { ~apc.turnGridYellow(4, 3) });
}, bank: mixerBank1, page: page);


////// 6 - SC:
// free:
~apc.setClipStopFunc(6, { ~foundation.sc.free; ~apc.turnClipStopButtonOff(6); },
  bank: mixerBank1, page: page);
// trem:
~apc.setGridMonitorFunc(\scTrem, {if( ~foundation.sc.tremIsPlaying,
  { ~apc.turnGridGreen(5, 2); }, { ~apc.turnGridMagenta(5, 2); });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(5, 2, { if( ~foundation.sc.tremIsPlaying,
  { ~foundation.sc.releaseTremolo }, { ~foundation.sc.playTremolo });
}, bank: mixerBank1, page: page);
// chords:
~apc.setGridMonitorFunc(\scChords, { if( ~foundation.sc.chordsIsPlaying,
  { ~apc.turnGridGreen(5, 1) }, { ~apc.turnGridBlue(5, 1); });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(5, 1, { if( ~foundation.sc.chordsIsPlaying,
  { ~foundation.sc.releaseChords }, { ~foundation.sc.playChords });
}, bank: mixerBank1, page: page);


////// 7 - Warps:
// free:
~apc.setClipStopFunc(7, { ~foundation.warps.free; ~apc.turnClipStopButtonOff(7) },
  bank: mixerBank1, page: page);
// warp1:
~apc.setGridMonitorFunc(\warp1, { if( ~foundation.warps.warp1IsPlaying,
  { ~apc.turnGridGreen(6, 4) }, { ~apc.turnGridPink(6, 4, 1) });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(6, 4, { if( ~foundation.warps.warp1IsPlaying,
  {~foundation.warps.releaseWarp1  }, { ~foundation.warps.playWarp1 });
}, bank: mixerBank1, page: page);
// warp 2:
~apc.setGridMonitorFunc(\warp2, { if( ~foundation.warps.warp2IsPlaying,
  { ~apc.turnGridGreen(6, 3) }, { ~apc.turnGridPurple(6, 3, 1) });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(6, 3, { if( ~foundation.warps.warp2IsPlaying,
  {~foundation.warps.releaseWarp2  }, { ~foundation.warps.playWarp2 });
}, bank: mixerBank1, page: page);
// warp 3:
~apc.setGridMonitorFunc(\warp3, {  if( ~foundation.warps.warp3IsPlaying,
  { ~apc.turnGridGreen(6, 2) }, { ~apc.turnGridMagenta(6, 2, 1); });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(6, 2, { if( ~foundation.warps.warp3IsPlaying,
  {~foundation.warps.releaseWarp3  }, { ~foundation.warps.playWarp3 });
}, bank: mixerBank1, page: page);
// warp 4:
~apc.setGridMonitorFunc(\warp4, { if( ~foundation.warps.warp4IsPlaying,
  { ~apc.turnGridGreen(6, 1) }, { ~apc.turnGridYellow(6, 1, 1); });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(6, 1, { if( ~foundation.warps.warp4IsPlaying,
  {~foundation.warps.releaseWarp4  }, { ~foundation.warps.playWarp4 });
}, bank: mixerBank1, page: page);
// warp 5:
~apc.setGridMonitorFunc(\warp5, {  if( ~foundation.warps.warp5IsPlaying,
  { ~apc.turnGridGreen(6, 0) }, { ~apc.turnGridBlue(6, 0, 1); });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(6, 0, { if( ~foundation.warps.warp5IsPlaying,
  {~foundation.warps.releaseWarp5  }, { ~foundation.warps.playWarp5 });
}, bank: mixerBank1, page: page);


// 8 - end trumpets:
// free:
~apc.setClipStopFunc(8, { ~foundation.endTrumpets.free; ~apc.turnClipStopButtonOff(8) },
  bank: mixerBank1, page: page);
~apc.setGridMonitorFunc(\endTrumpets, { if( ~foundation.endTrumpets.endSequenceIsPlaying,
  { ~apc.turnGridGreen(7, 0) }, { ~apc.turnGridBlue(7, 0) });
}, bank: mixerBank1, page: page);
~apc.setGridFunc(7, 0, { if( ~foundation.endTrumpets.endSequenceIsPlaying,
  { ~foundation.endTrumpets.stopEndSequence; }, { ~foundation.endTrumpets.playEndSequence });
}, bank: mixerBank1, page: page);

// 9 - trumpet section:
~apc.setClipStopFunc(1, { ~foundation.distTrumpets.free; ~apc.turnClipStopButtonOff(1) },
  bank: mixerBank2, page: page);

// 10 - distorted trumpets:
~apc.setClipStopFunc(2, { ~foundation.distTrumpets.free; ~apc.turnClipStopButtonOff(2); },
  bank: mixerBank2, page: page);
~apc.setGridMonitorFunc(\distTrumpets, { if( ~foundation.distTrumpets.arrivalSequenceIsPlaying,
  { ~apc.turnGridGreen(1, 3); }, { ~apc.turnGridYellow(1, 3, 1) });
}, bank: mixerBank2, page: page);

// 11 - cellos:
~apc.setClipStopFunc(3, { ~foundation.cellos.free; ~apc.turnClipStopButtonOff(3); },
  bank: mixerBank2, page: page);
~apc.setGridMonitorFunc(\cellos, { if( ~foundation.cellos.mainSequenceIsPlaying,
  { ~apc.turnGridGreen(2, 2) }, { ~apc.turnGridPink(2, 2, 1); });
}, bank: mixerBank2, page: page);


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
  ~apc.setActiveMixerEncodersBank((~apc.activeMixerEncodersBank%4)+4);
  sendMod = 4;
}, page: page);


/////////////////////
/////  Mixer:  /////
///////////////////

//////// Mixer Bank 1:

8.do({ | i |
  // free colors:
  ~apc.turnClipStopButtonOn(i+1, mixerBank1, page);

  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~foundation.mixer.setVol(i, val.ccdbfs); }, bank: mixerBank1, page: page);

  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~foundation.mixer.tglMute(i); }, bank: mixerBank1, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~foundation.mixer.isMuted(i),
      { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, bank: mixerBank1, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~foundation.mixer.preOrPost(i) == 'post',
      { ~foundation.mixer.setSendPre(i); }, { ~foundation.mixer.setSendPost(i) });
  }, bank: mixerBank1, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~foundation.mixer.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, bank: mixerBank1, page: page);

  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~foundation.mixer.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~foundation.mixer.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~foundation.mixer.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~foundation.mixer.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~foundation.mixer.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~foundation.mixer.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~foundation.mixer.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~foundation.mixer.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

});

// moog input mute:
~apc.setMixerMonitorFunc(\moogInput, {
  if( ~foundation.moog.moog.hardwareIn.isMuted,
    { ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
}, bank: mixerBank1, page: page);
~apc.setRecordEnableButtonFunc(1, { ~foundation.moog.moog.hardwareIn.tglMute },
  bank: mixerBank1, page: page);
// clean trumpet input:
~apc.setMixerMonitorFunc(\cleanTptInput, {
  if( ~foundation.cleanTrumpetInput.isMuted,
    { ~apc.turnRecordEnableButtonOff(2) }, { ~apc.turnRecordEnableButtonOn(2) });
}, bank: mixerBank1, page: page);
~apc.setRecordEnableButtonFunc(2, { ~foundation.cleanTrumpetInput.tglMute },
  bank: mixerBank1, page: page);
// dist trumpet input:
~apc.setMixerMonitorFunc(\distTptInput, {
  if( ~foundation.trumpet.input.isMuted,
    { ~apc.turnRecordEnableButtonOff(3) }, { ~apc.turnRecordEnableButtonOn(3) });
}, bank: mixerBank1, page: page);
~apc.setRecordEnableButtonFunc(3, { ~foundation.trumpet.input.tglMute },
  bank: mixerBank1, page: page);


//////// Mixer Bank 2:

3.do({ | i |
  // free buttons:
  ~apc.turnClipStopButtonOn(i+1, mixerBank2, page);

  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~foundation.mixer.setVol(i+8, val.ccdbfs); }, bank: mixerBank2, page: page);

  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~foundation.mixer.tglMute(i+8); }, bank: mixerBank2, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~foundation.mixer.isMuted(i+8),
      { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, bank: mixerBank2, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~foundation.mixer.preOrPost(i+8) == 'post',
      { ~foundation.mixer.setSendPre(i+8); }, { ~foundation.mixer.setSendPost(i+8) });
  }, bank: mixerBank2, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~foundation.mixer.preOrPost(i+8) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, bank: mixerBank2, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~foundation.mixer.sendVol(i+8, 0).dbfsCC); }, bank: send4, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~foundation.mixer.sendVol(i+8, 1).dbfsCC); }, bank: send5, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~foundation.mixer.sendVol(i+8, 2).dbfsCC); }, bank: send6, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~foundation.mixer.sendVol(i+8, 3).dbfsCC); }, bank: send7, page: page);

});



///////////////////////////
//// Device Encoders: ////
/////////////////////////

//// Moog High Pass:
~apc.setDeviceEncodersMonitorFunc(\moogHighPass, {
  var freq = ~foundation.moog.eq.highPassCutoff.explin(20, 1310, 0, 127);
  ~apc.setDeviceEncoderValue(1, freq);
}, page: page);
~apc.setDeviceEncoderFunc(1, { | val |
  var freq = val.linexp(0, 127, 20, 1310);
  ~foundation.moog.eq.setHighPassCutoff(freq);
}, page: page);

//// Moog Low Pass:
~apc.setDeviceEncodersMonitorFunc(\moogLowPass, {
  var freq = ~foundation.moog.eq.lowPassCutoff.explin(3010, 20000, 0, 127);
  ~apc.setDeviceEncoderValue(2, freq);
}, page: page);
~apc.setDeviceEncoderFunc(2, { | val |
  var freq = val.linexp(0, 127, 3010, 20000);
  ~foundation.moog.eq.setLowPassCutoff(freq);
}, page: page);

//// Delay Feedback:
~apc.setDeviceEncodersMonitorFunc(\delayFB, {
  var fb = ~foundationDelay.feedback.linlin(0, 0.95, 0, 127);
  ~apc.setDeviceEncoderValue(3, fb);
}, page: page);
~apc.setDeviceEncoderFunc(3, { | val |
  var fb = val.linlin(0, 127, 0, 0.95);
  ~foundationDelay.setFeedback(fb);
}, page: page);
