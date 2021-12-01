/*
Thursday, May 32rd 2018
Light_Chorale.sc
prm

there was a time when the world was suffused with light
*/

Light_Chorale : IM_Module {
  var server, <isLoaded;

  var <synth;

  var <choraleIsPlaying;

  *new { | outBus = 0, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try {mixer.isLoaded } != true }, { 0.001.wait; });

      synth = SubJuno.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { synth.isLoaded } != true }, { 0.001.wait; });

      this.prInitializeParameters;

      server.sync;

      this.prMakePatternParameters;

      choraleIsPlaying = false;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    synth.setOsc1Waveform('rect');
    synth.setOsc2Waveform('rect');
    synth.setOsc2Octave(3);
    synth.setOsc1PulseWidthLFO2Bottom(-0.5);
    synth.setOsc1PulseWidthLFO2Bottom(0.5);
    synth.setOsc2PulseWidthLFO1Bottom(0.5);
    synth.setOsc2PulseWidthLFO1Bottom(-0.5);
    synth.setLFO1Waveform('noise');
    synth.setLFO1Freq(0.3);
    synth.setLFO2Waveform('noise');
    synth.setLFO2Freq(0.3);
    synth.setFilterCutoff(1200);
    synth.setFilterCutoffLFO1BottomRatio(0.8);
    synth.setFilterCutoffLFO1TopRatio(2);
    synth.setAttackTime(0.5);
    synth.setReleaseTime(1);
    synth.setFilterDrive(50);

    synth.makeSequence(\chorale);

    synth.sequencerClock.tempo = 75/60;
  }

  prMakePatternParameters {
    synth.addKey(\chorale, \legato, 1);
    synth.addKey(\chorale, \dur, Pseq([6, 4, 3, 5, 2.5, 2.5, 3, 4, 6, 4, 3, 4, 2.5, 2.5, 3, 9], inf));
    synth.addKey(\chorale, \note, Pseq([
      [ 14, 6, -3, -10 ], [ 11, 6, -1, -10 ], [ 9, 6, 2, -10 ], [ 13, 6, -3, -11 ], [ 13, 6, -3, -10 ], [ 14, 6, -1, -13 ],
      [ 16, 9, 1, -15 ], [ 19, 11, 2, -17 ], [ 18, 9, 2, -10 ], [ 18, 9, 2, -13 ], [ 18, 9, 2, -10 ], [ 18, 9, 6, -11 ],
      [ 21, 13, 6, -10 ], [ 21, 14, 6, -13 ], [ 21, 16, 1, -15 ], [ 23, 19, 2, -17 ] ], inf));
  }

  //////// public functions:

  free {
    choraleIsPlaying = false;
    synth.free;
    this.freeModule;
    isLoaded = false;
  }

  playChorale {
    synth.playSequence(\chorale);
    choraleIsPlaying = true;
  }
  stopChorale {
    synth.stopSequence(\chorale);
    choraleIsPlaying = false;
  }
  tglChorale { if( choraleIsPlaying == false, { this.playChorale }, { this.stopChorale }); }

}