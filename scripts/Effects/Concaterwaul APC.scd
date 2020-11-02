/*
Friday, October 16th 2020
Concaterwaul APC.scd
prm
*/

var page = 'concaterwaul';

var main = 0;


var freqArray;
var freq1Reset, freq2Reset, freq3Reset, freq4Reset, freq5Reset;


~apc.makePage(page);

//// master funcs:
~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.storageDict[\loadMasterFunc].value;

//////// Mixer:

~apc.setFaderFunc(1, { | val |
	~conc.mixer.setVol(val.ccdbfs(-60, 0));
}, main, page);
~apc.setTrackSelectFunc(1, { ~conc.mixer.tglMute },
	bank: main, page: page);
~apc.setRecordEnableButtonFunc(1, {  ~concInput.tglMute; },
	bank: main, page: page);
~apc.setCrossfaderSelectFunc(1, {
	if( ~conc.mixer.preOrPost(0) == 'post',
		{ ~conc.mixer.setSendPre(0); }, { ~conc.mixer.setSendPost(0); });
}, page: page);
~apc.setMixerMonitorFunc(\main, {
	if( ~conc.mixer.isMuted, { ~apc.turnTrackSelectButtonOff(1) }, { ~apc.turnTrackSelectButtonOn(1) });
	if( ~concInput.isMuted, { ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
	if( ~conc.mixer.preOrPost == 'post',
		{ ~apc.turnCrossfaderSelectButtonOrange(1) }, { ~apc.turnCrossfaderSelectButtonYellow(1) });
}, main, page);

// sends:
~apc.setMixerEncodersMonitorFunc(\sends, {
	var reverb, granulator, modular, delay;
	reverb = ~conc.mixer.sendVol(0);
	granulator = ~conc.mixer.sendVol(1);
	modular = ~conc.mixer.sendVol(2);
	delay = ~conc.mixer.sendVol(3);
	~apc.setMixerEncoderValue(1, reverb.dbfsCC, page: page);
	~apc.setMixerEncoderValue(2, granulator.dbfsCC, page: page);
	~apc.setMixerEncoderValue(3, modular.dbfsCC, page: page);
	~apc.setMixerEncoderValue(4, delay.dbfsCC, page: page);
}, main, page);
~apc.setMixerEncoderFunc(1, { | val | ~conc.mixer.setSendVol(0, val.ccdbfs) }, main, page);
~apc.setMixerEncoderFunc(2, { | val | ~conc.mixer.setSendVol(1, val.ccdbfs) }, main, page);
~apc.setMixerEncoderFunc(3, { | val | ~conc.mixer.setSendVol(2, val.ccdbfs) }, main, page);
~apc.setMixerEncoderFunc(4, { | val | ~conc.mixer.setSendVol(3, val.ccdbfs) }, main, page);

//////// SubMixer:

4.do({ | i |
	// vol:
	~apc.setFaderFunc(i+2, { | val | ~conc.subMixer.setVol(i, val.ccdbfs); }, bank: main, page: page);
	// toggle mute:
	~apc.setTrackSelectFunc(i+2, { ~conc.subMixer.tglMute(i); }, bank: main, page: page);
	~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
		if( ~conc.subMixer.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+2) }, { ~apc.turnTrackSelectButtonOn(i+2); });
	}, bank: main, page: page);
});

/////////////////////////////
///// mixer encoders: //////
///////////////////////////

~apc.setMixerEncodersMonitorFunc(\nonSends, {
	var freq, modFreq, detune, lfo;
	freq = ~conc.baseFreq.explin(50, 2500, 0, 127);
	modFreq = ~conc.baseModFreq.explin(0.1, 55, 0, 127);
	detune = ~conc.detune.linlin(0, 0.1, 0, 127);
	lfo = ~conc.lfo.frequency.explin(0.001, 22, 0, 127);
	~apc.setMixerEncoderValue(5, freq);
	~apc.setMixerEncoderValue(6, modFreq);
	~apc.setMixerEncoderValue(7, detune);
	~apc.setMixerEncoderValue(8, lfo);
}, bank: main, page: page);
~apc.setMixerEncoderFunc(5, { | val |
	var freq = val.linexp(0, 127, 50, 2500);
	~conc.setBaseFreq(freq);
}, bank: main, page: page);
~apc.setMixerEncoderFunc(6, { | val |
	var freq = val.linexp(0, 127, 0.1, 55);
	~conc.setBaseModFreq(freq);
}, bank: main, page: page);
~apc.setMixerEncoderFunc(7, { | val |
	var d = val.linlin(0, 127, 0, 0.1);
	~conc.setDetune(d);
}, bank: main, page: page);
~apc.setMixerEncoderFunc(8, { | val |
	var freq = val.linexp(0, 127, 0.001, 22);
	~conc.lfo.setFrequency(freq);
}, bank: main, page: page);

/////////////////////////////
///// device encoders: /////
///////////////////////////

