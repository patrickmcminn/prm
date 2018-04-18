/*
Tuesday, April 17th 2018
Meaning_Hiss.sc
prm

everything was pregnant with meaning
*/

Meaning_Hiss : IM_Module {

  var <isLoaded;
  var server;

  var <sampler, splitter, <glitchLooper;
  var <isPlaying;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path, sampleArray;

      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/everything was pregnant with meaning/samples/hiss/";
      sampleArray = (path ++ "*").pathMatch;
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      glitchLooper = GlitchLooper.newStereo(mixer.chanStereo(0), 0.3, relGroup: group, addAction: \addToHead);
      while({ try { glitchLooper.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newStereo(2, [mixer.chanStereo(0), glitchLooper.inBus], relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newStereo(splitter.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      sampler.makeSequence(\hiss1);
      sampler.makeSequence(\hiss2);
      sampler.makeSequence(\hiss3);
      sampler.makeSequence(\hiss4);

      server.sync;

      this.prMakePatternParameters;
      glitchLooper.mixer.setVol(-6);

      isPlaying = false;
      isLoaded = true;
    }
  }

  prMakePatternParameters {
    sampler.addKey(\hiss1, \buffer, Prand(
      [sampler.bufferArray[0], sampler.bufferArray[1], sampler.bufferArray[2], sampler.bufferArray[3]], inf));
    sampler.addKey(\hiss1, \dur, 1.71428571428572);
    sampler.addKey(\hiss1, \amp, 0.6);
    sampler.addKey(\hiss1, \legato, 1);
    sampler.addKey(\hiss1, \pan, Pbrown(-1, 1, 0.2, inf));

    sampler.addKey(\hiss2, \buffer, Prand(
      [sampler.bufferArray[0], sampler.bufferArray[1], sampler.bufferArray[2], sampler.bufferArray[3]], inf));
    sampler.addKey(\hiss2, \dur, 1.71428571428572);
    sampler.addKey(\hiss2, \amp, 0.6);
    sampler.addKey(\hiss2, \legato, 1);
    sampler.addKey(\hiss2, \pan, Pbrown(-1, 1, 0.2, inf));

    sampler.addKey(\hiss3, \buffer, Prand(
      [sampler.bufferArray[0], sampler.bufferArray[1], sampler.bufferArray[2], sampler.bufferArray[3]], inf));
    sampler.addKey(\hiss3, \dur, 1.71428571428572);
    sampler.addKey(\hiss3, \amp, 0.6);
    sampler.addKey(\hiss3, \legato, 1);
    sampler.addKey(\hiss3, \pan, Pbrown(-1, 1, 0.2, inf));

  }

  ///////// public functions:

  free {
    sampler.free;
    splitter.free;
    glitchLooper.free;
    this.freeModule;
    isLoaded = false;
  }

  playSequences {
    this.sampler.playSequence(\hiss1);
    this.sampler.playSequence(\hiss2);
    this.sampler.playSequence(\hiss3);
    isPlaying = true;
  }

  stopSequences {
    this.sampler.stopSequence(\hiss1);
    this.sampler.stopSequence(\hiss2);
    this.sampler.stopSequence(\hiss3);
    isPlaying = false;
  }

  tglSequences {
    if( isPlaying == false, { this.playSequences; }, { this.stopSequences; });
  }


}
