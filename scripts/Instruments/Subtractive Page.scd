/*
Monday, September 12th 2016
Subtractive Page.scd
next attempt at making a control surface page for the Subtractive synth
prm
*/

var page = 'subtractive';

var mainKnobBank = 0;

var mainGridBank = 0;
var keyboardGridBank = 0;

var mainSliderBank = 0;

var mainCrossfaderBank = 0;

~ohm.makePage(page);

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
  var attack = val.linexp(0, 127, 0, 7);
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
  var release = val.linexp(0, 127, 0.0, 12);
  ~prm.subtractive.setReleaseTime(release);
}, mainSliderBank, page);

////// Grid:

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





