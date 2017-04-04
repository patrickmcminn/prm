/*
Monday, March 20th 2017
Habit Base.scd
prm
*/

var page = 'habit';

var mainMixBank = 0;
var miscBank = 1;

var activeFadersBank = mainMixBank;

var mainGridBank = 0;

var sequenceResetFunc;

var pitchArray = [1, 3, 4, 6, 8, 9, 11];

~base.makePage(page);
~base.addTouchButtonsBanks(1, page: page);
~base.addFadersBanks(1, page: page);

//////// Control Buttons:

~base.setControlButtonMonitorFunc(1, {
  ~base.turnControlButtonMagenta(1);
  ~base.turnControlButtonMagenta(1, 'right');
  ~base.turnControlButtonBlue(2);
  ~base.turnControlButtonBlue(2, 'right');
  ~base.turnControlButtonGreen(activeFadersBank + 1, 'right');
}, page: page);

~base.setControlButtonFunc(1, {
  activeFadersBank = mainMixBank;
  ~base.setActiveFadersBank(activeFadersBank);
  ~base.setActiveTouchButtonsBank(activeFadersBank);
}, page: page);
~base.setControlButtonFunc(2, {
  activeFadersBank = miscBank;
  ~base.setActiveFadersBank(activeFadersBank);
  ~base.setActiveTouchButtonsBank(activeFadersBank)
}, page: page);

//////// faders:

////// Moog:

//// Mute:
~base.setTouchButtonMonitorFunc(1, {
  if( ~habit.moog.mixer.isMuted, { ~base.turnTouchButtonRed(1); }, { ~base.turnTouchButtonGreen(1); });
}, bank: mainMixBank, page: page);
~base.setTouchButtonFunc(1, { ~habit.moog.mixer.tglMute; }, bank: mainMixBank, page: page);
//// Fader:
~base.setFaderMode(1, 'magentaFill', bank: mainMixBank, page: page);
~base.setFaderMonitorFunc(1, { ~base.setFaderValue(1, ~habit.moog.mixer.vol.dbfsCC); },
  bank: mainMixBank, page: page);
~base.setFaderFunc(1, { | val | ~habit.moog.mixer.setVol(val.ccdbfs(-140, 0)); },
  bank: mainMixBank, page: page);

////// Modular:

//// Mute:
~base.setTouchButtonMonitorFunc(2, {
  if( ~habit.modular.isMuted, { ~base.turnTouchButtonRed(2); }, { ~base.turnTouchButtonGreen(2); });
}, bank: mainMixBank, page: page);
~base.setTouchButtonFunc(2, { ~habit.modular.tglMute; }, bank: mainMixBank, page: page);
//// Fader:
~base.setFaderMode(2, 'magentaFill', bank: mainMixBank, page: page);
~base.setFaderMonitorFunc(2, { ~base.setFaderValue(2, ~habit.modular.vol.dbfsCC); },
  bank: mainMixBank, page: page);
~base.setFaderFunc(2, { | val | ~habit.modular.setVol(val.ccdbfs(-140, 0)); },
  bank: mainMixBank, page: page);

//////// Live Trumpet:

//// Mute:
~base.setTouchButtonMonitorFunc(3, {
  if( ~habit.trumpet.mixer.isMuted, { ~base.turnTouchButtonRed(3); }, { ~base.turnTouchButtonGreen(3); }); },
  bank: mainMixBank, page: page);
~base.setTouchButtonFunc(3, { ~habit.trumpet.mixer.tglMute; }, bank: mainMixBank, page: page);
//// Fader:
~base.setFaderMode(3, 'whiteFill', bank: mainMixBank, page: page);
~base.setFaderMonitorFunc(3, { ~base.setFaderValue(3, ~habit.trumpet.mixer.vol.dbfsCC); },
  bank: mainMixBank, page: page);
~base.setFaderFunc(3, { | val | ~habit.trumpet.mixer.setVol(val.ccdbfs(-140, 0)); },
  bank: mainMixBank, page: page);

//////// Trumpet Looper SubMix:

//// Mute:
~base.setTouchButtonMonitorFunc(4, {
  if( ~habit.trumpetLoopers.mixer.isMuted, { ~base.turnTouchButtonRed(4); }, { ~base.turnTouchButtonGreen(4) }); },
  bank: mainMixBank, page: page);
