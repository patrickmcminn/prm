/*
Saturday, June 28th 2014
Subtractive.sc
prm
*/

Subtractive : IM_Module {

  var lfo, <voiceArray, <orderArray, lfoBus, maxVoices, <numVoices, <orderNum, spilloverSynth;
  var nodeWatcher;

  var applyMode;

  var lfoFreqLFOBottomRatio, lfoFreqLFOTopRatio;
  var lfoWaveform, lfoFreq, lfoPulseWidth;
  var lfoEnvType, lfoAttackTime, lfoReleaseTime;

  var osc1OctaveMul;
  var osc1FreqEnvStartRatio, osc1FreqEnvEndRatio, osc1FreqEnvTime;
  var osc1FreqLFOBottomRatio, osc1FreqLFOTopRatio, osc1FreqLFO2BottomRatio, osc1FreqLFO2TopRatio;
  var osc1PulseWidthLFOBottom, osc1PulseWidthLFOTop, osc1PulseWidthLFO2Bottom, osc1PulseWidthLFO2Top;
  var osc1AmpLFOBottom, osc1AmpLFOTop, osc1AmpLFO2Bottom, osc1AmpLFO2Top;
  var osc1WaveformLFOBottom, osc1WaveformLFOTop, osc1WaveformLFO2Bottom, osc1WaveformLFO2Top;
  var osc1Waveform, osc1PulseWidth, osc1Amp, osc1SubAmp;

  var osc2OctaveMul, osc2DetuneCents;
  var osc2FreqEnvStartRatio, osc2FreqEnvEndRatio, osc2FreqEnvTime;
  var osc2FreqLFOBottomRatio, osc2FreqLFOTopRatio, osc2FreqLFO2BottomRatio, osc2FreqLFO2TopRatio;
  var osc2PulseWidthLFOBottom, osc2PulseWidthLFOTop, osc2PulseWidthLFO2Bottom, osc2PulseWidthLFO2Top;
  var osc2AmpLFOBottom, osc2AmpLFOTop, osc2AmpLFO2Bottom, osc2AmpLFO2Top;
  var osc2WaveformLFOBottom, osc2WaveformLFOTop, osc2WaveformLFO2Bottom, osc2WaveformLFO2Top;
  var osc2Waveform, osc2PulseWidth, osc2Amp, osc2SubAmp;

  var noiseOscAmpLFOBottom, noiseOscAmpLFOTop, noiseOscAmpLFO2Bottom, noiseOscAmpLFO2Top;
  var noiseOscFilterLFOBottomRatio, noiseOscFilterLFOTopRatio, noiseOscFilterLFO2BottomRatio, noiseOscFilterLFO2TopRatio;
  var noiseOscAmp, noiseOscCutoff;

  var filterEnvAttackRatio, filterEnvPeakRatio, filterEnvSustainRatio, filterEnvReleaseRatio;
  var filterEnvAttackTime, filterEnvDecayTime, filterEnvReleaseTime;
  var filterEnvLoop;
  var filterCutoffLFOBottomRatio, filterCutoffLFOTopRatio, filterCutoffLFO2BottomRatio, filterCutoffLFO2TopRatio;
  var filterResLFOBottom, filterResLFOTop, filterResLFO2Bottom, filterResLFO2Top;
  var filterCutoff, filterRes, filterType;

  var ampLFOBottom, ampLFOTop, ampLFO2Bottom, ampLFO2Top;
  var attackTime, decayTime, sustainLevel, releaseTime;

  var pan, panLFOBottom, panLFOTop, panLFO2Bottom, panLFO2Top, amp;

  var server;

  *new { | outBus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(relGroup);
  }

  prInit { | relGroup = nil |
    server = Server.default;
    server.waitForBoot {
      this.prAddSynthDefs;
      this.prInitializeParameters;
      voiceArray = Array.fill(maxVoices, { nil });
      orderArray = Array.fill(maxVoices, { nil });
      nodeWatcher = NodeWatcher.newFrom(server);
      orderNum = 0;

      lfoBus = Bus.control;

      server.sync;
      lfo = Synth(\prm_Subtractive_LFO, [\outBus, lfoBus], relGroup, \addToHead);
    }
  }

  prAddSynthDefs {

    SynthDef(\prm_Subtractive_LFO, {
      | outBus = 0, lfoWaveform = 0, freq = 1, lfoPulseWidth = 0.5 |
      var lfo, lfoSine, lfoTri, lfoSaw, lfoRect, lfoNoise0, lfoNoise2;
      lfoSine = SinOsc.kr(freq);
      lfoTri = LFTri.kr(freq);
      lfoSaw = LFSaw.kr(freq);
      lfoRect = LFPulse.kr(freq, width: lfoPulseWidth);
      lfoNoise0 = LFNoise0.kr(freq);
      lfoNoise2 = LFNoise2.kr(freq);
      lfo = SelectX.kr(lfoWaveform, [lfoSine, lfoTri, lfoSaw, lfoRect, lfoNoise0, lfoNoise2]);
      Out.kr(outBus, lfo);
    }).add;

    SynthDef(\prm_Subtractive_Voice, {
      |
      outBus = 0, lfo2InBus = 0, amp = 1,
      freq = 220,
      gate = 1,

      lfoFreqLFOBottomRatio = 1.0, lfoFreqLFOTopRatio = 1.0
      lfoWaveform = 0, lfoFreq = 1, lfoPulseWidth = 0.5,
      lfoEnvType = 0, lfoAttackTime = 0.05, lfoReleaseTime = 0.05,

      osc1OctaveMul = 1,
      osc1FreqEnvStartRatio = 1.0, osc1FreqEnvEndRatio = 1.0, osc1FreqEnvTime = 0,
      osc1FreqLFOBottomRatio = 1.0, osc1FreqLFOTopRatio = 1.0, osc1FreqLFO2BottomRatio = 1.0,
      osc1FreqLFO2TopRatio = 1.0,
      osc1PulseWidthLFOBottom = 0, osc1PulseWidthLFOTop = 0, osc1PulseWidthLFO2Bottom = 0,
      osc1PulseWidthLFO2Top = 0,
      osc1AmpLFOBottom = 1, osc1AmpLFOTop = 1, osc1AmpLFO2Bottom = 1, osc1AmpLFO2Top = 1,
      osc1WaveformLFOBottom = 0, osc1WaveformLFOTop = 0, osc1WaveformLFO2Bottom = 0, osc1WaveformLFO2Top = 0,
      osc1Waveform = 2, osc1PulseWidth = 0.5, osc1Amp = 0.5, osc1SubAmp = 0,

      osc2OctaveMul = 0.5, osc2DetuneCents = 0,
      osc2FreqEnvStartRatio = 1.0, osc2FreqEnvEndRatio = 1.0, osc2FreqEnvTime = 0,
      osc2FreqLFOBottomRatio = 1.0, osc2FreqLFOTopRatio = 1.0, osc2FreqLFO2BottomRatio = 1.0,
      osc2FreqLFO2TopRatio = 1.0,
      osc2PulseWidthLFOBottom = 0, osc2PulseWidthLFOTop = 0, osc2PulseWidthLFO2Bottom = 0,
      osc2PulseWidthLFO2Top = 0,
      osc2AmpLFOBottom = 1, osc2AmpLFOTop = 1, osc2AmpLFO2Bottom = 1, osc2AmpLFO2Top = 1,
      osc2WaveformLFOBottom = 0, osc2WaveformLFOTop = 0, osc2WaveformLFO2Bottom = 0, osc2WaveformLFO2Top = 0,
      osc2Waveform = 3, osc2PulseWidth = 0.5, osc2Amp = 0.25, osc2SubAmp = 0,

      noiseOscAmpLFOBottom = 1, noiseOscAmpLFOTop = 1, noiseOscAmpLFO2Bottom = 1, noiseOscAmpLFO2Top = 1,
      noiseOscFilterLFOBottomRatio = 1, noiseOscFilterLFOTopRatio = 1, noiseOscFilterLFO2BottomRatio = 1,
      noiseOscFilterLFO2TopRatio = 1,
      noiseOscAmp = 0, noiseOscCutoff = 10000,

      filterEnvAttackRatio = 1.0, filterEnvPeakRatio = 1.0, filterEnvSustainRatio = 1.0,
      filterEnvReleaseRatio = 1.0,
      filterEnvAttackTime = 0.05, filterEnvDecayTime = 0, filterEnvReleaseTime = 0.05,
      filterEnvLoop = 0,
      filterCutoffLFOBottomRatio = 1.0, filterCutoffLFOTopRatio = 1.0, filterCutoffLFO2BottomRatio = 1.0,
      filterCutoffLFO2TopRatio = 1.0,
      filterResLFOBottom = 0.0, filterResLFOTop = 0.0, filterResLFO2Bottom = 0.0, filterResLFO2Top = 0.0,
      filterCutoff = 2500, filterRes = 0,
      filterType = 0,

      ampLFOBottom = 1.0, ampLFOTop = 1.0, ampLFO2Bottom = 1.0, ampLFO2Top = 1.0,
      attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05,

      pan = 0,
      panLFOBottom = 0.0, panLFOTop = 0.0, panLFO2Bottom = 0.0, panLFO2Top = 0.0
      |

      var lfo2;
      var lfo, lfoFreqLFO, thisLFOFreq, lfoSine, lfoTri, lfoSaw, lfoRect, lfoNoise0, lfoNoise2;
      var lfoAttackEnv, lfoReleaseEnv, lfoAttackAndReleaseEnv;

      var osc1Freq, osc1FreqEnv, osc1FreqLFO, osc1FreqLFO2, osc1WidthLFO, osc1WidthLFO2;
      var osc1AmpLFO, osc1AmpLFO2, osc1WaveformLFO, osc1WaveformLFO2;
      var osc1, osc1Sine, osc1Tri, osc1Saw, osc1Rect, osc1Sub;

      var osc2Freq, osc2FreqEnv, osc2FreqLFO, osc2FreqLFO2, osc2WidthLFO, osc2WidthLFO2;
      var osc2AmpLFO, osc2AmpLFO2, osc2WaveformLFO, osc2WaveformLFO2;
      var osc2, osc2Sine, osc2Tri, osc2Saw, osc2Rect, osc2Sub;

      var noiseOsc, noise, noiseOscFilter, noiseOscAmpLFO, noiseOscAmpLFO2, noiseOscFilterLFO, noiseOscFilterLFO2;

      var oscSum;

      var filter, thisFilterCutoff, thisFilterRes, lowPassFilter, highPassFilter, bandPassFilter;
      var filterCutoffLFO, filterCutoffLFO2, filterResLFO, filterResLFO2, filterEnv;

      var ampEnv, ampLFO, ampLFO2;
      var panning, panningLFO, panningLFO2;

      var sig;

      lfo2 = In.kr(lfo2InBus);

      // Voice LFO:
      lfoFreqLFO = lfo2.linlin(-1, 1, lfoFreqLFOBottomRatio, lfoFreqLFOTopRatio);
      thisLFOFreq = lfoFreq * lfoFreqLFO;
      lfoSine = SinOsc.kr(thisLFOFreq);
      lfoTri = LFTri.kr(thisLFOFreq);
      lfoSaw = LFSaw.kr(thisLFOFreq);
      lfoRect = (LFPulse.kr(thisLFOFreq, lfoPulseWidth) - 0.5) * 2;
      lfoNoise0 = LFNoise0.kr(thisLFOFreq);
      lfoNoise2 = LFNoise2.kr(thisLFOFreq);
      lfoAttackEnv = EnvGen.kr(Env.new([0, 1], [lfoAttackTime], 'cubed', 1), gate);
      lfoReleaseEnv = EnvGen.kr(Env.cutoff(lfoReleaseTime, 1, 'cubed'), gate);
      lfoAttackAndReleaseEnv = EnvGen.kr(Env.asr(lfoAttackTime, 1, lfoReleaseTime), gate);
      lfo = SelectX.kr(lfoWaveform, [lfoSine, lfoTri, lfoSaw, lfoRect, lfoNoise0, lfoNoise2]);
      lfo = Select.kr(lfoEnvType, [lfo, lfo * lfoAttackEnv, lfo * lfoReleaseEnv, lfo * lfoAttackAndReleaseEnv]);


      // Oscillator 1:
      osc1FreqEnv = XLine.ar(osc1FreqEnvStartRatio, osc1FreqEnvEndRatio, osc1FreqEnvTime);
      osc1FreqLFO = lfo.linlin(-1, 1, osc1FreqLFOBottomRatio, osc1FreqLFOTopRatio);
      osc1FreqLFO2 = lfo2.linlin(-1, 1, osc1FreqLFO2BottomRatio, osc1FreqLFO2TopRatio);
      osc1AmpLFO = lfo.linlin(-1, 1, osc1AmpLFOBottom, osc1AmpLFOTop);
      osc1AmpLFO2 = lfo2.linlin(-1, 1, osc1AmpLFO2Bottom, osc1AmpLFO2Top);
      osc1WidthLFO = lfo.linlin(-1, 1, osc1PulseWidthLFOBottom, osc1PulseWidthLFOTop);
      osc1WidthLFO2 = lfo2.linlin(-1, 1, osc1PulseWidthLFO2Bottom, osc1PulseWidthLFO2Top);
      osc1WaveformLFO = lfo.linlin(-1, 1, osc1WaveformLFOBottom, osc1WaveformLFOTop);
      osc1WaveformLFO2 = lfo2.linlin(-1, 1, osc1WaveformLFO2Bottom, osc1WaveformLFO2Top);
      osc1Freq = freq * osc1OctaveMul * osc1FreqEnv * osc1FreqLFO * osc1FreqLFO2;

      osc1Sub = SinOsc.ar(osc1Freq/2) * osc1SubAmp;

      osc1Sine = SinOsc.ar(osc1Freq);
      osc1Tri = LPF.ar(LFTri.ar(osc1Freq), 18000);
      osc1Saw = Saw.ar(osc1Freq);
      osc1Rect = Pulse.ar(osc1Freq, (osc1PulseWidth + osc1WidthLFO + osc1WidthLFO2).wrap(0, 1));
      osc1 = SelectX.ar((osc1Waveform + osc1WaveformLFO + osc1WaveformLFO2).wrap(0.0, 4.0),
        [osc1Sine, osc1Tri, osc1Saw, osc1Rect]);
      //osc1 = SelectX.ar(osc1Waveform, [osc1Sine, osc1Tri, osc1Saw, osc1Rect]);

      osc1 = osc1 * osc1Amp;
      osc1 = osc1 + osc1Sub;
      osc1 = osc1 * osc1AmpLFO * osc1AmpLFO2;

      // Oscillator 2:
      osc2FreqEnv = XLine.ar(osc2FreqEnvStartRatio, osc2FreqEnvEndRatio, osc2FreqEnvTime);
      osc2FreqLFO = lfo.linlin(-1, 1, osc2FreqLFOBottomRatio, osc2FreqLFOTopRatio);
      osc2FreqLFO2 = lfo2.linlin(-1, 1, osc2FreqLFO2BottomRatio, osc2FreqLFO2TopRatio);
      osc2WidthLFO = lfo.linlin(-1, 1, osc2PulseWidthLFOBottom, osc2PulseWidthLFOTop);
      osc2WidthLFO2 = lfo2.linlin(-1, 1, osc2PulseWidthLFO2Bottom, osc2PulseWidthLFO2Top);
      osc2AmpLFO = lfo.linlin(-1, 1, osc2AmpLFOBottom, osc2AmpLFOTop);
      osc2AmpLFO2 = lfo2.linlin(-1, 1, osc2AmpLFO2Bottom, osc2AmpLFO2Top);
      osc2WaveformLFO = lfo.linlin(-1, 1, osc2WaveformLFOBottom, osc2WaveformLFOTop);
      osc2WaveformLFO2 = lfo2.linlin(-1, 1, osc2WaveformLFO2Bottom, osc2WaveformLFO2Top);
      osc2Freq = freq * osc2OctaveMul * osc2FreqEnv * osc2FreqLFO * osc2FreqLFO2 * (osc2DetuneCents/100).midiratio;

      osc2Sub = SinOsc.ar(osc2Freq/2) * osc2SubAmp;

      osc2Sine = SinOsc.ar(osc2Freq);
      osc2Tri = LPF.ar(LFTri.ar(osc2Freq), 18000);
      osc2Saw = Saw.ar(osc2Freq);
      osc2Rect = Pulse.ar(osc2Freq, (osc2PulseWidth + osc2WidthLFO + osc2WidthLFO2).wrap(0, 1));
      osc2 = SelectX.ar((osc2Waveform + osc2WaveformLFO + osc2WaveformLFO2).wrap(0.0, 4.0),
        [osc2Sine, osc2Tri, osc2Saw, osc2Rect]);

      osc2 = osc2 * osc2Amp;
      osc2 = osc2 + osc2Sub;
      osc2 = osc2 * osc2AmpLFO * osc2AmpLFO2;

      noiseOscAmpLFO = lfo.linlin(-1, 1, noiseOscAmpLFOBottom, noiseOscAmpLFOTop);
      noiseOscAmpLFO2 = lfo2.linlin(-1, 1, noiseOscAmpLFO2Bottom, noiseOscAmpLFO2Top);
      noiseOscFilterLFO = lfo.linlin(-1, 1, noiseOscFilterLFOBottomRatio, noiseOscFilterLFOTopRatio);
      noiseOscFilterLFO2 = lfo2.linlin(-1, 1, noiseOscFilterLFO2BottomRatio, noiseOscFilterLFO2TopRatio);
      noise = WhiteNoise.ar(1);
      noiseOscFilter = LPF.ar(noise, noiseOscCutoff * noiseOscFilterLFO * noiseOscFilterLFO2);
      noiseOsc = noiseOscFilter * noiseOscAmp;
      noiseOsc = noiseOsc * noiseOscAmpLFO * noiseOscAmpLFO2;

      oscSum = Mix.ar([osc1, osc2, noiseOsc]);

      // filter:

      if(filterEnvLoop == 0, { filterEnvLoop = nil }, { filterEnvLoop = filterEnvLoop - 1 });
      filterEnv = EnvGen.kr(
        Env.new([filterEnvAttackRatio, filterEnvPeakRatio, filterEnvSustainRatio, filterEnvReleaseRatio],
          [filterEnvAttackTime, filterEnvDecayTime, filterEnvReleaseTime], -4, 2, filterEnvLoop),
        gate);
      filterCutoffLFO = lfo.linlin(-1, 1, filterCutoffLFOBottomRatio, filterCutoffLFOTopRatio);
      filterCutoffLFO2 = lfo2.linlin(-1, 1, filterCutoffLFO2BottomRatio, filterCutoffLFO2TopRatio);
      filterResLFO = lfo.linlin(-1, 1, filterResLFOBottom, filterResLFOTop);
      filterResLFO2 = lfo2.linlin(-1, 1, filterResLFO2Bottom, filterResLFO2Top);
      thisFilterCutoff = filterCutoff * filterEnv * filterCutoffLFO * filterCutoffLFO2;
      thisFilterRes = filterRes + filterResLFO + filterResLFO2;
      lowPassFilter = DFM1.ar(oscSum, thisFilterCutoff, thisFilterRes, 1, 0, 0.0005);
      highPassFilter = DFM1.ar(oscSum, thisFilterCutoff, thisFilterRes, 1, 1, 0.0005);
      bandPassFilter = BBandPass.ar(oscSum, thisFilterCutoff, filterRes);
      filter = Select.ar(filterType, [lowPassFilter, highPassFilter, bandPassFilter]);

      // Amplitude Envelope:
      ampLFO = lfo.linlin(-1, 1, ampLFOBottom, ampLFOTop);
      ampLFO2 = lfo2.linlin(-1, 1, ampLFO2Bottom, ampLFO2Top);
      //ampEnv = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4), gate);
      ampEnv = EnvGen.kr(Env.new([0, 0, 1, sustainLevel, 0], [0, attackTime, decayTime, releaseTime],
        curve: 4, releaseNode: 3), gate);
      ampEnv = ampEnv * ampLFO * ampLFO2;

      panningLFO = lfo.linlin(-1, 1, panLFOBottom, panLFOTop);
      panningLFO = lfo2.linlin(-1, 1, panLFO2Bottom, panLFO2Top);
      panning = Pan2.ar(filter * ampEnv, (pan + panningLFO).clip(-1, 1));

      sig = panning * amp;

      Out.ar(outBus, sig);

    }).add;

  }

  prInitializeParameters {

    applyMode = true;

    maxVoices = 8; numVoices = 0; amp = 0.2;

    lfoFreqLFOBottomRatio = 1.0; lfoFreqLFOTopRatio = 1.0; lfoWaveform = 0;
    lfoFreq = 1; lfoPulseWidth = 0.5; lfoEnvType = 0; lfoAttackTime = 0.05; lfoReleaseTime = 0.05;

    osc1OctaveMul = 1; osc1FreqEnvStartRatio = 1.0; osc1FreqEnvEndRatio = 1.0; osc1FreqEnvTime = 0;
    osc1FreqLFOBottomRatio = 1.0; osc1FreqLFOTopRatio = 1.0; osc1FreqLFO2BottomRatio = 1.0;
    osc1FreqLFO2TopRatio = 1.0; osc1PulseWidthLFOBottom = 0; osc1PulseWidthLFOTop = 0;
    osc1PulseWidthLFO2Bottom = 0; osc1PulseWidthLFO2Top = 0; osc1AmpLFOBottom = 1; osc1AmpLFOTop = 1;
    osc1AmpLFO2Bottom = 1; osc1AmpLFO2Top = 1; osc1WaveformLFOBottom = 0; osc1WaveformLFOTop = 0;
    osc1WaveformLFO2Bottom = 0; osc1WaveformLFO2Top = 0; osc1Waveform = 2; osc1PulseWidth = 0.5;
    osc1Amp = 0.5; osc1SubAmp = 0;

    osc2OctaveMul = 0.5; osc2DetuneCents = 0;
    osc2FreqEnvStartRatio = 1.0; osc2FreqEnvEndRatio = 1.0; osc2FreqEnvTime = 0;
    osc2FreqLFOBottomRatio = 1.0; osc2FreqLFOTopRatio = 1.0; osc2FreqLFO2BottomRatio = 1.0; osc2FreqLFO2TopRatio = 1.0;
    osc2PulseWidthLFOBottom = 0; osc2PulseWidthLFOTop = 0; osc2PulseWidthLFO2Bottom = 0; osc2PulseWidthLFO2Top = 0;
    osc2AmpLFOBottom = 1; osc2AmpLFOTop = 1; osc2AmpLFO2Bottom = 1; osc2AmpLFO2Top = 1;
    osc2WaveformLFOBottom = 0; osc2WaveformLFOTop = 0; osc2WaveformLFO2Bottom = 0; osc2WaveformLFO2Top = 0;
    osc2Waveform = 3; osc2PulseWidth = 0.5; osc2Amp = 0.25; osc2SubAmp = 0;

    noiseOscAmpLFOBottom = 1; noiseOscAmpLFOTop = 1; noiseOscAmpLFO2Bottom = 1; noiseOscAmpLFO2Top = 1;
    noiseOscFilterLFOBottomRatio = 1; noiseOscFilterLFOTopRatio = 1; noiseOscFilterLFO2BottomRatio = 1; noiseOscFilterLFO2TopRatio = 1;
    noiseOscAmp = 0; noiseOscCutoff = 10000;

    filterEnvAttackRatio = 1.0; filterEnvPeakRatio = 1.0; filterEnvSustainRatio = 1.0; filterEnvReleaseRatio = 1.0;
    filterEnvAttackTime = 0.05; filterEnvDecayTime = 0; filterEnvReleaseTime = 0.05;
    filterEnvLoop = 0;
    filterCutoffLFOBottomRatio = 1.0; filterCutoffLFOTopRatio = 1.0; filterCutoffLFO2BottomRatio = 1.0;
    filterCutoffLFO2TopRatio = 1.0; filterResLFOBottom = 0.0; filterResLFOTop = 0.0;
    filterResLFO2Bottom = 0.0; filterResLFO2Top = 0.0;
    filterCutoff = 2000; filterRes = 0.0; filterType = 0;

    ampLFOBottom = 1.0; ampLFOTop = 1.0; ampLFO2Bottom = 1.0; ampLFO2Top = 1.0;
    attackTime = 0.05; decayTime = 0.05; sustainLevel = 1; releaseTime = 0.05;

    pan = 0;
    panLFOBottom = 0.0; panLFOTop = 0.0; panLFO2Bottom = 0.0; panLFO2Top = 0.0;

  }

  prGetFreeVoice {
    var freeVoice = nil;
    var index = 0;
    while( { freeVoice.isNil } ,
      { if( voiceArray[index].isNil, { freeVoice = index; },
        {index = index + 1; }
      );};
    );
    ^freeVoice;
  }

  prManageOrder {
    var subArray1, subArray2, newArray;
    var nilPos = nil;
    var index = 0;
    while( { nilPos.isNil },{
        if( orderArray[index].isNil,
          { nilPos = index; }, { index = index + 1; });
    });
    subArray1 = Array.fill(nilPos, { | i | orderArray[i]; });
    subArray2 = Array.fill(orderArray.size - (nilPos + 1), { | i | orderArray[i + nilPos + 1] });
    newArray = subArray1 ++ subArray2;
    newArray = newArray.add(nil);
    orderArray = newArray;
  }

  prManageVoices {
    var activeVoices = 0;
    if( orderNum >= maxVoices,
      {
        spilloverSynth = voiceArray[orderArray[0]];
        if( voiceArray[orderArray[0]].notNil, {
          spilloverSynth.free;
          voiceArray[orderArray[0]] = nil;
          orderArray[0] = nil;
          this.prManageOrder;
          orderArray.do({ | val, index |
            if( val.notNil, { activeVoices = activeVoices + 1; });
          });
          orderNum = activeVoices;
        });
    });
  }

  //////// public functions:

  playNoteControlSurface { | freq = 220 |

    var voiceNumber;
    var noteBundle;

    this.prManageVoices;
    //while( { orderNum >= maxVoices }, { r { loop { 0.01.wait; }}.play; });
    voiceNumber = this.prGetFreeVoice;

    noteBundle = server.makeBundle(false, {
      voiceArray[voiceNumber] = Synth(\prm_Subtractive_Voice, [
        \outBus, mixer.chanStereo(0), \lfo2InBus, lfoBus, \amp, amp, \freq, freq, \gate, 1,

        \lfoFreqLFOBottomRatio, lfoFreqLFOBottomRatio, \lfoFreqLFOTopRatio, lfoFreqLFOTopRatio,
        \lfoWaveform, lfoWaveform, \lfoFreq, lfoFreq, \lfoPulseWidth, lfoPulseWidth,
        \lfoEnvType, lfoEnvType, \lfoAttackTime, lfoAttackTime, \lfoReleaseTime, lfoReleaseTime,

        \osc1OctaveMul, osc1OctaveMul, \osc1FreqEnvStartRatio, osc1FreqEnvStartRatio,
        \osc1FreqEnvEndRatio, osc1FreqEnvEndRatio, \osc1FreqEnvTime, osc1FreqEnvTime,
        \osc1FreqLFOBottomRatio, osc1FreqLFOBottomRatio, \osc1FreqLFOTopRatio, osc1FreqLFOTopRatio,
        \osc1FreqLFO2BottomRatio, osc1FreqLFO2BottomRatio, \osc1FreqLFO2TopRatio, osc1FreqLFO2TopRatio,
        \osc1PulseWidthLFOBottom, osc1PulseWidthLFOBottom, \osc1PulseWidthLFOTop, osc1PulseWidthLFOTop,
        \osc1PulseWidthLFO2Bottom, osc1PulseWidthLFO2Bottom, \osc1PulseWidthLFO2Top, osc1PulseWidthLFO2Top,
        \osc1AmpLFOBottom, osc1AmpLFOBottom, \osc1AmpLFOTop, osc1AmpLFOTop,
        \osc1AmpLFO2Bottom, osc1AmpLFO2Bottom, \osc1AmpLFO2Top, osc1AmpLFO2Top,
        \osc1WaveformLFOBottom, osc1WaveformLFOBottom, \osc1WaveformLFOTop, osc1WaveformLFOTop,
        \osc1WaveformLFO2Bottom, osc1WaveformLFO2Bottom, \osc1WaveformLFO2Top, osc1WaveformLFO2Top,
        \osc1Waveform, osc1Waveform, \osc1PulseWidth, osc1PulseWidth, \osc1Amp, osc1Amp, \osc1SubAmp, osc1SubAmp,

        \osc2OctaveMul, osc2OctaveMul, \osc2DetuneCents, osc2DetuneCents, \osc2FreqEnvStartRatio, osc2FreqEnvStartRatio,
        \osc2FreqEnvEndRatio , osc2FreqEnvEndRatio, \osc2FreqEnvTime, osc2FreqEnvTime,
        \osc2FreqLFOBottomRatio, osc2FreqLFOBottomRatio, \osc2FreqLFOTopRatio, osc2FreqLFOTopRatio,
        \osc2FreqLFO2BottomRatio, osc2FreqLFO2BottomRatio, \osc2FreqLFO2TopRatio, osc2FreqLFO2TopRatio,
        \osc2PulseWidthLFOBottom, osc2PulseWidthLFOBottom, \osc2PulseWidthLFOTop, osc2PulseWidthLFOTop,
        \osc2PulseWidthLFO2Bottom, osc2PulseWidthLFO2Bottom, \osc2PulseWidthLFO2Top, osc2PulseWidthLFOTop,
        \osc2AmpLFOBottom, osc2AmpLFOBottom, \osc2AmpLFOTop, osc2AmpLFOTop,
        \osc2AmpLFO2Bottom, osc2AmpLFO2Bottom, \osc2AmpLFO2Top, osc2AmpLFO2Top,
        \osc2WaveformLFOBottom, osc2WaveformLFOBottom, \osc2WaveformLFOTop, osc2WaveformLFOTop,
        \osc2WaveformLFO2Bottom, osc2WaveformLFO2Bottom, \osc2WaveformLFO2Top, osc2WaveformLFO2Top,
        \osc2Waveform, osc2Waveform, \osc2PulseWidth, osc2PulseWidth, \osc2Amp, osc2Amp, \osc2SubAmp, osc2SubAmp,

        \noiseOscAmpLFOBottom, noiseOscAmpLFOBottom, \noiseOscAmpLFOTop, noiseOscAmpLFOTop,
        \noiseOscAmpLFO2Bottom, noiseOscAmpLFO2Bottom, \noiseOscAmpLFO2Top, noiseOscAmpLFO2Top,
        \noiseOscFilterLFOBottomRatio, noiseOscFilterLFOBottomRatio, \noiseOscFilterLFOTopRatio, noiseOscFilterLFOTopRatio,
        \noiseOscFilterLFO2BottomRatio, noiseOscFilterLFO2BottomRatio, \noiseOscFilterLFO2TopRatio, noiseOscFilterLFO2TopRatio,
        \noiseOscAmp, noiseOscAmp, \noiseOscCutoff, noiseOscCutoff,

        \filterEnvAttackRatio, filterEnvAttackRatio, \filterEnvPeakRatio, filterEnvPeakRatio,
        \filterEnvSustainRatio, filterEnvSustainRatio, \filterEnvReleaseRatio, filterEnvReleaseRatio,
        \filterEnvAttackTime, filterEnvAttackTime, \filterEnvDecayTime, filterEnvDecayTime,
        \filterEnvReleaseTime, filterEnvReleaseTime, \filterEnvLoop, filterEnvLoop,
        \filterCutoffLFOBottomRatio, filterCutoffLFOBottomRatio, \filterCutoffLFOTopRatio, filterCutoffLFOTopRatio,
        \filterCutoffLFO2BottomRatio, filterCutoffLFO2BottomRatio, \filterCutoffLFO2TopRatio, filterCutoffLFO2TopRatio,
        \filterResLFOBottom, filterResLFOBottom, \filterResLFOTop, filterResLFOTop,
        \filterResLFO2Bottom, filterResLFO2Bottom, \filterResLFO2Top, filterResLFO2Top,
        \filterCutoff, filterCutoff, \filterRes, filterRes, \filterType, filterType,

        \ampLFOBottom, ampLFOBottom, \ampLFOTop, ampLFOTop, \ampLFO2Bottom, ampLFO2Bottom,
        \ampLFO2Top, ampLFO2Top, \attackTime, attackTime, \decayTime, decayTime,
        \sustainLevel, sustainLevel, \releaseTime, releaseTime,

        \pan, pan, \panLFOBottom, panLFOBottom, \panLFOTop, panLFOTop,
        \panLFO2Bottom, panLFO2Bottom, \panLFO2Top, panLFO2Top
      ], lfo, \addAfter);
      nodeWatcher.register(voiceArray[voiceNumber]);
    });

    server.listSendBundle(nil, noteBundle);

    orderArray[orderNum] = voiceNumber;
    orderNum = orderNum + 1;
    //voiceNumber = voiceNum;
    //voiceNum = voiceNum + 1;

    ^voiceNumber;
  }

  releaseNoteControlSurface { | voiceNumber = 0 |
    {
      var activeVoices = 0;
      var synth = voiceArray[voiceNumber];
      synth.set(\gate, 0);
      releaseTime.wait;
      synth.free;
      while({ synth.isPlaying; }, { 0.01.wait; });
      if( synth.notNil, {
        voiceArray[voiceNumber] = nil;
        orderArray.do({ | num, index | if( num == voiceNumber, { orderArray[index] = nil; }); });
        this.prManageOrder;
        orderArray.do({ | val, index |
            if( val.notNil, { activeVoices = activeVoices + 1; });
          orderNum = activeVoices;
        });
      });
      //voiceNum = this.prGetFreeVoice;
      //("post clear" + synthDict).postln;
    }.fork;
  }

  freeNoteControlSurface { | voiceNumber = 0 |
    {
      var synth = voiceArray[voiceNumber];
      var activeVoices = 0;
      if( synth.notNil, {
        synth.free;
        while({ synth.isPlaying; }, { 0.01.wait; });
        voiceArray[voiceNumber] = nil;
        orderArray.do({ | num, index | if( num == voiceNumber, { orderArray[index] = nil; }); });
        this.prManageOrder;
        orderArray.do({ | val, index |
            if( val.notNil, { activeVoices = activeVoices + 1; });
          orderNum = activeVoices;
        });
      });
    }.fork;
  }

  /*
  playNote { | freq = 220 |
    {
      var noteBundle;
      //("note struck" + synthDict).postln;
      if( synthDict[freq].notNil, { this.freeNote(freq); });
      while( { synthDict[freq].notNil }, { 0.01.wait; });
      //("duplicated freed" + synthDict).postln;

      noteBundle = server.makeBundle(false, {
        synthDict[freq] = Synth(\prm_Subtractive_Voice, [
          \outBus, mixer.chanStereo(0), \lfo2InBus, lfoBus, \amp, amp, \freq, freq, \gate, 1,

          \lfoFreqLFOBottomRatio, lfoFreqLFOBottomRatio, \lfoFreqLFOTopRatio, lfoFreqLFOTopRatio,
          \lfoWaveform, lfoWaveform, \lfoFreq, lfoFreq, \lfoPulseWidth, lfoPulseWidth,
          \lfoEnvType, lfoEnvType, \lfoAttackTime, lfoAttackTime, \lfoReleaseTime, lfoReleaseTime,

          \osc1OctaveMul, osc1OctaveMul, \osc1FreqEnvStartRatio, osc1FreqEnvStartRatio,
          \osc1FreqEnvEndRatio, osc1FreqEnvEndRatio, \osc1FreqEnvTime, osc1FreqEnvTime,
          \osc1FreqLFOBottomRatio, osc1FreqLFOBottomRatio, \osc1FreqLFOTopRatio, osc1FreqLFOTopRatio,
          \osc1FreqLFO2BottomRatio, osc1FreqLFO2BottomRatio, \osc1FreqLFO2TopRatio, osc1FreqLFO2TopRatio,
          \osc1PulseWidthLFOBottom, osc1PulseWidthLFOBottom, \osc1PulseWidthLFOTop, osc1PulseWidthLFOTop,
          \osc1PulseWidthLFO2Bottom, osc1PulseWidthLFO2Bottom, \osc1PulseWidthLFO2Top, osc1PulseWidthLFO2Top,
          \osc1AmpLFOBottom, osc1AmpLFOBottom, \osc1AmpLFOTop, osc1AmpLFOTop,
          \osc1AmpLFO2Bottom, osc1AmpLFO2Bottom, \osc1AmpLFO2Top, osc1AmpLFO2Top,
          \osc1WaveformLFOBottom, osc1WaveformLFOBottom, \osc1WaveformLFOTop, osc1WaveformLFOTop,
          \osc1WaveformLFO2Bottom, osc1WaveformLFO2Bottom, \osc1WaveformLFO2Top, osc1WaveformLFO2Top,
          \osc1Waveform, osc1Waveform, \osc1PulseWidth, osc1PulseWidth, \osc1Amp, osc1Amp, \osc1SubAmp, osc1SubAmp,

          \osc2OctaveMul, osc2OctaveMul, \osc2DetuneCents, osc2DetuneCents, \osc2FreqEnvStartRatio, osc2FreqEnvStartRatio,
          \osc2FreqEnvEndRatio , osc2FreqEnvEndRatio, \osc2FreqEnvTime, osc2FreqEnvTime,
          \osc2FreqLFOBottomRatio, osc2FreqLFOBottomRatio, \osc2FreqLFOTopRatio, osc2FreqLFOTopRatio,
          \osc2FreqLFO2BottomRatio, osc2FreqLFO2BottomRatio, \osc2FreqLFO2TopRatio, osc2FreqLFO2TopRatio,
          \osc2PulseWidthLFOBottom, osc2PulseWidthLFOBottom, \osc2PulseWidthLFOTop, osc2PulseWidthLFOTop,
          \osc2PulseWidthLFO2Bottom, osc2PulseWidthLFO2Bottom, \osc2PulseWidthLFO2Top, osc2PulseWidthLFOTop,
          \osc2AmpLFOBottom, osc2AmpLFOBottom, \osc2AmpLFOTop, osc2AmpLFOTop,
          \osc2AmpLFO2Bottom, osc2AmpLFO2Bottom, \osc2AmpLFO2Top, osc2AmpLFO2Top,
          \osc2WaveformLFOBottom, osc2WaveformLFOBottom, \osc2WaveformLFOTop, osc2WaveformLFOTop,
          \osc2WaveformLFO2Bottom, osc2WaveformLFO2Bottom, \osc2WaveformLFO2Top, osc2WaveformLFO2Top,
          \osc2Waveform, osc2Waveform, \osc2PulseWidth, osc2PulseWidth, \osc2Amp, osc2Amp, \osc2SubAmp, osc2SubAmp,

          \noiseOscAmpLFOBottom, noiseOscAmpLFOBottom, \noiseOscAmpLFOTop, noiseOscAmpLFOTop,
          \noiseOscAmpLFO2Bottom, noiseOscAmpLFO2Bottom, \noiseOscAmpLFO2Top, noiseOscAmpLFO2Top,
          \noiseOscFilterLFOBottomRatio, noiseOscFilterLFOBottomRatio, \noiseOscFilterLFOTopRatio, noiseOscFilterLFOTopRatio,
          \noiseOscFilterLFO2BottomRatio, noiseOscFilterLFO2BottomRatio, \noiseOscFilterLFO2TopRatio, noiseOscFilterLFO2TopRatio,
          \noiseOscAmp, noiseOscAmp, \noiseOscCutoff, noiseOscCutoff,

          \filterEnvAttackRatio, filterEnvAttackRatio, \filterEnvPeakRatio, filterEnvPeakRatio,
          \filterEnvSustainRatio, filterEnvSustainRatio, \filterEnvReleaseRatio, filterEnvReleaseRatio,
          \filterEnvAttackTime, filterEnvAttackTime, \filterEnvDecayTime, filterEnvDecayTime,
          \filterEnvReleaseTime, filterEnvReleaseTime, \filterEnvLoop, filterEnvLoop,
          \filterCutoffLFOBottomRatio, filterCutoffLFOBottomRatio, \filterCutoffLFOTopRatio, filterCutoffLFOTopRatio,
          \filterCutoffLFO2BottomRatio, filterCutoffLFO2BottomRatio, \filterCutoffLFO2TopRatio, filterCutoffLFO2TopRatio,
          \filterResLFOBottom, filterResLFOBottom, \filterResLFOTop, filterResLFOTop,
          \filterResLFO2Bottom, filterResLFO2Bottom, \filterResLFO2Top, filterResLFO2Top,
          \filterCutoff, filterCutoff, \filterRes, filterRes, \filterType, filterType,

          \ampLFOBottom, ampLFOBottom, \ampLFOTop, ampLFOTop, \ampLFO2Bottom, ampLFO2Bottom,
          \ampLFO2Top, ampLFO2Top, \attackTime, attackTime, \decayTime, decayTime,
          \sustainLevel, sustainLevel, \releaseTime, releaseTime,

          \pan, pan, \panLFOBottom, panLFOBottom, \panLFOTop, panLFOTop,
          \panLFO2Bottom, panLFO2Bottom, \panLFO2Top, panLFO2Top
        ], lfo, \addAfter);
        nodeWatcher.register(synthDict[freq]);
      });

      server.listSendBundle(nil, noteBundle);
    }.fork;
  }
  */

  /*
  releaseNote { | freq = 220 |
    {
      //var synth = synthDict[freq];
      //("pre release" + synthDict).postln;
      synth.set(\gate, 0);
      releaseTime.wait;
      synth.free;
      /*
      voiceArray.do({ | val, index | if(val == freq, {
        voiceArray[index] = nil;
        numVoices = numVoices - 1;
      });});
      */
      while({ synth.isPlaying; }, { 0.01.wait; });
      //synthDict.removeAt(freq);
      //("post clear" + synthDict).postln;
    }.fork;
  }
