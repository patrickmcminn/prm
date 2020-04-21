/*
Sunday, April 19th 2020
Docile APC.scd
prm

to compress the world to a docile state
*/

var page = 'docile';

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
~apc.setUserButtonFunc({ ~prm.reverb.loadPreset('docile'); ~apc.turnUserButtonOn; }, page: page);
~apc.setUserButtonFunc({ ~apc.turnUserButtonOff }, 'noteOff', page: page);

// load midi funcs:
~apc.setSceneLaunchMonitorFunc(\midi, { if(~docile.midiFuncsLoaded == true,
	{ ~apc.turnSceneLaunchButtonGreen(0); }, { ~apc.turnSceneLaunchButtonYellow(0) });
}, page: page);
~apc.setSceneLaunchFunc(0, { if(~docile.midiFuncsLoaded == false,
	{ ~docile.makeMIDIFuncs(~pyr); }, { ~docile.freeMIDIFuncs });
}, page: page);

~apc.turnAllGridMagenta(1, mainBank, page);

/////////////////
//// Mixer: ////
///////////////

4.do({ | i |
  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~docile.mixer.setVol(i, val.ccdbfs); }, page: page);
  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~docile.mixer.tglMute(i); }, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~docile.mixer.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~docile.mixer.preOrPost(i) == 'post',
      { ~docile.mixer.setSendPre(i); }, { ~docile.mixer.setSendPost(i) });
  }, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~docile.mixer.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, page: page);

  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val | ~docile.mixer.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~docile.mixer.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~docile.mixer.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~docile.mixer.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~docile.mixer.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~docile.mixer.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~docile.mixer.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~docile.mixer.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

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

///////////////////////////
//// Device Encoders: ////
/////////////////////////

~apc.setDeviceEncodersMonitorFunc(\mainBank, {
	var delayMix, feedback;
	delayMix = ~docile.noisies.delayLeft.mix.linlin(0, 1, 0, 127);
	feedback = ~docile.noisies.delayLeft.feedback.linlin(0, 1, 0, 127);
	~apc.setDeviceEncoderValue(1, delayMix);
	~apc.setDeviceEncoderValue(2, feedback);
}, mainBank, page);
~apc.setDeviceEncoderFunc(1, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~docile.noisies.delayLeft.setMix(mix);
	~docile.noisies.delayRight.setMix(mix);
}, mainBank, page);
~apc.setDeviceEncoderFunc(2, { | val |
	var feedback = val.linlin(0, 127, 0, 1);
	~docile.noisies.delayLeft.setFeedback(feedback);
	~docile.noisies.delayRight.setFeedback(feedback);
}, mainBank, page); 