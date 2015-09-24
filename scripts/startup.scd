/*
Thursday, January 22nd 2015
startup.scd
prm
Sydney, Australia
*/

r {
  ~prm = AudioSystem.new(2);

  while( { try { ~prm.isLoaded } != true; }, { 0.001.wait; });

  try { ~ohm = OhmRGB.new("iConnectAudio4+", "HST1"); };
  try { ~base = Base.new("iConnectAudio4+", "HST3"); };

  "/Global Functions/Load Global Functions.scd".loadRelative;
  "/System Scripts/Load System Scripts.scd".loadRelative;
  "/Songs/Load Songs.scd".loadRelative;
}.play

