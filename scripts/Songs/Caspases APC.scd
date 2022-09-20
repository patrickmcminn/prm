/*
Saturday, August 20th 2022
Caspases APC.scd
prm
*/

var page = 'caspases';

var main = 0;

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var junoPreset = 'main';


~apc.makePage(page);
~apc.addPageLoadFunction(\masterFunc, { ~apc.storageDict[\loadMasterFunc].value; }, page);

~apc.addMixerEncodersBanks(3, page: page);

~apc.setUserButtonFunc({
	~prm.reverb.loadPreset('caspases');
	~apc.turnUserButtonOn;
}, bank: main, page: page);

~apc.setSceneLaunchMonitorFunc(\midi, {
	var midi = ~caspases.midiEnabled;
	if( midi == true, { ~apc.turnSceneLaunchButtonGreen(0) }, { ~apc.turnSceneLaunchButtonWhite(0) });
}, bank: main, page: page);
~apc.setSceneLaunchFunc(0, { ~caspases.toggleMIDIFuncs }, bank: main, page: page);

//////////////////////
//// Mixer: /////////
////////////////////

~apc.mapMixer("~caspases.mixer", 8, 0, 0, send0, send1, send2, send3, main, page);
~apc.mapSendBanks(\main, send0, send1, send2, send3, main, page);

// input mutes:
~apc.setMixerMonitorFunc(\inputMutes, {

}, bank: main, page: page);

//////////////////////
//// Grid: //////////
////////////////////

///// input mutes:

~apc.setMixerMonitorFunc(\mutes, {
	var lead = ~caspases.lead.input.isMuted;
	var mel = ~caspases.synthMel.input.isMuted;
	var delays = ~caspases.tptDelaysInput.isMuted;
	var tpt = ~caspases.tpt.isMuted;
	if( lead, { ~apc.turnRecordEnableButtonOff(2) }, { ~apc.turnRecordEnableButtonOn(2) });
	if( mel, { ~apc.turnRecordEnableButtonOff(3) }, { ~apc.turnRecordEnableButtonOn(3); });
	if( delays, { ~apc.turnRecordEnableButtonOff(6) }, { ~apc.turnRecordEnableButtonOn(6) });
	if (tpt, { ~apc.turnRecordEnableButtonOff(8) }, { ~apc.turnRecordEnableButtonOn(8) });
}, bank: main, page: page);

~apc.setRecordEnableButtonFunc(2, { ~caspases.lead.input.tglMute }, bank: main, page: page);
~apc.setRecordEnableButtonFunc(3, { ~caspases.synthMel.input.tglMute }, bank: main, page: page);
~apc.setRecordEnableButtonFunc(6, { ~caspases.tptDelaysInput.tglMute }, bank: main, page: page);
~apc.setRecordEnableButtonFunc(8, { ~caspases.tpt.tglMute }, bank: main, page: page);


//////// presets:
~apc.setGridMonitorFunc(\presetMon, {
	var lead = ~caspases.lead.preset;

	if( lead == 'initial', { ~apc.turnGridGreen(0, 4) }, { ~apc.turnGridWhite(0, 4); });
	if( lead == 'culmination', { ~apc.turnGridGreen(1, 4) }, { ~apc.turnGridWhite(1, 4); });

	if( junoPreset == 'main', { ~apc.turnGridGreen(0, 3) }, { ~apc.turnGridWhite(0, 3); });
	if( junoPreset == 'arrival', {  ~apc.turnGridGreen(1, 3); }, { ~apc.turnGridWhite(1, 3) });

}, bank: main, page: page);

// lead:
~apc.setGridFunc(0, 4, { ~caspases.lead.loadInitial; }, bank: main, page: page);
~apc.setGridFunc(1, 4, { ~caspases.lead.loadCulmination }, bank: main, page: page);
~apc.setGridFunc(0, 3, { ~caspases.pad.readPreset('caspasesJuno'); junoPreset = 'main'; }, bank: main, page: page);
~apc.setGridFunc(1, 3, { ~caspases.pad.readPreset('caspasesArrival'); junoPreset = 'arrival' }, bank: main, page: page);


// juno:

// sends:

// tpt fx mutes:
~apc.setMixerMonitorFunc(\tptFuncs, {
	var sc = if( ~caspases.scTpt.input.isMuted, { ~apc.turnClipStopButtonOff(6) }, { ~apc.turnClipStopButtonOn(6) });
	var dist = if ( ~caspases.distTpt.input.isMuted,
		{ ~apc.turnClipStopButtonOff(7) }, { ~apc.turnClipStopButtonOn(7) });
	var shift = if( ~caspases.shiftTpt.input.isMuted,
		{ ~apc.turnClipStopButtonOff(8) }, { ~apc.turnClipStopButtonOn(8) });
}, bank: main, page: page);
~apc.setClipStopFunc(6, { ~caspases.scTpt.input.tglMute; }, bank: main, page: page);
~apc.setClipStopFunc(7, { ~caspases.distTpt.input.tglMute }, bank: main, page: page);
~apc.setClipStopFunc(8, { ~caspases.shiftTpt.input.tglMute }, bank: main, page: page);


