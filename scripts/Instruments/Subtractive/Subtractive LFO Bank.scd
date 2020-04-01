/*
Wednesday, April 1st 2020
Subtractive LFO Bank.scd
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

var lfo1lfo2Invert = false;

~apc.setMixerEncodersMonitorFunc(\lfo, {
	var lfo2Freq = ~prm.subtractive.lfo2Freq.explin(0.001, 55, 0, 127);
	var lfo2Waveform = ~prm.subtractive.lfo2Waveform.linlin(0, 5, 0, 127);
	var lfo2PulseWidth = ~prm.subtractive.lfo2PulseWidth.linlin(0, 1, 0, 127);

	~apc.setMixerEncoderValue(1, lfo2Freq);
	~apc.setMixerEncoderValue(2, lfo2Waveform);
	~apc.setMixerEncoderValue(3, lfo2PulseWidth);
}, lfoBank, page);
~apc.setMixerEncoderFunc(1, { | val |
	var freq = val.linexp(0, 127, 0.001, 55);
	~prm.subtractive.setLFO2Freq(freq);
}, lfoBank, page);
~apc.setMixerEncoderFunc(2, { | val |
	var waveform = val.linlin(0, 127, 0, 5);
	~prm.subtractive.setLFO2Waveform(waveform);
}, lfoBank, page);
~apc.setMixerEncoderFunc(3, { | val |
	var width = val.linlin(0, 127, 0, 1);
	~prm.subtractive.setLFO2PulseWidth(width);
}, lfoBank, page);

~apc.setDeviceEncodersMonitorFunc(\lfo, {
	var lfo2;
	var freq = ~prm.subtractive.lfo1Freq.explin(0.001, 55, 0, 127);
	var waveform = ~prm.subtractive.lfo1Waveform.linlin(0, 5, 0, 127);
	var width = ~prm.subtractive.lfo1PulseWidth.linlin(0, 1, 0, 127);

	var attack = ~prm.subtractive.lfo1AttackTime.explin(0.1, 10, 0, 127);
	var release = ~prm.subtractive.lfo1ReleaseTime.explin(0.1, 10, 0, 127);

	if(lfo1lfo2Invert == false,
		{ lfo2 = ~prm.subtractive.lfo1FreqLFO2TopRatio.linlin(1, 4, 0, 127) },
		{ lfo2 = ~prm.subtractive.lfo1FreqLFO2BottomRatio.linlin(1, 4, 0, 127) });

	~apc.setDeviceEncoderValue(1, freq);
	~apc.setDeviceEncoderValue(2, waveform);
	~apc.setDeviceEncoderValue(3, width);

	~apc.setDeviceEncoderValue(4, attack);
	~apc.setDeviceEncoderValue(5, release);
	~apc.setDeviceEncoderValue(6, lfo2);
}, lfoBank, page);

~apc.setDeviceEncoderFunc(1, { | val |
	var freq = val.linexp(0, 127, 0.001, 55);
	~prm.subtractive.setLFO1Freq(freq);
}, lfoBank, page);
~apc.setDeviceEncoderFunc(2, { | val |
	var waveform = val.linlin(0, 127, 0, 5);
	~prm.subtractive.setLFO1Waveform(waveform);
}, lfoBank, page);
~apc.setDeviceEncoderFunc(3, { | val |
	var width = val.linlin(0, 127, 0, 1);
	~prm.subtractive.setLFO1PulseWidth(width);
}, lfoBank, page);

~apc.setDeviceEncoderFunc(5, { | val |
	var attack = val.linexp(0, 127, 0.1, 10);
	~prm.subtractive.setLFO1AttackTime(attack);
}, lfoBank, page);
~apc.setDeviceEncoderFunc(6, { | val |
	var release = val.linexp(0, 127, 0.1, 10);
	~prm.subtractive.setLFO1ReleaseTime(release);
}, lfoBank, page);
~apc.setDeviceEncoderFunc(7, { | val |
	var low, high, bottom, top;
	low = val.linlin(0, 127, 1, 0.25);
	high = val.linlin(0, 127, 1, 4);
	if( lfo1lfo2Invert == false, { bottom = low; top = high; }, { bottom = high; top = low; });
	~prm.subtractive.setLFO1FreqLFO2BottomRatio(bottom);
	~prm.subtractive.setLFO1FreqLFO2TopRatio(top);
}, lfoBank, page);

//////////////////
//// GRID: //////
////////////////

~apc.setGridMonitorFunc(\envType, {
	if( ~prm.subtractive.lfo1EnvType == 0, { ~apc.turnGridGreen(0, 4) }, { ~apc.turnGridMagenta(0, 4) });
	if( ~prm.subtractive.lfo1EnvType == 1, { ~apc.turnGridGreen(1, 4) }, { ~apc.turnGridMagenta(1, 4) });
	if( ~prm.subtractive.lfo1EnvType == 2, { ~apc.turnGridGreen(2, 4) }, { ~apc.turnGridMagenta(2, 4) });
	if( ~prm.subtractive.lfo1EnvType == 3, { ~apc.turnGridGreen(3, 4) }, { ~apc.turnGridMagenta(3, 4) });
}, lfoBank, page);
~apc.setGridFunc(0, 4, { ~prm.subtractive.setLFO1EnvType('none') }, bank: lfoBank, page: page);
~apc.setGridFunc(1, 4, { ~prm.subtractive.setLFO1EnvType('attack') }, bank: lfoBank, page: page);
~apc.setGridFunc(2, 4, { ~prm.subtractive.setLFO1EnvType('release') }, bank: lfoBank, page: page);
~apc.setGridFunc(3, 4, { ~prm.subtractive.setLFO1EnvType('attackAndRelease') }, bank: lfoBank, page: page);

///// LFO 1 Waveform:
~apc.setGridMonitorFunc(\lfo1, {
	if( ~prm.subtractive.lfo1Waveform == 0, { ~apc.turnGridGreen(0, 2) }, { ~apc.turnGridPink(0, 2); });
	if( ~prm.subtractive.lfo1Waveform == 1, { ~apc.turnGridGreen(1, 2) }, { ~apc.turnGridPink(1, 2); });
	if( ~prm.subtractive.lfo1Waveform == 2, { ~apc.turnGridGreen(2, 2) }, { ~apc.turnGridPink(2, 2); });
	if( ~prm.subtractive.lfo1Waveform == 3, { ~apc.turnGridGreen(3, 2) }, { ~apc.turnGridPink(3, 2); });
	if( ~prm.subtractive.lfo1Waveform == 4, { ~apc.turnGridGreen(4, 2) }, { ~apc.turnGridPink(4, 2); });
	if( ~prm.subtractive.lfo1Waveform == 5, { ~apc.turnGridGreen(5, 2) }, { ~apc.turnGridPink(5, 2); });
}, lfoBank, page);
~apc.setGridFunc(0, 2, { ~prm.subtractive.setLFO1Waveform(0) }, bank: lfoBank, page: page);
~apc.setGridFunc(1, 2, { ~prm.subtractive.setLFO1Waveform(1) }, bank: lfoBank, page: page);
~apc.setGridFunc(2, 2, { ~prm.subtractive.setLFO1Waveform(2) }, bank: lfoBank, page: page);
~apc.setGridFunc(3, 2, { ~prm.subtractive.setLFO1Waveform(3) }, bank: lfoBank, page: page);
~apc.setGridFunc(4, 2, { ~prm.subtractive.setLFO1Waveform(4) }, bank: lfoBank, page: page);
~apc.setGridFunc(5, 2, { ~prm.subtractive.setLFO1Waveform(5) }, bank: lfoBank, page: page);


///// LFO 2 Waveform:
~apc.setGridMonitorFunc(\lfo2, {
	if( ~prm.subtractive.lfo2Waveform == 0, { ~apc.turnGridGreen(0, 1) }, { ~apc.turnGridPurple(0, 1); });
	if( ~prm.subtractive.lfo2Waveform == 1, { ~apc.turnGridGreen(1, 1) }, { ~apc.turnGridPurple(1, 1); });
	if( ~prm.subtractive.lfo2Waveform == 2, { ~apc.turnGridGreen(2, 1) }, { ~apc.turnGridPurple(2, 1); });
	if( ~prm.subtractive.lfo2Waveform == 3, { ~apc.turnGridGreen(3, 1) }, { ~apc.turnGridPurple(3, 1); });
	if( ~prm.subtractive.lfo2Waveform == 4, { ~apc.turnGridGreen(4, 1) }, { ~apc.turnGridPurple(4, 1); });
	if( ~prm.subtractive.lfo2Waveform == 5, { ~apc.turnGridGreen(5, 1) }, { ~apc.turnGridPurple(5, 1); });
}, lfoBank, page);
~apc.setGridFunc(0, 1, { ~prm.subtractive.setLFO2Waveform(0) }, bank: lfoBank, page: page);
~apc.setGridFunc(1, 1, { ~prm.subtractive.setLFO2Waveform(1) }, bank: lfoBank, page: page);
~apc.setGridFunc(2, 1, { ~prm.subtractive.setLFO2Waveform(2) }, bank: lfoBank, page: page);
~apc.setGridFunc(3, 1, { ~prm.subtractive.setLFO2Waveform(3) }, bank: lfoBank, page: page);
~apc.setGridFunc(4, 1, { ~prm.subtractive.setLFO2Waveform(4) }, bank: lfoBank, page: page);
~apc.setGridFunc(5, 1, { ~prm.subtractive.setLFO2Waveform(5) }, bank: lfoBank, page: page);

// Invert:
~apc.turnGridYellow(0, 0, bank: lfoBank, page: page);
~apc.setGridFunc(0, 0, { if( lfo1lfo2Invert == false,
	{ lfo1lfo2Invert = true; ~apc.turnGridWhite(0, 0) },
	{ lfo1lfo2Invert = false; ~apc.turnGridYellow(0, 0); });
}, bank: lfoBank, page: page);