/*
Sunday, March 19th 2017
Habit Ohm.scd
prm
*/

var page = 'habit';

var mainMixBank = 0;
var trumpetLooperBank = 1;
var leftKnobMiscBank = 1;

var sequenceResetFunc;

var activeSlidersBank = mainMixBank;
var activeLeftKnobsBank = mainMixBank;

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var pitchArray = [1, 3, 4, 6, 8, 9, 11];

~apc.makePage(page);

~apc.addMixerEncodersBanks(3, page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);


//// Mixer:

4.do({ | i |
  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~habit.songMixer.setVol(i, val.ccdbfs); }, page: page);
  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~habit.songMixer.tglMute(i); }, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~habit.songMixer.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~habit.songMixer.preOrPost(i) == 'post',
      { ~habit.songMixer.setSendPre(i); }, { ~habit.songMixer.setSendPost(i) });
  }, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~habit.songMixer.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  });

  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val | ~habit.songMixer.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~habit.songMixer.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~habit.songMixer.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~habit.songMixer.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~habit.songMixer.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~habit.songMixer.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~habit.songMixer.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~habit.songMixer.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

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

4.do({ | i | ~apc.turnClipStopButtonOn(i+1, page: page);});
~apc.setMixerMonitorFunc(\inputEnable, {
  if( ~habit.trumpetInput.isMuted,
    { ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
  if( ~habit.modularInput.isMuted,
    { ~apc.turnRecordEnableButtonOff(3) }, { ~apc.turnRecordEnableButtonOn(3) });
  if( ~habit.trumpetLoopersInput.isMuted,
    { ~apc.turnRecordEnableButtonOff(4) }, { ~apc.turnRecordEnableButtonOn(4) });
}, page: page);

////// trumpet:
~apc.setClipStopFunc(1, { ~habit.trumpet.free; ~habit.trumpetInput.free; ~apc.turnClipStopButtonOff(1) },
  page: page);
~apc.setRecordEnableButtonFunc(1, { ~habit.trumpetInput.tglMute }, page: page);
////// moog:
~apc.setClipStopFunc(2, { ~habit.moog.free; ~apc.turnClipStopButtonOff(2) }, page: page);
////// modular:
~apc.setClipStopFunc(3, { ~habit.modular.free; ~habit.modularInput.free; ~apc.turnClipStopButtonOff(3) },
  page: page);
~apc.setRecordEnableButtonFunc(3, { ~habit.modularInput.tglMute }, page: page);
///// trumpet loopers:
~apc.setClipStopFunc(4, { ~habit.trumpetLoopers.free; ~habit.trumpetLoopersInput.free; ~apc.turnStopButtonOff(4) },
  page: page);
~apc.setRecordEnableButtonFunc(4, { ~habit.trumpetLoopersInput.tglMute }, page: page);



////// Trumpet Looper Submixer:
4.do({ | i |
  ~apc.setFaderFunc(i+4, { | val | ~habit.trumpetLoopers.mixer.setVol(i, val.ccdbfs); }, page: page);
   // toggle mute:
  ~apc.setTrackSelectFunc(i+4, { ~habit.trumpetLoopers.mixer.tglMute(i); }, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~habit.trumpetLoopers.mixer.isMuted(i),
      { ~apc.turnTrackSelectButtonOff(i+4) }, { ~apc.turnTrackSelectButtonOn(i+4); });
  }, page: page);

});

/////////// Device Encoders:

//// Tempo:
~apc.setDeviceEncodersMonitorFunc(\tempo, {
  var tempo = (~habit.clock.tempo/60).explin(20, 1200, 0, 127);
  ~apc.setDeviceEncoderValue(1, tempo);
}, page: page);
~apc.setDeviceEncoderFunc(1, { |val |
  var tempo = val.linexp(0, 127, 20, 1200);
  ~habit.setClockTempo(tempo);
}, page: page);


//// Delay:

~apc.setDeviceEncodersMonitorFunc(\delay, {
  var delayTime = ~habit.delay.delayTime.linlin(0.001, 5, 0, 127);
  var feedback = ~habit.delay.feedback.linlin(0, 1, 0, 127);

  ~apc.setDeviceEncoderValue(2, ~habit.delay.mixer.vol.dbfsCC);
  ~apc.setDeviceEncoderValue(3, delayTime);
  ~apc.setDeviceEncoderValue(4, feedback);
}, page: page);
// Volume:
~apc.setDeviceEncoderFunc(2, { | val |
  ~habit.delay.mixer.setVol(val.ccdbfs);
}, page: page);
// Delay Time:
~apc.setDeviceEncoderFunc(3, { | val |
  var delayTime = val.linlin(0, 127, 0.001, 5);
  ~habit.delay.setDelayTime(delayTime);
}, page: page);
// feedback:
~apc.setDeviceEncoderFunc(4, { | val |
  var feedback = val.linlin(0, 127, 0, 1);
  ~habit.delay.setFeedback(feedback);
}, page: page);

