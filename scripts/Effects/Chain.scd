/*
Chain.scd
*/

var page = 'chain';

var main = 0;
var concat1 = 1;
var concat2 = 2;
var granulator = 3;
var delay = 4;
var eq = 5;

var freezeRate = 1;

var shiftPreset = 0;

var freezeResetFunc, rateReset, envReset, shiftReset, devButtonReset;

var activeDeviceBank = 0;

var tempFB;

~apc.makePage(page);

//// master funcs:
~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.storageDict[\loadMasterFunc].value;

~apc.addDeviceEncodersBanks(5, page);

//////// mixer:

~apc.setMixerMonitorFunc(\mixer, {
	var main, freeze, conGlitch, conPitch, conNoPitch, conDry;
	var input, mic, prePost;
	main = ~chain.mixer.isMuted;
	freeze = ~chain.freezer.mixer.isMuted;
	conGlitch = ~chain.concat.subMixer.isMuted(0);
	conPitch = ~chain.concat.subMixer.isMuted(1);
	conNoPitch = ~chain.concat.subMixer.isMuted(2);
	conDry = ~chain.concat.subMixer.isMuted(3);
	input = ~chInput.isMuted;
	mic = ~chMic.isMuted;
	prePost = ~chain.mixer.preOrPost;
	if(main, { ~apc.turnTrackSelectButtonOff(1) }, { ~apc.turnTrackSelectButtonOn(1) });
	if(freeze, { ~apc.turnTrackSelectButtonOff(2) }, { ~apc.turnTrackSelectButtonOn(2) });
	if(conGlitch, { ~apc.turnTrackSelectButtonOff(3) }, { ~apc.turnTrackSelectButtonOn(3) });
	if(conPitch, { ~apc.turnTrackSelectButtonOff(4) }, { ~apc.turnTrackSelectButtonOn(4) });
	if(conNoPitch, { ~apc.turnTrackSelectButtonOff(5) }, { ~apc.turnTrackSelectButtonOn(5) });
	if(conDry, { ~apc.turnTrackSelectButtonOff(6) }, { ~apc.turnTrackSelectButtonOn(6) });
	if(input, { ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
	if(mic, { ~apc.turnRecordEnableButtonOff(2) }, { ~apc.turnRecordEnableButtonOn(2) });
	if( prePost == 'post', { ~apc.turnCrossfaderSelectButtonOrange(1) }, { ~apc.turnCrossfaderSelectButtonYellow(1) });
}, main, page);


// mixer funcs:
~apc.setFaderFunc(1, { | val | ~chain.mixer.setVol(val.ccdbfs); }, main, page);
~apc.setFaderFunc(2, { | val | ~chain.freezer.mixer.setVol(val.ccdbfs); }, main, page);
~apc.setFaderFunc(3, { | val | ~chain.concat.subMixer.setVol(0, val.ccdbfs); }, main, page);
~apc.setFaderFunc(4, { | val | ~chain.concat.subMixer.setVol(1, val.ccdbfs);  }, main, page);
~apc.setFaderFunc(5, { | val | ~chain.concat.subMixer.setVol(2, val.ccdbfs);  }, main, page);
~apc.setFaderFunc(6, { | val | ~chain.concat.subMixer.setVol(3, val.ccdbfs);  }, main, page);
// granulator mix:
~apc.setFaderFunc(7, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~chain.granulator.setMix(mix);
}, main, page);
// delay mix:
~apc.setFaderFunc(8, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~chain.delay.setMix(mix);
}, main, page);

