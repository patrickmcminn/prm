/*
Tuesday, July 25th 2017
Boy_BassSection.sc
prm
*/

Boy_BassSection :IM_Module {

  var <isLoaded;
  var server;

  var <moog, <satur;

  var <section2IsPlaying, <section3IsPlaying;

  *new { | inBus = 3, outBus = 0, moogDeviceName, moogPortName, relGroup = nil, addAction = 'addToHead' |
    ^super.new(2, outBus, relGroup: relGroup, addAction: addAction).prInit(inBus, moogDeviceName, moogPortName);
  }

  prInit { | inBus = 3, moogDeviceName, moogPortName |
    server = Server.default;
    section2IsPlaying = false;
    section3IsPlaying = false;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      moog = Mother32.new(inBus, mixer.chanStereo(0), moogDeviceName, moogPortName,
        relGroup: group, addAction: \addToHead);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });

      satur = SaturSynth.new(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { satur.isLoaded } != true }, { 0.001.wait; });

      server.sync;
      moog.makeSequence(\section2);
      moog.makeSequence(\section3);
      satur.makeSequence(\section2);
      satur.makeSequence(\section3);

      server.sync;
      this.prMakePatternParameters;

      isLoaded = true;
    }
  }

  prMakePatternParameters {
    moog.addKey(\section2, \root, 7);
    moog.addKey(\section2, \note, Pseq([12, 9, 12, 9, 7, 5, 12, 7], inf));
    moog.addKey(\section2, \dur, Pseq([4, 3.5, 4.5, 3.5, 4, 4.5, 4, 4], inf));
    moog.addKey(\section2, \octave, 3);

    moog.addKey(\section3, \root, 7);
    moog.addKey(\section3, \octave, 3);
    moog.addKey(\section3, \note, Pseq([12, 9], inf));
    moog.addKey(\section3, \dur, Pseq([4, 3.5, 4.5, 3.5], inf));

    satur.addKey(\section2, \root, 7);
    satur.addKey(\section2, \note, Pseq([12, 9, 12, 9, 7, 5, 12, 7], inf));
    satur.addKey(\section2, \dur, Pseq([4, 3.5, 4.5, 3.5, 4, 4.5, 4, 4], inf));
    satur.addKey(\section2, \octave, 3);

    satur.addKey(\section3, \root, 7);
    satur.addKey(\section3, \octave, 3);
    satur.addKey(\section3, \note, Pseq([12, 9], inf));
    satur.addKey(\section3, \dur, Pseq([4, 3.5, 4.5, 3.5], inf));

  }

  //////// public functions:
  free {
    this.stopSection2;
    this.stopSection3;
    satur.free;
    moog.free;
    this.freeModule;
    isLoaded = false;
  }

  playSection2 { | clock |
    moog.playSequence(\section2, clock);
    satur.playSequence(\section2, clock);
    section2IsPlaying = true;
  }
  stopSection2 {
    moog.stopSequence(\section2);
    satur.stopSequence(\section2);
    section2IsPlaying = false;
  }

  playSection3 { | clock |
    moog.playSequence(\section3, clock);
    satur.playSequence(\section3, clock);
    section3IsPlaying = true;
  }
  stopSection3 {
    moog.stopSequence(\section3);
    satur.stopSequence(\section3);
    section3IsPlaying = false;
  }
}