//// volumes:
~apc.setMixerMonitorFunc(\dbTpts, {
	var scTpt = ~caspases.scTpt.mixer.vol;
	var dist = ~caspases.distTpt.mixer.vol;
	var shift = ~caspases.shiftTpt.mixer.vol;
	if( scTpt >= -60, { ~apc.turnGridGreen(5, 0) });
	if( scTpt >= -18, { ~apc.turnGridGreen(5, 1) }, { ~apc.turnGridOff(5, 1) });
	if( scTpt >= -12, { ~apc.turnGridGreen(5, 2) }, { ~apc.turnGridOff(5, 2) });
	if( scTpt >= -3, { ~apc.turnGridGreen(5, 3) }, { ~apc.turnGridOff(5, 3) });
	if( scTpt >= 0, { ~apc.turnGridGreen(5 ,4) }, { ~apc.turnGridOff(5, 4) });

	if( dist >= -60, { ~apc.turnGridGreen(6, 0) });
	if( dist >= -18, { ~apc.turnGridGreen(6, 1) }, { ~apc.turnGridOff(6, 1) });
	if( dist >= -12, { ~apc.turnGridGreen(6, 2) }, { ~apc.turnGridOff(6, 2) });
	if( dist >= -3, { ~apc.turnGridGreen(6, 3) }, { ~apc.turnGridOff(6, 3) });
	if( dist >= 0, { ~apc.turnGridGreen(6 ,4) }, { ~apc.turnGridOff(6, 4) });

	if( shift >= -60, { ~apc.turnGridGreen(7, 0) });
	if( shift >= -18, { ~apc.turnGridGreen(7, 1) }, { ~apc.turnGridOff(7, 1) });
	if( shift >= -12, { ~apc.turnGridGreen(7, 2) }, { ~apc.turnGridOff(7, 2) });
	if( shift >= -3, { ~apc.turnGridGreen(7, 3) }, { ~apc.turnGridOff(7, 3) });
	if( shift >= 0, { ~apc.turnGridGreen(7 ,4) }, { ~apc.turnGridOff(7, 4) });
}, bank: main, page: page);

~apc.setGridFunc(5, 0, { ~caspases.scTpt.mixer.setVol(-60); }, bank: main, page: page);
~apc.setGridFunc(5, 1, { ~caspases.scTpt.mixer.setVol(-18); }, bank: main, page: page);
~apc.setGridFunc(5, 2, { ~caspases.scTpt.mixer.setVol(-12); }, bank: main, page: page);
~apc.setGridFunc(5, 3, { ~caspases.scTpt.mixer.setVol(-3); }, bank: main, page: page);
~apc.setGridFunc(5, 4, { ~caspases.scTpt.mixer.setVol(0); }, bank: main, page: page);

~apc.setGridFunc(6, 0, { ~caspases.distTpt.mixer.setVol(-60); }, bank: main, page: page);
~apc.setGridFunc(6, 1, { ~caspases.distTpt.mixer.setVol(-18); }, bank: main, page: page);
~apc.setGridFunc(6, 2, { ~caspases.distTpt.mixer.setVol(-12); }, bank: main, page: page);
~apc.setGridFunc(6, 3, { ~caspases.distTpt.mixer.setVol(-3); }, bank: main, page: page);
~apc.setGridFunc(6, 4, { ~caspases.distTpt.mixer.setVol(0); }, bank: main, page: page);

~apc.setGridFunc(7, 0, { ~caspases.shiftTpt.mixer.setVol(-60); }, bank: main, page: page);
~apc.setGridFunc(7, 1, { ~caspases.shiftTpt.mixer.setVol(-18); }, bank: main, page: page);
~apc.setGridFunc(7, 2, { ~caspases.shiftTpt.mixer.setVol(-12); }, bank: main, page: page);
~apc.setGridFunc(7, 3, { ~caspases.shiftTpt.mixer.setVol(-3); }, bank: main, page: page);
~apc.setGridFunc(7, 4, { ~caspases.shiftTpt.mixer.setVol(0); }, bank: main, page: page);

/////////
// knobs:
////////
~apc.setGridMonitorFunc(\params, {
	var dist, oct1, oct2;
	var concat, subVol, octVol, squareVol;
	var fb;
	concat = ~caspases.lead.concat.mix.linlin(0, 1, 0, 127);
	subVol = ~caspases.lead.microsynth.subVol.dbfsCC;
	octVol = ~caspases.lead.microsynth.octVol.dbfsCC;
	squareVol = ~caspases.lead.microsynth.squareVol.dbfsCC;
	dist = ~caspases.lead.distortion.mix.linlin(0, 1, 0, 127);
	oct1 = ~caspases.lead.oc2.oct1Vol.dbfsCC;
	oct2 = ~caspases.lead.oc2.oct2Vol.dbfsCC;
	fb = ~caspases.shiftTpt.delay.feedback.linlin(0, 1, 0, 127);
	~apc.setDeviceEncoderValue(1, concat);
	~apc.setDeviceEncoderValue(2, subVol);
	~apc.setDeviceEncoderValue(3, octVol);
	~apc.setDeviceEncoderValue(4, squareVol);
	~apc.setDeviceEncoderValue(5, dist);
	~apc.setDeviceEncoderValue(6, oct1);
	~apc.setDeviceEncoderValue(7, oct2);
	~apc.setDeviceEncoderValue(8, fb);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(1, { | val |
	var mix = val.linlin(0,127, 0, 1);
	~caspases.lead.concat.setMix(mix);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(2, { | val |
	~caspases.lead.microsynth.setSubVol(val.ccdbfs);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(3, { | val |
	~caspases.lead.microsynth.setOctVol(val.ccdbfs);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(4, { | val |
	~caspases.lead.microsynth.setSquareVol(val.ccdbfs);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(5, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~caspases.lead.distortion.setMix(mix);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(6, { | val |
	~caspases.lead.oc2.setOct1Vol(val.ccdbfs);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(7, { | val |
	~caspases.lead.oc2.setOct2Vol(val.ccdbfs);
}, bank: main, page: page);
~apc.setDeviceEncoderFunc(8, { | val |
	var fb = val.linlin(0, 127, 0, 1);
	~caspases.shiftTpt.delay.setFeedback(fb);
}, bank: main, page: page);
