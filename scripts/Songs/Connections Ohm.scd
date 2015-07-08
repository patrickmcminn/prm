/*
Wednesday, July 8th 2015
Connections Ohm.scd
prm
*/

var page = 'connections';

// banks:
var mainBank = 0;

var loadAirSputtersFuncs, loadDronerFuncs, loadNoteRecordFuncs, loadBasslineFuncs, loadTrumpetGranFuncs;
var loadInletFuncs, loadChordFuncs, loadMicFuncs;

var freeAirSputtersFuncs, freeDronerFuncs, freeNoteRecordFuncs, freeBasslineFuncs;
var freeTrumpetGranFuncs, freeInletFuncs, freeChordFuncs, freeMicFuncs;

var airSputtersMonitorRoutine;

var noteRecordLoaded, airSputtersLoaded, dronerLoaded, basslinesLoaded, trumpetGranLoaded;
var inletLoaded, chordsLoaded;

noteRecordLoaded = true;
airSputtersLoaded = false;
dronerLoaded = false;
basslinesLoaded = false;
trumpetGranLoaded = false;
inletLoaded = false;
chordsLoaded = false;

~ohm.makePage(page);

// page load function:
~ohm.setPageLoadFunction({
  ~ohm.storageDict[\loadGlobalSubmixerFunc].value;
}, page);



// Air Sputters:

// On/Off:
~ohm.turnGridWhite(0, 0, bank: mainBank, page: page);
~ohm.setGridFunc(0, 0, {
  if( airSputtersLoaded == false, {
    r {
      ~connections.toggleLoadAirSputters;
      while({ try { ~connections.airSputters.isLoaded } != true }, { 0.001.wait; });
      loadAirSputtersFuncs.value;
      ~ohm.turnGridGreen(0, 0);
      airSputtersLoaded = true;
    }.play;
    },
    {
      ~connections.toggleLoadAirSputters;
      freeAirSputtersFuncs.value;
      ~ohm.turnGridRed(0, 0);
      airSputtersLoaded = false;
    }
  );
}, bank: mainBank, page: page);

// Function Load:
loadAirSputtersFuncs = {

  // monitoring routines:
  airSputtersMonitorRoutine.reset;
  airSputtersMonitorRoutine.play;

  // Fader:
  ~ohm.setLeftSliderFunc(0, { | val |
    ~connections.mixerA.setVol(0, val.ccdbfs(-70, 0));
  }, bank: mainBank, page: page);

  // monitors air sputters:
  ~ohm.turnGridBlue(1, 0, page: page);

  // starts/stops air sputters:
  ~ohm.turnGridGreen(2, 0, page: page);
  ~ohm.setGridFunc(2, 0, r {
    loop {
      ~connections.airSputters.stopAllPatterns;
      ~ohm.turnGridRed(2, 0).yield;

      ~connections.airSputters.playAllPatterns;
      ~ohm.turnGridGreen(2, 0).yield;
    };
  }, bank: mainBank, page: page);

};

// Function Offload:

freeAirSputtersFuncs {
  airSputtersMonitorRoutine.stop;
  ~ohm.setLeftSliderFunc(0, { }, bank: mainBank, page: page);
  ~ohm.setGridFunc(2, 0, { }, bank: mainBank, page: page);
  ~ohm.turnGridOff(1, 0, bank: mainBank, page: page);
  ~ohm.turnGridOff(2, 0, bank: mainBank, page: page);
};


airSputtersMonitorRoutine = r {
  loop {
    switch ( ~connections.airSputters.sputterNum,
      1, { ~ohm.turnGridPurple(1, 0, bank: mainBank, page: page); },
      2, { ~ohm.turnGridCyan(1, 0, bank: mainBank, page: page); },
      3, { ~ohm.turnGridYellow(1, 0, bank: mainBank, page: page); },
      4, { ~ohm.turnGridWhite(1, 0, bank: mainBank, page: page); }
    );
    0.05.wait;
  }
};

//////// Droner:

// On/Off:
~ohm.turnGridCyan(0, 1, bank: mainBank, page: page);
~ohm.setGridFunc(0, 1, {
  if( dronerLoaded == false, {
    r {
      ~connections.toggleLoadDroner;
      while({ try { ~connections.droner.isLoaded } != true }, { 0.001.wait; });
      loadDronerFuncs.value;
      ~ohm.turnGridGreen(0, 1);
      dronerLoaded = true;
    }.play;
    },
    {
      ~connections.toggleLoadDroner;
      freeDronerFuncs.value;
      ~ohm.turnGridRed(0, 1);
      dronerLoaded = false;
  });
}, bank: mainBank, page: page);

loadDronerFuncs {

  var grainRateResetFunc;

  grainRateResetFunc = {
    3.do({ | i | ~ohm.turnGridPurple(i + 1, 2, bank: mainBank, page: page); });
  };

  // fader:
  ~ohm.setLeftSliderFunc(1, { | val |
    ~connections.mixerA.setVol(1, val.ccdbfs(-70, 0));
  }, bank: mainBank, page: page);

  ////// buttons:

  // mute:
  ~ohm.turnGridGreen(1, 1, bank: mainBank, page: page);
  ~ohm.setGridFunc(1, 1, {
    ~connections.droner.input.tglMute;
    if( ~connections.droner.input.isMuted,
      { ~ohm.turnGridRed(1, 1); },
      { ~ohm.turnGridGreen(1, 1); });
  }, bank: mainBank, page: page);

  //// erosion:
  ~ohm.turnGridYellow(0, 2, bank: mainBank, page: page);
  // mix high:
  ~ohm.setGridFunc(0, 2, {
    ~connections.droner.setErosionMix(0.7);
    ~ohm.turnGridWhite(0, 2);
  }, type: 'noteOn', bank: mainBank, page: page);
  // mix normal:
  ~ohm.setGridFunc(0, 2, {
    ~connections.droner.setErosionMix(0.02);
    ~ohm.turnGridYellow(0, 2);
  }, type: 'noteOff', bank: mainBank, page: page);

  //// grain rate shifts:
  grainRateResetFunc.value;
  ~ohm.turnGridGreen(1, 2, bank: mainBank, page: page);

  // normal:
  ~ohm.setGridFunc(1, 2, {
    ~connections.droner.setGrainRates([0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.25]);
    grainRateResetFunc.value;
    ~ohm.turnGridGreen(1, 2, bank: mainBank, page: page);
  }, bank: mainBank, page: page);

  // 1s added:
  ~ohm.setGridFunc(2, 2, {
    ~connections.droner.setGrainRates([1, 1, 0.5, 0.5, 0.5, 0.25, 0.25]);
    grainRateResetFunc.value;
    ~ohm.turnGridGreen(2, 2, bank: mainBank, page: page);
  }, bank: mainBank, page: page);

  // 0.5s all around:
  ~ohm.setGridFunc(3, 2, {
    ~connections.droner.setGrainRates([0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5]);
    grainRateResetFunc.value;
    ~ohm.turnGridGreen(3, 2, bank: mainBank, page: page);
  }, bank: mainBank, page: page);

  ////// knobs:

  // granulator mix:
  ~ohm.setLeftKnobFunc(0, 0, { | val |
    var crossfade = val.linlin(0, 127, -1.0, 1.0);
    ~connections.droner.granulator.setGranulatorCrossfade(crossfade);
  }, bank: mainBank, page: page);

  // low shelf:
  ~ohm.setLeftKnobFunc(1, 0, { | val |
    var eq = val.linlin(0, 127, -6.0, 6.0);
    ~connections.droner.eq.setLowGain(eq);
  }, bank: mainBank, page: page);

  // high shelf;
  ~ohm.setLeftKnobFunc(2, 0, { | val |
    var eq = val.linlin(0, 127, -6.0, 6.0);
    ~connections.droner.eq.setHighGain(eq);
  }, bank: mainBank, page: page);
};

freeDronerFuncs {
  // slider:
  ~ohm.setLeftSliderFunc(1, { }, bank: mainBank, page: page);

  //// buttons:
  ~ohm.turnGridOff(1, 1, bank: mainBank, page: page);
  ~ohm.turnGridOff(0, 2);
  ~ohm.turnGridOff(1, 2);
  ~ohm.turnGridOff(2, 2);
  ~ohm.turnGridOff(3, 2);
  ~ohm.setGridFunc(1, 1, { });
  ~ohm.setGridFunc(0, 2, { });
  ~ohm.setGridFunc(1, 2, { });
  ~ohm.setGridFunc(2, 2, { });
  ~ohm.setGridFunc(3, 2, { });

  //// knobs:
  ~ohm.setLeftKnobFunc(0, 0, { });
  ~ohm.setLeftKnobFunc(1, 0, { });
  ~ohm.setLeftKnobFunc(2, 0, { });
};

//////// Bassline:

//////// Trumpet Gran:

//////// Inlet:

//////// Chords:

//////// Microphone:



