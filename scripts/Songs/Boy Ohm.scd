/*
Tuesday, July 25th
Boy Ohm.scd
prm
*/

var page = 'boy';

var leftKnobsBankKind = 'controlBank';

var activeLeftBank = leftBank1;
var leftBank1 = 0;
var leftBank2 = 1;
var controlBank = 2;

var mainGridBank = 0;

~ohm.makePage(page);

~ohm.addLeftButtonsBanks(1, page: page);
~ohm.addLeftSlidersBanks(1, page: page);
~ohm.addLeftKnobsBanks(2, page: page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);

////////////////////////////////
/////// Bank Switch: //////////
//////////////////////////////

~ohm.setCrossfaderButtonMonitorFunc(0, {
  if( activeLeftBank == leftBank1,
    { ~ohm.turnCrossfaderButtonBlue(0) }, { ~ohm.turnCrossfaderButtonMagenta(0) });
}, mainGridBank, page);
~ohm.setCrossfaderButtonFunc(0, {
  if( activeLeftBank == leftBank1,
    { activeLeftBank = leftBank2 }, { activeLeftBank = leftBank1 });
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  if( leftKnobsBankKind == 'mainBank', { ~ohm.setActiveLeftKnobsBank(activeLeftBank) });
}, bank: mainGridBank, page: page);

////////////////////////////////
///////// Grid: ///////////////
//////////////////////////////

//////// bank type change:
~ohm.setGridMonitorFunc(0, 7, {
  if( leftKnobsBankKind == 'controlBank',
    { ~ohm.turnGridCyan(0, 7); },
    { ~ohm.turnGridMagenta(0, 7); }
  );
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 7, {
  if( leftKnobsBankKind == 'controlBank',
    {
      ~ohm.setActiveLeftKnobsBank(activeLeftBank);
      leftKnobsBankKind = 'mainBank';
    },
    {
      ~ohm.setActiveLeftKnobsBank(controlBank);
      leftKnobsBankKind = 'controlBank';
    }
  );
}, bank: mainGridBank, page: page);


//////// transport controls:
~ohm.setGridMonitorFunc(0, 0, {
  if( ~theBoy.introIsPlaying,
    { ~ohm.turnGridGreen(0, 0) }, { ~ohm.turnGridMagenta(0, 0) });
}, mainGridBank, page);
~ohm.setGridMonitorFunc(1, 0, {
  if( ~theBoy.section1IsPlaying,
    { ~ohm.turnGridGreen(1, 0) }, { ~ohm.turnGridMagenta(1, 0) });
}, mainGridBank, page);
~ohm.setGridMonitorFunc(2, 0, {
  if( ~theBoy.lullIsPlaying,
    { ~ohm.turnGridGreen(2, 0) }, { ~ohm.turnGridMagenta(2, 0) });
}, mainGridBank, page);
~ohm.setGridMonitorFunc(3, 0, {
  if( ~theBoy.section2IsPlaying,
    { ~ohm.turnGridGreen(3, 0) }, { ~ohm.turnGridMagenta(3, 0) });
}, mainGridBank, page);
~ohm.setGridMonitorFunc(4, 0, {
  if( ~theBoy.section3IsPlaying,
    { ~ohm.turnGridGreen(4, 0) }, { ~ohm.turnGridMagenta(4, 0) });
}, mainGridBank, page);
~ohm.setGridMonitorFunc(5, 0, {
  if( ~theBoy.outroIsPlaying,
    { ~ohm.turnGridGreen(5, 0) }, { ~ohm.turnGridMagenta(5, 0) });
}, mainGridBank, page);

//////// sections:
4.do({ | i | ~ohm.turnGridYellow(i, 1, bank: mainGridBank, page: page); });
~ohm.setGridFunc(0, 1, { ~theBoy.playSection1;},
  bank: mainGridBank, page: page);
~ohm.setGridFunc(1, 1, { ~theBoy.loopSection1 },
  bank: mainGridBank, page: page);
~ohm.setGridFunc(2, 1, { ~theBoy.playSection2 },
  bank: mainGridBank, page: page);
~ohm.setGridFunc(3, 1, { ~theBoy.playSection3 },
  bank: mainGridBank, page: page);

/////// main bell:
~ohm.setGridMonitorFunc(0, 2, {
  if( ~theBoy.mainBell.isLoaded,
    {~ohm.turnGridYellow(0, 2); }, { ~ohm.turnGridRed(0, 2) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 2, {
  if( ~theBoy.mainBell.isLoaded, {
    ~theBoy.mainBell.free;
    ~ohm.setGridMonitorFunc(1, 2, {});
    ~ohm.setGridFunc(1, 2, {});
    ~ohm.turnGridOff(1, 2);
  });
}, bank: mainGridBank, page: page);
// play/stop:
~ohm.setGridMonitorFunc(1, 2, {
  if( ~theBoy.mainBell.mainBellIsPlaying,
    { ~ohm.turnGridGreen(1, 2); }, { ~ohm.turnGridRed(1, 2); });
}, mainGridBank, page);
~ohm.setGridFunc(1, 2, {
  if( ~theBoy.mainBell.mainBellIsPlaying,
    { ~theBoy.clock.playNextBar({~theBoy.mainBell.stopMainBell}); },
    { ~theBoy.clock.playNextBar({~theBoy.mainBell.playMainBell(~theBoy.clock)}); }
  );
}, bank: mainGridBank, page: page);

//////// row fuzz:
~ohm.setGridMonitorFunc(0, 3, {
  if( ~theBoy.rowFuzz.isLoaded,
    { ~ohm.turnGridMagenta(0, 3); }, { ~ohm.turnGridRed(0, 3) });
},bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 3, {
  if( ~theBoy.rowFuzz.isLoaded, {
    ~theBoy.rowFuzz.free;
    ~ohm.setGridMonitorFunc(1, 3, {});
    ~ohm.setGridFunc(1, 3, {});
    ~ohm.setGridMonitorFunc(2, 3, {});
    ~ohm.setGridFunc(2, 3, {});
    ~ohm.turnGridOff(1, 3);
    ~ohm.turnGridOff(2, 3);
  });
}, bank: mainGridBank, page: page);
// main:
~ohm.setGridMonitorFunc(1, 3, {
  if( ~theBoy.rowFuzz.mainRowFuzzIsPlaying,
    { ~ohm.turnGridGreen(1, 3) }, { ~ohm.turnGridRed(1, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(1, 3, {
  if( ~theBoy.rowFuzz.mainRowFuzzIsPlaying,
    { ~theBoy.clock.playNextBar({ ~theBoy.rowFuzz.stopMainRowFuzz; });},
    { ~theBoy.clock.playNextBar({ ~theBoy.rowFuzz.playMainRowFuzz(~theBoy.clock) }); }
  );
}, bank: mainGridBank, page: page);
// low:
~ohm.setGridMonitorFunc(2, 3, {
  if( ~theBoy.rowFuzz.lowRowFuzzIsPlaying,
    { ~ohm.turnGridGreen(2, 3) }, { ~ohm.turnGridRed(2, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(2, 3, {
  if( ~theBoy.rowFuzz.lowRowFuzzIsPlaying,
    { ~theBoy.clock.playNextBar({ ~theBoy.rowFuzz.stopLowRowFuzz; }); },
    { ~theBoy.clock.playNextBar({ ~theBoy.rowFuzz.playLowRowFuzz(~theBoy.clock) }); });
}, bank: mainGridBank, page: page);

//// piano:
~ohm.setGridMonitorFunc(0, 4, {
  if( ~theBoy.piano.isLoaded,
    { ~ohm.turnGridBlue(0, 4) }, { ~ohm.turnGridRed(0, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 4, {
  if( ~theBoy.piano.isLoaded, {
    ~theBoy.piano.free;
    ~ohm.setGridMonitorFunc(1, 4, {});
    ~ohm.setGridFunc(1, 4, {});
    ~ohm.setGridMonitorFunc(2, 4, {});
    ~ohm.setGridFunc(2, 4, {});
    ~ohm.setGridMonitorFunc(3, 4, {});
    ~ohm.setGridFunc(3, 4, {});
    ~ohm.turnGridOff(1, 3);
    ~ohm.turnGridOff(2, 4);
    ~ohm.turnGridOff(3, 4);
  });
}, bank: mainGridBank, page: page);
// section 1:
~ohm.setGridMonitorFunc(1, 4, {
  if( ~theBoy.piano.section1IsPlaying,
    { ~ohm.turnGridGreen(1, 4) }, { ~ohm.turnGridRed(1, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(1, 4, {
  if( ~theBoy.piano.section1IsPlaying,
    { ~theBoy.clock.playNextBar({ ~theBoy.piano.stopSection1 }); },
    { ~theBoy.clock.playNextBar({ ~theBoy.piano.playSection1(~theBoy.clock) }); }
  );
}, bank: mainGridBank, page: page);
// section 2:
~ohm.setGridMonitorFunc(2, 4, {
  if( ~theBoy.piano.section2IsPlaying,
    { ~ohm.turnGridGreen(2, 4) }, { ~ohm.turnGridRed(2, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(2, 4, {
  if( ~theBoy.piano.section2IsPlaying,
    { ~theBoy.clock.playNextBar({ ~theBoy.piano.stopSection2 }); },
    { ~theBoy.clock.playNextBar({ ~theBoy.piano.playSection2(~theBoy.clock) }); }
  );
}, bank: mainGridBank, page: page);
// section 3:
~ohm.setGridMonitorFunc(3, 4, {
  if( ~theBoy.piano.section3IsPlaying,
    { ~ohm.turnGridGreen(3, 4) }, { ~ohm.turnGridRed(3, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(3, 4, {
  if( ~theBoy.piano.section3IsPlaying,
    { ~theBoy.clock.playNextBar({ ~theBoy.piano.stopSection3 }); },
    { ~theBoy.clock.playNextBar({ ~theBoy.piano.playSection3(~theBoy.clock) }); }
  );
}, bank: mainGridBank, page: page);

//////// Noise Chords:
~ohm.setGridMonitorFunc(0, 5, {
  if( ~theBoy.noiseChords.isLoaded,
    { ~ohm.turnGridWhite(0, 5) }, { ~ohm.turnGridRed(0, 5) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 5, {
  if( ~theBoy.noiseChords.isLoaded, {
    ~theBoy.noiseChords.free;
    ~ohm.setGridMonitorFunc(1, 5, {});
    ~ohm.setGridFunc(1, 5, {});
    ~ohm.turnGridOff(1, 5);
    ~ohm.setGridMonitorFunc(2, 5, {});
    ~ohm.setGridFunc(2, 5, {});
    ~ohm.turnGridOff(2, 5);
    ~ohm.setGridMonitorFunc(3, 5, {});
    ~ohm.setGridFunc(3, 5, {});
    ~ohm.turnGridOff(3, 5);
  });
}, bank: mainGridBank, page: page);
// section 1:
~ohm.setGridMonitorFunc(1, 5, {
  if( ~theBoy.noiseChords.section1IsPlaying,
    { ~ohm.turnGridGreen(1, 5) }, { ~ohm.turnGridRed(1, 5) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(1, 5, {
  if( ~theBoy.noiseChords.section1IsPlaying,
    { ~theBoy.clock.playNextBar({ ~theBoy.noiseChords.stopSection1 }); },
    { ~theBoy.clock.playNextBar({ ~theBoy.noiseChords.playSection1(~theBoy.clock) }); }
  );
}, bank: mainGridBank, page: page);
// section 2:
~ohm.setGridMonitorFunc(2, 5, {
  if( ~theBoy.noiseChords.section2IsPlaying,
    { ~ohm.turnGridGreen(2, 5) }, { ~ohm.turnGridRed(2, 5) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(2, 5, {
  if( ~theBoy.noiseChords.section2IsPlaying,
    { ~theBoy.clock.playNextBar({ ~theBoy.noiseChords.stopSection2 }); },
    { ~theBoy.clock.playNextBar({ ~theBoy.noiseChords.playSection2(~theBoy.clock) }); }
  );
}, bank: mainGridBank, page: page);
// section 3:
~ohm.setGridMonitorFunc(3, 5, {
  if( ~theBoy.noiseChords.section3IsPlaying,
    { ~ohm.turnGridGreen(3, 5) }, { ~ohm.turnGridRed(3, 5) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(3, 5, {
  if( ~theBoy.noiseChords.section3IsPlaying,
    { ~theBoy.clock.playNextBar({ ~theBoy.noiseChords.stopSection3 }); },
    { ~theBoy.clock.playNextBar({ ~theBoy.noiseChords.playSection3(~theBoy.clock) }); }
  );
}, bank: mainGridBank, page: page);

//////// Bass:
~ohm.setGridMonitorFunc(0, 6, {
  if( ~theBoy.bassSection.isLoaded,
    { ~ohm.turnGridGreen(0, 6) }, { ~ohm.turnGridRed(0, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 6, {
  if( ~theBoy.bassSection.isLoaded, {
    ~theBoy.bassSection.free;
    ~ohm.setGridMonitorFunc(1, 6, {});
    ~ohm.setGridFunc(1, 6, {});
    ~ohm.turnGridOff(1, 6);
    ~ohm.setGridMonitorFunc(2, 6, {});
    ~ohm.setGridFunc(2, 6, {});
    ~ohm.turnGridOff(2, 6);
  });
}, bank: mainGridBank, page: page);
// section 2:
~ohm.setGridMonitorFunc(1, 6, {
  if( ~theBoy.bassSection.section2IsPlaying,
    { ~ohm.turnGridGreen(1, 6) }, { ~ohm.turnGridRed(1, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(1, 6, {
  if( ~theBoy.bassSection.section2IsPlaying,
    { ~theBoy.clock.playNextBar({ ~theBoy.bassSection.stopSection2; }) },
    { ~theBoy.clock.playNextBar({ ~theBoy.bassSection.playSection2(~theBoy.clock)}) },
  );
}, bank: mainGridBank, page: page);
// section 3:
~ohm.setGridMonitorFunc(2, 6, {
  if( ~theBoy.bassSection.section3IsPlaying,
    { ~ohm.turnGridGreen(2, 6) }, { ~ohm.turnGridRed(2, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(2, 6, {
  if( ~theBoy.bassSection.section3IsPlaying,
    { ~theBoy.clock.playNextBar({ ~theBoy.bassSection.stopSection3; }) },
    { ~theBoy.clock.playNextBar({ ~theBoy.bassSection.playSection3(~theBoy.clock)}) },
  );
}, bank: mainGridBank, page: page);

//////// Trumpet:
~ohm.setGridMonitorFunc(4, 2, {
  if( ~theBoy.trumpet.isLoaded,
    {~ohm.turnGridMagenta(4, 2)}, {~ohm.turnGridRed(4, 2)});
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 2, {
  if( ~theBoy.trumpet.isLoaded, {
    ~theBoy.trumpet.free;
    ~ohm.setGridMonitorFunc(5, 2, {});
    ~ohm.setGridFunc(5, 2, {});
    ~ohm.turnGridOff(5, 2);
    ~ohm.setGridMonitorFunc(6, 2, {});
    ~ohm.setGridFunc(6, 2, {});
    ~ohm.turnGridOff(6, 2);
    ~ohm.setGridMonitorFunc(7, 2, {});
    ~ohm.setGridFunc(7, 2, {});
    ~ohm.turnGridOff(7, 2);

  });
}, bank: mainGridBank, page: page);

// input mute:
~ohm.setGridMonitorFunc(5, 2, {
  if( ~theBoy.trumpetInput.isMuted,
    { ~ohm.turnGridRed(5, 2) }, { ~ohm.turnGridGreen(5, 2) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 2, { ~theBoy.trumpetInput.tglMute },
  bank: mainGridBank, page: page);

// pitch shift 8va:
~ohm.setGridMonitorFunc(6, 2, {
  if( ~theBoy.trumpet.nebula.pitchShiftAmount == 12,
    { ~ohm.turnGridYellow(6, 2) }, { ~ohm.turnGridCyan(6, 2) });
}, mainGridBank, page);
~ohm.setGridFunc(6, 2, {
  if( ~theBoy.trumpet.nebula.pitchShiftAmount == 12,
    { ~theBoy.trumpet.nebula.setPitchShiftAmount(0) },
    { ~theBoy.trumpet.nebula.setPitchShiftAmount(12) });
}, bank: mainGridBank, page: page);

// pitch shift 15ma:
~ohm.setGridMonitorFunc(7, 2, {
  if( ~theBoy.trumpet.nebula.pitchShiftAmount == 24,
    { ~ohm.turnGridWhite(7, 2) }, { ~ohm.turnGridCyan(7, 2) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(7, 2, {
  if( ~theBoy.trumpet.nebula.pitchShiftAmount == 24,
    { ~theBoy.trumpet.nebula.setPitchShiftAmount(0) },
    { ~theBoy.trumpet.nebula.setPitchShiftAmount(24) });
}, bank: mainGridBank, page: page);

//////// Low Bells:
~ohm.setGridMonitorFunc(4, 3, {
  if( ~theBoy.lowBells.isLoaded,
    { ~ohm.turnGridCyan(4, 3) }, { ~ohm.turnGridRed(4, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 3, {
  if( ~theBoy.lowBells.isLoaded, {
    ~theBoy.lowBells.free;
    ~ohm.setGridFunc(5, 3, {});
    ~ohm.setGridMonitorFunc(5, 3, {});
    ~ohm.turnGridOff(5, 3);
  });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(5, 3, {
  if( ~theBoy.lowBells.lowBellsIsPlaying,
    { ~ohm.turnGridGreen(5, 3) }, { ~ohm.turnGridRed(5, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 3, {
  if( ~theBoy.lowBells.lowBellsIsPlaying,
    { ~theBoy.lowBells.stopLowBells },
    { ~theBoy.lowBells.playLowBells(~theBoy.clock) });
}, bank: mainGridBank, page: page);

//////// End Synth:
~ohm.setGridMonitorFunc(4, 4, {
  if( ~theBoy.endSynth.isLoaded,
    { ~ohm.turnGridYellow(4, 4) }, { ~ohm.turnGridRed(4, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 4, {
  if( ~theBoy.endSynth.isLoaded, {
    ~theBoy.endSynth.free;
    ~ohm.setGridMonitorFunc(5, 4, {});
    ~ohm.setGridFunc(5, 4, {});
    ~ohm.turnGridOff(5, 4);
  });
}, bank: mainGridBank, page: page);
// end loop:
~ohm.setGridMonitorFunc(5, 4, {
  if( ~theBoy.endSynth.endLoopIsPlaying,
    { ~ohm.turnGridGreen(5, 4) }, { ~ohm.turnGridRed(5, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 4, {
  if( ~theBoy.endSynth.endLoopIsPlaying,
    { ~theBoy.endSynth.stopEndLoop },
    { ~theBoy.clock.playNextBar({ ~theBoy.endSynth.playEndLoop(~theBoy.clock) }); }
  );
}, bank: mainGridBank, page: page);



//////// Metronome:
~ohm.addPageLoadFunction(\metronome, {
  ~theBoy.clock.schedAbs(~theBoy.clock.beats.ceil, { | beat |
    //~falseSelf.clock.beatInBar.postln;
    ~theBoy.clock.playNextBar({
      ~ohm.turnControlButtonWhite(1, 0, page: page);
      { ~ohm.turnControlButtonOff(1, 0, page: page); }.defer(0.1);
    });
    ~ohm.turnControlButtonGreen(2, 0, page: page);
    { ~ohm.turnControlButtonOff(2, 0, page: page); }.defer(0.1);
    1;
  },page);
},page: page);

///////////////////////////////
///////// Left Side: /////////
/////////////////////////////


////////////////////////////////
///////// Mixer Banks: ////////
//////////////////////////////

////// Main Bell:
// volume:
~ohm.setLeftSliderFunc(0, { | val |
  ~theBoy.mixerA.setVol(0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// mute:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~theBoy.mixerA.isMuted(0),
    { ~ohm.turnLeftButtonRed(0) },
    { ~ohm.turnLeftButtonGreen(0) }
  );
}, bank: leftBank1, page: page);
~ohm.setLeftButtonFunc(0, {
  ~theBoy.mixerA.tglMute(0)
}, bank: leftBank1, page: page);
//// sends:
// verb:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~theBoy.mixerA.setSendVol(0, 0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// gran:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~theBoy.mixerA.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// modular:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~theBoy.mixerA.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);

//// Row Fuzz:
// volume:
~ohm.setLeftSliderFunc(1, { | val |
  ~theBoy.mixerA.setVol(1, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// mute:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~theBoy.mixerA.isMuted(1),
    { ~ohm.turnLeftButtonRed(1) },
    { ~ohm.turnLeftButtonGreen(1) }
  );
}, bank: leftBank1, page: page);
~ohm.setLeftButtonFunc(1, {
  ~theBoy.mixerA.tglMute(1)
}, bank: leftBank1, page: page);
//// sends:
// verb:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~theBoy.mixerA.setSendVol(1, 0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// gran:
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~theBoy.mixerA.setSendVol(1, 1, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// modular:
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~theBoy.mixerA.setSendVol(1, 2, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);

//// Piano:
// volume:
~ohm.setLeftSliderFunc(2, { | val |
  ~theBoy.mixerB.setVol(0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// mute:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~theBoy.mixerB.isMuted(0),
    { ~ohm.turnLeftButtonRed(2) },
    { ~ohm.turnLeftButtonGreen(2) }
  );
}, bank: leftBank1, page: page);
~ohm.setLeftButtonFunc(2, {
  ~theBoy.mixerB.tglMute(0)
}, bank: leftBank1, page: page);
//// sends:
// verb:
~ohm.setLeftKnobFunc(2, 2, { | val |
  ~theBoy.mixerB.setSendVol(0, 0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// gran:
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~theBoy.mixerB.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// modular:
~ohm.setLeftKnobFunc(2, 0, { | val |
  ~theBoy.mixerB.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);

//// Trumpet:
// volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~theBoy.mixerC.setVol(0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// mute:
~ohm.setLeftButtonMonitorFunc(3, {
  if( ~theBoy.mixerC.isMuted(0),
    { ~ohm.turnLeftButtonRed(3) },
    { ~ohm.turnLeftButtonGreen(3) }
  );
}, bank: leftBank1, page: page);
~ohm.setLeftButtonFunc(3, {
  ~theBoy.mixerC.tglMute(0)
}, bank: leftBank1, page: page);
//// sends:
// verb:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~theBoy.mixerC.setSendVol(0, 0, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// gran:
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~theBoy.mixerC.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);
// modular:
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~theBoy.mixerC.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, bank: leftBank1, page: page);

//// Low Bells:
// volume:
~ohm.setLeftSliderFunc(0, { | val |
  ~theBoy.mixerA.setVol(2, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// mute:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~theBoy.mixerA.isMuted(2),
    { ~ohm.turnLeftButtonRed(0) },
    { ~ohm.turnLeftButtonGreen(0) }
  );
}, bank: leftBank2, page: page);
~ohm.setLeftButtonFunc(0, {
  ~theBoy.mixerA.tglMute(2)
}, bank: leftBank2, page: page);
//// sends:
// verb:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~theBoy.mixerA.setSendVol(2, 0, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// gran:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~theBoy.mixerA.setSendVol(2, 1, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// modular:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~theBoy.mixerA.setSendVol(2, 2, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);

//// Noise Chords:
// volume:
~ohm.setLeftSliderFunc(1, { | val |
  ~theBoy.mixerC.setVol(1, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// mute:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~theBoy.mixerC.isMuted(1),
    { ~ohm.turnLeftButtonRed(1) },
    { ~ohm.turnLeftButtonGreen(1) }
  );
}, bank: leftBank2, page: page);
~ohm.setLeftButtonFunc(1, {
  ~theBoy.mixerC.tglMute(1)
}, bank: leftBank2, page: page);
//// sends:
// verb:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~theBoy.mixerC.setSendVol(1, 0, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// gran:
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~theBoy.mixerC.setSendVol(1, 1, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// modular:
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~theBoy.mixerC.setSendVol(1, 2, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);


//// Bass Section:
// volume:
~ohm.setLeftSliderFunc(2, { | val |
  ~theBoy.mixerB.setVol(1, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// mute:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~theBoy.mixerB.isMuted(1),
    { ~ohm.turnLeftButtonRed(2) },
    { ~ohm.turnLeftButtonGreen(2) }
  );
}, bank: leftBank2, page: page);
~ohm.setLeftButtonFunc(2, {
  ~theBoy.mixerB.tglMute(1)
}, bank: leftBank2, page: page);
//// sends:
// verb:
~ohm.setLeftKnobFunc(2, 2, { | val |
  ~theBoy.mixerB.setSendVol(1, 0, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// gran:
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~theBoy.mixerB.setSendVol(1, 1, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// modular:
~ohm.setLeftKnobFunc(2, 0, { | val |
  ~theBoy.mixerB.setSendVol(1, 2, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);

//// End Synth:
// volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~theBoy.mixerC.setVol(2, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// mute:
~ohm.setLeftButtonMonitorFunc(3, {
  if( ~theBoy.mixerC.isMuted(2),
    { ~ohm.turnLeftButtonRed(3) },
    { ~ohm.turnLeftButtonGreen(3) }
  );
}, bank: leftBank2, page: page);
~ohm.setLeftButtonFunc(3, {
  ~theBoy.mixerC.tglMute(2)
}, bank: leftBank2, page: page);
//// sends:
// verb:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~theBoy.mixerC.setSendVol(2, 0, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// gran:
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~theBoy.mixerC.setSendVol(2, 1, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);
// modular:
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~theBoy.mixerC.setSendVol(2, 2, val.ccdbfs(-70, 0));
}, bank: leftBank2, page: page);



////////////////////////////
//////// Control Bank /////
//////////////////////////

//// piano filter cutoff:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var cutoff = val.linexp(0, 127, 150, 5500);
  ~theBoy.piano.eq.setLowPassCutoff(cutoff);
}, bank: controlBank, page: page);

//// low bells:
// granulator mix:
~ohm.setLeftKnobFunc(0, 1, { | val |
  var mix = val.linlin(0, 127, -1, 1);
  ~theBoy.lowBells.granulator.setGranulatorCrossfade(mix);
}, bank: controlBank, page: page);


