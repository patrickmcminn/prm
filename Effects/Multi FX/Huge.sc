/*
Tuesday, March 17th 2020
Huge.sc
prm

based on old massive SynthDef from 2013
wow, that was a long time ago
*/

Huge : IM_Module {

	var <isLoaded, server;
	var <subMixer;
	var <eq, <endDelay;

	var subOsc, <multiShift, <granulator;

	var <reverb, <distortion, <lowPass;

	var <freezer;
	var <input, <initShift, <pitchSplitter, <initSplitter, <splitter;


	/*
	-- initial pitch shift
	-- input freeze
	-- sub oscillator
	-- pitch shifting section
	-- filter distortion section (goes into pitch shifter)
	-- granulator section
	-- final delay section
	*/

	*new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			mixer.mute;

			this.prAddSynthDefs;

			eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			endDelay = SimpleDelay.newStereo(eq.inBus, 0.4, 0.35, 5, relGroup: group, addAction: \addToHead);
			while({ try { endDelay.isLoaded } != true }, { 0.001.wait; });

			subMixer = IM_Mixer.new(4, endDelay.inBus, relGroup: group, addAction: \addToHead);
			while({ try { subMixer.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			granulator = GranularDelay.new(subMixer.chanStereo(3), group, \addToHead);
			while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

			multiShift = IM_MultiShift.newStereo(subMixer.chanStereo(2), [7, 12, -5, 7], 0,
				group, \addToHead);
			while({ try { multiShift.isLoaded } != true }, { 0.001.wait; });

			splitter = Splitter.newStereo(2, [subMixer.chanStereo(1), multiShift.inBus, ],
				false, group, \addToHead);
			while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

			lowPass = LowPassFilter.newStereo(splitter.inBus, relGroup: group, addAction: \addToHead);
			while({ try { lowPass.isLoaded } != true }, { 0.001.wait; });

			distortion = Decapitator.newStereo(lowPass.inBus, granulator.inBus, nil, nil, nil,
				relGroup: group, addAction: \addToHead);
			while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

			reverb = LittlePlate.newStereo(distortion.inBus, relGroup: group, addAction: \addToHead);
			while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

			initShift = PitchShifter.newMono(reverb.inBus, -12,
				relGroup: group, addAction: \addToHead);
			while({ try { initShift.isLoaded } != true }, { 0.001.wait; });

			pitchSplitter = Splitter.newStereo(2, [reverb.inBus, initShift.inBus], false,
				group, \addToHead);
			while({ try { pitchSplitter.isLoaded } != true }, { 0.001.wait; });

			freezer = GrainFreeze2.newStereo(pitchSplitter.inBus,
				relGroup: group, addAction: \addToHead);
			while({ try { freezer.isLoaded } != true }, { 0.001.wait; });

			initSplitter = Splitter.newStereo(2, [pitchSplitter.inBus, freezer.inBus],
				false, group, \addToHead);
			while({ try { initSplitter.isLoaded } != true }, { 0.001.wait; });

			input = IM_Mixer_1Ch.new(initSplitter.inBus, relGroup: group, addAction: \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			subOsc = Synth(\prm_huge_subOsc, [\inBus, input.inBus, \outBus, subMixer.chanStereo(0)],
				input.group, \addAfter);
			//while({ try { subOsc.notNil }}, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prAddSynthDefs {
		SynthDef(\prm_huge_subOsc, {
			| inBus, outBus, amp = 1, cutoffRatio = 2, width = 0.5, ampScalar = 6.5 |
			var input, freq, hasFreq, amplitude;
			var osc, filter, sig;
			var subFreq;
			input = In.ar(inBus);
			#freq, hasFreq = Tartini.kr(input);
			subFreq = freq/4;
			amplitude = Amplitude.ar(input, 0.01, 0.01);
			amplitude = amplitude * ampScalar;
			osc = VarSaw.ar(subFreq, 0.0, width);
			//filter = LPF.ar(osc, ((subFreq)*cutoffRatio).lag2(0.05));
			filter = LPF.ar(osc, 400);
			sig = filter * amplitude;
			sig = Pan2.ar(sig, 0);
			sig = sig * amp;
			Out.ar(outBus, sig);
		}).add;
	}

	prSetInitialParameters {

		4.do({ | i | subMixer.setPreVol(i, -6); });
		subMixer.setVol(0, -3);
		subMixer.setVol(1, -3);
		subMixer.setVol(2, -9);
		subMixer.setVol(3, -15);

		distortion.mixer.setSendPre;
		distortion.mixer.setSendVol(0, 0);

		lowPass.setCutoff(2300);

		reverb.loadPreset(\huge);
		distortion.loadPreset(\huge);

		eq.setPeak1Freq(300);
		eq.setPeak1Gain(-3);
		eq.setLowFreq(110);
		eq.setLowGain(3);

		granulator.granulator.setCrossfade(1);
		granulator.delay.setMix(0);
		granulator.setRate(2, 2);
		granulator.setSync(1);
		granulator.setGrainDur(0.05, 0.05);
		granulator.setTrigRate(8);
		granulator.setGrainEnvelope(\perc);

		mixer.unMute;

	}

	//////// public functions:

	inBus { ^input.inBus }

	free {
		subMixer.free;
		eq.free; endDelay.free;

		subOsc.free; multiShift.free; granulator;

		reverb.free; distortion.free; lowPass.free;

		freezer.free; input.free;
		initShift.free; pitchSplitter.free;
		initSplitter.free; splitter.free;

		this.freeModule;
	}

	freeze {
		freezer.releaseNote(\huge);
		freezer.recordBuffer;
		freezer.playNote(\huge);
	}

	releaseFreeze {
		freezer.releaseNote(\huge);
	}
}

/*
SynthDef(\huge, {
|
in, out, amp = 0.5, dist = 1000,
ampScale = 2, subAmp = 0.6,
verb1Mix = 0.92, verb1Room = 0.95, verbMix = 0.6, verbRoom = 0.7, shiftAmp = 1,
bufLength = 1, modFreq = 0.3, trigLo = 5, trigHi = 12, grainDur = 0.05, rateLo = 1, rateHi = 150, grainAmp = 0.07,
cutoffLo = 500, cutoffHi = 7000, cutoffChangeRate = 16, res = 1,
maxDelay = 4, delayTime = 0.4, decayTime = 4
bassBoost = 4
|

var input, shift, freq, hasFreq, amplitude;
var sine, verb1, distortion, verb2;
var buffer, playhead, record, rate, trigMod, trigger, panner, granulation;
var shiftArray, shifter1, shifter2, shifter3, shift1, shift2, shift3, shiftSum;
var cutoffShift, filter, sum, delayLag, delay, eq, sig;


//input:
input = In.ar(in);
shift = PitchShift.ar(input, 0.2, 0.5);

// analysis:
# freq, hasFreq = Pitch.kr(input, ampThreshold: 0.02, median: 7);
amplitude = Amplitude.ar(input, 0.01, 0.07);

// sub Oscillator
sine = SinOsc.ar(freq/4) * amplitude;
sine = sine * subAmp;
sine = sine ! 2;

// distortion chain
verb1 = FreeVerb.ar(shift + (input * 0.5), verb1Mix, verb1Room, 0.9);
distortion = (verb1 * dist).distort;
distortion = distortion * amplitude;
verb2 = FreeVerb.ar(distortion, verbMix, verbRoom, 1);
cutoffShift = TRand.ar(cutoffLo, cutoffHi, Dust.ar(cutoffChangeRate));
filter = RLPF.ar(verb2, cutoffShift, res);
filter = filter ! 2;


// granulation section:
buffer = LocalBuf(s.sampleRate * bufLength, 1);
playhead = Phasor.ar(0, BufRateScale.kr(buffer), 0, BufFrames.kr(buffer));
record = BufWr.ar(distortion, buffer, playhead, 1);
trigMod = SinOsc.ar(modFreq).range(trigLo, trigHi);
trigger = Impulse.ar(trigMod);
panner = TRand.ar(-1, 1, trigger);
rate = TRand.ar(rateLo, rateHi, trigger);
granulation = GrainBuf.ar(1, trigger, grainDur, buffer, rate, 0.5, 2, panner, -1);
granulation = granulation * grainAmp;


// pitchShift Section:
shiftArray = [1.5, 2, 0.75, 4/3, 1.5, 1.5, 2];
shifter1 = Demand.ar(Dust.ar(cutoffChangeRate), 0, Drand(shiftArray, inf));
shift1 = PitchShift.ar(filter, 0.2, shifter1);
shifter2 = Demand.ar(Dust.ar(cutoffChangeRate), 0, Drand(shiftArray, inf));
shift2 = PitchShift.ar(filter, 0.2, shifter2);
shifter3 = Demand.ar(Dust.ar(cutoffChangeRate), 0, Drand(shiftArray, inf));
shift3 = PitchShift.ar(filter, 0.2, shifter3);
shiftSum = Mix.new([shift1, shift2, shift3]);
shiftSum = shiftSum * shiftAmp;
shiftSum = shiftSum ! 2;

// mix Section:
sum = Mix.new([filter, sine, shiftSum, granulation]);
//sum = sum/3;
delayLag = Lag2.kr(delayTime, 0.1);
delay = CombC.ar(sum, maxDelay, delayLag, decayTime);
delay = (delay * 0.5) + sum;
eq = BLowShelf.ar(delay, freq/2, 1, bassBoost);
sig = eq * amp;
sig = Out.ar(out, sig);

}).add;

*/