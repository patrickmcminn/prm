/*
Wednesday, April 25th 2018
Meaning_Ohm.scd
prm

everything was pregnant with meaning
*/

var page = 'meaning';

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;


~apc.makePage(page);
~apc.addPageLoadFunction(\masterFunc, { ~apc.storageDict[\loadMasterFunc].value; }, page);

~apc.addMixerEncodersBanks(3, page: page);

/////////////////
//// Grid //////
///////////////

//~apc.turnAllGridBlue(page: page);

~apc.setGridMonitorFunc(\hissPlaying, {
  if( ~meaning.hiss.isPlaying == false,
    { ~apc.turnGridRed(0, 4); }, { ~apc.turnGridGreen(0, 4) });
}, page: page);
~apc.setGridFunc(0, 4, {
  if( ~meaning.hiss.isPlaying == false,
    { ~meaning.hiss.playSequences },
    { ~meaning.hiss.stopSequences });
}, page: page);

//////////////////////
//// Mixer: /////
////////////////////

3.do({ | i |
  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~meaning.mainMixer.setVol(i, val.ccdbfs); }, page: page);
  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~meaning.mainMixer.tglMute(i); }, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~meaning.mainMixer.isMuted(i),
      { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~meaning.mainMixer.preOrPost(i) == 'post',
      { ~meaning.mainMixer.setSendPre(i); }, { ~meaning.mainMixer.setSendPost(i) });
  }, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~meaning.mainMixer.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, page: page);


  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~meaning.mainMixer.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~meaning.mainMixer.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~meaning.mainMixer.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~meaning.mainMixer.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);


  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~meaning.mainMixer.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~meaning.mainMixer.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~meaning.mainMixer.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~meaning.mainMixer.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);
});

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


~apc.setMixerEncodersMonitorFunc(\test, {
  ~apc.setMixerEncoderValue(1, ~meaning.mainMixer.sendVol(0, 0).dbfsCC) }, bank: send0, page: page);

///// Delay Return:
// mute:
~apc.setMixerMonitorFunc(\delayMute, {
  if( ~meaning.returns.isMuted(1),
    { ~apc.turnTrackSelectButtonOff(4) }, { ~apc.turnTrackSelectButtonOn(4) });
}, page: page);
~apc.setTrackSelectFunc(4, { ~meaning.returns.tglMute(1) }, page: page);

// volume:
~apc.setFaderFunc(4, { | val | ~meaning.returns.setVol(1, val.ccdbfs); }, page: page);

//////////////////////
////// Modular //////
////////////////////

~apc.setMixerMonitorFunc(\cMixModular, {
  if( ~prm.cmix.isMuted(2),
    { ~apc.turnTrackSelectButtonOff(5) }, { ~apc.turnTrackSelectButtonOn(5) });
  if( ~prm.cmix.preOrPost(2) == 'post',
    { ~apc.turnCrossfaderSelectButtonOrange(5) }, { ~apc.turnCrossfaderSelectButtonYellow(5) });
}, page: page);
~apc.setTrackSelectFunc(5, { ~prm.cmix.tglMute(2) }, page: page);
~apc.setCrossfaderSelectFunc(5, {
  if( ~prm.cmix.preOrPost(2) == 'post',
    { ~prm.cmix.setSendPre(2); }, { ~prm.cmix.setSendPost(2) });
}, page: page);
~apc.setFaderFunc(5, { | val |
  ~prm.cmix.setVol(2, val.ccdbfs);
}, page: page);

~apc.setMixerEncodersMonitorFunc(\cmixsend1, {
  ~apc.setMixerEncoderValue(5, ~prm.cmix.sendVol(2, 0).dbfsCC); }, bank: send0, page: page);
~apc.setMixerEncodersMonitorFunc(\cmixsend2, {
  ~apc.setMixerEncoderValue(5, ~prm.cmix.sendVol(2, 1).dbfsCC); }, bank: send1, page: page);
~apc.setMixerEncodersMonitorFunc(\cmixsend3, {
  ~apc.setMixerEncoderValue(5, ~prm.cmix.sendVol(2, 2).dbfsCC); }, bank: send2, page: page);
~apc.setMixerEncodersMonitorFunc(\cmixsend4, {
  ~apc.setMixerEncoderValue(5, ~prm.cmix.sendVol(2, 3).dbfsCC); }, bank: send3, page: page);


