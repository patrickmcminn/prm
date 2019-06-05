/*
Wednesday, August 29th
APC40 Main Page.scd
prm
*/

var page = 'main';

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var foundationIsLoaded = false;
var falseSelfIsLoaded = false;

var habitIsLoaded = false;

var connectionsIsLoaded = false;

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.storageDict[\loadMasterFunc].value;

~apc.addMixerEncodersBanks(3);

/////// Grid Functions:
'Built on a Flawed Foundation';

~apc.turnGridCyan(0, 4, page: page);
~apc.setGridFunc(0, 4, {
  if(foundationIsLoaded == false, {
    r {
      ~foundationDelay = SimpleDelay.newStereo(~prm.audioIn, 2.5, 0.21, 2.5,
        relGroup: ~prm.procGroup, addAction: \addToHead);
      while({ try { ~foundationDelay.isLoaded } != true }, { 0.001.wait; });
      s.sync;
      ~foundationDelay.setMix(1);
      /*
      ~foundation = Foundation.new(~prm.audioIn,
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~foundationDelay.inBus,
        ~prm.procGroup, \addToHead, ~moogDevice, ~moogPort);
      */
      ~foundation = Foundation.new(~prm.audioIn, ~micIn, ~pickupIn, ~modInput[0], ~modOutput[7],
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
        ~moogDevice, ~moogPort, ~prm.procGroup, \addToHead);
      while({ try { ~foundation.isLoaded } != true }, { 0.001.wait; });
      ~apc.setGridFunc(1, 4, { ~apc.setPage('foundation'); }, page: page);
      ~apc.turnGridGreen(1, 4);
      foundationIsLoaded = true;
    }.play;
    },
    {
      ~foundation.free;
      ~foundationDelay.free;
      ~apc.setGridFunc(1, 4, { });
      ~apc.turnGridOff(1, 4);
      foundationIsLoaded = false;
  });
}, page: page);

'False Self';

~apc.turnGridWhite(0, 3, page: page);
~apc.setGridFunc(0, 3, {
  if(falseSelfIsLoaded == false, {
    r {
      ~falseSelf = FalseSelf.new(~prm.audioIn, ~moogDevice, ~moogPort,
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
        ~prm.procGroup, \addToHead);
      while({ try { ~falseSelf.isLoaded } != true }, { 0.001.wait; });
      ~apc.setGridFunc(1, 3, { ~apc.setPage('falseSelf'); }, page: page);
      ~apc.turnGridGreen(1, 3);
      falseSelfIsLoaded = true;
    }.play;
    },
    {
      ~falseSelf.free;
      ~apc.setGridFunc(1, 3, {});
      ~apc.turnGridOff(1, 3);
      falseSelfIsLoaded = false;
  });
}, page: page);

'Habit Fills Up What Remains';

~apc.turnGridPurple(4, 4, 2, page: page);
~apc.setGridFunc(4, 4, {
  if(habitIsLoaded == false, {
    r{
      ~habit = Habit.new(~prm.audioIn, 1, 2, 3, ~moogDevice, ~moogPort,
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus,
        relGroup: ~prm.procGroup, addAction: \addToHead);
      while({ try { ~habit.isLoaded } != true }, { 0.001.wait; });
      ~apc.setGridFunc(5, 4, { ~apc.setPage('habit'); }, page: page);
      ~apc.turnGridGreen(5, 4);
      habitIsLoaded = true;
    }.play;
    },
    {
      ~habit.free;
      ~apc.setGridFunc(5, 4, { });
      ~apc.turnGridOff(5, 4);
      habitIsLoaded = false;
  });
}, page: page);

'Connections';

~apc.turnGridBlue(4, 3, 2, page: page);
~apc.setGridFunc(4, 3, {
  if(connectionsIsLoaded == false, {
    r{
      ~connections = Connections.new(~prm.audioIn, ~micIn, ~pickupIn, ~modInput[0], ~modOutput[7],
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
        ~moogDevice, ~moogPort, ~prm.irLibrary.irDict['3.4Cathedral'], ~prm.procGroup, \addToHead);
      while({ try { ~connections.isLoaded } != true }, { 0.001.wait; });
      ~apc.setGridFunc(5, 3, { ~apc.setPage('connections'); }, page: page);
      ~apc.turnGridGreen(5, 3);
      connectionsIsLoaded = true;
    }.play;
  },
  {
    ~connections.free;
    ~apc.setGridFunc(5, 3, {});
    ~apc.turnGridOff(5, 3);
    connectionsIsLoaded = false;
  });
}, page: page);



