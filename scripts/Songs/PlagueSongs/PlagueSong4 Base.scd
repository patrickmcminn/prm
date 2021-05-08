/*
Tuesday April 6th 2021
PlagueSong4 Base.scd
prm
*/

var page = 'plague4';

var main = 0;

~base.makePage(page);

5.do({ | i | ~base.setFaderMode(i+1, 'whiteFill', main, page); });

//// volumes:

// volume monitoring:
5.do({ | chan |
	~base.setFaderMonitorFunc(chan+1, {
		var volume = ~p4.mixer.vol(chan).dbfsCC;
		~base.setFaderValue(chan+1, volume, main, page);
	}, main, page);
});

// volume:
5.do({ | chan |
	~base.setFaderFunc(chan+1, { | val |
		~p4.mixer.setVol(chan, val.ccdbfs);
	}, main, page);
});

/////// GRID:
~base.turnAllGridCyan(main, page);

// envs:
~base.setGridMonitorFunc(0, 0, {
	var shard, env, arbhar, lubadh;

	shard = ~p4.shardEnv.gateIsHigh;
	env = ~p4.envelope.isTriggered;
	arbhar = ~p4.arbharEnv.isTriggered;
	lubadh = ~p4.lubadhEnv.gateIsHigh;

	if( shard, { ~base.turnGridGreen(0, 0) }, { ~base.turnGridWhite(0, 0) });
	if( env, { ~base.turnGridGreen(1, 0) }, { ~base.turnGridWhite(1, 0) });
	if( arbhar, { ~base.turnGridGreen(2, 0) }, { ~base.turnGridWhite(2, 0) });
	if( lubadh, { ~base.turnGridGreen(3, 0) }, { ~base.turnGridWhite(3, 0) });
}, main, page);

// shard env:
~base.setGridFunc(0, 0, { if( ~p4.shardEnv.gateIsHigh == false, { ~p4.shardEnv.makeGate; });}, 'noteOn', main, page);
~base.setGridFunc(0, 0, { ~p4.shardEnv.releaseGate; }, 'noteOff', main, page);

// env 2:
~base.setGridFunc(1, 0, { ~p4.envelope.trigger; }, 'noteOn', main, page);
~base.setGridFunc(1, 0, { ~p4.envelope.release; }, 'noteOff', main, page);

// arbhar env:
~base.setGridFunc(2, 0, { ~p4.arbharEnv.trigger; }, 'noteOn', main, page);
~base.setGridFunc(2, 0, { ~p4.arbharEnv.release; }, 'noteOff', main, page);

// lubadh env:
~base.setGridFunc(3, 0, { if( ~p4.lubadhEnv.gateIsHigh == false, { ~p4.lubadhEnv.makeGate; })}, 'noteOn', main, page);
~base.setGridFunc(3, 0, { ~p4.lubadhEnv.releaseGate; }, 'noteOff', main, page); 