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
~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);

~apc.addMixerEncodersBanks(3);

/////// Grid Functions:
'Built on a Flawed Foundation';

~ohm.turnGridCyan(0, 4, page: page);
~ohm.setGridFunc(0, 4, {
  if(foundationIsLoaded == false, {
    r {
      ~foundationDelay = SimpleDelay.newStereo(~prm.audioIn, 2.5, 0.21, 2.5,
        relGroup: ~prm.procGroup, addAction: \addToHead);
      while({ try { ~foundationDelay.isLoaded } != true }, { 0.001.wait; });
      s.sync;
      ~foundationDelay.setMix(1);
      ~foundation = Foundation.new(~prm.audioIn,
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~foundationDelay.inBus,
        ~prm.procGroup, \addToHead, ~moogDevice, ~moogPort);
      while({ try { ~foundation.isLoaded } != true }, { 0.001.wait; });
      ~ohm.setGridFunc(1, 4, { ~ohm.setPage('foundation'); }, page: page);
      ~ohm.turnGridGreen(1, 4);
      foundationIsLoaded = true;
    }.play;
    },
    {
      ~foundation.free;
      ~foundationDelay.free;
      ~ohm.setGridFunc(1, 4, { });
      ~ohm.turnGridOff(1, 4);
      foundationIsLoaded = false;
  });
}, page: page);


//////// There Was a Time when the World was Suffused with Light ////////

~apc.turnSceneLaunchButtonPurple(0, 2, page: page);
~apc.setSceneLaunchFunc(0, { ~apc.setPage('light album'); }, page: page);


///////////////////////////////////
////// Convenience Mixer: ////////
/////////////////////////////////
/*
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
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  });

  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val | ~convMix.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~convMix.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~convMix.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~convMix.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~convMix.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~convMix.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~convMix.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~convMix.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

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
  });

  ~apc.setDeviceButtonFunc(1, { ~apc.setActiveMixerEncodersBank(send0); });
  ~apc.setDeviceButtonFunc(2, { ~apc.setActiveMixerEncodersBank(send1);  });
  ~apc.setDeviceButtonFunc(3, { ~apc.setActiveMixerEncodersBank(send2); });
  ~apc.setDeviceButtonFunc(4, { ~apc.setActiveMixerEncodersBank(send3); });

});
*/
