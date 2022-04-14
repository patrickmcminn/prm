/*
Monday, April 4th 2022
Years_FreezeTpts.sc
prm
*/

Years_FreezeTpts : IM_Module {

	var <isLoaded, server;
	var <freeze;
	var <eq, <isPlaying;

	*new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			freeze = GrainFreeze2.newMono(eq.inBus, relGroup: group, addAction: \addToHead);
			while({ try { freeze.isLoaded } != true }, { 0.001.wait; });

			server.sync;
			this.prSetInitialParameters;
			isLoaded = true;
		}
	}

	prSetInitialParameters {
		isPlaying = false;

		freeze.mixer.setPreVol(-15);
		eq.setHighPassCutoff(300);
		eq.setHighGain(-1.5);
		eq.setPeak1Freq(400);
		eq.setPeak1Gain(-3);

		freeze.setAttackTime(3);
		freeze.setReleaseTime(9);
	}

	//////// public functions:

	free {
		freeze.free;
		eq.free;
		this.freeModule;
	}

	//////// convenience functions:

	inBus { ^freeze.inBus }

	recordBuffer { freeze.recordBuffer }

	toggleCluster { if( isPlaying == false, { this.playCluster }, { this.releaseCluster }); }

	playCluster {
		var notes = [-7, -5, -3, -2, 0, 2, 4];
		7.do({ | i | freeze.playNote(i.asSymbol, notes[i], -12); });
		isPlaying = true;
	}

	releaseCluster {
		7.do({ | i | freeze.releaseNote(i.asSymbol); });
		isPlaying = false;
	}



} 