~apc.setMixerEncoderFunc(5, { | val | ~prm.cmix.setSendVol(2, 0, val.ccdbfs); }, bank: send0, page: page);
~apc.setMixerEncoderFunc(5, { | val | ~prm.cmix.setSendVol(2, 1, val.ccdbfs); }, bank: send1, page: page);
~apc.setMixerEncoderFunc(5, { | val | ~prm.cmix.setSendVol(2, 2, val.ccdbfs); }, bank: send2, page: page);
~apc.setMixerEncoderFunc(5, { | val | ~prm.cmix.setSendVol(2, 3, val.ccdbfs); }, bank: send3, page: page);

//////////////////////
////// Moog //////
////////////////////

~apc.setMixerMonitorFunc(\cMixModular, {
  if( ~prm.cmix.isMuted(3),
    { ~apc.turnTrackSelectButtonOff(6) }, { ~apc.turnTrackSelectButtonOn(6) });
  if( ~prm.cmix.preOrPost(3) == 'post',
    { ~apc.turnCrossfaderSelectButtonOrange(6) }, { ~apc.turnCrossfaderSelectButtonYellow(6) });
}, page: page);
~apc.setTrackSelectFunc(6, { ~prm.cmix.tglMute(3) }, page: page);
~apc.setCrossfaderSelectFunc(6, {
  if( ~prm.cmix.preOrPost(3) == 'post',
    { ~prm.cmix.setSendPre(3); }, { ~prm.cmix.setSendPost(3) });
}, page: page);
~apc.setFaderFunc(6, { | val |
  ~prm.cmix.setVol(3, val.ccdbfs);
}, page: page);

~apc.setMixerEncodersMonitorFunc(\cmixmoogsend1, {
  ~apc.setMixerEncoderValue(6, ~prm.cmix.sendVol(3, 0).dbfsCC); }, bank: send0, page: page);
~apc.setMixerEncodersMonitorFunc(\cmixmoogsend2, {
  ~apc.setMixerEncoderValue(6, ~prm.cmix.sendVol(3, 1).dbfsCC); }, bank: send1, page: page);
~apc.setMixerEncodersMonitorFunc(\cmixmoogsend3, {
  ~apc.setMixerEncoderValue(6, ~prm.cmix.sendVol(3, 2).dbfsCC); }, bank: send2, page: page);
~apc.setMixerEncodersMonitorFunc(\cmixmoogsend4, {
  ~apc.setMixerEncoderValue(6, ~prm.cmix.sendVol(3, 3).dbfsCC); }, bank: send3, page: page);


~apc.setMixerEncoderFunc(6, { | val | ~prm.cmix.setSendVol(3, 0, val.ccdbfs); }, bank: send0, page: page);
~apc.setMixerEncoderFunc(6, { | val | ~prm.cmix.setSendVol(3, 1, val.ccdbfs); }, bank: send1, page: page);
~apc.setMixerEncoderFunc(6, { | val | ~prm.cmix.setSendVol(3, 2, val.ccdbfs); }, bank: send2, page: page);
~apc.setMixerEncoderFunc(6, { | val | ~prm.cmix.setSendVol(3, 3, val.ccdbfs); }, bank: send3, page: page);



/////// incidentals:

//// Synth Cutoff:
~apc.setDeviceEncodersMonitorFunc(\synthCutoff, {
  var cutoff = ~meaning.synth.eq.highPassCutoff.explin(150, 13000, 0, 127);
  ~apc.setDeviceEncoderValue(1, cutoff);
}, page: page);
~apc.setDeviceEncoderFunc(1, { | val |
   var cutoff = val.linexp(0, 127, 150, 13000);
  ~meaning.synth.eq.setHighPassCutoff(cutoff);
}, page: page);


//// main low pass:
~apc.setDeviceEncodersMonitorFunc(\lowpass, {
  var cutoff = ~meaning.main.sampler.filterCutoff.explin(150, 20000, 0, 127);
  ~apc.setDeviceEncoderValue(2, cutoff);
}, page: page);
~apc.setDeviceEncoderFunc(2, { | val |
  var cutoff = val.linexp(0, 127, 150, 20000);
  ~meaning.main.sampler.setFilterCutoff(cutoff);
}, page: page);
