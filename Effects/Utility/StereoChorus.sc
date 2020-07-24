/*
Monday May 4th 2020
StereoChorus.sc
prm
*/

StereoChorus : IM_Processor {

	var <isLoaded, server;
	var synth;

	var <depth, <rateMul, <timeMul;

	*new { | outBus, relGroup, addAction = \addToHead |
		^super.new(2, 1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDef;

			server.sync;

			depth = 0.5; rateMul = 0.5; timeMul = 0.03;

			synth = Synth(\prm_StereoChorus, [\inBus, inBus, \outBus, mixer.chanStereo], group, \addToHead);
			while({ try { synth == nil }}, { 0.001.wait; });

			server.sync;

			isLoaded = true;
		}
	}

	prAddSynthDef {

		SynthDef(\prm_StereoChorus, {
			| inBus, outBus, amp = 1, depth = 0.5, rateMul = 0.4, timeMul = 0.03 |
			var input, sig;
			var lfo11, lfo12, lfo21, lfo22, lfo31, lfo32, lfo41, lfo42;
			var delay1, delay2, delay3, delay4, delayLeft, delayRight;

			var depthHigh = depth * 0.01;
			var depthLow = depth * 0.0002;

			var rate11 = rateMul * 0.75;
			var rate12 = rateMul * 12.5;
			var rate21 = rateMul;
			var rate22 = rateMul * 13.75;
			var rate31 = rateMul * 0.454545;
			var rate32 = rateMul* 15;
			var rate41 = rateMul * 0.775;
			var rate42 = rateMul * 12.625;

			var time1 = timeMul * 0.8333333;
			var time2 = timeMul;
			var time3 = timeMul * 1.5;
			var time4 = timeMul * 0.9;


			input = In.ar(inBus, 2);

			lfo11 = SinOsc.kr(rate11).range(depthHigh.neg, depthHigh);
			lfo12 = SinOsc.kr(rate12).range(depthLow.neg, depthLow);
			lfo21 = SinOsc.kr(rate21).range(depthHigh.neg, depthHigh);
			lfo22 = SinOsc.ar(rate22).range(depthLow.neg, depthLow);
			lfo31 = SinOsc.kr(rate31).range(depthHigh.neg, depthHigh);
			lfo32 = SinOsc.kr(rate32).range(depthLow.neg, depthLow);
			lfo41 = SinOsc.kr(rate41).range(depthHigh.neg, depthHigh);
			lfo42 = SinOsc.kr(rate42).range(depthLow.neg, depthLow);

			delay1 = DelayL.ar(input, time1, (0.025 + lfo11 + lfo12));
			delay2 = DelayL.ar(input, time2, (0.03 + lfo21 + lfo22));
			delay3 = DelayL.ar(input, time3, (0.045 + lfo31 + lfo32));
			delay4 = DelayL.ar(input, time4, (0.027 + lfo41 + lfo42));

			delayLeft = Mix.ar([delay1 + delay2 + delay3])/2;
			delayRight = Mix.ar([delay2 + delay3 + delay4])/2;

			sig = Balance2.ar(delayLeft, delayRight);
			sig = sig * amp;

			Out.ar(outBus, sig);
		}).add;

	}

	//////// public functions:

	free {
		synth.free;
		this.freeProcessor;
		isLoaded = false;
	}

	setDepth { | d = 0.5 |
		depth = d;
		synth.set(\depth, depth);
	}

	setRateMul { | rate = 0.4 |
		rateMul = rate;
		synth.set(\rateMul, rateMul);
	}

	setTimeMul { | time = 0.03 |
		timeMul = time;
		synth.set(\timeMul, timeMul);
	}

}