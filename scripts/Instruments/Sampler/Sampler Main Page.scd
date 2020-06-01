/*
Thursday, May 14th 2020
Sampler Main Page.scd
prm
*/

var page = 'sampler';
var main = 0;
var activeSlot = nil;

/*
var slot1Bank = 1;
var slot2Bank = 2;
var slot3Bank = 3;
var slot4Bank = 4;
var slot5Bank = 5;
var slot6Bank = 6;
var slot7Bank = 7;
var slot8Bank = 8;
var slot9Bank = 9;
var slot10Bank = 10;
var slot11Bank = 11;
var slot12Bank = 12;
var slot13Bank = 13;
var slot14Bank = 14;
var slot15Bank = 15;
var slot16Bank = 16;
*/

var load = 1;
var name = 2;
var copy = 3;
var paste = 4;
var loadPreset = 5;
var writePreset = 6;

var activeBank = main;

var editResetFunc, loadResetFunc, nameResetFunc, recordResetFunc;

var path, colorReset, loader;
var addPreset, makeGUI, populateWriteFuncs, presetLoad;


~apc.makePage(page);
//~apc.addMixerEncodersBanks(16, page);
//~apc.addDeviceEncodersBanks(16, page);
~apc.addGridBanks(7, page);

//// master funcs:
~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.storageDict[\loadMasterFunc].value;

//////// change banks:
~apc.setDeviceButtonsMonitorFunc(\banks, {
	if( activeBank == main, { ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1); });
	if( activeBank == load, { ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2); });
	if( activeBank == name, { ~apc.turnDeviceButtonOn(3) }, { ~apc.turnDeviceButtonOff(3); });

	if( activeBank == copy, { ~apc.turnDeviceButtonOn(5) }, { ~apc.turnDeviceButtonOff(5) });

	if( activeBank == loadPreset, { ~apc.turnDeviceButtonOn(7) }, { ~apc.turnDeviceButtonOff(7); });
	if( activeBank == writePreset, { ~apc.turnDeviceButtonOn(8) }, { ~apc.turnDeviceButtonOff(8); });
}, main, page);

~apc.setDeviceButtonFunc(1, { activeBank = main; ~apc.setActiveGridBank(activeBank); }, bank: main, page: page);
~apc.setDeviceButtonFunc(2, { activeBank = load; ~apc.setActiveGridBank(activeBank); }, bank: main, page: page);
~apc.setDeviceButtonFunc(3, { activeBank = name; ~apc.setActiveGridBank(activeBank); }, bank: main, page: page);

~apc.setDeviceButtonFunc(5, { activeBank = copy; ~apc.setActiveGridBank(activeBank); }, bank: main, page: page);
~apc.setDeviceButtonFunc(6, { activeBank = paste; ~apc.setActiveGridBank(activeBank) }, bank: main, page: page);

~apc.setDeviceButtonFunc(7, { activeBank = loadPreset; ~apc.setActiveGridBank(activeBank); }, bank: main, page: page);
~apc.setDeviceButtonFunc(8, {activeBank = writePreset; ~apc.setActiveGridBank(activeBank); }, bank: main, page: page);

///////// MIDI:

~apc.setSceneLaunchMonitorFunc(4, {
	if( ~prm.sampler.midiLoaded == true,
		{ ~apc.turnSceneLaunchButtonGreen(4); },
		{ ~apc.turnSceneLaunchButtonPurple(4); })
}, main, page);
~apc.setSceneLaunchFunc(4, {
	if( ~prm.sampler.midiLoaded == true, { ~prm.sampler.freeMIDIFuncs }, { ~prm.sampler.makeMIDIFuncs(~pyr, 13); });
}, 'noteOn', main, page);

/////////////////////////
///// COPY + PASTE: ////
///////////////////////

4.do({ | column | 4.do({ | row | ~apc.turnGridYellow(column+4, row+1, bank: copy, page: page); }); });
4.do({ | column | 4.do({ | row | ~apc.turnGridYellowGreen(column+4, row+1, bank: paste, page: page); }); });

16.do({ | slot |
	~apc.setGridFunc((slot%4)+4, ((slot/4).asInt+1), {
		~prm.sampler.copySlot(slot);
		~apc.turnGridGreen((slot%4)+4, ((slot/4).asInt+1));
	}, 'noteOn', copy, page);
	~apc.setGridFunc((slot%4)+4, ((slot/4).asInt+1), {
		//~prm.sampler.copySlot(slot);
		~apc.turnGridYellow((slot%4)+4, ((slot/4).asInt+1));
	}, 'noteOff', copy, page);
});

16.do({ | slot |
	~apc.setGridFunc((slot%4)+4, ((slot/4).asInt+1), {
		~prm.sampler.pasteSlot(slot);
		~apc.turnGridGreen((slot%4)+4, ((slot/4).asInt+1));
	}, 'noteOn', paste, page);
	~apc.setGridFunc((slot%4)+4, ((slot/4).asInt+1), {
		//~prm.sampler.copySlot(slot);
		~apc.turnGridYellowGreen((slot%4)+4, ((slot/4).asInt+1));
	}, 'noteOff', paste, page);
});


////////////////////////
//// Main Grid: ///////
//////////////////////


5.do({ | i |
	4.do({ | column | 4.do({ | row | ~apc.turnGridMagenta(column, row+1, bank: i, page: page); }); });
});

6.do({ | bank |
	/////// Play the Sampler:
	4.do({ | i |
		~apc.setGridFunc(i, 1, { ~prm.sampler.playSample(i, -3); ~apc.turnGridGreen(i, 1); }, 'noteOn', bank, page);
		~apc.setGridFunc(i, 1, { ~prm.sampler.releaseSample(i); ~apc.turnGridMagenta(i, 1);  }, 'noteOff', bank, page);
	});
	4.do({ | i |
		~apc.setGridFunc(i, 2, { ~prm.sampler.playSample(i+4, -3); ~apc.turnGridGreen(i, 2);}, 'noteOn', bank, page);
		~apc.setGridFunc(i, 2, { ~prm.sampler.releaseSample(i+4); ~apc.turnGridMagenta(i, 2); }, 'noteOff', bank, page);
	});
	4.do({ | i |
		~apc.setGridFunc(i, 3, { ~prm.sampler.playSample(i+8, -3); ~apc.turnGridGreen(i, 3);}, 'noteOn', bank, page);
		~apc.setGridFunc(i, 3, { ~prm.sampler.releaseSample(i+8); ~apc.turnGridMagenta(i, 3); }, 'noteOff', bank, page);
	});
	4.do({ | i |
		~apc.setGridFunc(i, 4, { ~prm.sampler.playSample(i+12, -3); ~apc.turnGridGreen(i, 4);}, 'noteOn', bank, page);
		~apc.setGridFunc(i, 4, { ~prm.sampler.releaseSample(i+12); ~apc.turnGridMagenta(i, 4); }, 'noteOff', bank, page);
	})
});

~apc.setSceneLaunchMonitorFunc(\record, {
	if( ~prm.sampler.isRecording == false,
		{ ~apc.turnSceneLaunchButtonYellow(1) }, { ~apc.turnSceneLaunchButtonRed(1) });
}, main, page);
~apc.setSceneLaunchFunc(1, { if( activeSlot.notNil, {
	if( ~prm.sampler.isRecording == false,
		{ ~prm.sampler.recordSampleStereo; },
		{ activeSlot.postln; ~prm.sampler.stopRecordingLoadSlot(activeSlot); });
});
}, 'noteOn', main, page);


////////////////////////////
/////// Edit Sample: //////
//////////////////////////

///// choose sample to edit:
editResetFunc = {
	4.do({ | column | 4.do({ | row | ~apc.turnGridBlue(column+4, row+1, bank: main, page: page); }); });
	~apc.turnSceneLaunchButtonBlue(0, page: page);
};

editResetFunc.value;

~apc.setGridMonitorFunc(\edit, {
	if(activeSlot.isInteger,
		{  ~apc.turnGridGreen((activeSlot%4)+4, (activeSlot/4).asInt+1); },
		{ ~apc.turnSceneLaunchButtonGreen(0) }
	);
}, main, page);

~apc.setSceneLaunchFunc(0, {
	activeSlot = nil;
	//~apc.setActiveDeviceEncodersBank(main);
	//~apc.setActiveMixerEncodersBank(main);
	editResetFunc.value;
}, bank: main, page: page);

4.do({ | i |
	~apc.setGridFunc(i+4, 1, {
		activeSlot = i;
		//	~apc.setActiveDeviceEncodersBank(i+1);
		//~apc.setActiveMixerEncodersBank(i+1);
		editResetFunc.value;
	}, bank: main, page: page);
});
4.do({ | i |
	~apc.setGridFunc(i+4, 2, {
		activeSlot = i+4;
		//	~apc.setActiveDeviceEncodersBank(i+5);
		//~apc.setActiveMixerEncodersBank(i+5);
		editResetFunc.value;
	}, bank: main, page: page);
});
4.do({ | i |
	~apc.setGridFunc(i+4, 3, {
		activeSlot = i+8;
		//	~apc.setActiveDeviceEncodersBank(i+9);
		//~apc.setActiveMixerEncodersBank(i+9);
		editResetFunc.value;
	}, bank: main, page: page);
});
4.do({ | i |
	~apc.setGridFunc(i+4, 4, {
		activeSlot = i+12;
		//~apc.setActiveDeviceEncodersBank(i+13);
		//~apc.setActiveMixerEncodersBank(i+13);
		editResetFunc.value;
	}, bank: main, page: page);
});


////////////////////////////
/////// Load Sample: //////
//////////////////////////

loadResetFunc = 4.do({ | column | 4.do({ | row | ~apc.turnGridLightBlue(column+4, row+1, bank: load, page: page); }); });

~apc.turnPanButtonOn(main, page);
~apc.setPanButtonFunc({ ~prm.sampler.loadSampleGridGUI; }, 'noteOn', main, page);

~apc.setGridMonitorFunc(\load, {
	16.do({ | slot |
		if( ~prm.sampler.samplePath(slot) == nil,
			{ ~apc.turnGridLightBlue((slot%4)+4, ((slot/4).asInt+1)); },
			{ ~apc.turnGridGreen((slot%4)+4, ((slot/4).asInt+1)); });
	});
}, load, page);

4.do({ | i |
	~apc.setGridFunc(i+4, 1, {
		~prm.sampler.loadSample(i);
		~apc.turnGridGreen(i+4, 1);
	}, 'noteOn', load, page);
	~apc.setGridFunc(i+4, 1, {
		~apc.turnGridLightBlue(i+4, 1);
	}, 'noteOff', load, page);
});
4.do({ | i |
	~apc.setGridFunc(i+4, 2, {
		~prm.sampler.loadSample(i+4);
		~apc.turnGridGreen(i+4, 2);
	}, 'noteOn', load, page);
	~apc.setGridFunc(i+4, 2, {
		~apc.turnGridLightBlue(i+4, 2);
	}, 'noteOff', load, page);
});
4.do({ | i |
	~apc.setGridFunc(i+4, 3, {
		~prm.sampler.loadSample(i+8);
		~apc.turnGridGreen(i+4, 3);
	}, 'noteOn', load, page);
	~apc.setGridFunc(i+4, 3, {
		~apc.turnGridLightBlue(i+4, 3);
	}, 'noteOff', load, page);
});
4.do({ | i |
	~apc.setGridFunc(i+4, 4, {
		~prm.sampler.loadSample(i+12);
		~apc.turnGridGreen(i+4, 4);
	}, 'noteOn', load, page);
	~apc.setGridFunc(i+4, 4, {
		~apc.turnGridLightBlue(i+4, 4);
	}, 'noteOff', load, page);
});

////////////////////////////
/////// Name Sample: //////
//////////////////////////

nameResetFunc = 4.do({ | column | 4.do({ | row | ~apc.turnGridCyan(column+4, row+1, bank: name, page: page); }); });
nameResetFunc.value;

16.do({ | slot |
	//~apc.turnGridLightBlue((slot%4)+4, 4-((slot/4).asInt))
	~apc.setGridFunc((slot%4)+4, ((slot/4).asInt+1), {
		~prm.sampler.setSampleNameGUI(slot);
		~apc.turnGridGreen((slot%4)+4, ((slot/4).asInt+1));
	}, 'noteOn', name, page);
	~apc.setGridFunc((slot%4)+4, ((slot/4).asInt+1), {
		~apc.turnGridCyan((slot%4)+4, ((slot/4).asInt+1));
	}, 'noteOff', name, page);
});


////// grid parameter edits:


//////// Pos GUI:
~apc.turnSceneLaunchButtonWhite(3, bank: main, page: page);
~apc.setSceneLaunchFunc(3, {
	if(activeSlot.isInteger, { ~prm.sampler.setPosGUI(activeSlot); });
	activeSlot.postln;
	~apc.turnSceneLaunchButtonGreen(3);
}, 'noteOn', main, page);
~apc.setSceneLaunchFunc(3, {
	~apc.turnSceneLaunchButtonWhite(3);
}, 'noteOff', main, page);

///////// play mode:
3.do({ | i | ~apc.turnGridLightBlue(i, 0, bank: main, page: page); });
//////// Play Mode:
~apc.setGridMonitorFunc(\playMode, {
	if( activeSlot.isInteger, {
		if( ~prm.sampler.playMode(activeSlot) == 'oneShot',
			{ ~apc.turnGridGreen(0, 0); }, { ~apc.turnGridLightBlue(0, 0) });
		if( ~prm.sampler.playMode(activeSlot) == 'sustaining',
			{ ~apc.turnGridGreen(1, 0); }, { ~apc.turnGridLightBlue(1, 0) });
		if( ~prm.sampler.playMode(activeSlot) == 'granular',
			{ ~apc.turnGridGreen(2, 0); }, { ~apc.turnGridLightBlue(2, 0) });
	}, { 3.do({ | i | ~apc.turnGridLightBlue(i, 0, bank: main, page: page); }); });
}, main, page);
~apc.setGridFunc(0, 0, { if( activeSlot.isInteger, { ~prm.sampler.setPlayMode(activeSlot, 'oneShot') });
}, bank: main, page: page);
~apc.setGridFunc(1, 0, { if( activeSlot.isInteger, { ~prm.sampler.setPlayMode(activeSlot, 'sustaining') });
}, bank: main, page: page);
~apc.setGridFunc(2, 0, { if( activeSlot.isInteger, { ~prm.sampler.setPlayMode(activeSlot, 'granular') });
}, bank: main, page: page);

//////// rate:
5.do({ | i | ~apc.turnGridCyan(i+3, 0, bank: main, page: page); });
~apc.setGridMonitorFunc(\rate, {
	if( activeSlot.isInteger, {
		if( ~prm.sampler.rate(activeSlot) == 0.125,
			{ ~apc.turnGridGreen(3, 0) }, { ~apc.turnGridCyan(3, 0) });
		if( ~prm.sampler.rate(activeSlot) == 0.25,
			{ ~apc.turnGridGreen(4, 0) }, { ~apc.turnGridCyan(4, 0) });
		if( ~prm.sampler.rate(activeSlot) == 0.5,
			{ ~apc.turnGridGreen(5, 0) }, { ~apc.turnGridCyan(5, 0) });
		if( ~prm.sampler.rate(activeSlot) == 1,
			{ ~apc.turnGridGreen(6, 0) }, { ~apc.turnGridCyan(6, 0) });
		if( ~prm.sampler.rate(activeSlot) == 2,
			{ ~apc.turnGridGreen(7, 0) }, { ~apc.turnGridCyan(7, 0) });
	}, { 5.do({ | i | ~apc.turnGridCyan(i+3, 0, bank: main, page: page); }); });
}, main, page);
~apc.setGridFunc(3, 0, { ~prm.sampler.setRate(activeSlot, 0.125) }, bank: main, page: page);
~apc.setGridFunc(4, 0, { ~prm.sampler.setRate(activeSlot, 0.25) }, bank: main, page: page);
~apc.setGridFunc(5, 0, { ~prm.sampler.setRate(activeSlot, 0.5) }, bank: main, page: page);
~apc.setGridFunc(6, 0, { ~prm.sampler.setRate(activeSlot, 1) }, bank: main, page: page);
~apc.setGridFunc(7, 0, { ~prm.sampler.setRate(activeSlot, 2) }, bank: main, page: page);


~apc.setMixerEncodersMonitorFunc(\parameters, {
	var  volume, pan, startPos, endPos;
	~apc.setMixerEncoderValue(1, ~prm.cmix.sendVol(6, 0).dbfsCC);
	~apc.setMixerEncoderValue(2, ~prm.cmix.sendVol(6, 1).dbfsCC);
	~apc.setMixerEncoderValue(3, ~prm.cmix.sendVol(6, 2).dbfsCC);
	~apc.setMixerEncoderValue(4, ~prm.cmix.sendVol(6, 3).dbfsCC);
	if( activeSlot.notNil, {
		volume = ~prm.sampler.sampleVol(activeSlot).linlin(-12, 18, 0, 127);
		pan = ~prm.sampler.samplePan(activeSlot).linlin(-1, 1, 0, 127);
		startPos = ~prm.sampler.startPos(activeSlot).linlin(0, 1, 0, 127);
		endPos = ~prm.sampler.endPos(activeSlot).linlin(0, 1, 0, 127);
		~apc.setMixerEncoderValue(5, volume);
		~apc.setMixerEncoderValue(6, pan);
		~apc.setMixerEncoderValue(7, startPos);
		~apc.setMixerEncoderValue(8, endPos);
	}, { 4.do({ | i | ~apc.setMixerEncoderValue(i+5, 0); }); });
}, main, page);

~apc.setMixerEncoderFunc(1, { | val |
	~prm.cmix.setSendVol(6, 0, val.ccdbfs);
}, main, page);
~apc.setMixerEncoderFunc(2, { | val |
	~prm.cmix.setSendVol(6, 1, val.ccdbfs);
}, main, page);
~apc.setMixerEncoderFunc(3, { | val |
	~prm.cmix.setSendVol(6, 2, val.ccdbfs);
}, main, page);
~apc.setMixerEncoderFunc(4, { | val |
	~prm.cmix.setSendVol(6, 3, val.ccdbfs);
}, main, page);

~apc.setMixerEncoderFunc(5, { | val |
	var volume = val.linlin(0, 127, -12, 18);
	if( activeSlot.notNil, { ~prm.sampler.setSampleVol(activeSlot, volume); volume.postln; });
}, main, page);
~apc.setMixerEncoderFunc(6, { | val |
	var pan = val.linlin(0, 127, -1, 1);
	if( activeSlot.notNil, { ~prm.sampler.setSamplePan(activeSlot, pan); });
}, main, page);
~apc.setMixerEncoderFunc(7, { | val |
	var startPos = val.linlin(0, 127, 0, 1);
	if( activeSlot.notNil, { ~prm.sampler.setStartPos(activeSlot, startPos); });
}, main, page);
~apc.setMixerEncoderFunc(8, { | val |
	var endPos = val.linlin(0, 127, 0, 1);
	if( activeSlot.notNil, { ~prm.sampler.setEndPos(activeSlot, endPos); });
}, main, page);

~apc.setDeviceEncodersMonitorFunc(\parameters, {
	var lowPass, highPass, rate, trig;
	var durLow, durHigh, panLow, panHigh;
	if (activeSlot.notNil, {
		lowPass = ~prm.sampler.lowPassCutoff(activeSlot).explin(100, 20000, 0, 127);
		highPass = ~prm.sampler.highPassCutoff(activeSlot).explin(10, 6000, 0, 127);
		rate = ~prm.sampler.rate(activeSlot).linlin(0.0125, 4, 0, 127);
		trig = ~prm.sampler.trigRate(activeSlot).linlin(1, 16, 0, 127);
		durLow = ~prm.sampler.grainDurLow(activeSlot).linlin(0.05, 3, 0, 127);
		durHigh = ~prm.sampler.grainDurHigh(activeSlot).linlin(0.1, 3.5, 0, 127);
		panLow = ~prm.sampler.panLow(activeSlot).linlin(-1, 1, 0, 127);
		panHigh = ~prm.sampler.panHigh(activeSlot).linlin(-1, 1, 0, 127);
		~apc.setDeviceEncoderValue(1, lowPass);
		~apc.setDeviceEncoderValue(2, highPass);
		~apc.setDeviceEncoderValue(3, rate);
		~apc.setDeviceEncoderValue(4, trig);
		~apc.setDeviceEncoderValue(5, durLow);
		~apc.setDeviceEncoderValue(6, durHigh);
		~apc.setDeviceEncoderValue(7, panLow);
		~apc.setDeviceEncoderValue(8, panHigh);
	}, { 8.do({ | i | ~apc.setDeviceEncoderValue(i+1, 0); }); });
}, main, page);

~apc.setDeviceEncoderFunc(1, { | val |
	var cutoff = val.linexp(0, 127, 100, 20000);
	if( activeSlot.notNil, { ~prm.sampler.setLowPassCutoff(activeSlot, cutoff); });
}, main, page);
~apc.setDeviceEncoderFunc(2, { | val |
	var cutoff = val.linexp(0, 127, 10, 6000);
	if( activeSlot.notNil, { ~prm.sampler.setHighPassCutoff(activeSlot, cutoff); });
}, main, page);
~apc.setDeviceEncoderFunc(3, { | val |
	var rate = val.linlin(0, 127, 0.0125, 4);
	if( activeSlot.notNil, { ~prm.sampler.setRate(activeSlot, rate); });
}, main, page);
~apc.setDeviceEncoderFunc(4, { | val |
	var trig = val.linlin(0, 127, 1, 16);
	if( activeSlot.notNil, { ~prm.sampler.setTrigRate(activeSlot, trig); });
}, main, page);
~apc.setDeviceEncoderFunc(5, { | val |
	var dur = val.linlin(0, 127, 0.05, 3);
	if( activeSlot.notNil, { ~prm.sampler.setGrainDurLow(activeSlot, dur); });
}, main, page);
~apc.setDeviceEncoderFunc(6, { | val |
	var dur = val.linlin(0, 127, 0.1, 3.5);
	if( activeSlot.notNil, { ~prm.sampler.setGrainDurHigh(activeSlot, dur); });
}, main, page);
~apc.setDeviceEncoderFunc(7, { | val |
	var pan = val.linlin(0, 127, -1, 1);
	if( activeSlot.notNil, { ~prm.sampler.setGrainPanLow(activeSlot, pan); });
}, main, page);
~apc.setDeviceEncoderFunc(8, { | val |
	var pan = val.linlin(0, 127, -1, 1);
	if( activeSlot.notNil, { ~prm.sampler.setGrainPanHigh(activeSlot, pan); });
}, main, page);

// attack:
~apc.setFaderFunc(5, { | val |
	var attack = val.linexp(0, 127, 0.01, 10);
	if( activeSlot.isInteger, { ~prm.sampler.setAttackTime(activeSlot, attack); });
}, main, page);
// decay:
~apc.setFaderFunc(6, { | val |
	var decay = val.linexp(0, 127, 0.01, 5);
	if( activeSlot.isInteger, { ~prm.sampler.setDecayTime(activeSlot, decay); });
}, main, page);
// sustain:
~apc.setFaderFunc(7, { | val |
	var sustain = val.linlin(0, 127, 0, 1);
	if( activeSlot.isInteger, { ~prm.sampler.setSustainLevel(activeSlot, sustain); });
}, main, page);
// release:
~apc.setFaderFunc(8, { | val |
	var release = val.linexp(0, 127, 0.01, 10);
	if( activeSlot.isInteger, { ~prm.sampler.setReleaseTime(activeSlot, release); });
}, main, page);

/////// mixer:

// vol:
~apc.setFaderFunc(1, { | val | ~prm.cmix.setVol(6, val.ccdbfs); }, page: page);
// toggle mute:
~apc.setTrackSelectFunc(1, { ~prm.cmix.tglMute(6); }, page: page);
~apc.setMixerMonitorFunc(\mute, {
	if( ~prm.cmix.isMuted(6), { ~apc.turnTrackSelectButtonOff(1) }, { ~apc.turnTrackSelectButtonOn(1); });
}, page: page);
// sends pre/post:
~apc.setCrossfaderSelectFunc(1, {
	if( ~prm.cmix.preOrPost(6) == 'post',
		{ ~prm.cmix.setSendPre(6); }, { ~prm.cmix.setSendPost(6) });
}, page: page);
~apc.setMixerMonitorFunc(\preOrPost, {
	if( ~prm.cmix.preOrPost(6) == 'post',
		{ ~apc.turnCrossfaderSelectButtonOrange(1) },
		{ ~apc.turnCrossfaderSelectButtonYellow(1) });
}, page: page);

// pre amp:

/////////////////////////
/////// presets: ///////
///////////////////////

path = "~/Library/Application Support/SuperCollider/Extensions/prm/scripts/Instruments/Sampler/Sampler Preset Loader.scd".standardizePath;

colorReset = {
	var array = SemiColonFileReader.read(path);
	array.size.do({ | line |
		var column = array[line][0];
		var row = array[line][1];
		var color = (array[line][3]).asSymbol;
		("~apc.turnGrid" ++ color ++ "(" ++ column ++ "," ++ row ++ ", bank: 5, page: 'sampler');").interpret;
		("~apc.turnGrid" ++ color ++ "(" ++ column ++ "," ++ row ++ ", bank: 6, page: 'sampler');").interpret;
	});
};

loader = {
	var array = SemiColonFileReader.read(path);
	array.size.do({ | line |
		var column = array[line][0].asInteger;
		var row = array[line][1].asInteger;
		var key = array[line][2].asSymbol;
		var color = (array[line][3]).asSymbol;
		~apc.setGridFunc(column, row, {
			~prm.sampler.readPreset(key);
			colorReset.value;
			~apc.turnGridGreen(column, row, bank: loadPreset, page: page);
		}, bank: loadPreset,  page: page);
	});
};

populateWriteFuncs = {
	5.do({ | row |
		8.do({ | column |
			~apc.setGridFunc(column, row, {
				//row.postln;  column.postln;
				// GUI bullshit goes here:
				{ makeGUI.value(column, row); }.defer;
			}, bank: writePreset, page: page);
		});
	});
};

makeGUI = { | column, row |
	var window, colorPicker, text, colorText, textField, goButton;
	column.postln;
	row.postln;
	window = Window.new("add preset key at ("++column++","++row++")", Rect((100.rand+400),(100.rand+400),225,100));
	text = StaticText.new(window).string_("preset name:");
	textField = TextField.new(window);
	colorText = StaticText.new(window).string_("controller color:");
	colorPicker = PopUpMenu.new(window);
	colorPicker.items = ["Red", "Yellow", "YellowGreen", "Cyan", "LightBlue", "Blue", "Purple", "Magenta", "Pink", "White"];
	goButton = Button.new(window).action_({
		[column, row, textField.string, colorPicker.item].postln;
		addPreset.value(column, row, textField.string, colorPicker.item);
		window.close;
	});
	goButton.string = "Write Preset";
	window.layout = VLayout(HLayout(VLayout(text, textField), VLayout(colorText, colorPicker)), goButton);
	window.front;
};

addPreset = { | column, row, key, color |
	var file = File(path, "a");
	// note that color needs to be capitalized to work!
	if( ~prm.sampler.masterPresetDict.includesKey(key.asSymbol),
		{ "preset already exists".postln; },
		{
			~prm.sampler.writePreset(key.asSymbol);
			file.write("\n"++column++";"++row++";"++key++";"++color++";");
			file.close;
			s.sync;
			loader.value;
			s.sync;
			colorReset.value;
			~apc.turnGridGreen(column, row, 2, loadPreset, page);
			("~apc.turnGrid" ++ color ++ "(" ++ column ++ "," ++ row ++ ", bank: 6, page: 'sampler');").interpret;
		}.fork;
	);
};

colorReset.value;
loader.value;
populateWriteFuncs.value;
