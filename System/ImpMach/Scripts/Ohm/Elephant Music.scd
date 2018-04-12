var page = 'elephantMusic';
var dryBank = 0;
var songBank = 1;

~ohm.makePage(page);

~ohm.addLeftKnobsBanks(1, page: page);
~ohm.addLeftSlidersBanks(1, page: page);
~ohm.addLeftButtonsBanks(1, page: page);

// add master funcs:
~ohmFunc.addMasterFaders(page);
~ohmFunc.addDryFaders(page, dryBank);

// go home:
~ohm.turnControlButtonGreen(3, 0, page: page);
~ohm.setControlButtonFunc(3, 0, { ~ohm.setPage('main'); }, page: page);

// Bank Changer:
~ohm.turnCrossfaderButtonBlue(0, page: page);
~ohm.setCrossfaderButtonFunc(0, r {
  loop {
    ~ohm.setActiveLeftKnobsBank(songBank);
    ~ohm.setActiveLeftSlidersBank(songBank);
    ~ohm.setActiveLeftKnobsBank(songBank);
    ~ohm.turnCrossfaderButtonPurple(0).yield;

    ~ohm.setActiveLeftKnobsBank(dryBank);
    ~ohm.setActiveLeftSlidersBank(dryBank);
    ~ohm.setActiveLeftKnobsBank(dryBank);
    ~ohm.turnCrossfaderButtonBlue(0).yield;
  }
}, page: page);

// Song Control:

// Chimes:

~ohm.turnLeftButtonGreen(0, page: page);
~ohm.setLeftSliderFunc(0, { | val | ~im.getSong('ElephantMusic').mixer.setVol(0, val.ccdbfs(-70, 0), 0.1); },
  page: page, bank: songBank);
~ohm.setLeftButtonFunc(0, {
  if( ~im.getSong('ElephantMusic').mixer.isMuted(0),
    { ~im.getSong('ElephantMusic').mixer.unMute(0); ~ohm.turnLeftButtonGreen(0); },
    { ~im.getSong('ElephantMusic').mixer.mute(0); ~ohm.turnLeftButtonRed(0); }
  );
}, page: page, bank: songBank);
// verb send:
~ohm.setLeftKnobFunc(0, 1, { | val | ~im.getSong('ElephantMusic').mixer.setSendVol(0, 1, val.ccdbfs(-70, 0), 0.1); },
  page: page, bank: songBank);
// granulator send:
~ohm.setLeftKnobFunc(0, 0, { | val | ~im.getSong('ElephantMusic').mixer.setSendVol(0, 2, val.ccdbfs(-70, 0), 0.1); },
  page: page, bank: songBank);


// Looper:

~ohm.turnLeftButtonGreen(1, page: page);
~ohm.setLeftSliderFunc(1, { | val | ~im.getSong('ElephantMusic').mixer.setVol(1, val.ccdbfs(-70, 0), 0.1); },
  page: page, bank: songBank);
~ohm.setLeftButtonFunc(0, {
  if( ~im.getSong('ElephantMusic').mixer.isMuted(1),
    { ~im.getSong('ElephantMusic').mixer.unMute(1); ~ohm.turnLeftButtonGreen(1); },
    { ~im.getSong('ElephantMusic').mixer.mute(1); ~ohm.turnLeftButtonRed(1); }
  );
}, page: page, bank: songBank);
// verb send:
~ohm.setLeftKnobFunc(1, 1, { | val | ~im.getSong('ElephantMusic').mixer.setSendVol(1, 1, val.ccdbfs(-70, 0), 0.1); },
  page: page, bank: songBank);
// granulator send:
~ohm.setLeftKnobFunc(1, 0, { | val | ~im.getSong('ElephantMusic').mixer.setSendVol(1, 2, val.ccdbfs(-70, 0), 0.1); },
  page: page, bank: songBank);



//////// Button Funcs:


// loop capture

// loop record/toggle:
~ohm.turnGridYellow(0, 0, page: page);
~ohm.setGridFunc(0, 0, r {
  loop {
    ~im.getSong('ElephantMusic').looper.toggleRecord;
    ~im.getSong('ElephantMusic').mixer.setSendVol(1, 1, -6);
    ~ohm.turnGridRed(0, 0).yield;

    ~im.getSong('ElephantMusic').looper.toggleRecord;
    ~ohm.turnGridGreen(0, 0).yield;
  }
}, page: page);

// loop start/stop:
~ohm.turnGridGreen(1, 0, page: page);
~ohm.setGridFunc(1, 0, r {
  loop {
    ~im.getSong('ElephantMusic').looper.togglePlay;
    ~ohm.turnGridRed(1, 0).yield;

    ~im.getSong('ElephantMusic').looper.togglePlay;
    ~ohm.turnGridGreen(1, 0).yield;
  };
}, page: page);

// reset loop:
~ohm.turnGridWhite(2, 0, page: page);
~ohm.setGridFunc(2, 0, { ~im.getSong('ElephantMusic').looper.reset }, page: page);


// Elephant Chimes:

// play chime:
~ohm.turnGridCyan(0, 1, page: page);
~ohm.setGridFunc(0, 1, { ~im.getSong('ElephantMusic').chimes.playTunedChime(8.rand); }, page: page);

// start and resume loop:
~ohm.turnGridBlue(1, 1, page: page);
~ohm.setGridFunc(1, 1, r {
  ~im.getSong('ElephantMusic').chimes.playIntro;
  loop {
    ~im.getSong('ElephantMusic').chimes.pauseIntro;
    ~ohm.turnGridRed(1, 1).yield;

    ~im.getSong('ElephantMusic').chimes.resumeIntro;
    ~ohm.turnGridGreen(1, 1).yield;
  }
}, page: page);


// cycle through preset changes:
~ohm.turnGridWhite(2, 1, page: page);
~ohm.setGridFunc(2, 1, r {
  loop {
    ~im.getSong('ElephantMusic').chimes.playIntro(attackTime: 10, attackJitter: 0.5);
    ~ohm.turnGridBlue(2, 1).yield;
    ~im.getSong('ElephantMusic').chimes.setIntroTempo(20);
    ~ohm.turnGridCyan(2, 1).yield;
    ~im.getSong('ElephantMusic').chimes.playIntro(volJitter: 20, attackJitter: 0.1, releaseJitter: 10, timingJitter: 2);
    ~ohm.turnGridYellow(2, 1).yield;
    ~im.getSong('ElephantMusic').chimes.setIntroTempo(1);
    ~ohm.turnGridPurple(2, 1).yield;
  }
}, page: page);

