/*
Monday, July 6th 2015
Connections_Bassline.sc
prm
*/

Connections_Bassline : IM_Module {

  var <isLoaded, server;
  var <eq, <reverb, <granulator, <sampler;
  var <bassline1IsPlaying, <bassline2IsPlaying;

  *new { | outBus = 0, bufferArray, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(bufferArray);
  }

  prInit { | bufferArray |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      reverb = IM_Reverb.new(granulator.inBus, nil, nil, nil, nil, false, 1, 0.7, 0.8, 0.45, group, \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(reverb.inBus, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newMono(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prMakePatterns;

      sampler.setBufferArray(bufferArray);

      eq.setLowFreq(250);
      eq.setLowGain(15);
      eq.setHighFreq(1770);
      eq.setHighGain(-6);

      granulator.setGranulatorCrossfade(-1);
      granulator.setDelayMix(0);
      granulator.setGrainDur(0.2, 0.4);
      granulator.setTrigRate(30);

      mixer.setPreVol(18);

      server.sync;

      this.prMakePatternParameters(bufferArray);

      isLoaded = true;
    };
  }

  prMakePatterns {
    sampler.makeSequence(\bassline1, 'sustaining');
    sampler.makeSequence(\bassline2, 'sustaining');
    bassline1IsPlaying = false;
    bassline2IsPlaying = false;
  }

  prMakePatternParameters { | bufferArray |
    var noteCount = Pseq([8, 6, 16, 9, 4], inf);
    //bassline 1:
    sampler.addKey(\bassline1, \buffer, Pseq([bufferArray[0], bufferArray[4], bufferArray[2],
      bufferArray[3], bufferArray[1]], inf));
    sampler.addKey(\bassline1, \rate, 0.25);
    sampler.addKey(\bassline1, \attackTime, 0.05);
    sampler.addKey(\bassline1, \releaseTime, 0.05);
    sampler.addKey(\bassline1, \dur, Pseq([8, 8, 8, 4, 4], inf));
    sampler.addKey(\bassline1, \legato, 0.95);
    sampler.addKey(\bassline1, \startPos, 0.1);
    sampler.addKey(\bassline1, \amp, 1);

    //bassline 2:
    sampler.addKey(\bassline2, \buffer, Pstutter(
      Pseq([8, 6, 16, 9, 4], inf), Pseq([bufferArray[2], bufferArray[3], bufferArray[0], bufferArray[1], bufferArray[4]], inf)));
    sampler.addKey(\bassline2, \rate, 0.5);
    sampler.addKey(\bassline2, \amp, 0.8);
    sampler.addKey(\bassline2, \dur, Pstutter(
			noteCount,
      Pseq([0.5, 2/3, 0.25, 1/3, 0.25], inf)));
    sampler.addKey(\bassline2, \attackTime, Pstutter(
			noteCount,
      Pseq([0.05, 1/30, 0.025, 1/30, 0.025], inf)));
    sampler.addKey(\bassline2, \releaseTime, Pstutter(
			noteCount,
      Pseq([0.05, 1/30, 0.025, 1/30, 0.025], inf)));
    sampler.addKey(\bassline2, \startPos, 0.1);
    sampler.addKey(\bassline2, \legato, 0.95);
  }

  playBassline1 { | clock |
    sampler.playSequence(\bassline1, clock, 1);
    bassline1IsPlaying = true;
  }
  stopBassline1 {
    sampler.stopSequence(\bassline1);
    bassline1IsPlaying = false;
  }
  toggleBassline1Play {
    if( bassline1IsPlaying == false,
      { this.playBassline1 },
      { this.stopBassline1 }
    );
  }

  playBassline2 { | clock |
    sampler.playSequence(\bassline2, clock, 1);
    bassline2IsPlaying = true;
  }
  stopBassline2 {
    sampler.stopSequence(\bassline2);
    bassline2IsPlaying = false;
  }
  toggleBassline2Play {
    if( bassline2IsPlaying == false,
      { this.playBassline2 },
      { this.stopBassline2 }
    );
  }

}