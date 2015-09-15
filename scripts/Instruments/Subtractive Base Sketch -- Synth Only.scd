var page = 'subtractive';
var controlButtonsResetFunc;
var basicBank = 0;
var returnsBank = 7;
var octaveAdd = [36, 48, 60, 72];
var majorGridBank = 0;
var majorArray = [0, 2, 4, 5, 7, 9, 11, 12.0001];
var majorArrayColor = ['white', 'cyan', 'magenta', 'cyan', 'yellow', 'cyan', 'blue', 'white'];


SynthDef(\prm_Subtractive_Voice, {
      |
      outBus = 0, lfo2InBus = 0, amp = 1,
      freq = 220,
      gate = 1,

      lfo1FreqLFO2BottomRatio = 1.0, lfo1FreqLFO2TopRatio = 1.0
      lfo1Waveform = 0, lfo1Freq = 1, lfo1PulseWidth = 0.5,
      lfo1EnvType = 0, lfo1AttackTime = 0.05, lfo1ReleaseTime = 0.05,

      osc1OctaveMul = 1,
      osc1FreqEnvStartRatio = 1.0, osc1FreqEnvEndRatio = 1.0, osc1FreqEnvTime = 0,
      osc1FreqLFO1BottomRatio = 1.0, osc1FreqLFO1TopRatio = 1.0, osc1FreqLFO2BottomRatio = 1.0,
      osc1FreqLFO2TopRatio = 1.0,
      osc1PulseWidthLFO1Bottom = 0, osc1PulseWidthLFO1Top = 0, osc1PulseWidthLFO2Bottom = 0,
      osc1PulseWidthLFO2Top = 0,
      osc1AmpLFO1Bottom = 1, osc1AmpLFO1Top = 1, osc1AmpLFO2Bottom = 1, osc1AmpLFO2Top = 1,
      osc1WaveformLFO1Bottom = 0, osc1WaveformLFO1Top = 0, osc1WaveformLFO2Bottom = 0, osc1WaveformLFO2Top = 0,
      osc1Waveform = 2, osc1PulseWidth = 0.5, osc1Amp = 0.5, osc1SubAmp = 0,

      osc2OctaveMul = 0.5, osc2DetuneCents = 0,
      osc2FreqEnvStartRatio = 1.0, osc2FreqEnvEndRatio = 1.0, osc2FreqEnvTime = 0,
      osc2FreqLFO1BottomRatio = 1.0, osc2FreqLFO1TopRatio = 1.0, osc2FreqLFO2BottomRatio = 1.0,
      osc2FreqLFO2TopRatio = 1.0,
      osc2PulseWidthLFO1Bottom = 0, osc2PulseWidthLFO1Top = 0, osc2PulseWidthLFO2Bottom = 0,
      osc2PulseWidthLFO2Top = 0,
      osc2AmpLFO1Bottom = 1, osc2AmpLFO1Top = 1, osc2AmpLFO2Bottom = 1, osc2AmpLFO2Top = 1,
      osc2WaveformLFO1Bottom = 0, osc2WaveformLFO1Top = 0, osc2WaveformLFO2Bottom = 0, osc2WaveformLFO2Top = 0,
      osc2Waveform = 3, osc2PulseWidth = 0.5, osc2Amp = 0.25, osc2SubAmp = 0,

      noiseOscAmpLFO1Bottom = 1, noiseOscAmpLFO1Top = 1, noiseOscAmpLFO2Bottom = 1, noiseOscAmpLFO2Top = 1,
      noiseOscFilterLFO1BottomRatio = 1, noiseOscFilterLFO1TopRatio = 1, noiseOscFilterLFO2BottomRatio = 1,
      noiseOscFilterLFO2TopRatio = 1,
      noiseOscAmp = 0, noiseOscCutoff = 10000,

      filterEnvAttackRatio = 1.0, filterEnvPeakRatio = 1.0, filterEnvSustainRatio = 1.0,
      filterEnvReleaseRatio = 1.0,
      filterEnvAttackTime = 0.05, filterEnvDecayTime = 0, filterEnvReleaseTime = 0.05,
      filterEnvLoop = 0,
      filterCutoffLFO1BottomRatio = 1.0, filterCutoffLFO1TopRatio = 1.0, filterCutoffLFO2BottomRatio = 1.0,
      filterCutoffLFO2TopRatio = 1.0,
      filterResLFO1Bottom = 0.0, filterResLFO1Top = 0.0, filterResLFO2Bottom = 0.0, filterResLFO2Top = 0.0,
      drive = 1.0, filterCutoff = 2500, filterRes = 0, filterType = 0,

      ampLFO1Bottom = 1.0, ampLFO1Top = 1.0, ampLFO2Bottom = 1.0, ampLFO2Top = 1.0,
      attackTime = 0.05, decayTime = 0.05, sustainLevel = 1, releaseTime = 0.05,

      pan = 0,
      panLFO1Bottom = 0.0, panLFO1Top = 0.0, panLFO2Bottom = 0.0, panLFO2Top = 0.0
      |

      var lfo2;
      var lfo1, lfo1FreqLFO, thisLFO1Freq, lfo1Sine, lfo1Saw, lfo1RevSaw, lfo1Rect, lfo1Noise0, lfo1Noise2;
      var lfo1AttackEnv, lfo1ReleaseEnv, lfo1AttackAndReleaseEnv;

      var osc1Freq, osc1FreqEnv, osc1FreqLFO1, osc1FreqLFO2, osc1WidthLFO1, osc1WidthLFO2;
      var osc1AmpLFO1, osc1AmpLFO2, osc1WaveformLFO1, osc1WaveformLFO2;
      var osc1, osc1Sine, osc1Tri, osc1Saw, osc1Rect, osc1Sub;

      var osc2Freq, osc2FreqEnv, osc2FreqLFO1, osc2FreqLFO2, osc2WidthLFO1, osc2WidthLFO2;
      var osc2AmpLFO1, osc2AmpLFO2, osc2WaveformLFO1, osc2WaveformLFO2;
      var osc2, osc2Sine, osc2Tri, osc2Saw, osc2Rect, osc2Sub;

      var noiseOsc, noise, noiseOscFilter, noiseOscAmpLFO1, noiseOscAmpLFO2, noiseOscFilterLFO1, noiseOscFilterLFO2;

      var oscSum;

      var preFilterDrive, filter, thisFilterCutoff, thisFilterRes, lowPassFilter, highPassFilter, bandPassFilter;
      var filterCutoffLFO1, filterCutoffLFO2, filterResLFO1, filterResLFO2, filterEnv;

      var ampEnv, ampLFO1, ampLFO2;
      var panning, panningLFO1, panningLFO2;

      var sig;

      lfo2 = In.kr(lfo2InBus);

      // Voice LFO:
      lfo1FreqLFO = lfo2.linlin(-1, 1, lfo1FreqLFO2BottomRatio, lfo1FreqLFO2TopRatio);
      thisLFO1Freq = lfo1Freq * lfo1FreqLFO;
      lfo1Sine = SinOsc.kr(thisLFO1Freq);
      lfo1Saw = LFSaw.kr(thisLFO1Freq, 1);
      lfo1RevSaw = LFSaw.kr(thisLFO1Freq, 1) * -1;
      lfo1Rect = (LFPulse.kr(thisLFO1Freq, width: lfo1PulseWidth) - 0.5) * 2;
      lfo1Noise0 = LFNoise0.kr(thisLFO1Freq);
      lfo1Noise2 = LFNoise2.kr(thisLFO1Freq);
      lfo1AttackEnv = EnvGen.kr(Env.new([0, 0, 1], [0, lfo1AttackTime], 'cubed', 1), gate);
      lfo1ReleaseEnv = EnvGen.kr(Env.cutoff(lfo1ReleaseTime, 1, 'cubed'), gate);
      lfo1AttackAndReleaseEnv = EnvGen.kr(Env.asr(lfo1AttackTime, 1, lfo1ReleaseTime, curve: 'cubed'), gate);
      //lfo1AttackAndReleaseEnv = EnvGen.kr(Env.new([0, 0, 1, 0], [0, lfo1AttackTime, lfo1ReleaseTime], 'cubed', 1),
        //gate);
      lfo1 = SelectX.kr(lfo1Waveform, [lfo1Sine, lfo1Saw, lfo1RevSaw, lfo1Rect, lfo1Noise0, lfo1Noise2]);
      lfo1 = Select.kr(lfo1EnvType, [lfo1, lfo1 * lfo1AttackEnv, lfo1 * lfo1ReleaseEnv, lfo1 * lfo1AttackAndReleaseEnv]);


      // Oscillator 1:
      osc1FreqEnv = EnvGen.kr(Env.new([osc1FreqEnvStartRatio, osc1FreqEnvStartRatio, osc1FreqEnvEndRatio],
        [0, osc1FreqEnvTime], 'exp'), gate);
      osc1FreqLFO1 = lfo1.linlin(-1, 1, osc1FreqLFO1BottomRatio, osc1FreqLFO1TopRatio);
      osc1FreqLFO2 = lfo2.linlin(-1, 1, osc1FreqLFO2BottomRatio, osc1FreqLFO2TopRatio);
      osc1AmpLFO1 = lfo1.linlin(-1, 1, osc1AmpLFO1Bottom, osc1AmpLFO1Top);
      osc1AmpLFO2 = lfo2.linlin(-1, 1, osc1AmpLFO2Bottom, osc1AmpLFO2Top);
      osc1WidthLFO1 = lfo1.linlin(-1, 1, osc1PulseWidthLFO1Bottom, osc1PulseWidthLFO1Top);
      osc1WidthLFO2 = lfo2.linlin(-1, 1, osc1PulseWidthLFO2Bottom, osc1PulseWidthLFO2Top);
      osc1WaveformLFO1 = lfo1.linlin(-1, 1, osc1WaveformLFO1Bottom, osc1WaveformLFO1Top);
      osc1WaveformLFO2 = lfo2.linlin(-1, 1, osc1WaveformLFO2Bottom, osc1WaveformLFO2Top);
      osc1Freq = freq * osc1OctaveMul * osc1FreqEnv * osc1FreqLFO1 * osc1FreqLFO2;

      osc1Sub = SinOsc.ar(osc1Freq/2) * osc1SubAmp;

      osc1Sine = SinOsc.ar(osc1Freq);
      osc1Tri = LPF.ar(LFTri.ar(osc1Freq), 18000);
      osc1Saw = Saw.ar(osc1Freq);
      osc1Rect = Pulse.ar(osc1Freq, (osc1PulseWidth + osc1WidthLFO1 + osc1WidthLFO2).wrap(0, 1));
      osc1 = SelectX.ar((osc1Waveform.lag(0.1) + osc1WaveformLFO1 + osc1WaveformLFO2).wrap(0.0, 4.0),
        [osc1Sine, osc1Tri, osc1Saw, osc1Rect]);

      osc1 = osc1 * osc1Amp;
      osc1 = osc1 + osc1Sub;
      osc1 = osc1 * osc1AmpLFO1 * osc1AmpLFO2;

      // Oscillator 2:
      osc2FreqEnv = EnvGen.kr(Env.new([osc2FreqEnvStartRatio, osc2FreqEnvStartRatio, osc2FreqEnvEndRatio],
        [0, osc2FreqEnvTime], 'exp'), gate);
      osc2FreqLFO1 = lfo1.linlin(-1, 1, osc2FreqLFO1BottomRatio, osc2FreqLFO1TopRatio);
      osc2FreqLFO2 = lfo2.linlin(-1, 1, osc2FreqLFO2BottomRatio, osc2FreqLFO2TopRatio);
      osc2WidthLFO1 = lfo1.linlin(-1, 1, osc2PulseWidthLFO1Bottom, osc2PulseWidthLFO1Top);
      osc2WidthLFO2 = lfo2.linlin(-1, 1, osc2PulseWidthLFO2Bottom, osc2PulseWidthLFO2Top);
      osc2AmpLFO1 = lfo1.linlin(-1, 1, osc2AmpLFO1Bottom, osc2AmpLFO1Top);
      osc2AmpLFO2 = lfo2.linlin(-1, 1, osc2AmpLFO2Bottom, osc2AmpLFO2Top);
      osc2WaveformLFO1 = lfo1.linlin(-1, 1, osc2WaveformLFO1Bottom, osc2WaveformLFO1Top);
      osc2WaveformLFO2 = lfo2.linlin(-1, 1, osc2WaveformLFO2Bottom, osc2WaveformLFO2Top);
      osc2Freq = freq * osc2OctaveMul * osc2FreqEnv * osc2FreqLFO1 * osc2FreqLFO2 * (osc2DetuneCents/100).midiratio;

      osc2Sub = SinOsc.ar(osc2Freq/2) * osc2SubAmp;

      osc2Sine = SinOsc.ar(osc2Freq);
      osc2Tri = LPF.ar(LFTri.ar(osc2Freq), 18000);
      osc2Saw = Saw.ar(osc2Freq);
      osc2Rect = Pulse.ar(osc2Freq, (osc2PulseWidth + osc2WidthLFO1 + osc2WidthLFO2).wrap(0, 1));
      osc2 = SelectX.ar((osc2Waveform.lag(0.1) + osc2WaveformLFO1 + osc2WaveformLFO2).wrap(0.0, 4.0),
        [osc2Sine, osc2Tri, osc2Saw, osc2Rect]);

      osc2 = osc2 * osc2Amp;
      osc2 = osc2 + osc2Sub;
      osc2 = osc2 * osc2AmpLFO1 * osc2AmpLFO2;

      noiseOscAmpLFO1 = lfo1.linlin(-1, 1, noiseOscAmpLFO1Bottom, noiseOscAmpLFO1Top);
      noiseOscAmpLFO2 = lfo2.linlin(-1, 1, noiseOscAmpLFO2Bottom, noiseOscAmpLFO2Top);
      noiseOscFilterLFO1 = lfo1.linlin(-1, 1, noiseOscFilterLFO1BottomRatio, noiseOscFilterLFO1TopRatio);
      noiseOscFilterLFO2 = lfo2.linlin(-1, 1, noiseOscFilterLFO2BottomRatio, noiseOscFilterLFO2TopRatio);
      noise = WhiteNoise.ar(1);
      noiseOscFilter = LPF.ar(noise, noiseOscCutoff * noiseOscFilterLFO1 * noiseOscFilterLFO2);
      noiseOsc = noiseOscFilter * noiseOscAmp;
      noiseOsc = noiseOsc * noiseOscAmpLFO1 * noiseOscAmpLFO2;

      oscSum = Mix.ar([osc1, osc2, noiseOsc]);

      // filter:

      if(filterEnvLoop == 0, { filterEnvLoop = nil }, { filterEnvLoop = filterEnvLoop - 1 });
      filterEnv = EnvGen.kr(
        Env.new([filterEnvAttackRatio, filterEnvAttackRatio, filterEnvPeakRatio,
          filterEnvSustainRatio, filterEnvReleaseRatio],
          [0, filterEnvAttackTime, filterEnvDecayTime, filterEnvReleaseTime], -4, 3, filterEnvLoop),
        gate);
      filterCutoffLFO1 = lfo1.linlin(-1, 1, filterCutoffLFO1BottomRatio, filterCutoffLFO1TopRatio);
      filterCutoffLFO2 = lfo2.linlin(-1, 1, filterCutoffLFO2BottomRatio, filterCutoffLFO2TopRatio);
      filterResLFO1 = lfo1.linlin(-1, 1, filterResLFO1Bottom, filterResLFO1Top);
      filterResLFO2 = lfo2.linlin(-1, 1, filterResLFO2Bottom, filterResLFO2Top);
      thisFilterCutoff = filterCutoff.lag(0.1) * filterEnv * filterCutoffLFO1 * filterCutoffLFO2;
      thisFilterRes = filterRes + filterResLFO1 + filterResLFO2;
      preFilterDrive = (oscSum * drive).distort;
      lowPassFilter = DFM1.ar(preFilterDrive, thisFilterCutoff, thisFilterRes, 1, 0, 0.0005);
      highPassFilter = DFM1.ar(preFilterDrive, thisFilterCutoff, thisFilterRes, 1, 1, 0.0005);
      bandPassFilter = BBandPass.ar(preFilterDrive, thisFilterCutoff, filterRes);
      filter = Select.ar(filterType, [lowPassFilter, highPassFilter, bandPassFilter]);

      // Amplitude Envelope:
      ampLFO1 = lfo1.linlin(-1, 1, ampLFO1Bottom, ampLFO1Top);
      ampLFO2 = lfo2.linlin(-1, 1, ampLFO2Bottom, ampLFO2Top);
      //ampEnv = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4), gate);
      ampEnv = EnvGen.kr(Env.new([0, 0, 1, sustainLevel, 0], [0, attackTime, decayTime, releaseTime],
        curve: -4, releaseNode: 3), gate, doneAction: 2);
      ampEnv = ampEnv * ampLFO1 * ampLFO2;

      panningLFO1 = lfo1.linlin(-1, 1, panLFO1Bottom, panLFO1Top);
      panningLFO2 = lfo2.linlin(-1, 1, panLFO2Bottom, panLFO2Top);
      panning = Pan2.ar(filter * ampEnv, (pan + panningLFO1 + panningLFO2).clip(-1, 1));

      sig = panning * amp;

      Out.ar(outBus, sig);

    }).add;


/* // if things need to be made:
~prm = AudioSystem.new(2);
~prm.systemMixer.setVol(0, 24);
~base = Base.new;
~sub = Subtractive.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus);
*/


~base.makePage(page);
~base.setPage(page);
~base.addFadersBanks(7, page: page);
~base.addTouchButtonsBanks(7, page: page);
~base.addGridBanks(7, page: page);







/////////////
// Grids: //
////////////

//////// Major Grid Bank:

// first row:
(
var synth;
8.do({ | i | ~base.setGridFunc(i, 0, { | vel |
  var vol = vel.ccdbfs(-24, 3);
  synth = Synth(\prm_Subtractive_Voice, [\outBus, 0, \freq, (majorArray[i] + octaveAdd[1]).midicps]);
  ~base.turnGridGreen(i, 0);
  }, 'noteOn', bank: majorGridBank, page: page);
});
8.do({ | i | ~base.setGridFunc(i, 0, {
  synth.set(\gate, 0);
  ~base.turnGridColor(i, 0, majorArrayColor[i], page: page);
  }, 'noteOff', bank: majorGridBank, page: page);
});
);
/*
// second row:
8.do({ | i | ~base.setGridFunc(i, 1, { | vel |
  var vol = vel.ccdbfs(-24, 3);
  ~sub.playNote((majorArray[i] + octaveAdd[1]).midicps, vol);
  ~base.turnGridGreen(i, 1);
  }, 'noteOn', bank: majorGridBank, page: page);
});
8.do({ | i | ~base.setGridFunc(i, 1, {
  ~sub.releaseNote((majorArray[i] + octaveAdd[1]).midicps);
  ~base.turnGridColor(i, 1, majorArrayColor[i], page: page);
  }, 'noteOff', bank: majorGridBank, page: page);
});

// third row:
8.do({ | i | ~base.setGridFunc(i, 2, { | vel |
  var vol = vel.ccdbfs(-24, 3);
  ~sub.playNote((majorArray[i] + octaveAdd[2]).midicps, vol);
  ~base.turnGridGreen(i, 2);
  }, 'noteOn', bank: majorGridBank, page: page);
});
8.do({ | i | ~base.setGridFunc(i, 2, {
  ~sub.releaseNote((majorArray[i] + octaveAdd[2]).midicps);
  ~base.turnGridColor(i, 2, majorArrayColor[i], page: page);
  }, 'noteOff', bank: majorGridBank, page: page);
});

// fourth row:
8.do({ | i | ~base.setGridFunc(i, 3, { | vel |
  var vol = vel.ccdbfs(-24, 3);
  ~sub.playNote((majorArray[i] + octaveAdd[3]).midicps, vol);
  ~base.turnGridGreen(i, 3);
  }, 'noteOn', bank: majorGridBank, page: page);
});
8.do({ | i | ~base.setGridFunc(i, 3, {
  ~sub.releaseNote((majorArray[i] + octaveAdd[3]).midicps);
  ~base.turnGridColor(i, 3, majorArrayColor[i], page: page);
  }, 'noteOff', bank: majorGridBank, page: page);
});
*/





