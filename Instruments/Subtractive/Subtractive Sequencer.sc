//////// Pattern Sequencer:
+ Subtractive {

  makeSequence { | uniqueName |
    fork {
      sequencerDict[uniqueName] = PatternSequencer.new(uniqueName,  lfo, \addAfter);
      server.sync;
      //1.wait;
      sequencerDict[uniqueName].addKey(\instrument, \prm_Subtractive_Voice_Seq);
      sequencerDict[uniqueName].addKey(\outBus,  mixer.chanStereo(0));
      sequencerDict[uniqueName].addKey(\lfo2InBus,  Pfunc({lfoBus}) );
      sequencerDict[uniqueName].addKey(\lfoFreqLFOBottomRatio, Pfunc({ lfoFreqLFOBottomRatio }));
      sequencerDict[uniqueName].addKey(  \lfoFreqLFOTopRatio,  Pfunc({ lfoFreqLFOTopRatio }));
      sequencerDict[uniqueName].addKey( \lfoWaveform,  Pfunc({ lfoWaveform }));
      sequencerDict[uniqueName].addKey(  \lfoFreq,  Pfunc({ lfoFreq }));
      sequencerDict[uniqueName].addKey( \lfoPulseWidth,  Pfunc({ lfoPulseWidth }));
      sequencerDict[uniqueName].addKey( \lfoEnvType,  Pfunc({ lfoEnvType }));
      sequencerDict[uniqueName].addKey( \lfoAttackTime,  Pfunc({ lfoAttackTime }));
      sequencerDict[uniqueName].addKey( \lfoReleaseTime,  Pfunc({ lfoReleaseTime }));

      sequencerDict[uniqueName].addKey( \osc1OctaveMul,  Pfunc({ osc1OctaveMul }));
      sequencerDict[uniqueName].addKey( \osc1FreqEnvStartRatio,  Pfunc({ osc1FreqEnvStartRatio }));
      sequencerDict[uniqueName].addKey( \osc1FreqEnvEndRatio,  Pfunc({ osc1FreqEnvEndRatio }));
      sequencerDict[uniqueName].addKey( \osc1FreqEnvTime,  Pfunc({ osc1FreqEnvTime }));
      sequencerDict[uniqueName].addKey( \osc1FreqLFOBottomRatio,  Pfunc({ osc1FreqLFOBottomRatio }));
      sequencerDict[uniqueName].addKey( \osc1FreqLFOTopRatio,  Pfunc({ osc1FreqLFOTopRatio }));
      sequencerDict[uniqueName].addKey( \osc1FreqLFO2BottomRatio,  Pfunc({ osc1FreqLFO2BottomRatio }));
      sequencerDict[uniqueName].addKey( \osc1FreqLFO2TopRatio,  Pfunc({ osc1FreqLFO2TopRatio }));
      sequencerDict[uniqueName].addKey( \osc1PulseWidthLFOBottom,  Pfunc({ osc1PulseWidthLFOBottom }));
      sequencerDict[uniqueName].addKey( \osc1PulseWidthLFOTop,  Pfunc({ osc1PulseWidthLFOTop }));
      sequencerDict[uniqueName].addKey( \osc1PulseWidthLFO2Bottom,  Pfunc({ osc1PulseWidthLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \osc1PulseWidthLFO2Top,  Pfunc({ osc1PulseWidthLFO2Top }));
      sequencerDict[uniqueName].addKey( \osc1AmpLFOBottom,  Pfunc({ osc1AmpLFOBottom }));
      sequencerDict[uniqueName].addKey( \osc1AmpLFOTop,  Pfunc({ osc1AmpLFOTop }));
      sequencerDict[uniqueName].addKey( \osc1AmpLFO2Bottom,  Pfunc({ osc1AmpLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \osc1AmpLFO2Top,  Pfunc({ osc1AmpLFO2Top }));
      sequencerDict[uniqueName].addKey( \osc1WaveformLFOBottom,  Pfunc({ osc1WaveformLFOBottom }));
      sequencerDict[uniqueName].addKey( \osc1WaveformLFOTop,  Pfunc({ osc1WaveformLFOTop }));
      sequencerDict[uniqueName].addKey( \osc1WaveformLFO2Bottom,  Pfunc({ osc1WaveformLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \osc1WaveformLFO2Top,  Pfunc({ osc1WaveformLFO2Top }));
      sequencerDict[uniqueName].addKey( \osc1Waveform,  Pfunc({ osc1Waveform }));
      sequencerDict[uniqueName].addKey( \osc1PulseWidth,  Pfunc({ osc1PulseWidth }));
      sequencerDict[uniqueName].addKey( \osc1Amp,  Pfunc({ osc1Amp }));

      sequencerDict[uniqueName].addKey( \osc1SubAmp,  Pfunc({ osc1SubAmp }));
      sequencerDict[uniqueName].addKey( \osc2OctaveMul,  Pfunc({ osc2OctaveMul }));
      sequencerDict[uniqueName].addKey( \osc2DetuneCents,  Pfunc({ osc2DetuneCents }));
      sequencerDict[uniqueName].addKey( \osc2FreqEnvStartRatio,  Pfunc({ osc2FreqEnvStartRatio }));
      sequencerDict[uniqueName].addKey( \osc2FreqEnvEndRatio ,  Pfunc({ osc2FreqEnvEndRatio }));
      sequencerDict[uniqueName].addKey( \osc2FreqEnvTime,  Pfunc({ osc2FreqEnvTime }));
      sequencerDict[uniqueName].addKey( \osc2FreqLFOBottomRatio,  Pfunc({ osc2FreqLFOBottomRatio }));
      sequencerDict[uniqueName].addKey( \osc2FreqLFOTopRatio,  Pfunc({ osc2FreqLFOTopRatio }));
      sequencerDict[uniqueName].addKey( \osc2FreqLFO2BottomRatio,  Pfunc({ osc2FreqLFO2BottomRatio }));
      sequencerDict[uniqueName].addKey( \osc2FreqLFO2TopRatio,  Pfunc({ osc2FreqLFO2TopRatio }));
      sequencerDict[uniqueName].addKey( \osc2PulseWidthLFOBottom,  Pfunc({ osc2PulseWidthLFOBottom }));
      sequencerDict[uniqueName].addKey( \osc2PulseWidthLFOTop,  Pfunc({ osc2PulseWidthLFOTop }));
      sequencerDict[uniqueName].addKey( \osc2PulseWidthLFO2Bottom,  Pfunc({ osc2PulseWidthLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \osc2PulseWidthLFO2Top,  Pfunc({ osc2PulseWidthLFOTop }));
      sequencerDict[uniqueName].addKey( \osc2AmpLFOBottom,  Pfunc({ osc2AmpLFOBottom }));
      sequencerDict[uniqueName].addKey( \osc2AmpLFOTop,  Pfunc({ osc2AmpLFOTop }));
      sequencerDict[uniqueName].addKey( \osc2AmpLFO2Bottom,  Pfunc({ osc2AmpLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \osc2AmpLFO2Top,  Pfunc({ osc2AmpLFO2Top }));
      sequencerDict[uniqueName].addKey( \osc2WaveformLFOBottom,  Pfunc({ osc2WaveformLFOBottom }));
      sequencerDict[uniqueName].addKey( \osc2WaveformLFOTop,  Pfunc({ osc2WaveformLFOTop }));
      sequencerDict[uniqueName].addKey( \osc2WaveformLFO2Bottom,  Pfunc({ osc2WaveformLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \osc2WaveformLFO2Top,  Pfunc({ osc2WaveformLFO2Top }));
      sequencerDict[uniqueName].addKey( \osc2Waveform,  Pfunc({ osc2Waveform }));
      sequencerDict[uniqueName].addKey(\osc2PulseWidth,  Pfunc({ osc2PulseWidth }));
      sequencerDict[uniqueName].addKey(\osc2Amp,  Pfunc({ osc2Amp }));
      sequencerDict[uniqueName].addKey(\osc2SubAmp,  Pfunc({ osc2SubAmp }));

      sequencerDict[uniqueName].addKey(\noiseOscAmpLFOBottom,  Pfunc({ noiseOscAmpLFOBottom }));
      sequencerDict[uniqueName].addKey(\noiseOscAmpLFOTop, Pfunc({  noiseOscAmpLFOTop }));
      sequencerDict[uniqueName].addKey(\noiseOscAmpLFO2Bottom,  Pfunc({ noiseOscAmpLFO2Bottom }));
      sequencerDict[uniqueName].addKey(\noiseOscAmpLFO2Top,  Pfunc({ noiseOscAmpLFO2Top }));
      sequencerDict[uniqueName].addKey(\noiseOscFilterLFOBottomRatio,  Pfunc({ noiseOscFilterLFOBottomRatio }));
      sequencerDict[uniqueName].addKey(\noiseOscFilterLFOTopRatio, Pfunc({  noiseOscFilterLFOTopRatio }));
      sequencerDict[uniqueName].addKey(\noiseOscFilterLFO2BottomRatio,  Pfunc({ noiseOscFilterLFO2BottomRatio }));
      sequencerDict[uniqueName].addKey(\noiseOscFilterLFO2TopRatio,  Pfunc({ noiseOscFilterLFO2TopRatio }));
      sequencerDict[uniqueName].addKey(\noiseOscAmp,  Pfunc({ noiseOscAmp }));
      sequencerDict[uniqueName].addKey(\noiseOscCutoff,  Pfunc({ noiseOscCutoff }));

      sequencerDict[uniqueName].addKey(\filterEnvAttackRatio, Pfunc({  filterEnvAttackRatio }));
      sequencerDict[uniqueName].addKey(\filterEnvPeakRatio,  Pfunc({ filterEnvPeakRatio }));
      sequencerDict[uniqueName].addKey(\filterEnvSustainRatio,  Pfunc({ filterEnvSustainRatio }));
      sequencerDict[uniqueName].addKey(\filterEnvReleaseRatio,  Pfunc({ filterEnvReleaseRatio }));
      sequencerDict[uniqueName].addKey(\filterEnvAttackTime,  Pfunc({ filterEnvAttackTime }));
      sequencerDict[uniqueName].addKey(\filterEnvDecayTime,  Pfunc({ filterEnvDecayTime }));
      sequencerDict[uniqueName].addKey(\filterEnvReleaseTime,  Pfunc({ filterEnvReleaseTime }));
      sequencerDict[uniqueName].addKey(\filterEnvLoop,  Pfunc({ filterEnvLoop }));
      sequencerDict[uniqueName].addKey(\filterCutoffLFOBottomRatio,  Pfunc({ filterCutoffLFOBottomRatio }));
      sequencerDict[uniqueName].addKey(\filterCutoffLFOTopRatio,  Pfunc({ filterCutoffLFOTopRatio }));
      sequencerDict[uniqueName].addKey(\filterCutoffLFO2BottomRatio, Pfunc({ filterCutoffLFO2BottomRatio }));
      sequencerDict[uniqueName].addKey(\filterCutoffLFO2TopRatio, Pfunc({ filterCutoffLFO2TopRatio }));
      sequencerDict[uniqueName].addKey(\filterResLFOBottom,  Pfunc({ filterResLFOBottom }));
      sequencerDict[uniqueName].addKey(\filterResLFOTop,  Pfunc({ filterResLFOTop }));
      sequencerDict[uniqueName].addKey(\filterResLFO2Bottom,  Pfunc({ filterResLFO2Bottom }));
      sequencerDict[uniqueName].addKey(\filterResLFO2Top,  Pfunc({ filterResLFO2Top }));
      sequencerDict[uniqueName].addKey(\drive, Pfunc({ filterDrive }));
      sequencerDict[uniqueName].addKey(\filterCutoff,  Pfunc({filterCutoff}));
      sequencerDict[uniqueName].addKey(\filterRes,  Pfunc({ filterRes }));
      sequencerDict[uniqueName].addKey(\filterType,  Pfunc({ filterType }));

      sequencerDict[uniqueName].addKey(\ampLFOBottom,  Pfunc({ ampLFOBottom }));
      sequencerDict[uniqueName].addKey(\ampLFOTop,  Pfunc({ ampLFOTop }));
      sequencerDict[uniqueName].addKey(\ampLFO2Bottom,  Pfunc({ ampLFO2Bottom }));
      sequencerDict[uniqueName].addKey(\ampLFO2Top,  Pfunc({ ampLFO2Top }));
      sequencerDict[uniqueName].addKey(\attackTime,  Pfunc({ attackTime }));
      sequencerDict[uniqueName].addKey(\decayTime,  Pfunc({ decayTime }));
      sequencerDict[uniqueName].addKey(\sustainLevel, Pfunc({  sustainLevel }));
      sequencerDict[uniqueName].addKey(\releaseTime,  Pfunc({ releaseTime }));

      sequencerDict[uniqueName].addKey( \pan,  Pfunc({ pan }));
      sequencerDict[uniqueName].addKey( \panLFOBottom,  Pfunc({ panLFOBottom }));
      sequencerDict[uniqueName].addKey( \panLFOTop,  Pfunc({ panLFOTop }));
      sequencerDict[uniqueName].addKey( \panLFO2Bottom,  Pfunc({ panLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \panLFO2Top,  Pfunc({ panLFO2Top }));
    }
  }

  addKey {  | uniqueName, key, action |
    sequencerDict[uniqueName].addKey(key, action);
  }

  playSequence { | uniqueName, clock = 'internal' |
    var playClock;
    if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
    sequencerDict[uniqueName].play(playClock);
  }

  resetSequence { | uniqueName | sequencerDict[uniqueName].reset; }
  stopSequence { | uniqueName | sequencerDict[uniqueName].stop; }
  pauseSequence { | uniqueName | sequencerDict[uniqueName].pause }
  resumeSequence { | uniqueName | sequencerDict[uniqueName].resume; }
  isSequencePlaying { | uniqueName | ^sequencerDict[uniqueName].isPlaying }

  setSequencerClockTempo { | bpm = 60 |
    var bps = bpm/60;
    tempo = bps;
    sequencerClock.tempo = tempo;
  }
}