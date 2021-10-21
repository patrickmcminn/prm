/*
Main Page.scd
Thursday, December 11th 2019
prm
*/

var page = 'main';

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var send01 = 4;
var send11 = 5;
var send21 = 6;
var send31 = 7;

var activeCMix = 0;

var cmix0 = 0;
var cmix1 = 1;

var foundationIsLoaded = false;
var falseSelfIsLoaded = false;

var habitIsLoaded = false;

var birdsIsLoaded = false;
var connectionsIsLoaded = false;

var meaningIsLoaded = false;

var chainIsLoaded = false;

var tuningNoteIsPlaying = false;

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.storageDict[\loadMasterFunc].value;

~apc.addMixerEncodersBanks(7, page);
~apc.addDeviceButtonsBanks(1, page);
~apc.addMixerBanks(1, page);

//////// Sampler Page:
~apc.turnSceneLaunchButtonPurple(2, 2, page: page);
~apc.setSceneLaunchFunc(2, { ~apc.setPage('sampler') }, page: page);

//////// Effects Page:
~apc.turnSceneLaunchButtonPurple(4, 2, page: page);
~apc.setSceneLaunchFunc(4, { ~apc.setPage('effects'); }, page: page);

//////// Subtractive Page:
~apc.turnSceneLaunchButtonPink(3, 2, page: page);
~apc.setSceneLaunchFunc(3, { ~apc.setPage('subtractive'); }, page: page);

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
				~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~foundationDelay.inBus,
				~moogDevice, ~moogPort, ~pyr, ~prm.procGroup, \addToHead);
      //~foundation = Foundation.new(~prm.audioIn, ~micIn, ~pickupIn, ~modInput[0], ~modOutput[7],
        //~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~foundationDelay.inBus,
        //~moogDevice, ~moogPort, ~prm.procGroup, \addToHead);
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

/*
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
*/

'Where the Birds Must Be Drunk to be Amidst the Unknown Spray and Sky';
~apc.turnGridPink(2, 4, 2, page: page);
~apc.setGridFunc(2, 4, {
  if( birdsIsLoaded == false, {
    r{
      ~birds = WhereTheBirds.new(~prm.audioIn, ~micIn, ~modInput[0], ~modInput[2], ~modInput[3],
        ~modOutput[2], ~modOutput[3], ~modOutput[4], ~modOutput[7],
        ~moogDevice, ~moogPort, ~pyr,
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
        ~prm.procGroup, \addToHead);
      while({ try { ~birds.isLoaded } != true }, { 0.001.wait; });
      ~apc.setGridFunc(3, 4, { ~apc.setPage('birds') }, page: page);
      ~apc.turnGridGreen(3, 4);
      birdsIsLoaded = true;
    }.play;
  },
  {
    ~birds.free;
    ~apc.setGridFunc(3, 4, { });
    ~apc.turnGridOff(3, 4);
    birdsIsLoaded = false;
  });
}, page: page);

'Connections';

~apc.turnGridBlue(2, 2, 2, page: page);
~apc.setGridFunc(2, 2, {
  if(connectionsIsLoaded == false, {
    r{
			/*
      ~connections = Connections.new(~prm.audioIn, ~micIn, ~pickupIn, ~modInput[0], ~modOutput[7],
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
        ~moogDevice, ~moogPort, ~prm.irLibrary.irDict['3.4Cathedral'], ~prm.procGroup, \addToHead);
			*/
			/*
			~connections = Connections.new(~prm.audioIn, ~micIn, ~pickupIn, ~modInput[0], ~prm.reverb.inBus,
				~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus, ~moogDevice, ~moogPort,
				~prm.procGroup, \addToHead);
			*/
			~connections = Connections.new(~prm.audioIn, ~micIn, ~pickupIn, ~modInput[0], ~modInput[1], ~modInput[3],
				~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
				~prm.procGroup, \addToHead);
      while({ try { ~connections.isLoaded } != true }, { 0.001.wait; });
      ~apc.setGridFunc(3, 2, { ~apc.setPage('connections'); }, page: page);
      ~apc.turnGridGreen(3, 2);
      connectionsIsLoaded = true;
    }.play;
  },
  {
    ~connections.free;
    ~apc.setGridFunc(3, 2, {});
    ~apc.turnGridOff(3, 2);
    connectionsIsLoaded = false;
  });
}, page: page);


