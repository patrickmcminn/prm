/*
Sunday, October 2nd 2016
SubtractiveVCA.scd
next attempt at making a control surface page for the Subtractive synth
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

// VCA:
~ohm.setGridFunc(0, 7, {
  ~ohm.setActiveGridBank(mainGridBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(mainButtonBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank);
  ~ohm.setActiveLeftSlidersBank(mainSliderBank);
  ~ohm.setActiveCrossfaderBank(mainCrossfaderBank);
}, bank: vcaGridBank, page: page);
~ohm.setGridFunc(1, 7, {
  ~ohm.setActiveGridBank(osc1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc1ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc1SliderBank);
  ~ohm.setActiveCrossfaderBank(osc1CrossfaderBank);
}, bank: vcaGridBank, page: page);
~ohm.setGridFunc(2, 7, {
  ~ohm.setActiveGridBank(osc2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc2ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc2SliderBank);
  ~ohm.setActiveCrossfaderBank(osc2CrossfaderBank);
}, bank: vcaGridBank, page: page);
~ohm.setGridFunc(3, 7, {
  ~ohm.setActiveGridBank(filterGridBank, page);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(filterButtonBank);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank);
  ~ohm.setActiveLeftSlidersBank(filterSliderBank);
  ~ohm.setActiveCrossfaderBank(filterCrossfaderBank);
}, bank: vcaGridBank, page: page);
~ohm.setGridFunc(4, 7, {
  ~ohm.setActiveGridBank(vcaGridBank, page);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(vcaButtonBank);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank);
  ~ohm.setActiveLeftSlidersBank(vcaSliderBank);
  ~ohm.setActiveCrossfaderBank(vcaCrossfaderBank);
}, bank: vcaGridBank, page: page);
~ohm.setGridFunc(5, 7, {
  ~ohm.setActiveGridBank(lfo1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo1ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo1SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo1CrossfaderBank);
}, bank: vcaGridBank, page: page);
~ohm.setGridFunc(6, 7, {
  ~ohm.setActiveGridBank(lfo2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo2ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo2SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo2CrossfaderBank);
}, bank: vcaGridBank, page: page);
~ohm.setGridFunc(7, 7, {
  ~ohm.setActiveGridBank(presetsGridBank, page);
  ~ohm.setActiveLeftKnobsBank(presetsKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(presetsButtonBank);
  ~ohm.setActiveLeftSlidersBank(presetsSliderBank);
  ~ohm.setActiveCrossfaderBank(presetsCrossfaderBank);
}, bank: vcaGridBank, page: page);

//////// VCA:

//// sliders:

// attack time:
~ohm.setLeftSliderFunc(0, { | val |
  var attack = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setAttackTime(attack);
}, vcaSliderBank, page);
// decay time:
~ohm.setLeftSliderFunc(1, { | val |
  var decay = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setDecayTime(decay);
}, vcaSliderBank, page);
// sustain level:
~ohm.setLeftSliderFunc(2, { | val |
  var sustain = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setSustainLevel(sustain);
}, vcaSliderBank, page);
// release time:
~ohm.setLeftSliderFunc(3, { | val |
  var release = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setReleaseTime(release);
}, vcaSliderBank, page);


//// knobs:

// noise osc vol:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~prm.subtractive.setNoiseOscVol(val.ccdbfs(-70, 0));
}, vcaKnobBank, page);
// noise osc cutoff:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var cutoff = val.linexp(0, 127, 50, 20000);
  ~prm.subtractive.setNoiseOscCutoff(cutoff);
}, vcaKnobBank, page);
// pan:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var pan = val.linlin(0, 127, -1, 1);
  ~prm.subtractive.setPan(pan);
}, vcaKnobBank, page);
// vol:
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~prm.subtractive.mixer.setVol(val.ccdbfs(-70, 0));
}, vcaKnobBank, page);

// noise osc amp lfo 1:
~ohm.setLeftKnobFunc(0, 1, { | val |
  var amount = val.linlin(0, 127, 1, 0);
  ~prm.subtractive.setNoiseOscAmpLFO1Bottom(amount);
}, vcaKnobBank, page);
// noise osc amp lfo 2:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var amount = val.linlin(0, 127, 1, 0);
  ~prm.subtractive.setNoiseOscAmpLFO2Bottom(amount);
}, vcaKnobBank, page);
// noise osc filter lfo 1:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.00125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  ~prm.subtractive.setNoiseOscFilterLFO1BottomRatio(amountLow);
  ~prm.subtractive.setNoiseOscFilterLFO1TopRatio(amountHigh);
}, vcaKnobBank, page);
// noise osc filter lfo 2:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.00125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  ~prm.subtractive.setNoiseOscFilterLFO2BottomRatio(amountLow);
  ~prm.subtractive.setNoiseOscFilterLFO2TopRatio(amountHigh);
}, vcaKnobBank, page);

// noise osc filter lfo 2:

// amp lfo 1:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var amount = val.linlin(0, 127, 1, 0);
  ~prm.subtractive.setAmplitudeLFO1Bottom(amount);
}, vcaKnobBank, page);
// amp lfo 2:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var amount = val.linlin(0, 127, 1, 0);
  ~prm.subtractive.setAmplitudeLFO2Bottom(amount);
}, vcaKnobBank, page);
// pan lfo 1:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var amountLow = val.linlin(0, 127, 0, -1);
  var amountHigh = val.linlin(0, 127, 0, 1);
  ~prm.subtractive.setPanLFO1Bottom(amountLow);
  ~prm.subtractive.setPanLFO1Top(amountHigh);
}, vcaKnobBank, page);
// pan lfo 2:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var amountLow = val.linlin(0, 127, 0, -1);
  var amountHigh = val.linlin(0, 127, 0, 1);
  ~prm.subtractive.setPanLFO2Bottom(amountLow);
  ~prm.subtractive.setPanLFO2Top(amountHigh);
}, vcaKnobBank, page);


// grid:

