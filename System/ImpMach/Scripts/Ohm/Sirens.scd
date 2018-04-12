/*
Thursday, April 10th 2014
Sirens.scd
prm
*/

var page = 'sirens';
var songBank = 0;
var dryBank = 1;
var drumBank = 2;

var granShiftRoutine;

~ohm.makePage(page);
~ohm.addLeftSlidersBanks(2, page: page);
~ohm.addLeftButtonsBanks(2, page: page);
~ohm.addLeftKnobsBanks(2, page: page);

//~ohm.turnAllGridYellow(page: page);

~ohmFunc.addMasterFaders(page);
~ohmFunc.addDryFaders(page, dryBank);
//~ohmFunc.addHomeButton(page);

// go home:
~ohm.turnControlButtonGreen(3, 0, page: page);
~ohm.setControlButtonFunc(3, 0, { ~ohm.setPage('main'); }, page: page);


// trumpet freeze bend 1:
// capture:
~ohm.turnGridBlue(0, 0, page: page);
~ohm.setGridFunc(0, 0, r {
  loop {
    ~im.getSong('Sirens').trumpet.freezeBend1.freeze;
    ~ohm.turnGridGreen(0, 0).yield;

    ~im.getSong('Sirens').trumpet.freezeBend1.releaseFreeze;
    ~ohm.turnGridRed(0, 0).yield;
  }
}, page: page);

// bend:
~ohm.turnGridWhite(1, 0, page: page);
~ohm.setGridFunc(1, 0, r {
  loop {
    ~im.getSong('Sirens').trumpet.freezeBend1.makeRateLFO(0.1, 0.8, 1);
    ~ohm.turnGridGreen(1, 0).yield;

    ~im.getSong('Sirens').trumpet.freezeBend1.freeRateLFO;
    ~ohm.turnGridRed(1, 0).yield;
  }
}, page: page);

// trumpet freeze bend 2:

// capture:
~ohm.turnGridCyan(0, 1, page: page);
~ohm.setGridFunc(0, 1, r {
  loop {
    ~im.getSong('Sirens').trumpet.freezeBend2.freeze;
    ~ohm.turnGridGreen(0, 1).yield;

    ~im.getSong('Sirens').trumpet.freezeBend2.releaseFreeze;
    ~ohm.turnGridRed(0, 1).yield;
  }
}, page: page);

// bend:
~ohm.turnGridWhite(1, 1, page: page);
~ohm.setGridFunc(1, 1, r {
  loop {
    ~im.getSong('Sirens').trumpet.freezeBend2.makeRateLFO(0.09, 0.8, 1);
    ~ohm.turnGridGreen(1, 1).yield;

    ~im.getSong('Sirens').trumpet.freezeBend2.freeRateLFO;
    ~ohm.turnGridRed(1, 1).yield;
  }
}, page: page);

// flugel freeze bend 1:

// capture:
~ohm.turnGridPurple(0, 2, page: page);
~ohm.setGridFunc(0, 2, r {
  loop {
    ~im.getSong('Sirens').flugel.freezeBend1.freeze;
    ~ohm.turnGridGreen(0, 2).yield;

    ~im.getSong('Sirens').flugel.freezeBend1.releaseFreeze;
    ~ohm.turnGridRed(0, 2).yield;
  }
}, page: page);

// bend:
~ohm.turnGridWhite(1, 2, page: page);
~ohm.setGridFunc(1, 2, r {
  loop {
    ~im.getSong('Sirens').flugel.freezeBend1.makeRateLFO(0.11, 0.81, 1);
    ~ohm.turnGridGreen(1, 2).yield;

    ~im.getSong('Sirens').flugel.freezeBend1.freeRateLFO;
    ~ohm.turnGridRed(1, 2).yield;
  }
}, page: page);

// flugel freeze bend 2:

// capture:
~ohm.turnGridBlue(0, 3, page: page);
~ohm.setGridFunc(0, 3, r {
  loop {
    ~im.getSong('Sirens').flugel.freezeBend2.freeze;
    ~ohm.turnGridGreen(0, 3).yield;

    ~im.getSong('Sirens').flugel.freezeBend2.releaseFreeze;
    ~ohm.turnGridRed(0, 3).yield;
  }
}, page: page);

