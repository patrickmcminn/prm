/*
Friday, December 4th 2015
FalseSelf_FreezeGtr.sc
prm
Kingston, NY
*/

FalseSelf_FreezeGtr : IM_Module {

  var server, <isLoaded;
  var <sampler, eq;
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

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);

      sampler = Sampler.newStereo(eq.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prMakeNoteDict;
      this.prMakePatterns;

      sampler.setAttackTime(0.25);
      sampler.setReleaseTime(0.5);
      //sampler.setSequencerClockTempo(125);

      // eq parameters:
      eq.setLowFreq(73.2);
      eq.setLowGain(-3);
      eq.setLowPassCutoff(2500);

      mixer.setPreVol(-9);

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
      sampler.makeSequence(\voice1);
      sampler.makeSequence(\voice2);
      sampler.makeSequence(\voice3);
      sampler.makeSequence(\voice4);

      server.sync;

      sampler.addKey(\chordProgression, \legato, 1);
      sampler.addKey(\chordProgression, \dur, Pseq([20, 8, 8, 6, 10, 8, 8, 6, 6, 8, 6, 8], 1));
      //sampler.addKey(\chordProgression, \dur, Pseq([8, 8, 6, 10, 8, 8, 6, 6, 8, 6, 8], 1));
      sampler.addKey(\chordProgression, \buffer, Pseq([
        //[bufferDict[\highCSharp], bufferDict[\midGSharp], bufferDict[\highCSharp2], bufferDict[\highE]],
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

      sampler.addKey(\chordProgression, \amp, 0.6);

      sampler.addKey(\voice1, \legato, 1.1);
      sampler.addKey(\voice1, \buffer, Pseq([bufferDict[\lowGSharp], bufferDict[\midA], bufferDict[\midGSharp]], 1));
      sampler.addKey(\voice1, \dur, Pseq([190, 52, 77], 1));
      sampler.addKey(\voice1, \releaseTime, 3);
      sampler.addKey(\voice1, \amp, 0.4);
      sampler.addKey(\voice1, \pan, Pwhite(-0.75, 0.75));

      sampler.addKey(\voice2, \legato, 1.1);
      sampler.addKey(\voice2, \buffer, Pseq([bufferDict[\midB], bufferDict[\highCSharp2]], 1));
      sampler.addKey(\voice2, \dur, Pseq([208, 132], 1));
      sampler.addKey(\voice2, \releaseTime, 3);
      sampler.addKey(\voice2, \amp, 0.4);
      sampler.addKey(\voice2, \pan, Pwhite(-0.75, 0.75));

      sampler.addKey(\voice3, \legato, 1.1);
      sampler.addKey(\voice3, \buffer, Pseq([bufferDict[\highDSharp2], bufferDict[\highE]], 1));
      sampler.addKey(\voice3, \dur, Pseq([116, 202], 1));
      sampler.addKey(\voice3, \releaseTime, 3);
      sampler.addKey(\voice3, \amp, 0.4);
      sampler.addKey(\voice3, \pan, Pwhite(-0.5, 0.5));

      sampler.addKey(\voice4, \legato, 1.1);
      sampler.addKey(\voice4, \buffer, Pseq(
        [bufferDict[\midGSharp], bufferDict[\midE], bufferDict[\lowFSharp],
          bufferDict[\highCSharp2], bufferDict[\highDSharp],
      ], 1));
      sampler.addKey(\voice4, \dur, Pseq([133, 51, 39, 52, 47], 1));
      sampler.addKey(\voice4, \releaseTime, 3);
      sampler.addKey(\voice4, \amp, 0.4);
      sampler.addKey(\voice4, \pan, Pwhite(-0.75, 0.75));

    }.fork;
  }

  //////// public functions:

  free {
    sampler.free;
    this.freeModule;
    isLoaded = false;
  }

  playNote { | name, vol = 0 | sampler.playSampleSustaining(name, noteDict[name], vol); }
  releaseNote { | name | sampler.releaseSampleSustaining(name); }

  fadeVolume { | start = -inf, end = 0, time = 10 |
    {
      var bus = Bus.control;
      server.sync;
      { Out.kr(bus, Line.kr(start.dbamp, end.dbamp, time, doneAction: 2)) }.play;
      mixer.mapAmp(bus);
      { bus.free }.defer(time);
      { mixer.setVol(end) }.defer(time);
    }.fork;
  }

  playChordProgression { | clock | sampler.playSequence(\chordProgression, clock); }
  stopChordProgression { | clock |sampler.stopSequence(\chordProgression, clock); }

  playEndProgression { | clock |
    sampler.playSequence(\voice1, clock);
    sampler.playSequence(\voice2, clock);
    sampler.playSequence(\voice3, clock);
    sampler.playSequence(\voice4, clock);
  }
  stopEndProgression {
    sampler.stopSequence(\voice1);
    sampler.stopSequence(\voice2);
    sampler.stopSequence(\voice3);
    sampler.stopSequence(\voice4);
  }

}

