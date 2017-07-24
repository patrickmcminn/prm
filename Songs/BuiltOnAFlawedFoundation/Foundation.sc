/*
Monday, June 19th 2017
Foundation.sc
prm
*/

Foundation : Song {

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
    mixAOutBus, mixBOutBus, mixCOutBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead',
    moogDeviceName = "USB Uno MIDI Interface", moogPortName = "USB Uno MIDI Interface"
    |
    ^super.new(mixAOutBus, 4, mixBOutBus, 2, mixCOutBus, 5, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(moogDeviceName, moogPortName);
  }

  prInit { | moogDeviceName = "USB Uno MIDI Interface", moogPortName = "USB Uno MIDI Interface" |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixerC.isLoaded } != true }, { 0.001.wait; });

      // distorted trumpet gonna be LOUD (so mute it!)
      mixerC.tglMute(1);

      this.prSetInitialPlayingConditions;

      //// tempo clock:
      clock = TempoClock.new(96/60);
      server.sync;

      songBuffers = Foundation_SongBuffers.new(group, \addToHead);
      while({ try { songBuffers.isLoaded } != true }, { 0.001.wait; });
      songBuffersInput = IM_HardwareIn.new(0, songBuffers.inBus, group, \addToHead);
      while({ try { songBuffersInput.isLoaded } != true }, { 0.001.wait; });

      //// Mixer A:
      // moog:
      moog = Foundation_Moog.new(3, mixerA.chanStereo(0), moogDeviceName, moogPortName, group, \addToHead);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });
      // chord synth:
      chordSynth = Foundation_ChordSynth.new(mixerA.chanStereo(1), group, \addToHead);
      while({ try { chordSynth.isLoaded } != true }, { 0.001.wait; });
      // SC:
      sc = Foundation_SC.new(mixerA.chanStereo(2), group, \addToHead);
      while({ try { sc.isLoaded } != true }, { 0.001.wait; });
      // Warps:
      warps = Foundation_Warps.new(mixerA.chanStereo(3), group, \addToHead);
      while({ try { warps.isLoaded } != true }, { 0.001.wait; });

      //// Mixer B:
      // bass section:
      bassSection = Foundation_BassSection.new(mixerB.chanStereo(0), group, \addToHead);
      while({ try { bassSection.isLoaded } != true }, { 0.001.wait; });
      // cellos:
      cellos = Foundation_Cellos.new(mixerB.chanStereo(1), group, \addToHead);
      while({ try { cellos.isLoaded } != true }, { 0.001.wait; });

      //// Mixer C:
      // Clean Trumpet:
      cleanTrumpet = Foundation_CleanTrumpet.new(mixerC.chanStereo(0), group, \addToHead);
      while({ try { cleanTrumpet.isLoaded } != true }, { 0.001.wait; });
      // input:
      cleanTrumpetInput = IM_HardwareIn.new(0, cleanTrumpet.inBus, group, \addToHead);
      while({ try { cleanTrumpetInput.isLoaded } != true }, { 0.001.wait; });
      // Distorted Trumpet:
      trumpet = FoundationTrumpet.new(mixerC.chanStereo(1), 0, relGroup: group, addAction: \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });
      // trumpet section:
      trumpetSection = Foundation_TrumpetSection.new(mixerC.chanStereo(2),
        songBuffers.lowDBuffer, songBuffers.aBuffer, songBuffers.highDBuffer, group, \addToHead);
      while({ try { trumpetSection.isLoaded } != true }, { 0.001.wait; });
      // distorted trumpets:
      distTrumpets = Foundation_DistTrumpets.new(mixerC.chanStereo(3),
        songBuffers.highDBuffer, group, \addToHead);
      while({ try { distTrumpets.isLoaded } != true }, { 0.001.wait; });
      // end trumpets:
      endTrumpets = Foundation_EndTrumpets.new(mixerC.chanStereo(4),
        songBuffers.aBuffer, group, \addToHead);
      while({ try { endTrumpets.isLoaded } != true }, { 0.001.wait; });

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
    mixerA.setVol(0, -7);
    mixerA.setSendVol(0, 0, -21);
    mixerA.setSendVol(0, 1, -16.2);
    mixerA.setSendVol(0, 3, -23);

    //chord synth:
    mixerA.setVol(1, -3);
    mixerA.setSendVol(1, 0, -15);
    mixerA.setSendVol(1, 1, -21);

    // sc:
    mixerA.setVol(2, -3);
    mixerA.setSendVol(2, 0, -15);

    // warps:
    mixerA.setVol(3, -3);
    mixerA.setSendVol(3, 0, -9);

    // Bass Section:
    mixerB.setVol(0, -3);

    // Cellos:
    mixerB.setVol(1, -3);
    mixerB.setSendVol(1, 0, -15);

    // Clean Trumpet:
    mixerC.setVol(0, -12);
    mixerC.setSendVol(0, 0, -6);
    mixerC.setSendVol(0, 1, -9);

    // distorted trumpet:
    trumpet.input.mute;
    mixerC.setVol(1, -3);
    mixerC.setSendVol(1, 0, -12);
    mixerC.setSendVol(1, 1, -15);
    mixerC.setSendVol(1, 3, -6);

    // trumpet section:
    mixerC.setVol(2, -3);
    mixerC.setSendVol(2, 0, -13.6);
    mixerC.setSendVol(2, 1, -15);

    // distorted Trumpets:
    mixerC.setVol(3, -9);
    mixerC.setSendVol(3, 0, -9);
    mixerC.setSendVol(3, 1, -15);
    mixerC.setSendVol(3, 3, -9);

    // end trumpets:
    mixerC.setVol(4, -9);
    mixerC.setSendVol(4, 0, -18);
    mixerC.setSendVol(4, 1, -inf);
    mixerC.setSendVol(4, 3, 0);

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
      clock.sched(42, { mixerC.setVol(3, -4); });



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
      clock.sched(46, { mixerC.unMute(1); trumpet.input.unMute; });
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
        mixerC.setSendVol(2, 0, -7);
        mixerC.setSendVol(2, 1, -9);
        mixerC.setSendVol(2, 3, 0);
      });
      clock.sched(150, { mixerC.setSendVol(2, 1, 0) });


      //////// this section ends 154 beats after the start of 'arrival' ////////
      clock.sched(154, { mainIsPlaying = false; endIsPlaying = true; });
   });
  }

}