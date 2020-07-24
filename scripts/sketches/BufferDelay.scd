

SynthDef(\prm_bufDelay_mono, {
	|
	inBus, outBus, amp, buffer, mix = 0.5,
	delayTime = 0.5, feedback = 0.5
	|
	var input, localIn, source;
	var bufWrite, bufRead;
	var playHead, recordHead;
	var sig;

	input = In.ar(inBus, 1);
	localIn = LocalIn.ar(1);
	source = input + (localIn * feedback);

	recordHead = Phasor.ar(0.0, BufRateScale.kr(buffer), 0.0, BufFrames.kr(buffer));
	playHead = recordHead - (delayTime.lag2(0.1) *SampleRate.ir);

	bufWrite = BufWr.ar(source, buffer, recordHead, 1.0);
	bufRead = BufRd.ar(1, buffer, playHead, 1.0, 2);

	LocalOut.ar(bufRead);

	sig = (localIn * mix) + (input * (1-mix));
	sig = sig * amp;
	Out.ar(outBus, sig);
}).add;


~buf = Buffer.alloc(s, s.sampleRate, 1);
~bus = Bus.audio(s, 1);

~del = Synth(\prm_bufDelay_mono, [\inBus, ~bus, \outBus, 0, \amp, 1, \buffer, ~buf]);

~source = { Out.ar(~bus, Impulse.ar(1)) }.play

~source.free;


~source = { Out.ar(~bus, SinOsc.ar(220) * EnvGen.kr(Env.perc(0.01, 0.2, 1), Impulse.kr(1)) ) }.play
Dust
~del.set(\delayTime, 0.22);

~source.free;
x = { SinOsc.ar(220) * EnvGen.kr(

Trig
~modBus = Bus.control(s, 1);

~do = { Out.kr(~modBus, SinOsc.kr(1).range(0.1, 0.7)) }.play;
~do.free;

~del.set(\feedback, 0.95);

~del.set(\delayTime, ~modBus.asMap);

	SynthDef(\prm_simpleDelayMono, {
      |
      inBus = 0, outBus = 0, amp = 1, delayTime = 0.3, maxDelayTime = 5, feedback = 0.2,
      filterType = 0, cutoff = 20000, rq = 1, mix = 0.5
      |
      var input, lowPass, highPass, bandPass, filter, localIn, delay, sig;
      input = In.ar(inBus);
      localIn = LocalIn.ar(1);

      lowPass = RLPF.ar(localIn, cutoff, rq);
      highPass = RHPF.ar(localIn, cutoff, rq);
      bandPass = BPF.ar(localIn, cutoff, rq);
      filter = Select.ar(filterType, [localIn, lowPass, highPass, bandPass]);

      delay = DelayC.ar(input + (filter * feedback), maxDelayTime, delayTime.lag2(0.1));
      LocalOut.ar(delay);

      sig = (filter * mix) + (input * (1-mix));
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;