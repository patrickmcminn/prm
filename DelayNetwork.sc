/*
Tuesday, December 10th 2013
DelayNetwork.sc
prm
*/

DelayNetwork {

  var server;
  var delayBus, faderBus;
  var inputSynth, delayArray, faderSynth;

  *new {
    |
    inBus = 0, outBus = 0, amp = 1, balance = 0, numDelays = 5, maxDelay = 6, delayTimeLow = 1, delayTimeHigh = 4,
    decayTimeLow = 1, decayTimeHigh = 1, filterType = 0, cutoffLow = 400, cutoffHigh = 1000, resLow = 0, resHigh = 0.5,
    panLow = -1, panHigh = 1, pitchShiftArray = 0, group = nil, addAction = 'addToTail'
    |
   ^super.new.prInit(inBus, outBus, amp, balance, numDelays, maxDelay, delayTimeLow, delayTimeHigh,
      decayTimeLow, decayTimeHigh, filterType, cutoffLow, cutoffHigh, resLow, resHigh, panLow, panHigh,
      pitchShiftArray, group, addAction);
  }

  prInit {
    |
    inBus = 0, outBus = 0, amp = 1, balance = 0, numDelays = 5, maxDelay = 6, delayTimeLow = 1, delayTimeHigh = 4,
    decayTimeLow = 1, decayTimeHigh = 1, filterType = 0, cutoffLow = 400, cutoffHigh = 1000, resLow = 0, resHigh = 0.5,
    panLow = -1, panHigh = 1, pitchShiftArray = 0, group = nil, addAction = 'addToTail'
    |
    server = Server.default;
    server.waitForBoot {
      this.prAddSynthDefs;
      server.sync;
      this.prMakeBusses;
      server.sync;
      this.prMakeSynths(inBus, outBus, amp, balance, numDelays, maxDelay, delayTimeLow, delayTimeHigh,
      decayTimeLow, decayTimeHigh, filterType, cutoffLow, cutoffHigh, resLow, resHigh, panLow, panHigh,
      pitchShiftArray, group, addAction);
    };
  }

  prAddSynthDefs {
    SynthDef(\prm_DelayNetwork, {
      |
      inBus, outBus, amp = 1, mix = 0.5,
      maxDelay = 6, delayTime = 2, decayTime = 5,
      filterType = 0, cutoff = 10000, res = 0,
      pitchShift = 0, pan = 0
      |
      var input, dry, delay, filter, int, pitchShifter, sig;
      input = In.ar(inBus);
      dry = input * (1-mix);
      delay = CombC.ar(input, maxDelay, delayTime, decayTime);
      filter = DFM1.ar(delay, cutoff, res, 1, filterType);
      int = exp(0.057762265 * pitchShift);
      pitchShifter = PitchShift.ar(filter, 0.1, int, 0.001, 0.04);

      sig = pitchShifter * mix;
      sig = sig + dry;
      sig = sig * amp;
      sig = Pan2.ar(sig, pan);
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_DelayNetwork_Input, {
      | inBus, dryOutBus, wetOutBus, dryMute = 1, wetMute = 1 |
      var input, sig;
      input = In.ar(inBus);
      Out.ar(dryOutBus, Pan2.ar(input * dryMute));
      Out.ar(wetOutBus, Pan2.ar(input * wetMute));
    }).add;


    SynthDef(\prm_DelayNetwork_StereoFader, {
      | inBus, outBus, amp = 0.6, balance = 0, mute = 1 |
      var input, bal, sig;
      var lagTime = 0.05;
      input = In.ar(inBus, 2);
      bal = Balance2.ar(input[0], input[1], balance, amp.lag(lagTime));
      sig = input * amp;
      sig = sig * mute;
      sig = Out.ar(outBus, sig);
    }).add;
  }

  prMakeBusses {
    delayBus = Bus.audio;
    faderBus = Bus.audio(server, 2);

  }

  prKillBusses {
    delayBus.free;
    faderBus.free;
  }

  prMakeSynths {
    | inBus = 0, outBus = 0, amp = 1, balance = 0, numDelays = 5, maxDelay = 6, delayTimeLow = 1, delayTimeHigh = 4,
    decayTimeLow = 1, decayTimeHigh = 1, filterType = 0, cutoffLow = 400, cutoffHigh = 1000, resLow = 0, resHigh = 0.5,
    panLow = -1, panHigh = 1, pitchShiftArray = 0, group = nil, addAction = 'addToTail'
    |
    faderSynth = Synth(\prm_DelayNetwork_StereoFader, [\inBus, faderBus, \outBus, outBus, \amp, amp, \balance, balance],
      group, addAction);
    inputSynth = Synth(\prm_DelayNetwork_Input, [\inBus, inBus, \dryOutBus, faderBus, \wetOutBus, delayBus],
      faderSynth, \addBefore);
    delayArray = Array.fill(numDelays, {
      var shift;
      if( pitchShiftArray.isArray, { shift = pitchShiftArray.choose; }, { shift = pitchShiftArray });
      Synth(\prm_DelayNetwork, [\inBus, delayBus, \outBus, faderBus, \amp, 1/numDelays, \mix, 1, \maxDelay, maxDelay,
        \delayTime, rrand(delayTimeLow, delayTimeHigh), \decayTime, rrand(decayTimeLow, decayTimeHigh),
        \filterType, filterType, \cutoff, exprand(cutoffLow, cutoffHigh), \res, rrand(resLow, resHigh),
        \pan, rrand(panLow, panHigh), \pitchShift, shift], inputSynth, \addAfter);
    });
  }

  prKillSynths {
    faderSynth.free;
    delayArray.do({ | synth | synth.free; });
    inputSynth.free;
  }

  //////// Public Functions:

  setAmp { | amp |
    faderSynth.set(\amp, amp);
    ^amp;
  }

  setVol { | vol |
    this.setAmp(vol.dbamp);
    ^vol;
  }

  setBalance { | balance |
    faderSynth.set(\balance, balance);
  }

  setInput { | inBus |
    inputSynth.set(\inBus, inBus);
    ^inBus;
  }

  setOutput { | outBus |
    faderSynth.set(\outBus, outBus);
    ^outBus;
  }

  addDelay { | delayTime = 1, decayTime = 1, maxDelay = 6, filterType = 0, cutoff = 10000, res = 0, pan = 0, pitchShift = 0 |
    delayArray.add(
      Synth(\prm_DelayNetwork, [\inBus, delayBus, \outBus, faderBus, \amp, 1/delayArray.size, \mix, 1, \maxDelay, maxDelay,
        \delayTime, delayTime, \decayTime, decayTime, \filterType, filterType, \cutoff, cutoff, \res, res,
        \pan, pan, \pitchShift, pitchShift], inputSynth, \addAfter);
    );
  }

  setDelayTime { | delayNum = 0, delayTime = 1 |
    delayArray.at(delayNum).set(\delayTime, delayTime);
  }

  setDecayTime { | delayNum = 0, decayTime = 1 |
    delayArray.at(delayNum).set(\decayTime, decayTime);
  }

  randomizeParameters {

    |
    delayTimeLow = 1, delayTimeHigh = 4, decayTimeLow = 1, decayTimeHigh = 1, cutoffLow = 400, cutoffHigh = 1000,
    resLow = 0, resHigh = 0.5, panLow = -1, panHigh = 1, pitchShiftArray = 0
    |

    delayArray.do({ | synth |
      var shift;
      if( pitchShiftArray.isArray, { shift = pitchShiftArray.choose; }, { shift = pitchShiftArray });
      synth.set(\delayTime, rrand(delayTimeLow, delayTimeHigh), \decayTime, rrand(decayTimeLow, decayTimeHigh),
        \cutoff, exprand(cutoffLow, cutoffHigh), \res, rrand(resLow, resHigh),
        \pan, rrand(panLow, panHigh), \pitchShift, shift);
    });
    ^"parameters randomized";
  }

  randomizeDelayTime { | delayTimeLow = 1, delayTimeHigh = 4 |
    delayArray.do({ | synth |
      synth.set(\delayTime, rrand(delayTimeLow, delayTimeHigh));
    });
  }

  randomizeDecayTime { | decayTimeLow = 1, decayTimeHigh = 1.5 |
    delayArray.do({ | synth |
      synth.set(\decayTime, rrand(decayTimeLow, decayTimeHigh));
    });
  }

  randomizeCutoff { | cutoffLow = 700, cutoffHigh = 2500 |
    delayArray.do({ | synth |
      synth.set(\cutoff, exprand(cutoffLow, cutoffHigh));
    });
  }

}
