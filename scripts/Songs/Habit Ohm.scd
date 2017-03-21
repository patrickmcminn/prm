/*
Sunday, March 19th 2017
Habit Ohm.scd
prm
*/

var page = 'habit';

var mainMixBank = 0;
var trumpetLooperBank = 1;
var leftKnobMiscBank = 1;

var sequenceResetFunc;

var activeSlidersBank = mainMixBank;
var activeLeftKnobsBank = mainMixBank;

var pitchArray = [1, 3, 4, 6, 8, 9, 11];

~ohm.makePage(page);

~ohm.addLeftButtonsBanks(1, page: page);
~ohm.addLeftSlidersBanks(1, page: page);
~ohm.addLeftKnobsBanks(1, page: page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);

////////// Left Side:

//////// Main Mix Bank:

////// Moog:

//// Mute:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~habit.moog.mixer.isMuted, { ~ohm.turnLeftButtonRed(0); }, { ~ohm.turnLeftButtonGreen(0); }); },
  bank: mainMixBank, page: page);
~ohm.setLeftButtonFunc(0, { ~habit.moog.mixer.tglMute; }, bank: mainMixBank, page: page);
//// Volume:
~ohm.setLeftSliderFunc(0, { | val | ~habit.moog.mixer.setVol(val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(0, 2, { | val | ~habit.moog.mixer.setSendVol(0, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
// Granulator:
~ohm.setLeftKnobFunc(0, 1, { | val | ~habit.moog.mixer.setSendVol(1, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
// Modular:
~ohm.setLeftKnobFunc(0, 0, { | val | ~habit.moog.mixer.setSendVol(2, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);

////// Modular:

//// Mute:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~habit.modular.isMuted, { ~ohm.turnLeftButtonRed(1); }, { ~ohm.turnLeftButtonGreen(1); }); },
  bank: mainMixBank, page: page);
~ohm.setLeftButtonFunc(1, { ~habit.modular.tglMute; }, bank: mainMixBank, page: page);
//// Volume:
~ohm.setLeftSliderFunc(1, { | val | ~habit.modular.setVol(val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(1, 2, { | val | ~habit.modular.setSendVol(0, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
// Granulator:
~ohm.setLeftKnobFunc(1, 1, { | val | ~habit.modular.setSendVol(1, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
// Modular:
~ohm.setLeftKnobFunc(1, 0, { | val | ~habit.modular.setSendVol(2, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);

////// Main Trumpet:

//// Mute:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~habit.trumpet.mixer.isMuted, { ~ohm.turnLeftButtonRed(2); }, { ~ohm.turnLeftButtonGreen(2); }); },
  bank: mainMixBank, page: page);
~ohm.setLeftButtonFunc(2, { ~habit.trumpet.mixer.tglMute; }, bank: mainMixBank, page: page);
//// Volume:
~ohm.setLeftSliderFunc(2, { | val | ~habit.trumpet.mixer.setVol(val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(2, 2, { | val | ~habit.trumpet.mixer.setSendVol(0, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
// Granulator:
~ohm.setLeftKnobFunc(2, 1, { | val | ~habit.trumpet.mixer.setSendVol(1, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
// Modular:
~ohm.setLeftKnobFunc(2, 0, { | val | ~habit.trumpet.mixer.setSendVol(2, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);

////// Trumpet Looper Submixer:

//// Mute:
~ohm.setLeftButtonMonitorFunc(3, {
  if( ~habit.trumpetLoopers.mixer.isMuted, { ~ohm.turnLeftButtonRed(3); }, { ~ohm.turnLeftButtonGreen(3); }); },
  bank: mainMixBank, page: page);
~ohm.setLeftButtonFunc(3, { ~habit.trumpetLoopers.mixer.tglMute; }, bank: mainMixBank, page: page);
//// Volume:
~ohm.setLeftSliderFunc(3, { | val | ~habit.trumpetLoopers.mixer.setVol(val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
//// Sends:
// Reverb:
~ohm.setLeftKnobFunc(3, 2, { | val | ~habit.trumpetLoopers.mixer.setSendVol(0, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
// Granulator:
~ohm.setLeftKnobFunc(3, 1, { | val | ~habit.trumpetLoopers.mixer.setSendVol(1, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);
// Modular:
~ohm.setLeftKnobFunc(3, 0, { | val | ~habit.trumpetLoopers.mixer.setSendVol(2, val.ccdbfs(-70, 0)); },
  bank: mainMixBank, page: page);

//////// Looper Mix Bank:

//// Looper 1:
// Mute:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~habit.trumpetLoopers.mixer.isMuted(0), { ~ohm.turnLeftButtonRed(0); }, { ~ohm.turnLeftButtonGreen(0); });
}, bank: trumpetLooperBank, page: page);
~ohm.setLeftButtonFunc(0, { ~habit.trumpetLoopers.mixer.tglMute(0); }, bank: trumpetLooperBank, page: page);
// Volume:
~ohm.setLeftSliderFunc(0, { | val | ~habit.trumpetLoopers.mixer.setVol(0, val.ccdbfs(-70, 0));
}, bank: trumpetLooperBank, page: page);

//// Looper 2:
// Mute:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~habit.trumpetLoopers.mixer.isMuted(1), { ~ohm.turnLeftButtonRed(1); }, { ~ohm.turnLeftButtonGreen(1); });
}, bank: trumpetLooperBank, page: page);
~ohm.setLeftButtonFunc(1, { ~habit.trumpetLoopers.mixer.tglMute(1); }, bank: trumpetLooperBank, page: page);
// Volume:
~ohm.setLeftSliderFunc(1, { | val | ~habit.trumpetLoopers.mixer.setVol(1, val.ccdbfs(-70, 0));
}, bank: trumpetLooperBank, page: page);

//// Looper 3:
// Mute:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~habit.trumpetLoopers.mixer.isMuted(2), { ~ohm.turnLeftButtonRed(2); }, { ~ohm.turnLeftButtonGreen(2); });
}, bank: trumpetLooperBank, page: page);
~ohm.setLeftButtonFunc(2, { ~habit.trumpetLoopers.mixer.tglMute(2); }, bank: trumpetLooperBank, page: page);
// Volume:
~ohm.setLeftSliderFunc(2, { | val | ~habit.trumpetLoopers.mixer.setVol(2, val.ccdbfs(-70, 0));
}, bank: trumpetLooperBank, page: page);


//// Looper 4:
// Mute:
~ohm.setLeftButtonMonitorFunc(3, {
  if( ~habit.trumpetLoopers.mixer.isMuted(3), { ~ohm.turnLeftButtonRed(3); }, { ~ohm.turnLeftButtonGreen(3); });
}, bank: trumpetLooperBank, page: page);
~ohm.setLeftButtonFunc(3, { ~habit.trumpetLoopers.mixer.tglMute(3); }, bank: trumpetLooperBank, page: page);
// Volume:
~ohm.setLeftSliderFunc(3, { | val | ~habit.trumpetLoopers.mixer.setVol(3, val.ccdbfs(-70, 0));
}, bank: trumpetLooperBank, page: page);

//////// Left Knob Misc. Bank:

//// Tempo:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var tempo = val.linexp(0, 127, 20, 1200);
  ~habit.setClockTempo(tempo);
}, bank: leftKnobMiscBank, page: page);

//// Delay:
// Volume:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~habit.delay.mixer.setVol(val.ccdbfs(-70, 0));
}, bank: leftKnobMiscBank, page: page);
// Delay Time:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var delayTime = val.linlin(0, 127, 0.001, 5);
  ~habit.delay.setDelayTime(delayTime);
}, bank: leftKnobMiscBank, page: page);
// feedback:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var feedback = val.linlin(0, 127, 0, 1);
  ~habit.delay.setFeedback(feedback);
}, bank: leftKnobMiscBank, page: page);

//// Main Granulator:
// Volume:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~prm.granulator.mixer.setVol(val.ccdbfs);
}, bank: leftKnobMiscBank, page: page);
// Trig Rate:
~ohm.setLeftKnobFunc(1, 2, {  | val |
  var trigRate = val.linlin(0, 127, 0, 100);
  ~prm.granulator.setTrigRate(trigRate);
}, bank: leftKnobMiscBank, page: page);
// Grain Dur Low:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var dur = val.linlin(0, 127, 0.001, 0.5);
  ~prm.granulator.setGrainDurLow(dur);
}, bank: leftKnobMiscBank, page: page);
// Grain Dur High:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var dur = val.linlin(0, 127, 0.005, 3);
  ~prm.granulator.setGrainDurHigh(dur);
}, bank: leftKnobMiscBank, page: page);

//////// grid:

//// sequences:

sequenceResetFunc = {
  ~ohm.turnGridCyan(0, 0);
  ~ohm.turnGridMagenta(1, 0);
  ~ohm.turnGridBlue(2, 0);
  ~ohm.turnGridCyan(3, 0);
  ~ohm.turnGridYellow(4, 0);
  ~ohm.turnGridWhite(5, 0);
  ~ohm.turnGridRed(6, 0);
};

~ohm.setGridMonitorFunc(0, 0, {
  sequenceResetFunc.value;
  switch(~habit.moog.sequencePlaying,
    'sequence1', { ~ohm.turnGridGreen(0, 0); },
    'sequence2', { ~ohm.turnGridGreen(1, 0); },
    'sequence3', { ~ohm.turnGridGreen(2, 0) },
    'sequence4', { ~ohm.turnGridGreen(3, 0) },
    'sectionEndLoop', { ~ohm.turnGridGreen(4, 0) },
    'sectionEnd', { ~ohm.turnGridGreen(5, 0) },
    'pieceEnd', { ~ohm.turnGridGreen(6, 0) }
  );
}, page: page);

~ohm.setGridFunc(0, 0, { ~habit.moog.playSequence1(~habit.clock); }, page: page);
~ohm.setGridFunc(1, 0, { ~habit.moog.playSequence2(~habit.clock); }, page: page);
~ohm.setGridFunc(2, 0, { ~habit.moog.playSequence3(~habit.clock); }, page: page);
~ohm.setGridFunc(3, 0, { ~habit.moog.playSequence4(~habit.clock); }, page: page);
~ohm.setGridFunc(4, 0, { ~habit.moog.playSectionEndLoop(~habit.clock); }, page: page);
~ohm.setGridFunc(5, 0, { ~habit.moog.playSectionEnd(~habit.clock); }, page: page);
~ohm.setGridFunc(6, 0, { ~habit.moog.playPieceEnd(~habit.clock); }, page: page);

//// notes being played:
~ohm.setGridMonitorFunc(0, 1, {
  8.do({ | i | ~ohm.turnGridWhite(i, 1); });
  if( ~habit.moog.notePlaying.isInteger, { ~ohm.turnGridGreen(pitchArray.atIdentityHash(~habit.moog.notePlaying), 1) },
    { 8.do({ | i | ~ohm.turnGridWhite(i, 1); }); });
}, page: page);


// (row 1)

////// Looper (rows 2-4);
//// Looper 1:
// Loop:
~ohm.setGridMonitorFunc(0, 4, {
  if( ~habit.trumpetLoopers.looper1.isRecording, { ~ohm.turnGridWhite(0, 4); },
    if( ~habit.trumpetLoopers.looper1.isPlaying,
      { ~ohm.turnGridGreen(0, 4); },
      { ~ohm.turnGridBlue(0, 4); });
  );
}, page: page);
~ohm.setGridFunc(0, 4, { ~habit.trumpetLoopers.looper1.loop; }, page: page);
// play:
~ohm.setGridMonitorFunc(0, 3, {
  if( ~habit.trumpetLoopers.looper1.isPlaying, { ~ohm.turnGridGreen(0, 3); }, { ~ohm.turnGridRed(0, 3); });
}, page: page);
~ohm.setGridFunc(0, 3, { ~habit.trumpetLoopers.looper1.togglePlayLoop; }, page: page);
// erase:
~ohm.turnGridRed(0, 2, page: page);
~ohm.setGridFunc(0, 2, { ~habit.trumpetLoopers.looper1.clearLoop(15); }, page: page);

//// Looper 2:
// Loop:
~ohm.setGridMonitorFunc(1, 4, {
  if( ~habit.trumpetLoopers.looper2.isRecording, { ~ohm.turnGridWhite(1, 4); },
    if( ~habit.trumpetLoopers.looper2.isPlaying,
      { ~ohm.turnGridGreen(1, 4); },
      { ~ohm.turnGridBlue(1, 4); });
  );
}, page: page);
~ohm.setGridFunc(1, 4, { ~habit.trumpetLoopers.looper2.loop; }, page: page);
// play:
~ohm.setGridMonitorFunc(1, 3, {
  if( ~habit.trumpetLoopers.looper2.isPlaying, { ~ohm.turnGridGreen(1, 3); }, { ~ohm.turnGridRed(1, 3); });
}, page: page);
~ohm.setGridFunc(1, 3, { ~habit.trumpetLoopers.looper2.togglePlayLoop; }, page: page);
// erase:
~ohm.turnGridRed(1, 2, page: page);
~ohm.setGridFunc(1, 2, { ~habit.trumpetLoopers.looper2.clearLoop(15); }, page: page);


//// Looper 3:
// Loop:
~ohm.setGridMonitorFunc(2, 4, {
  if( ~habit.trumpetLoopers.looper3.isRecording, { ~ohm.turnGridWhite(2, 4); },
    if( ~habit.trumpetLoopers.looper3.isPlaying,
      { ~ohm.turnGridGreen(2, 4); },
      { ~ohm.turnGridBlue(2, 4); });
  );
}, page: page);
~ohm.setGridFunc(2, 4, { ~habit.trumpetLoopers.looper3.loop; }, page: page);
// play:
~ohm.setGridMonitorFunc(2, 3, {
  if( ~habit.trumpetLoopers.looper3.isPlaying, { ~ohm.turnGridGreen(2, 3); }, { ~ohm.turnGridRed(2, 3); });
}, page: page);
~ohm.setGridFunc(2, 3, { ~habit.trumpetLoopers.looper3.togglePlayLoop; }, page: page);
// erase:
~ohm.turnGridRed(2, 2, page: page);
~ohm.setGridFunc(2, 2, { ~habit.trumpetLoopers.looper3.clearLoop(15); }, page: page);


//// Looper 4:
// Loop:
~ohm.setGridMonitorFunc(3, 4, {
  if( ~habit.trumpetLoopers.looper4.isRecording, { ~ohm.turnGridWhite(3, 4); },
    if( ~habit.trumpetLoopers.looper4.isPlaying,
      { ~ohm.turnGridGreen(3, 4); },
      { ~ohm.turnGridBlue(3, 4); });
  );
}, page: page);
~ohm.setGridFunc(3, 4, { ~habit.trumpetLoopers.looper4.loop; }, page: page);
// play:
~ohm.setGridMonitorFunc(3, 3, {
  if( ~habit.trumpetLoopers.looper4.isPlaying, { ~ohm.turnGridGreen(3, 3); }, { ~ohm.turnGridRed(3, 3); });
}, page: page);
~ohm.setGridFunc(3, 3, { ~habit.trumpetLoopers.looper4.togglePlayLoop; }, page: page);
// erase:
~ohm.turnGridRed(3, 2, page: page);
~ohm.setGridFunc(3, 2, { ~habit.trumpetLoopers.looper4.clearLoop(15); }, page: page);

//// Grain envelopes:
~ohm.setGridMonitorFunc(0, 5, {
  7.do({ | i | ~ohm.turnGridCyan(i, 5); });
  switch( ~prm.granulator.grainEnv,
    'sine', { ~ohm.turnGridGreen(0, 5); },
    'gabor', { ~ohm.turnGridGreen(1, 5); },
    'gabWide', { ~ohm.turnGridGreen(2, 5); },
    'perc', { ~ohm.turnGridGreen(3, 5); },
    'revPerc', { ~ohm.turnGridGreen(4, 5); },
    'expodec', { ~ohm.turnGridGreen(5, 5); },
    'rexpodec', { ~ohm.turnGridGreen(6, 5); }
  );
}, page: page);
// sine:
~ohm.setGridFunc(0, 5, { ~prm.granulator.setGrainEnvelope('sine'); }, page: page);
// gabor:
~ohm.setGridFunc(1, 5, { ~prm.granulator.setGrainEnvelope('gabor'); }, page: page);
// gabor wide:
~ohm.setGridFunc(2, 5, { ~prm.granulator.setGrainEnvelope('gabWide'); }, page: page);
// perc:
~ohm.setGridFunc(3, 5, { ~prm.granulator.setGrainEnvelope('perc'); }, page: page);
// revPerc:
~ohm.setGridFunc(4, 5, { ~prm.granulator.setGrainEnvelope('revPerc'); }, page: page);
// expodec:
~ohm.setGridFunc(5, 5, { ~prm.granulator.setGrainEnvelope('expodec'); }, page: page);
// rexpodec:
~ohm.setGridFunc(6, 5, { ~prm.granulator.setGrainEnvelope('rexpodec'); }, page: page);


/////// left knob bank change:
~ohm.setGridMonitorFunc(0, 7, {
  if( activeLeftKnobsBank == mainMixBank, { ~ohm.turnGridMagenta(0, 7); }, { ~ohm.turnGridBlue(0, 7) });
}, page: page);
~ohm.setGridFunc(0, 7, {
  if( activeLeftKnobsBank == mainMixBank, {activeLeftKnobsBank = leftKnobMiscBank}, {activeLeftKnobsBank = mainMixBank});
  ~ohm.setActiveLeftKnobsBank(activeLeftKnobsBank);
}, page: page);

//////// tempo reset:
// set tempo to 60 BPM:
~ohm.setGridMonitorFunc(6, 7, {
  if( ~habit.clock.tempo == 1, { ~ohm.turnGridGreen(6, 7); }, { ~ohm.turnGridWhite(6, 7); });
}, page: page);
~ohm.setGridFunc(6, 7, { ~habit.setClockTempo(60); }, page: page);
// set tempo to 120 BPM:
~ohm.setGridMonitorFunc(7, 7, {
  if( ~habit.clock.tempo == 2, { ~ohm.turnGridGreen(7, 7); }, { ~ohm.turnGridWhite(7, 7); });
}, page: page);
~ohm.setGridFunc(7, 7, { ~habit.setClockTempo(120); }, page: page);

/////// crossfader func:
~ohm.setCrossfaderButtonMonitorFunc(0, {
  if( activeSlidersBank == mainMixBank, { ~ohm.turnCrossfaderButtonMagenta(0); }, { ~ohm.turnCrossfaderButtonCyan(0); });
}, page: page);
~ohm.setCrossfaderButtonFunc(0, {
  if( activeSlidersBank == mainMixBank, { activeSlidersBank = trumpetLooperBank }, { activeSlidersBank = mainMixBank });
  ~ohm.setActiveLeftButtonsBank(activeSlidersBank);
  ~ohm.setActiveLeftSlidersBank(activeSlidersBank);
  activeSlidersBank.postln;
}, page: page);
  