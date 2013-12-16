/*
Friday, November 15th 2013
MicroCluster.sc
prm
*/

MicroCluster {

  var input;
  var trigBus, faderBus, <outBus, nilBus;
  var triggerSynth, drySynth, pitchShiftArray, faderSynth;

  *new { | inBus, amp = 1, numPitchShifts = 12, rangeLo = -1.0, rangeHi = 1.0,
    trigType = 0, trigRate = 5, dryAmp = 0, cutoff = 15000, pan = 0, group = nil |
    ^super.new.prInit(inBus, amp, numPitchShifts, rangeLo, rangeHi, trigType, trigRate, dryAmp, cutoff, pan, group);

  }

  prInit { | inBus, amp = 1, numPitchShifts = 12, rangeLo = -1.0, rangeHi = 1.0,
    trigType = 0, trigRate = 5, dryAmp = 0, cutoff = 15000, pan = 0, group = nil |
    var server = Server.default;
    server.waitForBoot {
      server.sync;
      this.prMakeSynthDefs;
      server.sync;
      this.prMakeBusses;
      server.sync;
      this.prMakeSynths(inBus, amp, numPitchShifts, rangeLo, rangeHi, trigType, trigRate, dryAmp, cutoff, pan, group);
      server.sync;
    }
  }

  ///////////// Private Functions:

  prMakeSynthDefs {

    SynthDef(\PRM_externalImpulse, {
      | outBus, trigRate = 1 |
      var trigger, sig;
      trigger = Impulse.ar(trigRate);
      sig = Out.ar(outBus, trigger);
    }).add;

    SynthDef(\PRM_dryOutput, {
      | inBus, outBus, amp = 1, pan = 0, mute = 1 |
      var input, sig;
      input = In.ar(inBus);
      //sig = Pan2.ar(input, pan, amp);
      sig = input * amp;
      sig = sig * mute;
      sig = Out.ar(outBus, sig);
    }).add;

    SynthDef(\PRM_triggerShifter, {
      | inBus, outBus, trigBus, trigSelect = 1, trigRate = 5,
      amp = 1, rangeLo = -1.0, rangeHi = 1.0, pan = 0, cutoff = 15000, mute = 1 |
      var input, triggerInput, trigger, trigShift, interval, pitchShift, filter, sig;
      var lagTime = 0.05;
      input = In.ar(inBus);
      triggerInput = In.ar(trigBus);
      trigger = Select.ar(trigSelect, [Impulse.ar(trigRate.lag(lagTime)), Dust.ar(trigRate.lag(lagTime)), triggerInput]);
      trigShift = Demand.ar(trigger, 0, Dwhite(rangeLo, rangeHi, inf));
      interval = exp(0.057762265 * trigShift);
      pitchShift = PitchShift.ar(input, 0.2, interval);
      filter = LPF.ar(pitchShift, cutoff.lag(lagTime));
      //sig = Pan2.ar(filter, 0, amp.lag(lagTime));
      sig = filter * amp.lag(lagTime);
      sig = sig * mute;
      sig = Out.ar(outBus, sig);
    }).add;

    SynthDef(\PRM_fader, {
      | inBus, outBus, amp = 0.6, bal = 0, mute = 1 |
      var input, balance, sig;
      var lagTime = 0.05;
      input = In.ar(inBus);
      //balance = Balance2.ar(input[0], input[1], bal, amp.lag(lagTime));
      sig = input * amp;
      sig = sig * mute;
      sig = Out.ar(outBus, sig);
    }).add;

  }

  prMakeBusses {
    trigBus = Bus.audio(Server.default);
    faderBus = Bus.audio(Server.default);
    outBus = Bus.audio(Server.default);
    //nilBus = Bus.audio(Server.default, 2);
  }

  prFreeBusses {
    trigBus.free;
    faderBus.free;
    outBus.free;
  }

  prMakeSynths {
    | inBus, outBus, amp = 1, numPitchShifts = 12, rangeLo = -1.0, rangeHi = 1.0,
    trigType = 0, trigRate = 5, dryAmp = 0, cutoff = 15000, pan = 0, group = nil |
    triggerSynth = Synth(\PRM_externalImpulse, [\outBus, trigBus, \trigRate, trigRate], group, \addToTail);
    drySynth = Synth(\PRM_dryOutput, [\inBus, inBus, \outBus, faderBus, \amp, dryAmp], group, \addToTail);
    pitchShiftArray = Array.fill(numPitchShifts, {
      Synth(\PRM_triggerShifter, [\inBus, inBus, \outBus, faderBus, \trigBus, trigBus, \amp, 1/(numPitchShifts/2), \trigSelect, trigType,
        \trigRate, trigRate, \rangeLo, rangeLo, \rangeHi, rangeHi, \pan, pan, \cutoff, cutoff], group, \addToTail);
    });
    faderSynth = Synth(\PRM_fader, [\inBus, faderBus, \outBus, outBus, \balance, pan, \amp, amp], group, \addToTail);

  }

  prFreeSynths {
    triggerSynth.free;
    drySynth.free;
    pitchShiftArray.do({ | synth | synth.free; });
    faderSynth.free;
  }

  ////////////// Public Methods:

  setAmp { | amp = 0.5 |
    faderSynth.set(\amp, amp);
  }

  setVol { | vol |
    faderSynth.set(\amp, vol.dbamp);
  }

  setDryAmp { | amp |
    drySynth.set(\amp, amp);
  }

  setDryVol { | vol |
    drySynth.set(\amp, vol.dbamp);
  }

  setWetAmp { | amp |
    pitchShiftArray.do({ | synth | synth.set(\amp, amp/(pitchShiftArray.size/2)) });
  }

  setWetVol { | vol |
    pitchShiftArray.do({ | synth | synth.set(\amp, vol.dbamp/(pitchShiftArray.size/2)) });
  }

  setCutoff { | cutoff = 15000 |
    pitchShiftArray.do({ | synth | synth.set(\cutoff, cutoff) });
  }

  setRange { | rangeLo, rangeHi |
    pitchShiftArray.do({ | synth | synth.set(\rangeLo, rangeLo, \rangeHi, rangeHi) });
  }

  setRangeLo { | rangeLo |
    pitchShiftArray.do({ | synth | synth.set(\rangeLo, rangeLo); });
  }

  setRangeHi { | rangeHi |
    pitchShiftArray.do({ | synth | synth.set(\rangeHi, rangeHi); });
  }

  setTrigRate { | trigRate |
    triggerSynth.set(\trigRate, trigRate);
    pitchShiftArray.do({ | synth | synth.set(\trigRate, trigRate); });
  }

  setTrigType { | trigType |
    pitchShiftArray.do({ | synth | synth.set(\trigSelect, trigType); });
  }

  randomizeTrigRate { | rangeLo, rangeHi |
    pitchShiftArray.do({ | synth | synth.set(\trigRate, rrand(rangeLo, rangeHi) ); });
  }

  tglMute {
    faderSynth.get(\mute, { | mute |
      if( mute == 0, { faderSynth.set(\mute, 1); }, { faderSynth.set(\mute, 0) });
    });
  }

  mute {
    faderSynth.set(\mute, 0);
  }

  unMute {
    faderSynth.set(\mute, 1);
  }

  tglMuteDry {
    drySynth.get(\mute, { | mute |
      if( mute == 0, { drySynth.set(\mute, 1); }, { drySynth.set(\mute, 0) });
    });
  }

  muteDry {
    drySynth.set(\mute, 0);
  }

  unMuteDry {
    drySynth.set(\mute, 1);
  }

  tglMuteWet {
    pitchShiftArray.do({ | synth |
      synth.get(\mute, { | mute |
        if( mute == 0, { synth.set(\mute, 1); }, { synth.set(\mute, 0) }); });
    });
  }

  muteWet {
    pitchShiftArray.do({ | synth | synth.set(\mute, 0); });
  }

  unMuteWet {
    pitchShiftArray.do({ | synth | synth.set(\mute, 1); });
  }

  free {
    this.prFreeSynths;
    this.prFreeBusses;
  }
}