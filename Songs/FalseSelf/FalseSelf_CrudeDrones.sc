/*
Friday, December 4th 2015
FalseSelf_CrudeDrones.sc
prm
*/

FalseSelf_CrudeDrones :IM_Module {

  var server, <isLoaded;
  var <sampler;
  var <delay;

  // note: send out to reverb!!

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

      delay = SimpleDelay.newStereo(mixer.chanStereo(0), 0.48, 0.1, 0.5, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newStereo(delay.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;
      this.prMakeSequences;

      mixer.setPreVol(3);
      // reverb send:
      mixer.setSendVol(0, -6);
      // modular send:
      mixer.setSendVol(2, 0);

      isLoaded = true;
    }
  }

  prMakeSequences {
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

  playVoice1Sequence { | clock = 'internal' | sampler.playSequence(\voice1, clock); }
  stopVoice1Sequence { sampler.stopSequence(\voice1); }

  playVoice2Sequence { | clock = 'internal' | sampler.playSequence(\voice2, clock); }
  stopVoice2Sequence { sampler.stopSequence(\voice2); }

  playVoice3Sequence { | clock = 'internal' | sampler.playSequence(\voice3, clock); }
  stopVoice3Sequence { sampler.stopSequence(\voice3); }
}