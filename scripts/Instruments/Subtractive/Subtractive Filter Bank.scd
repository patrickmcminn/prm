/*
Monday, March 30th 2020
Subtractive Filter Bank.scd
prm
*/

var page = 'subtractive';

var mainGridBank = 0;
var keyboardGridBank = 1;

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

var cutoff1Invert = false;
var cutoff2Invert = false;
var res1Invert = false;
var res2Invert = false;

//////////////// mixer encoder funcs:
~apc.setMixerEncodersMonitorFunc(\filter, {
	var attackR = ~prm.subtractive.filterEnvAttackRatio.explin(0.25, 4, 0, 127);
	var peakR = ~prm.subtractive.filterEnvPeakRatio.explin(0.125, 8, 0, 127);
	var sustainR = ~prm.subtractive.filterEnvSustainRatio.explin(0.125, 8, 0, 127);
	var releaseR = ~prm.subtractive.filterEnvReleaseRatio.explin(0.25, 4, 0, 127);
	var attackT = ~prm.subtractive.filterEnvAttackTime.explin(0.001, 10, 0, 127);
	var decayT = ~prm.subtractive.filterEnvDecayTime.explin(0.001, 7, 0, 127);
	var releaseT = ~prm.subtractive.filterEnvReleaseTime.explin(0.001, 10, 0, 127);
	var drive = ~prm.subtractive.filterDrive.explin(1, 100, 0, 127);
	~apc.setMixerEncoderValue(1, attackR);
	~apc.setMixerEncoderValue(2, peakR);
	~apc.setMixerEncoderValue(3, sustainR);
	~apc.setMixerEncoderValue(4, releaseR);
	~apc.setMixerEncoderValue(5, attackT);
	~apc.setMixerEncoderValue(6, decayT);
	~apc.setMixerEncoderValue(7, releaseT);
	~apc.setMixerEncoderValue(8, drive);
}, filterBank, page);
// attack ratio:
~apc.setMixerEncoderFunc(1, { | val |
	var ratio = val.linexp(0, 127, 0.25, 4);
	~prm.subtractive.setFilterEnvAttackRatio(ratio);
}, filterBank, page);
// decay ratio:
~apc.setMixerEncoderFunc(2, { | val |
	var ratio = val.linexp(0, 127, 0.125, 8);
	~prm.subtractive.setFilterEnvPeakRatio(ratio);
}, filterBank, page);
// peak ratio:
~apc.setMixerEncoderFunc(3, { | val |
	var ratio = val.linexp(0, 127, 0.125, 8);
	~prm.subtractive.setFilterEnvSustainRatio(ratio);
}, filterBank, page);
// release ratio:
~apc.setMixerEncoderFunc(4, { | val |
	var ratio = val.linexp(0, 127, 0.25, 4);
	~prm.subtractive.setFilterEnvReleaseRatio(ratio);
}, filterBank, page);
// attack time:
~apc.setMixerEncoderFunc(5, { | val |
	var time = val.linexp(0, 127, 0.001, 10);
	~prm.subtractive.setFilterEnvAttackTime(time);
}, filterBank, page);
// decay time:
~apc.setMixerEncoderFunc(6, { | val |
	var time = val.linexp(0, 127, 0.001, 7);
	~prm.subtractive.setFilterEnvDecayTime(time);
}, filterBank, page);
// release time:
~apc.setMixerEncoderFunc(7, { | val |
	var time = val.linexp(0, 127, 0.001, 10);
	~prm.subtractive.setFilterEnvReleaseTime(time);
}, filterBank, page);
// filter drive:
~apc.setMixerEncoderFunc(8, { | val |
	var drive = val.linexp(0, 127, 1, 100);
	~prm.subtractive.setFilterDrive(drive);
}, filterBank, page);



