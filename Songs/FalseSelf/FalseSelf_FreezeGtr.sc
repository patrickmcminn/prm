/*
Friday, December 4th 2015
FalseSelf_FreezeGtr.sc
prm
Kingston, NY
*/

FalseSelf_FreezeGtr : IM_Module {

  var server, <isLoaded;
  var <sampler, <reverb;
  var noteDict, bufferDict;

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

    bufferDict = IdentityDictionary.new;
    bufferDict[\lowE] = sampler.bufferArray[0];
    bufferDict[\lowFSharp] = sampler.bufferArray[1];
    bufferDict[\lowGSharp] = sampler.bufferArray[2];
    bufferDict[\lowA] = sampler.bufferArray[3];
    bufferDict[\lowB] = sampler.bufferArray[4];
    bufferDict[\midC] = sampler.bufferArray[5];
    bufferDict[\midDSharp] = sampler.bufferArray[6];
    bufferDict[\midE] = sampler.bufferArray[7];
    bufferDict[\midE2] = sampler.bufferArray[8];
    bufferDict[\midFSharp] = sampler.bufferArray[9];
    bufferDict[\midG] = sampler.bufferArray[10];
    bufferDict[\midGSharp] = sampler.bufferArray[11];
    bufferDict[\midA] = sampler.bufferArray[12];
    bufferDict[\midB] = sampler.bufferArray[13];
    bufferDict[\highC] = sampler.bufferArray[14];
    bufferDict[\highCSharp] = sampler.bufferArray[15];
    bufferDict[\highCSharp2] = sampler.bufferArray[16];
    bufferDict[\highDSharp] = sampler.bufferArray[17];
    bufferDict[\highDSharp2] = sampler.bufferArray[18];
    bufferDict[\highE] = sampler.bufferArray[19];
  }

  prMakePatterns {
    {
      sampler.makeSequence(\chordProgression, 'sustaining');
      server.sync;
      sampler.addKey(\legato, 1);
      sampler.addKey(\dur, Pseq([20, 8, 8, 6, 10, 8, 8, 6, 6, 8, 6, 8], 1));
      sampler.addKey(\buffer, Pseq([
        [bufferDict[\highCSharp], bufferDict[\midGSharp], bufferDict[\highCSharp2], bufferDict[\highE]],
        [bufferDict[\highCSharp], bufferDict[\midGSharp], bufferDict[\highCSharp2], bufferDict[\highE]],
        [bufferDict[\midC], bufferDict[\midG], bufferDict[\highC], bufferDict[\highE]],
        [bufferDict[\highCSharp], bufferDict[\midGSharp], bufferDict[\highCSharp2], bufferDict[\highE]],
        [bufferDict[\lowGSharp], bufferDict[\midDSharp], bufferDict[\highC], bufferDict[\highDSharp]],
        [bufferDict[\lowA], bufferDict[\midE], bufferDict[\highCSharp], bufferDict[\midA]],
        [bufferDict[\highCSharp], bufferDict[\midGSharp], bufferDict[\highCSharp2], bufferDict[\highE]],
        [bufferDict[\lowA], bufferDict[\midE], bufferDict[\highCSharp], bufferDict[\midA]],
        [bufferDict[\lowGSharp], bufferDict[\midDSharp], bufferDict[\highC], bufferDict[\highDSharp]],
        [bufferDict[\lowFSharp], bufferDict[\highCSharp], bufferDict[\midA], bufferDict[\midFSharp]],
        [bufferDict[\lowE], bufferDict[\lowB], bufferDict[\midE], bufferDict[\midGSharp]],
        [bufferDict[\lowB], bufferDict[\midB], bufferDict[\highDSharp2], bufferDict[\midFSharp]]
      ], 1));
    }.fork;
  }

  //////// public functions:

  free {
    sampler.free;
    reverb.free;
    this.freeModule;
  }

  playNote { | name, vol = 0 | sampler.playSampleSustaining(name, noteDict[name], vol); }
  releaseNote { | name | sampler.releaseSampleSustaining(name); }
}

