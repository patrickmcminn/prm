/*
Tuesday, July 25th 2017
Boy_RandBells.sc
prm
*/

Boy_LowBells : IM_Module {

  var <isLoaded;
  var server;

  var <midBells, <granulator;

  var <lowBellsIsPlaying;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      lowBellsIsPlaying = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(mixer.chanStereo(0), group, 'addToHead');
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      midBells = MidBells.new(granulator.inBus, relGroup: group, addAction: 'addToHead');
      while({ try { midBells.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

      server.sync;

      this.prMakePatternParameters;

      server.sync;


      isLoaded = true;
    }
  }

  prSetInitialParameters {
    granulator.setGranulatorCrossfade(0.5);
    granulator.setGrainDur(2.9, 3.1);
    granulator.setTrigRate(20);
    granulator.setPan(-1, 1);
    granulator.setDelayMix(0.5);
    granulator.setDelayTime(3);
    granulator.setFeedback(0.5);

    midBells.makeSequence(\dSharp);
    midBells.makeSequence(\e);
    midBells.makeSequence(\fSharp);
    midBells.makeSequence(\g);
    midBells.makeSequence(\gSharp);
    midBells.makeSequence(\b);
    midBells.makeSequence(\d);
    midBells.makeSequence(\highFSharp);
    midBells.makeSequence(\highG);
  }

  prMakePatternParameters {

    // D Sharp:
    midBells.addKey(\dSharp, \dur, 0.25);
    midBells.addKey(\dSharp, \attack, 0);
    midBells.addKey(\dSharp, \releaseTime, 7);
    midBells.addKey(\dSharp, \legato, 1);
    midBells.addKey(\dSharp, \rate, Prand([0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.25, 1], inf));
    midBells.addKey(\dSharp, \pan, Pwhite(-1, 1, inf));
    midBells.addKey(\dSharp, \buffer, midBells.sampler.bufferArray[3]);
    midBells.addKey(\dSharp, \type, Pif(Pfunc({ 300.rand == 0 }), \note, \rest));

    // E:
    midBells.addKey(\e, \dur, 0.25);
    midBells.addKey(\e, \attack, 0);
    midBells.addKey(\e, \releaseTime, 7);
    midBells.addKey(\e, \legato, 1);
    midBells.addKey(\e, \rate, Prand([0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.25, 1], inf));
    midBells.addKey(\e, \pan, Pwhite(-1, 1, inf));
    midBells.addKey(\e, \buffer, midBells.sampler.bufferArray[4]);
    midBells.addKey(\e, \type, Pif(Pfunc({ 35.rand == 0 }), \note, \rest));

    // FSharp:
    midBells.addKey(\fSharp, \dur, 0.25);
    midBells.addKey(\fSharp, \attack, 0);
    midBells.addKey(\fSharp, \releaseTime, 7);
    midBells.addKey(\fSharp, \legato, 1);
    midBells.addKey(\fSharp, \rate, Prand([0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.25, 1], inf));
    midBells.addKey(\fSharp, \pan, Pwhite(-1, 1, inf));
    midBells.addKey(\fSharp, \buffer, midBells.sampler.bufferArray[6]);
    midBells.addKey(\fSharp, \type, Pif(Pfunc({ 80.rand == 0 }), \note, \rest));

    // G:
    midBells.addKey(\g, \dur, 0.25);
    midBells.addKey(\g, \attack, 0);
    midBells.addKey(\g, \releaseTime, 7);
    midBells.addKey(\g, \legato, 1);
    midBells.addKey(\g, \rate, Prand([0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.25, 1], inf));
    midBells.addKey(\g, \pan, Pwhite(-1, 1, inf));
    midBells.addKey(\g, \buffer, midBells.sampler.bufferArray[7]);
    midBells.addKey(\g, \type, Pif(Pfunc({ 20.rand == 0 }), \note, \rest));

    // G#:
    midBells.addKey(\gSharp, \dur, 0.25);
    midBells.addKey(\gSharp, \attack, 0);
    midBells.addKey(\gSharp, \releaseTime, 7);
    midBells.addKey(\gSharp, \legato, 1);
    midBells.addKey(\gSharp, \rate, Prand([0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.25, 1], inf));
    midBells.addKey(\gSharp, \pan, Pwhite(-1, 1, inf));
    midBells.addKey(\gSharp, \buffer, midBells.sampler.bufferArray[8]);
    midBells.addKey(\gSharp, \type, Pif(Pfunc({ 150.rand == 0 }), \note, \rest));

    // B:
    midBells.addKey(\b, \dur, 0.25);
    midBells.addKey(\b, \attack, 0);
    midBells.addKey(\b, \releaseTime, 7);
    midBells.addKey(\b, \legato, 1);
    midBells.addKey(\b, \rate, Prand([0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.25, 1], inf));
    midBells.addKey(\b, \pan, Pwhite(-1, 1, inf));
    midBells.addKey(\b, \buffer, midBells.sampler.bufferArray[11]);
    midBells.addKey(\b, \type, Pif(Pfunc({ 30.rand == 0 }), \note, \rest));

    // D:
    midBells.addKey(\d, \dur, 0.25);
    midBells.addKey(\d, \attack, 0);
    midBells.addKey(\d, \releaseTime, 7);
    midBells.addKey(\d, \legato, 1);
    midBells.addKey(\d, \rate, Prand([0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.25, 1], inf));
    midBells.addKey(\d, \pan, Pwhite(-1, 1, inf));
    midBells.addKey(\d, \buffer, midBells.sampler.bufferArray[14]);
    midBells.addKey(\d, \type, Pif(Pfunc({ 30.rand == 0 }), \note, \rest));

    // High F#:
    midBells.addKey(\highFSharp, \dur, 0.25);
    midBells.addKey(\highFSharp, \attack, 0);
    midBells.addKey(\highFSharp, \releaseTime, 7);
    midBells.addKey(\highFSharp, \legato, 1);
    midBells.addKey(\highFSharp, \rate, Prand([0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.25, 1], inf));
    midBells.addKey(\highFSharp, \pan, Pwhite(-1, 1, inf));
    midBells.addKey(\highFSharp, \buffer, midBells.sampler.bufferArray[18]);
    midBells.addKey(\highFSharp, \type, Pif(Pfunc({ 25.rand == 0 }), \note, \rest));

    // High G:
    midBells.addKey(\highG, \dur, 0.25);
    midBells.addKey(\highG, \attack, 0);
    midBells.addKey(\highG, \releaseTime, 7);
    midBells.addKey(\highG, \legato, 1);
    midBells.addKey(\highG, \rate, Prand([0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.25, 1], inf));
    midBells.addKey(\highG, \pan, Pwhite(-1, 1, inf));
    midBells.addKey(\highG, \buffer, midBells.sampler.bufferArray[19]);
    midBells.addKey(\highG, \type, Pif(Pfunc({ 30.rand == 0 }), \note, \rest));
  }


  //////// public functions:

  free {
    this.stopLowBells;
    granulator.free;
    midBells.free;
    this.freeModule;
    isLoaded = false;
  }

  playLowBells { | clock |
    lowBellsIsPlaying = true;
    midBells.playSequence(\dSharp, clock);
    midBells.playSequence(\e, clock);
    midBells.playSequence(\fSharp, clock);
    midBells.playSequence(\g, clock);
    midBells.playSequence(\gSharp, clock);
    midBells.playSequence(\b, clock);
    midBells.playSequence(\d, clock);
    midBells.playSequence(\highFSharp, clock);
    midBells.playSequence(\highG, clock);
  }

  stopLowBells {
    lowBellsIsPlaying = false;
    midBells.stopSequence(\dSharp);
    midBells.stopSequence(\e);
    midBells.stopSequence(\fSharp);
    midBells.stopSequence(\g);
    midBells.stopSequence(\gSharp);
    midBells.stopSequence(\b);
    midBells.stopSequence(\d);
    midBells.stopSequence(\highFSharp);
    midBells.stopSequence(\highG);
  }

}