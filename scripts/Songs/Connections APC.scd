/*
Monday, May 27th 2019
Connections APC.scd
prm
*/

var page = 'connections';


var sendMod = 0;

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var mixerBank1 = 0;

~apc.makePage(page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.addMixerEncodersBanks(3, page);


//////// Metronome:
~apc.addPageLoadFunction(\metronome, {
  ~connections.clock.schedAbs(~connections.clock.beats.ceil, { | beat |
    ~connections.clock.playNextBar({
      ~apc.turnSendsButtonOn(page: page);
      { ~apc.turnSendsButtonOff(page: page) }.defer(0.1);
    });
    ~apc.turnMetronomeButtonOn(page: page);
    { ~apc.turnMetronomeButtonOff(page: page); }.defer(0.1);
    1;
  });
}, page: page);

//////// Modular Clock:
~apc.setMetronomeButtonFunc({
  ~connections.modularClock.togglePlay;
  if( ~connections.modularClock.isPlaying,
    { ~apc.turnMetronomeButtonOn }, { ~apc.turnMetronomeButtonOff });
}, page: page);


////////////////
//// Mixer ////
//////////////


7.do({ | i |

  // volume:
  ~apc.setFaderFunc(i+1, { | val | ~connections.mixer.setVol(i, val.ccdbfs); }, bank: mixerBank1, page: page);

  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~connections.mixer.tglMute(i); },bank: mixerBank1, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~connections.mixer.isMuted(i),
      { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, bank: mixerBank1, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~connections.mixer.preOrPost(i) == 'post',
      { ~connections.mixer.setSendPre(i); }, { ~connections.mixer.setSendPost(i) });
  }, bank: mixerBank1, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~connections.mixer.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, bank: mixerBank1, page: page);

  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~connections.mixer.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~connections.mixer.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~connections.mixer.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~connections.mixer.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~connections.mixer.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~connections.mixer.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~connections.mixer.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1,  ~connections.mixer.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

});

//// microphone:
~apc.setFaderFunc(8, { | val | ~prm.microphone.setVol(val.ccdbfs); }, bank: mixerBank1, page: page);
~apc.setTrackSelectFunction(8, { ~prm.microphone.tglMute; }, bank: mixerBank1, page: page);
~apc.setMixerMonitorFunc("micMute", {
  if( ~prm.microphone.isMuted,
    { ~apc.turnTrackSelectButtonOff(8); }, { ~apc.turnTrackSelectButtonOn(8) });
}, bank: mixerBank1, page: page);
//// sends:
~apc.setMixerEncoderFunc(8, { | val |
  ~prm.microphone.setSendVol(0, val.ccdbfs); }, bank: send0, page: page);
~apc.setMixerEncoderFunc(8, { | val |
  ~prm.microphone.setSendVol(1, val.ccdbfs); }, bank: send1, page: page);
~apc.setMixerEncoderFunc(8, { | val |
  ~prm.microphone.setSendVol(2, val.ccdbfs); }, bank: send2, page: page);
~apc.setMixerEncoderFunc(8, { | val |
  ~prm.microphone.setSendVol(3, val.ccdbfs); }, bank: send3, page: page);

~apc.setMixerEncodersMonitorFunc("micSend0", {
  ~apc.setMixerEncoderValue(8,  ~prm.microphone.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
~apc.setMixerEncodersMonitorFunc("micSend1", {
  ~apc.setMixerEncoderValue(8,  ~prm.microphone.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
~apc.setMixerEncodersMonitorFunc("micSend2", {
  ~apc.setMixerEncoderValue(8,  ~prm.microphone.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
~apc.setMixerEncodersMonitorFunc("micSend3", {
  ~apc.setMixerEncoderValue(8,  ~prm.microphone.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);
//// input mute:
~apc.setRecordEnableButtonFunc(8, { ~prm.micInput.tglMute; }, bank: mixerBank1, page: page);
~apc.setMixerMonitorFunc('micInput', { if(~prm.micInput.isMuted,
  { ~apc.turnRecordEnableButtonOff(8) }, { ~apc.turnRecordEnableButtonOn(8) });
}, bank: mixerBank1, page: page);

///////////////////////
//// input mutes: ////
/////////////////////

//// air sputters:
~apc.setRecordEnableButtonFunc(1



//// droner:

//// trumpet gran:


