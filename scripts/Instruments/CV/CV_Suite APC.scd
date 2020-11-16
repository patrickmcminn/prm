/*
Tuesday, July 28th 2020
CV_Suite APC.scd
prm
*/

var page = 'cvSuite';

var activeBank = 0;

var main = 0;
var lfo12 = 1;
var lfo34 = 2;
var trigEnv = 3;
var gateEnv = 4;
var gate = 5;
var constant = 6;
var freq = 7;
var pitchGate = 8;

~apc.makePage(page);
~apc.addGridBanks(8, page);
~apc.addDeviceEncodersBanks(8, page);
~apc.addMixerEncodersBanks(8, page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.storageDict[\loadMasterFunc].value;

////// edit select:
~apc.setSceneLaunchMonitorFunc(\sceneEdit, {
	if( activeBank == 0,
		{ ~apc.turnSceneLaunchButtonGreen(0, page: page) }, { ~apc.turnSceneLaunchButtonBlue(0, 1, page: page); });
}, page: page);
~apc.setSceneLaunchFunc(0, {
	activeBank = 0;
	~apc.setActiveGridBank(activeBank);
	~apc.setActiveDeviceEncodersBank(activeBank);
	~apc.setActiveMixerEncodersBank(activeBank);
}, page: page);
9.do({ | bank |
	8.do({ | slot |
		~apc.setGridFunc(slot, 4, {
			activeBank = slot+1;
			~apc.setActiveGridBank(activeBank);
			~apc.setActiveDeviceEncodersBank(activeBank);
			~apc.setActiveMixerEncodersBank(activeBank);
		}, bank: bank, page: page);
	});
	8.do({ | i | ~apc.turnGridLightBlue(i, 4, 1, bank, page); });
	if( bank != 0, { ~apc.turnGridGreen(bank-1, 4, 2, bank, page); });
});


/////////////////////
///// Main: ///////
//////////////////

///// Device Encoders:
~apc.setDeviceEncodersMonitorFunc(\main, {
	var lfo1, lfo2, lfo3, lfo4;
	var const1, const2;
	lfo1 = ~prm.cv.lfo1.frequency.explin(0.01, 18, 0, 127);
	lfo2 = ~prm.cv.lfo2.frequency.explin(0.01, 18, 0, 127);
	lfo3 = ~prm.cv.lfo3.frequency.explin(0.01, 18, 0, 127);
	lfo4 = ~prm.cv.lfo4.frequency.explin(0.01, 18, 0, 127);
	const1 = ~prm.cv.constant1.value.linlin(-0.5, 0.5, 0, 127);
	const2 = ~prm.cv.constant2.value.linlin(-0.5, 0.5, 0, 127);
	~apc.setDeviceEncoderValue(1, lfo1);
	~apc.setDeviceEncoderValue(2, lfo2);
	~apc.setDeviceEncoderValue(3, lfo3);
	~apc.setDeviceEncoderValue(4, lfo4);
	~apc.setDeviceEncoderValue(5, const1);
	~apc.setDeviceEncoderValue(6, const2);
}, main, page);

// lfo1:
~apc.setDeviceEncoderFunc(1, { | val |
	var freq = val.linexp(0, 127, 0.01, 18);
	~prm.cv.lfo1.setFreq(freq);
}, main, page);
// lfo2:
~apc.setDeviceEncoderFunc(2, { | val |
	var freq = val.linexp(0, 127, 0.01, 18);
	~prm.cv.lfo2.setFreq(freq);
}, main, page);
// lfo3:
~apc.setDeviceEncoderFunc(3, { | val |
	var freq = val.linexp(0, 127, 0.01, 18);
	~prm.cv.lfo3.setFreq(freq);
}, main, page);
// lfo4:
~apc.setDeviceEncoderFunc(4, { | val |
	var freq = val.linexp(0, 127, 0.01, 18);
	~prm.cv.lfo4.setFreq(freq);
}, main, page);
// constant 1:
~apc.setDeviceEncoderFunc(5, { | val |
	var value = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.constant1.setValue(value);
}, main, page);
// constant 2:
~apc.setDeviceEncoderFunc(6, { | val |
	var value = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.constant2.setValue(value);
}, main, page);

//////// grid:

// perc env trig 1:
~apc.turnGridMagenta(0, 2, 1, main, page);
~apc.setGridFunc(0, 2, {
	~apc.turnGridGreen(0, 2);
	~prm.cv.trigEnv1.trigger;
}, 'noteOn', main, page);
~apc.setGridFunc(0, 2, {
	~apc.turnGridMagenta(0, 2, 1);
}, 'noteOff', main, page);

// perc env trig 2:
~apc.turnGridMagenta(1, 2, 1, main, page);
~apc.setGridFunc(1, 2, {
	~apc.turnGridGreen(1, 2);
	~prm.cv.trigEnv2.trigger;
}, 'noteOn', main, page);
~apc.setGridFunc(1, 2, {
	~apc.turnGridMagenta(1, 2, 1);
}, 'noteOff', main, page);

// adsr env gate 1:
~apc.turnGridPink(2, 2, 1, main, page);
~apc.setGridFunc(2, 2, {
	~apc.turnGridGreen(2, 2);
	~prm.cv.gateEnv1.trigger;
}, 'noteOn', main, page);
~apc.setGridFunc(2, 2, {
	~apc.turnGridPink(2, 2, 1);
	~prm.cv.gateEnv1.release;
}, 'noteOff', main, page);

// adsr env gate 2:
~apc.turnGridPink(3, 2, 1, main, page);
~apc.setGridFunc(3, 2, {
	~apc.turnGridGreen(3, 2);
	~prm.cv.gateEnv2.trigger;
}, 'noteOn', main, page);
~apc.setGridFunc(3, 2, {
	~apc.turnGridPink(3, 2, 1);
	~prm.cv.gateEnv2.release;
}, 'noteOff', main, page);

// gate 1:
~apc.turnGridPurple(4, 2, 1, main, page);
~apc.setGridFunc(4, 2, {
	~apc.turnGridGreen(4, 2);
	~prm.cv.gate1.makeGate;
}, 'noteOn', main, page);
~apc.setGridFunc(4, 2, {
	~apc.turnGridPurple(4, 2, 1);
	~prm.cv.gate1.releaseGate;
}, 'noteOff', main, page);

// gate 2:
~apc.turnGridPurple(5, 2, 1, main, page);
~apc.setGridFunc(5, 2, {
	~apc.turnGridGreen(5, 2);
	~prm.cv.gate2.makeGate;
}, 'noteOn', main, page);
~apc.setGridFunc(5, 2, {
	~apc.turnGridPurple(5, 2, 1);
	~prm.cv.gate2.releaseGate;
}, 'noteOff', main, page);

/////////////////////
// LFOs 1 + 2: ////
//////////////////

///// LFO1:

// waveform:
~apc.setGridMonitorFunc(\lfo1Waveform, {
	if( ~prm.cv.lfo1.waveform == 0, { ~apc.turnGridGreen(0, 3, 2) }, { ~apc.turnGridBlue(0, 3, 1) });
	if( ~prm.cv.lfo1.waveform == 1, { ~apc.turnGridGreen(1, 3, 2) }, { ~apc.turnGridBlue(1, 3, 1) });
	if( ~prm.cv.lfo1.waveform == 2, { ~apc.turnGridGreen(2, 3, 2) }, { ~apc.turnGridBlue(2, 3, 1) });
	if( ~prm.cv.lfo1.waveform == 3, { ~apc.turnGridGreen(3, 3, 2) }, { ~apc.turnGridBlue(3, 3, 1) });
	if( ~prm.cv.lfo1.waveform == 4, { ~apc.turnGridGreen(4, 3, 2) }, { ~apc.turnGridBlue(4, 3, 1) });
	if( ~prm.cv.lfo1.waveform == 5, { ~apc.turnGridGreen(5, 3, 2) }, { ~apc.turnGridBlue(5, 3, 1) });
}, lfo12, page);
6.do({ | i | ~apc.setGridFunc(i, 3, {  ~prm.cv.lfo1.setWaveform(i); }, bank: lfo12, page: page); });

// output:
~apc.setGridMonitorFunc(\lfo1Output, {
	if( ~prm.cv.lfo1.out == ~modOutput[0], { ~apc.turnGridGreen(0, 2, 2) }, { ~apc.turnGridPink(0, 2, 1) });
	if( ~prm.cv.lfo1.out == ~modOutput[1], { ~apc.turnGridGreen(1, 2, 2) }, { ~apc.turnGridPink(1, 2, 1) });
	if( ~prm.cv.lfo1.out == ~modOutput[2], { ~apc.turnGridGreen(2, 2, 2) }, { ~apc.turnGridPink(2, 2, 1) });
	if( ~prm.cv.lfo1.out == ~modOutput[3], { ~apc.turnGridGreen(3, 2, 2) }, { ~apc.turnGridPink(3, 2, 1) });
	if( ~prm.cv.lfo1.out == ~modOutput[4], { ~apc.turnGridGreen(4, 2, 2) }, { ~apc.turnGridPink(4, 2, 1) });
	if( ~prm.cv.lfo1.out == ~modOutput[5], { ~apc.turnGridGreen(5, 2, 2) }, { ~apc.turnGridPink(5, 2, 1) });
	if( ~prm.cv.lfo1.out == ~modOutput[6], { ~apc.turnGridGreen(6, 2, 2) }, { ~apc.turnGridPink(6, 2, 1) });
	if( ~prm.cv.lfo1.out == ~modOutput[7], { ~apc.turnGridGreen(7, 2, 2) }, { ~apc.turnGridPink(7, 2, 1) });
}, lfo12, page);
8.do({ | i | ~apc.setGridFunc(i, 2, { ~prm.cv.lfo1.setOutBus(~modOutput[i]); }, bank: lfo12, page: page); });
~apc.turnGridRed(7, 3, 1, lfo12, page);
~apc.setGridFunc(7, 3, { ~prm.cv.lfo1.setOutBus(~prm.cv.nilBus); }, bank: lfo12, page: page);

//////// Knobs:

~apc.setMixerEncodersMonitorFunc(\lfo1, {
	var freq, pw, rangeLow, rangeHigh;
	freq = ~prm.cv.lfo1.frequency.explin(0.01, 18, 0, 127);
	pw = ~prm.cv.lfo1.pulseWidth.linlin(-1, 1, 0, 127);
	rangeLow = ~prm.cv.lfo1.rangeLow.linlin(-0.5, 0.5, 0, 127);
	rangeHigh = ~prm.cv.lfo1.rangeHigh.linlin(-0.5, 0.5, 0, 127);
	~apc.setMixerEncoderValue(1, freq);
	~apc.setMixerEncoderValue(2, pw);
	~apc.setMixerEncoderValue(3, rangeLow);
	~apc.setMixerEncoderValue(4, rangeHigh);
}, lfo12, page);
~apc.setMixerEncoderFunc(1, { | val |
	var freq = val.linexp(0, 127, 0.01, 18);
	~prm.cv.lfo1.setFrequency(freq);
}, lfo12, page);
~apc.setMixerEncoderFunc(2, { | val |
	var pw = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo1.setPulseWidth(pw);
}, lfo12, page);
~apc.setMixerEncoderFunc(3, { | val |
	var range = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.lfo1.setRangeLow(range);
}, lfo12, page);
~apc.setMixerEncoderFunc(4, { | val |
	var range = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.lfo1.setRangeHigh(range);
}, lfo12, page);

~apc.setDeviceEncodersMonitorFunc(\lfo1, {
	var freq, waveform, rangeLow, rangeHigh;
	freq = ~prm.cv.lfo1.frequency.explin(0.001, 40, 0, 127);
	waveform = ~prm.cv.lfo1.waveform.linlin(0, 5, 0, 127);
	rangeLow = ~prm.cv.lfo1.rangeLow.linlin(-1, 1, 0, 127);
	rangeHigh = ~prm.cv.lfo1.rangeHigh.linlin(-1, 1, 0, 127);
	~apc.setDeviceEncoderValue(1, freq);
	~apc.setDeviceEncoderValue(2, waveform);
	~apc.setDeviceEncoderValue(3, rangeLow);
	~apc.setDeviceEncoderValue(4, rangeHigh);
}, lfo12, page);
~apc.setDeviceEncoderFunc(1, { | val |
	var freq = val.linexp(0, 127, 0.001, 40);
	~prm.cv.lfo1.setFrequency(freq);
}, lfo12, page);
~apc.setDeviceEncoderFunc(2, { | val |
	var waveform = val.linlin(0, 127, 0, 5);
	~prm.cv.lfo1.setWaveform(waveform);
}, lfo12, page);
~apc.setDeviceEncoderFunc(3, { | val |
	var range = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo1.setRangeLow(range);
}, lfo12, page);
~apc.setDeviceEncoderFunc(4, { | val |
	var range = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo1.setRangeHigh(range);
}, lfo12, page);

///// LFO2:

// waveform:
~apc.setGridMonitorFunc(\lfo2Waveform, {
	if( ~prm.cv.lfo2.waveform == 0, { ~apc.turnGridGreen(0, 1, 2) }, { ~apc.turnGridPurple(0, 1, 1) });
	if( ~prm.cv.lfo2.waveform == 1, { ~apc.turnGridGreen(1, 1, 2) }, { ~apc.turnGridPurple(1, 1, 1) });
	if( ~prm.cv.lfo2.waveform == 2, { ~apc.turnGridGreen(2, 1, 2) }, { ~apc.turnGridPurple(2, 1, 1) });
	if( ~prm.cv.lfo2.waveform == 3, { ~apc.turnGridGreen(3, 1, 2) }, { ~apc.turnGridPurple(3, 1, 1) });
	if( ~prm.cv.lfo2.waveform == 4, { ~apc.turnGridGreen(4, 1, 2) }, { ~apc.turnGridPurple(4, 1, 1) });
	if( ~prm.cv.lfo2.waveform == 5, { ~apc.turnGridGreen(5, 1, 2) }, { ~apc.turnGridPurple(5, 1, 1) });
}, lfo12, page);
6.do({ | i | ~apc.setGridFunc(i, 1, {  ~prm.cv.lfo2.setWaveform(i); }, bank: lfo12, page: page); });

// output:
~apc.setGridMonitorFunc(\lfo2Output, {
	if( ~prm.cv.lfo2.out == ~modOutput[0], { ~apc.turnGridGreen(0, 0, 2) }, { ~apc.turnGridMagenta(0, 0, 1) });
	if( ~prm.cv.lfo2.out == ~modOutput[1], { ~apc.turnGridGreen(1, 0, 2) }, { ~apc.turnGridMagenta(1, 0, 1) });
	if( ~prm.cv.lfo2.out == ~modOutput[2], { ~apc.turnGridGreen(2, 0, 2) }, { ~apc.turnGridMagenta(2, 0, 1) });
	if( ~prm.cv.lfo2.out == ~modOutput[3], { ~apc.turnGridGreen(3, 0, 2) }, { ~apc.turnGridMagenta(3, 0, 1) });
	if( ~prm.cv.lfo2.out == ~modOutput[4], { ~apc.turnGridGreen(4, 0, 2) }, { ~apc.turnGridMagenta(4, 0, 1) });
	if( ~prm.cv.lfo2.out == ~modOutput[5], { ~apc.turnGridGreen(5, 0, 2) }, { ~apc.turnGridMagenta(5, 0, 1) });
	if( ~prm.cv.lfo2.out == ~modOutput[6], { ~apc.turnGridGreen(6, 0, 2) }, { ~apc.turnGridMagenta(6, 0, 1) });
	if( ~prm.cv.lfo2.out == ~modOutput[7], { ~apc.turnGridGreen(7, 0, 2) }, { ~apc.turnGridMagenta(7, 0, 1) });
}, lfo12, page);
8.do({ | i | ~apc.setGridFunc(i, 0, { ~prm.cv.lfo2.setOutBus(~modOutput[i]); }, bank: lfo12, page: page); });
~apc.turnGridRed(7, 1, 1, lfo12, page);
~apc.setGridFunc(7, 1, { ~prm.cv.lfo2.setOutBus(~prm.cv.nilBus); }, bank: lfo12, page: page);

//////// Knobs:

~apc.setMixerEncodersMonitorFunc(\lfo2, {
	var freq, pw, rangeLow, rangeHigh;
	freq = ~prm.cv.lfo2.frequency.explin(0.01, 18, 0, 127);
	pw = ~prm.cv.lfo2.pulseWidth.linlin(-1, 1, 0, 127);
	rangeLow = ~prm.cv.lfo2.rangeLow.linlin(-0.5, 0.5, 0, 127);
	rangeHigh = ~prm.cv.lfo2.rangeHigh.linlin(-0.5, 0.5, 0, 127);
	~apc.setMixerEncoderValue(5, freq);
	~apc.setMixerEncoderValue(6, pw);
	~apc.setMixerEncoderValue(7, rangeLow);
	~apc.setMixerEncoderValue(8, rangeHigh);
}, lfo12, page);
~apc.setMixerEncoderFunc(5, { | val |
	var freq = val.linexp(0, 127, 0.01, 18);
	~prm.cv.lfo2.setFrequency(freq);
}, lfo12, page);
~apc.setMixerEncoderFunc(6, { | val |
	var pw = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo2.setPulseWidth(pw);
}, lfo12, page);
~apc.setMixerEncoderFunc(7, { | val |
	var range = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.lfo2.setRangeLow(range);
}, lfo12, page);
~apc.setMixerEncoderFunc(8, { | val |
	var range = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.lfo2.setRangeHigh(range);
}, lfo12, page);

~apc.setDeviceEncodersMonitorFunc(\lfo2, {
	var freq, waveform, rangeLow, rangeHigh;
	freq = ~prm.cv.lfo2.frequency.explin(0.001, 40, 0, 127);
	waveform = ~prm.cv.lfo2.waveform.linlin(0, 5, 0, 127);
	rangeLow = ~prm.cv.lfo2.rangeLow.linlin(-1, 1, 0, 127);
	rangeHigh = ~prm.cv.lfo2.rangeHigh.linlin(-1, 1, 0, 127);
	~apc.setDeviceEncoderValue(5, freq);
	~apc.setDeviceEncoderValue(6, waveform);
	~apc.setDeviceEncoderValue(7, rangeLow);
	~apc.setDeviceEncoderValue(8, rangeHigh);
}, lfo12, page);
~apc.setDeviceEncoderFunc(5, { | val |
	var freq = val.linexp(0, 127, 0.001, 40);
	~prm.cv.lfo2.setFrequency(freq);
}, lfo12, page);
~apc.setDeviceEncoderFunc(6, { | val |
	var waveform = val.linlin(0, 127, 0, 5);
	~prm.cv.lfo2.setWaveform(waveform);
}, lfo12, page);
~apc.setDeviceEncoderFunc(7, { | val |
	var range = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo2.setRangeLow(range);
}, lfo12, page);
~apc.setDeviceEncoderFunc(8, { | val |
	var range = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo2.setRangeHigh(range);
}, lfo12, page);


/////////////////////
// LFOs 3 + 4: ////
//////////////////

///// LFO1:

// waveform:
~apc.setGridMonitorFunc(\lfo3Waveform, {
	if( ~prm.cv.lfo3.waveform == 0, { ~apc.turnGridGreen(0, 3, 2) }, { ~apc.turnGridMagenta(0, 3, 1) });
	if( ~prm.cv.lfo3.waveform == 1, { ~apc.turnGridGreen(1, 3, 2) }, { ~apc.turnGridMagenta(1, 3, 1) });
	if( ~prm.cv.lfo3.waveform == 2, { ~apc.turnGridGreen(2, 3, 2) }, { ~apc.turnGridMagenta(2, 3, 1) });
	if( ~prm.cv.lfo3.waveform == 3, { ~apc.turnGridGreen(3, 3, 2) }, { ~apc.turnGridMagenta(3, 3, 1) });
	if( ~prm.cv.lfo3.waveform == 4, { ~apc.turnGridGreen(4, 3, 2) }, { ~apc.turnGridMagenta(4, 3, 1) });
	if( ~prm.cv.lfo3.waveform == 5, { ~apc.turnGridGreen(5, 3, 2) }, { ~apc.turnGridMagenta(5, 3, 1) });
}, lfo34, page);
6.do({ | i | ~apc.setGridFunc(i, 3, {  ~prm.cv.lfo3.setWaveform(i); }, bank: lfo34, page: page); });

// output:
~apc.setGridMonitorFunc(\lfo1Output, {
	if( ~prm.cv.lfo3.out == ~modOutput[0], { ~apc.turnGridGreen(0, 2, 2) }, { ~apc.turnGridPink(0, 2, 1) });
	if( ~prm.cv.lfo3.out == ~modOutput[1], { ~apc.turnGridGreen(1, 2, 2) }, { ~apc.turnGridPink(1, 2, 1) });
	if( ~prm.cv.lfo3.out == ~modOutput[2], { ~apc.turnGridGreen(2, 2, 2) }, { ~apc.turnGridPink(2, 2, 1) });
	if( ~prm.cv.lfo3.out == ~modOutput[3], { ~apc.turnGridGreen(3, 2, 2) }, { ~apc.turnGridPink(3, 2, 1) });
	if( ~prm.cv.lfo3.out == ~modOutput[4], { ~apc.turnGridGreen(4, 2, 2) }, { ~apc.turnGridPink(4, 2, 1) });
	if( ~prm.cv.lfo3.out == ~modOutput[5], { ~apc.turnGridGreen(5, 2, 2) }, { ~apc.turnGridPink(5, 2, 1) });
	if( ~prm.cv.lfo3.out == ~modOutput[6], { ~apc.turnGridGreen(6, 2, 2) }, { ~apc.turnGridPink(6, 2, 1) });
	if( ~prm.cv.lfo3.out == ~modOutput[7], { ~apc.turnGridGreen(7, 2, 2) }, { ~apc.turnGridPink(7, 2, 1) });
}, lfo34, page);
8.do({ | i | ~apc.setGridFunc(i, 2, { ~prm.cv.lfo3.setOutBus(~modOutput[i]); }, bank: lfo34, page: page); });
~apc.turnGridRed(7, 3, 1, lfo34, page);
~apc.setGridFunc(7, 3, { ~prm.cv.lfo3.setOutBus(~prm.cv.nilBus); }, bank: lfo34, page: page);

//////// Knobs:

~apc.setMixerEncodersMonitorFunc(\lfo3, {
	var freq, pw, rangeLow, rangeHigh;
	freq = ~prm.cv.lfo3.frequency.explin(0.01, 18, 0, 127);
	pw = ~prm.cv.lfo3.pulseWidth.linlin(-1, 1, 0, 127);
	rangeLow = ~prm.cv.lfo3.rangeLow.linlin(-0.5, 0.5, 0, 127);
	rangeHigh = ~prm.cv.lfo3.rangeHigh.linlin(-0.5, 0.5, 0, 127);
	~apc.setMixerEncoderValue(1, freq);
	~apc.setMixerEncoderValue(2, pw);
	~apc.setMixerEncoderValue(3, rangeLow);
	~apc.setMixerEncoderValue(4, rangeHigh);
}, lfo34, page);
~apc.setMixerEncoderFunc(1, { | val |
	var freq = val.linexp(0, 127, 0.01, 18);
	~prm.cv.lfo3.setFrequency(freq);
}, lfo34, page);
~apc.setMixerEncoderFunc(2, { | val |
	var pw = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo3.setPulseWidth(pw);
}, lfo34, page);
~apc.setMixerEncoderFunc(3, { | val |
	var range = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.lfo3.setRangeLow(range);
}, lfo34, page);
~apc.setMixerEncoderFunc(4, { | val |
	var range = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.lfo3.setRangeHigh(range);
}, lfo34, page);

~apc.setDeviceEncodersMonitorFunc(\lfo3, {
	var freq, waveform, rangeLow, rangeHigh;
	freq = ~prm.cv.lfo3.frequency.explin(0.001, 40, 0, 127);
	waveform = ~prm.cv.lfo3.waveform.linlin(0, 5, 0, 127);
	rangeLow = ~prm.cv.lfo3.rangeLow.linlin(-1, 1, 0, 127);
	rangeHigh = ~prm.cv.lfo3.rangeHigh.linlin(-1, 1, 0, 127);
	~apc.setDeviceEncoderValue(1, freq);
	~apc.setDeviceEncoderValue(2, waveform);
	~apc.setDeviceEncoderValue(3, rangeLow);
	~apc.setDeviceEncoderValue(4, rangeHigh);
}, lfo34, page);
~apc.setDeviceEncoderFunc(1, { | val |
	var freq = val.linexp(0, 127, 0.001, 40);
	~prm.cv.lfo3.setFrequency(freq);
}, lfo34, page);
~apc.setDeviceEncoderFunc(2, { | val |
	var waveform = val.linlin(0, 127, 0, 5);
	~prm.cv.lfo3.setWaveform(waveform);
}, lfo34, page);
~apc.setDeviceEncoderFunc(3, { | val |
	var range = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo3.setRangeLow(range);
}, lfo34, page);
~apc.setDeviceEncoderFunc(4, { | val |
	var range = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo3.setRangeHigh(range);
}, lfo34, page);

///// LFO2:

// waveform:
~apc.setGridMonitorFunc(\lfo4Waveform, {
	if( ~prm.cv.lfo4.waveform == 0, { ~apc.turnGridGreen(0, 1, 2) }, { ~apc.turnGridYellow(0, 1, 1) });
	if( ~prm.cv.lfo4.waveform == 1, { ~apc.turnGridGreen(1, 1, 2) }, { ~apc.turnGridYellow(1, 1, 1) });
	if( ~prm.cv.lfo4.waveform == 2, { ~apc.turnGridGreen(2, 1, 2) }, { ~apc.turnGridYellow(2, 1, 1) });
	if( ~prm.cv.lfo4.waveform == 3, { ~apc.turnGridGreen(3, 1, 2) }, { ~apc.turnGridYellow(3, 1, 1) });
	if( ~prm.cv.lfo4.waveform == 4, { ~apc.turnGridGreen(4, 1, 2) }, { ~apc.turnGridYellow(4, 1, 1) });
	if( ~prm.cv.lfo4.waveform == 5, { ~apc.turnGridGreen(5, 1, 2) }, { ~apc.turnGridYellow(5, 1, 1) });
}, lfo34, page);
6.do({ | i | ~apc.setGridFunc(i, 1, {  ~prm.cv.lfo4.setWaveform(i); }, bank: lfo34, page: page); });

// output:
~apc.setGridMonitorFunc(\lfo4Output, {
	if( ~prm.cv.lfo4.out == ~modOutput[0], { ~apc.turnGridGreen(0, 0, 2) }, { ~apc.turnGridYellowGreen(0, 0, 1) });
	if( ~prm.cv.lfo4.out == ~modOutput[1], { ~apc.turnGridGreen(1, 0, 2) }, { ~apc.turnGridYellowGreen(1, 0, 1) });
	if( ~prm.cv.lfo4.out == ~modOutput[2], { ~apc.turnGridGreen(2, 0, 2) }, { ~apc.turnGridYellowGreen(2, 0, 1) });
	if( ~prm.cv.lfo4.out == ~modOutput[3], { ~apc.turnGridGreen(3, 0, 2) }, { ~apc.turnGridYellowGreen(3, 0, 1) });
	if( ~prm.cv.lfo4.out == ~modOutput[4], { ~apc.turnGridGreen(4, 0, 2) }, { ~apc.turnGridYellowGreen(4, 0, 1) });
	if( ~prm.cv.lfo4.out == ~modOutput[5], { ~apc.turnGridGreen(5, 0, 2) }, { ~apc.turnGridYellowGreen(5, 0, 1) });
	if( ~prm.cv.lfo4.out == ~modOutput[6], { ~apc.turnGridGreen(6, 0, 2) }, { ~apc.turnGridYellowGreen(6, 0, 1) });
	if( ~prm.cv.lfo4.out == ~modOutput[7], { ~apc.turnGridGreen(7, 0, 2) }, { ~apc.turnGridYellowGreen(7, 0, 1) });
}, lfo34, page);
8.do({ | i | ~apc.setGridFunc(i, 0, { ~prm.cv.lfo4.setOutBus(~modOutput[i]); }, bank: lfo34, page: page); });
~apc.turnGridRed(7, 1, 1, lfo34, page);
~apc.setGridFunc(7, 1, { ~prm.cv.lfo4.setOutBus(~prm.cv.nilBus); }, bank: lfo34, page: page);

//////// Knobs:

~apc.setMixerEncodersMonitorFunc(\lfo4, {
	var freq, pw, rangeLow, rangeHigh;
	freq = ~prm.cv.lfo4.frequency.explin(0.01, 18, 0, 127);
	pw = ~prm.cv.lfo4.pulseWidth.linlin(-1, 1, 0, 127);
	rangeLow = ~prm.cv.lfo4.rangeLow.linlin(-0.5, 0.5, 0, 127);
	rangeHigh = ~prm.cv.lfo4.rangeHigh.linlin(-0.5, 0.5, 0, 127);
	~apc.setMixerEncoderValue(5, freq);
	~apc.setMixerEncoderValue(6, pw);
	~apc.setMixerEncoderValue(7, rangeLow);
	~apc.setMixerEncoderValue(8, rangeHigh);
}, lfo34, page);
~apc.setMixerEncoderFunc(5, { | val |
	var freq = val.linexp(0, 127, 0.01, 18);
	~prm.cv.lfo4.setFrequency(freq);
}, lfo34, page);
~apc.setMixerEncoderFunc(6, { | val |
	var pw = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo4.setPulseWidth(pw);
}, lfo34, page);
~apc.setMixerEncoderFunc(7, { | val |
	var range = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.lfo4.setRangeLow(range);
}, lfo34, page);
~apc.setMixerEncoderFunc(8, { | val |
	var range = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.lfo4.setRangeHigh(range);
}, lfo34, page);

~apc.setDeviceEncodersMonitorFunc(\lfo4, {
	var freq, waveform, rangeLow, rangeHigh;
	freq = ~prm.cv.lfo4.frequency.explin(0.001, 40, 0, 127);
	waveform = ~prm.cv.lfo4.waveform.linlin(0, 5, 0, 127);
	rangeLow = ~prm.cv.lfo4.rangeLow.linlin(-1, 1, 0, 127);
	rangeHigh = ~prm.cv.lfo4.rangeHigh.linlin(-1, 1, 0, 127);
	~apc.setDeviceEncoderValue(5, freq);
	~apc.setDeviceEncoderValue(6, waveform);
	~apc.setDeviceEncoderValue(7, rangeLow);
	~apc.setDeviceEncoderValue(8, rangeHigh);
}, lfo34, page);
~apc.setDeviceEncoderFunc(5, { | val |
	var freq = val.linexp(0, 127, 0.001, 40);
	~prm.cv.lfo4.setFrequency(freq);
}, lfo34, page);
~apc.setDeviceEncoderFunc(6, { | val |
	var waveform = val.linlin(0, 127, 0, 5);
	~prm.cv.lfo4.setWaveform(waveform);
}, lfo34, page);
~apc.setDeviceEncoderFunc(7, { | val |
	var range = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo4.setRangeLow(range);
}, lfo34, page);
~apc.setDeviceEncoderFunc(8, { | val |
	var range = val.linlin(0, 127, -1, 1);
	~prm.cv.lfo4.setRangeHigh(range);
}, lfo34, page);