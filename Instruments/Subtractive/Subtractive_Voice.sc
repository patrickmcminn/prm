Subtractive_Voice : Subtractive {

  var <synth;
  var isPlaying, isReleasing;

  *new { | freq = 220 |
    ^super.new.prInit(freq);
  }

  prInit { | freq = 220 |
    var server = Server.default;
    server.waitForBoot {
      synth = Synth(\prm_Subtractive_Voice, [
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

        \osc2OctaveMul, osc2OctaveMul, \osc2DetuneCents, osc2DetuneCents,
        \osc2FreqEnvStartRatio, osc2FreqEnvStartRatio,
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
        \noiseOscFilterLFOBottomRatio, noiseOscFilterLFOBottomRatio,
        \noiseOscFilterLFOTopRatio, noiseOscFilterLFOTopRatio,
        \noiseOscFilterLFO2BottomRatio, noiseOscFilterLFO2BottomRatio,
        \noiseOscFilterLFO2TopRatio, noiseOscFilterLFO2TopRatio,
        \noiseOscAmp, noiseOscAmp, \noiseOscCutoff, noiseOscCutoff,

        \filterEnvAttackRatio, filterEnvAttackRatio, \filterEnvPeakRatio, filterEnvPeakRatio,
        \filterEnvSustainRatio, filterEnvSustainRatio, \filterEnvReleaseRatio, filterEnvReleaseRatio,
        \filterEnvAttackTime, filterEnvAttackTime, \filterEnvDecayTime, filterEnvDecayTime,
        \filterEnvReleaseTime, filterEnvReleaseTime, \filterEnvLoop, filterEnvLoop,
        \filterCutoffLFOBottomRatio, filterCutoffLFOBottomRatio,
        \filterCutoffLFOTopRatio, filterCutoffLFOTopRatio,
        \filterCutoffLFO2BottomRatio, filterCutoffLFO2BottomRatio,
        \filterCutoffLFO2TopRatio, filterCutoffLFO2TopRatio,
        \filterResLFOBottom, filterResLFOBottom, \filterResLFOTop, filterResLFOTop,
        \filterResLFO2Bottom, filterResLFO2Bottom, \filterResLFO2Top, filterResLFO2Top,
        \filterCutoff, filterCutoff, \filterRes, filterRes, \filterType, filterType,

        \ampLFOBottom, ampLFOBottom, \ampLFOTop, ampLFOTop, \ampLFO2Bottom, ampLFO2Bottom,
        \ampLFO2Top, ampLFO2Top, \attackTime, attackTime, \decayTime, decayTime,
        \sustainLevel, sustainLevel, \releaseTime, releaseTime,

        \pan, pan, \panLFOBottom, panLFOBottom, \panLFOTop, panLFOTop,
        \panLFO2Bottom, panLFO2Bottom, \panLFO2Top, panLFO2Top
      ], lfo, \addAfter);

      isPlaying = true;
      isReleasing = false;
    };
  }

  //////// public functions:

  free {
    synth.free;
    synth = nil;
  }

  release {
    {
      synth.set(\gate, 0);
      isReleasing = true;
      releaseTime.wait;
      isReleasing = false;
      isPlaying = false;
      this.free;
    }.fork;
  }

  steal { | freq = 220 |
    synth.set(\gate, -1.05);
    {
      synth.set(\gate, 1);
      synth.set(\freq, freq);
    }.defer(0.05);
  }

}