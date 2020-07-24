/*
Main Page.scd
Thursday, December 5th 2019
prm
*/

var page = 'effects';

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var reverb = 0;
var granulator = 1;
var modular = 2;
var delay = 3;

var tuningNoteIsPlaying = false;

var loopDelayIsLoaded = false;
var hugeIsLoaded = false;

var envReset;

~apc.makePage(page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.storageDict[\loadMasterFunc].value;

~apc.addMixerEncodersBanks(3, page);
~apc.addDeviceEncodersBanks(3, page);

//~apc.setActiveDeviceEncodersBank(granulator, page);

///////////////////////
////// Grid: ////////
////////////////////

//////// tuning note:
~apc.turnSceneLaunchButtonWhite(4);
~apc.setSceneLaunchFunc(4, {
	if( tuningNoteIsPlaying == false,
		{ ~tuner = { Out.ar(~prm.audioIn, SinOsc.ar(60.midicps) * (-12.dbamp)) }.play; tuningNoteIsPlaying = true },
		{ ~tuner.free; tuningNoteIsPlaying = false; });
}, page: page);

// LoopDelay:
~apc.turnGridWhite(0, 4, page: page);
~apc.setGridFunc(0, 4, {
	if( loopDelayIsLoaded == false,
		{
			r {
				~loopDelay = LoopDelay.new(~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus,
					~prm.modularSend.inBus, ~prm.delay.inBus, ~prm.procGroup, \addToHead);
				while({ try { ~loopDelay.isLoaded } != true }, { 0.001.wait; });
				~loopDelayInput = IM_HardwareIn(~micIn, ~loopDelay.inBus, ~prm.procGroup, \addToHead);
				while({ try { ~loopDelayInput.isLoaded } != true }, { 0.001.wait; });
				~apc.turnGridGreen(1, 4);
				~apc.setGridFunc(1, 4, { ~apc.setPage('loopDelay'); }, page: page);
			}.play
		},
		{
			~loopDelayInput.free;
			~loopDelay.free;
			~apc.setGridFunc(1, 4, { }, page: page);
			~apc.turnGridOff(1, 4);
	});
}, page: page);

// huge:

~apc.turnGridYellow(0, 3, page: page);
~apc.setGridFunc(0, 3, {
	if( hugeIsLoaded == false,
		{
			r{
				~huge = Huge.new(~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus,
					~prm.modularSend.inBus, ~prm.delay.inBus, ~prm.procGroup, \addToHead);
				while({ try { ~huge.isLoaded } != true }, { 0.001.wait; });
				~hugeInput = IM_HardwareIn.new(~pickupIn, ~huge.inBusMono, ~prm.procGroup, \addToHead);
				while({ try { ~hugeInput.isLoaded } != true }, { 0.001.wait; });
				~apc.turnGridGreen(1, 3);
				~apc.setGridFunc(1, 3, { ~apc.setPage('huge') }, page: page);
			}.play;
		},
		{
			~huge.free;
			~hugeInput.free;
			~apc.setGridFunc(1, 3, { }, page: page);
			~apc.turnGridOff(1, 3);
	});
}, page: page);


///////////////////////////////////
////// Convenience Mixer: ////////
/////////////////////////////////

8.do({ | i |
	// vol:
	~apc.setFaderFunc(i+1, { | val | ~prm.cmix.setVol(i, val.ccdbfs); }, page: page);
	// toggle mute:
	~apc.setTrackSelectFunc(i+1, { ~prm.cmix.tglMute(i); }, page: page);
	~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
		if( ~prm.cmix.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
	}, page: page);
	// sends pre/post:
	~apc.setCrossfaderSelectFunc(i+1, {
		if( ~prm.cmix.preOrPost(i) == 'post',
			{ ~prm.cmix.setSendPre(i); }, { ~prm.cmix.setSendPost(i) });
	}, page: page);
	~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
		if( ~prm.cmix.preOrPost(i) == 'post',
			{ ~apc.turnCrossfaderSelectButtonOrange(i+1) },
			{ ~apc.turnCrossfaderSelectButtonYellow(i+1) });
	}, page: page);

	//// sends:
	~apc.setMixerEncoderFunc(i+1, { | val | ~prm.cmix.setSendVol(i, 0, val.ccdbfs); },
		bank: send0, page: page);
	~apc.setMixerEncoderFunc(i+1, { | val | ~prm.cmix.setSendVol(i, 1, val.ccdbfs); },
		bank: send1, page: page);
	~apc.setMixerEncoderFunc(i+1, { | val | ~prm.cmix.setSendVol(i, 2, val.ccdbfs); },
		bank: send2, page: page);
	~apc.setMixerEncoderFunc(i+1, { | val | ~prm.cmix.setSendVol(i, 3, val.ccdbfs); },
		bank: send3, page: page);

	~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
		~apc.setMixerEncoderValue(i+1, ~prm.cmix.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
	~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
		~apc.setMixerEncoderValue(i+1, ~prm.cmix.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
	~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
		~apc.setMixerEncoderValue(i+1, ~prm.cmix.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
	~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
		~apc.setMixerEncoderValue(i+1, ~prm.cmix.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

	//// send bank changes:
	~apc.setDeviceButtonsMonitorFunc(\sends, {
		if( ~apc.activeMixerEncodersBank == send0,
			{ ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1) });
		if( ~apc.activeMixerEncodersBank  == send1,
			{ ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2) });
		if( ~apc.activeMixerEncodersBank  == send2,
			{ ~apc.turnDeviceButtonOn(3) }, { ~apc.turnDeviceButtonOff(3) });
		if( ~apc.activeMixerEncodersBank == send3,
			{ ~apc.turnDeviceButtonOn(4) }, { ~apc.turnDeviceButtonOff(4) });
	}, page: page);

	~apc.setDeviceButtonFunc(1, { ~apc.setActiveMixerEncodersBank(send0); }, page: page);
	~apc.setDeviceButtonFunc(2, { ~apc.setActiveMixerEncodersBank(send1);  }, page: page);
	~apc.setDeviceButtonFunc(3, { ~apc.setActiveMixerEncodersBank(send2); }, page: page);
	~apc.setDeviceButtonFunc(4, { ~apc.setActiveMixerEncodersBank(send3); }, page: page);

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
	var env = ~prm.granulator.grainEnv;
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



//// beauty buttons:

// delay:
~apc.turnGridWhite(4, 1, page: page);
~apc.setGridFunc(4, 1, {
	~prm.beauty.setDelayTimes(rrand(0.001, 0.1), rrand(0.001, 0.1),
		rrand(0.001, 0.1), rrand(0.001, 0.1));
	~apc.turnGridColor(4, 1, 128.rand+1);
}, page: page);

// pans:
~apc.turnGridCyan(5, 1, page: page);
~apc.setGridFunc(5, 1, {
	~prm.beauty.setPans(rrand(-1.0, 1.0), rrand(-1.0, 1.0), rrand(-1.0, 1.0), rrand(-1.0, 1.0));
	~apc.turnGridColor(5, 1, 128.rand+1);
}, page: page);

// averages:
~apc.turnGridPink(6, 1, page: page);
~apc.setGridFunc(6, 1, {
	~prm.beauty.setAverages(10000.rand, 15000.rand);
	~apc.turnGridColor(6, 1, 128.rand+1);
}, page: page);

// smooths:
~apc.turnGridPurple(7, 1, page: page);
~apc.setGridFunc(7, 1, {
	~prm.beauty.setSmooths(500.rand, 500.rand);
	~apc.turnGridColor(7, 1, 128.rand+1);
}, page: page);

// low pass cutoffs:
~apc.turnGridBlue(5, 0, page: page);
~apc.setGridFunc(5, 0, {
	~prm.beauty.setLowPassCutoffs(4000.rand + 200, 4000.rand + 300);
	~apc.turnGridColor(5, 0, 128.rand+1);
}, page: page);

// high pass cutoffs:
~apc.turnGridMagenta(6, 0, page: page);
~apc.setGridFunc(6, 0, {
	~prm.beauty.setHighPassCutoffs(350.rand, 350.rand);
	~apc.turnGridColor(6, 0, 128.rand+1);
}, page: page);

// resonances:
~apc.turnGridYellowGreen(7, 0, page: page);
~apc.setGridFunc(7, 0, {
	~prm.beauty.setResonances(0.5.rand, 0.5.rand);
	~apc.turnGridColor(7, 0, 128.rand+1);
}, page: page);


////////////////////////////////
////// Device Encoders: ///////
//////////////////////////////

///////////////////
// Bank Change: //
/////////////////

//// send bank changes:
~apc.setDeviceButtonsMonitorFunc(\effects, {
	if( ~apc.activeDeviceEncodersBank == reverb,
		{ ~apc.turnDeviceButtonOn(5) }, { ~apc.turnDeviceButtonOff(5) });
	if( ~apc.activeDeviceEncodersBank  == granulator,
		{ ~apc.turnDeviceButtonOn(6) }, { ~apc.turnDeviceButtonOff(6) });
	if( ~apc.activeDeviceEncodersBank  == modular,
		{ ~apc.turnDeviceButtonOn(7) }, { ~apc.turnDeviceButtonOff(7) });
	if( ~apc.activeDeviceEncodersBank == delay,
		{ ~apc.turnDeviceButtonOn(8) }, { ~apc.turnDeviceButtonOff(8) });
}, page: page);

~apc.setDeviceButtonFunc(5, { ~apc.setActiveDeviceEncodersBank(reverb); }, page: page);
~apc.setDeviceButtonFunc(6, { ~apc.setActiveDeviceEncodersBank(granulator);  }, page: page);
~apc.setDeviceButtonFunc(7, { ~apc.setActiveDeviceEncodersBank(modular); }, page: page);
~apc.setDeviceButtonFunc(8, { ~apc.setActiveDeviceEncodersBank(delay); }, page: page);



~apc.setDeviceButtonsMonitorFunc(\sends, {
	if( ~apc.activeMixerEncodersBank == send0,
		{ ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1) });
	if( ~apc.activeMixerEncodersBank  == send1,
		{ ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2) });
	if( ~apc.activeMixerEncodersBank  == send2,
		{ ~apc.turnDeviceButtonOn(3) }, { ~apc.turnDeviceButtonOff(3) });
	if( ~apc.activeMixerEncodersBank == send3,
		{ ~apc.turnDeviceButtonOn(4) }, { ~apc.turnDeviceButtonOff(4) });
}, page: page);

~apc.setDeviceButtonFunc(1, { ~apc.setActiveMixerEncodersBank(send0); }, page: page);
~apc.setDeviceButtonFunc(2, { ~apc.setActiveMixerEncodersBank(send1);  }, page: page);
~apc.setDeviceButtonFunc(3, { ~apc.setActiveMixerEncodersBank(send2); }, page: page);
~apc.setDeviceButtonFunc(4, { ~apc.setActiveMixerEncodersBank(send3); }, page: page);

//// Reverb:
~apc.setDeviceEncodersMonitorFunc(\verbEQ, {
	var postLow, postHigh, preLow, preHigh;
	postLow = ~prm.reverb.postEQ.lowPassCutoff.explin(200, 20000, 0, 127);
	postHigh = ~prm.reverb.postEQ.highPassCutoff.explin(40, 4500, 0, 127);
	preLow = ~prm.reverb.preEQ.lowPassCutoff.explin(200, 20000, 0, 127);
	preHigh = ~prm.reverb.preEQ.highPassCutoff.explin(40, 4500, 0, 127);
	~apc.setDeviceEncoderValue(1, postLow, reverb, page);
	~apc.setDeviceEncoderValue(2, postHigh, reverb, page);
	~apc.setDeviceEncoderValue(3, preLow, reverb, page);
	~apc.setDeviceEncoderValue(4, preHigh, reverb, page);
}, bank: reverb, page: page);
// post EQ Low Pass:
~apc.setDeviceEncoderFunc(1, { | val |
	var cutoff = val.linexp(0, 127, 200, 20000);
	~prm.reverb.postEQ.setLowPassCutoff(cutoff);
}, reverb, page);
// post EQ High Pass:
~apc.setDeviceEncoderFunc(2, { | val |
	var cutoff = val.linexp(0, 127, 40, 4500);
	~prm.reverb.postEQ.setHighPassCutoff(cutoff);
}, reverb, page);
// pre EQ Low Pass:
~apc.setDeviceEncoderFunc(3, { | val |
	var cutoff = val.linexp(0, 127, 200, 20000);
	~prm.reverb.preEQ.setLowPassCutoff(cutoff);
}, reverb, page);
// pre EQ High Pass:
~apc.setDeviceEncoderFunc(4, { | val |
	var cutoff = val.linexp(0, 127, 40, 4500);
	~prm.reverb.preEQ.setHighPassCutoff(cutoff);
}, reverb, page);

//// Granulator:
~apc.setDeviceEncodersMonitorFunc(\granulator, {
	var grainDurLow, grainDurHigh, trigRate, cutoff;
	var mix, delayTime, feedback, pan;
	grainDurLow = ~prm.granulator.granulator.grainDurLow.explin(0.005, 3, 0, 127);
	grainDurHigh = ~prm.granulator.granulator.grainDurHigh.explin(0.01, 3.5, 0, 127);
	trigRate = ~prm.granulator.granulator.trigRate.linlin(1, 64, 0, 127);
	cutoff = ~prm.granulator.granulator.filterCutoff.explin(150, 20000, 0, 127);
	mix = ~prm.granulator.delay.mix.linlin(0, 1, 0, 127);
	delayTime = ~prm.granulator.delay.delayTime.linlin(0.01, 5, 0, 127);
	feedback = ~prm.granulator.delay.feedback.linlin(0, 1, 0, 127);
	pan = ~prm.granulator.granulator.panHigh.linlin(0, 1, 0, 127);
	~apc.setDeviceEncoderValue(1, grainDurLow, granulator, page);
	~apc.setDeviceEncoderValue(2, grainDurHigh, granulator, page);
	~apc.setDeviceEncoderValue(3, trigRate, granulator, page);
	~apc.setDeviceEncoderValue(4, cutoff, granulator, page);
	~apc.setDeviceEncoderValue(5, mix, granulator, page);
	~apc.setDeviceEncoderValue(6, delayTime, granulator, page);
	~apc.setDeviceEncoderValue(7, feedback, granulator, page);
	~apc.setDeviceEncoderValue(8, pan, granulator, page);
}, granulator, page);

// grainDur Low:
~apc.setDeviceEncoderFunc(1, { | val |
	var grainDur = val.linexp(0, 127, 0.005, 3);
	~prm.granulator.setGrainDurLow(grainDur);
}, granulator, page);
// grainDur High:
~apc.setDeviceEncoderFunc(2, { | val |
	var grainDur = val.linexp(0, 127, 0.01, 3.5);
	~prm.granulator.setGrainDurHigh(grainDur);
}, granulator, page);
// trigRate:
~apc.setDeviceEncoderFunc(3, { | val |
	var rate = val.linlin(0, 127, 1, 64);
	~prm.granulator.setTrigRate(rate);
}, granulator, page);
// cutoff:
~apc.setDeviceEncoderFunc(4, { | val |
	var cutoff = val.linexp(0, 127, 150, 20000);
	~prm.granulator.setGranulatorFilterCutoff(cutoff);
}, granulator, page);
// mix:
~apc.setDeviceEncoderFunc(5, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~prm.granulator.delay.setMix(mix);
}, granulator, page);
//delayTime:
~apc.setDeviceEncoderFunc(6, { | val |
	var delay = val.linlin(0, 127, 0.01, 5);
	~prm.granulator.setDelayTime(delay);
}, granulator, page);
//feedback:
~apc.setDeviceEncoderFunc(7, { | val |
	var feedback = val.linlin(0, 127, 0, 1);
	~prm.granulator.setFeedback(feedback);
}, granulator, page);
// pan:
~apc.setDeviceEncoderFunc(8, { | val |
	var panLow = val.linlin(0, 127, 0, -1);
	var panHigh = val.linlin(0, 127, 0, 1);
	~prm.granulator.setPanLow(panLow);
	~prm.granulator.setPanHigh(panHigh);
}, granulator, page);

//// Modular Send:

//// Delay:
~apc.setDeviceEncodersMonitorFunc(\delay, {
	var time, fb;
	time = ~prm.delay.delayTime.explin(0.05, 6, 0, 127);
	fb = ~prm.delay.feedback.linlin(0, 1, 0, 127);
	~apc.setDeviceEncoderValue(1, time, delay, page);
	~apc.setDeviceEncoderValue(2, fb, delay, page);
}, delay, page);
// delay time:
~apc.setDeviceEncoderFunc(1, { | val |
	var time = val.linexp(0, 127, 0.05, 6);
	~prm.delay.setDelayTime(time);
}, delay, page);
// feedback:
~apc.setDeviceEncoderFunc(2, { | val |
	var fb = val.linlin(0, 127, 0, 1);
	~prm.delay.setFeedback(fb);
}, delay, page);

