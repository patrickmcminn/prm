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

  try { ~ohm = OhmRGB.new; };
  try { ~base = Base.new; };

  "/Global Functions/Load Global Functions.scd".loadRelative;
  "/System Scripts/Load System Scripts.scd".loadRelative;
  //"/Songs/Load Songs.scd".loadRelative;

}.play;


s.meter;


~ohm.setPage('habit');