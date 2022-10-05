/*
Monday, April 5th 2021
Plague4APC.scd
prm
*/

var page = 'plague4';

var verb = 0;
var gran = 1;
var mod = 2;
var delay = 3;

var main = 0;

var rateReset, envReset, tremReset;

~apc.makePage(page);
~apc.addMixerEncodersBanks(3, page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);

//////// mixer:

~apc.mapMixer(~p4.mixer, 5, 0, 0, verb, gran, mod, delay, main, page);
~apc.mapSendBanks(\main, verb, gran, mod, delay, main, page);

//////// grid:

tremReset = { 4.do({ | i | ~apc.turnGridLightBlue(i, 4, 1, main, page); }); };

/*
~apc.setGridMonitorFunc(\tremSpeed, {
	var base = 136/60;
	var speed = ~p4.tremolo.frequency;
	switch(speed,
		(base * 2), { tremReset.value; ~apc.turnGridGreen(0, 4) },
		(base * 8/3), { tremReset.value; ~apc.turnGridGreen(1, 4) },
		(base * 3), { tremReset.value; ~apc.turnGridGreen(2, 4) },
		(base * 4),{ tremReset.value; ~apc.turnGridGreen(3, 4) });
}, main, page);
*/
~apc.setGridMonitorFunc(\tremSpeed, {
	var base = 136/60;
	//var speed = ~p4.tremolo.frequency;
	if( ~p4.tremolo.frequency == (base*2), { tremReset.value;  ~apc.turnGridGreen(0, 4) });
	if( ~p4.tremolo.frequency == (base*8/3), { tremReset.value; ~apc.turnGridGreen(1, 4) });
	if( ~p4.tremolo.frequency == (base*3), { tremReset.value; ~apc.turnGridGreen(2, 4) });
	if( ~p4.tremolo.frequency == (base*4), { tremReset.value; ~apc.turnGridGreen(3, 4) });
}, main, page);

~apc.setGridFunc(0, 4, {
	var base = 136/60; ~p4.tremolo.setFrequency(base * 2);
}, bank: main, page: page);
~apc.setGridFunc(1, 4, {
	var base = 136/60; ~p4.tremolo.setFrequency(base * 8/3);
}, bank: main, page: page);
~apc.setGridFunc(2, 4, {
	var base = 136/60; ~p4.tremolo.setFrequency(base * 3);
}, bank: main, page: page);
~apc.setGridFunc(3, 4, {
	var base = 136/60; ~p4.tremolo.setFrequency(base * 4);
}, bank: main, page: page);


~apc.setGridMonitorFunc(\envs, {
	var shard, env, arbhar, lubadh;

	shard = ~p4.shardEnv.gateIsHigh;
	env = ~p4.envelope.isTriggered;
	arbhar = ~p4.arbharEnv.isTriggered;
	lubadh = ~p4.lubadhEnv.gateIsHigh;

	if( shard, { ~apc.turnGridGreen(0, 0) }, { ~apc.turnGridWhite(0, 0) });
	if( env, { ~apc.turnGridGreen(1, 0) }, { ~apc.turnGridWhite(1, 0) });
	if( arbhar, { ~apc.turnGridGreen(2, 0) }, { ~apc.turnGridWhite(2, 0) });
	if( lubadh, { ~apc.turnGridGreen(3, 0) }, { ~apc.turnGridWhite(3, 0) });
}, main, page);

~apc.setGridFunc(0, 0, { ~p4.shardEnv.toggleGate; }, 'noteOn', main, page);
~apc.setGridFunc(1, 0, { if( ~p4.envelope.isTriggered,
	{ ~p4.envelope.release }, { ~p4.envelope.trigger; });
}, 'noteOn', main, page);
~apc.setGridFunc(2, 0, { if( ~p4.arbharEnv.isTriggered,
	{ ~p4.arbharEnv.release }, { ~p4.arbharEnv.trigger; });
}, 'noteOn', main, page);
~apc.setGridFunc(3, 0, { ~p4.lubadhEnv.toggleGate }, 'noteOn', main, page);



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

~apc.setMixerEncodersMonitorFunc(\highPass, {
	var cutoff = ~p4.granEQ.highPassCutoff.explin(20, 4500, 0, 127);
	~apc.setMixerEncoderValue(8, cutoff);
}, main, page);
~apc.setMixerEncoderFunc(8, { | val |
	var cutoff = val.linexp(0, 127, 20, 4500);
	~p4.granEQ.setHighPassCutoff(cutoff);
}, main, page);


//// Granulator:
~apc.setDeviceEncodersMonitorFunc(\granulator, {
	var grainDurLow, grainDurHigh, trigRate, cutoff;
	var level, delayTime, feedback, pan;
	grainDurLow = ~prm.granulator.grainDurLow.explin(0.005, 2.5, 0, 127);
	grainDurHigh = ~prm.granulator.grainDurHigh.explin(0.01, 2.7, 0, 127);
	trigRate = ~prm.granulator.trigRate.linlin(1, 64, 0, 127);
	cutoff = ~prm.granulator.filterCutoff.explin(150, 20000, 0, 127);
	level = ~prm.granulator.delayLevel.linlin(0, 1, 0, 127);
	delayTime = ~prm.granulator.delayTime.linlin(0.01, 5, 0, 127);
	feedback = ~prm.granulator.feedback.linlin(0, 1, 0, 127);
	pan = ~prm.granulator.panHigh.linlin(0, 1, 0, 127);
	~apc.setDeviceEncoderValue(1, grainDurLow, main, page);
	~apc.setDeviceEncoderValue(2, grainDurHigh, main, page);
	~apc.setDeviceEncoderValue(3, trigRate, main, page);
	~apc.setDeviceEncoderValue(4, cutoff, main, page);
	~apc.setDeviceEncoderValue(5, level, main, page);
	~apc.setDeviceEncoderValue(6, delayTime, main, page);
	~apc.setDeviceEncoderValue(7, feedback, main, page);
	~apc.setDeviceEncoderValue(8, pan, main, page);
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
// delay level:
~apc.setDeviceEncoderFunc(5, { | val |
	var level = val.linlin(0, 127, 0, 1);
	~prm.granulator.setDelayLevel(level);
}, main, page);
//delayTime:
~apc.setDeviceEncoderFunc(6, { | val |
	var delay = val.linlin(0, 127, 0.01, 5);
	~prm.granulator.setDelayTime(delay);
}, main, page);
//feedback:
~apc.setDeviceEncoderFunc(7, { | val |
	var feedback = val.linlin(0, 127, 0, 1);
	~prm.granulator.setFeedback(feedback);
}, main, page);
// pan:
~apc.setDeviceEncoderFunc(8, { | val |
	var panLow = val.linlin(0, 127, 0, -1);
	var panHigh = val.linlin(0, 127, 0, 1);
	~prm.granulator.setPanLow(panLow);
	~prm.granulator.setPanHigh(panHigh);
}, main, page);