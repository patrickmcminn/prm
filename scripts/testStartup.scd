/*
Tuesday, February 21st 2017
testStartup.scd
prm
startup script for testing code w/o an
audio interface
*/



Server.killAll
s.meter



//////////////////
//// Studio: ////
////////////////

r {
  ~prm = AudioSystem.new(2);

  ~moogDevice = "UMC1820";
  ~moogPort = "UMC1820";

  while( { try { ~prm.isLoaded } != true; }, { 0.001.wait; });

  //try { ~ohm = OhmRGB.new; };
  try { ~apc = APC40Mk2.new("mio10", "HST 2"); };
  try { ~base = Base.new("mio10", "HST 3"); };

  "/Global Functions/Load Global Functions.scd".loadRelative;
  "/System Scripts/Load System Scripts.scd".loadRelative;
  "/Songs/Load Songs.scd".loadRelative;

}.play;

//////////////////
//// Live: //////
////////////////

r {
  ~prm = AudioSystem.new(2);

  ~moogDevice = "UMC404HD 192k";
  ~moogPort = "UMC404HD 192k";

  while( { try { ~prm.isLoaded } != true; }, { 0.001.wait; });

  //try { ~ohm = OhmRGB.new; };
  try { ~apc = APC40Mk2.new; };
  try { ~base = Base.new; };

  "/Global Functions/Load Global Functions.scd".loadRelative;
  "/System Scripts/Load System Scripts.scd".loadRelative;
  "/Songs/Load Songs.scd".loadRelative;

}.play;