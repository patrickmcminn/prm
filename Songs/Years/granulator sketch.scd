~path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/Years/samples/Years guitar.aif";
~buf = Buffer.readChannel(s, ~path, channels: 1);

~buf.play

~prm.isLoaded

y = BufferGranulator.newMono(~prm.audioIn, ~buf, ~prm.reverb.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);

y.isLoaded;

y.playNote('test');


y.mixer.setSendVol(0, -6);

y.setGrainEnvelope('gabWide');
y.setFilterCutoff(20000);

Tremolo

y.releaseNote(\test);

x.free;
y.makeSequence(\test);

y.addKey(\test, \dur, Pseq([128], inf));
y.addKey(\test, \buffer, ~buf);

y.addKey(\test, \amp, 0.1);

y.mixer.setPreVol(-18);


y.mixer.setSendVol(0, -3);
s.meter


y.buf

y.playSequence(\test);

x.stopSequence(\test);


~buf


x = Synth(\prm_BufferGranulator_env, [\outBus, 0, \buffer, ~buf]);
x.free;


s.meter

{ GrainBuf.ar(2, Dust.ar(12), 1, ~buf, 1) }.play

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
			//position.poll;
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
	