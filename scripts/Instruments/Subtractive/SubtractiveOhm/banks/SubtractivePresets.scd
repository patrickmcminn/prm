/*
Sunday, October 2nd 2016
SubtractivePresets.scd
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

var presetArray = Array.newClear(48);
var presetNum = 0;


// presets:
~ohm.setGridFunc(0, 7, {
  ~ohm.setActiveGridBank(mainGridBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(mainButtonBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank);
  ~ohm.setActiveLeftSlidersBank(mainSliderBank);
  ~ohm.setActiveCrossfaderBank(mainCrossfaderBank);
}, bank: presetsGridBank, page: page);
~ohm.setGridFunc(1, 7, {
  ~ohm.setActiveGridBank(osc1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc1ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc1SliderBank);
  ~ohm.setActiveCrossfaderBank(osc1CrossfaderBank);
}, bank: presetsGridBank, page: page);
~ohm.setGridFunc(2, 7, {
  ~ohm.setActiveGridBank(osc2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc2ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc2SliderBank);
  ~ohm.setActiveCrossfaderBank(osc2CrossfaderBank);
}, bank: presetsGridBank, page: page);
~ohm.setGridFunc(3, 7, {
  ~ohm.setActiveGridBank(filterGridBank, page);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(filterButtonBank);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank);
  ~ohm.setActiveLeftSlidersBank(filterSliderBank);
  ~ohm.setActiveCrossfaderBank(filterCrossfaderBank);
}, bank: presetsGridBank, page: page);
~ohm.setGridFunc(4, 7, {
  ~ohm.setActiveGridBank(vcaGridBank, page);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(vcaButtonBank);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank);
  ~ohm.setActiveLeftSlidersBank(vcaSliderBank);
  ~ohm.setActiveCrossfaderBank(vcaCrossfaderBank);
}, bank: presetsGridBank, page: page);
~ohm.setGridFunc(5, 7, {
  ~ohm.setActiveGridBank(lfo1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo1ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo1SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo1CrossfaderBank);
}, bank: presetsGridBank, page: page);
~ohm.setGridFunc(6, 7, {
  ~ohm.setActiveGridBank(lfo2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo2ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo2SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo2CrossfaderBank);
}, bank: presetsGridBank, page: page);
~ohm.setGridFunc(7, 7, {
  ~ohm.setActiveGridBank(presetsGridBank, page);
  ~ohm.setActiveLeftKnobsBank(presetsKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(presetsButtonBank);
  ~ohm.setActiveLeftSlidersBank(presetsSliderBank);
  ~ohm.setActiveCrossfaderBank(presetsCrossfaderBank);
}, bank: presetsGridBank, page: page);

//////// grid:
~ohm.setGridMonitorFunc(0, 0, {
  6.do({ |row | 8.do({ | column |  ~ohm.turnGridBlue(column, row); }); });
  ~ohm.turnGridGreen(presetNum % 8, presetNum/8);
}, presetsGridBank, page);

// preset loading:
6.do({ | row | 8.do({ | column |
  ~ohm.setGridFunc(column, row, {
    presetNum = row + column;
    if( presetArray[row + column] != nil, { ~prm.subtractive.loadPreset(presetArray[column + row]); });
  }, 'noteOn', presetsGridBank, page);
  });
});

// parameter printing:
~ohm.turnGridMagenta(0, 6, presetsGridBank, page);
~ohm.setGridFunc(0, 6, { ~prm.subtractive.printAllParameters; }, 'noteOn', presetsGridBank, page);


//////// carryovers:
//// Left Knobs:

// 0, 0: Filter Cutoff:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var cutoff = val.linexp(0, 127, 60, 20000);
  ~prm.subtractive.setFilterCutoff(cutoff);
}, presetsKnobBank, page);

// 1, 0: Filter Resonance:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var res = val.linlin(0, 127, 0, 1.5);
  ~prm.subtractive.setFilterRes(res);
}, presetsKnobBank, page);

// 2, 0: LFO 1 to Cutoff:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.03125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  ~prm.subtractive.setFilterCutoffLFO1BottomRatio(amountLow);
  ~prm.subtractive.setFilterCutoffLFO1TopRatio(amountHigh);
}, presetsKnobBank, page);

// 3, 0: LFO 2 to Cutoff:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.03125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  ~prm.subtractive.setFilterCutoffLFO2BottomRatio(amountLow);
  ~prm.subtractive.setFilterCutoffLFO2TopRatio(amountHigh);
}, presetsKnobBank, page);

// 0, 1: Oscillator 1 Volume:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~prm.subtractive.setOsc1Vol(val.ccdbfs(-70, 0));
}, presetsKnobBank, page);

// 1, 1: Oscillator 1 Pulse Width:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var width = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc1PulseWidth(width);
}, presetsKnobBank, page);

// 2, 1: Oscillator 2 Volume:
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~prm.subtractive.setOsc2Vol(val.ccdbfs(-70, 0));
  val.postln;
}, presetsKnobBank, page);

// 3, 1: Oscillator 2 Pulse Width:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var width = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc2PulseWidth(width);
}, presetsKnobBank, page);

// 0, 2 : Oscillator 1 Pulse Width LFO 1:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var amountLow = val.linlin(0, 127, 0.0, -1.0);
  var amountHigh = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc1PulseWidthLFO1Bottom(amountLow);
  ~prm.subtractive.setOsc1PulseWidthLFO1Top(amountHigh);
}, presetsKnobBank, page);

// 1, 2 : Oscillator 1 Pulse Width LFO 1:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var amountLow = val.linlin(0, 127, 0.0, -1.0);
  var amountHigh = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setOsc2PulseWidthLFO1Bottom(amountLow);
  ~prm.subtractive.setOsc2PulseWidthLFO1Top(amountHigh);
}, presetsKnobBank, page);

// 2, 2 : LFO 1 Freq:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var freq = val.linexp(0, 127, 0.001, 60);
  ~prm.subtractive.setLFO1Freq(freq);
}, presetsKnobBank, page);

// 3, 2 : LFO 2 Freq:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var freq = val.linexp(0, 127, 0.001, 60);
  ~prm.subtractive.setLFO2Freq(freq);
}, presetsKnobBank, page);


//// Left Sliders:

// 0: Attack Time:
~ohm.setLeftSliderFunc(0, { | val |
  var attack = val.linlin(0, 127, 0, 7);
  ~prm.subtractive.setAttackTime(attack);
}, presetsSliderBank, page);

// 1: decayTime:
~ohm.setLeftSliderFunc(1, { | val |
  var decay = val.linlin(0, 127, 0, 7);
  ~prm.subtractive.setDecayTime(decay);
}, presetsSliderBank, page);

// 2: sustain Level:
~ohm.setLeftSliderFunc(2, { | val |
  var sustain = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setSustainLevel(sustain);
}, presetsSliderBank, page);

// 3: release time:
~ohm.setLeftSliderFunc(3, { | val |
  var release = val.linlin(0, 127, 0.0, 12);
  ~prm.subtractive.setReleaseTime(release);
}, presetsSliderBank, page);


//////// Presets:

// default:
presetArray[0] = 'default';
~prm.subtractive.savePreset(presetArray[0], [ 1, 1, 0, 1, 0.5, 0, 0.05, 0.05, 1, 0.5, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 2, 0.5, 0.5, 0, 0.5, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 3, 0.5, 0.25, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 1, 1, 1, 1, 0.05, 0, 0.05, false, 1, 1, 1, 1, 0, 0, 0, 0, 1, 2000, 0, 0, 1, 1, 1, 1, 0.05, 0.05, 1, 0.05, 0, 0, 0, 0, 0 ]);

// Music for Reemergence:
presetArray[1] = 'emerge';
~prm.subtractive.savePreset(presetArray[1], [ 1, 1, 5, 0.1, 0.5, 0, 0.05, 0.05, 0.11, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 0.4, 0.4, -0.4, 0.4, 0.4, 1, 1, 1, 0, 1.8, 0, 0, 0, 0.5, 0.5, 0, 2, 0, 1, 1, 0, 1, 1, 1, 1, 0.4, -0.4, 0.4, -0.4, 1, 0.4, 1, 1, 0.3, -0.9, 0, 0, 1, 0.5, 0.25, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 1, 1, 1, 1, 0.05, 0, 0.05, false, 0.7, 1.3, 1, 1, 0, 0, 0, 0, 1, 1500, 0.3, 0, 1, 1, 1, 1, 10, 0.05, 1, 10, 0, -1, 1, 0, 0 ]);


