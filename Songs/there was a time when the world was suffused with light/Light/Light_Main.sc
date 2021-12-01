/*
Saturday, April 22nd 2017
Light_Main.sc
prm
*/

Light_Main : IM_Module {

  var server, <isLoaded;
  var <sampler;
  var <splitter, <shiftMixer, <multiShift, <lfo;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    var path, sampleArray;
    server = Server.default;
    server.waitForBoot{
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/Light/Samples/Loops/";
      sampleArray = (path ++ "*").pathMatch;

      /*
      lfo = LFO.new(10, 'noise', 0, 1, group, 'addToHead');
      while({ try { lfo.isLoaded } != true }, { 0.001.wait; });

      shiftMixer = IM_Mixer_1Ch.newNoSends(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { shiftMixer.isLoaded } != true }, { 0.001.wait; });

      multiShift = IM_MultiShift.newStereo(shiftMixer.inBus, [0.5, 3/2, 2, 2.5, 3, 3.5, 4], 0, group, 'addToHead');
      while({ try { multiShift.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      multiShift.mixer.mapAmp(0, lfo.outBus);
      multiShift.mixer.mapAmp(1, lfo.outBus);
      multiShift.mixer.mapAmp(2, lfo.outBus);
      multiShift.mixer.mapAmp(3, lfo.outBus);
      multiShift.mixer.mapAmp(4, lfo.outBus);
      multiShift.mixer.mapAmp(5, lfo.outBus);
      multiShift.mixer.mapAmp(6, lfo.outBus);

      splitter = Splitter.newStereo(2, [mixer.chanStereo(0), multiShift.inBus],
        relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });
      */

      sampler = Sampler.newStereo(mixer.chanStereo(0), sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

      isLoaded = true;
    }
  }

  prSetInitialParameters {
    mixer.setPreVol(-18);
    //shiftMixer.setVol(-9);
  }

  ///////// public functions:

  free {
    sampler.free;
    this.freeModule;
    isLoaded = false;
  }

  playLoop1 { | vol = 0 | sampler.playSampleSustaining(\loop1, 0, vol); }
  releaseLoop1 { sampler.releaseSampleSustaining(\loop1); }

  playLoop2 { | vol = 0 | sampler.playSampleSustaining(\loop2, 1, vol); }
  releaseLoop2 { sampler.releaseSampleSustaining(\loop2); }

  playLoop3 { | vol = 0 | sampler.playSampleSustaining(\loop3, 2, vol); }
  releaseLoop3 { sampler.releaseSampleSustaining(\loop3); }

  playLoop4 { | vol = 0 | sampler.playSampleSustaining(\loop4, 3, vol); }
  releaseLoop4 { sampler.releaseSampleSustaining(\loop4); }

  playLoop1Rev { | vol = 0 | sampler.playSampleSustaining(\loop1Rev, 4, vol); }
  releaseLoop1Rev { sampler.releaseSampleSustaining(\loop1Rev); }

  playLoop2Rev { | vol = 0 | sampler.playSampleSustaining(\loop2Rev, 5, vol); }
  releaseLoop2Rev { sampler.releaseSampleSustaining(\loop2Rev); }

  playLoop3Rev { | vol = 0 | sampler.playSampleSustaining(\loop3Rev, 6, vol); }
  releaseLoop3Rev { sampler.releaseSampleSustaining(\loop3Rev); }

  playLoop4Rev { | vol = 0 | sampler.playSampleSustaining(\loop4Rev, 7, vol); }
  releaseLoop4Rev { sampler.releaseSampleSustaining(\loop4Rev); }

  playLoop1Half { | vol = 0 | sampler.playSampleSustaining(\loop1Half, 0, vol, 0.5); }
  releaseLoop1Half {  sampler.releaseSampleSustaining(\loop1Half); }

  setFilterCutoff { | cutoff = 150 | sampler.setFilterCutoff(cutoff); }

}