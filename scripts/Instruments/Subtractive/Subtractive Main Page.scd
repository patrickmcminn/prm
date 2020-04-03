/*
Saturday, March 28th 2020
Subtractive Main Page.scd
prm
*/

var page = 'subtractive';

var mainGridBank = 0;
var keyboardBank = 6;
//var keyboardHoldBank = 7;

var mainMixerBank = 0;
var mainDeviceEncodersBank = 0;
var mainMixerEncodersBank = 0;
var mainSceneLaunchBank = 0;

var mainBank = 0;
var osc1Bank = 1;
var osc2Bank = 2;
var filterBank = 3;
var envBank = 4;
var lfoBank = 5;
var keyboard = 6;
var presetLoad = 7;
var presetSave = 8;

var osc1WaveformResetFunc, osc2WaveformResetFunc;
var lfo1WaveformResetFunc, lfo2WaveformResetFunc;

var makeMIDIFuncs, freeMIDIFuncs;
var onArray, offArray;
var midiFuncsLoaded = false;

var metaBank = mainBank;

var whiteKeys = [0, 2, 4, 5, 7, 9, 11, 12]+48;
var blackKeys = [1, 3, 6, 8, 10]+48;
var octaveMod = 0;

~apc.makePage(page);
~apc.addGridBanks(10, page);
~apc.addDeviceEncodersBanks(10, page);
~apc.addMixerEncodersBanks(10, page);


makeMIDIFuncs = { | device = nil, channel = 13 |
	onArray = Array.fill(128, { | i |
		MIDIFunc.noteOn({ | vel | ~prm.subtractive.playNote(i.midicps, vel.ccdbfs);
	}, i, channel, device); });
	offArray = Array.fill(128, { | i |
		MIDIFunc.noteOff({ ~prm.subtractive.releaseNote(i.midicps); }, i, channel, device); });

	midiFuncsLoaded = true;
};

freeMIDIFuncs = {
	onArray.do({ | i | i.free; });
	offArray.do({ | i | i.free; });
	midiFuncsLoaded = false;
};

~apc.setSceneLaunchMonitorFunc(\midi, {
	if( midiFuncsLoaded == true, { ~apc.turnSceneLaunchButtonGreen(4); }, { ~apc.turnSceneLaunchButtonYellow(4) });
}, mainSceneLaunchBank, page);
~apc.setSceneLaunchFunc(4, {
	if( midiFuncsLoaded == true, { freeMIDIFuncs.value; }, { makeMIDIFuncs.value; });
}, bank: mainSceneLaunchBank, page: page);


///////////////////
// Preset Dump: //
/////////////////

~apc.setBankButtonFunc({ ~prm.subtractive.printAllParameters }, page: page);

//////////////////////////
//// Meta Bank Change: //
////////////////////////

~apc.setDeviceButtonsMonitorFunc(\meta, {
	if( metaBank == mainBank, { ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1) });
	if( metaBank == osc1Bank, { ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2) });
	if( metaBank == osc2Bank, { ~apc.turnDeviceButtonOn(3) }, { ~apc.turnDeviceButtonOff(3) });
	if( metaBank == filterBank, { ~apc.turnDeviceButtonOn(4) }, { ~apc.turnDeviceButtonOff(4) });
	if( metaBank == envBank, { ~apc.turnDeviceButtonOn(5) }, { ~apc.turnDeviceButtonOff(5) });
	if( metaBank == lfoBank, { ~apc.turnDeviceButtonOn(6) }, { ~apc.turnDeviceButtonOff(6) });
	if( metaBank == presetLoad, { ~apc.turnDeviceButtonOn(7) }, { ~apc.turnDeviceButtonOff(7) });
	if( metaBank == presetSave, { ~apc.turnDeviceButtonOn(8) }, { ~apc.turnDeviceButtonOff(8) });
}, page: page);

// main bank:
~apc.setDeviceButtonFunc(1, {
	if( ~apc.activeGridBank != keyboard, { ~apc.setActiveGridBank(mainBank); });
	~apc.setActiveDeviceEncodersBank(mainBank);
	~apc.setActiveMixerEncodersBank(mainBank);
	metaBank = mainBank;
}, page: page);

