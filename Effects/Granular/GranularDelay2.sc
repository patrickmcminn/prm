/*
Sunday, January 31st 2021
GranularDelay2.sc
prm

fixing some big 'ole problems with the first GranularDelay class
including:
- discontinuities
- lack of sends

also, now the granulator is in the feedback loop of the delay
oh happy times
*/


GranularDelay2 : IM_Processor {

	var <isLoaded, server;
	var synth;
	var rateChangeRoutine;
	var buffer, grainBufDict;

	var <mix, <grainEnvelope, <grainDurLow, <grainDurHigh;
	var <trigRate, <panLow, <panHigh;
	var <isFrozen, <posMod, <sync;
	var <rateLow, <rateHigh, <filterCutoff, <bufferLength;

	var <delayLevel, <delayTime, <feedback;
	var <filterType, <delayCutoff, <delayRQ;

	*new {
		| outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDef;
			this.prMakeGrainBuffers;
			server.sync;

			buffer = Buffer.alloc(server, server.sampleRate * 3, 1);

			synth = Synth(\prm_granularDelay2, [\inBus, inBus, \outBus, mixer.chanStereo(0), \buffer, buffer],
				group, \addToHead);
			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prAddSynthDef {
		SynthDef(\prm_granularDelay2, {
			|
			inBus = 0, outBus = 0, buffer, panLow = -1, panHigh = 1,
			grainDurLow = 0.01, grainDurHigh = 0.19, rateLow = 1, rateHigh = 1,
			posMod = 0.5, env = -1, sync = 0, trigRate = 25, 	freeze = 0,
			amp = 1, mix = 1, delayLevel = 1, cutoff = 20000,
			sampleDelay = 20000, minSampleDelay = 1000, sampleRand = 5000,

			delayTime = 1, maxDelayTime = 5, feedback = 0.3,
			filterType = 0, delayCutoff = 20000, rq = 1
			|

			var input, dry, granStereoSum, delaySum;
			var playhead, record, trigger, duration, position;
			var pan, rate, granulation, filter, sig;

			var playHead, playHeadMod, playHeadDelay, recHead;
			var maxGrainDur, durCtrl;

			var safetyHighPass, lowPass, highPass, bandPass, delayFilter, localIn, delay;

			input = In.ar(inBus, 2);

			///// delay section:

			localIn = LocalIn.ar(2);
			safetyHighPass = HPF.ar(localIn, 80);
			safetyHighPass = LeakDC.ar(safetyHighPass);
			lowPass = RLPF.ar(safetyHighPass, delayCutoff, rq);
			highPass = RHPF.ar(safetyHighPass, delayCutoff, rq);
			bandPass = BPF.ar(safetyHighPass, delayCutoff, rq);
			delayFilter = Select.ar(filterType, [safetyHighPass, lowPass, highPass, bandPass]);
			delayFilter = delayFilter * delayLevel;

			delaySum = input + delayFilter;
			dry = delaySum * (1-mix);
			granStereoSum = Mix.ar([delaySum[0], delaySum[1]]) * 0.5;
			granStereoSum = granStereoSum.softclip;

			trigger = SelectX.ar(sync, [Dust.ar(trigRate), Impulse.ar(trigRate)]);

			recHead = Phasor.ar(0, BufRateScale.kr(buffer), 0, BufFrames.kr(buffer));
			recHead = Select.ar(freeze, [recHead, DC.ar(0)]);
			record = BufWr.ar(granStereoSum, buffer, recHead, 1);

			playHeadMod = TRand.ar(posMod.neg, posMod, trigger);
			playHeadMod = playHeadMod * BufFrames.kr(buffer);
			//playHeadMod = LFNoise1.ar(100).bipolar(sampleRand);
			playHeadDelay = max(sampleDelay - playHeadMod, minSampleDelay);

			playHead = recHead - playHeadDelay;
			playHead  = playHead / BufFrames.kr(buffer);

			rate = TRand.ar(rateLow, rateHigh, trigger);


			maxGrainDur = (playHeadDelay / rate) / SampleRate.ir;

			duration = TRand.ar(grainDurLow, grainDurHigh, trigger);
			duration = min(duration, maxGrainDur);

			pan = TRand.ar(panLow, panHigh, trigger);

			granulation = GrainBuf.ar(2, trigger: trigger, dur: duration, sndbuf: buffer,
				rate: rate, pos: playHead, pan: pan, envbufnum: env);

			granulation = (granulation * mix) + dry;

			delay = DelayC.ar(input + (granulation * feedback), maxDelayTime, delayTime.lag2(0.1));
			LocalOut.ar(delay);

			filter = LPF.ar(granulation, cutoff);

			sig = filter * amp;
			sig = sig.softclip;
			Out.ar(outBus, sig);
		}).add;
	}

	prMakeGrainBuffers {
		{
			var grainEnvs;
			grainBufDict = IdentityDictionary.new;
			grainEnvs = (
				gabor:	Env.sine(1, 1),
				gabWide:	Env([0, 1, 1, 0], [1, 1, 1]),
				perc:	Env.perc(0.01, 0.99),
				revPerc:	Env.perc(0.99, 0.01),
				expodec:	Env.perc(0.01, 0.99, 1, 4),
				rexpodec:	Env.perc(0.99, 0.01, 1, 4)
			);
			server.sync;
			grainBufDict[\gabor] = Buffer.sendCollection(server, grainEnvs.at(\gabor).discretize, 1);
			grainBufDict[\gabWide] = Buffer.sendCollection(server, grainEnvs.at(\gabWide).discretize, 1);
			grainBufDict[\perc] = Buffer.sendCollection(server, grainEnvs.at(\perc).discretize, 1);
			grainBufDict[\revPerc] = Buffer.sendCollection(server, grainEnvs.at(\revPerc).discretize, 1);
			grainBufDict[\expodec] = Buffer.sendCollection(server, grainEnvs.at(\expodec).discretize, 1);
			grainBufDict[\rexpodec] = Buffer.sendCollection(server, grainEnvs.at(\rexpodec).discretize, 1);
			grainEnvs.do({ | env | env = nil; });
			grainEnvs = nil;
		}.fork;
	}

	prSetInitialParameters {
		mix = 1;
		grainDurLow = 0.01;
		grainDurHigh = 0.19;
		trigRate = 32;
		panLow = -1;
		panHigh = 1;
		grainEnvelope = 'hann';
		sync = 0;
		rateLow = 1;
		rateHigh = 1;
		posMod = 0.5;
		isFrozen = false;
		filterCutoff = 20000;
		bufferLength = 3;

		delayLevel = 0.75;
		delayTime = 1;
		feedback = 0.3;
		filterType = 'none';
		delayCutoff = 20000;
		delayRQ = 1;
	}


	//////// public functions:

	free {
		synth.free;
		grainBufDict.do({ | buf | buf.free; });
		this.freeProcessor;
	}

	setMix { | m = 1 |
		mix = m; synth.set(\mix, mix);
	}

	setGrainEnvelope { | env = 'gabor' |
		grainEnvelope = env;
		switch(env,
			{ 'hann' }, { synth.set(\env, -1) },
			{ 'gabor' }, { synth.set(\env, grainBufDict[\gabor]); },
			{ 'gabWide' }, { synth.set(\env, grainBufDict[\gabWide]); },
			{ 'perc' }, { synth.set(\env, grainBufDict[\perc]); },
			{ 'revPerc' }, { synth.set(\env, grainBufDict[\revPerc]); },
			{ 'expodec' }, { synth.set(\env, grainBufDict[\expodec]); },
			{ 'rexpodec' }, { synth.set(\env, grainBufDict[\rexpodec]); },
		);
	}

	setBufferLength { | length = 3 |
		{
			synth.set(\amp, 0);
			buffer.free;

			server.sync;

			buffer = Buffer.alloc(server, server.sampleRate * length, 1);
			synth.set(\buffer, buffer);

			server.sync;

			synth.set(\amp, 1);
		}.fork;
	}

	setGrainDur { | durLow = 0.01, durHigh = 0.19 |
		this.setGrainDurLow(durLow);
		this.setGrainDurHigh(durHigh);
	}
	setGrainDurLow { | dur = 0.01 |
		grainDurLow = dur;
		synth.set(\grainDurLow, grainDurLow);
	}
	setGrainDurHigh { | dur = 0.19 |
		grainDurHigh = dur;
		synth.set(\grainDurHigh, grainDurHigh);
	}

	setTrigRate { | rate = 32 |
		trigRate = rate;
		synth.set(\trigRate, trigRate)
	}

	setPan { | low = -1, high = 1 |
		this.setPanLow(low);
		this.setPanHigh(high);
	}
	setPanLow { | pan = -1 |
		panLow = pan;
		synth.set(\panLow, panLow);
	}
	setPanHigh { | pan = 1 |
		panHigh = pan;
		synth.set(\panHigh, panHigh);
	}

	setRate { | low = 1, high = 1 |
		this.setRateLow(low);
		this.setRateHigh(high);
	}
	setRateLow { | rate = 1 |
		rateLow = rate;
		synth.set(\rateLow, rateLow);
	}
	setRateHigh { | rate = 1 |
		rateHigh = rate;
		synth.set(\rateHigh, rateHigh);
	}

	setPosMod { | mod = 0.5 |
		posMod = mod; synth.set(\posMod, posMod);
	}

	setSync { | s = 0 |
		sync = s;
		synth.set(\sync, sync);
	}
	turnSyncOff { sync = 0; synth.set(\sync, sync); }
	turnSyncOn { sync = 1; synth.set(\sync, sync); }
	toggleSync { if( sync == 1, { this.turnSyncOff }, { this.turnSyncOn }); }

	setFilterCutoff { | cutoff = 20000 |
		filterCutoff = cutoff;
		synth.set(\cutoff, filterCutoff);
	}

	toggleFreeze {
		if( isFrozen == true, { this.unFreeze }, { this.freeze });
	}
	freeze { isFrozen = true; synth.set(\freeze, 1); }
	unFreeze { isFrozen = false; synth.set(\freeze, 0); }

	////// delays:
	setDelayLevel { | level |
		delayLevel = level;
		synth.set(\delayLevel, delayLevel);
	}

	setDelayTime { | delay = 1 |
    delayTime = delay;
    synth.set(\delayTime, delayTime); }
  setFeedback { | fb = 0.3 |
    feedback = fb;
    synth.set(\feedback, feedback); }

  setDelayFilterCutoff { | cutoff = 20000 |
		delayCutoff = cutoff;
		synth.set(\cutoff, cutoff);
	}
  setDelayFilterRQ { | rq = 1 |
		delayRQ = rq;
		synth.set(\rq, 1);
	}
  setDelayFilterType { | type = 'none' |
		filterType = type;
    switch(type,
      { 'none' }, { synth.set(\filterType, 0); },
      { 'lowPass' }, { synth.set(\filterType, 1); },
      { 'highPass' }, { synth.set(\filterType, 2); },
      { 'bandPass' }, { synth.set(\filterType, 3); }
    );
  }

	mapParameter {  | param, bus |
		synth.set(param, bus.asMap);
  }

}