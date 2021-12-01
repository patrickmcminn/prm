/*
Wednesday, November 10th 2021
Light APC.scd
prm
*/

var page = 'light';

var main = 0;

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;


~apc.makePage(page);
~apc.addPageLoadFunction(\masterFunc, { ~apc.storageDict[\loadMasterFunc].value; }, page);

~apc.addMixerEncodersBanks(3, page: page);

~apc.setUserButtonFunc({
  ~prm.reverb.loadPreset('light');
  ~apc.turnUserButtonOn;
}, bank: main, page: page);

~apc.setSceneLaunchMonitorFunc(\midi, {
  var midi = ~light.midiEnabled;
  if( midi == true, { ~apc.turnSceneLaunchButtonGreen(0) }, { ~apc.turnSceneLaunchButtonWhite(0) });
}, bank: main, page: page);
~apc.setSceneLaunchFunc(0, { ~light.toggleMIDIFuncs }, bank: main, page: page);

//////////////////////
//// Mixer: /////////
////////////////////

~apc.mapMixer("~light.mixer", 8, 0, 0, send0, send1, send2, send3, main, page);
~apc.mapSendBanks(\main, send0, send1, send2, send3, main, page);

// input mutes:

~apc.setMixerMonitorFunc(\inputs, {
  var arps = ~light.arps.synth2.isMuted;
  var bass = ~light.bass.isMuted;
  var trumpets = ~light.noiseTrumpetInput.isMuted;
  var noise = ~light.noise.isMuted;
  var trumpet = ~light.trumpet.isMuted;
  if( arps, { ~apc.turnRecordEnableButtonOff(2) }, { ~apc.turnRecordEnableButtonOn(2) });
  if( bass, { ~apc.turnRecordEnableButtonOff(3) }, { ~apc.turnRecordEnableButtonOn(3) });
  if( trumpets, { ~apc.turnRecordEnableButtonOff(5) }, { ~apc.turnRecordEnableButtonOn(5) });
  if( noise, { ~apc.turnRecordEnableButtonOff(6) }, { ~apc.turnRecordEnableButtonOn(6) });
  if( trumpet, { ~apc.turnRecordEnableButtonOff(7) }, { ~apc.turnRecordEnableButtonOn(7) });
}, main, page);
~apc.setRecordEnableButtonFunc(2, { ~light.arps.synth2.tglMute }, bank: main, page: page);
~apc.setRecordEnableButtonFunc(3, { ~light.bass.tglMute }, bank: main, page: page);
~apc.setRecordEnableButtonFunc(5, { ~light.noiseTrumpetInput.tglMute }, bank: main, page: page);
~apc.setRecordEnableButtonFunc(6, { ~light.noise.tglMute }, bank: main, page: page);
~apc.setRecordEnableButtonFunc(7, { ~light.trumpet.tglMute }, bank: main, page: page);

/////////////////
//// Knobs: ////
///////////////

~apc.setDeviceEncodersMonitorFunc(\mainBank, {
  var mainCutoff = ~light.main.sampler.filterCutoff.explin(150, 20000, 0, 127);
  var arpsCutoff = ~light.arps.lowPass.filterCutoff.explin(150, 20000, 0, 127);
  var trumpetCutoff = ~light.noiseTrumpet.eq.lowPassCutoff.explin(450, 20000, 0, 127);
  var noiseCutoff = ~light.noiseFilter.filterCutoff.explin(150, 20000, 0, 127);

  ~apc.setDeviceEncoderValue(1, mainCutoff);
  ~apc.setDeviceEncoderValue(2, arpsCutoff);
  ~apc.setDeviceEncoderValue(3, trumpetCutoff);
  ~apc.setDeviceEncoderValue(4, noiseCutoff);
}, main, page);

~apc.setDeviceEncoderFunc(1, { | val |
  var cutoff = val.linexp(0, 127, 150, 20000);
  ~light.main.sampler.setFilterCutoff(cutoff);
}, main, page);
~apc.setDeviceEncoderFunc(2, { | val |
  var cutoff = val.linexp(0, 127, 150, 20000);
  ~light.arps.lowPass.setCutoff(cutoff);
}, main, page);
~apc.setDeviceEncoderFunc(3, { | val |
  var cutoff = val.linexp(0, 127, 450, 20000);
  ~light.noiseTrumpet.eq.setLowPassCutoff(cutoff);
}, main, page);
~apc.setDeviceEncoderFunc(4, { | val |
  var cutoff = val.linexp(0, 127, 150, 20000);
  ~light.noiseFilter.setCutoff(cutoff);
}, main, page);

/////////////////
//// Grid: /////
///////////////

~apc.turnAllGridYellow(1, main, page);

~apc.setGridMonitorFunc(\chorale, {
  if( ~light.chorale.choraleIsPlaying, { ~apc.turnGridGreen(7, 4) }, { ~apc.turnGridWhite(7, 4) });
}, main, page);
~apc.setGridFunc(7, 4, { ~light.chorale.tglChorale; }, bank: main, page: page);

~apc.turnGridPink(7, 0, bank: main, page: page);
~apc.setGridFunc(7, 0, { ~light.playEndSong }, bank: main, page: page);

  