/*
Saturday, April 22nd 2017
Light Base.scd
prm
*/

var page = 'light';


~base.makePage(page);

////////////////
//// Grid: ////
//////////////

//////// Loops:

4.do({ | i | ~base.turnGridWhite(i, 3, page: page); });

// Loop 1:
~base.setGridFunc(0, 3, {
  ~light.main.playLoop1;
  ~base.turnGridGreen(0, 3);
}, 'noteOn', page: page);
~base.setGridFunc(0, 3, {
  ~light.main.releaseLoop1;
  ~base.turnGridWhite(0, 3);
}, 'noteOff', page: page);

// Loop 2:
~base.setGridFunc(1, 3, {
  ~light.main.playLoop2;
  ~base.turnGridGreen(1, 3);
}, 'noteOn', page: page);
~base.setGridFunc(1, 3, {
  ~light.main.releaseLoop2;
  ~base.turnGridWhite(1, 3);
}, 'noteOff', page: page);

// Loop 3:
~base.setGridFunc(2, 3, {
  ~light.main.playLoop3;
  ~base.turnGridGreen(2, 3);
}, 'noteOn', page: page);
~base.setGridFunc(2, 3, {
  ~light.main.releaseLoop3;
  ~base.turnGridWhite(2, 3);
}, 'noteOff', page: page);

// Loop 4:
~base.setGridFunc(3, 3, {
  ~light.main.playLoop4;
  ~base.turnGridGreen(3, 3);
}, 'noteOn', page: page);
~base.setGridFunc(3, 3, {
  ~light.main.releaseLoop4;
  ~base.turnGridWhite(3, 3);
}, 'noteOff', page: page);

//////// Arps:

// chord 1:
~base.setGridMonitorFunc(0, 2, {
  if( ~light.arps.chord1IsPlaying,
    { ~base.turnGridGreen(0, 2) }, { ~base.turnGridBlue(0, 2) });
}, page: page);
~base.setGridFunc(0, 2, { ~light.arps.playChord1 }, page: page);
~base.setGridFunc(0, 2, { ~light.arps.stopChord1 }, \noteOff, page: page);
// chord 2:
~base.setGridMonitorFunc(1, 2, {
  if( ~light.arps.chord2IsPlaying,
    { ~base.turnGridGreen(1, 2) }, { ~base.turnGridBlue(1, 2) });
}, page: page);
~base.setGridFunc(1, 2, { ~light.arps.playChord2 }, page: page);
~base.setGridFunc(1, 2, { ~light.arps.stopChord2 }, \noteOff, page: page);
// chord 3:
~base.setGridMonitorFunc(2, 2, {
  if( ~light.arps.chord3IsPlaying,
    { ~base.turnGridGreen(2, 2) }, { ~base.turnGridBlue(2, 2) });
}, page: page);
~base.setGridFunc(2, 2, { ~light.arps.playChord3 }, page: page);
~base.setGridFunc(2, 2, { ~light.arps.stopChord3 }, \noteOff, page: page);
// chord 4:
~base.setGridMonitorFunc(3, 2, {
  if( ~light.arps.chord4IsPlaying,
    { ~base.turnGridGreen(3, 2) }, { ~base.turnGridBlue(3, 2) });
}, page: page);
~base.setGridFunc(3, 2, { ~light.arps.playChord4 }, page: page);
~base.setGridFunc(3, 2, { ~light.arps.stopChord4 }, \noteOff, page: page);
// chord 5:
~base.setGridMonitorFunc(4, 2, {
  if( ~light.arps.chord5IsPlaying,
    { ~base.turnGridGreen(4, 2) }, { ~base.turnGridBlue(4, 2) });
}, page: page);
~base.setGridFunc(4, 2, { ~light.arps.playChord5 }, page: page);
~base.setGridFunc(4, 2, { ~light.arps.stopChord5 }, \noteOff, page: page);


//////// Bass:

8.do({ | i | ~base.turnGridMagenta(i, 0, page: page); });
8.do({ | i | ~base.turnGridMagenta(i, 1, page: page); });

// Low D:
~base.setGridFunc(0, 0, {
  ~light.bass.playNote(26.midicps);
  ~base.turnGridGreen(0, 0);
}, 'noteOn', page: page);
~base.setGridFunc(0, 0, {
  ~light.bass.releaseNote(26.midicps);
  ~base.turnGridMagenta(0, 0);
}, 'noteOff', page: page);

// E:
~base.setGridFunc(1, 0, {
  ~light.bass.playNote(28.midicps);
  ~base.turnGridGreen(1, 0);
}, 'noteOn', page: page);
~base.setGridFunc(1, 0, {
  ~light.bass.releaseNote(28.midicps);
  ~base.turnGridMagenta(1, 0);
}, 'noteOff', page: page);

