/*
Sunday, October 2nd 2016
SubtractiveOsc1.scd
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
~ohm.setGridFunc(4, 7, {
  ~ohm.setActiveGridBank(vcaGridBank, page);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(vcaButtonBank);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank);
  ~ohm.setActiveLeftSlidersBank(vcaSliderBank);
  ~ohm.setActiveCrossfaderBank(vcaCrossfaderBank);
}, bank: osc1GridBank, page: page);
~ohm.setGridFunc(5, 7, {
  ~ohm.setActiveGridBank(lfo1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo1ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo1SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo1CrossfaderBank);
}, bank: osc1GridBank, page: page);
~ohm.setGridFunc(6, 7, {
  ~ohm.setActiveGridBank(lfo2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo2ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo2SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo2CrossfaderBank);
}, bank: osc1GridBank, page: page);
~ohm.setGridFunc(7, 7, {
  ~ohm.setActiveGridBank(presetsGridBank, page);
  ~ohm.setActiveLeftKnobsBank(presetsKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(presetsButtonBank);
  ~ohm.setActiveLeftSlidersBank(presetsSliderBank);
  ~ohm.setActiveCrossfaderBank(presetsCrossfaderBank);
}, bank: osc1GridBank, page: page);

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
  var low = val.linexp(0, 127, 1, 0.25);
  var high = val.linexp(0, 127, 1, 2);
  ~prm.subtractive.setOsc1FreqLFO1BottomRatio(low);
  ~prm.subtractive.setOsc1FreqLFO1TopRatio(high);
}, osc1KnobBank, page);
// LFO 2 Osc 1 Freq:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var low = val.linlin(0, 127, 1, 0.25);
  var high = val.linlin(0, 127, 1, 2);
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






