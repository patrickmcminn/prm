/*
Monday, November 30th 2015
GlockSynth.sc
prm
*/

GlockSynth : IM_Module {

  var server, <isLoaded;
  var <synth, <reverb, <delay;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new.prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      delay = SimpleDelay.newStereo(mixer.chanStereo(0), 0.48, 0.6, 5, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });
      reverb = IM_Reverb.new(delay.inBus, mix: 0.5, roomSize: 0.5, damp: 0.85, relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });
      synth = Subtractive.new(reverb.inBus, relGroup: group, addAction: \addToHead);
      while({ try { synth.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetSubtractiveParameters;
      delay.setMix(0.5);
      mixer.setPreVol(6);

      server.sync;

      isLoaded = true;
    }
  }

  prSetSubtractiveParameters {
    synth.setSustainLevel(1);
    synth.setAttackTime(0.05);
    synth.setDecayTime(0);
    synth.setReleaseTime(3);

    synth.setOsc2Vol(-70);
    synth.setOsc1Waveform('tri');
    synth.setOsc1Octave(4);

    synth.setFilterCutoff(1870);
    //synth.setFilterDrive(3);

    synth.setOsc2Vol(-70);

  }

  ///////// public functions:

  free {
    synth.free;
    reverb.free;
    delay.free;
    this.freeModule;
  }

}