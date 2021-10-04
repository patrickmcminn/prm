/*
Friday, June 7th 2019
WhereTheBirds Chords.sc
prm

CV interface to play chord progression on
MI Plaits

assumes
- Frequency at noon
- Harmonics all the way CCW
- Timbre at noon

chord values:
0 - 8va
0.05 - P5
0.1 - sus4
0.13 - m
0.19 - m7

0.38 - M9
0.42 - M7s
0.5 - M
*/

WhereTheBirds_Chords : IM_Module {

  var server, <isLoaded;
  var <input, <eq;
  var synth;

  var cvPitchOutBus, cvChordOutBus, cvInversionOutBus;
  var <frequency, <chordCV, <inversionCV;

  var <sequencerDict;

  var <droneIsPlaying, <section1IsPlaying, <turnaroundIsPlaying, <endDroneIsPlaying;

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
      this.prMakeSequences;


      sequencerDict = IdentityDictionary.new;

      cvPitchOutBus = pitchOutBus;
      cvChordOutBus = chordOutBus;
      cvInversionOutBus = inversionOutBus;

      frequency = 220;
      chordCV = 0;
      inversionCV = 0;

      eq = Equalizer.newMono(mixer.chanStereo, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      input = IM_HardwareIn.new(inBus, eq.inBus, group, \addToHead);

      server.sync;
      /*
      synth = Synth(\prm_birdsChords, [\pitchOutBus, pitchOutBus, \chordOutBus, chordOutBus,
      \inversionOutBus, inversionOutBus, \freq, frequency, \chordValue, chordCV,
      \inversionValue, inversionCV], group, \addToHead);

      server.sync;
      */

      droneIsPlaying = false;
      section1IsPlaying = false;
      turnaroundIsPlaying = false;
      endDroneIsPlaying = false;

      this.prSetSequenceParameters;

      isLoaded = true;
    }
  }

  prAddSynthDef {

    SynthDef(\prm_birdsChords, {
      |
      pitchOutBus, chordOutBus, inversionOutBus,
      freq = 220, chordValue = 0, inversionValue = 0,
      gate = 1
      |
      var pitchCV, pitch, chord, inversion;
      pitchCV = ((freq/261.1).log2) / 10;
      pitch = EnvGen.ar(Env.cutoff(0, 1.0), gate, doneAction: 2);
      pitch = pitch * pitchCV;
      chord = EnvGen.ar(Env.cutoff(0, 1.0), gate, doneAction: 2);
      chord = chord * chordValue;
      inversion = EnvGen.ar(Env.cutoff(0, 1.0), gate, doneAction: 2);
      inversion = inversion * inversionValue;
      Out.ar(pitchOutBus, pitch);
      Out.ar(chordOutBus, chord);
      Out.ar(inversionOutBus, inversion);

    }).add;
  }

  prMakeSequences {
    {
      this.makeSequence(\drone);
      this.makeSequence(\section1);
      this.makeSequence(\turnaround);
      this.makeSequence(\section2);
      this.makeSequence(\section3);
      this.makeSequence(\endDrone);

      server.sync;


    }.fork;
  }

  prSetSequenceParameters {
    this.addKey(\drone, \dur, 4);
    this.addKey(\drone, \freq, 66.midicps);
    this.addKey(\drone, \chordValue, 0);
    this.addKey(\drone, \inversionValue, 0);

    this.addKey(\section1, \dur, 4);
    this.addKey(\section1, \freq, Pseq([66.midicps, 69.midicps, 61.midicps, 64.midicps], inf));
    this.addKey(\section1, \chordValue, Pseq([0.13, 0.5, 0.13, 0.5], inf));
    this.addKey(\section1, \inversionValue, Pseq([0.05, 0.03, 0.05, 0], inf));

    this.addKey(\turnaround, \dur, 4);
    this.addKey(\turnaround, \freq, Pseq([66.midicps, 69.midicps, 61.midicps, 64.midicps], inf));
    this.addKey(\turnaround, \chordValue, Pseq([0.13, 0.5, 0.13, 0.5], inf));
    this.addKey(\turnaround, \inversionValue, Pseq([0.05, 0.05, 0.15, 0.2], inf));

    this.addKey(\endDrone, \dur, 8);
    this.addKey(\endDrone, \freq, 54.midicps);
    this.addKey(\endDrone, \chordValue, 0.13);
    this.addKey(\endDrone, \inversionValue, 0);

  }

  //////// public functions:

  free {
    synth.free;
    eq.free;
    input.free;
    this.freeModule;
    isLoaded = false;
  }

	setDrone {
		this.setFreq(66.midicps);
		this.setChordValue(0);
		this.setInversionValue(0);
	}

	setChord1 {

	}

  setFreq { | freq = 220 |
    frequency = freq;
    synth.set(\freq, frequency);
  }
  setChordValue { | cv = 0 |
    chordCV = cv;
    synth.set(\chordValue, chordCV);
  }
  setInversionValue { | cv = 0 |
    inversionCV = cv;
    synth.set(\inversionValue, inversionCV);
  }

  makeSequence { | name |
    fork {
      sequencerDict[name] = IM_PatternSeq.new(name, group, \addToHead);
      server.sync;
      sequencerDict[name].stop;
      sequencerDict[name].addKey(\instrument, \prm_birdsChords);
      //sequencerDict[name].addKey(\outBus, mixer.chanStereo(0));
      //sequencerDict[name].addKey(\type, \set);
      //sequencerDict[name].addKey(\id, synth.nodeID);
      //sequencerDict[name].addKey(\args, #[\pitchValue, \chordValue, \inversionValue]);
      sequencerDict[name].addKey(\freq, Pfunc({ frequency }) );
      sequencerDict[name].addKey(\chordValue, Pfunc({ chordCV }) );
      sequencerDict[name].addKey(\inversionValue, Pfunc({ inversionCV }) );
      sequencerDict[name].addKey(\pitchOutBus, Pfunc({ cvPitchOutBus }) );
      sequencerDict[name].addKey(\chordOutBus, Pfunc({ cvChordOutBus }) );
      sequencerDict[name].addKey(\inversionOutBus, Pfunc({ cvInversionOutBus }) );
      sequencerDict[name].addKey(\legato, 1);
    };
  }

  addKey { | name, key, action |
    sequencerDict[name].addKey(key, action);
  }

  playSequence { | name, clock, quant = 'nil' |
    sequencerDict[name].play(clock);
  }

  resetSequence { | name | sequencerDict[name].reset; }
  stopSequence { | name | sequencerDict[name].stop; }
  pauseSequence { | name | sequencerDict[name].pause }
  resumeSequence { | name | sequencerDict[name].resume; }
  isSequencePlaying { | name | ^sequencerDict[name].isPlaying }
  setSequenceQuant { | name, quant = 0 | sequencerDict[name].setQuant(quant) }

  toggleDrone { | clock | if( droneIsPlaying,
    { this.stopDrone }, { this.playDrone(clock) });
  }
  playDrone { | clock |
    sequencerDict[\drone].play(clock);
    droneIsPlaying = true;
  }
  stopDrone {
    sequencerDict[\drone].stop;
    droneIsPlaying = false;
  }

  toggleSection1 { | clock | if( section1IsPlaying,
    { this.stopSection1 }, { this.playSection1(clock) });
  }
  playSection1 { | clock |
    sequencerDict[\section1].play(clock);
    section1IsPlaying = true;
  }
  stopSection1 {
    sequencerDict[\section1].stop;
    section1IsPlaying = false;
  }

  toggleTurnaround { | clock | if( turnaroundIsPlaying,
    { this.stopTurnaround }, { this.playTurnaround(clock) });
  }
  playTurnaround { | clock |
    sequencerDict[\turnaround].play(clock);
    turnaroundIsPlaying = true;
  }
  stopTurnaround {
    sequencerDict[\turnaround].stop;
    turnaroundIsPlaying = false;
  }

  toggleEndDrone { | clock | if( endDroneIsPlaying,
    { this.stopEndDrone }, { this.playEndDrone(clock) });
  }
  playEndDrone { | clock |
    sequencerDict[\endDrone].play(clock);
    endDroneIsPlaying = true;
  }
  stopEndDrone {
    sequencerDict[\endDrone].stop;
    endDroneIsPlaying = false;
  }


}