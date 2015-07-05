/*
Sunday, July 5th 2015
Dauphine_SequenceSynth.sc
prm
*/

Dauphine_SequenceSynth : IM_Module {

  var <isLoaded, server;
  var synth;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      synth = Subtractive.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);

      while({ try { synth.isLoaded } != true }, { 0.001.wait; });
      server.sync;
      synth.makeSequence('accomp');
      server.sync;
      this.prInitializeParameters;
      this.prCreateSequence;

      isLoaded = true;
    };
  }

  prInitializeParameters {
    synth.setOsc2Waveform('sine');
    synth.setOsc2Octave(3);
    synth.setFilterCutoff(712);
    synth.setNoiseOscVol(-18);
    synth.setNoiseOscCutoff(461);
    synth.setDecayTime(2.65);
    synth.setSustainLevel(0.02);
    synth.setReleaseTime(0.052);
    synth.setFilterDrive(2);
  }

  prCreateSequence {
    synth.addKey(\accomp, \dur, 0.5);
    synth.addKey(\accomp, \note, Pseq([0, 7, 14, 21, 0, 7, 14, 21, 0, 7, 17, 16, 0, 7, 17, 16], inf));
    synth.addKey(\accomp, \octave, 4);
    synth.addKey(\accomp, \legato, 1);
    synth.addKey(\accomp, \amp, -6.dbamp);
  }

  //////// public functions:

  free {
    synth.free;
    this.freeModule;
    synth = nil;
  }


  playSequence { | clock = nil |
    synth.playSequence('accomp', clock);
  }

  stopSequence { synth.stopSequence('accomp'); }
}