//////////////// device encoder funcs:
~apc.setDeviceEncodersMonitorFunc(\filter, {
	var cutoff1, cutoff2, res1, res2;
	var cutoff = ~prm.subtractive.filterCutoff.explin(50, 20000, 0, 127);
	var res = ~prm.subtractive.filterRes.linlin(0, 1.8, 0, 127);
	var lfo1 = ~prm.subtractive.lfo1Freq.explin(0.001, 55, 0, 127);
	var lfo2 = ~prm.subtractive.lfo2Freq.explin(0.001, 55, 0, 127);
	if( cutoff1Invert == true,
		{ cutoff1 = ~prm.subtractive.filterCutoffLFO1BottomRatio.linlin(1, 4, 0, 127); },
		{ cutoff1 = ~prm.subtractive.filterCutoffLFO1TopRatio.linlin(1, 4, 0, 127) });
	if( res1Invert == true,
		{ res1 = ~prm.subtractive.filterResLFO1Bottom.linlin(0, 1.2, 0, 127); },
		{ res1 = ~prm.subtractive.filterResLFO1Top.linlin(0, 1.2, 0, 127); });
	if( cutoff2Invert == true,
		{ cutoff2 = ~prm.subtractive.filterCutoffLFO2BottomRatio.linlin(1, 4, 0, 127); },
		{ cutoff2 = ~prm.subtractive.filterCutoffLFO2TopRatio.linlin(1, 4, 0, 127) });
	if( res2Invert == true,
		{ res2 = ~prm.subtractive.filterResLFO2Bottom.linlin(0, 1.2, 0, 127); },
		{ res2 = ~prm.subtractive.filterResLFO2Top.linlin(0, 1.2, 0, 127); });
	~apc.setDeviceEncoderValue(1, cutoff);
	~apc.setDeviceEncoderValue(2, res);
	~apc.setDeviceEncoderValue(3, lfo1);
	~apc.setDeviceEncoderValue(4, lfo2);
	~apc.setDeviceEncoderValue(5, cutoff1);
	~apc.setDeviceEncoderValue(6, res1);
	~apc.setDeviceEncoderValue(7, cutoff2);
	~apc.setDeviceEncoderValue(8, res2);
}, filterBank, page);
// cutoff:
~apc.setDeviceEncoderFunc(1, { | val |
	var cutoff = val.linexp(0, 127, 50, 20000);
	~prm.subtractive.setFilterCutoff(cutoff);
}, filterBank, page);
// resonance:
~apc.setDeviceEncoderFunc(2, { | val |
	var res = val.linlin(0, 127, 0, 1.8);
	~prm.subtractive.setFilterRes(res);
}, filterBank, page);
// lfo 1 freq:
~apc.setDeviceEncoderFunc(3, { | val |
	var freq = val.linexp(0, 127, 0.001, 55);
	~prm.subtractive.setLFO1Freq(freq);
}, filterBank, page);
// lfo 2 freq:
~apc.setDeviceEncoderFunc(4, { | val |
	var freq = val.linexp(0, 127, 0.001, 55);
	~prm.subtractive.setLFO2Freq(freq);
}, filterBank, page);
// cutoff lfo 1:
~apc.setDeviceEncoderFunc(5, { | val |
	var high, low, top, bottom;
	high = val.linlin(0, 127, 1, 4);
	low = val.linlin(0, 127, 1, 0.25);
	if( cutoff1Invert == true, { top = low; bottom = high; }, { top = high; bottom = low; });
	~prm.subtractive.setFilterCutoffLFO1BottomRatio(bottom);
	~prm.subtractive.setFilterCutoffLFO1TopRatio(top);
}, filterBank, page);
// res lfo 1:
~apc.setDeviceEncoderFunc(6, { | val |
	var high, low, top, bottom;
	high = val.linlin(0, 127, 0, 1.2);
	low = 0;
	if( res1Invert == true, { top = low; bottom = high; }, { top = high; bottom = low; });
	~prm.subtractive.setFilterResLFO1Bottom(bottom);
	~prm.subtractive.setFilterResLFO1Top(top);
}, filterBank, page);
// cutoff lfo 2:
~apc.setDeviceEncoderFunc(7, { | val |
	var high, low, top, bottom;
	high = val.linlin(0, 127, 1, 4);
	low = val.linlin(0, 127, 1, 0.25);
	if( cutoff2Invert == true, { top = low; bottom = high; }, { top = high; bottom = low; });
	~prm.subtractive.setFilterCutoffLFO2BottomRatio(bottom);
	~prm.subtractive.setFilterCutoffLFO2TopRatio(top);
}, filterBank, page);
// res lfo 2:
~apc.setDeviceEncoderFunc(8, { | val |
	var high, low, top, bottom;
	high = val.linlin(0, 127, 0, 1.2);
	low = 0;
	if( res2Invert == true, { top = low; bottom = high; }, { top = high; bottom = low; });
	~prm.subtractive.setFilterResLFO2Bottom(bottom);
	~prm.subtractive.setFilterResLFO2Top(top);
}, filterBank, page);


