/*
Monday, September 12th 2016
Subtractive Page.scd
next attempt at making a control surface page for the Subtractive synth
prm
*/

var page = 'subtractive';

var mainKnobBank = 0;
var osc1KnobBank = 1;
var osc2KnobBank = 2;
var filterKnobBank = 3;

var mainGridBank = 0;
var osc1GridBank = 1;
var osc2GridBank = 2;
var filterGridBank = 3;
var keyboardGridBank = 8;

var mainSliderBank = 0;
var osc1SliderBank = 1;
var osc2SliderBank = 2;
var filterSliderBank = 3;

var mainCrossfaderBank = 0;
var osc1CrossfaderBank = 1;
var osc2CrossfaderBank = 2;
var filterCrossfaderBank = 3;

var mainButtonBank = 0;
var osc1ButtonBank = 1;
var osc2ButtonBank = 2;
var filterButtonBank = 3;

~ohm.makePage(page);

~ohm.addGridBanks(8, page);
~ohm.addLeftButtonsBanks(7, page);
~ohm.addLeftKnobsBanks(7, page);
~ohm.addLeftSlidersBanks(7, page);
~ohm.addCrossfaderButtonsBanks(7, page);
~ohm.addCrossfaderBanks(7, page);

//////// Bank Change:
~ohm.setGridMonitorFunc(0, 7, {
  8.do({ | i | ~ohm.turnGridYellow(i, 7, mainGridBank, page); });
  ~ohm.turnGridGreen(~ohm.activeLeftKnobsBank, 7, mainGridBank, page);
}, mainGridBank, page);
~ohm.setGridMonitorFunc(0, 7, {
  8.do({ | i | ~ohm.turnGridYellow(i, 7, osc1GridBank, page); });
  ~ohm.turnGridGreen(~ohm.activeLeftKnobsBank, 7, osc1GridBank, page);
}, osc1GridBank, page);
~ohm.setGridMonitorFunc(0, 7, {
  8.do({ | i | ~ohm.turnGridYellow(i, 7, osc2GridBank, page); });
  ~ohm.turnGridGreen(~ohm.activeLeftKnobsBank, 7, osc2GridBank, page);
}, osc2GridBank, page);
~ohm.setGridMonitorFunc(0, 7, {
  8.do({ | i | ~ohm.turnGridYellow(i, 7, filterGridBank, page); });
  ~ohm.turnGridGreen(~ohm.activeLeftKnobsBank, 7, osc2GridBank, page);
}, filterGridBank, page);


// main:
~ohm.setGridFunc(0, 7, {
  ~ohm.setActiveLeftKnobsBank(mainKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(mainButtonBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank);
  ~ohm.setActiveLeftSlidersBank(mainSliderBank);
  ~ohm.setActiveCrossfaderBank(mainCrossfaderBank);
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(1, 7, {
  ~ohm.setActiveGridBank(osc1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc1ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc1SliderBank);
  ~ohm.setActiveCrossfaderBank(osc1CrossfaderBank);
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(2, 7, {
  ~ohm.setActiveGridBank(osc2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc2ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc2SliderBank);
  ~ohm.setActiveCrossfaderBank(osc2CrossfaderBank);
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(3, 7, {
  ~ohm.setActiveGridBank(filterGridBank, page);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(filterButtonBank);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank);
  ~ohm.setActiveLeftSlidersBank(filterSliderBank);
  ~ohm.setActiveCrossfaderBank(filterCrossfaderBank);
}, bank: mainGridBank, page: page);


// osc 1:
~ohm.setGridFunc(0, 7, {
  ~ohm.setActiveGridBank(mainGridBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(mainButtonBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank);
  ~ohm.setActiveLeftSlidersBank(mainSliderBank);
  ~ohm.setActiveCrossfaderBank(mainCrossfaderBank);
}, bank: osc1GridBank, page: page);
~ohm.setGridFunc(1, 7, {
  ~ohm.setActiveGridBank(osc1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc1ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc1SliderBank);
  ~ohm.setActiveCrossfaderBank(osc1CrossfaderBank);
}, bank: osc1GridBank, page: page);
~ohm.setGridFunc(2, 7, {
  ~ohm.setActiveGridBank(osc2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc2ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc2SliderBank);
  ~ohm.setActiveCrossfaderBank(osc2CrossfaderBank);
}, bank: osc1GridBank, page: page);
~ohm.setGridFunc(3, 7, {
  ~ohm.setActiveGridBank(filterGridBank, page);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(filterButtonBank);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank);
  ~ohm.setActiveLeftSlidersBank(filterSliderBank);
  ~ohm.setActiveCrossfaderBank(filterCrossfaderBank);
}, bank: osc1GridBank, page: page);

// osc2:
~ohm.setGridFunc(0, 7, {
  ~ohm.setActiveGridBank(mainGridBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(mainButtonBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank);
  ~ohm.setActiveLeftSlidersBank(mainSliderBank);
  ~ohm.setActiveCrossfaderBank(mainCrossfaderBank);
}, bank: osc2GridBank, page: page);
~ohm.setGridFunc(1, 7, {
  ~ohm.setActiveGridBank(osc1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc1ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc1SliderBank);
  ~ohm.setActiveCrossfaderBank(osc1CrossfaderBank);
}, bank: osc2GridBank, page: page);
~ohm.setGridFunc(2, 7, {
  ~ohm.setActiveGridBank(osc2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc2ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc2SliderBank);
  ~ohm.setActiveCrossfaderBank(osc2CrossfaderBank);
}, bank: osc2GridBank, page: page);
~ohm.setGridFunc(3, 7, {
  ~ohm.setActiveGridBank(filterGridBank, page);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(filterButtonBank);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank);
  ~ohm.setActiveLeftSlidersBank(filterSliderBank);
  ~ohm.setActiveCrossfaderBank(filterCrossfaderBank);
}, bank: osc2GridBank, page: page);

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

////////// Main Bank:

//// Left Knobs:

// 0, 0: Filter Cutoff:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var cutoff = val.linexp(0, 127, 60, 20000);
  ~prm.subtractive.setFilterCutoff(cutoff);
}, mainKnobBank, page);

// 1, 0: Filter Resonance:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var res = val.linlin(0, 127, 0, 1.5);
  ~prm.subtractive.setFilterRes(res);
}, mainKnobBank, page);

// 2, 0: LFO 1 to Cutoff:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.03125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  ~prm.subtractive.setFilterCutoffLFO1BottomRatio(amountLow);
  ~prm.subtractive.setFilterCutoffLFO1TopRatio(amountHigh);
}, mainKnobBank, page);

