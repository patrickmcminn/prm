/*
Tuesday, March 31st 2020
Subtractive Amplitude Envelope Bank
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

var amp1Invert = false;
var pan1Invert = false;
var amp2Invert = false;
var pan2Invert = false;

var noiseVol1Invert = false;
var noiseCutoff1Invert = false;
var noiseVol2Invert = false;
var noiseCutoff2Invert = false;

~apc.setMixerEncodersMonitorFunc(\env, {
	var noiseVolLFO1, noiseCutoffLFO1, noiseVolLFO2, noiseCutoffLFO2;
	var pan = ~prm.subtractive.pan.linlin(-1, 1, 0, 127);
	var cutoff = ~prm.subtractive.filterCutoff.explin(50, 20000, 0, 127);
	var noiseVol = ~prm.subtractive.noiseOscAmp.ampdb.dbfsCC;
	var noiseCutoff = ~prm.subtractive.noiseOscCutoff.explin(50, 20000, 0, 127);
	if( noiseVol1Invert == false,
		{ noiseVolLFO1 = ~prm.subtractive.noiseOscAmpLFO1Bottom.linlin(0, 1, 127, 0) },
		{ noiseVolLFO1 = ~prm.subtractive.noiseOscAmpLFO1Top.linlin(0, 1, 127, 0); });
	if( noiseCutoff1Invert == false,
		{ noiseCutoffLFO1 = ~prm.subtractive.noiseOscFilterLFO1TopRatio.linlin(1, 4, 0, 127); },
		{ noiseCutoffLFO1 = ~prm.subtractive.noiseOscFilterLFO1BottomRatio.linlin(1, 4, 0, 127) });
	if( noiseVol2Invert == false,
		{ noiseVolLFO2 = ~prm.subtractive.noiseOscAmpLFO2Bottom.linlin(0, 1, 127, 0) },
		{ noiseVolLFO2 = ~prm.subtractive.noiseOscAmpLFO2Top.linlin(0, 1, 127, 0); });
	if( noiseCutoff2Invert == false,
		{ noiseCutoffLFO2 = ~prm.subtractive.noiseOscFilterLFO2TopRatio.linlin(1, 4, 0, 127); },
		{ noiseCutoffLFO2 = ~prm.subtractive.noiseOscFilterLFO2BottomRatio.linlin(1, 4, 0, 127) });

	~apc.setMixerEncoderValue(1, pan);
	~apc.setMixerEncoderValue(2, cutoff);
	~apc.setMixerEncoderValue(3, noiseVol);
	~apc.setMixerEncoderValue(4, noiseCutoff);
	~apc.setMixerEncoderValue(5, noiseVolLFO1);
	~apc.setMixerEncoderValue(6, noiseCutoffLFO1);
	~apc.setMixerEncoderValue(7, noiseVolLFO2);
	~apc.setMixerEncoderValue(8, noiseCutoffLFO2);
}, envBank, page);

~apc.setMixerEncoderFunc(1, { | val |
	var pan = val.linlin(0, 127, -1, 1);
	~prm.subtractive.setPan(pan);
}, envBank, page);
~apc.setMixerEncoderFunc(2, { | val |
	var cutoff = val.linexp(0, 127, 50, 20000);
	~prm.subtractive.setFilterCutoff(cutoff);
}, envBank, page);
~apc.setMixerEncoderFunc(3, { | val |
	~prm.subtractive.setNoiseOscVol(val.ccdbfs);
}, envBank, page);
~apc.setMixerEncoderFunc(4, { | val |
	var cutoff = val.linexp(0, 127, 50, 20000);
	~prm.subtractive.setNoiseOscCutoff(cutoff);
}, envBank, page);
~apc.setMixerEncoderFunc(5, { | val |
	var low, high, bottom, top;
	low = val.linlin(0, 127, 1, 0);
	high = 1;
	if( noiseVol1Invert == false, { bottom = low; top = high; }, { bottom = high; top = low; });
	~prm.subtractive.setNoiseOscAmpLFO1Bottom(bottom);
	~prm.subtractive.setNoiseOscAmpLFO1Top(top);
}, envBank, page);
~apc.setMixerEncoderFunc(6, { | val |
	var low, high, bottom, top;
	low = val.linlin(0, 127, 1, 0.25);
	high = val.linlin(0, 127, 1, 4);
	if( noiseCutoff1Invert == false, { bottom = low; top = high; }, { bottom = high; top = low; });
	~prm.subtractive.setNoiseOscFilterLFO1BottomRatio(bottom);
	~prm.subtractive.setNoiseOscFilterLFO1TopRatio(top);
}, envBank, page);
~apc.setMixerEncoderFunc(7, { | val |
	var low, high, bottom, top;
	low = val.linlin(0, 127, 1, 0);
	high = 1;
	if( noiseVol2Invert == false, { bottom = low; top = high; }, { bottom = high; top = low; });
	~prm.subtractive.setNoiseOscAmpLFO2Bottom(bottom);
	~prm.subtractive.setNoiseOscAmpLFO2Top(top);
}, envBank, page);
~apc.setMixerEncoderFunc(8, { | val |
	var low, high, bottom, top;
	low = val.linlin(0, 127, 1, 0.25);
	high = val.linlin(0, 127, 1, 4);
	if( noiseCutoff2Invert == false, { bottom = low; top = high; }, { bottom = high; top = low; });
	~prm.subtractive.setNoiseOscFilterLFO2BottomRatio(bottom);
	~prm.subtractive.setNoiseOscFilterLFO2TopRatio(top);
}, envBank, page);

~apc.setDeviceEncodersMonitorFunc(\env, {
	var ampLFO1, panLFO1, ampLFO2, panLFO2;
	var attack = ~prm.subtractive.attackTime.explin(0.001, 10, 0, 127);
	var decay = ~prm.subtractive.decayTime.explin(0.001, 7, 0, 127);
	var sustain = ~prm.subtractive.sustainLevel.linlin(0, 1, 0, 127);
	var release = ~prm.subtractive.releaseTime.explin(0.001, 15, 0, 127);

	if( amp1Invert == false,
		{ ampLFO1 = ~prm.subtractive.ampLFO1Bottom.linlin(0, 1, 127, 0); },
		{ ampLFO1 = ~prm.subtractive.ampLFO1Top.linlin(0, 1, 127, 0); });
	if( pan1Invert == false,
		{ panLFO1 = ~prm.subtractive.panLFO1Top.linlin(0, 1, 0, 127) },
		{ panLFO1 = ~prm.subtractive.panLFO1Bottom.linlin(0, 1, 0, 127) });
	if( amp2Invert == false,
		{ ampLFO2 = ~prm.subtractive.ampLFO2Bottom.linlin(0, 1, 127, 0); },
		{ ampLFO2 = ~prm.subtractive.ampLFO2Top.linlin(0, 1, 127, 0); });
	if( pan2Invert == false,
		{ panLFO2 = ~prm.subtractive.panLFO2Top.linlin(0, 1, 0, 127) },
		{ panLFO2 = ~prm.subtractive.panLFO2Bottom.linlin(0, 1, 0, 127) });

	~apc.setDeviceEncoderValue(1, ampLFO1);
	~apc.setDeviceEncoderValue(2, panLFO1);
	~apc.setDeviceEncoderValue(3, ampLFO2);
	~apc.setDeviceEncoderValue(4, panLFO2);
	~apc.setDeviceEncoderValue(5, attack);
	~apc.setDeviceEncoderValue(6, decay);
	~apc.setDeviceEncoderValue(7, sustain);
	~apc.setDeviceEncoderValue(8, release);
}, envBank, page);

// amp lfo 1:
~apc.setDeviceEncoderFunc(1, { | val |
	var high, low, top, bottom;
	high = 1;
	low = val.linlin(0, 127, 1, 0);
	if( amp1Invert == false, { top = high; bottom = low; }, { top = low; bottom = high; });
	~prm.subtractive.setAmplitudeLFO1Bottom(bottom);
	~prm.subtractive.setAmplitudeLFO1Top(top);
}, bank: envBank, page: page);
// pan lfo 1:
~apc.setDeviceEncoderFunc(2, { | val |
	var high, low, top, bottom;
	high = val.linlin(0, 127, 0, 1);
	low = val.linlin(0, 127, 0, -1);
	if ( pan1Invert == false, { top = high; bottom = low; }, { top = low; bottom = high;});
	~prm.subtractive.setPanLFO1Bottom(bottom);
	~prm.subtractive.setPanLFO1Top(top);
}, bank: envBank, page: page);
// amp lfo 2:
~apc.setDeviceEncoderFunc(3, { | val |
	var high, low, top, bottom;
	high = 1;
	low = val.linlin(0, 127, 1, 0);
	if( amp2Invert == false, { top = high; bottom = low; }, { top = low; bottom = high; });
	~prm.subtractive.setAmplitudeLFO2Bottom(bottom);
	~prm.subtractive.setAmplitudeLFO2Top(top);
}, bank: envBank, page: page);
// pan lfo 2:
~apc.setDeviceEncoderFunc(4, { | val |
	var high, low, top, bottom;
	high = val.linlin(0, 127, 0, 1);
	low = val.linlin(0, 127, 0, -1);
	if ( pan2Invert == false, { top = high; bottom = low; }, { top = low; bottom = high;});
	~prm.subtractive.setPanLFO2Bottom(bottom);
	~prm.subtractive.setPanLFO2Top(top);
}, bank: envBank, page: page);
// attack time:
~apc.setDeviceEncoderFunc(5, { | val |
	var attack = val.linexp(0, 127, 0.001, 10);
	~prm.subtractive.setAttackTime(attack);
}, envBank, page);
// decay time:
~apc.setDeviceEncoderFunc(6, { | val |
	var decay = val.linexp(0, 127, 0.001, 7);
	~prm.subtractive.setDecayTime(decay);
}, envBank, page);
// sustain level:
~apc.setDeviceEncoderFunc(7, { | val |
	var sustain = val.linlin(0, 127, 0, 1);
	~prm.subtractive.setSustainLevel(sustain);
}, envBank, page);
// release time:
~apc.setDeviceEncoderFunc(8, { | val |
	var release = val.linexp(0, 127, 0.001, 15);
	~prm.subtractive.setReleaseTime(release);
}, envBank, page);


///////////////
//// GRID ////
/////////////

// LFO Inverts:

4.do({ | i | ~apc.turnGridYellow(i, 0, 2, envBank, page); });
~apc.setGridFunc(0, 0, { if (amp1Invert == false,
	{ amp1Invert = true; ~apc.turnGridWhite(0, 0); },
	{ amp1Invert = false; ~apc.turnGridYellow(0, 0); });
}, bank: envBank, page: page);
~apc.setGridFunc(1, 0, { if( pan1Invert == false,
	{ pan1Invert = true; ~apc.turnGridWhite(1, 0) },
	{ pan1Invert = false; ~apc.turnGridYellow(1, 0) });
}, bank: envBank, page: page);
~apc.setGridFunc(2, 0, { if (amp2Invert == false,
	{ amp2Invert = true; ~apc.turnGridWhite(2, 0); },
	{ amp2Invert = false; ~apc.turnGridYellow(2, 0); });
}, bank: envBank, page: page);
~apc.setGridFunc(3, 0, { if( pan2Invert == false,
	{ pan2Invert = true; ~apc.turnGridWhite(3, 0) },
	{ pan2Invert = false; ~apc.turnGridYellow(3, 0) });
}, bank: envBank, page: page);
