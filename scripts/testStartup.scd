/*
Tuesday, February 21st 2017
testStartup.scd
prm
startup script for testing code w/o an
audio interface
*/

/*var device, ohm, base;
device = "iConnectAudio4+";
//ohm = "Ohm Port 1";
//base = "Base Port 1";
ohm = "HST1";
base = "HST3";*/

//
// s.options.blockSize = 64;
// s.options.memSize = 131072;
// s.options.numAudioBusChannels = 512;

//s.boot;

r {
  ~prm = AudioSystem.new(2);

  while( { try { ~prm.isLoaded } != true; }, { 0.001.wait; });

  // convenience mixer
  ~convMix = IM_Mixer.new(4, ~prm.audioIn,
    ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
    false, ~prm.procGroup, \addToHead);
  while({ try { ~convMix.isLoaded } != true }, { 0.001.wait; });
  ~convMix.do({ | chan | ~convMix.mute(chan); });

  try { ~ohm = OhmRGB.new; };
  try { ~apc = APC40Mk2.new; };
  //try { ~apc = APC40Mk2.new("mio10", "HST 2"); };
  try { ~base = Base.new; };


  "/Global Functions/Load Global Functions.scd".loadRelative;
  "/System Scripts/Load System Scripts.scd".loadRelative;
  "/Songs/Load Songs.scd".loadRelative;

}.play;


s.meter;
s.makeWindow
AudioSystem
~ohm.setPage('main');
~ohm.setPage('darkness');

~ohm.setPage('habit');
~base.setPage('habit');

Server.killAll;


MIDIIn.connectAll;
~ohm = OhmRGB.new("iConnectAudio4+", "HST1");
~base = Base.new("iConnectAudio4+", "HST3");


~lightSend = Light_Sends.new(~prm.submixA, ~prm.submixB, ~prm.submixC,
        ~prm.irLibrary.irDict['3.4Cathedral'], ~prm.reverb.inBus,
        relGroup: ~prm.procGroup, addAction: \addToHead);
~light = Light.new(~prm.submixA, ~prm.submixB, ~prm.submixC,
        ~lightSend.reverb.inBus, ~lightSend.delays.inBus, ~prm.modularSend.inBus, ~lightSend.multiShift.inBus,
        ~prm.procGroup, \addToHead);


~lightSend.isLoaded;

~lightMain = Light_Main.new(0);
~lightMain.isLoaded;

~light.isLoaded;

~light.bass.isLoaded;

~light.mixerB.chanStereo

Light_Bass

