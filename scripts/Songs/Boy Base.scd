/*
Tuesday, July 25th 2017
Boy Base.scd
prm
*/

var page = 'boy';
var endSynthBank = 0;

var mixerBank = 0;

~base.makePage(page);

////////////////
//// Grid: ////
//////////////

//////// Grid Synth Bank:

// c#:
~base.turnGridGreen(3, 0, bank: endSynthBank, page: page);
~base.setGridFunc(3, 0, {
  ~theBoy.endSynth.playNote(73.midicps, -6);
  ~base.turnGridWhite(3, 0);
}, 'noteOn', endSynthBank, page);
~base.setGridFunc(3, 0, {
  ~theBoy.endSynth.releaseNote(73.midicps);
  ~base.turnGridGreen(3, 0);
}, 'noteOff', endSynthBank, page);

// d:
~base.turnGridGreen(5, 0, bank: endSynthBank, page: page);
~base.setGridFunc(5, 0, {
  ~theBoy.endSynth.playNote(74.midicps, -6);
  ~base.turnGridWhite(5, 0);
}, bank: endSynthBank, page: page);
~base.setGridFunc(5, 0, {
  ~theBoy.endSynth.releaseNote(74.midicps);
  ~base.turnGridGreen(5, 0);
}, 'noteOff', endSynthBank, page);
// d#
~base.turnGridGreen(6, 0, bank: endSynthBank, page: page);
~base.setGridFunc(6, 0, {
  ~theBoy.endSynth.playNote(75.midicps, -6);
  ~base.turnGridWhite(6, 0);
}, bank: endSynthBank, page: page);
~base.setGridFunc(6, 0, {
  ~theBoy.endSynth.releaseNote(75.midicps);
  ~base.turnGridGreen(6, 0);
}, 'noteOff', endSynthBank, page);
// e:
~base.turnGridGreen(3, 1, bank: endSynthBank, page: page);
~base.setGridFunc(3, 1, {
  ~theBoy.endSynth.playNote(76.midicps);
  ~base.turnGridWhite(3, 1);
}, 'noteOn', endSynthBank, page);
~base.setGridFunc(3, 1, {
  ~theBoy.endSynth.releaseNote(76.midicps);
  ~base.turnGridGreen(3, 1);
}, 'noteOff', endSynthBank, page);

// f#:
~base.turnGridGreen(4, 1, bank: endSynthBank, page: page);
~base.setGridFunc(4, 1, {
  ~theBoy.endSynth.playNote(78.midicps, -6);
  ~base.turnGridWhite(4, 1);
}, bank: endSynthBank, page: page);
~base.setGridFunc(4, 1, {
  ~theBoy.endSynth.releaseNote(78.midicps);
  ~base.turnGridGreen(4, 1);
}, 'noteOff', endSynthBank, page);
// g:
~base.turnGridGreen(5, 1, bank: endSynthBank, page: page);
~base.setGridFunc(5, 1, {
  ~theBoy.endSynth.playNote(79.midicps, -6);
  ~base.turnGridWhite(5, 1);
}, bank: endSynthBank, page: page);
~base.setGridFunc(5, 1, {
  ~theBoy.endSynth.releaseNote(79.midicps);
  ~base.turnGridGreen(5, 1);
}, 'noteOff', endSynthBank, page);
//g#
~base.turnGridGreen(6, 1, bank: endSynthBank, page: page);
~base.setGridFunc(6, 1, {
  ~theBoy.endSynth.playNote(80.midicps, -6);
  ~base.turnGridWhite(6, 1);
}, bank: endSynthBank, page: page);
~base.setGridFunc(6, 1, {
  ~theBoy.endSynth.releaseNote(80.midicps);
  ~base.turnGridGreen(6, 1);
}, 'noteOff', endSynthBank, page);
// a:
~base.turnGridGreen(7, 1, bank: endSynthBank, page: page);
~base.setGridFunc(7, 1, {
  ~theBoy.endSynth.playNote(81.midicps, -6);
  ~base.turnGridWhite(7, 1);
}, 'noteOn', bank: endSynthBank, page: page);
~base.setGridFunc(7, 1, {
  ~theBoy.endSynth.releaseNote(81.midicps);
  ~base.turnGridGreen(7, 1);
}, 'noteOff', endSynthBank, page);
// b:
~base.turnGridGreen(5, 2, bank: endSynthBank, page: page);
~base.setGridFunc(5, 2, {
  ~theBoy.endSynth.playNote(83.midicps, -6);
  ~base.turnGridWhite(5, 2);
}, bank: endSynthBank, page: page);
~base.setGridFunc(5, 2, {
  ~theBoy.endSynth.releaseNote(83.midicps);
  ~base.turnGridGreen(5, 2);
}, 'noteOff', endSynthBank, page);
// high C#
~base.turnGridGreen(6, 2, bank: endSynthBank, page: page);
~base.setGridFunc(6, 2, {
  ~theBoy.endSynth.playNote(85.midicps, -6);
  ~base.turnGridWhite(6, 2);
}, 'noteOn', endSynthBank, page);
~base.setGridFunc(6, 2, {
  ~theBoy.endSynth.releaseNote(85.midicps);
  ~base.turnGridGreen(6, 2);
}, 'noteOff', endSynthBank, page);
// high d#
~base.turnGridGreen(7, 2, bank: endSynthBank, page: page);
~base.setGridFunc(7, 2, {
  ~theBoy.endSynth.playNote(87.midicps, -6);
  ~base.turnGridWhite(7, 2);
}, 'noteOn', endSynthBank, page);
~base.setGridFunc(7, 2, {
  ~theBoy.endSynth.releaseNote(87.midicps);
  ~base.turnGridGreen(7, 2);
}, 'noteOff', endSynthBank, page);
// e:
~base.turnGridGreen(4, 3, bank: endSynthBank, page: page);
~base.setGridFunc(4, 3, {
  ~theBoy.endSynth.playNote(88.midicps, -6);
  ~base.turnGridWhite(4, 3);
}, 'noteOn', endSynthBank, page);
~base.setGridFunc(4, 3, {
  ~theBoy.endSynth.releaseNote(88.midicps);
  ~base.turnGridGreen(4, 3);
}, 'noteOff', endSynthBank, page);
// f#:
~base.turnGridGreen(5, 3, bank: endSynthBank, page: page);
~base.setGridFunc(5, 3, {
  ~theBoy.endSynth.playNote(90.midicps, -6);
  ~base.turnGridWhite(5, 3);
}, 'noteOn', endSynthBank, page);
~base.setGridFunc(5, 3, {
  ~theBoy.endSynth.releaseNote(90.midicps);
  ~base.turnGridGreen(5, 3);
}, 'noteOff', endSynthBank, page);
// g#:
~base.turnGridGreen(6, 3, bank: endSynthBank, page: page);
~base.setGridFunc(6, 3, {
  ~theBoy.endSynth.playNote(92.midicps, -6);
  ~base.turnGridWhite(6, 3);
}, 'noteOn', endSynthBank, page);
~base.setGridFunc(6, 3, {
  ~theBoy.endSynth.releaseNote(92.midicps);
  ~base.turnGridGreen(6, 3);
}, 'noteOff', endSynthBank, page);
// b:
~base.turnGridGreen(7, 3, bank: endSynthBank, page: page);
~base.setGridFunc(7, 3, {
  ~theBoy.endSynth.playNote(95.midicps, -6);
  ~base.turnGridWhite(7, 3);
}, 'noteOn', endSynthBank, page);
~base.setGridFunc(7, 3, {
  ~theBoy.endSynth.releaseNote(95.midicps);
  ~base.turnGridGreen(7, 3);
}, 'noteOff', endSynthBank, page);

///////////////////////////
//////// Mixer Bank //////
/////////////////////////

//////// Main Bell:
// mute:
~base.setTouchButtonMonitorFunc(1, {
  if( ~theBoy.mixerA.isMuted(0),
    { ~base.turnTouchButtonRed(1) }, { ~base.turnTouchButtonGreen(1) }
  );
}, bank: mixerBank, page: page);
~base.setTouchButtonFunc(1, { ~theBoy.mixerA.tglMute(0);
}, bank: mixerBank, page: page);
// volume:
~base.setFaderMode(1, 'yellowFill', mixerBank, page);
~base.setFaderMonitorFunc(1, {
  ~base.setFaderValue(1, ~theBoy.mixerA.vol(0).dbfsCC);
}, mixerBank, page);
~base.setFaderFunc(1, { | val |
  ~theBoy.mixerA.setVol(0, val.ccdbfs);
}, mixerBank, page);

