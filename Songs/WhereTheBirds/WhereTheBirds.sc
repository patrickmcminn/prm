/*
Sunday, June 9th 2019
WhereTheBirds.sc
prm


*/

WhereTheBirds : IM_Module {

  var <isLoaded, server;
  var <clock, <modularClock;
  var <bed, <chords, <distChords;
  var <bass, <noiseSynth;
  var <noise, <mic;

  *new {
    |
    outBus = 0, micIn, moogIn, chordsIn, noiseIn,
    pitchOut, chordOut, invOut, clockOut, moogDevice, moogPort,
    send0Bus, send1Bus, send2Bus, send3Bus,
    relGroup, addAction = 'addToHead'
    |
    ^super.new(7, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(
      micIn, moogIn, chordsIn, noiseIn,pitchOut, chordOut, invOut, clockOut, moogDevice, moogPort);
  }

  prInit { | micIn, moogIn, chordsIn, noiseIn,pitchOut, chordOut, invOut, clockOut, moogDevice, moogPort |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      mixer.masterChan.mute;

      clock = TempoClock.new(60/60);

      modularClock = ModularClock.new(clockOut, 96, 24, group, 'addToHead');
      while({ try { modularClock.isLoaded } != true }, { 0.001.wait; });

      // 1 -- bed:
      bed = WhereTheBirds_Bed.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { bed.isLoaded } != true }, { 0.001.wait; });

      // 2 -- chords:
      chords = WhereTheBirds_Chords.new(mixer.chanStereo(1), pitchOut, chordOut, invOut, chordsIn,
        group, \addToHead);
      while({ try { chords.isLoaded } != true }, { 0.001.wait; });

      // 3 -- Dist Chords:
      distChords = WhereTheBirds_DistChords.new(mixer.chanStereo(2), chordsIn, group, \addToHead);
      while({ try { distChords.isLoaded } != true }, { 0.001.wait; });

      // 4 -- Bass:
      bass = WhereTheBirds_Bass.new(mixer.chanStereo(3), moogIn, moogDevice, moogPort, group, \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });

      // 5 -- Noise Synth:
      noiseSynth = WhereTheBirds_NoiseSynth.new(mixer.chanStereo(4), group, \addToHead);
      while({ try { noiseSynth.isLoaded } != true }, { 0.001.wait; });

      // 6 -- Noise:
      noise = IM_HardwareIn.new(noiseIn, mixer.chanMono(5), group, \addToHead);
      while({ try { noise.isLoaded } != true }, { 0.001.wait; });

      // 7 -- mic:
      mic = WhereTheBirds_Mic.new(mixer.chanStereo(6), micIn, group, \addToHead);
      while({ try { mic.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

      mixer.masterChan.unMute;
      isLoaded = true;
    }
  }

  prSetInitialParameters {
    // bed:
    mixer.setVol(0, -6);
    mixer.setSendVol(0, 0, 0);
    // chords:
    mixer.mute(1);
    mixer.setVol(1, -15);
    mixer.setSendVol(1, 0, -9);
    // noise chords:
    mixer.mute(2);
    mixer.setVol(1, -inf);
    mixer.setSendVol(2, 0, -15);
    // bass:
    mixer.setVol(3, -9);
    mixer.setSendVol(3, 0, -24);
    mixer.setPreVol(3, 3);
    // noise synth:
    mixer.setVol(4, -3);
    mixer.setSendVol(4, 0, -6);
    // noise:
    mixer.mute(5);
    mixer.setVol(5, -inf);
    mixer.setSendVol(5, 0, -12);
    // mic:
    //mixer.mute(6);
    mic.input.mute;
    mixer.setPreVol(6, 3);
    mixer.setSendVol(6, 0, -3);

  }

  free {
    clock.stop;
    clock.free;
    modularClock.stop;
    modularClock.free;
    bed.free;
    chords.free;
    distChords.free;
    bass.free;
    noiseSynth.free;
    noise.free;
    mic.free;
    this.freeModule;
    isLoaded = false;
  }
}