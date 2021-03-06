/*
Thursday, June 2nd 2015
MidBells.sc
Lyon, France
*/

MidBells : IM_Module {


  var <isLoaded;
  var server;
  var <sampler;

  var <attackTime, <decayTime, <sustainLevel, <releaseTime;

  var <sequencerDict, <sequencerClock, <tempo;


  *new {
    |
    outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    var path, sampleArray;
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      sequencerDict = IdentityDictionary.new;

      this.prAddSynthDef;

      attackTime = 0.05;
      decayTime = 0;
      sustainLevel = 1;
      releaseTime = 0.05;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;
      path = "~/Library/Application Support/SuperCollider/Extensions/prm/Instruments/MidBells/samples/";
      sampleArray = (path ++ "*").pathMatch;
      sampler = Sampler.newStereo(mixer.chanStereo(0), sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      sequencerDict = IdentityDictionary.new;
      sequencerClock = TempoClock.new;

      server.sync;
      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\prm_MidBells_LFO, {
      | outBus = 0, freq = 1, waveForm = 1 |
    }).add
  }

  //////// public functions:

  free {
    sampler.free;
    server = nil;
    isLoaded = false;
    this.freeModule;
  }

  // notes:
  /*
  0 - 60
  1 - 61
  2 - 62
  3 - 63
  4 - 64
  5 - 65
  6 - 66
  7 - 67
  8 - 68
  9 - 69
  10 - 70
  11 - 71
  12 - 72
  13 - 73
  14 - 74
  */

  playNote { | freq = 440, vol = 0, attackTime = 0.05, startPos = 0, endPos = 1, pan = 0 |
    var note = freq.cpsmidi;
    sampler.setAttackTime(attackTime);
    case
    { note <= 60 } { sampler.playSampleOneShot(0, vol, (note - 60).midiratio, startPos, endPos); }
    { note > 60 && note <= 61 } { sampler.playSampleOneShot(1, vol, (note - 61).midiratio, startPos, endPos, pan); }
    { note > 61 && note <= 62 } { sampler.playSampleOneShot(2, vol, (note - 62).midiratio, startPos, endPos, pan); }
    { note > 62 && note <= 63 } { sampler.playSampleOneShot(3, vol, (note - 63).midiratio, startPos, endPos, pan); }
    { note > 63 && note <= 64 } { sampler.playSampleOneShot(4, vol, (note - 64).midiratio, startPos, endPos, pan); }
    { note > 64 && note <= 65 } { sampler.playSampleOneShot(5, vol, (note - 65).midiratio, startPos, endPos, pan); }
    { note > 65 && note <= 66 } { sampler.playSampleOneShot(6, vol, (note - 66).midiratio, startPos, endPos, pan); }
    { note > 66 && note <= 67 } { sampler.playSampleOneShot(7, vol, (note - 67).midiratio, startPos, endPos, pan); }
    { note > 67 && note <= 68 } { sampler.playSampleOneShot(8, vol, (note - 68).midiratio, startPos, endPos, pan); }
    { note > 68 && note <= 69 } { sampler.playSampleOneShot(9, vol, (note - 69).midiratio, startPos, endPos, pan); }
    { note > 69 && note <= 70 } { sampler.playSampleOneShot(10, vol, (note - 70).midiratio, startPos, endPos, pan); }
    { note > 70 && note <= 71 } { sampler.playSampleOneShot(11, vol, (note - 71).midiratio, startPos, endPos, pan); }
    { note > 71 && note <= 72 } { sampler.playSampleOneShot(12, vol, (note - 72).midiratio, startPos, endPos, pan); }
    { note > 72 && note <= 73 } { sampler.playSampleOneShot(13, vol, (note - 73).midiratio, startPos, endPos, pan); }
    { note > 73 } { sampler.playSampleOneShot(14, vol, (note - 74).midiratio, startPos, endPos); }
  }

  playLoopingNote { | freq = 440, vol = 0, startPos = 0, endPos = 1, pan = 0 |
    var note = freq.cpsmidi;
    sampler.setAttackTime(attackTime);
    case
    { note <= 60 } { sampler.playSampleSustaining(freq.asSymbol, 0, vol, (note - 60).midiratio, startPos, endPos); }
    { note > 60 && note <= 61 } { sampler.playSampleSustaining(freq.asSymbol,1, vol, (note - 61).midiratio,
      startPos, endPos, pan); }
    { note > 61 && note <= 62 } { sampler.playSampleSustaining(freq.asSymbol,2, vol, (note - 62).midiratio,
      startPos, endPos, pan); }
    { note > 62 && note <= 63 } { sampler.playSampleSustaining(freq.asSymbol,3, vol, (note - 63).midiratio,
      startPos, endPos, pan); }
    { note > 63 && note <= 64 } { sampler.playSampleSustaining(freq.asSymbol,4, vol, (note - 64).midiratio,
      startPos, endPos, pan); }
    { note > 64 && note <= 65 } { sampler.playSampleSustaining(freq.asSymbol,5, vol, (note - 65).midiratio,
      startPos, endPos, pan); }
    { note > 65 && note <= 66 } { sampler.playSampleSustaining(freq.asSymbol,6, vol, (note - 66).midiratio,
      startPos, endPos, pan); }
    { note > 66 && note <= 67 } { sampler.playSampleSustaining(freq.asSymbol,7, vol, (note - 67).midiratio,
      startPos, endPos, pan); }
    { note > 67 && note <= 68 } { sampler.playSampleSustaining(freq.asSymbol,8, vol, (note - 68).midiratio,
      startPos, endPos, pan); }
    { note > 68 && note <= 69 } { sampler.playSampleSustaining(freq.asSymbol,9, vol, (note - 69).midiratio,
      startPos, endPos, pan); }
    { note > 69 && note <= 70 } { sampler.playSampleSustaining(freq.asSymbol,10, vol, (note - 70).midiratio,
      startPos, endPos, pan); }
    { note > 70 && note <= 71 } { sampler.playSampleSustaining(freq.asSymbol,11, vol, (note - 71).midiratio,
      startPos, endPos, pan); }
    { note > 71 && note <= 72 } { sampler.playSampleSustaining(freq.asSymbol,12, vol, (note - 72).midiratio,
      startPos, endPos, pan); }
    { note > 72 && note <= 73 } { sampler.playSampleSustaining(freq.asSymbol,13, vol, (note - 73).midiratio,
      startPos, endPos, pan); }
    { note > 73 } { sampler.playSampleSustaining(freq.asSymbol,14, vol, (note - 74).midiratio, startPos, endPos); }
  }

  releaseLoopingNote { | freq | sampler.releaseSampleSustaining(freq.asSymbol); }

  playDriftingNote {
    |freq = 440, vol = 0, posFreq = 3, startPosLow = 0, startPosHigh = 0.2, endPosLow = 0.03, endPosHigh = 0.09, pan = 0|
    var note = freq.cpsmidi;
    sampler.setAttackTime(attackTime);
    case
    { note <= 60 } { sampler.playSampleDrifting(freq.asSymbol, 0, vol, (note - 60).midiratio, posFreq, startPosLow,
      startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 60 && note <= 61 } { sampler.playSampleDrifting(freq.asSymbol,1, vol, (note - 61).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 61 && note <= 62 } { sampler.playSampleDrifting(freq.asSymbol,2, vol, (note - 62).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 62 && note <= 63 } { sampler.playSampleDrifting(freq.asSymbol,3, vol, (note - 63).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 63 && note <= 64 } { sampler.playSampleDrifting(freq.asSymbol,4, vol, (note - 64).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 64 && note <= 65 } { sampler.playSampleDrifting(freq.asSymbol,5, vol, (note - 65).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 65 && note <= 66 } { sampler.playSampleDrifting(freq.asSymbol,6, vol, (note - 66).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 66 && note <= 67 } { sampler.playSampleDrifting(freq.asSymbol,7, vol, (note - 67).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 67 && note <= 68 } { sampler.playSampleDrifting(freq.asSymbol,8, vol, (note - 68).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 68 && note <= 69 } { sampler.playSampleDrifting(freq.asSymbol,9, vol, (note - 69).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 69 && note <= 70 } { sampler.playSampleDrifting(freq.asSymbol,10, vol, (note - 70).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 70 && note <= 71 } { sampler.playSampleDrifting(freq.asSymbol,11, vol, (note - 71).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 71 && note <= 72 } { sampler.playSampleDrifting(freq.asSymbol,12, vol, (note - 72).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 72 && note <= 73 } { sampler.playSampleDrifting(freq.asSymbol,13, vol, (note - 73).midiratio,
      posFreq, startPosLow, startPosHigh, endPosLow, endPosHigh, pan); }
    { note > 73 } { sampler.playSampleDrifting(freq.asSymbol,14, vol, (note - 74).midiratio, posFreq, startPosLow,
      startPosHigh, endPosLow, endPosHigh, pan); }
  }

  releaseDriftingNote { | freq | sampler.releaseSampleDrifting(freq.asSymbol); }

  setAttackTime { | attack = 0.05 |
    attackTime = attack;
    sampler.setAttackTime(attackTime);
  }
  setDecayTime { | decay = 0 |
    decayTime = decay;
    sampler.setDecayTime(decayTime);
  }
  setSustainLevel { | sustain = 0 |
    sustainLevel = sustain;
    sampler.setSustainLevel(sustainLevel);
  }
  setReleaseTime { | release = 0.05 |
    releaseTime = release;
    sampler.setReleaseTime(releaseTime);
  }

//////// sequencer:

  makeSequence { | name |
    fork {
      sampler.makeSequence(name, 'oneShot');
      server.sync;
      sampler.addKey(name, \attackTime, Pfunc({ attackTime }));
      sampler.addKey(name, \decayTime, Pfunc({ decayTime }));
      sampler.addKey(name, \sustainLevel, Pfunc({ sustainLevel }));
      sampler.addKey(name, \releaseTime, Pfunc({ releaseTime }));
      //sampler.addKey(name, \freq, Pfunc{ | ev | ev.use(ev[\freq]) });
      //sampler.addKey(name, \buffer, Pfunc({ | ev | sampler.bufferArray[ev[\freq].cpsmidi -60] }));
      //sampler.addKey(name, \printer, Pfunc({ | ev | ev[\freq].cpsmidi.postln; }) );
    };
  }

  addKey {  | name, key, action |
    sampler.addKey(name, key, action);
  }

  playSequence { | name, clock = 'internal', quant = 'nil' |
    sampler.playSequence(name, clock, quant);
  }


  resetSequence { | name | sampler.resetSequence(name); }
  stopSequence { | name | sampler.stopSequence(name); }
  pauseSequence { | name | sampler.pauseSequence(name); }
  resumeSequence { | name | sampler.resumeSequence(name); }
  isSequencePlaying { | name |^sampler.isSequencePlaying }
  setSequenceQuant { | name, quant = 0 | sampler.setSequenceQuant(name, quant); }

  setSequencerClockTempo { | bpm = 60 |
    sampler.setSequencerClockTempo(bpm);
  }

}
