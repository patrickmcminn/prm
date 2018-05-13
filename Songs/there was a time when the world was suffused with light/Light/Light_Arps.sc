/*
Saturday, April 22nd 2017
Light_Arps.sc
prm

edited Thursday, May 3rd 2018
*/


Light_Arps : IM_Module {

  var server, <isLoaded;

  var <synth, <granulator, <lowPass;

  var <chord1IsPlaying, <chord2IsPlaying, <chord3IsPlaying, <chord4IsPlaying, <chord5IsPlaying;

  var <arp1Chord1, <arp1Chord2, <arp1Chord3, <arp1Chord4, <arp1Chord5;
  var <arp2Chord1, <arp2Chord2, <arp2Chord3, <arp2Chord4, <arp2Chord5;


  *new { | outBus = 0, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      lowPass = LowPassFilter.newStereo(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { lowPass.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(lowPass.inBus, group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      synth = Subtractive.new(granulator.inBus, relGroup: group, addAction: \addToHead);
      while({ try { synth.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitializeParameters;
      this.prMakePatterns;

      server.sync;

      this.prMakePatternParameters;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    mixer.setPreVol(6);
    chord1IsPlaying = false; chord2IsPlaying = false; chord3IsPlaying = false;
    chord4IsPlaying = false;chord5IsPlaying = false;

    // lowPass:
    lowPass.setCutoff(26);

    // granulator:
    granulator.setGranulatorCrossfade(0.4);
    granulator.setGrainDur(0.01, 0.1);
    granulator.setDelayMix(0);
    granulator.setGrainEnvelope('percRev');

    // synth:
    synth.setAllParameters([ 1, 1, 0, 1, 0.5, 0, 0.05, 0.05, 1, 0.5, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 2, 0.5, 0.5, 0, 1, -22, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0.5, 0.25, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 1, 1, 1, 1, 0.05, 0.29, 0.227, false, 1, 1, 0.5, 4, 0, 0, 0, 0, 1, 2000, 0, 0, 1, 1, 1, 1, 0.05, 0.1, 0.1, 0.05, 0, 0, 0, 0, 0 ]);
    synth.sequencerClock.tempo = 75/60;

    /*
    x.setFilterCutoff(2000);
    x.setDecayTime(2.65);
    x.setReleaseTime(6.31);
    x.setSustainLevel(1);

    x.setFilterEnvDecayTime(0.29);
    x.setFilterEnvReleaseTime(0.227);

    x.setOsc1Waveform('saw');
    x.setOsc2Waveform('sine');

    x.setOsc2Octave(3);

    x.setOsc2DetuneCents(-22);


    x.setFilterCutoffLFO2BottomRatio(0.5);
    x.setFilterCutoffLFO2TopRatio(4);


    x.printAllParameters;

    x.releaseNote(55);
    x.playNote(220)
    x.playNote(55);
    */

  }

  prMakePatterns {
    synth.makeSequence(\arp1Chord1);
    synth.makeSequence(\arp1Chord2);
    synth.makeSequence(\arp1Chord3);
    synth.makeSequence(\arp1Chord4);
    synth.makeSequence(\arp1Chord5);

    synth.makeSequence(\arp2Chord1);
    synth.makeSequence(\arp2Chord2);
    synth.makeSequence(\arp2Chord3);
    synth.makeSequence(\arp2Chord4);
    synth.makeSequence(\arp2Chord5);

  }

  prMakePatternParameters {
    synth.addKey(\arp1Chord1, \pan, -1);
    synth.addKey(\arp1Chord1, \dur, 1/6);
    synth.addKey(\arp1Chord1, \note, Pseq([9, 6, 13, 2, 14], inf));

    synth.addKey(\arp1Chord2, \pan, -1);
    synth.addKey(\arp1Chord2, \dur, 1/6);
    synth.addKey(\arp1Chord2, \note, Pseq([9, 6, 11, 2, 14], inf));

    synth.addKey(\arp1Chord3, \pan, -1);
    synth.addKey(\arp1Chord3, \dur, 1/6);
    synth.addKey(\arp1Chord3, \note, Pseq([9, 6, 13, 4, 18], inf));

    synth.addKey(\arp1Chord4, \pan, -1);
    synth.addKey(\arp1Chord4, \dur, 1/6);
    synth.addKey(\arp1Chord4, \note, Pseq([11, 9, 13, 4, 14], inf));

    synth.addKey(\arp1Chord5, \pan, -1);
    synth.addKey(\arp1Chord5, \dur, 0.0625);
    synth.addKey(\arp1Chord5, \note, Pseq([7, 6, 11, 2, 14, -5], inf));

    synth.addKey(\arp2Chord1, \pan, -1);
    synth.addKey(\arp2Chord1, \dur, 1/6);
    synth.addKey(\arp2Chord1, \note, Pseq([14, 13, 9, 6, 2, 2, 6, 9, 13, 14], inf));

    synth.addKey(\arp2Chord2, \pan, -1);
    synth.addKey(\arp2Chord2, \dur, 1/6);
    synth.addKey(\arp2Chord2, \note, Pseq([14, 11, 9, 6, 2, 2, 6, 9, 11, 14], inf));

    synth.addKey(\arp2Chord3, \pan, -1);
    synth.addKey(\arp2Chord3, \dur, 1/6);
    synth.addKey(\arp2Chord3, \note, Pseq([18, 13, 9, 6, 4, 1, 1, 4, 6, 9, 13, 18], inf));

    synth.addKey(\arp2Chord4, \pan, -1);
    synth.addKey(\arp2Chord4, \dur, 1/6);
    synth.addKey(\arp2Chord4, \note, Pseq([14, 13, 11, 9, 4, 4, 9, 11, 13, 14], inf));

    synth.addKey(\arp2Chord5, \pan, -1);
    synth.addKey(\arp2Chord5, \dur, 1/12);
    synth.addKey(\arp2Chord5, \note, Pseq([14, 11, 7, 6, 2, -5, -5, 2, 6, 7, 11, 14], inf));

  }

  ///////// public functions:
  free {
    synth.free;
    granulator.free;
    lowPass.free;
    this.freeModule;
    isLoaded = false;
  }

  playChord1 {
    synth.playSequence(\arp1Chord1);
    synth.playSequence(\arp2Chord1);
    chord1IsPlaying = true;
  }
  stopChord1 {
    synth.stopSequence(\arp1Chord1);
    synth.stopSequence(\arp2Chord1);
    chord1IsPlaying = false;
  }
  tglChord1 {
    if(chord1IsPlaying == false, { this.playChord1 }, { this.stopChord1 });
  }

  playChord2 {
    synth.playSequence(\arp1Chord2);
    synth.playSequence(\arp2Chord2);
    chord2IsPlaying = true;
  }
  stopChord2 {
    synth.stopSequence(\arp1Chord2);
    synth.stopSequence(\arp2Chord2);
    chord2IsPlaying = false;
  }
  tglChord2 {
    if(chord2IsPlaying == false, { this.playChord2 }, { this.stopChord2 });
  }

  playChord3 {
    synth.playSequence(\arp1Chord3);
    synth.playSequence(\arp2Chord3);
    chord3IsPlaying = true;
  }
  stopChord3 {
    synth.stopSequence(\arp1Chord3);
    synth.stopSequence(\arp2Chord3);
    chord3IsPlaying = false;
  }
  tglChord3 {
    if(chord3IsPlaying == false, { this.playChord3 }, { this.stopChord3 });
  }

  playChord4 {
    synth.playSequence(\arp1Chord4);
    synth.playSequence(\arp2Chord4);
    chord4IsPlaying = true;
  }
  stopChord4 {
    synth.stopSequence(\arp1Chord4);
    synth.stopSequence(\arp2Chord4);
    chord4IsPlaying = false;
  }
  tglChord4 {
    if(chord4IsPlaying == false, { this.playChord4 }, { this.stopChord4 });
  }

  playChord5 {
    synth.playSequence(\arp1Chord5);
    synth.playSequence(\arp2Chord5);
    chord5IsPlaying = true;
  }
  stopChord5 {
    synth.stopSequence(\arp1Chord5);
    synth.stopSequence(\arp2Chord5);
    chord5IsPlaying = false;
  }
  tglChord5 {
    if(chord5IsPlaying == false, { this.playChord5 }, { this.stopChord5 });
  }

}


/*

old cheating version:


Light_Arps : IM_Module {

var server, <isLoaded;
var <sampler;

*new { | outBus, relGroup = nil, addAction = 'addToHead' |
^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
}

prInit {
var path, sampleArray;
server = Server.default;
server.waitForBoot {
isLoaded = false;

while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/Light/Samples/Arp/";
sampleArray = (path ++ "*").pathMatch;

sampler = Sampler.newStereo(mixer.chanStereo(0), sampleArray, relGroup: group, addAction: \addToHead);
while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

isLoaded = true;
}
}

//////// public functions:

free {
sampler.free;
this.freeModule;
isLoaded = false;
}

playArps {
sampler.playSampleOneShot(0, 0);
sampler.playSampleOneShot(1, 0);
}

setFilterCutoff { | cutoff = 150 |
sampler.setFilterCutoff(cutoff);
}

}
*/