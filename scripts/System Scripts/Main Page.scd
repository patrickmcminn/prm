/*
Thursday, January 22nd 2015
Main Page.scd
prm
Sydney, Australia
*/

var page = 'main';

~ohm.storageDict[\loadGlobalSubmixerFunc].value;
~ohm.setPageLoadFunction({
  ~ohm.storageDict[\loadGlobalSubmixerFunc].value;
}, page);

//////////// Grid Functions:

// Nebula Page:
~ohm.turnGridCyan(0, 0, page: page);
~ohm.setGridFunc(0, 0, r {
  loop {
    ~prm.songBook[\nebula] = TrumpetNebula.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus,
      relGroup: ~prm.procGroup, addAction: \addToHead);
    while({ try { ~prm.songBook[\nebula].isLoaded } != true }, { 0.001.wait; });
    ~prm.songBook[\nebulaInput] = IM_HardwareIn.new(0, ~prm.songBook[\nebula].inBus, ~prm.procGroup, \addToHead);
    while({ try { ~prm.songBook[\nebulaInput].isLoaded } != true }, { 0.001.wait; });
    ~ohm.setGridFunc(1, 0, { ~ohm.setPage('nebula'); }, page: page);
    ~ohm.turnGridGreen(1, 0).yield;

    ~prm.songBook[\nebulaInput].free;
    ~prm.songBook[\nebula].free;
    ~ohm.setGridFunc(1, 0, { });
    ~ohm.turnGridRed(1, 0).yield;
  }
}, page: page);

// Returns Page:
~ohm.turnGridBlue(5, 7, page: page);
~ohm.setGridFunc(5, 7, { ~ohm.setPage('returns'); }, page: page);
// Master EQ Page:
~ohm.turnGridPurple(6, 7, page: page);
~ohm.setGridFunc(6, 7, { ~ohm.setPage('masterEQ'); }, page: page);
// Submixer Effects Page:
~ohm.turnGridCyan(7, 7, page: page);
~ohm.setGridFunc(7, 7, { ~ohm.setPage('submixer'); }, page: page);





