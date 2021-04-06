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

~apc.makePage(page);
~apc.addMixerEncodersBanks(3, page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);

//////// mixer:

~apc.mapMixer(~p4.mixer, 6, 0, 0, verb, gran, mod, delay, main, page);
~apc.mapSendBanks(\main, verb, gran, mod, delay, main, page);

//////// grid:

~apc.setGridMonitorFunc(\envs, {
	var shard, env, arbhar, lubadh;

	shard = ~p4.shardEnv.isTriggered;
	env = ~p4.envelope.isTriggered;
	arbhar = ~p4.arbharEnv.isTriggered;
	lubadh = ~p4.lubadhEnv.isTriggered;

	if( shard, { ~apc.turnGridGreen(0, 0) }, { ~apc.turnGridWhite(0, 0) });
	if( env, { ~apc.turnGridGreen(1, 0) }, { ~apc.turnGridWhite(1, 0) });
	if( arbhar, { ~apc.turnGridGreen(2, 0) }, { ~apc.turnGridWhite(2, 0) });
	if( lubadh, { ~apc.turnGridGreen(3, 0) }, { ~apc.turnGridWhite(3, 0) });
}, main, page);

~apc.setGridFunc(0, 0, { if( ~p4.shardEnv.isTriggered,
	{ ~p4.shardEnv.release }, { ~p4.shardEnv.trigger; });
}, 'noteOn', main, page);
~apc.setGridFunc(1, 0, { if( ~p4.envelope.isTriggered,
	{ ~p4.envelope.release }, { ~p4.envelope.trigger; });
}, 'noteOn', main, page);
~apc.setGridFunc(2, 0, { if( ~p4.arbharEnv.isTriggered,
	{ ~p4.arbharEnv.release }, { ~p4.arbharEnv.trigger; });
}, 'noteOn', main, page);
~apc.setGridFunc(3, 0, { if( ~p4.lubadhEnv.isTriggered,
	{ ~p4.lubadhEnv.release }, { ~p4.lubadhEnv.trigger; });
}, 'noteOn', main, page);
	