'everything was pregnant with meaning';
~apc.turnGridMagenta(4, 4, page: page);
~apc.setGridFunc(4, 4, {
  if( meaningIsLoaded == false, {
    r{
      /*
      ~meaning = Meaning.new(~prm.audioIn, ~prm.modularSend.inBus, ~prm.irLibrary.irDict['2.0MediumHall'],
      ~prm.procGroup, \addToHead);
      */
      ~meaningDelay = Meaning_Delay.new(~prm.audioIn, ~prm.procGroup, \addToHead);
      while({ try { ~meaningDelay.isLoaded } != true }, { 0.001.wait; });
      ~meaning = Meaning.new(~prm.audioIn, ~micIn, ~modInput[0], ~modInput[1], ~pyr,
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~meaningDelay.inBus,
        ~prm.procGroup, \addToHead);
      while({ try { ~meaning.isLoaded } != true }, { 0.001.wait; });
      ~apc.turnGridGreen(5, 4);
      ~apc.setGridFunc(5, 4, { ~apc.setPage('meaning'); }, page: page);
      meaningIsLoaded = true;
    }.play;
  },
  {
    ~meaningDelay.free;
    ~meaning.free;
    ~apc.setGridFunc(5, 4, {});
    ~apc.turnGridOff(5, 4);
    meaningIsLoaded = false;
  });
}, page: page);

/*
'CHAIN:';
~apc.turnGridWhite(0, 0, page: page);
~apc.setGridFunc(0, 0, {
  if(chainIsLoaded == false, {
    r {

      ~chain = Chain.new(~prm.audioIn,
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus,
        ~prm.delay.inBus, ~prm.procGroup, \addToHead);
      while({ try { ~chain.isLoaded } != true }, { 0.001.wait; });
      ~chInput = IM_HardwareIn.new(~pickupIn, ~chain.inBus, ~prm.procGroup, \addToHead);
      while({ try { ~chInput.isLoaded } != true }, { 0.001.wait; });
      ~chMic = IM_HardwareIn.new(~micIn, ~chain.inBus, ~prm.procGroup, \addToHead);
      while({ try { ~chMic.isLoaded } != true }, { 0.001.wait; });
      ~apc.turnGridGreen(1, 0);
      ~apc.setGridFunc(1, 0, { ~apc.setPage('chain'); }, page: page);
      chainIsLoaded = true;
    }.play;
  },
  {
    ~chain.free;
    ~chInput.free;
    ~chMic.free;
    ~apc.setGridFunc(1, 0, {});
    ~apc.turnGridOff(1, 0);
    chainIsLoaded = false; \
  });
}, page: page);
*/

/*
'Habit Fills Up What Remains';

~apc.turnGridPurple(4, 4, 2, page: page);
~apc.setGridFunc(4, 4, {
  if(habitIsLoaded == false, {
    r{
			/*
      ~habit = Habit.new(~prm.audioIn, 1, 2, 3, ~moogDevice, ~moogPort,
        ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus,
        relGroup: ~prm.procGroup, addAction: \addToHead);
			*/
			~habit = Habit.new(~prm.audioIn, ~micIn, ~modInput, ~modOutput, ~moogDevice, ~moogPort, ~pyr, ~prm.delay,
				~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
				~prm.procGroup, \addToHead);
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
*/


/*
//////// There Was a Time when the World was Suffused with Light ////////

~apc.turnSceneLaunchButtonPurple(0, 2, page: page);
~apc.setSceneLaunchFunc(0, { ~apc.setPage('light album'); }, page: page);

*/

///////////////////////////////////
////// Convenience Mixer: ////////
/////////////////////////////////

8.do({ | i |
  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~prm.cmix.setVol(i, val.ccdbfs); }, bank: cmix0, page: page);
  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~prm.cmix.tglMute(i); }, bank: cmix0, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~prm.cmix.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, bank: cmix0, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~prm.cmix.preOrPost(i) == 'post',
      { ~prm.cmix.setSendPre(i); }, { ~prm.cmix.setSendPost(i) });
  }, bank: cmix0, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~prm.cmix.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, bank: cmix0, page: page);

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
  }, bank: cmix0, page: page);

  ~apc.setDeviceButtonFunc(1, { ~apc.setActiveMixerEncodersBank(send0); }, bank: cmix0, page: page);
  ~apc.setDeviceButtonFunc(2, { ~apc.setActiveMixerEncodersBank(send1);  }, bank: cmix0, page: page);
  ~apc.setDeviceButtonFunc(3, { ~apc.setActiveMixerEncodersBank(send2); }, bank: cmix0, page: page);
  ~apc.setDeviceButtonFunc(4, { ~apc.setActiveMixerEncodersBank(send3); }, bank: cmix0, page: page);

});

