/*
Tuesday, July 7th 2015
Connections_Cascade.sc
prm
*/

Connections_Cascade :IM_Module {

  var server, <isLoaded;
  var <eq, <sampler;
  var bufferArray;
  var <isPlaying;

  *new { | outBus = 0, noteBufferArray, cascadeBufferArray, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(noteBufferArray, cascadeBufferArray);
  }

  prInit { | noteBufferArray, cascadeBufferArray |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      isPlaying = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newMono(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      bufferArray = noteBufferArray ++ cascadeBufferArray;
      this.prMakePatterns;

      server.sync;
      sampler.setBufferArray(bufferArray);
      server.sync;

      eq.setHighFreq(2500);
      eq.setHighGain(-7);
      eq.setLowPassCutoff(3500);
      eq.setPeak3Freq(1050);
      eq.setPeak3Gain(-3);
      eq.setPeak2Freq(650);
      eq.setPeak2Gain(-4);
      eq.setPeak1Freq(400);
      eq.setPeak1Gain(6);

      //mixer.setPreVol(9);
      mixer.setPreVol(-9);

      server.sync;
      this.prMakePatternParameters;
      isLoaded = true;
    }
  }

  prMakePatterns {
    sampler.makeSequence(\highCSharp);
    sampler.makeSequence(\gSharp);
    sampler.makeSequence(\lowFSharp);
    sampler.makeSequence(\b);
    sampler.makeSequence(\e);
    sampler.makeSequence(\a);
    sampler.makeSequence(\lowCSharp);
    sampler.makeSequence(\highFSharp);
  }

  prMakePatternParameters {

    var env = Env(#[0, 0.4, 0, 0], #[6.4, 6.4, 12.8], 4);
    var attackTime = 0.025;
    //var sustain = 0.2;
    var releaseTime = 0.025;

    sampler.addKey(\highCSharp, \buffer, bufferArray[0]);
    sampler.addKey(\highCSharp, \dur, 0.25);
    sampler.addKey(\highCSharp, \rate, 1);
    sampler.addKey(\highCSharp, \amp, Pn(Pif(Ptime(inf) <= 25.6, env), inf));
    sampler.addKey(\highCSharp, \attackTime, attackTime);
    sampler.addKey(\highCSharp, \releaseTime, releaseTime);
    sampler.addKey(\highCSharp, \startPos, 0.1);
    sampler.addKey(\highCSharp, \legato, 0.9);

    sampler.addKey(\gSharp, \buffer, bufferArray[0]);
    sampler.addKey(\gSharp, \dur, 0.25);
    sampler.addKey(\gSharp, \rate, 3/4);
    sampler.addKey(\gSharp, \amp, Pn(Pif(Ptime(inf) <= 25.6, env), inf));
    sampler.addKey(\gSharp, \attackTime, attackTime);
    sampler.addKey(\gSharp, \releaseTime, releaseTime);
    sampler.addKey(\gSharp, \startPos, 0.1);
    sampler.addKey(\gSharp, \legato, 0.9);

    sampler.addKey(\lowFSharp, \buffer, bufferArray[0]);
    sampler.addKey(\lowFSharp, \dur, 0.25);
    sampler.addKey(\lowFSharp, \rate, 2/3);
    sampler.addKey(\lowFSharp, \amp, Pn(Pif(Ptime(inf) <= 25.6, env), inf));
    sampler.addKey(\lowFSharp, \attackTime, attackTime);
    sampler.addKey(\lowFSharp, \releaseTime, releaseTime);
    sampler.addKey(\lowFSharp, \startPos, 0.1);
    sampler.addKey(\lowFSharp, \legato, 0.9);

    sampler.addKey(\b, \buffer, bufferArray[0]);
    sampler.addKey(\b, \dur, 0.25);
    sampler.addKey(\b, \rate, 8/9);
    sampler.addKey(\b, \amp, Pn(Pif(Ptime(inf) <= 25.6, env), inf));
    sampler.addKey(\b, \attackTime, attackTime);
    sampler.addKey(\b, \releaseTime, releaseTime);
    sampler.addKey(\b, \startPos, 0.1);
    sampler.addKey(\b, \legato, 0.9);

    sampler.addKey(\e, \buffer, bufferArray[0]);
    sampler.addKey(\e, \dur, 0.25);
    sampler.addKey(\e, \rate, 6/5);
    sampler.addKey(\e, \amp, Pn(Pif(Ptime(inf) <= 25.6, env), inf));
    sampler.addKey(\e, \attackTime, attackTime);
    sampler.addKey(\e, \releaseTime, releaseTime);
    sampler.addKey(\e, \startPos, 0.1);
    sampler.addKey(\e, \legato, 0.9);

    sampler.addKey(\a, \buffer, bufferArray[0]);
    sampler.addKey(\a, \dur, 0.25);
    sampler.addKey(\a, \rate, 4/5);
    sampler.addKey(\a, \amp, Pn(Pif(Ptime(inf) <= 25.6, env), inf));
    sampler.addKey(\a, \attackTime, attackTime);
    sampler.addKey(\a, \releaseTime, releaseTime);
    sampler.addKey(\a, \startPos, 0.1);
    sampler.addKey(\a, \legato, 0.9);

    sampler.addKey(\lowCSharp, \buffer, bufferArray[0]);
    sampler.addKey(\lowCSharp, \dur, 0.25);
    sampler.addKey(\lowCSharp, \rate, 0.5);
    sampler.addKey(\lowCSharp, \amp, Pn(Pif(Ptime(inf) <= 25.6, env), inf));
    sampler.addKey(\lowCSharp, \attackTime, attackTime);
    sampler.addKey(\lowCSharp, \releaseTime, releaseTime);
    sampler.addKey(\lowCSharp, \startPos, 0.1);
    sampler.addKey(\lowCSharp, \legato, 0.9);

    sampler.addKey(\highFSharp, \buffer, bufferArray[0]);
    sampler.addKey(\highFSharp, \dur, 0.25);
    sampler.addKey(\highFSharp, \rate, 4/3);
    sampler.addKey(\highFSharp, \amp, Pn(Pif(Ptime(inf) <= 25.6, env), inf));
    sampler.addKey(\highFSharp, \attackTime, attackTime);
    sampler.addKey(\highFSharp, \releaseTime, releaseTime);
    sampler.addKey(\highFSharp, \startPos, 0.1);
    sampler.addKey(\highFSharp, \legato, 0.9);
  }

  //////// public functions:

  free {
    sampler.free;
    eq.free;
    bufferArray.do({ | buf | buf.free; });
    this.freeModule;
    isPlaying = false;
    isLoaded = false;
  }

  playSequences { | clock |
    clock.schedAbs(clock.nextTimeOnGrid, { sampler.playSequence(\highCSharp, clock) });
    clock.schedAbs(clock.nextTimeOnGrid + 4, { sampler.playSequence(\gSharp, clock)} );
    clock.schedAbs(clock.nextTimeOnGrid + 8, { sampler.playSequence(\lowFSharp, clock)});
    clock.schedAbs(clock.nextTimeOnGrid + 12, { sampler.playSequence(\b, clock) });
    clock.schedAbs(clock.nextTimeOnGrid + 16, { sampler.playSequence(\e, clock) });
    clock.schedAbs(clock.nextTimeOnGrid + 20, { sampler.playSequence(\a, clock) });
    clock.schedAbs(clock.nextTimeOnGrid + 24, { sampler.playSequence(\lowCSharp, clock)});
    clock.schedAbs(clock.nextTimeOnGrid + 28, { sampler.playSequence(\highFSharp, clock)} );
    isPlaying = true;
  }

  stopSequences {
    sampler.stopSequence(\highCSharp);
    sampler.stopSequence(\gSharp);
    sampler.stopSequence(\lowFSharp);
    sampler.stopSequence(\b);
    sampler.stopSequence(\e);
    sampler.stopSequence(\a);
    sampler.stopSequence(\lowCSharp);
    sampler.stopSequence(\highFSharp);
    isPlaying = false;
  }

  togglePlaySequences { | clock |
    if( isPlaying == false, { this.playSequences(clock); }, { this.stopSequences(clock); });
  }

}