//// track mutes, etc:
// record enable:
~apc.setRecordEnableButtonFunc(1, { ~chInput.tglMute; }, bank: main, page: page);
// record enable:
~apc.setRecordEnableButtonFunc(2, { ~chMic.tglMute; }, bank: main, page: page);
// pre/post:
~apc.setCrossfaderSelectFunc(1, {
	if( ~chain.mixer.preOrPost == 'post', { ~chain.mixer.setSendPre }, { ~chain.mixer.setSendPost });
}, bank: main, page: page);
// effect on:
~apc.setTrackSelectFunc(1, { ~chain.mixer.tglMute; }, bank: main, page: page);
// freezer on:
~apc.setTrackSelectFunc(2, { ~chain.freezer.mixer.tglMute; }, bank: main, page: page);
// concat glitch:
~apc.setTrackSelectFunc(3, { ~chain.concat.subMixer.tglMute(0) }, bank: main, page: page);
// concat pitch:
~apc.setTrackSelectFunc(4, { ~chain.concat.subMixer.tglMute(1) }, bank: main, page: page);
// concat noPitch:
~apc.setTrackSelectFunc(5, { ~chain.concat.subMixer.tglMute(2) }, bank: main, page: page);
// concat dry:
~apc.setTrackSelectFunc(6, { ~chain.concat.subMixer.tglMute(3) }, bank: main, page: page);

//// sends:
~apc.setMixerEncodersMonitorFunc('mainBank', {
	var reverb, granulator, modular, delay;
	var preVol, granMix, delMix, lowPass;
	reverb = ~chain.mixer.sendVol(0).dbfsCC;
	granulator = ~chain.mixer.sendVol(1).dbfsCC;
	modular = ~chain.mixer.sendVol(2).dbfsCC;
	delay = ~chain.mixer.sendVol(3).dbfsCC;
	granMix = ~chain.granulator.mix.linlin(0, 1, 0, 127);
	delMix = ~chain.delay.mix.linlin(0, 1, 0, 127);
	lowPass = ~chain.eq.lowPassCutoff.explin(150, 20000, 0, 127);
	//preVol = ~chInput.mixer.vol.dbfsCC;
	~apc.setMixerEncoderValue(1, reverb);
	~apc.setMixerEncoderValue(2, granulator);
	~apc.setMixerEncoderValue(3, modular);
	~apc.setMixerEncoderValue(4, delay);
	~apc.setMixerEncoderValue(6, granMix);
	~apc.setMixerEncoderValue(7, delMix);
	~apc.setMixerEncoderValue(8, lowPass);
}, main, page);
~apc.setMixerEncoderFunc(1, { | val |
	~chain.mixer.setSendVol(0, val.ccdbfs);
}, main, page);
~apc.setMixerEncoderFunc(2, { | val |
	~chain.mixer.setSendVol(1, val.ccdbfs);
}, main, page);
~apc.setMixerEncoderFunc(3, { | val |
	~chain.mixer.setSendVol(2, val.ccdbfs);
}, main, page);
~apc.setMixerEncoderFunc(4, { | val |
	~chain.mixer.setSendVol(3, val.ccdbfs);
}, main, page);

~apc.setMixerEncoderFunc(6, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~chain.granulator.setMix(mix);
}, main, page);
~apc.setMixerEncoderFunc(7, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~chain.delay.setMix(mix);
}, main, page);
~apc.setMixerEncoderFunc(8, { | val |
	var freq = val.linexp(0, 127, 150, 20000);
	~chain.eq.setLowPassCutoff(freq);
}, main, page);

////////
// main grid:
////////

//~apc.turnAllGridCyan(0, main, page);

