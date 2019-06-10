/*
Sunday, June 9th 2019
WhereTheBirds APC.scd
prm
*/

var page = 'birds';

var sendMod = 0;

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var mixerBank1 = 0;
var gridBank1 = 0;
var encoderBank1 = 0;

~apc.makePage(page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.addMixerEncodersBanks(3, page);

//////// Metronome:
~apc.addPageLoadFunction(\metronome, {
  ~birds.clock.schedAbs(~birds.clock.beats.ceil, { | beat |
    ~birds.clock.playNextBar({
      ~apc.turnSendsButtonOn(page: page);
      { ~apc.turnSendsButtonOff(page: page) }.defer(0.1);
    });
    ~apc.turnMetronomeButtonOn(page: page);
    { ~apc.turnMetronomeButtonOff(page: page); }.defer(0.1);
    1;
  });
}, page: page);

//////// Modular Clock:
~apc.setPanButtonFunc({
  ~birds.modularClock.togglePlay;
  if( ~birds.modularClock.isPlaying,
    { ~apc.turnPanButtonOn }, { ~apc.turnPanButtonOff });
}, page: page);

/////////////////////
// Bank Change: ////
///////////////////

//// send bank changes:

~apc.setDeviceButtonsMonitorFunc(\sends, {
  if( ~apc.activeMixerEncodersBank == (send0+sendMod),
    { ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1) });
  if( ~apc.activeMixerEncodersBank  == (send1+sendMod),
    { ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2) });
  if( ~apc.activeMixerEncodersBank  == (send2+sendMod),
    { ~apc.turnDeviceButtonOn(3) }, { ~apc.turnDeviceButtonOff(3) });
  if( ~apc.activeMixerEncodersBank == (send3+sendMod),
    { ~apc.turnDeviceButtonOn(4) }, { ~apc.turnDeviceButtonOff(4) });
}, page: page);


~apc.setDeviceButtonFunc(1, { ~apc.setActiveMixerEncodersBank(send0+sendMod); }, page: page);
~apc.setDeviceButtonFunc(2, { ~apc.setActiveMixerEncodersBank(send1+sendMod);  }, page: page);
~apc.setDeviceButtonFunc(3, { ~apc.setActiveMixerEncodersBank(send2+sendMod); }, page: page);
~apc.setDeviceButtonFunc(4, { ~apc.setActiveMixerEncodersBank(send3+sendMod); }, page: page);

////////////////
//// Mixer ////
//////////////


7.do({ | i |

  // volume:
  ~apc.setFaderFunc(i+1, { | val |~birds.mixer.setVol(i, val.ccdbfs); }, bank: mixerBank1, page: page);

  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, {~birds.mixer.tglMute(i); },bank: mixerBank1, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if(~birds.mixer.isMuted(i),
      { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, bank: mixerBank1, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if(~birds.mixer.preOrPost(i) == 'post',
      {~birds.mixer.setSendPre(i); }, {~birds.mixer.setSendPost(i) });
  }, bank: mixerBank1, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if(~birds.mixer.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, bank: mixerBank1, page: page);

  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~birds.mixer.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~birds.mixer.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~birds.mixer.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val |
    ~birds.mixer.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~birds.mixer.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~birds.mixer.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~birds.mixer.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~birds.mixer.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

});


//////////////////////
//// input mute: ////
////////////////////
~apc.setMixerMonitorFunc(\inputMutes, {
  if( ~birds.chords.input.isMuted == false,
    { ~apc.turnRecordEnableButtonOn(2); }, { ~apc.turnRecordEnableButtonOff(2) });
  if( ~birds.distChords.input.isMuted == false,
    { ~apc.turnRecordEnableButtonOn(3); }, { ~apc.turnRecordEnableButtonOff(3) });
  if( ~birds.noise.isMuted == false,
    { ~apc.turnRecordEnableButtonOn(6); }, { ~apc.turnRecordEnableButtonOff(6) });
  if( ~birds.mic.input.isMuted == false,
    { ~apc.turnRecordEnableButtonOn(7); }, { ~apc.turnRecordEnableButtonOff(7) });
}, bank: mixerBank1, page: page);
// chords:
~apc.setRecordEnableButtonFunc(2, {
  ~birds.chords.input.tglMute }, bank: mixerBank1, page: page);
// distChords:
~apc.setRecordEnableButtonFunc(3, {
  ~birds.distChords.input.tglMute }, bank: mixerBank1, page: page);
// noise:
~apc.setRecordEnableButtonFunc(6, {
  ~birds.noise.tglMute }, bank: mixerBank1, page: page);
// trumpet:
~apc.setRecordEnableButtonFunc(7, {
  ~birds.mic.input.tglMute }, bank: mixerBank1, page: page);


///////////////
//// GRID ////
/////////////

//// bed:
~apc.setGridMonitorFunc(\bed, {
  if( ~birds.bed.fSharpIsPlaying,
    { ~apc.turnGridGreen(0, 4) }, { ~apc.turnGridBlue(0, 4, 1) });
  if( ~birds.bed.gSharpIsPlaying,
    { ~apc.turnGridGreen(1, 4) }, { ~apc.turnGridBlue(1, 4, 1) });
  if( ~birds.bed.aIsPlaying,
    { ~apc.turnGridGreen(2, 4) }, { ~apc.turnGridBlue(2, 4, 1) });
  if( ~birds.bed.bIsPlaying,
    { ~apc.turnGridGreen(3, 4) }, { ~apc.turnGridBlue(3, 4, 1) });
  if( ~birds.bed.cSharpIsPlaying,
    { ~apc.turnGridGreen(4, 4) }, { ~apc.turnGridBlue(4, 4, 1) });
  if( ~birds.bed.eIsPlaying,
    { ~apc.turnGridGreen(5, 4) }, { ~apc.turnGridBlue(5, 4, 1) });
}, bank: gridBank1, page: page);

// F#
~apc.setGridFunc(0, 4, { ~birds.bed.toggleFSharp },
  bank: gridBank1, page: page);
// G#
~apc.setGridFunc(1, 4, { ~birds.bed.toggleGSharp },
  bank: gridBank1, page: page);
// A
~apc.setGridFunc(2, 4, { ~birds.bed.toggleA },
  bank: gridBank1, page: page);
// B
~apc.setGridFunc(3, 4, { ~birds.bed.toggleB },
  bank: gridBank1, page: page);
// C#
~apc.setGridFunc(4, 4, { ~birds.bed.toggleCSharp },
  bank: gridBank1, page: page);
// E
~apc.setGridFunc(5, 4, { ~birds.bed.toggleE },
  bank: gridBank1, page: page);

//// chords:
~apc.setGridMonitorFunc(\chords, {
  if( ~birds.chords.droneIsPlaying,
    { ~apc.turnGridGreen(0, 3) }, { ~apc.turnGridPurple(0, 3) });
  if( ~birds.chords.section1IsPlaying,
    { ~apc.turnGridGreen(1, 3) }, { ~apc.turnGridMagenta(1, 3) });
  if( ~birds.chords.turnaroundIsPlaying,
    { ~apc.turnGridGreen(2, 3) }, { ~apc.turnGridMagenta(2, 3) });
  if( ~birds.chords.endDroneIsPlaying,
    { ~apc.turnGridGreen(3, 3) }, { ~apc.turnGridMagenta(3, 3) });
}, bank: gridBank1, page: page);

// drone:
~apc.setGridFunc(0, 3, {
  // ~birds.chords.stopDrone;
  ~birds.chords.stopSection1;
  ~birds.chords.stopTurnaround;
  ~birds.chords.stopEndDrone;
  ~birds.clock.playNextBar({
    ~birds.chords.playDrone(~birds.clock);
  });
}, bank: gridBank1, page: page);

// section 1:
~apc.setGridFunc(1, 3, {
  ~birds.chords.stopDrone;
  //~birds.chords.stopSection1;
  ~birds.chords.stopTurnaround;
  ~birds.chords.stopEndDrone;
  ~birds.clock.playNextBar({
    ~birds.chords.playSection1(~birds.clock);
  });
}, bank: gridBank1, page: page);

// turnaround:
~apc.setGridFunc(2, 3, {
  ~birds.chords.stopDrone;
  ~birds.chords.stopSection1;
  //~birds.chords.stopTurnaround;
  ~birds.chords.stopEndDrone;
  ~birds.clock.playNextBar({
    ~birds.chords.playTurnaround(~birds.clock);
  });
}, bank: gridBank1, page: page);

// endDrone:
~apc.setGridFunc(3, 3, {
  ~birds.chords.stopDrone;
  ~birds.chords.stopSection1;
  ~birds.chords.stopTurnaround;
  //~birds.chords.stopEndDrone;
  ~birds.clock.playNextBar({
    ~birds.chords.playEndDrone(~birds.clock);
  });
}, bank: gridBank1, page: page);
~apc.turnSceneLaunchButtonRed(1, bank: gridBank1, page: page);
//~apc.turnGridRed(4, 3, bank: gridBank1, page: page);
~apc.setSceneLaunchFunc(1, {
  ~birds.clock.playNextBar({
    ~birds.chords.stopDrone;
    ~birds.chords.stopSection1;
    ~birds.chords.stopTurnaround;
    ~birds.chords.stopEndDrone;
    //~birds.chords.playEndDrone(~birds.clock);
  })
}, bank: gridBank1, page: page);



//// bass:
~apc.setGridMonitorFunc(\bass, {
  if( ~birds.bass.section2IsPlaying,
    { ~apc.turnGridGreen(0, 2) }, { ~apc.turnGridLightBlue(0, 2) });
  if( ~birds.bass.section3IsPlaying,
    { ~apc.turnGridGreen(1, 2) }, { ~apc.turnGridLightBlue(1, 2) });
  if( ~birds.bass.endDroneIsPlaying,
    { ~apc.turnGridGreen(2, 2) }, { ~apc.turnGridLightBlue(2, 2) });
}, bank: gridBank1, page: page);

// section2:
~apc.setGridFunc(0, 2, {
  ~birds.clock.playNextBar({
    //~birds.bass.stopSection2;
    ~birds.bass.stopSection3;
    ~birds.bass.stopEndDrone;
    ~birds.bass.playSection2(~birds.clock);
  });
}, bank: gridBank1, page: page);

// section 3:
~apc.setGridFunc(1, 2, {
  ~birds.clock.playNextBar({
    ~birds.bass.stopSection2;
    //~birds.bass.stopSection3;
    ~birds.bass.stopEndDrone;
    ~birds.bass.playSection3(~birds.clock);
  });
}, bank: gridBank1, page: page);

// end drone:
~apc.setGridFunc(2, 2, {
  ~birds.clock.playNextBar({
    ~birds.bass.stopSection2;
    ~birds.bass.stopSection3;
    //~birds.bass.stopEndDrone;
    ~birds.bass.playEndDrone(~birds.clock);
  });
}, bank: gridBank1, page: page);
// stop:
~apc.turnSceneLaunchButtonRed(2, bank: gridBank1, page: page);
//~apc.turnGridRed(3, 2, bank: gridBank1, page: page);
~apc.setSceneLaunchFunc(2, {
  ~birds.clock.playNextBar({
    ~birds.bass.stopSection2;
    ~birds.bass.stopSection3;
    ~birds.bass.stopEndDrone;
    //~birds.bass.playEndDrone(~birds.clock);
  });
}, bank: gridBank1, page: page);


//// noise synth:
~apc.setGridMonitorFunc(\noiseSynth, {
  if( ~birds.noiseSynth.phrase1IsPlaying,
    { ~apc.turnGridGreen(0, 1) }, { ~apc.turnGridWhite(0, 1, 1) });
  if( ~birds.noiseSynth.phrase2IsPlaying,
    { ~apc.turnGridGreen(1, 1) }, { ~apc.turnGridWhite(1, 1, 1) });
  if( ~birds.noiseSynth.phrase3IsPlaying,
    { ~apc.turnGridGreen(2, 1) }, { ~apc.turnGridWhite(2, 1, 1) });
  if( ~birds.noiseSynth.phrase4IsPlaying,
    { ~apc.turnGridGreen(3, 1) }, { ~apc.turnGridWhite(3, 1, 1) });
}, bank: gridBank1, page: page);

// phrase 1:
~apc.setGridFunc(0, 1, {
  ~birds.clock.playNextBar({
    //~birds.noiseSynth.stopPhrase1;
    ~birds.noiseSynth.stopPhrase2;
    ~birds.noiseSynth.stopPhrase3;
    ~birds.noiseSynth.stopPhrase4;
    ~birds.noiseSynth.playPhrase1(~birds.clock);
  });
}, bank: gridBank1, page: page);

// phrase 2:
~apc.setGridFunc(1, 1, {
  ~birds.clock.playNextBar({
    ~birds.noiseSynth.stopPhrase1;
    //~birds.noiseSynth.stopPhrase2;
    ~birds.noiseSynth.stopPhrase3;
    ~birds.noiseSynth.stopPhrase4;
    ~birds.noiseSynth.playPhrase2(~birds.clock);
  });
}, bank: gridBank1, page: page);

// phrase 3:
~apc.setGridFunc(2, 1, {
  ~birds.clock.playNextBar({
    ~birds.noiseSynth.stopPhrase1;
    ~birds.noiseSynth.stopPhrase2;
    //~birds.noiseSynth.stopPhrase3;
    ~birds.noiseSynth.stopPhrase4;
    ~birds.noiseSynth.playPhrase3(~birds.clock);
  });
}, bank: gridBank1, page: page);

// phrase 4:
~apc.setGridFunc(3, 1, {
  ~birds.clock.playNextBar({
    ~birds.noiseSynth.stopPhrase1;
    ~birds.noiseSynth.stopPhrase2;
    ~birds.noiseSynth.stopPhrase3;
    //~birds.noiseSynth.stopPhrase4;
    ~birds.noiseSynth.playPhrase4(~birds.clock);
  });
}, bank: gridBank1, page: page);
// stop:
~apc.turnSceneLaunchButtonRed(3, bank: gridBank1, page: page);
~apc.setSceneLaunchFunc(3, {
  ~birds.clock.playNextBar({
    ~birds.noiseSynth.stopPhrase1;
    ~birds.noiseSynth.stopPhrase2;
    ~birds.noiseSynth.stopPhrase3;
    ~birds.noiseSynth.stopPhrase4;
    //~birds.noiseSynth.playPhrase4(~birds.clock);
  });
}, bank: gridBank1, page: page);

///////////////////////////
//// Device Encoders: ////
/////////////////////////

~apc.setDeviceEncodersMonitorFunc(\bank1, {
  var crossfade = ~birds.noiseSynth.granulator.crossfade.linlin(-1.0, 1.0, 0, 127);
  var cutoff = ~birds.distChords.distortion.postEQ.lowPassCutoff.explin(900, 5000, 0, 127);
  ~apc.setDeviceEncoderValue(1, cutoff);
  ~apc.setDeviceEncoderValue(2, crossfade);

}, bank: encoderBank1, page: page);

// noise chords cutoff:
~apc.setDeviceEncoderFunc(1, { | val |
  var cutoff = val.linexp(0, 127, 900, 5000);
  ~birds.distChords.distortion.postEQ.setLowPassCutoff(cutoff);
}, bank: encoderBank1, page: page);

// noise synth granulator:
~apc.setDeviceEncoderFunc(2, { | val |
  var crossfade = val.linlin(0, 127, -1.0, 1.0);
  ~birds.noiseSynth.granulator.setGranulatorCrossfade(crossfade);
}, bank: encoderBank1, page: page);


