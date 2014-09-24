/*
Saturday, Septermber 20th 2014
BufferGranulator.sc
prm
*/

BufferGranulator : IM_Module {

  var <isLoaded;
  var <synth, grainBufDict;
  var server;

  *newMono { | outBus = 0, buffer = nil, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono(buffer);
  }

  prInitMono { | buffer |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      this.prAddSynthDef;
      server.sync;
      this.prMakeGrainBuffers;
      server.sync;
      synth = Synth(\prm_BufferGranulator, [\outBus, mixer.chanStereo, \buffer, buffer], group, \addToHead);
      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_BufferGranulator, {
      |
      outBus = 0, amp = 1, buffer, trigRate = 32, grainDurLow = 0.5, grainDurHigh = 1.5,
      grainEnv = -1, sync = 0, rateLow = 1, rateHigh = 1, panLow = 0, panHigh = 0,
      posLow = 0, posHigh = 1, cutoff = 18000, rq = 1
      |
      var syncTrigger, randTrigger, trigger, grainDur, rate, pan, position;
      var granulator, filter, sig;
      syncTrigger = Impulse.ar(trigRate);
      randTrigger = Dust.ar(trigRate);
      trigger = Select.ar(sync, [randTrigger, syncTrigger]);
      grainDur = TRand.ar(grainDurLow, grainDurHigh, trigger);
      rate = TRand.ar(rateLow, rateHigh, trigger);
      pan = TRand.ar(panLow, panHigh, trigger);
      position = TRand.ar(posLow, posHigh, trigger);
      position.poll;
      granulator = GrainBuf.ar(2, trigger, grainDur, buffer, rate, position, 2, pan, grainEnv, 512);
      filter = RLPF.ar(granulator, cutoff, rq);
      sig = filter * amp;
      Out.ar(outBus, sig);
    }).add;
  }

   prMakeGrainBuffers {
    {
      var grainEnvs;
      grainBufDict = IdentityDictionary.new;
      grainEnvs = (
        gabor:	Env.sine(1, 1),
        gabWide:	Env([0, 1, 1, 0], [1, 1, 1]),
        perc:	Env.perc(0.01, 0.99),
        revPerc:	Env.perc(0.99, 0.01),
        expodec:	Env.perc(0.01, 0.99, 1, 4),
        rexpodec:	Env.perc(0.99, 0.01, 1, 4)
      );
      server.sync;
      grainBufDict[\gabor] = Buffer.sendCollection(server, grainEnvs.at(\gabor).discretize, 1);
      grainBufDict[\gabWide] = Buffer.sendCollection(server, grainEnvs.at(\gabWide).discretize, 1);
      grainBufDict[\perc] = Buffer.sendCollection(server, grainEnvs.at(\perc).discretize, 1);
      grainBufDict[\revPerc] = Buffer.sendCollection(server, grainEnvs.at(\revPerc).discretize, 1);
      grainBufDict[\expodec] = Buffer.sendCollection(server, grainEnvs.at(\expodec).discretize, 1);
      grainBufDict[\rexpodec] = Buffer.sendCollection(server, grainEnvs.at(\rexpodec).discretize, 1);
      grainEnvs.do({ | env | env = nil; });
      grainEnvs = nil;
    }.fork;
  }

  //////// public functions:

  free {
    {
      grainBufDict.do({ | env | env.free; });
      server.sync;
      grainBufDict.do({ | env | env = nil; });
      grainBufDict = nil;
      synth.free;
      synth = nil;
    }.fork;
    this.freeModule;
  }

  setGrainDur { | grainDurLow = 0.01, grainDurHigh = 0.19 |
    this.setGrainDurLow(grainDurLow);
    this.setGrainDurHigh(grainDurHigh);
  }
  setGrainDurLow { | grainDurLow = 0.01 | synth.set(\grainDurLow, grainDurLow); }
  setGrainDurHigh { | grainDurHigh = 0.19 | synth.set(\grainDurHigh, grainDurHigh); }
  setTrigRate { | trigRate = 32 | synth.set(\trigRate, trigRate) }
  setPan { | panLow = -1, panHigh = 1 |
    this.setPanLow(panLow);
    this.setPanHigh(panHigh);
  }
  setPanLow { | panLow = -1 | synth.set(\panLow, panLow); }
  setPanHigh { | panHigh = 1 | synth.set(\panHigh, panHigh); }
  setRate { | rateLow = 1, rateHigh = 1 |
    this.setRateLow(rateLow);
    this.setRateHigh(rateHigh);
  }
  setRateLow { | rateLow = 1 | synth.set(\rateLow, rateLow); }
  setRateHigh { | rateHigh = 1 | synth.set(\rateHigh, rateHigh); }
  setPos { | posLow = 0.2, posHigh = 0.6 |
    this.setPosLow(posLow);
    this.setPosHigh(posHigh);
  }
  setPosLow { | posLow = 0.2 | synth.set(\posLow, posLow); }
  setPosHigh { | posHigh = 0.6 | synth.set(\posHigh, posHigh); }
  setSync { | sync = 0 |
    synth.set(\sync, sync);
  }

  setGrainEnvelope { | env = 'gabor' |
    switch(env,
      { 'hann' }, { synth.set(\env, -1) },
      { 'gabor' }, { synth.set(\env, grainBufDict[\gabor]); },
      { 'gabWide' }, { synth.set(\env, grainBufDict[\gabWide]); },
      { 'perc' }, { synth.set(\env, grainBufDict[\perc]); },
      { 'revPerc' }, { synth.set(\env, grainBufDict[\revPerc]); },
      { 'expodec' }, { synth.set(\env, grainBufDict[\expodec]); },
      { 'rexpodec' }, { synth.set(\env, grainBufDict[\rexpodec]); },
    );
  }

  setFilterCutoff { | cutoff = 20000 |
    synth.set(\cutoff, cutoff);
  }

  setCrossfade { | crossfade = 1 |
    synth.set(\mix, crossfade);
  }

}