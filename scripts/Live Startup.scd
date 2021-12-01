
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
(
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


~pyr


~light.bass.playNote(220);
~light.bass.releaseNote(220);

