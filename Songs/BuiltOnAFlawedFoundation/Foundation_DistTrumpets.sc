/*
Monday, June 19th 2017
Foundation_DistTrumpets.sc
prm
*/

Foundation_DistTrumpets : IM_Module {

  var server, <isLoaded;
  var <trumpet;
  var <distortion, <granulator;
  var <arrivalSequenceIsPlaying;

  *new { | outBus, highDBuffer, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: \addToHead).prInit(highDBuffer);
  }

  prInit { | highDBuffer |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      distortion = Distortion.newStereo(granulator.inBus, 3, relGroup: group, addAction: \addToHead);
      while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

      trumpet = SamplePlayer.newMono(distortion.inBus, relGroup: group, addAction: \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

      this.prSetInitialParameters;
      trumpet.setBuffer(highDBuffer);

      server.sync;

      trumpet.makeSequence(\arrival);

      server.sync;
      this.prMakeSequence;

      arrivalSequenceIsPlaying = false;

      isLoaded = true;
    }
  }

  prSetInitialParameters {
    granulator.setGranulatorCrossfade(-0.5);
    granulator.setDelayMix(0.35);
    granulator.setDelayTime(1.25);
    granulator.setFeedback(0.2);
    granulator.setGrainDur(0.1, 0.3);
    granulator.setGrainEnvelope('hanning');

    distortion.preEQ.setHighPassCutoff(400);
    distortion.postEQ.setHighPassCutoff(550);
    distortion.postEQ.setLowPassCutoff(4500);

    trumpet.setAttackTime(0.25);
    trumpet.setReleaseTime(0.25);
  }

  prMakeSequence {
    trumpet.addKey(\arrival, \legato, 1);
    trumpet.addKey(\arrival, \dur, Pseq([16, 4, 4, 4, 4, 4, 4, 4, 4, 4], 1));
    trumpet.addKey(\arrival, \rate, Pseq([
      [-12, -9, -5],
      [3, 0, 7], [3, 0, 7],[3, 0, 7], [3, 0, 7],
      [-12, -9, -5], [-14, -9, -5], [0, 3, 7], [-2, 3, 7],
      [-2, 3, 7]
    ].midiratio, 1));
  }

  //////// public functions:

  free {
    trumpet.free;
    distortion.free;
    granulator.free;
    this.freeModule;
    isLoaded = false;
  }

  playArrivalSequence { | clock |
    trumpet.playSequence(\arrival, clock);
    arrivalSequenceIsPlaying = true;
  }
  stopArrivalSequence {
    trumpet.stopSequence(\arrival);
    arrivalSequenceIsPlaying = false;
  }
}
