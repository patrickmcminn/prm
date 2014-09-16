/*
Saturday, June 28th 2014
Subtractive.sc
prm
*/

Subtractive : IM_Module {

  var <isLoaded;

  var lfo, <synthDict, <synthGroup, orderArray, <lfoBus, <maxVoices, numVoices, orderNum;

  var presetDict;

  var applyMode;

  var <lfoFreqLFOBottomRatio, <lfoFreqLFOTopRatio;
  var <lfoWaveform, <lfoFreq, <lfoPulseWidth;
  var <lfoEnvType, <lfoAttackTime, <lfoReleaseTime;

  var <osc1OctaveMul;
  var <osc1FreqEnvStartRatio, <osc1FreqEnvEndRatio, <osc1FreqEnvTime;
  var <osc1FreqLFOBottomRatio, <osc1FreqLFOTopRatio, <osc1FreqLFO2BottomRatio, <osc1FreqLFO2TopRatio;
  var <osc1PulseWidthLFOBottom, <osc1PulseWidthLFOTop, <osc1PulseWidthLFO2Bottom, <osc1PulseWidthLFO2Top;
  var <osc1AmpLFOBottom, <osc1AmpLFOTop, <osc1AmpLFO2Bottom, <osc1AmpLFO2Top;
  var <osc1WaveformLFOBottom, <osc1WaveformLFOTop, <osc1WaveformLFO2Bottom, <osc1WaveformLFO2Top;
  var <osc1Waveform, <osc1PulseWidth, <osc1Amp, <osc1SubAmp;

  var <osc2OctaveMul, <osc2DetuneCents;
  var <osc2FreqEnvStartRatio, <osc2FreqEnvEndRatio, <osc2FreqEnvTime;
  var <osc2FreqLFOBottomRatio, <osc2FreqLFOTopRatio, <osc2FreqLFO2BottomRatio, <osc2FreqLFO2TopRatio;
  var <osc2PulseWidthLFOBottom, <osc2PulseWidthLFOTop, <osc2PulseWidthLFO2Bottom, <osc2PulseWidthLFO2Top;
  var <osc2AmpLFOBottom, <osc2AmpLFOTop, <osc2AmpLFO2Bottom, <osc2AmpLFO2Top;
  var <osc2WaveformLFOBottom, <osc2WaveformLFOTop, <osc2WaveformLFO2Bottom, <osc2WaveformLFO2Top;
  var <osc2Waveform, <osc2PulseWidth, <osc2Amp, <osc2SubAmp;

  var <noiseOscAmpLFOBottom, <noiseOscAmpLFOTop, <noiseOscAmpLFO2Bottom, <noiseOscAmpLFO2Top;
  var <noiseOscFilterLFOBottomRatio, <noiseOscFilterLFOTopRatio, <noiseOscFilterLFO2BottomRatio;
  var <noiseOscFilterLFO2TopRatio;
  var <noiseOscAmp, <noiseOscCutoff;

  var <filterEnvAttackRatio, <filterEnvPeakRatio, <filterEnvSustainRatio, <filterEnvReleaseRatio;
  var <filterEnvAttackTime, <filterEnvDecayTime, <filterEnvReleaseTime;
  var <filterEnvLoop;
  var <filterCutoffLFOBottomRatio, <filterCutoffLFOTopRatio, <filterCutoffLFO2BottomRatio, <filterCutoffLFO2TopRatio;
  var <filterResLFOBottom, <filterResLFOTop, <filterResLFO2Bottom, <filterResLFO2Top;
  var <filterDrive, <filterCutoff, <filterRes, <filterType;

  var <ampLFOBottom, <ampLFOTop, <ampLFO2Bottom, <ampLFO2Top;
  var <attackTime, <decayTime, <sustainLevel, <releaseTime;

  var <pan, <panLFOBottom, <panLFOTop, <panLFO2Bottom, <panLFO2Top, <amp;

  var server;

  var <sequencerDict, <sequencerClock, <tempo, <beats;

  *new { | outBus, send1Bus = nil, send2Bus = nil, send3Bus = nil, send4Bus = nil, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send1Bus, send2Bus, send3Bus, send4Bus, false, relGroup, addAction).prInit(relGroup);
  }

  prInit { | relGroup = nil |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait });

      this.prAddSynthDefs;
      this.prInitializeParameters;

      synthDict = IdentityDictionary.new;
      orderArray = Array.fill(maxVoices, { nil });
      presetDict = IdentityDictionary.new;
      sequencerDict = IdentityDictionary.new;
      sequencerClock = TempoClock.new(tempo, beats);
      lfoBus = Bus.control;
      server.sync;
      lfo = Synth(\prm_Subtractive_LFO, [\outBus, lfoBus], relGroup, \addToHead);
      synthGroup = Group.new(lfo, \addAfter);
      server.sync;
      isLoaded = true;
    }
  }

  prAddSynthDefs {

    SynthDef(\prm_Subtractive_LFO, {
      | outBus = 0, lfoWaveform = 0, freq = 1, lfoPulseWidth = 0.5 |
      var lfo, lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2;
      lfoSine = SinOsc.kr(freq);
      lfoSaw = LFSaw.kr(freq, 1);
      lfoRevSaw = LFSaw.kr(freq, 1) * -1;
      lfoRect = LFPulse.kr(freq, width: lfoPulseWidth);
      lfoNoise0 = LFNoise0.kr(freq);
      lfoNoise2 = LFNoise2.kr(freq);
      lfo = SelectX.kr(lfoWaveform, [lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2]);
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
      drive = 1.0, filterCutoff = 2500, filterRes = 0, filterType = 0,

      ampLFOBottom = 1.0, ampLFOTop = 1.0, ampLFO2Bottom = 1.0, ampLFO2Top = 1.0,
      attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05,

      pan = 0,
      panLFOBottom = 0.0, panLFOTop = 0.0, panLFO2Bottom = 0.0, panLFO2Top = 0.0
      |

      var lfo2;
      var lfo, lfoFreqLFO, thisLFOFreq, lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2;
      var lfoAttackEnv, lfoReleaseEnv, lfoAttackAndReleaseEnv;

      var osc1Freq, osc1FreqEnv, osc1FreqLFO, osc1FreqLFO2, osc1WidthLFO, osc1WidthLFO2;
      var osc1AmpLFO, osc1AmpLFO2, osc1WaveformLFO, osc1WaveformLFO2;
      var osc1, osc1Sine, osc1Tri, osc1Saw, osc1Rect, osc1Sub;

      var osc2Freq, osc2FreqEnv, osc2FreqLFO, osc2FreqLFO2, osc2WidthLFO, osc2WidthLFO2;
      var osc2AmpLFO, osc2AmpLFO2, osc2WaveformLFO, osc2WaveformLFO2;
      var osc2, osc2Sine, osc2Tri, osc2Saw, osc2Rect, osc2Sub;

      var noiseOsc, noise, noiseOscFilter, noiseOscAmpLFO, noiseOscAmpLFO2, noiseOscFilterLFO, noiseOscFilterLFO2;

      var oscSum;

      var preFilterDrive, filter, thisFilterCutoff, thisFilterRes, lowPassFilter, highPassFilter, bandPassFilter;
      var filterCutoffLFO, filterCutoffLFO2, filterResLFO, filterResLFO2, filterEnv;

      var ampEnv, ampLFO, ampLFO2;
      var panning, panningLFO, panningLFO2;

      var sig;

      lfo2 = In.kr(lfo2InBus);

      // Voice LFO:
      lfoFreqLFO = lfo2.linlin(-1, 1, lfoFreqLFOBottomRatio, lfoFreqLFOTopRatio);
      thisLFOFreq = lfoFreq * lfoFreqLFO;
      lfoSine = SinOsc.kr(thisLFOFreq);
      lfoSaw = LFSaw.kr(thisLFOFreq, 1);
      lfoRevSaw = LFSaw.kr(thisLFOFreq, 1) * -1;
      lfoRect = (LFPulse.kr(thisLFOFreq, width: lfoPulseWidth) - 0.5) * 2;
      lfoNoise0 = LFNoise0.kr(thisLFOFreq);
      lfoNoise2 = LFNoise2.kr(thisLFOFreq);
      lfoAttackEnv = EnvGen.kr(Env.new([0, 0, 1], [0, lfoAttackTime], 'cubed', 1), gate);
      lfoReleaseEnv = EnvGen.kr(Env.cutoff(lfoReleaseTime, 1, 'cubed'), gate);
      lfoAttackAndReleaseEnv = EnvGen.kr(Env.asr(lfoAttackTime, 1, lfoReleaseTime), gate);
      lfoAttackAndReleaseEnv = EnvGen.kr(Env.new([0, 0, 1, 0], [0, lfoAttackTime, lfoReleaseTime], 'cubed', 1),
        gate);
      lfo = SelectX.kr(lfoWaveform, [lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2]);
      lfo = Select.kr(lfoEnvType, [lfo, lfo * lfoAttackEnv, lfo * lfoReleaseEnv, lfo * lfoAttackAndReleaseEnv]);


      // Oscillator 1:
      osc1FreqEnv = EnvGen.kr(Env.new([osc1FreqEnvStartRatio, osc1FreqEnvStartRatio, osc1FreqEnvEndRatio],
        [0, osc1FreqEnvTime], 'exp'), gate);
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
      osc1 = SelectX.ar((osc1Waveform.lag(0.1) + osc1WaveformLFO + osc1WaveformLFO2).wrap(0.0, 4.0),
        [osc1Sine, osc1Tri, osc1Saw, osc1Rect]);

      osc1 = osc1 * osc1Amp;
      osc1 = osc1 + osc1Sub;
      osc1 = osc1 * osc1AmpLFO * osc1AmpLFO2;

      // Oscillator 2:
      osc2FreqEnv = EnvGen.kr(Env.new([osc2FreqEnvStartRatio, osc2FreqEnvStartRatio, osc2FreqEnvEndRatio],
        [0, osc2FreqEnvTime], 'exp'), gate);
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
      osc2 = SelectX.ar((osc2Waveform.lag(0.1) + osc2WaveformLFO + osc2WaveformLFO2).wrap(0.0, 4.0),
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
        Env.new([filterEnvAttackRatio, filterEnvAttackRatio, filterEnvPeakRatio,
          filterEnvSustainRatio, filterEnvReleaseRatio],
          [0, filterEnvAttackTime, filterEnvDecayTime, filterEnvReleaseTime], -4, 3, filterEnvLoop),
        gate);
      filterCutoffLFO = lfo.linlin(-1, 1, filterCutoffLFOBottomRatio, filterCutoffLFOTopRatio);
      filterCutoffLFO2 = lfo2.linlin(-1, 1, filterCutoffLFO2BottomRatio, filterCutoffLFO2TopRatio);
      filterResLFO = lfo.linlin(-1, 1, filterResLFOBottom, filterResLFOTop);
      filterResLFO2 = lfo2.linlin(-1, 1, filterResLFO2Bottom, filterResLFO2Top);
      thisFilterCutoff = filterCutoff.lag(0.1) * filterEnv * filterCutoffLFO * filterCutoffLFO2;
      thisFilterRes = filterRes + filterResLFO + filterResLFO2;
      preFilterDrive = (oscSum * drive).distort;
      lowPassFilter = DFM1.ar(preFilterDrive, thisFilterCutoff, thisFilterRes, 1, 0, 0.0005);
      highPassFilter = DFM1.ar(preFilterDrive, thisFilterCutoff, thisFilterRes, 1, 1, 0.0005);
      bandPassFilter = BBandPass.ar(preFilterDrive, thisFilterCutoff, filterRes);
      filter = Select.ar(filterType, [lowPassFilter, highPassFilter, bandPassFilter]);

      // Amplitude Envelope:
      ampLFO = lfo.linlin(-1, 1, ampLFOBottom, ampLFOTop);
      ampLFO2 = lfo2.linlin(-1, 1, ampLFO2Bottom, ampLFO2Top);
      //ampEnv = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4), gate);
      ampEnv = EnvGen.kr(Env.new([0, 0, 1, sustainLevel, 0], [0, attackTime, decayTime, releaseTime],
        curve: -4, releaseNode: 3), gate);
      ampEnv = ampEnv * ampLFO * ampLFO2;

      panningLFO = lfo.linlin(-1, 1, panLFOBottom, panLFOTop);
      panningLFO2 = lfo2.linlin(-1, 1, panLFO2Bottom, panLFO2Top);
      panning = Pan2.ar(filter * ampEnv, (pan + panningLFO + panningLFO2).clip(-1, 1));

      sig = panning * amp;

      Out.ar(outBus, sig);

    }).add;

    SynthDef(\prm_Subtractive_Voice_Seq, {
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
      drive = 1.0, filterCutoff = 2500, filterRes = 0, filterType = 0,

      ampLFOBottom = 1.0, ampLFOTop = 1.0, ampLFO2Bottom = 1.0, ampLFO2Top = 1.0,
      attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05,

      pan = 0,
      panLFOBottom = 0.0, panLFOTop = 0.0, panLFO2Bottom = 0.0, panLFO2Top = 0.0
      |

      var lfo2;
      var lfo, lfoFreqLFO, thisLFOFreq, lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2;
      var lfoAttackEnv, lfoReleaseEnv, lfoAttackAndReleaseEnv;

      var osc1Freq, osc1FreqEnv, osc1FreqLFO, osc1FreqLFO2, osc1WidthLFO, osc1WidthLFO2;
      var osc1AmpLFO, osc1AmpLFO2, osc1WaveformLFO, osc1WaveformLFO2;
      var osc1, osc1Sine, osc1Tri, osc1Saw, osc1Rect, osc1Sub;

      var osc2Freq, osc2FreqEnv, osc2FreqLFO, osc2FreqLFO2, osc2WidthLFO, osc2WidthLFO2;
      var osc2AmpLFO, osc2AmpLFO2, osc2WaveformLFO, osc2WaveformLFO2;
      var osc2, osc2Sine, osc2Tri, osc2Saw, osc2Rect, osc2Sub;

      var noiseOsc, noise, noiseOscFilter, noiseOscAmpLFO, noiseOscAmpLFO2, noiseOscFilterLFO, noiseOscFilterLFO2;

      var oscSum;

      var preFilterDrive, filter, thisFilterCutoff, thisFilterRes, lowPassFilter, highPassFilter, bandPassFilter;
      var filterCutoffLFO, filterCutoffLFO2, filterResLFO, filterResLFO2, filterEnv;

      var ampEnv, ampLFO, ampLFO2;
      var panning, panningLFO, panningLFO2;

      var sig;

      lfo2 = In.kr(lfo2InBus);

      // Voice LFO:
      lfoFreqLFO = lfo2.linlin(-1, 1, lfoFreqLFOBottomRatio, lfoFreqLFOTopRatio);
      thisLFOFreq = lfoFreq * lfoFreqLFO;
      lfoSine = SinOsc.kr(thisLFOFreq);
      lfoSaw = LFSaw.kr(thisLFOFreq,1);
      lfoRevSaw = LFSaw.kr(thisLFOFreq, 1) * -1;
      lfoRect = (LFPulse.kr(thisLFOFreq, width: lfoPulseWidth) - 0.5) * 2;
      lfoNoise0 = LFNoise0.kr(thisLFOFreq);
      lfoNoise2 = LFNoise2.kr(thisLFOFreq);
      lfoAttackEnv = EnvGen.kr(Env.new([0, 0, 1], [0, lfoAttackTime], 'cubed', 1), gate);
      lfoReleaseEnv = EnvGen.kr(Env.cutoff(lfoReleaseTime, 1, 'cubed'), gate);
      lfoAttackAndReleaseEnv = EnvGen.kr(Env.asr(lfoAttackTime, 1, lfoReleaseTime), gate);
      lfoAttackAndReleaseEnv = EnvGen.kr(Env.new([0, 0, 1, 0], [0, lfoAttackTime, lfoReleaseTime], 'cubed', 1),
        gate);
      lfo = SelectX.kr(lfoWaveform, [lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2]);
      lfo = Select.kr(lfoEnvType, [lfo, lfo * lfoAttackEnv, lfo * lfoReleaseEnv, lfo * lfoAttackAndReleaseEnv]);


      // Oscillator 1:
      osc1FreqEnv = EnvGen.kr(Env.new([osc1FreqEnvStartRatio, osc1FreqEnvStartRatio, osc1FreqEnvEndRatio],
        [0, osc1FreqEnvTime], 'exp'), gate);
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
      osc1 = SelectX.ar((osc1Waveform.lag(0.1) + osc1WaveformLFO + osc1WaveformLFO2).wrap(0.0, 4.0),
        [osc1Sine, osc1Tri, osc1Saw, osc1Rect]);

      osc1 = osc1 * osc1Amp;
      osc1 = osc1 + osc1Sub;
      osc1 = osc1 * osc1AmpLFO * osc1AmpLFO2;

      // Oscillator 2:
      osc2FreqEnv = EnvGen.kr(Env.new([osc2FreqEnvStartRatio, osc2FreqEnvStartRatio, osc2FreqEnvEndRatio],
        [0, osc2FreqEnvTime], 'exp'), gate);
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
      osc2 = SelectX.ar((osc2Waveform.lag(0.1) + osc2WaveformLFO + osc2WaveformLFO2).wrap(0.0, 4.0),
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
        Env.new([filterEnvAttackRatio, filterEnvAttackRatio, filterEnvPeakRatio,
          filterEnvSustainRatio, filterEnvReleaseRatio],
          [0, filterEnvAttackTime, filterEnvDecayTime, filterEnvReleaseTime], -4, 3, filterEnvLoop),
        gate);
      filterCutoffLFO = lfo.linlin(-1, 1, filterCutoffLFOBottomRatio, filterCutoffLFOTopRatio);
      filterCutoffLFO2 = lfo2.linlin(-1, 1, filterCutoffLFO2BottomRatio, filterCutoffLFO2TopRatio);
      filterResLFO = lfo.linlin(-1, 1, filterResLFOBottom, filterResLFOTop);
      filterResLFO2 = lfo2.linlin(-1, 1, filterResLFO2Bottom, filterResLFO2Top);
      thisFilterCutoff = filterCutoff.lag(0.1) * filterEnv * filterCutoffLFO * filterCutoffLFO2;
      thisFilterRes = filterRes + filterResLFO + filterResLFO2;
      preFilterDrive = (oscSum * drive).distort;
      lowPassFilter = DFM1.ar(preFilterDrive, thisFilterCutoff, thisFilterRes, 1, 0, 0.0005);
      highPassFilter = DFM1.ar(preFilterDrive, thisFilterCutoff, thisFilterRes, 1, 1, 0.0005);
      bandPassFilter = BBandPass.ar(preFilterDrive, thisFilterCutoff, filterRes);
      filter = Select.ar(filterType, [lowPassFilter, highPassFilter, bandPassFilter]);

      // Amplitude Envelope:
      ampLFO = lfo.linlin(-1, 1, ampLFOBottom, ampLFOTop);
      ampLFO2 = lfo2.linlin(-1, 1, ampLFO2Bottom, ampLFO2Top);
      //ampEnv = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4), gate);
      ampEnv = EnvGen.kr(Env.new([0, 0, 1, sustainLevel, 0], [0, attackTime, decayTime, releaseTime],
        curve: -4, releaseNode: 3), gate, doneAction: 2);
      ampEnv = ampEnv * ampLFO * ampLFO2;

      panningLFO = lfo.linlin(-1, 1, panLFOBottom, panLFOTop);
      panningLFO2 = lfo2.linlin(-1, 1, panLFO2Bottom, panLFO2Top);
      panning = Pan2.ar(filter * ampEnv, (pan + panningLFO + panningLFO2).clip(-1, 1));

      sig = panning * amp;

      Out.ar(outBus, sig);

    }).add;
  }

  prManageOrder { | nilPos |
    var subArray1, subArray2, newArray;
    subArray1 = Array.fill(nilPos, { | i | orderArray[i]; });
    subArray2 = Array.fill(orderArray.size - (nilPos + 1), { | i | orderArray[i + nilPos + 1] });
    newArray = subArray1 ++ subArray2;
    newArray = newArray.add(nil);
    orderArray = newArray;
    orderNum = orderArray.find([nil]);
  }

  prStealVoice { | freq |
    var oldFreq, voiceArray, nilArray;
    oldFreq = orderArray[0];
    synthDict[oldFreq].steal(freq);
    synthDict[freq] = synthDict[oldFreq];
    synthDict[oldFreq] = nil;
    synthDict.removeAt(oldFreq);
    orderArray[0] = freq;
    orderArray = orderArray.rotate(-1);
  }


  //////// public functions:

  free {
    sequencerDict.do({ | sequence | sequence.free; });
    sequencerClock.clear;
    sequencerDict.stop;
    sequencerDict = nil;
    synthDict.do({ | voice | voice.free; });
    lfo.free;
    lfo = nil;
    lfoBus.free;
    lfoBus = nil;
    synthGroup.free;
    synthGroup = nil;

    this.freeModule;
  }

  playNote { | freq = 220 |
    {
      var playTest, order;
      playTest = try { synthDict[freq] }.isPlaying;
      if( playTest == true, { synthDict[freq].steal(freq); }, {
        if( numVoices < maxVoices, {
          // assign synth to a the synth dict:
          synthDict[freq] = Subtractive_Voice.new(freq, this, synthGroup, \addToTail);
          // put synth marker in the correct order slot:
          orderArray[orderNum] = freq;
          // increment the number of voices:
          numVoices = numVoices + 1;
          // increment the order of voices:
          orderNum = orderNum + 1;
          },
          { this.prStealVoice(freq); });
      });
    }.fork;
  }

  releaseNote { | freq = 220 |
    var orderPos;
    var synth = synthDict[freq];
    {
      if( synthDict[freq].isPlaying == true, {
        synth.release;
        while( { synthDict[freq].isPlaying == true }, { 0.001.wait; });
        orderPos = orderArray.find([freq]);
        //orderPos.postln;

        if( synthDict[freq].isPlaying == false && orderPos.notNil, {
          synthDict[freq] = nil;
          synthDict.removeAt(freq);
          //orderPos.postln;
          orderArray[orderPos] = nil;
          this.prManageOrder(orderPos);
          numVoices = numVoices - 1;
        });
      });
    }.fork;
  }

  releaseAllNotes {
    orderArray.do({ | freq |
      this.releaseNote(freq);
    });
  }
}