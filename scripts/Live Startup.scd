
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

	//~prm = AudioSystem.new(2, ~micIn, ~pickupIn, ~modInput[0], ~modInput[1], ~modOutput[0]);
	~prm = AudioSystem.new(2, ~micIn, ~pickupIn, ~modInput, ~modOutput);

	while( { try { ~prm.isLoaded } != true; }, { 0.001.wait; });

	s.sync;

	~s = [~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus];

	try { ~apc = APC40Mk2.new; };
	s.sync;
	try { ~base = Base.new; };
	s.sync;

	//// MIDI Devices:

	// M32:
	~moogDevice = "Pyramid MIDI USB";
	~moogPort = "Pyramid MIDI USB";

	// Pyramid :
	try { ~pyr = MIDIIn.findPort("Pyramid MIDI USB", "Pyramid MIDI USB");};
	try { ~pyrOut = MIDIOut.newByName("Pyramid MIDI USB", "Pyramid MIDI USB"); };

	//UMC 1820 MIDI:
	try { ~umc = MIDIIn.findPort("UMC1820", "UMC1820"); };
	try { ~umcOut = MIDIOut.newByName("UMC1820", "UMC1820"); };

	// Digitone:
	try { ~digi = MIDIIn.findPort("Elektron Digitone", "Elektron Digitone"); };
	try { ~digiOut = MIDIOut.newByName("Elektron Digitone", "Elektron Digitone") };

	// Analog Heat+FX:
	try { ~heat = MIDIIn.findPort("Elektron Analog Heat +FX", "Elektron Analog Heat +FX") };
	try { ~heatOut = MIDIOut.newByName("Elektron Analog Heat +FX", "Elektron Analog Heat +FX") };

	s.sync;

	try { "/Global Functions/Load Global Functions.scd".loadRelative; };
	try { "/System Scripts/Load System Scripts.scd".loadRelative;};
	try { "/Effects/Load Effects.scd".loadRelative; };
	try { "/Instruments/Load Instruments.scd".loadRelative; };
	try { "/Songs/Load Songs.scd".loadRelative; };

	s.sync;

}.play;
)

s.makeWindow;
s.meter;
Server.killAll;

~apc.setPage('main');


~s[0]

~prm.isLoaded