// 3, 0: LFO 2 to Cutoff:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.03125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  ~prm.subtractive.setFilterCutoffLFO2BottomRatio(amountLow);
  ~prm.subtractive.setFilterCutoffLFO2TopRatio(amountHigh);
}, mainKnobBank, page);

// 0, 1: Oscillator 1 Volume:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~prm.subtractive.setOsc1Vol(val.ccdbfs(-70, 0));
}, mainKnobBank, page);

// 1, 1: Oscillator 1 Pulse Width:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var width = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc1PulseWidth(width);
}, mainKnobBank, page);

// 2, 1: Oscillator 2 Volume:
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~prm.subtractive.setOsc2Vol(val.ccdbfs(-70, 0));
  val.postln;
}, mainKnobBank, page);

// 3, 1: Oscillator 2 Pulse Width:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var width = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc2PulseWidth(width);
}, mainKnobBank, page);

// 0, 2 : Oscillator 1 Pulse Width LFO 1:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var amountLow = val.linlin(0, 127, 0.0, -1.0);
  var amountHigh = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc1PulseWidthLFO1Bottom(amountLow);
  ~prm.subtractive.setOsc1PulseWidthLFO1Top(amountHigh);
}, mainKnobBank, page);

// 1, 2 : Oscillator 1 Pulse Width LFO 1:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var amountLow = val.linlin(0, 127, 0.0, -1.0);
  var amountHigh = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc2PulseWidthLFO1Bottom(amountLow);
  ~prm.subtractive.setOsc2PulseWidthLFO1Top(amountHigh);
}, mainKnobBank, page);

// 2, 2 : LFO 1 Freq:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var freq = val.linexp(0, 127, 0.001, 220);
  ~prm.subtractive.setLFO1Freq(freq);
}, mainKnobBank, page);

// 3, 2 : LFO 2 Freq:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var freq = val.linexp(0, 127, 0.001, 220);
  ~prm.subtractive.setLFO2Freq(freq);
}, mainKnobBank, page);


//// Left Sliders:

// 0: Attack Time:
~ohm.setLeftSliderFunc(0, { | val |
  var attack = val.linlin(0, 127, 0, 7);
  ~prm.subtractive.setAttackTime(attack);
}, mainSliderBank, page);

// 1: decayTime:
~ohm.setLeftSliderFunc(1, { | val |
  var decay = val.linlin(0, 127, 0, 7);
  ~prm.subtractive.setDecayTime(decay);
}, mainSliderBank, page);

// 2: sustain Level:
~ohm.setLeftSliderFunc(2, { | val |
  var sustain = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setSustainLevel(sustain);
}, mainSliderBank, page);

// 3: release time:
~ohm.setLeftSliderFunc(3, { | val |
  var release = val.linlin(0, 127, 0.0, 12);
  ~prm.subtractive.setReleaseTime(release);
}, mainSliderBank, page);

////// Main Grid:

//~ohm.turnAllGridCyan(mainGridBank, page);

//// Osc 1 Waveform:

// sine:
~ohm.setGridMonitorFunc(0, 0, {
  if( ~prm.subtractive.osc1Waveform == 0,
    { ~ohm.turnGridGreen(0, 0, mainGridBank, page); }, { ~ohm.turnGridWhite(0, 0, mainGridBank, page); }
  );
}, mainGridBank, page);
~ohm.setGridFunc(0, 0, { ~prm.subtractive.setOsc1Waveform('sine'); }, bank: mainGridBank, page: page);

