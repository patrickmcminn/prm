var page = 'cvSuite';

var activeBank = 0;

var main = 0;
var lfo12 = 1;
var lfo34 = 2;
var trigEnv = 3;
var gateEnv = 4;
var gate = 5;
var constant = 6;
var freq = 7;
var pitchGate = 8;

var freq1Chromatic = 9;
var freq1Diatonic = 10;
var freq2Chromatic = 11;
var freq2Diatonic = 12;

var freq1RootSel = 13;
var freq2RootSel = 14;

var pitchGate1Chromatic = 15;
var pitchGate1Diatonic = 16;
var pitchGate2Chromatic = 17;
var pitchGate2Diatonic = 18;

var gate1Val = 0.5;
var gate2Val = 0.5;

var scales;

var freq1ActiveScale = 0;
var freq2ActiveScale = 5;
var pitchGate1ActiveScale = 0;
var pitchGate2ActiveScale = 5;

var freq1OctScale = 0;
var freq1Root = 0;
var freq2OctScale = 0;
var freq2Root = 0;

var pitchGate1Reset, pitchGate2Reset;

var blackPos = [1, 2, 4, 5, 6];

scales = [Scale.ionian, Scale.dorian, Scale.phrygian, Scale.lydian, Scale.mixolydian, Scale.aeolian, Scale.locrian];


////////////////////////////////
//////// ADSR Env: ////////////
//////////////////////////////

//////// Grid:

//// adsr env polarity:
~apc.setGridMonitorFunc(\envPolarity, {
	var pol1 = ~prm.cv.gateEnv1.polarity;
	var pol2 = ~prm.cv.gateEnv2.polarity;
	if( pol1 == 1, { ~apc.turnGridYellow(6, 3); }, { ~apc.turnGridWhite(6, 3) });
	if ( pol2 == 1, { ~apc.turnGridYellow(6, 1) }, { ~apc.turnGridWhite(6, 1) });
}, gateEnv, page);
~apc.setGridFunc(6, 3, {
	var pol = ~prm.cv.gateEnv1.polarity;
	if( pol == 1,
		{ ~prm.cv.gateEnv1.setPolarity(-1); },
		{ ~prm.cv.gateEnv1.setPolarity(1); });
}, 'noteOn', gateEnv, page);
~apc.setGridFunc(6, 1, {
	var pol = ~prm.cv.gateEnv2.polarity;
	if( pol == 1,
		{ ~prm.cv.gateEnv2.setPolarity(-1); },
		{ ~prm.cv.gateEnv2.setPolarity(1); });
}, 'noteOn', gateEnv, page);

//// adsr env gate 1:
~apc.turnGridPink(0, 3, 2, gateEnv, page);
~apc.setGridFunc(0, 3, {
	~apc.turnGridGreen(0, 3);
	~prm.cv.gateEnv1.trigger;
}, 'noteOn', gateEnv, page);
~apc.setGridFunc(0, 3, {
	~apc.turnGridPink(0, 3, 2);
	~prm.cv.gateEnv1.release;
}, 'noteOff', gateEnv, page);

//// adsr env gate 2:
~apc.turnGridPurple(0, 1, 2, gateEnv, page);
~apc.setGridFunc(0, 1, {
	~apc.turnGridGreen(0, 1);
	~prm.cv.gateEnv2.trigger;
}, 'noteOn', gateEnv, page);
~apc.setGridFunc(0, 1, {
	~apc.turnGridPurple(0, 1, 2);
	~prm.cv.gateEnv2.release;
}, 'noteOff', gateEnv, page);

//// outputs:
// gate env output 1:
~apc.setGridMonitorFunc(\gateEnv1Output, {
	if( ~prm.cv.gateEnv1.out == ~modOutput[0], { ~apc.turnGridGreen(0, 2, 2) }, { ~apc.turnGridBlue(0, 2, 1) });
	if( ~prm.cv.gateEnv1.out == ~modOutput[1], { ~apc.turnGridGreen(1, 2, 2) }, { ~apc.turnGridBlue(1, 2, 1) });
	if( ~prm.cv.gateEnv1.out == ~modOutput[2], { ~apc.turnGridGreen(2, 2, 2) }, { ~apc.turnGridBlue(2, 2, 1) });
	if( ~prm.cv.gateEnv1.out == ~modOutput[3], { ~apc.turnGridGreen(3, 2, 2) }, { ~apc.turnGridBlue(3, 2, 1) });
	if( ~prm.cv.gateEnv1.out == ~modOutput[4], { ~apc.turnGridGreen(4, 2, 2) }, { ~apc.turnGridBlue(4, 2, 1) });
	if( ~prm.cv.gateEnv1.out == ~modOutput[5], { ~apc.turnGridGreen(5, 2, 2) }, { ~apc.turnGridBlue(5, 2, 1) });
	if( ~prm.cv.gateEnv1.out == ~modOutput[6], { ~apc.turnGridGreen(6, 2, 2) }, { ~apc.turnGridBlue(6, 2, 1) });
	if( ~prm.cv.gateEnv1.out == ~modOutput[7], { ~apc.turnGridGreen(7, 2, 2) }, { ~apc.turnGridBlue(7, 2, 1) });
}, gateEnv, page);
8.do({ | i | ~apc.setGridFunc(i, 2, { ~prm.cv.gateEnv1.setOutBus(~modOutput[i]); }, bank: gateEnv, page: page); });
~apc.turnGridRed(7, 3, 1, gateEnv, page);
~apc.setGridFunc(7, 3, { ~prm.cv.gateEnv1.setOutBus(~prm.cv.nilBus); }, bank: gateEnv, page: page);

