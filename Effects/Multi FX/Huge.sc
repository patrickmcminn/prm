/*
Sunday, September 28th 2014
Huge.sc
(needs a better name)
prm
*/

Huge : IM_Processor {

  var <isLoaded;
  var server;

  *new { | outBus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, relGroup: relGroup, addAction: addAction).prInit
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDef;
    };
  }

  prAddSynthDef {

    SynthDef(\prm_huge, {
      |
      inBus = 0, outBus = 0, amp = 1, initialPitchShift = 0.5, initialMix = 0.75,
      amplitudeMul = 1
      subOscRatio = 0.25, subOscWaveform = 0, subRectWidth = 0.5,
      subOscCutoffLow = 100, subOscCutoffHigh = 20000
      |
      var input, shift, inputMix, hasFreq, freq, amplitude;
      var subFreq, subSine, subTri, subSaw, subRect, subOscSelect, subOscCutoffScale, subOscFilter, subOsc;
      input = In.ar(inBus);
      shift = PitchShift.ar(input, 0.05, initialPitchShift);
      inputMix = Mix.ar([input*(1-initialMix), shift*initialMix]);

      #freq, hasFreq = Pitch.kr(input, ampThreshold: 0.02, median: 7);
      amplitude = Amplitude.ar(input, 0.01, 0.07);
      amplitude = amplitude * amplitudeMul;

      subFreq = freq * subOscRatio;
      subSine = SinOsc.ar(subFreq);
      subTri = LFTri.ar(subFreq);
      subSaw = Saw.ar(subFreq);
      subRect = Pulse.ar(subFreq, subRectWidth);
      subOscSelect = SelectX.ar(subOscWaveform, [subSine, subTri, subSaw, subRect]);
      subOscCutoffScale = amplitude.linexp(0, 1, subOscCutoffLow, subOscCutoffHigh);
      subOscFilter = LPF.ar(subOsc, subOscCutoffScale);
      subOsc = subOscFilter * amplitude;





    }).add;

  }

}