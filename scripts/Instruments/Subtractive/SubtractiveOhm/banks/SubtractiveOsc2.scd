/*
Sunday, October 2nd 2016
SubtractiveOsc2.scd
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

// grid change:
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
~ohm.setGridFunc(4, 7, {
  ~ohm.setActiveGridBank(vcaGridBank, page);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(vcaButtonBank);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank);
  ~ohm.setActiveLeftSlidersBank(vcaSliderBank);
  ~ohm.setActiveCrossfaderBank(vcaCrossfaderBank);
}, bank: osc2GridBank, page: page);
~ohm.setGridFunc(5, 7, {
  ~ohm.setActiveGridBank(lfo1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo1ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo1SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo1CrossfaderBank);
}, bank: osc2GridBank, page: page);
~ohm.setGridFunc(6, 7, {
  ~ohm.setActiveGridBank(lfo2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo2ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo2SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo2CrossfaderBank);
}, bank: osc2GridBank, page: page);
~ohm.setGridFunc(7, 7, {
  ~ohm.setActiveGridBank(presetsGridBank, page);
  ~ohm.setActiveLeftKnobsBank(presetsKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(presetsButtonBank);
  ~ohm.setActiveLeftSlidersBank(presetsSliderBank);
  ~ohm.setActiveCrossfaderBank(presetsCrossfaderBank);
}, bank: osc2GridBank, page: page);

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
  var low = val.linexp(0, 127, 1, 0.25);
  var high = val.linexp(0, 127, 1, 2);
  ~prm.subtractive.setOsc2FreqLFO1BottomRatio(low);
  ~prm.subtractive.setOsc2FreqLFO1TopRatio(high);
}, osc2KnobBank, page);
// LFO 2 Osc 2 Freq:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var low = val.linlin(0, 127, 1, 0.25);
  var high = val.linlin(0, 127, 1, 2);
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