// gate env output 2:
~apc.setGridMonitorFunc(\gateEnv2Output, {
	if( ~prm.cv.gateEnv2.out == ~modOutput[0], { ~apc.turnGridGreen(0, 0, 2) }, { ~apc.turnGridCyan(0, 0, 1) });
	if( ~prm.cv.gateEnv2.out == ~modOutput[1], { ~apc.turnGridGreen(1, 0, 2) }, { ~apc.turnGridCyan(1, 0, 1) });
	if( ~prm.cv.gateEnv2.out == ~modOutput[2], { ~apc.turnGridGreen(2, 0, 2) }, { ~apc.turnGridCyan(2, 0, 1) });
	if( ~prm.cv.gateEnv2.out == ~modOutput[3], { ~apc.turnGridGreen(3, 0, 2) }, { ~apc.turnGridCyan(3, 0, 1) });
	if( ~prm.cv.gateEnv2.out == ~modOutput[4], { ~apc.turnGridGreen(4, 0, 2) }, { ~apc.turnGridCyan(4, 0, 1) });
	if( ~prm.cv.gateEnv2.out == ~modOutput[5], { ~apc.turnGridGreen(5, 0, 2) }, { ~apc.turnGridCyan(5, 0, 1) });
	if( ~prm.cv.gateEnv2.out == ~modOutput[6], { ~apc.turnGridGreen(6, 0, 2) }, { ~apc.turnGridCyan(6, 0, 1) });
	if( ~prm.cv.gateEnv2.out == ~modOutput[7], { ~apc.turnGridGreen(7, 0, 2) }, { ~apc.turnGridCyan(7, 0, 1) });
}, gateEnv, page);
8.do({ | i | ~apc.setGridFunc(i, 0, { ~prm.cv.gateEnv2.setOutBus(~modOutput[i]); }, bank: gateEnv, page: page); });
~apc.turnGridRed(7, 1, 1, gateEnv, page);
~apc.setGridFunc(7, 1, { ~prm.cv.gateEnv2.setOutBus(~prm.cv.nilBus); }, bank: gateEnv, page: page);

///////// Mixer Encoders:

~apc.setMixerEncodersMonitorFunc(\cvADSR, {
	var attack1, decay1, sustain1, release1;
	var attack2, decay2, sustain2, release2;
	attack1 = ~prm.cv.gateEnv1.attackTime.explin(0.01, 5, 0, 127);
	decay1 = ~prm.cv.gateEnv1.decayTime.explin(0.01, 5, 0, 127);
	sustain1 = ~prm.cv.gateEnv1.sustainLevel.linlin(0, 1, 0, 127);
	release1 = ~prm.cv.gateEnv1.releaseTime.explin(0.01, 7, 0, 127);
	attack2 = ~prm.cv.gateEnv2.attackTime.explin(0.01, 5, 0, 127);
	decay2 = ~prm.cv.gateEnv2.decayTime.explin(0.01, 5, 0, 127);
	sustain2 = ~prm.cv.gateEnv2.sustainLevel.linlin(0, 1, 0, 127);
	release2 = ~prm.cv.gateEnv2.releaseTime.explin(0.01, 7, 0, 127);
	~apc.setMixerEncoderValue(1, attack1);
	~apc.setMixerEncoderValue(2, decay1);
	~apc.setMixerEncoderValue(3, sustain1);
	~apc.setMixerEncoderValue(4, release1);
	~apc.setMixerEncoderValue(5, attack2);
	~apc.setMixerEncoderValue(6, decay2);
	~apc.setMixerEncoderValue(7, sustain2);
	~apc.setMixerEncoderValue(8, release2);
}, gateEnv, page);
~apc.setMixerEncoderFunc(1, { | val |
	var attack = val.linexp(0, 127, 0.01, 5);
	~prm.cv.gateEnv1.setAttackTime(attack);
}, gateEnv, page);
~apc.setMixerEncoderFunc(2, { | val |
	var decay = val.linexp(0, 127, 0.01, 5);
	~prm.cv.gateEnv1.setDecayTime(decay);
}, gateEnv, page);
~apc.setMixerEncoderFunc(3, { | val |
	var sustain = val.linlin(0, 127, 0, 1);
	~prm.cv.gateEnv1.setSustainLevel(sustain);
}, gateEnv, page);
~apc.setMixerEncoderFunc(4, { | val |
	var release = val.linexp(0, 127, 0.01, 7);
	~prm.cv.gateEnv1.setReleaseTime(release);
}, gateEnv, page);
~apc.setMixerEncoderFunc(5, { | val |
	var attack = val.linexp(0, 127, 0.01, 5);
	~prm.cv.gateEnv2.setAttackTime(attack);
}, gateEnv, page);
~apc.setMixerEncoderFunc(6, { | val |
	var decay = val.linexp(0, 127, 0.01, 5);
	~prm.cv.gateEnv2.setDecayTime(decay);
}, gateEnv, page);
~apc.setMixerEncoderFunc(7, { | val |
	var sustain = val.linlin(0, 127, 0, 1);
	~prm.cv.gateEnv2.setSustainLevel(sustain);
}, gateEnv, page);
~apc.setMixerEncoderFunc(8, { | val |
	var release = val.linexp(0, 127, 0.01, 7);
	~prm.cv.gateEnv2.setReleaseTime(release);
}, gateEnv, page);

///// Device Encoders:
~apc.setDeviceEncodersMonitorFunc(\cvADSR, {
	var attack1, release1, peak1, curve1;
	var attack2, release2, peak2, curve2;
	attack1 = ~prm.cv.gateEnv1.attackTime.explin(0.01, 10, 0, 127);
	release1 = ~prm.cv.gateEnv1.releaseTime.explin(0.01, 15, 0, 127);
	peak1 = ~prm.cv.gateEnv1.peakLevel.linlin(0, 1, 0, 127);
	curve1 = ~prm.cv.gateEnv1.curve.linlin(-8, 8, 0, 127);
	attack2 = ~prm.cv.gateEnv2.attackTime.explin(0.01, 10, 0, 127);
	release2 = ~prm.cv.gateEnv2.releaseTime.explin(0.01, 15, 0, 127);
	peak2 = ~prm.cv.gateEnv2.peakLevel.linlin(0, 1, 0, 127);
	curve2 = ~prm.cv.gateEnv2.curve.linlin(-8, 8, 0, 127);
	~apc.setDeviceEncoderValue(1, attack1);
	~apc.setDeviceEncoderValue(2, release1);
	~apc.setDeviceEncoderValue(3, peak1);
	~apc.setDeviceEncoderValue(4, curve1);
	~apc.setDeviceEncoderValue(5, attack2);
	~apc.setDeviceEncoderValue(6, release2);
	~apc.setDeviceEncoderValue(7, peak2);
	~apc.setDeviceEncoderValue(8, curve2);
}, gateEnv, page);

~apc.setDeviceEncoderFunc(1, { | val |
	var attack = val.linexp(0, 127, 0.01, 10);
	~prm.cv.gateEnv1.setAttackTime(attack);
}, gateEnv, page);
~apc.setDeviceEncoderFunc(2, { | val |
	var release = val.linexp(0, 127, 0.01, 15);
	~prm.cv.gateEnv1.setReleaseTime(release);
}, gateEnv, page);
~apc.setDeviceEncoderFunc(3, { | val |
	var peak = val.linlin(0, 127, 0, 1);
	~prm.cv.gateEnv1.setPeakLevel(peak);
}, gateEnv, page);
~apc.setDeviceEncoderFunc(4, { | val |
	var curve = val.linlin(0, 127, -8, 8);
	~prm.cv.gateEnv1.setCurve(curve);
}, gateEnv, page);
~apc.setDeviceEncoderFunc(5, { | val |
	var attack = val.linexp(0, 127, 0.01, 10);
	~prm.cv.gateEnv2.setAttackTime(attack);
}, gateEnv, page);
~apc.setDeviceEncoderFunc(6, { | val |
	var release = val.linexp(0, 127, 0.01, 15);
	~prm.cv.gateEnv2.setReleaseTime(release);
}, gateEnv, page);
~apc.setDeviceEncoderFunc(7, { | val |
	var peak = val.linlin(0, 127, 0, 1);
	~prm.cv.gateEnv2.setPeakLevel(peak);
}, gateEnv, page);
~apc.setDeviceEncoderFunc(8, { | val |
	var curve = val.linlin(0, 127, -8, 8);
	~prm.cv.gateEnv2.setCurve(curve);
}, gateEnv, page);

////////////////////////
/////// GATES: ////////
//////////////////////

//// outputs:
// gate output 1:
~apc.setGridMonitorFunc(\gate1Output, {
	if( ~prm.cv.gate1.out == ~modOutput[0], { ~apc.turnGridGreen(0, 2, 2) }, { ~apc.turnGridPink(0, 2, 1) });
	if( ~prm.cv.gate1.out == ~modOutput[1], { ~apc.turnGridGreen(1, 2, 2) }, { ~apc.turnGridPink(1, 2, 1) });
	if( ~prm.cv.gate1.out == ~modOutput[2], { ~apc.turnGridGreen(2, 2, 2) }, { ~apc.turnGridPink(2, 2, 1) });
	if( ~prm.cv.gate1.out == ~modOutput[3], { ~apc.turnGridGreen(3, 2, 2) }, { ~apc.turnGridPink(3, 2, 1) });
	if( ~prm.cv.gate1.out == ~modOutput[4], { ~apc.turnGridGreen(4, 2, 2) }, { ~apc.turnGridPink(4, 2, 1) });
	if( ~prm.cv.gate1.out == ~modOutput[5], { ~apc.turnGridGreen(5, 2, 2) }, { ~apc.turnGridPink(5, 2, 1) });
	if( ~prm.cv.gate1.out == ~modOutput[6], { ~apc.turnGridGreen(6, 2, 2) }, { ~apc.turnGridPink(6, 2, 1) });
	if( ~prm.cv.gate1.out == ~modOutput[7], { ~apc.turnGridGreen(7, 2, 2) }, { ~apc.turnGridPink(7, 2, 1) });
}, gate, page);
8.do({ | i | ~apc.setGridFunc(i, 2, { ~prm.cv.gate1.setOutBus(~modOutput[i]); }, bank: gate, page: page); });
~apc.turnGridRed(7, 3, 1, gate, page);
~apc.setGridFunc(7, 3, { ~prm.cv.gate1.setOutBus(~prm.cv.nilBus); }, bank: gate, page: page);

// gate output 2:
~apc.setGridMonitorFunc(\gate2Output, {
	if( ~prm.cv.gate2.out == ~modOutput[0], { ~apc.turnGridGreen(0, 0, 2) }, { ~apc.turnGridPurple(0, 0, 1) });
	if( ~prm.cv.gate2.out == ~modOutput[1], { ~apc.turnGridGreen(1, 0, 2) }, { ~apc.turnGridPurple(1, 0, 1) });
	if( ~prm.cv.gate2.out == ~modOutput[2], { ~apc.turnGridGreen(2, 0, 2) }, { ~apc.turnGridPurple(2, 0, 1) });
	if( ~prm.cv.gate2.out == ~modOutput[3], { ~apc.turnGridGreen(3, 0, 2) }, { ~apc.turnGridPurple(3, 0, 1) });
	if( ~prm.cv.gate2.out == ~modOutput[4], { ~apc.turnGridGreen(4, 0, 2) }, { ~apc.turnGridPurple(4, 0, 1) });
	if( ~prm.cv.gate2.out == ~modOutput[5], { ~apc.turnGridGreen(5, 0, 2) }, { ~apc.turnGridPurple(5, 0, 1) });
	if( ~prm.cv.gate2.out == ~modOutput[6], { ~apc.turnGridGreen(6, 0, 2) }, { ~apc.turnGridPurple(6, 0, 1) });
	if( ~prm.cv.gate2.out == ~modOutput[7], { ~apc.turnGridGreen(7, 0, 2) }, { ~apc.turnGridPurple(7, 0, 1) });
}, gate, page);
8.do({ | i | ~apc.setGridFunc(i, 0, { ~prm.cv.gate2.setOutBus(~modOutput[i]); }, bank: gate, page: page); });
~apc.turnGridRed(7, 1, 1, gate, page);
~apc.setGridFunc(7, 1, { ~prm.cv.gate2.setOutBus(~prm.cv.nilBus); }, bank: gate, page: page);

//// gate 1:
~apc.turnGridYellow(0, 3, 2, gate, page);
~apc.setGridFunc(0, 3, {
	~prm.cv.gate1.makeGate(gate1Val);
	~apc.turnGridGreen(0, 3);
}, 'noteOn', gate, page);
~apc.setGridFunc(0, 3, {
	~prm.cv.gate1.releaseGate;
	~apc.turnGridYellow(0, 3);
}, 'noteOff', gate, page);

// gate 2:
~apc.turnGridYellowGreen(0, 1, 2, gate, page);
~apc.setGridFunc(0, 1, {
	~prm.cv.gate2.makeGate(gate2Val);
	~apc.turnGridGreen(0, 1);
}, 'noteOn', gate, page);
~apc.setGridFunc(0, 1, {
	~prm.cv.gate2.releaseGate;
	~apc.turnGridYellowGreen(0, 1);
}, 'noteOff', gate, page);


// gate vals:
~apc.setMixerEncodersMonitorFunc(\gate, {
	var gate1 = gate1Val.linlin(-1, 1, 0, 127);
	var gate2 = gate2Val.linlin(-1, 1, 0, 127);
	~apc.setMixerEncoderValue(1, gate1);
	~apc.setMixerEncoderValue(2, gate2);
}, gate, page);
~apc.setMixerEncoderFunc(1, { | val |
	var gate = val.linlin(0, 127, -1, 1);
	gate1Val = gate;
}, gate, page);
~apc.setMixerEncoderFunc(2, { | val |
	var gate = val.linlin(0, 127, -1, 1);
	gate2Val = gate;
}, gate, page);

