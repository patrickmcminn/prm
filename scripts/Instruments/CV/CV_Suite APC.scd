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

var freq1Chromatic = 9;
var freq1Diatonic = 10;
var freq2Chromatic = 11;
var freq2Diatonic = 12;

var freq1RootSel = 13;
var freq2RootSel = 14;

var pitchGate1Chromatic = 15;
var pitchGate1Diatonic = 16;
var pitchGate2Chromatic = 17;
var pitchGate2Diatonic = 18;

var gate1Val = 0.5;
var gate2Val = 0.5;

var scales;

var freq1ActiveScale = 0;
var freq2ActiveScale = 5;
var pitchGate1ActiveScale = 0;
var pitchGate2ActiveScale = 5;

var freq1OctScale = 0;
var freq1Root = 0;
var freq2OctScale = 0;
var freq2Root = 0;

var pitchGate1Reset, pitchGate2Reset;

var blackPos = [1, 2, 4, 5, 6];

scales = [Scale.ionian, Scale.dorian, Scale.phrygian, Scale.lydian, Scale.mixolydian, Scale.aeolian, Scale.locrian];

~apc.makePage(page);
~apc.addGridBanks(20, page);
~apc.addDeviceEncodersBanks(8, page);
~apc.addMixerEncodersBanks(8, page);
~apc.addClipStopBanks(8, page);
~apc.addDeviceButtonsBanks(8, page);

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
	~apc.setActiveClipStopBank(activeBank);
	~apc.setActiveDeviceButtonsBank(activeBank);
}, page: page);
9.do({ | bank |
	8.do({ | slot |
		~apc.setGridFunc(slot, 4, {
			activeBank = slot+1;
			~apc.setActiveGridBank(activeBank);
			~apc.setActiveDeviceEncodersBank(activeBank);
			~apc.setActiveMixerEncodersBank(activeBank);
			~apc.setActiveClipStopBank(activeBank);
			~apc.setActiveDeviceButtonsBank(activeBank);
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


////////////////////////////////
//////// Trig Env: ////////////
//////////////////////////////

////// Grid:

//// Trig Env 1:

// perc env trig 1:
~apc.turnGridMagenta(0, 3, 2, trigEnv, page);
~apc.setGridFunc(0, 3, {
	~apc.turnGridGreen(0, 3);
	~prm.cv.trigEnv1.trigger;
}, 'noteOn', trigEnv, page);
~apc.setGridFunc(0, 3, {
	~apc.turnGridMagenta(0, 3, 2);
}, 'noteOff', trigEnv, page);

// trig env output 1:
~apc.setGridMonitorFunc(\trigEnv1Output, {
	if( ~prm.cv.trigEnv1.out == ~modOutput[0], { ~apc.turnGridGreen(0, 2, 2) }, { ~apc.turnGridPurple(0, 2, 1) });
	if( ~prm.cv.trigEnv1.out == ~modOutput[1], { ~apc.turnGridGreen(1, 2, 2) }, { ~apc.turnGridPurple(1, 2, 1) });
	if( ~prm.cv.trigEnv1.out == ~modOutput[2], { ~apc.turnGridGreen(2, 2, 2) }, { ~apc.turnGridPurple(2, 2, 1) });
	if( ~prm.cv.trigEnv1.out == ~modOutput[3], { ~apc.turnGridGreen(3, 2, 2) }, { ~apc.turnGridPurple(3, 2, 1) });
	if( ~prm.cv.trigEnv1.out == ~modOutput[4], { ~apc.turnGridGreen(4, 2, 2) }, { ~apc.turnGridPurple(4, 2, 1) });
	if( ~prm.cv.trigEnv1.out == ~modOutput[5], { ~apc.turnGridGreen(5, 2, 2) }, { ~apc.turnGridPurple(5, 2, 1) });
	if( ~prm.cv.trigEnv1.out == ~modOutput[6], { ~apc.turnGridGreen(6, 2, 2) }, { ~apc.turnGridPurple(6, 2, 1) });
	if( ~prm.cv.trigEnv1.out == ~modOutput[7], { ~apc.turnGridGreen(7, 2, 2) }, { ~apc.turnGridPurple(7, 2, 1) });
}, trigEnv, page);
8.do({ | i | ~apc.setGridFunc(i, 2, { ~prm.cv.trigEnv1.setOutBus(~modOutput[i]); }, bank: trigEnv, page: page); });
~apc.turnGridRed(7, 3, 1, trigEnv, page);
~apc.setGridFunc(7, 3, { ~prm.cv.trigEnv1.setOutBus(~prm.cv.nilBus); }, bank: trigEnv, page: page);

//// Trig Env 2:

// perc env trig 2:
~apc.turnGridMagenta(0, 1, 2, trigEnv, page);
~apc.setGridFunc(0, 1, {
	~apc.turnGridGreen(0, 1);
	~prm.cv.trigEnv2.trigger;
}, 'noteOn', trigEnv, page);
~apc.setGridFunc(0, 1, {
	~apc.turnGridMagenta(0, 1, 2);
}, 'noteOff', trigEnv, page);

// trig env output 2:
~apc.setGridMonitorFunc(\trigEnv2Output, {
	if( ~prm.cv.trigEnv2.out == ~modOutput[0], { ~apc.turnGridGreen(0, 0, 2) }, { ~apc.turnGridMagenta(0, 0, 1) });
	if( ~prm.cv.trigEnv2.out == ~modOutput[1], { ~apc.turnGridGreen(1, 0, 2) }, { ~apc.turnGridMagenta(1, 0, 1) });
	if( ~prm.cv.trigEnv2.out == ~modOutput[2], { ~apc.turnGridGreen(2, 0, 2) }, { ~apc.turnGridMagenta(2, 0, 1) });
	if( ~prm.cv.trigEnv2.out == ~modOutput[3], { ~apc.turnGridGreen(3, 0, 2) }, { ~apc.turnGridMagenta(3, 0, 1) });
	if( ~prm.cv.trigEnv2.out == ~modOutput[4], { ~apc.turnGridGreen(4, 0, 2) }, { ~apc.turnGridMagenta(4, 0, 1) });
	if( ~prm.cv.trigEnv2.out == ~modOutput[5], { ~apc.turnGridGreen(5, 0, 2) }, { ~apc.turnGridMagenta(5, 0, 1) });
	if( ~prm.cv.trigEnv2.out == ~modOutput[6], { ~apc.turnGridGreen(6, 0, 2) }, { ~apc.turnGridMagenta(6, 0, 1) });
	if( ~prm.cv.trigEnv2.out == ~modOutput[7], { ~apc.turnGridGreen(7, 0, 2) }, { ~apc.turnGridMagenta(7, 0, 1) });
}, trigEnv, page);
8.do({ | i | ~apc.setGridFunc(i, 0, { ~prm.cv.trigEnv2.setOutBus(~modOutput[i]); }, bank: trigEnv, page: page); });
~apc.turnGridRed(7, 1, 1, trigEnv, page);
~apc.setGridFunc(7, 1, { ~prm.cv.trigEnv2.setOutBus(~prm.cv.nilBus); }, bank: trigEnv, page: page);

//////////// mixer encoders:

~apc.setMixerEncodersMonitorFunc(\trig, {
	var attack1, release1, level1, curve1;
	var attack2, release2, level2, curve2;
	attack1 = ~prm.cv.trigEnv1.attackTime.explin(0.01, 5, 0, 127);
	release1 = ~prm.cv.trigEnv1.releaseTime.explin(0.01, 7, 0, 127);
	level1 = ~prm.cv.trigEnv1.level.linlin(-0.5, 0.5, 0, 127);
	curve1 = ~prm.cv.trigEnv1.curve.linlin(-5, 5, 0, 127);
	attack2 = ~prm.cv.trigEnv2.attackTime.explin(0.01, 5, 0, 127);
	release2 = ~prm.cv.trigEnv2.releaseTime.explin(0.01, 7, 0, 127);
	level2 = ~prm.cv.trigEnv2.level.linlin(-0.5, 0.5, 0, 127);
	curve2 = ~prm.cv.trigEnv2.curve.linlin(-5, 5, 0, 127);
	~apc.setMixerEncoderValue(1, attack1);
	~apc.setMixerEncoderValue(2, release1);
	~apc.setMixerEncoderValue(3, level1);
	~apc.setMixerEncoderValue(4, curve1);
	~apc.setMixerEncoderValue(5, attack2);
	~apc.setMixerEncoderValue(6, release2);
	~apc.setMixerEncoderValue(7, level2);
	~apc.setMixerEncoderValue(8, curve2);
}, trigEnv, page);

~apc.setMixerEncoderFunc(1, { | val |
	var attack = val.linexp(0, 127, 0.01, 5);
	~prm.cv.trigEnv1.setAttackTime(attack);
}, trigEnv, page);
~apc.setMixerEncoderFunc(2, { | val |
	var release = val.linexp(0, 127, 0.01, 7);
	~prm.cv.trigEnv1.setReleaseTime(release);
}, trigEnv, page);
~apc.setMixerEncoderFunc(3, { | val |
	var level = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.trigEnv1.setLevel(level);
}, trigEnv, page);
~apc.setMixerEncoderFunc(4, { | val |
	var curve = val.linlin(0, 127, -5, 5);
	~prm.cv.trigEnv1.setCurve(curve);
}, trigEnv, page);
~apc.setMixerEncoderFunc(5, { | val |
	var attack = val.linexp(0, 127, 0.01, 5);
	~prm.cv.trigEnv2.setAttackTime(attack);
}, trigEnv, page);
~apc.setMixerEncoderFunc(6, { | val |
	var release = val.linexp(0, 127, 0.01, 7);
	~prm.cv.trigEnv2.setReleaseTime(release);
}, trigEnv, page);
~apc.setMixerEncoderFunc(7, { | val |
	var level = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.trigEnv2.setLevel(level);
}, trigEnv, page);
~apc.setMixerEncoderFunc(8, { | val |
	var curve = val.linlin(0, 127, -5, 5);
	~prm.cv.trigEnv2.setCurve(curve);
}, trigEnv, page);

//////////// device encoders:

~apc.setDeviceEncodersMonitorFunc(\trig, {
	var attack1, release1, level1, curve1;
	var attack2, release2, level2, curve2;
	attack1 = ~prm.cv.trigEnv1.attackTime.explin(0.001, 10, 0, 127);
	release1 = ~prm.cv.trigEnv1.releaseTime.explin(0.001, 15, 0, 127);
	level1 = ~prm.cv.trigEnv1.level.linlin(-1, 1, 0, 127);
	curve1 = ~prm.cv.trigEnv1.curve.linlin(-8, 8, 0, 127);
	attack2 = ~prm.cv.trigEnv2.attackTime.explin(0.001, 10, 0, 127);
	release2 = ~prm.cv.trigEnv2.releaseTime.explin(0.001, 15, 0, 127);
	level2 = ~prm.cv.trigEnv2.level.linlin(-1, 1, 0, 127);
	curve2 = ~prm.cv.trigEnv2.curve.linlin(-8, 8, 0, 127);
	~apc.setDeviceEncoderValue(1, attack1);
	~apc.setDeviceEncoderValue(2, release1);
	~apc.setDeviceEncoderValue(3, level1);
	~apc.setDeviceEncoderValue(4, curve1);
	~apc.setDeviceEncoderValue(5, attack2);
	~apc.setDeviceEncoderValue(6, release2);
	~apc.setDeviceEncoderValue(7, level2);
	~apc.setDeviceEncoderValue(8, curve2);
}, trigEnv, page);

~apc.setDeviceEncoderFunc(1, { | val |
	var attack = val.linexp(0, 127, 0.001, 10);
	~prm.cv.trigEnv1.setAttackTime(attack);
}, trigEnv, page);
~apc.setDeviceEncoderFunc(2, { | val |
	var release = val.linexp(0, 127, 0.001, 15);
	~prm.cv.trigEnv1.setReleaseTime(release);
}, trigEnv, page);
~apc.setDeviceEncoderFunc(3, { | val |
	var level = val.linlin(0, 127, -1, 1);
	~prm.cv.trigEnv1.setLevel(level);
}, trigEnv, page);
~apc.setDeviceEncoderFunc(4, { | val |
	var curve = val.linlin(0, 127, -8, 8);
	~prm.cv.trigEnv1.setCurve(curve);
}, trigEnv, page);
~apc.setDeviceEncoderFunc(5, { | val |
	var attack = val.linexp(0, 127, 0.001, 10);
	~prm.cv.trigEnv2.setAttackTime(attack);
}, trigEnv, page);
~apc.setDeviceEncoderFunc(6, { | val |
	var release = val.linexp(0, 127, 0.001, 15);
	~prm.cv.trigEnv2.setReleaseTime(release);
}, trigEnv, page);
~apc.setDeviceEncoderFunc(7, { | val |
	var level = val.linlin(0, 127, -1, 1);
	~prm.cv.trigEnv2.setLevel(level);
}, trigEnv, page);
~apc.setDeviceEncoderFunc(8, { | val |
	var curve = val.linlin(0, 127, -8, 8);
	~prm.cv.trigEnv2.setCurve(curve);
}, trigEnv, page);

