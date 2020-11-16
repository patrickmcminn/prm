/*
Tuesday, July 28th 2020
CV_PitchGate.sc
prm
*/

CV_PitchGate {

	var server, <isLoaded;
	var <freq, <gate;
	var <group;
	var <freqOut, <gateOut;

	*new { | freqOutBus = 0, gateOutBus = 1, relGroup, addAction = 'addToHead' |
		^super.new.prInit(freqOutBus, gateOutBus, relGroup, addAction);
	}

	prInit { | freqOutBus, gateOutBus, relGroup, addAction |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;

			group = Group.new(relGroup, addAction);

			freq = CV_Freq.new(freqOut, 0, group, 'addToHead');
			while({ try { freq.isLoaded } != true }, { 0.001.wait; });

			gate = CV_Gate.new(gateOut, group, \addToHead);
			while({ try { gate.isLoaded } != true }, { 0.001.wait; });

			freqOut = freqOutBus;
			gateOut = gateOutBus;

			server.sync;

			isLoaded = true;
		}
	}

	/////// public functions:

	free {
		freq.free; gate.free;
		isLoaded = false;
	}

	makeNoteGate { | freq = 220, gateValue = 0.5 |
		gate.makeGate(gateValue);
		freq.setFreq(freq);
	}

	releaseNoteGate { gate.releaseGate; }

	makeNoteTrig { | freq = 220, trigValue = 0.5, trigLength = 0.1 |
		gate.makeTrig(trigValue, trigLength);
		freq.setFreq(freq);
	}

	setFreqOut { | outBus | freqOut = outBus; freq.setOutBus(freqOut); }
	setGateOut { | outBus | gateOut = outBus; gate.setOutBus(gateOut); }

} 