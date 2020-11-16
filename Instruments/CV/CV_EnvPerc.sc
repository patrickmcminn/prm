/*
Monday, June 3rd 2019
CV_EnvPerc.sc
prm
*/

CV_EnvPerc {

	var <isLoaded, server;
	var <out, rG, action;

	var <attackTime, <releaseTime, <level, <curve;

	*new { | outBus, relGroup, addAction = 'addToHead' |
		^super.new.prInit(outBus, relGroup, addAction);
	}

	prInit {| outBus, relGroup, addAction |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			out = outBus;
			rG = relGroup;
			action = addAction;
			this.prAddSynthDef;

			server.sync;

			attackTime = 0.05;
			releaseTime = 0.5;
			level = 0.5;
			curve = -4;
			out = outBus;

			isLoaded = true;
		}
	}

	prAddSynthDef {
		SynthDef(\prm_CV_EnvPerc, {
			| outBus = 0, attackTime = 0.05, releaseTime = 0.5, envLevel = 0.5, envCurve = -4 |
			var env;
			env = EnvGen.ar(Env.perc(attackTime, releaseTime, envLevel, envCurve), 1, doneAction: 2);
			Out.ar(outBus, env);
		}).add;
	}

	/////// public functions:

	free {
		isLoaded = false;
	}

	trigger {
		var synth;
		if(isLoaded == true, { Synth(\prm_CV_EnvPerc,
			[\outBus, out, \attackTime, attackTime, \releaseTime, releaseTime,
				\envLevel, level, \envCurve, curve], rG, action); });
	}

	setOutBus { | outBus | out = outBus; }

	setAttackTime { | time = 0.05 | attackTime = time; }
	setReleaseTime { | time = 0.5 | releaseTime = time; }
	setLevel { | l = 0.5 | level = l; }
	setCurve { | c = -4 | curve = c; }
}