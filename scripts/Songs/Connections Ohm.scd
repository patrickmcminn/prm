/*
Wednesday, July 8th 2015
Connections Ohm.scd
prm
*/

var page = 'connections';

// banks:
var mainBank = 0;
var auxBank = 1;

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

~ohm.makePage(page);

~ohm.addLeftSlidersBanks(1, page: page);

// page load function:
~ohm.setPageLoadFunction({
  ~ohm.storageDict[\loadGlobalSubmixerFunc].value;
}, page);

// bring base on over:
~ohm.turnControlButtonPurple(2, 0, bank: mainBank, page: page);
~ohm.setControlButtonFunc(2, 0, { ~base.setPage(page); }, bank: mainBank, page: page);


// bank change:
~ohm.turnCrossfaderButtonWhite(0, bank: mainBank, page: page);
~ohm.setCrossfaderButtonFunc(0, r {
  loop {
    ~ohm.setActiveLeftSlidersBank(auxBank, page: page);
    ~ohm.turnCrossfaderButtonPurple(0).yield;

    ~ohm.setActiveLeftSlidersBank(mainBank, page: page);
    ~ohm.turnCrossfaderButtonWhite(0).yield;
  };
}, bank: mainBank, page: page);



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

freeAirSputtersFuncs = {
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

loadDronerFuncs =  {

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

freeDronerFuncs =  {
  // slider:
  ~ohm.setLeftSliderFunc(1, { }, bank: mainBank, page: page);

  //// buttons:
  ~ohm.turnGridOff(1, 1, bank: mainBank, page: page);
  ~ohm.turnGridOff(0, 2, bank: mainBank);
  ~ohm.turnGridOff(1, 2, bank: mainBank);
  ~ohm.turnGridOff(2, 2, bank: mainBank);
  ~ohm.turnGridOff(3, 2, bank: mainBank);
  ~ohm.setGridFunc(1, 1, { }, bank: mainBank);
  ~ohm.setGridFunc(0, 2, { }, bank: mainBank);
  ~ohm.setGridFunc(1, 2, { }, bank: mainBank);
  ~ohm.setGridFunc(2, 2, { }, bank: mainBank);
  ~ohm.setGridFunc(3, 2, { }, bank: mainBank);

  //// knobs:
  ~ohm.setLeftKnobFunc(0, 0, { });
  ~ohm.setLeftKnobFunc(1, 0, { });
  ~ohm.setLeftKnobFunc(2, 0, { });
};


//////// Note Record:
noteRecordColorArray = ['blue', 'cyan', 'purple', 'white', 'green', 'yellow'];
cascadeRecordColorArray = ['yellow', 'blue', 'cyan', 'purple', 'white'];
chordRecordColorArray = ['purple', 'green', 'cyan', 'purple', 'white', 'green', 'yellow'];

//// bassline record:
~ohm.turnGridBlue(0, 3, bank: mainBank, page: page);
~ohm.setGridFunc(0, 3, {
  ~connections.noteRecord.basslineRecordRoutine.next;
  ~ohm.turnGridColor(0, 3, noteRecordColorArray[~connections.noteRecord.basslineRecordNum]);
}, bank: mainBank, page: page);
// resets:
~ohm.turnGridRed(0, 4, bank: mainBank, page: page);
~ohm.setGridFunc(0, 4, {
  ~connections.noteRecord.resetBasslineRecordRoutine;
  ~ohm.turnGridColor(0, 3, noteRecordColorArray[0]);
}, bank: mainBank, page: page);

//// cascade record:
~ohm.turnGridYellow(1, 3, bank: mainBank, page: page);
~ohm.setGridFunc(1, 3, {
  ~connections.noteRecord.cascadeRecordRoutine.next;
  ~ohm.turnGridColor(1, 3, cascadeRecordColorArray[~connections.noteRecord.cascadeRecordNum]);
}, bank: mainBank, page: page);
// reset:
~ohm.turnGridRed(1, 4, bank: mainBank, page: page);
~ohm.setGridFunc(1, 4, {
  ~connections.noteRecord.resetCascadeRecordRoutine;
  ~ohm.turnGridColor(1, 3, cascadeRecordColorArray[0]);
}, bank: mainBank, page: page);


// chord record:
~ohm.turnGridPurple(2, 3, bank: mainBank, page: page);
~ohm.setGridFunc(2, 3, {
  ~connections.noteRecord.chordRecordRoutine.next;
  ~ohm.turnGridColor(2, 3, chordRecordColorArray[~connections.noteRecord.chordRecordNum]);
}, bank: mainBank, page: page);
// reset:
~ohm.turnGridRed(2, 4, bank: mainBank, page: page);
~ohm.setGridFunc(2, 4, {
  ~connections.noteRecord.resetChordRecordRoutine;
  ~ohm.turnGridColor(2, 3, chordRecordColorArray[0]);
}, bank: mainBank, page: page);



//////// Bassline:

// On/Off:
~ohm.turnGridBlue(0, 5, bank: mainBank, page: page);
~ohm.setGridFunc(0, 5, {
  if( basslinesLoaded == false,
    {
      r{
        ~connections.toggleLoadBass;
        while({ try { ~connections.bass.isLoaded } != true }, { 0.001.wait; });
        loadBasslineFuncs.value;
        ~ohm.turnGridGreen(0, 5);
        basslinesLoaded = true;
      }.play;
    },
    {
      ~connections.toggleLoadBass;
      freeBasslineFuncs.value;
      ~ohm.turnGridRed(0, 5);
      basslinesLoaded = false;
  });
}, bank: mainBank, page: page);


loadBasslineFuncs = {
  // Fader:
  ~ohm.setLeftSliderFunc(2, { | val |
    ~connections.mixerB.setVol(0, val.ccdbfs);
  }, bank: mainBank, page: page);

  //// knobs:

  //granular mix:
  ~ohm.setLeftKnobFunc(0, 1, { | val |
    var crossfade = val.linlin(0, 127, -1.0, 1.0);
    ~connections.bass.granulator.setGranulatorCrossfade(crossfade);
  }, bank: mainBank, page: page);


  //// buttons:

  // bassline 1:
  ~ohm.turnGridCyan(1, 5);
  ~ohm.setGridFunc(1, 5, {
    if( ~connections.bass.bassline1IsPlaying == false,
      { ~connections.bass.playBassline1(~connections.clock); ~ohm.turnGridGreen(1, 5); },
      { ~connections.bass.stopBassline1; ~ohm.turnGridRed(1, 5); }
    );
  }, bank: mainBank, page: page);

  // bassline 2:
  ~ohm.turnGridPurple(2, 5);
  ~ohm.setGridFunc(2, 5, {
    if( ~connections.bass.bassline2IsPlaying == false,
      { ~connections.bass.playBassline2(~connections.clock); ~ohm.turnGridGreen(2, 5); },
      { ~connections.bass.stopBassline2; ~ohm.turnGridRed(2, 5); }
    );
  }, bank: mainBank, page: page);


};

freeBasslineFuncs =  {
  ~ohm.setLeftSliderFunc(2, { }, bank: mainBank, page: page);
  ~ohm.setLeftKnobFunc(0, 1, { }, bank: mainBank, page: page);
  ~ohm.setGridFunc(1, 5, { }, bank: mainBank, page: page);
  ~ohm.setGridFunc(2, 5, { }, bank: mainBank, page: page);
  ~ohm.turnGridOff(1, 5, bank: mainBank);
  ~ohm.turnGridOff(2, 5, bank: mainBank);
};

//////// Trumpet Gran:

// On/Off:
~ohm.turnGridYellow(0, 6, bank: mainBank, page: page);
~ohm.setGridFunc(0, 6, {
  if( trumpetGranLoaded == false,
    { r {
      ~connections.toggleLoadTrumpetGran;
      while({ try { ~connections.trumpetGran.isLoaded } != true }, { 0.001.wait; });
      loadTrumpetGranFuncs.value;
      ~ohm.turnGreen(0, 6);
      trumpetGranLoaded = true;
    }.play; },
    {
      freeTrumpetGranFuncs.value;
      ~ohm.turnGridRed(0, 6);
      trumpetGranLoaded = false;
  });
}, bank: mainBank, page: page);


loadTrumpetGranFuncs = {
  //// knobs:
  // vol:
  ~ohm.setLeftKnobFunc(0, 2, { | val |
    ~connections.mixerA.setVol(2, val.ccdbfs(-70, 0));
  }, bank: mainBank, page: page);
  // feedback:
  ~ohm.setLeftKnobFunc(1, 2, { | val |
    var feedback = val.linlin(0, 127, 0, 1);
    ~connections.trumpetGran.granulator.setFeedback(feedback);
  }, bank: mainBank, page: page);
  // high gain:
  ~ohm.setLeftKnobFunc(2, 2, { | val |
    var gain = val.linlin(0, 127, -6, 9);
    ~connections.trumpetGran.eq.setHighGain(gain);
  }, bank: mainBank, page: page);

  // button:
  ~ohm.turnGridGreen(1, 6, bank: mainBank, page: page);
  ~ohm.setGridFunc(1, 6, {
    ~connections.trumpetGran.input.tglMute;
    if( ~connections.trumpetGran.input.isMuted,
      { ~ohm.turnGridRed(1, 6); },
      { ~ohm.turnGridGreen(1, 6); });
  }, bank: mainBank, page: page);

};

freeTrumpetGranFuncs = {
  ~ohm.setLeftKnobFunc(0, 2, { }, bank: mainBank);
  ~ohm.setLeftKnobFunc(1, 2, { }, bank: mainBank);
  ~ohm.setLeftKnobFunc(2, 2, {}, bank: mainBank);
  ~ohm.setGridFunc(1, 6, {}, bank: mainBank);
  ~ohm.turnGridOff(1, 6, bank: mainBank);
};

//////// Inlet:
// On/Off:
~ohm.turnGridWhite(5, 0, bank: mainBank, page: page);
~ohm.setGridFunc(5, 0, {
  if( inletLoaded == false,
    { r{
      ~connections.toggleLoadInlet;
      while({ try { ~connections.inlet.isLoaded } != true }, { 0.001.wait; });
      loadInletFuncs.value;
      ~ohm.turnGridGreen(5, 0);
      inletLoaded = true;
    }.play; },
    {
      ~connections.toggleLoadInlet;
      freeInletFuncs.value;
      ~ohm.turnGridRed(5, 0);
      inletLoaded = false;
  });
}, bank: mainBank, page: page);


// load:
loadInletFuncs = {

  ////// inlet:
  //granulator:
  ~ohm.setLeftKnobFunc(3, 1, { | val |
    var crossfade = val.linlin(0, 127, -1, 1);
    ~connections.inlet.granulator.setGranulatorCrossfade(crossfade);
  }, bank: mainBank, page: page);

  ////// attack randomizer:
  // slider:
  ~ohm.setLeftSliderFunc(0, { | val |
    ~connections.inlet.attackRandomizer.mixer.setVol(val.ccdbfs(-70, 0));
  }, auxBank, page);

  // button:
  ~ohm.turnGridBlue(0, 7, bank: mainBank, page: page);
  ~ohm.setGridFunc(0, 7, {
    if( ~connections.inlet.attackRandomizer.isPlaying,
      { ~connections.inlet.attackRandomizer.stopSequences; ~ohm.turnGridRed(0, 7); },
      { ~connections.inlet.attackRandomizer.playSequences(~connections.clock); ~ohm.turnGridGreen(0, 7); });
  }, bank: mainBank, page: page);

  ////// cascade:
  // slider:
  ~ohm.setLeftSliderFunc(1, { | val |
    ~connections.inlet.cascade.mixer.setVol(val.ccdbfs(-70, 0));
  }, auxBank, page);

  // button:
  ~ohm.turnGridPurple(1, 7, bank: mainBank, page: page);
  ~ohm.setGridFunc(1, 7, {
    if( ~connections.inlet.cascade.isPlaying,
      { ~connections.inlet.cascade.stopSequences; ~ohm.turnGridRed(1, 7); },
      { ~connections.inlet.cascade.playSequences(~connections.clock); ~ohm.turnGridGreen(1, 7); });
  }, bank: mainBank, page: page);

};

// free:

freeInletFuncs = {
  ~ohm.setLeftKnobFunc(3, 1, {});
  ~ohm.setLeftSliderFunc(0, { }, bank: auxBank, page: page);
  ~ohm.setLeftSliderFunc(1, { }, bank: auxBank, page: page);
  ~ohm.setGridFunc(0, 7, { }, bank: mainBank);
  ~ohm.setGridFunc(1, 7, { }, bank: mainBank);
  ~ohm.turnGridOff(0, 7, bank: mainBank);
  ~ohm.turnGridOff(1, 7, bank: mainBank);
};


//////// Chords:

// On/Off:
~ohm.turnGridCyan(5, 1, bank: mainBank, page: page);
~ohm.setGridFunc(5, 1, {
  if( chordsLoaded == false,
    { r{
      ~connections.toggleLoadChords;
      while({ try { ~connections.chords.isLoaded } != true }, { 0.001.wait; });
      loadChordFuncs.value;
      ~ohm.turnGridGreen(5, 1);
      chordsLoaded = true;
    }.play; },
    {
      ~connections.toggleLoadChords;
      freeChordFuncs.value;
      ~ohm.turnGridRed(5, 1);
      chordsLoaded = false;
  });
}, bank: mainBank, page: page);


//// funcs:

// load:

loadChordFuncs = {
  //// slider:
  ~ohm.setLeftSliderFunc(2, { | val |
    ~connections.mixerC.setVol(0, val.ccdbfs);
  }, bank: auxBank, page: page);

  //// buttons:

  // chord sum:
  ~ohm.turnGridPurple(5, 2, bank: mainBank, page: page);
  ~ohm.setGridFunc(5, 2, {
    ~connections.chords.recordChords;
    ~ohm.turnGridGreen(5, 2);
  }, bank: mainBank, page: page);

  // random chords:
  ~ohm.turnGridYellow(6, 2, bank: mainBank, page: page);
  ~ohm.setGridFunc(6, 2, {
    if( ~connections.chords.randomChordsIsPlaying,
      { ~connections.chords.stopRandomChords; ~ohm.turnGridRed(6, 2); },
      { ~connections.chords.playRandomChords(~connections.clock); ~ohm.turnGridGreen(6, 2); });
  }, bank: mainBank, page: page);

  // chord progression:
  ~ohm.turnGridBlue(7, 2, bank: mainBank, page: page);
  ~ohm.setGridFunc(7, 2, {
    if( ~connections.chords.chordProgressionIsPlaying,
      { ~connections.chords.stopChordProgression; ~ohm.turnGridRed(7, 2); },
      { ~connections.chords.playChordProgression(~connections.clock); ~ohm.turnGridGreen(7, 2); });
  }, bank: mainBank, page: page);

};

// free:

freeChordFuncs = {
  ~ohm.setLeftSliderFunc(2, { }, bank: auxBank);
  ~ohm.setGridFunc(5, 2, { }, bank: mainBank);
  ~ohm.setGridFunc(6, 2, { }, bank: mainBank);
  ~ohm.setGridFunc(7, 2, { }, bank: mainBank);
  ~ohm.turnGridOff(5, 2, bank: mainBank);
  ~ohm.turnGridOff(6, 2, bank: mainBank);
  ~ohm.turnGridOff(7, 2, bank: mainBank);
};

//////// Microphone:

// volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~prm.microphone.setVol(val.ccdbfs(-70, 0));
}, bank: mainBank, page: page);

// reverb send:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~prm.microphone.setSendVol(0, val.ccdbfs);
}, bank: mainBank, page: page);




