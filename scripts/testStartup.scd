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
//// Live: //////
////////////////

/*
Audio Interface:
1 - mic
2 - pickup

Modular:
Inputs:
1 - Moog
2 - Morphagene
3 - Plaits
4 - Noise

Outputs:
1 - Modular Send
8 - clock out
*/

r {

	//// Audio Inputs:
	~micIn = 0;
	~pickupIn = 1;


	~modInput = Array.fill(4, { | i | 10 + i });
	~modOutput = Array.fill(8, { | i | 12 + i });

	//// MIDI Devices:


	// M32:
	~moogDevice = "Pyramid MIDI USB";
	~moogPort = "Pyramid MIDI USB";

	//~prm = AudioSystem.new(2, ~micIn, ~pickupIn, ~modInput[0], ~modInput[1], ~modOutput[0]);
	~prm = AudioSystem.new(2, ~micIn, ~pickupIn, ~modInput[0], ~modInput[1], ~modInput[2], ~modInput[3],
		~modOutput[0]);

	while( { try { ~prm.isLoaded } != true; }, { 0.001.wait; });

	try { ~apc = APC40Mk2.new; };
	try { ~base = Base.new; };

	"/Global Functions/Load Global Functions.scd".loadRelative;
	"/System Scripts/Load System Scripts.scd".loadRelative;
	"/Songs/Load Songs.scd".loadRelative;
	"/Effects/Load Effects.scd".loadRelative;
	"/Instruments/Load Instruments.scd".loadRelative;

}.play;


//////////////////
//// Studio: ////
////////////////

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

	~prm = AudioSystem.new(2, ~micIn, ~pickupIn, ~modInput[0], ~modInput[1], ~modInput[2], ~modInput[3],
		~modOutput[0]);

	while( { try { ~prm.isLoaded } != true; }, { 0.001.wait; });

	try { ~apc = APC40Mk2.new; };
	try { ~base = Base.new; };
	try { ~pyr = MIDIIn.findPort("Pyramid MIDI USB", "Pyramid MIDI USB");}


	"/Global Functions/Load Global Functions.scd".loadRelative;
	"/System Scripts/Load System Scripts.scd".loadRelative;
	"/Songs/Load Songs.scd".loadRelative;
	"/Effects/Load Effects.scd".loadRelative;
	"/Instruments/Load Instruments.scd".loadRelative;


}.play;

s.makeWindow;

Server.killAll;
s.meter

~prm.subtractive.playNote(220);

~foundation.moog.moog.free

s.meter
~prm.subtractive.free;

