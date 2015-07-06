 /*
Sunday, July 5th 2015
Dauphine Street.scd
prm
*/

var page = 'dauphine';

var mainBank = 0;


~base.makePage(page);
~base.addFadersBanks(5);
~base.addTouchButtonsBanks(5);



//////////////////////////////
/////////////////////////////
/////// Main Bank: //////////
///////////////////////////
/////////////////////////

//////// volumes:


// trumpet:
~base.setFaderMode(1, 'whiteFill', bank: mainBank, page: page);
~base.setFaderFunc(1, { | val |
  ~dauphine.mixerA.setVol(0, val.ccdbfs(-90, -6));
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

