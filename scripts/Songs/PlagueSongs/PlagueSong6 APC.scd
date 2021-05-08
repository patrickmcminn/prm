/*
Monday, April 30th 2021
Plague6APC.scd
prm
*/

var page = 'plague6';

var verb = 0;
var gran = 1;
var mod = 2;
var delay = 3;

var main = 0;

var envReset, rateReset;

~apc.makePage(page);
~apc.addMixerEncodersBanks(3, page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);

~apc.mapMixer(~p6.mixer, 4, 0, 0, verb, gran, mod, delay, main, page);
~apc.mapSendBanks(\main, verb, gran, mod, delay, main, page);

/////// record enables:
~apc.setGridMonitorFunc(\recEnable, {
	if( ~p6.dry.isMuted, { ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1); });
	if( ~p6.multiShiftInput.isMuted, { ~apc.turnRecordEnableButtonOff(2) }, { ~apc.turnRecordEnableButtonOn(2); });
	if( ~p6.delNetInput.isMuted, { ~apc.turnRecordEnableButtonOff(3) }, { ~apc.turnRecordEnableButtonOn(3); });
	if( ~p6.concatInput.isMuted, { ~apc.turnRecordEnableButtonOff(4) }, { ~apc.turnRecordEnableButtonOn(4); });
}, main, page);
~apc.setRecordEnableButtonFunc(1, { ~p6.dry.tglMute }, bank: main, page: page);
~apc.setRecordEnableButtonFunc(2, { ~p6.multiShiftInput.tglMute }, bank: main, page: page);
~apc.setRecordEnableButtonFunc(3, { ~p6.delNetInput.tglMute }, bank: main, page: page);
~apc.setRecordEnableButtonFunc(4, { ~p6.concatInput.tglMute }, bank: main, page: page);

//////// concat:

//////// Concat SubMixer:

4.do({ | i |
	// vol:
	~apc.setFaderFunc(i+5, { | val | ~p6.concat.subMixer.setVol(i, val.ccdbfs); }, bank: main, page: page);
	// toggle mute:
	~apc.setTrackSelectFunc(i+5, { ~p6.concat.subMixer.tglMute(i); }, bank: main, page: page);
	~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
		if( ~p6.concat.subMixer.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+5) }, { ~apc.turnTrackSelectButtonOn(i+5); });
	}, bank: main, page: page);
});



//// grainEnv buttons:
envReset = {
	~apc.turnGridBlue(4, 4, 1, page: page);
	~apc.turnGridCyan(5, 4, 1, page: page);
	~apc.turnGridYellowGreen(6, 4, 1, page: page);
	~apc.turnGridLightBlue(7, 4, 1, page: page);
	~apc.turnGridYellow(4, 3, 1, page: page);
	~apc.turnGridWhite(5, 3, 1, page: page);
	~apc.turnGridPurple(6, 3, 1, page: page);
};
envReset.value;

~apc.setGridMonitorFunc(\grainEnv, {
	var env = ~prm.granulator.grainEnvelope;
	switch( env,
		'hann', { envReset.value; ~apc.turnGridGreen(4, 4, page: page); },
		'gabor', { envReset.value; ~apc.turnGridGreen(5, 4, page: page); },
		'gabWide', { envReset.value; ~apc.turnGridGreen(6, 4, page: page); },
		'perc', { envReset.value; ~apc.turnGridGreen(7, 4, page: page); },
		'revPerc', { envReset.value; ~apc.turnGridGreen(4, 3, page: page); },
		'expodec', { envReset.value; ~apc.turnGridGreen(5, 3, page: page); },
		'rexpodec', { envReset.value; ~apc.turnGridGreen(6, 3, page: page); });
}, page: page);

~apc.setGridFunc(4, 4, { ~prm.granulator.setGrainEnvelope('hann'); }, page: page);
~apc.setGridFunc(5, 4, { ~prm.granulator.setGrainEnvelope('gabor'); }, page: page);
~apc.setGridFunc(6, 4, { ~prm.granulator.setGrainEnvelope('gabWide'); }, page: page);
~apc.setGridFunc(7, 4, { ~prm.granulator.setGrainEnvelope('perc'); }, page: page);
~apc.setGridFunc(4, 3, { ~prm.granulator.setGrainEnvelope('revPerc'); }, page: page);
~apc.setGridFunc(5, 3, { ~prm.granulator.setGrainEnvelope('expodec'); }, page: page);
~apc.setGridFunc(6, 3, { ~prm.granulator.setGrainEnvelope('rexpodec'); }, page: page);

rateReset = { 4.do({ | i | ~apc.turnGridYellow(i+4, 1, 1, main, page); }); };
rateReset.value;

~apc.setGridMonitorFunc(\granulator, {
	var freeze, sync;
	var rate = ~prm.granulator.rateLow;
	freeze = ~prm.granulator.isFrozen;
	sync = ~prm.granulator.sync;
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
~apc.setGridFunc(4, 2, { ~prm.granulator.toggleFreeze; }, bank: main, page: page);
// toggle sync:
~apc.setGridFunc(5, 2, { ~prm.granulator.toggleSync }, bank: main, page: page);
// normalizes grain size to grainDurLow, turns sync on, sets pans to 0.
~apc.setGridFunc(6, 2, {
	var size = ~prm.granulator.grainDurLow;
	~prm.granulator.setGrainDur(size, size);
	~prm.granulator.turnSyncOn;
	~prm.granulator.setPan(0, 0);
	~apc.turnGridLightBlue(6, 2, 2);
}, bank: main, page: page);
~apc.setGridFunc(6, 2, { ~apc.turnGridLightBlue(6, 2, 1); }, 'noteOff', bank: main, page: page);

//// grain rates:
// 1:
~apc.setGridFunc(4, 1, { ~prm.granulator.setRate(1, 1); }, bank: main, page: page);
// 0.5:
~apc.setGridFunc(5, 1, { ~prm.granulator.setRate(0.5, 0.5); }, bank: main, page: page);
// 0.25:
~apc.setGridFunc(6, 1, { ~prm.granulator.setRate(0.25, 0.25); }, bank: main, page: page);
// 2:
~apc.setGridFunc(7, 1, { ~prm.granulator.setRate(2, 2); }, bank: main, page: page);


////////////////////
//// Encoders: ////
//////////////////

//// Granulator:
~apc.setDeviceEncodersMonitorFunc(\granulator, {
	var grainDurLow, grainDurHigh, trigRate, cutoff;
	var glitch, glitchMod;
	grainDurLow = ~prm.granulator.grainDurLow.explin(0.005, 2.5, 0, 127);
	grainDurHigh = ~prm.granulator.grainDurHigh.explin(0.01, 2.7, 0, 127);
	trigRate = ~prm.granulator.trigRate.linlin(1, 64, 0, 127);
	cutoff = ~prm.granulator.filterCutoff.explin(150, 20000, 0, 127);
	glitch = ~p6.concat.glitchLength.linlin(0.05, 3, 0, 127);
	glitchMod = ~p6.concat.glitchModRange.linlin(0.01, 1, 0, 127);
	~apc.setDeviceEncoderValue(1, grainDurLow, main, page);
	~apc.setDeviceEncoderValue(2, grainDurHigh, main, page);
	~apc.setDeviceEncoderValue(3, trigRate, main, page);
	~apc.setDeviceEncoderValue(4, cutoff, main, page);
	~apc.setDeviceEncoderValue(5, glitch, main, page);
	~apc.setDeviceEncoderValue(6, glitchMod, main, page);
}, main, page);

// grainDur Low:
~apc.setDeviceEncoderFunc(1, { | val |
	var grainDur = val.linexp(0, 127, 0.005, 2.5);
	~prm.granulator.setGrainDurLow(grainDur);
}, main, page);
// grainDur High:
~apc.setDeviceEncoderFunc(2, { | val |
	var grainDur = val.linexp(0, 127, 0.01, 2.7);
	~prm.granulator.setGrainDurHigh(grainDur);
}, main, page);
// trigRate:
~apc.setDeviceEncoderFunc(3, { | val |
	var rate = val.linlin(0, 127, 1, 64);
	~prm.granulator.setTrigRate(rate);
}, main, page);
// cutoff:
~apc.setDeviceEncoderFunc(4, { | val |
	var cutoff = val.linexp(0, 127, 150, 20000);
	~prm.granulator.setFilterCutoff(cutoff);
}, main, page);
// glitch length:
~apc.setDeviceEncoderFunc(5, { | val |
	var length = val.linlin(0, 127, 0.05, 3);
	~p6.concat.setGlitchLength(length);
}, main, page);
~apc.setDeviceEncoderFunc(6, { | val |
	var mod = val.linlin(0, 127, 0.01, 1);
	~p6.concat.setGlitchModRange(mod);
}, main, page); 