SampleGrid_Voice : IM_Module {

	var server, <isLoaded;
	var <lfo;

	var <samplePath, <sampleVol, <playMode, <lowPassCutoff, <highPassCutoff;
	var <startPos, <endPos, <samplePan;
	var <attackTime, <decayTime, <sustainLevel, <releaseTime;
	var <rate;

	var <panLow, <panHigh, <grainDurLow, <grainDurHigh, <trigRate;

	var <buffer, bufferLeft, bufferRight, <isPlaying;

	var <monoOrStereo;

	var synth;

	*new { | outBus, relGroup = nil, addAction = 'addToHead' |
		^super.newNoSends(1, outBus, false, relGroup, addAction).prInit;
		//^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while( { try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDefs;
			this.prSetInitialParameters;
			/*
			lfo = LFO.new(1, relGroup: group, addAction: \addToHead);
			while({ try { lfo.isLoaded } != true }, { 0.001.wait; });
			*/

			buffer = Buffer.alloc(server, server.sampleRate);

			server.sync;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		sampleVol = -6;
		playMode = 'oneShot';
		lowPassCutoff = 20000; highPassCutoff = 20;
		startPos = 0; endPos = 1;
		attackTime = 0.01; decayTime = 0; sustainLevel = 1; releaseTime = 0.01;
		rate = 1; isPlaying = false;
		grainDurLow = 1; grainDurHigh = 2; trigRate = 3;
		panLow = -0.5; panHigh = 0.5;
	}

	prAddSynthDefs {
		/*
		three types, each w/ a mono/stereo pair
		-- sustaining -- loops through sample w/ ADSR envelope until gate = 0
		-- one shot -- plays start through for sustai time
		-- granular -- starts grains and wraps them from startPos to endPos
		*/

		SynthDef(\prm_SampleGrid_Voice_Sustaining_Stereo, {
			|
			outBus = 0, amp = 1, buffer, rate = 1, loop = 1,
			startPos = 0, endPos = 1,
			attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05, gate = 1
			lowPassCutoff = 20000, highPassCutoff = 0, pan = 0
			|
			var playHead, player, highPass, lowPass, env, sig;
			playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
				BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
			player = BufRd.ar(2, buffer, playHead, loop);
			highPass = HPF.ar(player, highPassCutoff.lag(0.3));
			lowPass = LPF.ar(highPass, lowPassCutoff);
			env = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4), gate, doneAction: 2);
			sig = lowPass * env;
			sig = sig * amp;
			sig = Balance2.ar(sig[0], sig[1], pan);
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_SampleGrid_Voice_Sustaining_Mono, {
			|
			outBus = 0, amp = 1, buffer, rate = 1, loop = 1,
			startPos = 0, endPos = 1,
			attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05, gate = 1
			lowPassCutoff = 20000, highPassCutoff = 0, pan = 0
			|
			var playHead, player, highPass, lowPass, env, sig;
			playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
				BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
			player = BufRd.ar(1, buffer, playHead, loop);
			highPass = HPF.ar(player, highPassCutoff.lag(0.3));
			lowPass = LPF.ar(highPass, lowPassCutoff);
			env = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4), gate, doneAction: 2);
			sig = lowPass * env;
			sig = sig * amp;
			sig = Pan2.ar(sig, pan);
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_SampleGrid_Voice_OneShot_Stereo, {
			|
			outBus = 0, amp = 1, buffer, rate = 1, loop = 0,
			startPos = 0, endPos = 1,
			attackTime = 0.05, releaseTime = 0.05,
			highPassCutoff = 0, lowPassCutoff = 20000, pan = 0, gate = 1
			|
			var playHead, player, highPass, lowPass, env, sig, sus;
			playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
				BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
			player = BufRd.ar(2, buffer, playHead, loop);
			highPass = HPF.ar(player, highPassCutoff.lag(0.3));
			lowPass = LPF.ar(highPass, lowPassCutoff);
			sus = ((BufSamples.ir(buffer) * endPos) - (BufSamples.ir(buffer) * startPos)) / SampleRate.ir;
			sus = sus - (attackTime + releaseTime);
			env = EnvGen.kr(Env.linen(attackTime, sus, releaseTime, 1, -4), gate, doneAction: 2);
			sig = lowPass * env;
			sig = sig * amp;
			sig = Balance2.ar(sig[0], sig[1], pan);
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_SampleGrid_Voice_OneShot_Mono, {
			|
			outBus = 0, amp = 1, buffer, rate = 1, loop = 0,
			startPos = 0, endPos = 1,
			attackTime = 0.05, releaseTime = 0.05,
			highPassCutoff = 0, lowPassCutoff = 20000, pan = 0, gate = 1
			|
			var playHead, player, highPass, lowPass, env, sig, sus;
			playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
				BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
			player = BufRd.ar(1, buffer, playHead, loop);
			highPass = HPF.ar(player, highPassCutoff.lag(0.3));
			lowPass = LPF.ar(highPass, lowPassCutoff);
			sus = ((BufSamples.ir(buffer) * endPos) - (BufSamples.ir(buffer) * startPos)) / SampleRate.ir;
			sus = sus - (attackTime + releaseTime);
			env = EnvGen.kr(Env.linen(attackTime, sus, releaseTime, 1, -4), gate, doneAction: 2);
			sig = lowPass * env;
			sig = sig * amp;
			sig = Pan2.ar(sig, pan);
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_SampleGrid_Voice_Granulator_Stereo, {
			|
			outBus = 0, bufferLeft, bufferRight, panLow = -1, panHigh = 1,
			grainDurLow = 1, grainDurHigh = 2, rateLow = 1, rateHigh = 1,
			startPos = 0.2, endPos = 0.6, env = -1, sync = 0, trigRate = 3,
			attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05, gate = 1,
			amp = 1, mix = 1, highPassCutoff = 0, lowPassCutoff = 20000, pan = 0
			|

			var input, dry, granStereoSum;
			var playhead, record, trigger, duration, position, envelope;
			var panner, rate, granulationLeft, granulationRight, granulation, lowPass, highPass, sig;

			trigger = SelectX.ar(sync, [Dust.ar(trigRate), Impulse.ar(trigRate)]);
			duration = TRand.ar(grainDurLow, grainDurHigh, trigger);
			position = TRand.ar(startPos, endPos, trigger);
			position = Wrap.ar(position, startPos, endPos);
			rate = TRand.ar(rateLow, rateHigh, trigger);
			panner = TRand.ar(panLow, panHigh, trigger);

			granulationLeft = GrainBuf.ar(2, trigger: trigger, dur: duration, sndbuf: bufferLeft,
				rate: rate, pos: position, pan: panner, envbufnum: env);
			granulationRight = GrainBuf.ar(2, trigger: trigger, dur: duration, sndbuf: bufferRight,
				rate: rate, pos: position, pan: panner, envbufnum: env);
			granulation = Mix.ar([granulationLeft, granulationRight]);

			highPass = HPF.ar(granulation, highPassCutoff.lag(0.3));
			lowPass = LPF.ar(highPass, lowPassCutoff);

			envelope = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime), gate, doneAction: 2);

			sig = lowPass * envelope;
			sig = sig * amp;
			sig = sig.softclip;
			sig = Balance2.ar(sig[0], sig[1], pan, 3.dbamp);
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_SampleGrid_Voice_Granulator_Mono, {
			|
			outBus = 0, buffer, panLow = -1, panHigh = 1,
			grainDurLow = 1, grainDurHigh = 2, rateLow = 1, rateHigh = 1,
			startPos = 0.2, endPos = 0.6, env = -1, sync = 0, trigRate = 3,
			attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05, gate = 1,
			amp = 1, mix = 1, highPassCutoff = 0, lowPassCutoff = 20000, pan = 0
			|

			var input, dry, granStereoSum;
			var playhead, record, trigger, duration, position, envelope;
			var panner, rate, granulationLeft, granulationRight, granulation, lowPass, highPass, sig;

			trigger = SelectX.ar(sync, [Dust.ar(trigRate), Impulse.ar(trigRate)]);
			duration = TRand.ar(grainDurLow, grainDurHigh, trigger);
			position = TRand.ar(startPos, endPos, trigger);
			position = Wrap.ar(position, startPos, endPos);
			rate = TRand.ar(rateLow, rateHigh, trigger);
			panner = TRand.ar(panLow, panHigh, trigger);

			granulation = GrainBuf.ar(2, trigger: trigger, dur: duration, sndbuf: buffer,
				rate: rate, pos: position, pan: panner, envbufnum: env);

			highPass = HPF.ar(granulation, highPassCutoff.lag(0.3));
			lowPass = LPF.ar(highPass, lowPassCutoff);

			envelope = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime), gate, doneAction: 2);

			sig = lowPass * env;
			sig = sig * amp;
			sig = sig.softclip;
			sig = Balance2.ar(sig[0], sig[1], pan, 3.dbamp);
			Out.ar(outBus, sig);
		}).add;
	}

	//////// public functions:
	free {
		if( isPlaying == true, { synth.free; });
		this.freeModule;
	}

	loadSample {
		if(isPlaying == true, { this.freeSample; });
		{
			buffer.free;
			server.sync;
			buffer = Buffer.loadDialog(server, 0, -1, { | buf |
				if(buf.numChannels == 1, { monoOrStereo = 'mono' }, { monoOrStereo = 'stereo' });
				this.prSetInitialParameters;
				if(monoOrStereo == 'stereo', {
					bufferLeft = Buffer.readChannel(server, buffer.path, 0, -1, 0);
					bufferRight = Buffer.readChannel(server, buffer.path, 0, -1, 1); });
			});
		}.fork(AppClock);
	}

	setPosGUI { | windowName = "sample" |
		{
			var window = Window.new(windowName, Rect(200, 300, 1040, 200));
			var view = SoundFileView.new(window, Rect(20, 20, 1000, 120));

			view.soundfile = buffer;
			view.read(0, buffer.numFrames);
			view.refresh;
			view.gridResolution(0.1);
			view.gridColor = Color.magenta;
			view.timeCursorColor = Color.white;
			view.setSelectionColor(0, Color.white);
			view.mouseUpAction = {
				var start = view.selections[0][0];
				var length = view.selections[0][1];
				var startPos = start/buffer.numFrames;
				var endPos = (start + length)/buffer.numFrames;
				this.setPos(startPos, endPos);
				[startPos, endPos].postln;
			};
			window.front;
		}.fork(AppClock);
	}

	setPos { | startPos = 0, endPos = 1 |
		this.setStartPos(startPos);
		this.setEndPos(endPos);
	}
	setStartPos { | pos  = 0 | startPos = pos; if( isPlaying == true, { synth.set(\startPos, pos); }); }
	setEndPos { | pos = 1 | endPos = pos; if( isPlaying == true, { synth.set(\endPos, pos); }); }

	playSample {
		if( playMode == 'sustaining', { this.playSampleSustaining; });
		if( playMode == 'oneShot', { this.playSampleOneShot; });
		if( playMode == 'granular', { this.playSampleGranular; });
	}

	releaseSample { synth.set(\gate, 0); isPlaying = false; }
	freeSample { synth.free; isPlaying = false; }

	setPlayMode { | mode = 'sustaining' | playMode = mode; }
	setSampleVol { | vol = -6 | sampleVol = vol; if(isPlaying==true, { synth.set(\amp, sampleVol.dbamp) }) }
	setSamplePan { | pan = 0 | samplePan = pan; if(isPlaying == true, { synth.set(\pan, samplePan) }) }

	setAttackTime { | time = 0.01 | attackTime = time; if( isPlaying == true, { synth.set(\attackTime, attackTime); });}
	setDecayTime { | time = 0.01 | decayTime = time; if( isPlaying == true, { synth.set(\decayTime, decayTime); });}
	setSustainLevel { | level = 1 | sustainLevel = level; if( isPlaying == true, { synth.set(\sustainLevel, sustainLevel)})}
	setReleaseTime { | time = 0.01 | releaseTime = time; if(isPlaying==true,{synth.set(\releaseTime,releaseTime)})}

	setLowPassCutoff { | cutoff=20000 | lowPassCutoff=cutoff;if(isPlaying==true,{synth.set(\lowPassCutoff,lowPassCutoff)})}
	setHighPassCutoff{ | cutoff=20 | highPassCutoff=cutoff;if(isPlaying==true,{synth.set(\highPassCutoff,highPassCutoff)})}

	setRate{ | r = 1 | rate = r; if(isPlaying==true, { synth.set(\rate, rate); }) }


	///// Granular section:
	setGrainPan { | panLow, panHigh | this.setGrainPanLow(panLow); this.setGrainPanHigh(panHigh); }
	setGrainPanLow { | pan = -0.5 | panLow = pan; if(isPlaying == true, { synth.set(\panLow, panLow); }) }
	setGrainPanHigh { | pan = 0.5 | panHigh = pan; if(isPlaying == true, { synth.set(\panHigh, panHigh); }) }

	setGrainDur { |durLow, durHigh| this.setGrainDurLow(durLow); this.setGrainDurHigh(durHigh)}
	setGrainDurLow { | dur = 1 | grainDurLow = dur; if(isPlaying == true, { synth.set(\grainDurLow, grainDurLow)})}
	setGrainDurHigh { | dur = 2 | grainDurHigh = dur; if(isPlaying == true, { synth.set(\grainDurHigh, grainDurHigh)})}

	setTrigRate { | rate | trigRate = rate; if(isPlaying == true, { synth.set(\trigRate, rate) }) }


	playSampleSustaining {
		if(monoOrStereo == 'stereo', {
			synth = Synth(\prm_SampleGrid_Voice_Sustaining_Stereo,
				[
					\outBus, mixer.chanStereo, \amp, sampleVol.dbamp, \buffer, buffer, \rate, rate,
					\startPos, startPos, \endPos, endPos, \attackTime, attackTime, \decayTime, decayTime,
					\sustainLevel, sustainLevel, \releaseTime, releaseTime,
					\highPassCutoff, highPassCutoff, \lowPassCutoff, lowPassCutoff, \pan, samplePan
				],
				group, \addToHead);
			isPlaying = true;
		},
		{
			synth = Synth(\prm_SampleGrid_Voice_Sustaining_Mono,
				[
					\outBus, mixer.chanStereo, \amp, sampleVol.dbamp, \buffer, buffer, \rate, rate,
					\startPos, startPos, \endPos, endPos, \attackTime, attackTime, \decayTime, decayTime,
					\sustainLevel, sustainLevel, \releaseTime, releaseTime,
					\highPassCutoff, highPassCutoff, \lowPassCutoff, lowPassCutoff, \pan, samplePan
				],
				group, \addToHead);
			isPlaying = true;
		});
	}


	playSampleOneShot {
		if(monoOrStereo == 'stereo', {
			synth = Synth(\prm_SampleGrid_Voice_OneShot_Stereo,
				[
					\outBus, mixer.chanStereo, \amp, sampleVol.dbamp, \buffer, buffer,
					\rate, rate, \startPos, startPos, \endPos, endPos,
					\attackTime, attackTime, \decayTime, decayTime,
					\releaseTime, releaseTime, \highPassCutoff, highPassCutoff, \lowPassCutoff, lowPassCutoff,
					\pan, samplePan
			], group, \addToHead);
		}, {
			synth = Synth(\prm_SampleGrid_Voice_OneShot_Mono,
				[
					\outBus, mixer.chanStereo, \amp, sampleVol.dbamp, \buffer, buffer,
					\rate, rate, \startPos, startPos, \endPos, endPos,
					\attackTime, attackTime, \decayTime, decayTime,
					\releaseTime, releaseTime, \highPassCutoff, highPassCutoff, \lowPassCutoff, lowPassCutoff,
					\pan, samplePan
			], group, \addToHead);
		});
	}

	playSampleGranular {
		if(monoOrStereo == 'stereo', {
			synth = Synth(\prm_SampleGrid_Voice_Granulator_Stereo,
				[
					\outBus, mixer.chanStereo, \bufferLeft, bufferLeft, \bufferRight, bufferRight, \panLow, panLow,
					\panHigh, panHigh, \grainDurLow, grainDurLow, \grainDurHigh, grainDurHigh,
					\rateLow, rate, \rateHigh, rate, \startPos, startPos, \endPos, endPos,
					\attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel,
					\releaseTime, releaseTime, \amp, sampleVol.dbamp, \highPassCutoff, highPassCutoff,
					\lowPassCutoff, lowPassCutoff, \pan, samplePan
			], group, \addToHead);
			isPlaying = true;
		}, {
			synth = Synth(\prm_SampleGrid_Voice_Granulator_Mono,
				[
					\outBus, mixer.chanStereo, \buffer, buffer, \panLow, panLow,
					\panHigh, panHigh, \grainDurLow, grainDurLow, \grainDurHigh, grainDurHigh,
					\rateLow, rate, \rateHigh, rate, \startPos, startPos, \endPos, endPos,
					\attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel,
					\releaseTime, releaseTime, \amp, sampleVol.dbamp, \highPassCutoff, highPassCutoff,
					\lowPassCutoff, lowPassCutoff, \pan, samplePan
			], group, \addToHead);
			isPlaying = true;
		});
	}
}