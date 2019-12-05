/*
Wednesday, December 4th 2019
LoopDelay Page.scd
prm
*/


var page = 'loopDelay';
var sendsBank = 0;

var divResetFunc, posResetFunc, rateResetFunc;

// page creation:
~apc.makePage(page);

// bank creation:
~apc.addMixerEncodersBanks(1, page);

//// master funcs:
~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.storageDict[\loadMasterFunc].value;


//////////////////
//// Mixers: ////
////////////////

//// main mixer:
// vol:
~apc.setFaderFunc(1, { | val |
	~loopDelay.mixer.setVol(val.ccdbfs);
}, page: page);
// mute:
~apc.setTrackSelectFunc(1, { ~loopDelay.mixer.tglMute }, page: page);
// input:
~apc.setRecordEnableButtonFunc(1, { ~loopDelayInput.tglMute }, page: page);
~apc.setMixerMonitorFunc('main', {
	if( ~loopDelay.mixer.isMuted,
		{ ~apc.turnTrackSelectButtonOff(1) }, { ~apc.turnTrackSelectButtonOn(1) });
	if( ~loopDelayInput.isMuted,
		{ ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
}, page: page);

// sends:
~apc.setMixerEncodersMonitorFunc(\sends, {
	var reverb, granulator, modular, delay;
	reverb = ~loopDelay.mixer.sendVol(0);
	granulator = ~loopDelay.mixer.sendVol(1);
	modular = ~loopDelay.mixer.sendVol(2);
	delay = ~loopDelay.mixer.sendVol(3);
	~apc.setMixerEncoderValue(1, reverb.dbfsCC, page: page);
	~apc.setMixerEncoderValue(2, granulator.dbfsCC, page: page);
	~apc.setMixerEncoderValue(3, modular.dbfsCC, page: page);
	~apc.setMixerEncoderValue(4, delay.dbfsCC, page: page);
}, sendsBank, page);
~apc.setMixerEncoderFunc(1, { | val | ~loopDelay.mixer.setSendVol(0, val.ccdbfs) }, sendsBank, page);
~apc.setMixerEncoderFunc(2, { | val | ~loopDelay.mixer.setSendVol(1, val.ccdbfs) }, sendsBank, page);
~apc.setMixerEncoderFunc(3, { | val | ~loopDelay.mixer.setSendVol(2, val.ccdbfs) }, sendsBank, page);
~apc.setMixerEncoderFunc(4, { | val | ~loopDelay.mixer.setSendVol(3, val.ccdbfs) }, sendsBank, page);


//// Dry:
~apc.setFaderFunc(2, { | val |
	~loopDelay.delayNetwork.dry.setVol(val.ccdbfs);
}, page: page);
~apc.setTrackSelectFunc(2, { ~loopDelay.delayNetwork.dry.tglMute }, page: page);
~apc.setMixerMonitorFunc('dryMute', {
	if( ~loopDelay.delayNetwork.dry.isMuted,
		{ ~apc.turnTrackSelectButtonOff(2) }, { ~apc.turnTrackSelectButtonOn(2) });
}, page: page);


//// Delays:
~apc.setFaderFunc(3, { | val |
	~loopDelay.delayNetwork.subMixer.setMasterVol(val.ccdbfs);
}, page: page);


//// Looper:
~apc.setFaderFunc(4, { | val |
	var loopMix = val.linlin(0, 127, -1, 0);
	~loopDelay.looper.setMix(loopMix);
}, page: page);


//////////////////
//// Delays: ////
////////////////

///// Delay Times:
4.do({ | i | ~apc.turnGridBlue(i, 4, page: page); });
// shortest:
~apc.setGridFunc(0, 4, { ~loopDelay.delayNetwork.randomizeDelayTime(0.003, 0.1); }, page: page);
// snappy:
~apc.setGridFunc(1, 4, { ~loopDelay.delayNetwork.randomizeDelayTime(0.05, 0.25); }, page: page);
// medium:
~apc.setGridFunc(2, 4, { ~loopDelay.delayNetwork.randomizeDelayTime(0.5, 3); }, page: page);
// long:
~apc.setGridFunc(3, 4, { ~loopDelay.delayNetwork.randomizeDelayTime(1, 8); }, page: page);

//// Feedback:
4.do({ | i | ~apc.turnGridWhite(i+4, 4, page: page); });
// none:
~apc.setGridFunc(4, 4, { ~loopDelay.delayNetwork.randomizeFeedback(0, 0.1); }, page: page);
// a lil:
~apc.setGridFunc(5, 4, { ~loopDelay.delayNetwork.randomizeFeedback(0.2, 0.55); }, page: page);
// good amount:
~apc.setGridFunc(6, 4, { ~loopDelay.delayNetwork.randomizeFeedback(0.55, 0.8); }, page: page);
// uh oh:
~apc.setGridFunc(7, 4, { ~loopDelay.delayNetwork.randomizeFeedback(0.8, 0.95); }, page: page);

//// Filter Cutoff:
4.do({ | i | ~apc.turnGridCyan(i, 3, page: page); });
// dark:
~apc.setGridFunc(0, 3, { ~loopDelay.delayNetwork.randomizeCutoff(300, 800); }, page: page);
// mix:
~apc.setGridFunc(1, 3, { ~loopDelay.delayNetwork.randomizeCutoff(300, 4500); }, page: page);
// filtered:
~apc.setGridFunc(2, 3, { ~loopDelay.delayNetwork.randomizeCutoff(900, 3500); }, page: page);
// fully open:
~apc.setGridFunc(3, 3, { ~loopDelay.delayNetwork.randomizeCutoff(20000, 20000); }, page: page);

//// Pitch Shift Array:
4.do({  | i | ~apc.turnGridYellow(i+4, 3, page: page); });
// nothin':
~apc.setGridFunc(4, 3, { ~loopDelay.delayNetwork.setPitchShiftArray([0]); }, page: page);
// octs and 5ths:
~apc.setGridFunc(5, 3, {
	~loopDelay.delayNetwork.setPitchShiftArray([0, 0, -12, -12, 12, -5, -5, 7]); }, page: page);
// diatonic cluster:
~apc.setGridFunc(6, 3, {
	~loopDelay.delayNetwork.setPitchShiftArray([-5, -3, -2, 0, 2, 3, 5, 7]); }, page: page);
// microtonal fuck you:
~apc.setGridFunc(4, 3, {
	~loopDelay.delayNetwork.setPitchShiftArray([-0.1, 0, 0.1, -0.5, 0.5, 0.3, 0.25, -0.25]); }, page: page);

//////////////////
//// Looper: ////
////////////////

///// Randomizers:

// randomizer 1:
~apc.turnSceneLaunchButtonWhite(4, page: page);
~apc.setSceneLaunchFunc(4, {
	var rateArray = [1, 0.5, 0.25, 2];
	var divArray = [2, 3, 4, 6, 8];
	var posArray = [0, 0.25, 1/3, 0.5, 2/3, 0.75, 0.9];
	~loopDelay.looper.setLoopRate(rateArray.wchoose([3/4, 1/8, 1/12, 1/24]));
	~loopDelay.looper.setLoopDivison(divArray.wchoose([1/2, 1/4, 1/6, 1/24, 1/24]));
	~loopDelay.looper.setLoopPosition(rrand(0, 0.8));
}, page: page);

//////// Rates:
//rateResetFunc = { 8.do({ | i | ~apc.turnGridPink(i, 0, 1, page: page); });	};
~apc.setGridMonitorFunc(\rates, {
	if( ~loopDelay.looper.rate == 1, { ~apc.turnGridGreen(0, 0) }, { ~apc.turnGridPink(0, 0, 1); });
	if( ~loopDelay.looper.rate == 0.5, { ~apc.turnGridGreen(1, 0) }, { ~apc.turnGridPink(1, 0, 1); });
	if( ~loopDelay.looper.rate == 0.25,
		{ ~apc.turnGridGreen(2, 0) }, { ~apc.turnGridPink(2, 0, 1); });
	if( ~loopDelay.looper.rate == 0.125,
		{ ~apc.turnGridGreen(3, 0) }, { ~apc.turnGridPink(3, 0, 1); });
	if( ~loopDelay.looper.rate == 2, { ~apc.turnGridGreen(4, 0) }, { ~apc.turnGridPink(4, 0, 1); });
	if( ~loopDelay.looper.rate == 0.75,
		{ ~apc.turnGridGreen(5, 0) }, { ~apc.turnGridPink(5, 0, 1); });
	if( ~loopDelay.looper.rate == 1.5, { ~apc.turnGridGreen(6, 0) }, { ~apc.turnGridPink(6, 0, 1); });
	if( ~loopDelay.looper.rate == (2/3),
		{ ~apc.turnGridGreen(7, 0) }, { ~apc.turnGridPink(7, 0, 1); });
}, page: page);
// sets rates to 1, 1/2, 1/4, 1/8:
4.do({ | i | ~apc.setGridFunc(i, 0, { ~loopDelay.looper.setLoopRate(1/(2.pow(i))); }, page: page); });
~apc.setGridFunc(4, 0, { ~loopDelay.looper.setLoopRate(2) }, page: page);
~apc.setGridFunc(5, 0, { ~loopDelay.looper.setLoopRate(0.75) }, page: page);
~apc.setGridFunc(6, 0, { ~loopDelay.looper.setLoopRate(1.5) }, page: page);
~apc.setGridFunc(7, 0, { ~loopDelay.looper.setLoopRate(2/3) }, page: page);

//////// Positions:
//postResetFunc = { 8.do({ | i | ~apc.turnGridPurple(i, 1, 1, page: page); }); };
8.do({ | i |
	~apc.setGridMonitorFunc("pos"++i, {
		if( ~loopDelay.looper.pos == (i/8),
			{ ~apc.turnGridGreen(i, 1) }, { ~apc.turnGridPurple(i, 1, 1); });}, page: page);
});
8.do({ | i | ~apc.setGridFunc(i, 1, { ~loopDelay.looper.setLoopPosition(i/8); }, page: page); });

//////// Divisions:z
//divResetFunc = { 8.do({ | i | ~apc.turnGridMagenta(i, 2, 1, page: page); }); };
8.do({ | i |
	~apc.setGridMonitorFunc("div" ++ i, {
		if( ~loopDelay.looper.div == (2.pow(i)),
			{ ~apc.turnGridGreen(i, 2) }, { ~apc.turnGridMagenta(i, 2, 1); }); }, page: page);	});
8.do({ | i | ~apc.setGridFunc(i, 2,
	{ ~loopDelay.looper.setLoopDivison(2.pow(i)); }, page: page); });

////////////////////////////////////////////////////////////////////////////////////////////////////////
// Remember in the First Version of this code how you forgot to add the actual loop buttons, you idiot.
////////////////////////////////////////////////////////////////////////////////////////////////////////

///// Record Loop:
~apc.setSceneLaunchMonitorFunc(\record, {
	if( ~loopDelay.looper.isRecording, { ~apc.turnSceneLaunchButtonWhite(0) },
		if( ~loopDelay.looper.isPlaying,
			{ ~apc.turnSceneLaunchButtonGreen(0) },
			{ ~apc.turnSceneLaunchButtonBlue(0) });
	);
}, page: page);
~apc.setSceneLaunchFunc(0, { ~loopDelay.looper.loop; }, page: page);
//// Play Loop:
~apc.setSceneLaunchMonitorFunc(\play, {
	if( ~loopDelay.looper.isPlaying,
		{ ~apc.turnSceneLaunchButtonGreen(1) }, { ~apc.turnSceneLaunchButtonRed(1) });
}, page: page);
~apc.setSceneLaunchFunc(1, { ~loopDelay.looper.togglePlayLoop }, page: page);
//// Clear Loop:
~apc.turnSceneLaunchButtonRed(2, page: page);
~apc.setSceneLaunchFunc(2, {
	~loopDelay.looper.setLoopRate(1);
	~loopDelay.looper.setLoopDivison(1);
	~loopDelay.looper.setLoopPosition(0);
	~loopDelay.looper.stopLoop;
	~loopDelay.looper.clearLoop(10);
}, page: page);

