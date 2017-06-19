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
      // warps:
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

      isLoaded = true;
    }
  }

  prSetInitialPlayingConditions {
    introIsPlaying = true;
    arrivalIsPlaying = false;
    mainIsPlaying = false;
    endIsPlaying = false;
  }

}