//////////////////
//// GRID: //////
////////////////

///// Filter Type:
~apc.setGridMonitorFunc(\filter, {
	if( ~prm.subtractive.filterType == 0, { ~apc.turnGridGreen(0, 0) }, { ~apc.turnGridWhite(0, 0) });
	if( ~prm.subtractive.filterType == 1, { ~apc.turnGridGreen(1, 0) }, { ~apc.turnGridWhite(1, 0) });
	if( ~prm.subtractive.filterType == 2, { ~apc.turnGridGreen(2, 0) }, { ~apc.turnGridWhite(2, 0) });
	if( ~prm.subtractive.filterEnvLoop == 1, { ~apc.turnGridGreen(7, 4) }, { ~apc.turnGridCyan(7, 4); });
}, filterBank, page);
~apc.setGridFunc(0, 0, { ~prm.subtractive.setFilterType('lowPass') }, bank: filterBank, page: page);
~apc.setGridFunc(1, 0, { ~prm.subtractive.setFilterType('highPass') }, bank: filterBank, page: page);
~apc.setGridFunc(2, 0, { ~prm.subtractive.setFilterType('bandPass') }, bank: filterBank, page: page);

///// Envelope Reset:
4.do({ | i | ~apc.turnGridYellowGreen(i, 4, 2, filterBank, page); });
// attack ratio reset:
~apc.setGridFunc(0, 4,
	{ ~prm.subtractive.setFilterEnvAttackRatio(1.0);
		~apc.turnGridWhite(0, 4);
}, 'noteOn', filterBank, page);
~apc.setGridFunc(0, 4, { ~apc.turnGridYellowGreen(0, 4) }, 'noteOff', filterBank, page);
// peak ratio:
~apc.setGridFunc(1, 4, {
	~prm.subtractive.setFilterEnvPeakRatio(1.0);
	~apc.turnGridWhite(1, 4);
}, 'noteOn', filterBank, page);
~apc.setGridFunc(1, 4, { ~apc.turnGridYellowGreen(1, 4) }, 'noteOff', filterBank, page);
// sustain ratio:
~apc.setGridFunc(2, 4, {
	~prm.subtractive.setFilterEnvSustainRatio(1.0);
	~apc.turnGridWhite(2, 4);
}, 'noteOn', filterBank, page);
~apc.setGridFunc(2, 4, { ~apc.turnGridYellowGreen(2, 4) }, 'noteOff', filterBank, page);
// release ratio:
~apc.setGridFunc(3, 4, {
	~prm.subtractive.setFilterEnvReleaseRatio(1.0);
	~apc.turnGridWhite(3, 4);
}, 'noteOn', filterBank, page);
~apc.setGridFunc(3, 4, { ~apc.turnGridYellowGreen(3, 4) }, 'noteOff', filterBank, page);

//// Filter Loop:
~apc.setGridFunc(7, 4, { if( ~prm.subtractive.filterEnvLoop == 1,
	{ ~prm.subtractive.setFilterEnvLoop(false) }, { ~prm.subtractive.setFilterEnvLoop(true) });
}, bank: filterBank, page: page);


///// Filter LFO Invert:
4.do({ | i | ~apc.turnGridYellow(i+4, 0, 2, filterBank, page); });
~apc.setGridFunc(4, 0, {
	if( cutoff1Invert == true,
		{ cutoff1Invert = false; ~apc.turnGridYellow(4, 0); },
		{ cutoff1Invert = true; ~apc.turnGridWhite(4, 0); });
}, bank: filterBank, page: page);
~apc.setGridFunc(5, 0, {
	if( res1Invert == true,
		{ res1Invert = false; ~apc.turnGridYellow(5, 0) },
		{ res1Invert = true; ~apc.turnGridWhite(5, 0) });
}, bank: filterBank, page: page);
~apc.setGridFunc(6, 0, {
	if ( cutoff2Invert == true,
		{ cutoff2Invert = false; ~apc.turnGridYellow(6, 0); },
		{ cutoff2Invert = true; ~apc.turnGridWhite(6, 0) });
}, bank: filterBank, page: page);
~apc.setGridFunc(7, 0, {
	if( res2Invert == true,
		{ res2Invert = false; ~apc.turnGridYellow(7, 0); },
		{ res2Invert = true; ~apc.turnGridWhite(7, 0); });
}, bank: filterBank, page: page);