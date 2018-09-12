/*
Monday, June 19th 2017
Foundation.sc
prm
*/

Foundation : IM_Module {

  var server, <isLoaded;

  var <songBuffers, <songBuffersInput;
  var <moog, <chordSynth;
  var <trumpetSection, <distTrumpets, <endTrumpets;
  var <cleanTrumpet, <cleanTrumpetInput, <trumpet, <trumpetInput;
  var <bassSection, <cellos;
  var <sc, <warps;
  var <clock, <bar, relBar;

  var <introIsPlaying, <arrivalIsPlaying, <mainIsPlaying, <endIsPlaying;

  *new {
    |
    outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead',
    moogDeviceName = "USB Uno MIDI Interface", moogPortName = "USB Uno MIDI Interface"
    |
    ^super.new(11, outBus, send0Bus, send1Bus, send2Bus, send3Bus,  false, relGroup, addAction).prInit(moogDeviceName, moogPortName);
  }

  prInit { | moogDeviceName = "USB Uno MIDI Interface", moogPortName = "USB Uno MIDI Interface" |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      // distorted trumpet gonna be LOUD (so mute it!)
      mixer.tglMute(2);

      this.prSetInitialPlayingConditions;

      //// tempo clock:
      clock = TempoClock.new(96/60);
      server.sync;

      songBuffers = Foundation_SongBuffers.new(group, \addToHead);
      while({ try { songBuffers.isLoaded } != true }, { 0.001.wait; });
      songBuffersInput = IM_HardwareIn.new(0, songBuffers.inBus, group, \addToHead);
      while({ try { songBuffersInput.isLoaded } != true }, { 0.001.wait; });

      // 1 - moog:
      moog = Foundation_Moog.new(3, mixer.chanStereo(0), moogDeviceName, moogPortName, group, \addToHead);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });
      // 2 - Clean Trumpet:
      cleanTrumpet = Foundation_CleanTrumpet.new(mixer.chanStereo(1), group, \addToHead);
      while({ try { cleanTrumpet.isLoaded } != true }, { 0.001.wait; });
      // input:
      cleanTrumpetInput = IM_HardwareIn.new(0, cleanTrumpet.inBus, group, \addToHead);
      while({ try { cleanTrumpetInput.isLoaded } != true }, { 0.001.wait; });
      // 3 - Distorted Trumpet:
      trumpet = FoundationTrumpet.new(mixer.chanStereo(2), 0, relGroup: group, addAction: \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });
      // 4 - bass section:
      bassSection = Foundation_BassSection.new(mixer.chanStereo(3), group, \addToHead);
      while({ try { bassSection.isLoaded } != true }, { 0.001.wait; });
      // 5 - chord synth:
      chordSynth = Foundation_ChordSynth.new(mixer.chanStereo(4), group, \addToHead);
      while({ try { chordSynth.isLoaded } != true }, { 0.001.wait; });
      // 6 - SC:
      sc = Foundation_SC.new(mixer.chanStereo(5), group, \addToHead);
      while({ try { sc.isLoaded } != true }, { 0.001.wait; });
      // 7 - Warps:
      warps = Foundation_Warps.new(mixer.chanStereo(6), group, \addToHead);
      while({ try { warps.isLoaded } != true }, { 0.001.wait; });
      // 8 - end trumpets:
      endTrumpets = Foundation_EndTrumpets.new(mixer.chanStereo(7),
        songBuffers.aBuffer, group, \addToHead);
      while({ try { endTrumpets.isLoaded } != true }, { 0.001.wait; });
      // 9 - trumpet section:
      trumpetSection = Foundation_TrumpetSection.new(mixer.chanStereo(8),
        songBuffers.lowDBuffer, songBuffers.aBuffer, songBuffers.highDBuffer, group, \addToHead);
      while({ try { trumpetSection.isLoaded } != true }, { 0.001.wait; });
      // 10 - distorted trumpets:
      distTrumpets = Foundation_DistTrumpets.new(mixer.chanStereo(9),
        songBuffers.highDBuffer, group, \addToHead);
      while({ try { distTrumpets.isLoaded } != true }, { 0.001.wait; });
      // 11 - cellos:
      cellos = Foundation_Cellos.new(mixer.chanStereo(10), group, \addToHead);
      while({ try { cellos.isLoaded } != true }, { 0.001.wait; });

      server.sync;
      this.prSetInitialParameters;

      relBar = 0;
      clock.schedAbs(clock.beats.ceil, { | beat | bar = (clock.bar - relBar) + 1; 1 });

      isLoaded = true;
    }
  }

  prSetInitialPlayingConditions {
    introIsPlaying = true;
    arrivalIsPlaying = false;
    mainIsPlaying = false;
    endIsPlaying = false;
  }

  prSetInitialParameters {
    //moog:
    mixer.setVol(0, -7);
    mixer.setSendVol(0, 0, -21);
    mixer.setSendVol(0, 1, -16.2);
    mixer.setSendVol(0, 3, -23);

    //chord synth:
    mixer.setVol(4, -3);
    mixer.setSendVol(4, 0, -15);
    mixer.setSendVol(4, 1, -21);

    // sc:
    mixer.setVol(5, -3);
    mixer.setSendVol(5, 0, -15);

    // warps:
    mixer.setVol(6, -3);
    mixer.setSendVol(6, 0, -9);

    // Bass Section:
    mixer.setVol(3, -3);

    // Cellos:
    mixer.setVol(10, -3);
    mixer.setSendVol(10, 0, -15);

    // Clean Trumpet:
    mixer.setVol(1, -12);
    mixer.setPreVol(1, 6);
    mixer.setSendVol(1, 0, -6);
    mixer.setSendVol(1, 1, -9);

    // distorted trumpet:
    trumpet.input.mute;
    mixer.setVol(2, -3);
    mixer.setSendVol(2, 0, -12);
    mixer.setSendVol(2, 1, -15);
    mixer.setSendVol(2, 3, -6);

    // trumpet section:
    mixer.setVol(8, -3);
    mixer.setSendVol(8, 0, -13.6);
    mixer.setSendVol(8, 1, -15);

    // distorted Trumpets:
    mixer.setVol(9, -9);
    mixer.setSendVol(9, 0, -9);
    mixer.setSendVol(9, 1, -15);
    mixer.setSendVol(9, 3, -9);

    // end trumpets:
    mixer.setVol(7, -9);
    mixer.setSendVol(7, 0, -18);
    mixer.setSendVol(7, 1, -inf);
    mixer.setSendVol(7, 3, 0);

  }

  //////// public functions:

  free {
    songBuffers.free; songBuffersInput.free;
    moog.free; chordSynth.free;
    trumpetSection.free; distTrumpets.free; endTrumpets.free;
    cleanTrumpet.free; cleanTrumpetInput.free; trumpet.free; trumpetInput.free;
    bassSection.free; cellos.free;
    sc.free; warps.free;
  }

  //////// song sequencing:
  startSong {
    introIsPlaying = true;
  }

  playMainSequence {
    clock.playNextBar({
      introIsPlaying = false;
      arrivalIsPlaying = true;
      relBar = clock.bar;

      //////// arrival:
      //// levels:
      // mute clean trumpet input:
      cleanTrumpetInput.mute;
      // trumpet section:
      //mixerC.setVol(3, -3);
      // moog:
      moog.eq.setLowPassCutoff(20000);
      moog.eq.setHighPassCutoff(20);

      //// actions:
      // clean trumpet input mute:
      cleanTrumpetInput.mute;
      // moog:
      moog.playMainSection;
      // bass:
      bassSection.playArrivalSequence(clock);
      // trumpet section:
      trumpetSection.playArrivalSequence(clock);
      // distorted trumpets:
      distTrumpets.playArrivalSequence(clock);

      // distorted trumpet kick:
      clock.sched(42, { mixer.setVol(2, -4); });



      //////// main: ////////
      /// (starts 12 bars (48 beats) after arrival ///

      /////////////////////////////
      //// clock management: /////
      ///////////////////////////
      clock.sched(48, { arrivalIsPlaying = false; mainIsPlaying = true });

      //// meter:

      //// levels:
      // moog eq:
      clock.sched(48, { moog.eq.setHighPassCutoff(120); });

      // unmute trumpet!
      clock.sched(46, { trumpet.input.unMute; mixer.unMute(2);  });
      //

      //// actions:
      // chord synth:
      clock.sched(48, { chordSynth.playMainSequence(clock); });
      // bass synth:
      clock.sched(48, { bassSection.playMainSequence(clock); });
      // cellos:
      clock.sched(48, { cellos.playMainSequence(clock); });
      // trumpets:
      clock.sched(48, { trumpetSection.playMainSequence(clock); });

      // trumpet section mud:
      clock.sched(130, {
        mixer.setSendVol(8, 0, -7);
        mixer.setSendVol(8, 1, -9);
        mixer.setSendVol(8, 3, 0);
      });
      clock.sched(150, { mixer.setSendVol(8, 1, 0) });


      //////// this section ends 154 beats after the start of 'arrival' ////////
      clock.sched(154, { mainIsPlaying = false; endIsPlaying = true; });
   });
  }

}