/*
Wednesday, April 25th 2018
Meaning Base.scd
prm

everything was pregnant with meaning
*/

var page = 'meaning';

~base.makePage(page);

/////////////////
//// Grid //////
///////////////

///////// main samples:


///// note 1:
~base.setGridMonitorFunc(0, 3, {
  if( ~meaning.main.note1IsPlaying == true,
    { ~base.turnGridGreen(0, 3) }, { ~base.turnGridMagenta(0, 3) });
}, page: page);

~base.setGridFunc(0, 3, { ~meaning.main.playNote1; }, 'noteOn', page: page);
~base.setGridFunc(0, 3, { ~meaning.main.releaseNote1 }, 'noteOff', page: page);


///// note 2:
~base.setGridMonitorFunc(1, 3, {
  if( ~meaning.main.note2IsPlaying == true,
    { ~base.turnGridGreen(1, 3) }, { ~base.turnGridMagenta(1, 3) });
}, page: page);

~base.setGridFunc(1, 3, { ~meaning.main.playNote2; }, 'noteOn', page: page);
~base.setGridFunc(1, 3, { ~meaning.main.releaseNote2 }, 'noteOff', page: page);

///// note 3:
~base.setGridMonitorFunc(2, 3, {
  if( ~meaning.main.note3IsPlaying == true,
    { ~base.turnGridGreen(2, 3) }, { ~base.turnGridMagenta(2, 3) });
}, page: page);

~base.setGridFunc(2, 3, { ~meaning.main.playNote3; }, 'noteOn', page: page);
~base.setGridFunc(2, 3, { ~meaning.main.releaseNote3; }, 'noteOff', page: page);

///// note 4:
~base.setGridMonitorFunc(3, 3, {
  if( ~meaning.main.note4IsPlaying == true,
    { ~base.turnGridGreen(3, 3) }, { ~base.turnGridMagenta(3, 3) });
}, page: page);

~base.setGridFunc(3, 3, { ~meaning.main.playNote4; }, 'noteOn', page: page);
~base.setGridFunc(3, 3, { ~meaning.main.releaseNote4; }, 'noteOff', page: page);


/*
//////// Synth:

//// chord 1:
~base.setGridMonitorFunc(0, 1, {
  if( ~meaning.synth.chord1IsPlaying,
    { ~base.turnGridGreen(0, 1) }, { ~base.turnGridCyan(0, 1) });
}, page: page);

~base.setGridFunc(0, 1, { ~meaning.synth.playChord1 }, 'noteOn', page: page);
~base.setGridFunc(0, 1, { ~meaning.synth.releaseChord1 }, 'noteOff', page: page);

//// chord 2:
~base.setGridMonitorFunc(1, 1, {
  if( ~meaning.synth.chord2IsPlaying,
    { ~base.turnGridGreen(1, 1) }, { ~base.turnGridCyan(1, 1) });
}, page: page);

~base.setGridFunc(1, 1, { ~meaning.synth.playChord2 }, 'noteOn', page: page);
~base.setGridFunc(1, 1, { ~meaning.synth.releaseChord2 }, 'noteOff', page: page);

//// chord 3:
~base.setGridMonitorFunc(2, 1, {
  if( ~meaning.synth.chord3IsPlaying,
    { ~base.turnGridGreen(2, 1) }, { ~base.turnGridCyan(2, 1) });
}, page: page);

~base.setGridFunc(2, 1, { ~meaning.synth.playChord3 }, 'noteOn', page: page);
~base.setGridFunc(2, 1, { ~meaning.synth.releaseChord3 }, 'noteOff', page: page);

//// chord 4:
~base.setGridMonitorFunc(3, 1, {
  if( ~meaning.synth.chord4IsPlaying,
    { ~base.turnGridGreen(3, 1) }, { ~base.turnGridCyan(3, 1) });
}, page: page);

~base.setGridFunc(3, 1, { ~meaning.synth.playChord4 }, 'noteOn', page: page);
~base.setGridFunc(3, 1, { ~meaning.synth.releaseChord4 }, 'noteOff', page: page);
*/


//////////////
//// Top: ///
////////////

//////// Hiss:
//// Mute:
~base.setTouchButtonMonitorFunc(1, {
  if( ~meaning.mixer.isMuted(0) == true,
    { ~base.turnTouchButtonRed(1) }, { ~base.turnTouchButtonGreen(1) });
}, page: page);
~base.setTouchButtonFunc(1, { ~meaning.mixer.tglMute(0); }, page: page);

