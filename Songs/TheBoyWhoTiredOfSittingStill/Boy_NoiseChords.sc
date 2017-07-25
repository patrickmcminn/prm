/*
Tuesday, July 25th 2017
Boy_NoiseChords.sc
prm
*/

Boy_NoiseChords : IM_Module {

  var <isLoaded;
  var server;
  var <hammond, <distortion;
  var <section1IsPlaying, <section2IsPlaying, <section3IsPlaying;

  *new { |outBus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      distortion = Distortion.newStereo(mixer.chanStereo(0), 5000, relGroup: group, addAction: \addToHead);
      while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

      hammond = Hammond.new(distortion.inBus, relGroup: group, addAction: \addToHead);
      while({ try { hammond.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      hammond.makeSequence(\section1);
      hammond.makeSequence(\section2);
      hammond.makeSequence(\section3);

      server.sync;

      this.prMakePatternParameters;

      server.sync;

      distortion.postEQ.setLowPassCutoff(4500);
      distortion.mixer.setVol(-15);
      distortion.postEQ.setHighPassCutoff(450);

      section1IsPlaying = false;
      section2IsPlaying = false;
      section3IsPlaying = false;

      isLoaded = true;
    }
  }

  prMakePatternParameters {
    var gMajor7, eMajor7, dMajor7, aMajor;
    gMajor7 = [-12, 0, 4, 7, 11];
    eMajor7 = [-15, 1, 4, 8, 9];
    dMajor7 = [-17, -1, 2, 6, 7];
    aMajor = [-10, -3, 2, 6, 9];

    hammond.addKey(\section1, \dist, 1.2);
    hammond.addKey(\section1, \amp, 0.2);
    hammond.addKey(\section1, \cutoff, 20000);
    hammond.addKey(\section1, \legato, 1);
    hammond.addKey(\section1, \root, 7);
    hammond.addKey(\section1, \note, Pseq([gMajor7, eMajor7, dMajor7, aMajor], inf));
    hammond.addKey(\section1, \dur, Pseq([4, 3.5, 4.5, 4], inf));
    hammond.addKey(\section1, \attackTime, 0.01);
    hammond.addKey(\section1, \releaseTime, 0.01);
    hammond.addKey(\section1, \octave, 4);

    hammond.addKey(\section2, \dist, 1.2);
    hammond.addKey(\section2, \amp, 0.35);
    hammond.addKey(\section2, \cutoff, 20000);
    hammond.addKey(\section2, \legato, 1);
    hammond.addKey(\section2, \root, 7);
    hammond.addKey(\section2, \note, Pseq([gMajor7, eMajor7, gMajor7, eMajor7, dMajor7, aMajor, gMajor7, dMajor7], inf));
    hammond.addKey(\section2, \dur, Pseq([4, 3.5, 4.5, 3.5, 4, 4.5, 4, 4], inf));
    hammond.addKey(\section2, \attackTime, 0.12);
    hammond.addKey(\section2, \octave, 4);


    hammond.addKey(\section3, \dist, 1.2);
    hammond.addKey(\section3, \amp, 0.3);
    hammond.addKey(\section3, \cutoff, 20000);
    hammond.addKey(\section3, \legato, 1);
    hammond.addKey(\section3, \root, 7);
    hammond.addKey(\section3, \note, Pseq([gMajor7, eMajor7], inf));
    hammond.addKey(\section3, \dur, Pseq([4, 3.5, 4.5, 3.5], inf));
    hammond.addKey(\section3, \attackTime, 0.12);
    hammond.addKey(\section3, \octave, 4);
  }

  /////// public functions:

  free {
    distortion.free;
    hammond.free;
    this.freeModule;
    isLoaded = false;
  }

  playSection1 { | clock |
    hammond.playSequence(\section1, clock);
    section1IsPlaying = true;
  }
  stopSection1 {
    hammond.stopSequence(\section1);
    section1IsPlaying = false;
  }

  playSection2 { | clock |
    hammond.playSequence(\section2, clock);
    section2IsPlaying = true;
  }
  stopSection2 {
    hammond.stopSequence(\section2);
    section2IsPlaying = false;
  }

  playSection3 { | clock |
    hammond.playSequence(\section3, clock);
    section3IsPlaying = true;
  }
  stopSection3 {
    hammond.stopSequence(\section3);
    section3IsPlaying = false;
  }
}