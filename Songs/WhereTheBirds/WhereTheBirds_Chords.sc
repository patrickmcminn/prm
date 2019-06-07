/*
Friday, June 7th 2019
WhereTheBirds Chords.sc
prm

CV interface to play chord progression on
MI Plaits
*/

WhereTheBirds_Chords : IM_Module {

  var server, <isLoaded;
  var <input, <eq;

  var cvPitchOutBus, cvChordOutBus, cvInversionOutBus;

  *new { | outBus, pitchOutBus, chordOutBus, inversionOutBus, inBus, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(pitchOutBus, chordOutBus, inversionOutBus, inBus);
  }

  prInit { | pitchOutBus, chordOutBus, inversionOutBus, inBus |

    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;

      this.prAddSynthDef;
      cvPitchOutBus = pitchOutBus;
      cvChordOutBus = chordOutBus;
      cvInversionOutBus = inversionOutBus;

      eq = Equalizer.newStereo(mixer.chanStereo, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      input = IM_HardwareIn.new(inBus, eq.inBus, group, \addToHead);

      isLoaded = true;
    }
  }

  prAddSynthDef {

    Synth(\prm_birdsChords, {
      |
      pitchOutBus, chordOutBus, inversionOutBus,
      pitchValue = 0, chordValue = 0, inversionValue = 0,
      gate = 1
      |
      var pitch, chord, inversion;
      pitch = EnvGen.ar(Env.cutoff(0, 1.0), gate, doneAction: 2);
      pitch = pitch * pitchValue;
      chord = EnvGen.ar(Env.cutoff(0, 1.0), gate, doneAction: 2);
      chord = chord * chordValue;
      inversion = EnvGen.ar(Env.cutoff(0, 1.0), gate, doneAction: 2);
      inversion = inversion * inversionValue;
      Out.ar(pitchOutBus, pitch);
      Out.ar(chordOutBus, chord);
      Out.ar(inversionOutBus, inversion);

    }).add;
  }

}