// osc1 bank:
~apc.setDeviceButtonFunc(2, {
	if( ~apc.activeGridBank != keyboard, { ~apc.setActiveGridBank(osc1Bank); });
	~apc.setActiveDeviceEncodersBank(osc1Bank);
	~apc.setActiveMixerEncodersBank(osc1Bank);
	metaBank = osc1Bank;
}, page: page);

// osc 2 bank:
~apc.setDeviceButtonFunc(3, {
	if( ~apc.activeGridBank != keyboard, { ~apc.setActiveGridBank(osc2Bank); });
	~apc.setActiveDeviceEncodersBank(osc2Bank);
	~apc.setActiveMixerEncodersBank(osc2Bank);
	metaBank = osc2Bank;
}, page: page);

// filter bank:
~apc.setDeviceButtonFunc(4, {
	if( ~apc.activeGridBank != keyboard, { ~apc.setActiveGridBank(filterBank); });
	~apc.setActiveDeviceEncodersBank(filterBank);
	~apc.setActiveMixerEncodersBank(filterBank);
	metaBank = filterBank;
}, page: page);

// env bank:
~apc.setDeviceButtonFunc(5, {
	if( ~apc.activeGridBank != keyboard, { ~apc.setActiveGridBank(envBank); });
	~apc.setActiveDeviceEncodersBank(envBank);
	~apc.setActiveMixerEncodersBank(envBank);
	metaBank = envBank;
}, page: page);

// lfo:
~apc.setDeviceButtonFunc(6, {
	if( ~apc.activeGridBank != keyboard, { ~apc.setActiveGridBank(lfoBank); });
	~apc.setActiveDeviceEncodersBank(lfoBank);
	~apc.setActiveMixerEncodersBank(lfoBank);
	metaBank = lfoBank;
}, page: page);

~apc.setDeviceButtonFunc(7, {
	~apc.setActiveGridBank(presetLoad);
	~apc.setActiveDeviceEncodersBank(presetLoad);
	~apc.setActiveDeviceEncodersBank(presetLoad);
	metaBank = presetLoad;
}, page: page);

~apc.setDeviceButtonFunc(8, {
	~apc.setActiveGridBank(presetSave);
	~apc.setActiveDeviceEncodersBank(presetSave);
	~apc.setActiveDeviceEncodersBank(presetSave);
	metaBank = presetSave;
}, page: page);

////// change to keyboard bank:

~apc.setBankSelectFunc('down', { ~apc.setActiveGridBank(keyboard) }, page: page);
~apc.setBankSelectFunc('up', { ~apc.setActiveGridBank(metaBank) }, page: page);

////////////////////////
////// Mixer Bank /////
//////////////////////

//////// volume:

~apc.setFaderFunc(1, { | val |
	~prm.cmix.setVol(7, val.ccdbfs) }, page: page);
~apc.setMixerMonitorFunc('main', {
	if( ~prm.cmix.isMuted(7),
		{ ~apc.turnTrackSelectButtonOff(1) }, { ~apc.turnTrackSelectButtonOn(1) });
	if( ~prm.cmix.preOrPost(7) == 'post',
		{ ~apc.turnCrossfaderSelectButtonOrange(1) }, { ~apc.turnCrossfaderSelectButtonYellow(1) });
}, page: page);
~apc.setCrossfaderSelectFunc(1, {
	if( ~prm.cmix.preOrPost(7) == 'post',
		{ ~prm.cmix.setSendPre(7); }, { ~prm.cmix.setSendPost(7); });
}, page: page);
~apc.setTrackSelectFunc(1, { ~prm.cmix.tglMute(7) }, page: page);

