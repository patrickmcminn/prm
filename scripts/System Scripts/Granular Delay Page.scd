/*
Wednesday, March 18th 2015
Granular Delay Page.scd
prm
Red Emma's
Baltimore, MD
*/

var page = 'granulator';
var granulatorBank = 0;
var delayBank = 1;

var grainEnvResetFunc, grainRateResetFunc, delayFilterResetFunc;

~ohm.makePage(page);
~ohm.addLeftKnobsBanks(1);



//////// Left Knobs:

//// Granulator:
// mix:
~ohm2.setLeftKnobFunc(0, 0, { | val |
  var mix = val.linlin(0, 127, -1, 1);
  ~prm.granularDelay.setGranulatorCrossfade(mix);
}, page: page, bank: granulatorBank);
// grain dur low:
~ohm2.setLeftKnobFunc(1, 0, { | val |
  var durLow = val.linexp(0, 127, 0.005, 1);
  ~prm.granularDelay.setGrainDurLow(durLow);
}, page: page, bank: granulatorBank);
// grain dur high:
~ohm2.setLeftKnobFunc(2, 0, { | val |
  var durHigh = val.linexp(0, 127, 0.01, 2.5);
  ~prm.granularDelay.setGrainDurHigh(durHigh);
}, page: page, bank: granulatorBank);
// trig rate:
~ohm2.setLeftKnobFunc(3, 0, { | val |
  var trigRate = val.linexp(0, 127, 5, 65);
  ~prm.granularDelay.setTrigRate(trigRate);
}, page: page, bank: granulatorBank);



//// Delay:

// mix:
~ohm2.setLeftKnobFunc(0, 0, { | val |
  var mix = val.linlin(0, 127, 0, 1);
  ~prm.granularDelay.setDelayMix(mix);
}, page: page, bank: delayBank);
//Delay Time:
~ohm2.setLeftKnobFunc(1, 0, { | val |
  var delay = val.linlin(0, 127, 0.05, 4);
  ~prm.granularDelay.setDelayTime(delay);
}, page: page, bank: delayBank);
// feedback:
~ohm2.setLeftKnobFunc(2, 0, { | val |
  var feedback = val.linlin(0, 127, 0, 0.95);
  ~prm.granularDelay.setFeedback(feedback);
}, page: page, bank: delayBank);
// filter cutoff:
/*
~ohm2.setLeftKnobFunc(3, 0, { | val |
  var cutoff = val.linexp(100, 15000);
  ~prm.granularDelay.setDelayFilterCutoff(cutoff);
}, page: page);
*/


////////////
///GRID/////
////////////

//////// granular:

// envelopes:
grainEnvResetFunc = {
  7.do({ | i | ~ohm2.turnGridBlue(i, 0, page: page); });
};
grainEnvResetFunc.value;
~ohm2.turnGridGreen(0, 0, page: page);
~ohm2.setGridFunc(0, 0, {
  grainEnvResetFunc.value;
  ~ohm2.turnGridGreen(0, 0);
  ~lucky.drone.granularDelay.setGrainEnvelope('hanning');
}, page: page);
~ohm2.setGridFunc(1, 0, {
  grainEnvResetFunc.value;
  ~ohm2.turnGridGreen(1, 0);
  ~lucky.drone.granularDelay.setGrainEnvelope('gabor');
}, page: page);
~ohm2.setGridFunc(2, 0, {
  grainEnvResetFunc.value;
  ~ohm2.turnGridGreen(2, 0);
  ~lucky.drone.granularDelay.setGrainEnvelope('gabWide');
}, page: page);
~ohm2.setGridFunc(3, 0, {
  grainEnvResetFunc.value;
  ~ohm2.turnGridGreen(3, 0);
  ~lucky.drone.granularDelay.setGrainEnvelope('perc');
}, page: page);
~ohm2.setGridFunc(4, 0, {
  grainEnvResetFunc.value;
  ~ohm2.turnGridGreen(4, 0);
  ~lucky.drone.granularDelay.setGrainEnvelope('revPerc');
}, page: page);
~ohm2.setGridFunc(5, 0, {
  grainEnvResetFunc.value;
  ~ohm2.turnGridGreen(5, 0);
  ~lucky.drone.granularDelay.setGrainEnvelope('expodec');
}, page: page);
~ohm2.setGridFunc(6, 0, {
  grainEnvResetFunc.value;
  ~ohm2.turnGridGreen(6, 0);
  ~lucky.drone.granularDelay.setGrainEnvelope('rexpodec');
}, page: page);

// play rates:
grainRateResetFunc = {
  6.do({ | i | ~ohm2.turnGridCyan(i, 1, page: page); });
};
grainRateResetFunc.value;
~ohm2.turnGridGreen(1, 1, page: page);

~ohm2.setGridFunc(0, 1, {
  grainRateResetFunc.value;
  ~ohm2.turnGridGreen(0, 1);
  ~lucky.drone.granularDelay.setRate(0.5, 0.5);
}, page: page);
~ohm2.setGridFunc(1, 1, {
  grainRateResetFunc.value;
  ~ohm2.turnGridGreen(1, 1);
  ~lucky.drone.granularDelay.setRate(1, 1);
}, page: page);
~ohm2.setGridFunc(2, 1, {
  grainRateResetFunc.value;
  ~ohm2.turnGridGreen(2, 1);
  ~lucky.drone.granularDelay.setRate(2, 2);
}, page: page);
~ohm2.setGridFunc(3, 1, {
  grainRateResetFunc.value;
  ~ohm2.turnGridGreen(3, 1);
  ~lucky.drone.granularDelay.setRate(4, 4);
}, page: page);
~ohm2.setGridFunc(4, 1, {
  grainRateResetFunc.value;
  ~ohm2.turnGridGreen(4, 1);
  ~lucky.drone.granularDelay.setRate(0.25, 0.75);
}, page: page);
~ohm2.setGridFunc(5, 1, {
  grainRateResetFunc.value;
  ~ohm2.turnGridGreen(5, 1);
  ~lucky.drone.granularDelay.setRate(1, 2);
}, page: page);

//////// delay:
// filter type:
delayFilterResetFunc = {
  4.do({ | i | ~ohm2.turnGridPurple(i, 4, page: page); });
};
delayFilterResetFunc.value;
~ohm2.turnGridGreen(0, 4, page: page);

// none:
~ohm2.setGridFunc(0, 4, {
  delayFilterResetFunc.value;
  ~ohm2.turnGridGreen(0, 4);
  ~lucky.drone.granularDelay.setDelayFilterType('none');
}, page: page);

// lowpass:
~ohm2.setGridFunc(1, 4, {
  delayFilterResetFunc.value;
  ~ohm2.turnGridGreen(1, 4);
  ~lucky.drone.granularDelay.setDelayFilterType('lowPass');
}, page: page);

// high pass:
~ohm2.setGridFunc(2, 4, {
  delayFilterResetFunc.value;
  ~ohm2.turnGridGreen(2, 4);
  ~lucky.drone.granularDelay.setDelayFilterType('highPass');
}, page: page);

// bandpass:
~ohm2.setGridFunc(3, 4, {
  delayFilterResetFunc.value;
  ~ohm2.turnGridGreen(3, 4);
  ~lucky.drone.granularDelay.setDelayFilterType('bandPass');
}, page: page);

