/*
Friday, December 4th 2015
FalseSelf_FreezeGtr.sc
prm
Kingston, NY
*/

FalseSelf_FreezeGtr : IM_Module {

  var server, <isLoaded;
  var <sampler, <reverb;
  var noteDict;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      var path = "~/Library/Application Support/SuperCollider/Extensions/prm/Songs/FalseSelf/samples/guitars/";
      var sampleArray = (path ++ "*").pathMatch;
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      reverb = IM_Reverb.new(mixer.chanStereo(0), mix: 0.65, roomSize: 0.92, damp: 0.9,
        relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newStereo(reverb.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prMakeNoteDict;
      this.prMakePatterns;

      sampler.setAttackTime(0.5);
      sampler.setReleaseTime(3);

      isLoaded = true;
    };
  }

  prMakeNoteDict {
    noteDict = IdentityDictionary.new;
    noteDict[\lowE] = 0;
    noteDict[\lowFSharp] = 1;
    noteDict[\lowGSharp] = 2;
    noteDict[\lowA] = 3;
    noteDict[\lowB] = 4;
    noteDict[\midC] = 5;
    noteDict[\midDSharp] = 6;
    noteDict[\midE] = 7;
    noteDict[\midE2] = 8;
    noteDict[\midFSharp] = 9;
    noteDict[\midG] = 10;
    noteDict[\midGSharp] = 11;
    noteDict[\midA] = 12;
    noteDict[\midB] = 13;
    noteDict[\highC] = 14;
    noteDict[\highCSharp] = 15;
    noteDict[\highCSharp2] = 16;
    noteDict[\highDSharp] = 17;
    noteDict[\highDSharp2] = 18;
    noteDict[\highE] = 19;
  }

  prMakePatterns { }

  //////// public functions:

  free {
    sampler.free;
    reverb.free;
    this.freeModule;
  }

  playNote { | name, vol = 0 |
    sampler.playSampleSustaining(name, noteDict[name], vol);
  }
  releaseNote { | name |
    sampler.releaseSampleSustaining(name);
  }

}

