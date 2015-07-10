//////// Pattern Sequencer:
+ Subtractive {

  makeSequence { | name |
    fork {
      sequencerDict[name] = IM_PatternSeq.new(name,  synthGroup, \addToTail);
      sequencerDict[name].stop;
      server.sync;
      //1.wait;
      sequencerDict[name].addKey(\instrument, \prm_Subtractive_Voice_Seq);
      sequencerDict[name].addKey(\outBus,  mixer.chanStereo(0));
      sequencerDict[name].addKey(\lfo2InBus,  Pfunc({lfoBus}) );
      sequencerDict[name].addKey(\lfo1FreqLFO2BottomRatio, Pfunc({ lfo1FreqLFO2BottomRatio }));
      sequencerDict[name].addKey(  \lfo1FreqLFO2TopRatio,  Pfunc({ lfo1FreqLFO2TopRatio }));
      sequencerDict[name].addKey( \lfo1Waveform,  Pfunc({ lfo1Waveform }));
      sequencerDict[name].addKey(  \lfo1Freq,  Pfunc({ lfo1Freq }));
      sequencerDict[name].addKey( \lfo1PulseWidth,  Pfunc({ lfo1PulseWidth }));
      sequencerDict[name].addKey( \lfo1EnvType,  Pfunc({ lfo1EnvType }));
      sequencerDict[name].addKey( \lfo1AttackTime,  Pfunc({ lfo1AttackTime }));
      sequencerDict[name].addKey( \lfo1ReleaseTime,  Pfunc({ lfo1ReleaseTime }));

      sequencerDict[name].addKey( \osc1OctaveMul,  Pfunc({ osc1OctaveMul }));
      sequencerDict[name].addKey( \osc1FreqEnvStartRatio,  Pfunc({ osc1FreqEnvStartRatio }));
      sequencerDict[name].addKey( \osc1FreqEnvEndRatio,  Pfunc({ osc1FreqEnvEndRatio }));
      sequencerDict[name].addKey( \osc1FreqEnvTime,  Pfunc({ osc1FreqEnvTime }));
      sequencerDict[name].addKey( \osc1FreqLFO1BottomRatio,  Pfunc({ osc1FreqLFO1BottomRatio }));
      sequencerDict[name].addKey( \osc1FreqLFO1TopRatio,  Pfunc({ osc1FreqLFO1TopRatio }));
      sequencerDict[name].addKey( \osc1FreqLFO2BottomRatio,  Pfunc({ osc1FreqLFO2BottomRatio }));
      sequencerDict[name].addKey( \osc1FreqLFO2TopRatio,  Pfunc({ osc1FreqLFO2TopRatio }));
      sequencerDict[name].addKey( \osc1PulseWidthLFO1Bottom,  Pfunc({ osc1PulseWidthLFO1Bottom }));
      sequencerDict[name].addKey( \osc1PulseWidthLFO1Top,  Pfunc({ osc1PulseWidthLFO1Top }));
      sequencerDict[name].addKey( \osc1PulseWidthLFO2Bottom,  Pfunc({ osc1PulseWidthLFO2Bottom }));
      sequencerDict[name].addKey( \osc1PulseWidthLFO2Top,  Pfunc({ osc1PulseWidthLFO2Top }));
      sequencerDict[name].addKey( \osc1AmpLFO1Bottom,  Pfunc({ osc1AmpLFO1Bottom }));
      sequencerDict[name].addKey( \osc1AmpLFO1Top,  Pfunc({ osc1AmpLFO1Top }));
      sequencerDict[name].addKey( \osc1AmpLFO2Bottom,  Pfunc({ osc1AmpLFO2Bottom }));
      sequencerDict[name].addKey( \osc1AmpLFO2Top,  Pfunc({ osc1AmpLFO2Top }));
      sequencerDict[name].addKey( \osc1WaveformLFO1Bottom,  Pfunc({ osc1WaveformLFO1Bottom }));
      sequencerDict[name].addKey( \osc1WaveformLFO1Top,  Pfunc({ osc1WaveformLFO1Top }));
      sequencerDict[name].addKey( \osc1WaveformLFO2Bottom,  Pfunc({ osc1WaveformLFO2Bottom }));
      sequencerDict[name].addKey( \osc1WaveformLFO2Top,  Pfunc({ osc1WaveformLFO2Top }));
      sequencerDict[name].addKey( \osc1Waveform,  Pfunc({ osc1Waveform }));
      sequencerDict[name].addKey( \osc1PulseWidth,  Pfunc({ osc1PulseWidth }));
      sequencerDict[name].addKey( \osc1Amp,  Pfunc({ osc1Amp }));

      sequencerDict[name].addKey( \osc1SubAmp,  Pfunc({ osc1SubAmp }));
      sequencerDict[name].addKey( \osc2OctaveMul,  Pfunc({ osc2OctaveMul }));
      sequencerDict[name].addKey( \osc2DetuneCents,  Pfunc({ osc2DetuneCents }));
      sequencerDict[name].addKey( \osc2FreqEnvStartRatio,  Pfunc({ osc2FreqEnvStartRatio }));
      sequencerDict[name].addKey( \osc2FreqEnvEndRatio ,  Pfunc({ osc2FreqEnvEndRatio }));
      sequencerDict[name].addKey( \osc2FreqEnvTime,  Pfunc({ osc2FreqEnvTime }));
      sequencerDict[name].addKey( \osc2FreqLFO1BottomRatio,  Pfunc({ osc2FreqLFO1BottomRatio }));
      sequencerDict[name].addKey( \osc2FreqLFO1TopRatio,  Pfunc({ osc2FreqLFO1TopRatio }));
      sequencerDict[name].addKey( \osc2FreqLFO2BottomRatio,  Pfunc({ osc2FreqLFO2BottomRatio }));
      sequencerDict[name].addKey( \osc2FreqLFO2TopRatio,  Pfunc({ osc2FreqLFO2TopRatio }));
      sequencerDict[name].addKey( \osc2PulseWidthLFO1Bottom,  Pfunc({ osc2PulseWidthLFO1Bottom }));
      sequencerDict[name].addKey( \osc2PulseWidthLFO1Top,  Pfunc({ osc2PulseWidthLFO1Top }));
      sequencerDict[name].addKey( \osc2PulseWidthLFO2Bottom,  Pfunc({ osc2PulseWidthLFO2Bottom }));
      sequencerDict[name].addKey( \osc2PulseWidthLFO2Top,  Pfunc({ osc2PulseWidthLFO2Top }));
      sequencerDict[name].addKey( \osc2AmpLFO1Bottom,  Pfunc({ osc2AmpLFO1Bottom }));
      sequencerDict[name].addKey( \osc2AmpLFO1Top,  Pfunc({ osc2AmpLFO1Top }));
      sequencerDict[name].addKey( \osc2AmpLFO2Bottom,  Pfunc({ osc2AmpLFO2Bottom }));
      sequencerDict[name].addKey( \osc2AmpLFO2Top,  Pfunc({ osc2AmpLFO2Top }));
      sequencerDict[name].addKey( \osc2WaveformLFO1Bottom,  Pfunc({ osc2WaveformLFO1Bottom }));
      sequencerDict[name].addKey( \osc2WaveformLFO1Top,  Pfunc({ osc2WaveformLFO1Top }));
      sequencerDict[name].addKey( \osc2WaveformLFO2Bottom,  Pfunc({ osc2WaveformLFO2Bottom }));
      sequencerDict[name].addKey( \osc2WaveformLFO2Top,  Pfunc({ osc2WaveformLFO2Top }));
      sequencerDict[name].addKey( \osc2Waveform,  Pfunc({ osc2Waveform }));
      sequencerDict[name].addKey(\osc2PulseWidth,  Pfunc({ osc2PulseWidth }));
      sequencerDict[name].addKey(\osc2Amp,  Pfunc({ osc2Amp }));
      sequencerDict[name].addKey(\osc2SubAmp,  Pfunc({ osc2SubAmp }));

      sequencerDict[name].addKey(\noiseOscAmpLFO1Bottom,  Pfunc({ noiseOscAmpLFO1Bottom }));
      sequencerDict[name].addKey(\noiseOscAmpLFO1Top, Pfunc({  noiseOscAmpLFO1Top }));
      sequencerDict[name].addKey(\noiseOscAmpLFO2Bottom,  Pfunc({ noiseOscAmpLFO2Bottom }));
      sequencerDict[name].addKey(\noiseOscAmpLFO2Top,  Pfunc({ noiseOscAmpLFO2Top }));
      sequencerDict[name].addKey(\noiseOscFilterLFO1BottomRatio,  Pfunc({ noiseOscFilterLFO1BottomRatio }));
      sequencerDict[name].addKey(\noiseOscFilterLFO1TopRatio, Pfunc({  noiseOscFilterLFO1TopRatio }));
      sequencerDict[name].addKey(\noiseOscFilterLFO2BottomRatio,  Pfunc({ noiseOscFilterLFO2BottomRatio }));
      sequencerDict[name].addKey(\noiseOscFilterLFO2TopRatio,  Pfunc({ noiseOscFilterLFO2TopRatio }));
      sequencerDict[name].addKey(\noiseOscAmp,  Pfunc({ noiseOscAmp }));
      sequencerDict[name].addKey(\noiseOscCutoff,  Pfunc({ noiseOscCutoff }));

      sequencerDict[name].addKey(\filterEnvAttackRatio, Pfunc({  filterEnvAttackRatio }));
      sequencerDict[name].addKey(\filterEnvPeakRatio,  Pfunc({ filterEnvPeakRatio }));
      sequencerDict[name].addKey(\filterEnvSustainRatio,  Pfunc({ filterEnvSustainRatio }));
      sequencerDict[name].addKey(\filterEnvReleaseRatio,  Pfunc({ filterEnvReleaseRatio }));
      sequencerDict[name].addKey(\filterEnvAttackTime,  Pfunc({ filterEnvAttackTime }));
      sequencerDict[name].addKey(\filterEnvDecayTime,  Pfunc({ filterEnvDecayTime }));
      sequencerDict[name].addKey(\filterEnvReleaseTime,  Pfunc({ filterEnvReleaseTime }));
      sequencerDict[name].addKey(\filterEnvLoop,  Pfunc({ filterEnvLoop }));
      sequencerDict[name].addKey(\filterCutoffLFO1BottomRatio,  Pfunc({ filterCutoffLFO1BottomRatio }));
      sequencerDict[name].addKey(\filterCutoffLFO1TopRatio,  Pfunc({ filterCutoffLFO1TopRatio }));
      sequencerDict[name].addKey(\filterCutoffLFO2BottomRatio, Pfunc({ filterCutoffLFO2BottomRatio }));
      sequencerDict[name].addKey(\filterCutoffLFO2TopRatio, Pfunc({ filterCutoffLFO2TopRatio }));
      sequencerDict[name].addKey(\filterResLFO1Bottom,  Pfunc({ filterResLFO1Bottom }));
      sequencerDict[name].addKey(\filterResLFO1Top,  Pfunc({ filterResLFO1Top }));
      sequencerDict[name].addKey(\filterResLFO2Bottom,  Pfunc({ filterResLFO2Bottom }));
      sequencerDict[name].addKey(\filterResLFO2Top,  Pfunc({ filterResLFO2Top }));
      sequencerDict[name].addKey(\drive, Pfunc({ filterDrive }));
      sequencerDict[name].addKey(\filterCutoff,  Pfunc({filterCutoff}));
      sequencerDict[name].addKey(\filterRes,  Pfunc({ filterRes }));
      sequencerDict[name].addKey(\filterType,  Pfunc({ filterType }));

      sequencerDict[name].addKey(\ampLFO1Bottom,  Pfunc({ ampLFO1Bottom }));
      sequencerDict[name].addKey(\ampLFO1Top,  Pfunc({ ampLFO1Top }));
      sequencerDict[name].addKey(\ampLFO2Bottom,  Pfunc({ ampLFO2Bottom }));
      sequencerDict[name].addKey(\ampLFO2Top,  Pfunc({ ampLFO2Top }));
      sequencerDict[name].addKey(\attackTime,  Pfunc({ attackTime }));
      sequencerDict[name].addKey(\decayTime,  Pfunc({ decayTime }));
      sequencerDict[name].addKey(\sustainLevel, Pfunc({  sustainLevel }));
      sequencerDict[name].addKey(\releaseTime,  Pfunc({ releaseTime }));

      sequencerDict[name].addKey( \pan,  Pfunc({ pan }));
      sequencerDict[name].addKey( \panLFO1Bottom,  Pfunc({ panLFO1Bottom }));
      sequencerDict[name].addKey( \panLFO1Top,  Pfunc({ panLFO1Top }));
      sequencerDict[name].addKey( \panLFO2Bottom,  Pfunc({ panLFO2Bottom }));
      sequencerDict[name].addKey( \panLFO2Top,  Pfunc({ panLFO2Top }));
    }
  }

  addKey {  | name, key, action |
    sequencerDict[name].addKey(key, action);
  }

  playSequence { | name, clock = 'internal', quant = 'nil' |
    var playClock;
    if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
    sequencerDict[name].play(playClock);
  }

  resetSequence { | name | sequencerDict[name].reset; }
  stopSequence { | name | sequencerDict[name].stop; }
  pauseSequence { | name | sequencerDict[name].pause }
  resumeSequence { | name | sequencerDict[name].resume; }
  isSequencePlaying { | name | ^sequencerDict[name].isPlaying }
  setSequenceQuant { | name, quant = 0 | sequencerDict[name].setQuant(quant) }

  setSequencerClockTempo { | bpm = 60 |
    var bps = bpm/60;
    tempo = bps;
    sequencerClock.tempo = tempo;
  }
}