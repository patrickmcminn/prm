/*
Thursdsay, August 4th 2022
Octave_OC2.sc
prm
*/

Octave_OC2 : IM_Processor {

	var <isLoaded, server;
	var synth;
	var <inFilterCutoff, <dryVol, <oct1Vol, <oct2Vol;
	*newStereo {  | outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
		^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo;
	}

	prInitStereo {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDefs;

			server.sync;

			synth = Synth(\prm_oc2_ff_stereo,
				[\inBus, inBus, \outBus, mixer.chanStereo(0)], group, \addToHead);

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		inFilterCutoff = 600;
		dryVol = -3.dbamp;
		oct1Vol = -6.dbamp;
		oct2Vol = -12.dbamp;

	}

	prAddSynthDefs {
		SynthDef(\prm_oc2_ff_mono, {
			| inBus, outBus, dryAmp = 0.5, oct1Amp = 1, oct2Amp = 1, amp = 1
			inFilterCutoff = 500 |
			var input, proc, octave1, octave2, sig;
			input = In.ar(inBus);
			proc = LeakDC.ar(input, 0.999);
			proc = LPF.ar(proc, inFilterCutoff);

			octave1 = ToggleFF.ar(proc);
			octave1 = octave1 * proc;
			octave1 = octave1 * oct1Amp;

			octave2 = ToggleFF.ar(octave1);
			octave2 = octave2 * proc;
			octave2 = octave2 * oct2Amp;

			sig = Mix.new([(input * dryAmp), octave1, octave2]);

			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_oc2_ff_stereo, {
			| inBus, outBus, dryAmp = 0.5, oct1Amp = 1, oct2Amp = 1, amp = 1
			inFilterCutoff = 500 |
			var input, proc, octave1, octave2, sig;
			input = In.ar(inBus, 2);
			proc = LeakDC.ar(input, 0.999);
			proc = LPF.ar(proc, inFilterCutoff);

			octave1 = ToggleFF.ar(proc);
			octave1 = octave1 * proc;
			octave1 = octave1 * oct1Amp;

			octave2 = ToggleFF.ar(octave1);
			octave2 = octave2 * proc;
			octave2 = octave2 * oct2Amp;

			sig = Mix.new([(input * dryAmp), octave1, octave2]);

			Out.ar(outBus, sig);
		}).add;
	}

	////// public:

	free {
		synth.free;
		this.freeProcessor;
	}

	setInFilterCutoff { | cutoff = 600 |
		inFilterCutoff = cutoff;
		synth.set(\inFilterCutoff);
	}
	setDryVol { | vol = -3 |
		dryVol = vol;
		synth.set(\dryVol, dryVol.dbamp);
	}
	setOct1Vol { | vol = -6 |
		oct1Vol = vol;
		synth.set(\oct1Amp, oct1Vol.dbamp);
	}
	setOct2Vol { | vol = -6 |
		oct2Vol = vol;
		synth.set(\oct2Amp, oct2Vol.dbamp);
	}
}


