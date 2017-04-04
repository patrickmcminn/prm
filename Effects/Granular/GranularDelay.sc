/*
Thursday, September 11th 2014
GranularDelay.sc
prm
*/

GranularDelay : IM_Module {

  var <isLoaded = false;
  var <granulator, <delay;
  var rateChangeRoutine;
  var <grainEnv;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit;
  }

  prInit {
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true}, { 0.01.wait; });
      delay = SimpleDelay.newStereo(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true}, { 0.01.wait; });
      granulator = IM_Granulator.new(delay.inBus, relGroup: group, addAction: \addToHead);
      while({ try { granulator.isLoaded } != true}, { 0.01.wait; });
      granulator.setCrossfade(-1);
      grainEnv = 'sine';
      isLoaded = true;
    };
  }

  //////// public functions:
  free {
    granulator.free;
    granulator = nil;
    delay.free;
    delay = nil;
    this.freeModule;
  }

  inBus { ^granulator.inBus }

  //////// granulator:

  setGrainDur { | grainDurLow = 0.01, grainDurHigh = 0.19 |
    granulator.setGrainDurLow(grainDurLow);
    granulator.setGrainDurHigh(grainDurHigh);
  }
  setGrainDurLow { | grainDurLow = 0.01 | granulator.setGrainDurLow(grainDurLow); }
  setGrainDurHigh { | grainDurHigh = 0.19 | granulator.setGrainDurHigh(grainDurHigh); }
  setTrigRate { | trigRate = 32 | granulator.setTrigRate(trigRate); }
  setPan { | panLow = -1, panHigh = 1 |
    granulator.setPanLow(panLow);
    granulator.setPanHigh(panHigh);
  }
  setPanLow { | panLow = -1 | granulator.setPanLow(panLow); }
  setPanHigh { | panHigh = 1 | granulator.setPanHigh(panHigh); }
  setRate { | rateLow = 1, rateHigh = 1 |
    granulator.setRateLow(rateLow);
    granulator.setRateHigh(rateHigh);
  }
  setRateLow { | rateLow = 1 | granulator.setRateLow(rateLow); }
  setRateHigh { | rateHigh = 1 | granulator.setRateHigh(rateHigh); }
  setPos { | posLow = 0.2, posHigh = 0.6 |
    granulator.setPosLow(posLow);
    granulator.setPosHigh(posHigh);
  }
  setPosLow { | posLow = 0.2 | granulator.setPosLow(posLow); }
  setPosHigh { | posHigh = 0.6 | granulator.setPosHigh(posHigh); }
  setSync { | sync = 0 | granulator.setSync(sync);}
  setGrainEnvelope { | env = 'gabor' |
    grainEnv = env;
    granulator.setGrainEnvelope(env);
  }
  setGranulatorFilterCutoff { | cutoff = 20000 | granulator.setFilterCutoff(cutoff); }
  setGranulatorCrossfade { | crossfade = 1 | granulator.setCrossfade(crossfade); }

  mapGranulatorParameter { | param, bus |
    granulator.synth.set(param, bus.asMap);
  }

  //////// delay:
  setDelayTime { | delayTime = 0.2 | delay.setDelayTime(delayTime); }
  setFeedback { | feedback = 0.2 | delay.setFeedback(feedback); }
  setDelayMix { | mix = 0.5 | delay.setMix(mix); }
  setDelayFilterCutoff { | cutoff = 20000 | delay.setFilterCutoff(cutoff); }
  setDelayFilterRQ { | rq = 1 | delay.setFilterRQ(rq); }
  setDelayFilterType { | type = 'none' | delay.setFilterType(type); }


}