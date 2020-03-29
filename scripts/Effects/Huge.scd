/*
Wednesday, March 18th 2020
Huge Page.scd
prm

during 2020 COVID-19
*/

var page = 'huge';

var mainGridBank = 0;
var rateGridBank = 1;

var sendsBank = 0;

var mainEncodersBank = 0;
var encodersBank2 = 1;

var preset0 = 0;

var rateResetFunc, envReset;

var shiftArray = [7, 12, -5, 7];

var shiftResetFunc, presetResetFunc;

var makeMIDIFuncs, freeMIDIFuncs, mTrigRate, mCutoff, mShiftArray, mShift;
var midiFuncsIsLoaded = false;

makeMIDIFuncs = { | device = nil, chan = 14 |
	//// cc 20 controls the LFO on the grain rate:
	mTrigRate = MIDIFunc.cc({ | i |
		var rate = i.linlin(0, 127, 4, 16);
		~huge.granulator.setTrigRate(rate) }, 20, chan, device.uid);

	// noteOn 0 triggers a random change in the filter cutoff after the distortion channel:
	mCutoff = MIDIFunc.noteOn({ ~huge.lowPass.setCutoff(exprand(200, 5000)) }, 0, chan, device.uid);

	// noteOns 12-15 triggers a redistribution of the current randomized shift array:
	mShiftArray = Array.fill(4, { | i |
		MIDIFunc.noteOn({ ~huge.multiShift.setShift(i, shiftArray.choose) }, i+12, chan, device.uid) });

	midiFuncsIsLoaded = true;
};

freeMIDIFuncs = {
	mTrigRate.free; mCutoff.free;
	mShift.do({ | i | i.free; });
	midiFuncsIsLoaded = false;
};

~apc.makePage(page);
~apc.addGridBanks(1, page);
~apc.addDeviceEncodersBanks(1, page);
~apc.addSceneLaunchBanks(1, page);

//// master funcs:
~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.storageDict[\loadMasterFunc].value;


///////////////////////////////
//// grid bank select: ///////
/////////////////////////////

~apc.setBankSelectFunc('down', { try { ~apc.setActiveGridBank(~apc.activeGridBank + 1) } }, page: page);
~apc.setBankSelectFunc('up', { try { ~apc.setActiveGridBank(~apc.activeGridBank -1) } }, page: page);



//////////////////
//// Mixers: ////
////////////////

//////////// final mixer:

~apc.setFaderFunc(1, { | val |
	~huge.mixer.setVol(val.ccdbfs);
}, page: page);
// mute:
~apc.setTrackSelectFunc(1, { ~huge.mixer.tglMute }, page: page);
// input:
~apc.setRecordEnableButtonFunc(1, { ~huge.input.tglMute }, page: page);
~apc.setMixerMonitorFunc('main', {
	if( ~huge.mixer.isMuted,
		{ ~apc.turnTrackSelectButtonOff(1) }, { ~apc.turnTrackSelectButtonOn(1) });
	if( ~huge.input.isMuted,
		{ ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
	if( ~huge.mixer.preOrPost == 'post',
		{ ~apc.turnCrossfaderSelectButtonOrange(1) }, { ~apc.turnCrossfaderSelectButtonYellow(1) });
}, page: page);
~apc.setCrossfaderSelectFunc(1, {
	if( ~huge.mixer.preOrPost(0) == 'post',
		{ ~huge.mixer.setSendPre(0); }, { ~huge.mixer.setSendPost(0); });
}, page: page);

// sends:
~apc.setMixerEncodersMonitorFunc(\sends, {
	var reverb, granulator, modular, delay;
	reverb = ~huge.mixer.sendVol(0);
	granulator = ~huge.mixer.sendVol(1);
	modular = ~huge.mixer.sendVol(2);
	delay = ~huge.mixer.sendVol(3);
	~apc.setMixerEncoderValue(1, reverb.dbfsCC, page: page);
	~apc.setMixerEncoderValue(2, granulator.dbfsCC, page: page);
	~apc.setMixerEncoderValue(3, modular.dbfsCC, page: page);
	~apc.setMixerEncoderValue(4, delay.dbfsCC, page: page);
}, sendsBank, page);
~apc.setMixerEncoderFunc(1, { | val | ~huge.mixer.setSendVol(0, val.ccdbfs) }, sendsBank, page);
~apc.setMixerEncoderFunc(2, { | val | ~huge.mixer.setSendVol(1, val.ccdbfs) }, sendsBank, page);
~apc.setMixerEncoderFunc(3, { | val | ~huge.mixer.setSendVol(2, val.ccdbfs) }, sendsBank, page);
~apc.setMixerEncoderFunc(4, { | val | ~huge.mixer.setSendVol(3, val.ccdbfs) }, sendsBank, page);

//////////// SubMixer:

4.do({ | i |
	// vol:
	~apc.setFaderFunc(i+2, { | val | ~huge.subMixer.setVol(i, val.ccdbfs); }, page: page);
	// toggle mute:
	~apc.setTrackSelectFunc(i+2, { ~huge.subMixer.tglMute(i); }, page: page);
	~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
		if( ~huge.subMixer.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+2) }, { ~apc.turnTrackSelectButtonOn(i+2); });
	}, page: page);
});


