/*
Monday, June 3rd 2019
CV_EnvADSR.sc
prm
*/

CV_EnvADSR {

	var <isLoaded, server;
	var <out, rG, action;

	var env;

	var <isTriggered;

	var <attackTime, <decayTime, <sustainLevel, <releaseTime, <curve, <polarity, <peakLevel;

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
			decayTime = 1;
			sustainLevel = 0.5;
			releaseTime = 0.5;
			curve = -4;
			peakLevel = 0.5;
			polarity = 1;

			out = outBus;

			isTriggered = false;

			isLoaded = true;
		}
	}

	prAddSynthDef {
		SynthDef(\prm_cv_adsr, {
			| outBus, attackTime, decayTime, sustainLevel, releaseTime, curve, polarity, gate, peakLevel |
			var env;
			env = EnvGen.ar(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, peakLevel, curve),
				gate, doneAction: 2);
			env = env * polarity;
			Out.ar(outBus, env);
		}).add;
	}

	//////// public functions:

	free {
		env.free;
		isTriggered = false;
		isLoaded = false;
	}

	trigger {
		if( isLoaded == true, {
			if( isTriggered == true, { try { env.set(\gate, 0); }; });
			env = Synth(\prm_cv_adsr,
				[
					\outBus, out, \attackTime, attackTime, \decayTime, decayTime,
					\sustainLevel, sustainLevel, \releaseTime, releaseTime, \curve, curve,
					\peakLevel, peakLevel, \polarity, polarity, \gate, 1
				],
				rG, action);
			isTriggered = true;
		});
	}

	release { if(isLoaded == true, { env.set(\gate, 0); isTriggered = false; }); }

	setOutBus { | outBus = 0 | out = outBus; }

	setAttackTime { | attack = 0.05 | attackTime = attack; }
	setDecayTime { | decay = 0.3 | decayTime = decay; }
	setSustainLevel { | level = 0.5 | sustainLevel = level; }
	setReleaseTime { | release = 0.3 | releaseTime = release; }

	setPeakLevel { | level |
		peakLevel = level;
		if( isTriggered == true, { env.set(\peakLevel, peakLevel); });
	}
	setCurve { | c = -4 | curve = c; }
	setPolarity { | pol = 1 |
		polarity = pol;
		if( isTriggered == true, { env.set(\polarity, polarity); });
	}

} 