
/*
Tuesday, February 21st 2017
testStartup.scd
prm
startup script for testing code w/o an
audio interface
*/



Server.killAll
s.meter
s.makeWindow

//////////////////
//// Studio: ////
////////////////

(
r {
	//// Audio Inputs:
	~micIn = 0;
	~pickupIn = 1;

	~di1In = 2;
	~di2In = 3;

	~junoIn = 4;

	~shure58In = 5;

	~k2500RIn = [6, 7];

	~bassIn = 8;

	~modInput = Array.fill(4, { | i | 32 + i });
	~modOutput = Array.fill(8, { | i | 32 + i });


	//// MIDI Devices:

	// M32:
	~moogDevice = "Pyramid MIDI USB";
	~moogPort = "Pyramid MIDI USB";
	~moogIn = ~modInput[0];


	// Juno 106:
	~junoDevice = "mio10";
	~junoPort = "DIN 2";

	// K2500R:
	~k2500RDevice = "mio10";
	~k2500RPort = "DIN 3";

	~prm = AudioSystem.new(2, ~micIn, ~pickupIn, ~modInput, ~modOutput);

	while( { try { ~prm.isLoaded } != true; }, { 0.001.wait; });

	s.sync;

	try { ~apc = APC40Mk2.new; };
	s.sync;
	try { ~base = Base.new; };
	s.sync;
	try { ~pyr = MIDIIn.findPort("Pyramid MIDI USB", "Pyramid MIDI USB");};
	s.sync;

	"/Global Functions/Load Global Functions.scd".loadRelative;
	"/System Scripts/Load System Scripts.scd".loadRelative;
	"/Songs/Load Songs.scd".loadRelative;
	"/Effects/Load Effects.scd".loadRelative;
	"/Instruments/Load Instruments.scd".loadRelative;

}.play;
)


s.makeWindow;

Server.killAll;

s.meter

~apc.setPage('main');
~prm.isLoaded;






