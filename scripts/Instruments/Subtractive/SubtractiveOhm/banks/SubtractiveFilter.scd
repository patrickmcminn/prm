/*
Sunday, October 2nd 2016
SubtractiveFilter.scd
prm
*/

var page = 'subtractive';

var mainKnobBank = 0;
var osc1KnobBank = 1;
var osc2KnobBank = 2;
var filterKnobBank = 3;
var vcaKnobBank = 4;
var lfo1KnobBank = 5;
var lfo2KnobBank = 6;
var presetsKnobBank = 7;

var mainGridBank = 0;
var osc1GridBank = 1;
var osc2GridBank = 2;
var filterGridBank = 3;
var vcaGridBank = 4;
var lfo1GridBank = 5;
var lfo2GridBank = 6;
var presetsGridBank = 7;
var keyboardGridBank = 8;

var mainSliderBank = 0;
var osc1SliderBank = 1;
var osc2SliderBank = 2;
var filterSliderBank = 3;
var vcaSliderBank = 4;
var lfo1SliderBank = 5;
var lfo2SliderBank = 6;
var presetsSliderBank = 7;

var mainCrossfaderBank = 0;
var osc1CrossfaderBank = 1;
var osc2CrossfaderBank = 2;
var filterCrossfaderBank = 3;
var vcaCrossfaderBank = 4;
var lfo1CrossfaderBank = 5;
var lfo2CrossfaderBank = 6;
var presetsCrossfaderBank = 7;

var mainButtonBank = 0;
var osc1ButtonBank = 1;
var osc2ButtonBank = 2;
var filterButtonBank = 3;
var vcaButtonBank = 4;
var lfo1ButtonBank = 5;
var lfo2ButtonBank = 6;
var presetsButtonBank = 7;

var cutoffLFO1Invert = false;
var resLFO1Invert = false;
var cutoffLFO2Invert = false;
var resLFO2Invert = false;

// filter:
~ohm.setGridFunc(0, 7, {
  ~ohm.setActiveGridBank(mainGridBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(mainButtonBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank);
  ~ohm.setActiveLeftSlidersBank(mainSliderBank);
  ~ohm.setActiveCrossfaderBank(mainCrossfaderBank);
}, bank: filterGridBank, page: page);
~ohm.setGridFunc(1, 7, {
  ~ohm.setActiveGridBank(osc1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc1ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc1SliderBank);
  ~ohm.setActiveCrossfaderBank(osc1CrossfaderBank);
}, bank: filterGridBank, page: page);
~ohm.setGridFunc(2, 7, {
  ~ohm.setActiveGridBank(osc2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc2ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc2SliderBank);
  ~ohm.setActiveCrossfaderBank(osc2CrossfaderBank);
}, bank: filterGridBank, page: page);
~ohm.setGridFunc(3, 7, {
  ~ohm.setActiveGridBank(filterGridBank, page);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(filterButtonBank);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank);
  ~ohm.setActiveLeftSlidersBank(filterSliderBank);
  ~ohm.setActiveCrossfaderBank(filterCrossfaderBank);
}, bank: filterGridBank, page: page);
~ohm.setGridFunc(4, 7, {
  ~ohm.setActiveGridBank(vcaGridBank, page);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(vcaButtonBank);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank);
  ~ohm.setActiveLeftSlidersBank(vcaSliderBank);
  ~ohm.setActiveCrossfaderBank(vcaCrossfaderBank);
}, bank: filterGridBank, page: page);
~ohm.setGridFunc(5, 7, {
  ~ohm.setActiveGridBank(lfo1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo1ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo1SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo1CrossfaderBank);
}, bank: filterGridBank, page: page);
~ohm.setGridFunc(6, 7, {
  ~ohm.setActiveGridBank(lfo2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo2ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo2SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo2CrossfaderBank);
}, bank: filterGridBank, page: page);
~ohm.setGridFunc(7, 7, {
  ~ohm.setActiveGridBank(presetsGridBank, page);
  ~ohm.setActiveLeftKnobsBank(presetsKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(presetsButtonBank);
  ~ohm.setActiveLeftSlidersBank(presetsSliderBank);
  ~ohm.setActiveCrossfaderBank(presetsCrossfaderBank);
}, bank: filterGridBank, page: page);

//////// Filter:

//// Sliders Bank:

// Filter Env Attack Time:
~ohm.setLeftSliderFunc(0, { | val |
  var attack = val.linlin(0, 127, 0, 7);
  ~prm.subtractive.setFilterEnvAttackTime(attack);
}, filterSliderBank, page);

// Filter Env Decay Time:
~ohm.setLeftSliderFunc(1, { | val |
  var decay = val.linlin(0, 126, 0, 7);
  ~prm.subtractive.setFilterEnvDecayTime(decay);
}, filterSliderBank, page);

// Filter Env Release Time:
~ohm.setLeftSliderFunc(2, { | val |
  var release = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setFilterEnvReleaseTime(release);
}, filterSliderBank, page);

// Subtractive Vol:
~ohm.setLeftSliderFunc(3, { | val |
  ~prm.subtractive.mixer.setVol(val.ccdbfs(-70, 0));
}, filterSliderBank, page);

//// Knob Bank:

// Filter Cutoff:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var cutoff = val.linexp(0, 127, 50, 20000);
  ~prm.subtractive.setFilterCutoff(cutoff);
}, filterKnobBank, page);

