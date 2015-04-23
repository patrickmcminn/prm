//////// Pattern Sequencer:
+ Subtractive {

  makeSequence { | uniqueName |
    fork {
      sequencerDict[uniqueName] = PatternSequencer.new(uniqueName,  synthGroup, \addToTail);
      server.sync;
      //1.wait;
      sequencerDict[uniqueName].addKey(\instrument, \prm_Subtractive_Voice_Seq);
      sequencerDict[uniqueName].addKey(\outBus,  mixer.chanStereo(0));
      sequencerDict[uniqueName].addKey(\lfo2InBus,  Pfunc({lfoBus}) );
      sequencerDict[uniqueName].addKey(\lfo1FreqLFO2BottomRatio, Pfunc({ lfo1FreqLFO2BottomRatio }));
      sequencerDict[uniqueName].addKey(  \lfo1FreqLFO2TopRatio,  Pfunc({ lfo1FreqLFO2TopRatio }));
      sequencerDict[uniqueName].addKey( \lfo1Waveform,  Pfunc({ lfo1Waveform }));
      sequencerDict[uniqueName].addKey(  \lfo1Freq,  Pfunc({ lfo1Freq }));
      sequencerDict[uniqueName].addKey( \lfo1PulseWidth,  Pfunc({ lfo1PulseWidth }));
      sequencerDict[uniqueName].addKey( \lfo1EnvType,  Pfunc({ lfo1EnvType }));
      sequencerDict[uniqueName].addKey( \lfo1AttackTime,  Pfunc({ lfo1AttackTime }));
      sequencerDict[uniqueName].addKey( \lfo1ReleaseTime,  Pfunc({ lfo1ReleaseTime }));

      sequencerDict[uniqueName].addKey( \osc1OctaveMul,  Pfunc({ osc1OctaveMul }));
      sequencerDict[uniqueName].addKey( \osc1FreqEnvStartRatio,  Pfunc({ osc1FreqEnvStartRatio }));
      sequencerDict[uniqueName].addKey( \osc1FreqEnvEndRatio,  Pfunc({ osc1FreqEnvEndRatio }));
      sequencerDict[uniqueName].addKey( \osc1FreqEnvTime,  Pfunc({ osc1FreqEnvTime }));
      sequencerDict[uniqueName].addKey( \osc1FreqLFO1BottomRatio,  Pfunc({ osc1FreqLFO1BottomRatio }));
      sequencerDict[uniqueName].addKey( \osc1FreqLFO1TopRatio,  Pfunc({ osc1FreqLFO1TopRatio }));
      sequencerDict[uniqueName].addKey( \osc1FreqLFO2BottomRatio,  Pfunc({ osc1FreqLFO2BottomRatio }));
      sequencerDict[uniqueName].addKey( \osc1FreqLFO2TopRatio,  Pfunc({ osc1FreqLFO2TopRatio }));
      sequencerDict[uniqueName].addKey( \osc1PulseWidthLFO1Bottom,  Pfunc({ osc1PulseWidthLFO1Bottom }));
      sequencerDict[uniqueName].addKey( \osc1PulseWidthLFO1Top,  Pfunc({ osc1PulseWidthLFO1Top }));
      sequencerDict[uniqueName].addKey( \osc1PulseWidthLFO2Bottom,  Pfunc({ osc1PulseWidthLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \osc1PulseWidthLFO2Top,  Pfunc({ osc1PulseWidthLFO2Top }));
      sequencerDict[uniqueName].addKey( \osc1AmpLFO1Bottom,  Pfunc({ osc1AmpLFO1Bottom }));
      sequencerDict[uniqueName].addKey( \osc1AmpLFO1Top,  Pfunc({ osc1AmpLFO1Top }));
      sequencerDict[uniqueName].addKey( \osc1AmpLFO2Bottom,  Pfunc({ osc1AmpLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \osc1AmpLFO2Top,  Pfunc({ osc1AmpLFO2Top }));
      sequencerDict[uniqueName].addKey( \osc1WaveformLFO1Bottom,  Pfunc({ osc1WaveformLFO1Bottom }));
      sequencerDict[uniqueName].addKey( \osc1WaveformLFO1Top,  Pfunc({ osc1WaveformLFO1Top }));
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
      sequencerDict[uniqueName].addKey( \osc2FreqLFO1BottomRatio,  Pfunc({ osc2FreqLFO1BottomRatio }));
      sequencerDict[uniqueName].addKey( \osc2FreqLFO1TopRatio,  Pfunc({ osc2FreqLFO1TopRatio }));
      sequencerDict[uniqueName].addKey( \osc2FreqLFO2BottomRatio,  Pfunc({ osc2FreqLFO2BottomRatio }));
      sequencerDict[uniqueName].addKey( \osc2FreqLFO2TopRatio,  Pfunc({ osc2FreqLFO2TopRatio }));
      sequencerDict[uniqueName].addKey( \osc2PulseWidthLFO1Bottom,  Pfunc({ osc2PulseWidthLFO1Bottom }));
      sequencerDict[uniqueName].addKey( \osc2PulseWidthLFO1Top,  Pfunc({ osc2PulseWidthLFO1Top }));
      sequencerDict[uniqueName].addKey( \osc2PulseWidthLFO2Bottom,  Pfunc({ osc2PulseWidthLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \osc2PulseWidthLFO2Top,  Pfunc({ osc2PulseWidthLFO2Top }));
      sequencerDict[uniqueName].addKey( \osc2AmpLFO1Bottom,  Pfunc({ osc2AmpLFO1Bottom }));
      sequencerDict[uniqueName].addKey( \osc2AmpLFO1Top,  Pfunc({ osc2AmpLFO1Top }));
      sequencerDict[uniqueName].addKey( \osc2AmpLFO2Bottom,  Pfunc({ osc2AmpLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \osc2AmpLFO2Top,  Pfunc({ osc2AmpLFO2Top }));
      sequencerDict[uniqueName].addKey( \osc2WaveformLFO1Bottom,  Pfunc({ osc2WaveformLFO1Bottom }));
      sequencerDict[uniqueName].addKey( \osc2WaveformLFO1Top,  Pfunc({ osc2WaveformLFO1Top }));
      sequencerDict[uniqueName].addKey( \osc2WaveformLFO2Bottom,  Pfunc({ osc2WaveformLFO2Bottom }));
      sequencerDict[uniqueName].addKey( \osc2WaveformLFO2Top,  Pfunc({ osc2WaveformLFO2Top }));
      sequencerDict[uniqueName].addKey( \osc2Waveform,  Pfunc({ osc2Waveform }));
      sequencerDict[uniqueName].addKey(\osc2PulseWidth,  Pfunc({ osc2PulseWidth }));
      sequencerDict[uniqueName].addKey(\osc2Amp,  Pfunc({ osc2Amp }));
      sequencerDict[uniqueName].addKey(\osc2SubAmp,  Pfunc({ osc2SubAmp }));

      sequencerDict[uniqueName].addKey(\noiseOscAmpLFO1Bottom,  Pfunc({ noiseOscAmpLFO1Bottom }));
      sequencerDict[uniqueName].addKey(\noiseOscAmpLFO1Top, Pfunc({  noiseOscAmpLFO1Top }));
      sequencerDict[uniqueName].addKey(\noiseOscAmpLFO2Bottom,  Pfunc({ noiseOscAmpLFO2Bottom }));
      sequencerDict[uniqueName].addKey(\noiseOscAmpLFO2Top,  Pfunc({ noiseOscAmpLFO2Top }));
      sequencerDict[uniqueName].addKey(\noiseOscFilterLFO1BottomRatio,  Pfunc({ noiseOscFilterLFO1BottomRatio }));
      sequencerDict[uniqueName].addKey(\noiseOscFilterLFO1TopRatio, Pfunc({  noiseOscFilterLFO1TopRatio }));
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
      sequencerDict[uniqueName].addKey(\filterCutoffLFO1BottomRatio,  Pfunc({ filterCutoffLFO1BottomRatio }));
      sequencerDict[uniqueName].addKey(\filterCutoffLFO1TopRatio,  Pfunc({ filterCutoffLFO1TopRatio }));
      sequencerDict[uniqueName].addKey(\filterCutoffLFO2BottomRatio, Pfunc({ filterCutoffLFO2BottomRatio }));
      sequencerDict[uniqueName].addKey(\filterCutoffLFO2TopRatio, Pfunc({ filterCutoffLFO2TopRatio }));
      sequencerDict[uniqueName].addKey(\filterResLFO1Bottom,  Pfunc({ filterResLFO1Bottom }));
      sequencerDict[uniqueName].addKey(\filterResLFO1Top,  Pfunc({ filterResLFO1Top }));
      sequencerDict[uniqueName].addKey(\filterResLFO2Bottom,  Pfunc({ filterResLFO2Bottom }));
      sequencerDict[uniqueName].addKey(\filterResLFO2Top,  Pfunc({ filterResLFO2Top }));
      sequencerDict[uniqueName].addKey(\drive, Pfunc({ filterDrive }));
      sequencerDict[uniqueName].addKey(\filterCutoff,  Pfunc({filterCutoff}));
      sequencerDict[uniqueName].addKey(\filterRes,  Pfunc({ filterRes }));
      sequencerDict[uniqueName].addKey(\filterType,  Pfunc({ filterType }));

      sequencerDict[uniqueName].addKey(\ampLFO1Bottom,  Pfunc({ ampLFO1Bottom }));
      sequencerDict[uniqueName].addKey(\ampLFO1Top,  Pfunc({ ampLFO1Top }));
      sequencerDict[uniqueName].addKey(\ampLFO2Bottom,  Pfunc({ ampLFO2Bottom }));
      sequencerDict[uniqueName].addKey(\ampLFO2Top,  Pfunc({ ampLFO2Top }));
      sequencerDict[uniqueName].addKey(\attackTime,  Pfunc({ attackTime }));
      sequencerDict[uniqueName].addKey(\decayTime,  Pfunc({ decayTime }));
      sequencerDict[uniqueName].addKey(\sustainLevel, Pfunc({  sustainLevel }));
      sequencerDict[uniqueName].addKey(\releaseTime,  Pfunc({ releaseTime }));

      sequencerDict[uniqueName].addKey( \pan,  Pfunc({ pan }));
      sequencerDict[uniqueName].addKey( \panLFO1Bottom,  Pfunc({ panLFO1Bottom }));
      sequencerDict[uniqueName].addKey( \panLFO1Top,  Pfunc({ panLFO1Top }));
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
  setSequenceQuant { | uniqueName, quant = 0 | sequencerDict[uniqueName].setQuant(quant) }

  setSequencerClockTempo { | bpm = 60 |
    var bps = bpm/60;
    tempo = bps;
    sequencerClock.tempo = tempo;
  }
}