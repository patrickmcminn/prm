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
var falseSelfIsLoaded = false;
var lightIsLoaded = false;

~ohm.storageDict[\loadGlobalSubmixerFunc].value;
~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);

//////////// Grid Functions:


'Habit Fills Up What Remains';

~ohm.turnGridMagenta(0, 0, page: page);
~ohm.setGridFunc(0, 0, r {
  loop {
    ~habit = Habit.new(1, 2, 3, "iConnectAudio4+", "DIN",
      ~prm.submixA, ~prm.submixB, ~prm.submixC,
      ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus,
      relGroup: ~prm.procGroup, addAction: \addToHead);
    while({ try { ~habit.isLoaded } != true }, { 0.001.wait; });
    ~ohm.setGridFunc(1, 0, { ~ohm.setPage('habit'); }, page: 'main');
    ~ohm.turnGridGreen(1, 0).yield;

    ~habit.free;
    ~habit = nil;
    ~ohm.setGridFunc(1, 0, { });
    ~ohm.turnGridOff(1, 0).yield;
  }
}, page: page);

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
      connectionsIsLoaded = true;
    }.play;
    },
    {
      ~connections.free;
      ~ohm.setGridFunc(1, 1, { });
      ~ohm.turnGridOff(1, 1);
      connectionsIsLoaded = false;
  });
}, page: page);

// False Self:
~ohm.turnGridWhite(0, 2, page: page);
~ohm.setGridFunc(0, 2, {
  if( falseSelfIsLoaded == false, {
    r {
      ~falseSelf = FalseSelf.new(~prm.submixA, ~prm.submixB, ~prm.submixC,
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, nil,
        ~prm.procGroup, \addToHead);
      while({ try { ~falseSelf.isLoaded } != true }, { 0.001.wait; });
      ~ohm.setGridFunc(1, 2, { ~ohm.setPage('falseSelf'); }, page: page);
      ~ohm.turnGridGreen(1, 2);
      falseSelfIsLoaded = true;
    }.play;
    },
    {
      ~falseSelf.free;
      ~ohm.setGridFunc(1, 2, { });
      ~ohm.turnGridOff(1, 2);
      falseSelfIsLoaded = false;
  });
}, page: page);

// Light:
~ohm.turnGridYellow(0, 3, page: page);
~ohm.setGridFunc(0, 3, {
  if(lightIsLoaded == false, {
    r {
      ~lightSend = Light_Sends.new(~prm.submixA, ~prm.submixB, ~prm.submixC,
        ~prm.irLibrary.irDict['3.4Cathedral'], ~prm.reverb.inBus,
        relGroup: ~prm.procGroup, addAction: \addToHead);
      while({ try { ~lightSend.isLoaded } != true }, { 0.001.wait; });
      s.sync;
      ~light = Light.new(~prm.submixA, ~prm.submixB, ~prm.submixC,
        ~lightSend.reverb.inBus, ~lightSend.delays.inBus, ~prm.modularSend.inBus, ~lightSend.multiShift.inBus,
        ~prm.procGroup, \addToHead);
      while({ try { ~light.isLoaded } != true }, { 0.001.wait; });
      ~ohm.setGridFunc(1, 3, { ~ohm.setPage('light'); }, page: page);
      ~ohm.turnGridGreen(1, 3);
      lightIsLoaded = true;
    }.play;
    },
    {
      ~light.free;
      ~lightSend.free;
      ~ohm.setGridFunc(1, 3, {  });
      ~ohm.turnGridOff(1, 3);
  });
}, page: page);


