
/*
Tuesday, February 21st 2017
testStartup.scd
prm
startup script for testing code w/o an
audio interface
*/



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

(THE ABOVE IS NOW VERY OUTDATED)

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

	~digiMaster = 18;
	~digi1 = 20;
	~digi2 = 22;
	~digi3 = 24;
	~digi4 = 26;

	~digiTrackArray = [~digi1, ~digi2, ~digi3, ~digi4];

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
	// Pyramid:
	try { ~pyr = MIDIIn.findPort("Pyramid MIDI USB", "Pyramid MIDI USB");};
	try { ~pyrOut = MIDIOut.newByName("Pyramid MIDI USB", "Pyramid MIDI USB"); };

	// Digitone (via UMC1820) for THE ARM / perhaps other things?
	try { ~umc = MIDIIn.findPort("UMC1820", "UMC1820"); };
	try { ~umcOut = MIDIOut.newByName("UMC1820", "UMC1820"); };

	s.sync;

	"/Global Functions/Load Global Functions.scd".loadRelative;
	"/System Scripts/Load System Scripts.scd".loadRelative;
	"/Songs/Load Songs.scd".loadRelative;
	"/Effects/Load Effects.scd".loadRelative;
	"/Instruments/Load Instruments.scd".loadRelative;

	s.sync;

	~s = [~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus];

}.play;
)

s.makeWindow;
s.meter;
Server.killAll;

~apc.setPage('main');

