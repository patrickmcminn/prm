/*
Tuesday, December 3rd 2015
FalseSelf_BassSection.sc
prm
driving from Portland, ME to Ithaca, NY
*/

FalseSelf_BassSection :IM_Module {

  var server, <isLoaded;
  var <satur, <feedback, <guitar, <moog;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead',
    moogDeviceName = "iConnectAudio4+", moogPortName = "DIN" |
    ^super.new(4, outBus, send0Bus, send1Bus, send3Bus, send3Bus, false, relGroup, addAction).prInit(
      moogDeviceName, moogPortName);
  }

  prInit { | moogDeviceName = "iConnectAudio4+", moogPortName = "DIN" |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      satur = SaturSynth.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { satur.isLoaded } != true }, { 0.001.wait; });

      feedback = FeedbackSynth.new(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { feedback.isLoaded } != true },  { 0.001.wait; });

      guitar = FakeGuitar.new(mixer.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { guitar.isLoaded } != true }, { 0.001.wait; });

      moog = Mother32.new(3, mixer.chanStereo(3), moogDeviceName, moogPortName, 1, 1,
        relGroup: group, addAction: \addToHead);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });



      server.sync;

      this.prMakeMoogPattern;
      guitar.setFilterCutoff(238);

      server.sync;

      isLoaded = true;
    }
  }

  //////// public:
  free {
    guitar.free;
    feedback.free;
    satur.free;
    this.freeModule;
  }

  prMakeMoogPattern {
    {
      var part1Note, part1Dur, part2Note, part2Dur, part3Note, part3Dur;

      moog.makeSequence('bassline');

      server.sync;

      part1Note = Pseq([1], 1);
      part1Dur = 56;
      part2Note = Pseq([6, 5, 6, 5, 6, 8, 6, 13, 6, 5], 2);
      part2Dur = Pseq([6, 8, 6, 6, 6, 8, 2, 2, 2, 6], 2);
      part3Note = Pseq([1], 1);
      part3Dur = 256;

      moog.addKey(\bassline, \octave, 2);
      moog.addKey(\bassline, \legato, 0.99);
      moog.addKey(\bassline, \dur, Pseq([part2Dur, part3Dur], 1));
      moog.addKey(\bassline, \note, Pseq([part2Note, part3Note], 1));
    }.fork;
  }

  playBassNote { | freq, saturVol = -6, feedbackVol = -6, guitarVol = -6 |
    satur.playNote(freq, saturVol);
    feedback.playNote(freq, feedbackVol);
    guitar.playNote(freq, guitarVol);
  }

  releaseBassNote { | freq |
    satur.releaseNote(freq);
    feedback.releaseNote(freq);
    guitar.releaseNote(freq);
  }
}