/*
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



// Dauphine Street:
~ohm.turnGridMagenta(0, 2, page: page);
~ohm.setGridFunc(0, 2, {
  if( dauphineStreetIsLoaded == false, {
    r {
      ~dauphine = DauphineStreet.new(~prm.submixA, ~prm.submixB, ~prm.submixC, ~prm.reverb.inBus, ~prm.granulator.inBus,
        ~prm.modularSend.inBus, nil, ~prm.procGroup, \addToHead);
      while({ try { ~dauphine.isLoaded } != true }, { 0.001.wait; });
      ~ohm.setGridFunc(1, 2, { ~base.setPage('dauphine'); }, page: page);
      ~ohm.turnGridGreen(1, 2);
      dauphineStreetIsLoaded = true;
    }.play;
    },
    {
      ~dauphine.free;
      ~ohm.setGridFunc(1, 2, { });
      ~ohm.turnGridOff(1, 2);
      dauphineStreetIsLoaded = false;
  });
}, page: page);

// Cortez:
~ohm.turnGridCyan(0, 3, page: page);
~ohm.setGridFunc(0, 3, {
  if( cortezIsLoaded == false, {
    r {
      ~cortez = x = Cortez.new(~prm.submixA, ~prm.submixB, ~prm.submixC, ~prm.reverb.inBus,
        ~prm.granulator.inBus, ~prm.modularSend.inBus,
        relGroup: ~prm.procGroup, addAction: \addToHead);
      while({ try { ~cortez.isLoaded } != true }, { 0.001.wait; });
      ~ohm.setGridFunc(1, 3, { ~ohm.setPage('cortez'); }, page: page);
      ~ohm.turnGridGreen(1, 3);
      cortezIsLoaded = true;
    }.play;
    },
    {
      ~cortez.free;
      ~ohm.setGridFunc(1, 3, { });
      ~ohm.turnGridOff(1, 3);
      cortezIsLoaded = false;
  });
}, page: page);
*/




// mic pre/post:
~ohm.turnGridMagenta(0, 7, page: page);
~ohm.setGridFunc(0, 7, r {
  loop {
    ~prm.microphone.setSendPre;
    ~ohm.turnGridBlue(0, 7).yield;

    ~prm.microphone.setSendPost;
    ~ohm.turnGridMagenta(0, 7).yield;
  }
}, page: page);

//// mic:
~ohm.setLeftButtonMonitorFunc(3, {
  if( ~prm.microphone.isMuted, { ~ohm.turnLeftButtonRed(3); }, { ~ohm.turnLeftButtonGreen(3) });
}, page: page);
~ohm.setLeftButtonFunc(3, { ~prm.microphone.tglMute; }, page: page);
~ohm.setLeftSliderFunc(3, { | val |
  ~prm.microphone.setVol(val.ccdbfs(-70, 0));
}, page: page);

// sends:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~prm.microphone.setSendVol(0, val.ccdbfs(-70, 0));
}, page: page);
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~prm.microphone.setSendVol(1, val.ccdbfs(-70, 0));
}, page: page);
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~prm.microphone.setSendVol(2, val.ccdbfs(-70, 0));
}, page: page);

//// Moog:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~prm.moog.isMuted, { ~ohm.turnLeftButtonRed(2); }, { ~ohm.turnLeftButtonGreen(2) });
}, page: page);
~ohm.setLeftButtonFunc(2, { ~prm.moog.tglMute; }, page: page);
~ohm.setLeftSliderFunc(2, { | val | ~prm.moog.setVol(val.ccdbfs(-70, 0)); }, page: page);
// sends:
~ohm.setLeftKnobFunc(2, 2, { | val | ~prm.moog.setSendVol(0, val.ccdbfs(-70, 0)); }, page: page);
~ohm.setLeftKnobFunc(2, 1, { | val | ~prm.moog.setSendVol(1, val.ccdbfs(-70, 0)); }, page: page);
~ohm.setLeftKnobFunc(2, 0, { | val | ~prm.moog.setSendVol(2, val.ccdbfs(-70, 0)); }, page: page);

//// modular:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~prm.modular.isMuted, { ~ohm.turnLeftButtonRed(1); }, { ~ohm.turnLeftButtonGreen(1); })
}, page: page);
~ohm.setLeftButtonFunc(1, { ~prm.modular.tglMute; }, page: page);
~ohm.setLeftSliderFunc(1, { | val |
  ~prm.modular.setVol(val.ccdbfs(-70, 0));
}, page: page);

// sends:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~prm.modular.setSendVol(0, val.ccdbfs(-70, 0));
}, page: page);
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~prm.modular.setSendVol(1, val.ccdbfs(-70, 0));
}, page: page);
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~prm.modular.setSendVol(2, val.ccdbfs(-70, 0));
}, page: page);

// Returns Page:
~ohm.turnGridBlue(5, 7, page: page);
~ohm.setGridFunc(5, 7, { ~ohm.setPage('submixer'); }, page: page);
// Master EQ Page:
~ohm.turnGridMagenta(6, 7, page: page);
~ohm.setGridFunc(6, 7, { ~ohm.setPage('masterEQ'); }, page: page);
// Submixer Effects Page:
~ohm.turnGridCyan(7, 7, page: page);
~ohm.setGridFunc(7, 7, { ~ohm.setPage('submixer'); }, page: page);





