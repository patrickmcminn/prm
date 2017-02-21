/*
Sunday, October 2nd 2016
SubtractiveLFO2.scd
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

var cutoffLFO2Invert = false;
var lfo1FreqInvert = false;

// lfo 2:
~ohm.setGridFunc(0, 7, {
  ~ohm.setActiveGridBank(mainGridBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(mainButtonBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank);
  ~ohm.setActiveLeftSlidersBank(mainSliderBank);
  ~ohm.setActiveCrossfaderBank(mainCrossfaderBank);
}, bank: lfo2GridBank, page: page);
~ohm.setGridFunc(1, 7, {
  ~ohm.setActiveGridBank(osc1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc1ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc1SliderBank);
  ~ohm.setActiveCrossfaderBank(osc1CrossfaderBank);
}, bank: lfo2GridBank, page: page);
~ohm.setGridFunc(2, 7, {
  ~ohm.setActiveGridBank(osc2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc2ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc2SliderBank);
  ~ohm.setActiveCrossfaderBank(osc2CrossfaderBank);
}, bank: lfo2GridBank, page: page);
~ohm.setGridFunc(3, 7, {
  ~ohm.setActiveGridBank(filterGridBank, page);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(filterButtonBank);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank);
  ~ohm.setActiveLeftSlidersBank(filterSliderBank);
  ~ohm.setActiveCrossfaderBank(filterCrossfaderBank);
}, bank: lfo2GridBank, page: page);
~ohm.setGridFunc(4, 7, {
  ~ohm.setActiveGridBank(vcaGridBank, page);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(vcaButtonBank);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank);
  ~ohm.setActiveLeftSlidersBank(vcaSliderBank);
  ~ohm.setActiveCrossfaderBank(vcaCrossfaderBank);
}, bank: lfo2GridBank, page: page);
~ohm.setGridFunc(5, 7, {
  ~ohm.setActiveGridBank(lfo1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo1ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo1SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo1CrossfaderBank);
}, bank: lfo2GridBank, page: page);
~ohm.setGridFunc(6, 7, {
  ~ohm.setActiveGridBank(lfo2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo2ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo2SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo2CrossfaderBank);
}, bank: lfo2GridBank, page: page);
~ohm.setGridFunc(7, 7, {
  ~ohm.setActiveGridBank(presetsGridBank, page);
  ~ohm.setActiveLeftKnobsBank(presetsKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(presetsButtonBank);
  ~ohm.setActiveLeftSlidersBank(presetsSliderBank);
  ~ohm.setActiveCrossfaderBank(presetsCrossfaderBank);
}, bank: lfo2GridBank, page: page);

//////// control:

//////// sliders:
//// amplitude envelope:
// attack time:
~ohm.setLeftSliderFunc(0, { | val |
  var attack = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setAttackTime(attack);
}, lfo2SliderBank, page);
// decay time:
~ohm.setLeftSliderFunc(1, { | val |
  var decay = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setDecayTime(decay);
}, lfo2SliderBank, page);
// sustain level:
~ohm.setLeftSliderFunc(2, { | val |
  var sustain = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setSustainLevel(sustain);
}, lfo2SliderBank, page);
// release time:
~ohm.setLeftSliderFunc(3, { | val |
  var release = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setReleaseTime(release);
}, lfo2SliderBank, page);


//////// knobs:
///// carryovers:
// cutoff:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var cutoff = val.linexp(0, 127, 50, 20000);
  ~prm.subtractive.setFilterCutoff(cutoff);
}, lfo2KnobBank, page);
// res:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var res = val.linlin(0, 127, 0, 2.5);
  ~prm.subtractive.setFilterRes(res);
}, lfo2KnobBank, page);
// filter lfo 2 cutoff:
~ohm.setLeftKnobFunc(2, 0, { | val |
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
}, lfo2KnobBank, page);
// amp lfo 2:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var amount = val.linlin(0, 127, 1, 0);
  ~prm.subtractive.setAmplitudeLFO2Bottom(amount);
}, lfo2KnobBank, page);

// freq:
~ohm.setLeftKnobFunc(0, 1, { | val |
  var freq = val.linexp(0, 127, 0.001, 220);
  ~prm.subtractive.setLFO2Freq(freq);
}, lfo2KnobBank, page);
// pulse width:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var width = val.linlin(0, 127, 0, 1);
  ~prm.subtractive.setLFO2PulseWidth(width);
}, lfo2KnobBank, page);
// waveform:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var waveform = val.linlin(0, 127, 0, 5);
  ~prm.subtractive.setLFO2Waveform(waveform);
}, lfo2KnobBank, page);
// lfo 1 frequency modulation:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var amountLow = val.linexp(0, 127, 1, 0.025);
  var amountHigh = val.linlin(0, 127, 1, 16);
  if( lfo1FreqInvert == false,
    {
      ~prm.subtractive.setLFO1FreqLFO2BottomRatio(amountLow);
      ~prm.subtractive.setLFO1FreqLFO2TopRatio(amountHigh);
    },
    {
      ~prm.subtractive.setLFO1FreqLFO2BottomRatio(amountHigh);
      ~prm.subtractive.setLFO1FreqLFO2TopRatio(amountLow);
  });
}, lfo2KnobBank, page);



// filter cutoff lfo 1:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.03125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  ~prm.subtractive.setFilterCutoffLFO1BottomRatio(amountLow);
  ~prm.subtractive.setFilterCutoffLFO1TopRatio(amountHigh);
}, lfo2KnobBank, page);
// filter cutoff lfo 2:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.03125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  ~prm.subtractive.setFilterCutoffLFO2BottomRatio(amountLow);
  ~prm.subtractive.setFilterCutoffLFO2TopRatio(amountHigh);
}, lfo2KnobBank, page);
// lfo 1 freq:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var freq = val.linexp(0, 127, 0.001, 60);
  ~prm.subtractive.setLFO1Freq(freq);
}, lfo2KnobBank, page);
// lfo 1 waveform:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var waveform = val.linlin(0, 127, 0, 5);
  ~prm.subtractive.setLFO1Waveform(waveform);
}, lfo2KnobBank, page);

//////// grid:


//// LFO 2 Waveforms:

// sine:
~ohm.setGridMonitorFunc(2, 5, {
  if( ~prm.subtractive.lfo2Waveform == 0,
    { ~ohm.turnGridGreen(2, 5, lfo2GridBank, page); }, {~ohm.turnGridWhite(2, 5, lfo2GridBank, page); });
}, lfo2GridBank, page);
~ohm.setGridFunc(2, 5, { ~prm.subtractive.setLFO2Waveform('sine'); }, bank: lfo2GridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(3, 5, {
  if( (~prm.subtractive.lfo2Waveform >=1) && (~prm.subtractive.lfo2Waveform < 2),
    { ~ohm.turnGridGreen(3, 5, lfo2GridBank, page); }, { ~ohm.turnGridBlue(3, 5, lfo2GridBank, page); });
}, lfo2GridBank, page);
~ohm.setGridFunc(3, 5, { ~prm.subtractive.setLFO2Waveform('saw'); }, bank: lfo2GridBank, page: page);

// revSaw:
~ohm.setGridMonitorFunc(4, 5, {
  if( (~prm.subtractive.lfo2Waveform >=2) && (~prm.subtractive.lfo2Waveform <3),
    { ~ohm.turnGridGreen(4, 5, lfo2GridBank, page); }, { ~ohm.turnGridYellow(4, 5, lfo2GridBank, page); });
}, lfo2GridBank, page);
~ohm.setGridFunc(4, 5, { ~prm.subtractive.setLFO2Waveform('revSaw'); }, bank: lfo2GridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(5, 5, {
  if( (~prm.subtractive.lfo2Waveform >=3) && (~prm.subtractive.lfo2Waveform < 4),
    { ~ohm.turnGridGreen(5, 5, lfo2GridBank, page); }, { ~ohm.turnGridMagenta(5, 5, lfo2GridBank, page); });
}, lfo2GridBank, page);
~ohm.setGridFunc(5, 5, { ~prm.subtractive.setLFO2Waveform('rect'); }, bank: lfo2GridBank, page: page);

// noise:
~ohm.setGridMonitorFunc(7, 5, {
  if (~prm.subtractive.lfo2Waveform >=5,
    { ~ohm.turnGridGreen(7, 5, lfo2GridBank, page); }, { ~ohm.turnGridRed(7, 5, lfo2GridBank, page); });
}, lfo2GridBank, page);
~ohm.setGridFunc(7, 5, { ~prm.subtractive.setLFO2Waveform('noise'); }, bank: lfo2GridBank, page: page);

// sample and hold:
~ohm.setGridMonitorFunc(6, 5, {
  if( ( ~prm.subtractive.lfo2Waveform >=4) && (~prm.subtractive.lfo2Waveform <5),
    { ~ohm.turnGridGreen(6, 5, lfo2GridBank, page); }, { ~ohm.turnGridCyan(6, 5, lfo2GridBank, page); });
}, lfo2GridBank, page);
~ohm.setGridFunc(6, 5, { ~prm.subtractive.setLFO2Waveform('sampleAndHold'); }, bank: lfo2GridBank, page: page);

////// LFO Inversions:

// LFO 1 Filter Cutoff:
~ohm.setGridMonitorFunc(6, 6, {
  if( cutoffLFO2Invert == false, { ~ohm.turnGridBlue(6, 6); }, { ~ohm.turnGridWhite(6, 6); });
}, lfo2GridBank, page);
~ohm.setGridFunc(6, 6, {
  if( cutoffLFO2Invert == false, { cutoffLFO2Invert = true }, { cutoffLFO2Invert = false });
}, bank: lfo2GridBank, page: page);

// LFO 1 LFO 2 Freq Mod:
~ohm.setGridMonitorFunc(7, 6, {
  if( lfo1FreqInvert == false, { ~ohm.turnGridBlue(7, 6); }, { ~ohm.turnGridWhite(7, 6); });
}, lfo2GridBank, page);
~ohm.setGridFunc(7, 6, {
  if( lfo1FreqInvert == false, { lfo1FreqInvert = true }, { lfo1FreqInvert = false });
}, bank:lfo2GridBank, page: page);