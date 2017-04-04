/*
Tuesday, January 13th 2015
Equalizer.sc
prm
*/

Equalizer : IM_Processor {

  var <isLoaded;
  var server;
  var synth;

  var <lowFreq, <lowRQ, <lowGain;
  var <peak1Freq, <peak1RQ, <peak1Gain;
  var <peak2Freq, <peak2RQ, <peak2Gain;
  var <peak3Freq, <peak3RQ, <peak3Gain;
  var <highFreq, <highRQ, <highGain;
  var <lowPassCutoff, <lowPassRQ;
  var <highPassCutoff, <highPassRQ;

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
      this.prInitializeParameters;
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
      this.prInitializeParameters;
      while({ try { synth } == nil }, { 0.001.wait; });
      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\prm_Equalizer_mono, {
      |
      amp = 1,
      highPassCutoff = 5, highPassRQ = 1.0,
      lowFreq = 250, lowRQ = 1, lowGain = 0,
      peak1Freq = 600,  peak1RQ = 1, peak1Gain = 0,
      peak2Freq = 1000, peak2RQ = 1, peak2Gain = 0,
      peak3Freq = 1500, peak3RQ = 1, peak3Gain = 0,
      highFreq = 2500, highRQ = 1, highGain = 0,
      lowPassCutoff = 20000, lowPassRQ = 1,
      inBus = 0, outBus = 0
      |
      var input, highPass, lowShelf, peak1, peak2, peak3, highShelf, lowPass, sig;
      input = In.ar(inBus, 1);
      highPass = RHPF.ar(input, highPassCutoff.lag(0.1), highPassRQ);
      lowShelf = BLowShelf.ar(highPass, lowFreq, lowRQ, lowGain);
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
      highPassCutoff = 5, highPassRQ = 1.0,
      lowFreq = 250, lowRQ = 1, lowGain = 0,
      peak1Freq = 600,  peak1RQ = 1, peak1Gain = 0,
      peak2Freq = 1000, peak2RQ = 1, peak2Gain = 0,
      peak3Freq = 1500, peak3RQ = 1, peak3Gain = 0,
      highFreq = 2500, highRQ = 1, highGain = 0,
      lowPassCutoff = 20000, lowPassRQ = 1,
      inBus = 0, outBus = 0
      |
      var input, highPass, lowShelf, peak1, peak2, peak3, highShelf, lowPass, sig;
      input = In.ar(inBus, 2);
      highPass = RHPF.ar(input, highPassCutoff.lag(0.1), highPassRQ);
      lowShelf = BLowShelf.ar(highPass, lowFreq, lowRQ, lowGain);
      peak1 = BPeakEQ.ar(lowShelf, peak1Freq, peak1RQ, peak1Gain);
      peak2 = BPeakEQ.ar(peak1, peak2Freq, peak2RQ, peak2Gain);
      peak3 = BPeakEQ.ar(peak2, peak3Freq, peak3RQ, peak3Gain);
      highShelf = BHiShelf.ar(peak3, highFreq, highRQ, highGain);
      lowPass = RLPF.ar(highShelf, lowPassCutoff, lowPassRQ);
      sig = lowPass * amp;
      Out.ar(outBus, sig);
    }, [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1]).add;
  }

  prInitializeParameters {
    lowFreq = 250;
    lowRQ = 1;
    lowGain = 0;
    peak1Freq = 600;
    peak1RQ = 1;
    peak1Gain = 0;
    peak2Freq = 1000;
    peak2RQ = 1;
    peak2Gain = 0;
    peak3Freq = 1500;
    peak3RQ = 1;
    peak3Gain = 0;
    highFreq = 2500;
    highRQ = 1;
    highGain = 0;
    lowPassCutoff = 20000;
    lowPassRQ = 3;
    highPassCutoff = 5;
    highPassRQ = 1;
  }

  //////// public functions:
  free {
    synth.free;
    synth = nil;
    this.freeProcessor;
  }

  setHighPassCutoff { | freq = 0 |
    highPassCutoff = freq;
    synth.set(\highPassCutoff, highPassCutoff);
  }
  setHighPassRQ { | rq = 1 |
    highPassRQ = rq;
    synth.set(\highPassRQ, highPassRQ);
  }

  setLowFreq { | freq = 250 |
    lowFreq = freq;
    synth.set(\lowFreq, lowFreq);
  }
  setLowRQ { | rq = 1 |
    lowRQ = rq;
    synth.set(\lowRQ, lowRQ);
  }
  setLowGain { | gain = 0 |
    lowGain = gain;
    synth.set(\lowGain, lowGain);
  }

  setPeak1Freq { | freq = 600 |
    peak1Freq = freq;
    synth.set(\peak1Freq, peak1Freq);
  }
  setPeak1RQ { | rq = 1 |
    peak1RQ = rq;
    synth.set(\peak1RQ, peak1RQ);
  }
  setPeak1Gain { | gain = 0 |
    peak1Gain = gain;
    synth.set(\peak1Gain, peak1Gain);
  }

  setPeak2Freq { | freq = 1000 |
    peak2Freq = freq;
    synth.set(\peak2Freq, peak2Freq);
  }
  setPeak2RQ { | rq = 1 |
    peak2RQ = rq;
    synth.set(\peak2RQ, peak2RQ);
  }
  setPeak2Gain { | gain = 0 |
    peak2Gain = gain;
    synth.set(\peak2Gain, peak2Gain);
  }

  setPeak3Freq { | freq = 1500 |
    peak3Freq = freq;
    synth.set(\peak3Freq, peak3Freq);
  }
  setPeak3RQ { | rq = 1 |
    peak3RQ = rq;
    synth.set(\peak3RQ, peak3RQ);
  }
  setPeak3Gain { | gain = 0 |
    peak3Gain = gain;
    synth.set(\peak3Gain, peak3Gain);
  }

  setHighFreq { | freq = 2500 |
    highFreq = freq;
    synth.set(\highFreq, highFreq);
  }
  setHighRQ { | rq = 1 |
    highRQ = rq;
    synth.set(\highRQ, highRQ);
  }
  setHighGain { | gain = 0 |
    highGain = gain;
    synth.set(\highGain, highGain);
  }

  setLowPassCutoff { | cutoff = 20000 |
    lowPassCutoff = cutoff;
    synth.set(\lowPassCutoff, lowPassCutoff);
  }
  setLowPassRQ { | rq = 1.0 |
    lowPassRQ = rq;
    synth.set(\lowPassRQ, lowPassRQ);
  }

}