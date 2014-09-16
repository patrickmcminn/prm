/*
Thursday, September 11th 2014
SimpleDelay.sc
prm
*/

SimpleDelay : IM_Processor {

  var <isLoaded;
  var synth;

  *newMono {
    |
    outBus, delayTime = 0.3, delayFeedback = 0.2, maxDelayTime = 5,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, feedback = false,
    relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitMono(
      delayTime, delayFeedback, maxDelayTime);
  }

  *newStereo {
    |
    outBus, delayTime = 0.3, delayFeedback = 0.2, maxDelayTime = 5,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, feedback = false,
    relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitStereo(
      delayTime, delayFeedback, maxDelayTime);
  }

  prInitMono { | delayTime = 0.3, delayFeedback = 0.2, maxDelayTime = 5 |
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true}, { 0.01.wait; });
      this.prAddSynthDef;
      server.sync;
      synth = Synth(\prm_simpleDelayMono, [\inBus, inBus, \outBus, mixer.chanMono(0), \amp, 1, \delayTime, delayTime,
        \feedback, delayFeedback, \maxDelayTime, maxDelayTime, \cutoff, 20000, \rq, 1, \mix, 0.5], group, \addToHead);
      server.sync;
      isLoaded = true;
    };
  }

  prInitStereo { | delayTime = 0.3, delayFeedback = 0.2, maxDelayTime = 5 |
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true}, { 0.01.wait; });
      this.prAddSynthDef;
      server.sync;
      synth = Synth(\prm_simpleDelayStereo, [\inBus, inBus, \outBus, mixer.chanStereo(0), \amp, 1, \delayTime, delayTime,
        \feedback, delayFeedback, \maxDelayTime, maxDelayTime, \cutoff, 20000, \rq, 1, \mix, 0.5], group, \addToHead);
      server.sync;
      isLoaded = true;
    };
  }

  prAddSynthDef {
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

    SynthDef(\prm_simpleDelayStereo, {
      |
      inBus = 0, outBus = 0, amp = 1, delayTime = 0.3, maxDelayTime = 5, feedback = 0.2,
      filterType = 0, cutoff = 20000, rq = 1, mix = 0.5
      |
      var input, lowPass, highPass, bandPass, filter, localIn, delay, sig;
      input = In.ar(inBus, 2);
      localIn = LocalIn.ar(2);

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
  }

  //////// public functions:

  free {
    synth.free;
    synth = nil;
    this.freeProcessor;
  }

  setDelayTime { | delayTime = 0.2 | synth.set(\delayTime, delayTime); }
  setFeedback { | feedback = 0.2 | synth.set(\feedback, feedback); }
  setMix { | mix = 0.5 | synth.set(\mix, mix); }
  setFilterCutoff { | cutoff = 20000 | synth.set(\cutoff, cutoff); }
  setFilterRQ { | rq = 1 | synth.set(\rq, 1); }
  setFilterType { | type = 'none' |
    switch(type,
      { 'none' }, { synth.set(\filterType, 0); },
      { 'lowPass' }, { synth.set(\filterType, 1); },
      { 'highPass' }, { synth.set(\filterType, 2); },
      { 'bandPass' }, { synth.set(\filterType, 3); }
    );
  }



}