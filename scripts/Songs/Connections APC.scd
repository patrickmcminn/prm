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
var gridBank1 = 0;
var encoderBank1 = 0;

var loadAirSputtersFuncs, loadDronerFuncs, loadNoteRecordFuncs, loadBasslineFuncs, loadTrumpetGranFuncs;
var loadInletFuncs, loadChordFuncs, loadMicFuncs;

var freeAirSputtersFuncs, freeDronerFuncs, freeNoteRecordFuncs, freeBasslineFuncs;
var freeTrumpetGranFuncs, freeInletFuncs, freeChordFuncs, freeMicFuncs;

var airSputtersMonitorRoutine;

var noteRecordLoaded, airSputtersLoaded, dronerLoaded, basslinesLoaded, trumpetGranLoaded;
var inletLoaded, chordsLoaded;

var noteRecordColorArray, cascadeRecordColorArray, chordRecordColorArray;

noteRecordLoaded = true;
airSputtersLoaded = false;
dronerLoaded = false;
basslinesLoaded = false;
trumpetGranLoaded = false;
inletLoaded = false;
chordsLoaded = false;

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
~apc.setTrackSelectFunc(8, { ~prm.microphone.tglMute; }, bank: mixerBank1, page: page);
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
~apc.setRecordEnableButtonFunc(1, { ~connections.airSputtersInput.tglMute },
  bank: mixerBank1, page: page);
