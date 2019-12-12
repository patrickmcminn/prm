/*
Thursday, December 12th 2019
Base Effects Page.scd
prm
*/

var page = 'effects';

var granulator = 0;

~base.makePage(page);

~base.turnAllGridBlue(page: page);


///// placeholder for send fx banks:
4.do({ | i |
	~base.turnControlButtonMagenta(i+1, 'left', page: page);
	~base.turnControlButtonMagenta(i+1, 'right', page: page);
});
~base.turnControlButtonGreen(2, 'left', page: page);

//////////////////////
//// Granulator: ////
////////////////////

//// grain size low:
~base.setFaderMode(1, 'magentaFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(1, {
	var dur = ~prm.granulator.granulator.grainDurLow.explin(0.005, 3, 0, 127);
	~base.setFaderValue(1, dur);
}, bank: granulator, page: page);
~base.setFaderFunc(1, { | val |
	var dur = val.linexp(0, 127, 0.005, 3);
	~prm.granulator.setGrainDurLow(dur);
}, bank: granulator, page: page);

//// grain size high:
~base.setFaderMode(2, 'magentaFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(2, {
	var dur = ~prm.granulator.granulator.grainDurHigh.explin(0.01, 3.5, 0, 127);
	~base.setFaderValue(2, dur);
}, bank: granulator, page: page);
~base.setFaderFunc(2, { | val |
	var dur = val.linexp(0, 127, 0.01, 3.5);
	~prm.granulator.setGrainDurHigh(dur);
}, bank: granulator, page: page);

//// trig rate:
~base.setFaderMode(3, 'whiteFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(3, {
	var rate = ~prm.granulator.granulator.trigRate.linlin(1, 64, 0, 127);
	~base.setFaderValue(3, rate);
}, bank: granulator, page: page);
~base.setFaderFunc(3, { | val |
	var rate = val.linlin(0, 127, 1, 64);
	~prm.granulator.setTrigRate(rate);
}, bank: granulator, page: page);

//// filter cutoff:
~base.setFaderMode(4, 'greenFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(4, {
	var cutoff = ~prm.granulator.granulator.filterCutoff.explin(150, 20000, 0, 127);
	~base.setFaderValue(4, cutoff);
}, bank: granulator, page: page);
~base.setFaderFunc(4, { | val |
	var cutoff = val.linexp(0, 127, 150, 20000);
	~prm.granulator.setGranulatorFilterCutoff(cutoff);
}, bank: granulator, page: page);

//// delay mix:
~base.setFaderMode(5, 'yellowFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(5, {
	var mix = ~prm.granulator.delay.mix.linlin(0, 1, 0, 127);
	~base.setFaderValue(5, mix);
}, bank: granulator, page: page);
~base.setFaderFunc(5, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~prm.granulator.delay.setMix(mix);
}, bank: granulator, page: page);

//// delay time:
~base.setFaderMode(6, 'yellowFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(6, {
	var delay = ~prm.granulator.delay.delayTime.linlin(0.01, 5, 0, 127);
	~base.setFaderValue(6, delay);
}, bank: granulator, page: page);
~base.setFaderFunc(6, { | val |
	var delay = val.linlin(0, 127, 0.01, 5);
	~prm.granulator.setDelayTime(delay);
}, bank: granulator, page: page);

//// delay feedback:
~base.setFaderMode(7, 'yellowFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(7, {
	var feedback = ~prm.granulator.delay.feedback.linlin(0, 1, 0, 127);
	~base.setFaderValue(7, feedback);
}, bank: granulator, page: page);
~base.setFaderFunc(7, { | val |
	var feedback = val.linlin(0, 127, 0, 1);
	~prm.granulator.setFeedback(feedback);
}, bank: granulator, page: page);