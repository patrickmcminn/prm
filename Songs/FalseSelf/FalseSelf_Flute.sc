/*
Sunday, December 6th 2015
FalseSelf_Flute.sc
prm
Boston, MA
*/

FalseSelf_Flute :IM_Module {

  var server, <isLoaded;
  var <sampler, <vocoderOutBus, splitter;

  *new { | outBus = 0, vocoderOutBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(vocoderOutBus);
  }

  prInit { | vocoderOutBus = 0 |
    var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/FalseSelf/samples/mahler/mahlerFlute.wav";
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      vocoderOutBus = Bus.audio(server, 2);
      server.sync;

      splitter = Splitter.newStereo(2, [mixer.chanStereo(0), vocoderOutBus], relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      sampler = SamplePlayer.newStereo(splitter.inBus, path, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      sampler.setFilterCutoff(6470);
      mixer.setSendVol(0, -6);

      server.sync;
      isLoaded= true;
    };
  }


}