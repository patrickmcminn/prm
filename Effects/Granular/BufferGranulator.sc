/*
Saturday, Septermber 20th 2014
BufferGranulator.sc
prm
*/

BufferGranulator : IM_Module {

	// only sequences work right now

	var <isLoaded;
	var <synth, grainBufDict;
	var server;
	var <sequencerDict, <sequencerClock;
	var <tempo;

	var <noteDict;

	var <isStereo;

	var <isPlaying;

	var <attackTime, <decayTime, <sustainLevel, <releaseTime;
	var <buf;
	var trigRate, grainDurLow, grainDurHigh, grainEnv;
	var	sync, rateLow, rateHigh, panLow;
	var panHigh, posLow, posHigh, cutoff, rq;

	*newMono { | outBus = 0, buffer = nil, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono(buffer);
	}

	*newStereo { | outBus = 0, buffer = nil, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo(buffer);
	}

	prInitStereo { | buffer |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;

			noteDict = IdentityDictionary.new;

			sequencerDict = IdentityDictionary.new;
			sequencerClock = TempoClock.new;
			tempo = 1;

			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prInitializeParameters(buffer);

			this.prAddSynthDef;
			server.sync;
			this.prMakeGrainBuffers;
			server.sync;
			//synth = Synth(\prm_BufferGranulator, [\outBus, mixer.chanStereo, \buffer, buffer], group, \addToHead);
			isStereo = true;
			isLoaded = true;
		}
	}

	prInitMono { | buffer |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;

			noteDict = IdentityDictionary.new;

			sequencerDict = IdentityDictionary.new;
			sequencerClock = TempoClock.new;
			tempo = 1;

			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prInitializeParameters(buffer);

			this.prAddSynthDef;
			server.sync;
			this.prMakeGrainBuffers;
			server.sync;
			//synth = Synth(\prm_BufferGranulator, [\outBus, mixer.chanStereo, \buffer, buffer], group, \addToHead);
			isStereo = false;
			isLoaded = true;
		}
	}

	prInitializeParameters { | buffer |
		buf = buffer;
		isPlaying = false;

		attackTime = 0.05;
		decayTime = 0;
		sustainLevel = 1;
		releaseTime = 0.05;
		trigRate = 32;
		grainDurLow = 0.5;
		grainDurHigh = 1.5;
		grainEnv = -1;
		sync = 0;
		rateLow = 1;
		rateHigh = 1;
		panLow = 0;
		panHigh = 0;
		posLow = 0;
		posHigh = 1;
		cutoff = 18000;
		rq = 1;
	}

	prAddSynthDef {
		SynthDef(\prm_BufferGranulator, {
			|
			outBus = 0, amp = 1, buffer, trigRate = 32, grainDurLow = 0.5, grainDurHigh = 1.5,
			grainEnv = -1, sync = 0, rateLow = 1, rateHigh = 1, panLow = 0, panHigh = 0,
			posLow = 0, posHigh = 1, cutoff = 18000, rq = 1
			|
			var syncTrigger, randTrigger, trigger, grainDur, rate, pan, position;
			var granulator, filter, sig;
			syncTrigger = Impulse.ar(trigRate);
			randTrigger = Dust.ar(trigRate);
			trigger = Select.ar(sync, [randTrigger, syncTrigger]);
			grainDur = TRand.ar(grainDurLow, grainDurHigh, trigger);
			rate = TRand.ar(rateLow, rateHigh, trigger);
			pan = TRand.ar(panLow, panHigh, trigger);
			position = TRand.ar(posLow, posHigh, trigger);
			position.poll;
			granulator = GrainBuf.ar(2, trigger, grainDur, buffer, rate, position, 2, pan, grainEnv, 512);
			filter = RLPF.ar(granulator, cutoff, rq);
			sig = filter * amp;
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_BufferGranulator_env, {
			|
			outBus = 0, amp = 1, buffer, trigRate = 32, grainDurLow = 0.5, grainDurHigh = 1.5,
			grainEnv = -1, sync = 0, rateLow = 1, rateHigh = 1, panLow = 0, panHigh = 0,
			posLow = 0, posHigh = 1, cutoff = 18000, rq = 1,
			attackTime = 0.05, decayTime = 0, sustainLevel = 1, releaseTime = 0.05, gate = 1
			|
			var syncTrigger, randTrigger, trigger, grainDur, rate, pan, position, envelope;
			var granulator, filter, sig;
			syncTrigger = Impulse.ar(trigRate);
			randTrigger = Dust.ar(trigRate);
			trigger = Select.ar(sync, [randTrigger, syncTrigger]);
			grainDur = TRand.ar(grainDurLow, grainDurHigh, trigger);
			rate = TRand.ar(rateLow, rateHigh, trigger);
			pan = TRand.ar(panLow, panHigh, trigger);
			position = TRand.ar(posLow, posHigh, trigger);
			granulator = GrainBuf.ar(2, trigger, grainDur, buffer, rate, position, 2, pan, grainEnv, 512);
			filter = RLPF.ar(granulator, cutoff, rq);
			envelope = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, curve: 4), gate, doneAction: 2);
			sig = filter * envelope;
			sig = sig * amp;
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

	//////// public functions:

	free {
		{
			grainBufDict.do({ | env | env.free; });
			server.sync;
			grainBufDict.do({ | env | env = nil; });
			grainBufDict = nil;
			//synth.free;
			//synth = nil;
			sequencerDict.do({ | seq | seq.free; });
			sequencerClock.free;
		}.fork;
		this.freeModule;
	}

	playNote { | name |
		try { noteDict[name].set(\gate, 0); };
		noteDict[name] = Synth(\prm_BufferGranulator_env,
			[\outBus, mixer.chanStereo(0), \buffer, buf, \trigRate, trigRate, \grainDurLow, grainDurLow,
				\grainDurHigh, grainDurHigh, \grainEnv, grainEnv, \sync, sync, \rateLow, rateLow,
				\rateHigh, rateHigh, \panLow, panLow, \panHigh, panHigh,
				\posLow, posLow, \posHigh, posHigh, \cutoff, cutoff, \rq, rq,
				\attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel,
				\releaseTime, releaseTime
			],
			group, \addToHead);
	}

	releaseNote { | name | noteDict[name].set(\gate, 0); }

	setGrainDur { | grainDurLow = 0.01, grainDurHigh = 0.19 |
		this.setGrainDurLow(grainDurLow);
		this.setGrainDurHigh(grainDurHigh);
	}
	setGrainDurLow { | grainDurLow = 0.01 | noteDict.do({ | synth | synth.set(\grainDurLow, grainDurLow); }); }
	setGrainDurHigh { | grainDurHigh = 0.19 | noteDict.do({ | synth | synth.set(\grainDurHigh, grainDurHigh); }); }
	setTrigRate { | trigRate = 32 | noteDict.do({ | synth | synth.set(\trigRate, trigRate) }); }
	setPan { | panLow = -1, panHigh = 1 |
		this.setPanLow(panLow);
		this.setPanHigh(panHigh);
	}
	setPanLow { | panLow = -1 | noteDict.do({ | synth | synth.set(\panLow, panLow); }); }
	setPanHigh { | panHigh = 1 | noteDict.do({ | synth | synth.set(\panHigh, panHigh); }); }
	setRate { | rateLow = 1, rateHigh = 1 |
		this.setRateLow(rateLow);
		this.setRateHigh(rateHigh);
	}
	setRateLow { | rateLow = 1 | noteDict.do({ | synth | synth.set(\rateLow, rateLow); }); }
	setRateHigh { | rateHigh = 1 | noteDict.do({ | synth | synth.set(\rateHigh, rateHigh); }); }
	setPos { | posLow = 0.2, posHigh = 0.6 |
		this.setPosLow(posLow);
		this.setPosHigh(posHigh);
	}
	setPosLow { | posLow = 0.2 | noteDict.do({ | synth | synth.set(\posLow, posLow); }); }
	setPosHigh { | posHigh = 0.6 | noteDict.do({ | synth | synth.set(\posHigh, posHigh); }); }
	setSync { | sync = 0 |
		noteDict.do({ | synth | synth.set(\sync, sync); });
	}

	setGrainEnvelope { | env = 'gabor' |
		switch(env,
			{ 'hann' }, { noteDict.do({ | synth | synth.set(\env, -1) }); },
			{ 'gabor' }, { noteDict.do({ | synth | synth.set(\env, grainBufDict[\gabor]); }); },
			{ 'gabWide' }, { noteDict.do({ | synth | synth.set(\env, grainBufDict[\gabWide]);  }); },
			{ 'perc' }, { noteDict.do({ | synth | synth.set(\env, grainBufDict[\perc]); }); },
			{ 'revPerc' }, { noteDict.do({ | synth | synth.set(\env, grainBufDict[\revPerc]); }); },
			{ 'expodec' }, { noteDict.do({ | synth | synth.set(\env, grainBufDict[\expodec]); }); },
			{ 'rexpodec' }, { noteDict.do({ | synth | synth.set(\env, grainBufDict[\rexpodec]); }); },
		);
	}

	setFilterCutoff { | cutoff = 20000 |
		noteDict.do({ | synth | synth.set(\cutoff, cutoff); });
	}

	setCrossfade { | crossfade = 1 |
		noteDict.do({ | synth | synth.set(\mix, crossfade); });
	}

	setAttackTime { | attack = 0.05 |
		attackTime = attack;
		noteDict.do({ | synth | synth.set(\attackTime, attackTime); });
	}
	setDecayTime { | decay = 0.05 |
		decayTime = decay;
		noteDict.do({ | synth | synth.set(\decayTime, decayTime); });
	}
	setSustainLevel { | level = 1 |
		sustainLevel = level;
		noteDict.do({ | synth | synth.set(\sustainLevel, sustainLevel); });
	}
	setReleaseTime { | release = 0.05 |
		releaseTime = release;
		noteDict.do({ | synth | synth.set(\releaseTime, releaseTime); });
	}



	////// Sequencing:

	makeSequence { | name, type = 'sustaining' |
		fork {
			sequencerDict[name] = IM_PatternSeq.new(name, group, \addToHead);
			sequencerDict[name].stop;
			server.sync;
			sequencerDict[name].addKey(\instrument, \prm_BufferGranulator_env);
			sequencerDict[name].addKey(\outBus, mixer.chanStereo(0));
			sequencerDict[name].addKey(\attackTime, Pfunc({ attackTime }));
			sequencerDict[name].addKey(\decayTime, Pfunc({ decayTime }));
			sequencerDict[name].addKey(\sustainLevel, Pfunc({ sustainLevel }));
			sequencerDict[name].addKey(\releaseTime, Pfunc({ releaseTime }));
			sequencerDict[name].addKey(\amp, 1);
			sequencerDict[name].addKey(\freq, 1);
			sequencerDict[name].addKey(\posLow, 0.3);
			sequencerDict[name].addKey(\posHigh, 0.6);
			sequencerDict[name].addKey(\trigRate, 30);
		}
	}

	addKey {  | name, key, action |
		sequencerDict[name].addKey(key, action);
	}

	playSequence { | name, clock = 'internal', quant = 'nil' |
		var playClock;
		if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
		sequencerDict[name].play(playClock);
	}

	resetSequence { | name | sequencerDict[name].reset; }
	stopSequence { | name | sequencerDict[name].stop; }
	pauseSequence { | name | sequencerDict[name].pause }
	resumeSequence { | name | sequencerDict[name].resume; }
	isSequencePlaying { | name | ^sequencerDict[name].isPlaying }
	setSequenceQuant { | name, quant = 0 | sequencerDict[name].setQuant(quant) }

	setSequencerClockTempo { | bpm = 60 |
		var bps = bpm/60;
		tempo = bps;
		sequencerClock.tempo = tempo;
	}

}