/////// Osc 1:
~apc.setFaderFunc(2, { | val | ~prm.subtractive.setOsc1Vol(val.ccdbfs); }, mainMixerBank, page);
////// Sub Osc 1:
~apc.setFaderFunc(3, { | val | ~prm.subtractive.setOsc1SubVol(val.ccdbfs) }, mainMixerBank, page);
///// Osc 2:
~apc.setFaderFunc(4, { | val | ~prm.subtractive.setOsc2Vol(val.ccdbfs) }, mainMixerBank, page);
///// Sub Osc 2:
~apc.setFaderFunc(5, { | val | ~prm.subtractive.setOsc2SubVol(val.ccdbfs) }, mainMixerBank, page);
///// Filter Drive:
~apc.setFaderFunc(6, { | val |
	var drive = val.linexp(0, 127, 1, 100);
	~prm.subtractive.setFilterDrive(drive);
}, mainMixerBank, page);
///// LFO 1 Pulse Width LFO:
~apc.setFaderFunc(7, { | val |
	var bottom = val.linlin(0, 127, 0, -1);
	var top = val.linlin(0, 127, 0, 1);
	~prm.subtractive.setOsc1PulseWidthLFO1Bottom(bottom);
	~prm.subtractive.setOsc1PulseWidthLFO1Top(top);
	~prm.subtractive.setOsc2PulseWidthLFO1Bottom(bottom);
	~prm.subtractive.setOsc2PulseWidthLFO1Top(top);
}, mainMixerBank, page);
///// LFO 2 Pulse Width LFO:
~apc.setFaderFunc(8, { | val |
	var bottom = val.linlin(0, 127, 0, -1);
	var top = val.linlin(0, 127, 0, 1);
	~prm.subtractive.setOsc1PulseWidthLFO2Bottom(bottom);
	~prm.subtractive.setOsc1PulseWidthLFO2Top(top);
	~prm.subtractive.setOsc2PulseWidthLFO2Bottom(bottom);
	~prm.subtractive.setOsc2PulseWidthLFO2Top(top);
}, mainMixerBank, page);


///////////////////////////////////////
///// Main Mixer Encoders Bank: //////
/////////////////////////////////////

~apc.setMixerEncodersMonitorFunc(\main, {
	var reverb = ~prm.cmix.sendVol(7, 0);
	var granulator = ~prm.cmix.sendVol(7, 1);
	var modular = ~prm.cmix.sendVol(7, 2);
	var delay = ~prm.cmix.sendVol(7, 3);
	var filterLFO1 = ~prm.subtractive.filterCutoffLFO1TopRatio.linlin(1, 8, 0, 127);
	var filterLFO2 = ~prm.subtractive.filterCutoffLFO2TopRatio.linlin(1, 8, 0, 127);
	var osc1PulseWidth = ~prm.subtractive.osc1PulseWidth.linlin(0, 1, 0, 127);
	var osc2PulseWidth = ~prm.subtractive.osc2PulseWidth.linlin(0, 1, 0, 127);

	~apc.setMixerEncoderValue(1, reverb.dbfsCC, page: page);
	~apc.setMixerEncoderValue(2, granulator.dbfsCC, page: page);
	~apc.setMixerEncoderValue(3, modular.dbfsCC, page: page);
	~apc.setMixerEncoderValue(4, delay.dbfsCC, page: page);

	~apc.setMixerEncoderValue(5, filterLFO1);
	~apc.setMixerEncoderValue(6, filterLFO2);
	~apc.setMixerEncoderValue(7, osc1PulseWidth);
	~apc.setMixerEncoderValue(8, osc2PulseWidth);
}, mainMixerEncodersBank, page);
~apc.setMixerEncoderFunc(1, { | val | ~prm.cmix.setSendVol(7, 0, val.ccdbfs) },
	mainMixerEncodersBank, page);
~apc.setMixerEncoderFunc(2, { | val | ~prm.cmix.setSendVol(7, 1, val.ccdbfs) },
	mainMixerEncodersBank, page);
~apc.setMixerEncoderFunc(3, { | val | ~prm.cmix.setSendVol(7, 2, val.ccdbfs) },
	mainMixerEncodersBank, page);
~apc.setMixerEncoderFunc(4, { | val | ~prm.cmix.setSendVol(7, 3, val.ccdbfs) },
	mainMixerEncodersBank, page);

// filter LFO 1:
~apc.setMixerEncoderFunc(5, { | val |
	var bottom = val.linlin(0, 127, 1, 0.125);
	var top = val.linlin(0, 127, 1, 8);
	~prm.subtractive.setFilterCutoffLFO1BottomRatio(bottom);
	~prm.subtractive.setFilterCutoffLFO1TopRatio(top);
}, mainMixerEncodersBank, page);

// filter LFO 2:
~apc.setMixerEncoderFunc(6, { | val |
	var bottom = val.linlin(0, 127, 1, 0.125);
	var top = val.linlin(0, 127, 1, 8);
	~prm.subtractive.setFilterCutoffLFO2BottomRatio(bottom);
	~prm.subtractive.setFilterCutoffLFO2TopRatio(top);
}, mainMixerEncodersBank, page);

// Osc 1 Pulse Width:
~apc.setMixerEncoderFunc(7, { | val |
	var width = val.linlin(0, 127, 0, 1);
	~prm.subtractive.setOsc1PulseWidth(width);
}, mainMixerEncodersBank, page);

// Osc 2 Pulse Width:
~apc.setMixerEncoderFunc(8, { | val |
	var width = val.linlin(0, 127, 0, 1);
	~prm.subtractive.setOsc2PulseWidth(width);
}, mainMixerEncodersBank, page);


///////////////////////////////////////
///// Main Device Encoder Bank: //////
////////////////////////////////////

~apc.setDeviceEncodersMonitorFunc(\main, {
	var cutoff = ~prm.subtractive.filterCutoff.explin(30, 20000, 0, 127);
	var res = ~prm.subtractive.filterRes.linlin(0, 1.2, 0, 127);
	var lfo1Freq = ~prm.subtractive.lfo1Freq.explin(0.001, 55, 0, 127);
	var lfo2Freq = ~prm.subtractive.lfo2Freq.explin(0.001, 55, 0, 127);
	var attack = ~prm.subtractive.attackTime.explin(0.001, 10, 0, 127);
	var decay = ~prm.subtractive.decayTime.explin(0.001, 7, 0, 127);
	var sustain = ~prm.subtractive.sustainLevel.linlin(0, 1, 0, 127);
	var release = ~prm.subtractive.releaseTime.explin(0.001, 15, 0, 127);

	~apc.setDeviceEncoderValue(1, cutoff);
	~apc.setDeviceEncoderValue(2, res);
	~apc.setDeviceEncoderValue(3, lfo1Freq);
	~apc.setDeviceEncoderValue(4, lfo2Freq);
	~apc.setDeviceEncoderValue(5, attack);
	~apc.setDeviceEncoderValue(6, decay);
	~apc.setDeviceEncoderValue(7, sustain);
	~apc.setDeviceEncoderValue(8, release);
}, mainMixerEncodersBank, page);

// cutoff:
~apc.setDeviceEncoderFunc(1, { | val |
	var cutoff = val.linexp(0, 127, 30, 20000);
	~prm.subtractive.setFilterCutoff(cutoff);
}, mainMixerEncodersBank, page);

// res:
~apc.setDeviceEncoderFunc(2, { | val |
	var res = val.linlin(0, 127, 0, 1.2);
	~prm.subtractive.setFilterRes(res);
}, mainMixerEncodersBank, page);

// lfo1Freq:
~apc.setDeviceEncoderFunc(3, { | val |
	var freq = val.linexp(0, 127, 0.001, 55);
	~prm.subtractive.setLFO1Freq(freq);
}, mainMixerEncodersBank, page);

// lfo2Freq:
~apc.setDeviceEncoderFunc(4, { | val |
	var freq = val.linexp(0, 127, 0.001, 55);
	~prm.subtractive.setLFO2Freq(freq);
}, mainMixerEncodersBank, page);

// attack time:
~apc.setDeviceEncoderFunc(5, { | val |
	var attack = val.linexp(0, 127, 0.001, 10);
	~prm.subtractive.setAttackTime(attack);
}, mainMixerEncodersBank, page);

// decay time:
~apc.setDeviceEncoderFunc(6, { | val |
	var decay = val.linexp(0, 127, 0.001, 7);
	~prm.subtractive.setDecayTime(decay);
}, mainMixerEncodersBank, page);

// sustain level:
~apc.setDeviceEncoderFunc(7, { | val |
	var sustain = val.linlin(0, 127, 0, 1);
	~prm.subtractive.setSustainLevel(sustain);
}, mainMixerEncodersBank, page);

// release time:
~apc.setDeviceEncoderFunc(8, { | val |
	var release = val.linexp(0, 127, 0.001, 15);
	~prm.subtractive.setReleaseTime(release);
}, mainMixerEncodersBank, page);


//////////////////////////
//// Main Grid Bank: ////
////////////////////////

osc1WaveformResetFunc = { 4.do({ | i | ~apc.turnGridBlue(i, 4, 2, mainGridBank, page); }); };
osc2WaveformResetFunc = { 4.do({ | i | ~apc.turnGridLightBlue(i+4, 4, 2, mainGridBank, page); }); };

///// Osc 1 Waveform:
~apc.setGridMonitorFunc(\osc1, {
	if( ~prm.subtractive.osc1Waveform == 0, { ~apc.turnGridGreen(0, 4) }, { ~apc.turnGridBlue(0, 4) });
	if( ~prm.subtractive.osc1Waveform == 1, { ~apc.turnGridGreen(1, 4) }, { ~apc.turnGridBlue(1, 4) });
	if( ~prm.subtractive.osc1Waveform == 2, { ~apc.turnGridGreen(2, 4) }, { ~apc.turnGridBlue(2, 4) });
	if( ~prm.subtractive.osc1Waveform == 3, { ~apc.turnGridGreen(3, 4) }, { ~apc.turnGridBlue(3, 4) });
	if( ~prm.subtractive.osc1OctaveMul == 0.5, { ~apc.turnGridGreen(0, 3) }, { ~apc.turnGridBlue(0, 3) });
	if( ~prm.subtractive.osc1OctaveMul == 1, { ~apc.turnGridGreen(1, 3) }, { ~apc.turnGridBlue(1, 3) });
	if( ~prm.subtractive.osc1OctaveMul == 2, { ~apc.turnGridGreen(2, 3) }, { ~apc.turnGridBlue(2, 3) });
	if( ~prm.subtractive.osc1OctaveMul == 4, { ~apc.turnGridGreen(3, 3) }, { ~apc.turnGridBlue(3, 3) });

}, mainGridBank, page);
~apc.setGridFunc(0, 4, { ~prm.subtractive.setOsc1Waveform(0); }, bank: mainGridBank, page: page);
~apc.setGridFunc(1, 4, { ~prm.subtractive.setOsc1Waveform(1); }, bank: mainGridBank, page: page);
~apc.setGridFunc(2, 4, { ~prm.subtractive.setOsc1Waveform(2); }, bank: mainGridBank, page: page);
~apc.setGridFunc(3, 4, { ~prm.subtractive.setOsc1Waveform(3); }, bank: mainGridBank, page: page);

~apc.setGridFunc(0, 3, { ~prm.subtractive.setOsc1Octave(2) }, bank: mainGridBank, page: page);
~apc.setGridFunc(1, 3, { ~prm.subtractive.setOsc1Octave(3) }, bank: mainGridBank, page: page);
~apc.setGridFunc(2, 3, { ~prm.subtractive.setOsc1Octave(4) }, bank: mainGridBank, page: page);
~apc.setGridFunc(3, 3, { ~prm.subtractive.setOsc1Octave(5) }, bank: mainGridBank, page: page);


///// Osc 2 Waveform:
~apc.setGridMonitorFunc(\osc2, {
	if( ~prm.subtractive.osc2Waveform == 0, { ~apc.turnGridGreen(4, 4) }, { ~apc.turnGridLightBlue(4, 4) });
	if( ~prm.subtractive.osc2Waveform == 1, { ~apc.turnGridGreen(5, 4) }, { ~apc.turnGridLightBlue(5, 4) });
	if( ~prm.subtractive.osc2Waveform == 2, { ~apc.turnGridGreen(6, 4) }, { ~apc.turnGridLightBlue(6, 4) });
	if( ~prm.subtractive.osc2Waveform == 3, { ~apc.turnGridGreen(7, 4) }, { ~apc.turnGridLightBlue(7, 4) });
	if( ~prm.subtractive.osc2OctaveMul == 0.5, { ~apc.turnGridGreen(4, 3) }, { ~apc.turnGridLightBlue(4, 3) });
	if( ~prm.subtractive.osc2OctaveMul == 1, { ~apc.turnGridGreen(5, 3) }, { ~apc.turnGridLightBlue(5, 3) });
	if( ~prm.subtractive.osc2OctaveMul == 2, { ~apc.turnGridGreen(6, 3) }, { ~apc.turnGridLightBlue(6, 3) });
	if( ~prm.subtractive.osc2OctaveMul == 4, { ~apc.turnGridGreen(7, 3) }, { ~apc.turnGridLightBlue(7, 3) });
}, mainGridBank, page);
~apc.setGridFunc(4, 4, { ~prm.subtractive.setOsc2Waveform(0); }, bank: mainGridBank, page: page);
~apc.setGridFunc(5, 4, { ~prm.subtractive.setOsc2Waveform(1); }, bank: mainGridBank, page: page);
~apc.setGridFunc(6, 4, { ~prm.subtractive.setOsc2Waveform(2); }, bank: mainGridBank, page: page);
~apc.setGridFunc(7, 4, { ~prm.subtractive.setOsc2Waveform(3); }, bank: mainGridBank, page: page);
~apc.setGridFunc(4, 3, { ~prm.subtractive.setOsc2Octave(2) }, bank: mainGridBank, page: page);
~apc.setGridFunc(5, 3, { ~prm.subtractive.setOsc2Octave(3) }, bank: mainGridBank, page: page);
~apc.setGridFunc(6, 3, { ~prm.subtractive.setOsc2Octave(4) }, bank: mainGridBank, page: page);
~apc.setGridFunc(7, 3, { ~prm.subtractive.setOsc2Octave(5) }, bank: mainGridBank, page: page);

