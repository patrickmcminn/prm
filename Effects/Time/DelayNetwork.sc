/*
Tuesday, December 10th 2013
DelayNetwork.sc
prm
*/

DelayNetwork {

  var server, group;
  var delayBus, faderBus, rampBus;
  var inputSynth, delayArray, faderSynth;
  var faderAmp;

  *new {
    |
    inBus = 0, outBus = 0, amp = 1, balance = 0, numDelays = 5, maxDelay = 6, delayTimeLow = 1, delayTimeHigh = 4,
    decayTimeLow = 1, decayTimeHigh = 1, filterType = 0, cutoffLow = 400, cutoffHigh = 1000, resLow = 0, resHigh = 0.5,
    panLow = -1, panHigh = 1, pitchShiftArray = 0, relGroup = nil, addAction = 'addToTail'
    |
   ^super.new.prInit(inBus, outBus, amp, balance, numDelays, maxDelay, delayTimeLow, delayTimeHigh,
      decayTimeLow, decayTimeHigh, filterType, cutoffLow, cutoffHigh, resLow, resHigh, panLow, panHigh,
      pitchShiftArray, relGroup, addAction);
  }

  prInit {
    |
    inBus = 0, outBus = 0, amp = 1, balance = 0, numDelays = 5, maxDelay = 6, delayTimeLow = 1, delayTimeHigh = 4,
    decayTimeLow = 1, decayTimeHigh = 1, filterType = 0, cutoffLow = 400, cutoffHigh = 1000, resLow = 0, resHigh = 0.5,
    panLow = -1, panHigh = 1, pitchShiftArray = 0, relGroup = nil, addAction = 'addToTail'
    |
    server = Server.default;
    server.waitForBoot {
      this.prAddSynthDefs;
      server.sync;
      this.prMakeGroup(relGroup, addAction);
      this.prMakeBusses;
      server.sync;
      this.prMakeSynths(inBus, outBus, amp, balance, numDelays, maxDelay, delayTimeLow, delayTimeHigh,
      decayTimeLow, decayTimeHigh, filterType, cutoffLow, cutoffHigh, resLow, resHigh, panLow, panHigh,
      pitchShiftArray);
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
      | inBus, dryOutBus, wetOutBus, dryAmp = 1, dryMute = 1, wetAmp = 1, wetMute = 1 |
      var input, sig;
      input = In.ar(inBus);
      Out.ar(dryOutBus, Pan2.ar(input * dryAmp * dryMute));
      Out.ar(wetOutBus, input * wetAmp * wetMute);
    }).add;


    SynthDef(\prm_DelayNetwork_StereoFader, {
      | inBus, outBus, amp = 0.6, balance = 0, mute = 1 |
      var input, bal, sig;
      var lagTime = 0.05;
      input = In.ar(inBus, 2);
      bal = Balance2.ar(input[0], input[1], balance, amp.lag(lagTime));
      sig = bal * mute;
      sig = Out.ar(outBus, sig);
    }).add;
  }

  prMakeGroup { | relGroup = nil, addAction = 'addToTail' |
    group = Group.new(relGroup, addAction);
  }

  prFreeGroup { group.free; }

  prMakeBusses {
    rampBus = Bus.control;
    delayBus = Bus.audio;
    faderBus = Bus.audio(server, 2);

  }

  prFreeBusses {
    rampBus.free;
    delayBus.free;
    faderBus.free;
  }

  prMakeSynths {
    | inBus = 0, outBus = 0, amp = 1, balance = 0, numDelays = 5, maxDelay = 6, delayTimeLow = 1, delayTimeHigh = 4,
    decayTimeLow = 1, decayTimeHigh = 1, filterType = 0, cutoffLow = 400, cutoffHigh = 1000, resLow = 0, resHigh = 0.5,
    panLow = -1, panHigh = 1, pitchShiftArray = 0
    |
    faderAmp = amp;

    faderSynth = Synth(\prm_DelayNetwork_StereoFader, [\inBus, faderBus, \outBus, outBus, \amp, faderAmp, \balance, balance],
      group, \addToTail);
    inputSynth = Synth(\prm_DelayNetwork_Input, [\inBus, inBus, \dryOutBus, faderBus, \wetOutBus, delayBus, \dryMute, 0],
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

  prFreeSynths {
    faderSynth.free;
    delayArray.do({ | synth | synth.free; });
    inputSynth.free;
  }

  //////// Public Functions:

  setInBus { | inBus = 0 |
    inputSynth.set(\inBus, inBus);
  }

  setAmp { | amp |
    faderAmp = amp;
    faderSynth.set(\amp, faderAmp);
    ^amp;
  }

  setVol { | vol |
    faderAmp = vol.dbamp;
    this.setAmp(faderAmp);
    ^vol;
  }

  setBalance { | balance |
    faderSynth.set(\balance, balance);
  }

  setDryAmp { | amp |
    inputSynth.set(\dryAmp, amp);
  }

  setDryVol { | vol |
    this.setDryAmp(vol.dbamp);
  }

  toggleDry {
    inputSynth.get(\dryMute, { | muteState |
      if(muteState == 0, { this.unMuteDry }, { this.muteDry });
    });
  }

  muteDry {
    inputSynth.set(\dryMute, 0);
  }

  unMuteDry {
    inputSynth.set(\dryMute, 1);
  }

  setInput { | inBus |
    inputSynth.set(\inBus, inBus);
    ^inBus;
  }

  setOutput { | outBus |
    faderSynth.set(\outBus, outBus);
    ^outBus;
  }

  getNumDelays {
    ^delayArray.size
  }

  addDelay { | delayTime = 1, decayTime = 1, maxDelay = 6, filterType = 0, cutoff = 10000, res = 0, pan = 0, pitchShift = 0 |
    delayArray.add(
      Synth(\prm_DelayNetwork, [\inBus, delayBus, \outBus, faderBus, \amp, 1/delayArray.size, \mix, 1, \maxDelay, maxDelay,
        \delayTime, delayTime, \decayTime, decayTime, \filterType, filterType, \cutoff, cutoff, \res, res,
        \pan, pan, \pitchShift, pitchShift], inputSynth, \addAfter);
    );
  }

  setDelayTime { | delayNum = 0, delayTime = 1 |
    if( delayArray.size > delayNum,
      { delayArray.at(delayNum).set(\delayTime, delayTime); },
      { ^"Delay Synth Does Not Exist at This Index"; });
  }

  setDecayTime { | delayNum = 0, decayTime = 1 |
    if( delayArray.size > delayNum,
      { delayArray.at(delayNum).set(\decayTime, decayTime); },
      { ^"Delay Synth Does Not Exist at This Index"; });
  }

  setCutoff { | delayNum = 0, cutoff = 1000 |
    if( delayArray.size > delayNum,
      { delayArray.at(delayNum).set(\cutoff, cutoff); },
      { ^"Delay Synth Does Not Exist at This Index"; });
  }

  setRes { | delayNum = 0, res = 0 |
    if( delayArray.size > delayNum,
      { delayArray.at(delayNum).set(\res, res); },
      { ^"Delay Synth Does Not Exist at This Index"; });
  }

  setPan { | delayNum = 0, pan = 0 |
    if( delayArray.size > delayNum,
      { delayArray.at(delayNum).set(\pan, pan); },
      { ^"Delay Synth Does Not Exist at This Index"; });
  }

  setPitchShift { | delayNum = 0, pitchShift = 0 |
    if( delayArray.size > delayNum,
      { delayArray.at(delayNum).set(\pitchShift, pitchShift); },
      { ^"Delay Synth Does Not Exist at This Index"; });
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

  randomizeRes { | resLow = 0, resHigh = 0.5 |
    delayArray.do({ | synth |
      synth.set(\res, rrand(resLow, resHigh));
    });
  }

  randomizePan { | panLow = -1, panHigh = 1 |
    delayArray.do({ | synth |
      synth.set(\pan, rrand(panLow, panHigh));
    });
  }

  setPitchShiftArray { | pitchShiftArray |
    delayArray.do({ | synth | synth.set(\pitchShift, pitchShiftArray.choose;) });
  }

  clearDelays {

    var delayRestoreArray = Array.newClear(delayArray.size);
    var decayRestoreArray = Array.newClear(delayArray.size);
    var waitTime = 2;

    faderSynth.set(\mute, 0);
    delayArray.do({ | synth, index |
      synth.get(\delayTime, { | val |
        delayRestoreArray[index] = val;
      });
      synth.get(\decayTime, { | val |
        decayRestoreArray[index] = val;
      });
      synth.set(\delayTime, 0, \decayTime, 0);
    });

    {
      delayArray.do({ | synth, index |
        synth.set(\delayTime, delayRestoreArray[index], \decayTime, decayRestoreArray[index]);
      });
      faderSynth.set(\mute, 1);
      faderSynth.set(\amp, faderAmp);
    }.defer(waitTime);

  }

  fadeOutDelays { | rampTime = 3 |
    { Out.kr(rampBus, Line.kr(faderAmp, 0, rampTime, doneAction: 2)); }.play;
    faderSynth.set(\amp, rampBus.asMap);
    { this.clearDelays }.defer(rampTime);
  }


  free {
    this.prFreeSynths;
    this.prFreeBusses;
    this.prFreeGroup;
  }


}