////////////////////////////
/////// CONSTANTS: ////////
//////////////////////////

//// outputs:
// constant output 1:
~apc.setGridMonitorFunc(\constant1Out, {
	if( ~prm.cv.constant1.out == ~modOutput[0], { ~apc.turnGridGreen(0, 3, 2) }, { ~apc.turnGridWhite(0, 3, 1) });
	if( ~prm.cv.constant1.out == ~modOutput[1], { ~apc.turnGridGreen(1, 3, 2) }, { ~apc.turnGridWhite(1, 3, 1) });
	if( ~prm.cv.constant1.out == ~modOutput[2], { ~apc.turnGridGreen(2, 3, 2) }, { ~apc.turnGridWhite(2, 3, 1) });
	if( ~prm.cv.constant1.out == ~modOutput[3], { ~apc.turnGridGreen(3, 3, 2) }, { ~apc.turnGridWhite(3, 3, 1) });
	if( ~prm.cv.constant1.out == ~modOutput[4], { ~apc.turnGridGreen(4, 3, 2) }, { ~apc.turnGridWhite(4, 3, 1) });
	if( ~prm.cv.constant1.out == ~modOutput[5], { ~apc.turnGridGreen(5, 3, 2) }, { ~apc.turnGridWhite(5, 3, 1) });
	if( ~prm.cv.constant1.out == ~modOutput[6], { ~apc.turnGridGreen(6, 3, 2) }, { ~apc.turnGridWhite(6, 3, 1) });
	if( ~prm.cv.constant1.out == ~modOutput[7], { ~apc.turnGridGreen(7, 3, 2) }, { ~apc.turnGridWhite(7, 3, 1) });
}, constant, page);
8.do({ | i | ~apc.setGridFunc(i, 3, { ~prm.cv.constant1.setOutBus(~modOutput[i]); }, bank: constant, page: page); });
~apc.turnClipStopButtonOn(1, constant, page);
~apc.setClipStopFunc(1, { ~prm.cv.constant1.setOutBus(~prm.cv.nilBus);}, bank: constant, page: page);


// constant output 2:
~apc.setGridMonitorFunc(\constant2Out, {
	if( ~prm.cv.constant2.out == ~modOutput[0], { ~apc.turnGridGreen(0, 2, 2) }, { ~apc.turnGridYellow(0, 2, 1) });
	if( ~prm.cv.constant2.out == ~modOutput[1], { ~apc.turnGridGreen(1, 2, 2) }, { ~apc.turnGridYellow(1, 2, 1) });
	if( ~prm.cv.constant2.out == ~modOutput[2], { ~apc.turnGridGreen(2, 2, 2) }, { ~apc.turnGridYellow(2, 2, 1) });
	if( ~prm.cv.constant2.out == ~modOutput[3], { ~apc.turnGridGreen(3, 2, 2) }, { ~apc.turnGridYellow(3, 2, 1) });
	if( ~prm.cv.constant2.out == ~modOutput[4], { ~apc.turnGridGreen(4, 2, 2) }, { ~apc.turnGridYellow(4, 2, 1) });
	if( ~prm.cv.constant2.out == ~modOutput[5], { ~apc.turnGridGreen(5, 2, 2) }, { ~apc.turnGridYellow(5, 2, 1) });
	if( ~prm.cv.constant2.out == ~modOutput[6], { ~apc.turnGridGreen(6, 2, 2) }, { ~apc.turnGridYellow(6, 2, 1) });
	if( ~prm.cv.constant2.out == ~modOutput[7], { ~apc.turnGridGreen(7, 2, 2) }, { ~apc.turnGridYellow(7, 2, 1) });
}, constant, page);
8.do({ | i | ~apc.setGridFunc(i, 2, { ~prm.cv.constant2.setOutBus(~modOutput[i]); }, bank: constant, page: page); });
~apc.turnClipStopButtonOn(2, constant, page);
~apc.setClipStopFunc(2, { ~prm.cv.constant2.setOutBus(~prm.cv.nilBus);}, bank: constant, page: page);

// constant output 3:
~apc.setGridMonitorFunc(\constant3Out, {
	if( ~prm.cv.constant3.out == ~modOutput[0], { ~apc.turnGridGreen(0, 1, 2) }, { ~apc.turnGridBlue(0, 1, 1) });
	if( ~prm.cv.constant3.out == ~modOutput[1], { ~apc.turnGridGreen(1, 1, 2) }, { ~apc.turnGridBlue(1, 1, 1) });
	if( ~prm.cv.constant3.out == ~modOutput[2], { ~apc.turnGridGreen(2, 1, 2) }, { ~apc.turnGridBlue(2, 1, 1) });
	if( ~prm.cv.constant3.out == ~modOutput[3], { ~apc.turnGridGreen(3, 1, 2) }, { ~apc.turnGridBlue(3, 1, 1) });
	if( ~prm.cv.constant3.out == ~modOutput[4], { ~apc.turnGridGreen(4, 1, 2) }, { ~apc.turnGridBlue(4, 1, 1) });
	if( ~prm.cv.constant3.out == ~modOutput[5], { ~apc.turnGridGreen(5, 1, 2) }, { ~apc.turnGridBlue(5, 1, 1) });
	if( ~prm.cv.constant3.out == ~modOutput[6], { ~apc.turnGridGreen(6, 1, 2) }, { ~apc.turnGridBlue(6, 1, 1) });
	if( ~prm.cv.constant3.out == ~modOutput[7], { ~apc.turnGridGreen(7, 1, 2) }, { ~apc.turnGridBlue(7, 1, 1) });
}, constant, page);
8.do({ | i | ~apc.setGridFunc(i, 1, { ~prm.cv.constant3.setOutBus(~modOutput[i]); }, bank: constant, page: page); });
~apc.turnClipStopButtonOn(3, constant, page);
~apc.setClipStopFunc(3, { ~prm.cv.constant3.setOutBus(~prm.cv.nilBus);}, bank: constant, page: page);