//////////// Pitch Shifter:
~apc.setFaderFunc(6, { | val | ~huge.initShift.mixer.setVol(val.ccdbfs); },
	page: page);
// toggle mute:
~apc.setTrackSelectFunc(6, { ~huge.initShift.mixer.tglMute }, page: page);

~apc.setMixerMonitorFunc(\muteInitShift, {
	if(~huge.initShift.mixer.isMuted,
		{ ~apc.turnTrackSelectButtonOff(6) }, { ~apc.turnTrackSelectButtonOn(6) });
}, page: page);



///////// Input:

~apc.setFaderFunc(8, { | val | ~huge.input.setVol(val.ccdbfs) }, page: page);
~apc.turnTrackSelectButtonOn(8, page: page);

////////////////////
//// Knobs: ///////
//////////////////

/*
what are you looking to do?

-- 1st 4 knobs are regulard sends to the system
-- that means, without bank changes, you have 12 knobs to work with
-- what do you absolutely need?

1 - master low pass cutoff
2 - delay mix
3 - delay time
4 - delay feedback

5 - trig rate
6 - grain dur
7 - distortion filter
8 - distortion resonance


-- high pass filter
-- subOsc cutoff
-- reverb mix
-- distortion mix

-- granulator delay mix
-- granulator delay time
-- granulator delay feedback
-- granulator pan



---- master grain dur
---- grain rate unsequenced


master high pass cutoff??? not as important

*/

////////// Active Device Encoder:

~apc.setDeviceButtonsMonitorFunc(\deviceEncodersBank, {
	if( ~apc.activeDeviceEncodersBank == mainEncodersBank,
		{ ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1) });
	if( ~apc.activeDeviceEncodersBank == encodersBank2,
		{ ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2) });
}, page: page);
~apc.setDeviceButtonFunc(1, { ~apc.setActiveDeviceEncodersBank(mainEncodersBank) }, page: page);
~apc.setDeviceButtonFunc(2, { ~apc.setActiveDeviceEncodersBank(encodersBank2) }, page: page);

~apc.setDeviceEncodersMonitorFunc(\mainBank, {
	var cutoff = ~huge.eq.lowPassCutoff.explin(150, 20000, 0, 127);
	var delayMix = ~huge.endDelay.mix.linlin(0, 1, 0, 127);
	var delayTime = ~huge.endDelay.delayTime.explin(0.05, 5, 0, 127);
	var delayFeedback = ~huge.endDelay.feedback.linlin(0, 1, 0, 127);

	var trigRate = ~huge.granulator.granulator.trigRate.explin(1, 48, 0, 127);
	var grainDur = ~huge.granulator.granulator.grainDurLow.explin(0.01, 1, 0, 127);
	var lowPass = ~huge.lowPass.filterCutoff.explin(500, 20000, 0, 127);
	//var subOscCutoff = ~huge.subOscCutoff.explin(100, 18000, 0, 127);
	var lowPassRes = ~huge.lowPass.filterRQ.linlin(0.01, 1.0, 127, 0);

	~apc.setDeviceEncoderValue(1, cutoff);
	~apc.setDeviceEncoderValue(2, delayMix);
	~apc.setDeviceEncoderValue(3, delayTime);
	~apc.setDeviceEncoderValue(4, delayFeedback);
	~apc.setDeviceEncoderValue(5, trigRate);
	~apc.setDeviceEncoderValue(6, grainDur);
	~apc.setDeviceEncoderValue(7, lowPass);
	~apc.setDeviceEncoderValue(8, lowPassRes);

}, mainEncodersBank, page);


