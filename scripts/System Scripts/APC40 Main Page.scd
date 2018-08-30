/*
Wednesday, August 29th
APC40 Main Page.scd
prm
*/

var page = 'main';

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;


~apc.storageDict[\loadMasterFunc].value;
~apc.addPageLoadFunction(\master, { apc.storageDict[\loadMasterFunc].value; }, page);

~apc.addMixerEncodersBanks(3);

/////// Grid Functions:

///////////////////////////////////
////// Convenience Mixer: ////////
/////////////////////////////////

4.do({ | i |
  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~convMix.setVol(i, val.ccdbfs); }, page: page);
  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~convMix.tglMute(i); }, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~convMix.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~convMix.preOrPost(i) == 'post',
      { ~convMix.setSendPre(i); }, { ~convMix.setSendPost(i) });
  }, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~convMix.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOn });
  });

  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val | ~convMix.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~convMix.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~convMix.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~convMix.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

});


};