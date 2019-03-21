/*

11/7/2013
IM_Reverb.sc


NB: Pass the class an fx group that's held in IM_Mixer (should exist in front
of master channel).

6/6/2014
- breaking out the IR Dict into its own class, called IM_IRLibrary.
- adding constructor method to create non-convolution reverb

6/19/2014 - converting to new system

9/11/2015 - adding ability to access lowpass and highpass
*/

IM_Reverb : IM_Processor {

  var synth;
  var <isLoaded;
  var <lowPassFreq, <highPassFreq;
  var <preEQ, <postEQ;
  var <isEnabled;
  var reverbBus;
  var server;
  var isConvolution;

  *newConvolution { | outBus = 0, send0Bus = nil, send1Bus = nil,
    send2Bus = nil, send3Bus = nil, feedback = false, amp = 1,
    bufName = nil, fftMul = 2, relGroup = nil, addAction = 'addToTail' |

    ^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback,
      relGroup, addAction).prInitConvolution(bufName, fftMul);
  }

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    feedback = false, amp = 1, mix = 1, roomSize = 0.7, damp = 0.5,
    relGroup = nil, addAction = 'addToTail' |

    ^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback,
      relGroup, addAction).prInit(mix, roomSize, damp);
  }

  prInitConvolution { |bufName = nil, fftMul = 2|
    server = Server.default;

    //// legacy values:
    lowPassFreq = 15000;
    highPassFreq = 80;

    isEnabled = true;
    isConvolution = true;

    server.waitForBoot {
      isLoaded = false;
      this.prMakeSynthDefs;

      reverbBus = Bus.audio(server, 2);

      server.sync;

      while( { mixer.isLoaded.not }, { 0.001.wait; });

      postEQ = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { postEQ.isLoaded } != true }, { 0.001.wait; });

      synth = Synth(\IM_reverbConv, [\inBus, reverbBus, \outBus, postEQ.inBus,
        \buffer, bufName, \fftMul, fftMul], group, \addToHead);
      while( { synth == nil }, { 0.001.wait; });

      preEQ = Equalizer.newStereo(reverbBus, group, \addToHead);
      while({ try { preEQ.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    };
  }

  prInit { |mix = 1, roomSize = 0.7, damp = 0.5|
    var server = Server.default;

    lowPassFreq = 15000;
    highPassFreq = 80;
    isEnabled = true;
    isConvolution = false;

    server.waitForBoot {
      isLoaded = false;
      this.prMakeSynthDefs;
      server.sync;

      reverbBus = Bus.audio(server, 2);

      //while( { try { mixer.chanMono(0) } == nil }, { 0.01.wait } );
      while({ mixer.isLoaded.not }, { 0.001.wait; });

      postEQ = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { postEQ.isLoaded } != true }, { 0.001.wait; });

      synth = Synth(\IM_reverb, [\inBus, reverbBus, \outBus, postEQ.inBus, \mix, mix,
        \roomSize, roomSize, \damp, damp], group, \addToHead);
      while( { synth == nil }, { 0.001.wait; });

      preEQ = Equalizer.newStereo(reverbBus, group, \addToHead);
      while({ try { preEQ.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    };
  }

  prMakeSynthDefs {
    SynthDef(\IM_reverbConv, {
      | inBus = 0, outBus = 0, preAmp = 1, amp = 1,
      lowPassFreq = 15000, highPassFreq = 80, buffer, fftMul = 2, mute = 1 |

      var fftSize, input, leftConvolution, rightConvolution, sum, lowPass, highPass;
      var sig, lagTime;

      lagTime = 0.1;
      fftSize = 1024 * fftMul;

      input = In.ar(inBus, 2);
      input = input * preAmp;
      leftConvolution = PartConv.ar(input[0], fftSize, buffer);
      rightConvolution = PartConv.ar(input[1], fftSize, buffer);
      sum = [leftConvolution, rightConvolution];

      lowPass = LPF.ar(sum, lowPassFreq.lag(lagTime));
      highPass = HPF.ar(lowPass, highPassFreq.lag(lagTime));

      sig = highPass * amp.lag(lagTime);
      sig = sig * -10.dbamp * mute;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\IM_reverb, {
      | inBus = 0, outBus = 0, preAmp = 1, amp = 1,
      lowPassFreq = 15000, highPassFreq = 80, roomSize = 0.7, damp = 0.5, mix = 1, mute = 1 |

      var input, reverb, lowPass, highPass, sig, lagTime;

      lagTime = 0.1;

      input = In.ar(inBus, 2);
      input = input * preAmp;
      reverb = FreeVerb2.ar(input[0], input[1], mix, roomSize, damp);
      lowPass = LPF.ar(reverb, lowPassFreq);
      highPass = HPF.ar(lowPass, highPassFreq);
      sig = highPass * amp.lag(lagTime);
      sig = sig * mute;
      Out.ar(outBus, sig);
    }).add;
  }

  free {
    mixer.mute;
    reverbBus.free;
    synth.free;
    synth = nil;
    this.freeProcessor;
  }

  inBus { ^preEQ.inBus }

  setPreAmp { | amp = 1 |  synth.set(\preAmp, amp); }

  setLowPassFreq { | freq = 15000 |
    lowPassFreq = freq;
    synth.set(\lowPassFreq, lowPassFreq);
  }
  setHighPassFreq { | freq = 80 |
    highPassFreq = freq;
    synth.set(\highPassFreq, highPassFreq);
  }

  enable {
    synth.run(true);
    isEnabled = true;
  }
  disable {
    synth.run(false);
    isEnabled = false;
  }
  toggleEnable {
    if( isEnabled == true, { this.disable }, { this.enable });
  }

  setMix {  | mix = 1 |
    synth.set(\mix, mix);

  }

  changeIR { | bufName, fadeTime = 2, preAmp = 1, fftMul = 2 |
    {
      var oldVerb;
      var fadeOutBus, fadeInBus;
      fadeOutBus = Bus.control;
      fadeInBus = Bus.control;

      oldVerb = synth;
      synth = Synth(\IM_reverbConv, [\inBus, reverbBus, \outBus, postEQ.inBus,
        \buffer, bufName, \fftMul, fftMul, \amp, 0, \preAmp, preAmp], postEQ.synth, \addBefore);
      server.sync;

      oldVerb.map(\amp, fadeOutBus);
      synth.map(\amp, fadeInBus);

      server.sync;

      { Out.kr(fadeOutBus, Line.kr(1, 0, fadeTime, doneAction: 2)) }.play;
      { Out.kr(fadeInBus, Line.kr(0, 1, fadeTime, doneAction: 2)); }.play;

      { oldVerb.free }.defer(fadeTime);
    }.fork
  }

}