~base.setTouchButtonFunc(4, { ~habit.trumpetLoopers.mixer.tglMute; }, bank: mainMixBank, page: page);
//// Fader:
~base.setFaderMode(4, 'whiteFill', bank: mainMixBank, page: page);
~base.setFaderMonitorFunc(4, { ~base.setFaderValue(4, ~habit.trumpetLoopers.mixer.vol.dbfsCC); },
  bank: mainMixBank, page: page);
~base.setFaderFunc(4, { | val | ~habit.trumpetLoopers.mixer.vol(val.ccdbfs(-140, 0)); },
  bank: mainMixBank, page: page);

//////// Trumpet Looper 1:
~base.setTouchButtonMonitorFunc(5, {
  if( ~habit.trumpetLoopers.submixer.isMuted(0), { ~base.turnTouchButtonRed(5); }, { ~base.turnTouchButtonGreen(5) }); },
  bank: mainMixBank, page: page);
~base.setTouchButtonFunc(5, { ~habit.trumpetLoopers.submixer.tglMute(0) }, bank: mainMixBank, page: page);
//// Fader:
~base.setFaderMode(5, 'cyanFill', bank: mainMixBank, page: page);
~base.setFaderMonitorFunc(5, { ~base.setFaderValue(5, ~habit.trumpetLoopers.submixer.vol(0).dbfsCC); },
  bank: mainMixBank, page: page);
~base.setFaderFunc(5, { | val | ~habit.trumpetLoopers.submixer.setVol(0, val.ccdbfs(-140, 0)); },
  bank: mainMixBank, page: page);

//////// Trumpet Looper 2:
~base.setTouchButtonMonitorFunc(6, {
  if( ~habit.trumpetLoopers.submixer.isMuted(1), { ~base.turnTouchButtonRed(6); }, { ~base.turnTouchButtonGreen(6) }); },
  bank: mainMixBank, page: page);
~base.setTouchButtonFunc(6, { ~habit.trumpetLoopers.submixer.tglMute(1) }, bank: mainMixBank, page: page);
//// Fader:
~base.setFaderMode(6, 'cyanFill', bank: mainMixBank, page: page);
~base.setFaderMonitorFunc(6, { ~base.setFaderValue(6, ~habit.trumpetLoopers.submixer.vol(1).dbfsCC); },
  bank: mainMixBank, page: page);
~base.setFaderFunc(6, { | val | ~habit.trumpetLoopers.submixer.setVol(1, val.ccdbfs(-140, 0)); },
  bank: mainMixBank, page: page);

//////// Trumpet Looper 3:
~base.setTouchButtonMonitorFunc(7, {
  if( ~habit.trumpetLoopers.submixer.isMuted(2), { ~base.turnTouchButtonRed(7); }, { ~base.turnTouchButtonGreen(7) }); },
  bank: mainMixBank, page: page);
~base.setTouchButtonFunc(7, { ~habit.trumpetLoopers.submixer.tglMute(2) }, bank: mainMixBank, page: page);
//// Fader:
~base.setFaderMode(7, 'cyanFill', bank: mainMixBank, page: page);
~base.setFaderMonitorFunc(7, { ~base.setFaderValue(7, ~habit.trumpetLoopers.submixer.vol(2).dbfsCC); },
  bank: mainMixBank, page: page);
~base.setFaderFunc(7, { | val | ~habit.trumpetLoopers.submixer.setVol(2, val.ccdbfs(-140, 0)); },
  bank: mainMixBank, page: page);

//////// Trumpet Looper 4:
~base.setTouchButtonMonitorFunc(8, {
  if( ~habit.trumpetLoopers.submixer.isMuted(3), { ~base.turnTouchButtonRed(8); }, { ~base.turnTouchButtonGreen(8) }); },
  bank: mainMixBank, page: page);
~base.setTouchButtonFunc(8, { ~habit.trumpetLoopers.submixer.tglMute(3) }, bank: mainMixBank, page: page);
//// Fader:
~base.setFaderMode(8, 'cyanFill', bank: mainMixBank, page: page);
~base.setFaderMonitorFunc(8, { ~base.setFaderValue(8, ~habit.trumpetLoopers.submixer.vol(3).dbfsCC); },
  bank: mainMixBank, page: page);
~base.setFaderFunc(8, { | val | ~habit.trumpetLoopers.submixer.setVol(3, val.ccdbfs(-140, 0)); },
  bank: mainMixBank, page: page);

