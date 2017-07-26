/*
Monday, July 24th 2017
Boy_Piano.sc
prm
*/

Boy_Piano : IM_Module {

  var <isLoaded;
  var server;

  var <reverb, <eq;

  var noteDict;

  var <sequencerDict, <sequencerClock, <tempo;

  var <section1IsPlaying, <section2IsPlaying, <section3IsPlaying;


  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      this.prAddSynthDef;

      reverb = IM_Reverb.new(mixer.chanStereo(0), amp: 1, mix: 0.3, roomSize: 0.7, damp: 0.2,
        relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(reverb.inBus, group, \addToHead);
      while({ try { eq.isLoaded } != true },  { 0.001.wait; });

      sequencerDict = IdentityDictionary.new;
      sequencerClock = TempoClock.new;

      server.sync;

      this.prSetInitialParameters;

      this.makeSequence(\section1);
      this.makeSequence(\section2);
      this.makeSequence(\section3);

      server.sync;

      this.prMakePatternParameters;

      mixer.setPreVol(6);

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_pianoFade, {
      | outBus, freq = 220, amp = 0.5, attackTime = 1, sustainTime = 6,
      cutoff = 1500, lowGain = 6 |

      var decay, env, filter, loShelf, delayTime, detune, strike, hammerEnv, hammer, sum, sig;

      decay = attackTime + sustainTime;
      env = EnvGen.kr(Env.linen(attackTime, sustainTime, 0));
      strike = TDuty.ar(Dseq([decay], 1), doneAction: 2);
      hammerEnv = Decay2.ar(strike, 0.008, 0.004);
      sum = Mix.ar(Array.fill(3, { | i |
        detune = #[-0.05, 0, 0.05].at(i);
        delayTime = 1 / (freq + detune);
        hammer = LFNoise2.ar(1300, hammerEnv);
        CombL.ar(hammer,
          delayTime,
          delayTime,
          decay);
      }));
      filter = LPF.ar(sum, cutoff);
      loShelf = BLowShelf.ar(filter, 200, 1, lowGain);
      sig = loShelf * env;
      sig = sig * amp;
      sig = Out.ar(outBus, sig);
    }).add;
  }

  prSetInitialParameters {
    eq.setLowFreq(250);
    eq.setLowGain(2);
    eq.setLowPassCutoff(150);
    section1IsPlaying = false;
    section2IsPlaying = false;
    section3IsPlaying = false;
  }

  prMakePatternParameters {
    var gMajor7, eMajor7, dMajor7, aMajor;
    gMajor7 = [0, 12, 16, 19, 23];
    eMajor7 = [-3, 9, 13, 16, 20];
    dMajor7 = [-5, 7, 11, 14, 18];
    aMajor = [-10, 2, 9, 14, 18];

    //// section 1:
    this.addKey(\section1, \amp, 1);
    this.addKey(\section1, \cutoff, 1000);
    this.addKey(\section1, \loGain, 6);
    this.addKey(\section1, \legato, 1);
    this.addKey(\section1, \root, 7);
		this.addKey(\section1, \note, Pstutter(
			Pseq([16, 14, 12, 9], inf),
			Pseq([gMajor7, eMajor7, dMajor7, aMajor], inf)
    ));
		this.addKey(\section1, \dur, Pstutter(
			Pseq([16, 14, 12, 9], inf),
			Pseq([0.25, 0.25, 1/3, 0.5], inf)
    ));
    this.addKey(\section1, \attackTime, 0.12);
    this.addKey(\section1, \sustainTime, Pkey(\dur) * 4.5);
    this.addKey(\section1, \octave, 3);

    //// section 2:
    this.addKey(\section2, \amp, 1.2);
    this.addKey(\section2, \cutoff, 1000);
    this.addKey(\section2, \loGain, 6);
    this.addKey(\section2, \legato, 1);
    this.addKey(\section2, \root, 7);
		this.addKey(\section2, \note, Pstutter(
			Pseq([16, 14, 18, 7, 12, 9, 5, 1, 8], inf),
			Pseq([gMajor7, eMajor7, gMajor7, eMajor7, dMajor7, aMajor, gMajor7, gMajor7, dMajor7], inf)
    ));
		this.addKey(\section2, \dur, Pstutter(
			Pseq([16, 14, 18, 7, 12, 9, 5, 1, 8], inf),
			Pseq([0.25, 0.25, 0.25, 0.5, 1/3, 0.5, 0.75, 0.25, 0.5], inf)
    ));
    this.addKey(\section2, \attackTime, 0.07);
    this.addKey(\section2, \sustainTime, Pkey(\dur) * 5);
    this.addKey(\section2, \octave, 3);

    //// section 3:
    this.addKey(\section3, \amp, 1);
    this.addKey(\section3, \cutoff, 800);
    this.addKey(\section3, \loGain, 6);
    this.addKey(\section3, \legato, 1);
    this.addKey(\section3, \root, 7);
		this.addKey(\section3, \note, Pstutter(
			Pseq([16, 14, 18, 7], inf),
			Pseq([gMajor7, eMajor7], inf)
    ));
		this.addKey(\section3, \dur, Pstutter(
			Pseq([16, 14, 18, 7], inf),
			Pseq([0.25, 0.25, 0.25, 0.5], inf)
    ));
    this.addKey(\section3, \attackTime, 0.12);
    this.addKey(\section3, \sustainTime, Pkey(\dur) * 4.5);
    this.addKey(\section3, \octave, 3);
  }

  //////// public functions:
  free {
    reverb.free;
    eq.free;
    this.freeModule;
    isLoaded = false;
  }

   ///////// Pattern Sequencer:
  makeSequence { | name |
    fork {
      sequencerDict[name] = IM_PatternSeq.new(name, group, \addToHead);
      sequencerDict[name].stop;
      server.sync;
      sequencerDict[name].addKey(\instrument, \prm_pianoFade);
      sequencerDict[name].addKey(\outBus, eq.inBus);
      //sequencerDict[name].addKey(\amp, 0.5);
    };
  }

  addKey {  | name, key, action |
    sequencerDict[name].addKey(key, action);
  }

  playSequence { | name, clock = 'internal', quant = 'nil' |
    var playClock;
    if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
    sequencerDict[name].play(playClock);
  }

  resetSequence { | name | sequencerDict[name].reset; }
  stopSequence { | name | sequencerDict[name].stop; }
  pauseSequence { | name | sequencerDict[name].pause }
  resumeSequence { | name | sequencerDict[name].resume; }
  isSequencePlaying { | name | ^sequencerDict[name].isPlaying }
  setSequenceQuant { | name, quant = 0 | sequencerDict[name].setQuant(quant) }

  setSequencerClockTempo { | bpm = 60 |
    var bps = bpm/60;
    tempo = bps;
    sequencerClock.tempo = tempo;
  }

  playSection1 { |clock |
    this.playSequence(\section1, clock);
    section1IsPlaying = true;
  }
  stopSection1 {
    this.stopSequence(\section1);
    section1IsPlaying = false;
  }

  playSection2 { |clock |
    this.playSequence(\section2, clock);
    section2IsPlaying = true;
  }
  stopSection2 {
    this.stopSequence(\section2);
    section2IsPlaying = false;
  }

  playSection3 { |clock |
    this.playSequence(\section3, clock);
    section3IsPlaying = true;
  }
  stopSection3 {
    this.stopSequence(\section3);
    section3IsPlaying = false;
  }

}