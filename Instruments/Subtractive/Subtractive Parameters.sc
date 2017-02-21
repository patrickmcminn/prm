+ Subtractive {

  prInitializeParameters {

    applyMode = true;

    maxVoices = 8; numVoices = 0; orderNum = 0; amp = 0.6;

    lfo1FreqLFO2BottomRatio = 1.0; lfo1FreqLFO2TopRatio = 1.0; lfo1Waveform = 0;
    lfo1Freq = 1; lfo1PulseWidth = 0.5; lfo1EnvType = 0; lfo1AttackTime = 0.05; lfo1ReleaseTime = 0.05;

    osc1OctaveMul = 1; osc1FreqEnvStartRatio = 1.0; osc1FreqEnvEndRatio = 1.0; osc1FreqEnvTime = 0;
    osc1FreqLFO1BottomRatio = 1.0; osc1FreqLFO1TopRatio = 1.0; osc1FreqLFO2BottomRatio = 1.0;
    osc1FreqLFO2TopRatio = 1.0; osc1PulseWidthLFO1Bottom = 0; osc1PulseWidthLFO1Top = 0;
    osc1PulseWidthLFO2Bottom = 0; osc1PulseWidthLFO2Top = 0; osc1AmpLFO1Bottom = 1; osc1AmpLFO1Top = 1;
    osc1AmpLFO2Bottom = 1; osc1AmpLFO2Top = 1; osc1WaveformLFO1Bottom = 0; osc1WaveformLFO1Top = 0;
    osc1WaveformLFO2Bottom = 0; osc1WaveformLFO2Top = 0; osc1Waveform = 2; osc1PulseWidth = 0.5;
    osc1Amp = 0.5; osc1SubAmp = 0;

    osc2OctaveMul = 0.5; osc2DetuneCents = 0;
    osc2FreqEnvStartRatio = 1.0; osc2FreqEnvEndRatio = 1.0; osc2FreqEnvTime = 0;
    osc2FreqLFO1BottomRatio = 1.0; osc2FreqLFO1TopRatio = 1.0; osc2FreqLFO2BottomRatio = 1.0; osc2FreqLFO2TopRatio = 1.0;
    osc2PulseWidthLFO1Bottom = 0; osc2PulseWidthLFO1Top = 0; osc2PulseWidthLFO2Bottom = 0; osc2PulseWidthLFO2Top = 0;
    osc2AmpLFO1Bottom = 1; osc2AmpLFO1Top = 1; osc2AmpLFO2Bottom = 1; osc2AmpLFO2Top = 1;
    osc2WaveformLFO1Bottom = 0; osc2WaveformLFO1Top = 0; osc2WaveformLFO2Bottom = 0; osc2WaveformLFO2Top = 0;
    osc2Waveform = 3; osc2PulseWidth = 0.5; osc2Amp = 0.25; osc2SubAmp = 0;

    noiseOscAmpLFO1Bottom = 1; noiseOscAmpLFO1Top = 1; noiseOscAmpLFO2Bottom = 1; noiseOscAmpLFO2Top = 1;
    noiseOscFilterLFO1BottomRatio = 1; noiseOscFilterLFO1TopRatio = 1; noiseOscFilterLFO2BottomRatio = 1;
    noiseOscFilterLFO2TopRatio = 1;
    noiseOscAmp = 0; noiseOscCutoff = 10000;

    filterEnvAttackRatio = 1.0; filterEnvPeakRatio = 1.0; filterEnvSustainRatio = 1.0; filterEnvReleaseRatio = 1.0;
    filterEnvAttackTime = 0.05; filterEnvDecayTime = 0; filterEnvReleaseTime = 0.05;
    filterEnvLoop = 0;
    filterCutoffLFO1BottomRatio = 1.0; filterCutoffLFO1TopRatio = 1.0; filterCutoffLFO2BottomRatio = 1.0;
    filterCutoffLFO2TopRatio = 1.0; filterResLFO1Bottom = 0.0; filterResLFO1Top = 0.0;
    filterResLFO2Bottom = 0.0; filterResLFO2Top = 0.0;
    filterDrive = 1.0; filterCutoff = 2000; filterRes = 0.0; filterType = 0;

    ampLFO1Bottom = 1.0; ampLFO1Top = 1.0; ampLFO2Bottom = 1.0; ampLFO2Top = 1.0;
    attackTime = 0.05; decayTime = 0.05; sustainLevel = 1; releaseTime = 0.05;

    pan = 0;
    panLFO1Bottom = 0.0; panLFO1Top = 0.0; panLFO2Bottom = 0.0; panLFO2Top = 0.0;

    lfo2Freq = 1; lfo2PulseWidth = 0.5; lfo2Waveform = 0;

    tempo = 1; beats = 0;

  }

    //// Filter:
  setFilterCutoff { | cutoff = 2000 |
    filterCutoff = cutoff;
    if( applyMode == true, {  synthGroup.set(\filterCutoff, cutoff); });
  }

  setFilterRes { | res = 0.0 |
    filterRes = res;
    if( applyMode == true, {
      synthGroup.set(\filterRes, res); });
  }

  setFilterDrive { | drive = 1.0 |
    filterDrive = drive;
    if( applyMode == true, {
      synthGroup.set(\drive, filterDrive); });
  }

  setFilterType { | type = 'lowPass' |
    if( type.isInteger, { filterType = type },
      {
        switch(type,
          { 'lowPass' }, { filterType = 0 },
          { 'highPass' }, { filterType = 1 },
          { 'bandPass' }, { filterType = 2 },
        );
    });
    if( applyMode == true, {
      synthGroup.set(\filterType, filterType); });
  }

  // filter Envelope:
  setFilterEnvAttackTime { | attack = 0.05 |
    filterEnvAttackTime = attack;
    if( applyMode == true, {
      synthGroup.set(\filterEnvAttackTime, filterEnvAttackTime); });
  }
  setFilterEnvDecayTime { | decay = 0 |
    filterEnvDecayTime = decay;
    if( applyMode == true, {
      synthGroup.set(\filterEnvDecayTime, filterEnvDecayTime); });
  }
  setFilterEnvReleaseTime { | release = 0.05 |
    filterEnvReleaseTime = release;
    if( applyMode == true, {
      synthGroup.set(\filterEnvReleaseTime, filterEnvReleaseTime); });
  }
  setFilterEnvAttackRatio { | ratio = 1.0 |
    filterEnvAttackRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\filterEnvAttackRatio, filterEnvAttackRatio); });
  }
  setFilterEnvPeakRatio { | ratio = 1.0 |
    filterEnvPeakRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\filterEnvPeakRatio, filterEnvPeakRatio); });
  }
  setFilterEnvSustainRatio { | ratio = 1.0 |
    filterEnvSustainRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\filterEnvSustainRatio, filterEnvSustainRatio); });
  }
  setFilterEnvReleaseRatio { | ratio = 1.0 |
    filterEnvReleaseRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\filterEnvReleaseRatio, filterEnvReleaseRatio); });
  }
  setFilterEnvLoop { | loop = false |
    if( loop, { filterEnvLoop = 1; },{ filterEnvLoop = 0 });
    if( loop.isInteger, { filterEnvLoop = loop; });
    if( applyMode == true, {
      synthGroup.set(\filterEnvLoop, filterEnvLoop); });
  }

  // Filter LFO:
  setFilterCutoffLFO1BottomRatio { | ratio = 1.0 |
    filterCutoffLFO1BottomRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\filterCutoffLFO1BottomRatio, filterCutoffLFO1BottomRatio); });
  }
  setFilterCutoffLFO1TopRatio { | ratio = 1.0 |
    filterCutoffLFO1TopRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\filterCutoffLFO1TopRatio, filterCutoffLFO1TopRatio); });
  }
  setFilterResLFO1Bottom { | bottom = 0.0 |
    filterResLFO1Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\filterResLFO1Bottom, filterResLFO1Bottom); });
  }
  setFilterResLFO1Top { | top = 0.0 |
    filterResLFO1Top = top;
    if( applyMode == true, {
      synthGroup.set(\filterResLFO1Top, filterResLFO1Top); });
  }

  // LFO2:
  setFilterCutoffLFO2BottomRatio { | ratio = 1.0 |
    filterCutoffLFO2BottomRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\filterCutoffLFO2BottomRatio, filterCutoffLFO2BottomRatio); });
  }
  setFilterCutoffLFO2TopRatio { | ratio = 1.0 |
    filterCutoffLFO2TopRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\filterCutoffLFO2TopRatio, filterCutoffLFO2TopRatio); });
  }
  setFilterResLFO2Bottom { | bottom = 0.0 |
    filterResLFO2Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\filterResLFO2Bottom, filterResLFO2Bottom); });
  }
  setFilterResLFO2Top { | top = 0.0 |
    filterResLFO2Top = top;
    if( applyMode == true, {
      synthGroup.set(\filterResLFO2Top, filterResLFO2Top); });
  }


  //// Amplitude Envelope:
  setAttackTime { | attack = 0.05 |
    attackTime = attack;
    if( applyMode == true, {
      synthGroup.set(\attackTime, attackTime); });
  }

  setDecayTime { | decay = 0 |
    decayTime = decay;
    if( applyMode == true, {
      synthGroup.set(\decayTime, decayTime); });
  }

  setSustainLevel { | sustain = 1.0 |
    sustainLevel = sustain;
    if( applyMode == true, {
      synthGroup.set(\sustainLevel, sustainLevel); });
  }

  setReleaseTime { | release = 0.05 |
    releaseTime = release;
    if( applyMode == true, {
      synthGroup.set(\releaseTime, releaseTime); });
  }

  setVoiceAmp { | voiceAmp = 0.2 |
    amp = voiceAmp;
    if( applyMode == true, {
      synthGroup.set(\amp, amp); });
  }

  setPan { | panning = 0 |
    pan = panning;
    if( applyMode == true, {
      synthGroup.set(\pan, pan); });
  }

  // Amplitude LFO 1:
  setAmplitudeLFO1Bottom { | bottom = 1.0 |
    ampLFO1Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\ampLFO1Bottom, ampLFO1Bottom); });
  }

  setAmplitudeLFO1Top { | top = 1.0 |
    ampLFO1Top = top;
    if( applyMode == true, {
      synthGroup.set(\ampLFO1Top, ampLFO1Top); });
  }

  setPanLFO1Bottom { | bottom = 0 |
    panLFO1Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\panLFO1Bottom, panLFO1Bottom); });
  }

  setPanLFO1Top { | top = 0 |
    panLFO1Top = top;
    if( applyMode == true, {
      synthGroup.set(\panLFO1Top, panLFO1Top); });
  }

  // Amplitude LFO 2:
  setAmplitudeLFO2Bottom { | bottom = 1.0 |
    ampLFO2Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\ampLFO2Bottom, ampLFO2Bottom); });
  }

  setAmplitudeLFO2Top { | top = 1.0 |
    ampLFO2Top = top;
    if( applyMode == true, {
      synthGroup.set(\ampLFO2Top, ampLFO2Top); });
  }

  setPanLFO2Bottom { | bottom = 0 |
    panLFO2Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\panLFO2Bottom, panLFO2Bottom); });
  }

  setPanLFO2Top { | top = 0 |
    panLFO2Top = top;
    if( applyMode == true, {
      synthGroup.set(\panLFO2Top, panLFO2Top); });
  }


  //// Oscillator 1:

  setOsc1Vol { | vol = -6 |
    osc1Amp = vol.dbamp;
    if( applyMode == true, {
      synthGroup.set(\osc1Amp, osc1Amp); });
  }

  setOsc1SubVol { | vol = -70 |
    osc1SubAmp = vol.dbamp;
    if( applyMode == true, {
      synthGroup.set(\osc1SubAmp, osc1SubAmp); });
  }

  setOsc1Waveform { | waveform = 'saw' |
    if( waveform.isInteger || waveform.isFloat, { osc1Waveform = waveform },
      {
        switch(waveform,
          { 'sine' }, { osc1Waveform = 0 },
          { 'tri' }, { osc1Waveform = 1 },
          { 'saw' }, { osc1Waveform = 2 },
          { 'rect' }, { osc1Waveform = 3 }
        );
    });
    if( applyMode == true, {
      synthGroup.set(\osc1Waveform, osc1Waveform); });
  }

  setOsc1OctaveMul { | octaveMul = 1 |
    osc1OctaveMul = octaveMul;
    if( applyMode == true, {
      synthGroup.set(\osc1OctaveMul, osc1OctaveMul); });
  }

  setOsc1Octave { | octave = 3 |
    switch(octave,
      { 0 }, { this.setOsc1OctaveMul(0.125) },
      { 1 }, { this.setOsc1OctaveMul(0.25) },
      { 2 }, { this.setOsc1OctaveMul(0.5) },
      { 3 }, { this.setOsc1OctaveMul(1) },
      { 4 }, { this.setOsc1OctaveMul(2) },
      { 5 }, { this.setOsc1OctaveMul(4) },
      { 6 }, { this.setOsc1OctaveMul(8) }
    );
  }

  setOsc1PulseWidth { | pulseWidth = 0.5 |
    osc1PulseWidth = pulseWidth;
    if( applyMode == true, {
      synthGroup.set(\osc1PulseWidth, osc1PulseWidth); });
  }


  // frequency envelope:
  setOsc1FreqEnvStartRatio { | ratio = 1.0 |
    osc1FreqEnvStartRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc1FreqEnvStartRatio, osc1FreqEnvStartRatio); });
  }
  setOsc1FreqEnvEndRatio { | ratio = 1.0 |
    osc1FreqEnvEndRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc1FreqEnvEndRatio, osc1FreqEnvEndRatio); });
  }
  setOsc1FreqEnvTime { | time = 0.0 |
    osc1FreqEnvTime = time;
    if( applyMode == true, {
      synthGroup.set(\osc1FreqEnvTime, osc1FreqEnvTime); });
  }

  // Oscillator 1 LFO 1:
  setOsc1FreqLFO1BottomRatio { | ratio = 1.0 |
    osc1FreqLFO1BottomRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc1FreqLFO1BottomRatio, osc1FreqLFO1BottomRatio); });

  }

  setOsc1FreqLFO1TopRatio { | ratio = 1.0 |
    osc1FreqLFO1TopRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc1FreqLFO1TopRatio, osc1FreqLFO1TopRatio); });
  }

  setOsc1PulseWidthLFO1Bottom { | bottom = 0.0 |
    osc1PulseWidthLFO1Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc1PulseWidthLFO1Bottom, osc1PulseWidthLFO1Bottom); });
  }

  setOsc1PulseWidthLFO1Top { | top = 0.0 |
    osc1PulseWidthLFO1Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc1PulseWidthLFO1Top, osc1PulseWidthLFO1Top); });
  }

  setOsc1AmpLFO1Bottom { | bottom = 1.0 |
    osc1AmpLFO1Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc1AmpLFO1Bottom, osc1AmpLFO1Bottom); });
  }

  setOsc1AmpLFO1Top { | top = 1.0 |
    osc1AmpLFO1Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc1AmpLFO1Top, osc1AmpLFO1Top); });
  }

  setOsc1WaveformLFO1Bottom { | bottom = 0.0 |
    osc1WaveformLFO1Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc1WaveformLFO1Bottom, osc1WaveformLFO1Bottom); });
  }

  setOsc1WaveformLFO1Top { | top = 0.0 |
    osc1WaveformLFO1Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc1WaveformLFO1Top, osc1WaveformLFO1Top); });
  }

   // Oscillator 1 LFO 2:
  setOsc1FreqLFO2BottomRatio { | ratio = 1.0 |
    osc1FreqLFO2BottomRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc1FreqLFO2BottomRatio, osc1FreqLFO2BottomRatio); });
  }

  setOsc1FreqLFO2TopRatio { | ratio = 1.0 |
    osc1FreqLFO2TopRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc1FreqLFO2TopRatio, osc1FreqLFO2TopRatio); });
  }

  setOsc1PulseWidthLFO2Bottom { | bottom = 0.0 |
    osc1PulseWidthLFO2Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc1PulseWidthLFO2Bottom, osc1PulseWidthLFO2Bottom); });
  }

  setOsc1PulseWidthLFO2Top { | top = 0.0 |
    osc1PulseWidthLFO2Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc1PulseWidthLFO2Top, osc1PulseWidthLFO2Top); });
  }

  setOsc1AmpLFO2Bottom { | bottom = 1.0 |
    osc1AmpLFO2Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc1AmpLFO2Bottom, osc1AmpLFO2Bottom); });
  }

  setOsc1AmpLFO2Top { | top = 1.0 |
    osc1AmpLFO2Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc1AmpLFO2Top, osc1AmpLFO2Top); });
  }

  setOsc1WaveformLFO2Bottom { | bottom = 0.0 |
    osc1WaveformLFO2Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc1WaveformLFO2Bottom, osc1WaveformLFO2Bottom); });
  }

  setOsc1WaveformLFO2Top { | top = 0.0 |
    osc1WaveformLFO2Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc1WaveformLFO2Top, osc1WaveformLFO2Top); });
  }


  //// Oscillator 2:

  setOsc2Vol { | vol = -6 |
    osc2Amp = vol.dbamp;
    if( applyMode == true, {
      synthGroup.set(\osc2Amp, osc2Amp); });

  }

  setOsc2SubVol { | vol = -70 |
    osc2SubAmp = vol.dbamp;
    if( applyMode == true, {
      synthGroup.set(\osc2SubAmp, osc2SubAmp); });
  }

  setOsc2Waveform { | waveform = 'saw' |
    if( waveform.isInteger || waveform.isFloat, { osc2Waveform = waveform },
      {
        switch(waveform,
          { 'sine' }, { osc2Waveform = 0 },
          { 'tri' }, { osc2Waveform = 1 },
          { 'saw' }, { osc2Waveform = 2 },
          { 'rect' }, { osc2Waveform = 3 }
        );
    });
    if( applyMode == true, {
      synthGroup.set(\osc2Waveform, osc2Waveform); });
  }

  setOsc2OctaveMul { | octaveMul = 1 |
    osc2OctaveMul = octaveMul;
    if( applyMode == true, {
      synthGroup.set(\osc2OctaveMul, osc2OctaveMul); });
  }

  setOsc2Octave { | octave = 3 |
    switch(octave,
      { 0 }, { this.setOsc2OctaveMul(0.125) },
      { 1 }, { this.setOsc2OctaveMul(0.25) },
      { 2 }, { this.setOsc2OctaveMul(0.5) },
      { 3 }, { this.setOsc2OctaveMul(1) },
      { 4 }, { this.setOsc2OctaveMul(2) },
      { 5 }, { this.setOsc2OctaveMul(4) },
      { 6 }, { this.setOsc2OctaveMul(8) }
    );
  }

  setOsc2PulseWidth { | pulseWidth = 0.5 |
    osc2PulseWidth = pulseWidth;
    if( applyMode == true, {
      synthGroup.set(\osc2PulseWidth, osc2PulseWidth); });
  }

  setOsc2DetuneCents { | detune = 0 |
    osc2DetuneCents = detune;
    if( applyMode == true, {
      synthGroup.set(\osc2DetuneCents, osc2DetuneCents); });
  }

  // frequency envelope:
  setOsc2FreqEnvStartRatio { | ratio = 1.0 |
    osc2FreqEnvStartRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc2FreqEnvStartRatio, osc2FreqEnvStartRatio); });
  }
  setOsc2FreqEnvEndRatio { | ratio = 1.0 |
    osc2FreqEnvEndRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc2FreqEnvEndRatio, osc2FreqEnvEndRatio); });
  }
  setOsc2FreqEnvTime { | time = 0.0 |
    osc2FreqEnvTime = time;
    if( applyMode == true, {
      synthGroup.set(\osc2FreqEnvTime, osc2FreqEnvTime); });
  }

  // Oscillator 2 LFO 1:
  setOsc2FreqLFO1BottomRatio { | ratio = 1.0 |
    osc2FreqLFO1BottomRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc2FreqLFO1BottomRatio, osc2FreqLFO1BottomRatio); });
  }

  setOsc2FreqLFO1TopRatio { | ratio = 1.0 |
    osc2FreqLFO1TopRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc2FreqLFO1TopRatio, osc2FreqLFO1TopRatio); });
  }

  setOsc2PulseWidthLFO1Bottom { | bottom = 0.0 |
    osc2PulseWidthLFO1Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc2PulseWidthLFO1Bottom, osc2PulseWidthLFO1Bottom); });
  }

  setOsc2PulseWidthLFO1Top { | top = 0.0 |
    osc2PulseWidthLFO1Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc2PulseWidthLFO1Top, osc2PulseWidthLFO1Top); });
  }

  setOsc2AmpLFO1Bottom { | bottom = 1.0 |
    osc2AmpLFO1Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc2AmpLFO1Bottom, osc2AmpLFO1Bottom); });
  }

  setOsc2AmpLFO1Top { | top = 1.0 |
    osc2AmpLFO1Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc2AmpLFO1Top, osc2AmpLFO1Top); });
  }

  setOsc2WaveformLFO1Bottom { | bottom = 0.0 |
    osc2WaveformLFO1Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc2WaveformLFO1Bottom, osc2WaveformLFO1Bottom); });
  }

  setOsc2WaveformLFO1Top { | top = 0.0 |
    osc2WaveformLFO1Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc2WaveformLFO1Top, osc2WaveformLFO1Top); });
  }

  // Oscillator 2 LFO 2:
  setOsc2FreqLFO2BottomRatio { | ratio = 1.0 |
    osc2FreqLFO2BottomRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc2FreqLFO2BottomRatio, osc2FreqLFO2BottomRatio); });
  }

  setOsc2FreqLFO2TopRatio { | ratio = 1.0 |
    osc2FreqLFO2TopRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\osc2FreqLFO2TopRatio, osc2FreqLFO2TopRatio); });
  }

  setOsc2PulseWidthLFO2Bottom { | bottom = 0.0 |
    osc2PulseWidthLFO2Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc2PulseWidthLFO2Bottom, osc2PulseWidthLFO2Bottom); });
  }

  setOsc2PulseWidthLFO2Top { | top = 0.0 |
    osc2PulseWidthLFO2Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc2PulseWidthLFO2Top, osc2PulseWidthLFO2Top); });
  }

  setOsc2AmpLFO2Bottom { | bottom = 1.0 |
    osc2AmpLFO2Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc2AmpLFO2Bottom, osc2AmpLFO2Bottom); });
  }

  setOsc2AmpLFO2Top { | top = 1.0 |
    osc2AmpLFO2Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc2AmpLFO2Top, osc2AmpLFO2Top); });
  }

  setOsc2WaveformLFO2Bottom { | bottom = 0.0 |
    osc2WaveformLFO2Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\osc2WaveformLFO2Bottom, osc2WaveformLFO2Bottom); });
  }

  setOsc2WaveformLFO2Top { | top = 0.0 |
    osc2WaveformLFO2Top = top;
    if( applyMode == true, {
      synthGroup.set(\osc2WaveformLFO2Top, osc2WaveformLFO2Top); });
  }


  //// Noise Oscillator:
  setNoiseOscVol { | vol = -70 |
    noiseOscAmp = vol.dbamp;
    if( applyMode == true, {
      synthGroup.set(\noiseOscAmp, noiseOscAmp); });
  }

  setNoiseOscCutoff { | cutoff = 1000 |
    noiseOscCutoff = cutoff;
    if( applyMode == true, {
      synthGroup.set(\noiseOscCutoff, noiseOscCutoff); });
  }

  // Noise Oscillator LFO 1:

  setNoiseOscAmpLFO1Bottom { | bottom = 1.0 |
    noiseOscAmpLFO1Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\noiseOscAmpLFO1Bottom, noiseOscAmpLFO1Bottom); });
  }

  setNoiseOscAmpLFO1Top { | top = 1.0 |
    noiseOscAmpLFO1Top = top;
    if( applyMode == true, {
      synthGroup.set(\noiseOscAmpLFO1Top, noiseOscAmpLFO1Top); });
  }

  setNoiseOscFilterLFO1BottomRatio { | ratio = 1.0 |
    noiseOscFilterLFO1BottomRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\noiseOscFilterLFO1BottomRatio, noiseOscFilterLFO1BottomRatio); });
  }

  setNoiseOscFilterLFO1TopRatio { | ratio = 1.0 |
    noiseOscFilterLFO1TopRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\noiseOscFilterLFO1TopRatio, noiseOscFilterLFO1TopRatio); });
  }

  // Noise Oscillator LFO 2:

  setNoiseOscAmpLFO2Bottom { | bottom = 1.0 |
    noiseOscAmpLFO2Bottom = bottom;
    if( applyMode == true, {
      synthGroup.set(\noiseOscAmpLFO2Bottom, noiseOscAmpLFO2Bottom); });
  }

  setNoiseOscAmpLFO2Top { | top = 1.0 |
    noiseOscAmpLFO2Top = top;
    if( applyMode == true, {
      synthGroup.set(\noiseOScAmpLFO2Top, noiseOscAmpLFO2Top); });
  }

  setNoiseOscFilterLFO2BottomRatio { | ratio = 1.0 |
    noiseOscFilterLFO2BottomRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\nosieOscFilterLFO2BottomRatio, noiseOscFilterLFO2BottomRatio); });
  }

  setNoiseOscFilterLFO2TopRatio { | ratio = 1.0 |
    noiseOscFilterLFO2TopRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\noiseOscFilterLFO2TopRatio, noiseOscFilterLFO2TopRatio); });
  }

  //// LFO1:
  setLFO1Freq { | freq = 1 |
    lfo1Freq = freq;
    if( applyMode == true, {
      synthGroup.set(\lfo1Freq, lfo1Freq); });
  }

  setLFO1Waveform { | waveform = 'sine' |
    if( waveform.isInteger || waveform.isFloat, { lfo1Waveform = waveform },
      {
        switch(waveform,
          { 'sine' }, { lfo1Waveform = 0 },
          { 'saw' }, { lfo1Waveform = 1 },
          { 'revSaw' }, { lfo1Waveform = 2 },
          { 'rect' }, { lfo1Waveform = 3 },
          { 'sampleAndHold' }, { lfo1Waveform = 4 },
          { 'noise' }, { lfo1Waveform = 5 }
        );
    });
    if( applyMode == true, {
      synthGroup.set(\lfo1Waveform, lfo1Waveform); });
  }

  setLFO1PulseWidth { | pulseWidth = 0.5 |
    lfo1PulseWidth = pulseWidth;
    if( applyMode == true, {
      synthGroup.set(\lfo1PulseWidth, lfo1PulseWidth); });
  }

  setLFO1EnvType { | type = 'none' |
    switch(type,
      { 'none' }, { lfo1EnvType = 0 },
      { 'attack' }, { lfo1EnvType = 1 },
      { 'release' }, { lfo1EnvType = 2 },
      { 'attackAndRelease' }, { lfo1EnvType = 3 }
    );
    if( type.isInteger, { lfo1EnvType = type; });
    if( applyMode == true, {
      synthGroup.set(\lfo1EnvType, lfo1EnvType); });
  }

  setLFO1AttackTime { | attack = 0.05 |
    lfo1AttackTime = attack;
    if( applyMode == true, {
      synthGroup.set(\lfo1AttackTime, lfo1AttackTime); });
  }

  setLFO1ReleaseTime { | release = 0.05 |
    lfo1ReleaseTime = release;
    if( applyMode == true, {
      synthGroup.set(\lfo1ReleaseTime, lfo1ReleaseTime); });
  }

  setLFO1FreqLFO2BottomRatio { | ratio = 1.0 |
    lfo1FreqLFO2BottomRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\lfo1FreqLFO2BottomRatio, lfo1FreqLFO2BottomRatio); });
  }

  setLFO1FreqLFO2TopRatio { | ratio = 1.0 |
    lfo1FreqLFO2TopRatio = ratio;
    if( applyMode == true, {
      synthGroup.set(\lfo1FreqLFO2TopRatio, lfo1FreqLFO2TopRatio); });
  }

  // LFO 2:
  setLFO2Freq { | freq = 1.0 |
    lfo2Freq = freq;
    lfo.set(\lfo2Freq, lfo2Freq);
  }


  setLFO2PulseWidth { | width = 0.5 |
    lfo2PulseWidth = width;
    lfo.set(\lfo2PulseWidth, lfo2PulseWidth);
  }

  setLFO2Waveform { | waveform = 'sine' |
    if( waveform.isInteger || waveform.isFloat, { lfo.set(\lfo2Waveform, waveform); lfo2Waveform = waveform; },
      {
        switch(waveform,
          { 'sine' }, { lfo.set(\lfo2Waveform, 0); lfo2Waveform = 0; },
          { 'saw' }, { lfo.set(\lfo2Waveform, 1); lfo2Waveform = 1; },
          { 'revSaw' }, { lfo.set(\lfo2Waveform, 2); lfo2Waveform = 2;},
          { 'rect' }, { lfo.set(\lfo2Waveform, 3); lfo2Waveform = 3;},
          { 'sampleAndHold' }, { lfo.set(\lfo2Waveform, 4); lfo2Waveform = 4;},
          { 'noise' }, { lfo.set(\lfo2Waveform, 5); lfo2Waveform = 5; }
        );
    });
  }
}

//////// Presets:
+ Subtractive {

  prMakeParameterArray {
    parameterArray =
    [
      lfo1FreqLFO2BottomRatio, lfo1FreqLFO2TopRatio, lfo1Waveform,
      lfo1Freq, lfo1PulseWidth, lfo1EnvType, lfo1AttackTime, lfo1ReleaseTime,

      lfo2Freq, lfo2PulseWidth, lfo2Waveform,

      osc1OctaveMul, osc1FreqEnvStartRatio, osc1FreqEnvEndRatio, osc1FreqEnvTime,
      osc1FreqLFO1BottomRatio, osc1FreqLFO1TopRatio, osc1FreqLFO2BottomRatio,
      osc1FreqLFO2TopRatio, osc1PulseWidthLFO1Bottom, osc1PulseWidthLFO1Top,
      osc1PulseWidthLFO2Bottom, osc1PulseWidthLFO2Top, osc1AmpLFO1Bottom, osc1AmpLFO1Top,
      osc1AmpLFO2Bottom, osc1AmpLFO2Top, osc1WaveformLFO1Bottom, osc1WaveformLFO1Top,
      osc1WaveformLFO2Bottom, osc1WaveformLFO2Top, osc1Waveform, osc1PulseWidth,
      osc1Amp, osc1SubAmp,

      osc2OctaveMul, osc2DetuneCents,
      osc2FreqEnvStartRatio, osc2FreqEnvEndRatio, osc2FreqEnvTime,
      osc2FreqLFO1BottomRatio, osc2FreqLFO1TopRatio, osc2FreqLFO2BottomRatio, osc2FreqLFO2TopRatio,
      osc2PulseWidthLFO1Bottom, osc2PulseWidthLFO1Top, osc2PulseWidthLFO2Bottom, osc2PulseWidthLFO2Top,
      osc2AmpLFO1Bottom, osc2AmpLFO1Top, osc2AmpLFO2Bottom, osc2AmpLFO2Top,
      osc2WaveformLFO1Bottom, osc2WaveformLFO1Top, osc2WaveformLFO2Bottom, osc2WaveformLFO2Top,
      osc2Waveform, osc2PulseWidth, osc2Amp, osc2SubAmp,

      noiseOscAmpLFO1Bottom, noiseOscAmpLFO1Top, noiseOscAmpLFO2Bottom, noiseOscAmpLFO2Top,
      noiseOscFilterLFO1BottomRatio, noiseOscFilterLFO1TopRatio, noiseOscFilterLFO2BottomRatio,
      noiseOscFilterLFO2TopRatio,
      noiseOscAmp, noiseOscCutoff,

      filterEnvAttackRatio, filterEnvPeakRatio, filterEnvSustainRatio, filterEnvReleaseRatio,
      filterEnvAttackTime, filterEnvDecayTime, filterEnvReleaseTime,
      if(filterEnvLoop == 1, { true }, { false }),
      filterCutoffLFO1BottomRatio, filterCutoffLFO1TopRatio, filterCutoffLFO2BottomRatio,
      filterCutoffLFO2TopRatio, filterResLFO1Bottom, filterResLFO1Top,
      filterResLFO2Bottom, filterResLFO2Top,
      filterDrive, filterCutoff, filterRes, filterType,

      ampLFO1Bottom, ampLFO1Top, ampLFO2Bottom, ampLFO2Top,
      attackTime, decayTime, sustainLevel, releaseTime,

      pan,
      panLFO1Bottom, panLFO1Top, panLFO2Bottom, panLFO2Top,

    ];
  }

  printAllParameters {
    this.prMakeParameterArray;
    parameterArray.postln;
  }

  savePreset { | name = 'myPreset', array |
    presetDict[name] = array;
  }

  saveCurrentAsPreset { | name = 'myPreset' |
    this.prMakeParameterArray;
    parameterArray.postln;
    presetDict[name] = parameterArray;
  }

  setAllParameters { | array |

    // LFO 1:
    this.setLFO1FreqLFO2BottomRatio(array.at(0));
    this.setLFO1FreqLFO2TopRatio(array.at(1));
    this.setLFO1Waveform(array.at(2));
    this.setLFO1Freq(array.at(3));
    this.setLFO1PulseWidth(array.at(4));
    this.setLFO1EnvType(array.at(5));
    this.setLFO1AttackTime(array.at(6));
    this.setLFO1ReleaseTime(array.at(7));

    this.setLFO2Freq(array.at(8));
    this.setLFO2PulseWidth(array.at(9));
    this.setLFO2Waveform(array.at(10));

    this.setOsc1OctaveMul(array.at(11));
    this.setOsc1FreqEnvStartRatio(array.at(12));
    this.setOsc1FreqEnvEndRatio(array.at(13));
    this.setOsc1FreqEnvTime(array.at(14));
    this.setOsc1FreqLFO1BottomRatio(array.at(15));
    this.setOsc1FreqLFO1TopRatio(array.at(16));
    this.setOsc1FreqLFO2BottomRatio(array.at(17));
    this.setOsc1FreqLFO2TopRatio(array.at(18));
    this.setOsc1PulseWidthLFO1Bottom(array.at(19));
    this.setOsc1PulseWidthLFO1Top(array.at(20));
    this.setOsc1PulseWidthLFO2Bottom(array.at(21));
    this.setOsc1PulseWidthLFO2Top(array.at(22));
    this.setOsc1AmpLFO1Bottom(array.at(23));
    this.setOsc1AmpLFO1Top(array.at(24));
    this.setOsc1AmpLFO2Bottom(array.at(25));
    this.setOsc1AmpLFO2Top(array.at(26));
    this.setOsc1WaveformLFO1Bottom(array.at(27));
    this.setOsc1WaveformLFO1Top(array.at(28));
    this.setOsc1WaveformLFO2Bottom(array.at(29));
    this.setOsc1WaveformLFO2Top(array.at(30));
    this.setOsc1Waveform(array.at(31));
    this.setOsc1PulseWidth(array.at(32));
    this.setOsc1Vol(array.at(33).ampdb);
    this.setOsc1SubVol(array.at(34).ampdb);


    this.setOsc2OctaveMul(array.at(35));
    this.setOsc2DetuneCents(array.at(36));
    this.setOsc2FreqEnvStartRatio(array.at(37));
    this.setOsc2FreqEnvEndRatio(array.at(38));
    this.setOsc2FreqEnvTime(array.at(39));
    this.setOsc2FreqLFO1BottomRatio(array.at(40));
    this.setOsc2FreqLFO1TopRatio(array.at(41));
    this.setOsc2FreqLFO2BottomRatio(array.at(42));
    this.setOsc2FreqLFO2TopRatio(array.at(43));
    this.setOsc2PulseWidthLFO1Bottom(array.at(44));
    this.setOsc2PulseWidthLFO1Top(array.at(45));
    this.setOsc2PulseWidthLFO2Bottom(array.at(46));
    this.setOsc2PulseWidthLFO2Top(array.at(47));
    this.setOsc2AmpLFO1Bottom(array.at(48));
    this.setOsc2AmpLFO1Top(array.at(49));
    this.setOsc2AmpLFO2Bottom(array.at(50));
    this.setOsc2AmpLFO2Top(array.at(51));
    this.setOsc2WaveformLFO1Bottom(array.at(52));
    this.setOsc2WaveformLFO1Top(array.at(53));
    this.setOsc2WaveformLFO2Bottom(array.at(54));
    this.setOsc2WaveformLFO2Top(array.at(55));
    this.setOsc2Waveform(array.at(56));
    this.setOsc2PulseWidth(array.at(57));
    this.setOsc2Vol(array.at(58).ampdb);
    this.setOsc2SubVol(array.at(59).ampdb);

    this.setNoiseOscAmpLFO1Bottom(array.at(60));
    this.setNoiseOscAmpLFO1Top(array.at(61));
    this.setNoiseOscAmpLFO2Bottom(array.at(62));
    this.setNoiseOscAmpLFO2Top(array.at(63));
    this.setNoiseOscFilterLFO1BottomRatio(array.at(64));
    this.setNoiseOscFilterLFO1TopRatio(array.at(65));
    this.setNoiseOscFilterLFO2BottomRatio(array.at(66));
    this.setNoiseOscFilterLFO2TopRatio(array.at(67));
    this.setNoiseOscVol(array.at(68).ampdb);
    this.setNoiseOscCutoff(array.at(69));

    this.setFilterEnvAttackRatio(array.at(70));
    this.setFilterEnvPeakRatio(array.at(71));
    this.setFilterEnvSustainRatio(array.at(72));
    this.setFilterEnvReleaseRatio(array.at(73));
    this.setFilterEnvAttackTime(array.at(74));
    this.setFilterEnvDecayTime(array.at(75));
    this.setFilterEnvReleaseTime(array.at(76));
    this.setFilterEnvLoop(array.at(77));

    this.setFilterCutoffLFO1BottomRatio(array.at(78));
    this.setFilterCutoffLFO1TopRatio(array.at(79));
    this.setFilterCutoffLFO2BottomRatio(array.at(80));
    this.setFilterCutoffLFO2TopRatio(array.at(81));
    this.setFilterResLFO1Bottom(array.at(82));
    this.setFilterResLFO1Top(array.at(83));
    this.setFilterResLFO2Bottom(array.at(84));
    this.setFilterResLFO2Top(array.at(85));
    this.setFilterDrive(array.at(86));
    this.setFilterCutoff(array.at(87));
    this.setFilterRes(array.at(88));
    this.setFilterType(array.at(89));


    this.setAmplitudeLFO1Bottom(array.at(90));
    this.setAmplitudeLFO1Top(array.at(91));
    this.setAmplitudeLFO2Bottom(array.at(92));
    this.setAmplitudeLFO2Top(array.at(93));
    this.setAttackTime(array.at(94));
    this.setDecayTime(array.at(95));
    this.setSustainLevel(array.at(96));
    this.setReleaseTime(array.at(97));

    this.setPan(array.at(98));
    this.setPanLFO1Bottom(array.at(99));
    this.setPanLFO1Top(array.at(100));
    this.setPanLFO2Bottom(array.at(101));
    this.setPanLFO2Top(array.at(102));

  }

  loadPreset { | name = 'myPreset' |
    this.setAllParameters(presetDict[name]);
  }

}

 