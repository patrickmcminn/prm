/*
Tuesday, December 3rd 2015
FalseSelf_BassSection.sc
prm
driving from Portland, ME to Ithaca, NY
*/

FalseSelf_BassSection :IM_Module {

  var server, <isLoaded;
  var <satur, <feedback, <guitar;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(3, outBus, send0Bus, send1Bus, send3Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
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

