Subtractive_Voice {

  var <synth;
  var <isReleasing, <watcher;
  var server;
  var parent;

  *new { | freq = 220, vol = -12, subtractive, group = nil, addAction = 'addToHead' |
    ^super.new.prInit(freq, vol, subtractive, group, addAction);
  }

  prInit { | freq = 220, vol = -12, subtractive, group, addAction |
    server = Server.default;
    parent = subtractive;
    {
    watcher = server.makeBundle(false, {
      synth = Synth(\prm_Subtractive_Voice, [
        \outBus, parent.mixer.chanStereo(0), \lfo2InBus, parent.lfoBus, \amp, vol.dbamp, \freq, freq, \gate, 1,

        \lfo1FreqLFO2BottomRatio, parent.lfo1FreqLFO2BottomRatio, \lfo1FreqLFO2TopRatio, parent.lfo1FreqLFO2TopRatio,
        \lfo1Waveform, parent.lfo1Waveform, \lfo1Freq, parent.lfo1Freq, \lfo1PulseWidth, parent.lfo1PulseWidth,
        \lfo1EnvType, parent.lfo1EnvType, \lfo1AttackTime, parent.lfo1AttackTime,
        \lfo1ReleaseTime, parent.lfo1ReleaseTime,

        \osc1OctaveMul, parent.osc1OctaveMul, \osc1FreqEnvStartRatio, parent.osc1FreqEnvStartRatio,
        \osc1FreqEnvEndRatio, parent.osc1FreqEnvEndRatio, \osc1FreqEnvTime, parent.osc1FreqEnvTime,
        \osc1FreqLFO1BottomRatio, parent.osc1FreqLFO1BottomRatio, \osc1FreqLFO1TopRatio, parent.osc1FreqLFO1TopRatio,
        \osc1FreqLFO2BottomRatio, parent.osc1FreqLFO2BottomRatio,
        \osc1FreqLFO2TopRatio, parent.osc1FreqLFO2TopRatio,
        \osc1PulseWidthLFO1Bottom, parent.osc1PulseWidthLFO1Bottom,
        \osc1PulseWidthLFO1Top, parent.osc1PulseWidthLFO1Top,
        \osc1PulseWidthLFO2Bottom, parent.osc1PulseWidthLFO2Bottom,
        \osc1PulseWidthLFO2Top, parent.osc1PulseWidthLFO2Top,
        \osc1AmpLFO1Bottom, parent.osc1AmpLFO1Bottom, \osc1AmpLFO1Top, parent.osc1AmpLFO1Top,
        \osc1AmpLFO2Bottom, parent.osc1AmpLFO2Bottom, \osc1AmpLFO2Top, parent.osc1AmpLFO2Top,
        \osc1WaveformLFO1Bottom, parent.osc1WaveformLFO1Bottom, \osc1WaveformLFO1Top, parent.osc1WaveformLFO1Top,
        \osc1WaveformLFO2Bottom, parent.osc1WaveformLFO2Bottom,
        \osc1WaveformLFO2Top, parent.osc1WaveformLFO2Top,
        \osc1Waveform, parent.osc1Waveform, \osc1PulseWidth, parent.osc1PulseWidth,
        \osc1Amp, parent.osc1Amp, \osc1SubAmp, parent.osc1SubAmp,

        \osc2OctaveMul, parent.osc2OctaveMul, \osc2DetuneCents, parent.osc2DetuneCents,
        \osc2FreqEnvStartRatio, parent.osc2FreqEnvStartRatio,
        \osc2FreqEnvEndRatio , parent.osc2FreqEnvEndRatio, \osc2FreqEnvTime, parent.osc2FreqEnvTime,
        \osc2FreqLFO1BottomRatio, parent.osc2FreqLFO1BottomRatio, \osc2FreqLFO1TopRatio, parent.osc2FreqLFO1TopRatio,
        \osc2FreqLFO2BottomRatio, parent.osc2FreqLFO2BottomRatio,
        \osc2FreqLFO2TopRatio, parent.osc2FreqLFO2TopRatio,
        \osc2PulseWidthLFO1Bottom, parent.osc2PulseWidthLFO1Bottom,
        \osc2PulseWidthLFO1Top, parent.osc2PulseWidthLFO1Top,
        \osc2PulseWidthLFO2Bottom, parent.osc2PulseWidthLFO2Bottom,
        \osc2PulseWidthLFO2Top, parent.osc2PulseWidthLFO2Top,
        \osc2AmpLFO1Bottom, parent.osc2AmpLFO1Bottom, \osc2AmpLFO1Top, parent.osc2AmpLFO1Top,
        \osc2AmpLFO2Bottom, parent.osc2AmpLFO2Bottom, \osc2AmpLFO2Top, parent.osc2AmpLFO2Top,
        \osc2WaveformLFO1Bottom, parent.osc2WaveformLFO1Bottom, \osc2WaveformLFO1Top, parent.osc2WaveformLFO1Top,
        \osc2WaveformLFO2Bottom, parent.osc2WaveformLFO2Bottom, \osc2WaveformLFO2Top, parent.osc2WaveformLFO2Top,
        \osc2Waveform, parent.osc2Waveform, \osc2PulseWidth, parent.osc2PulseWidth,
        \osc2Amp, parent.osc2Amp, \osc2SubAmp, parent.osc2SubAmp,

        \noiseOscAmpLFO1Bottom, parent.noiseOscAmpLFO1Bottom, \noiseOscAmpLFO1Top, parent.noiseOscAmpLFO1Top,
        \noiseOscAmpLFO2Bottom, parent.noiseOscAmpLFO2Bottom, \noiseOscAmpLFO2Top, parent.noiseOscAmpLFO2Top,
        \noiseOscFilterLFO1BottomRatio, parent.noiseOscFilterLFO1BottomRatio,
        \noiseOscFilterLFO1TopRatio, parent.noiseOscFilterLFO1TopRatio,
        \noiseOscFilterLFO2BottomRatio, parent.noiseOscFilterLFO2BottomRatio,
        \noiseOscFilterLFO2TopRatio, parent.noiseOscFilterLFO2TopRatio,
        \noiseOscAmp, parent.noiseOscAmp, \noiseOscCutoff, parent.noiseOscCutoff,

        \filterEnvAttackRatio, parent.filterEnvAttackRatio, \filterEnvPeakRatio, parent.filterEnvPeakRatio,
        \filterEnvSustainRatio, parent.filterEnvSustainRatio,
        \filterEnvReleaseRatio, parent.filterEnvReleaseRatio,
        \filterEnvAttackTime, parent.filterEnvAttackTime,
        \filterEnvDecayTime, parent.filterEnvDecayTime,
        \filterEnvReleaseTime, parent.filterEnvReleaseTime, \filterEnvLoop, parent.filterEnvLoop,
        \filterCutoffLFO1BottomRatio, parent.filterCutoffLFO1BottomRatio,
        \filterCutoffLFO1TopRatio, parent.filterCutoffLFO1TopRatio,
        \filterCutoffLFO2BottomRatio, parent.filterCutoffLFO2BottomRatio,
        \filterCutoffLFO2TopRatio, parent.filterCutoffLFO2TopRatio,
        \filterResLFO1Bottom, parent.filterResLFO1Bottom, \filterResLFO1Top, parent.filterResLFO1Top,
        \filterResLFO2Bottom, parent.filterResLFO2Bottom, \filterResLFO2Top, parent.filterResLFO2Top,
        \drive, parent.filterDrive, \filterCutoff, parent.filterCutoff, \filterRes, parent.filterRes, \filterType, parent.filterType,

        \ampLFO1Bottom, parent.ampLFO1Bottom, \ampLFO1Top, parent.ampLFO1Top,
        \ampLFO2Bottom, parent.ampLFO2Bottom,
        \ampLFO2Top, parent.ampLFO2Top, \attackTime, parent.attackTime, \decayTime, parent.decayTime,
        \sustainLevel, parent.sustainLevel, \releaseTime, parent.releaseTime,

        \pan, parent.pan, \panLFO1Bottom, parent.panLFO1Bottom, \panLFO1Top, parent.panLFO1Top,
        \panLFO2Bottom, parent.panLFO2Bottom, \panLFO2Top, parent.panLFO2Top
      ], group, addAction);

      NodeWatcher.register(synth);
    });
    server.listSendBundle(nil, watcher);
    server.sync;
    //isPlaying = true;
    isReleasing = false;
    }.fork;


  }

//////// public functions:

  free {
    //synth.free;
    //synth = nil;
  }

  release {
    synth.set(\gate, 0);
    SystemClock.sched(parent.releaseTime, {
      {
        server.sync;
        synth.isPlaying.postln;
      }.fork;
    });
    // basic: (not working)
    /*
    synth.set(\gate, 0);
    {
      parent.releaseTime.wait;
      if( isReleasing == true, {
        isReleasing = false;
        this.free;
        if( parent.numVoices == 0, { parent.synthGroup.freeAll });
      });
    }.fork;
    */
    /*
    synth.set(\gate, 0);
    isReleasing = true;
    SystemClock.sched( parent.releaseTime, {
      isReleasing = false;
      8.do({ if( synth.isPlaying, { synth.free; }); });
    });
    */
  }

  steal { | freq = 220 |
    {
      synth.set(\gate, -1.05);
      server.sync;
      synth.set(\gate, 1);
      synth.set(\freq, freq);
      //this.setAllParameters;
      isReleasing = false;
      //isPlaying = true;
    }.fork;
  }

  reset {
    {
      synth.set(\gate, 0);
      server.sync;
      synth.set(\gate, 1);
    }.fork;
  }

  /*
  setAllParameters {
    synth.set(\amp, amp,
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
      \panLFO2Bottom, panLFO2Bottom, \panLFO2Top, panLFO2Top);
  }
  */

}