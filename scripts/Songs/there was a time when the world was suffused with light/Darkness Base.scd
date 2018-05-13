/*
Monday, April 30th 2018
Darkness Base.scd
prm

a shallow eclipsing darkness
*/


var page = 'darkness';
var tempo = 50;

var scale = [1, 3, 4, 6, 8, 9, 11, 13];

~base.makePage(page);

//////////////////
//// Faders:  //
////////////////

///////// High Pass:
~base.setFaderMode(1, 'yellowFill', page: page);
~base.setFaderMonitorFunc(1, {
  //var cutoff = ~prm.masterEQ.highPassCutoff.explin(1, 1100, 0, 127);
  //~base.setFaderValue(1, cutoff);
  var gain = ~prm.masterEQ.lowGain.linlin(-70, 6, 0, 127);
  ~base.setFaderValue(1, gain);
}, page: page);
~base.setFaderFunc(1, { | val |
  //var cutoff = val.linexp(0, 127, 1, 1000);
  //~prm.masterEQ.setHighPassCutoff(cutoff);
  ~prm.masterEQ.setLowGain(val.ccdbfs(-50, 0));
}, page: page);

///////// bass tempo:
~base.setFaderMode(2, 'cyanFill', page: page);
~base.setFaderMonitorFunc(2, {
  var t = tempo.linlin(100, 2000, 0, 127);
  ~base.setFaderValue(2, t);
}, page: page);
~base.setFaderFunc(2, { | val |
  var t = val.linlin(0, 127, 100, 2000);
  tempo = t;
  ~darkness.setBasslineTempo(tempo);
}, page: page);

///////// granulator vol:
~base.setFaderMode(3, 'whiteFill', page: page);
~base.setFaderMonitorFunc(3, {
  ~base.setFaderValue(3, ~darknessReturns.mixer.vol(1).dbfsCC);
}, page: page);
~base.setFaderFunc(3, { | val |
  ~darknessReturns.mixer.setVol(1, val.ccdbfs);
}, page: page);


/////////////////////////
///////// GRID /////////
///////////////////////

~base.turnAllGridWhite(page: page);

////// HIGH PASS //////
~base.turnGridYellow(0, 3, page: page);
/*
~base.setGridFunc(0, 3, { | val |
  var cutoff = val.linexp(0, 127, 0, 350);
  ~prm.masterEQ.setHighPassCutoff(cutoff);
  cutoff.postln;
}, 'pressure', page: page);
*/
/*
~base.setGridFunc(0, 3, {
  ~prm.masterEQ.setLowGain(-70);
}, page: page);
~base.setGridFunc(0, 3, {
  ~prm.masterEQ.setLowGain(0);
}, 'noteOff', page: page);
*/
~base.setGridFunc(0, 3, { | val |
  var gain = val.linlin(0, 127, 127, 0);
  ~prm.masterEQ.setLowGain(gain.ccdbfs);
}, 'pressure', page: page);

//////// TRUMPET LFO ////////
~base.turnGridBlue(1, 3, page: page);
~base.setGridFunc(1, 3, {
  ~darkness.trumpet.lowPassFilter.setCutoffLFOBottomRatio(0.1);
}, 'noteOn', page: page);
~base.setGridFunc(1, 3, {
  ~darkness.trumpet.lowPassFilter.setCutoffLFOBottomRatio(1);
}, 'noteOff', page: page);


//////////// keyboard:

8.do({ | i | ~base.turnGridMagenta(i, 0, page: page); ~base.turnGridMagenta(i, 1, page: page); });
8.do({ | note |
  var octaveOffset = 24;
  ~base.setGridFunc(note, 0, {
    ~darkness.bass.moog.playNote((scale[note] + octaveOffset).midicps);
    ~base.turnGridGreen(note, 0);
  }, 'noteOn', page: page);
  ~base.setGridFunc(note, 0, {
    ~darkness.bass.moog.releaseNote((scale[note] + octaveOffset).midicps);
    ~base.turnGridMagenta(note, 0);
  }, 'noteOff', page: page);
});
7.do({ | note |
  var octaveOffset = 36;
  ~base.setGridFunc(note+1, 1, {
    ~darkness.bass.moog.playNote((scale[note+1] + octaveOffset).midicps);
    ~base.turnGridGreen(note+1, 1);
  }, 'noteOn', page: page);
  ~base.setGridFunc(note+1, 1, {
    ~darkness.bass.moog.releaseNote((scale[note+1] + octaveOffset).midicps);
    ~base.turnGridMagenta(note+1, 1);
  }, 'noteOff', page: page);
});

