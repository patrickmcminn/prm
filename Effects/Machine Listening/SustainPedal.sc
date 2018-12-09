/*

(
~sustainPedal = {| bufLength = 0.5, sustainedNotes = 16, release 15, amp = 0.7 |
	var cycler, synthTrigger;
	~sustainArray = Array.newClear(sustainedNotes);
	~sustainBufs = Buffer.allocConsecutive(sustainedNotes, s, s.sampleRate * bufLength, 1);
	~sustainBus = Bus.audio;
	 cycler = Routine({	// simple counter
		 var i = 0;
		 loop{
			 i = i + 1;
			 i.yield;
		 };
	 });
	 synthTrigger = { | bufName |	// takes counter values and spawns Synths w/ different buffers
		 var bufRec, sustainer;
		 ~sustainBufs.wrapAt(bufName).zero;	// zero the buffer
		 ~sustainArray.wrapAt(bufName).free;	// free the Synth in the Array index
		 bufRec = Synth(\recordBuf, [\in, ~ensembleBus, \bufName, ~sustainBufs.wrapAt(bufName)], 			~fx, addAction: \addToTail);
		 sustainer = TempoClock.sched(bufLength, {
			 ~sustainArray.wrapPut(bufName, Synth(\sustainerPlayer,
			 	[\out, ~fxBus, \bufName, ~sustainBufs.wrapAt(bufName), \release, release,
	 		 	\amp, amp], ~fx, addAction: \addToTail))});
	 };
	 p.remove;
	 p = OSCresponderNode(s.addr, '\tr', { synthTrigger.(cycler.value)} ).add;
	 ~onset = Synth(\onsetDetectorTrig, [\in, ~ensembleBus, \id, 0, \thresh, 0.005,
	 		\fastMul, 0.65], ~fx, addAction: \addToTail);
};

~sustainPedalFree = {
	p.remove;
	~onset.free;
	~sustainBufs.do(_.free);
	~sustainArray.size.do({ | i | ~sustainArray.at(i).free; });
	~sustainBus.free;
};
);
*/

/*
SynthDef(\sustainerPlayer, {
	| out, bufName, pan = 0, trigRate = 60, grainDur = 0.35, pos = 0.3
	attack = 0.01, release = 6.0, amp = 1 |
	var grainTrig, granulation, env, sig;
	grainTrig = Dust.ar(trigRate);
	granulation = GrainBuf.ar(2, trigger: grainTrig, dur: grainDur,
		sndbuf: bufName, rate: 1, pos: pos, pan: pan);
	env = EnvGen.ar(Env.perc(attack, release), 1);
	sig = granulation * env;
	sig = sig * amp;
	sig = Out.ar(out, sig);
}).add;

SynthDef(\onsetDetectorTrig, {
	|in = 0, id = 0
	trackFall = 0.2, slowLag = 0.2, fastLag = 0.01, fastMul = 0.5, thresh = 0.05, minDur = 0.1 |
	var input, detect, trigger, sig;
	input = In.ar(in);
	detect = Coyote.kr(input, trackFall, slowLag, fastLag, fastMul, thresh, minDur);
	sig = SendTrig.kr(detect, id, 1);
}).add;

*/