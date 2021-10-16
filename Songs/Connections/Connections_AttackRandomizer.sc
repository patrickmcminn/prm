/*
Tuesday, July 7th 2015
Connections_AttackRandomizer.sc
prm
*/

Connections_AttackRandomizer :IM_Module {

  var server, <isLoaded;
  var <sampler, <eq;
  var myBufferArray;
  var <isPlaying;

  *new { | outBus = 0, bufferArray, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(bufferArray);
  }

  prInit { | bufferArray |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      isPlaying = false;
      myBufferArray = bufferArray;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newMono(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      this.prMakePatterns;
      server.sync;

      sampler.setBufferArray(myBufferArray);
      eq.setHighFreq(2500);
      eq.setHighGain(-7);
      mixer.setPreVol(-6);

      server.sync;

      this.prMakePatternParameters(myBufferArray);

      isLoaded = true;
    };
  }

  prMakePatterns {
    sampler.makeSequence(\cSharp);
    sampler.makeSequence(\gSharp);
    sampler.makeSequence(\fSharp);
    sampler.makeSequence(\a);
  }

  prMakePatternParameters { | bufferArray |

    sampler.addKey(\cSharp, \buffer, bufferArray[0]);
    sampler.addKey(\cSharp, \dur, 2);
    sampler.addKey(\cSharp, \attackTime, 3);
    sampler.addKey(\cSharp, \releaseTime, 0.05);
    sampler.addKey(\cSharp, \amp, Pif( Pfunc( { 12.rand > 8 }), 0.35, 0));
    sampler.addKey(\cSharp, \pan, Pwhite(-1, 1));
    sampler.addKey(\cSharp, \legato, 1.5);

    sampler.addKey(\gSharp, \buffer, bufferArray[1]);
    sampler.addKey(\gSharp, \dur, 2.01);
    sampler.addKey(\gSharp, \attackTime, 3);
    sampler.addKey(\gSharp, \releaseTime, 0.05);
    sampler.addKey(\gSharp, \amp, Pif( Pfunc( { 12.rand > 9 }), 0.35, 0));
    sampler.addKey(\gSharp, \pan, Pwhite(-1, 1));
    sampler.addKey(\gSharp, \legato, 1.5);

    sampler.addKey(\fSharp, \buffer, bufferArray[2]);
    sampler.addKey(\fSharp, \dur, 1.99);
    sampler.addKey(\fSharp, \attackTime, 3);
    sampler.addKey(\fSharp, \releaseTime, 0.05);
    sampler.addKey(\fSharp, \amp, Pif( Pfunc( { 16.rand > 12 }), 0.35, 0));
    sampler.addKey(\fSharp, \pan, Pwhite(-1, 1));
    sampler.addKey(\fSharp, \legato, 1.5);

    sampler.addKey(\a, \buffer, bufferArray[3]);
    sampler.addKey(\a, \dur, 2.011);
    sampler.addKey(\a, \attackTime, 3);
    sampler.addKey(\a, \releaseTime, 0.05);
    sampler.addKey(\a, \amp, Pif( Pfunc( { 8.rand > 6 }), 0.35, 0));
    sampler.addKey(\a, \pan, Pwhite(-1, 1));
    sampler.addKey(\a, \legato, 1.5);

  }

  //////// public functions:

  free {
    eq.free;
    sampler.free;
    this.freeModule;
  }

  playSequences { | clock |
    sampler.playSequence(\cSharp, clock);
    sampler.playSequence(\gSharp, clock);
    sampler.playSequence(\fSharp, clock);
    sampler.playSequence(\a, clock);
    isPlaying = true;
  }

  stopSequences {
    sampler.stopSequence(\cSharp);
    sampler.stopSequence(\gSharp);
    sampler.stopSequence(\fSharp);
    sampler.stopSequence(\a);
    isPlaying = false;
  }

}