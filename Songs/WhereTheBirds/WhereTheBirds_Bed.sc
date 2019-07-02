/*
Saturday, June 8th 2019
WhereTheBirds_Bed.sc
prm
*/

WhereTheBirds_Bed : IM_Module {

  var <isLoaded, server;
  var <granulator, <reverb, <eq, <synth;

  var <cSharpIsPlaying, <fSharpIsPlaying, <gSharpIsPlaying;
  var <aIsPlaying, <bIsPlaying, <eIsPlaying;

  *new { | outBus = 0, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });


      reverb = IM_Reverb.new(granulator.inBus, mix: 0.35, roomSize: 0.85, damp: 0.9, relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      synth = Subtractive.new(reverb.inBus, relGroup: group, addAction: \addToHead);
      while({ try { synth.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;
      this.prMakeSequences;

      server.sync;
      this.prSetSequenceParameters;

      isLoaded = true;
    }
  }

  prSetInitialParameters {

    cSharpIsPlaying = false; fSharpIsPlaying = false; gSharpIsPlaying = false;
    aIsPlaying = false; bIsPlaying = false; eIsPlaying = false;

    mixer.setPreVol(-12);

    synth.setAttackTime(2);
    synth.setReleaseTime(1.5);
    synth.setFilterDrive(2);
    synth.setFilterCutoff(1100);
    synth.setOsc1Waveform(1);
    synth.setOsc2Waveform(1.5);
    synth.setOsc1Octave(3);
    synth.setOsc2Octave(3);

    reverb.postEQ.setHighPassCutoff(500);
    reverb.postEQ.setPeak1Freq(350);
    reverb.postEQ.setPeak1Gain(-6);
    reverb.postEQ.setPeak2Freq(650);
    reverb.postEQ.setPeak2Gain(-6);

    granulator.setGranulatorCrossfade(-0.75);
    granulator.setDelayMix(0);
    granulator.setTrigRate(32);
    granulator.setGrainDur(0.5, 1.5);
    granulator.setPan(-0.75, 0.75);

  }

  prMakeSequences {
    synth.makeSequence(\cSharp);
    synth.makeSequence(\gSharp);
    synth.makeSequence(\fSharp);
    synth.makeSequence(\a);
    synth.makeSequence(\b);
    synth.makeSequence(\e);
  }

  prSetSequenceParameters {
    synth.addKey(\cSharp, \dur, Pseq([1.576, Rest(5.424)], inf));
    synth.addKey(\cSharp, \note, 1);
    synth.addKey(\cSharp, \octave, 6);
    synth.addKey(\cSharp, \amp, Pwhite(0.3, 0.55, inf));
    synth.addKey(\cSharp, \pan, Pwhite(-1, 1, inf));


    synth.addKey(\gSharp, \dur, Pseq([1.576, Rest(6)], inf));
    synth.addKey(\gSharp, \note, 8);
    synth.addKey(\gSharp, \octave, 5);
    synth.addKey(\gSharp, \amp, Pwhite(0.25, 0.3, inf));
    synth.addKey(\gSharp, \pan, Pwhite(-1, 1, inf));


    synth.addKey(\a, \dur, Pseq([1.576, Rest(5.9)], inf));
    synth.addKey(\a, \note, 9);
    synth.addKey(\a, \octave, 5);
    synth.addKey(\a, \amp, Pwhite(0.2, 0.29, inf));
    synth.addKey(\a, \pan, Pwhite(-1, 1, inf));

    synth.addKey(\fSharp, \dur, Pseq([1.576, Rest(5.5)], inf));
    synth.addKey(\fSharp, \note, 6);
    synth.addKey(\fSharp, \octave, 5);
    synth.addKey(\fSharp, \amp, Pwhite(0.25, 0.35, inf));
    synth.addKey(\fSharp, \pan, Pwhite(-1, 1, inf));

    synth.addKey(\b, \dur, Pseq([1.576, Rest(7)], inf));
    synth.addKey(\b, \note, 11);
    synth.addKey(\b, \octave, 5);
    synth.addKey(\b, \amp, Pwhite(0.25, 0.35));
    synth.addKey(\b, \pan, Pwhite(-1, 1, inf));

    synth.addKey(\e, \dur, Pseq([1.576, Rest(7.5)], inf));
    synth.addKey(\e, \note, 4);
    synth.addKey(\e, \octave, Prand([5, 6], inf));
    synth.addKey(\e, \amp, Pwhite(0.2, 0.29));
    synth.addKey(\e, \pan, Pwhite(-1, 1, inf));
  }

  //////// public functions:

  free {
    isLoaded = false;
    synth.free;
    eq.free;
    reverb.free;
    granulator.free;
    this.freeModule;
  }

  toggleCSharp {
    if( cSharpIsPlaying, { this.stopCSharp }, { this.playCSharp });
  }
  playCSharp {
    synth.playSequence(\cSharp);
    cSharpIsPlaying = true;
  }
  stopCSharp {
    synth.stopSequence(\cSharp);
    cSharpIsPlaying = false;
  }

  toggleGSharp {
    if( gSharpIsPlaying, { this.stopGSharp }, { this.playGSharp });
  }
  playGSharp {
    synth.playSequence(\gSharp);
    gSharpIsPlaying = true;
  }
  stopGSharp {
    synth.stopSequence(\gSharp);
    gSharpIsPlaying = false;
  }

  toggleFSharp {
    if( fSharpIsPlaying, { this.stopFSharp }, { this.playFSharp });
  }
  playFSharp {
    synth.playSequence(\fSharp);
    fSharpIsPlaying = true;
  }
  stopFSharp {
    synth.stopSequence(\fSharp);
    fSharpIsPlaying = false;
  }

  toggleA {
    if( aIsPlaying, { this.stopA }, { this.playA });
  }
  playA {
    synth.playSequence(\a);
    aIsPlaying = true;
  }
  stopA {
    synth.stopSequence(\a);
    aIsPlaying = false;
  }

  toggleB {
    if( bIsPlaying, { this.stopB }, { this.playB });
  }
  playB {
    synth.playSequence(\b);
    bIsPlaying = true;
  }
  stopB {
    synth.stopSequence(\b);
    bIsPlaying = false;
  }

  toggleE {
    if( eIsPlaying, { this.stopE }, { this.playE });
  }
  playE {
    synth.playSequence(\e);
    eIsPlaying = true;
  }
  stopE {
    synth.stopSequence(\e);
    eIsPlaying = false;
  }

}