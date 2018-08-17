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
    outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead',
    moogDeviceName = "USB Uno MIDI Interface", moogPortName = "USB Uno MIDI Interface"
    |
    ^super.new(8, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(moogDeviceName, moogPortName);
  }

  prInit {
    | moogDeviceName = "USB Uno MIDI Interface", moogPortName = "USB Uno MIDI Interface" |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      clock = TempoClock.new(82/60);


      //// Mixer A:

      // main bell:
      mainBell = Boy_MainBell.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { mainBell.isLoaded } != true }, { 0.001.wait; });

      // row fuzz:
      rowFuzz = Boy_RowFuzz.new(mixer.chanStereo(1), group, \addToHead);
      while({ try { rowFuzz.isLoaded } != true }, { 0.001.wait; });

      // piano:
      piano = Boy_Piano.new(mixer.chanStereo(2), group, \addToHead);
      while({ try { piano.isLoaded } != true }, { 0.001.wait; });

      // bass section:
      bassSection = Boy_BassSection.new(3, mixer.chanStereo(3), "USB Uno MIDI Interface", "USB Uno MIDI Interface");
      while({ try { bassSection.isLoaded } != true }, { 0.001.wait; });

      // noise chords:
      noiseChords = Boy_NoiseChords.new(mixer.chanStereo(4), group, \addToHead);
      while({ try { noiseChords.isLoaded } != true }, { 0.001.wait; });

      // trumpet:
      mixer.mute(4);
      trumpet = Boy_Trumpet.new(mixer.chanStereo(5), group, \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });
      trumpetInput = IM_HardwareIn.new(0, trumpet.inBus, group, \addToHead);
      while({ try { trumpetInput.isLoaded } != true }, { 0.001.wait; });


      // low bells:
      lowBells = Boy_LowBells.new(mixer.chanStereo(6), group, \addToHead);
      while({ try { lowBells.isLoaded } != true }, { 0.001.wait; });



      // end synth:
      endSynth = Boy_EndSynth.new(mixer.chanStereo(7), group, \addToHead);
      while({ try { endSynth.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

      server.sync;
      isLoaded = true;
    };
  }

  prSetInitialParameters {

    // Main Bell
    mixer.setVol(0, -12);
    mixer.setSendVol(0, 0, -1);
    // row fuzz:
    mixer.setVol(1, -12);
    mixer.setSendVol(1, 0, -9);
    // low bells:
    mixer.setVol(6, -12);
    mixer.setSendVol(6, 0, -9);

    // piano:
    mixer.setVol(2, -12);
    mixer.setSendVol(2, 0, -9);
    // bass section:
    mixer.setVol(3, -12);
    mixer.setSendVol(3, 0, -18);

    // trumpet:
    mixer.setVol(5, -21);
    mixer.setSendVol(5, 0, -21);
    // noise chords:
    mixer.setVol(4, -inf);
    mixer.setSendVol(4, 0, -3);
    // end synth:
    mixer.setVol(7, -inf);
    mixer.setSendVol(7, 0, -12);

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