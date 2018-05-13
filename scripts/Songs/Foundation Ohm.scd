/*
Foundation Ohm.scd
Tuesday, June 20th 2017
prm
*/

var page = 'foundation';

var activeGridBank = mainGridBank;

var mainGridBank = 0;
var endGridBank = 1;

var activeLeftBank = mixerBank1;
var mixerBank1 = 0;
var mixerBank2 = 1;
var mixerBank3 = 2;


~ohm.makePage(page);

~ohm.addGridBanks(3, page: page);
~ohm.addLeftButtonsBanks(3, page: page);
~ohm.addLeftSlidersBanks(3, page: page);
~ohm.addLeftKnobsBanks(3, page: page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);

//////// Metronome:
~ohm.addPageLoadFunction(\metronome, {
  ~foundation.clock.schedAbs(~foundation.clock.beats.ceil, { | beat |
    //~falseSelf.clock.beatInBar.postln;
    ~foundation.clock.playNextBar({
      ~ohm.turnControlButtonWhite(1, 0, page: page);
      { ~ohm.turnControlButtonOff(1, 0, page: page); }.defer(0.1);
    });
    ~ohm.turnControlButtonGreen(2, 0, page: page);
    { ~ohm.turnControlButtonOff(2, 0, page: page); }.defer(0.1);
    1;
  },page);
},page: page);

/*

//// grid bank change:
~ohm.setCrossfaderButtonMonitorFunc(0, {
  if( activeGridBank == mainGridBank,
    { ~ohm.turnCrossfaderButtonCyan(0) }, { ~ohm.turnCrossfaderButtonWhite(0) });
}, page: page);
~ohm.setCrossfaderButtonFunc(0, {
  if( activeGridBank == mainGridBank,
    { activeGridBank = endGridBank; ~ohm.setActiveGridBank(activeGridBank); },
    { activeGridBank = mainGridBank; ~ohm.setActiveGridBank(activeGridBank) });
}, page: page);
*/


//// left faders bank change:

/////////////////////
//// main grid: ////
////////////////////

//////// locations:

//////// buffer record:
// active:
~ohm.setGridMonitorFunc(0, 1, {
  if( ~foundation.songBuffers.isLoaded, { ~ohm.turnGridGreen(0, 1) }, { ~ohm.turnGridRed(0, 1); });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 1, {
  if( ~foundation.songBuffers.isLoaded, { ~foundation.songBuffers.free; });
}, bank: mainGridBank, page: page);

//// buffers:
~ohm.turnGridBlue (1, 1, bank: mainGridBank, page: page);
~ohm.turnGridCyan(2, 1, bank: mainGridBank, page: page);
~ohm.turnGridMagenta(3, 1, bank: mainGridBank, page: page);
// low d:
~ohm.setGridFunc(1, 1, { ~foundation.songBuffers.recordLowDBuffer; ~ohm.turnGridGreen(1, 1); },
  bank: mainGridBank, page: page);
// a:
~ohm.setGridFunc(2, 1, { ~foundation.songBuffers.recordABuffer; ~ohm.turnGridGreen(2, 1); },
  bank: mainGridBank, page: page);
// high d:
~ohm.setGridFunc(3, 1, { ~foundation.songBuffers.recordHighDBuffer; ~ohm.turnGridGreen(3, 1); },
  bank: mainGridBank, page: page);

/////////////////////////
//// BIG GO BUTTON: ////
///////////////////////

3.do({ | i | ~ohm.turnGridGreen(i, 2, bank: mainGridBank, page: page); });
3.do({ | i | ~ohm.turnGridGreen(i, 4, bank: mainGridBank, page: page); });
~ohm.turnGridGreen(0, 3, bank: mainGridBank, page: page);
~ohm.turnGridGreen(2, 3, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(1, 3, {
  if( ~foundation.arrivalIsPlaying, { ~ohm.turnGridWhite(1, 3) }, { ~ohm.turnGridYellow(1, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(1, 3, { ~foundation.playMainSequence; }, bank: mainGridBank, page: page);

//// clean trumpet:

~ohm.setGridMonitorFunc(0, 5, {
  if( ~foundation.cleanTrumpet.isLoaded, { ~ohm.turnGridMagenta(0, 5) }, { ~ohm.turnGridRed(0, 5) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 5, { if( ~foundation.cleanTrumpet.isLoaded, { ~foundation.cleanTrumpet.free; });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(1, 5, {
  if( ~foundation.cleanTrumpetInput.isMuted, { ~ohm.turnGridRed(1, 5) }, { ~ohm.turnGridGreen(1, 5) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(1, 5, { ~foundation.cleanTrumpetInput.tglMute }, bank: mainGridBank, page: page);

//// distorted trumpet:

~ohm.setGridMonitorFunc(0, 6, {
  if( ~foundation.trumpet.isLoaded, { ~ohm.turnGridWhite(0, 6) }, { ~ohm.turnGridRed(0, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 6, { if( ~foundation.trumpet.isLoaded, { ~foundation.trumpet.free; });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(1, 6, {
  if( ~foundation.trumpet.input.isMuted, { ~ohm.turnGridRed(1, 6) }, { ~ohm.turnGridGreen(1, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(1, 6, { ~foundation.trumpet.input.tglMute }, bank: mainGridBank, page: page);


//// moog:
~ohm.setGridMonitorFunc(4, 1, {
  if( ~foundation.moog.isLoaded, { ~ohm.turnGridYellow(4, 1) }, { ~ohm.turnGridRed(4, 1) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 1, { if( ~foundation.moog.isLoaded, { ~foundation.moog.free; });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(5, 1, {
  if( ~foundation.moog.mainSequenceIsPlaying == true, { ~ohm.turnGridGreen(5, 1) }, { ~ohm.turnGridRed(5, 1); });
}, bank: mainGridBank, page: page);
// end sequence:
~ohm.setGridMonitorFunc(6, 1, { if( ~foundation.moog.endSequenceIsPlaying,
  { ~ohm.turnGridGreen(6, 1) }, { ~ohm.turnGridRed(6, 1) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 1, { ~foundation.moog.playEndSection }, bank: mainGridBank, page: page);


//// bass synth:
~ohm.setGridMonitorFunc(4, 2, {
  if( ~foundation.bassSection.isLoaded, { ~ohm.turnGridCyan(4, 2) }, { ~ohm.turnGridRed(4, 2) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 2, { if(~foundation.bassSection.isLoaded, { ~foundation.bassSection.free; });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(5, 2, { if( ~foundation.bassSection.arrivalSequenceIsPlaying,
  { ~ohm.turnGridGreen(5, 2) }, { ~ohm.turnGridRed(5, 2) });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(6, 2, { if( ~foundation.bassSection.mainSequenceIsPlaying,
  { ~ohm.turnGridGreen(6, 2) }, { ~ohm.turnGridRed(6, 2) });
}, bank: mainGridBank, page: page);

//// cello synth:
~ohm.setGridMonitorFunc(4, 3, {
  if( ~foundation.cellos.isLoaded,
    { ~ohm.turnGridBlue(4, 3) }, { ~ohm.turnGridRed(4, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 3, { if( ~foundation.cellos.isLoaded, { ~foundation.cellos.free; });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(5, 3, { if( ~foundation.cellos.mainSequenceIsPlaying,
  { ~ohm.turnGridGreen(5, 3) }, { ~ohm.turnGridRed(5, 3) });
}, bank: mainGridBank, page: page);

//// dist trumpets:
~ohm.setGridMonitorFunc(6, 3, { if(~foundation.distTrumpets.isLoaded,
  { ~ohm.turnGridWhite(6, 3) }, { ~ohm.turnGridRed(6, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 3, { if(~foundation.distTrumpets.isLoaded, { ~foundation.distTrumpets.free });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(7, 3, { if( ~foundation.distTrumpets.arrivalSequenceIsPlaying,
  { ~ohm.turnGridGreen(7, 3) }, { ~ohm.turnGridRed(7, 3) });
}, bank: mainGridBank, page: page);

//// trumpet section (saving for later)

// SC:
~ohm.setGridMonitorFunc(4, 4, { if( ~foundation.sc.isLoaded,
  { ~ohm.turnGridMagenta(4, 4); }, { ~ohm.turnGridRed(4, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 4, { if( ~foundation.sc.isLoaded, { ~foundation.sc.free; });
}, bank: mainGridBank, page: page);
// trem:
~ohm.setGridMonitorFunc(5, 4, { if( ~foundation.sc.tremIsPlaying,
  { ~ohm.turnGridGreen(5, 4) }, { ~ohm.turnGridRed(5, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 4, { if( ~foundation.sc.tremIsPlaying,
  { ~foundation.sc.releaseTremolo }, { ~foundation.sc.playTremolo });
}, bank: mainGridBank, page: page);
// chords:
~ohm.setGridMonitorFunc(6, 4, { if( ~foundation.sc.chordsIsPlaying,
  { ~ohm.turnGridGreen(6, 4) }, { ~ohm.turnGridRed(6, 4); });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 4, { if( ~foundation.sc.chordsIsPlaying,
  { ~foundation.sc.releaseChords }, { ~foundation.sc.playChords });
}, bank: mainGridBank, page: page);

// Warps:
~ohm.setGridMonitorFunc(4, 5, { if( ~foundation.warps.isLoaded,
  { ~ohm.turnGridYellow(4, 5) }, { ~ohm.turnGridRed(4, 5) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 5, { if( ~foundation.warps.isLoaded, { ~foundation.warps.free; });
}, bank: mainGridBank, page: page);
// warp1:
~ohm.setGridMonitorFunc(5, 5, { if( ~foundation.warps.warp1IsPlaying,
  { ~ohm.turnGridGreen(5, 5) }, { ~ohm.turnGridRed(5, 5) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 5, { if( ~foundation.warps.warp1IsPlaying,
  {~foundation.warps.releaseWarp1  }, { ~foundation.warps.playWarp1 });
}, bank: mainGridBank, page: page);

// warp2:
~ohm.setGridMonitorFunc(6, 5, { if( ~foundation.warps.warp2IsPlaying,
  { ~ohm.turnGridGreen(6, 5) }, { ~ohm.turnGridRed(6, 5) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 5, { if( ~foundation.warps.warp2IsPlaying,
  {~foundation.warps.releaseWarp2  }, { ~foundation.warps.playWarp2 });
}, bank: mainGridBank, page: page);

// warp3:
~ohm.setGridMonitorFunc(7, 5, { if( ~foundation.warps.warp3IsPlaying,
  { ~ohm.turnGridGreen(7, 5) }, { ~ohm.turnGridRed(7, 5) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(7, 5, { if( ~foundation.warps.warp3IsPlaying,
  {~foundation.warps.releaseWarp3  }, { ~foundation.warps.playWarp3 });
}, bank: mainGridBank, page: page);

// warp4:
~ohm.setGridMonitorFunc(4, 6, { if( ~foundation.warps.warp4IsPlaying,
  { ~ohm.turnGridGreen(4, 6) }, { ~ohm.turnGridRed(4, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 6, { if( ~foundation.warps.warp4IsPlaying,
  {~foundation.warps.releaseWarp4  }, { ~foundation.warps.playWarp4 });
}, bank: mainGridBank, page: page);

// warp5:
~ohm.setGridMonitorFunc(5, 6, { if( ~foundation.warps.warp5IsPlaying,
  { ~ohm.turnGridGreen(5, 6) }, { ~ohm.turnGridRed(5, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 6, { if( ~foundation.warps.warp5IsPlaying,
  {~foundation.warps.releaseWarp5  }, { ~foundation.warps.playWarp5 });
}, bank: mainGridBank, page: page);

///// End Trumpet:
~ohm.setGridMonitorFunc(4, 7, { if( ~foundation.endTrumpets.isLoaded,
  { ~ohm.turnGridCyan(4, 7) }, { ~ohm.turnGridRed(4, 7) })
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 7, { if( ~foundation.endTrumpets.isLoaded, { ~foundation.endTrumpets.free; });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(5, 7, { if( ~foundation.endTrumpets.endSequenceIsPlaying,
  { ~ohm.turnGridGreen(5, 7) }, { ~ohm.turnGridRed(5, 7) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 7, { if( ~foundation.endTrumpets.endSequenceIsPlaying,
  { ~foundation.endTrumpets.stopEndSequence; }, { ~foundation.endTrumpets.playEndSequence });
}, bank: mainGridBank, page: page);

//// chord synth:
~ohm.setGridMonitorFunc(6, 7, {
  if( ~foundation.chordSynth.isLoaded, { ~ohm.turnGridWhite(6, 7) }, { ~ohm.turnGridRed(6, 7) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 7, { if( ~foundation.chordSynth.isLoaded, { ~foundation.chordSynth.free; });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(7, 7, { if( ~foundation.chordSynth.mainSequenceIsPlaying,
  { ~ohm.turnGridGreen(7, 7) }, { ~ohm.turnGridRed(7, 7) });
}, bank: mainGridBank, page: page);

/////////////////////
// Bank Change: ////
///////////////////

//// Bank Change:
~ohm.setGridMonitorFunc(0, 7, {
  3.do({ | i | ~ohm.turnGridCyan(i, 7) });
  ~ohm.turnGridGreen(activeLeftBank, 7);
}, bank: mainGridBank, page: page);

// mix bank 1:
~ohm.setGridFunc(0, 7, {
  activeLeftBank = mixerBank1;
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  //~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, bank: mainGridBank, page: page);

// mix bank 2:
~ohm.setGridFunc(1, 7, {
  activeLeftBank = mixerBank2;
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  //~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, bank: mainGridBank, page: page);

// mix bank 3:
~ohm.setGridFunc(2, 7, {
  activeLeftBank = mixerBank3;
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  //~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, bank: mainGridBank, page: page);


/////////////////////
/// Left Side: /////
///////////////////

//////// Mix Bank 1:

//// Moog:
// Button:
~ohm.setLeftButtonMonitorFunc(0, { if( ~foundation.mixerA.isMuted(0),
  { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, bank: mixerBank1, page: page);
~ohm.setLeftButtonFunc(0, { ~foundation.mixerA.tglMute(0) }, bank: mixerBank1, page: page);
// Fader:
~ohm.setLeftSliderFunc(0, { | val |
  ~foundation.mixerA.setVol(0, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);

//// Bass Section:
// Button:
~ohm.setLeftButtonMonitorFunc(1, { if( ~foundation.mixerB.isMuted(0),
  { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, bank: mixerBank1, page: page);
~ohm.setLeftButtonFunc(1, { ~foundation.mixerB.tglMute(0) }, bank: mixerBank1, page: page);
// Fader:
~ohm.setLeftSliderFunc(1, { | val |
  ~foundation.mixerB.setVol(0, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);

//// Clean Trumpet:
// Button:
~ohm.setLeftButtonMonitorFunc(2, { if( ~foundation.mixerC.isMuted(0),
  { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, bank: mixerBank1, page: page);
~ohm.setLeftButtonFunc(2, { ~foundation.mixerC.tglMute(0) }, bank: mixerBank1, page: page);
// Fader:
~ohm.setLeftSliderFunc(2, { | val |
  ~foundation.mixerC.setVol(0, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);

//// Distorted Trumpet:
// Button:
~ohm.setLeftButtonMonitorFunc(3, { if( ~foundation.mixerC.isMuted(1),
  { ~ohm.turnLeftButtonRed(3) }, { ~ohm.turnLeftButtonGreen(3) });
}, bank: mixerBank1, page: page);
~ohm.setLeftButtonFunc(3, { ~foundation.mixerC.tglMute(1) }, bank: mixerBank1, page: page);
// Fader:
~ohm.setLeftSliderFunc(3, { | val |
  ~foundation.mixerC.setVol(1, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);

//////// Mix Bank 2:

//// SC:
// Button:
~ohm.setLeftButtonMonitorFunc(0, { if( ~foundation.mixerA.isMuted(2),
  { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, bank: mixerBank2, page: page);
~ohm.setLeftButtonFunc(0, { ~foundation.mixerA.tglMute(2) }, bank: mixerBank2, page: page);
// Fader:
~ohm.setLeftSliderFunc(0, { | val |
  ~foundation.mixerA.setVol(2, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);

//// Warps:
// Button:
~ohm.setLeftButtonMonitorFunc(1, { if( ~foundation.mixerA.isMuted(3),
  { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, bank: mixerBank2, page: page);
~ohm.setLeftButtonFunc(1, { ~foundation.mixerA.tglMute(3) }, bank: mixerBank2, page: page);
// Fader:
~ohm.setLeftSliderFunc(1, { | val |
  ~foundation.mixerA.setVol(3, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);

//// End Trumpets:
// Button:
~ohm.setLeftButtonMonitorFunc(2, { if( ~foundation.mixerC.isMuted(4),
  { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, bank: mixerBank2, page: page);
~ohm.setLeftButtonFunc(2, { ~foundation.mixerC.tglMute(4) }, bank: mixerBank2, page: page);
// Fader:
~ohm.setLeftSliderFunc(2, { | val |
  ~foundation.mixerC.setVol(4, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);

//// Chord Synth:
// Button:
~ohm.setLeftButtonMonitorFunc(3, { if( ~foundation.mixerA.isMuted(1),
  { ~ohm.turnLeftButtonRed(3) }, { ~ohm.turnLeftButtonGreen(3) });
}, bank: mixerBank2, page: page);
~ohm.setLeftButtonFunc(3, { ~foundation.mixerA.tglMute(1) }, bank: mixerBank2, page: page);
// Fader:
~ohm.setLeftSliderFunc(3, { | val |
  ~foundation.mixerA.setVol(1, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);

//////// Mix Bank 3:

//// trumpet section:
// Button:
~ohm.setLeftButtonMonitorFunc(0, { if( ~foundation.mixerC.isMuted(2),
  { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, bank: mixerBank3, page: page);
~ohm.setLeftButtonFunc(0, { ~foundation.mixerC.tglMute(2) }, bank: mixerBank3, page: page);
// Fader:
~ohm.setLeftSliderFunc(0, { | val |
  ~foundation.mixerC.setVol(2, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);

//// dist trumpets:
// Button:
~ohm.setLeftButtonMonitorFunc(1, { if( ~foundation.mixerC.isMuted(3),
  { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, bank: mixerBank3, page: page);
~ohm.setLeftButtonFunc(1, { ~foundation.mixerC.tglMute(3) }, bank: mixerBank3, page: page);
// Fader:
~ohm.setLeftSliderFunc(1, { | val |
  ~foundation.mixerC.setVol(3, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);

//// cellos:
// Button:
~ohm.setLeftButtonMonitorFunc(2, { if( ~foundation.mixerB.isMuted(1),
  { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, bank: mixerBank3, page: page);
~ohm.setLeftButtonFunc(2, { ~foundation.mixerB.tglMute(1) }, bank: mixerBank3, page: page);
// Fader:
~ohm.setLeftSliderFunc(2, { | val |
  ~foundation.mixerB.setVol(1, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);

//////////////////////
//// Left Knobs: ////
////////////////////

//// Moog High Pass:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var freq = val.linexp(0, 127, 20, 1310);
  ~foundation.moog.eq.setHighPassCutoff(freq);
}, bank: mixerBank1, page: page);

//// Moog Low Pass:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var freq = val.linexp(0, 127, 3010, 20000);
  ~foundation.moog.eq.setLowPassCutoff(freq);
}, bank: mixerBank1, page: page);

//// Delay Feedback:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var fb = val.linlin(0, 127, 0, 0.95);
  ~foundationDelay.setFeedback(fb);
}, bank: mixerBank1, page: page);

//// Granulator:
// feedback:
/*
~ohm.setLeftKnobFunc(0, 1, { | val |
  var fb = val.linlin(0, 127, 0, 1);
  ~prm.granulator.setFeedback(fb);
}, bank: mixerBank1, page: page);
// trig rate:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var rate = val.linlin(0, 127, 24, 64);
  ~prm.granulator.setTrigRate(rate);
}, bank: mixerBank1, page: page);
*/

//// distorted trumpet granulator send:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~foundation.mixerC.setSendVol(1, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);

//// end trumpets granulator send:
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~foundation.mixerC.setSendVol(4, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);


