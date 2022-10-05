/*
Monday, October 3rd 2022
Chesapeake.sc
prm
*/

Chesapeake : IM_Module {

	var <isLoaded, server;
	var <sequencer;
	var <tide;
	var <midiDict, <midiEnabled;
	var data;

	*new {
		|
		outBus, seq,
		send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead'
		|
		^super.new(9, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(seq);
	}

	prInit { | seq |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			sequencer = try { seq.uid };
			midiDict = IdentityDictionary.new;
			midiEnabled = false;

			tide = Chesa_Tide.new(mixer.chanStereo(0), group, \addToHead);
			while({ try { tide.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}

	}

	prSetInitialParameters {
		//// tide:
		mixer.setPreVol(0, -12);

	}


} 