// tri:
~ohm.setGridMonitorFunc(0, 1, {
  if( (~prm.subtractive.osc1Waveform >= 1 ) && (~prm.subtractive.osc1Waveform < 2 ),
    { ~ohm.turnGridGreen(0, 1, mainGridBank, page); }, { ~ohm.turnGridCyan(0, 1, mainGridBank, page); }
  );
}, mainGridBank, page);
~ohm.setGridFunc(0, 1, { ~prm.subtractive.setOsc1Waveform('tri'); }, bank: mainGridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(0, 2, {
  if ( (~prm.subtractive.osc1Waveform >= 2) && (~prm.subtractive.osc1Waveform <3 ),
    { ~ohm.turnGridGreen(0, 2, mainGridBank, page); }, { ~ohm.turnGridBlue(0, 2, mainGridBank, page); } );
}, mainGridBank, page);
~ohm.setGridFunc(0, 2, { ~prm.subtractive.setOsc1Waveform('saw'); }, bank: mainGridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(0, 3, {
  if( ( ~prm.subtractive.osc1Waveform >=3),
    { ~ohm.turnGridGreen(0, 3, mainGridBank, page); }, { ~ohm.turnGridMagenta(0, 3, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(0, 3, { ~prm.subtractive.setOsc1Waveform('rect'); }, bank: mainGridBank, page: page);

//// Osc 1 Octave:

// monitor func:
~ohm.setGridMonitorFunc(3, 0, {
  5.do({ | i | ~ohm.turnGridBlue(i+3, 0, mainGridBank, page); });
  switch(~prm.subtractive.osc1OctaveMul,
    { 0.25 }, { ~ohm.turnGridGreen(3, 0, mainGridBank, page); },
    { 0.5 }, { ~ohm.turnGridGreen(4, 0, mainGridBank, page); },
    { 1 }, { ~ohm.turnGridGreen(5, 0, mainGridBank, page); },
    { 2 }, { ~ohm.turnGridGreen(6, 0, mainGridBank, page); },
    { 4 }, { ~ohm.turnGridGreen(7, 0, mainGridBank, page); },
  );
}, mainGridBank, page);
~ohm.setGridFunc(3, 0, { ~prm.subtractive.setOsc1OctaveMul(0.25); }, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 0, { ~prm.subtractive.setOsc1OctaveMul(0.5); }, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 0, { ~prm.subtractive.setOsc1OctaveMul(1); }, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 0, { ~prm.subtractive.setOsc1OctaveMul(2); }, bank: mainGridBank, page: page);
~ohm.setGridFunc(7, 0, { ~prm.subtractive.setOsc1OctaveMul(4); }, bank: mainGridBank, page: page);


//// Osc 2 Waveform:

// sine:
~ohm.setGridMonitorFunc(1, 0, {
  if( ~prm.subtractive.osc2Waveform == 0,
    { ~ohm.turnGridGreen(1, 0, mainGridBank, page); }, { ~ohm.turnGridWhite(1, 0, mainGridBank, page); }
  );
}, mainGridBank, page);
~ohm.setGridFunc(1, 0, { ~prm.subtractive.setOsc2Waveform('sine'); }, bank: mainGridBank, page: page);

// tri:
~ohm.setGridMonitorFunc(1, 1, {
  if( (~prm.subtractive.osc2Waveform >= 1 ) && (~prm.subtractive.osc2Waveform < 2 ),
    { ~ohm.turnGridGreen(1, 1, mainGridBank, page); }, { ~ohm.turnGridCyan(1, 1, mainGridBank, page); }
  );
}, mainGridBank, page);
~ohm.setGridFunc(1, 1, { ~prm.subtractive.setOsc2Waveform('tri'); }, bank: mainGridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(1, 2, {
  if ( (~prm.subtractive.osc2Waveform >= 2) && (~prm.subtractive.osc2Waveform <3 ),
    { ~ohm.turnGridGreen(1, 2, mainGridBank, page); }, { ~ohm.turnGridBlue(1, 2, mainGridBank, page); } );
}, mainGridBank, page);
~ohm.setGridFunc(1, 2, { ~prm.subtractive.setOsc2Waveform('saw'); }, bank: mainGridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(1, 3, {
  if( ( ~prm.subtractive.osc2Waveform >=3),
    { ~ohm.turnGridGreen(1, 3, mainGridBank, page); }, { ~ohm.turnGridMagenta(1, 3, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(1, 3, { ~prm.subtractive.setOsc2Waveform('rect'); }, bank: mainGridBank, page: page);


//// Osc 2 Octave:

// monitor func:
~ohm.setGridMonitorFunc(3, 1, {
  5.do({ | i | ~ohm.turnGridBlue(i+3, 1, mainGridBank, page); });
  switch(~prm.subtractive.osc2OctaveMul,
    { 0.25 }, { ~ohm.turnGridGreen(3, 1, mainGridBank, page); },
    { 0.5 }, { ~ohm.turnGridGreen(4, 1, mainGridBank, page); },
    { 1 }, { ~ohm.turnGridGreen(5, 1, mainGridBank, page); },
    { 2 }, { ~ohm.turnGridGreen(6, 1, mainGridBank, page); },
    { 4 }, { ~ohm.turnGridGreen(7, 1, mainGridBank, page); },
  );
}, mainGridBank, page);
~ohm.setGridFunc(3, 1, { ~prm.subtractive.setOsc2OctaveMul(0.25); }, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 1, { ~prm.subtractive.setOsc2OctaveMul(0.5); }, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 1, { ~prm.subtractive.setOsc2OctaveMul(1); }, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 1, { ~prm.subtractive.setOsc2OctaveMul(2); }, bank: mainGridBank, page: page);
~ohm.setGridFunc(7, 1, { ~prm.subtractive.setOsc2OctaveMul(4); }, bank: mainGridBank, page: page);

// LFO 1 Waveforms:

// sine:
~ohm.setGridMonitorFunc(2, 4, {
  if( ~prm.subtractive.lfo1Waveform == 0,
    { ~ohm.turnGridGreen(2, 4, mainGridBank, page); }, {~ohm.turnGridWhite(2, 4, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(2, 4, { ~prm.subtractive.setLFO1Waveform('sine'); }, bank: mainGridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(3, 4, {
  if( (~prm.subtractive.lfo1Waveform >=1) && (~prm.subtractive.lfo1Waveform < 2),
    { ~ohm.turnGridGreen(3, 4, mainGridBank, page); }, { ~ohm.turnGridBlue(3, 4, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(3, 4, { ~prm.subtractive.setLFO1Waveform('saw'); }, bank: mainGridBank, page: page);

// revSaw:
~ohm.setGridMonitorFunc(4, 4, {
  if( (~prm.subtractive.lfo1Waveform >=2) && (~prm.subtractive.lfo1Waveform <3),
    { ~ohm.turnGridGreen(4, 4, mainGridBank, page); }, { ~ohm.turnGridYellow(4, 4, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(4, 4, { ~prm.subtractive.setLFO1Waveform('revSaw'); }, bank: mainGridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(5, 4, {
  if( (~prm.subtractive.lfo1Waveform >=3) && (~prm.subtractive.lfo1Waveform < 4),
    { ~ohm.turnGridGreen(5, 4, mainGridBank, page); }, { ~ohm.turnGridMagenta(5, 4, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(5, 4, { ~prm.subtractive.setLFO1Waveform('rect'); }, bank: mainGridBank, page: page);

// noise:
~ohm.setGridMonitorFunc(7, 4, {
  if (~prm.subtractive.lfo1Waveform >=5,
    { ~ohm.turnGridGreen(7, 4, mainGridBank, page); }, { ~ohm.turnGridRed(7, 4, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(7, 4, { ~prm.subtractive.setLFO1Waveform('noise'); }, bank: mainGridBank, page: page);

// sample and hold:
~ohm.setGridMonitorFunc(6, 4, {
  if( ( ~prm.subtractive.lfo1Waveform >=4) && (~prm.subtractive.lfo1Waveform <5),
    { ~ohm.turnGridGreen(6, 4, mainGridBank, page); }, { ~ohm.turnGridCyan(6, 4, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(6, 4, { ~prm.subtractive.setLFO1Waveform('sampleAndHold'); }, bank: mainGridBank, page: page);


//// LFO 2 Waveforms:

// sine:
~ohm.setGridMonitorFunc(2, 5, {
  if( ~prm.subtractive.lfo2Waveform == 0,
    { ~ohm.turnGridGreen(2, 5, mainGridBank, page); }, {~ohm.turnGridWhite(2, 5, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(2, 5, { ~prm.subtractive.setLFO2Waveform('sine'); }, bank: mainGridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(3, 5, {
  if( (~prm.subtractive.lfo2Waveform >=1) && (~prm.subtractive.lfo2Waveform < 2),
    { ~ohm.turnGridGreen(3, 5, mainGridBank, page); }, { ~ohm.turnGridBlue(3, 5, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(3, 5, { ~prm.subtractive.setLFO2Waveform('saw'); }, bank: mainGridBank, page: page);

// revSaw:
~ohm.setGridMonitorFunc(4, 5, {
  if( (~prm.subtractive.lfo2Waveform >=2) && (~prm.subtractive.lfo2Waveform <3),
    { ~ohm.turnGridGreen(4, 5, mainGridBank, page); }, { ~ohm.turnGridYellow(4, 5, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(4, 5, { ~prm.subtractive.setLFO2Waveform('revSaw'); }, bank: mainGridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(5, 5, {
  if( (~prm.subtractive.lfo2Waveform >=3) && (~prm.subtractive.lfo2Waveform < 4),
    { ~ohm.turnGridGreen(5, 5, mainGridBank, page); }, { ~ohm.turnGridMagenta(5, 5, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(5, 5, { ~prm.subtractive.setLFO2Waveform('rect'); }, bank: mainGridBank, page: page);

// noise:
~ohm.setGridMonitorFunc(7, 5, {
  if (~prm.subtractive.lfo2Waveform >=5,
    { ~ohm.turnGridGreen(7, 5, mainGridBank, page); }, { ~ohm.turnGridRed(7, 5, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(7, 5, { ~prm.subtractive.setLFO2Waveform('noise'); }, bank: mainGridBank, page: page);

// sample and hold:
~ohm.setGridMonitorFunc(6, 5, {
  if( ( ~prm.subtractive.lfo2Waveform >=4) && (~prm.subtractive.lfo2Waveform <5),
    { ~ohm.turnGridGreen(6, 5, mainGridBank, page); }, { ~ohm.turnGridCyan(6, 5, mainGridBank, page); });
}, mainGridBank, page);
~ohm.setGridFunc(6, 5, { ~prm.subtractive.setLFO2Waveform('sampleAndHold'); }, bank: mainGridBank, page: page);


//// Filter:
~ohm.setGridMonitorFunc(0, 6, {
  3.do({ | i | ~ohm.turnGridCyan(i, 6, mainGridBank, page); });
  switch(~prm.subtractive.filterType,
    { 0 }, { ~ohm.turnGridGreen(0, 6, mainGridBank, page); },
    { 1 }, { ~ohm.turnGridGreen(1, 6, mainGridBank, page); },
    { 2 }, { ~ohm.turnGridGreen(2, 6, mainGridBank, page); }
  );
}, mainGridBank, page);

~ohm.setGridFunc(0, 6, { ~prm.subtractive.setFilterType('lowPass') }, bank: mainGridBank, page: page);
~ohm.setGridFunc(1, 6, { ~prm.subtractive.setFilterType('highPass') }, bank: mainGridBank, page: page);
~ohm.setGridFunc(2, 6, { ~prm.subtractive.setFilterType('bandPass') }, bank: mainGridBank, page: page);



//////// Crossfader:

~ohm.setCrossfaderFunc({ | val | ~prm.subtractive.mixer.setVol(val.ccdbfs(-70, 0)); }, mainCrossfaderBank, page);
~ohm.setCrossfaderButtonMonitorFunc(0, {
  switch(~ohm.activeGridBank,
    { mainGridBank }, { ~ohm.turnCrossfaderButtonMagenta(0, mainCrossfaderBank, page); },
    { keyboardGridBank }, { ~ohm.turnCrossfaderButtonBlue(0, mainCrossfaderBank, page); }
  );
}, mainCrossfaderBank, page);
~ohm.setCrossfaderButtonFunc(0, { ~ohm.setActiveGridBank(keyboardGridBank, page); },
  bank: mainCrossfaderBank, page: page);


//////// Oscillator 1 Bank:

//// Left Sliders:
// osc 1 vol:
~ohm.setLeftSliderFunc(0, { | val |  ~prm.subtractive.setOsc1Vol(val.ccdbfs(-70, 6)); }, osc1SliderBank, page);
// osc 1 subVol:
~ohm.setLeftSliderFunc(1, { | val | ~prm.subtractive.setOsc1SubVol(val.ccdbfs(-70, 0)); }, osc1SliderBank, page);
// osc 1 waveform:
~ohm.setLeftSliderFunc(2, { | val |
  var waveform = val.linlin(0, 127, 0, 3);
  ~prm.subtractive.setOsc1Waveform(waveform);
}, osc1SliderBank, page);
// Subtractive Vol:
~ohm.setLeftSliderFunc(3, { | val | ~prm.subtractive.mixer.setVol(val.ccdbfs(-70, 0)); }, osc1SliderBank, page);

//// Left Knobs:
// pulse width:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var width = val.linlin(0, 127, 0, 1);
  ~prm.subtractive.setOsc1PulseWidth(width);
}, osc1KnobBank, page);

// Freq Env Start Ratio:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var low = val.linexp(0, 63, 0.125, 1);
  var high = val.linexp(64, 127, 1, 4);
  if( val <= 63,
    { ~prm.subtractive.setOsc1FreqEnvStartRatio(low); }, { ~prm.subtractive.setOsc1FreqEnvStartRatio(high); });
}, osc1KnobBank, page);
// Freq Env End Ratio:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var low = val.linexp(0, 63, 0.125, 1);
  var high = val.linexp(64, 127, 1, 4);
  if( val <= 63,
    { ~prm.subtractive.setOsc1FreqEnvEndRatio(low); }, { ~prm.subtractive.setOsc1FreqEnvEndRatio(high); });
}, osc1KnobBank, page);
// Freq Env Time:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var time = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setOsc1FreqEnvTime(time);
}, osc1KnobBank, page);

// LFO 1 Osc 1 Freq:
~ohm.setLeftKnobFunc(0, 1, { | val |
  var low = val.linexp(0, 127, 1, 0.125);
  var high = val.linexp(0, 127, 1, 4);
  ~prm.subtractive.setOsc1FreqLFO1BottomRatio(low);
  ~prm.subtractive.setOsc1FreqLFO1TopRatio(high);
}, osc1KnobBank, page);
// LFO 2 Osc 1 Freq:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var low = val.linlin(0, 127, 1, 0.125);
  var high = val.linlin(0, 127, 1, 4);
  ~prm.subtractive.setOsc1FreqLFO2BottomRatio(low);
  ~prm.subtractive.setOsc1FreqLFO2TopRatio(high);
}, osc1KnobBank, page);
// LFO 1 Osc 1 Amp:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var ratio = val.linlin(0, 127, 1, 0);
  ~prm.subtractive.setOsc1AmpLFO1Bottom(ratio);
}, osc1KnobBank, page);
// LFO 2 Osc 1 Amp:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var ratio = val.linlin(0, 127, 1, 0);
  ~prm.subtractive.setOsc1AmpLFO2Bottom(ratio);
}, osc1KnobBank, page);

// Oscillator 1 Pulse Width LFO 1:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var amountLow = val.linlin(0, 127, 0.0, -1.0);
  var amountHigh = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc1PulseWidthLFO1Bottom(amountLow);
  ~prm.subtractive.setOsc1PulseWidthLFO1Top(amountHigh);
}, osc1KnobBank, page);
// Oscillator 1 Pulse Width LFO 2:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var amountLow = val.linlin(0, 127, 0.0, -1.0);
  var amountHigh = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc1PulseWidthLFO2Bottom(amountLow);
  ~prm.subtractive.setOsc1PulseWidthLFO2Top(amountHigh);
}, osc1KnobBank, page);
// Osc 1 Waveform LFO 1:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var low = val.linlin(0, 127, 0, -3);
  var high = val.linlin(0, 127, 0, 3);
  ~prm.subtractive.setOsc1WaveformLFO1Bottom(low);
  ~prm.subtractive.setOsc1WaveformLFO1Top(high);
}, osc1KnobBank, page);
// Osc 1 Waveform LFO 2:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var low = val.linlin(0, 127, 0, -3);
  var high = val.linlin(0, 127, 0, 3);
  ~prm.subtractive.setOsc1WaveformLFO2Bottom(low);
  ~prm.subtractive.setOsc1WaveformLFO2Top(high);
}, osc1KnobBank, page);

//// Osc 1 Grid:

// Frequency Envelope Reset:
~ohm.turnGridWhite(0, 5, osc1GridBank, page);
~ohm.setGridFunc(0, 5, {
  ~prm.subtractive.setOsc1FreqEnvStartRatio(1);
  ~prm.subtractive.setOsc1FreqEnvEndRatio(1);
  ~prm.subtractive.setOsc1FreqEnvTime(0);
}, bank: osc1GridBank, page: page);

//// Osc 1 Waveform:

// sine:
~ohm.setGridMonitorFunc(0, 0, {
  if( ~prm.subtractive.osc1Waveform == 0,
    { ~ohm.turnGridGreen(0, 0, osc1GridBank, page); }, { ~ohm.turnGridWhite(0, 0, osc1GridBank, page); }
  );
}, osc1GridBank, page);
~ohm.setGridFunc(0, 0, { ~prm.subtractive.setOsc1Waveform('sine'); }, bank: osc1GridBank, page: page);

// tri:
~ohm.setGridMonitorFunc(0, 1, {
  if( (~prm.subtractive.osc1Waveform >= 1 ) && (~prm.subtractive.osc1Waveform < 2 ),
    { ~ohm.turnGridGreen(0, 1, osc1GridBank, page); }, { ~ohm.turnGridCyan(0, 1, osc1GridBank, page); }
  );
}, osc1GridBank, page);
~ohm.setGridFunc(0, 1, { ~prm.subtractive.setOsc1Waveform('tri'); }, bank: osc1GridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(0, 2, {
  if ( (~prm.subtractive.osc1Waveform >= 2) && (~prm.subtractive.osc1Waveform <3 ),
    { ~ohm.turnGridGreen(0, 2, osc1GridBank, page); }, { ~ohm.turnGridBlue(0, 2, osc1GridBank, page); } );
}, osc1GridBank, page);
~ohm.setGridFunc(0, 2, { ~prm.subtractive.setOsc1Waveform('saw'); }, bank: osc1GridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(0, 3, {
  if( ( ~prm.subtractive.osc1Waveform >=3),
    { ~ohm.turnGridGreen(0, 3, osc1GridBank, page); }, { ~ohm.turnGridMagenta(0, 3, osc1GridBank, page); });
}, osc1GridBank, page);
~ohm.setGridFunc(0, 3, { ~prm.subtractive.setOsc1Waveform('rect'); }, bank: osc1GridBank, page: page);

//// Osc 1 Octave:

// monitor func:
~ohm.setGridMonitorFunc(3, 0, {
  5.do({ | i | ~ohm.turnGridBlue(i+3, 0, osc1GridBank, page); });
  switch(~prm.subtractive.osc1OctaveMul,
    { 0.25 }, { ~ohm.turnGridGreen(3, 0, osc1GridBank, page); },
    { 0.5 }, { ~ohm.turnGridGreen(4, 0, osc1GridBank, page); },
    { 1 }, { ~ohm.turnGridGreen(5, 0, osc1GridBank, page); },
    { 2 }, { ~ohm.turnGridGreen(6, 0, osc1GridBank, page); },
    { 4 }, { ~ohm.turnGridGreen(7, 0, osc1GridBank, page); },
  );
}, osc1GridBank, page);
~ohm.setGridFunc(3, 0, { ~prm.subtractive.setOsc1OctaveMul(0.25); }, bank: osc1GridBank, page: page);
~ohm.setGridFunc(4, 0, { ~prm.subtractive.setOsc1OctaveMul(0.5); }, bank: osc1GridBank, page: page);
~ohm.setGridFunc(5, 0, { ~prm.subtractive.setOsc1OctaveMul(1); }, bank: osc1GridBank, page: page);
~ohm.setGridFunc(6, 0, { ~prm.subtractive.setOsc1OctaveMul(2); }, bank: osc1GridBank, page: page);
~ohm.setGridFunc(7, 0, { ~prm.subtractive.setOsc1OctaveMul(4); }, bank: osc1GridBank, page: page);

//// Crossfader:
~ohm.setCrossfaderFunc({ | val |
  var amt = val.linlin(0, 127, 1, 15);
  ~prm.subtractive.setFilterDrive(amt);
}, osc1CrossfaderBank, page);






//////// Oscillator 2:

//// Left Sliders:
// osc 2 vol:
~ohm.setLeftSliderFunc(0, { | val |  ~prm.subtractive.setOsc2Vol(val.ccdbfs(-70, 6)); }, osc2SliderBank, page);
// osc 2 subVol:
~ohm.setLeftSliderFunc(1, { | val | ~prm.subtractive.setOsc2SubVol(val.ccdbfs(-70, 0)); }, osc2SliderBank, page);
// osc 2 waveform:
~ohm.setLeftSliderFunc(2, { | val |
  var waveform = val.linlin(0, 127, 0, 3);
  ~prm.subtractive.setOsc2Waveform(waveform);
}, osc2SliderBank, page);
// Subtractive Vol:
~ohm.setLeftSliderFunc(3, { | val | ~prm.subtractive.mixer.setVol(val.ccdbfs(-70, 0)); }, osc2SliderBank, page);

//// Left Knobs:
// pulse width:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var width = val.linlin(0, 127, 0, 1);
  ~prm.subtractive.setOsc2PulseWidth(width);
}, osc2KnobBank, page);

// Freq Env Start Ratio:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var low = val.linexp(0, 63, 0.125, 1);
  var high = val.linexp(64, 127, 1, 4);
  if( val <= 63,
    { ~prm.subtractive.setOsc2FreqEnvStartRatio(low); }, { ~prm.subtractive.setOsc2FreqEnvStartRatio(high); });
}, osc2KnobBank, page);
// Freq Env End Ratio:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var low = val.linexp(0, 63, 0.125, 1);
  var high = val.linexp(64, 127, 1, 4);
  if( val <= 63,
    { ~prm.subtractive.setOsc2FreqEnvEndRatio(low); }, { ~prm.subtractive.setOsc2FreqEnvEndRatio(high); });
}, osc2KnobBank, page);
// Freq Env Time:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var time = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setOsc2FreqEnvTime(time);
}, osc2KnobBank, page);

// LFO 1 Osc 2 Freq:
~ohm.setLeftKnobFunc(0, 1, { | val |
  var low = val.linexp(0, 127, 1, 0.125);
  var high = val.linexp(0, 127, 1, 4);
  ~prm.subtractive.setOsc2FreqLFO1BottomRatio(low);
  ~prm.subtractive.setOsc2FreqLFO1TopRatio(high);
}, osc2KnobBank, page);
// LFO 2 Osc 2 Freq:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var low = val.linlin(0, 127, 1, 0.125);
  var high = val.linlin(0, 127, 1, 4);
  ~prm.subtractive.setOsc2FreqLFO2BottomRatio(low);
  ~prm.subtractive.setOsc2FreqLFO2TopRatio(high);
}, osc2KnobBank, page);
// LFO 1 Osc 2 Amp:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var ratio = val.linlin(0, 127, 1, 0);
  ~prm.subtractive.setOsc2AmpLFO1Bottom(ratio);
}, osc2KnobBank, page);
// LFO 2 Osc 2 Amp:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var ratio = val.linlin(0, 127, 1, 0);
  ~prm.subtractive.setOsc2AmpLFO2Bottom(ratio);
}, osc2KnobBank, page);

// Oscillator 2 Pulse Width LFO 1:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var amountLow = val.linlin(0, 127, 0.0, -1.0);
  var amountHigh = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc2PulseWidthLFO1Bottom(amountLow);
  ~prm.subtractive.setOsc2PulseWidthLFO1Top(amountHigh);
}, osc2KnobBank, page);
// Oscillator 2 Pulse Width LFO 2:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var amountLow = val.linlin(0, 127, 0.0, -1.0);
  var amountHigh = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc2PulseWidthLFO2Bottom(amountLow);
  ~prm.subtractive.setOsc2PulseWidthLFO2Top(amountHigh);
}, osc2KnobBank, page);
// Osc 2 Waveform LFO 1:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var low = val.linlin(0, 127, 0, -3);
  var high = val.linlin(0, 127, 0, 3);
  ~prm.subtractive.setOsc2WaveformLFO1Bottom(low);
  ~prm.subtractive.setOsc2WaveformLFO1Top(high);
}, osc2KnobBank, page);
// Osc 2 Waveform LFO 2:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var low = val.linlin(0, 127, 0, -3);
  var high = val.linlin(0, 127, 0, 3);
  ~prm.subtractive.setOsc2WaveformLFO2Bottom(low);
  ~prm.subtractive.setOsc2WaveformLFO2Top(high);
}, osc2KnobBank, page);


////// Osc2 Grid:

// Frequency Envelope Reset:
~ohm.turnGridWhite(0, 5, osc2GridBank, page);
~ohm.setGridFunc(0, 5, {
  ~prm.subtractive.setOsc2FreqEnvStartRatio(1);
  ~prm.subtractive.setOsc2FreqEnvEndRatio(1);
  ~prm.subtractive.setOsc2FreqEnvTime(0);
}, bank: osc2GridBank, page: page);

//// Osc 2 Waveform:

// sine:
~ohm.setGridMonitorFunc(1, 0, {
  if( ~prm.subtractive.osc2Waveform == 0,
    { ~ohm.turnGridGreen(1, 0, osc2GridBank, page); }, { ~ohm.turnGridWhite(1, 0, osc2GridBank, page); }
  );
}, osc2GridBank, page);
~ohm.setGridFunc(1, 0, { ~prm.subtractive.setOsc2Waveform('sine'); }, bank: osc2GridBank, page: page);

// tri:
~ohm.setGridMonitorFunc(1, 1, {
  if( (~prm.subtractive.osc2Waveform >= 1 ) && (~prm.subtractive.osc2Waveform < 2 ),
    { ~ohm.turnGridGreen(1, 1, osc2GridBank, page); }, { ~ohm.turnGridCyan(1, 1, osc2GridBank, page); }
  );
}, osc2GridBank, page);
~ohm.setGridFunc(1, 1, { ~prm.subtractive.setOsc2Waveform('tri'); }, bank: osc2GridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(1, 2, {
  if ( (~prm.subtractive.osc2Waveform >= 2) && (~prm.subtractive.osc2Waveform <3 ),
    { ~ohm.turnGridGreen(1, 2, osc2GridBank, page); }, { ~ohm.turnGridBlue(1, 2, osc2GridBank, page); } );
}, osc2GridBank, page);
~ohm.setGridFunc(1, 2, { ~prm.subtractive.setOsc2Waveform('saw'); }, bank: osc2GridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(1, 3, {
  if( ( ~prm.subtractive.osc2Waveform >=3),
    { ~ohm.turnGridGreen(1, 3, osc2GridBank, page); }, { ~ohm.turnGridMagenta(1, 3, osc2GridBank, page); });
}, osc2GridBank, page);
~ohm.setGridFunc(1, 3, { ~prm.subtractive.setOsc2Waveform('rect'); }, bank: osc2GridBank, page: page);


//// Osc 2 Octave:

// monitor func:
~ohm.setGridMonitorFunc(3, 1, {
  5.do({ | i | ~ohm.turnGridBlue(i+3, 1, osc2GridBank, page); });
  switch(~prm.subtractive.osc2OctaveMul,
    { 0.25 }, { ~ohm.turnGridGreen(3, 1, osc2GridBank, page); },
    { 0.5 }, { ~ohm.turnGridGreen(4, 1, osc2GridBank, page); },
    { 1 }, { ~ohm.turnGridGreen(5, 1, osc2GridBank, page); },
    { 2 }, { ~ohm.turnGridGreen(6, 1, osc2GridBank, page); },
    { 4 }, { ~ohm.turnGridGreen(7, 1, osc2GridBank, page); },
  );
}, osc2GridBank, page);
~ohm.setGridFunc(3, 1, { ~prm.subtractive.setOsc2OctaveMul(0.25); }, bank: osc2GridBank, page: page);
~ohm.setGridFunc(4, 1, { ~prm.subtractive.setOsc2OctaveMul(0.5); }, bank: osc2GridBank, page: page);
~ohm.setGridFunc(5, 1, { ~prm.subtractive.setOsc2OctaveMul(1); }, bank: osc2GridBank, page: page);
~ohm.setGridFunc(6, 1, { ~prm.subtractive.setOsc2OctaveMul(2); }, bank: osc2GridBank, page: page);
~ohm.setGridFunc(7, 1, { ~prm.subtractive.setOsc2OctaveMul(4); }, bank: osc2GridBank, page: page);


//// crossfader:
~ohm.setCrossfaderFunc({ | val |
  var detune = val.linlin(0, 127, 0, -50);
  ~prm.subtractive.setOsc2DetuneCents(detune);
}, bank: osc2GridBank, page: page);



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

// Filter Env Attack Ratio:
~ohm.setLeftKnobFunc(0, 0, {  | val |
  var attackLow = val.linlin(0, 63, 0.0001, 1);
  var attackHigh = val.linlin(64, 127, 1, 32);
  if( val <= 63,
    { ~prm.subtractive.setFilterEnvAttackRatio(attackLow); },
    { ~prm.subtractive.setFilterEnvAttackRatio(attackHigh); }
  );
}, filterKnobBank, page);

// Filter Env Peak Ratio:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var peakLow = val.linlin(0, 63, 0.0001, 1);
  var peakHigh = val.linlin(64, 127, 1, 32);
  if ( val <= 63,
    { ~prm.subtractive.setFilterEnvPeakRatio(peakLow); },
    { ~prm.subtractive.setFilterEnvPeakRatio(peakHigh); }
  );
}, filterKnobBank, page);

// Filter Env Sustain Ratio:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var susLow = val.linlin(0, 63, 0.0001, 1);
  var susHigh = val.linlin(64, 127, 1, 32);
  if ( val <= 63,
    { ~prm.subtractive.setFilterEnvSustainRatio(susLow); },
    { ~prm.subtractive.setFilterEnvSustainRatio(susHigh); }
  );
}, filterKnobBank, page);

// Filter Env Release Ratio;
~ohm.setLeftKnobFunc(3, 0, { | val |
  var releaseLow = val.linlin(0, 63, 0.0001, 1);
  var releaseHigh = val.linlin(64, 127, 1, 32);
  if ( val <= 63,
    { ~prm.subtractive.setFilterEnvReleaseRatio(releaseLow); },
    { ~prm.subtractive.setFilterEnvReleaseRatio(releaseHigh); }
  );
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

// Filter Cutoff:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var cutoff = val.linexp(0, 127, 50, 20000);
  ~prm.subtractive.setFilterCutoff(cutoff);
}, filterKnobBank, page);

// Filter Res:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var res = val.linlin(0, 127, 0, 2.5);
  ~prm.subtractive.setFilterRes(res);
}, filterKnobBank, page);

// Filter Drive:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var drive = val.linlin(0, 127, 1, 100);
  ~prm.subtractive.setFilterDrive(drive);
}, filterKnobBank, page);


// Filter Type:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var type = val.linlin(0, 127, 0, 2);
  ~prm.subtractive.setFilterType(type);
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
  if( ~prm.subtractive.filterEnvLoop == 1,
    { ~prm.subtractive.setFilterEnvLoop(0); },
    { ~prm.subtractive.setFilterEnvLoop(1); }
  );
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




//////// Keyboard Grid:

