/*
Thursday, April 26th 2018
Sorrow Ohm.scd
prm

there is a sorrow in beginning to understand
*/

var page = 'sorrow';

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

~apc.makePage(page);
~apc.addPageLoadFunction(\masterFunc, { ~apc.storageDict[\loadMasterFunc].value; }, page);

~apc.addMixerEncodersBanks(3, page);


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


//////////////////////
//// Grid: //////////
////////////////////

// ~ohm.turnAllGridMagenta(page: page);

//// Record Buffer:
~apc.turnGridWhite(0, 4, page: page);
~apc.setGridFunc(0, 4, {
  ~sorrow.trumpets.recordBuffer;
  ~apc.turnGridGreen(0, 4)
}, page: page);

//////////////////////
//// Mixer: /////
////////////////////

3.do({ | i |
  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~sorrow.trumpets.mixer.setVol(i, val.ccdbfs); }, page: page);
  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~sorrow.trumpets.mixer.tglMute(i); }, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~sorrow.trumpets.mixer.isMuted(i),
      { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~sorrow.trumpets.mixer.preOrPost(i) == 'post',
      { ~sorrow.trumpets.mixer.setSendPre(i); }, { ~meaning.mainMixer.setSendPost(i) });
  }, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~sorrow.trumpets.mixer.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, page: page);

});

~apc.setMixerEncoderFunc(1, { | val |
  ~sorrow.mixer.setSendVol(0, 0, val.ccdbfs); }, bank: send0, page: page);
~apc.setMixerEncoderFunc(1, { | val |
  ~sorrow.mixer.setSendVol(0, 1, val.ccdbfs); }, bank: send1, page: page);
~apc.setMixerEncoderFunc(1, { | val |
  ~sorrow.mixer.setSendVol(0, 2, val.ccdbfs); }, bank: send2, page: page);
~apc.setMixerEncoderFunc(1, { | val |
  ~sorrow.mixer.setSendVol(0, 3, val.ccdbfs); }, bank: send3, page: page);

~apc.setMixerEncodersMonitorFunc(\tptSend0, {
  ~apc.setMixerEncoderValue(1,  ~sorrow.mixer.sendVol(0, 0).dbfsCC); }, bank: send0, page: page);
~apc.setMixerEncodersMonitorFunc(\tptSend1, {
  ~apc.setMixerEncoderValue(1,  ~sorrow.mixer.sendVol(0, 1).dbfsCC); }, bank: send1, page: page);
~apc.setMixerEncodersMonitorFunc(\tptSend2, {
  ~apc.setMixerEncoderValue(1,  ~sorrow.mixer.sendVol(0, 2).dbfsCC); }, bank: send2, page: page);
~apc.setMixerEncodersMonitorFunc(\tptSend3, {
  ~apc.setMixerEncoderValue(1,  ~sorrow.mixer.sendVol(0, 3).dbfsCC); }, bank: send3, page: page);


//////// Moog:
//// Mute:
~apc.setMixerMonitorFunc(\moogMute, {
  if( ~sorrow.mixer.isMuted(1),
    { ~apc.turnTrackSelectButtonOff(4) }, { ~apc.turnTrackSelectButtonOn(4) });
}, page: page);
~apc.setTrackSelectFunc(4, { ~sorrow.mixer.tglMute(1) }, page: page);
//// Volume:
~apc.setFaderFunc(4, { | val |
  ~sorrow.mixer.setVol(1, val.ccdbfs);
}, page: page);

//// Sends:
~apc.setMixerEncoderFunc(4, { | val |
  ~sorrow.mixer.setSendVol(1, 0, val.ccdbfs);
}, bank: send0, page: page);
~apc.setMixerEncoderFunc(4, { | val |
  ~sorrow.mixer.setSendVol(1, 1, val.ccdbfs);
}, bank: send1, page: page);
~apc.setMixerEncoderFunc(4, { | val |
  ~sorrow.mixer.setSendVol(1, 2, val.ccdbfs);
}, bank: send2, page: page);
~apc.setMixerEncoderFunc(4, { | val |
  ~sorrow.mixer.setSendVol(1, 3, val.ccdbfs);
}, bank: send3, page: page);
~apc.setMixerEncodersMonitorFunc(\moogSend0, {
  ~apc.setMixerEncoderValue(4,  ~sorrow.mixer.sendVol(1, 0).dbfsCC); }, bank: send0, page: page);
~apc.setMixerEncodersMonitorFunc(\moogSend1, {
  ~apc.setMixerEncoderValue(4,  ~sorrow.mixer.sendVol(1, 1).dbfsCC); }, bank: send1, page: page);
~apc.setMixerEncodersMonitorFunc(\moogSend2, {
  ~apc.setMixerEncoderValue(4,  ~sorrow.mixer.sendVol(1, 2).dbfsCC); }, bank: send2, page: page);
~apc.setMixerEncodersMonitorFunc(\moogSend3, {
  ~apc.setMixerEncoderValue(4,  ~sorrow.mixer.sendVol(1, 3).dbfsCC); }, bank: send3, page: page);


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