// constant output 4:
~apc.setGridMonitorFunc(\constant4Out, {
	if( ~prm.cv.constant4.out == ~modOutput[0], { ~apc.turnGridGreen(0, 0, 2) }, { ~apc.turnGridPurple(0, 0, 1) });
	if( ~prm.cv.constant4.out == ~modOutput[1], { ~apc.turnGridGreen(1, 0, 2) }, { ~apc.turnGridPurple(1, 0, 1) });
	if( ~prm.cv.constant4.out == ~modOutput[2], { ~apc.turnGridGreen(2, 0, 2) }, { ~apc.turnGridPurple(2, 0, 1) });
	if( ~prm.cv.constant4.out == ~modOutput[3], { ~apc.turnGridGreen(3, 0, 2) }, { ~apc.turnGridPurple(3, 0, 1) });
	if( ~prm.cv.constant4.out == ~modOutput[4], { ~apc.turnGridGreen(4, 0, 2) }, { ~apc.turnGridPurple(4, 0, 1) });
	if( ~prm.cv.constant4.out == ~modOutput[5], { ~apc.turnGridGreen(5, 0, 2) }, { ~apc.turnGridPurple(5, 0, 1) });
	if( ~prm.cv.constant4.out == ~modOutput[6], { ~apc.turnGridGreen(6, 0, 2) }, { ~apc.turnGridPurple(6, 0, 1) });
	if( ~prm.cv.constant4.out == ~modOutput[7], { ~apc.turnGridGreen(7, 0, 2) }, { ~apc.turnGridPurple(7, 0, 1) });
}, constant, page);
8.do({ | i | ~apc.setGridFunc(i, 0, { ~prm.cv.constant4.setOutBus(~modOutput[i]); }, bank: constant, page: page); });
~apc.turnClipStopButtonOn(4, constant, page);
~apc.setClipStopFunc(4, { ~prm.cv.constant4.setOutBus(~prm.cv.nilBus);}, bank: constant, page: page);

///// reset:
4.do({ | i | ~apc.turnClipStopButtonOn(i+5, constant, page); });
~apc.setClipStopFunc(5, {
	~prm.cv.constant1.setValue(0);
	~apc.turnClipStopButtonOff(5);
}, 'noteOn', constant, page);
~apc.setClipStopFunc(5, {
	~apc.turnClipStopButtonOn(5);
}, 'noteOff', constant, page);

~apc.setClipStopFunc(6, {
	~prm.cv.constant2.setValue(0);
	~apc.turnClipStopButtonOff(6);
}, 'noteOn', constant, page);
~apc.setClipStopFunc(6, {
	~apc.turnClipStopButtonOn(6);
}, 'noteOff', constant, page);

~apc.setClipStopFunc(7, {
	~prm.cv.constant3.setValue(0);
	~apc.turnClipStopButtonOff(7);
}, 'noteOn', constant, page);
~apc.setClipStopFunc(7, {
	~apc.turnClipStopButtonOn(7);
}, 'noteOff', constant, page);

~apc.setClipStopFunc(8, {
	~prm.cv.constant4.setValue(0);
	~apc.turnClipStopButtonOff(8);
}, 'noteOn', constant, page);
~apc.setClipStopFunc(8, {
	~apc.turnClipStopButtonOn(8);
}, 'noteOff', constant, page);

//////// Mixer Encoders:

~apc.setMixerEncodersMonitorFunc(\constants, {
	var c1, c2, c3, c4, c11, c22, c33, c44;
	c1 = ~prm.cv.constant1.value.linlin(-0.25, 0.25, 0, 127);
	c2 = ~prm.cv.constant2.value.linlin(-0.25, 0.25, 0, 127);
	c3 = ~prm.cv.constant3.value.linlin(-0.25, 0.25, 0, 127);
	c4 = ~prm.cv.constant4.value.linlin(-0.25, 0.25, 0, 127);
	c11 = ~prm.cv.constant1.value.linlin(-0.5, 0.5, 0, 127);
	c22 = ~prm.cv.constant2.value.linlin(-0.5, 0.5, 0, 127);
	c33 = ~prm.cv.constant3.value.linlin(-0.5, 0.5, 0, 127);
	c44 = ~prm.cv.constant4.value.linlin(-0.5, 0.5, 0, 127);
	~apc.setMixerEncoderValue(1, c1);
	~apc.setMixerEncoderValue(2, c2);
	~apc.setMixerEncoderValue(3, c3);
	~apc.setMixerEncoderValue(4, c4);
	~apc.setMixerEncoderValue(5, c11);
	~apc.setMixerEncoderValue(6, c22);
	~apc.setMixerEncoderValue(7, c33);
	~apc.setMixerEncoderValue(8, c44);
}, constant, page);
~apc.setMixerEncoderFunc(1, { | val |
	var value = val.linlin(0, 127, -0.25, 0.25);
	~prm.cv.constant1.setValue(value);
}, constant, page);
~apc.setMixerEncoderFunc(2, { | val |
	var value = val.linlin(0, 127, -0.25, 0.25);
	~prm.cv.constant2.setValue(value);
}, constant, page);
~apc.setMixerEncoderFunc(3, { | val |
	var value = val.linlin(0, 127, -0.25, 0.25);
	~prm.cv.constant3.setValue(value);
}, constant, page);
~apc.setMixerEncoderFunc(4, { | val |
	var value = val.linlin(0, 127, -0.25, 0.25);
	~prm.cv.constant4.setValue(value);
}, constant, page);
~apc.setMixerEncoderFunc(5, { | val |
	var value = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.constant1.setValue(value);
}, constant, page);
~apc.setMixerEncoderFunc(6, { | val |
	var value = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.constant2.setValue(value);
}, constant, page);
~apc.setMixerEncoderFunc(7, { | val |
	var value = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.constant3.setValue(value);
}, constant, page);
~apc.setMixerEncoderFunc(8, { | val |
	var value = val.linlin(0, 127, -0.5, 0.5);
	~prm.cv.constant4.setValue(value);
}, constant, page);

//// Device Encoder Funcs:
~apc.setDeviceEncodersMonitorFunc(\constant, {
	var c1, c2, c3, c4;
	c1 = ~prm.cv.constant1.value.linlin(-1, 1, 0, 127);
	c2 = ~prm.cv.constant2.value.linlin(-1, 1, 0, 127);
	c3 = ~prm.cv.constant3.value.linlin(-1, 1, 0, 127);
	c4 = ~prm.cv.constant4.value.linlin(-1, 1, 0, 127);
	~apc.setDeviceEncoderValue(1, c1);
	~apc.setDeviceEncoderValue(2, c2);
	~apc.setDeviceEncoderValue(3, c3);
	~apc.setDeviceEncoderValue(4, c4);
}, constant, page);
~apc.setDeviceEncoderFunc(1, { | val |
	var value = val.linlin(0, 127, -1, 1);
	~prm.cv.constant1.setValue(value);
}, constant, page);
~apc.setDeviceEncoderFunc(2, { | val |
	var value = val.linlin(0, 127, -1, 1);
	~prm.cv.constant2.setValue(value);
}, constant, page);
~apc.setDeviceEncoderFunc(3, { | val |
	var value = val.linlin(0, 127, -1, 1);
	~prm.cv.constant3.setValue(value);
}, constant, page);
~apc.setDeviceEncoderFunc(4, { | val |
	var value = val.linlin(0, 127, -1, 1);
	~prm.cv.constant4.setValue(value);
}, constant, page);


/////////////////////////
//////// FREQS: ////////
///////////////////////


///////// bank switching:

~apc.turnGridGreen(0, 3, 2, freq, page);
4.do({ | i | ~apc.turnGridWhite(i+1, 3, 2, freq, page); });

~apc.setGridFunc(1, 3, { ~apc.setActiveGridBank(freq1Chromatic) }, bank: freq, page: page);
~apc.setGridFunc(2, 3, { ~apc.setActiveGridBank(freq1Diatonic) }, bank: freq, page: page);
~apc.setGridFunc(3, 3, { ~apc.setActiveGridBank(freq2Chromatic) }, bank: freq, page: page);
~apc.setGridFunc(4, 3, { ~apc.setActiveGridBank(freq2Diatonic) }, bank: freq, page: page);

4.do({ | i |
	var bank = i + 9;
	~apc.turnGridWhite(0, 4, 2, bank, page);
	~apc.setGridMonitorFunc(\freqScale, {
		if( ~apc.activeGridBank == 9, { ~apc.turnGridGreen(1, 4, 2) }, { ~apc.turnGridWhite(1, 4, 1) });
		if( ~apc.activeGridBank == 10, { ~apc.turnGridGreen(2, 4, 2) }, { ~apc.turnGridWhite(2, 4, 1) });
		if( ~apc.activeGridBank == 11, { ~apc.turnGridGreen(3, 4, 2) }, { ~apc.turnGridWhite(3, 4, 1) });
		if( ~apc.activeGridBank == 12, { ~apc.turnGridGreen(4, 4, 2) }, { ~apc.turnGridWhite(4, 4, 1) });
	}, bank, page);
	~apc.setGridFunc(0, 4, { ~apc.setActiveGridBank(freq) }, bank: bank, page: page);
	~apc.setGridFunc(1, 4, { ~apc.setActiveGridBank(freq1Chromatic) }, bank: bank, page: page);
	~apc.setGridFunc(2, 4, { ~apc.setActiveGridBank(freq1Diatonic) }, bank: bank, page: page);
	~apc.setGridFunc(3, 4, { ~apc.setActiveGridBank(freq2Chromatic) }, bank: bank, page: page);
	~apc.setGridFunc(4, 4, { ~apc.setActiveGridBank(freq2Diatonic) }, bank: bank, page: page);
});

// freq 1 oct scale:
2.do({ | i |
	var bank = i + 9;
	~apc.setGridMonitorFunc(\freq1OctScale, {
		if( freq1OctScale == 0, { ~apc.turnGridLightBlue(6, 4); ~apc.turnGridLightBlue(7, 4); });
		if( freq1OctScale == -1, { ~apc.turnGridYellow(6, 4); ~apc.turnGridLightBlue(7, 4); });
		if( freq1OctScale == -2, { ~apc.turnGridWhite(6, 4); ~apc.turnGridLightBlue(7, 4); });
		if( freq1OctScale == 1, { ~apc.turnGridLightBlue(6, 4); ~apc.turnGridYellow(7, 4); });
		if( freq1OctScale == 2, {~apc.turnGridLightBlue(6, 4); ~apc.turnGridWhite(7, 4); });
	}, bank, page);
	//// octave shifts:
	~apc.setGridFunc(6, 4, {
		if( freq1OctScale != -2, { freq1OctScale = (freq1OctScale -1); });
		freq1OctScale.postln;
	}, bank: bank, page: page);
	~apc.setGridFunc(7, 4, {
		if( freq1OctScale !=2, { freq1OctScale = (freq1OctScale + 1); });
		freq1OctScale.postln;
	}, bank: bank, page: page);
});

// freq 2 oct scale:
2.do({ | i |
	var bank = i + 11;
	~apc.setGridMonitorFunc(\freq2OctScale, {
		if( freq2OctScale == 0, { ~apc.turnGridLightBlue(6, 4); ~apc.turnGridLightBlue(7, 4); });
		if( freq2OctScale == -1, { ~apc.turnGridYellow(6, 4); ~apc.turnGridLightBlue(7, 4); });
		if( freq2OctScale == -2, { ~apc.turnGridWhite(6, 4); ~apc.turnGridLightBlue(7, 4); });
		if( freq2OctScale == 1,  { ~apc.turnGridLightBlue(6, 4); ~apc.turnGridYellow(7, 4); });
		if( freq2OctScale == 2, {~apc.turnGridLightBlue(6, 4); ~apc.turnGridWhite(7, 4); });
	}, bank, page);
	//// octave shifts:
	~apc.setGridFunc(6, 4, {
		if( freq2OctScale != -2, { freq2OctScale = (freq2OctScale -1); });
	}, bank: bank, page: page);
	~apc.setGridFunc(7, 4, {
		if( freq2OctScale != 2, { freq2OctScale = (freq2OctScale + 1) });
	}, bank: bank, page: page);
});

//// freq1 output:

~apc.setGridMonitorFunc(\freq1Output, {
	if( ~prm.cv.freq1.out == ~modOutput[0], { ~apc.turnGridGreen(0, 2, 2) }, { ~apc.turnGridYellow(0, 2, 1) });
	if( ~prm.cv.freq1.out == ~modOutput[1], { ~apc.turnGridGreen(1, 2, 2) }, { ~apc.turnGridYellow(1, 2, 1) });
	if( ~prm.cv.freq1.out == ~modOutput[2], { ~apc.turnGridGreen(2, 2, 2) }, { ~apc.turnGridYellow(2, 2, 1) });
	if( ~prm.cv.freq1.out == ~modOutput[3], { ~apc.turnGridGreen(3, 2, 2) }, { ~apc.turnGridYellow(3, 2, 1) });
	if( ~prm.cv.freq1.out == ~modOutput[4], { ~apc.turnGridGreen(4, 2, 2) }, { ~apc.turnGridYellow(4, 2, 1) });
	if( ~prm.cv.freq1.out == ~modOutput[5], { ~apc.turnGridGreen(5, 2, 2) }, { ~apc.turnGridYellow(5, 2, 1) });
	if( ~prm.cv.freq1.out == ~modOutput[6], { ~apc.turnGridGreen(6, 2, 2) }, { ~apc.turnGridYellow(6, 2, 1) });
	if( ~prm.cv.freq1.out == ~modOutput[7], { ~apc.turnGridGreen(7, 2, 2) }, { ~apc.turnGridYellow(7, 2, 1) });
}, freq, page);
8.do({ | i | ~apc.setGridFunc(i, 2, { ~prm.cv.freq1.setOutBus(~modOutput[i]); }, bank: freq, page: page); });
~apc.turnGridRed(7, 3, 1, freq, page);
~apc.setGridFunc(7, 3, { ~prm.cv.freq1.setOutBus(~prm.cv.nilBus); }, bank: freq, page: page);

