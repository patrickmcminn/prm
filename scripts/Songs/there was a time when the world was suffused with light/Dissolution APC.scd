/*
Sunday, May 6th 2018
Dissolution Ohm.scd
prm
*/

var page = 'dissolution';

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

~apc.makePage(page);

~apc.addPageLoadFunction(\masterFunc, { ~apc.storageDict[\loadMasterFunc].value; }, page);

~apc.addMixerEncodersBanks(3, page: page);

~apc.turnGridPink(0, 4, page: page);
~apc.setGridFunc(0, 4, {
  ~dissolution.playSample;
  ~apc.turnGridGreen(0, 4);
}, page: page);

~apc.setFaderFunc(1, { | val |
  ~dissolution.mixer.setVol(val.ccdbfs);
}, page: page);


//// sends:
~apc.setMixerEncoderFunc(1, { | val |
  ~dissolution.mixer.setSendVol(0, 0, val.ccdbfs); }, bank: send0, page: page);
~apc.setMixerEncoderFunc(1, { | val |
  ~dissolution.mixer.setSendVol(0, 1, val.ccdbfs); }, bank: send1, page: page);
~apc.setMixerEncoderFunc(1, { | val |
  ~dissolution.mixer.setSendVol(0, 2, val.ccdbfs); }, bank: send2, page: page);
~apc.setMixerEncoderFunc(1, { | val |
  ~dissolution.mixer.setSendVol(0, 3, val.ccdbfs); }, bank: send3, page: page);


~apc.setMixerEncodersMonitorFunc(("mixer"++0++"send").asSymbol, {
  ~apc.setMixerEncoderValue(1,  ~dissolution.mixer.sendVol(0, 0).dbfsCC); }, bank: send0, page: page);
~apc.setMixerEncodersMonitorFunc(("mixer"++0++"send").asSymbol, {
  ~apc.setMixerEncoderValue(1,  ~dissolution.mixer.sendVol(0, 1).dbfsCC); }, bank: send1, page: page);
~apc.setMixerEncodersMonitorFunc(("mixer"++0++"send").asSymbol, {
  ~apc.setMixerEncoderValue(1,  ~dissolution.mixer.sendVol(0, 2).dbfsCC); }, bank: send2, page: page);
~apc.setMixerEncodersMonitorFunc(("mixer"++0++"send").asSymbol, {
  ~apc.setMixerEncoderValue(1,  ~dissolution.mixer.sendVol(0, 3).dbfsCC); }, bank: send3, page: page);

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



/*
var page = 'dissolution';

~ohm.makePage(page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);

~ohm.turnAllGridBlue(page: page);

~ohm.turnGridWhite(0, 0, page: page);
~ohm.setGridFunc(0, 0, {
~dissolution.playSample;
~ohm.turnGridGreen(0, 0);
}, page: page);

~ohm.setLeftSliderFunc(0, { | val |
~dissolution.mixer.setVol(val.ccdbfs);
}, page: page);
*/