~apc.setDeviceEncodersMonitorFunc(\mainEncoders, {
	var lowpass, highpass, glitchLength, glitchMod;
	var unPitchLength, unPitchMod, pitchLength, pitchMod;
	lowpass = ~conc.postEQ.lowPassCutoff.explin(150, 20000, 0, 127);
	highpass = ~conc.postEQ.highPassCutoff.explin(10, 3500, 0, 127);
	glitchLength = ~conc.glitchLength.explin(0.05, 3, 0, 127);
	glitchMod = ~conc.glitchModRange.explin(0.01, 1, 0, 127);
	unPitchLength = ~conc.unPitchLength.linlin(0.001, 0.1, 0, 127);
	unPitchMod = ~conc.unPitchModRange.linlin(0.0005, 0.05, 0, 127);
	pitchLength = ~conc.pitchLength.linlin(0.005, 0.5, 0, 127);
	pitchMod = ~conc.pitchModRange.linlin(0.001, 0.3, 0, 127);
	~apc.setDeviceEncoderValue(1, lowpass);
	~apc.setDeviceEncoderValue(2, highpass);
	~apc.setDeviceEncoderValue(3, glitchLength);
	~apc.setDeviceEncoderValue(4, glitchMod);
	~apc.setDeviceEncoderValue(5, unPitchLength);
	~apc.setDeviceEncoderValue(6, unPitchMod);
	~apc.setDeviceEncoderValue(7, pitchLength);
	~apc.setDeviceEncoderValue(8, pitchMod);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(1, { | val |
	var lowpass = val.linexp(0, 127, 150, 20000);
	~conc.postEQ.setLowPassCutoff(lowpass);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(2, { | val |
	var highpass = val.linexp(0, 127, 10, 3500);
	~conc.postEQ.setHighPassCutoff(highpass);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(3, { | val |
	var glitchLength = val.linexp(0, 127, 0.05, 3);
	~conc.setGlitchLength(glitchLength);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(4, { | val |
	var glitchMod = val.linexp(0, 127, 0.01, 1);
	~conc.setGlitchModRange(glitchMod);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(5, { | val |
	var unPitchLength = val.linlin(0, 127, 0.001, 0.1);
	~conc.setUnPitchLength(unPitchLength);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(6, { | val |
	var unPitchMod = val.linlin(0, 127, 0.0005, 0.05);
	~conc.setUnPitchModRange(unPitchMod);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(7, { | val |
	var pitchLength = val.linlin(0, 127, 0.005, 0.5);
	~conc.setPitchLength(pitchLength);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(8, { | val |
	var pitchMod = val.linlin(0, 127, 0.001, 0.3);
	~conc.setPitchModRange(pitchMod);
}, bank: main, page: page);

/*
things to snag:
- post lowpass
- post highpass
- detune
- freq
- mod freq
- glitch amp mod
- unpitch amp mod
- glitch length
- glitch mod length
- unPitchLength
- unPitch Mod Length
- pitch Length
- pitch ModLength
- lfo rate
*/



//////////////////
///// grid: /////
////////////////

freqArray = [1, 0.5, 2, 0.25, 0.75, 1.5, 4, 0.125];
freq1Reset = { 8.do({ | i | ~apc.turnGridBlue(i, 4, 1, bank: main, page: page) }); };
freq2Reset = { 8.do({ | i | ~apc.turnGridLightBlue(i, 3, 1, bank: main, page: page) }); };
freq3Reset = { 8.do({ | i | ~apc.turnGridCyan(i, 2, 1, bank: main, page: page) }); };
freq4Reset = { 8.do({ | i | ~apc.turnGridMagenta(i, 1, 1, bank: main, page: page) }); };
freq5Reset = { 8.do({ | i | ~apc.turnGridPurple(i, 0, 1, bank: main, page: page) }); };

freq1Reset.value; freq2Reset.value; freq3Reset.value; freq4Reset.value; freq5Reset.value;
~apc.turnGridGreen(0, 4, 2, main, page);
~apc.turnGridGreen(0, 3, 2, main, page);
~apc.turnGridGreen(0, 2, 2, main, page);
~apc.turnGridGreen(1, 1, 2, main, page);
~apc.turnGridGreen(2, 0, 2, main, page);

// freq 1 mul:
8.do({ | i | ~apc.setGridFunc(i, 4, {
	~conc.setFreqMul1(freqArray[i]);
	freq1Reset.value;
	~apc.turnGridGreen(i, 4);
}, bank: main, page: page);
});
// freq 2 mul:
8.do({ | i | ~apc.setGridFunc(i, 3, {
	~conc.setFreqMul2(freqArray[i]);
	freq2Reset.value;
	~apc.turnGridGreen(i, 3);
}, bank: main, page: page);
});
// freq 3 mul:
8.do({ | i | ~apc.setGridFunc(i, 2, {
	~conc.setFreqMul3(freqArray[i]);
	freq3Reset.value;
	~apc.turnGridGreen(i, 2);
}, bank: main, page: page);
});
// freq 4 mul:
8.do({ | i | ~apc.setGridFunc(i, 1, {
	~conc.setFreqMul4(freqArray[i]);
	freq4Reset.value;
	~apc.turnGridGreen(i, 1);
}, bank: main, page: page);
});
// freq 5 mul:
8.do({ | i | ~apc.setGridFunc(i, 0, {
	~conc.setFreqMul5(freqArray[i]);
	freq5Reset.value;
	~apc.turnGridGreen(i, 0);
}, bank: main, page: page);
});

//////// Scene Launch:
~apc.setSceneLaunchMonitorFunc(\ampTrack, {
	if( ~conc.glitchAmpIsTracking == true,
		{ ~apc.turnSceneLaunchButtonWhite(0) }, { ~apc.turnSceneLaunchButtonPink(0) });
}, bank: main, page: page);
~apc.setSceneLaunchFunc(0, { ~conc.toggleGlitchTrackAmp }, bank: main, page: page);

//////// jump to huge:
~apc.setSceneLaunchMonitorFunc(\hugeShortcut, {
	if( try { ~huge.isLoaded } == true,
		{ ~apc.setSceneLaunchFunc(3, { ~apc.setPage('huge') }, page: page); ~apc.turnSceneLaunchButtonGreen(3, page: page); },
		{ ~apc.setSceneLaunchFunc(3, { }, page: page); ~apc.turnSceneLaunchButtonOff(3, page: page); });
}, page: page);
