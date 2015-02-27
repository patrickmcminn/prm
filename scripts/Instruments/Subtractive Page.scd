/*
Saturday, July 26th 2014
Subtractive Page.scd
prm
*/

var page = 'subtractive';
var mainBank = 0;
var osc1Bank = 1;
var osc2Bank = 2;
var noiseOscBank = 3;
var filterBank = 4;
var lfo1Bank = 5;
var lfo2Bank = 6;
var ampBank = 7;

var controlBank = 0;
var keyboardBank1 = 1;

var cutoff = 2500;
var lfo1Freq = 1;
var lfo2Freq = 2;

var bankSelectReset;

var lfo1WaveshapeReset;
var lfo2WaveshapeReset;
var osc1WaveshapeReset;
var osc2WaveshapeReset;
var osc1OctaveReset;
var osc2OctaveReset;

var filterSelectReset;


// (if needed)
//~ohm = OhmRGB.new;
//~sub = Subtractive.new;

MIDIIn.connectAll;
~inPort = MIDIIn.findPort("LPK25", "LPK25");
127.do({ | note |
  MIDIFunc.noteOn({ | vel |
    var amp = vel.linlin(0, 127, 0, 1);
    ~sub.playNote(note.midicps);
  }, note, nil, ~inPort.uid);
  MIDIFunc.noteOff({
    ~sub.releaseNote(note.midicps);
  }, note, nil, ~inPort.uid);
});

////////
~ohm.makePage(page);
~ohm.setPage(page);
~ohm.addLeftKnobsBanks(7, page: page);
~ohm.addLeftSlidersBanks(7, page: page);
~ohm.addLeftButtonsBanks(7, page: page);
~ohm.addGridBanks(2, page: page);

//////// Grid:

//// Control Grid:
~ohm.turnGridRed(7, 7, bank: controlBank, page: page);
~ohm.setGridFunc(7, 7, { ~sub.releaseAllNotes; }, page: page, bank: controlBank);

// bank selection:
bankSelectReset = {
  // main:
  ~ohm.turnGridBlue(0, 0, page: page, bank: controlBank);
  // lfo1:
  ~ohm.turnGridYellow(1, 0, page: page, bank: controlBank);
  // lfo2:
  ~ohm.turnGridYellow(2, 0, page: page, bank: controlBank);
  // osc1:
  ~ohm.turnGridCyan(3, 0, page: page, bank: controlBank);
  // osc2:
  ~ohm.turnGridCyan(4, 0, page: page, bank: controlBank);
  // filter:
  ~ohm.turnGridPurple(5, 0, page: page, bank: controlBank);
  //ampEnv:
  ~ohm.turnGridPurple(6, 0, page: page, bank: controlBank);
};

