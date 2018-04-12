/*
Thursday, February 20th 2014
OhmMain.scd
*/


var page = 'main';
var dryBank = 0;
var fxBank = 1;

~ohm.addLeftKnobsBanks(1);

~ohmFunc.addMasterFaders(page);
~ohmFunc.addDryFaders(page, dryBank);


// Serpent Gang:
~ohm.turnGridYellow(0, 0, page: page);
~ohm.setGridFunc(0, 0, r {
  loop {
    ~im.makeSong('SerpentGang');
    //~serpentGang = IM_SerpentGang.new(1, 2, 10, ~im.systemMixer.inBus, ~im.delay.inBus, ~im.reverb.inBus,
      //~im.granulator.inBus, nil, ~im.procGroup, \addToHead);
    ~ohm.setGridFunc(1, 0, { ~ohm.setPage('serpentGang'); });
    ~ohm.turnGridWhite(1, 0);
    ~ohm.turnGridGreen(0, 0).yield;

    ~im.freeSong('SerpentGang');
    //~serpentGang.free;
    ~ohm.setGridFunc(1, 0, {});
    ~ohm.turnGridOff(1, 0);
    ~ohm.turnGridRed(0, 0).yield;
  }
}, page: page);

~ohm.turnGridBlue(0, 1, page: page);
~ohm.setGridFunc(0, 1, r {
  loop {
    ~im.makeSong('FicusBlossom');
    //~ficusBlossom = IM_FicusBlossom.new(3, ~im.systemMixer.inBus, ~im.delay.inBus, ~im.reverb.inBus, ~im.granulator.inBus,
      //nil, ~im.procGroup, \addToHead);
    ~ohm.setGridFunc(1, 1, { ~ohm.setPage('ficusBlossom'); });
    ~ohm.turnGridCyan(1, 1);
    ~ohm.turnGridGreen(0, 1).yield;

    ~im.freeSong('FicusBlossom');
    //~ficusBlossom.free;
    ~ohm.setGridFunc(1, 1, { });
    ~ohm.turnGridOff(1, 1);
    ~ohm.turnGridRed(0, 1).yield;
  }
}, page: page);

// Donkey Jaw:
~ohm.turnGridCyan(0, 2);
//~ohm.setGridFunc(0, 2, { ~ohm.setPage('donkeyJaw'); }, page: page);

// Für Alina:
~ohm.turnGridPurple(0, 3);


// Elephant Music:
~ohm.turnGridWhite(0, 4);
~ohm.setGridFunc(0, 4, r {
  loop {
    ~im.makeSong('ElephantMusic');
    //~elephantMusic = IM_ElephantMusic.new(~im.systemMixer.inBus, 10, ~im.delay.inBus, ~im.reverb.inBus, ~im.granulator.inBus, nil,
     // ~im.procGroup, \addToHead);
    ~ohm.setGridFunc(1, 4, { ~ohm.setPage('elephantMusic'); });
    ~ohm.turnGridBlue(1, 4);
    ~ohm.turnGridGreen(0, 4).yield;

    ~im.freeSong('ElephantMusic');
    //~elephantMusic.free;
    ~ohm.setGridFunc(1, 4, { });
    ~ohm.turnGridOff(1, 4);
    ~ohm.turnGridRed(0, 4).yield;
  };
}, page: page);

// Sirens:
~ohm.turnGridYellow(0, 5);
~ohm.setGridFunc(0, 5, r {
  loop {
    ~im.makeSong('Sirens');
    //~sirens = IM_Sirens.new(~im.systemMixer.inBus, ~im.delay.inBus, ~im.reverb.inBus,
      //~im.granulator.inBus, nil, ~im.procGroup, \addToHead);
    ~ohm.setGridFunc(1, 5, { ~ohm.setPage('sirens');  });
    ~ohm.turnGridWhite(1, 5);
    ~ohm.turnGridGreen(0, 5).yield;

    ~im.freeSong('Sirens');
    //~sirens.free;
    ~ohm.setGridFunc(1, 5, { });
    ~ohm.turnGridOff(1, 5);
    ~ohm.turnGridRed(0, 5).yield;
  };
}, page: page);

// Bank Change:

~ohm.turnCrossfaderButtonBlue(0, page: page);
~ohm.setCrossfaderButtonFunc(0, {
  if( ~ohm.activePage.activeLeftKnobsBank == dryBank,
    { ~ohm.setActiveLeftKnobsBank(fxBank); ~ohm.turnCrossfaderButtonCyan(0); },
    { ~ohm.setActiveLeftKnobsBank(dryBank); ~ohm.turnCrossfaderButtonBlue(0); }
  );
}, page: page);



// Granulator:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var trigRate = val.linlin(0, 127, 12, 100);
  ~im.granulator.setTrigRate(trigRate);
}, page: page, bank: fxBank);

~ohm.setLeftKnobFunc(1, 0, { | val |
  var grainDur = val.linlin(0, 127, 0.01, 1);
  ~im.granulator.setGrainDurLow(grainDur);
}, page: page, bank: fxBank);

~ohm.setLeftKnobFunc(2, 0, { | val |
  var grainDurOffset = val.linlin(0, 127, 0.01, 3);
  ~im.granulator.setGrainDurHigh(grainDurOffset);
}, page: page, bank: fxBank);


// Delay:

~ohm.setLeftKnobFunc(0, 1, { | val |
  var scale = val.linlin(0, 127, 1, 8);
  ~im.delay.setLPFreqScale(scale);
}, page: page, bank: fxBank);

~ohm.setLeftKnobFunc(1, 1, { | val |
  var sampleRate = val.linlin(0, 127, 22000, 5000);
  ~im.delay.setSampleRate(sampleRate);
}, page: page, bank: fxBank);


~ohm.setLeftKnobFunc(2, 1, { | val |
  var feedback = val.linlin(0, 127, 0, 1);
  ~im.delay.setFeedback(feedback);
}, page: page, bank: fxBank);


