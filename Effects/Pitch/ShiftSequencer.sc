/*
Monday, September 15th 2014
ShiftSequencer.sc
prm
*/

ShiftSequencer : IM_Processor {

  var <isLoaded;
  var shiftSynth;
  var sequencerDict, <sequencerClock, <tempo;
  var <attackTime, <decayTime, <sustainLevel, <releaseTime;
  var <filterCutoff;
  var input, <outBus;
  var <clock;
  var server;

  *new { | outBus = 0,  relGroup = nil, addAction = 'addToTail' |
    ^super.new(1, 1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true}, { 0.01.wait; });

      sequencerDict = IdentityDictionary.new;
      tempo = 1;
      sequencerClock = TempoClock.new(tempo);
      attackTime = 0.05;
      decayTime = 0;
      sustainLevel = 1.0;
      releaseTime = 0.05;
      filterCutoff = 20000;

      this.prMakeSynthDefs;
      server.sync;
      isLoaded = true;
    }
  }

  ///////// Private Functions

  prMakeSynthDefs {

    SynthDef(\prm_shiftSequence_pitchShift, {
      |
      inBus, outBus, shiftAmount = 0, amp = 1, cutoff = 20000,
      attackTime = 0.05, decayTime = 0.05, sustainLevel = 1.0, releaseTime = 0.05, gate = 1
      |
      var int, input, filter, shift, envelope, sig;
      int = exp(0.057762265 * shiftAmount);
      input = In.ar(inBus);
      filter = LPF.ar(input, cutoff);
      shift = PitchShift.ar(filter, 0.1, int, 0.001, 0.04);
      envelope = EnvGen.ar(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime), gate, doneAction: 2);
      sig = shift *envelope;
      sig = sig * amp;
      sig = Out.ar(outBus, sig);
    }).add;

  }

  //////// Public Methods:

  free {
    sequencerClock.clear;
    sequencerClock.stop;
    sequencerClock = nil;
    sequencerDict.do({ | sequence | sequence.free; });
    this.freeProcessor;
  }

  setAttackTime { | attack = 0.05 |
    attackTime = attack;
    group.set(\attackTime, attackTime);
  }
  setDecayTime { | decay = 0 |
    decayTime = decay;
    group.set(\decayTime, decayTime);
  }
  setSustainLevel { | sustain = 1.0 |
    sustainLevel = sustain;
    group.set(\sustainLevel, sustainLevel);
  }
  setReleaseTime { | release = 0.05 |
    releaseTime = release;
    group.set(\releaseTime, releaseTime);
  }
  setFilterCutoff { | cutoff = 20000 |
    filterCutoff = cutoff;
    group.set(\cutoff, filterCutoff);
  }


  //////// sequencer:

  makeSequence { | uniqueName |
   {
      sequencerDict[uniqueName] = PatternSequencer.new(uniqueName, group, \addToHead);
      server.sync;
      sequencerDict[uniqueName].addKey(\instrument, \prm_shiftSequence_pitchShift);
      sequencerDict[uniqueName].addKey(\inBus, inBus);
      sequencerDict[uniqueName].addKey(\outBus, mixer.chanMono(0));
      sequencerDict[uniqueName].addKey(\attackTime, Pfunc({ attackTime}));
      sequencerDict[uniqueName].addKey(\decayTime, Pfunc({ decayTime }));
      sequencerDict[uniqueName].addKey(\sustainLevel, Pfunc({ sustainLevel }));
      sequencerDict[uniqueName].addKey(\releaseTime, Pfunc({ releaseTime }));
      sequencerDict[uniqueName].addKey(\amp, 1);
    }.fork;
  }

  addKey {  | uniqueName, key, action |
    sequencerDict[uniqueName].addKey(key, action);
  }

  playSequence { | uniqueName, clock = 'internal' |
    var playClock;
    if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
    sequencerDict[uniqueName].play(clock);
  }

  resetSequence { | uniqueName | sequencerDict[uniqueName].reset; }
  stopSequence { | uniqueName | sequencerDict[uniqueName].stop; }
  pauseSequence { | uniqueName | sequencerDict[uniqueName].pause }
  resumeSequence { | uniqueName | sequencerDict[uniqueName].resume; }
  isSequencePlaying { | uniqueName | ^sequencerDict[uniqueName].isPlaying }
  setSequenceQuant { | uniqueName, quant = 0 | sequencerDict[uniqueName].setQuant(quant) }

  setSequencerClockTempo { | bpm = 60 |
    var bps = bpm/60;
    tempo = bps;
    sequencerClock.tempo =tempo;
  }

}