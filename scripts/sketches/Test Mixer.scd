/*
Test Mixer
*/


var page = 'mix';

var reverbBank = 0;
var granulatorBank = 1;
var modularBank = 2;
var delayBank = 3;


{
  ~prm = AudioSystem.new(2);
  while({ try { ~prm.isLoaded } != true }, { 0.001.wait; });


  //~apc = APC40Mk2.new("mio10", "HST 2");
  ~apc = APC40Mk2.new;

  ~mix = IM_Mixer.new(8, ~prm.submixA,
    ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
    relGroup: ~prm.procGroup, addAction: \addToHead);
  while({ try { ~mix.isLoaded } != true }, { 0.001.wait; });
  ~prm.delay.setDelayTime((60/160)*2);

  ~in1 = IM_HardwareIn.new(2, ~mix.chanMono(0), ~prm.procGroup, \addToHead);
  while({ try { ~in1.isLoaded } != true }, { 0.001.wait; });
  ~in2 = IM_HardwareIn.new(3, ~mix.chanMono(1), ~prm.procGroup, \addToHead);
  while({ try { ~in2.isLoaded } != true }, { 0.001.wait; });
  ~tpt = IM_HardwareIn.new(1, ~mix.chanMono(2), ~prm.procGroup, \addToHead);

  s.sync;

  ~apc.makePage(page);

  ~apc.addMixerEncodersBanks(3, page: page);

  8.do{ | i |
    ~apc.setFaderFunc(i+1, { | val | ~mix.setVol(i, val.ccdbfs); }, page: page);
    ~apc.setTrackSelectFunc(i+1, { ~mix.tglMute(i); }, page: page);
    ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
      if( ~mix.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
    }, page: page);

    //// sends:
    ~apc.setMixerEncoderFunc(i+1, { | val | ~mix.setSendVol(i, 0, val.ccdbfs); }, bank: reverbBank, page: page);
    ~apc.setMixerEncoderFunc(i+1, { | val | ~mix.setSendVol(i, 1, val.ccdbfs); }, bank: granulatorBank, page: page);
    ~apc.setMixerEncoderFunc(i+1, { | val | ~mix.setSendVol(i, 2, val.ccdbfs); }, bank: modularBank, page: page);
    ~apc.setMixerEncoderFunc(i+1, { | val | ~mix.setSendVol(i, 3, val.ccdbfs); }, bank: delayBank, page: page);

    ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
      ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 0).dbfsCC); }, bank: reverbBank, page: page);
    ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
      ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 1).dbfsCC); }, bank: granulatorBank, page: page);
    ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
      ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 2).dbfsCC); }, bank: modularBank, page: page);
    ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
      ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 3).dbfsCC); }, bank: delayBank, page: page);


  };

  //// send bank changes:
  ~apc.setDeviceButtonsMonitorFunc(\sends, {
    if( ~apc.activeMixerEncodersBank == reverbBank,
      { ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1) });
    if( ~apc.activeMixerEncodersBank  == granulatorBank,
      { ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2) });
    if( ~apc.activeMixerEncodersBank  == modularBank,
      { ~apc.turnDeviceButtonOn(3) }, { ~apc.turnDeviceButtonOff(3) });
    if( ~apc.activeMixerEncodersBank == delayBank,
      { ~apc.turnDeviceButtonOn(4) }, { ~apc.turnDeviceButtonOff(4) });
  }, page: page);

  ~apc.setDeviceButtonFunc(1, { ~apc.setActiveMixerEncodersBank(reverbBank, page); }, page: page);
  ~apc.setDeviceButtonFunc(2, { ~apc.setActiveMixerEncodersBank(granulatorBank, page);  }, page: page);
  ~apc.setDeviceButtonFunc(3, { ~apc.setActiveMixerEncodersBank(modularBank, page); }, page: page);
  ~apc.setDeviceButtonFunc(4, { ~apc.setActiveMixerEncodersBank(delayBank, page); }, page: page);


  s.sync;

  ~apc.setPage('mix');


}.fork;