/////// Row Fuzz:
~base.setTouchButtonMonitorFunc(2, {
  if( ~theBoy.mixerA.isMuted(1),
    { ~base.turnTouchButtonRed(2) }, { ~base.turnTouchButtonGreen(2) }
  );
}, bank: mixerBank, page: page);
~base.setTouchButtonFunc(2, { ~theBoy.mixerA.tglMute(1);
}, bank: mixerBank, page: page);
// volume:
~base.setFaderMode(2, 'magentaFill', mixerBank, page);
~base.setFaderMonitorFunc(2, {
  ~base.setFaderValue(2, ~theBoy.mixerA.vol(1).dbfsCC);
}, mixerBank, page);
~base.setFaderFunc(2, { | val |
  ~theBoy.mixerA.setVol(1, val.ccdbfs);
}, mixerBank, page);

/////// Piano:
~base.setTouchButtonMonitorFunc(3, {
  if( ~theBoy.mixerB.isMuted(0),
    { ~base.turnTouchButtonRed(3) }, { ~base.turnTouchButtonGreen(3) }
  );
}, bank: mixerBank, page: page);
~base.setTouchButtonFunc(3, { ~theBoy.mixerB.tglMute(0);
}, bank: mixerBank, page: page);
// volume:
~base.setFaderMode(3, 'blueFill', mixerBank, page);
~base.setFaderMonitorFunc(3, {
  ~base.setFaderValue(3, ~theBoy.mixerB.vol(0).dbfsCC);
}, mixerBank, page);
~base.setFaderFunc(3, { | val |
  ~theBoy.mixerB.setVol(0, val.ccdbfs);
}, mixerBank, page);

/////// Trumpet:
~base.setTouchButtonMonitorFunc(4, {
  if( ~theBoy.mixerC.isMuted(0),
    { ~base.turnTouchButtonRed(4) }, { ~base.turnTouchButtonGreen(4) }
  );
}, bank: mixerBank, page: page);
~base.setTouchButtonFunc(4, { ~theBoy.mixerC.tglMute(0);
}, bank: mixerBank, page: page);
// volume:
~base.setFaderMode(4, 'magentaFill', mixerBank, page);
~base.setFaderMonitorFunc(4, {
  ~base.setFaderValue(4, ~theBoy.mixerC.vol(0).dbfsCC);
}, mixerBank, page);
~base.setFaderFunc(4, { | val |
  ~theBoy.mixerC.setVol(0, val.ccdbfs);
}, mixerBank, page);

/////// Low Bells:
~base.setTouchButtonMonitorFunc(5, {
  if( ~theBoy.mixerA.isMuted(2),
    { ~base.turnTouchButtonRed(5) }, { ~base.turnTouchButtonGreen(5) }
  );
}, bank: mixerBank, page: page);
~base.setTouchButtonFunc(5, { ~theBoy.mixerA.tglMute(2);
}, bank: mixerBank, page: page);
// volume:
~base.setFaderMode(5, 'cyanFill', mixerBank, page);
~base.setFaderMonitorFunc(5, {
  ~base.setFaderValue(5, ~theBoy.mixerA.vol(2).dbfsCC);
}, mixerBank, page);
~base.setFaderFunc(5, { | val |
  ~theBoy.mixerA.setVol(2, val.ccdbfs);
}, mixerBank, page);

/////// Noise Chords:
~base.setTouchButtonMonitorFunc(6, {
  if( ~theBoy.mixerC.isMuted(1),
    { ~base.turnTouchButtonRed(6) }, { ~base.turnTouchButtonGreen(6) }
  );
}, bank: mixerBank, page: page);
~base.setTouchButtonFunc(6, { ~theBoy.mixerC.tglMute(1);
}, bank: mixerBank, page: page);
// volume:
~base.setFaderMode(6, 'whiteFill', mixerBank, page);
~base.setFaderMonitorFunc(6, {
  ~base.setFaderValue(6, ~theBoy.mixerC.vol(1).dbfsCC);
}, mixerBank, page);
~base.setFaderFunc(6, { | val |
  ~theBoy.mixerC.setVol(1, val.ccdbfs);
}, mixerBank, page);

/////// Bass Section:

/////// Noise Synth:
