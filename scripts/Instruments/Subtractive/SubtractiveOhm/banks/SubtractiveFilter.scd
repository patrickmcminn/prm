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
  ~prm.mixer.setVol(val.ccdbfs(-70, 0));
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
  ~prm.subtractive.setFilterCutoffLFO1BottomRatio(amountLow);
  ~prm.subtractive.setFilterCutoffLFO1TopRatio(amountHigh);
}, filterKnobBank, page);

// Res LFO 1:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var amountLow = val.linlin(0, 127, 0, -2);
  var amountHigh = val.linlin(0, 127, 0, 2);
  ~prm.subtractive.setFilterResLFO1Bottom(amountLow);
  ~prm.subtractive.setFilterResLFO1Top(amountHigh);
}, filterKnobBank, page);

// Cutoff LFO 2:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.03125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  ~prm.subtractive.setFilterCutoffLFO2BottomRatio(amountLow);
  ~prm.subtractive.setFilterCutoffLFO2TopRatio(amountHigh);
}, filterKnobBank, page);

// Res LFO 2:
~ohm.setLeftKnobFunc(3, 1, { | val |
   var amountLow = val.linlin(0, 127, 0, -2);
  var amountHigh = val.linlin(0, 127, 0, 2);
  ~prm.subtractive.setFilterResLFO2Bottom(amountLow);
  ~prm.subtractive.setFilterResLFO2Top(amountHigh);
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

// Filter Loop:
~ohm.setGridMonitorFunc(0, 0, {
  if( ~prm.subtractive.filterEnvLoop == 1,
    { ~ohm.turnGridGreen(0, 0, filterGridBank, page); },
    { ~ohm.turnGridRed(0, 0, filterGridBank, page); }
  );
}, filterGridBank, page);
~ohm.setGridFunc(0, 0, {
  if( ~prm.subtractive.filterEnvLoop == 0,
    { ~prm.subtractive.setFilterEnvLoop(true); }, { ~prm.subtractive.setFilterEnvLoop(false); });
}, filterGridBank, page);

// Filter Env Ratio Reset:
~ohm.turnGridWhite(0, 1, filterGridBank, page);
~ohm.setGridFunc(0, 1, {
  ~prm.subtractive.setFilterEnvAttackRatio(1);
  ~prm.subtractive.setFilterEnvPeakRatio(1);
  ~prm.subtractive.setFilterEnvSustainRatio(1);
  ~prm.subtractive.setFilterEnvReleaseRatio(1);
}, filterGridBank, page);


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
