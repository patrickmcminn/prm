/*
Tuesday, March 4th 2014
Donkey Jaw.scd
prm
*/

var page = 'donkeyJaw';
var donkeyBank = 0;
var dryBank = 1;

~ohm.makePage(page);
~ohm.addLeftSlidersBanks(1, page: page);
~ohm.addLeftKnobsBanks(1, page: page);
~ohm.addLeftButtonsBanks(1, page: page);

~ohm.turnAllGridCyan(page: page);

~ohmFunc.addMasterFaders(page);
~ohmFunc.addDryFaders(page, dryBank);
// go home:
~ohm.turnControlButtonGreen(3, 0, page: page);
~ohm.setControlButtonFunc(3, 0, { ~ohm.setPage('main'); }, page: page);

// Harmonizer:

/*
~ohm.turnGridWhite(0, 0, page: page)
~ohm.setGridFunc(0, 0, r {
  loop {
*/

// Interlude:
~ohm.turnGridPurple(0, 1, page: page);
~ohm.setGridFunc(0, 1, r {
  loop {
    ~donkeyJawInterlude = IM_DonkeyJaw_Interlude.new(~im.systemMixer.inBus, relGroup: ~im.procGroup, addAction: \addToTail);
    ~ohm.setGridFunc;
    ~ohm.turnGridYellow(1, 1);
    ~ohm.turnGridWhite(0, 1).yield;

    ~donkeyJawInterlude.free;
    ~ohm.setGridFunc(1, 1, { });
    ~oh.turnGridOff(1, 1);
    ~ohm.turnGridRed(0, 1).yield;
  }
}, page: page);

// change banks:
~ohm.turnGridPurple(0, 7, page: page);
~ohm.setGridFunc(0, 7, r {
  loop {
    ~ohm.setActiveLeftKnobsBank(dryBank);
    ~ohm.setActiveLeftSlidersBank(dryBank);
    ~ohm.setActiveLeftButtonsBank(dryBank);
    ~ohm.turnGridYellow(0, 7).yield;

    ~ohm.setActiveLeftKnobsBank(donkeyBank);
    ~ohm.setActiveLeftSlidersBank(donkeyBank);
    ~ohm.setActiveLeftButtonsBank(donkeyBank);
    ~ohm.turnGridPurple(0, 7).yield;
  };
}, page: page);

//////// Faders:

// Interlude Volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~donkeyJawInterlude.setVol(val.ccdbfs(-70, 0));
}, page: page, bank: donkeyBank);

// reverb send:
~ohm.setLeftKnobFunc(3, 2, { | val |
    ~donkeyJawInterlude.mixer.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, page: page, bank: donkeyBank);

// granulator send:
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~donkeyJawInterlude.mixer.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, page: page, bank: donkeyBank);