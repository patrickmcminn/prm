/*
Thursday, December 3rd 2015
FalseSelf_MidBuzz.sc
prm
Ithaca, NY
*/

FalseSelf_MidBuzz :IM_Module {

  var server, <isLoaded;
  var <sampler, <filter, <delay;

  *new { | outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path = "~/Library/Application Support/SuperCollider/Extensions/prm/Songs/FalseSelf/samples/midbuzz/";
      var sampleArray = (path ++ "*").pathMatch;
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      delay = SimpleDelay.newStereo(mixer.chanStereo(0), 0.48, 0.45, 0.5, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      filter = LowPassFilter.newStereo(delay.inBus, 1950, 0.4, relGroup: group, addAction: \addToHead);
      while({ try { filter.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newStereo(filter.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });
      server.sync;

      filter.lfo.setFrequency(0.16);
      filter.lfo.setWaveform('noise');
      filter.setCutoffLFOBottomRatio(0.4);
      filter.setCutoffLFOTopRatio(1.5);

      this.prMakePattern;
      isLoaded = true;
    }
  }

  prMakePattern {
    {
      sampler.makeSequence(\midBuzz, 'sustaining');
      server.sync;
      sampler.addKey(\midBuzz, \buffer, Prand([sampler.bufferArray[0], sampler.bufferArray[1], sampler.bufferArray[2],
        sampler.bufferArray[3], sampler.bufferArray[4], sampler.bufferArray[5]], inf));
      sampler.addKey(\midBuzz, \dur, Prand([3, 4, 5, 6, 7], inf));
      sampler.addKey(\midBuzz, \legato, 1);
      sampler.addKey(\midBuzz, \amp, 1);

    }.fork;
  }

  //////// public functions:

  free {
    sampler.free;
    this.freeModule;
    isLoaded = false;
  }

  fadeVolume { | start = 0, end = -inf, time = 10 |
    {
      var bus = Bus.control;
      server.sync;
      { Out.kr(bus, Line.kr(start.dbamp, end.dbamp, time, doneAction: 2)); }.play;
      mixer.mapAmp(bus);
      { mixer.setVol(end); }.defer(time);
      { bus.free; }.defer(time);
    }.fork;
  }


  playSequence { | clock = 'internal' | sampler.playSequence(\midBuzz, clock); }
  stopSequence { sampler.stopSequence(\midBuzz); }


}