/*
Friday, November 15th 2013
MicroCluster.sc
prm
*/

MicroCluster : IM_Processor {

  var <isLoaded;
  var trigBus;
  var triggerSynth, drySynth, pitchShiftArray;

  *new {
    |
    outBus = 0, amp = 1, numPitchShifts = 12,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, feedback = false,
    relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(1, 2, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInit(numPitchShifts);
  }

  prInit { | numPitchShifts = 12 |
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true}, { 0.01.wait; });
      server.sync;
      this.prMakeSynthDefs;
      server.sync;
      trigBus = Bus.audio(Server.default);
      server.sync;
      this.prMakeSynths(numPitchShifts);
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
      amp = 1, rangeLo = -1.0, rangeHi = 1.0, pan = 0, cutoff = 15000 |
      var input, triggerInput, trigger, trigShift, interval, pitchShift, filter, sig;
      var lagTime = 0.05;
      input = In.ar(inBus);
      triggerInput = In.ar(trigBus);
      trigger = Select.ar(trigSelect, [Impulse.ar(trigRate.lag(lagTime)), Dust.ar(trigRate.lag(lagTime)), triggerInput]);
      trigShift = Demand.ar(trigger, 0, Dwhite(rangeLo, rangeHi, inf));
      interval = exp(0.057762265 * trigShift);
      pitchShift = PitchShift.ar(input, 0.2, interval);
      filter = LPF.ar(pitchShift, cutoff.lag(lagTime));
      sig = Pan2.ar(filter, pan, amp.lag(lagTime));
      //sig = filter * amp.lag(lagTime);
      sig = Out.ar(outBus, sig);
    }).add;

  }


  prMakeSynths { | numPitchShifts = 12 |
    pitchShiftArray = Array.fill(numPitchShifts, {
      Synth(\PRM_triggerShifter, [\inBus, inBus, \outBus, mixer.chanStereo(1), \trigBus, trigBus,
        \amp, 1/(numPitchShifts), \pan, rrand(-1, 1)], group, \addToHead);
    });
    drySynth = Synth(\PRM_dryOutput, [\inBus, inBus, \outBus, mixer.chanMono(0)], group, \addToTail);
    triggerSynth = Synth(\PRM_externalImpulse, [\outBus, trigBus], group, \addToHead);
  }

  prFreeSynths {
    triggerSynth.free;
    drySynth.free;
    pitchShiftArray.do({ | synth | synth.free; });
  }

  ////////////// Public Methods:

  setDryVol{ | vol = 0 | mixer.setVol(0, vol); }
  setWetVol { | vol = 0 | mixer.setVol(1, vol); }

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


  free {
    this.prFreeSynths;
    trigBus.free;
    this.freeProcessor;
  }
}