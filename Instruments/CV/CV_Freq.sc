/*
Tuesday, July 28th 2020
CV_Freq.sc
prm
*/

CV_Freq {

	var group;
	var <isLoaded;
	var <synth;
	var <frequency;

	*new {
		| outBus = 0, freq = 220, relGroup = nil, addAction = 'addToHead' |
    ^super.new.prInit(outBus, freq, relGroup, addAction);
  }

	prInit { | outBus, freq, relGroup, addAction |
		var server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			this.prAddSynthDef;
			server.sync;

			group = Group.new(relGroup, addAction);

			server.sync;

			frequency = freq;
			synth = Synth(\prm_cv_freq,
				[\outBus, outBus, \freq, freq, \gate, 1, \lag, 0],
				group, \addToHead);

			isLoaded = true;
		}
	}

	prAddSynthDef {
		SynthDef(\prm_cv_freq, {
      | outBus = 0, freq = 220, gate = 1, lag = 0 |
			var pitchCV, sig;
			pitchCV = ((freq/261.6255653006).log2)/10;
			sig = EnvGen.ar(Env.cutoff(0, 1.0), gate, doneAction: 2);
      sig = sig * pitchCV;
      sig = Lag2.ar(sig, lag);
      Out.ar(outBus, sig);
    }).add;
	}

	//////// public functions:

	free {
		group.free;
		synth.free;
		isLoaded = false;
	}

	setFreq { | freq = 220 | frequency = freq; synth.set(\freq, frequency); }

	setOutBus { | outBus | synth.set(\outBus, outBus); }

} 