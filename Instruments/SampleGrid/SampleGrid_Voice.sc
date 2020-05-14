SampleGrid_Voice : IM_Module {

	var server, <isLoaded;
	//var <lfo;

	var <samplePath, <sampleVol, <playMode, <lowPassCutoff, <highPassCutoff;
	var <>startPos, <endPos, <samplePan;
	var <attackTime, <decayTime, <sustainLevel, <releaseTime;
	var <rate;

	var <panLow, <panHigh, <grainDurLow, <grainDurHigh, <trigRate;

	var <buffer, <isPlaying;

	var <monoOrStereo;

	var synth;

	var oscFunc, granGroup, granSum, granTrigger, granBus;

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

			granGroup = Group.new(group, \addBefore);
			granBus = Bus.audio(server, 2);
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
				(BufSamples.ir(buffer)/2) * startPos, (BufSamples.ir(buffer)/2) * endPos);
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
				(BufSamples.ir(buffer)/2) * startPos, (BufSamples.ir(buffer)/2) * endPos);
			player = BufRd.ar(2, buffer, playHead, loop);
			highPass = HPF.ar(player, highPassCutoff.lag(0.3));
			lowPass = LPF.ar(highPass, lowPassCutoff);
			sus = (((BufSamples.ir(buffer)/2) * endPos) - ((BufSamples.ir(buffer)/2) * startPos)) / SampleRate.ir;
			sus = sus - (attackTime + releaseTime);
			sus = sus/rate;
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
			sus = sus/rate;
			env = EnvGen.kr(Env.linen(attackTime, sus, releaseTime, 1, -4), gate, doneAction: 2);
			sig = lowPass * env;
			sig = sig * amp;
			sig = Pan2.ar(sig, pan);
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_SampleGrid_GranularSum, {
			|
			inBus = 0, outBus = 0, amp = 1,
			attackTime = 0.01, decayTime = 0.01, sustainLevel = 1, releaseTime = 0.01, gate = 1,
			pan = 0
			|
			var input, envelope, sig;
			input = In.ar(inBus, 2);
			envelope = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime), gate, doneAction: 2);
			sig = input * envelope;
			sig = Balance2.ar(sig[0], sig[1], pan, 3.dbamp);
			sig = sig * amp;
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_SampleGrid_GranularTrigger, {
			| trigRate = 3 |
			var trigger;
			trigger = Dust.ar(trigRate);
			SendReply.ar(trigger, '/sampleGrid_Granular', 1);
		}).add;

		SynthDef(\prm_SampleGrid_Granular_Stereo, {
			|
			outBus = 0, buffer, amp = 1, rate = 1, dur = 1, startPos = 0, endPos = 1, pan = 0,
			attackTime = 0.01, releaseTime = 0.01, highPassCutoff = 20, lowPassCutoff = 20000,
			gate = 1
			|
			var playHead, player, highPass, lowPass, env, sig, sus;
			playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
				(BufSamples.ir(buffer)/2) * startPos, (BufSamples.ir(buffer)/2) * endPos);
			player = BufRd.ar(2, buffer, playHead);
			highPass = HPF.ar(player, highPassCutoff.lag(0.3));
			lowPass = LPF.ar(highPass, lowPassCutoff);
			sus = dur - (attackTime + releaseTime);
			//sus = sus/rate;
			env = EnvGen.kr(Env.linen(attackTime, sus, releaseTime, 1, -4), gate, doneAction: 2);
			sig = lowPass * env;
			sig = sig * amp;
			sig = Balance2.ar(sig[0], sig[1], pan, 3.dbamp);
			Out.ar(outBus, sig);
		}).add;

		SynthDef(\prm_SampleGrid_Granular_Mono, {
			|
			outBus = 0, buffer, amp = 1, rate = 1, dur = 1, startPos = 0, endPos = 1, pan = 0,
			attackTime = 0.01, releaseTime = 0.01, highPassCutoff = 20, lowPassCutoff = 20000,
			gate = 1
			|
			var playHead, player, highPass, lowPass, env, sig, sus;
			playHead = Phasor.ar(0, BufRateScale.kr(buffer) * rate,
				BufSamples.ir(buffer) * startPos, BufSamples.ir(buffer) * endPos);
			player = BufRd.ar(1, buffer, playHead);
			highPass = HPF.ar(player, highPassCutoff.lag(0.3));
			lowPass = LPF.ar(highPass, lowPassCutoff);
			sus = dur - (attackTime + releaseTime);
			//sus = sus/rate;
			env = EnvGen.kr(Env.linen(attackTime, sus, releaseTime, 1, -4), gate, doneAction: 2);
			sig = lowPass * env;
			sig = sig * amp;
			sig = Pan2.ar(sig, pan, 3.dbamp);
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
			});
			samplePath = buffer.path;
		}.fork(AppClock);
	}

	loadSampleByPath { | path |
		if(isPlaying == true, { this.freeSample; });
		{
			buffer.free;
			server.sync;
			//buffer = Buffer.read(server, path);
			buffer = Buffer.read(server, path, 0, -1, { | buf |
				if( buf.numChannels == 1, { monoOrStereo = 'mono' }, { monoOrStereo = 'stereo' });
				//this.prSetInitialParameters;
				samplePath = buffer.path;
			});
		}.fork;
	}

	setSamplePath { | path |
		samplePath = path;
		this.loadSampleByPath(samplePath);
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

	playSample { | vol = -3 |
		if( playMode == 'sustaining', { this.playSampleSustaining(vol); });
		if( playMode == 'oneShot', { this.playSampleOneShot(vol); });
		if( playMode == 'granular', { this.playSampleGranular(vol); });
	}

	releaseSample { if(playMode == 'granular',
		{ this.releaseSampleGranular; },
		{ synth.set(\gate, 0); isPlaying = false;});
	}

	freeSample { synth.free; isPlaying = false; }

	setPlayMode { | mode = 'sustaining' | this.releaseSample; playMode = mode; }
	setSampleVol { | vol = -6 | sampleVol = vol; mixer.setVol(vol); }
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
	setGrainPanLow { | pan = -0.5 | panLow = pan; if(isPlaying == true, { granGroup.set(\panLow, panLow); }) }
	setGrainPanHigh { | pan = 0.5 | panHigh = pan; if(isPlaying == true, { granGroup.set(\panHigh, panHigh); }) }

	setGrainDur { |durLow, durHigh| this.setGrainDurLow(durLow); this.setGrainDurHigh(durHigh)}
	setGrainDurLow { | dur = 1 | grainDurLow = dur; if(isPlaying == true, { granGroup.set(\grainDurLow, grainDurLow)})}
	setGrainDurHigh { | dur = 2 | grainDurHigh = dur; if(isPlaying == true, { granGroup.set(\grainDurHigh, grainDurHigh)})}

	setTrigRate { | rate | trigRate = rate; if(isPlaying == true, { granTrigger.set(\trigRate, rate) }) }


	playSampleSustaining { | vol = -3 |
		if(monoOrStereo == 'stereo', {
			synth = Synth(\prm_SampleGrid_Voice_Sustaining_Stereo,
				[
					\outBus, mixer.chanStereo, \amp, vol.dbamp, \buffer, buffer, \rate, rate,
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
					\outBus, mixer.chanStereo, \amp, vol.dbamp, \buffer, buffer, \rate, rate,
					\startPos, startPos, \endPos, endPos, \attackTime, attackTime, \decayTime, decayTime,
					\sustainLevel, sustainLevel, \releaseTime, releaseTime,
					\highPassCutoff, highPassCutoff, \lowPassCutoff, lowPassCutoff, \pan, samplePan
				],
				group, \addToHead);
			isPlaying = true;
		});
	}


	playSampleOneShot { | vol = -3 |
		if(monoOrStereo == 'stereo', {
			synth = Synth(\prm_SampleGrid_Voice_OneShot_Stereo,
				[
					\outBus, mixer.chanStereo, \amp, vol.dbamp, \buffer, buffer,
					\rate, rate, \startPos, startPos, \endPos, endPos,
					\attackTime, attackTime, \decayTime, decayTime,
					\releaseTime, releaseTime, \highPassCutoff, highPassCutoff, \lowPassCutoff, lowPassCutoff,
					\pan, samplePan
			], group, \addToHead);
		}, {
			synth = Synth(\prm_SampleGrid_Voice_OneShot_Mono,
				[
					\outBus, mixer.chanStereo, \amp, vol.dbamp, \buffer, buffer,
					\rate, rate, \startPos, startPos, \endPos, endPos,
					\attackTime, attackTime, \decayTime, decayTime,
					\releaseTime, releaseTime, \highPassCutoff, highPassCutoff, \lowPassCutoff, lowPassCutoff,
					\pan, samplePan
			], group, \addToHead);
		});
	}

	playSampleGranular { | vol = -3 |
		if(isPlaying == true, { this.releaseSampleGranular });
		// input for all synth grains:
		granSum = Synth(\prm_SampleGrid_GranularSum,
			[\inBus, granBus, \outBus, mixer.chanStereo, \amp, vol.dbamp,
				\attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel,
				\releaseTime, releaseTime, \pan, samplePan],
			group, \addToHead);

		// sends OSC triggers:
		granTrigger = Synth(\prm_SampleGrid_GranularTrigger, [\trigRate, trigRate], group, \addToHead);

		// creates OSC Def to take trigger and make synth grains:
		if(monoOrStereo == 'stereo', {
			oscFunc = OSCFunc.new({ | msg |
				var val = msg.at(3);
				if( val == 1, {
					Synth(\prm_SampleGrid_Granular_Stereo,
						[\buffer, buffer, \outBus, granBus, \rate, rate, \dur, rrand(grainDurLow, grainDurHigh),
							\startPos, startPos,\endPos, endPos, \pan, rrand(panLow, panHigh),
							\highPassCutoff, highPassCutoff, \lowPassCutoff, lowPassCutoff],
						granGroup, \addToHead);
				});
			}, '/sampleGrid_Granular');
			isPlaying = true;
		},
		{
			oscFunc = OSCFunc.new({ | msg |
				var val = msg.at(3);
				if( val == 1, {
					Synth(\prm_SampleGrid_Granular_Mono,
						[\buffer, buffer, \outBus, granBus, \rate, rate, \dur, rrand(grainDurLow, grainDurHigh),
							\startPos, startPos,\endPos, endPos, \pan, rrand(panLow, panHigh),
							\highPassCutoff, highPassCutoff, \lowPassCutoff, lowPassCutoff],
						granGroup, \addToHead);
				});
			}, '/sampleGrid_Granular');
			isPlaying = true;
		});
	}

	releaseSampleGranular {
		granGroup.set(\gate, 0);
		granSum.set(\gate, 0);
		granTrigger.free;
		oscFunc.free;
		isPlaying = false;
	}
}