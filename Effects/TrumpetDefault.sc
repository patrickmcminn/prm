/*
5/20/2014
TrumpetDefault.sc
prm
*/

TrumpetDefault {

  var server, group, synth;

  *new {
    | inBus, outBus, amp = 1, relGroup = nil, addAction = 'addToTail' |
    ^super.new.prInit(inBus, outBus, amp, relGroup, addAction);
  }

  *newCustom {
    | inBus, outBus, amp = 1,
    leftDelayTime = 2.748, rightDelayTime = 0.915, feedback = 0.5, delayAmp = 1,
    relGroup = nil, addAction = 'addToTail' |
  }

  prInit { | inBus, outBus, amp = 1, relGroup = nil, addAction = 'addToTail' |
    server = Server.default;
    server.waitForBoot {
      this.prAddSynthDef;
      server.sync;
      this.prMakeGroup;
      //this.prMakeBus;
      server.sync;
      this.prMakeSynth;
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
      leftDelayTime = 2.748, rightDelayTime = 0.915, feedback = 0.5, delayAmp = 1,
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
      delayLeft = DelayN.ar(delayIn[0], 3, leftDelayTime);
      delayLeft = delayLeft * delayAmp;
      delayRight = DelayN.ar(delayIn[1], 3, rightDelayTime);
      delayRight = delayRight * delayAmp;

      LocalOut.ar(nebula);

      balancer = Balance2.ar(delayLeft, delayRight, balance);
      filter = LPF.ar(balancer, cutoff);
      sig = filter * amp;

      Out.ar(outBus, sig);
    }).add;
  }

  // prMakeBus { }

  prMakeGroup { | relGroup = nil, addAction = 'addToTail' | group = Group.new(relGroup, addAction); }
  prFreeGroup { group.free; }

  prMakeSynth { | inBus, outBus, amp = 1 |
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

}