/*
Thursday, December 19th 2013
VariDist.sc
prm
*/

VariDist {
  var server;
  var distortionBus, faderBus;
  var inputSynth, <distortionSynth, faderSynth;

  *new {
    |
    inBus = 0, outBus = 0, amp = 0.5, octaveAmp = 1, dist = 1000, trigRate = 1,
    cutoffLow = 800, cutoffHigh = 3500, resLow = 1, resHigh = 0.4,
    group = nil, addAction = 'addToTail'
    |

    ^super.new.prInit(inBus, outBus, amp, octaveAmp, dist, trigRate, cutoffLow, cutoffHigh, resLow, resHigh, group, addAction);
  }

  prInit {
    |
    inBus = 0, outBus = 0, amp = 0.5, octaveAmp = 1, dist = 1000, trigRate = 1,
    cutoffLow = 800, cutoffHigh = 3500, resLow = 1, resHigh = 0.4,
    group = nil, addAction = 'addToTail'
    |
    server = Server.default;
    server.waitForBoot {
      this.prAddSynthDefs;
      server.sync;
      this.prMakeBusses;
      server.sync;
      this.prMakeSynths(inBus, outBus, amp, octaveAmp, dist, trigRate, cutoffLow, cutoffHigh, resLow, resHigh, group, addAction);
    }
  }

  prAddSynthDefs {

    SynthDef(\prm_variDist_Input, {
      | inBus, outBus, amp = 1, mute = 1 |
      var input, sig;
      input = In.ar(inBus);
      sig = input * amp * mute;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_variDist_Fader, {
      | inBus, outBus, amp = 0.6, pan = 0, mute = 1 |
      var input, panning, sig;
      var lagTime = 0.05;
      input = In.ar(inBus);
      panning = Pan2.ar(input, pan, amp.lag(lagTime));
      sig = panning * mute;
      sig = Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_variDist, {
      |
      inBus, outBus, amp = 0.2, octaveAmp = 1, dist = 1000,
      lowBoost = 0, lowFreq = 500, midBoost = 0, midFreq = 1000, highBoost = 0, highFreq = 2500,
      trigSelect = 1, trigRate = 1, cutoffLow = 800, cutoffHigh = 3500, resLow = 1, resHigh = 0.4
      |
      var input, pitchShift, sum, trigger, distortion;
      var lowShelf, mid, highShelf;
      var cutoffChange, resonanceChange, filter, sig;
      input = In.ar(inBus);
      pitchShift = PitchShift.ar(input, 0.2, 0.5) * octaveAmp;
      sum = input + pitchShift;
      distortion = (sum * dist).distort;
      lowShelf = BLowShelf.ar(distortion, lowFreq, 1, lowBoost);
      mid = BPeakEQ.ar(lowShelf, midFreq, 1, midBoost);
      highShelf = BHiShelf.ar(mid, highFreq, 1, highBoost);
      trigger = Select.ar(trigSelect, [Impulse.ar(trigRate), Dust.ar(trigRate)]);
      cutoffChange = TExpRand.ar(cutoffLow, cutoffHigh, trigger);
      resonanceChange = TRand.ar(resLow, resHigh, trigger);
      filter = RLPF.ar(highShelf, cutoffChange, resonanceChange);
      sig = filter * amp;
      Out.ar(outBus, sig);
    }).add;

  }

  prMakeBusses {
    distortionBus = Bus.audio;
    faderBus = Bus.audio;
  }

  prFreeBusses {
    distortionBus.free;
    faderBus.free;
  }

  prMakeSynths {
    |
    inBus = 0, outBus = 0, amp = 0.5, octaveAmp = 1, dist = 1000, trigRate = 1,
    cutoffLow = 800, cutoffHigh = 3500, resLow = 1, resHigh = 0.4,
    group = nil, addAction = 'addToTail'
    |
    inputSynth = Synth(\prm_variDist_Input, [\inBus, inBus, \outBus, distortionBus], group, addAction);
    distortionSynth = Synth(\prm_variDist, [\inBus, distortionBus, \outBus, faderBus, \dist, dist, \trigRate, trigRate, \octaveAmp, octaveAmp,
      \cutoffLow, cutoffLow, \cutoffHigh, cutoffHigh, \resLow, resLow, \resHigh, resHigh], inputSynth, \addAfter);
    faderSynth = Synth(\prm_variDist_Fader, [\inBus, faderBus, \outBus, outBus, \amp, amp], distortionSynth, \addAfter);
  }

  prFreeSynths {
    inputSynth.free;
    distortionSynth.free;
    faderSynth.free;
  }

  //////// Public Functions:

  setAmp { | amp = 0.5 |
    faderSynth.set(\amp, amp);
    ^amp;
  }

  setVol { | vol |
    faderSynth.set(\amp, vol.dbamp);
  }

  setInput { | inBus = 0 |
    inputSynth.set(\inBus, inBus);
  }

  setOutput { | outBus = 0 |
    faderSynth.set(\outBus, outBus);
  }

  setPan { | pan = 0 |
    faderSynth.set(\pan, pan);
    ^pan;
  }

  setOctaveAmp { | amp = 1 |
    distortionSynth.set(\octaveAmp, amp);
    ^amp;
  }

  setOctaveVol { | vol = 0 |
    distortionSynth.set(\octaveAmp, vol.dbamp);
  }

  setDist { | dist = 100 |
    distortionSynth.set(\dist, dist);
    ^dist;
  }

  setTrigRate { | trigRate = 5 |
    distortionSynth.set(\trigRate, trigRate);
    ^trigRate;
  }

  setCutoffLow { | cutoff |
    distortionSynth.set(\cutoffLow, cutoff);
  }

  setCutoffHigh { | cutoff |
    distortionSynth.set(\cutoffHigh, cutoff);
  }

  setCutoff { | cutoffLow, cutoffHigh |
    this.setCutoffLow(cutoffLow);
    this.setCutoffHigh(cutoffHigh);
  }

  setResLow { | res |
    distortionSynth.set(\resLow, res);
  }

  setResHigh { | res |
    distortionSynth.set(\resHigh, res);
  }

  setRes { | resLow, resHigh |
    this.setResLow(resLow);
    this.setResHigh(resHigh);
  }

  setLowBoost { | boost = 0 |
    distortionSynth.set(\lowBoost, boost);
  }

  setLowFreq { | freq = 500 |
    distortionSynth.set(\lowFreq, freq);
  }

  setMidBoost { | boost = 0 |
    distortionSynth.set(\midBoost, boost);
  }

  setMidFreq { | freq = 1000 |
    distortionSynth.set(\midFreq, freq);
  }

  setHighBoost { | boost = 0 |
    distortionSynth.set(\highBoost, boost);
  }

  setHighFreq { | freq = 2500 |
    distortionSynth.set(\highFreq, freq);
  }

  setEQBoost { | lowBoost = 0, midBoost = 0, highBoost = 0 |
    this.setLowBoost(lowBoost);
    this.setMidBoost(midBoost);
    this.setHighBoost(highBoost);
  }

  setEQFreq { | lowFreq = 500, midFreq = 1000, highFreq = 2500 |
    this.setLowFreq(lowFreq);
    this.setMidFreq(midFreq);
    this.setHighFreq(highFreq);
  }

  setEQ { | lowBoost = 0, lowFreq = 500, midBoost = 0, midFreq = 1000, highBoost = 0, highFreq = 2500 |
    this.setEQBoost(lowBoost, midBoost, highBoost);
    this.setEQFreq(lowFreq, midFreq, highFreq);
  }


  free {
    this.prFreeSynths;
    this.prFreeBusses;
  }


}