bankSelectReset.value;
~ohm.turnGridGreen(0, 0, page: page, bank: controlBank);
// main bank:
~ohm.setGridFunc(0, 0, {
  ~ohm.setActiveLeftKnobsBank(controlBank);
  ~ohm.setActiveLeftSlidersBank(controlBank);
  ~ohm.setActiveLeftButtonsBank(controlBank);
  bankSelectReset.value;
  ~ohm.turnGridGreen(0, 0);
}, page: page, bank: controlBank);
// LFO 1 Bank:
~ohm.setGridFunc(1, 0, {
  ~ohm.setActiveLeftKnobsBank(lfo1Bank);
  ~ohm.setActiveLeftSlidersBank(lfo1Bank);
  ~ohm.setActiveLeftButtonsBank(lfo1Bank);
  bankSelectReset.value;
  ~ohm.turnGridGreen(1, 0);
}, page: page, bank: controlBank);
// LFO 2 Bank:
~ohm.setGridFunc(2, 0, {
  ~ohm.setActiveLeftKnobsBank(lfo2Bank);
  ~ohm.setActiveLeftSlidersBank(lfo2Bank);
  ~ohm.setActiveLeftButtonsBank(lfo2Bank);
  bankSelectReset.value;
  ~ohm.turnGridGreen(2, 0);
}, page: page, bank: controlBank);
// osc 1 bank:
~ohm.setGridFunc(3, 0, {
  ~ohm.setActiveLeftKnobsBank(osc1Bank);
  ~ohm.setActiveLeftSlidersBank(osc1Bank);
  ~ohm.setActiveLeftButtonsBank(osc1Bank);
  bankSelectReset.value;
  ~ohm.turnGridGreen(3, 0);
}, page: page, bank: controlBank);
// osc 2 bank:
~ohm.setGridFunc(4, 0, {
  ~ohm.setActiveLeftKnobsBank(osc2Bank);
  ~ohm.setActiveLeftSlidersBank(osc2Bank);
  ~ohm.setActiveLeftButtonsBank(osc2Bank);
  bankSelectReset.value;
  ~ohm.turnGridGreen(4, 0);
}, page: page, bank: controlBank);
// Filter Bank:
~ohm.setGridFunc(5, 0, {
  ~ohm.setActiveLeftKnobsBank(filterBank);
  ~ohm.setActiveLeftSlidersBank(filterBank);
  ~ohm.setActiveLeftButtonsBank(filterBank);
  bankSelectReset.value;
  ~ohm.turnGridGreen(5, 0);
}, page: page, bank: controlBank);
// Amp Env Bank:
~ohm.setGridFunc(6, 0, {
  ~ohm.setActiveLeftKnobsBank(ampBank);
  ~ohm.setActiveLeftSlidersBank(ampBank);
  ~ohm.setActiveLeftButtonsBank(ampBank);
  bankSelectReset.value;
  ~ohm.turnGridGreen(6, 0);
}, page: page, bank: controlBank);



// lfo1:
lfo1WaveshapeReset =  {
  ~ohm.turnGridBlue(0, 2, page: page, bank: controlBank);
  ~ohm.turnGridBlue(0, 3, page: page, bank: controlBank);
  ~ohm.turnGridBlue(1, 2, page: page, bank: controlBank);
  ~ohm.turnGridBlue(1, 3, page: page, bank: controlBank);
  ~ohm.turnGridBlue(2, 2, page: page, bank: controlBank);
  ~ohm.turnGridBlue(2, 3, page: page, bank: controlBank);
};
lfo1WaveshapeReset.value;
~ohm.turnGridGreen(0, 2, page: page, bank: controlBank);
~ohm.setGridFunc(0, 2, {
  ~sub.setLFO1Waveform('sine');
  lfo1WaveshapeReset.value;
  ~ohm.turnGridGreen(0, 2);
}, page: page, bank: controlBank);
~ohm.setGridFunc(0, 3, {
  ~sub.setLFO1Waveform('saw');
  lfo1WaveshapeReset.value;
  ~ohm.turnGridGreen(0, 3);
}, page: page, bank: controlBank);
~ohm.setGridFunc(1, 2, {
  ~sub.setLFO1Waveform('revSaw');
  lfo1WaveshapeReset.value;
  ~ohm.turnGridGreen(1, 2);
}, page: page, bank: controlBank);
~ohm.setGridFunc(1, 3, {
  ~sub.setLFO1Waveform('rect');
  lfo1WaveshapeReset.value;
  ~ohm.turnGridGreen(1, 3);
}, page: page, bank: controlBank);
~ohm.setGridFunc(2, 2, {
  ~sub.setLFO1Waveform('sampleAndHold');
  lfo1WaveshapeReset.value;
  ~ohm.turnGridGreen(2, 2);
}, page: page, bank: controlBank);
~ohm.setGridFunc(2, 3, {
  ~sub.setLFO1Waveform('noise');
  lfo1WaveshapeReset.value;
  ~ohm.turnGridGreen(2, 3);
}, page: page, bank: controlBank);

// lfo2:
lfo2WaveshapeReset =  {
  ~ohm.turnGridCyan(4, 2, page: page, bank: controlBank);
  ~ohm.turnGridCyan(4, 3, page: page, bank: controlBank);
  ~ohm.turnGridCyan(5, 2, page: page, bank: controlBank);
  ~ohm.turnGridCyan(5, 3, page: page, bank: controlBank);
  ~ohm.turnGridCyan(6, 2, page: page, bank: controlBank);
  ~ohm.turnGridCyan(6, 3, page: page, bank: controlBank);
};
lfo2WaveshapeReset.value;
~ohm.turnGridGreen(4, 2, page: page, bank: controlBank);
~ohm.setGridFunc(4, 2, {
  ~sub.setLFO2Waveform('sine');
  lfo2WaveshapeReset.value;
  ~ohm.turnGridGreen(4, 2);
}, page: page, bank: controlBank);
~ohm.setGridFunc(4, 3, {
  ~sub.setLFO2Waveform('saw');
  lfo2WaveshapeReset.value;
  ~ohm.turnGridGreen(4, 3);
}, page: page, bank: controlBank);
~ohm.setGridFunc(5, 2, {
  ~sub.setLFO2Waveform('revSaw');
  lfo2WaveshapeReset.value;
  ~ohm.turnGridGreen(5, 2);
}, page: page, bank: controlBank);
~ohm.setGridFunc(5, 3, {
  ~sub.setLFO2Waveform('rect');
  lfo2WaveshapeReset.value;
  ~ohm.turnGridGreen(5, 3);
}, page: page, bank: controlBank);
~ohm.setGridFunc(6, 2, {
  ~sub.setLFO2Waveform('sampleAndHold');
  lfo2WaveshapeReset.value;
  ~ohm.turnGridGreen(6, 2);
}, page: page, bank: controlBank);
~ohm.setGridFunc(6, 3, {
  ~sub.setLFO2Waveform('noise');
  lfo2WaveshapeReset.value;
  ~ohm.turnGridGreen(6, 3);
}, page: page, bank: controlBank);


// osc1:

// Waveform:
osc1WaveshapeReset = {
  ~ohm.turnGridPurple(0, 4, page: page);
  ~ohm.turnGridPurple(1, 4, page: page);
  ~ohm.turnGridPurple(2, 4, page: page);
  ~ohm.turnGridPurple(3, 4, page: page);
};
osc1WaveshapeReset.value;
~ohm.turnGridGreen(2, 4, page: page, bank: controlBank);
~ohm.setGridFunc(0, 4, {
  ~sub.setOsc1Waveform('sine');
  osc1WaveshapeReset.value;
  ~ohm.turnGridGreen(0, 4);
}, page: page, bank: controlBank);
~ohm.setGridFunc(1, 4, {
  ~sub.setOsc1Waveform('tri');
  osc1WaveshapeReset.value;
  ~ohm.turnGridGreen(1, 4);
}, page: page, bank: controlBank);
~ohm.setGridFunc(2, 4, {
  ~sub.setOsc1Waveform('saw');
  osc1WaveshapeReset.value;
  ~ohm.turnGridGreen(2, 4);
}, page: page, bank: controlBank);
~ohm.setGridFunc(3, 4, {
  ~sub.setOsc1Waveform('rect');
  osc1WaveshapeReset.value;
  ~ohm.turnGridGreen(3, 4);
}, page: page, bank: controlBank);

// Octave:
osc1OctaveReset =  {
  ~ohm.turnGridCyan(0, 5, page: page, bank: controlBank);
  ~ohm.turnGridCyan(1, 5, page: page, bank: controlBank);
  ~ohm.turnGridCyan(2, 5, page: page, bank: controlBank);
  ~ohm.turnGridCyan(0, 6, page: page, bank: controlBank);
  ~ohm.turnGridCyan(1, 6, page: page, bank: controlBank);
  ~ohm.turnGridCyan(2, 6, page: page, bank: controlBank);
};
osc1OctaveReset.value;
~ohm.turnGridGreen(2, 5, page: page, bank: controlBank);
~ohm.setGridFunc(0, 5, {
  ~sub.setOsc1Octave(1);
  osc1OctaveReset.value;
  ~ohm.turnGridGreen(0, 5);
}, page: page, bank: controlBank);
~ohm.setGridFunc(1, 5, {
  ~sub.setOsc1Octave(2);
  osc1OctaveReset.value;
  ~ohm.turnGridGreen(1, 5);
}, page: page, bank: controlBank);
~ohm.setGridFunc(2, 5, {
  ~sub.setOsc1Octave(3);
  osc1OctaveReset.value;
  ~ohm.turnGridGreen(2, 5);
}, page: page, bank: controlBank);
~ohm.setGridFunc(0, 6, {
  ~sub.setOsc1Octave(4);
  osc1OctaveReset.value;
  ~ohm.turnGridGreen(0, 6);
}, page: page, bank: controlBank);
~ohm.setGridFunc(1, 6, {
  ~sub.setOsc1Octave(5);
  osc1OctaveReset.value;
  ~ohm.turnGridGreen(1, 6);
}, page: page, bank: controlBank);
~ohm.setGridFunc(2, 6, {
  ~sub.setOsc1Octave(6);
  osc1OctaveReset.value;
  ~ohm.turnGridGreen(2, 6);
}, page: page, bank: controlBank);


