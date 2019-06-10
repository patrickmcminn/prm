/*
Friday, June 9th 2019
WhereTheBirds_DistChords.sc
prm
*/

WhereTheBirds_DistChords : IM_Module {

  var <isLoaded, server;
  var <distortion, <lfo, <input;

  *new { | outBus, inBus, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(inBus);
  }

  prInit { | inBus |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      distortion = Distortion2.newMono(mixer.chanStereo, 0.3, 0.3, relGroup: group, addAction: \addToHead);
      while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

      input = IM_HardwareIn.new(inBus, distortion.inBus, group, \addToHead);
      while({ try { input.isLoaded } != true }, { 0.001.wait; });

      lfo = LFO.new(5, 'sine', 0.25, 1, group, \addToHead);
      while({ try { lfo.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      lfo.setWaveform(4.5);
      mixer.mapAmp(lfo.outBus);
      //mixer.setPreVol(-6);
      distortion.postEQ.setLowPassCutoff(1500);
      distortion.postEQ.setHighPassCutoff(100);

      server.sync;

      isLoaded = true;
    }
  }

  /////// public functions:

  free {
    lfo.free;
    input.free;
    distortion.free;
    this.freeModule
  }


}