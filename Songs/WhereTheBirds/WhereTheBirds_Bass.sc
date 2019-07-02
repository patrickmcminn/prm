/*
Saturday, June 8th 2019
WhereTheBirds_Bass.sc
prm
*/


WhereTheBirds_Bass : IM_Module {

  var server, <isLoaded;
  var <moog, <satur;
  //var <cvLFO, <lfo1, <lfo2;

  var <section2IsPlaying, <section3IsPlaying, <endDroneIsPlaying;

  *new { | outBus = 0, moogInBus, moogDeviceName, moogPortName, relGroup, addAction = 'addToHead' |
    ^super.new(2, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(moogInBus, moogDeviceName, moogPortName);
  }

  prInit { | moogInBus, moogDeviceName, moogPortName |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      moog = Mother32.new(moogInBus, mixer.chanStereo(0), moogDeviceName, moogPortName, relGroup: group, addAction: \addToHead);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });

      satur = SaturSynth.new(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { satur.isLoaded } != true }, { 0.001.wait; });

      /*
      //cvLFO = CV_LFO.new();
      lfo1 = LFO.new(1, 'sine', 100, 500, group, \addToHead);
      while({ try { lfo1.isLoaded } != true }, { 0.001.wait; });

      lfo2 = LFO.new(0.1, 'sine', 1, 20, group, \addToHead);
      while({ try { lfo2.isLoaded } != true }, { 0.001.wait; });
      */

      server.sync;

      this.prMakeSequences;
      this.prSetInitialParameters;

      server.sync;

      this.prSetMoogSequenceParameters;
      server.sync;
      this.prSetSaturSequenceParameters;

      isLoaded = true;
    }

  }

  prSetInitialParameters {
    section2IsPlaying = false;
    section3IsPlaying = false;
    endDroneIsPlaying = false;
    /*
    mixer.setPreVol(0, -6);
    mixer.setPreVol(1, -6);
    */

    //lfo1.mapFreq(lfo2.outBus);

  }

  prMakeSequences {

    moog.makeSequence(\bass2);
    moog.makeSequence(\bass3);
    moog.makeSequence(\endDrone);

    satur.makeSequence(\bass3);
    satur.makeSequence(\endDrone);

  }

  prSetMoogSequenceParameters {

    moog.addKey(\bass2, \dur, 4);
    moog.addKey(\bass2, \note, Pseq([6, 9, 13, 4, 6, 9, 1, 4], inf));
    moog.addKey(\bass2, \legato, 0.99);
    moog.addKey(\bass2, \octave, 3);

    moog.addKey(\bass3, \dur, 4);
    moog.addKey(\bass3, \note, Pseq([6, 9, 13, 4], inf));
    moog.addKey(\bass3, \legato, 0.99);
    moog.addKey(\bass3, \octave, 2);

    moog.addKey(\endDrone, \dur, 8);
    moog.addKey(\endDrone, \note, 6);
    moog.addKey(\endDrone, \octave, 2);
    moog.addKey(\endDrone, \legato, 0.99);

  }

  prSetSaturSequenceParameters {
    satur.addKey(\bass3, \dur, 4);
    satur.addKey(\bass3, \note, Pseq([6, 9, 13, 4], inf));
    satur.addKey(\bass3, \legato, 1);
    satur.addKey(\bass3, \octave, 3);

    satur.addKey(\endDrone, \dur, 8);
    satur.addKey(\endDrone, \note, 6);
    satur.addKey(\endDrone, \octave, 2);
    satur.addKey(\endDrone, \legato, 1);

  }

  /////// public functions:

  free {
    moog.free;
    satur.free;
    this.freeModule;
  }

  toggleSection2 { | clock | if( section2IsPlaying == true, { this.stopSection2; }, { this.playSection2(clock) }); }
  playSection2 { | clock |
    moog.playSequence(\bass2, clock);
    section2IsPlaying = true;
  }
  stopSection2 {
    moog.stopSequence(\bass2);
    section2IsPlaying = false;
  }

  toggleSection3 { | clock | if( section3IsPlaying == true, { this.stopSection3; }, { this.playSection3(clock) }); }
  playSection3 { | clock |
    moog.playSequence(\bass3, clock);
    satur.playSequence(\bass3, clock);
    section3IsPlaying = true;
  }
  stopSection3 {
    moog.stopSequence(\bass3);
    satur.stopSequence(\bass3);
    section3IsPlaying = false;
  }

  toggleEndDrone { | clock | if( endDroneIsPlaying == true, { this.stopEndDrone }, { this.playEndDrone(clock) }); }
  playEndDrone { | clock |
    //satur.mapCutoff(lfo1.outBus);
    moog.playSequence(\endDrone, clock);
    satur.playSequence(\endDrone, clock);
    endDroneIsPlaying = true;
  }
  stopEndDrone {
    moog.stopSequence(\endDrone);
    satur.stopSequence(\endDrone);
    endDroneIsPlaying = false;
  }

}