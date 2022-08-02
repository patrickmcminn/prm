/*
Thursday, April 7th 2022
Years APC.scd
prm
*/

var page = 'years';

var main = 0;

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var fb = 0.75;

~apc.makePage(page);
~apc.addPageLoadFunction(\masterFunc, { ~apc.storageDict[\loadMasterFunc].value; }, page);

~apc.addMixerEncodersBanks(3, page: page);

~apc.setUserButtonFunc({
  ~prm.reverb.loadPreset('years');
	~years.ensemble.unMuteEnsembleInputs;
  ~apc.turnUserButtonOn;
}, bank: main, page: page);

~apc.setSceneLaunchMonitorFunc(\midi, {
  var midi = ~years.midiEnabled;
  if( midi == true, { ~apc.turnSceneLaunchButtonGreen(0) }, { ~apc.turnSceneLaunchButtonWhite(0) });
}, bank: main, page: page);
~apc.setSceneLaunchFunc(0, { ~years.toggleMIDIFuncs }, bank: main, page: page);

//////////////////////
//// Mixer: /////////
////////////////////

~apc.mapMixer("~years.mixer", 8, 0, 0, send0, send1, send2, send3, main, page);
~apc.mapSendBanks(\main, send0, send1, send2, send3, main, page);

// input mutes:
~apc.setMixerMonitorFunc(\inputMutes, {
	var ens = ~years.ensemble.ensembleIsMuted;
	var freeze = ~years.freezeTptsInput.isMuted;
	var tpt = ~years.tpt.isMuted;
	var endTpt = ~years.endTptsInput.isMuted;
	if( ens, { ~apc.turnRecordEnableButtonOff(3) }, { ~apc.turnRecordEnableButtonOn(3) });
	if( freeze, { ~apc.turnRecordEnableButtonOff(6) }, { ~apc.turnRecordEnableButtonOn(6) });
	if( tpt, { ~apc.turnRecordEnableButtonOff(7) }, { ~apc.turnRecordEnableButtonOn(7) });
	if( endTpt, { ~apc.turnRecordEnableButtonOff(8) }, { ~apc.turnRecordEnableButtonOn(8) });
}, main, page);
~apc.setRecordEnableButtonFunc(3, { ~years.ensemble.tglMuteEnsembleInputs;
}, bank: main, page: page);
~apc.setRecordEnableButtonFunc(6, { ~years.freezeTptsInput.tglMute
}, bank: main, page: page);
~apc.setRecordEnableButtonFunc(7, { ~years.tpt.tglMute
}, bank: main, page: page);
~apc.setRecordEnableButtonFunc(8, { ~years.endTptsInput.tglMute
}, bank: main, page: page);

//////////////////////
//// Grid: //////////
////////////////////

//////// click:
~apc.setGridMonitorFunc(\click, {
	if( ~years.click == true,
		{
			4.do({ | i |  ~apc.turnGridWhite(i+4, 4) });

		},
		{
			4.do({ | i | ~apc.turnGridOff(i+4, 4); });

	});
}, main, page);

//////// stuff playing:
~apc.turnGridRed(0, 2, 2, main, page);
~apc.turnGridBlue(1, 3, 2, main, page);

~apc.setGridMonitorFunc(\playing, {
	var isPlaying = ~years.drone.droneIsPlaying;
	var trem = ~years.droneTremRoutIsPlaying;
	var cutoff = ~years.droneCutoffRoutIsPlaying;
	var glock = ~years.glockNoteRout1IsPlaying;
	var freeze = ~years.freezeTpts.isPlaying;
	if( isPlaying, { ~apc.turnGridGreen(0, 4); }, { ~apc.turnGridCyan(0, 4) });
	if( trem, { ~apc.turnGridGreen(1, 4) }, { ~apc.turnGridCyan(1, 4); });
	if(cutoff, { ~apc.turnGridGreen(2, 4) }, { ~apc.turnGridCyan(2, 4); });
	if( glock, { ~apc.turnGridGreen(0, 3) }, { ~apc.turnGridYellow(0, 3) });
	if( freeze, { ~apc.turnGridGreen(1, 2) }, { ~apc.turnGridWhite(1, 2) });
}, main, page);
~apc.setGridFunc(0, 4, { ~years.drone.toggleDrone; }, bank: main, page: page);
~apc.setGridFunc(1, 4, { ~years.toggleDroneTremRout }, bank: main, page: page);
~apc.setGridFunc(2, 4, { ~years.toggleDroneCutoffRout }, bank: main, page: page);
~apc.setGridFunc(0, 3, { ~years.toggleGlockNoteRout1 }, bank: main, page: page);
~apc.setGridFunc(1, 3, { ~years.resetGlockNoteRout1 }, bank: main, page: page);
~apc.setGridFunc(0, 2, { ~years.freezeTpts.recordBuffer; ~apc.turnGridGreen(0, 2) }, bank: main, page: page);
~apc.setGridFunc(1, 2, { ~years.freezeTpts.toggleCluster }, bank: main, page: page);

///// drums:
4.do({ | i | ~apc.turnGridPink(4 + i, 0, 2, main, page); });
4.do({ | i | ~apc.turnGridPink(4 + i, 1, 2, main, page); });
4.do({ | i | ~apc.turnGridPink(4 + i, 2, 2, main, page); });
4.do({ | i | ~apc.turnGridPink(4 + i, 3, 2, main, page); });
4.do({ | i | ~apc.setGridFunc(4 + i, 0, { ~years.drums.drums.playSample(i, -3); }, bank: main, page: page); });
4.do({ | i | ~apc.setGridFunc(4 + i, 1, { ~years.drums.drums.playSample(i+4, -3); }, bank: main, page: page); });
4.do({ | i | ~apc.setGridFunc(4 + i, 2, { ~years.drums.drums.playSample(i+8, -3); }, bank: main, page: page); });
4.do({ | i | ~apc.setGridFunc(4 + i, 3, { ~years.drums.drums.playSample(i+12, -3); }, bank: main, page: page); });


//////////////////////////////
//////// Encoders: //////////
////////////////////////////

~apc.setDeviceEncodersMonitorFunc(\main, {
	var ensDist = ~years.ensemble.distortion.mix.linlin(0, 1, 0, 127);
	var ensDel = ~years.ensemble.delayNetwork1.mixer.vol.dbfsCC;
	var ensGran = ~years.ensemble.granulator.mix.linlin(0, 1, 0, 127);
	var ensFuck = ~years.ensemble.granulator.feedback.linlin(0, 1, 0, 127);
	//var ensAtk = ~years.ensemble.subtractive.attackTime.
	var drumsGranDelay = ~years.drums.granulator.delayLevel.linlin(0, 1, 0, 127);
	var drumsGranFeedback = ~years.drums.granulator.feedback.linlin(0, 1, 0, 127);
	var blockChords = ~years.blockChords.delay.mixer.vol.dbfsCC;
	//var drumsDist = ~years.drums.distortion.mix.linlin(0, 1, 0, 127);
	var delFB = fb.linlin(0, 1, 0, 127);


	~apc.setDeviceEncoderValue(1, ensDist);
	~apc.setDeviceEncoderValue(2, ensDel);
	~apc.setDeviceEncoderValue(3, ensGran);
	~apc.setDeviceEncoderValue(4, ensFuck);

	~apc.setDeviceEncoderValue(5, drumsGranDelay);
	~apc.setDeviceEncoderValue(6, drumsGranFeedback);
	//~apc.setDeviceEncoderValue(7, drumsDist);
	~apc.setDeviceEncoderValue(7, blockChords);
	~apc.setDeviceEncoderValue(8, delFB);

}, bank: main, page: page);

~apc.setDeviceEncoderFunc(1, { | val |
	var dist = val.linlin(0, 127, 0, 1);
	~years.ensemble.distortion.setMix(dist);
}, main, page);

~apc.setDeviceEncoderFunc(2, { | val |
	~years.ensemble.delayNetwork1.mixer.setVol(val.ccdbfs);
	~years.ensemble.delayNetwork2.mixer.setVol(val.ccdbfs);
}, main, page);
~apc.setDeviceEncoderFunc(3, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~years.ensemble.granulator.setMix(mix);
}, main, page);
~apc.setDeviceEncoderFunc(4, { | val |
	var level = val.linlin(0, 127, 0, 1);
	~years.ensemble.granulator.setDelayLevel(level);
	~years.ensemble.granulator.setFeedback(level);
}, main, page);

~apc.setDeviceEncoderFunc(5, { | val |
	var del = val.linlin(0, 127, 0, 1);
	~years.drums.granulator.setDelayLevel(del);
}, main, page);
~apc.setDeviceEncoderFunc(6, { | val |
	var fb = val.linlin(0, 127, 0, 1);
	~years.drums.granulator.setFeedback(fb);
}, main, page);
/*
~apc.setDeviceEncoderFunc(7, {  | val |
	var dist = val.linlin(0, 127, 0, 1);
	~years.drums.distortion.setMix(dist);
}, main, page);
*/

~apc.setDeviceEncoderFunc(7, { | val |
	~years.blockChords.delay.mixer.setVol(val.ccdbfs);
}, main, page);

~apc.setDeviceEncoderFunc(8, { | val |
	var feedback = val.linlin(0, 127, 0, 1);
	var fb1 = val.linlin(0, 127, 0, 1);
	var fb2 = val.linlin(0, 127, 0, 0.95);
	var fb3 = val.linlin(0, 127, 0, 0.66);
	fb = feedback;
	~yearsDelay.delay.setFeedback(0, fb1);
	~yearsDelay.delay.setFeedback(1, fb1);
	~yearsDelay.delay.setFeedback(2, fb2);
	~yearsDelay.delay.setFeedback(3, fb2);
	~yearsDelay.delay.setFeedback(4, fb3);
	~yearsDelay.delay.setFeedback(5, fb3);
}, main, page);



