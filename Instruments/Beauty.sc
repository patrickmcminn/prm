/*
Wednesday, September 12th 2018
Beauty.sc
prm

Based on an old lloopp patch
ppooll

"http://ppooll.klingt.org/"
*/

Beauty : IM_Module {

  var <isLoaded, server;
  var <monoOrStereo;

  var synth, <input, inputBus;

  var <delayTime1, <delayTime2, <delayTime3, <delayTime4;
  var <pan1, <pan2, <pan3, <pan4;
  var <lowPassCutoffLeft, <lowPassCutoffRight;
  var <resonanceLeft, <resonanceRight, <highPassCutoffLeft, highPassCutoffRight;
  var <averageLeft, <averageRight, <smoothLeft, <smoothRight, <divisor;

  var <isBypassed;

  *newMono {
    |
    outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = 'addToTail'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono;
  }

  *newStereo {
    |
    outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = 'addToTail'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo;
  }

  prInitMono {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      this.prAddSynthDefs;

      monoOrStereo = 'mono';
      this.prInitializeParameters;

      inputBus = Bus.audio(server, 1);

      server.sync;

      synth = Synth(\prm_beauty_mono, [\inBus, inputBus], group, \addToHead);
      while({ try { synth != nil } != true }, { 0.001.wait; });
      input = IM_Mixer_1Ch.new(inputBus, relGroup: group, addAction: \addToHead);
      while({ try { input.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    }
  }

  prInitStereo {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      this.prAddSynthDefs;

      monoOrStereo = 'stereo';
      this.prInitializeParameters;

      inputBus = Bus.audio(server, 2);

      server.sync;

      synth = Synth(\prm_beauty_stereo, [\inBus, inputBus], group, \addToHead);
      while({ try { synth != nil } != true }, { 0.001.wait; });
      input = IM_Mixer_1Ch.new(inputBus, relGroup: group, addAction: \addToHead);
      while({ try { input.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    }
  }

  prAddSynthDefs {
    SynthDef(\prm_beauty_mono, {

      |
      outBus = 0, inBus = 0,
      delayTime1 = 0.4, delayTime2 = 0.7, delayTime3 = 0.8, delayTime4 = 0.9,
      pan1 = -1, pan2 = -0.7, pan3 = 0.6, pan4 = 1,
      noiseAmp = 0.03, highPassCutoffLeft = 20, highPassCutoffRight = 60,
      lowPassCutoffLeft = 13000, lowPassCutoffRight = 15000, lowPassResLeft = 0, lowPassResRight = 0,
      averageLeft = 8000, averageRight = 14000,
      sampleRate = 44100,
      smoothLeft = 300, smoothRight = 400,
      divisor = 0.02, bypass = 0
      |

      var phaseLeft, phaseRight, phase, sig, local, localBufLeft, localBufRight;
      var delay1, delay2, delay3, delay4;
      var panner1, panner2, panner3, panner4;
      var hpf1, hpf2, hpf3, hpf4;
      var avg1, avg2, avg3, avg4;
      var feedback, feedbackLeft, feedbackRight, feedbackSum;
      var out, byp;

      localBufLeft = LocalBuf.new(SampleRate.ir *1, 1);
      localBufRight = LocalBuf.new(SampleRate.ir *1, 1);

      local = LocalIn.ar(2);
      sig = WhiteNoise.ar(noiseAmp) * Line.kr(0, 1, 0.1);

      phaseLeft = DelTapWr.ar(localBufLeft, sig +  local[0]);
      phaseRight = DelTapWr.ar(localBufRight, sig + local[1]);

      delay1 = DelTapRd.ar(localBufLeft, phaseLeft, delayTime1);
      delay2 = DelTapRd.ar(localBufLeft, phaseLeft, delayTime2);
      delay3 = DelTapRd.ar(localBufRight, phaseRight, delayTime3);
      delay4 = DelTapRd.ar(localBufRight, phaseRight, delayTime4);



      panner1 = Pan2.ar(delay1, pan1);
      panner2 = Pan2.ar(delay2, pan2);
      panner3 = Pan2.ar(delay3, pan3);
      panner4 = Pan2.ar(delay4, pan4);

      feedback = Mix.new([panner1, panner2, panner3, panner4] * 0.25);

      feedbackLeft = HPF.ar(feedback[0], highPassCutoffLeft);
      feedbackLeft = feedbackLeft *
      (divisor / (Lag.ar(AverageOutput.ar(abs(feedbackLeft), Impulse.kr((averageLeft/sampleRate).reciprocal)),
        smoothLeft/sampleRate).clip(0.0001, 1)));
      feedbackLeft = DFM1.ar(feedbackLeft, lowPassCutoffLeft, lowPassResLeft, noiselevel: 0);

      feedbackRight = HPF.ar(feedback[1], highPassCutoffRight);
      feedbackRight = feedbackRight *
      (divisor / (Lag.ar(AverageOutput.ar(abs(feedbackRight), Impulse.kr((averageRight/sampleRate).reciprocal)),
        smoothRight/sampleRate).clip(0.0001, 1)));
      feedbackRight = DFM1.ar(feedbackRight, lowPassCutoffRight, lowPassResRight, noiselevel: 0);


      feedbackSum = [feedbackLeft, feedbackRight];


      //feedbackSum = feedback * (0.02 / (Lag.ar(AverageOutput.ar(abs(feedback), Impulse.kr((8000/sampleRate).reciprocal)),
      //300/sampleRate).clip(0.0001, 1)));

      LocalOut.ar(feedbackSum + In.ar(inBus).dup);

      out = feedbackSum * (1-bypass);
      byp = In.ar(inBus).dup * bypass;
      out = out + byp;


      Out.ar(outBus, out);
    }).add;

    SynthDef(\prm_beauty_stereo, {

      |
      outBus = 0, inBus = 0,
      delayTime1 = 0.4, delayTime2 = 0.7, delayTime3 = 0.8, delayTime4 = 0.9,
      pan1 = -1, pan2 = -0.7, pan3 = 0.6, pan4 = 1,
      noiseAmp = 0.03, highPassCutoffLeft = 20, highPassCutoffRight = 60,
      lowPassCutoffLeft = 13000, lowPassCutoffRight = 15000, lowPassResLeft = 0, lowPassResRight = 0,
      averageLeft = 8000, averageRight = 14000,
      sampleRate = 44100,
      smoothLeft = 300, smoothRight = 400,
      divisor = 0.02, bypass = 0
      |

      var phaseLeft, phaseRight, phase, sig, local, localBufLeft, localBufRight;
      var delay1, delay2, delay3, delay4;
      var panner1, panner2, panner3, panner4;
      var hpf1, hpf2, hpf3, hpf4;
      var avg1, avg2, avg3, avg4;
      var feedback, feedbackLeft, feedbackRight, feedbackSum;
      var out, byp;

      localBufLeft = LocalBuf.new(SampleRate.ir *1, 1);
      localBufRight = LocalBuf.new(SampleRate.ir *1, 1);

      local = LocalIn.ar(2);
      sig = WhiteNoise.ar(noiseAmp) * Line.kr(0, 1, 0.1);

      phaseLeft = DelTapWr.ar(localBufLeft, sig +  local[0]);
      phaseRight = DelTapWr.ar(localBufRight, sig + local[1]);

      delay1 = DelTapRd.ar(localBufLeft, phaseLeft, delayTime1);
      delay2 = DelTapRd.ar(localBufLeft, phaseLeft, delayTime2);
      delay3 = DelTapRd.ar(localBufRight, phaseRight, delayTime3);
      delay4 = DelTapRd.ar(localBufRight, phaseRight, delayTime4);

      panner1 = Pan2.ar(delay1, pan1);
      panner2 = Pan2.ar(delay2, pan2);
      panner3 = Pan2.ar(delay3, pan3);
      panner4 = Pan2.ar(delay4, pan4);

      feedback = Mix.new([panner1, panner2, panner3, panner4] * 0.25);

      feedbackLeft = HPF.ar(feedback[0], highPassCutoffLeft);
      feedbackLeft = feedbackLeft *
      (divisor / (Lag.ar(AverageOutput.ar(abs(feedbackLeft), Impulse.kr((averageLeft/sampleRate).reciprocal)),
        smoothLeft/sampleRate).clip(0.0001, 1)));
      feedbackLeft = DFM1.ar(feedbackLeft, lowPassCutoffLeft, lowPassResLeft, noiselevel: 0);

      feedbackRight = HPF.ar(feedback[1], highPassCutoffRight);
      feedbackRight = feedbackRight *
      (divisor / (Lag.ar(AverageOutput.ar(abs(feedbackRight), Impulse.kr((averageRight/sampleRate).reciprocal)),
        smoothRight/sampleRate).clip(0.0001, 1)));
      feedbackRight = DFM1.ar(feedbackRight, lowPassCutoffRight, lowPassResRight, noiselevel: 0);


      feedbackSum = [feedbackLeft, feedbackRight];


      //feedbackSum = feedback * (0.02 / (Lag.ar(AverageOutput.ar(abs(feedback), Impulse.kr((8000/sampleRate).reciprocal)),
      //300/sampleRate).clip(0.0001, 1)));

      LocalOut.ar(feedbackSum + In.ar(inBus, 2));

      out = feedbackSum * (1-bypass);
      byp = In.ar(inBus, 2) * bypass;
      out = out + byp;

      Out.ar(outBus, out);
    }).add;

  }

  prInitializeParameters {

    isBypassed = false;

    delayTime1 = 0.4; delayTime2 = 0.7; delayTime3 = 0.8; delayTime4 = 0.9;
    pan1 = -1; pan2 = -0.7; pan3 = 0.6; pan4 = 1;
    lowPassCutoffLeft = 13000; lowPassCutoffRight = 15000;
    resonanceLeft = 0; resonanceRight = 0; highPassCutoffLeft = 0; highPassCutoffRight = 0;
    averageLeft = 8000; averageRight = 14000; smoothLeft = 300; smoothRight = 400; divisor = 0.02;

  }

  free {
    synth.free;
    input.free;
    inputBus.free;
  }

  inBus { ^input.inBus }

  //////// Parameter Setting:

  bypass {
    isBypassed = true;
    synth.set(\bypass, 1);
  }

  unBypass {
    isBypassed = false;
    synth.set(\bypass, 0);
  }

  tglBypass {
    if(isBypassed == true, { this.unBypass }, { this.bypass });
  }

  setDelayTimes { | d1 = 0.04, d2 = 0.003, d3 = 0.02, d4 = 0.03 |
    this.setDelayTime1(d1);
    this.setDelayTime2(d2);
    this.setDelayTime3(d3);
    this.setDelayTime4(d4);
  }

  setDelayTime1 { | time = 0.4 |
    if( time < 1,
      {
        delayTime1 = time;
        synth.set(\delayTime1, delayTime1);
      },
      {
        ^"Delay time > 1 will BLOW UP YOUR SYSTEM".postln;
    });
  }
  setDelayTime2 { | time = 0.4 |
    if( time < 1,
      {
        delayTime2 = time;
        synth.set(\delayTime2, delayTime2);
      },
      {
        ^"Delay time > 1 will BLOW UP YOUR SYSTEM".postln;
    });
  }
  setDelayTime3 { | time = 0.4 |
   if( time < 1,
      {
        delayTime3 = time;
        synth.set(\delayTime3, delayTime3);
      },
      {
        ^"Delay time > 1 will BLOW UP YOUR SYSTEM".postln;
    });
  }
  setDelayTime4 { | time = 0.4 |
    if( time < 1,
      {
        delayTime4 = time;
        synth.set(\delayTime4, delayTime4);
      },
      {
        ^"Delay time > 1 will BLOW UP YOUR SYSTEM".postln;
    });
  }

  setPan1 { | pan = 0 |
    pan1 = pan;
    synth.set(\pan1, pan1);
  }
  setPan2 { | pan = 0 |
    pan2 = pan;
    synth.set(\pan1, pan2);
  }
  setPan3 { | pan = 0 |
    pan3 = pan;
    synth.set(\pan1, pan3);
  }
  setPan4 { | pan = 0 |
    pan4 = pan;
    synth.set(\pan1, pan4);
  }

  setLowPassCutoffLeft { | cutoff = 13000 |
    lowPassCutoffLeft = cutoff;
    synth.set(\lowPassCutoffLeft, lowPassCutoffLeft);
  }
  setLowPassCutoffRight { | cutoff = 15000 |
    lowPassCutoffRight = cutoff;
    synth.set(\lowPassCutoffRight, lowPassCutoffRight);
  }
  setResonanceLeft { | res = 0 |
    resonanceLeft = res;
    synth.set(\lowPassResLeft, resonanceLeft);
  }
  setResonanceRight { | res = 0 |
    resonanceRight = res;
    synth.set(\lowPassResRight, resonanceRight);
  }

  setHighPassCutoffLeft { | cutoff = 80 |
    highPassCutoffLeft = cutoff;
    synth.set(\highPassCutoffLeft, cutoff);
  }
  setHighPassCutoffRight { | cutoff = 100 |
    highPassCutoffRight = cutoff;
    synth.set(\highPassCutoffRight, cutoff);
  }

  setAverageLeft { | average = 8000 |
    averageLeft = average;
    synth.set(\averageLeft, averageLeft);
  }
  setAverageRight { | average = 14000 |
    averageRight = average;
    synth.set(\averageRight, averageRight);
  }

  setSmoothLeft { | smooth = 300 |
    smoothLeft = smooth;
    synth.set(\smoothLeft, smooth);
  }
  setSmoothRight { | smooth = 400 |
    smoothRight = smooth;
    synth.set(\smoothRight, smooth);
  }

  setDivisor { | div = 0.02 |
    divisor = div;
    synth.set(\divisor, divisor);
  }


}