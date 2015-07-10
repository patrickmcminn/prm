/*
Thursday, January 22nd 2015
startup.scd
prm
Sydney, Australia
*/

r {
  ~prm = AudioSystem.new(2);

  while( { try { ~prm.isLoaded } != true; }, { 0.001.wait; });

  try { ~ohm = OhmRGB.new; };
  try { ~base = Base.new; };

  "/Global Functions/Load Global Functions.scd".loadRelative;
  "/System Scripts/Load System Scripts.scd".loadRelative;
  "/Songs/Load Songs.scd".loadRelative;

  "loaded".postn;
}.play;

s.meter;