*/
  /*
  freeNote { | freq = 220 |
    {
      var synth = synthDict[freq];
      //("pre free" + synthDict).postln;
      synth.free;
      while({ synth.isPlaying; }, { 0.01.wait; });
      synthDict.removeAt(freq);
      //("post free clear" + synthDict).postln;
    }.fork;
  }
  */


  manageVoices {
    if( numVoices >= maxVoices, {
      this.releaseNote(voiceArray[0]);
      voiceArray = voiceArray.rotate(-1);
      //numVoices = numVoices - 1;
    });
  }

  setMaxVoices { | voices = 8 |
    maxVoices = voices;
    if( numVoices > maxVoices, {
      while( numVoices > maxVoices, { this.manageVoices; });
    });
  }

  //// Filter:
  setFilterCutoff { | cutoff = 2000 |
    filterCutoff = cutoff;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\filterCutoff, cutoff); }); }); });
  }

  setFilterRes { | res = 0.0 |
    filterRes = res;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\filterRes, res); }); }); });
  }

  setFilterType { | type = 'lowPass' |
    switch(type,
      { 'lowPass' }, { filterType = 0 },
      { 'highPass' }, { filterType = 1 },
      { 'bandPass' }, { filterType = 2 },
    );
    if( type.isInteger, { filterType = type; });
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\filterType, filterType); }); }); });
  }

  // filter Envelope:
  setFilterEnvAttackTime { | attack = 0.05 |
    filterEnvAttackTime = attack;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterEnvAttackTime, filterEnvAttackTime); }); }); });
  }
  setFilterEnvDecayTime { | decay = 0 |
    filterEnvDecayTime = decay;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterEnvDecayTime, filterEnvDecayTime); }); }); });
  }
  setFilterEnvReleaseTime { | release = 0.05 |
    filterEnvReleaseTime = release;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterEnvReleaseTime, filterEnvReleaseTime); }); }); });
  }
  setFilterEnvAttackRatio { | ratio = 1.0 |
    filterEnvAttackRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterEnvAttackRatio, filterEnvAttackRatio); }); }); });
  }
  setFilterEnvPeakRatio { | ratio = 1.0 |
    filterEnvPeakRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterEnvPeakRatio, filterEnvPeakRatio); }); }); });
  }
  setFilterEnvSustainRatio { | ratio = 1.0 |
    filterEnvSustainRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterEnvSustainRatio, filterEnvSustainRatio); }); }); });
  }
  setFilterEnvReleaseRatio { | ratio = 1.0 |
    filterEnvReleaseRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterEnvReleaseRatio, filterEnvReleaseRatio); }); }); });
  }
  setFilterEnvLoop { | loop = false |
    if( loop, { filterEnvLoop = 1; },{ filterEnvLoop = 0 });
    if( loop.isInteger, { filterEnvLoop = loop; });
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterEnvLoop, filterEnvLoop); }); }); });
  }

  // Filter LFO:
  setFilterCutoffLFOBottomRatio { | ratio = 1.0 |
    filterCutoffLFOBottomRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterCutoffLFOBottomRatio, filterCutoffLFOBottomRatio);
      });
      });
    });
  }
  setFilterCutoffLFOTopRatio { | ratio = 1.0 |
    filterCutoffLFOTopRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterCutoffLFOTopRatio, filterCutoffLFOTopRatio); }); }); });
  }
  setFilterResLFOBottom { | bottom = 0.0 |
    filterResLFOBottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterResLFOBottom, filterResLFOBottom); }); }); });
  }
  setFilterResLFOTop { | top = 0.0 |
    filterResLFOTop = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterResLFOTop, filterResLFOTop); }); }); });
  }

  // LFO2:
  setFilterCutoffLFO2BottomRatio { | ratio = 1.0 |
    filterCutoffLFO2BottomRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterCutoffLFO2BottomRatio, filterCutoffLFO2BottomRatio);
      });
      });
    });
  }
  setFilterCutoffLFO2TopRatio { | ratio = 1.0 |
    filterCutoffLFO2TopRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterCutoffLFO2TopRatio, filterCutoffLFO2TopRatio); }); }); });
  }
  setFilterResLFO2Bottom { | bottom = 0.0 |
    filterResLFO2Bottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterResLFO2Bottom, filterResLFO2Bottom); }); }); });
  }
  setFilterResLFO2Top { | top = 0.0 |
    filterResLFO2Top = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\filterResLFO2Top, filterResLFO2Top); }); }); });
  }


  //// Amplitude Envelope:
  setAttackTime { | attack = 0.05 |
    attackTime = attack;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\attackTime, attack); }); }); });
  }

  setDecayTime { | decay = 0 |
    decayTime = decay;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\decayTime, decay); }); }); });
  }

  setSustainLevel { | sustain = 1.0 |
    sustainLevel = sustain;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\sustainLevel, sustain); }); }); });
  }

  setReleaseTime { | release = 0.05 |
    releaseTime = release;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\releaseTime, release); }); }); });
  }

  setVoiceAmp { | voiceAmp = 0.2 |
    amp = voiceAmp;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\amp, amp); }); }); });
  }

  setPan { | panning = 0 |
    pan = panning;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\pan, pan); }); }); });
  }

  // Amplitude LFO 1:
  setAmplitudeLFOBottom { | bottom = 1.0 |
    ampLFOBottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\ampLFOBottom, ampLFOBottom); }); }); });
  }

  setAmplitudeLFOTop { | top = 1.0 |
    ampLFOTop = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\ampLFOTop, ampLFOTop); }); }); });
  }

  // Amplitude LFO 2:
  setAmplitudeLFO2Bottom { | bottom = 1.0 |
    ampLFO2Bottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\ampLFO2Bottom, ampLFO2Bottom); }); }); });
  }

  setAmplitudeLFO2Top { | top = 1.0 |
    ampLFO2Top = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\ampLFO2Top, ampLFO2Top); }); }); });
  }


  //// Oscillator 1:

  setOsc1Vol { | vol = -6 |
    osc1Amp = vol.dbamp;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\osc1Amp, osc1Amp); }); }); });
  }

  setOsc1SubVol { | vol = -70 |
    osc1SubAmp = vol.dbamp;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\osc1SubAmp, osc1SubAmp); }); }); });
  }

  setOsc1Waveform { | waveform = 'saw' |
    switch(waveform,
      { 'sine' }, { osc1Waveform = 0 },
      { 'tri' }, { osc1Waveform = 1 },
      { 'saw' }, { osc1Waveform = 2 },
      { 'rect' }, { osc1Waveform = 3 }
    );
    if(waveform.isInteger, { osc1Waveform = waveform; });
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\osc1Waveform, osc1Waveform); }); }); });
  }

  setOsc1OctaveMul { | octaveMul = 1 |
    osc1OctaveMul = octaveMul;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\osc1OctaveMul, osc1OctaveMul); }); }); });
  }

  setOsc1Octave { | octave = 3 |
    switch(octave,
      { 0 }, { this.setOsc1OctaveMul(0.125) },
      { 1 }, { this.setOsc1OctaveMul(0.25) },
      { 2 }, { this.setOsc1OctaveMul(0.5) },
      { 3 }, { this.setOsc1OctaveMul(1) },
      { 4 }, { this.setOsc1OctaveMul(2) },
      { 5 }, { this.setOsc1OctaveMul(3) },
      { 6 }, { this.setOsc1OctaveMul(4) }
    );
  }

  setOsc1PulseWidth { | pulseWidth = 0.5 |
    osc1PulseWidth = pulseWidth;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\osc1PulseWidth, pulseWidth); }); }); });
  }


  // frequency envelope:
  setOsc1FreqEnvStartRatio { | ratio = 1.0 |
    osc1FreqEnvStartRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1FreqEnvStartRatio, osc1FreqEnvStartRatio); }); }); });
  }
  setOsc1FreqEnvEndRatio { | ratio = 1.0 |
    osc1FreqEnvEndRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1FreqEnvEndRatio, osc1FreqEnvEndRatio); }); }); });
  }
  setOsc1FreqEnvTime { | time = 0.0 |
    osc1FreqEnvTime = time;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1FreqEnvTime, osc1FreqEnvTime); }); }); });
  }

  // Oscillator 1 LFO 1:
  setOsc1FreqLFOBottomRatio { | ratio = 1.0 |
    osc1FreqLFOBottomRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1FreqLFOBottomRatio, osc1FreqLFOBottomRatio); }); }); });
  }

  setOsc1FreqLFOTopRatio { | ratio = 1.0 |
    osc1FreqLFOTopRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1FreqLFOTopRatio, osc1FreqLFOTopRatio); }); }); });
  }

  setOsc1PulseWidthLFOBottom { | bottom = 0.0 |
    osc1PulseWidthLFOBottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1PulseWidthLFOBottom, osc1PulseWidthLFOBottom); }); }); });
  }

  setOsc1PulseWidthLFOTop { | top = 0.0 |
    osc1PulseWidthLFOTop = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1PulseWidthLFOTop, osc1PulseWidthLFOTop); }); }); });
  }

  setOsc1AmpLFOBottom { | bottom = 1.0 |
    osc1AmpLFOBottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1AmpLFOBottom, osc1AmpLFOBottom); }); }); });
  }

  setOsc1AmpLFOTop { | top = 1.0 |
    osc1AmpLFOTop = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1AmpLFOTop, osc1AmpLFOTop); }); }); });
  }

  setOsc1WaveformLFOBottom { | bottom = 0.0 |
    osc1WaveformLFOBottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1WaveformLFOBottom, osc1WaveformLFOBottom); }); }); });
  }

  setOsc1WaveformLFOTop { | top = 0.0 |
    osc1WaveformLFOTop = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1WaveformLFOTop, osc1WaveformLFOTop); }); }); });
  }

   // Oscillator 1 LFO 2:
  setOsc1FreqLFO2BottomRatio { | ratio = 1.0 |
    osc1FreqLFO2BottomRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1FreqLFO2BottomRatio, osc1FreqLFO2BottomRatio); }); }); });
  }

  setOsc1FreqLFO2TopRatio { | ratio = 1.0 |
    osc1FreqLFO2TopRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1FreqLFO2TopRatio, osc1FreqLFO2TopRatio); }); }); });
  }

  setOsc1PulseWidthLFO2Bottom { | bottom = 0.0 |
    osc1PulseWidthLFO2Bottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1PulseWidthLFO2Bottom, osc1PulseWidthLFO2Bottom); }); }); });
  }

  setOsc1PulseWidthLFO2Top { | top = 0.0 |
    osc1PulseWidthLFO2Top = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1PulseWidthLFO2Top, osc1PulseWidthLFO2Top); }); }); });
  }

  setOsc1AmpLFO2Bottom { | bottom = 1.0 |
    osc1AmpLFO2Bottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1AmpLFO2Bottom, osc1AmpLFO2Bottom); }); }); });
  }

  setOsc1AmpLFO2Top { | top = 1.0 |
    osc1AmpLFO2Top = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1AmpLFO2Top, osc1AmpLFO2Top); }); }); });
  }

  setOsc1WaveformLFO2Bottom { | bottom = 0.0 |
    osc1WaveformLFO2Bottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1WaveformLFO2Bottom, osc1WaveformLFO2Bottom); }); }); });
  }

  setOsc1WaveformLFO2Top { | top = 0.0 |
    osc1WaveformLFO2Top = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc1WaveformLFO2Top, osc1WaveformLFO2Top); }); }); });
  }


  //// Oscillator 2:

  setOsc2Vol { | vol = -6 |
    osc2Amp = vol.dbamp;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\osc2Amp, osc2Amp); }); }); });
  }

  setOsc2SubVol { | vol = -70 |
    osc2SubAmp = vol.dbamp;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\osc2SubAmp, osc2SubAmp); }); }); });
  }

  setOsc2Waveform { | waveform = 'saw' |
    switch(waveform,
      { 'sine' }, { osc2Waveform = 0 },
      { 'tri' }, { osc2Waveform = 1 },
      { 'saw' }, { osc2Waveform = 2 },
      { 'rect' }, { osc2Waveform = 3 }
    );
    if(waveform.isInteger, { osc2Waveform = waveform; });
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\osc2Waveform, osc2Waveform); }); }); });
  }

  setOsc2OctaveMul { | octaveMul = 1 |
    osc2OctaveMul = octaveMul;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\osc2OctaveMul, osc2OctaveMul); }); }); });
  }

  setOsc2Octave { | octave = 3 |
    switch(octave,
      { 0 }, { this.setOsc2OctaveMul(0.125) },
      { 1 }, { this.setOsc2OctaveMul(0.25) },
      { 2 }, { this.setOsc2OctaveMul(0.5) },
      { 3 }, { this.setOsc2OctaveMul(1) },
      { 4 }, { this.setOsc2OctaveMul(2) },
      { 5 }, { this.setOsc2OctaveMul(3) },
      { 6 }, { this.setOsc2OctaveMul(4) }
    );
  }

  setOsc2PulseWidth { | pulseWidth = 0.5 |
    osc2PulseWidth = pulseWidth;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2PulseWidth, pulseWidth); }); }); });
  }

  setOsc2DetuneCents { | detune = 0 |
    osc2DetuneCents = detune;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2DetuneCents, osc2DetuneCents); }); }); });
  }

  // frequency envelope:
  setOsc2FreqEnvStartRatio { | ratio = 1.0 |
    osc2FreqEnvStartRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2FreqEnvStartRatio, osc2FreqEnvStartRatio); }); }); });
  }
  setOsc2FreqEnvEndRatio { | ratio = 1.0 |
    osc2FreqEnvEndRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2FreqEnvEndRatio, osc2FreqEnvEndRatio); }); }); });
  }
  setOsc2FreqEnvTime { | time = 0.0 |
    osc2FreqEnvTime = time;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2reqEnvTime, osc2FreqEnvTime); }); }); });
  }

  // Oscillator 2 LFO 1:
  setOsc2FreqLFOBottomRatio { | ratio = 1.0 |
    osc2FreqLFOBottomRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2FreqLFOBottomRatio, osc2FreqLFOBottomRatio); }); }); });
  }

  setOsc2FreqLFOTopRatio { | ratio = 1.0 |
    osc2FreqLFOTopRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2FreqLFOTopRatio, osc2FreqLFOTopRatio); }); }); });
  }

  setOsc2PulseWidthLFOBottom { | bottom = 0.0 |
    osc2PulseWidthLFOBottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2PulseWidthLFOBottom, osc2PulseWidthLFOBottom); }); }); });
  }

  setOsc2PulseWidthLFOTop { | top = 0.0 |
    osc2PulseWidthLFOTop = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2PulseWidthLFOTop, osc2PulseWidthLFOTop); }); }); });
  }

  setOsc2AmpLFOBottom { | bottom = 1.0 |
    osc2AmpLFOBottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2AmpLFOBottom, osc2AmpLFOBottom); }); }); });
  }

  setOsc2AmpLFOTop { | top = 1.0 |
    osc2AmpLFOTop = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2AmpLFOTop, osc2AmpLFOTop); }); }); });
  }

  setOsc2WaveformLFOBottom { | bottom = 0.0 |
    osc2WaveformLFOBottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2WaveformLFOBottom, osc2WaveformLFOBottom); }); }); });
  }

  setOsc2WaveformLFOTop { | top = 0.0 |
    osc2WaveformLFOTop = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2WaveformLFOTop, osc2WaveformLFOTop); }); }); });
  }

  // Oscillator 2 LFO 2:
  setOsc2FreqLFO2BottomRatio { | ratio = 1.0 |
    osc2FreqLFO2BottomRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2FreqLFO2BottomRatio, osc2FreqLFO2BottomRatio); }); }); });
  }

  setOsc2FreqLFO2TopRatio { | ratio = 1.0 |
    osc2FreqLFO2TopRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2FreqLFO2TopRatio, osc2FreqLFO2TopRatio); }); }); });
  }

  setOsc2PulseWidthLFO2Bottom { | bottom = 0.0 |
    osc2PulseWidthLFO2Bottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2PulseWidthLFO2Bottom, osc2PulseWidthLFO2Bottom); }); }); });
  }

  setOsc2PulseWidthLFO2Top { | top = 0.0 |
    osc2PulseWidthLFO2Top = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2PulseWidthLFO2Top, osc2PulseWidthLFO2Top); }); }); });
  }

  setOsc2AmpLFO2Bottom { | bottom = 1.0 |
    osc2AmpLFO2Bottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2AmpLFO2Bottom, osc2AmpLFO2Bottom); }); }); });
  }

  setOsc2AmpLFO2Top { | top = 1.0 |
    osc2AmpLFO2Top = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2AmpLFO2Top, osc2AmpLFO2Top); }); }); });
  }

  setOsc2WaveformLFO2Bottom { | bottom = 0.0 |
    osc2WaveformLFO2Bottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2WaveformLFO2Bottom, osc2WaveformLFO2Bottom); }); }); });
  }

  setOsc2WaveformLFO2Top { | top = 0.0 |
    osc2WaveformLFO2Top = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\osc2WaveformLFO2Top, osc2WaveformLFO2Top); }); }); });
  }


  //// Noise Oscillator:
  setNoiseOscVol { | vol = -70 |
    noiseOscAmp = vol.dbamp;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\noiseOscAmp, noiseOscAmp); }); }); });
  }

  setNoiseOscCutoff { | cutoff = 1000 |
    noiseOscCutoff = cutoff;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\noiseOscCutoff, noiseOscCutoff); }); }); });
  }

  // Noise Oscillator LFO 1:

  setNoiseOscAmpLFOBottom { | bottom = 1.0 |
    noiseOscAmpLFOBottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\noiseOscAmpLFOBottom, noiseOscAmpLFOBottom); }); }); });
  }

  setNoiseOscAmpLFOTop { | top = 1.0 |
    noiseOscAmpLFOTop = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\noiseOscAmpLFOTop, noiseOscAmpLFOTop); }); }); });
  }

  setNoiseOscFilterLFOBottomRatio { | ratio = 1.0 |
    noiseOscFilterLFOBottomRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\noiseOscFilterLFOBottomRatio, noiseOscFilterLFOBottomRatio);
      });
      });
    });
  }

  setNoiseOscFilterLFOTopRatio { | ratio = 1.0 |
    noiseOscFilterLFOTopRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\noiseOscFilterLFOTopRatio, noiseOscFilterLFOTopRatio); }); });
    });
  }

  // Noise Oscillator LFO 2:

  setNoiseOscAmpLFO2Bottom { | bottom = 1.0 |
    noiseOscAmpLFO2Bottom = bottom;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\noiseOscAmpLFO2Bottom, noiseOscAmpLFO2Bottom); }); }); });
  }

  setNoiseOscAmpLFO2Top { | top = 1.0 |
    noiseOscAmpLFO2Top = top;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\noiseOscAmpLFO2Top, noiseOscAmpLFO2Top); }); }); });
  }

  setNoiseOscFilterLFO2BottomRatio { | ratio = 1.0 |
    noiseOscFilterLFO2BottomRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\noiseOscFilterLFO2BottomRatio, noiseOscFilterLFO2BottomRatio);
      });
      });
    });
  }

  setNoiseOscFilterLFO2TopRatio { | ratio = 1.0 |
    noiseOscFilterLFO2TopRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\noiseOscFilterLFO2TopRatio, noiseOscFilterLFO2TopRatio); }); });
    });
  }

  //// LFO1:
  setLFO1Freq { | freq = 1 |
    lfoFreq = freq;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\lfoFreq, lfoFreq); }); }); });
  }

  setLFO1Waveform { | waveform = 'sine' |
    switch(waveform,
      { 'sine' }, { lfoWaveform = 0 },
      { 'tri' }, { lfoWaveform = 1 },
      { 'saw' }, { lfoWaveform = 2 },
      { 'rect' }, { lfoWaveform = 3 },
      { 'sampleAndHold' }, { lfoWaveform = 4 },
      { 'noise' }, { lfoWaveform = 5 }
    );
    if(waveform.isInteger, { osc2Waveform = waveform; });
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\lfoWaveform, lfoWaveform); }); }); });
  }

  setLFOPulseWidth { | pulseWidth = 0.5 |
    lfoPulseWidth = pulseWidth;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, {synth.set(\lfoPulseWidth, lfoPulseWidth); }); }); });
  }

  setLFOEnvType { | type = 'none' |
    switch(type,
      { 'none' }, { lfoEnvType = 0 },
      { 'attack' }, { lfoEnvType = 1 },
      { 'release' }, { lfoEnvType = 2 },
      { 'attackAndRelease' }, { lfoEnvType = 3 }
    );
    if( type.isInteger, { lfoEnvType = type; });
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\lfoEnvType, lfoEnvType); }); }); });
  }

  setLFOAttackTime { | attack = 0.05 |
    lfoAttackTime = attack;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\lfoAttackTime, lfoAttackTime); }); }); });
  }

  setLFOReleaseTime { | release = 0.05 |
    lfoReleaseTime = release;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\lfoReleaseTime, lfoReleaseTime); }); }); });
  }

  setLFOFreqLFO2BottomRatio { | ratio = 1.0 |
    lfoFreqLFOBottomRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\lfoFreqLFOBottomRatio, lfoFreqLFOBottomRatio); }); }); });
  }

  setLFOFreqLFO2TopRatio { | ratio = 1.0 |
    lfoFreqLFOTopRatio = ratio;
    if( applyMode, { voiceArray.do({ | synth | if( synth.notNil, { synth.set(\lfoFreqLFOTopRatio, lfoFreqLFOTopRatio); }); }); });
  }

  // LFO 2:
  setLFO2Freq { | freq = 1.0 |
    lfo.set(\freq, freq);
  }

  setLFO2PulseWidth { | width = 0.5 |
    lfo.set(\lfoPulseWidth, width);
  }

  setLFO2Waveform { | waveform = 'sine' |
    switch(waveform,
      { 'sine' }, { lfo.set(\lfoWaveform, 0); },
      { 'tri' }, { lfo.set(\lfoWaveform, 1); },
      { 'saw' }, { lfo.set(\lfoWaveform, 2); },
      { 'rect' }, { lfo.set(\lfoWaveform, 3); },
      { 'sampleAndHold' }, { lfo.set(\lfoWaveform, 4); },
      { 'noise' }, { lfo.set(\lfoWaveform, 5); }
    );
    if(waveform.isInteger, { lfo.set(\lfoWaveform, waveform); });
  }

}