//// Main Granulator:
~apc.setDeviceEncodersMonitorFunc(\granulator, {
  //var trigRate = ~prm.granulator.trigR
  ~apc.setDeviceEncoderValue(5, ~prm.granulator.mixer.vol.dbfsCC);
}, page: page);
// Volume:
~apc.setDeviceEncoderFunc(5, { | val |
  ~prm.granulator.mixer.setVol(val.ccdbfs);
}, page: page);
// Trig Rate:
~apc.setDeviceEncoderFunc(6, {  | val |
  var trigRate = val.linlin(0, 127, 0, 100);
  ~prm.granulator.setTrigRate(trigRate);
  ~apc.setDeviceEncoderValue(6, val);
}, page: page);
// Grain Dur Low:
~apc.setDeviceEncoderFunc(7, { | val |
  var dur = val.linlin(0, 127, 0.001, 0.5);
  ~prm.granulator.setGrainDurLow(dur);
  ~apc.setDeviceEncoderValue(7, val);
}, page: page);
// Grain Dur High:
~apc.setDeviceEncoderFunc(8, { | val |
  var dur = val.linlin(0, 127, 0.005, 3);
  ~prm.granulator.setGrainDurHigh(dur);
  ~apc.setDeviceEncoderValue(8, val);
}, page: page);

////////////////////////
//////// GRID: ////////
//////////////////////

//// sequences:

sequenceResetFunc = {
  ~apc.turnGridCyan(0, 4, 1);
  ~apc.turnGridMagenta(1, 4, 1);
  ~apc.turnGridBlue(2, 4, 1);
  ~apc.turnGridCyan(3, 4, 1);
  ~apc.turnGridYellow(4, 4, 1);
  ~apc.turnGridWhite(5, 4, 1);
  ~apc.turnGridRed(6, 4, 1);
};

~apc.setGridMonitorFunc(\sequences, {
  sequenceResetFunc.value;
  switch(~habit.moog.sequencePlaying,
    'sequence1', { ~apc.turnGridGreen(0, 4); },
    'sequence2', { ~apc.turnGridGreen(1, 4); },
    'sequence3', { ~apc.turnGridGreen(2, 4) },
    'sequence4', { ~apc.turnGridGreen(3, 4) },
    'sectionEndLoop', { ~apc.turnGridGreen(4, 4) },
    'sectionEnd', { ~apc.turnGridGreen(5, 4) },
    'pieceEnd', { ~apc.turnGridGreen(6, 4) }
  );
}, page: page);

~apc.setGridFunc(0, 4, { ~habit.moog.playSequence1(~habit.clock); }, page: page);
~apc.setGridFunc(1, 4, { ~habit.moog.playSequence2(~habit.clock); }, page: page);
~apc.setGridFunc(2, 4, { ~habit.moog.playSequence3(~habit.clock); }, page: page);
~apc.setGridFunc(3, 4, { ~habit.moog.playSequence4(~habit.clock); }, page: page);
~apc.setGridFunc(4, 4, { ~habit.moog.playSectionEndLoop(~habit.clock); }, page: page);
~apc.setGridFunc(5, 4, { ~habit.moog.playSectionEnd(~habit.clock); }, page: page);
~apc.setGridFunc(6, 4, { ~habit.moog.playPieceEnd(~habit.clock); }, page: page);

//// notes being played:
~apc.setGridMonitorFunc(\notesPlaying, {
  8.do({ | i | ~apc.turnGridWhite(i, 3); });
  if( ~habit.moog.notePlaying.isInteger, { ~apc.turnGridGreen(pitchArray.atIdentityHash(~habit.moog.notePlaying), 3) },
    { 8.do({ | i | ~ohm.turnGridWhite(i, 3); }); });
}, page: page);


////// Looper:
// Loop:
~apc.setGridMonitorFunc(\loopers, {
  if( ~habit.trumpetLoopers.looper1.isRecording, { ~apc.turnGridWhite(4, 1); },
    if( ~habit.trumpetLoopers.looper1.isPlaying,
      { ~apc.turnGridGreen(4, 1) }, { ~apc.turnGridBlue(4, 1, 1); });
  );
  if( ~habit.trumpetLoopers.looper1.isPlaying,
    { ~apc.turnGridGreen(4, 0) }, { ~apc.turnGridRed(4, 0, 0) });
  if( ~habit.trumpetLoopers.looper2.isRecording, { ~apc.turnGridWhite(5, 1) },
    if( ~habity.trumpetLoopers.looper2.isPlaying,
      { ~apc.turnGridGreen(5, 1) }, { ~apc.turnGridBlue(5, 1, 1); });
  );
  if( ~habit.trumpetLoopers.looper2.isPlaying,
    { ~apc.turnGridGreen(5, 0) }, { ~apc.turnGridRed(5, 0, 0) });
  if( ~habit.trumpetLoopers.looper3.isRecording, { ~apc.turnGridWhite(6, 1) },
    if( ~habity.trumpetLoopers.looper3.isPlaying,
      { ~apc.turnGridGreen(6, 1) }, { ~apc.turnGridBlue(6, 1, 1); });
  );
  if( ~habit.trumpetLoopers.looper3.isPlaying,
    { ~apc.turnGridGreen(6, 0) }, { ~apc.turnGridRed(6, 0, 0) });
  if( ~habit.trumpetLoopers.looper4.isRecording, { ~apc.turnGridWhite(7, 1) },
    if( ~habity.trumpetLoopers.looper4.isPlaying,
      { ~apc.turnGridGreen(7, 1) }, { ~apc.turnGridBlue(7, 1, 1); });
    if( ~habit.trumpetLoopers.looper4.isPlaying,
    { ~apc.turnGridGreen(7, 0) }, { ~apc.turnGridRed(7, 0, 0) });
  );
}, page: page);


//// erase buttons color:
4.do({ | i | ~apc.turnGridRed(i+4, 2, page: page); });


//// Looper 1:
~apc.setGridFunc(4, 1, { ~habit.trumpetLoopers.looper1.loop; }, page: page);
// play:
~apc.setGridFunc(4, 0, { ~habit.trumpetLoopers.looper1.togglePlayLoop; }, page: page);
~ohm.setGridFunc(0, 3, { ~habit.trumpetLoopers.looper1.togglePlayLoop; }, page: page);
// erase:
~apc.setGridFunc(4, 2, { ~habit.trumpetLoopers.looper1.clearLoop(90); }, page: page);

//// Looper 2:
// Loop:
~apc.setGridFunc(5, 1, { ~habit.trumpetLoopers.looper2.loop; }, page: page);
// play:
~apc.setGridFunc(5, 0, { ~habit.trumpetLoopers.looper2.togglePlayLoop; }, page: page);
// erase:
~apc.setGridFunc(5, 2, { ~habit.trumpetLoopers.looper2.clearLoop(90); }, page: page);


//// Looper 3:
// Loop:
~apc.setGridFunc(6, 1, { ~habit.trumpetLoopers.looper3.loop; }, page: page);
// play:
~apc.setGridFunc(6, 0, { ~habit.trumpetLoopers.looper3.togglePlayLoop; }, page: page);
// erase:
~apc.setGridFunc(6, 2, { ~habit.trumpetLoopers.looper3.clearLoop(90); }, page: page);


//// Looper 4:
// Loop:
~apc.setGridFunc(7, 1, { ~habit.trumpetLoopers.looper4.loop; }, page: page);
// play:
~apc.setGridFunc(7, 0, { ~habit.trumpetLoopers.looper4.togglePlayLoop; }, page: page);
// erase:
~apc.setGridFunc(7, 2, { ~habit.trumpetLoopers.looper4.clearLoop(90); }, page: page);


//// Grain envelopes:
~apc.setGridMonitorFunc(\grain, {
  3.do({ | i | ~apc.turnGridCyan(i, 1, 1); });
  3.do({ | i | ~apc.turnGridCyan(i, 0, 1); });
switch( ~prm.granulator.grainEnv,
    'sine', { ~apc.turnGridGreen(0, 1); },
    'gabor', { ~apc.turnGridGreen(1, 1); },
    'perc', { ~apc.turnGridGreen(2, 1); },
    'revPerc', { ~apc.turnGridGreen(0, 0); },
    'expodec', { ~apc.turnGridGreen(1, 0); },
    'rexpodec', { ~apc.turnGridGreen(2, 0); }
  );
}, page: page);
// sine:
~apc.setGridFunc(0, 1, { ~prm.granulator.setGrainEnvelope('sine'); }, page: page);
// gabor:
~apc.setGridFunc(1, 1, { ~prm.granulator.setGrainEnvelope('gabor'); }, page: page);
// perc:
~apc.setGridFunc(2, 1, { ~prm.granulator.setGrainEnvelope('perc'); }, page: page);
// revPerc:
~apc.setGridFunc(0, 0, { ~prm.granulator.setGrainEnvelope('revPerc'); }, page: page);
// expodec:
~apc.setGridFunc(1, 0, { ~prm.granulator.setGrainEnvelope('expodec'); }, page: page);
// rexpodec:
~apc.setGridFunc(2, 0, { ~prm.granulator.setGrainEnvelope('rexpodec'); }, page: page);



//////// tempo reset:
// set tempo to 60 BPM:
~apc.setSceneLaunchMonitorFunc(\tempo, {
  if( ~habit.clock.tempo == 1,
    { ~apc.turnSceneLaunchButtonGreen(0) }, { ~apc.turnSceneLaunchButtonWhite(1); });
}, page: page);
~apc.setSceneLaunchFunc(0, { ~habit.setClockTempo(60); }, page: page);
// set tempo to 120 BPM:
~apc.setSceneLaunchMonitorFunc(\tempo, {
  if( ~habit.clock.tempo == 2,
    { ~apc.turnSceneLaunchButtonGreen(1) }, { ~apc.turnSceneLaunchButtonWhite(1); });
}, page: page);
~apc.setSceneLaunchFunc(1, { ~habit.setClockTempo(120); }, page: page);
