/*
Thursday, December 3rd 2015
LowPassFilter.sc
prm
Ithaca, NY
*/

LowPassFilter : IM_Processor {

  var server, <isLoaded;
  var filter, <lfo;
  var <filterCutoff, <filterRQ;
  var <cutoffLFOBottomRatio, <cutoffLFOTopRatio;
  var <rqLFOBottom, <rqLFOTop;

  *newStereo { | outBus = 0, cutoff = 20000, rq = 1, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil,
    addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo(cutoff, rq);
  }

  *newMono { | outBus = 0, cutoff = 20000, rq = 1, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil,
    addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono(cutoff, rq);
  }

  prInitStereo { | cutoff = 20000, rq = 1 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      lfo = LFO.new(1, 'sine', -1, 1, relGroup: group, addAction: \addToHead);
      while({ try { lfo.isLoaded } != true }, { 0.001.wait; });

      filterCutoff = cutoff;
      filterRQ = rq;
      cutoffLFOBottomRatio = 1.0;
      cutoffLFOTopRatio = 1.0;
      rqLFOTop = 0.0;
      rqLFOBottom = 0.0;

      filter = Synth(\prm_LowPassFilter, [\inBus, inBus, \outBus, mixer.chanStereo(0),
        \lfoInBus, lfo.outBus, \cutoff, filterCutoff, \filterRQ, rq],
      target: lfo.synth, addAction: \addAfter);

      isLoaded = true;
    }
  }

  prInitMono { | cutoff = 20000, rq = 1 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      lfo = LFO.new(1, 'sine', -1, 1, relGroup: group, addAction: \addToHead);
      while({ try { lfo.isLoaded } != true }, { 0.001.wait; });

      filterCutoff = cutoff;
      filterRQ = rq;
      cutoffLFOBottomRatio = 1.0;
      cutoffLFOTopRatio = 1.0;
      rqLFOTop = 0.0;
      rqLFOBottom = 0.0;

      filter = Synth(\prm_LowPassFilter_mono, [\inBus, inBus, \outBus, mixer.chanMono(0),
        \lfoInBus, lfo.outBus, \cutoff, filterCutoff, \filterRQ, rq],
      target: lfo.synth, addAction: \addAfter);

      isLoaded = true;
    }
  }


  prAddSynthDefs {

    SynthDef(\prm_LowPassFilter, {
      |
      cutoff = 20000, rq = 1, inBus = 0, outBus = 0, lfoInBus = 0,
      cutoffLFOBottomRatio = 1, cutoffLFOTopRatio = 1,
      rqLFOBottom = 0, rqLFOTop = 0,
      amp = 1
      |
      var input, lfo, cutoffLFO, rqLFO, filter, sig;
      input = In.ar(inBus, 2);
      lfo = In.kr(lfoInBus, 1);
      cutoffLFO = lfo.linlin(-1, 1, cutoffLFOBottomRatio, cutoffLFOTopRatio);
      rqLFO = rq.linlin(-1, 1, rqLFOBottom, rqLFOTop);
      filter = RLPF.ar(input, (cutoff.lag2(0.1) * cutoffLFO).clip(20, 20000), rq.lag2(0.1) + rqLFO);
      sig = filter * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_LowPassFilter_mono, {
      |
      cutoff = 20000, rq = 1, inBus = 0, outBus = 0, lfoInBus = 0,
      cutoffLFOBottomRatio = 1, cutoffLFOTopRatio = 1,
      rqLFOBottom = 0, rqLFOTop = 0,
      amp = 1
      |
      var input, lfo, cutoffLFO, rqLFO, filter, sig;
      input = In.ar(inBus, 1);
      lfo = In.kr(lfoInBus, 1);
      cutoffLFO = lfo.linlin(-1, 1, cutoffLFOBottomRatio, cutoffLFOTopRatio);
      rqLFO = rq.linlin(-1, 1, rqLFOBottom, rqLFOTop);
      filter = RLPF.ar(input, (cutoff.lag2(0.1) * cutoffLFO).clip(20, 20000), rq.lag2(0.1) + rqLFO);
      sig = filter * amp;
      Out.ar(outBus, sig);
    }).add;

  }

  /////// public functions:

  free {
    filter.free;
    lfo.free;
    this.freeModule;
  }

  setCutoff { | cutoff = 20000 |
    filterCutoff = cutoff;
    filter.set(\cutoff, filterCutoff);
  }
  setRQ { | rq = 1 |
    filterRQ = rq;
    filter.set(\rq, filterRQ);
  }

  setCutoffLFOBottomRatio { | ratio = 1.0 |
    cutoffLFOBottomRatio = ratio;
    filter.set(\cutoffLFOBottomRatio, cutoffLFOBottomRatio);
  }
  setCutoffLFOTopRatio { | ratio = 1.0 |
    cutoffLFOTopRatio = ratio;
    filter.set(\cutoffLFOTopRatio, cutoffLFOTopRatio);
  }
  setRQLFOBottom { | bottom = 0.0 |
    rqLFOBottom = bottom;
    filter.set(\rqLFOBottom, rqLFOBottom);
  }
  setRQLFOTop { | top = 0.0 |
    rqLFOTop = top;
    filter.set(\rqLFOTop, rqLFOTop);
  }




}