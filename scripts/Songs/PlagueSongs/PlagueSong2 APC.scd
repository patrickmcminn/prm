/*
Saturday, February 6th 2021
PlagueSong2 APC.scd
prm
*/

var page = 'plague2';

var verb = 0;
var gran = 1;
var mod = 2;
var delay = 3;

var main = 0;

var shiftResetFunc;

var rateReset;

//var activePreset = 0;

~apc.makePage(page);
~apc.addMixerEncodersBanks(3, page);


//////// mixer:

~apc.mapMixer(~p2.mixer, 6, 0, 0, verb, gran, mod, delay, main, page);

/////// record enables:
~apc.setDeviceEncodersMonitorFunc(\record, {
	var tpt, shift;
	tpt = ~p2.trumpet.isMuted;
	shift = ~p2.tptInput.isMuted;
	if( tpt == true, { ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
	if( shift == true, { ~apc.turnRecordEnableButtonOff(2) }, { ~apc.turnRecordEnableButtonOn(2) });
}, bank: main, page: page);

// trumpet:
~apc.setRecordEnableButtonFunc(1, { ~p2.trumpet.tglMute }, bank: main, page: page);
// shift:
~apc.setRecordEnableButtonFunc(2, { ~p2.tptInput.tglMute }, bank: main, page: page);

////////////////////
//// Presets: /////
//////////////////

//// song granulator:
~apc.setSceneLaunchFunc(0, { ~p2.setGranulatorPreset; ~apc.turnSceneLaunchButtonGreen(0);
}, bank: main, page: page);
//// chorale preset:
~apc.setSceneLaunchFunc(1, { ~p2.setChoralePreset; ~apc.turnSceneLaunchButtonGreen(1);
}, bank: main, page: page);


/////////////////
//// Grid: /////
///////////////

shiftResetFunc = { 5.do({ | i | ~apc.turnGridPurple(i, 4, 2, bank: main, page: page); }); };


///// trumpet shifts:
~apc.setGridMonitorFunc(\chordShift, {
	switch(~p2.activeChord,
		1, { shiftResetFunc.value; ~apc.turnGridGreen(0, 4) },
		2, { shiftResetFunv.value; ~apc.turnGridGreen(1, 4) },
		3, { shiftResetFunv.value; ~apc.turnGridGreen(2, 4) },
		'cluster', { shiftResetFunv.value; ~apc.turnGridGreen(3, 4) },
		'minor', { shiftResetFunv.value; ~apc.turnGridGreen(4, 4) });
}, bank: main, page: page);

// chord 1:
~apc.setGridFunc(0, 4, { ~p2.setChord1Shift; }, bank: main, page: page);
// chord 2:
~apc.setGridFunc(1, 4, { ~p2.setChord2Shift; }, bank: main, page: page);
// chord 3:
~apc.setGridFunc(2, 4, { ~p2.setChord3Shift; }, bank: main, page: page);
// chord cluster:
~apc.setGridFunc(3, 4, { ~p2.setClusterShift; }, bank: main, page: page);
//  minor chord:
~apc.setGridFunc(4, 4, { ~p2.setMinorChordShift; }, bank: main, page: page);

///// granulator:
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

	