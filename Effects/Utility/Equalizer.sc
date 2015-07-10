/*
Tuesday, January 13th 2015
Equalizer.sc
prm
*/

Equalizer : IM_Processor {

  var <isLoaded;
  var server;
  var synth;

  *newMono { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, relGroup: relGroup, addAction: addAction).prInitMono;
  }

  *newStereo { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(2, 1, outBus, relGroup: relGroup, addAction: addAction).prInitStereo;
  }

  prInitMono {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDef;
      server.sync;

      while( { try { mixer.isLoaded } != true }, { 0.001.wait; });
      synth = Synth(\prm_Equalizer_mono, [\inBus, inBus, \outBus, mixer.chanMono(0), \amp, 1], group, \addToHead);
      while({ try { synth } == nil }, { 0.001.wait; });
      isLoaded = true;
    }

  }

  prInitStereo {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDef;
      server.sync;

      while( { try { mixer.isLoaded } != true }, { 0.001.wait; });
      synth = Synth(\prm_Equalizer_Stereo, [\inBus, inBus, \outBus, mixer.chanStereo(0), \amp, 1], group, \addToHead);
      while({ try { synth } == nil }, { 0.001.wait; });
      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\prm_Equalizer_mono, {
      |
      amp = 1,
      lowFreq = 250, lowRQ = 1, lowGain = 0,
      peak1Freq = 600,  peak1RQ = 1, peak1Gain = 0,
      peak2Freq = 1000, peak2RQ = 1, peak2Gain = 0,
      peak3Freq = 1500, peak3RQ = 1, peak3Gain = 0,
      highFreq = 2500, highRQ = 1, highGain = 0,
      lowPassCutoff = 20000, lowPassRQ = 1.0,
      inBus = 0, outBus = 0
      |
      var input,  lowShelf, peak1, peak2, peak3, highShelf, lowPass, sig;
      input = In.ar(inBus, 1);
      lowShelf = BLowShelf.ar(input, lowFreq, lowRQ, lowGain);
      peak1 = BPeakEQ.ar(lowShelf, peak1Freq, peak1RQ, peak1Gain);
      peak2 = BPeakEQ.ar(peak1, peak2Freq, peak2RQ, peak2Gain);
      peak3 = BPeakEQ.ar(peak2, peak3Freq, peak3RQ, peak3Gain);
      highShelf = BHiShelf.ar(peak3, highFreq, highRQ, highGain);
      lowPass = RLPF.ar(highShelf, lowPassCutoff, lowPassRQ);
      sig = lowPass * amp;
      Out.ar(outBus, sig);
    }, [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1]).add;

    SynthDef(\prm_Equalizer_Stereo, {
      |
      amp = 1,
      lowFreq = 250, lowRQ = 1, lowGain = 0,
      peak1Freq = 600,  peak1RQ = 1, peak1Gain = 0,
      peak2Freq = 1000, peak2RQ = 1, peak2Gain = 0,
      peak3Freq = 1500, peak3RQ = 1, peak3Gain = 0,
      highFreq = 2500, highRQ = 1, highGain = 0,
      lowPassCutoff = 20000, lowPassRQ = 1.0,
      inBus = 0, outBus = 0
      |
      var input,  lowShelf, peak1, peak2, peak3, highShelf, lowPass, sig;
      input = In.ar(inBus, 2);
      lowShelf = BLowShelf.ar(input, lowFreq, lowRQ, lowGain);
      peak1 = BPeakEQ.ar(lowShelf, peak1Freq, peak1RQ, peak1Gain);
      peak2 = BPeakEQ.ar(peak1, peak2Freq, peak2RQ, peak2Gain);
      peak3 = BPeakEQ.ar(peak2, peak3Freq, peak3RQ, peak3Gain);
      highShelf = BHiShelf.ar(peak3, highFreq, highRQ, highGain);
      lowPass = RLPF.ar(highShelf, lowPassCutoff, lowPassRQ);
      sig = lowPass * amp;
      Out.ar(outBus, sig);
    }, [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1]).add;
  }

  //////// public functions:
  free {
    synth.free;
    synth = nil;
    this.freeProcessor;
  }

  setLowFreq { | freq = 250 | synth.set(\lowFreq, freq); }
  setLowRQ { | rq = 1 | synth.set(\lowRQ, rq); }
  setLowGain { | gain = 0 | synth.set(\lowGain, gain); }

  setPeak1Freq { | freq = 600 | synth.set(\peak1Freq, freq); }
  setPeak1RQ { | rq = 1 | synth.set(\peak1RQ, rq); }
  setPeak1Gain { | gain = 0 | synth.set(\peak1Gain, gain); }

  setPeak2Freq { | freq = 1000 | synth.set(\peak2Freq, freq); }
  setPeak2RQ { | rq = 1 | synth.set(\peak2RQ, rq); }
  setPeak2Gain { | gain = 0 | synth.set(\peak2Gain, gain); }

  setPeak3Freq { | freq = 1500 | synth.set(\peak3Freq, freq); }
  setPeak3RQ { | rq = 1 | synth.set(\peak3RQ, rq); }
  setPeak3Gain { | gain = 0 | synth.set(\peak3Gain, gain); }

  setHighFreq { | freq = 2500 | synth.set(\highFreq, freq); }
  setHighRQ { | rq = 1 | synth.set(\highRQ, rq); }
  setHighGain { | gain = 0 | synth.set(\highGain, gain); }

  setLowPassCutoff { | cutoff = 20000 | synth.set(\lowPassCutoff, cutoff); }
  setLowPassRQ { | rq = 1.0 | synth.set(\lowPassRQ, rq); }

}