// osc2:

// waveform:
osc2WaveshapeReset = {
  ~ohm.turnGridWhite(4, 4, page: page);
  ~ohm.turnGridWhite(5, 4, page: page);
  ~ohm.turnGridWhite(6, 4, page: page);
  ~ohm.turnGridWhite(7, 4, page: page);
};
osc2WaveshapeReset.value;
~ohm.turnGridGreen(7, 4, page: page, bank: controlBank);
~ohm.setGridFunc(4, 4, {
  ~sub.setOsc2Waveform('sine');
  osc2WaveshapeReset.value;
  ~ohm.turnGridGreen(4, 4);
}, page: page, bank: controlBank);
~ohm.setGridFunc(5, 4, {
  ~sub.setOsc2Waveform('tri');
  osc2WaveshapeReset.value;
  ~ohm.turnGridGreen(5, 4);
}, page: page, bank: controlBank);
~ohm.setGridFunc(6, 4, {
  ~sub.setOsc2Waveform('saw');
  osc2WaveshapeReset.value;
  ~ohm.turnGridGreen(6, 4);
}, page: page, bank: controlBank);
~ohm.setGridFunc(7, 4, {
  ~sub.setOsc2Waveform('rect');
  osc2WaveshapeReset.value;
  ~ohm.turnGridGreen(7, 4);
}, page: page, bank: controlBank);

// octave:
osc2OctaveReset =  {
  ~ohm.turnGridBlue(4, 5, page: page, bank: controlBank);
  ~ohm.turnGridBlue(5, 5, page: page, bank: controlBank);
  ~ohm.turnGridBlue(6, 5, page: page, bank: controlBank);
  ~ohm.turnGridBlue(4, 6, page: page, bank: controlBank);
  ~ohm.turnGridBlue(5, 6, page: page, bank: controlBank);
  ~ohm.turnGridBlue(6, 6, page: page, bank: controlBank);
};
osc2OctaveReset.value;
~ohm.turnGridGreen(5, 5, page: page, bank: controlBank);
~ohm.setGridFunc(4, 5, {
  ~sub.setOsc2Octave(1);
  osc2OctaveReset.value;
  ~ohm.turnGridGreen(4, 5);
}, page: page, bank: controlBank);
~ohm.setGridFunc(5, 5, {
  ~sub.setOsc2Octave(2);
  osc2OctaveReset.value;
  ~ohm.turnGridGreen(5, 5);
}, page: page, bank: controlBank);
~ohm.setGridFunc(6, 5, {
  ~sub.setOsc2Octave(3);
  osc2OctaveReset.value;
  ~ohm.turnGridGreen(6, 5);
}, page: page, bank: controlBank);
~ohm.setGridFunc(4, 6, {
  ~sub.setOsc2Octave(4);
  osc2OctaveReset.value;
  ~ohm.turnGridGreen(4, 6);
}, page: page, bank: controlBank);
~ohm.setGridFunc(5, 6, {
  ~sub.setOsc2Octave(5);
  osc2OctaveReset.value;
  ~ohm.turnGridGreen(5, 6);
}, page: page, bank: controlBank);
~ohm.setGridFunc(6, 6, {
  ~sub.setOsc2Octave(6);
  osc2OctaveReset.value;
  ~ohm.turnGridGreen(6, 6);
}, page: page, bank: controlBank);

// main sliders:

// attack:
~ohm.setLeftSliderFunc(0, { | val |
  var attack = val.linexp(0, 127, 0.05, 7);
  ~sub.setAttackTime(attack);
}, page: page, bank: mainBank);

// decay:
~ohm.setLeftSliderFunc(1, { | val |
  var decay = val.linlin(0, 127, 0, 2);
  ~sub.setDecayTime(decay);
}, page: page, bank: mainBank);

// sustain:
~ohm.setLeftSliderFunc(2, { | val |
  var sustain = val.linlin(0, 127, 0.0, 1.0);
  ~sub.setSustainLevel(sustain);
}, page: page, bank: mainBank);

// release:
~ohm.setLeftSliderFunc(3, { | val |
  var release = val.linexp(0, 127, 0.05, 12);
  ~sub.setReleaseTime(release);
}, page: page, bank: mainBank);


// main knobs:

// master volume:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~sub.mixer.setVol(val.ccdbfs(-70, 6));
}, page: page, bank: mainBank);

// cutoff:
~ohm.setLeftKnobFunc(0, 0, { | val |
  cutoff = val.linexp(0, 127, 100, 18000);
  ~sub.setFilterCutoff(cutoff);
}, page: page, bank: mainBank);
// res:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var res = val.linlin(0, 127, 0, 1.2);
  ~sub.setFilterRes(res);
}, page: page, bank: mainBank);
// LFO 1 Speed:
~ohm.setLeftKnobFunc(2, 0, { | val |
  lfo1Freq = val.linexp(0, 127, 0.01, 18);
  ~sub.setLFO1Freq(lfo1Freq);
}, page: page, bank: mainBank);
// LFO 2 Speed:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var freq = val.linexp(0, 127, 0.01, 18);
  ~sub.setLFO2Freq(freq);
}, page: page, bank: mainBank);

// osc1 vol:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~sub.setOsc1Vol(val.ccdbfs(-70, 0));
}, page: page, bank: mainBank);
// osc1 SubAmp:
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~sub.setOsc1SubVol(val.ccdbfs(-70, 0));
}, page: page, bank: mainBank);

// osc2 Amp:
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~sub.setOsc2Vol(val.ccdbfs(-70, 0));
}, page: page, bank: mainBank);
// osc2 SubAmp:
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~sub.setOsc2SubVol(val.ccdbfs(-70, 0));
}, page: page, bank: mainBank);

// amp LFO:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var amount = val.linlin(0, 127, 1, 0);
  ~sub.setAmplitudeLFOBottom(amount);
}, page: page, bank: mainBank);

// filter LFO 1:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var lowAmount = val.linlin(0, 127, 1, 0.0625);
  var highAmount = val.linlin(0, 127, 1, 4);
  ~sub.setFilterCutoffLFOBottomRatio(lowAmount);
  ~sub.setFilterCutoffLFOTopRatio(highAmount);
}, page: page, bank: mainBank);

// filter LFO 2:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var lowAmount = val.linlin(0, 127, 1, 0.0625);
  var highAmount = val.linlin(0, 127, 1, 4);
  ~sub.setFilterCutoffLFO2BottomRatio(lowAmount);
  ~sub.setFilterCutoffLFO2TopRatio(highAmount);
}, page: page, bank: mainBank);

//////// LFO 1 Bank:

// LFO Freq Wide:
~ohm.setLeftKnobFunc(0, 0, { | val |
  lfo1Freq  = val.linexp(0, 127, 0.01, 18);
  ~sub.setLFO1Freq(lfo1Freq);
}, page: page, bank: lfo1Bank);
// LFO Freq Fine:
~ohm.setLeftKnobFunc(1, 0, { | val |
  //lfo1Freq = lfo1Freq + val.linlin(0, 127, -5, 5);
  ~sub.setLFO1Freq(lfo1Freq);
}, page: page, bank: lfo1Bank);
// Waveform:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var waveform = val.linlin(0, 127, 0, 5);
  ~sub.setLFO1Waveform(waveform);
}, page: page, bank: lfo1Bank);
// Pulse Width:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var pulseWidth = val.linlin(0, 127, 0, 1);
  ~sub.setLFOPulseWidth(pulseWidth);
}, page: page, bank: lfo1Bank);

//////// LFO 2 Bank:

////////// Osc 1 Bank:

///////// Osc 2 Bank:



s.makeWindow