// Filter Res:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var res = val.linlin(0, 127, 0, 2.5);
  ~prm.subtractive.setFilterRes(res);
}, filterKnobBank, page);

// Filter Drive:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var drive = val.linlin(0, 127, 1, 100);
  ~prm.subtractive.setFilterDrive(drive);
}, filterKnobBank, page);


// Filter Type:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var type = val.linlin(0, 127, 0, 2);
  ~prm.subtractive.setFilterType(type);
}, filterKnobBank, page);


// Cutoff LFO 1:
~ohm.setLeftKnobFunc(0, 1, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.03125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  if( cutoffLFO1Invert == false,
    {
      ~prm.subtractive.setFilterCutoffLFO1BottomRatio(amountLow);
      ~prm.subtractive.setFilterCutoffLFO1TopRatio(amountHigh);
    },
    {
      ~prm.subtractive.setFilterCutoffLFO1BottomRatio(amountHigh);
      ~prm.subtractive.setFilterCutoffLFO1TopRatio(amountLow);
  });
}, filterKnobBank, page);

// Res LFO 1:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var amountLow = val.linlin(0, 127, 0, -2);
  var amountHigh = val.linlin(0, 127, 0, 2);
  if( resLFO1Invert == false,
    {
      ~prm.subtractive.setFilterResLFO1Bottom(amountLow);
      ~prm.subtractive.setFilterResLFO1Top(amountHigh);
    },
    {
      ~prm.subtractive.setFilterResLFO1Bottom(amountHigh);
      ~prm.subtractive.setFilterResLFO1Top(amountLow);
  });
}, filterKnobBank, page);

// Cutoff LFO 2:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.03125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  if( cutoffLFO2Invert == false,
    {
      ~prm.subtractive.setFilterCutoffLFO2BottomRatio(amountLow);
      ~prm.subtractive.setFilterCutoffLFO2TopRatio(amountHigh);
    },
    {
      ~prm.subtractive.setFilterCutoffLFO2BottomRatio(amountHigh);
      ~prm.subtractive.setFilterCutoffLFO2TopRatio(amountLow);
  });
}, filterKnobBank, page);

// Res LFO 2:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var amountLow = val.linlin(0, 127, 0, -2);
  var amountHigh = val.linlin(0, 127, 0, 2);
  if( resLFO2Invert == false,
    {
      ~prm.subtractive.setFilterResLFO2Bottom(amountLow);
      ~prm.subtractive.setFilterResLFO2Top(amountHigh);
    },
    {
      ~prm.subtractive.setFilterResLFO2Bottom(amountHigh);
      ~prm.subtractive.setFilterResLFO2Top(amountLow);
  });
}, filterKnobBank, page);

// Filter Env Attack Ratio:
~ohm.setLeftKnobFunc(0, 2, {  | val |
  var attackLow = val.linlin(0, 63, 0.0001, 1);
  var attackHigh = val.linlin(64, 127, 1, 32);
  if( val <= 63,
    { ~prm.subtractive.setFilterEnvAttackRatio(attackLow); },
    { ~prm.subtractive.setFilterEnvAttackRatio(attackHigh); }
  );
}, filterKnobBank, page);

// Filter Env Peak Ratio:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var peakLow = val.linlin(0, 63, 0.0001, 1);
  var peakHigh = val.linlin(64, 127, 1, 32);
  if ( val <= 63,
    { ~prm.subtractive.setFilterEnvPeakRatio(peakLow); },
    { ~prm.subtractive.setFilterEnvPeakRatio(peakHigh); }
  );
}, filterKnobBank, page);

// Filter Env Sustain Ratio:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var susLow = val.linlin(0, 63, 0.0001, 1);
  var susHigh = val.linlin(64, 127, 1, 32);
  if ( val <= 63,
    { ~prm.subtractive.setFilterEnvSustainRatio(susLow); },
    { ~prm.subtractive.setFilterEnvSustainRatio(susHigh); }
  );
}, filterKnobBank, page);

// Filter Env Release Ratio;
~ohm.setLeftKnobFunc(3, 2, { | val |
  var releaseLow = val.linlin(0, 63, 0.0001, 1);
  var releaseHigh = val.linlin(64, 127, 1, 32);
  if ( val <= 63,
    { ~prm.subtractive.setFilterEnvReleaseRatio(releaseLow); },
    { ~prm.subtractive.setFilterEnvReleaseRatio(releaseHigh); }
  );
}, filterKnobBank, page);


////// Grid:


///// Left Buttons:

// Filter Loop:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~prm.subtractive.filterEnvLoop == 1,
    { ~ohm.turnLeftButtonGreen(0); }, { ~ohm.turnLeftButtonRed(0); });
}, filterButtonBank, page);
/*
~ohm.setGridMonitorFunc(0, 0, {
  if( ~prm.subtractive.filterEnvLoop == 1,
    { ~ohm.turnGridGreen(0, 0, filterGridBank, page); },
    { ~ohm.turnGridRed(0, 0, filterGridBank, page); }
  );
}, filterGridBank, page);
*/
~ohm.setLeftButtonFunc(0, {
  if( ~prm.subtractive.filterEnvLoop == 0,
    { ~prm.subtractive.setFilterEnvLoop(true); }, { ~prm.subtractive.setFilterEnvLoop(false); });
}, filterButtonBank, page);

/*
~ohm.setGridFunc(0, 0, {
  if( ~prm.subtractive.filterEnvLoop == 0,
    { ~prm.subtractive.setFilterEnvLoop(true); }, { ~prm.subtractive.setFilterEnvLoop(false); });
}, filterGridBank, page);
*/

// Filter Env Ratio Reset:
~ohm.turnLeftButtonWhite(1, filterButtonBank, page);
~ohm.setLeftButtonFunc(1, {
  ~prm.subtractive.setFilterEnvAttackRatio(1);
  ~prm.subtractive.setFilterEnvPeakRatio(1);
  ~prm.subtractive.setFilterEnvSustainRatio(1);
  ~prm.subtractive.setFilterEnvReleaseRatio(1);
}, filterButtonBank);

/*
~ohm.turnGridWhite(0, 1, filterGridBank, page);
~ohm.setGridFunc(0, 1, {
  ~prm.subtractive.setFilterEnvAttackRatio(1);
  ~prm.subtractive.setFilterEnvPeakRatio(1);
  ~prm.subtractive.setFilterEnvSustainRatio(1);
  ~prm.subtractive.setFilterEnvReleaseRatio(1);
}, filterGridBank, page);
*/


// Filter Type:
~ohm.setGridMonitorFunc(0, 6, {
  3.do({ | i | ~ohm.turnGridCyan(i, 6, filterGridBank, page); });
  switch(~prm.subtractive.filterType,
    { 0 }, { ~ohm.turnGridGreen(0, 6, filterGridBank, page); },
    { 1 }, { ~ohm.turnGridGreen(1, 6, filterGridBank, page); },
    { 2 }, { ~ohm.turnGridGreen(2, 6, filterGridBank, page); }
  );
}, filterGridBank, page);

~ohm.setGridFunc(0, 6, { ~prm.subtractive.setFilterType('lowPass') }, bank: filterGridBank, page: page);
~ohm.setGridFunc(1, 6, { ~prm.subtractive.setFilterType('highPass') }, bank: filterGridBank, page: page);
~ohm.setGridFunc(2, 6, { ~prm.subtractive.setFilterType('bandPass') }, bank: filterGridBank, page: page);

//////// because this grid bank is a little anemic looking, might as well duplicate several controls:
//// Osc 1 Waveform:

// sine:
~ohm.setGridMonitorFunc(0, 0, {
  if( ~prm.subtractive.osc1Waveform == 0,
    { ~ohm.turnGridGreen(0, 0, filterGridBank, page); }, { ~ohm.turnGridWhite(0, 0, filterGridBank, page); }
  );
}, filterGridBank, page);
~ohm.setGridFunc(0, 0, { ~prm.subtractive.setOsc1Waveform('sine'); }, bank: filterGridBank, page: page);

// tri:
~ohm.setGridMonitorFunc(0, 1, {
  if( (~prm.subtractive.osc1Waveform >= 1 ) && (~prm.subtractive.osc1Waveform < 2 ),
    { ~ohm.turnGridGreen(0, 1, filterGridBank, page); }, { ~ohm.turnGridCyan(0, 1, filterGridBank, page); }
  );
}, filterGridBank, page);
~ohm.setGridFunc(0, 1, { ~prm.subtractive.setOsc1Waveform('tri'); }, bank: filterGridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(0, 2, {
  if ( (~prm.subtractive.osc1Waveform >= 2) && (~prm.subtractive.osc1Waveform <3 ),
    { ~ohm.turnGridGreen(0, 2, filterGridBank, page); }, { ~ohm.turnGridBlue(0, 2, filterGridBank, page); } );
}, filterGridBank, page);
~ohm.setGridFunc(0, 2, { ~prm.subtractive.setOsc1Waveform('saw'); }, bank: filterGridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(0, 3, {
  if( ( ~prm.subtractive.osc1Waveform >=3),
    { ~ohm.turnGridGreen(0, 3, filterGridBank, page); }, { ~ohm.turnGridMagenta(0, 3, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(0, 3, { ~prm.subtractive.setOsc1Waveform('rect'); }, bank: filterGridBank, page: page);

//// Osc 1 Octave:

// monitor func:
~ohm.setGridMonitorFunc(3, 0, {
  5.do({ | i | ~ohm.turnGridBlue(i+3, 0, filterGridBank, page); });
  switch(~prm.subtractive.osc1OctaveMul,
    { 0.25 }, { ~ohm.turnGridGreen(3, 0, filterGridBank, page); },
    { 0.5 }, { ~ohm.turnGridGreen(4, 0, filterGridBank, page); },
    { 1 }, { ~ohm.turnGridGreen(5, 0, filterGridBank, page); },
    { 2 }, { ~ohm.turnGridGreen(6, 0, filterGridBank, page); },
    { 4 }, { ~ohm.turnGridGreen(7, 0, filterGridBank, page); },
  );
}, filterGridBank, page);
~ohm.setGridFunc(3, 0, { ~prm.subtractive.setOsc1OctaveMul(0.25); }, bank: filterGridBank, page: page);
~ohm.setGridFunc(4, 0, { ~prm.subtractive.setOsc1OctaveMul(0.5); }, bank: filterGridBank, page: page);
~ohm.setGridFunc(5, 0, { ~prm.subtractive.setOsc1OctaveMul(1); }, bank: filterGridBank, page: page);
~ohm.setGridFunc(6, 0, { ~prm.subtractive.setOsc1OctaveMul(2); }, bank: filterGridBank, page: page);
~ohm.setGridFunc(7, 0, { ~prm.subtractive.setOsc1OctaveMul(4); }, bank: filterGridBank, page: page);


//// Osc 2 Waveform:

// sine:
~ohm.setGridMonitorFunc(1, 0, {
  if( ~prm.subtractive.osc2Waveform == 0,
    { ~ohm.turnGridGreen(1, 0, filterGridBank, page); }, { ~ohm.turnGridWhite(1, 0, filterGridBank, page); }
  );
}, filterGridBank, page);
~ohm.setGridFunc(1, 0, { ~prm.subtractive.setOsc2Waveform('sine'); }, bank: filterGridBank, page: page);

// tri:
~ohm.setGridMonitorFunc(1, 1, {
  if( (~prm.subtractive.osc2Waveform >= 1 ) && (~prm.subtractive.osc2Waveform < 2 ),
    { ~ohm.turnGridGreen(1, 1, filterGridBank, page); }, { ~ohm.turnGridCyan(1, 1, filterGridBank, page); }
  );
}, filterGridBank, page);
~ohm.setGridFunc(1, 1, { ~prm.subtractive.setOsc2Waveform('tri'); }, bank: filterGridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(1, 2, {
  if ( (~prm.subtractive.osc2Waveform >= 2) && (~prm.subtractive.osc2Waveform <3 ),
    { ~ohm.turnGridGreen(1, 2, filterGridBank, page); }, { ~ohm.turnGridBlue(1, 2, filterGridBank, page); } );
}, filterGridBank, page);
~ohm.setGridFunc(1, 2, { ~prm.subtractive.setOsc2Waveform('saw'); }, bank: filterGridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(1, 3, {
  if( ( ~prm.subtractive.osc2Waveform >=3),
    { ~ohm.turnGridGreen(1, 3, filterGridBank, page); }, { ~ohm.turnGridMagenta(1, 3, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(1, 3, { ~prm.subtractive.setOsc2Waveform('rect'); }, bank: filterGridBank, page: page);


//// Osc 2 Octave:

// monitor func:
~ohm.setGridMonitorFunc(3, 1, {
  5.do({ | i | ~ohm.turnGridBlue(i+3, 1, filterGridBank, page); });
  switch(~prm.subtractive.osc2OctaveMul,
    { 0.25 }, { ~ohm.turnGridGreen(3, 1, filterGridBank, page); },
    { 0.5 }, { ~ohm.turnGridGreen(4, 1, filterGridBank, page); },
    { 1 }, { ~ohm.turnGridGreen(5, 1, filterGridBank, page); },
    { 2 }, { ~ohm.turnGridGreen(6, 1, filterGridBank, page); },
    { 4 }, { ~ohm.turnGridGreen(7, 1, filterGridBank, page); },
  );
}, filterGridBank, page);
~ohm.setGridFunc(3, 1, { ~prm.subtractive.setOsc2OctaveMul(0.25); }, bank: filterGridBank, page: page);
~ohm.setGridFunc(4, 1, { ~prm.subtractive.setOsc2OctaveMul(0.5); }, bank: filterGridBank, page: page);
~ohm.setGridFunc(5, 1, { ~prm.subtractive.setOsc2OctaveMul(1); }, bank: filterGridBank, page: page);
~ohm.setGridFunc(6, 1, { ~prm.subtractive.setOsc2OctaveMul(2); }, bank: filterGridBank, page: page);
~ohm.setGridFunc(7, 1, { ~prm.subtractive.setOsc2OctaveMul(4); }, bank: filterGridBank, page: page);

// LFO 1 Waveforms:

// sine:
~ohm.setGridMonitorFunc(2, 4, {
  if( ~prm.subtractive.lfo1Waveform == 0,
    { ~ohm.turnGridGreen(2, 4, filterGridBank, page); }, {~ohm.turnGridWhite(2, 4, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(2, 4, { ~prm.subtractive.setLFO1Waveform('sine'); }, bank: filterGridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(3, 4, {
  if( (~prm.subtractive.lfo1Waveform >=1) && (~prm.subtractive.lfo1Waveform < 2),
    { ~ohm.turnGridGreen(3, 4, filterGridBank, page); }, { ~ohm.turnGridBlue(3, 4, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(3, 4, { ~prm.subtractive.setLFO1Waveform('saw'); }, bank: filterGridBank, page: page);

// revSaw:
~ohm.setGridMonitorFunc(4, 4, {
  if( (~prm.subtractive.lfo1Waveform >=2) && (~prm.subtractive.lfo1Waveform <3),
    { ~ohm.turnGridGreen(4, 4, filterGridBank, page); }, { ~ohm.turnGridYellow(4, 4, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(4, 4, { ~prm.subtractive.setLFO1Waveform('revSaw'); }, bank: filterGridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(5, 4, {
  if( (~prm.subtractive.lfo1Waveform >=3) && (~prm.subtractive.lfo1Waveform < 4),
    { ~ohm.turnGridGreen(5, 4, filterGridBank, page); }, { ~ohm.turnGridMagenta(5, 4, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(5, 4, { ~prm.subtractive.setLFO1Waveform('rect'); }, bank: filterGridBank, page: page);

// noise:
~ohm.setGridMonitorFunc(7, 4, {
  if (~prm.subtractive.lfo1Waveform >=5,
    { ~ohm.turnGridGreen(7, 4, filterGridBank, page); }, { ~ohm.turnGridRed(7, 4, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(7, 4, { ~prm.subtractive.setLFO1Waveform('noise'); }, bank: filterGridBank, page: page);

// sample and hold:
~ohm.setGridMonitorFunc(6, 4, {
  if( ( ~prm.subtractive.lfo1Waveform >=4) && (~prm.subtractive.lfo1Waveform <5),
    { ~ohm.turnGridGreen(6, 4, filterGridBank, page); }, { ~ohm.turnGridCyan(6, 4, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(6, 4, { ~prm.subtractive.setLFO1Waveform('sampleAndHold'); }, bank: filterGridBank, page: page);


//// LFO 2 Waveforms:

// sine:
~ohm.setGridMonitorFunc(2, 5, {
  if( ~prm.subtractive.lfo2Waveform == 0,
    { ~ohm.turnGridGreen(2, 5, filterGridBank, page); }, {~ohm.turnGridWhite(2, 5, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(2, 5, { ~prm.subtractive.setLFO2Waveform('sine'); }, bank: filterGridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(3, 5, {
  if( (~prm.subtractive.lfo2Waveform >=1) && (~prm.subtractive.lfo2Waveform < 2),
    { ~ohm.turnGridGreen(3, 5, filterGridBank, page); }, { ~ohm.turnGridBlue(3, 5, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(3, 5, { ~prm.subtractive.setLFO2Waveform('saw'); }, bank: filterGridBank, page: page);

// revSaw:
~ohm.setGridMonitorFunc(4, 5, {
  if( (~prm.subtractive.lfo2Waveform >=2) && (~prm.subtractive.lfo2Waveform <3),
    { ~ohm.turnGridGreen(4, 5, filterGridBank, page); }, { ~ohm.turnGridYellow(4, 5, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(4, 5, { ~prm.subtractive.setLFO2Waveform('revSaw'); }, bank: filterGridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(5, 5, {
  if( (~prm.subtractive.lfo2Waveform >=3) && (~prm.subtractive.lfo2Waveform < 4),
    { ~ohm.turnGridGreen(5, 5, filterGridBank, page); }, { ~ohm.turnGridMagenta(5, 5, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(5, 5, { ~prm.subtractive.setLFO2Waveform('rect'); }, bank: filterGridBank, page: page);

// noise:
~ohm.setGridMonitorFunc(7, 5, {
  if (~prm.subtractive.lfo2Waveform >=5,
    { ~ohm.turnGridGreen(7, 5, filterGridBank, page); }, { ~ohm.turnGridRed(7, 5, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(7, 5, { ~prm.subtractive.setLFO2Waveform('noise'); }, bank: filterGridBank, page: page);

// sample and hold:
~ohm.setGridMonitorFunc(6, 5, {
  if( ( ~prm.subtractive.lfo2Waveform >=4) && (~prm.subtractive.lfo2Waveform <5),
    { ~ohm.turnGridGreen(6, 5, filterGridBank, page); }, { ~ohm.turnGridCyan(6, 5, filterGridBank, page); });
}, filterGridBank, page);
~ohm.setGridFunc(6, 5, { ~prm.subtractive.setLFO2Waveform('sampleAndHold'); }, bank: filterGridBank, page: page);

//// LFO Inversions:

// Cutoff LFO 1:
~ohm.setGridMonitorFunc(4, 6, {
  if( cutoffLFO1Invert == false, { ~ohm.turnGridBlue(4, 6); }, { ~ohm.turnGridWhite(4, 6); });
}, filterGridBank, page);
~ohm.setGridFunc(4, 6, {
  if( cutoffLFO1Invert == false, { cutoffLFO1Invert = true }, { cutoffLFO1Invert = false });
}, bank: filterGridBank, page: page);

// Res LFO 1:
~ohm.setGridMonitorFunc(5, 6, {
  if( resLFO1Invert == false, { ~ohm.turnGridBlue(5, 6); }, { ~ohm.turnGridWhite(5, 6); });
}, filterGridBank, page);
~ohm.setGridFunc(5, 6, {
  if( resLFO1Invert == false, { resLFO1Invert = true }, { resLFO1Invert = false });
}, bank: filterGridBank, page: page);

// Cutoff LFO 2:
~ohm.setGridMonitorFunc(6, 6, {
  if( cutoffLFO2Invert == false, { ~ohm.turnGridBlue(6, 6); }, { ~ohm.turnGridWhite(6, 6); });
}, filterGridBank, page);
~ohm.setGridFunc(6, 6, {
  if( cutoffLFO2Invert == false, { cutoffLFO2Invert = true }, { cutoffLFO2Invert = false });
}, bank: filterGridBank, page: page);

// Res LFO 2:
~ohm.setGridMonitorFunc(7, 6, {
  if( resLFO2Invert == false, { ~ohm.turnGridBlue(7, 6); }, { ~ohm.turnGridWhite(7, 6); });
}, filterGridBank, page);
~ohm.setGridFunc(7, 6, {
  if( resLFO2Invert == false, { resLFO2Invert = true }, { resLFO2Invert = false });
}, bank: filterGridBank, page: page);