// big cutoff knob:
~apc.setDeviceEncoderFunc(1, { | val |
	var cutoff = val.linexp(0, 127, 150, 20000);
	~huge.eq.setLowPassCutoff(cutoff);
}, mainEncodersBank, page);
// delay mix:
~apc.setDeviceEncoderFunc(2, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~huge.endDelay.setMix(mix);
}, mainEncodersBank, page);
// delay time:
~apc.setDeviceEncoderFunc(3, {  | val |
	var time = val.linexp(0, 127, 0.05, 5);
	~huge.endDelay.setDelayTime(time);
}, mainEncodersBank, page);
// delay feedback:
~apc.setDeviceEncoderFunc(4, { | val |
	var fb = val.linlin(0, 127, 0, 1);
	~huge.endDelay.setFeedback(fb);
}, mainEncodersBank, page);
// granulator trigger rate:
~apc.setDeviceEncoderFunc(5, { | val |
	var rate = val.linexp(0, 127, 1, 48);
	~huge.granulator.setTrigRate(rate);
}, mainEncodersBank, page);
// granulator grain dur:
~apc.setDeviceEncoderFunc(6, { | val |
	var dur = val.linexp(0, 127, 0.01, 1);
	~huge.granulator.setGrainDur(dur, dur);
}, mainEncodersBank, page);
// distortion low pass:
~apc.setDeviceEncoderFunc(7, { | val |
	var cutoff = val.linexp(0, 127, 500, 20000);
	~huge.lowPass.setCutoff(cutoff);
}, mainEncodersBank, page);
/*
// subOscCutoff:
~apc.setDeviceEncoderFunc(8, { | val |
var cutoff = val.linexp(0, 127, 100, 18000);
~huge.setSubOscCutoff(cutoff);
}, mainEncodersBank, page);
*/
// distortion filter res:
~apc.setDeviceEncoderFunc(8, { | val |
	var res = val.linlin(0, 127, 1, 0.01);
	~huge.lowPass.setRQ(res);
}, mainEncodersBank, page);

///////// encoders bank 2:

~apc.setDeviceEncodersMonitorFunc(\encodersBank2, {
	var highPass = ~huge.eq.highPassCutoff.explin(1, 10000, 0, 127);
	var subOscCutoff = ~huge.subOscCutoff.explin(80, 20000, 0, 127);
	var reverbMix = ~huge.reverb.mix.linlin(0, 1, 0, 127);
	var distortionMix = ~huge.distortion.mix.linlin(0, 1, 0, 127);

	var gDelayMix = ~huge.granulator.delay.mix.linlin(0, 1, 0, 127);
	var gDelayTime = ~huge.granulator.delay.delayTime.explin(0.001, 5, 0, 127);
	var gDelayFeedback = ~huge.granulator.delay.feedback.linlin(0, 1, 0, 127);
	var gDelayPan = ~huge.granulator.granulator.panHigh.linlin(0, 1, 0, 127);

	~apc.setDeviceEncoderValue(1, highPass);
	~apc.setDeviceEncoderValue(2, subOscCutoff);
	~apc.setDeviceEncoderValue(3, reverbMix);
	~apc.setDeviceEncoderValue(4, distortionMix);
	~apc.setDeviceEncoderValue(5, gDelayMix);
	~apc.setDeviceEncoderValue(6, gDelayTime);
	~apc.setDeviceEncoderValue(7, gDelayFeedback);
	~apc.setDeviceEncoderValue(8, gDelayPan);

}, encodersBank2, page);

// high pass cutoff:
~apc.setDeviceEncoderFunc(1, { | val |
	var cutoff = val.linexp(0, 127, 1, 10000);
	~huge.eq.setHighPassCutoff(cutoff);
}, encodersBank2, page);

// sub osc cutoff:
~apc.setDeviceEncoderFunc(2, { | val |
	var cutoff = val.linexp(0, 127, 80, 20000);
	~huge.setSubOscCutoff(cutoff);
}, encodersBank2, page);

// reverb mix:
~apc.setDeviceEncoderFunc(3, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~huge.reverb.setMix(mix);
}, encodersBank2, page);

// distortion mix:
~apc.setDeviceEncoderFunc(4, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~huge.distortion.setMix(mix);
}, encodersBank2, page);

// Granular Delay Mix:
~apc.setDeviceEncoderFunc(5, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~huge.granulator.setDelayMix(mix);
}, encodersBank2, page);

// Granular Delay Time:
~apc.setDeviceEncoderFunc(6, { | val |
	var time = val.linexp(0, 127, 0.001, 5);
	~huge.granulator.setDelayTime(time);
}, encodersBank2, page);

// Granular Delay Feedback:
~apc.setDeviceEncoderFunc(7, { | val |
	var feedback = val.linlin(0, 127, 0, 1);
	~huge.granulator.setFeedback(feedback);
}, encodersBank2, page);

// Granular Delay Pan Distribution:
~apc.setDeviceEncoderFunc(8, { | val |
	var panLow = val.linlin(0, 127, 0, -1);
	var panHigh = val.linlin(0, 127, 0, 1);
	~huge.granulator.setPan(panLow, panHigh);
}, encodersBank2, page);

///////////////////////////////////////
////////// SceneLaunch Presets: //////
/////////////////////////////////////

presetResetFunc = { 5.do({ | i | ~apc.turnSceneLaunchButtonCyan(i, 1, bank: preset0, page: page); }); };

presetResetFunc.value;

~apc.setSceneLaunchFunc(0, {
	presetResetFunc.value;
	~apc.turnSceneLaunchButtonGreen(0, 2);
	4.do({ | i | ~huge.subMixer.unMute(i); });
	~huge.subMixer.setVol(0, -3);
	~huge.subMixer.setVol(1, -3);
	~huge.subMixer.setVol(2, -9);
	~huge.subMixer.setVol(3, -15);

	~huge.lowPass.setCutoff(2300);

	~huge.eq.setPeak1Freq(300);
	~huge.eq.setPeak1Gain(-3);
	~huge.eq.setLowFreq(110);
	~huge.eq.setLowGain(3);
	~huge.eq.setLowPassCutoff(20000);

	~huge.granulator.granulator.setCrossfade(1);
	~huge.granulator.delay.setMix(0);
	~huge.granulator.setRate(2, 2);
	~huge.granulator.setSync(1);
	~huge.granulator.setGrainDur(0.05, 0.05);
	~huge.granulator.setTrigRate(8);
	~huge.granulator.setGrainEnvelope(\perc);

	~huge.endDelay.setMix(0.25);
	~huge.endDelay.setDelayTime(0.25);

	~huge.initShift.mixer.unMute;

	~huge.setSubOscCutoff(400);

	shiftArray = [7, 12, -5, 7];
	4.do({ | i |  ~huge.multiShift.setShift(i, shiftArray.choose) });
}, bank: preset0, page: page);

~apc.setSceneLaunchFunc(1, {
	/// dry-ish trumpet
	// delay unaffected

	presetResetFunc.value;
	~apc.turnSceneLaunchButtonGreen(1, 2);

	~huge.subMixer.mute(0);
	~huge.subMixer.mute(2);
	~huge.subMixer.mute(3);

	~huge.initShift.mixer.mute;
	~huge.reverb.setMix(0.9);
	~huge.distortion.setMix(0);

}, bank: preset0, page: page);

// default:


//////////////////
//// Grid: //////
////////////////


///////// main grid:

////// freeze:
~apc.turnGridWhite(0, 4, bank: mainGridBank, page: page);
~apc.setGridFunc(0, 4, { ~huge.freeze; ~apc.turnGridGreen(0, 4); },
	bank: mainGridBank, page: page);
~apc.setGridFunc(0, 4, { ~apc.turnGridWhite(0, 4) }, 'noteOff', bank: mainGridBank, page: page);

~apc.turnGridRed(1, 4, bank: mainGridBank, page: page);
~apc.setGridFunc(1, 4, { ~huge.releaseFreeze; ~apc.turnGridWhite(1, 4); },
	bank: mainGridBank, page: page);
~apc.setGridFunc(1, 4, { ~apc.turnGridRed(1, 4) }, 'noteOff', bank: mainGridBank, page: page);

/////////// pitch shifts:

shiftResetFunc = {
	4.do({ | i | ~apc.turnGridPink(i, 3, 1, mainGridBank, page); ~apc.turnGridPink(i, 2, 1, mainGridBank, page); });
};

shiftResetFunc.value;

///// fifths:
~apc.setGridFunc(0, 3, {
	shiftArray = [7, 12, -5, 7];
	shiftResetFunc.value;
	4.do({ | i | ~huge.multiShift.setShift(i, shiftArray.choose) });
	~apc.turnGridWhite(0, 3);
}, bank: mainGridBank, page: page);
~apc.setGridFunc(0, 3, {shiftResetFunc.value;  ~apc.turnGridGreen(0, 3) }, 'noteOff', mainGridBank, page);

///// octaves!:
~apc.setGridFunc(1, 3, {
	shiftArray = [-24, -12, 12, -12];
	shiftResetFunc.value;
	4.do({ | i |  ~huge.multiShift.setShift(i, shiftArray.choose) });
	~apc.turnGridWhite(1, 3);
}, bank: mainGridBank, page: page);
~apc.setGridFunc(1, 3, {shiftResetFunc.value;  ~apc.turnGridGreen(1, 3) }, 'noteOff', mainGridBank, page);

///// high fifths:
~apc.setGridFunc(2, 3, {
	shiftArray = [7, 7, 12, 19, 24, -5];
	shiftResetFunc.value;
	4.do({ | i | ~huge.multiShift.setShift(i, shiftArray.choose) });
	~apc.turnGridWhite(2, 3);
}, bank: mainGridBank, page: page);
~apc.setGridFunc(2, 3, {shiftResetFunc.value;  ~apc.turnGridGreen(2, 3) }, 'noteOff', mainGridBank, page);

///// pentatonic cluster:
~apc.setGridFunc(3, 3, {
	shiftArray = [2, 4, 7, 9, -12];
	shiftResetFunc.value;
	4.do({ | i |  ~huge.multiShift.setShift(i, shiftArray.choose) });
	~apc.turnGridWhite(3, 3);
}, bank: mainGridBank, page: page);
~apc.setGridFunc(3, 3, {shiftResetFunc.value;  ~apc.turnGridGreen(3, 3) }, 'noteOff', mainGridBank, page);

///// major:
~apc.setGridFunc(0, 2, {
	shiftArray = [-12, -5, 4, 7, -8];
	shiftResetFunc.value;
	4.do({ | i |  ~huge.multiShift.setShift(i, shiftArray.choose) });
	~apc.turnGridWhite(0, 2);
}, bank: mainGridBank, page: page);
~apc.setGridFunc(0, 2, {shiftResetFunc.value;  ~apc.turnGridGreen(0, 2) }, 'noteOff', mainGridBank, page);

///// major 7:
~apc.setGridFunc(1, 2, {
	shiftArray = [-12, -8, -5, -1, 4, 7];
	shiftResetFunc.value;
	4.do({ | i |  ~huge.multiShift.setShift(i, shiftArray.choose) });
	~apc.turnGridWhite(1, 2);
}, bank: mainGridBank, page: page);
~apc.setGridFunc(1, 2, {shiftResetFunc.value;  ~apc.turnGridGreen(1, 2) }, 'noteOff', mainGridBank, page);

///// minor:
~apc.setGridFunc(2, 2, {
	shiftArray = [-12, -9, -5, 3, 7];
	shiftResetFunc.value;
	4.do({ | i |  ~huge.multiShift.setShift(i, shiftArray.choose) });
	~apc.turnGridWhite(2, 2);
}, bank: mainGridBank, page: page);
~apc.setGridFunc(2, 2, {shiftResetFunc.value;  ~apc.turnGridGreen(2, 2) }, 'noteOff', mainGridBank, page);

///// minor 7:
~apc.setGridFunc(3, 2, {
	shiftArray = [-12, -9, -5, -2, 3, 7];
	shiftResetFunc.value;
	4.do({ | i |  ~huge.multiShift.setShift(i, shiftArray.choose) });
	~apc.turnGridWhite(3, 2);
}, bank: mainGridBank, page: page);
~apc.setGridFunc(3, 2, {shiftResetFunc.value;  ~apc.turnGridGreen(3, 2) }, 'noteOff', mainGridBank, page);


//// GRAIN ENVELOPES:
envReset = {
	~apc.turnGridBlue(4, 4, 1, bank: mainGridBank, page: page);
	~apc.turnGridBlue(5, 4, 1, bank: mainGridBank, page: page);
	~apc.turnGridBlue(6, 4, 1, bank: mainGridBank, page: page);
	~apc.turnGridBlue(7, 4, 1, bank: mainGridBank, page: page);
	~apc.turnGridBlue(4, 3, 1, bank: mainGridBank, page: page);
	~apc.turnGridBlue(5, 3, 1, bank: mainGridBank, page: page);
	~apc.turnGridBlue(6, 3, 1, bank: mainGridBank, page: page);
};
envReset.value;

~apc.setGridMonitorFunc(\grainEnv, {
	var env = ~huge.granulator.grainEnv;
	switch( env,
		'hann', { envReset.value; ~apc.turnGridGreen(4, 4, bank: mainGridBank, page: page); },
		'gabor', { envReset.value; ~apc.turnGridGreen(5, 4, bank: mainGridBank, page: page); },
		'gabWide', { envReset.value; ~apc.turnGridGreen(6, 4, bank: mainGridBank, page: page); },
		'perc', { envReset.value; ~apc.turnGridGreen(7, 4, bank: mainGridBank, page: page); },
		'revPerc', { envReset.value; ~apc.turnGridGreen(4, 3, bank: mainGridBank, page: page); },
		'expodec', { envReset.value; ~apc.turnGridGreen(5, 3, bank: mainGridBank, page: page); },
		'rexpodec', { envReset.value; ~apc.turnGridGreen(6, 3, bank: mainGridBank, page: page); });
}, bank: mainGridBank, page: page);

~apc.setGridFunc(4, 4, { ~huge.granulator.setGrainEnvelope('hann'); }, bank: mainGridBank, page: page);
~apc.setGridFunc(5, 4, { ~huge.granulator.setGrainEnvelope('gabor'); }, bank: mainGridBank, page: page);
~apc.setGridFunc(6, 4, { ~huge.granulator.setGrainEnvelope('gabWide'); }, bank: mainGridBank, page: page);
~apc.setGridFunc(7, 4, { ~huge.granulator.setGrainEnvelope('perc'); }, bank: mainGridBank, page: page);
~apc.setGridFunc(4, 3, { ~huge.granulator.setGrainEnvelope('revPerc'); }, bank: mainGridBank, page: page);
~apc.setGridFunc(5, 3, { ~huge.granulator.setGrainEnvelope('expodec'); }, bank: mainGridBank, page: page);
~apc.setGridFunc(6, 3, { ~huge.granulator.setGrainEnvelope('rexpodec'); }, bank: mainGridBank, page: page);

///// grains sync:

~apc.setGridMonitorFunc(\granSync, {
	if( ~huge.granulator.granulator.sync == 0, { ~apc.turnGridLightBlue(4, 1); }, { ~apc.turnGridPurple(4, 1) });
}, bank: mainGridBank, page: page);
~apc.setGridFunc(4, 1, { if( ~huge.granulator.granulator.sync == 0,
	{ ~huge.granulator.setSync(1) }, { ~huge.granulator.setSync(0) });
}, bank: mainGridBank, page: page);


///////////// grain rates:
~apc.setGridMonitorFunc(\grainRates, {
	var rate = ~huge.granulator.granulator.rateLow;
	if( rate == 0.5, { ~apc.turnGridGreen(4, 2) }, { ~apc.turnGridYellow(4, 2, 1); });
	if( rate == 1, { ~apc.turnGridGreen(5, 2) }, { ~apc.turnGridYellow(5, 2, 1); });
	if( rate == 2, { ~apc.turnGridGreen(6, 2) }, { ~apc.turnGridYellow(6, 2, 1); });
	if( rate == 4, { ~apc.turnGridGreen(7, 2) }, { ~apc.turnGridYellow(7, 2, 1); });
}, bank: mainGridBank, page: page);
~apc.setGridFunc(4, 2, { ~huge.granulator.setRate(0.5, 0.5); }, bank: mainGridBank, page: page);
~apc.setGridFunc(5, 2, { ~huge.granulator.setRate(1, 1); }, bank: mainGridBank, page: page);
~apc.setGridFunc(6, 2, { ~huge.granulator.setRate(2, 2); }, bank: mainGridBank, page: page);
~apc.setGridFunc(7, 2, { ~huge.granulator.setRate(4, 4); }, bank: mainGridBank, page: page);




///////// randomize filter cutoff:

~apc.turnGridYellowGreen(0, 0, 2, mainGridBank, page);
~apc.setGridFunc(0, 0, {
	var cutoff = exprand(300, 5500);
	~huge.lowPass.setCutoff(cutoff);
	~apc.turnGridWhite(0, 0);
}, bank: mainGridBank, page: page);
~apc.setGridFunc(0, 0, { ~apc.turnGridYellowGreen(0, 0) }, 'noteOff', mainGridBank, page);




///////// grain rate grid:

~apc.turnGridWhite(0, 4, bank: rateGridBank, page: page);
~apc.setGridFunc(0, 4, { ~huge.freeze; ~apc.turnGridGreen(0, 4); },
	bank: rateGridBank, page: page);
~apc.setGridFunc(0, 4, { ~apc.turnGridWhite(0, 4) }, 'noteOff', bank: rateGridBank, page: page);

~apc.turnGridRed(1, 4, bank: rateGridBank, page: page);
~apc.setGridFunc(1, 4, { ~huge.releaseFreeze; ~apc.turnGridWhite(1, 4); },
	bank: rateGridBank, page: page);
~apc.setGridFunc(1, 4, { ~apc.turnGridRed(1, 4) }, 'noteOff', bank: rateGridBank, page: page);

rateResetFunc = {
	8.do({ | i | ~apc.turnGridPurple(i, 0, 1, page: page, bank: rateGridBank); });
	8.do({ | i | ~apc.turnGridPink(i, 2, 1, page: page, bank: rateGridBank); });
	~apc.turnGridPurple(1, 1, 1, page: page, bank: rateGridBank);
	~apc.turnGridPurple(2, 1, 1, page: page, bank: rateGridBank);
	~apc.turnGridPurple(4, 1, 1, page: page, bank: rateGridBank);
	~apc.turnGridPurple(5, 1, 1, page: page, bank: rateGridBank);
	~apc.turnGridPurple(6, 1, 1, page: page, bank: rateGridBank);
	~apc.turnGridPink(1, 3, 1, page: page, bank: rateGridBank);
	~apc.turnGridPink(2, 3, 1, page: page, bank: rateGridBank);
	~apc.turnGridPink(4, 3, 1, page: page, bank: rateGridBank);
	~apc.turnGridPink(5, 3, 1, page: page, bank: rateGridBank);
	~apc.turnGridPink(6, 3, 1, page: page, bank: rateGridBank);

	4.do({ | i | ~apc.turnGridYellow(i+4, 4, 1, bank: rateGridBank, page: page); });
};


rateResetFunc.value;

8.do({ | i |
	var io = [0, 2, 4, 5, 7, 9, 11, 12];
	~apc.setGridFunc(i, 2, {
		~huge.granulator.setRate(io.at(i).midiratio, io.at(i).midiratio);
		rateResetFunc.value;
		~apc.turnGridGreen(i, 2);
	},bank: rateGridBank, page: page);
});
~apc.setGridFunc(1, 3, {
	~huge.granulator.setRate(1.midiratio, 1.midiratio);
	rateResetFunc.value;
	~apc.turnGridGreen(1, 3);
},bank: rateGridBank, page: page);
~apc.setGridFunc(2, 3, {
	~huge.granulator.setRate(3.midiratio, 3.midiratio);
	rateResetFunc.value;
	~apc.turnGridGreen(2, 3);
},bank: rateGridBank, page: page);
~apc.setGridFunc(4, 3, { ~huge.granulator.setRate(6.midiratio, 6.midiratio);
	rateResetFunc.value;
	~apc.turnGridGreen(4, 3);
},bank: rateGridBank, page: page);
~apc.setGridFunc(5, 3, { ~huge.granulator.setRate(8.midiratio, 8.midiratio);
	rateResetFunc.value;
	~apc.turnGridGreen(5, 3);
},bank: rateGridBank, page: page);
~apc.setGridFunc(6, 3, { ~huge.granulator.setRate(10.midiratio, 10.midiratio);
	rateResetFunc.value;
	~apc.turnGridGreen(6, 3);
},bank: rateGridBank, page: page);

8.do({ | i |
	var io = [0, 2, 4, 5, 7, 9, 11, 12]-12;
	~apc.setGridFunc(i, 0, {
		~huge.granulator.setRate(io.at(i).midiratio, io.at(i).midiratio);
		rateResetFunc.value;
		~apc.turnGridGreen(i, 0);
	},bank: rateGridBank, page: page);
});
~apc.setGridFunc(1, 1, { ~huge.granulator.setRate(-11.midiratio, -11.midiratio);
	rateResetFunc.value;
	~apc.turnGridGreen(1, 1);
},bank: rateGridBank, page: page);
~apc.setGridFunc(2, 1, { ~huge.granulator.setRate(-9.midiratio, -9.midiratio);
	rateResetFunc.value;
	~apc.turnGridGreen(2, 1);
},bank: rateGridBank, page: page);
~apc.setGridFunc(4, 1, { ~huge.granulator.setRate(-6.midiratio, -6.midiratio);
	rateResetFunc.value;
	~apc.turnGridGreen(4, 1);
},bank: rateGridBank, page: page);
~apc.setGridFunc(5, 1, { ~huge.granulator.setRate(-4.midiratio, -4.midiratio);
	rateResetFunc.value;
	~apc.turnGridGreen(5, 1);
},bank: rateGridBank, page: page);
~apc.setGridFunc(6, 1, { ~huge.granulator.setRate(-2.midiratio, -2.midiratio);
	rateResetFunc.value;
	~apc.turnGridGreen(6, 1);
}, bank: rateGridBank, page: page);


~apc.setGridFunc(4, 4, {
	~huge.granulator.setRate(0.25, 0.25);
	rateResetFunc.value;
	~apc.turnGridGreen(4, 4);
}, bank: rateGridBank, page: page);
~apc.setGridFunc(5, 4, {
	~huge.granulator.setRate(4, 4);
	rateResetFunc.value;
	~apc.turnGridGreen(5, 4);
}, bank: rateGridBank, page: page);
~apc.setGridFunc(6, 4, {
	~huge.granulator.setRate(0.1, 0.5);
	rateResetFunc.value;
	~apc.turnGridGreen(6, 4);
}, bank: rateGridBank, page: page);
~apc.setGridFunc(7, 4, {
	~huge.granulator.setRate(500, 1000);
	rateResetFunc.value;
	~apc.turnGridGreen(7, 4);
}, bank: rateGridBank, page: page);


~apc.turnGridMagenta(4, 0, bank: mainGridBank, page: page);
~apc.setGridFunc(4, 0, { if(midiFuncsIsLoaded != true,
	{ makeMIDIFuncs.value(~pyr, 14); ~apc.turnGridWhite(4, 0); },
	{ freeMIDIFuncs.value; ~apc.turnGridMagenta(4, 0) });
}, bank: mainGridBank, page: page);