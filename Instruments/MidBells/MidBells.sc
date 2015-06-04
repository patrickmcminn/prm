/*
Thursday, June 2nd 2015
MidBells.sc
Lyon, France
*/

MidBells : IM_Module {


  var <isLoaded;
  var server;
  var sampler;
  var sequencerDict;

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
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;
      path = "~/Library/Application Support/SuperCollider/Extensions/prm/Instruments/MidBells/samples/";
      sampleArray = (path ++ "*").pathMatch;
      sampler = Sampler.newStereo(mixer.chanStereo(0), sampleArray, relGroup: group, addAction: \addToHead);
      server.sync;
      isLoaded = true;
    };
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

}