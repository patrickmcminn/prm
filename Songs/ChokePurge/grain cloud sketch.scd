/*
prm
Tuesday, September 18th 2018
grain cloud sketch.scd
*/

Server.killAll;

~prm = AudioSystem.new(2);

~gc = GrainCloud2.new(~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus,
  ~prm.delay.inBus, ~prm.procGroup, \addToHead);

try { ~apc = APC40Mk2.new("mio10", "HST 2"); };

~apc.makePage('grainCloudSketch');
~apc.setPage('grainCloudSketch');


(
var page = 'grainCloudSketch';
var bank1 = 0;
var bank2 = 1;
var bank3 = 2;


~apc.addDeviceEncodersBanks(3, page);


//////// Bank1:

// sustain low:
~apc.setDeviceEncodersMonitorFunc(\susLow, {
  var sus = ~gc.sustainLow.linlin(0.00001, 0.3, 0, 127);
  ~apc.setDeviceEncoderValue(1, sus);
}, bank: bank1, page: page);
~apc.setDeviceEncoderFunc(1, { | val |
  var sustainLow = val.linlin(0, 127, 0.00001, 0.3);
  ~gc.setSustainLow(sustainLow);
}, bank: bank1, page: page);
// sustain high:
~apc.setDeviceEncodersMonitorFunc(\susHigh, {
  var sus = ~gc.sustainHigh.linlin(0.00005, 0.5, 0, 127);
  ~apc.setDeviceEncoderValue(2, sus);
}, bank: bank1, page: page);
~apc.setDeviceEncoderFunc(2, { | val |
  var sustainHigh = val.linlin(0, 127, 0.00005, 0.5);
  ~gc.setSustainHigh(sustainHigh);
}, bank: bank1, page: page);

// trig rate low:
~apc.setDeviceEncodersMonitorFunc(\trLow, {
  var rate = ~gc.trigRateLow.linlin(1, 64, 0, 127);
  ~apc.setDeviceEncoderValue(3, rate);
}, bank: bank1, page: page);
~apc.setDeviceEncoderFunc(3, { | val |
  var rate = val.linlin(0, 127, 1.0, 64.0);
  ~gc.setTrigRateLow(rate);
}, bank: bank1, page: page);
// trig rate high:
~apc.setDeviceEncodersMonitorFunc(\trHigh, {
  var rate = ~gc.trigRateHigh.linlin(1, 64, 0, 127);
  ~apc.setDeviceEncoderValue(4, rate);
}, bank: bank1, page: page);
~apc.setDeviceEncoderFunc(4, { | val |
  var rate = val.linlin(0, 127, 1, 64);
  ~gc.setTrigRateHigh(rate);
}, bank: bank1, page: page);

// cutoff low:
~apc.setDeviceEncodersMonitorFunc(\cutoffLow, {
  var cutoff = ~gc.cutoffLow.explin(50, 20000, 0, 127);
  ~apc.setDeviceEncoderValue(5, cutoff);
}, bank: bank1, page: page);
~apc.setDeviceEncoderFunc(5, { | val |
  var cutoff = val.linexp(0, 127, 50, 20000);
  ~gc.setCutoffLow(cutoff);
}, bank: bank1, page: page);
// cutoff high:
~apc.setDeviceEncodersMonitorFunc(\cutoffHigh, {
  var cutoff = ~gc.cutoffHigh.explin(50, 20000, 0, 127);
  ~apc.setDeviceEncoderValue(6, cutoff);
}, bank: bank1, page: page);
~apc.setDeviceEncoderFunc(6, { | val |
  var cutoff = val.linexp(0, 127, 50, 20000);
  ~gc.setCutoffHigh(cutoff);
}, bank: bank1, page: page);

// rq low:
~apc.setDeviceEncodersMonitorFunc(\rqLow, {
  var rq = 1 - ~gc.rqLow;
  rq = rq.linlin(0.01, 1, 0, 127);
  ~apc.setDeviceEncoderValue(7, rq);
}, bank: bank1, page: page);
~apc.setDeviceEncoderFunc(7, { | val |
  var rq = val.linlin(0.01, 127, 1, 0);
  rq.postln;
  ~gc.setRQLow(rq);
}, bank: bank1, page: page);
// rq high:
~apc.setDeviceEncodersMonitorFunc(\rqHigh, {
  var rq = 1 - ~gc.rqHigh;
  rq = rq.linlin(0.01, 1, 0, 127);
}, bank: bank1, page: page);
~apc.setDeviceEncoderFunc(8, { | val |
  var rq = val.linlin(0, 127, 1, 0.01);
  ~gc.setRQHigh(rq);
}, bank: bank1, page: page);
)

0.46460351208757.linlin(1, 0.01, 0, 127);

//////// Bank2:

//////// Bank3:
s.meter

~gc.addNote(60);
~gc.removeNote(60);
~gc.setInstArray([\percSaw]);
~gc.setInstArray([\gaborGendy]);
~gc.setInstArray([\gaborGendy, \rexpodecSaw]);
~gc.setInstArray([\expodecSaw]);

~gc.setPatternAmpLow(0.1);
~gc.setPatternAmpHigh(0.3);

~gc.toggleNote(58);
~gc.toggleNote(46);
~gc.toggleNote(34);
~gc.toggleNote(22);

~gc.toggleNote(27);
~gc.toggleNote(39);

~gc.toggleNote(29);
~gc.toggleNote(41);
~gc.toggleNote(53);
~gc.toggleNote(65);

~gc.toggleNote(54);

~gc.toggleNote(61);
~gc.toggleNote(60);
~gc.toggleNote(73);
~gc.toggleNote(67);
~gc.toggleNote(85);


s.makeWindow;




