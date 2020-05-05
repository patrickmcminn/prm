/*
Wednesday, April 22nd 2020
Darkness APC.scd
prm
*/

var page = 'darkness';

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var mainBank = 0;

~apc.makePage(page);
~apc.addMixerEncodersBanks(3, page);

//// master funcs:
~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.storageDict[\loadMasterFunc].value;

//// reverb:
~apc.setUserButtonFunc({ ~darkness.setReverb(~prm.reverb) ; ~apc.turnUserButtonOn; }, page: page);
~apc.setUserButtonFunc({ ~apc.turnUserButtonOff }, 'noteOff', page: page);

//// MIDIFuncs:
~apc.setSceneLaunchMonitorFunc(\midi, {
	if( ~darkness.midiFuncsLoaded == true,
		{ ~apc.turnSceneLaunchButtonGreen(0) }, { ~apc.turnSceneLaunchButtonYellow(0) });
}, page: page);
~apc.setSceneLaunchFunc(0, {
	if( ~darkness.midiFuncsLoaded == true, { ~darkness.freeMIDIFuncs }, { ~darkness.makeMIDIFuncs(~pyr); });
}, page: page);

/////////////////
//// Mixer: ////
///////////////

6.do({ | i |
  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~darkness.mixer.setVol(i, val.ccdbfs); }, page: page);
  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~darkness.mixer.tglMute(i); }, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~darkness.mixer.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~darkness.mixer.preOrPost(i) == 'post',
      { ~darkness.mixer.setSendPre(i); }, { ~darkness.mixer.setSendPost(i) });
  }, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~darkness.mixer.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, page: page);

  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val | ~darkness.mixer.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~darkness.mixer.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~darkness.mixer.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~darkness.mixer.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~darkness.mixer.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~darkness.mixer.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~darkness.mixer.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~darkness.mixer.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

  //// send bank changes:
  ~apc.setDeviceButtonsMonitorFunc(\sends, {
    if( ~apc.activeMixerEncodersBank == send0,
      { ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1) });
    if( ~apc.activeMixerEncodersBank  == send1,
      { ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2) });
    if( ~apc.activeMixerEncodersBank  == send2,
      { ~apc.turnDeviceButtonOn(3) }, { ~apc.turnDeviceButtonOff(3) });
    if( ~apc.activeMixerEncodersBank == send3,
      { ~apc.turnDeviceButtonOn(4) }, { ~apc.turnDeviceButtonOff(4) });
  }, page: page);

  ~apc.setDeviceButtonFunc(1, { ~apc.setActiveMixerEncodersBank(send0); }, page: page);
  ~apc.setDeviceButtonFunc(2, { ~apc.setActiveMixerEncodersBank(send1);  }, page: page);
  ~apc.setDeviceButtonFunc(3, { ~apc.setActiveMixerEncodersBank(send2); }, page: page);
  ~apc.setDeviceButtonFunc(4, { ~apc.setActiveMixerEncodersBank(send3); }, page: page);

});

////////// Input Mutes:
~apc.setMixerMonitorFunc(\inputMutes, {
	if( ~darkness.trumpetInput.isMuted,
		{ ~apc.turnRecordEnableButtonOff(3) }, { ~apc.turnRecordEnableButtonOn(3) });
}, page: page);
~apc.setRecordEnableButtonFunc(3, { ~darkness.trumpetInput.tglMute }, page: page);


////////////////
//// Grid: ////
//////////////

~apc.setGridMonitorFunc(\playing, {
	if( ~darkness.introIsPlaying == true,
		{ ~apc.turnGridGreen(0, 4); }, { ~apc.turnGridWhite(0, 4) });
	if( ~darkness.drone1IsPlaying == true,
		{ ~apc.turnGridGreen(0, 3) }, { ~apc.turnGridLightBlue(0, 3) });
	if( ~darkness.drone2IsPlaying == true,
		{ ~apc.turnGridGreen(1, 3) }, { ~apc.turnGridLightBlue(1, 3) });
	if( ~darkness.drone3IsPlaying == true,
		{ ~apc.turnGridGreen(2, 3) }, { ~apc.turnGridLightBlue(2, 3) });
	if( ~darkness.drone4IsPlaying == true,
		{ ~apc.turnGridGreen(3, 3) }, { ~apc.turnGridLightBlue(3, 3) });
	if( ~darkness.drone5IsPlaying == true,
		{ ~apc.turnGridGreen(4, 3) }, { ~apc.turnGridLightBlue(4, 3) });
	if( ~darkness.drone6IsPlaying == true,
		{ ~apc.turnGridGreen(0, 2) }, { ~apc.turnGridLightBlue(0, 2) });
	if( ~darkness.drone7IsPlaying == true,
		{ ~apc.turnGridGreen(1, 2) }, { ~apc.turnGridLightBlue(1, 2) });
	if( ~darkness.drone8IsPlaying == true,
		{ ~apc.turnGridGreen(2, 2) }, { ~apc.turnGridLightBlue(2, 2) });
	if( ~darkness.drone9IsPlaying == true,
		{ ~apc.turnGridGreen(3, 2) }, { ~apc.turnGridLightBlue(3, 2) });
	if( ~darkness.drone10IsPlaying == true,
		{ ~apc.turnGridGreen(4, 2) }, { ~apc.turnGridLightBlue(4, 2) });
}, mainBank, page);

