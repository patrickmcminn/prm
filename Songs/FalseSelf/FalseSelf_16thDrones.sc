/*
Friday, December 4th 2015
FalseSelf_16thDrones
prm
driving from Ithaca, to Kingston, NY
'Ol Bus
*/

FalseSelf_16thDrones : IM_Module {

  var server, <isLoaded;
  var <sampler, <delay, <filter1, <filter2, <eq;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path = "~/Library/Application Support/SuperCollider/Extensions/prm/Songs/FalseSelf/samples/crudedrone/";
      var sampleArray = (path ++ "*").pathMatch;
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });
      delay = SimpleDelay.newStereo(eq.inBus, 0.24, 0.3, 0.25, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });
      filter2 = LowPassFilter.newStereo(delay.inBus, 4570, 1, relGroup: group, addAction: \addToHead);
      while({ try { filter2.isLoaded } != true }, { 0.001.wait; });
      filter1 = LowPassFilter.newStereo(filter2.inBus, 5530, 1, relGroup: group, addAction: \addToHead);
      while({ try { filter1.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newStereo(filter1.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      filter2.lfo.setWaveform('noise');
      filter2.lfo.setFrequency(10);
      filter2.setCutoffLFOBottomRatio(0.7);
      filter2.setCutoffLFOTopRatio(1.3);

      filter1.lfo.setWaveform('rect');
      filter1.lfo.setFrequency(1/0.12);
      filter1.setCutoffLFOBottomRatio(0);
      filter1.setCutoffLFOTopRatio(3);

      //mixer.setSendVol(2, 0);
      //mixer.setPreVol(3);
      //mixer.setVol(-6);

      eq.setPeak2Freq(673);
      eq.setPeak2Gain(-9);

      this.prMakeSequences;

      isLoaded = true;
    };
  }

  prMakeSequences{
    {
      sampler.makeSequence(\voice1);
      sampler.makeSequence(\voice2);
      sampler.makeSequence(\voice3);
      server.sync;

      sampler.addKey(\voice1, \buffer, Prand([sampler.bufferArray[9], sampler.bufferArray[7], sampler.bufferArray[6],
        sampler.bufferArray[4], sampler.bufferArray[0], sampler.bufferArray[5]], inf));
      sampler.addKey(\voice1, \dur, Prand([8, 8, 8, 8, 8, 8, 3, 6, 6, 6, 4, 4, 4.5, 4.6], inf));
      sampler.addKey(\voice1, \legato, 1);
      sampler.addKey(\voice1, \amp, 1);

      sampler.addKey(\voice2, \buffer, Prand([sampler.bufferArray[8], sampler.bufferArray[9], sampler.bufferArray[10],
        sampler.bufferArray[11], sampler.bufferArray[11], sampler.bufferArray[12]], inf));
      sampler.addKey(\voice2, \dur, Prand([8, 8, 8, 8, 3, 6, 6, 6, 4, 4, 4.5, 4.6, 3, 4, 5, 3, 4, 5, 7], inf));
      sampler.addKey(\voice2, \legato, 1);
      sampler.addKey(\voice2, \amp, 1);

      sampler.addKey(\voice3, \buffer, Prand([sampler.bufferArray[0], sampler.bufferArray[1], sampler.bufferArray[2],
        sampler.bufferArray[3], sampler.bufferArray[4], sampler.bufferArray[5]], inf));
      sampler.addKey(\voice3, \dur, Prand([8, 8, 8, 8, 3, 6, 6, 6, 4, 4, 4.5, 4.6, 4.5, 4.5, 3], inf));
      sampler.addKey(\voice3, \legato, 1);
      sampler.addKey(\voice3, \amp, 1);
    }.fork;

  }

  //////// public functions:
  free {
    sampler.free;
    filter1.free;
    filter2.free;
    delay.free;
    this.freeModule;
    isLoaded = false;
  }

  playVoice1Sequence { | clock = 'internal ' | sampler.playSequence('voice1'); }
  stopVoice1Sequence { sampler.stopSequence('voice1'); }

  playVoice2Sequence { | clock = 'internal ' | sampler.playSequence('voice2'); }
  stopVoice2Sequence { sampler.stopSequence('voice2'); }

  playVoice3Sequence { | clock = 'internal ' | sampler.playSequence('voice3'); }
  stopVoice3Sequence { sampler.stopSequence('voice3'); }

}