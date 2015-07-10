/*
5/20/2014
TrumpetDefault.sc
prm
*/

TrumpetNebula : IM_Processor {

  var <synth;
  var <inputIsMuted;
  var <isLoaded;

  *new {
    | outBus, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToTail' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup: relGroup, addAction: addAction).prInit;
  }

  // not working yet:
  /*
  *newCustom {
    | outBus, amp = 1,
    leftDelayTime = 0.3, rightDelayTime = 0.2, feedback = 0.5, delayAmp = 1,
    relGroup = nil, addAction = 'addToTail' |
  }
  */

  prInit { | outBus, amp = 1, relGroup = nil, addAction = 'addToTail' |
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDef;
      server.sync;
      while( { mixer.isLoaded == false }, { 0.0001.wait; });
      inputIsMuted = false;
      synth = Synth(\prm_trumpetDefault, [\inBus, inBus, \outBus, mixer.chanStereo(0)], group, \addToHead);
      isLoaded = true;
    };
  }

  /*
  prInitCustom {
    this.prAddSynthDef;
    server.sync;
    this.prMakeGroup;
    server.sync;
    this.prMakeSynthCustom;
  }
  */

  prAddSynthDef {
    SynthDef(\prm_trumpetDefault, {
      |
      inBus = 0, outBus = 0, amp = 1, balance = 0, inputMute = 1, mute = 1,
      pitchShift = 0,
      lowGain = -6, highGain = -6, lowFrequency = 250, highFrequency = 2500,
      reverbMix = 0.75, reverbRoom = 0.7, reverbDamp = 1,
      dist = 150, distAmp = 0.5, postDistortionCutoff = 6000,
      nebulaDepth = 25, nebulaActivity = 50,
      leftDelayTime = 0.748, rightDelayTime = 0.915, feedback = 0.5, delayAmp = 1,
      bpCenter = 1000, bw = 3,
      cutoff = 20000
      |
      var input, interval, pitchShifter, lowShelf, highShelf, reverb, distortion;
      var nebulaRange, nebulaOffset, nebulaRate, nebulaTrigger, nebulaLeft, nebulaRight, nebula;
      var bandPass, delayIn, delayLeft, delayRight;
      var filter, balancer, sig;

      input = In.ar(inBus) * inputMute;
      //interval = exp(0.057762265 * pitchShift);
      pitchShifter = PitchShift.ar(input, 0.05, pitchShift.midiratio, 0.001, 0.04);
      input = (input + pitchShifter)/2;
      //input = pitchShifter;

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
      nebulaLeft = Mix.fill(4, { (TRand.kr(0, nebulaRange, nebulaTrigger) + nebulaOffset).lag(0.3); });
      nebulaLeft = nebulaLeft / 4;
      nebulaLeft = distortion * nebulaLeft;
      nebulaRight = Mix.fill(4, { (TRand.kr(0, nebulaRange, nebulaTrigger) + nebulaOffset).lag(0.3); });
      nebulaRight = nebulaRight / 4;
      nebulaRight = distortion * nebulaRight;
      nebula = [nebulaLeft, nebulaRight];

      delayIn = (LocalIn.ar(2) * feedback) + nebula;
      delayLeft = DelayN.ar(BBandPass.ar(delayIn[0], bpCenter, bw), 3, leftDelayTime.lag2(0.2));
      delayRight = DelayN.ar(BBandPass.ar(delayIn[1], bpCenter, bw), 3, rightDelayTime.lag2(0.2));
      balancer = Balance2.ar(delayLeft, delayRight, balance);

      LocalOut.ar(balancer);

      filter = LPF.ar((balancer * delayAmp) + nebula, cutoff);
      filter = HPF.ar(filter, 40);
      sig = filter.softclip;
      sig = sig * amp;

      Out.ar(outBus, sig);
    }).add;
  }

  prMakeBus { inBus = Bus.audio; }
  prFreeBus { inBus.free; inBus = nil; }

  prMakeGroup { | relGroup = nil, addAction = 'addToTail' | group = Group.new(relGroup, addAction); }
  prFreeGroup { group.free; }

  prMakeSynthCustom { }
  prFreeSynthCustom { }

  //////// public functions:

  free {
    synth.free;
    synth = nil;
    this.freeProcessor;
  }

  muteInput {
    synth.set(\inputMute, 0);
    inputIsMuted = true;
  }
  unMuteInput {
    synth.set(\inputMute, 1);
    inputIsMuted = false;
  }
  toggleMuteInput {
    if( inputIsMuted == true, { this.unMuteInput }, { this.muteInput });
  }
  setCutoff { | cutoff = 20000 | synth.set(\cutoff, cutoff); }

  setLeftDelayTime { | delayTime = 0.748 | synth.set(\leftDelayTime, delayTime); }
  setRightDelayTime { | delayTime = 0.915 | synth.set(\rightDelayTime, delayTime); }
  setFeedback { | feedback = 0.5 | synth.set(\feedback, feedback); }
  setDelayFilterCenterFreq { | center = 1000 | synth.set(\bpCenter, center); }
  setDelayFilterBW { | bw = 1 | synth.set(\bw, bw); }
  setDelayVol { | vol = 0 | synth.set(\delayAmp, vol.dbamp); }

  setLowGain { | gain = -6 | synth.set(\lowGain, gain); }
  setHighGain { | gain = -6 | synth.set(\highGain, gain); }

  setDistortionAmount { | distortion = 150 | synth.set(\dist, distortion); }
  setDistortionGain { | gain = 0 | synth.set(\distAmp, gain.dbamp); }
  setPostDistortionCutoff { | cutoff = 20000 | synth.set(\postDistortionCutoff, cutoff); }

  setReverbMix { | mix = 0.75 | synth.set(\reverbMix, mix); }
  setReverbRoom { | room = 0.7 | synth.set(\reverbRoom, room); }
  setReverbDamp { | damp = 0.1 | synth.set(\reverbDamp, damp); }

  setPitchShiftAmount { | pitchShift = 0 | synth.set(\pitchShift, pitchShift) }

  setNebulaDepth { | depth = 25 | synth.set(\nebulaDepth, depth); }
  setNebulaActivity { | activity = 50 | synth.set(\nebulaActivity, activity); }

}