/*
Thursday, December 12th 2019
Base Effects Page.scd
prm
*/

var page = 'effects';

var activeGridBank = 1;
var reverb = 0;
var granulator = 1;
var modular = 2;
var delay = 3;

var bankReset;

~base.makePage(page);

~base.addGridBanks(3, page);
~base.addFadersBanks(3, page);
~base.addTouchButtonsBanks(3, page);

~base.turnAllGridCyan(bank: reverb, page: page);
~base.turnAllGridBlue(bank: granulator, page: page);
~base.turnAllGridRed(bank: modular, page: page);
~base.turnAllGridYellow(bank: delay, page: page);

4.do({ | i | ~base.turnControlButtonMagenta(i+1, 'left', page: page); });

bankReset = {
	~base.turnControlButtonCyan(1, 'right', page: page);
	~base.turnControlButtonBlue(2, 'right', page: page);
	~base.turnControlButtonRed(3, 'right', page: page);
	~base.turnControlButtonYellow(4, 'right', page: page);
};
bankReset.value;

~base.setActiveGridBank(granulator, page);
~base.setActiveFadersBank(granulator, page);
~base.setActiveTouchButtonsBank(granulator, page);

//////// bank change:
~base.setControlButtonMonitorFunc(1, {
	if( activeGridBank == reverb, { ~base.turnControlButtonGreen(1) }, { ~base.turnControlButtonCyan(1) });
	if( activeGridBank == granulator, { ~base.turnControlButtonGreen(2) },  { ~base.turnControlButtonBlue(2) });
	if( activeGridBank == modular, { ~base.turnControlButtonGreen(3) },  { ~base.turnControlButtonRed(3) });
	if( activeGridBank == delay, { ~base.turnControlButtonGreen(4) },  { ~base.turnControlButtonYellow(4) });
}, 0, page: page);
~base.setControlButtonFunc(1, {
	activeGridBank = reverb;
	~base.setActiveGridBank(activeGridBank, page);
	~base.setActiveFadersBank(activeGridBank, page);
	~base.setActiveTouchButtonsBank(activeGridBank, page);
}, bank: 0, page: page);
~base.setControlButtonFunc(2, {
	activeGridBank = granulator;
	~base.setActiveGridBank(activeGridBank, page);
	~base.setActiveFadersBank(activeGridBank, page);
	~base.setActiveTouchButtonsBank(activeGridBank, page);
}, bank: 0, page: page);
~base.setControlButtonFunc(3, {
	activeGridBank = modular;
	~base.setActiveGridBank(activeGridBank, page);
	~base.setActiveFadersBank(activeGridBank, page);
	~base.setActiveTouchButtonsBank(activeGridBank, page);
}, bank: 0, page: page);
~base.setControlButtonFunc(4, {
	activeGridBank = delay;
	~base.setActiveGridBank(activeGridBank, page);
	~base.setActiveFadersBank(activeGridBank, page);
	~base.setActiveTouchButtonsBank(activeGridBank, page);
}, bank: 0, page: page);

/*
///// placeholder for send fx banks:

4.do({ | i |
	~base.turnControlButtonMagenta(i+1, 'left', page: page);
	~base.turnControlButtonMagenta(i+1, 'right', page: page);
});
~base.turnControlButtonGreen(2, 'left', page: page);
*/

//////////////////////
//// Granulator: ////
////////////////////

//// grain size low:
~base.setFaderMode(1, 'magentaFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(1, {
	var dur = ~prm.granulator.grainDurLow.explin(0.005, 3, 0, 127);
	~base.setFaderValue(1, dur);
}, bank: granulator, page: page);
~base.setFaderFunc(1, { | val |
	var dur = val.linexp(0, 127, 0.005, 3);
	~prm.granulator.setGrainDurLow(dur);
}, bank: granulator, page: page);

//// grain size high:
~base.setFaderMode(2, 'magentaFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(2, {
	var dur = ~prm.granulator.grainDurHigh.explin(0.01, 3.5, 0, 127);
	~base.setFaderValue(2, dur);
}, bank: granulator, page: page);
~base.setFaderFunc(2, { | val |
	var dur = val.linexp(0, 127, 0.01, 3.5);
	~prm.granulator.setGrainDurHigh(dur);
}, bank: granulator, page: page);

//// trig rate:
~base.setFaderMode(3, 'whiteFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(3, {
	var rate = ~prm.granulator.trigRate.linlin(1, 64, 0, 127);
	~base.setFaderValue(3, rate);
}, bank: granulator, page: page);
~base.setFaderFunc(3, { | val |
	var rate = val.linlin(0, 127, 1, 64);
	~prm.granulator.setTrigRate(rate);
}, bank: granulator, page: page);

//// filter cutoff:
~base.setFaderMode(4, 'greenFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(4, {
	var cutoff = ~prm.granulator.filterCutoff.explin(150, 20000, 0, 127);
	~base.setFaderValue(4, cutoff);
}, bank: granulator, page: page);
~base.setFaderFunc(4, { | val |
	var cutoff = val.linexp(0, 127, 150, 20000);
	~prm.granulator.setFilterCutoff(cutoff);
}, bank: granulator, page: page);

//// delay level:
~base.setFaderMode(5, 'yellowFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(5, {
	var level = ~prm.granulator.delayLevel.linlin(0, 1, 0, 127);
	~base.setFaderValue(5, level);
}, bank: granulator, page: page);
~base.setFaderFunc(5, { | val |
	var level = val.linlin(0, 127, 0, 1);
	~prm.granulator.setDelayLevel(level);
}, bank: granulator, page: page);

//// delay time:
~base.setFaderMode(6, 'yellowFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(6, {
	var delay = ~prm.granulator.delayTime.linlin(0.01, 5, 0, 127);
	~base.setFaderValue(6, delay);
}, bank: granulator, page: page);
~base.setFaderFunc(6, { | val |
	var delay = val.linlin(0, 127, 0.01, 5);
	~prm.granulator.setDelayTime(delay);
}, bank: granulator, page: page);

//// delay feedback:
~base.setFaderMode(7, 'yellowFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(7, {
	var feedback = ~prm.granulator.feedback.linlin(0, 1, 0, 127);
	~base.setFaderValue(7, feedback);
}, bank: granulator, page: page);
~base.setFaderFunc(7, { | val |
	var feedback = val.linlin(0, 127, 0, 1);
	~prm.granulator.setFeedback(feedback);
}, bank: granulator, page: page);

///// pan:
~base.setFaderMode(8, 'magentaFill', bank: granulator, page: page);
~base.setFaderMonitorFunc(8, {
	var pan = ~prm.granulator.panHigh.linlin(0, 1, 0, 127);
	~base.setFaderValue(8, pan);
}, bank: granulator, page: page);
~base.setFaderFunc(8, { | val |
	var pan = val.linlin(0, 127, 0, 1);
	~prm.granulator.setPan(pan.neg, pan);
}, bank: granulator, page: page); 