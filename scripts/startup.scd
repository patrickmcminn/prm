/*
Thursday, January 22nd 2015
startup.scd
prm
Sydney, Australia
*/

var device, ohm, base;
device = "iConnectAudio4+";
//ohm = "Ohm Port 1";
//base = "Base Port 1";
ohm = "HST1";
base = "HST3";

r {
  ~prm = AudioSystem.new(2);

  while( { try { ~prm.isLoaded } != true; }, { 0.001.wait; });

  try { ~ohm = OhmRGB.new(device, ohm); };
  try { ~base = Base.new(device, base); };

  "/Global Functions/Load Global Functions.scd".loadRelative;
  "/System Scripts/Load System Scripts.scd".loadRelative;
  "/Songs/Load Songs.scd".loadRelative;

}.play;

  s.meter;
