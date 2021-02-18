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

var shiftResetFunc, shiftVolResetFunc;

var rateReset;

//var activePreset = 0;

~apc.makePage(page);
~apc.addMixerEncodersBanks(3, page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);


//////// mixer:

~apc.mapMixer(~p2.mixer, 6, 0, 0, verb, gran, mod, delay, main, page);
~apc.mapSendBanks(\main, verb, gran, mod, delay, main, page);

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

////// granulator volume:
~apc.setFaderFunc(8, { | val |
	~prm.granulator.mixer.setVol(val.ccdbfs);
}, main, page);

////////////////////
//// Presets: /////
//////////////////

~apc.turnSceneLaunchButtonYellow(0, 2, main, page);
~apc.turnSceneLaunchButtonBlue(1, 2, main, page);
~apc.turnSceneLaunchButtonWhite(2, 2, main, page);

//// song granulator:
~apc.setSceneLaunchFunc(0, { ~p2.setGranulatorPreset(~prm.granulator); ~apc.turnSceneLaunchButtonGreen(0);
}, bank: main, page: page);
//// chorale preset:
~apc.setSceneLaunchFunc(1, { ~p2.setChoralePreset(~prm.reverb); ~apc.turnSceneLaunchButtonGreen(1);
}, bank: main, page: page);
//// normal verb:
~apc.setSceneLaunchFunc(2, { ~prm.reverb.setDecayTime(4); ~apc.turnSceneLaunchButtonGreen(2);
}, bank: main, page: page);



/////////////////
//// Grid: /////
///////////////

shiftResetFunc = { 5.do({ | i | ~apc.turnGridPurple(i, 4, 2, bank: main, page: page); }); };

///// trumpet shifts:
~apc.setGridMonitorFunc(\chordShift, {
	switch(~p2.activeChord,
		1, { shiftResetFunc.value; ~apc.turnGridGreen(0, 4) },
		2, { shiftResetFunc.value; ~apc.turnGridGreen(1, 4) },
		3, { shiftResetFunc.value; ~apc.turnGridGreen(2, 4) },
		'cluster', { shiftResetFunc.value; ~apc.turnGridGreen(3, 4) },
		'minor', { shiftResetFunc.value; ~apc.turnGridGreen(4, 4) });
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
rateReset = { 4.do({ | i | ~apc.turnGridYellow(i, 2, 1, main, page); }); };
rateReset.value;

~apc.setGridMonitorFunc(\granulator, {
	var freeze, sync;
	var rate = ~prm.granulator.rateLow;
	freeze = ~prm.granulator.isFrozen;
	//sync = ~prm.granulator.sync;
	if( freeze == true, { ~apc.turnGridRed(0, 3) }, { ~apc.turnGridWhite(0, 3) });
	//if( sync == 0, { ~apc.turnGridCyan(5, 2) }, { ~apc.turnGridGreen(5, 2) });
	switch( rate,
		1, { rateReset.value; ~apc.turnGridGreen(0, 2, 2); },
		0.5, { rateReset.value; ~apc.turnGridGreen(1, 2, 2); },
		0.25, { rateReset.value; ~apc.turnGridGreen(2, 2, 2); },
		2, { rateReset.value; ~apc.turnGridGreen(3, 2, 2); }
	);
}, page: page);

// granulator freeze:
~apc.setGridFunc(0, 3, { ~prm.granulator.toggleFreeze; }, bank: main, page: page);

//// granulator rates:
// 1:
~apc.setGridFunc(0, 2, { ~prm.granulator.setRate(1, 1); }, bank: main, page: page);
// 0.5:
~apc.setGridFunc(1, 2, { ~prm.granulator.setRate(0.5, 0.5); }, bank: main, page: page);
// 0.25:
~apc.setGridFunc(2, 2, { ~prm.granulator.setRate(0.25, 0.25); }, bank: main, page: page);
// 2:
~apc.setGridFunc(3, 2, { ~prm.granulator.setRate(2, 2); }, bank: main, page: page);

///// arbhar gate signal:
~apc.setGridMonitorFunc(\arbGate, {
	if( ~p2.arbGate.gateIsHigh, { ~apc.turnGridGreen(0, 0) }, { ~apc.turnGridLightBlue(0, 0); });
}, bank: main, page: page);
~apc.setGridFunc(0, 0, { ~p2.arbGate.toggleGate; }, bank: main, page: page);


//////// shifter pre vol:
shiftVolResetFunc = { 4.do({ | i | ~apc.turnGridBlue(i+4, 0, 1, main, page); }); };
shiftVolResetFunc.value;

~apc.setGridMonitorFunc(\shiftPreVol, {
	var vol = ~p2.tptEQ.mixer.vol;
	switch(vol,
		-3, { shiftVolResetFunc.value; ~apc.turnGridGreen(4, 0, 2); },
		0, { shiftVolResetFunc.value; ~apc.turnGridGreen(5, 0, 2); },
		3, { shiftVolResetFunc.value; ~apc.turnGridGreen(6, 0, 2); },
		6, { shiftVolResetFunc.value; ~apc.turnGridGreen(7, 0, 2); });
}, main, page);

~apc.setGridFunc(4, 0, { ~p2.tptEQ.mixer.setVol(-3); }, bank: main, page: page);
~apc.setGridFunc(5, 0, { ~p2.tptEQ.mixer.setVol(0); }, bank: main, page: page);
~apc.setGridFunc(6, 0, { ~p2.tptEQ.mixer.setVol(3); }, bank: main, page: page);
~apc.setGridFunc(7, 0, { ~p2.tptEQ.mixer.setVol(6); }, bank: main, page: page);

////////////////////////////
//// Device Encoders: /////
//////////////////////////

~apc.setDeviceEncodersMonitorFunc(\main, {
	var subCutoff = ~p2.subtractive.filterCutoff.explin(150, 20000, 0, 127);
	var granCutoff = ~prm.granulator.filterCutoff.explin(150, 20000, 0, 127);
	var durLow = ~prm.granulator.grainDurLow.linlin(0.005, 2.5, 0, 127);
	var durHigh = ~prm.granulator.grainDurHigh.linlin(0.01, 2.75, 0, 127);
	var delayLevel = ~prm.granulator.delayLevel.linlin(0, 1, 0, 127);
	var feedback = ~prm.granulator.feedback.linlin(0, 1, 0, 127);
	var plaitsVol = ~p2.plaitsVol.value.linlin(0, 0.5, 0, 127);
	var delayMix = ~p2.delayMix.value.linlin(0, 0.5, 0, 127);

	~apc.setDeviceEncoderValue(1, subCutoff);
	~apc.setDeviceEncoderValue(2, granCutoff);
	~apc.setDeviceEncoderValue(3, durLow);
	~apc.setDeviceEncoderValue(4, durHigh);
	~apc.setDeviceEncoderValue(5, delayLevel);
	~apc.setDeviceEncoderValue(6, feedback);
	~apc.setDeviceEncoderValue(7, plaitsVol);
	~apc.setDeviceEncoderValue(8, delayMix);

}, main, page);

// subtractive cutoff:
~apc.setDeviceEncoderFunc(1, { | val |
	var cutoff = val.linexp(0, 127, 150, 20000);
	~p2.subtractive.setFilterCutoff(cutoff);
}, main, page);

// granulator cutoff:
~apc.setDeviceEncoderFunc(2, { | val |
	var cutoff = val.linexp(0, 127, 150, 20000);
	~prm.granulator.setFilterCutoff(cutoff);
}, main, page);

// grain dur low:
~apc.setDeviceEncoderFunc(3, { | val |
	var dur = val.linlin(0, 127, 0.005, 2.5);
	~prm.granulator.setGrainDurLow(dur);
}, main, page);

// grain dur high:
~apc.setDeviceEncoderFunc(4, { | val |
	var dur = val.linlin(0, 127, 0.01, 2.75);
	~prm.granulator.setGrainDurHigh(dur);
}, main, page);

// delay level:
~apc.setDeviceEncoderFunc(5, { | val |
	var level = val.linlin(0, 127, 0, 1);
	~prm.granulator.setDelayLevel(level);
}, main, page);

// delay feedback:
~apc.setDeviceEncoderFunc(6, { | val |
	var feedback = val.linlin(0, 127, 0, 1);
	~prm.granulator.setFeedback(feedback);
}, main, page);

// plaits vol CV constant:
~apc.setDeviceEncoderFunc(7, { | val |
	var cv = val.linlin(0, 127, 0, 0.5);
	~p2.plaitsVol.setValue(cv);
}, main, page);

// delay mix CV constant:
~apc.setDeviceEncoderFunc(8, { | val |
	var cv = val.linlin(0, 127, 0, 0.5);
	~p2.delayMix.setValue(cv);
}, main, page);




	