~apc.setMixerMonitorFunc('airSputtersInput', { if( ~connections.airSputtersInput.isMuted,
  { ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
}, bank: mixerBank1, page: page);


//// droner:
~apc.setRecordEnableButtonFunc(2, { ~connections.dronerInput.tglMute },
  bank: mixerBank1, page: page);
~apc.setMixerMonitorFunc('dronerInput', { if ( ~connections.dronerInput.isMuted,
  { ~apc.turnRecordEnableButtonOff(2) }, { ~apc.turnRecordEnableButtonOn(2) });
}, bank: mixerBank1, page: page);

//// trumpet gran:
~apc.setRecordEnableButtonFunc(4, { ~connections.trumpetGranInput.tglMute },
  bank: mixerBank1, page: page);
~apc.setMixerMonitorFunc('trumpetGranInput', { if( ~connections.trumpetGranInput.isMuted,
  { ~apc.turnRecordEnableButtonOff(4) }, { ~apc.turnRecordEnableButtonOn(4) });
}, bank: mixerBank1, page: page);



cascadeRecordColorArray = ['yellow', 'blue', 'cyan', 'magenta', 'white'];
chordRecordColorArray = ['magenta', 'green', 'cyan', 'magenta', 'white', 'green', 'yellow'];


//// cascade record:
~apc.setSceneLaunchMonitorFunc(\cascadeRecord, {
  switch(~connections.noteRecord.cascadeRecordNum,
    0, { ~apc.turnSceneLaunchButtonYellow(0) },
    1, { ~apc.turnSceneLaunchButtonBlue(0) },
    2, { ~apc.turnSceneLaunchButtonCyan(0) },
    3, { ~apc.turnSceneLaunchButtonMagenta(0) },
    4, { ~apc.turnSceneLaunchButtonWhite(0) });
}, bank: gridBank1, page: page);
~apc.setSceneLaunchFunc(0, {
  ~connections.noteRecord.cascadeRecordRoutine.next;
}, bank: gridBank1, page: page);
~apc.setSceneLaunchMonitorFunc(\chordRecord, {
  switch(~connections.noteRecord.chordRecordNum,
    0, { ~apc.turnSceneLaunchButtonMagenta(1) },
    1, { ~apc.turnSceneLaunchButtonYellowGreen(1) },
    2, { ~apc.turnSceneLaunchButtonCyan(1) },
    3, { ~apc.turnSceneLaunchButtonMagenta(1) },
    4, { ~apc.turnSceneLaunchButtonWhite(1) },
    5, { ~apc.turnSceneLaunchButtonGreen(1) },
    6, { ~apc.turnSceneLaunchButtonYellow(1) });
}, bank: gridBank1, page: page);
~apc.setSceneLaunchFunc(1, { ~connections.noteRecord.chordRecordRoutine.next;
}, bank: gridBank1, page: page);

~apc.turnSceneLaunchButtonRed(3, bank: gridBank1, page: page);
~apc.setSceneLaunchFunc(3, { ~connections.noteRecord.resetCascadeRecordRoutine
}, bank: gridBank1, page: page);
~apc.turnSceneLaunchButtonRed(4, bank: gridBank1, page: page);
~apc.setSceneLaunchFunc(4, { ~connections.noteRecord.resetChordRecordRoutine;
}, bank: gridBank1, page: page);


/////// Air Sputters:

// load:
~apc.setClipStopFunc(1, { if( airSputtersLoaded == false,
  {
    r {
      ~connections.toggleLoadAirSputters;
      while({ try { ~connections.airSputters.isLoaded } != true }, { 0.001.wait; });
      loadAirSputtersFuncs.value;
      ~apc.turnClipStopButtonOn(1);
      airSputtersLoaded = true;
    }.play;
  },
  {
    ~connections.toggleLoadAirSputters;
    freeAirSputtersFuncs.value;
    ~apc.turnClipStopButtonOff(1);
    airSputtersLoaded = false;
});
}, bank: gridBank1, page: page);

// Function Load:
loadAirSputtersFuncs = {

  // monitoring routines:
  airSputtersMonitorRoutine.reset;
  airSputtersMonitorRoutine.play;

  // monitors air sputters:
  ~apc.turnGridBlue(0, 4, 1, bank: gridBank1, page: page);
  ~apc.setGridMonitorFunc('airSputtersMonitor', {
    switch( ~connections.airSputters.sputterNum,
      1, { ~apc.turnGridMagenta(0, 4) },
      2, { ~ohm.turnGridCyan(0, 4) },
      3, { ~ohm.turnGridYellow(0, 4) },
      4, { ~ohm.turnGridWhite(0, 4) }
    );
  }, bank: gridBank1, page: page);

  // starts/stops air sputters:
  ~apc.turnGridGreen(0, 3, 2, bank: gridBank1, page: page);
  ~apc.setGridFunc(0, 3, { if( ~connections.airSputters.isPlaying == true,
    { ~connections.airSputters.stopAllPatterns; },
    { ~connections.airSputters.playAllPatterns });
  }, bank: gridBank1, page: page);
  ~apc.setGridMonitorFunc('airSputters', {
    if( ~connections.airSputters.isPlaying == true,
      { ~apc.turnGridGreen(0, 3) },
      { ~apc.turnGridRed(0, 3, 1) });
  }, bank: gridBank1, page: page);

};

// Function Offload:

freeAirSputtersFuncs = {
  airSputtersMonitorRoutine.stop;
  ~apc.setGridFunc(0, 3, { }, bank: gridBank1, page: page);
  ~apc.setGridMonitorFunc('airSputtersMonitor', { }, bank: gridBank1, page: page);
  ~apc.setGridMonitorFunc('airSputters', { }, bank: gridBank1, page: page);
  ~apc.turnGridOff(0, 3);
  ~apc.turnGridOff(0, 4);
};


/////// Droner:
~apc.setClipStopMonitorFunc(\dronerIsLoaded, {
  if( ~connections.droner.isLoaded,
    { ~apc.turnClipStopButtonOn(2) }, { ~apc.turnClipStopButtonOff(2) });
}, bank: mixerBank1, page: page);
~apc.setClipStopFunc(2, {
  if(~connections.droner.isLoaded == false,
    r {
      ~connections.toggleLoadDroner;
      while({ try { ~connections.droner.isLoaded } != true }, { 0.001.wait; });
      loadDronerFuncs.value;
    }.play,
    {
      ~connections.toggleLoadDroner;
      freeDronerFuncs.value;
  });
}, bank: gridBank1, page: page);

loadDronerFuncs =  {

  var grainRateResetFunc;

  grainRateResetFunc = { 3.do({ | i | ~apc.turnGridPink(1, i+2, 1, bank: gridBank1, page: page); }); };

  ////// buttons:

  //// erosion:
  ~apc.turnGridYellow(1, 4, bank: gridBank1, page: page);
  // mix high:
  ~apc.setGridFunc(1, 4, {
    ~connections.droner.setErosionMix(0.7);
    ~apc.turnGridWhite(1, 4);
  }, type: 'noteOn', bank: gridBank1, page: page);
  ~apc.setGridFunc(1, 4, {
    ~connections.droner.setErosionMix(0.02);
    ~apc.turnGridYellow(1, 4);
  }, type: 'noteOff', bank: gridBank1, page: page);

  //// grain rate shifts:
  grainRateResetFunc.value;
  ~apc.turnGridGreen(1, 3, bank: gridBank1, page: page);

  // normal:
  ~apc.setGridFunc(1, 3, {
    ~connections.droner.setGrainRates([0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.25]);
    grainRateResetFunc.value;
    ~apc.turnGridGreen(1, 3);
  }, bank: gridBank1, page: page);

  // 1s added:
  ~apc.setGridFunc(1, 2, {
    ~connections.droner.setGrainRates([1, 1, 0.5, 0.5, 0.5, 0.25, 0.25]);
    grainRateResetFunc.value;
    ~apc.turnGridGreen(1, 2);
  }, bank: gridBank1, page: page);

  // 0.5s all around:
  ~apc.setGridFunc(1, 1, {
    ~connections.droner.setGrainRates([0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5]);
    grainRateResetFunc.value;
    ~apc.turnGridGreen(1, 1);
  }, bank: gridBank1, page: page);

  ////// knobs:

  ~apc.setDeviceEncodersMonitorFunc('dronerKnobs', {
    var crossfade = ~connections.droner.granulator.crossfade.linlin(-1.0, 1.0, 0, 127);
    var lowShelf = ~connections.droner.eq.lowGain.linlin(-6.0, 6.0, 0, 127);
    var highShelf = ~connections.droner.eq.highGain.linlin(-6.0, 6.0, 0, 127);
    ~apc.setDeviceEncoderValue(1, crossfade);
    ~apc.setDeviceEncoderValue(2, lowShelf);
    ~apc.setDeviceEncoderValue(3, highShelf);

  }, bank: encoderBank1, page: page);

  // granulator mix:
  ~apc.setDeviceEncoderFunc(1, { | val |
    var crossfade = val.linlin(0, 127, -1.0, 1.0);
    ~connections.droner.granulator.setGranulatorCrossfade(crossfade);
  }, bank: encoderBank1, page: page);

  // low shelf:
  ~apc.setDeviceEncoderFunc(2, { | val |
    var eq = val.linlin(0, 127, -6.0, 6.0);
    ~connections.droner.eq.setLowGain(eq);
  }, bank: encoderBank1, page: page);

  // high shelf;
  ~apc.setDeviceEncoderFunc(3, { | val |
    var eq = val.linlin(0, 127, -6.0, 6.0);
    ~connections.droner.eq.setHighGain(eq);
  }, bank: encoderBank1, page: page);

};

freeDronerFuncs =  {
  //// buttons:
  4.do({ | i | ~apc.turnGridOff(1, i+1, bank: gridBank1, page: page); });
  4.do({ | i | ~apc.setGridFunc(1, i+1, { }, bank: gridBank1, page: page); });


  //// knobs:
  ~apc.setDeviceEncodersMonitorFunc('dronerKnobs', { }, bank: encoderBank1, page: page);
  3.do({ | i | ~apc.setDeviceEncoderFunc(i + 1, { }, bank: encoderBank1, page: page); });
};


//////// Note Record:
noteRecordColorArray = ['blue', 'cyan', 'magenta', 'white', 'green', 'yellow'];

/////// Bass:

//// bassline record:
~apc.turnGridBlue(2, 3, bank: gridBank1, page: page);
~apc.setGridMonitorFunc(\basslineRecord, {
  switch(~connections.noteRecord.basslineRecordNum,
    0, { ~apc.turnGridBlue(2, 3) },
    1, { ~apc.turnGridCyan(2, 3) },
    2, { ~apc.turnGridMagenta(2, 3) },
    3, { ~apc.turnGridWhite(2, 3) },
    4, { ~apc.turnGridGreen(2, 3) },
    5, { ~apc.turnGridYellow(2, 3) }
  );
}, bank: gridBank1, page: page);
~apc.setGridFunc(2, 3, { ~connections.noteRecord.basslineRecordRoutine.next; }, bank: gridBank1, page: page);

// resets:
~apc.turnGridRed(2, 4, bank: gridBank1, page: page);
~apc.setGridFunc(2, 4, { ~connections.noteRecord.resetBasslineRecordRoutine; },
  bank: gridBank1, page: page);

//// Bassline:
// On/Off:
~apc.setClipStopMonitorFunc(\bassline, {
  if( ~connections.bass.isLoaded,
    { ~apc.turnClipStopButtonOn(3) }, { ~apc.turnClipStopButtonOff(3) });
}, bank: gridBank1, page: page);
~apc.setClipStopFunc(3, {
  if( ~connections.bass.isLoaded == false,
    {
      r {
        ~connections.toggleLoadBass;
        while({ try { ~connections.bass.isLoaded } != true }, { 0.001.wait; });
        loadBasslineFuncs.value;
      }.play
    },
    {
      ~connections.toggleLoadBass;
      freeBasslineFuncs.value;
  })
}, bank: gridBank1, page: page);

loadBasslineFuncs = {

  ///// buttons:

  ~apc.setGridMonitorFunc(\basslines, {
    if( ~connections.bass.bassline1IsPlaying,
      { ~apc.turnGridGreen(2, 1) }, { ~apc.turnGridCyan(2, 1, 1) });
    if( ~connections.bass.bassline2IsPlaying,
      { ~apc.turnGridGreen(2, 0) }, { ~apc.turnGridCyan(2, 1, 1) });
  }, bank: gridBank1, page: page);
  ~apc.setGridFunc(2, 1, {
    if( ~connections.bass.bassline1IsPlaying == false,
      { ~connections.bass.playBassline1(~connections.clock); },
      { ~connections.bass.stopBassline1(~connections.clock); }
    );
  }, bank: gridBank1, page: page);
  ~apc.setGridFunc(2, 0, {
    if( ~connections.bass.bassline2IsPlaying == false,
      { ~connections.bass.playBassline2(~connections.clock); },
      { ~connections.bass.stopBassline2(~connections.clock); }
    );
  }, bank: gridBank1, page: page);


  //// knobs:

  ~apc.setDeviceEncodersMonitorFunc(\bassGran, {
    var crossfade = ~connections.bass.granulator.crossfade.linlin(-1.0, 1.0, 0, 127);
    ~apc.setDeviceEncoderValue(4, crossfade);
  }, bank: encoderBank1, page: page);
  ~apc.setDeviceEncoderFunc(4, { | val |
    var crossfade = val.linlin(0, 127, -1.0, 1.0);
    ~connections.bass.granulator.setGranulatorCrossfade(crossfade);
  }, bank: encoderBank1, page: page);

};

freeBasslineFuncs = {
  ~apc.setDeviceEncodersMonitorFunc(\bassGran, { }, bank: encoderBank1, page: page);
  ~apc.setDeviceEncoderFunc(4, { }, bank: encoderBank1, page: page);
  2.do({ | i | ~apc.setGridFunc(2, i, { }, bank: gridBank1, page: page); });
  ~apc.setGridMonitorFunc(\basslines, { }, bank: gridBank1, page: page);
  2.do({ | i | ~apc.turnGridOff(2, i, bank: gridBank1, page: page); });
};


/////// Trumpet Gran:
~apc.setClipStopMonitorFunc(\trumpetGran, {
  if( ~connections.trumpetGran.isLoaded,
    { ~apc.turnClipStopButtonOn(4) }, { ~apc.turnClipStopButtonOff(4) });
}, bank: mixerBank1, page: page);
~apc.setClipStopFunc(4, {
  if( ~connections.trumpetGran.isLoaded == false,
    {
      r{
        ~connections.toggleLoadTrumpetGran;
        while({ try { ~connections.trumpetGran.isLoaded } != true }, { 0.001.wait; });
        loadTrumpetGranFuncs.value;
      }.play;
    },
    {
      ~connections.toggleLoadTrumpetGran;
      freeTrumpetGranFuncs.value;

    }
  );
}, bank: mixerBank1, page: page);

loadTrumpetGranFuncs = {
  //// knobs:
  ~apc.setDeviceEncodersMonitorFunc(\trumpetGran, {
    var feedback = ~connections.trumpetGran.granulator.delay.feedback.linlin(0.0, 1.1, 0, 127);
    var gain = ~connections.trumpetGran.eq.highGain.linlin(-6.0, 9.0, 0, 127);
    ~apc.setDeviceEncoderValue(5, feedback);
    ~apc.setDeviceEncoderValue(6, gain);
  }, bank: encoderBank1, page: page);

  ~apc.setDeviceEncoderFunc(5, { | val |
    var feedback = val.linlin(0, 127, 0, 1.1);
    ~connections.trumpetGran.granulator.setFeedback(feedback);
  }, bank: encoderBank1, page: page);
  ~apc.setDeviceEncoderFunc(6, { | val |
    var gain = val.linlin(0, 127, -6.0, 9.0);
    ~connections.trumpetGran.eq.setHighGain(gain);
  }, bank: encoderBank1, page: page);

};

freeTrumpetGranFuncs = {
  ~apc.setDeviceEncodersMonitorFunc(\trumpetGran, { }, bank: encoderBank1, page: page);
  2.do({ | i | ~apc.setDeviceEncoderFunc(i+5, {}, bank: encoderBank1, page: page); });
};

/////// Inlet:
~apc.setClipStopMonitorFunc(\inlet, {
  if( ~connections.inlet.isLoaded,
    { ~apc.turnClipStopButtonOn(5) }, { ~apc.turnClipStopButtonOff(5) });
}, bank: mixerBank1, page: page);
~apc.setClipStopFunc(5, {
  if( ~connections.inlet.isLoaded == false,
    {
      r{
        ~connections.toggleLoadInlet;
        while({ try { ~connections.inlet.isLoaded } != true }, { 0.001.wait; });
        loadInletFuncs.value;
      }.play;
    },
    {
      ~connections.toggleLoadInlet;
      freeInletFuncs.value;
  });
}, bank: mixerBank1, page: page);

loadInletFuncs = {
  ////// inlet:
  ~apc.setDeviceEncodersMonitorFunc(\inletFuncs, {
    var crossfade = ~connections.inlet.granulator.crossfade.linlin(-1.0, 1.0, 0, 127);
    var atkVol = ~connections.inlet.attackRandomizer.mixer.vol.dbfsCC;
    ~apc.setDeviceEncoderValue(7, crossfade);
    ~apc.setDeviceEncoderValue(8, atkVol);
  }, bank: encoderBank1, page: page);

  ~apc.setDeviceEncoderFunc(7, { | val |
    var crossfade = val.linlin(0, 127, -1.0, 1.0);
    ~connections.inlet.granulator.setCrossfade(crossfade);
  }, bank: encoderBank1, page: page);

  // attack randomizer:
  ~apc.setDeviceEncoderFunc(8, { | val |
    ~connections.inlet.attackRandomizer.mixer.setVol(val.ccdbfs(-70, 3));
  }, bank: encoderBank1, page: page);
  // cascade:
  ~apc.setFaderFunc(7, { | val |
    ~connections.inlet.cascade.mixer.setValue(val.ccdbfs(-70, 3));
  }, bank: encoderBank1 page: page);

  ~apc.setGridMonitorFunc(\inlet, {
    if( ~connections.inlet.attackRandomizer.isPlaying,
      { ~apc.turnGridGreen(4, 4) }, { ~apc.turnGridBlue(4, 4, 1) });
    if( ~connections.inlet.cascade.isPlaying,
      { ~apc.turnGridGreen(4, 3) }, { ~apc.turnGridPurple(4, 3, 1) });
  }, bank: mixerBank1, page: page);

  ~apc.setGridFunc(4, 4, {
    if( ~connections.inlet.attackRandomizer.isPlaying == false,
      { ~connections.inlet.attackRandomizer.playSequences(~connections.clock) },
      { ~conecctions.inlet.attackRandomizer.stopSequence; });
  }, bank: mixerBank1, page: page);
  ~apc.setGridFunc(4, 3, {
    if( ~connections.inlet.cascade.isPlaying == false,
      { ~connections.inlet.cascade.playSequences(~connections.clock); },
      { ~connections.inlet.cascade.stopSequences; });
  }, bank: mixerBank1, page: page);

};

freeInletFuncs = {
  2.do({ | i |
    ~apc.turnGridOff(4, i+3, bank: mixerBank1, page: page);
    ~apc.setGridFunc(4, i+3, {}, bank: mixerBank1, page: page);
    ~apc.setDeviceEncoderFunc(i+7, {}, bank: encoderBank1, page: page);
  });
  ~apc.setDeviceEncodersMonitorFunc(\inletFuncs, {}, bank: encoderBank1, page: page);
  ~apc.setMixerMonitorFunc(\inlet, {}, bank: mixerBank1, page: page);
};

/////// Chords:

~apc.setClipStopMonitorFunc(\chords, {
  if( ~connections.chords.isLoaded,
    { ~apc.turnClipStopButtonOn(6) }, { ~apc.turnClipStopButtonOff(7) });
}, bank: mixerBank1, page: page);
~apc.setClipStopFunc(6, {
  if( ~connections.chords.isLoaded == false,
    {
      r{
        ~connections.toggleLoadChords;
        while({ try { ~connections.chords.isLoaded } != true }, { 0.001.wait; });
        loadChordFuncs.value;
      }.play
    },
    {
      ~connections.toggleLoadChords;
      freeChordFuncs.value;
  });
}, bank: mixerBank1, page: page);

loadChordFuncs = {
  //// buttons:

  // chord sum:
  ~apc.turnGridPurple(5, 4, bank: gridBank1, page: page);
  ~apc.setGridFunc(5, 4, {
    ~connections.chords.recordChords;
    ~apc.turnGridGreen(5, 4, 1);
  }, bank: gridBank1, page: page);

  ~apc.setGridMonitorFunc(\chords, {
    if( ~connections.chords.randomChordsIsPlaying,
      { ~apc.turnGridGreen(5, 3) }, { ~apc.turnGridYellow(5, 3, 1) });
    if( ~connections.chords.chordProgressionIsPlaying,
      { ~apc.turnGridGreen(5, 2) }, { ~apc.turnGridBlue(5, 2, 1) });
  }, bank: mixerBank1, page: page);

  // random chords:
  ~apc.setGridFunc(5, 3, {
    if( ~connections.chords.randomChordsIsPlaying == false,
      { ~connections.chords.playRandomChords(~connections.clock); },
      { ~connections.chords.stopRandomChords; });
  }, bank: mixerBank1, page: page);
  // chord progression:
  ~apc.setGridFunc(5, 2, {
    if( ~connections.chords.chordProgressionIsPlaying == false,
      {  ~connections.chords.playChordProgression(~connections.clock); },
      { ~connections.chords.stopChordProgression; });
  }, bank: mixerBank1, page: page);

};

freeChordFuncs = {

  3.do({ | i |
    ~apc.turnGridOff(5, i+2, bank: gridBank1, page: page);
    ~apc.setGridFunc(5, i+2, { }, bank: gridBank1, page: page);
    ~apc.setGridMonitorFunc(\chords, { }, bank: gridBank1, page: page);
  });

}