//// freezer:
freezeResetFunc = { 4.do({ | i |  ~apc.turnGridWhite(i, 3, 1, main, page); }); };
~apc.setGridMonitorFunc(\freeze, {
	if( ~chain.freezer.freezeActive, { ~apc.turnGridGreen(0, 4) }, { ~apc.turnGridWhite(0, 4) });
	switch(freezeRate,
		0.25, { freezeResetFunc.value; ~apc.turnGridGreen(0, 3) },
		0.5, { freezeResetFunc.value; ~apc.turnGridGreen(1, 3) },
		1, { freezeResetFunc.value; ~apc.turnGridGreen(2, 3) },
		2, { freezeResetFunc.value; ~apc.turnGridGreen(3, 3) },
	);
}, main, page);
~apc.setGridFunc(0, 4, {
	if( ~chain.freezer.freezeActive, { ~chain.freezer.releaseFreeze }, { ~chain.freezer.freeze; });
}, bank: main, page: page);
~apc.setGridFunc(0, 3, { freezeRate = 0.25; ~chain.freezer.setPlayRate(freezeRate);
}, bank: main, page: page);
~apc.setGridFunc(1, 3, { freezeRate = 0.5; ~chain.freezer.setPlayRate(freezeRate);
}, bank: main, page: page);
~apc.setGridFunc(2, 3, { freezeRate = 1; ~chain.freezer.setPlayRate(freezeRate);
}, bank: main, page: page);
~apc.setGridFunc(3, 3, { freezeRate = 2; ~chain.freezer.setPlayRate(freezeRate);
}, bank: main, page: page);


//// grainEnv buttons:
envReset = {
	~apc.turnGridBlue(4, 4, 1, page: page);
	~apc.turnGridBlue(5, 4, 1, page: page);
	~apc.turnGridBlue(6, 4, 1, page: page);
	~apc.turnGridBlue(7, 4, 1, page: page);
	~apc.turnGridBlue(4, 3, 1, page: page);
	~apc.turnGridBlue(5, 3, 1, page: page);
	~apc.turnGridBlue(6, 3, 1, page: page);
};
envReset.value;

~apc.setGridMonitorFunc(\grainEnv, {
	var env = ~chain.granulator.grainEnvelope;
	switch( env,
		'hann', { envReset.value; ~apc.turnGridGreen(4, 4, page: page); },
		'gabor', { envReset.value; ~apc.turnGridGreen(5, 4, page: page); },
		'gabWide', { envReset.value; ~apc.turnGridGreen(6, 4, page: page); },
		'perc', { envReset.value; ~apc.turnGridGreen(7, 4, page: page); },
		'revPerc', { envReset.value; ~apc.turnGridGreen(4, 3, page: page); },
		'expodec', { envReset.value; ~apc.turnGridGreen(5, 3, page: page); },
		'rexpodec', { envReset.value; ~apc.turnGridGreen(6, 3, page: page); });
}, page: page);

~apc.setGridFunc(4, 4, { ~chain.granulator.setGrainEnvelope('hann'); }, page: page);
~apc.setGridFunc(5, 4, { ~chain.granulator.setGrainEnvelope('gabor'); }, page: page);
~apc.setGridFunc(6, 4, { ~chain.granulator.setGrainEnvelope('gabWide'); }, page: page);
~apc.setGridFunc(7, 4, { ~chain.granulator.setGrainEnvelope('perc'); }, page: page);
~apc.setGridFunc(4, 3, { ~chain.granulator.setGrainEnvelope('revPerc'); }, page: page);
~apc.setGridFunc(5, 3, { ~chain.granulator.setGrainEnvelope('expodec'); }, page: page);
~apc.setGridFunc(6, 3, { ~chain.granulator.setGrainEnvelope('rexpodec'); }, page: page);

rateReset = { 4.do({ | i | ~apc.turnGridYellow(i+4, 1, 1, main, page); }); };
rateReset.value;

~apc.setGridMonitorFunc(\granulator, {
	var freeze, sync;
	var rate = ~chain.granulator.rateLow;
	freeze = ~chain.granulator.isFrozen;
	sync = ~chain.granulator.sync;
	if( freeze == true, { ~apc.turnGridRed(4, 2) }, { ~apc.turnGridWhite(4, 2) });
	if( sync == 0, { ~apc.turnGridCyan(5, 2) }, { ~apc.turnGridGreen(5, 2) });
	switch( rate,
		1, { rateReset.value; ~apc.turnGridGreen(4, 1, 2); },
		0.5, { rateReset.value; ~apc.turnGridGreen(5, 1, 2); },
		0.25, { rateReset.value; ~apc.turnGridGreen(6, 1, 2); },
		2, { rateReset.value; ~apc.turnGridGreen(7, 1, 2); }
	);
}, page: page);