//// Volume:
~base.setFaderMode(1, 'whiteFill', page: page);
~base.setFaderMonitorFunc(1, {
  ~base.setFaderValue(1, ~meaning.mixer.vol(0).dbfsCC)
}, page: page);
~base.setFaderFunc(1, { | val |
  ~meaning.mixer.setVol(0, val.ccdbfs);
}, page: page);

/////// Main:
//// Mute:
~base.setTouchButtonMonitorFunc(2, {
  if( ~meaning.mixer.isMuted(1) == true,
    { ~base.turnTouchButtonRed(2) }, { ~base.turnTouchButtonGreen(2) });
}, page: page);
~base.setTouchButtonFunc(2, { ~meaning.mixer.tglMute(1); }, page: page);

//// Volume:
~base.setFaderMode(2, 'magentaFill', page: page);
~base.setFaderMonitorFunc(2, {
  ~base.setFaderValue(2, ~meaning.mixer.vol(1).dbfsCC)
}, page: page);
~base.setFaderFunc(2, { | val |
  ~meaning.mainMixer.setVol(1, val.ccdbfs);
}, page: page);

////// Synth:
//// Mute:
~base.setTouchButtonMonitorFunc(3, {
  if( ~meaning.mainMixer.isMuted(2) == true,
    { ~base.turnTouchButtonRed(3) }, { ~base.turnTouchButtonGreen(3) });
}, page: page);
~base.setTouchButtonFunc(3, { ~meaning.mainMixer.tglMute(2); }, page: page);

//// Volume:
~base.setFaderMode(3, 'cyanFill', page: page);
~base.setFaderMonitorFunc(3, {
  ~base.setFaderValue(3, ~meaning.mainMixer.vol(2).dbfsCC)
}, page: page);
~base.setFaderFunc(3, { | val |
  ~meaning.mainMixer.setVol(2, val.ccdbfs);
}, page: page);

/////// Reverb:
//// Mute:
~base.setTouchButtonMonitorFunc(4, {
  if( ~meaning.returns.isMuted(0) == true,
    { ~base.turnTouchButtonRed(4) }, { ~base.turnTouchButtonGreen(4) });
}, page: page);
~base.setTouchButtonFunc(4, { ~meaning.returns.tglMute(0); }, page: page);

//// Volume:
~base.setFaderMode(4, 'yellowFill', page: page);
~base.setFaderMonitorFunc(4, {
  ~base.setFaderValue(4, ~meaning.returns.vol(0).dbfsCC)
}, page: page);
~base.setFaderFunc(4, { | val |
  ~meaning.returns.setVol(0, val.ccdbfs);
}, page: page);


/////// Delay:
//// Mute:
~base.setTouchButtonMonitorFunc(5, {
  if( ~meaning.returns.isMuted(1) == true,
    { ~base.turnTouchButtonRed(5) }, { ~base.turnTouchButtonGreen(5) });
}, page: page);
~base.setTouchButtonFunc(5, { ~meaning.returns.tglMute(1); }, page: page);

//// Volume:
~base.setFaderMode(5, 'yellowFill', page: page);
~base.setFaderMonitorFunc(5, {
  ~base.setFaderValue(5, ~meaning.returns.vol(1).dbfsCC)
}, page: page);
~base.setFaderFunc(5, { | val |
  ~meaning.returns.setVol(1, val.ccdbfs);
}, page: page);



/////// High Pass:
~base.setFaderMode(6, 'whiteFill', page: page);
~base.setFaderMonitorFunc(6, {
  var cutoff = ~meaning.synth.eq.highPassCutoff.explin(100, 14000, 0, 127);
  ~base.setFaderValue(6, cutoff);
}, page: page);
~base.setFaderFunc(6, { | val |
  var cutoff = val.linexp(0, 127, 100, 14000);
  ~meaning.synth.eq.setHighPassCutoff(cutoff);
}, page: page);

/////// Sample Low Pass:
~base.setFaderMode(7, 'whiteFill', page: page);
~base.setFaderMonitorFunc(7, {
  var cutoff = ~meaning.main.sampler.filterCutoff.explin(20, 20000, 0, 127);
  ~base.setFaderValue(7, cutoff);
}, page: page);
~base.setFaderFunc(7, { | val |
  var cutoff = val.linexp(0, 127, 20, 20000);
  ~meaning.main.sampler.setFilterCutoff(cutoff);
}, page: page);

/////// Main Modular Send:
~base.setFaderMode(8, 'blueFill', page: page);
~base.setFaderMonitorFunc(8, {
  ~base.setFaderValue(8, ~meaning.mainMixer.sendVol(1, 2).dbfsCC);
}, page: page);
~base.setFaderFunc(8, { | val |
  ~meaning.mainMixer.setSendVol(1, 2, val.ccdbfs);
}, page: page);

