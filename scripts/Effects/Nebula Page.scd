/*
from a while ago
prm
*/


var pitchShiftColorReset;
var page = 'nebula';
var shiftAmount = 0;

var cutoffShiftRate = 10;
var cutoffLow = 700;
var cutoffHigh = 2500;
var cutoffShiftRoutine;

var nebulaBank = 0;
var sendsBank = 1;


~ohm.makePage(page);

~ohm.setPageLoadFunction({
  ~ohm.storageDict[\loadGlobalSubmixerFunc].value;
}, page);

~ohm.addLeftKnobsBanks(1);

//~nebula = TrumpetNebula.new(0);
//y = { Out.ar(~nebula.inBus, SoundIn.ar(0)) }.play;

//////// knob bank change:
~ohm.turnCrossfaderButtonBlue(0, page: page);
~ohm.setCrossfaderButtonFunc(0, r {
  loop {
    ~ohm.setActiveLeftKnobsBank(sendsBank);
    ~ohm.turnCrossfaderButtonPurple(0).yield;

    ~ohm.setActiveLeftKnobsBank(nebulaBank);
    ~ohm.turnCrossfaderButtonBlue(0).yield;
  };
}, page: page);

//////// Left Buttons:
~ohm.turnLeftButtonGreen(2, page: page);
~ohm.setLeftButtonFunc(2, {
  ~prm.songBook[\nebula].toggleMuteInput;
  if( ~prm.songBook[\nebula].inputIsMuted, { ~ohm.turnLeftButtonRed(3); }, { ~ohm.turnLeftButtonGreen(3); });
}, page: page);

////// Left Sliders:
// overall volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~prm.songBook[\nebula].setVol(0, val.ccdbfs(-70, 0), 0.1);
}, page: page);

// Left Delay:
~ohm.setLeftSliderFunc(0, { | val |
  var delayTime = val.linlin(0, 127, 0.05, 2);
  ~prm.songBook[\nebula].setLeftDelayTime(delayTime);
}, page: page);

// Right Delay:
~ohm.setLeftSliderFunc(1, { | val |
  var delayTime = val.linlin(0, 127, 0.05, 2);
  ~prm.songBook[\nebula].setRightDelayTime(delayTime);
}, page: page);

// Feedback:
~ohm.setLeftSliderFunc(2, { | val |
  var feedback = val.linlin(0, 127, 0, 1.2);
  ~prm.songBook[\nebula].setFeedback(feedback);
}, page: page);

////// Left Knobs:

//// Bottom Row:

// Delay Center Freq:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var freq = val.linexp(0, 127, 200, 3000);
  ~prm.songBook[\nebula].setDelayFilterCenterFreq(freq);
}, page: page, bank: nebulaBank);

// Delay BW:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var bw = val.linlin(0, 127, 8, 1);
  ~prm.songBook[\nebula].setDelayFilterBW(bw);
}, page: page, bank: nebulaBank);

// Delay Vol:
~ohm.setLeftKnobFunc(2, 2, { | val |
  ~prm.songBook[\nebula].setDelayVol(val.ccdbfs(-70, 12));
}, page: page, bank: nebulaBank);

// Cutoff:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var cutoff = val.linexp(0, 127, 300, 16000);
  ~prm.songBook[\nebula].setCutoff(cutoff);
}, page: page, bank: nebulaBank);

//// Middle Row:

// Distortion Amount:
~ohm.setLeftKnobFunc(0, 1, { | val |
  var distAmt = val.linlin(0, 127, 1, 500);
  ~prm.songBook[\nebula].setDistortionAmount(distAmt);
  ~prm.songBook[\nebula].setDistortionGain(val.ccdbfs(0, -18));
}, page: page, bank: nebulaBank);

// Post-Distortion Cutoff:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var cutoff = val.linexp(0, 127, 300, 10000);
  ~prm.songBook[\nebula].setPostDistortionCutoff(cutoff);
}, page: page, bank: nebulaBank);

// Reverb Mix:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var mix = val.linlin(0, 127, 0, 1);
  ~prm.songBook[\nebula].setReverbMix(mix);
}, page: page, bank: nebulaBank);

// Reverb Room:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var room = val.linlin(0, 127, 0, 1);
  ~prm.songBook[\nebula].setReverbRoom(room);
}, page: page, bank: nebulaBank);

//// Top Row:

// Low Gain:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~prm.songBook[\nebula].setLowGain(val.ccdbfs(-12, 12));
}, page: page, bank: nebulaBank);

// High Gain:
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~prm.songBook[\nebula].setHighGain(val.ccdbfs(-12, 12));
}, page: page, bank: nebulaBank);

// Nebula Activity:
~ohm.setLeftButtonFunc(2, 0, { | val |
  var activity = val.linlin(0, 127, 0, 100);
  ~prm.songBook[\nebula].setNebulaActivity(activity);
}, page: page, bank: nebulaBank);

// Nebula Depth:
~ohm.setLeftButtonFunc(3, 0, { | val |
  var depth = val.linlin(0, 127, 0, 100);
  ~prm.songBook[\nebula].setNebulaDepth(depth);
}, page: page, bank: nebulaBank);

//////// sends bank:

// reverb:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var vol = val.linlin(0, 127, -50, 0);
  ~prm.songBook[\nebula].mixer.setSendVol(0, vol);
}, page: page, bank: sendsBank);

// granulator:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var vol = val.linlin(0, 127, -50, 6);
  ~prm.songBook[\nebula].mixer.setSendVol(1, vol);
}, page: page, bank: sendsBank);


////// Grid:

//// Pitch Shifting:

pitchShiftColorReset = {
  ~ohm.turnGridBlue(0, 0, page: page);
  ~ohm.turnGridBlue(1, 0, page: page);
  ~ohm.turnGridBlue(2, 0, page: page);
  ~ohm.turnGridBlue(3, 0, page: page);
  ~ohm.turnGridBlue(4, 0, page: page);
  ~ohm.turnGridCyan(0, 1, page: page);
  ~ohm.turnGridWhite(1, 1, page: page);
};

pitchShiftColorReset.value;
~ohm.turnGridGreen(2, 0, page: page);

// 15vb:
~ohm.setGridFunc(0, 0, {
  pitchShiftColorReset.value;
  ~ohm.turnGridGreen(0, 0);
  shiftAmount = -24;
  ~prm.songBook[\nebula].setPitchShiftAmount(shiftAmount);
}, page: page);

// 8vb:
~ohm.setGridFunc(1, 0, {
  pitchShiftColorReset.value;
  ~ohm.turnGridGreen(1, 0);
  shiftAmount = -12;
  ~prm.songBook[\nebula].setPitchShiftAmount(shiftAmount);
}, page: page);

// 0:
~ohm.setGridFunc(2, 0, {
  pitchShiftColorReset.value;
  ~ohm.turnGridGreen(2, 0);
  shiftAmount = 0;
  ~prm.songBook[\nebula].setPitchShiftAmount(shiftAmount);
}, page: page);

// 8va:
~ohm.setGridFunc(3, 0, {
  pitchShiftColorReset.value;
  ~ohm.turnGridGreen(3, 0);
  shiftAmount = 12;
  ~prm.songBook[\nebula].setPitchShiftAmount(shiftAmount);
}, page: page);

// 15ma:
~ohm.setGridFunc(4, 0, {
  pitchShiftColorReset.value;
  ~ohm.turnGridGreen(4, 0);
  shiftAmount = 24;
  ~prm.songBook[\nebula].setPitchShiftAmount(shiftAmount);
}, page: page);

// -1:
~ohm.setGridFunc(0, 1, {
  ~ohm.turnGridYellow(0, 1);
  shiftAmount = shiftAmount - 1;
  ~prm.songBook[\nebula].setPitchShiftAmount(shiftAmount);
}, page: page);

// +1:
~ohm.setGridFunc(1, 1, {
  ~ohm.turnGridYellow(1, 1);
  shiftAmount = shiftAmount + 1;
  ~prm.songBook[\nebula].setPitchShiftAmount(shiftAmount);
}, page: page);


// cutoff shift routine:
cutoffShiftRoutine = r {
  loop {
    ~prm.songBook[\nebula].setPostDistortionCutoff(exprand(cutoffLow, cutoffHigh));
    (1/cutoffShiftRate).wait;
  }
};

~ohm.turnGridCyan(0, 4, page: page);
~ohm.setGridFunc(0, 4, r {
  loop {
    cutoffShiftRoutine.reset.play;
    ~ohm.turnGridGreen(0, 4).yield;

    cutoffShiftRoutine.stop;
    ~ohm.turnGridRed(0, 4).yield;
  };
}, page: page);
