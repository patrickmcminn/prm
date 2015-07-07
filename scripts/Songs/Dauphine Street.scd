 /*
Sunday, July 5th 2015
Dauphine Street.scd
prm
*/

var page = 'dauphine';

var mainBank = 0;
var mixerABank = 1;
var mixerBBank = 2;
var mixerCBank = 3;

var controlButtonColorArray = ['blue', 'white', 'magenta', 'cyan'];
var controlButtonResetFunc;


~base.makePage(page);
~base.addFadersBanks(5, page);
~base.addTouchButtonsBanks(5, page);


//////////////////////////////
/////////////////////////////
/////// Control Buttons: //////////
///////////////////////////
/////////////////////////

controlButtonResetFunc = {
  controlButtonColorArray.do({ | color, i |
    ~base.turnControlButtonColor(i+1, 'left', 'off', page: page);
    ~base.turnControlButtonColor(i+1, 'right', color, page: page);
  });
};
controlButtonResetFunc.value;
~base.turnControlButtonGreen(1, 'left', page: page);

~base.setControlButtonFunc(1, {
  ~base.setActiveFadersBank(mainBank);
  ~base.setActiveTouchButtonsBank(mainBank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(1, 'left', page: page);
}, page: page);

~base.setControlButtonFunc(2, {
  ~base.setActiveFadersBank(mixerABank);
  ~base.setActiveTouchButtonsBank(mixerABank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(2, 'left', page: page);
}, page: page);

~base.setControlButtonFunc(3, {
  ~base.setActiveFadersBank(mixerBBank);
  ~base.setActiveTouchButtonsBank(mixerBBank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(3, 'left', page: page);
}, page: page);

~base.setControlButtonFunc(4, {
  ~base.setActiveFadersBank(mixerCBank);
  ~base.setActiveTouchButtonsBank(mixerCBank);
  controlButtonResetFunc.value;
  ~base.turnControlButtonGreen(4, 'left', page: page);
}, page: page);




//////////////////////////////
/////////////////////////////
/////// Main Bank: //////////
///////////////////////////
/////////////////////////

//////// volumes:


// trumpet:
~base.setFaderMode(1, 'whiteFill', bank: mainBank, page: page);
~base.setFaderFunc(1, { | val |
  ~dauphine.mixerA.setVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: mainBank, page: page);

// main synth:
~base.setFaderMode(2, 'magentaFill', bank: mainBank, page: page);
~base.setFaderFunc(2, { | val |
  ~dauphine.mixerB.setVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: mainBank, page: page);

// accomp synth:
~base.setFaderMode(3, 'blueFill', bank: mainBank, page: page);
~base.setFaderFunc(3, { | val |
  ~dauphine.mixerB.setVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: mainBank, page: page);

// bassline:
~base.setFaderMode(4, 'cyanFill', bank: mainBank, page: page);
~base.setFaderFunc(4, { | val |
  ~dauphine.mixerC.setVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: mainBank, page: page);


//////// Bassline Granulator Mix:
~base.setFaderMode(5, 'cyanFill', bank: mainBank, page: page);
~base.setFaderValue(5, 127, bank: mainBank, page: page);
~base.setFaderFunc(5, { | val |
  ~dauphine.bass.granulator.setGranulatorCrossfade(val.linlin(0, 127, -1, 1));
  ~dauphine.bass.granulator.setDelayMix(val.linlin(0, 127, 0, 0.5));
  ~base.setFaderValue(5, val);
}, bank: mainBank, page: page);

//////// Main Synth Modular Send:
~base.setFaderMode(6, 'magentaFill', bank: mainBank, page: page);
~base.setFaderFunc(6, { | val |
  ~dauphine.mixerB.setSendVol(0, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(6, val);
}, bank: mainBank, page: page);

//////// Trumpet Modular Send:
~base.setFaderMode(7, 'whiteFill', bank: mainBank, page: page);
~base.setFaderFunc(7, { | val |
  ~dauphine.mixerA.setSendVol(0, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(7, val);
}, bank: mainBank, page: page);

//////// Modular Volume:
~base.setFaderMode(8, 'redFill', bank: mainBank, page: page);
~base.setFaderFunc(8, { | val |
  ~prm.modular.setVol(val.ccdbfs(-70, 0));
  ~base.setFaderValue(8, val);
}, bank: mainBank, page: page);



//////////////////////////////
/////////////////////////////
/////// Grid: //////////
///////////////////////////
/////////////////////////

// Trumpet input mute/unmute:

~base.turnGridWhite(0, 3, bank: mainBank, page: page);
~base.setGridFunc(0, 3, {
  ~dauphine.trumpet.inputMixer.tglMute;
  if(~dauphine.trumpet.inputMixer.isMuted,
    { ~base.turnGridRed(0, 3); }, { ~base.turnGridWhite(0, 3); });
}, bank: mainBank, page: page);

//  Backing Track:

~base.turnGridMagenta(1, 3, bank: mainBank, page: page);
~base.setGridFunc(1, 3, r {
  {
    ~dauphine.synths.playBackingTrack;
    ~base.turnGridGreen(1, 3).yield;

    ~dauphine.synths.stopBackingTrack;
    ~base.turnGridRed(1, 3).yield;
  }.loop;
}, bank: mainBank, page: page);

//  Accomp Sequence:

~base.turnGridBlue(2, 3, bank: mainBank, page: page);
~base.setGridFunc(2, 3, r {
  {
    ~dauphine.accomp.playSequence(~dauphine.tempoClock);
    ~base.turnGridGreen(2, 3).yield;

    ~dauphine.accomp.stopSequence;
    ~base.turnGridRed(2, 3).yield;
  }.loop;
}, bank: mainBank, page: page);

//////// Bassline Notes:

4.do({ | i | ~base.turnGridCyan(i, 0, bank: mainBank, page: page); });
2.do({ | i | ~base.turnGridCyan(i, 1, bank: mainBank, page: page); });

~base.setGridFunc(0, 0, { | val |
  ~dauphine.bass.playC(val.ccdbfs(-18, 0));
  ~base.turnGridGreen(0, 0);
  }, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(0, 0, { | val | ~base.turnGridCyan(0, 0); }, 'noteOff', bank: mainBank, page: page);

~base.setGridFunc(1, 0, { | val |
  ~dauphine.bass.playG(val.ccdbfs(-18, 0));
  ~base.turnGridGreen(1, 0);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(1, 0, { | val | ~base.turnGridCyan(1, 0); }, 'noteOff', bank: mainBank, page: page);

~base.setGridFunc(2, 0, { | val |
  ~dauphine.bass.playD(val.ccdbfs(-18, 0));
  ~base.turnGridGreen(2, 0);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(2, 0, { | val | ~base.turnGridCyan(2, 0); }, 'noteOff', bank: mainBank, page: page);

~base.setGridFunc(3, 0, { | val |
  ~dauphine.bass.playA(val.ccdbfs(-18, 0));
  ~base.turnGridGreen(3, 0);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(3, 0, { | val | ~base.turnGridCyan(3, 0); }, 'noteOff', bank: mainBank, page: page);

~base.setGridFunc(0, 1, { | val |
  ~dauphine.bass.playF(val.ccdbfs(-18, 0));
  ~base.turnGridGreen(0, 1);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(0, 1, { | val | ~base.turnGridCyan(0, 1); }, 'noteOff', bank: mainBank, page: page);

~base.setGridFunc(1, 1, { | val |
  ~dauphine.bass.playE(val.ccdbfs(-18, 0));
  ~base.turnGridGreen(1, 1);
}, 'noteOn', bank: mainBank, page: page);
~base.setGridFunc(1, 1, { | val | ~base.turnGridCyan(1, 1); }, 'noteOff', bank: mainBank, page: page);


//////////////////////////////
/////////////////////////////
/////// Mixer A Bank: //////////
///////////////////////////
/////////////////////////

~base.setFaderMode(1, 'whiteFill', bank: mixerABank, page: page);
~base.setFaderFunc(1, { | val |
  ~dauphine.mixerA.setVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: mixerABank, page: page);

3.do({ | i | ~base.setFaderMode(i + 2, 'magentaFill', bank: mixerABank, page: page); });

// reverb send:
~base.setFaderFunc(2, { | val |
  ~dauphine.mixerA.setSendVol(0, 0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: mixerABank, page: page);

// granulator send:
~base.setFaderFunc(3, { | val |
  ~dauphine.mixerA.setSendVol(0, 1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: mixerABank, page: page);

// modular send:
~base.setFaderFunc(4, { | val |
  ~dauphine.mixerA.setSendVol(0, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: mixerABank, page: page);

//////// parameters:

// delay feedback:
~base.setFaderMode(5, 'yellowFill', bank: mixerABank, page: page);
~base.setFaderValue(5, 64, bank: mixerABank, page: page);
~base.setFaderFunc(5, { | val |
  var feedback = val.linlin(0, 127, 0, 1);
  ~dauphine.trumpet.granulator.setFeedback(feedback);
  ~base.setFaderValue(5, val);
}, bank: mixerABank, page: page);

// Grain Trig Rate:
~base.setFaderMode(6, 'redWalk', bank: mixerABank, page: page);
~base.setFaderValue(6, 64, bank: mixerABank, page: page);
~base.setFaderFunc(6, { | val |
  var trigRate = val.linlin(0, 127, 4, 80);
  ~dauphine.trumpet.granulator.setTrigRate(trigRate);
  ~base.setFaderValue(6, val);
}, bank: mixerABank, page: page);

// Wash Lowpass Freq:
~base.setFaderMode(7, 'blueWalk', bank: mixerABank, page: page);
~base.setFaderValue(7, 100, bank: mixerABank, page: page);
~base.setFaderFunc(7, { | val |
  var cutoff = val.linexp(0, 127, 250, 12000);
  ~dauphine.trumpet.wash.setLowPassCutoff(cutoff);
  ~base.setFaderValue(7, val);
}, bank: mixerABank, page: page);

// Wash Feedback:
~base.setFaderMode(8, 'whiteFill', bank: mixerABank, page: page);
~base.setFaderValue(8, 20, bank: mixerABank, page: page);
~base.setFaderFunc(8, { | val |
  var fb = val.linlin(0, 127, 0, 1);
  ~dauphine.trumpet.wash.setFeedbackCoefficient(fb);
  ~base.setFaderValue(8, val);
}, bank: mixerABank, page: page);


//////////////////////////////
/////////////////////////////
/////// Mixer B Bank: //////////
///////////////////////////
/////////////////////////

~base.setFaderMode(1, 'greenFill', bank: mixerBBank, page: page);
~base.setFaderMode(5, 'greenFill', bank: mixerBBank, page: page);

3.do({ | i | ~base.setFaderMode(i + 1, 'magentaFill', bank: mixerBBank, page: page); });
3.do({ | i | ~base.setFaderMode(i + 6, 'magentaFill', bank: mixerBBank, page: page); });

//////// Main Synth:
// volume:
~base.setFaderFunc(1, { | val |
  ~dauphine.mixerB.setVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: mixerBBank, page: page);

// reverb:
~base.setFaderFunc(2, { | val |
  ~dauphine.mixerB.setSendVol(0, 0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: mixerBBank, page: page);

// granulator:
~base.setFaderFunc(3, { | val |
  ~dauphine.mixerB.setSendVol(0, 1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: mixerBBank, page: page);

// modular send:
~base.setFaderFunc(4, { | val |
  ~dauphine.mixerB.setSendVol(0, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: mixerBBank, page: page);


//////// Accomp Synth:
// volume:
~base.setFaderFunc(5, { | val |
  ~dauphine.mixerB.setVol(1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(5, val);
}, bank: mixerBBank, page: page);

// reverb:
~base.setFaderFunc(6, { | val |
  ~dauphine.mixerB.setSendVol(1, 0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(6, val);
}, bank: mixerBBank, page: page);

// granulator:
~base.setFaderFunc(7, { | val |
  ~dauphine.mixerB.setSendVol(1, 1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(7, val);
}, bank: mixerBBank, page: page);

// modular send:
~base.setFaderFunc(8, { | val |
  ~dauphine.mixerB.setSendVol(1, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(8, val);
}, bank: mixerBBank, page: page);


//////////////////////////////
/////////////////////////////
/////// Mixer C Bank: //////////
///////////////////////////
/////////////////////////

~base.setFaderMode(1, 'greenFill', bank: mixerCBank, page: page);
3.do({ | i | ~base.setFaderMode(i + 2, 'magentaFill', bank: mixerCBank, page: page); });

// volume:
~base.setFaderFunc(1, { | val |
  ~dauphine.mixerC.setVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val);
}, bank: mixerCBank, page: page);

// reverb:
~base.setFaderFunc(2, { | val |
  ~dauphine.mixerC.setSendVol(0, 0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(2, val);
}, bank: mixerCBank, page: page);

// granulator:
~base.setFaderFunc(3, { | val |
  ~dauphine.mixerC.setSendVol(0, 1, val.ccdbfs(-70, 0));
  ~base.setFaderValue(3, val);
}, bank: mixerCBank, page: page);

// modular send:
~base.setFaderFunc(4, { | val |
  ~dauphine.mixerC.setSendVol(0, 2, val.ccdbfs(-70, 0));
  ~base.setFaderValue(4, val);
}, bank: mixerCBank, page: page);



