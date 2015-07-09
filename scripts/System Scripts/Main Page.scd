/*
Thursday, January 22nd 2015
Main Page.scd
prm
Sydney, Australia
*/

var page = 'main';

var connectionsIsLoaded = false;
var dauphineStreetIsLoaded = false;
var cortezIsLoaded = false;

~ohm.storageDict[\loadGlobalSubmixerFunc].value;
~ohm.setPageLoadFunction({
  ~ohm.storageDict[\loadGlobalSubmixerFunc].value;
}, page);

//////////// Grid Functions:

// Nebula Page:
~ohm.turnGridCyan(0, 0, page: page);
// not working:
/*
~ohm.setGridFunc(0, 0, r {
  loop {
    ~prm.songBook[\nebula] = TrumpetNebula.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend,
      relGroup: ~prm.procGroup, addAction: \addToHead);
    while({ try { ~prm.songBook[\nebula].isLoaded } != true }, { 0.001.wait; "balls".postln });
    ~prm.songBook[\nebulaInput] = IM_HardwareIn.new(0, ~prm.songBook[\nebula].inBus, ~prm.procGroup, \addToHead);
    while({ try { ~prm.songBook[\nebulaInput].isLoaded } != true }, { 0.001.wait; });
    ~ohm.setGridFunc(1, 0, { ~ohm.setPage('nebula'); }, page: 'main');
    ~ohm.turnGridGreen(1, 0).yield;

    ~prm.songBook[\nebulaInput].free;
    ~prm.songBook[\nebula].free;
    ~ohm.setGridFunc(1, 0, { });
    ~ohm.turnGridOff(1, 0).yield;
  }
}, page: page);
*/

// Connections:
~ohm.turnGridBlue(0,1, page: page);
~ohm.setGridFunc(0, 1,  {
  if( connectionsIsLoaded == false, {
    r {
      ~connections = Connections.new(~prm.submixA, ~prm.submixB, ~prm.submixC,
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, nil, ~prm.procGroup, \addToHead);
      while({ try { ~connections.isLoaded } != true }, { 0.001.wait; });
      ~ohm.setGridFunc(1, 1, { ~ohm.setPage('connections'); }, page: page);
      ~ohm.turnGridGreen(1, 1);
    }.play;
    },
    {
      ~connections.free;
      ~ohm.setGridFunc(1, 1, { });
      ~ohm.turnGridOff(1, 1);
  });
}, page: page);

// Dauphine Street:
~ohm.turnGridPurple(0, 2, page: page);
~ohm.setGridFunc(0, 2, {
  if( dauphineStreetIsLoaded == false, {
    r {
      ~dauphine = DauphineStreet.new(~prm.submixA, ~prm.submixB, ~prm.submixC, ~prm.reverb.inBus, ~prm.granulator.inBus,
        ~prm.modularSend.inBus, nil, ~prm.procGroup, \addToHead);
      while({ try { ~dauphine.isLoaded } != true }, { 0.001.wait; });
      ~ohm.setGridFunc(1, 2, { ~base.setPage('dauphine'); }, page: page);
      ~ohm.turnGridGreen(1, 2);
    }.play;
    },
    {
      ~dauphine.free;
      ~ohm.setGridFunc(1, 2, { });
      ~ohm.turnGridOff(1, 2);
  });
}, page: page);

// Cortez:

// coming soon




// Returns Page:
~ohm.turnGridBlue(5, 7, page: page);
~ohm.setGridFunc(5, 7, { ~ohm.setPage('submixer'); }, page: page);
// Master EQ Page:
~ohm.turnGridPurple(6, 7, page: page);
~ohm.setGridFunc(6, 7, { ~ohm.setPage('masterEQ'); }, page: page);
// Submixer Effects Page:
~ohm.turnGridCyan(7, 7, page: page);
~ohm.setGridFunc(7, 7, { ~ohm.setPage('submixer'); }, page: page);





