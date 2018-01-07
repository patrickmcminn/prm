/*
Tuesday, July 25th 2017
TheBoy.sc
prm
*/

TheBoy : Song {

  var server, <isLoaded;

  var <clock;

  var <introIsPlaying, <section1IsPlaying, <lullIsPlaying, <section2IsPlaying, <section3IsPlaying, <outroIsPlaying;

  var <mainBell, <rowFuzz, <lowBells;
  var <piano, <bassSection;
  var <trumpet, <trumpetInput, <noiseChords, <endSynth;

  *new {
    |
    mixAOutBus, mixBOutBus, mixCOutBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead',
    moogDeviceName = "USB Uno MIDI Interface", moogPortName = "USB Uno MIDI Interface"
    |
    ^super.new(mixAOutBus, 3, mixBOutBus, 2, mixCOutBus, 3, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(moogDeviceName, moogPortName);
  }

  prInit {
    | moogDeviceName = "USB Uno MIDI Interface", moogPortName = "USB Uno MIDI Interface" |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixerC.isLoaded } != true }, { 0.001.wait; });

      clock = TempoClock.new(82/60);
      mixerC.mute(0);

      //// Mixer A:

      // main bell:
      mainBell = Boy_MainBell.new(mixerA.chanStereo(0), group, \addToHead);
      while({ try { mainBell.isLoaded } != true }, { 0.001.wait; });

      // row fuzz:
      rowFuzz = Boy_RowFuzz.new(mixerA.chanStereo(1), group, \addToHead);
      while({ try { rowFuzz.isLoaded } != true }, { 0.001.wait; });

      // low bells:
      lowBells = Boy_LowBells.new(mixerA.chanStereo(2), group, \addToHead);
      while({ try { lowBells.isLoaded } != true }, { 0.001.wait; });

      //// Mixer B:

      // piano:
      piano = Boy_Piano.new(mixerB.chanStereo(0), group, \addToHead);
      while({ try { piano.isLoaded } != true }, { 0.001.wait; });

      // bass section:
      bassSection = Boy_BassSection.new(3, mixerB.chanStereo(1), "USB Uno MIDI Interface", "USB Uno MIDI Interface");
      while({ try { bassSection.isLoaded } != true }, { 0.001.wait; });

      //// Mixer C:

      // trumpet:
      trumpet = Boy_Trumpet.new(mixerC.chanStereo(0), group, \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });
      trumpetInput = IM_HardwareIn.new(0, trumpet.inBus, group, \addToHead);
      while({ try { trumpetInput.isLoaded } != true }, { 0.001.wait; });

      // noise chords:
      noiseChords = Boy_NoiseChords.new(mixerC.chanStereo(1), group, \addToHead);
      while({ try { noiseChords.isLoaded } != true }, { 0.001.wait; });

      // end synth:
      endSynth = Boy_EndSynth.new(mixerC.chanStereo(2), group, \addToHead);
      while({ try { endSynth.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

      server.sync;
      isLoaded = true;
    };
  }

  prSetInitialParameters {
    //// Mixer A:
    3.do({ | i | mixerA.setPreVol(i, -6); });

    // Main Bell
    mixerA.setVol(0, -9);
    mixerA.setSendVol(0, 0, -15);
    // row fuzz:
    mixerA.setVol(1, -9);
    mixerA.setSendVol(1, 0, -9);
    // low bells:
    mixerA.setVol(2, -9);
    mixerA.setSendVol(2, 0, -9);

    //// Mixer B:
    2.do({ | i | mixerB.setPreVol(i, -6); });
    // piano:
    mixerB.setVol(0, -inf);
    mixerB.setSendVol(0, 0, -9);
    // bass section:
    mixerB.setVol(1, -6);
    mixerB.setSendVol(1, 0, -18);

    //// Mixer C:
    3.do({ | i | mixerC.setPreVol(i, -6); });
    // trumpet:
    mixerC.setVol(0, -15);
    mixerC.setSendVol(0, 0, -21);
    // noise chords:
    mixerC.setVol(1, -inf);
    mixerC.setSendVol(1, 0, -3);
    // end synth:
    mixerC.setVol(2, -inf);
    mixerC.setSendVol(2, 0, -12);

    introIsPlaying = true;
    section1IsPlaying = false;
    lullIsPlaying = false;
    section2IsPlaying = false;
    section3IsPlaying = false;
    outroIsPlaying = false;

  }

  //////// public functions:
  free {
    mainBell.free; rowFuzz.free; lowBells.free;
    piano.free; bassSection.free;
    trumpet.free; noiseChords.free; endSynth.free;
    this.freeSong;
    isLoaded = false;
  }

  playSection1 {
    clock.playNextBar {
      introIsPlaying = false;
      section1IsPlaying = true;
      lullIsPlaying = false;
      section2IsPlaying = false;
      section3IsPlaying = false;
      outroIsPlaying = false;

      piano.playSection1(clock);
      noiseChords.playSection1(clock);
    }
  }

  loopSection1 {
    clock.playNextBar {
      trumpet.looper.loop;
      clock.sched(32, {
        trumpet.looper.loop;
        trumpet.looper.stopLoop;
        this.playLull;
      });
    }
  }

  playLull {
    clock.playNextBar {
      introIsPlaying = false;
      section1IsPlaying = false;
      lullIsPlaying = true;
      section2IsPlaying = false;
      section3IsPlaying = false;
      outroIsPlaying = false;

      piano.stopSection1;
      noiseChords.stopSection1;
      rowFuzz.stopMainRowFuzz;
      rowFuzz.playLowRowFuzz(clock);
      mainBell.stopMainBell;
    }
  }

  playSection2 {
    rowFuzz.playMainRowFuzz(clock);
    clock.playNextBar {
      introIsPlaying = false;
      section1IsPlaying = false;
      lullIsPlaying = false;
      section2IsPlaying = true;
      section3IsPlaying = false;
      outroIsPlaying = false;

      piano.playSection2(clock);
      noiseChords.playSection2(clock);
      bassSection.playSection2(clock);

      mainBell.playMainBell(clock);

      trumpet.looper.playLoop;
    };
  }

  playSection3 {
    clock.playNextBar {
      introIsPlaying = false;
      section1IsPlaying = false;
      lullIsPlaying = false;
      section2IsPlaying = false;
      section3IsPlaying = true;
      outroIsPlaying = false;

      trumpet.looper.stopLoop;

      piano.stopSection2;
      noiseChords.stopSection2;
      bassSection.stopSection2;

      piano.playSection3(clock);
      noiseChords.playSection3(clock);
      bassSection.playSection3(clock);

      mainBell.stopMainBell;

      endSynth.playEndLoop(clock);
    }

  }

}