~apc.setGridFunc(0, 4, { ~darkness.tglIntro; }, bank: mainBank, page: page);
~apc.setGridFunc(0, 3, { ~darkness.tglDrone1 }, bank: mainBank, page: page);
~apc.setGridFunc(1, 3, { ~darkness.tglDrone2 }, bank: mainBank, page: page);
~apc.setGridFunc(2, 3, { ~darkness.tglDrone3 }, bank: mainBank, page: page);
~apc.setGridFunc(3, 3, { ~darkness.tglDrone4 }, bank: mainBank, page: page);
~apc.setGridFunc(4, 3, { ~darkness.tglDrone5 }, bank: mainBank, page: page);
~apc.setGridFunc(0, 2, { ~darkness.tglDrone6 }, bank: mainBank, page: page);
~apc.setGridFunc(1, 2, { ~darkness.tglDrone7 }, bank: mainBank, page: page);
~apc.setGridFunc(2, 2, { ~darkness.tglDrone8 }, bank: mainBank, page: page);
~apc.setGridFunc(3, 2, { ~darkness.tglDrone9 }, bank: mainBank, page: page);
~apc.setGridFunc(4, 2, { ~darkness.tglDrone10 }, bank: mainBank, page: page);

///////// trumpet:

~apc.setGridMonitorFunc(\trumpet, {
	if( ~darkness.trumpet.mixer.isMuted(1),
		{ ~apc.turnGridRed(0, 1) }, { ~apc.turnGridGreen(0, 1) });
	if(~darkness.trumpet.multiHarmonizer.isBypassed,
		{ ~apc.turnGridRed(1, 1) }, { ~apc.turnGridGreen(1, 1); });
}, mainBank, page);
~apc.setGridFunc(0, 1, { ~darkness.trumpet.mixer.tglMute(1) }, bank: mainBank, page: page);
~apc.setGridFunc(1, 1, { ~darkness.trumpet.multiHarmonizer.tglBypass }, bank: mainBank, page: page);

//////// spuds:

~apc.setGridMonitorFunc(\spuds, {
	if( ~darkness.spuds.synth.filterType == 0,
		{ ~apc.turnGridGreen(0, 0) }, { ~apc.turnGridWhite(0, 0); });
	if( ~darkness.spuds.synth.filterType == 1,
		{ ~apc.turnGridGreen(1, 0) }, { ~apc.turnGridWhite(1, 0) });
}, mainBank, page);
~apc.setGridFunc(0, 0, { ~darkness.spuds.synth.setFilterType('lowPass') }, bank: mainBank, page: page);
~apc.setGridFunc(1, 0, { ~darkness.spuds.synth.setFilterType('highPass') }, bank: mainBank, page: page);

//////// Device Encoders:
~apc.setDeviceEncodersMonitorFunc(\main, {
	var filterCutoff = ~darkness.spuds.synth.filterCutoff.explin(40, 20000, 0, 127);
	var granulatorMix = ~darkness.spuds.granulator.crossfade.linlin(-1, 1, 0, 127);
	var delayMix = ~darkness.spuds.granulator.delay.mix.linlin(0, 1, 0, 127);
	var delayFeedback = ~darkness.spuds.granulator.delay.feedback.linlin(0, 1, 0, 127);
	var noiseCutoff = ~darkness.noise.lowPass.filterCutoff.explin(20, 20000, 0, 127);
	var highPass = ~prm.masterEQ.highPassCutoff.explin(1, 900, 0, 127);
	~apc.setDeviceEncoderValue(1, filterCutoff);
	~apc.setDeviceEncoderValue(2, granulatorMix);
	~apc.setDeviceEncoderValue(3, delayMix);
	~apc.setDeviceEncoderValue(4, delayFeedback);
	~apc.setDeviceEncoderValue(5, noiseCutoff);
	~apc.setDeviceEncoderValue(8, highPass);
}, mainBank, page);

~apc.setDeviceEncoderFunc(1, { | val |
	var cutoff = val.linexp(0, 127, 40, 20000);
	~darkness.spuds.synth.setFilterCutoff(cutoff);
}, mainBank, page);
~apc.setDeviceEncoderFunc(2, { | val |
	var mix = val.linlin(0, 127, -1, 1);
	~darkness.spuds.granulator.setGranulatorCrossfade(mix);
}, mainBank, page);
~apc.setDeviceEncoderFunc(3, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~darkness.spuds.granulator.delay.setMix(mix);
}, mainBank, page);
~apc.setDeviceEncoderFunc(4, { | val |
	var feedback = val.linlin(0, 127, 0, 1);
	~darkness.spuds.granulator.delay.setFeedback(feedback);
}, mainBank, page);
~apc.setDeviceEncoderFunc(5, { | val |
	var cutoff = val.linexp(0, 127, 20, 20000);
	~darkness.noise.lowPass.setCutoff(cutoff);
}, mainBank, page);

//// CAREFUL!! ////
~apc.setDeviceEncoderFunc(8, { | val |
	var cutoff = val.linexp(0, 127, 1, 900);
	~prm.masterEQ.setHighPassCutoff(cutoff);
}, mainBank, page); 