~apc.turnGridLightBlue(6, 2, 1, main, page);
// toggle freeze:
~apc.setGridFunc(4, 2, { ~chain.granulator.toggleFreeze; }, bank: main, page: page);
// toggle sync:
~apc.setGridFunc(5, 2, { ~chain.granulator.toggleSync }, bank: main, page: page);
// normalizes grain size to grainDurLow, turns sync on, sets pans to 0.
~apc.setGridFunc(6, 2, {
	var size = ~prm.granulator.grainDurLow;
	~chain.granulator.setGrainDur(size, size);
	~chain.granulator.turnSyncOn;
	~chain.granulator.setPan(0, 0);
	~apc.turnGridLightBlue(6, 2, 2);
}, bank: main, page: page);
~apc.setGridFunc(6, 2, { ~apc.turnGridLightBlue(6, 2, 1); }, 'noteOff', bank: main, page: page);

//// grain rates:
// 1:
~apc.setGridFunc(4, 1, { ~chain.granulator.setRate(1, 1); }, bank: main, page: page);
// 0.5:
~apc.setGridFunc(5, 1, { ~chain.granulator.setRate(0.5, 0.5); }, bank: main, page: page);
// 0.25:
~apc.setGridFunc(6, 1, { ~chain.granulator.setRate(0.25, 0.25); }, bank: main, page: page);
// 2:
~apc.setGridFunc(7, 1, { ~chain.granulator.setRate(2, 2); }, bank: main, page: page);


////// multiShift;
shiftReset = { 4.do({ | i | ~apc.turnGridPink(i, 1, 1, main, page); }); };
shiftReset.value;
~apc.setGridMonitorFunc(\multiShift, {
	var bypass = ~chain.multiShift.isBypassed;
	if( bypass, { ~apc.turnGridRed(0, 2, 1); }, { ~apc.turnGridGreen(0, 2, 2) });
	switch(shiftPreset,
		0, { shiftReset.value; ~apc.turnGridGreen(0, 1); },
		1, { shiftReset.value; ~apc.turnGridGreen(1, 1); },
		2, { shiftReset.value; ~apc.turnGridGreen(2, 1); },
		3, { shiftReset.value; ~apc.turnGridGreen(3, 1); },
	);
}, main, page);
~apc.setGridFunc(0, 2, { ~chain.multiShift.tglBypass }, bank: main, page: page);
~apc.setGridFunc(0, 1, { shiftPreset = 0; ~chain.multiShift.setShiftArray([-12, 12, -5, 7]);
}, bank: main, page: page);
~apc.setGridFunc(1, 1, { shiftPreset = 1; ~chain.multiShift.setShiftArray([-2, 2, 4, 5]);
}, bank: main, page: page);
~apc.setGridFunc(2, 1, { shiftPreset = 2; ~chain.multiShift.setShiftArray([-12, -12, 0.0005, 0]);
}, bank: main, page: page);
~apc.setGridFunc(3, 1, { shiftPreset = 3; ~chain.multiShift.setShiftArray([-1, -0.5, 0.5, 1]);
}, bank: main, page: page);


/////// delay fun:
~apc.turnGridYellowGreen(0, 0, 2, main, page);
~apc.setGridFunc(0, 0, {
	tempFB = ~chain.delay.feedback;
	~chain.delay.setFeedback(1.3);
	~apc.turnGridGreen(0, 0);
}, 'noteOn', main, page);
~apc.setGridFunc(0, 0, {
	~chain.delay.setFeedback(tempFB);
	~apc.turnGridYellowGreen(0, 0);
}, 'noteOff', main, page);


////////// device encoders:

devButtonReset = { 8.do({ | i | ~apc.turnDeviceButtonOff(i+1, main, page); }); };

//// device encoders change:
~apc.setDeviceButtonsMonitorFunc(\stuffchange, {
	switch(activeDeviceBank,
		main, { devButtonReset.value; ~apc.turnDeviceButtonOn(1) },
		concat1,  { devButtonReset.value; ~apc.turnDeviceButtonOn(2) },
		concat2,  { devButtonReset.value; ~apc.turnDeviceButtonOn(3) },
		granulator,  { devButtonReset.value; ~apc.turnDeviceButtonOn(4) },
		delay,  { devButtonReset.value; ~apc.turnDeviceButtonOn(5) },
		eq,  { devButtonReset.value; ~apc.turnDeviceButtonOn(6) }
	);
}, main, page);

~apc.setDeviceButtonFunc(1, {
	activeDeviceBank = main;
	~apc.setActiveDeviceEncodersBank(activeDeviceBank);
}, bank: main, page: page);
~apc.setDeviceButtonFunc(2, {
	activeDeviceBank = concat1;
	~apc.setActiveDeviceEncodersBank(activeDeviceBank);
}, bank: main, page: page);
~apc.setDeviceButtonFunc(3, {
	activeDeviceBank = concat2;
	~apc.setActiveDeviceEncodersBank(activeDeviceBank);
}, bank: main, page: page);
~apc.setDeviceButtonFunc(4, {
	activeDeviceBank = granulator;
	~apc.setActiveDeviceEncodersBank(activeDeviceBank);
}, bank: main, page: page);
~apc.setDeviceButtonFunc(5, {
	activeDeviceBank = delay;
	~apc.setActiveDeviceEncodersBank(activeDeviceBank);
}, bank: main, page: page);
~apc.setDeviceButtonFunc(6, {
	activeDeviceBank = eq;
	~apc.setActiveDeviceEncodersBank(activeDeviceBank);
}, bank: main, page: page);


///// main:
~apc.setDeviceEncodersMonitorFunc(\main, {
	var time = ~chain.delay.delayTime.explin(0.05, 10, 0, 127);
	var fb = ~chain.delay.feedback.linlin(0, 1, 0, 127);
	var lowpass = ~chain.eq.lowPassCutoff.explin(20, 20000, 0, 127);
	var highpass = ~chain.eq.highPassCutoff.explin(5, 15000, 0, 127);
	~apc.setDeviceEncoderValue(1, time);
	~apc.setDeviceEncoderValue(2, fb);
	~apc.setDeviceEncoderValue(3, lowpass);
	~apc.setDeviceEncoderValue(4, highpass);
}, main, page);

// delay time:
~apc.setDeviceEncoderFunc(1, { | val |
	var time = val.linexp(0, 127, 0.05, 10);
	~chain.delay.setDelayTime(time);
}, main, page);
// delay feedback:
~apc.setDeviceEncoderFunc(2, { | val |
	var fb = val.linlin(0, 127, 0, 1);
	~chain.delay.setFeedback(fb);
}, main, page);
// lowpass:
~apc.setDeviceEncoderFunc(3, { | val |
	var cutoff = val.linexp(0, 127, 20, 20000);
	~chain.eq.setLowPassCutoff(cutoff);
}, main, page);
// highpass:
~apc.setDeviceEncoderFunc(4, { | val |
	var cutoff = val.linexp(0, 127, 5, 15000);
	~chain.eq.setHighPassCutoff(cutoff);
}, main, page);


//// Granulator:
~apc.setDeviceEncodersMonitorFunc(\granulator, {
	var grainDurLow, grainDurHigh, trigRate, cutoff;
	var level, delayTime, feedback, pan;
	grainDurLow = ~chain.granulator.grainDurLow.explin(0.005, 2.5, 0, 127);
	grainDurHigh = ~chain.granulator.grainDurHigh.explin(0.01, 2.7, 0, 127);
	trigRate = ~chain.granulator.trigRate.linlin(1, 64, 0, 127);
	cutoff = ~chain.granulator.filterCutoff.explin(150, 20000, 0, 127);
	level = ~chain.granulator.delayLevel.linlin(0, 1, 0, 127);
	delayTime = ~chain.granulator.delayTime.linlin(0.01, 5, 0, 127);
	feedback = ~chain.granulator.feedback.linlin(0, 1, 0, 127);
	pan = ~chain.granulator.panHigh.linlin(0, 1, 0, 127);
	~apc.setDeviceEncoderValue(1, grainDurLow, granulator, page);
	~apc.setDeviceEncoderValue(2, grainDurHigh, granulator, page);
	~apc.setDeviceEncoderValue(3, trigRate, granulator, page);
	~apc.setDeviceEncoderValue(4, cutoff, granulator, page);
	~apc.setDeviceEncoderValue(5, level, granulator, page);
	~apc.setDeviceEncoderValue(6, delayTime, granulator, page);
	~apc.setDeviceEncoderValue(7, feedback, granulator, page);
	~apc.setDeviceEncoderValue(8, pan, granulator, page);
}, granulator, page);

// grainDur Low:
~apc.setDeviceEncoderFunc(1, { | val |
	var grainDur = val.linexp(0, 127, 0.005, 2.5);
	~chain.granulator.setGrainDurLow(grainDur);
}, granulator, page);
// grainDur High:
~apc.setDeviceEncoderFunc(2, { | val |
	var grainDur = val.linexp(0, 127, 0.01, 2.7);
	~chain.granulator.setGrainDurHigh(grainDur);
}, granulator, page);
// trigRate:
~apc.setDeviceEncoderFunc(3, { | val |
	var rate = val.linlin(0, 127, 1, 64);
	~chain.granulator.setTrigRate(rate);
}, granulator, page);
// cutoff:
~apc.setDeviceEncoderFunc(4, { | val |
	var cutoff = val.linexp(0, 127, 150, 20000);
	~chain.granulator.setFilterCutoff(cutoff);
}, granulator, page);
// delay level:
~apc.setDeviceEncoderFunc(5, { | val |
	var level = val.linlin(0, 127, 0, 1);
	~chain.granulator.setDelayLevel(level);
}, granulator, page);
//delayTime:
~apc.setDeviceEncoderFunc(6, { | val |
	var delay = val.linlin(0, 127, 0.01, 5);
	~chain.granulator.setDelayTime(delay);
}, granulator, page);
//feedback:
~apc.setDeviceEncoderFunc(7, { | val |
	var feedback = val.linlin(0, 127, 0, 1);
	~chain.granulator.setFeedback(feedback);
}, granulator, page);
// pan:
~apc.setDeviceEncoderFunc(8, { | val |
	var panLow = val.linlin(0, 127, 0, -1);
	var panHigh = val.linlin(0, 127, 0, 1);
	~chain.granulator.setPanLow(panLow);
	~chain.granulator.setPanHigh(panHigh);
}, granulator, page);


// concat 1 (lengths):
~apc.setDeviceEncodersMonitorFunc(\concat1, {
	var lowpass, highpass, glitchLength, glitchMod;
	var unPitchLength, unPitchMod, pitchLength, pitchMod;
	lowpass = ~chain.concat.postEQ.lowPassCutoff.explin(150, 20000, 0, 127);
	highpass = ~chain.concat.postEQ.highPassCutoff.explin(10, 3500, 0, 127);
	glitchLength = ~chain.concat.glitchLength.explin(0.05, 3, 0, 127);
	glitchMod = ~chain.concat.glitchModRange.explin(0.01, 1, 0, 127);
	unPitchLength = ~chain.concat.unPitchLength.linlin(0.001, 0.1, 0, 127);
	unPitchMod = ~chain.concat.unPitchModRange.linlin(0.0005, 0.05, 0, 127);
	pitchLength = ~chain.concat.pitchLength.linlin(0.005, 0.5, 0, 127);
	pitchMod = ~chain.concat.pitchModRange.linlin(0.001, 0.3, 0, 127);
	~apc.setDeviceEncoderValue(1, lowpass);
	~apc.setDeviceEncoderValue(2, highpass);
	~apc.setDeviceEncoderValue(3, glitchLength);
	~apc.setDeviceEncoderValue(4, glitchMod);
	~apc.setDeviceEncoderValue(5, unPitchLength);
	~apc.setDeviceEncoderValue(6, unPitchMod);
	~apc.setDeviceEncoderValue(7, pitchLength);
	~apc.setDeviceEncoderValue(8, pitchMod);
}, bank: concat1, page: page);
~apc.setDeviceEncoderFunc(1, { | val |
	var lowpass = val.linexp(0, 127, 150, 20000);
	~chain.concat.postEQ.setLowPassCutoff(lowpass);
}, bank: concat1, page: page);
~apc.setDeviceEncoderFunc(2, { | val |
	var highpass = val.linexp(0, 127, 10, 3500);
	~chain.concat.postEQ.setHighPassCutoff(highpass);
}, bank: concat1, page: page);
~apc.setDeviceEncoderFunc(3, { | val |
	var glitchLength = val.linexp(0, 127, 0.05, 3);
	~chain.concat.setGlitchLength(glitchLength);
}, bank: concat1, page: page);
~apc.setDeviceEncoderFunc(4, { | val |
	var glitchMod = val.linexp(0, 127, 0.01, 1);
	~chain.concat.setGlitchModRange(glitchMod);
}, bank: concat1, page: page);
~apc.setDeviceEncoderFunc(5, { | val |
	var unPitchLength = val.linlin(0, 127, 0.001, 0.1);
	~chain.concat.setUnPitchLength(unPitchLength);
}, bank: concat1, page: page);
~apc.setDeviceEncoderFunc(6, { | val |
	var unPitchMod = val.linlin(0, 127, 0.0005, 0.05);
	~chain.concat.setUnPitchModRange(unPitchMod);
}, bank: concat1, page: page);
~apc.setDeviceEncoderFunc(7, { | val |
	var pitchLength = val.linlin(0, 127, 0.005, 0.5);
	~chain.concat.setPitchLength(pitchLength);
}, bank: concat1, page: page);
~apc.setDeviceEncoderFunc(8, { | val |
	var pitchMod = val.linlin(0, 127, 0.001, 0.3);
	~chain.concat.setPitchModRange(pitchMod);
}, bank: concat1, page: page);

// concat2: more fun:
~apc.setDeviceEncodersMonitorFunc(\concat2, {
	var freq, modFreq, detune, lfo;
	freq = ~chain.concat.baseFreq.explin(50, 2500, 0, 127);
	modFreq = ~chain.concat.baseModFreq.explin(0.1, 55, 0, 127);
	detune = ~chain.concat.detune.linlin(0, 0.1, 0, 127);
	lfo = ~chain.concat.lfo.frequency.explin(0.001, 22, 0, 127);
	~apc.setDeviceEncoderValue(1, freq);
	~apc.setDeviceEncoderValue(2, modFreq);
	~apc.setDeviceEncoderValue(3, detune);
	~apc.setDeviceEncoderValue(4, lfo);
}, bank: concat2, page: page);
~apc.setDeviceEncoderFunc(1, { | val |
	var freq = val.linexp(0, 127, 50, 2500);
	~chain.concat.setBaseFreq(freq);
}, bank: concat2, page: page);
~apc.setDeviceEncoderFunc(2, { | val |
	var freq = val.linexp(0, 127, 0.1, 55);
	~chain.concat.setBaseModFreq(freq);
}, bank: concat2, page: page);
~apc.setDeviceEncoderFunc(3, { | val |
	var d = val.linlin(0, 127, 0, 0.1);
	~chain.concat.setDetune(d);
}, bank: concat2, page: page);
~apc.setDeviceEncoderFunc(4, { | val |
	var freq = val.linexp(0, 127, 0.001, 22);
	~chain.concat.lfo.setFrequency(freq);
}, bank: concat2, page: page);

