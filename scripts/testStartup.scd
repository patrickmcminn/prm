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

  //// Audio Inputs:
  ~micIn = 0;
  ~pickupIn = 1;

  ~di1In = 2;
  ~di2In = 3;

  ~junoIn = 4;

  ~58In = 5;

  ~k2500RIn = [6, 7];

  ~bassIn = 8;

  ~modInput = Array.fill(4, { | i | 32 + i });
  ~modOutput = Array.fill(8, { | i | 32 + i }); ;


  //// MIDI Devices:


  // M32:
  ~moogDevice = "mio10";
  ~moogPort = "Moog Mother 32";

  // Juno 106:
  ~junoDevice = "mio10";
  ~junoPort = "Roland Juno 106"

  // K2500R:
  ~k2500RDevice = "mio10";
  ~k2500RPort = "Kurzweil K2500R";


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