// F#:
~base.setGridFunc(2, 0, {
  ~light.bass.playNote(30.midicps);
  ~base.turnGridGreen(2, 0);
}, 'noteOn', page: page);
~base.setGridFunc(2, 0, {
  ~light.bass.releaseNote(30.midicps);
  ~base.turnGridMagenta(2, 0);
}, 'noteOff', page: page);

// G:
~base.setGridFunc(3, 0, {
  ~light.bass.playNote(31.midicps);
  ~base.turnGridGreen(3, 0);
}, 'noteOn', page: page);
~base.setGridFunc(3, 0, {
  ~light.bass.releaseNote(31.midicps);
  ~base.turnGridMagenta(3, 0);
}, 'noteOff', page: page);


// A:
~base.setGridFunc(4, 0, {
  ~light.bass.playNote(33.midicps);
  ~base.turnGridGreen(4, 0);
}, 'noteOn', page: page);
~base.setGridFunc(4, 0, {
  ~light.bass.releaseNote(33.midicps);
  ~base.turnGridMagenta(4, 0);
}, 'noteOff', page: page);

// B:
~base.setGridFunc(5, 0, {
  ~light.bass.playNote(35.midicps);
  ~base.turnGridGreen(5, 0);
}, 'noteOn', page: page);
~base.setGridFunc(5, 0, {
  ~light.bass.releaseNote(35.midicps);
  ~base.turnGridMagenta(5, 0);
}, 'noteOff', page: page);

// C#:
~base.setGridFunc(6, 0, {
  ~light.bass.playNote(37.midicps);
  ~base.turnGridGreen(6, 0);
}, 'noteOn', page: page);
~base.setGridFunc(6, 0, {
  ~light.bass.releaseNote(37.midicps);
  ~base.turnGridMagenta(6, 0);
}, 'noteOff', page: page);

// D:
~base.setGridFunc(7, 0, {
  ~light.bass.playNote(38.midicps);
  ~base.turnGridGreen(7, 0);
}, 'noteOn', page: page);
~base.setGridFunc(7, 0, {
  ~light.bass.releaseNote(38.midicps);
  ~base.turnGridMagenta(7, 0);
}, 'noteOff', page: page);

// E:
~base.setGridFunc(1, 1, {
  ~light.bass.playNote(40.midicps);
  ~base.turnGridGreen(1, 1);
}, 'noteOn', page: page);
~base.setGridFunc(1, 1, {
  ~light.bass.releaseNote(40.midicps);
  ~base.turnGridMagenta(1, 1);
}, 'noteOff', page: page);

// F#:
~base.setGridFunc(2, 1, {
  ~light.bass.playNote(42.midicps);
  ~base.turnGridGreen(2, 1);
}, 'noteOn', page: page);
~base.setGridFunc(2, 1, {
  ~light.bass.releaseNote(42.midicps);
  ~base.turnGridMagenta(2, 1);
}, 'noteOff', page: page);

// G:
~base.setGridFunc(3, 1, {
  ~light.bass.playNote(43.midicps);
  ~base.turnGridGreen(3, 1);
}, 'noteOn', page: page);
~base.setGridFunc(3, 1, {
  ~light.bass.releaseNote(43.midicps);
  ~base.turnGridMagenta(3, 1);
}, 'noteOff', page: page);

// A:
~base.setGridFunc(4, 1, {
  ~light.bass.playNote(45.midicps);
  ~base.turnGridGreen(4, 1);
}, 'noteOn', page: page);
~base.setGridFunc(4, 1, {
  ~light.bass.releaseNote(45.midicps);
  ~base.turnGridMagenta(4, 1);
}, 'noteOff', page: page);

// B:
~base.setGridFunc(5, 1, {
  ~light.bass.playNote(47.midicps);
  ~base.turnGridGreen(5, 1);
}, 'noteOn', page: page);
~base.setGridFunc(5, 1, {
  ~light.bass.releaseNote(47.midicps);
  ~base.turnGridMagenta(5, 1);
}, 'noteOff', page: page);

// C#:
~base.setGridFunc(6, 1, {
  ~light.bass.playNote(49.midicps);
  ~base.turnGridGreen(6, 1);
}, 'noteOn', page: page);
~base.setGridFunc(6, 1, {
  ~light.bass.releaseNote(49.midicps);
  ~base.turnGridMagenta(6, 1);
}, 'noteOff', page: page);

// D:
~base.setGridFunc(7, 1, {
  ~light.bass.playNote(50.midicps);
  ~base.turnGridGreen(7, 1);
}, 'noteOn', page: page);
~base.setGridFunc(7, 1, {
  ~light.bass.releaseNote(50.midicps);
  ~base.turnGridMagenta(7, 1);
}, 'noteOff', page: page);