///// LFO 1 Waveform:
~apc.setGridMonitorFunc(\lfo1, {
	if( ~prm.subtractive.lfo1Waveform == 0, { ~apc.turnGridGreen(0, 2) }, { ~apc.turnGridPink(0, 2); });
	if( ~prm.subtractive.lfo1Waveform == 1, { ~apc.turnGridGreen(1, 2) }, { ~apc.turnGridPink(1, 2); });
	if( ~prm.subtractive.lfo1Waveform == 2, { ~apc.turnGridGreen(2, 2) }, { ~apc.turnGridPink(2, 2); });
	if( ~prm.subtractive.lfo1Waveform == 3, { ~apc.turnGridGreen(3, 2) }, { ~apc.turnGridPink(3, 2); });
	if( ~prm.subtractive.lfo1Waveform == 4, { ~apc.turnGridGreen(4, 2) }, { ~apc.turnGridPink(4, 2); });
	if( ~prm.subtractive.lfo1Waveform == 5, { ~apc.turnGridGreen(5, 2) }, { ~apc.turnGridPink(5, 2); });
}, mainGridBank, page);
~apc.setGridFunc(0, 2, { ~prm.subtractive.setLFO1Waveform(0) }, bank: mainGridBank, page: page);
~apc.setGridFunc(1, 2, { ~prm.subtractive.setLFO1Waveform(1) }, bank: mainGridBank, page: page);
~apc.setGridFunc(2, 2, { ~prm.subtractive.setLFO1Waveform(2) }, bank: mainGridBank, page: page);
~apc.setGridFunc(3, 2, { ~prm.subtractive.setLFO1Waveform(3) }, bank: mainGridBank, page: page);
~apc.setGridFunc(4, 2, { ~prm.subtractive.setLFO1Waveform(4) }, bank: mainGridBank, page: page);
~apc.setGridFunc(5, 2, { ~prm.subtractive.setLFO1Waveform(5) }, bank: mainGridBank, page: page);


///// LFO 2 Waveform:
~apc.setGridMonitorFunc(\lfo2, {
	if( ~prm.subtractive.lfo2Waveform == 0, { ~apc.turnGridGreen(0, 1) }, { ~apc.turnGridPurple(0, 1); });
	if( ~prm.subtractive.lfo2Waveform == 1, { ~apc.turnGridGreen(1, 1) }, { ~apc.turnGridPurple(1, 1); });
	if( ~prm.subtractive.lfo2Waveform == 2, { ~apc.turnGridGreen(2, 1) }, { ~apc.turnGridPurple(2, 1); });
	if( ~prm.subtractive.lfo2Waveform == 3, { ~apc.turnGridGreen(3, 1) }, { ~apc.turnGridPurple(3, 1); });
	if( ~prm.subtractive.lfo2Waveform == 4, { ~apc.turnGridGreen(4, 1) }, { ~apc.turnGridPurple(4, 1); });
	if( ~prm.subtractive.lfo2Waveform == 5, { ~apc.turnGridGreen(5, 1) }, { ~apc.turnGridPurple(5, 1); });
}, mainGridBank, page);
~apc.setGridFunc(0, 1, { ~prm.subtractive.setLFO2Waveform(0) }, bank: mainGridBank, page: page);
~apc.setGridFunc(1, 1, { ~prm.subtractive.setLFO2Waveform(1) }, bank: mainGridBank, page: page);
~apc.setGridFunc(2, 1, { ~prm.subtractive.setLFO2Waveform(2) }, bank: mainGridBank, page: page);
~apc.setGridFunc(3, 1, { ~prm.subtractive.setLFO2Waveform(3) }, bank: mainGridBank, page: page);
~apc.setGridFunc(4, 1, { ~prm.subtractive.setLFO2Waveform(4) }, bank: mainGridBank, page: page);
~apc.setGridFunc(5, 1, { ~prm.subtractive.setLFO2Waveform(5) }, bank: mainGridBank, page: page);


///// Filter Type:
~apc.setGridMonitorFunc(\filter, {
	if( ~prm.subtractive.filterType == 0, { ~apc.turnGridGreen(0, 0) }, { ~apc.turnGridWhite(0, 0) });
	if( ~prm.subtractive.filterType == 1, { ~apc.turnGridGreen(1, 0) }, { ~apc.turnGridWhite(1, 0) });
	if( ~prm.subtractive.filterType == 2, { ~apc.turnGridGreen(2, 0) }, { ~apc.turnGridWhite(2, 0) });
}, mainGridBank, page);
~apc.setGridFunc(0, 0, { ~prm.subtractive.setFilterType('lowPass') }, bank: mainGridBank, page: page);
~apc.setGridFunc(1, 0, { ~prm.subtractive.setFilterType('highPass') }, bank: mainGridBank, page: page);
~apc.setGridFunc(2, 0, { ~prm.subtractive.setFilterType('bandPass') }, bank: mainGridBank, page: page);



////////////////////
//// Keyboard: ////
//////////////////

//////// Normal Bank:
8.do({ | i |
	~apc.turnGridPurple(i, 0, bank: keyboardBank, page: page);
	~apc.turnGridPink(i, 2, bank: keyboardBank, page: page);
});
~apc.turnGridPurple(1, 1, bank: keyboardBank, page: page);
~apc.turnGridPurple(2, 1, bank: keyboardBank, page: page);
~apc.turnGridPurple(4, 1, bank: keyboardBank, page: page);
~apc.turnGridPurple(5, 1, bank: keyboardBank, page: page);
~apc.turnGridPurple(6, 1, bank: keyboardBank, page: page);
~apc.turnGridPink(1, 3, bank: keyboardBank, page: page);
~apc.turnGridPink(2, 3, bank: keyboardBank, page: page);
~apc.turnGridPink(4, 3, bank: keyboardBank, page: page);
~apc.turnGridPink(5, 3, bank: keyboardBank, page: page);
~apc.turnGridPink(6, 3, bank: keyboardBank, page: page);

8.do({ | i |
	~apc.setGridFunc(i, 0, { ~
		subtractive.playNote((whiteKeys[i] + octaveMod).midicps);
		~apc.turnGridGreen(i, 0);
	}, 'noteOn', keyboardBank, page);
	~apc.setGridFunc(i, 0, {
		~subtractive.releaseNote((whiteKeys[i] + octaveMod).midicps);
		~apc.turnGridPurple(i, 0);
	}, 'noteOff', keyboardBank, page);
	~apc.setGridFunc(i, 2, {
		~subtractive.playNote((whiteKeys[i] + 12 + octaveMod).midicps);
		~apc.turnGridGreen(i, 2);
	}, 'noteOn', keyboardBank, page);
	~apc.setGridFunc(i, 2, {
		~subtractive.releaseNote((whiteKeys[i] + 12 + octaveMod).midicps);
		~apc.turnGridPink(i, 2);
	}, 'noteOff', keyboardBank, page);
});
~apc.setGridFunc(1, 1, {
	~subtractive.playNote((blackKeys[0] + octaveMod).midicps);
	~apc.turnGridGreen(1, 1);
},'noteOn', keyboardBank, page);
~apc.setGridFunc(1, 1, {
	~subtractive.releaseNote((blackKeys[0] + octaveMod).midicps);
	~apc.turnGridPurple(1, 1);
},'noteOff', keyboardBank, page);
~apc.setGridFunc(2, 1, {
	~subtractive.playNote((blackKeys[1] + octaveMod).midicps);
	~apc.turnGridGreen(2, 1);
},'noteOn', keyboardBank, page);
~apc.setGridFunc(2, 1, {
	~subtractive.releaseNote((blackKeys[1] + octaveMod).midicps);
	~apc.turnGridPurple(2, 1);
},'noteOff', keyboardBank, page);
~apc.setGridFunc(4, 1, {
	~subtractive.playNote((blackKeys[2] + octaveMod).midicps);
	~apc.turnGridGreen(4, 1);
},'noteOn', keyboardBank, page);
~apc.setGridFunc(4, 1, {
	~subtractive.releaseNote((blackKeys[2] + octaveMod).midicps);
	~apc.turnGridPurple(4, 1);
},'noteOff', keyboardBank, page);
~apc.setGridFunc(5, 1, {
	~subtractive.playNote((blackKeys[3] + octaveMod).midicps);
	~apc.turnGridGreen(5, 1);
},'noteOn', keyboardBank, page);
~apc.setGridFunc(5, 1, {
	~subtractive.releaseNote((blackKeys[3] + octaveMod).midicps);
	~apc.turnGridPurple(5, 1);
},'noteOff', keyboardBank, page);
~apc.setGridFunc(6, 1, {
	~subtractive.playNote((blackKeys[4] + octaveMod).midicps);
	~apc.turnGridGreen(6, 1);
},'noteOn', keyboardBank, page);
~apc.setGridFunc(6, 1, {
	~subtractive.releaseNote((blackKeys[4] + octaveMod).midicps);
	~apc.turnGridPurple(6, 1);
},'noteOff', keyboardBank, page);


~apc.setGridFunc(1, 3, {
	~subtractive.playNote((blackKeys[0] + 12 + octaveMod).midicps);
	~apc.turnGridGreen(1, 3);
}, 'noteOn', keyboardBank, page);
~apc.setGridFunc(1, 3, {
	~subtractive.releaseNote((blackKeys[0] + 12 + octaveMod).midicps);
	~apc.turnGridPink(1, 3);
}, 'noteOff', keyboardBank, page);
~apc.setGridFunc(2, 3, {
	~subtractive.playNote((blackKeys[1] + 12 + octaveMod).midicps);
	~apc.turnGridGreen(2, 3);
}, 'noteOn', keyboardBank, page);
~apc.setGridFunc(2, 3, {
	~subtractive.releaseNote((blackKeys[1] + 12 + octaveMod).midicps);
	~apc.turnGridPink(2, 3);
}, 'noteOff', keyboardBank, page);
~apc.setGridFunc(4, 3, {
	~subtractive.playNote((blackKeys[2] + 12 + octaveMod).midicps);
	~apc.turnGridGreen(4, 3);
}, 'noteOn', keyboardBank, page);
~apc.setGridFunc(4, 3, {
	~subtractive.releaseNote((blackKeys[2] + 12 + octaveMod).midicps);
	~apc.turnGridPink(4, 3);
}, 'noteOff', keyboardBank, page);
~apc.setGridFunc(5, 3, {
	~subtractive.playNote((blackKeys[3] + 12 + octaveMod).midicps);
	~apc.turnGridGreen(5, 3);
}, 'noteOn', keyboardBank, page);
~apc.setGridFunc(5, 3, {
	~subtractive.releaseNote((blackKeys[3] + 12 + octaveMod).midicps);
	~apc.turnGridPink(5, 3);
}, 'noteOff', keyboardBank, page);
~apc.setGridFunc(6, 3, {
	~subtractive.playNote((blackKeys[4] + 12 + octaveMod).midicps);
	~apc.turnGridGreen(6, 3);
}, 'noteOn', keyboardBank, page);
~apc.setGridFunc(6, 3, {
	~subtractive.releaseNote((blackKeys[4] + 12 + octaveMod).midicps);
	~apc.turnGridPink(6, 3);
}, 'noteOff', keyboardBank, page);