//////// Misc Bank:
//// Delay:
// mute:
~base.setTouchButtonMonitorFunc(1, {
  if( ~habit.delay.mixer.isMuted, { ~base.turnTouchButtonRed(1); }, { ~base.turnTouchButtonGreen(1); });
}, bank: miscBank, page: page);
~base.setTouchButtonFunc(1, { ~habit.delay.mixer.tglMute; }, bank: miscBank, page: page);
// vol:
~base.setFaderMode(1, 'yellowFill', bank: miscBank, page: page);
~base.setFaderMonitorFunc(1, { ~base.setFaderValue(1, ~habit.delay.mixer.vol.dbfsCC); },
  bank: miscBank, page: page);
~base.setFaderFunc(1, { | val | ~habit.delay.mixer.setVol(val.ccdbfs); },
  bank: miscBank, page: page);
// delay time:
~base.setFaderMode(2, 'cyanFill', bank: miscBank, page: page);
~base.setFaderMonitorFunc(2, {
  var value = ~habit.delay.delayTime.linlin(0.005, 5, 0, 127);
  ~base.setFaderValue(2, value);
}, bank: miscBank, page: page);
~base.setFaderFunc(2, { | val |
  var delayTime = val.linlin(0, 127, 0.001, 5);
  ~habit.delay.setDelayTime(delayTime);
}, bank: miscBank, page: page);
// feedback:
~base.setFaderMode(3, 'redFill', bank: miscBank, page: page);
~base.setFaderMonitorFunc(3, {
  var value = ~habit.delay.feedback.linlin(0.0, 1.0, 0, 127);
  ~base.setFaderValue(3, value);
}, bank: miscBank, page: page);
~base.setFaderFunc(3, { | val |
  var feedback = val.linlin(0, 127, 0.0, 1.0);
  ~habit.delay.setFeedback(feedback);
}, bank: miscBank, page: page);


////// Delay Sends:
4.do({ | i | ~base.setFaderMode(i + 4, 'blueFill', bank: miscBank, page: page); });

//// Moog Send:
~base.setFaderMonitorFunc(4, { ~base.setFaderValue(4, ~habit.moog.mixer.sendVol(3).dbfsCC); },
  bank: miscBank, page: page);
~base.setFaderFunc(4, { | val | ~habit.moog.mixer.setSendVol(3, val.ccdbfs); },
  bank: miscBank, page: page);

//// Modular Send:
~base.setFaderMonitorFunc(5, { ~base.setFaderValue(5, ~habit.modular.sendVol(3).dbfsCC); },
  bank: miscBank, page: page);
~base.setFaderFunc(5, { | val | ~habit.modular.setSendVol(3, val.ccdbfs); },
  bank: miscBank, page: page);


//// Main Trumpet;
~base.setFaderMonitorFunc(6, { ~base.setFaderValue(6, ~habit.trumpet.mixer.sendVol(3).dbfsCC); },
  bank: miscBank, page: page);
~base.setFaderFunc(6, { | val | ~habit.trumpet.mixer.setSendVol(3, val.ccdbfs); },
  bank: miscBank, page: page);

//// Trumpet Loopers:
//// Main Trumpet;
~base.setFaderMonitorFunc(7, { ~base.setFaderValue(7, ~habit.trumpetLoopers.mixer.sendVol(3).dbfsCC); },
  bank: miscBank, page: page);
~base.setFaderFunc(7, { | val | ~habit.trumpetLoopers.mixer.setSendVol(3, val.ccdbfs); },
  bank: miscBank, page: page);



////////// Grid:

//// Looper 1:
// Loop:
~base.setGridMonitorFunc(0, 0, {
  if( ~habit.trumpetLoopers.looper1.isRecording, { ~base.turnGridWhite(0, 0); },
    if( ~habit.trumpetLoopers.looper1.isPlaying,
      { ~base.turnGridGreen(0, 0); },
      { ~base.turnGridBlue(0, 0); });
  );
}, page: page);
~base.setGridFunc(0, 0, { ~habit.trumpetLoopers.looper1.loop; }, page: page);
// play:
~base.setGridMonitorFunc(0, 1, {
  if( ~habit.trumpetLoopers.looper1.isPlaying, { ~base.turnGridGreen(0, 1); }, { ~base.turnGridRed(0, 1); });
}, page: page);
~base.setGridFunc(0, 1, { ~habit.trumpetLoopers.looper1.togglePlayLoop; }, page: page);


