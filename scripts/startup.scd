/*
Thursday, January 22nd 2015
CreateOhm.scd
prm
Sydney, Australia
*/

s.waitForBoot {
  ~prm = AudioSystem.new(2);

  while( { try { ~prm.isLoaded } != true; }, { 0.001.wait; });

  ~ohm = OhmRGB.new;
  try { ~base = Base.new; };

  "/Global Functions/Load Global Functions.scd".loadRelative;
  "/System Scripts/Load System Scripts.scd".loadRelative;
};