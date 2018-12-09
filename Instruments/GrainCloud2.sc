/*
Saturday, February 1st 2014
GrainCloud2.sc
prm
*/

// this class makes an extraordinary number of busses.
// It might be best to increase the number of audio busses available on the server.
// ex:
// x = Server.default.options;
// x.numAudioBusChannels;

// this class is specifically for 12tet grainclouds
// using a more traditional keyboard layout

GrainCloud2 : IM_Module {

  var <isLoaded;
  var server, cloudGroup, envGroup;
  var cloudPatternArray, cloudEnvArray, cloudBusArray;
  var faderBus, faderSynth, output;

  var <instArray, <playingArray;
  var <trigRateLow, <trigRateHigh, <sustainLow, <sustainHigh, <ampLow, <ampHigh;
  var <cutoffLow, <cutoffHigh, <rqLow, <rqHigh, <panLow, <panHigh;
  var <attack, <release;

  var <ampdist,  <durdist,  <adparam,  <ddparam;
  var <ampscale, <durscale,  <knum;

  // temp:
  var cloudPattern;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = \addToHead |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot{
      isLoaded = false;
      this.prAddSynthDefs;
      server.sync;
      this.prMakeGroups;
      this.prMakeParameters;
      this.prMakePatternArray;
      this.prMakeBusses;
      server.sync;
      isLoaded = true;
    }
  }

  //////// Private Functions:

  prAddSynthDefs{

    // SynthDefs based on Microsound chapter in SuperCollider book by
    // by Alberto de Campo

   	// a gabor (approx. gaussian-shaped) grain
      SynthDef(\gaborSine, { | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0 |
        var snd = FSinOsc.ar(freq);
        var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
        var env = EnvGen.ar(Env.sine(sustain, amp2), doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

      // wider, quasi-gaussian envelope, with a hold time in the middle.
      SynthDef(\gaborWideSine, { |outBus, amp=0.1, freq=440, sustain=0.01, pan, width=0.5|
        var holdT = sustain * width;
        var fadeT = 1 - width * sustain * 0.5;
        var snd = FSinOsc.ar(freq);
        var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
        var env = EnvGen.ar(Env([0, 1, 1, 0], [fadeT, holdT, fadeT], \sin),
          levelScale: amp2,
          doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

      // a simple percussive envelope
      SynthDef(\percSine, { |outBus, amp=0.1, freq=440, sustain=0.01, pan|
        var snd = FSinOsc.ar(freq);
        var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
        var env = EnvGen.ar(
          Env.perc(0.1, 0.9, amp2),
          timeScale: sustain,
          doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

      // a reversed  percussive envelope
      SynthDef(\percRevSine, { |outBus, amp=0.1, freq=440, sustain=0.01, pan|
        var snd = FSinOsc.ar(freq);
        var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
        var env = EnvGen.ar(
          Env.perc(0.9, 0.1, amp2),
          timeScale: sustain,
          doneAction: 2
        );
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

      // an exponential decay envelope
      SynthDef(\expodecSine, { |outBus, amp=0.1, freq=440, sustain=0.01, pan|
        var snd = FSinOsc.ar(freq);
        var amp2 = AmpComp.ir(freq.max(50)) * 0.5 * amp;
        var env = XLine.ar(amp2, amp2 * 0.001, sustain, doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

      // a reversed exponential decay envelope
      SynthDef(\rexpodecSine, { |outBus, amp=0.1, freq=440, sustain=0.01, pan|
        var snd = FSinOsc.ar(freq);
        var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
        var env = XLine.ar(amp2 * 0.001, amp2, sustain, doneAction: 2)
        * (AmpComp.ir(freq) * 0.5);
        OffsetOut.ar(outBus, Pan2.ar(snd * env, pan));
      }, \ir ! 5).add;

    SynthDef(\gaborSaw, {
      | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0, cutoff = 5000, rq = 1 |
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
      var env = EnvGen.ar(Env.sine(sustain, amp2), doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

    SynthDef(\gaborWideSaw, {
      | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0, width = 0.5, cutoff = 5000, rq = 1 |
      var holdT = sustain * width;
      var fadeT = 1 - width * sustain * 0.5;
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
      var env = EnvGen.ar(Env([0, 1, 1, 0], [fadeT, holdT, fadeT], \sin),
          levelScale: amp2,
          doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

    SynthDef(\percSaw, {
      | outBus, amp=0.1, freq=440, sustain=0.01, pan = 0, cutoff = 5000, rq = 1  |
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
      var env = EnvGen.ar(
          Env.perc(0.1, 0.9, amp2),
          timeScale: sustain,
          doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

      // a reversed  percussive envelope
      SynthDef(\percRevSaw, {
      | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0, cutoff = 5000, rq = 1 |
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
      var env = EnvGen.ar(
          Env.perc(0.9, 0.1, amp2),
          timeScale: sustain,
          doneAction: 2
        );
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

      // an exponential decay envelope
      SynthDef(\expodecSaw, {
      | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0, cutoff = 5000, rq = 1 |
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = AmpComp.ir(freq.max(50)) * 0.5 * amp;
      var env = XLine.ar(amp2, amp2 * 0.001, sustain, doneAction: 2);
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

      // a reversed exponential decay envelope
      SynthDef(\rexpodecSaw, {
      | outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0, cutoff = 5000, rq = 1 |
      var snd = Saw.ar(freq);
      var filter = RLPF.ar(snd, cutoff, rq);
      var amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
      var env = XLine.ar(amp2 * 0.001, amp2, sustain, doneAction: 2)
        * (AmpComp.ir(freq) * 0.5);
        OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
      }, \ir ! 5).add;

    SynthDef(\gaborGendy, {
      |
      outBus, amp = 0.1, freq = 440, sustain = 0.01, pan = 0, cutoff = 5000, rq = 1,
      ampdist = 1, durdist = 1, adparam = 1, ddparam = 1, ampscale = 0.5, durscale = 0.5, knum = 12
      |
      var snd, filter, amp2, env;
      snd = Gendy3.ar(ampdist, durdist, adparam, ddparam, freq, ampscale, durscale, 12, knum);
      //snd = Gendy3.ar(1, 1, 1, 1, freq, 0.5, 0.5, 12);
      filter = RLPF.ar(snd, cutoff, rq);
      amp2 = amp * AmpComp.ir(freq.max(50)) * 0.5;
      env = EnvGen.ar(Env.sine(sustain, amp2), doneAction: 2);
      OffsetOut.ar(outBus, Pan2.ar(filter * env, pan));
    }, \ir ! 5).add;

    SynthDef(\PRM_stereoEnv, {
      | inBus = 0, outBus = 0, amp = 1, balance = 0, attack = 0.05, release = 0.05, gate = 1 |
      var input, bal, env, sig;
      input = In.ar(inBus, 2);
      bal = Balance2.ar(input[0], input[1], balance);
      env = EnvGen.ar(Env.asr(attack, amp, release, 0), gate, doneAction: 2);
      sig = bal * env;
      Out.ar(outBus, sig);
    }).add;

  }

  prMakeGroups {
    //group = Group.new(relGroup, addAction);
    envGroup = Group.new(group, \addToHead);
    cloudGroup = Group.new(group, \addToHead);

  }

  prFreeGroups {
    //group.free;
    cloudGroup.free;
    envGroup.free;
  }


  prMakeParameters {
   // |
   // durLow, durHigh, sustainLow, sustainHigh, ampLow, ampHigh,
   // cutoffLow, cutoffHigh, rqLow, rqHigh, panLow, panHigh
   // |
    instArray = ['gaborSine'];
    playingArray = Array.fill(84, { false });
    trigRateLow = 20;
    trigRateHigh = 100;
    sustainLow = 0.1;
    sustainHigh = 0.3;
    ampLow = 0.1;
    ampHigh = 0.2;
    cutoffLow = 3000;
    cutoffHigh = 5000;
    rqLow = 1;
    rqHigh = 0.3;
    panLow = -1;
    panHigh = 1;
    attack = 0.05;
    release = 0.05;
    ampdist = 1; durdist = 1; adparam = 1; ddparam = 1;
    ampscale = 0.5; durscale = 0.5; knum = 12;
  }

  prMakeBusses {
    cloudBusArray = Array.fill(84, { Bus.audio };);
    faderBus = Bus.audio(server, 2);
  }

  prFreeBusses {
    cloudBusArray.do({ | bus | bus.free; });
    faderBus.free;
  }

  prMakePatternArray {
    cloudPatternArray = Array.newClear(84);
    cloudEnvArray = Array.newClear(84);
    /*
    cloudPattern = Pbind(
      \outBus, faderBus,
      \group, faderSynth,
      \addAction, \addBefore,
      \instrument, Prand(instArray, inf),
      \note, Prand(noteArray, inf),
      \octave, Prand(octaveArray, inf),
      \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).play;
    */
  }

  //////// Public Functions:

  free {
    cloudPatternArray.do({ | pattern | pattern.stop; });
    instArray = instArray.drop(instArray.size);
    instArray = nil;
    this.prFreeBusses;
  }

  setAttack { | atk = 0.05 |
    attack = atk;
  }

  setRelease { | rel = 0.05 |
    release = rel;
    envGroup.set(\release, release);
  }

  allNotesOff {
    var patternArray = Array.newClear(84);
    playingArray.do({ | item | item = false; });
    cloudEnvArray.do({ | synth | synth.set(\gate, 0); });
    patternArray.do({ | item, index | item = cloudPatternArray[index]; });
    { patternArray.do({ | pattern | pattern.stop; }); }.defer(release);
  }

  allNotesOffHard {
    cloudPatternArray.do({ | pattern | pattern.stop; });
    cloudEnvArray.do({ | synth | synth.free; });
    playingArray.do({ | item | item = false; });
  }

  toggleNote { | note |
    if( playingArray[note] == false, { this.addNote(note); }, { this.removeNote(note); });
  }

  addNote { | note |
    playingArray[note] = true;
    cloudEnvArray[note] = Synth(\PRM_stereoEnv,
      [\inBus, cloudBusArray[note], \outBus, mixer.chanStereo(0), \attack, attack, \release, release],
      envGroup, \addToTail);
    cloudPatternArray[note] = Pbind(
      \outBus, cloudBusArray[note],
      \group, cloudGroup,
      \addAction, \addToTail,
      \instrument, Prand(instArray, inf),
      \note, note,
      \octave, 1,
      \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
      \sustain, Pwhite(sustainLow, sustainHigh, inf),
      \amp, Pwhite(ampLow, ampHigh, inf),
      \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
      \rq, Pwhite(rqLow, rqHigh, inf),
      \pan, Pwhite(panLow, panHigh, inf)
    ).play;
  }

  removeNote { | note |
    var pattern;
    playingArray[note] = false;
    cloudEnvArray[note].set(\release, release);
    cloudEnvArray[note].set(\gate, 0);
    pattern = cloudPatternArray[note];
    { pattern.stop; }.defer(release);
  }

  setInstArray { | array |
    instArray = array;
    cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setTrigRateLow { | low |
    trigRateLow = low;
        cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setTrigRateHigh { | high |
    trigRateHigh = high;
        cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setTrigRate { | low, high |
    this.setTrigRateLow(low);
    this.setTrigRateHigh(high);
  }

  setSustainLow { | low |
    sustainLow = low;
        cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setSustainHigh { | high |
    sustainHigh = high;
        cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setSustain { | low, high |
    this.setSustainLow(low);
    this.setSustainHigh(high);
  }

  setPatternAmpLow { | low |
    ampLow = low;
        cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setPatternAmpHigh { | high |
    ampHigh = high;
       cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setPatternAmp { | low, high |
    this.setPatternAmpLow(low);
    this.setPatternAmpHigh(high);
  }

  setCutoffLow { | low |
    cutoffLow = low;
        cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setCutoffHigh { | high |
    cutoffHigh = high;
        cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setCutoff { | low, high |
    this.setCutoffLow(low);
    this.setCutoffHigh(high);
  }

  setRQLow { | low |
    rqLow = low;
        cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setRQHigh { | high |
    rqHigh = high;
        cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setRQ { | low, high |
    this.setRQLow(low);
    this.setRQHigh(high);
  }

  setPanLow { | low |
    panLow = low;
        cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setPanHigh { | high |
    panHigh = high;
        cloudPatternArray.do({ | pattern, index |
      if( playingArray[index] == true,
        {
          pattern.stream = Pbind(
            \outBus, cloudBusArray[index],
            \group, cloudGroup,
            \addAction, \addToTail,
            \instrument, Prand(instArray, inf),
            \note, index,
            \octave, 1,
            \dur, Pwhite((1/trigRateLow), (1/trigRateHigh), inf),
            \sustain, Pwhite(sustainLow, sustainHigh, inf),
            \amp, Pwhite(ampLow, ampHigh, inf),
            \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
            \rq, Pwhite(rqLow, rqHigh, inf),
            \pan, Pwhite(panLow, panHigh, inf)
          ).asStream;
      });
    });
  }

  setPan { | low, high |
    this.setPanLow(low);
    this.setPanHigh(high);
  }

  /*
  toggleNote { | note |
    if( noteArray.includes(note),
      {
        noteArray.remove(note);
        if( noteArray.isEmpty, { noteArray = noteArray.add(\r) }, { noteArray.remove(\r) });
        cloudPattern.stream = Pbind(
          \outBus, faderBus,
          \group, faderSynth,
          \addAction, \addBefore,
          \instrument, Prand(instArray, inf),
          \note, Prand(noteArray, inf),
          \octave, Prand(octaveArray, inf),
          \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
          \sustain, Pwhite(sustainLow, sustainHigh, inf),
          \amp, Pwhite(ampLow, ampHigh, inf),
          \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
          \rq, Pwhite(rqLow, rqHigh, inf),
          \pan, Pwhite(panLow, panHigh, inf)
        ).asStream;
        ^noteArray;
      },
      {
        noteArray = noteArray.add(note);
        if( noteArray.isEmpty, { noteArray = noteArray.add(\r) }, { noteArray.remove(\r) });
        cloudPattern.stream = Pbind(
          \outBus, faderBus,
          \group, faderSynth,
          \addAction, \addBefore,
          \instrument, Prand(instArray, inf),
          \note, Prand(noteArray, inf),
          \octave, Prand(octaveArray, inf),
          \dur, Pwhite(1/trigRateLow, 1/trigRateHigh, inf),
          \sustain, Pwhite(sustainLow, sustainHigh, inf),
          \amp, Pwhite(ampLow, ampHigh, inf),
          \cutoff, Pwhite(cutoffLow, cutoffHigh, inf),
          \rq, Pwhite(rqLow, rqHigh, inf),
          \pan, Pwhite(panLow, panHigh, inf)
        ).asStream;
        ^noteArray;
      }
    );
  }
  */
}