~apc.setGridMonitorFunc(\freq2Output, {
	if( ~prm.cv.freq2.out == ~modOutput[0], { ~apc.turnGridGreen(0, 0, 2) }, { ~apc.turnGridYellowGreen(0, 0, 1) });
	if( ~prm.cv.freq2.out == ~modOutput[1], { ~apc.turnGridGreen(1, 0, 2) }, { ~apc.turnGridYellowGreen(1, 0, 1) });
	if( ~prm.cv.freq2.out == ~modOutput[2], { ~apc.turnGridGreen(2, 0, 2) }, { ~apc.turnGridYellowGreen(2, 0, 1) });
	if( ~prm.cv.freq2.out == ~modOutput[3], { ~apc.turnGridGreen(3, 0, 2) }, { ~apc.turnGridYellowGreen(3, 0, 1) });
	if( ~prm.cv.freq2.out == ~modOutput[4], { ~apc.turnGridGreen(4, 0, 2) }, { ~apc.turnGridYellowGreen(4, 0, 1) });
	if( ~prm.cv.freq2.out == ~modOutput[5], { ~apc.turnGridGreen(5, 0, 2) }, { ~apc.turnGridYellowGreen(5, 0, 1) });
	if( ~prm.cv.freq2.out == ~modOutput[6], { ~apc.turnGridGreen(6, 0, 2) }, { ~apc.turnGridYellowGreen(6, 0, 1) });
	if( ~prm.cv.freq2.out == ~modOutput[7], { ~apc.turnGridGreen(7, 0, 2) }, { ~apc.turnGridYellowGreen(7, 0, 1) });
}, freq, page);
8.do({ | i | ~apc.setGridFunc(i, 0, { ~prm.cv.freq2.setOutBus(~modOutput[i]); }, bank: freq, page: page); });
~apc.turnGridRed(7, 1, 1, freq, page);
~apc.setGridFunc(7, 1, { ~prm.cv.freq2.setOutBus(~prm.cv.nilBus); }, bank: freq, page: page);



///// chromatic 1 grid:
~apc.setGridMonitorFunc(\freq1Chrom1, {
	/*
	var freq = ~prm.cv.freq1.frequency;
	var base = 48;
	var oct = 12 * freq1OctScale;
	var note = (freq.cpsmidi - oct - base).asInt;
	var whiteKeys = [0, 2, 4, 5, 7, 9, 11, 12];
	var blackKeys = [1, 3, 6, 8, 10];
	var blackPos = [1, 2, 4, 5, 6];
	var cheatArray = [nil, 1, 3, nil, 6, 8, 10, nil];
	8.do({ | i | ~apc.turnGridPurple(i, 0); });
	5.do({ | i | ~apc.turnGridPurple(blackPos[i], 1); });
	note.postln;
	if(whiteKeys.includes(note) && (note <= 12),
		{ ~apc.turnGridGreen(whiteKeys.indexOf(note), 0) },
		{ ~apc.turnGridGreen(cheatArray.indexOf(note), 1); });
	*/
}, freq1Chromatic, page);

~apc.setGridMonitorFunc(\freq1Chrom2, {
	/*
	var freq = ~prm.cv.freq1.frequency;
	var base = 48 + 12;
	var oct = 12 * freq1OctScale;
	var note = freq.cpsmidi - oct - base;
	var whiteKeys = [0, 2, 4, 5, 7, 9, 11, 12];
	var blackKeys = [1, 3, 6, 8, 10];
	var blackPos = [1, 2, 4, 5, 6];
	var cheatArray = [nil, 1, 3, nil, 6, 8, 10, nil];
	8.do({ | i | ~apc.turnGridPink(i, 2); });
	5.do({ | i | ~apc.turnGridPink(blackPos[i], 3); });
	if(whiteKeys.includes(note) && (note >= 0),
		{ ~apc.turnGridGreen(whiteKeys.indexOf(note), 0) },
		{ ~apc.turnGridGreen(cheatArray.indexOf(note), 1); });
	*/
}, freq1Chromatic, page);


	8.do({ | i | ~apc.turnGridPurple(i, 0); });
	5.do({ | i | ~apc.turnGridPurple(blackPos[i], 1); });
	8.do({ | i | ~apc.turnGridPink(i, 2); });
	5.do({ | i | ~apc.turnGridPink(blackPos[i], 3); });

2.do({ | i |
	var whiteVert = i*2;
	var blackVert = (i*2) + 1;
	var base = 48;
	var oct = 12 * freq1OctScale;
	var keyShift = i * 12;
	var note = base + oct + keyShift;
	var whiteKeys = [0, 2, 4, 5, 7, 9, 11, 12];
	var blackKeys = [1, 3, 6, 8, 10];
	var blackPos = [1, 2, 4, 5, 6];
	var cheatArray = [nil, 1, 3, nil, 6, 8, 10, nil];
	8.do({ | white |
		~apc.setGridFunc(white, whiteVert, {
			var playNote = (note + whiteKeys[white]).midicps;
			~prm.cv.freq1.setFreq(playNote);
			playNote.postln;
		}, bank: freq1Chromatic, page: page);
	});
	5.do({ | black |
		~apc.setGridFunc(blackPos[black], blackVert, {
			var playNote = (note + blackKeys[black]).midicps;
			~prm.cv.freq1.setFreq(playNote);
			playNote.postln;
		}, bank: freq1Chromatic, page: page);
	});
});

////// diatonic 1 root:
~apc.turnGridYellow(5, 4, 2, freq1Diatonic, page);
~apc.setGridFunc(5, 4, { ~apc.setActiveGridBank(freq1RootSel);
}, 'noteOn',freq1Diatonic, page);
~apc.turnGridGreen(5, 4, 2, freq1RootSel, page);
~apc.setGridFunc(5, 4, { ~apc.setActiveGridBank(freq1Diatonic);
}, 'noteOff', freq1RootSel, page);

~apc.setGridMonitorFunc(\freq1Root, {
	var whiteKeys = [0, 2, 4, 5, 7, 9, 11];
	var blackKeys = [1, 3, 6, 8, 10];
	var blackPos = [1, 2, 4, 5, 6];
	var cheatArray = [nil, 1, 3, nil, 6, 8, 10, nil];
	7.do({ | i | ~apc.turnGridPurple(i, 0); });
	5.do({ | i | ~apc.turnGridMagenta(blackPos[i], 1); });
	if( whiteKeys.includes(freq1Root),
		{ ~apc.turnGridGreen(whiteKeys.indexOf(freq1Root), 0); },
		{ ~apc.turnGridGreen(cheatArray.indexOf(freq1Root), 1); });
}, freq1RootSel, page);

7.do({ | key |
	var whiteKeys = [0, 2, 4, 5, 7, 9, 11];
	~apc.setGridFunc(key, 0, { freq1Root = whiteKeys[key] },
		'noteOn', freq1RootSel, page);
});
5.do({ | key |
	var blackKeys = [1, 3, 6, 8, 10];
				var blackPos = [1, 2, 4, 5, 6];
	~apc.setGridFunc(blackPos[key], 1, { freq1Root = blackKeys[key]; },
		'noteOn', freq1RootSel, page);
});



////// diatonic 2 root:
~apc.turnGridYellow(5, 4, 2, freq2Diatonic, page);
~apc.setGridFunc(5, 4, { ~apc.setActiveGridBank(freq2RootSel);
}, 'noteOn',freq2Diatonic, page);
~apc.turnGridGreen(5, 4, 2, freq2RootSel, page);
~apc.setGridFunc(5, 4, { ~apc.setActiveGridBank(freq2Diatonic);
}, 'noteOff', freq2RootSel, page);

~apc.setGridMonitorFunc(\freq2Root, {
	var whiteKeys = [0, 2, 4, 5, 7, 9, 11];
	var blackKeys = [1, 3, 6, 8, 10];
	var blackPos = [1, 2, 4, 5, 6];
	var cheatArray = [nil, 1, 3, nil, 6, 8, 10, nil];
	7.do({ | i | ~apc.turnGridPurple(i, 0); });
	5.do({ | i | ~apc.turnGridMagenta(blackPos[i], 1); });
	if( whiteKeys.includes(freq2Root),
		{ ~apc.turnGridGreen(whiteKeys.indexOf(freq2Root), 0); },
		{ ~apc.turnGridGreen(cheatArray.indexOf(freq2Root), 1); });
}, freq2RootSel, page);

7.do({ | key |
	var whiteKeys = [0, 2, 4, 5, 7, 9, 11];
	~apc.setGridFunc(key, 0, { freq2Root = whiteKeys[key] },
		'noteOn', freq2RootSel, page);
});
5.do({ | key |
	var blackKeys = [1, 3, 6, 8, 10];
				var blackPos = [1, 2, 4, 5, 6];
	~apc.setGridFunc(blackPos[key], 1, { freq2Root = blackKeys[key]; },
		'noteOn', freq2RootSel, page);
});






////////////////////////////
////// Pitch/Gate: ////////
//////////////////////////

pitchGate1Reset = { 8.do({ | i | ~apc.turnGridPink(i, 2, 1, pitchGate, page); });};

~apc.setGridMonitorFunc(\pitchGate1Output, {
	pitchGate1Reset.value;
	switch( ~prm.cv.pitchGate1.freqOut,
		0, { ~apc.turnGridGreen(0, 2); ~apc.turnGridYellowGreen(1, 2); },
		1, { ~apc.turnGridGreen(1, 2); ~apc.turnGridYellowGreen(2, 2); },
		2, { ~apc.turnGridGreen(2, 2); ~apc.turnGridYellowGreen(3, 2); },
		3, { ~apc.turnGridGreen(3, 2); ~apc.turnGridYellowGreen(4, 2); },
		4, { ~apc.turnGridGreen(4, 2); ~apc.turnGridYellowGreen(5, 2); },
		5, { ~apc.turnGridGreen(5, 2); ~apc.turnGridYellowGreen(6, 2); },
		6, { ~apc.turnGridGreen(6, 2); ~apc.turnGridYellowGreen(7, 2); },
	);
}, pitchGate, page);
7.do({ | i | ~apc.setGridFunc(i, 2, {
	~prm.cv.pitchGate1.setFreqOut(~modOutput[i]);
	~prm.cv.pitchGate1.setGateOut(~modOutput[i+1]);
}, 'noteOn', pitchGate, page);
});
~apc.turnGridRed(7, 3, 1, pitchGate, page);
~apc.setGridFunc(7, 3, {
	~prm.cv.pitchGate1.setFreqOut(~prm.cv.nilBus);
	~prm.cv.pitchGate1.setGateOut(~prm.cv.nilBus);
}, bank: pitchGate, page: page);

pitchGate2Reset = { 8.do({ | i | ~apc.turnGridPurple(i, 0, 1, pitchGate, page); });};

~apc.setGridMonitorFunc(\pitchGate2Output, {
	pitchGate2Reset.value;
	switch( ~prm.cv.pitchGate2.freqOut,
		0, { ~apc.turnGridGreen(0, 0); ~apc.turnGridYellowGreen(1, 0); },
		1, { ~apc.turnGridGreen(1, 0); ~apc.turnGridYellowGreen(2, 0); },
		2, { ~apc.turnGridGreen(2, 0); ~apc.turnGridYellowGreen(3, 0); },
		3, { ~apc.turnGridGreen(3, 0); ~apc.turnGridYellowGreen(4, 0); },
		4, { ~apc.turnGridGreen(4, 0); ~apc.turnGridYellowGreen(5, 0); },
		5, { ~apc.turnGridGreen(5, 0); ~apc.turnGridYellowGreen(6, 0); },
		6, { ~apc.turnGridGreen(6, 0); ~apc.turnGridYellowGreen(7, 0); },
	);
}, pitchGate, page);
7.do({ | i | ~apc.setGridFunc(i, 0, {
	~prm.cv.pitchGate2.setFreqOut(~modOutput[i]);
	~prm.cv.pitchGate2.setGateOut(~modOutput[i+1]);
}, 'noteOn', pitchGate, page);
});
~apc.turnGridRed(7, 1, 1, pitchGate, page);
~apc.setGridFunc(7, 1, {
	~prm.cv.pitchGate2.setFreqOut(~prm.cv.nilBus);
	~prm.cv.pitchGate2.setGateOut(~prm.cv.nilBus);
}, bank: pitchGate, page: page);