// ext ins:

~apc.setMixerMonitorFunc(\cMix0Enable, {
	if( ~prm.microphone.isMuted, { ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
	if( ~prm.pickup.isMuted, { ~apc.turnRecordEnableButtonOff(2) }, { ~apc.turnRecordEnableButtonOn(2) });
	if( ~prm.mod1.isMuted, { ~apc.turnRecordEnableButtonOff(3) }, { ~apc.turnRecordEnableButtonOn(3) });
	if( ~prm.mod2.isMuted, { ~apc.turnRecordEnableButtonOff(4) }, { ~apc.turnRecordEnableButtonOn(4) });
	if( ~prm.mod3.isMuted, { ~apc.turnRecordEnableButtonOff(5) }, { ~apc.turnRecordEnableButtonOn(5) });
	if( ~prm.mod4.isMuted, { ~apc.turnRecordEnableButtonOff(6) }, { ~apc.turnRecordEnableButtonOn(6) });
}, bank: cmix0, page: page);

~apc.setRecordEnableButtonFunc(1, { ~prm.microphone.tglMute; }, bank: cmix0, page: page);
~apc.setRecordEnableButtonFunc(2, { ~prm.pickup.tglMute; }, bank: cmix0, page: page);
~apc.setRecordEnableButtonFunc(3, { ~prm.mod1.tglMute; }, bank: cmix0, page: page);
~apc.setRecordEnableButtonFunc(4, { ~prm.mod2.tglMute; }, bank: cmix0, page: page);
~apc.setRecordEnableButtonFunc(5, { ~prm.mod3.tglMute; }, bank: cmix0, page: page);
~apc.setRecordEnableButtonFunc(6, { ~prm.mod4.tglMute; }, bank: cmix0, page: page);


//////// second mixer page:
~apc.mapMixer(~prm.cmix, 4, 8, 0, send01, send11, send21, send31, cmix1, page);
~apc.mapSendBanks(\cmix1, send01, send11, send21, send31, cmix1, page);

~apc.setMixerMonitorFunc(\cmix1Enable, {
	if( ~prm.threeAndFour.isMuted, { ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
	if( ~prm.five.isMuted, { ~apc.turnRecordEnableButtonOff(2) }, { ~apc.turnRecordEnableButtonOn(2) });
	if( ~prm.six.isMuted, { ~apc.turnRecordEnableButtonOff(3) }, { ~apc.turnRecordEnableButtonOn(3) });
	if( ~prm.sevenAndEight.isMuted, { ~apc.turnRecordEnableButtonOff(4) }, { ~apc.turnRecordEnableButtonOn(4) });
}, bank: cmix1, page: page);
~apc.setRecordEnableButtonFunc(1, { ~prm.threeAndFour.tglMute }, bank: cmix1, page: page);
~apc.setRecordEnableButtonFunc(2, { ~prm.fix.tglMute }, bank: cmix1, page: page);
~apc.setRecordEnableButtonFunc(3, { ~prm.six.tglMute }, bank: cmix1, page: page);
~apc.setRecordEnableButtonFunc(4, { ~prm.sevenAndEight.tglMute }, bank: cmix1, page: page);


//////// Page Change:

~apc.turnDeviceButtonOn(5, cmix0, page);
~apc.setDeviceButtonFunc(6, {
	activeCMix = 1;
	~apc.setActiveMixerBank(activeCMix);
	~apc.setActiveDeviceButtonsBank(activeCMix);
	~apc.setActiveMixerEncodersBank(send01);
}, bank: cmix0, page: page);

~apc.turnDeviceButtonOn(6, cmix1, page);
~apc.setDeviceButtonFunc(5, {
	activeCMix = 0;
	~apc.setActiveMixerBank(activeCMix);
	~apc.setActiveDeviceButtonsBank(activeCMix);
	~apc.setActiveMixerEncodersBank(send0);
}, bank: cmix1, page: page);


//////// tuning note:
~apc.setBankButtonFunc({
  if( tuningNoteIsPlaying == false,
    {
			~tuner = { Out.ar(~prm.audioIn, SinOsc.ar(60.midicps) * (-12.dbamp)) }.play;
			tuningNoteIsPlaying = true;
			~apc.turnBankButtonOn;
		},
    {
			~tuner.free; tuningNoteIsPlaying = false;
			~apc.turnBankButtonOff;
	});
}, page: page);

