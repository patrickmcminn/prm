/*
Sunday, October 2nd 2016
SubtractiveLFO1.scd
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

// bank set:
~ohm.setGridFunc(0, 7, {
  ~ohm.setActiveGridBank(mainGridBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(mainButtonBank);
  ~ohm.setActiveLeftKnobsBank(mainKnobBank);
  ~ohm.setActiveLeftSlidersBank(mainSliderBank);
  ~ohm.setActiveCrossfaderBank(mainCrossfaderBank);
}, bank: lfo1GridBank, page: page);
~ohm.setGridFunc(1, 7, {
  ~ohm.setActiveGridBank(osc1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc1ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc1KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc1SliderBank);
  ~ohm.setActiveCrossfaderBank(osc1CrossfaderBank);
}, bank: lfo1GridBank, page: page);
~ohm.setGridFunc(2, 7, {
  ~ohm.setActiveGridBank(osc2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(osc2ButtonBank);
  ~ohm.setActiveLeftKnobsBank(osc2KnobBank);
  ~ohm.setActiveLeftSlidersBank(osc2SliderBank);
  ~ohm.setActiveCrossfaderBank(osc2CrossfaderBank);
}, bank: lfo1GridBank, page: page);
~ohm.setGridFunc(3, 7, {
  ~ohm.setActiveGridBank(filterGridBank, page);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(filterButtonBank);
  ~ohm.setActiveLeftKnobsBank(filterKnobBank);
  ~ohm.setActiveLeftSlidersBank(filterSliderBank);
  ~ohm.setActiveCrossfaderBank(filterCrossfaderBank);
}, bank: lfo1GridBank, page: page);
~ohm.setGridFunc(4, 7, {
  ~ohm.setActiveGridBank(vcaGridBank, page);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(vcaButtonBank);
  ~ohm.setActiveLeftKnobsBank(vcaKnobBank);
  ~ohm.setActiveLeftSlidersBank(vcaSliderBank);
  ~ohm.setActiveCrossfaderBank(vcaCrossfaderBank);
}, bank: lfo1GridBank, page: page);
~ohm.setGridFunc(5, 7, {
  ~ohm.setActiveGridBank(lfo1GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo1KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo1ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo1SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo1CrossfaderBank);
}, bank: lfo1GridBank, page: page);
~ohm.setGridFunc(6, 7, {
  ~ohm.setActiveGridBank(lfo2GridBank, page);
  ~ohm.setActiveLeftKnobsBank(lfo2KnobBank, page);
  ~ohm.setActiveLeftButtonsBank(lfo2ButtonBank);
  ~ohm.setActiveLeftSlidersBank(lfo2SliderBank);
  ~ohm.setActiveCrossfaderBank(lfo2CrossfaderBank);
}, bank: lfo1GridBank, page: page);
~ohm.setGridFunc(7, 7, {
  ~ohm.setActiveGridBank(presetsGridBank, page);
  ~ohm.setActiveLeftKnobsBank(presetsKnobBank, page);
  ~ohm.setActiveLeftButtonsBank(presetsButtonBank);
  ~ohm.setActiveLeftSlidersBank(presetsSliderBank);
  ~ohm.setActiveCrossfaderBank(presetsCrossfaderBank);
}, bank: lfo1GridBank, page: page);

//////// sliders:

//// amplitude envelope:
// attack time:
~ohm.setLeftSliderFunc(0, { | val |
  var attack = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setAttackTime(attack);
}, lfo1SliderBank, page);
// decay time:
~ohm.setLeftSliderFunc(1, { | val |
  var decay = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setDecayTime(decay);
}, lfo1SliderBank, page);
// sustain level:
~ohm.setLeftSliderFunc(2, { | val |
  var sustain = val.linlin(0, 127, 0.0, 1.0);
  ~prm.subtractive.setSustainLevel(sustain);
}, lfo1SliderBank, page);
// release time:
~ohm.setLeftSliderFunc(3, { | val |
  var release = val.linlin(0, 127, 0, 10);
  ~prm.subtractive.setReleaseTime(release);
}, lfo1SliderBank, page);



//////// knobs:

///// carryovers:
// cutoff:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var cutoff = val.linexp(0, 127, 50, 20000);
  ~prm.subtractive.setFilterCutoff(cutoff);
}, lfo1KnobBank, page);
// res:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var res = val.linlin(0, 127, 0, 2.5);
  ~prm.subtractive.setFilterRes(res);
}, lfo1KnobBank, page);
// filter lfo 1 cutoff:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var amountLow = val.linlin(0, 127, 1, 0.03125);
  var amountHigh = val.linlin(0, 127, 1, 32);
  ~prm.subtractive.setFilterCutoffLFO1BottomRatio(amountLow);
  ~prm.subtractive.setFilterCutoffLFO1TopRatio(amountHigh);
}, lfo1KnobBank, page);
// amp lfo 1:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var amount = val.linlin(0, 127, 1, 0);
  ~prm.subtractive.setAmplitudeLFO1Bottom(amount);
}, lfo1KnobBank, page);

// lfo 1 freq:
~ohm.setLeftKnobFunc(0, 1, { | val |
  var freq = val.linexp(0, 127, 0.001, 220);
  ~prm.subtractive.setLFO1Freq(freq);
}, lfo1KnobBank, page);
// lfo 1 Pulse Width:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var width = val.linlin(0, 127, 0, 1);
  ~prm.subtractive.setLFO1PulseWidth(width);
}, lfo1KnobBank, page);
// lfo 1 waveform:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var waveform = val.linlin(0, 127, 0, 5);
  ~prm.subtractive.setLFO1Waveform(waveform);
}, lfo1KnobBank, page);
// lfo 1 frequency modulation:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var amountLow = val.linexp(0, 127, 1, 0.025);
  var amountHigh = val.linlin(0, 127, 1, 16);
  ~prm.subtractive.setLFO1FreqLFO2BottomRatio(amountLow);
  ~prm.subtractive.setLFO1FreqLFO2TopRatio(amountHigh);
}, lfo1KnobBank, page);


// lfo 1 attack time:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var attack = val.linlin(0, 127, 0, 5);
  ~prm.subtractive.setLFO1AttackTime(attack);
}, lfo1KnobBank, page);
// lfo 1 release time:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var release = val.linlin(0, 127, 0, 5);
  ~prm.subtractive.setLFO1ReleaseTime(release);
}, lfo1KnobBank, page);
// lfo 2 freq:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var freq = val.linexp(0, 127, 0.001, 60);
  ~prm.subtractive.setLFO2Freq(freq);
}, lfo1KnobBank, page);
// lfo 2 waveform:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var waveform = val.linlin(0, 127, 0, 5);
  ~prm.subtractive.setLFO2Waveform(waveform);
}, lfo1KnobBank, page);


//////// grid:

//// Envelope Type:

// monitor func:
~ohm.setGridMonitorFunc(0, 0, {
  4.do({ | i | ~ohm.turnGridCyan(i, 0); });
  ~ohm.turnGridGreen(~prm.subtractive.lfo1EnvType, 0);
}, lfo1GridBank, page);
// none:
~ohm.setGridFunc(0, 0, { ~prm.subtractive.setLFO1EnvType('none'); }, bank: lfo1GridBank, page: page);
// attack:
~ohm.setGridFunc(1, 0, { ~prm.subtractive.setLFO1EnvType('attack'); }, bank: lfo1GridBank, page: page);
// release:
~ohm.setGridFunc(2, 0, { ~prm.subtractive.setLFO1EnvType('release'); }, bank: lfo1GridBank, page: page);
// attack and release:
~ohm.setGridFunc(3, 0, { ~prm.subtractive.setLFO1EnvType('attackAndRelease'); }, bank: lfo1GridBank, page: page);

// LFO 1 Waveforms:

// sine:
~ohm.setGridMonitorFunc(2, 4, {
  if( ~prm.subtractive.lfo1Waveform == 0,
    { ~ohm.turnGridGreen(2, 4, lfo1GridBank, page); }, {~ohm.turnGridWhite(2, 4, lfo1GridBank, page); });
}, lfo1GridBank, page);
~ohm.setGridFunc(2, 4, { ~prm.subtractive.setLFO1Waveform('sine'); }, bank: lfo1GridBank, page: page);

// saw:
~ohm.setGridMonitorFunc(3, 4, {
  if( (~prm.subtractive.lfo1Waveform >=1) && (~prm.subtractive.lfo1Waveform < 2),
    { ~ohm.turnGridGreen(3, 4, lfo1GridBank, page); }, { ~ohm.turnGridBlue(3, 4, lfo1GridBank, page); });
}, lfo1GridBank, page);
~ohm.setGridFunc(3, 4, { ~prm.subtractive.setLFO1Waveform('saw'); }, bank: lfo1GridBank, page: page);

// revSaw:
~ohm.setGridMonitorFunc(4, 4, {
  if( (~prm.subtractive.lfo1Waveform >=2) && (~prm.subtractive.lfo1Waveform <3),
    { ~ohm.turnGridGreen(4, 4, lfo1GridBank, page); }, { ~ohm.turnGridYellow(4, 4, lfo1GridBank, page); });
}, lfo1GridBank, page);
~ohm.setGridFunc(4, 4, { ~prm.subtractive.setLFO1Waveform('revSaw'); }, bank: lfo1GridBank, page: page);

// rect:
~ohm.setGridMonitorFunc(5, 4, {
  if( (~prm.subtractive.lfo1Waveform >=3) && (~prm.subtractive.lfo1Waveform < 4),
    { ~ohm.turnGridGreen(5, 4, lfo1GridBank, page); }, { ~ohm.turnGridMagenta(5, 4, lfo1GridBank, page); });
}, lfo1GridBank, page);
~ohm.setGridFunc(5, 4, { ~prm.subtractive.setLFO1Waveform('rect'); }, bank: lfo1GridBank, page: page);

// noise:
~ohm.setGridMonitorFunc(7, 4, {
  if (~prm.subtractive.lfo1Waveform >=5,
    { ~ohm.turnGridGreen(7, 4, lfo1GridBank, page); }, { ~ohm.turnGridRed(7, 4, lfo1GridBank, page); });
}, lfo1GridBank, page);
~ohm.setGridFunc(7, 4, { ~prm.subtractive.setLFO1Waveform('noise'); }, bank: lfo1GridBank, page: page);

// sample and hold:
~ohm.setGridMonitorFunc(6, 4, {
  if( ( ~prm.subtractive.lfo1Waveform >=4) && (~prm.subtractive.lfo1Waveform <5),
    { ~ohm.turnGridGreen(6, 4, lfo1GridBank, page); }, { ~ohm.turnGridCyan(6, 4, lfo1GridBank, page); });
}, lfo1GridBank, page);
~ohm.setGridFunc(6, 4, { ~prm.subtractive.setLFO1Waveform('sampleAndHold'); }, bank: lfo1GridBank, page: page);