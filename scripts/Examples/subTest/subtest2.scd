~sub = Subtractive.new;

~sub.setReleaseTime(22);
~sub.setAttackTime(19);

~sub.setFilterCutoffLFOTopRatio(10);
~sub.setFilterCutoffLFOBottomRatio(0.01);
~sub.setFilterCutoffLFO2BottomRatio(0.2);
~sub.setFilterCutoffLFO2TopRatio(1);

~sub.setFilterCutoff(700);
~sub.setFilterType('lowPass');
~sub.setFilterType('highPass');
~sub.setFilterRes(1.5);

~sub.setOsc1FreqLFOTopRatio(2);
~sub.setOsc2FreqLFOTopRatio(2);


~sub.setLFO2Freq(1.1);
~sub.setLFO2Waveform('noise');
~sub.setLFO2Waveform('saw');
~sub.setLFO1Waveform('revSaw');
~sub.setLFO1Freq(1);
~sub.setLFOFreqLFO2BottomRatio(0.8);
~sub.setLFOFreqLFO2TopRatio(6);

~sub.setAmplitudeLFOBottom(0);
~sub.setAmplitudeLFOTop(1);
~sub.setAmplitudeLFO2Top(1);
~sub.setAmplitudeLFO2Bottom(0);

~sub.setOsc1AmpLFOBottom(0);

~sub.setOsc1WaveformLFO2Top(1);
~sub.setOsc1WaveformLFO2Bottom(4);
~sub.setOsc1SubVol(-7);
~sub.setOsc2Vol(12);
~sub.setOsc2SubVol(-12);
~sub.setOsc2Octave(2);
~sub.setOsc2OctaveMul(0.5);
~sub.setNoiseOscAmpLFO2Bottom(1);
~sub.setNoiseOscAmpLFO2Top(1);
~sub.setNoiseOscVol(-6);
~sub.setNoiseOscCutoff(1200);
~sub.setNoiseOscFilterLFOTopRatio(1);
~sub.setNoiseOscFilterLFOBottomRatio(12);
~sub.setOsc1Waveform('saw');
~sub.setOsc2Waveform('rect');
~sub.setOsc1PulseWidth(0.2);
~sub.setOsc2PulseWidth(0.4);


~sub.setLFOPulseWidth(0.1);
~sub.setLFO1Waveform('rect');
~sub.setLFO2PulseWidth(0.2)
~sub.setLFO2Waveform('saw');
~sub.setOsc1FreqEnvStartRatio(6);
~sub.setOsc1FreqEnvEndRatio(1);
~sub.setOsc1FreqEnvTime(60);
~sub.setOsc2FreqEnvTime(60);
~sub.setOsc2FreqEnvStartRatio(0.3);
~sub.setOsc2FreqEnvEndRatio(1);
~sub.setOsc2DetuneCents(0);

~sub.playNote(22.midicps);
~sub.playNote(34.midicps);
~sub.playNote(46.midicps);
~sub.playNote(58.midicps);
~sub.playNote(61.midicps);
~sub.playNote(63.midicps);

~sub.playNote(60.midicps);
~sub.playNote(61.midicps);
~sub.playNote(66.midicps);
~sub.playNote(71.midicps);
~sub.playNote(78.midicps);

~sub.playNote(55);

~sub.playNote(65.midicps);
~sub.playNote(82.midicps);
~sub.playNote()
~sub.playNote(228);

~sub.releaseNote(58.midicps);
s.makeWindow;

~sub.mixer.setVol(0, 3);

s.meter;

~sub.releaseAllNotes;

~sub.synthDict;

s.scope;

~sub.setOsc2PulseWidth(0.1);
~sub.setOsc1PulseWidth(0.9);

r = r {
  ~sub.playNote(67.midicps);
  loop {
    ~sub.playNote(60.midicps);
    ~sub.releaseNote(56.midicps);
    ~sub.releaseNote(63.midicps);
    ~sub.playNote(74.midicps);
    ~sub.playNote(81.midicps);
    rrand(10, 25).wait;
    ~sub.releaseNote(74.midicps);
    ~sub.releaseNote(81.midicps);
    ~sub.playNote(70.midicps);
    ~sub.playNote(77.midicps);
    rrand(10, 25).wait;
    ~sub.releaseNote(70.midicps);
    ~sub.releaseNote(77.midicps);
    ~sub.playNote(74.midicps);
    ~sub.playNote(81.midicps);
    rrand(10, 25).wait;
    ~sub.releaseNote(74.midicps);
    ~sub.releaseNote(81.midicps);
    ~sub.playNote(70.midicps);
    ~sub.playNote(77.midicps);
    rrand(10, 25).wait;

    ~sub.releaseNote(60.midicps);
    ~sub.releaseNote(70.midicps);
    ~sub.releaseNote(77.midicps);
    ~sub.playNote(56.midicps);
    ~sub.playNote(63.midicps);
    ~sub.playNote(74.midicps);
    rrand(15, 30).wait;
  }
}.play

r.stop;
~sub.setPanLFOTop(-1);
~sub.setPanLFOBottom(1);