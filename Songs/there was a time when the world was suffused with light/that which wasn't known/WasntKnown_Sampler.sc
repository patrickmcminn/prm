/*
Sunday, January 7th 2018
WasntKnown_Sampler.sc
prm
*/

WasntKnown_Sampler : IM_Module {

  var server, <isLoaded;
  var <sampler, <eq, <splitter, <filter;

  var <startPos, <endPos;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(2, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    var path, sampleArray;
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(1), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newStereo(2, [mixer.chanStereo(0), eq.inBus], false, group, \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      filter = LowPassFilter.newStereo(splitter.inBus, relGroup: group, addAction: \addToHead);
      while({ try { filter.isLoaded } != true }, { 0.001.wait; });

      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/that which wasn't known/samples/";
      sampleArray = (path ++ "*").pathMatch;

      sampler = Sampler.newStereo(filter.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      startPos = 0;
      endPos = 1;

      eq.setHighPassCutoff(1670);
      eq.setHighFreq(3110);
      eq.setHighGain(5.71);
      eq.setHighRQ(0.3268);

      isLoaded = true;
    }
  }

  free {
    sampler.free;
    filter.free;
    splitter.free;
    eq.free;
    isLoaded = false;
    this.freeModule;
  }

  //////// public functions:

  resetToDefault {
    eq.setHighPassCutoff(1670);
    eq.setHighFreq(3110);
    eq.setHighGain(5.71);
    eq.setHighRQ(0.3268);
    this.setFilterCutoff(20000);
    startPos = 0;
    endPos = 1;
  }

  playSample1 { | vol = 0 | sampler.playSampleSustaining('sample1', 0, vol, 1, startPos, endPos); }
  releaseSample1 { sampler.releaseSampleSustaining('sample1'); }

  playSample2 { | vol = 0 | sampler.playSampleSustaining('sample2', 1, vol, 1, startPos, endPos); }
  releaseSample2 { sampler.releaseSampleSustaining('sample2'); }

  playSample3 { | vol = 0 | sampler.playSampleSustaining('sample3', 2, vol, 1, startPos, endPos); }
  releaseSample3 { sampler.releaseSampleSustaining('sample3'); }

  playSample4 { | vol = 0 | sampler.playSampleSustaining('sample4', 3, vol, 1, startPos, endPos); }
  releaseSample4 { sampler.releaseSampleSustaining('sample4'); }

  playSample5 { | vol = 0 | sampler.playSampleSustaining('sample5', 4, vol, 1, startPos, endPos); }
  releaseSample5 { sampler.releaseSampleSustaining('sample5'); }

  playSample6 { | vol = 0 | sampler.playSampleSustaining('sample6', 5, vol, 1, startPos, endPos); }
  releaseSample6 { sampler.releaseSampleSustaining('sample6'); }

  playSample7 { | vol = 0 | sampler.playSampleSustaining('sample7', 6, vol, 1, startPos, endPos); }
  releaseSample7 { sampler.releaseSampleSustaining('sample7'); }

  playSample8 { | vol = 0 | sampler.playSampleSustaining('sample8', 7, vol, 1, startPos, endPos); }
  releaseSample8 { sampler.releaseSampleSustaining('sample8'); }

  playSample9 {| vol = 0 | sampler.playSampleSustaining('sample9', 8, vol, 1, startPos, endPos); }
  releaseSample9 { sampler.releaseSampleSustaining('sample9'); }

  setStartPos { | pos = 0 | startPos = pos; }
  setEndPos { | pos = 1 | endPos = pos; }

  setAttackTime { | attack = 0.05 | sampler.setAttackTime(attack) }
  setReleaseTime { | release = 0.05 | sampler.setReleaseTime(release) }

  setFilterCutoff { | cutoff = 20000 | filter.setCutoff(cutoff); }

}