///////////////////
//// Sliders: ////
/////////////////

////// Volumes:

//// Main:
// Mute:
~base.setTouchButtonMonitorFunc(1, {
  if( ~light.mixerA.isMuted(0), { ~base.turnTouchButtonRed(1) }, { ~base.turnTouchButtonGreen(1) });
}, page: page);
~base.setTouchButtonFunc(1, { ~light.mixerA.tglMute(0) }, page: page);
// Volume:
~base.setFaderMode(1, 'magentaFill', page: page);
~base.setFaderMonitorFunc(1, {
  ~base.setFaderValue(1, ~light.mixerA.vol(0).dbfsCC);
}, page: page);
~base.setFaderFunc(1, { | val | ~light.mixerA.setVol(0, val.ccdbfs(-70, 0));
}, page: page);

//// Bass:
// Mute:
~base.setTouchButtonMonitorFunc(2, {
  if( ~light.mixerB.isMuted(0), { ~base.turnTouchButtonRed(2) }, { ~base.turnTouchButtonGreen(2) });
}, page: page);
~base.setTouchButtonFunc(2, { ~light.mixerB.tglMute(0) }, page: page);
// Volume:
~base.setFaderMode(2, 'cyanFill', page: page);
~base.setFaderMonitorFunc(2, {
  ~base.setFaderValue(2, ~light.mixerB.vol(0).dbfsCC);
}, page: page);
~base.setFaderFunc(2, { | val | ~light.mixerB.setVol(0, val.ccdbfs(-70, 0));
}, page: page);

//// Arps:
// Mute:
~base.setTouchButtonMonitorFunc(3, {
  if( ~light.mixerA.isMuted(1), { ~base.turnTouchButtonRed(3) }, { ~base.turnTouchButtonGreen(3) });
}, page: page);
~base.setTouchButtonFunc(3, { ~light.mixerA.tglMute(1) }, page: page);
// Volume:
~base.setFaderMode(3, 'whiteFill', page: page);
~base.setFaderMonitorFunc(3, {
  ~base.setFaderValue(3, ~light.mixerA.vol(1).dbfsCC);
}, page: page);
~base.setFaderFunc(3, { | val | ~light.mixerA.setVol(1, val.ccdbfs(-70, 0));
}, page: page);


//// Reverb:
// Mute:
~base.setTouchButtonMonitorFunc(4, {
  if( ~lightSend.mixerC.isMuted(0), { ~base.turnTouchButtonRed(4) }, { ~base.turnTouchButtonGreen(4) });
}, page: page);
~base.setTouchButtonFunc(4, { ~lightSend.mixerC.tglMute(0) }, page: page);
// Volume:
~base.setFaderMode(4, 'blueFill', page: page);
~base.setFaderMonitorFunc(4, {
  ~base.setFaderValue(4, ~lightSend.mixerC.vol(0).dbfsCC);
}, page: page);
~base.setFaderFunc(4, { | val | ~lightSend.mixerC.setVol(0, val.ccdbfs(-70, 0));
}, page: page);

//// Delays:
// Mute:
~base.setTouchButtonMonitorFunc(5, {
  if( ~lightSend.mixerC.isMuted(1), { ~base.turnTouchButtonRed(5) }, { ~base.turnTouchButtonGreen(5) });
}, page: page);
~base.setTouchButtonFunc(5, { ~lightSend.mixerC.tglMute(1) }, page: page);
// Volume:
~base.setFaderMode(5, 'blueFill', page: page);
~base.setFaderMonitorFunc(5, {
  ~base.setFaderValue(5, ~lightSend.mixerC.vol(1).dbfsCC);
}, page: page);
~base.setFaderFunc(5, { | val | ~lightSend.mixerC.setVol(1, val.ccdbfs(-70, 0));
}, page: page);

//// MultiShift:
// Mute:
~base.setTouchButtonMonitorFunc(6, {
  if( ~lightSend.mixerC.isMuted(2), { ~base.turnTouchButtonRed(6) }, { ~base.turnTouchButtonGreen(6) });
}, page: page);
~base.setTouchButtonFunc(6, { ~lightSend.mixerC.tglMute(2) }, page: page);
// Volume:
~base.setFaderMode(6, 'blueFill', page: page);
~base.setFaderMonitorFunc(6, {
  ~base.setFaderValue(6, ~lightSend.mixerC.vol(2).dbfsCC);
}, page: page);
~base.setFaderFunc(6, { | val | ~lightSend.mixerC.setVol(2, val.ccdbfs(-70, 0));
}, page: page);