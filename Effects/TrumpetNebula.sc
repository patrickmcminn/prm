/*
5/20/2014
TrumpetDefault.sc
prm
*/

TrumpetNebula {

  var server, group, <inBus, <synth;

  *new {
    | outBus, amp = 1, relGroup = nil, addAction = 'addToTail' |
    ^super.new.prInit(outBus, amp, relGroup, addAction);
  }

  *newCustom {
    | outBus, amp = 1,
    leftDelayTime = 2.748, rightDelayTime = 0.915, feedback = 0.5, delayAmp = 1,
    relGroup = nil, addAction = 'addToTail' |
  }

  prInit { | outBus, amp = 1, relGroup = nil, addAction = 'addToTail' |
    server = Server.default;
    server.waitForBoot {
      this.prAddSynthDef;
      this.prMakeGroup(relGroup, addAction);
      this.prMakeBus;
      server.sync;
      this.prMakeSynth(outBus, amp);
    };
  }

  prInitCustom {
    this.prAddSynthDef;
    server.sync;
    this.prMakeGroup;
    server.sync;
    this.prMakeSynthCustom;
  }

  prAddSynthDef {
    SynthDef(\prm_trumpetDefault, {
      |
      inBus = 0, outBus = 0, amp = 1, balance = 0, inputMute = 1, mute = 1,
      pitchShift = 0,
      lowGain = -6, highGain = -6, lowFrequency = 250, highFrequency = 2500,
      reverbMix = 0.75, reverbRoom = 0.7, reverbDamp = 0.1,
      dist = 150, distAmp = 1, postDistortionCutoff = 20000,
      nebulaDepth = 25, nebulaActivity = 50,
      leftDelayTime = 0.748, rightDelayTime = 0.915, feedback = 0.5, delayAmp = 1,
      bpCenter = 1000, bw = 1,
      cutoff = 20000
      |
      var input, pitchShifter, lowShelf, highShelf, reverb, distortion;
      var nebulaRange, nebulaOffset, nebulaRate, nebulaTrigger, nebulaLeft, nebulaRight, nebula;
      var bandPass, delayIn, delayLeft, delayRight;
      var filter, balancer, sig;

      input = In.ar(inBus);
      pitchShifter = PitchShift.ar(input, 0.05, pitchShift.midiratio);
      input = (input + pitchShift)/2;

      lowShelf = BLowShelf.ar(input, lowFrequency, db: lowGain);
      highShelf = BHiShelf.ar(lowShelf, highFrequency, db: highGain);

      reverb = FreeVerb.ar(highShelf, reverbMix, reverbRoom, reverbDamp);

      distortion = (reverb * dist).distort;
      distortion = LPF.ar(distortion, postDistortionCutoff);
      distortion = distortion * distAmp;

      nebulaRange = nebulaDepth * 0.01;
      nebulaOffset = 0.945 - nebulaRange + (nebulaRange / 5);
      nebulaRate = ( (( nebulaActivity / 100 ) - 1) * (-500 + 11)) / 1000;
      nebulaTrigger = Impulse.kr(1/nebulaRate);
      nebulaLeft = Mix.fill(4, { (TRand.kr(0, nebulaRange, nebulaTrigger) + nebulaOffset).lag(0.7); });
      nebulaLeft = nebulaLeft / 4;
      nebulaLeft = distortion * nebulaLeft;
      nebulaRight = Mix.fill(4, { (TRand.kr(0, nebulaRange, nebulaTrigger) + nebulaOffset).lag(0.7); });
      nebulaRight = nebulaRight / 4;
      nebulaRight = distortion * nebulaRight;
      nebula = [nebulaLeft, nebulaRight];

      // need to add BandPass Filter:
      delayIn = (LocalIn.ar(2) * feedback) + nebula;
      delayLeft = DelayN.ar(BBandPass.ar(delayIn[0], bpCenter, bw), 3, leftDelayTime);
      delayLeft = delayLeft * delayAmp;
      delayRight = DelayN.ar(BBandPass.ar(delayIn[1], bpCenter, bw), 3, rightDelayTime);
      delayRight = delayRight * delayAmp;
      balancer = Balance2.ar(delayLeft, delayRight, balance);

      LocalOut.ar(balancer);

      filter = LPF.ar(balancer + nebula, cutoff);
      sig = filter.softclip;
      sig = sig * amp;

      Out.ar(outBus, sig);
    }).add;
  }

  prMakeBus { inBus = Bus.audio; }
  prFreeBus { inBus.free; inBus = nil; }

  prMakeGroup { | relGroup = nil, addAction = 'addToTail' | group = Group.new(relGroup, addAction); }
  prFreeGroup { group.free; }

  prMakeSynth { | outBus, amp = 1 |
    synth = Synth(\prm_trumpetDefault, [\inBus, inBus, \outBus, outBus, \amp, amp], group);
  }
  prFreeSynth { synth.free; }

  prMakeSynthCustom { }
  prFreeSynthCustom { }

  //////// public functions:

  free {
    synth.free;
    group.free;
  }

  setVol { | vol = 0 | synth.set(\amp, vol.dbamp); }
  setOutput { | outBus = 0 | synth.set(\outBus, outBus); }
  setCutoff { | cutoff = 20000 | synth.set(\cutoff, cutoff); }

  setLeftDelayTime { | delayTime = 0.748 | synth.set(\leftDelayTime, delayTime); }
  setRightDelayTime { | delayTime = 0.915 | synth.set(\rightDelayTime, delayTime); }
  setFeedback { | feedback = 0.5 | synth.set(\feedback, feedback); }

  setLowGain { | gain = -6 | synth.set(\lowGain, gain); }
  setHighGain { | gain = -6 | synth.set(\highGain, gain); }

  setDistortionAmount { | distortion = 150 | synth.set(\dist, distortion); }
  setDistortionGain { | gain = 0 | synth.set(\distAmp, 0.dbamp); }
  setPostDistortionCutoff { | cutoff = 20000 | synth.set(\postDistortionCutoff, cutoff); }

  setDelayFilterCenterFreq { | center = 1000 | synth.set(\bpCenter, center); }
  setDelayFilterBW { | bw = 1 | synth.set(\bw, bw); }

  setReverbMix { | mix = 0.75 | synth.set(\reverbMix, mix); }
  setReverbRoom { | room = 0.7 | synth.set(\reverbRoom, room); }
  setReverbDamp { | damp = 0.1 | synth.set(\reverbDamp, damp); }

}