// bend:
~ohm.turnGridWhite(1, 3, page: page);
~ohm.setGridFunc(1, 3, r {
  loop {
    ~im.getSong('Sirens').flugel.freezeBend2.makeRateLFO(0.095, 0.79, 1);
    ~ohm.turnGridGreen(1, 3).yield;

    ~im.getSong('Sirens').flugel.freezeBend2.freeRateLFO;
    ~ohm.turnGridRed(1, 3).yield;
  }
}, page: page);

// drum machine start/stop:
~ohm.turnGridBlue(0, 5, page: page);
~ohm.setGridFunc(0, 5, r {
  loop {
    ~im.getSong('Sirens').drumMachine.playSeq;
    ~ohm.turnGridGreen(0, 5, page: page).yield;

    ~im.getSong('Sirens').drumMachine.stopSeq;
    ~ohm.turnGridRed(0, 5, page: page).yield;
  };
}, page: page);

/*
~ohm.turnGridWhite(0, 0, page: page);
~ohm.setGridFunc(0, 0, r {
  loop {
    ~sirenTpt = IM_DopplerShift.new(~im.mixer.chan(9), 1, 1, 0.07, 0.1, relGroup: ~im.procGroup, addAction: \addToTail);
    ~sirenFlugel = IM_DopplerShift.new(~im.mixer.chan(10), 1, 1, 0.09, 0.15, relGroup: ~im.procGroup, addAction: \addToTail);
    { ~sirenTptDirect = IM_HardwareIn.new(1, ~sirenTpt.inBus, ~im.procGroup, \addToHead); }.defer(0.1);
    { ~sirenFlugelDirect = IM_HardwareIn.new(2, ~sirenFlugel.inBus, ~im.procGroup, \addToHead); }.defer(0.1);
    ~ohm.turnGridPurple(0, 0).yield;

    ~sirenTptDirect.free;
    ~sirenFlugelDirect.free;
    ~sirentTpt.free;
    ~sirenFlugel.free;
    ~ohm.turnGridRed(0, 0).yield;
  }
}, page: page);

*/


// change banks:
~ohm.turnGridPurple(0, 7, page: page);
~ohm.setGridFunc(0, 7, r {
  loop {
    ~ohm.setActiveLeftKnobsBank(dryBank);
    ~ohm.setActiveLeftSlidersBank(dryBank);
    ~ohm.setActiveLeftButtonsBank(dryBank);
    ~ohm.turnGridBlue(0, 7).yield;

    ~ohm.setActiveLeftKnobsBank(drumBank);
    ~ohm.setActiveLeftSlidersBank(drumBank);
    ~ohm.setActiveLeftButtonsBank(drumBank);
    ~ohm.turnGridGreen(0, 7).yield;

    ~ohm.setActiveLeftKnobsBank(songBank);
    ~ohm.setActiveLeftSlidersBank(songBank);
    ~ohm.setActiveLeftButtonsBank(songBank);
    ~ohm.turnGridPurple(0, 7).yield;

  };
}, page: page);

//////// Song Bank Functions:

// tpt:

// doppler:
// mixer order: doppler/fb1/fb2
// fader:
~ohm.setLeftSliderFunc(0, { | val |
  var dopplerChan = 0;
  ~im.getSong('Sirens').trumpet.setVol(dopplerChan, val.ccdbfs(-70, 0), 0.1);
}, page: page, bank: songBank);
// button:
~ohm.turnLeftButtonGreen(0, page: page, bank: songBank);
~ohm.setLeftButtonFunc(0, {
  var dopplerChan = 0;
  if( ~im.getSong('Sirens').trumpet.isMuted(dopplerChan),
    { ~im.getSong('Sirens').trumpet.unMute(dopplerChan); ~ohm.turnLeftButtonGreen(dopplerChan); },
    { ~im.getSong('Sirens').trumpet.mute(dopplerChan); ~ohm.turnLeftButtonRed(dopplerChan); }
  );
}, page: page, bank: songBank);
// verb send:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~im.getSong('Sirens').trumpet.mixer.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, page: page, bank: songBank);
// gran send:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~im.getSong('Sirens').trumpet.mixer.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, page: page, bank: songBank);


// freeze bends:

// fader:
~ohm.setLeftSliderFunc(2, { | val |
  ~im.getSong('Sirens').trumpet.setVol(1, val.ccdbfs(-70, -6), 0.1);
  ~im.getSong('Sirens').trumpet.setVol(2, val.ccdbfs(-70, -6), 0.1);
}, page: page, bank: songBank);
// button:
~ohm.turnLeftButtonGreen(2, page: page, bank: songBank);
~ohm.setLeftButtonFunc(2, {
  if( ~im.getSong('Sirens').trumpet.isMuted(1),
    { ~im.getSong('Sirens').trumpet.unMute(1); ~ohm.turnLeftButtonGreen(2); },
    { ~im.getSong('Sirens').trumpet.mute(1); ~ohm.turnLeftButtonRed(2); }
  );
  if( ~im.getSong('Sirens').trumpet.isMuted(2),
    { ~im.getSong('Sirens').trumpet.unMute(2);},
    { ~im.getSong('Sirens').trumpet.mute(2); }
  );
}, page: page, bank: songBank);
// verb send:
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~im.getSong('Sirens').trumpet.mixer.setSendVol(1, 1, val.ccdbfs(-70, 0));
  ~im.getSong('Sirens').trumpet.mixer.setSendVol(2, 1, val.ccdbfs(-70, 0));
}, page: page, bank: songBank);
// gran send:
~ohm.setLeftKnobFunc(2, 0, { | val |
  ~im.getSong('Sirens').trumpet.mixer.setSendVol(1, 2, val.ccdbfs(-70, 0));
  ~im.getSong('Sirens').trumpet.mixer.setSendVol(2, 2, val.ccdbfs(-70, 0));
}, page: page, bank: songBank);


// flugel:

// doppler:
~ohm.setLeftSliderFunc(1, { | val |
  ~im.getSong('Sirens').flugel.setVol(0, val.ccdbfs(-70, 0), 0.1);
}, page: page, bank: songBank);
~ohm.turnLeftButtonGreen(1, page: page, bank: songBank);
~ohm.setLeftButtonFunc(1, {
  if( ~im.getSong('Sirens').flugel.isMuted(0),
    { ~im.getSong('Sirens').flugel.unMute(0); ~ohm.turnLeftButtonGreen(1); },
    { ~im.getSong('Sirens').flugel.mute(0); ~ohm.turnLeftButtonRed(1); }
  );
}, page: page, bank: songBank);
// verb send:
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~im.getSong('Sirens').flugel.mixer.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, page: page, bank: songBank);
// gran send:
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~im.getSong('Sirens').flugel.mixer.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, page: page, bank: songBank);

// freeze bends:
~ohm.setLeftSliderFunc(3, { | val |
  ~im.getSong('Sirens').flugel.setVol(1, val.ccdbfs(-70, -6), 0.1);
  ~im.getSong('Sirens').flugel.setVol(2, val.ccdbfs(-70, -6), 0.1);
}, page: page, bank: songBank);
~ohm.turnLeftButtonGreen(3, page: page, bank: songBank);
  ~ohm.setLeftButtonFunc(3, {
  if( ~im.getSong('Sirens').flugel.isMuted(1),
    { ~im.getSong('Sirens').flugel.unMute(1); ~ohm.turnLeftButtonGreen(3); },
    { ~im.getSong('Sirens').flugel.mute(1); ~ohm.turnLeftButtonRed(3); }
  );
  if( ~im.getSong('Sirens').flugel.isMuted(2),
    { ~im.getSong('Sirens').flugel.unMute(2); },
    { ~im.getSong('Sirens').flugel.mute(2); }
  );
}, page: page, bank: songBank);

// verb send:
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~im.getSong('Sirens').flugel.mixer.setSendVol(1, 1, val.ccdbfs(-70, 0));
  ~im.getSong('Sirens').flugel.mixer.setSendVol(2, 1, val.ccdbfs(-70, 0));
}, page: page, bank: songBank);
// gran send:
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~im.getSong('Sirens').flugel.mixer.setSendVol(1, 2, val.ccdbfs(-70, 0));
  ~im.getSong('Sirens').flugel.mixer.setSendVol(2, 2, val.ccdbfs(-70, 0));
}, page: page, bank: songBank);





//////// Drum Bank:

~ohm.setLeftSliderFunc(0, { | val | ~im.getSong('Sirens').mixer.setVol(2, val.ccdbfs(-70, 6), 0.1); },
  page: page, bank: drumBank);
// mute:
~ohm.turnLeftButtonGreen(0, page: page, bank: drumBank);
~ohm.setLeftButtonFunc(0, {
  if( ~im.getSong('Sirens').mixer.isMuted(2),
    { ~im.getSong('Sirens').mixer.unMute(2); ~ohm.turnLeftButtonGreen(0); },
    { ~im.getSong('Sirens').mixer.mute(2); ~ohm.turnLeftButtonRed(0); }
  );
}, page: page, bank: drumBank);
// delay send:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~im.getSong('Sirens').mixer.setSendVol(2, 0, val.ccdbfs(-70, 0));
}, page: page, bank: drumBank);
// verb send:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~im.getSong('Sirens').mixer.setSendVol(2, 1, val.ccdbfs(-70, 0));
}, page: page, bank: drumBank);
// gran send:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~im.getSong('Sirens').mixer.setSendVol(2, 2, val.ccdbfs(-70, 0));
}, page: page, bank: drumBank);

~ohm.setLeftKnobFunc(1, 2, { | val |
  var msLength = val.linlin(0, 127, 50, 1500);
  ~im.delay.setMSLength(msLength);
}, page: page, bank: drumBank);

~ohm.setLeftKnobFunc(2, 2, { | val |
  var sampleRate = val.linlin(0, 127, 25000, 1000);
  ~im.delay.setSampleRate(sampleRate);
}, page: page, bank: drumBank);


~ohm.setLeftKnobFunc(3, 2, { | val |
  var feedback = val.linlin(0, 127, 0, 1);
  ~im.delay.setFeedback(feedback);
}, page: page, bank: drumBank);





// Granulator Shift:

granShiftRoutine = r {
  ~im.granulator.setTrigRate(5);
  ~im.granulator.setGrainDurLow(0.01);
  ~im.granulator.setGrainDurHigh(0.2);
  ~directIns.mixer.setSendVol(1, 2, -30);
  ~directIns.mixer.setSendVol(2, 2, -30);
  ~directIns.mixer.setSendVol(3, 2, -30);
  6.wait;

  ~im.granulator.setTrigRate(10);
  ~im.granulator.setGrainDurLow(0.07);
  ~im.granulator.setGrainDurHigh(0.25);
  ~directIns.mixer.setSendVol(1, 2, -20);
  ~directIns.mixer.setSendVol(2, 2, -20);
  ~directIns.mixer.setSendVol(3, 2, -20);
  6.wait;

  ~im.granulator.setTrigRate(20);
   ~im.granulator.setGrainDurLow(0.09);
  ~im.granulator.setGrainDurHigh(0.3);
  ~directIns.mixer.setSendVol(1, 2, -10);
  ~directIns.mixer.setSendVol(2, 2, -10);
  ~directIns.mixer.setSendVol(3, 2, -10);
  6.wait;

  ~im.granulator.setTrigRate(30);
  ~im.granulator.setGrainDurLow(0.1);
  ~im.granulator.setGrainDurHigh(0.35);
  ~directIns.mixer.setSendVol(1, 2, -6);
  ~directIns.mixer.setSendVol(2, 2, -6);
  ~directIns.mixer.setSendVol(3, 2, -6);
  6.wait;

  ~im.granulator.setTrigRate(40);
  ~im.granulator.setGrainDurLow(0.15);
  ~im.granulator.setGrainDurHigh(0.4);
  ~directIns.mixer.setSendVol(1, 2, -3);
  ~directIns.mixer.setSendVol(2, 2, -3);
  ~directIns.mixer.setSendVol(3, 2, -3);
  6.wait;

  ~im.granulator.setGrainDurLow(0.3);
  ~im.granulator.setGrainDurHigh(0.45);
  ~directIns.mixer.setSendVol(1, 2, 0);
  ~directIns.mixer.setSendVol(2, 2, 0);
  ~directIns.mixer.setSendVol(3, 2, 0);
};

~ohm.turnGridCyan(7, 7, page: page);
~ohm.setGridFunc(7, 7, { granShiftRoutine.play; ~ohm.turnGridGreen(7, 7); }, page: page);
