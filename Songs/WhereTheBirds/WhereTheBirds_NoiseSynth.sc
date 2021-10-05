/*
Friday, June 7th 2019
WhereTheBirds_NoiseSynth.sc
prm


lightly adapted from noiseSynth.scd
Friday, November 2nd, 2012
*/

WhereTheBirds_NoiseSynth : IM_Module {

  var <isLoaded, server;
  var <granulator, <lfo, <eq;

  var path, <bufferArray;

  var patternDict, mode;

  var attack, sustain, release;

  var <phrase1IsPlaying, <phrase2IsPlaying, <phrase3IsPlaying, <phrase4IsPlaying;

  *new { | outBus, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      patternDict = ();
      this.prAddSynthDef;

      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Instruments/NoiseSynth/";
      bufferArray = Array.fill(53, { | i |
        Buffer.read(server, path ++ (i + 32) ++ ".wav"); });

			eq = Equalizer.newStereo(mixer.chanStereo, group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			granulator = GranularDelay2.new(eq.inBus, group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      lfo = LFO.new(0.14, 'sine', -0.7, 0.7, group, \addToHead);
      while({ try { lfo.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      granulator.mixer.mapPan(lfo.outBus);

      this.prInitializeParameters;
      this.prMakeSequences;

      isLoaded = true;
    }
  }

  prAddSynthDef {

    SynthDef(\prm_birds_playBufStereo, {
      | bufName, out, rate = 1, loop = 0, startPos = 0, amp = 1,
      attack = 0.05, sustain = 1, release = 0.05, pan = 0 |
      var play, sig, env;
      play = PlayBuf.ar(2, bufName, rate, 1, startPos, loop);
      env = EnvGen.kr(Env.linen(attack, sustain, release), 1, doneAction: 2);
      sig =  env * play;
      sig = Pan2.ar(sig, pan);
      sig = sig * amp;
      sig = Out.ar(out, sig);
    }).add;
  }

  prInitializeParameters {
    mixer.setPreVol(-12);

    phrase1IsPlaying = false;
    phrase2IsPlaying = false;
    phrase3IsPlaying = false;
    phrase4IsPlaying = false;

    mode = Scale.aeolian;
    attack = 0.05;
    sustain = 0.5;
    release = 3;

    granulator.setMix(1);
    granulator.setGrainDur(0.4, 0.6);
    granulator.setTrigRate(45);
    granulator.setDelayTime(1);
    granulator.setFeedback(0.35);
    granulator.setDelayLevel(0.4);
		granulator.setGrainEnvelope('gabWide');

		eq.setLowPassCutoff(2450);
  }

  prMakeSequences {

    patternDict = (

      \phrase1: (
        \one: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.4, 0.7, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 4,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \loop, 1,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \two: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.4, 0.7, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 4,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \three: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.4, 0.7, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 4,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([6, 4], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \four: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.4, 0.7, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 4,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \five: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.4, 0.7, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 6,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \six: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.4, 0.7, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 4,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \seven: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.4, 0.7, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 4,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([6, 4], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \eight: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.4, 0.7, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 4,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \nine: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.4, 0.7, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 4,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([6, 6], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \ten: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.4, 0.7, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 4/3,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) }))
      ),

      \phrase2: (
        \one: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.5, 0.75, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 2,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \loop, 1,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \two: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.5, 0.75, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 2,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \three: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.5, 0.75, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 2,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([6, 4], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \four: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.5, 0.75, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 2,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \five: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.5, 0.75, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 3,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \six: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.5, 0.75, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 4,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \seven: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.5, 0.75, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 2,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([6, 4], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \eight: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.5, 0.75, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 6,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \nine: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.5, 0.75, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 3,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([6, 6], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \ten: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.5, 0.75, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 2/3,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) }))
      ),

      \phrase3: (
        \one: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.55, 0.8, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 1,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \loop, 1,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \two: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.55, 0.8, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 1,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \three: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.55, 0.8, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 1,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([6, 4], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \four: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.55, 0.8, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 1,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \five: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.55, 0.8, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 1.5,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \six: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.55, 0.8, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 2,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \seven: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.55, 0.8, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 1,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([6, 4], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \eight: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.55, 0.8, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 3,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \nine: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.55, 0.8, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 1.5,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([6, 6], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \ten: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.55, 0.8, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 1/3,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) }))
      ),
      \phrase4: (
        \one: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.8, 0.85, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 0.5,
          \attack, attack,
          \sustain, sustain,
          \release, release/2,
          \loop, 1,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \two: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.8, 0.85, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 0.5,
          \attack, attack,
          \sustain, sustain,
          \release, release/2,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \three: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.8, 0.85, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 0.5,
          \attack, attack,
          \sustain, sustain,
          \release, release/2,
          \degree, Pseq([6, 4], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \four: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.8, 0.85, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 0.5,
          \attack, attack,
          \sustain, sustain,
          \release, release/2,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \five: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.8, 0.85, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 0.75,
          \attack, attack,
          \sustain, sustain,
          \release, release/2,
          \degree, Pseq([2, 1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \six: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.8, 0.85, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 1,
          \attack, attack,
          \sustain, sustain,
          \release, release,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \seven: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.8, 0.85, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 0.5,
          \attack, attack,
          \sustain, sustain,
          \release, release/2,
          \degree, Pseq([6, 4], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \eight: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.8, 0.85, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 1.5,
          \attack, attack,
          \sustain, sustain,
          \release, release/2,
          \degree, Pseq([1], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \nine: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.8, 0.85, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 0.75,
          \attack, attack,
          \sustain, sustain,
          \release, release/2,
          \degree, Pseq([6, 6], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) })),

        \ten: Pbind(
          \instrument, \prm_birds_playBufStereo,
          \out, granulator.inBus,
          \group, granulator.group,
          \addAction, \addBefore,
          \amp, Pwhite(0.8, 0.85, inf),
          \scale, mode,
          \root, 6,
          \legato, 1,
          \octave, 4,
          \dur, 1/6,
          \attack, attack,
          \sustain, sustain,
          \release, release/2,
          \degree, Pseq([7, 2], 1),
          \freq, Pfunc { | ev | ev.use(ev[\freq]) },
          \bufName, Pfunc({ | ev | bufferArray.at((ev[\freq].cpsmidi)-32) }))
      )
    );
    Pdef(\birds_phrase1, Pdict(patternDict.phrase1,
      Prand([\one, \two, \three, \four, \five, \six, \seven, \eight, \nine, \ten], inf)));
    Pdef(\birds_phrase2, Pdict(patternDict.phrase2,
      Prand([\one, \two, \three, \four, \five, \six, \seven, \eight, \nine, \ten], inf)));
    Pdef(\birds_phrase3, Pdict(patternDict.phrase3,
      Prand([\one, \two, \three, \four, \five, \six, \seven, \eight, \nine, \ten], inf)));
    Pdef(\birds_phrase4, Pdict(patternDict.phrase4,
      Prand([\one, \two, \three, \four, \five, \six, \seven, \eight, \nine, \ten], inf)));

  }

  //////// public functions:

  free {
    this.stopPhrase1;
    this.stopPhrase2;
    this.stopPhrase3;
    this.stopPhrase4;
		eq.free;
    granulator.free;
    bufferArray.size.do({ | i | i.free; });
    this.freeModule;
  }

  togglePhrase1 { if( phrase1IsPlaying == true, { this.stopPhrase1 }, { this.playPhrase1 }); }
  playPhrase1 {
    Pdef(\birds_phrase1).play;
    phrase1IsPlaying = true;
  }
  stopPhrase1 {
    Pdef(\birds_phrase1).stop;
    phrase1IsPlaying = false;
  }

  togglePhrase2 { if( phrase2IsPlaying == true, { this.stopPhrase2 }, { this.playPhrase2 }); }
  playPhrase2 {
    Pdef(\birds_phrase2).play;
    phrase2IsPlaying = true;
  }
  stopPhrase2 {
    Pdef(\birds_phrase2).stop;
    phrase2IsPlaying = false;
  }

  togglePhrase3 { if( phrase3IsPlaying == true, { this.stopPhrase3 }, { this.playPhrase3 }); }
  playPhrase3 {
    Pdef(\birds_phrase3).play;
    phrase3IsPlaying = true;
  }
  stopPhrase3 {
    Pdef(\birds_phrase3).stop;
    phrase3IsPlaying = false;
  }

  togglePhrase4 { if( phrase4IsPlaying == true, { this.stopPhrase4 }, { this.playPhrase4 }); }
  playPhrase4 {
    Pdef(\birds_phrase4).play;
    phrase4IsPlaying = true;
  }
  stopPhrase4 {
    Pdef(\birds_phrase4).stop;
    phrase4IsPlaying = false;
  }




}