/*
Monday, March 30th 2020
Subtractive Osc Banks.scd
prm

Banks for Oscillators 1 + 2
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

var osc1Freq1Invert = false;
var osc1Freq2Invert = false;
var osc1Waveform1Invert = false;
var osc1Waveform2Invert = false;
var osc1Amp1Invert = false;
var osc1Amp2Invert = false;
var osc1pw1Invert = false;
var osc1pw2Invert = false;

var osc2Freq1Invert = false;
var osc2Freq2Invert = false;
var osc2Waveform1Invert = false;
var osc2Waveform2Invert = false;
var osc2Amp1Invert = false;
var osc2Amp2Invert = false;
var osc2pw1Invert = false;
var osc2pw2Invert = false;

//////////////////////////
//// Oscillator 1:  ////
///////////////////////

////////////// mixer encoders:

~apc.setMixerEncodersMonitorFunc(\osc1, {
	var vol = ~prm.subtractive.osc1Amp.ampdb.dbfsCC;
	var waveform = ~prm.subtractive.osc1Waveform.linlin(0, 3, 0, 127);
	var pulseWidth = ~prm.subtractive.osc1PulseWidth.linlin(0, 1, 0, 127);
	var startRatio = ~prm.subtractive.osc1FreqEnvStartRatio.explin(0.1, 10, 0, 127);
	var endRatio = ~prm.subtractive.osc1FreqEnvEndRatio.explin(0.1, 10, 0, 127);
	var time = ~prm.subtractive.osc1FreqEnvTime.explin(0.05, 30, 0, 127);
	var lfo1 = ~prm.subtractive.lfo1Freq.explin(0.001, 55, 0, 127);
	var lfo2 = ~prm.subtractive.lfo2Freq.explin(0.001, 55, 0, 127);
	~apc.setMixerEncoderValue(1, vol);
	~apc.setMixerEncoderValue(2, waveform);
	~apc.setMixerEncoderValue(3, pulseWidth);
	~apc.setMixerEncoderValue(4, startRatio);
	~apc.setMixerEncoderValue(5, endRatio);
	~apc.setMixerEncoderValue(6, time);
	~apc.setMixerEncoderValue(7, lfo1);
	~apc.setMixerEncoderValue(8, lfo2);
}, osc1Bank, page);

~apc.setMixerEncoderFunc(1, { | val |
	~prm.subtractive.setOsc1Vol(val.ccdbfs);
}, osc1Bank, page);
~apc.setMixerEncoderFunc(2, { | val |
	var waveform = val.linlin(0, 127, 0, 3);
	~prm.subtractive.setOsc1Waveform(waveform);
}, osc1Bank, page);
~apc.setMixerEncoderFunc(3, { | val |
	var pulseWidth = val.linlin(0, 127, 0, 1);
	~prm.subtractive.setOsc1PulseWidth(pulseWidth);
}, osc1Bank, page);
~apc.setMixerEncoderFunc(4, { | val |
	var ratio = val.linexp(0, 127, 0.1, 10);
	~prm.subtractive.setOsc1FreqEnvStartRatio(ratio);
}, osc1Bank, page);
~apc.setMixerEncoderFunc(5, { | val |
	var ratio = val.linexp(0, 127, 0.1, 10);
	~prm.subtractive.setOsc1FreqEnvEndRatio(ratio);
}, osc1Bank, page);
~apc.setMixerEncoderFunc(6, { | val |
	var time = val.linexp(0, 127, 0.05, 30);
	~prm.subtractive.setOsc1FreqEnvTime(time);
}, osc1Bank, page);
~apc.setMixerEncoderFunc(7, { | val |
	var freq = val.linexp(0, 127, 0.001, 55);
	~prm.subtractive.setLFO1Freq(freq);
}, osc1Bank, page);
~apc.setMixerEncoderFunc(8, { | val |
	var freq = val.linexp(0, 127, 0.001, 55);
	~prm.subtractive.setLFO2Freq(freq);
}, osc1Bank, page);


///////////// device encoders:
~apc.setDeviceEncodersMonitorFunc(\osc1, {
	var wave1, pulse1, amp1, freq1;
	var wave2, pulse2, amp2, freq2;
	if( osc1Waveform1Invert == true,
		{ wave1 = ~prm.subtractive.osc1WaveformLFO1Bottom.linlin(0, 3, 0, 127); },
		{ wave1 = ~prm.subtractive.osc1WaveformLFO1Top.linlin(0, 3, 0, 127); });
	if( osc1pw1Invert == true,
		{ pulse1 = ~prm.subtractive.osc1PulseWidthLFO1Bottom.linlin(0, 0.6, 0, 127) },
		{ pulse1 = ~prm.subtractive.osc1PulseWidthLFO1Top.linlin(0, 0.6, 0, 127) });
	if( osc1Amp1Invert == true,
		{ amp1 = ~prm.subtractive.osc1AmpLFO1Top.linlin(0, 1, 127, 0) },
		{ amp1 = ~prm.subtractive.osc1AmpLFO1Bottom.linlin(0, 1, 127, 0) });
	if( osc1Freq1Invert == true,
		{ freq1 = ~prm.subtractive.osc1FreqLFO1BottomRatio.explin(1, 4, 0, 127); },
		{ freq1 = ~prm.subtractive.osc1FreqLFO1TopRatio.explin(1, 4, 0, 127); });
	if( osc1Waveform2Invert == true,
		{ wave2 = ~prm.subtractive.osc1WaveformLFO2Bottom.linlin(0, 3, 0, 127); },
		{ wave2 = ~prm.subtractive.osc1WaveformLFO2Top.linlin(0, 3, 0, 127); });
	if( osc1pw2Invert == true,
		{ pulse2 = ~prm.subtractive.osc1PulseWidthLFO2Bottom.linlin(0, 0.6, 0, 127) },
		{ pulse2 = ~prm.subtractive.osc1PulseWidthLFO2Top.linlin(0, 0.6, 0, 127) });
	if( osc1Amp2Invert == true,
		{ amp2 = ~prm.subtractive.osc1AmpLFO2Top.linlin(0, 1, 127, 0) },
		{ amp2 = ~prm.subtractive.osc1AmpLFO2Bottom.linlin(0, 1, 127, 0) });
	if( osc1Freq2Invert == true,
		{ freq2 = ~prm.subtractive.osc1FreqLFO2BottomRatio.explin(1, 4, 0, 127); },
		{ freq2 = ~prm.subtractive.osc1FreqLFO2TopRatio.explin(1, 4, 0, 127); });
	~apc.setDeviceEncoderValue(1, wave1);
	~apc.setDeviceEncoderValue(2, pulse1);
	~apc.setDeviceEncoderValue(3, amp1);
	~apc.setDeviceEncoderValue(4, freq1);
	~apc.setDeviceEncoderValue(5, wave2);
	~apc.setDeviceEncoderValue(6, pulse2);
	~apc.setDeviceEncoderValue(7, amp2);
	~apc.setDeviceEncoderValue(8, freq2);
}, osc1Bank, page);
// waveform lfo 1:
~apc.setDeviceEncoderFunc(1, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 0, -3);
	high = val.linlin(0, 127, 0, 3);
	if( osc1Waveform1Invert == true, { top = low; bottom = high; }, { top = high; bottom = low; });
	~prm.subtractive.setOsc1WaveformLFO1Bottom(bottom);
	~prm.subtractive.setOsc1WaveformLFO1Top(top);
}, osc1Bank, page);
// pulse width lfo 1:
~apc.setDeviceEncoderFunc(2, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 0, -0.6);
	high = val.linlin(0, 127, 0, 0.6);
	if( osc1pw1Invert == true, { top = low; bottom = high }, { top = high; bottom = low; });
	~prm.subtractive.setOsc1PulseWidthLFO1Bottom(bottom);
	~prm.subtractive.setOsc1PulseWidthLFO1Top(top);
}, osc1Bank, page);
// amp lfo 1:
~apc.setDeviceEncoderFunc(3, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 1, 0);
	high = 1;
	if( osc1Amp1Invert == true, { top = low; bottom = high }, { top = high; bottom = low; });
	~prm.subtractive.setOsc1AmpLFO1Bottom(bottom);
	~prm.subtractive.setOsc1AmpLFO1Top(top);
}, osc1Bank, page);
// freq lfo 1:
~apc.setDeviceEncoderFunc(4, { | val |
	var high, low, top, bottom;
	low = val.linexp(0, 127, 1, 0.25);
	high = val.linexp(0, 127, 1, 4);
	if( osc1Freq1Invert == true, {  top = low; bottom = high; }, { bottom = low; top = high; });
	~prm.subtractive.setOsc1FreqLFO1BottomRatio(bottom);
	~prm.subtractive.setOsc1FreqLFO1TopRatio(top);
}, osc1Bank, page);
// waveform lfo 2:
~apc.setDeviceEncoderFunc(5, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 0, -3);
	high = val.linlin(0, 127, 0, 3);
	if( osc1Waveform2Invert == true, { top = low; bottom = high; }, { top = high; bottom = low; });
	~prm.subtractive.setOsc1WaveformLFO2Bottom(bottom);
	~prm.subtractive.setOsc1WaveformLFO2Top(top);
}, osc1Bank, page);
// pulse width lfo 2:
~apc.setDeviceEncoderFunc(6, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 0, -0.6);
	high = val.linlin(0, 127, 0, 0.6);
	if( osc1pw2Invert == true, { top = low; bottom = high }, { top = high; bottom = low; });
	~prm.subtractive.setOsc1PulseWidthLFO2Bottom(bottom);
	~prm.subtractive.setOsc1PulseWidthLFO2Top(top);
}, osc1Bank, page);
// amp lfo 2:
~apc.setDeviceEncoderFunc(7, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 1, 0);
	high = 1;
	if( osc1Amp2Invert == true, { top = low; bottom = high }, { top = high; bottom = low; });
	~prm.subtractive.setOsc1AmpLFO2Bottom(bottom);
	~prm.subtractive.setOsc1AmpLFO2Top(top);
}, osc1Bank, page);
// freq lfo 2:
~apc.setDeviceEncoderFunc(8, { | val |
	var high, low, top, bottom;
	low = val.linexp(0, 127, 1, 0.25);
	high = val.linexp(0, 127, 1, 4);
	if( osc1Freq2Invert == true, {  top = low; bottom = high; }, { bottom = low; top = high; });
	~prm.subtractive.setOsc1FreqLFO2BottomRatio(bottom);
	~prm.subtractive.setOsc1FreqLFO2TopRatio(top);
}, osc1Bank, page);


/////////////////////
/////// GRID: /////
//////////////////

///// Osc 1 Freq Env Reset:
~apc.turnGridYellowGreen(4, 4, bank: osc1Bank, page: page);
~apc.setGridFunc(4, 4, {
	~prm.subtractive.setOsc1FreqEnvStartRatio(1);
	~prm.subtractive.setOsc1FreqEnvEndRatio(1);
	~apc.turnGridGreen(4, 4);
}, bank: osc1Bank, page: page);
~apc.setGridFunc(4, 4, { ~apc.turnGridYellowGreen(4, 4); }, 'noteOff', osc1Bank, page);


~apc.setGridMonitorFunc(\osc1, {
	if( ~prm.subtractive.osc1Waveform == 0, { ~apc.turnGridGreen(0, 4) }, { ~apc.turnGridBlue(0, 4) });
	if( ~prm.subtractive.osc1Waveform == 1, { ~apc.turnGridGreen(1, 4) }, { ~apc.turnGridBlue(1, 4) });
	if( ~prm.subtractive.osc1Waveform == 2, { ~apc.turnGridGreen(2, 4) }, { ~apc.turnGridBlue(2, 4) });
	if( ~prm.subtractive.osc1Waveform == 3, { ~apc.turnGridGreen(3, 4) }, { ~apc.turnGridBlue(3, 4) });

	if( ~prm.subtractive.osc1OctaveMul == 0.125, { ~apc.turnGridGreen(0, 3) }, { ~apc.turnGridBlue(0, 3) });
	if( ~prm.subtractive.osc1OctaveMul == 0.25, { ~apc.turnGridGreen(1, 3) }, { ~apc.turnGridBlue(1, 3) });
	if( ~prm.subtractive.osc1OctaveMul == 0.5, { ~apc.turnGridGreen(2, 3) }, { ~apc.turnGridBlue(2, 3) });
	if( ~prm.subtractive.osc1OctaveMul == 1, { ~apc.turnGridGreen(3, 3) }, { ~apc.turnGridBlue(3, 3) });
	if( ~prm.subtractive.osc1OctaveMul == 2, { ~apc.turnGridGreen(4, 3) }, { ~apc.turnGridBlue(4, 3) });
	if( ~prm.subtractive.osc1OctaveMul == 4, { ~apc.turnGridGreen(5, 3) }, { ~apc.turnGridBlue(5, 3) });
	if( ~prm.subtractive.osc1OctaveMul == 8, { ~apc.turnGridGreen(6, 3) }, { ~apc.turnGridBlue(6, 3) });
	if( ~prm.subtractive.osc1OctaveMul == 16, { ~apc.turnGridGreen(7, 3) }, { ~apc.turnGridBlue(7, 3) });

}, osc1Bank, page);

///// Osc 1 Waveform:
~apc.setGridFunc(0, 4, { ~prm.subtractive.setOsc1Waveform(0); }, bank: osc1Bank, page: page);
~apc.setGridFunc(1, 4, { ~prm.subtractive.setOsc1Waveform(1); }, bank: osc1Bank, page: page);
~apc.setGridFunc(2, 4, { ~prm.subtractive.setOsc1Waveform(2); }, bank: osc1Bank, page: page);
~apc.setGridFunc(3, 4, { ~prm.subtractive.setOsc1Waveform(3); }, bank: osc1Bank, page: page);

///// Osc 1 Octave:
~apc.setGridFunc(0, 3, { ~prm.subtractive.setOsc1Octave(0) }, bank: osc1Bank, page: page);
~apc.setGridFunc(1, 3, { ~prm.subtractive.setOsc1Octave(1) }, bank: osc1Bank, page: page);
~apc.setGridFunc(2, 3, { ~prm.subtractive.setOsc1Octave(2) }, bank: osc1Bank, page: page);
~apc.setGridFunc(3, 3, { ~prm.subtractive.setOsc1Octave(3) }, bank: osc1Bank, page: page);
~apc.setGridFunc(4, 3, { ~prm.subtractive.setOsc1Octave(4) }, bank: osc1Bank, page: page);
~apc.setGridFunc(5, 3, { ~prm.subtractive.setOsc1Octave(5) }, bank: osc1Bank, page: page);
~apc.setGridFunc(6, 3, { ~prm.subtractive.setOsc1Octave(6) }, bank: osc1Bank, page: page);
~apc.setGridFunc(7, 3, { ~prm.subtractive.setOsc1Octave(7) }, bank: osc1Bank, page: page);


///// LFO inverts:
8.do({ | i | ~apc.turnGridYellow(i, 0, bank: osc1Bank, page: page); });
~apc.setGridFunc(0, 0, {
	if( osc1Waveform1Invert == false,
		{ osc1Waveform1Invert = true; ~apc.turnGridWhite(0, 0); },
		{ osc1Waveform1Invert = false; ~apc.turnGridYellow(0, 0); });
	~prm.subtractive.setOsc1WaveformLFO1Bottom(~prm.subtractive.osc1WaveformLFO1Top);
	~prm.subtractive.setOsc1WaveformLFO1Top(~prm.subtractive.osc1WaveformLFO1Bottom);
}, bank: osc1Bank, page: page);
~apc.setGridFunc(1, 0, {
	if( osc1pw1Invert == false,
		{ osc1pw1Invert = true; ~apc.turnGridWhite(1, 0); },
		{ osc1pw1Invert = false; ~apc.turnGridYellow(1, 0); });
	~prm.subtractive.setOsc1PulseWidthLFO1Bottom(~prm.subtractive.osc1PulseWidthLFO1Top);
	~prm.subtractive.setOsc1PulseWidthLFO1Top(~prm.subtractive.osc1PulseWidthLFO1Bottom);
}, bank: osc1Bank, page: page);
~apc.setGridFunc(2, 0, {
	if( osc1Amp1Invert == false,
		{ osc1Amp1Invert = true; ~apc.turnGridWhite(2, 0); },
		{ osc1Amp1Invert = false; ~apc.turnGridYellow(2, 0); });
	~prm.subtractive.setOsc1AmpLFO1Bottom(~prm.subtractive.osc1AmpLFO1Top);
	~prm.subtractive.setOsc1AmpLFO1Top(~prm.subtractive.osc1AmpLFO1Bottom);
}, bank: osc1Bank, page: page);
~apc.setGridFunc(3, 0, {
	if( osc1Freq1Invert == false,
		{ osc1Freq1Invert = true; ~apc.turnGridWhite(3, 0); },
		{ osc1Freq1Invert = false; ~apc.turnGridYellow(3, 0); });
	~prm.subtractive.setOsc1FreqLFO1BottomRatio(~prm.subtractive.osc1FreqLFO1TopRatio);
	~prm.subtractive.setOsc1FreqLFO1TopRatio(~prm.subtractive.osc1FreqLFO1BottomRatio);
}, bank: osc1Bank, page: page);
~apc.setGridFunc(4, 0, {
	if( osc1Waveform2Invert == false,
		{ osc1Waveform2Invert = true; ~apc.turnGridWhite(4, 0); },
		{ osc1Waveform2Invert = false; ~apc.turnGridYellow(4, 0); });
	~prm.subtractive.setOsc1WaveformLFO2Bottom(~prm.subtractive.osc1WaveformLFO2Top);
	~prm.subtractive.setOsc1WaveformLFO2Top(~prm.subtractive.osc1WaveformLFO2Bottom);
}, bank: osc1Bank, page: page);
~apc.setGridFunc(5, 0, {
	if( osc1pw2Invert == false,
		{ osc1pw2Invert = true; ~apc.turnGridWhite(5, 0); },
		{ osc1pw2Invert = false; ~apc.turnGridYellow(5, 0); });
	~prm.subtractive.setOsc1PulseWidthLFO2Bottom(~prm.subtractive.osc1PulseWidthLFO2Top);
	~prm.subtractive.setOsc1PulseWidthLFO2Top(~prm.subtractive.osc1PulseWidthLFO2Bottom);
}, bank: osc1Bank, page: page);
~apc.setGridFunc(6, 0, {
	if( osc1Amp2Invert == false,
		{ osc1Amp2Invert = true; ~apc.turnGridWhite(6, 0); },
		{ osc1Amp2Invert = false; ~apc.turnGridYellow(6, 0); });
	~prm.subtractive.setOsc1AmpLFO2Bottom(~prm.subtractive.osc1AmpLFO2Top);
	~prm.subtractive.setOsc1AmpLFO2Top(~prm.subtractive.osc1AmpLFO2Bottom);
}, bank: osc1Bank, page: page);
~apc.setGridFunc(7, 0, {
	if( osc1Freq2Invert == false,
		{ osc1Freq2Invert = true; ~apc.turnGridWhite(7, 0); },
		{ osc1Freq2Invert = false; ~apc.turnGridYellow(7, 0); });
	~prm.subtractive.setOsc1FreqLFO2BottomRatio(~prm.subtractive.osc1FreqLFO2TopRatio);
	~prm.subtractive.setOsc1FreqLFO2TopRatio(~prm.subtractive.osc1FreqLFO2BottomRatio);
}, bank: osc1Bank, page: page);




//////////////////////////
//// Oscillator 2:  ////
///////////////////////

////////////// mixer encoders:

~apc.setMixerEncodersMonitorFunc(\osc2, {
	var vol = ~prm.subtractive.osc2Amp.ampdb.dbfsCC;
	var waveform = ~prm.subtractive.osc2Waveform.linlin(0, 3, 0, 127);
	var pulseWidth = ~prm.subtractive.osc2PulseWidth.linlin(0, 1, 0, 127);
	var startRatio = ~prm.subtractive.osc2FreqEnvStartRatio.explin(0.1, 10, 0, 127);
	var endRatio = ~prm.subtractive.osc2FreqEnvEndRatio.explin(0.1, 10, 0, 127);
	var time = ~prm.subtractive.osc2FreqEnvTime.explin(0.05, 30, 0, 127);
	var detune = ~prm.subtractive.osc2DetuneCents.linlin(-50, 50, 0, 127);
	~apc.setMixerEncoderValue(1, vol);
	~apc.setMixerEncoderValue(2, waveform);
	~apc.setMixerEncoderValue(3, pulseWidth);
	~apc.setMixerEncoderValue(4, startRatio);
	~apc.setMixerEncoderValue(5, endRatio);
	~apc.setMixerEncoderValue(6, time);
	~apc.setMixerEncoderValue(7, detune);
}, osc2Bank, page);

~apc.setMixerEncoderFunc(1, { | val |
	~prm.subtractive.setOsc2Vol(val.ccdbfs);
}, osc2Bank, page);
~apc.setMixerEncoderFunc(2, { | val |
	var waveform = val.linlin(0, 127, 0, 3);
	~prm.subtractive.setOsc2Waveform(waveform);
}, osc2Bank, page);
~apc.setMixerEncoderFunc(3, { | val |
	var pulseWidth = val.linlin(0, 127, 0, 1);
	~prm.subtractive.setOsc2PulseWidth(pulseWidth);
}, osc2Bank, page);
~apc.setMixerEncoderFunc(4, { | val |
	var ratio = val.linexp(0, 127, 0.1, 10);
	~prm.subtractive.setOsc2FreqEnvStartRatio(ratio);
}, osc2Bank, page);
~apc.setMixerEncoderFunc(5, { | val |
	var ratio = val.linexp(0, 127, 0.1, 10);
	~prm.subtractive.setOsc2FreqEnvEndRatio(ratio);
}, osc2Bank, page);
~apc.setMixerEncoderFunc(6, { | val |
	var time = val.linexp(0, 127, 0.05, 30);
	~prm.subtractive.setOsc2FreqEnvTime(time);
}, osc2Bank, page);
~apc.setMixerEncoderFunc(7, { | val |
	var detune = val.linlin(0, 127, -50, 50);
	~prm.subtractive.setOsc2DetuneCents(detune);
}, osc2Bank, page);


///////////// device encoders:
~apc.setDeviceEncodersMonitorFunc(\osc2, {
	var wave1, pulse1, amp1, freq1;
	var wave2, pulse2, amp2, freq2;
	if( osc2Waveform1Invert == true,
		{ wave1 = ~prm.subtractive.osc2WaveformLFO1Bottom.linlin(0, 3, 0, 127); },
		{ wave1 = ~prm.subtractive.osc2WaveformLFO1Top.linlin(0, 3, 0, 127); });
	if( osc2pw1Invert == true,
		{ pulse1 = ~prm.subtractive.osc2PulseWidthLFO1Bottom.linlin(0, 0.6, 0, 127) },
		{ pulse1 = ~prm.subtractive.osc2PulseWidthLFO1Top.linlin(0, 0.6, 0, 127) });
	if( osc2Amp1Invert == true,
		{ amp1 = ~prm.subtractive.osc2AmpLFO1Top.linlin(0, 1, 127, 0) },
		{ amp1 = ~prm.subtractive.osc2AmpLFO1Bottom.linlin(0, 1, 127, 0) });
	if( osc2Freq1Invert == true,
		{ freq1 = ~prm.subtractive.osc2FreqLFO1BottomRatio.explin(1, 4, 0, 127); },
		{ freq1 = ~prm.subtractive.osc2FreqLFO1TopRatio.explin(1, 4, 0, 127); });
	if( osc2Waveform2Invert == true,
		{ wave2 = ~prm.subtractive.osc2WaveformLFO2Bottom.linlin(0, 3, 0, 127); },
		{ wave2 = ~prm.subtractive.osc2WaveformLFO2Top.linlin(0, 3, 0, 127); });
	if( osc2pw2Invert == true,
		{ pulse2 = ~prm.subtractive.osc2PulseWidthLFO2Bottom.linlin(0, 0.6, 0, 127) },
		{ pulse2 = ~prm.subtractive.osc2PulseWidthLFO2Top.linlin(0, 0.6, 0, 127) });
	if( osc2Amp2Invert == true,
		{ amp2 = ~prm.subtractive.osc2AmpLFO2Top.linlin(0, 1, 127, 0) },
		{ amp2 = ~prm.subtractive.osc2AmpLFO2Bottom.linlin(0, 1, 127, 0) });
	if( osc2Freq2Invert == true,
		{ freq2 = ~prm.subtractive.osc2FreqLFO2BottomRatio.explin(1, 4, 0, 127); },
		{ freq2 = ~prm.subtractive.osc2FreqLFO2TopRatio.explin(1, 4, 0, 127); });
	~apc.setDeviceEncoderValue(1, wave1);
	~apc.setDeviceEncoderValue(2, pulse1);
	~apc.setDeviceEncoderValue(3, amp1);
	~apc.setDeviceEncoderValue(4, freq1);
	~apc.setDeviceEncoderValue(5, wave2);
	~apc.setDeviceEncoderValue(6, pulse2);
	~apc.setDeviceEncoderValue(7, amp2);
	~apc.setDeviceEncoderValue(8, freq2);
}, osc2Bank, page);
// waveform lfo 1:
~apc.setDeviceEncoderFunc(1, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 0, -3);
	high = val.linlin(0, 127, 0, 3);
	if( osc2Waveform1Invert == true, { top = low; bottom = high; }, { top = high; bottom = low; });
	~prm.subtractive.setOsc2WaveformLFO1Bottom(bottom);
	~prm.subtractive.setOsc2WaveformLFO1Top(top);
}, osc2Bank, page);
// pulse width lfo 1:
~apc.setDeviceEncoderFunc(2, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 0, -0.6);
	high = val.linlin(0, 127, 0, 0.6);
	if( osc2pw1Invert == true, { top = low; bottom = high }, { top = high; bottom = low; });
	~prm.subtractive.setOsc2PulseWidthLFO1Bottom(bottom);
	~prm.subtractive.setOsc2PulseWidthLFO1Top(top);
}, osc2Bank, page);
// amp lfo 1:
~apc.setDeviceEncoderFunc(3, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 1, 0);
	high = 1;
	if( osc2Amp1Invert == true, { top = low; bottom = high }, { top = high; bottom = low; });
	~prm.subtractive.setOsc2AmpLFO1Bottom(bottom);
	~prm.subtractive.setOsc2AmpLFO1Top(top);
}, osc2Bank, page);
// freq lfo 1:
~apc.setDeviceEncoderFunc(4, { | val |
	var high, low, top, bottom;
	low = val.linexp(0, 127, 1, 0.25);
	high = val.linexp(0, 127, 1, 4);
	if( osc2Freq1Invert == true, {  top = low; bottom = high; }, { bottom = low; top = high; });
	~prm.subtractive.setOsc2FreqLFO1BottomRatio(bottom);
	~prm.subtractive.setOsc2FreqLFO1TopRatio(top);
}, osc2Bank, page);
// waveform lfo 2:
~apc.setDeviceEncoderFunc(5, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 0, -3);
	high = val.linlin(0, 127, 0, 3);
	if( osc2Waveform2Invert == true, { top = low; bottom = high; }, { top = high; bottom = low; });
	~prm.subtractive.setOsc2WaveformLFO2Bottom(bottom);
	~prm.subtractive.setOsc2WaveformLFO2Top(top);
}, osc2Bank, page);
// pulse width lfo 2:
~apc.setDeviceEncoderFunc(6, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 0, -0.6);
	high = val.linlin(0, 127, 0, 0.6);
	if( osc2pw2Invert == true, { top = low; bottom = high }, { top = high; bottom = low; });
	~prm.subtractive.setOsc2PulseWidthLFO2Bottom(bottom);
	~prm.subtractive.setOsc2PulseWidthLFO2Top(top);
}, osc2Bank, page);
// amp lfo 2:
~apc.setDeviceEncoderFunc(7, { | val |
	var high, low, top, bottom;
	low = val.linlin(0, 127, 1, 0);
	high = 1;
	if( osc2Amp2Invert == true, { top = low; bottom = high }, { top = high; bottom = low; });
	~prm.subtractive.setOsc2AmpLFO2Bottom(bottom);
	~prm.subtractive.setOsc2AmpLFO2Top(top);
}, osc2Bank, page);
// freq lfo 2:
~apc.setDeviceEncoderFunc(8, { | val |
	var high, low, top, bottom;
	low = val.linexp(0, 127, 1, 0.25);
	high = val.linexp(0, 127, 1, 4);
	if( osc2Freq2Invert == true, {  top = low; bottom = high; }, { bottom = low; top = high; });
	~prm.subtractive.setOsc2FreqLFO2BottomRatio(bottom);
	~prm.subtractive.setOsc2FreqLFO2TopRatio(top);
}, osc2Bank, page);


/////////////////////
/////// GRID: /////
//////////////////

///// Osc 2 Freq Env Reset:
~apc.turnGridYellowGreen(4, 4, bank: osc2Bank, page: page);
~apc.setGridFunc(4, 4, {
	~prm.subtractive.setOsc2FreqEnvStartRatio(1);
	~prm.subtractive.setOsc2FreqEnvEndRatio(1);
	~apc.turnGridGreen(4, 4);
}, bank: osc2Bank, page: page);
~apc.setGridFunc(4, 4, { ~apc.turnGridYellowGreen(4, 4); }, 'noteOff', osc2Bank, page);


~apc.setGridMonitorFunc(\osc2, {
	if( ~prm.subtractive.osc2Waveform == 0, { ~apc.turnGridGreen(0, 2) }, { ~apc.turnGridLightBlue(0, 2) });
	if( ~prm.subtractive.osc2Waveform == 1, { ~apc.turnGridGreen(1, 2) }, { ~apc.turnGridLightBlue(1, 2) });
	if( ~prm.subtractive.osc2Waveform == 2, { ~apc.turnGridGreen(2, 2) }, { ~apc.turnGridLightBlue(2, 2) });
	if( ~prm.subtractive.osc2Waveform == 3, { ~apc.turnGridGreen(3, 2) }, { ~apc.turnGridLightBlue(3, 2) });

	if( ~prm.subtractive.osc2OctaveMul == 0.125, { ~apc.turnGridGreen(0, 1) }, { ~apc.turnGridLightBlue(0, 1) });
	if( ~prm.subtractive.osc2OctaveMul == 0.25, { ~apc.turnGridGreen(1, 1) }, { ~apc.turnGridLightBlue(1, 1) });
	if( ~prm.subtractive.osc2OctaveMul == 0.5, { ~apc.turnGridGreen(2, 1) }, { ~apc.turnGridLightBlue(2, 1) });
	if( ~prm.subtractive.osc2OctaveMul == 1, { ~apc.turnGridGreen(3, 1) }, { ~apc.turnGridLightBlue(3, 1) });
	if( ~prm.subtractive.osc2OctaveMul == 2, { ~apc.turnGridGreen(4, 1) }, { ~apc.turnGridLightBlue(4, 1) });
	if( ~prm.subtractive.osc2OctaveMul == 4, { ~apc.turnGridGreen(5, 1) }, { ~apc.turnGridLightBlue(5, 1) });
	if( ~prm.subtractive.osc2OctaveMul == 8, { ~apc.turnGridGreen(6, 1) }, { ~apc.turnGridLightBlue(6, 1) });
	if( ~prm.subtractive.osc2OctaveMul == 16, { ~apc.turnGridGreen(7, 1) }, { ~apc.turnGridLightBlue(7, 1) });

}, osc2Bank, page);

///// Osc 1 Waveform:
~apc.setGridFunc(0, 2, { ~prm.subtractive.setOsc2Waveform(0); }, bank: osc2Bank, page: page);
~apc.setGridFunc(1, 2, { ~prm.subtractive.setOsc2Waveform(1); }, bank: osc2Bank, page: page);
~apc.setGridFunc(2, 2, { ~prm.subtractive.setOsc2Waveform(2); }, bank: osc2Bank, page: page);
~apc.setGridFunc(3, 2, { ~prm.subtractive.setOsc2Waveform(3); }, bank: osc2Bank, page: page);

///// Osc 1 Octave:
~apc.setGridFunc(0, 1, { ~prm.subtractive.setOsc2Octave(0) }, bank: osc2Bank, page: page);
~apc.setGridFunc(1, 1, { ~prm.subtractive.setOsc2Octave(1) }, bank: osc2Bank, page: page);
~apc.setGridFunc(2, 1, { ~prm.subtractive.setOsc2Octave(2) }, bank: osc2Bank, page: page);
~apc.setGridFunc(3, 1, { ~prm.subtractive.setOsc2Octave(3) }, bank: osc2Bank, page: page);
~apc.setGridFunc(4, 1, { ~prm.subtractive.setOsc2Octave(4) }, bank: osc2Bank, page: page);
~apc.setGridFunc(5, 1, { ~prm.subtractive.setOsc2Octave(5) }, bank: osc2Bank, page: page);
~apc.setGridFunc(6, 1, { ~prm.subtractive.setOsc2Octave(6) }, bank: osc2Bank, page: page);
~apc.setGridFunc(7, 1, { ~prm.subtractive.setOsc2Octave(7) }, bank: osc2Bank, page: page);


///// LFO inverts:
8.do({ | i | ~apc.turnGridYellow(i, 0, bank: osc2Bank, page: page); });
~apc.setGridFunc(0, 0, {
	if( osc2Waveform1Invert == false,
		{ osc2Waveform1Invert = true; ~apc.turnGridWhite(0, 0); },
		{ osc2Waveform1Invert = false; ~apc.turnGridYellow(0, 0); });
	~prm.subtractive.setOsc2WaveformLFO1Bottom(~prm.subtractive.osc2WaveformLFO1Top);
	~prm.subtractive.setOsc2WaveformLFO1Top(~prm.subtractive.osc2WaveformLFO1Bottom);
}, bank: osc2Bank, page: page);
~apc.setGridFunc(1, 0, {
	if( osc2pw1Invert == false,
		{ osc2pw1Invert = true; ~apc.turnGridWhite(1, 0); },
		{ osc2pw1Invert = false; ~apc.turnGridYellow(1, 0); });
	~prm.subtractive.setOsc2PulseWidthLFO1Bottom(~prm.subtractive.osc2PulseWidthLFO1Top);
	~prm.subtractive.setOsc2PulseWidthLFO1Top(~prm.subtractive.osc2PulseWidthLFO1Bottom);
}, bank: osc2Bank, page: page);
~apc.setGridFunc(2, 0, {
	if( osc2Amp1Invert == false,
		{ osc2Amp1Invert = true; ~apc.turnGridWhite(2, 0); },
		{ osc2Amp1Invert = false; ~apc.turnGridYellow(2, 0); });
	~prm.subtractive.setOsc2AmpLFO1Bottom(~prm.subtractive.osc2AmpLFO1Top);
	~prm.subtractive.setOsc2AmpLFO1Top(~prm.subtractive.osc2AmpLFO1Bottom);
}, bank: osc2Bank, page: page);
~apc.setGridFunc(3, 0, {
	if( osc2Freq1Invert == false,
		{ osc2Freq1Invert = true; ~apc.turnGridWhite(3, 0); },
		{ osc2Freq1Invert = false; ~apc.turnGridYellow(3, 0); });
	~prm.subtractive.setOsc2FreqLFO1BottomRatio(~prm.subtractive.osc2FreqLFO1TopRatio);
	~prm.subtractive.setOsc2FreqLFO1TopRatio(~prm.subtractive.osc2FreqLFO1BottomRatio);
}, bank: osc2Bank, page: page);
~apc.setGridFunc(4, 0, {
	if( osc2Waveform2Invert == false,
		{ osc2Waveform2Invert = true; ~apc.turnGridWhite(4, 0); },
		{ osc2Waveform2Invert = false; ~apc.turnGridYellow(4, 0); });
	~prm.subtractive.setOsc2WaveformLFO2Bottom(~prm.subtractive.osc2WaveformLFO2Top);
	~prm.subtractive.setOsc2WaveformLFO2Top(~prm.subtractive.osc2WaveformLFO2Bottom);
}, bank: osc2Bank, page: page);
~apc.setGridFunc(5, 0, {
	if( osc2pw2Invert == false,
		{ osc2pw2Invert = true; ~apc.turnGridWhite(5, 0); },
		{ osc2pw2Invert = false; ~apc.turnGridYellow(5, 0); });
	~prm.subtractive.setOsc2PulseWidthLFO2Bottom(~prm.subtractive.osc2PulseWidthLFO2Top);
	~prm.subtractive.setOsc2PulseWidthLFO2Top(~prm.subtractive.osc2PulseWidthLFO2Bottom);
}, bank: osc2Bank, page: page);
~apc.setGridFunc(6, 0, {
	if( osc2Amp2Invert == false,
		{ osc2Amp2Invert = true; ~apc.turnGridWhite(6, 0); },
		{ osc2Amp2Invert = false; ~apc.turnGridYellow(6, 0); });
	~prm.subtractive.setOsc2AmpLFO2Bottom(~prm.subtractive.osc2AmpLFO2Top);
	~prm.subtractive.setOsc2AmpLFO2Top(~prm.subtractive.osc2AmpLFO2Bottom);
}, bank: osc2Bank, page: page);
~apc.setGridFunc(7, 0, {
	if( osc2Freq2Invert == false,
		{ osc2Freq2Invert = true; ~apc.turnGridWhite(7, 0); },
		{ osc2Freq2Invert = false; ~apc.turnGridYellow(7, 0); });
	~prm.subtractive.setOsc2FreqLFO2BottomRatio(~prm.subtractive.osc2FreqLFO2TopRatio);
	~prm.subtractive.setOsc2FreqLFO2TopRatio(~prm.subtractive.osc2FreqLFO2BottomRatio);
}, bank: osc2Bank, page: page);