//////// There Was a Time when the World was Suffused with Light ////////

~apc.turnSceneLaunchButtonPurple(0, 2, page: page);
~apc.setSceneLaunchFunc(0, { ~apc.setPage('light album'); }, page: page);


///////////////////////////////////
////// Convenience Mixer: ////////
/////////////////////////////////

5.do({ | i |
  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~prm.cmix.setVol(i, val.ccdbfs); }, page: page);
  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~prm.cmix.tglMute(i); }, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~prm.cmix.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~prm.cmix.preOrPost(i) == 'post',
      { ~prm.cmix.setSendPre(i); }, { ~prm.cmix.setSendPost(i) });
  }, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~prm.cmix.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  });

  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val | ~prm.cmix.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~prm.cmix.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~prm.cmix.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~prm.cmix.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~prm.cmix.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~prm.cmix.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~prm.cmix.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~prm.cmix.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

  //// send bank changes:
  ~apc.setDeviceButtonsMonitorFunc(\sends, {
    if( ~apc.activeMixerEncodersBank == send0,
      { ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1) });
    if( ~apc.activeMixerEncodersBank  == send1,
      { ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2) });
    if( ~apc.activeMixerEncodersBank  == send2,
      { ~apc.turnDeviceButtonOn(3) }, { ~apc.turnDeviceButtonOff(3) });
    if( ~apc.activeMixerEncodersBank == send3,
      { ~apc.turnDeviceButtonOn(4) }, { ~apc.turnDeviceButtonOff(4) });
  });

  ~apc.setDeviceButtonFunc(1, { ~apc.setActiveMixerEncodersBank(send0); });
  ~apc.setDeviceButtonFunc(2, { ~apc.setActiveMixerEncodersBank(send1);  });
  ~apc.setDeviceButtonFunc(3, { ~apc.setActiveMixerEncodersBank(send2); });
  ~apc.setDeviceButtonFunc(4, { ~apc.setActiveMixerEncodersBank(send3); });

});

//// beauty buttons:

// delay:
~apc.turnGridWhite(4, 1, page: page);
~apc.setGridFunc(4, 1, {
  ~prm.beauty.setDelayTimes(rrand(0.001, 0.1), rrand(0.001, 0.1),
    rrand(0.001, 0.1), rrand(0.001, 0.1));
  ~apc.turnGridColor(4, 1, 128.rand+1);
}, page: page);

// pans:
~apc.turnGridCyan(5, 1, page: page);
~apc.setGridFunc(5, 1, {
  ~prm.beauty.setPans(rrand(-1.0, 1.0), rrand(-1.0, 1.0), rrand(-1.0, 1.0), rrand(-1.0, 1.0));
  ~apc.turnGridColor(5, 1, 128.rand+1);
}, page: page);

// averages:
~apc.turnGridPink(6, 1, page: page);
~apc.setGridFunc(6, 1, {
  ~prm.beauty.setAverages(10000.rand, 15000.rand);
  ~apc.turnGridColor(6, 1, 128.rand+1);
}, page: page);

// smooths:
~apc.turnGridPurple(7, 1, page: page);
~apc.setGridFunc(7, 1, {
  ~prm.beauty.setSmooths(500.rand, 500.rand);
  ~apc.turnGridColor(7, 1, 128.rand+1);
}, page: page);

// low pass cutoffs:
~apc.turnGridBlue(5, 0, page: page);
~apc.setGridFunc(5, 0, {
  ~prm.beauty.setLowPassCutoffs(4000.rand + 200, 4000.rand + 300);
  ~apc.turnGridColor(5, 0, 128.rand+1);
}, page: page);

// high pass cutoffs:
~apc.turnGridMagenta(6, 0, page: page);
~apc.setGridFunc(6, 0, {
  ~prm.beauty.setHighPassCutoffs(350.rand, 350.rand);
  ~apc.turnGridColor(6, 0, 128.rand+1);
}, page: page);

// resonances:
~apc.turnGridYellowGreen(7, 0, page: page);
~apc.setGridFunc(7, 0, {
  ~prm.beauty.setResonances(0.5.rand, 0.5.rand);
  ~apc.turnGridColor(7, 0, 128.rand+1);
}, page: page);



