/*
Wednesday, January 10th 2018
WasntKnownBase.scd
prm
*/

var page = 'wasntKnown';
var mainBank = 0;

//////////////////
//// Faders /////
////////////////


////// Main:
// Mute:
~base.setTouchButtonMonitorFunc(1, {
  if( ~wasntKnown.sampler.mixer.isMuted(0),
    { ~base.turnTouchButtonRed(1) }, { ~base.turnTouchButtonGreen(1) });
}, bank: mainBank, page: page);
~base.setTouchButtonFunc(1, {
  ~wasntKnown.sampler.mixer.tglMute(0);
}, bank: mainBank, page: page);
// Volume:
~base.setFaderMode(1, 'cyanFill', bank: mainBank, page: page);
~base.setFaderMonitorFunc(1, {
  ~base.setFaderValue(1, ~wasntKnown.sampler.mixer.vol(0).dbfsCC);
}, bank: mainBank, page: page);
~base.setFaderFunc(1, { | val|
  var vol = val.ccdbfs;
  ~wasntKnown.sampler.mixer.setVol(0, vol);
}, bank: mainBank, page: page);

////// EQ:
// Mute:
~base.setTouchButtonMonitorFunc(2, {
  if( ~wasntKnown.sampler.mixer.isMuted(1),
    { ~base.turnTouchButtonRed(2) }, { ~base.turnTouchButtonGreen(2) });
}, bank: mainBank, page: page);
~base.setTouchButtonFunc(2, {
  ~wasntKnown.sampler.mixer.tglMute(1);
}, bank: mainBank, page: page);
// Volume:
~base.setFaderMode(2, 'cyanFill', bank: mainBank, page: page);
~base.setFaderMonitorFunc(2, {
  ~base.setFaderValue(2, ~wasntKnown.sampler.mixer.vol(1).dbfsCC);
}, bank: mainBank, page: page);
~base.setFaderFunc(2, { | val|
  var vol = val.ccdbfs;
  ~wasntKnown.sampler.mixer.setVol(1, vol);
}, bank: mainBank, page: page);

////// Reverb Send:
~base.setFaderMode(3, 'magentaFill', bank: mainBank, page: page);
~base.setFaderMonitorFunc(4, {
  ~base.setFaderValue(3, ~wasntKnown.mixerA.sendVol(0, 0).dbfsCC);
}, bank: mainBank, page: page);
~base.setFaderFunc(3, { | val |
  var sendVol = val.ccdbfs;
  ~wasntKnown.mixerA.setSendVol(0, 0, sendVol);
}, bank: mainBank, page: page);


////// Granulator Send:
~base.setFaderMode(4, 'magentaFill', bank: mainBank, page: page);
~base.setFaderMonitorFunc(4, {
  ~base.setFaderValue(4, ~wasntKnown.mixerA.sendVol(0, 1).dbfsCC);
}, bank: mainBank, page: page);
~base.setFaderFunc(4, { | val |
  var sendVol = val.ccdbfs;
  ~wasntKnown.mixerA.setSendVol(0, 1, sendVol);
}, bank: mainBank, page: page);

////// Modular Send:
~base.setFaderMode(5, 'magentaFill', bank: mainBank, page: page);
~base.setFaderMonitorFunc(5, {
  ~base.setFaderValue(5, ~wasntKnown.mixerA.sendVol(0, 2).dbfsCC);
}, bank: mainBank, page: page);
~base.setFaderFunc(5, { | val |
  var sendVol = val.ccdbfs;
  ~wasntKnown.mixerA.setSendVol(0, 2, sendVol);
}, bank: mainBank, page: page);

////// Filter Cutoff:
~base.setFaderMode(6, 'yellowFill', bank: mainBank, page: page);
~base.setFaderMonitorFunc(6, {
  var val = ~wasntKnown.sampler.filter.filterCutoff.explin(80, 20000, 0, 127);
  ~base.setFaderValue(6, val);
}, bank: mainBank, page: page);
~base.setFaderFunc(6, { | val |
  var cutoff = val.linexp(0, 127, 80, 20000);
  ~wasntKnown.sampler.setFilterCutoff(cutoff);
}, bank: mainBank, page: page);

////// Start Position:
~base.setFaderMode(7, 'whiteWalk', bank: mainBank, page: page);
~base.setFaderMonitorFunc(7, {
  var val = ~wasntKnown.sampler.startPos.linlin(0, 1, 0, 127);
  ~base.setFaderValue(7, val);
}, bank: mainBank, page: page);
~base.setFaderFunc(7, { | val |
  var pos = val.linlin(0, 127, 0, 1);
  ~wasntKnown.sampler.setStartPos(pos);
}, bank: mainBank, page: page);

////// End Position:
~base.setFaderMode(8, 'whiteWalk', bank: mainBank, page: page);
~base.setFaderMonitorFunc(8, {
  var val = ~wasntKnown.sampler.endPos.linlin(0, 1, 0, 127);
  ~base.setFaderValue(8, val);
}, bank: mainBank, page: page);
~base.setFaderFunc(8, { | val |
  var pos = val.linlin(0, 127, 0, 1);
  ~wasntKnown.sampler.setEndPos(pos);
}, bank: mainBank, page: page);

//////////////////
//// Buttons ////
////////////////

//////// Reset:

//// Sample 1:

//// Sample 2:

//// Sample 3:

//// Sample 4:

//// Sample 5:

//// Sample 6:

//// Sample 7:

//// Sample 8:



y.setFaderMode(8, 'whiteEQ');

y.setFaderValue(8, 0);

Base


x = WasntKnown_Sampler.new(0);

x.playSample7;
x.releaseSample7;

x.setReleaseTime(3);

x.setStartPos(0.3);
x.setEndPos(0.55);

Server.killAll;

x.mixer.tglMute(1);
x.mixer.setVol(1, 0);

x.setFilterCutoff(20000);

x.filter.mixer.tglMute;

x.filter.setCutoff(20000);

x = WasntKnown.new(0, 0, 0);

x.sampler.playSample1;
x.sampler.releaseSample1;

~prm = AudioSystem.new;

(
~wasntKnown = WasntKnown.new(~prm.submixA, ~prm.submixB, ~prm.submixC,
  ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, nil, ~prm.procGroup, \addToHead);
);

~wasntKnown.sampler.playSample4;
~wasntKnown.sampler.releaseSample4;

~wasntKnown.sampler.setFilterCutoff(200);

~wasntKnown.mixerA.setSendVol(0, 0, -3);

~wasntKnown.isLoaded;

~base = Base.new;
~base.makePage('wasntKnown');
~base.setPage('wasntKnown');

y.setFaderValue(1, 127);