//// Looper 2:
// Loop:
~base.setGridMonitorFunc(1, 0, {
  if( ~habit.trumpetLoopers.looper2.isRecording, { ~base.turnGridWhite(1, 0); },
    if( ~habit.trumpetLoopers.looper2.isPlaying,
      { ~base.turnGridGreen(1, 0); },
      { ~base.turnGridBlue(1, 0); });
  );
}, page: page);
~base.setGridFunc(1, 0, { ~habit.trumpetLoopers.looper2.loop; }, page: page);
// play:
~base.setGridMonitorFunc(1, 1, {
  if( ~habit.trumpetLoopers.looper2.isPlaying, { ~base.turnGridGreen(1, 1); }, { ~base.turnGridRed(1, 1); });
}, page: page);
~base.setGridFunc(1, 1, { ~habit.trumpetLoopers.looper2.togglePlayLoop; }, page: page);


//// Looper 3:
// Loop:
~base.setGridMonitorFunc(2, 0, {
  if( ~habit.trumpetLoopers.looper3.isRecording, { ~base.turnGridWhite(2, 0); },
    if( ~habit.trumpetLoopers.looper3.isPlaying,
      { ~base.turnGridGreen(2, 0); },
      { ~base.turnGridBlue(2, 0); });
  );
}, page: page);
~base.setGridFunc(2, 0, { ~habit.trumpetLoopers.looper3.loop; }, page: page);
// play:
~base.setGridMonitorFunc(2, 1, {
  if( ~habit.trumpetLoopers.looper3.isPlaying, { ~base.turnGridGreen(2, 1); }, { ~base.turnGridRed(2, 1); });
}, page: page);
~base.setGridFunc(2, 1, { ~habit.trumpetLoopers.looper3.togglePlayLoop; }, page: page);


//// Looper 4:
// Loop:
~base.setGridMonitorFunc(3, 0, {
  if( ~habit.trumpetLoopers.looper4.isRecording, { ~base.turnGridWhite(3, 0); },
    if( ~habit.trumpetLoopers.looper4.isPlaying,
      { ~base.turnGridGreen(3, 0); },
      { ~base.turnGridBlue(3, 0); });
  );
}, page: page);
~base.setGridFunc(3, 0, { ~habit.trumpetLoopers.looper4.loop; }, page: page);
// play:
~base.setGridMonitorFunc(3, 1, {
  if( ~habit.trumpetLoopers.looper4.isPlaying, { ~base.turnGridGreen(3, 1); }, { ~base.turnGridRed(3, 1); });
}, page: page);
~base.setGridFunc(3, 1, { ~habit.trumpetLoopers.looper4.togglePlayLoop; }, page: page);

//////// notes being played:
//// notes being played:
~base.setGridMonitorFunc(0, 2, {
  8.do({ | i | ~base.turnGridWhite(i, 2); });
  if( ~habit.moog.notePlaying.isInteger, { ~base.turnGridGreen(pitchArray.atIdentityHash(~habit.moog.notePlaying), 2) },
    { 8.do({ | i | ~base.turnGridWhite(i, 2); }); });
}, page: page);

/////// Sequence

sequenceResetFunc = {
  ~base.turnGridCyan(0, 3);
  ~base.turnGridMagenta(1, 3);
  ~base.turnGridBlue(2, 3);
  ~base.turnGridCyan(3, 3);
  ~base.turnGridYellow(4, 3);
  ~base.turnGridWhite(5, 3);
  ~base.turnGridRed(6, 3);
};

~base.setGridMonitorFunc(0, 3, {
  sequenceResetFunc.value;
  switch(~habit.moog.sequencePlaying,
    'sequence1', { ~base.turnGridGreen(0, 3); },
    'sequence2', { ~base.turnGridGreen(1, 3); },
    'sequence3', { ~base.turnGridGreen(2, 3) },
    'sequence4', { ~base.turnGridGreen(3, 3) },
    'sectionEndLoop', { ~base.turnGridGreen(4, 3) },
    'sectionEnd', { ~base.turnGridGreen(5, 3) },
    'pieceEnd', { ~base.turnGridGreen(6, 3) }
  );
}, page: page);

~base.setGridFunc(0, 3, { ~habit.moog.playSequence1(~habit.clock); }, page: page);
~base.setGridFunc(1, 3, { ~habit.moog.playSequence2(~habit.clock); }, page: page);
~base.setGridFunc(2, 3, { ~habit.moog.playSequence3(~habit.clock); }, page: page);
~base.setGridFunc(3, 3, { ~habit.moog.playSequence4(~habit.clock); }, page: page);
~base.setGridFunc(4, 3, { ~habit.moog.playSectionEndLoop(~habit.clock); }, page: page);
~base.setGridFunc(5, 3, { ~habit.moog.playSectionEnd(~habit.clock); }, page: page);
~base.setGridFunc(6, 3, { ~habit.moog.playPieceEnd(~habit.clock); }, page: page);
