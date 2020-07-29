/*
Thursday, February 20th 2020
CV_Gate.sc
prm

eventually:
- non-sustaining gates that are triggered w/ a set length
- sustaining gates that are sustained until told to do otherwise
*/

CV_Gate {

	var <isLoaded, server;

	var out, rG, action;
	var gate;
	var <gateIsHigh;

	*new { | outBus, relGroup = nil, addAction = 'addToHead' |
		^super.new.prInit(outBus, relGroup, addAction);
	}

	prInit { | outBus, relGroup, addAction |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			out = outBus;
			rG = relGroup;
			gateIsHigh = false;
			action = addAction;
			this.prAddSynthDefs;

			server.sync;

			isLoaded = true;
		}
	}

	prAddSynthDefs {

		SynthDef(\prm_gateTrig, {
			| outBus = 0, gateVal = 0.5, gateLength = 0.1 |
			var val, env, sig;
			val = DC.ar(gateVal);
			env = EnvGen.kr(Env.linen(0, gateLength, 0), 1.0, doneAction: 2);
			sig = val * env;
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_gateGate, {
			| outBus = 0, gateVal = 0.5, gate = 1 |
			var sig;
			sig = EnvGen.ar(Env.cutoff(0, gateVal), gate, doneAction: 2);
			Out.ar(outBus, sig);
		}).add;
	}

	////// public functions:

	free {
		isLoaded = false;
	}

	makeTrig { | value = 0.5, length = 0.1 |
		var synth;
		synth = Synth(\prm_gateTrig, [\outBus, out, \gateVal, value, \gateLength, length],
			rG, action);
	}

	makeGate { | value = 0.5 |
		gateIsHigh = true;
		gate = Synth(\prm_gateGate, [\outBus, out, \gateVal, value], rG, action);
	}

	releaseGate {
		gateIsHigh = false;
		gate.set(\gate, 0);
	}

	setOutBus { | outBus | out = outBus }

}