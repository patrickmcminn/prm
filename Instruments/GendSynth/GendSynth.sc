/*
x = { | amp = 0, dur = 7, ad = 1  | HPF.ar(LPF.ar(Gendy3.ar(amp, dur, ad, 1, [440], 0.5, 0.5), 3500), 20) ! 2 }.play

x.set(\amp, 5);
x.set(\dur, 3);
x.set(\ad, 3);

//y = { Gendy3.ar(freq: [220, 110, 330], ampscale: SinOsc.kr(0.1).range(0, 1), durscale: 0.3) }.play;

SynthDef(\prm_PrimGendSynth, {

  |
  outBus = 0, lfo2InBus = 0, amp = 1, freq = 220, gate = 1,

  lfo1FreqLFO2BottomRatio = 1.0, lfo1FreqLFO2TopRatio = 1.0,
  lfo1Freq = 1, lfo1PulseWidth = 0.5, lfo1AttackTime = 0.05, lfo1ReleaseTime = 0.05,
  lfo1Waveform = 0, lfo1EnvType = 0,

  freqEnvStartRatio = 1, freqEnvEndRatio = 1, freqEnvTime = 0,

  freqLFO1BottomRatio = 1, freqLFO1TopRatio = 1, freqLFO2BottomRatio = 1, freqLFO2TopRatio = 1,

  ampDist = 1, ampDistLFO1Bottom = 0, ampDistLFO1Top = 0, ampDistLFO2Bottom = 0, ampDistLFO2Top = 0,
  durDist = 1, durDistLFO1Bottom = 0, durDistLFO1Top = 0, durDistLFO2Bottom = 0, durDistLFO2Top = 0,

  adParam = 1, adParamLFO1Bottom = 0, adParamLFO1Top = 0, adParamLFO2Bottom = 0, adParamLFO2Top = 0,
  ddParam = 1, ddParamLFO1Bottom = 0, ddParamLFO1Top = 0, ddParamLFO2Bottom = 0, ddParamLFO2Top = 0,

  ampScale = 0.2, ampScaleLFO1Bottom = 0, ampScaleLFO1Top = 0, ampScaleLFO2Bottom = 0, ampScaleLFO2Top = 0,
  durScale = 0.2, durScaleLFO1Bottom = 0, durScaleLFO1Top = 0, durScaleLFO2Bottom = 0, durScaleLFO2Top = 0,

  initCPs = 12,

  knum = 12, knumLFO1Bottom = 0, knumLFO1Top = 0, knumLFO2Bottom = 0, knumLFO2Top = 0,

  gendySubAmp = 0,

  highPassCutoff = 80, highPassRes = 0.0,
  highPassEnvLoop = 0,
  highPassEnvAttackRatio = 1.0, highPassEnvPeakRatio = 1.0, highPassEnvSustainRatio = 1.0,
  highPassEnvReleaseRatio = 1.0,
  highPassEnvAttackTime = 0.0, highPassEnvDecayTime = 0.0, highPassEnvReleaseTime = 0.0,
  highPassCutoffLFO1BottomRatio = 1.0, highPassCutoffLFO1TopRatio = 1.0,
  highPassCutoffLFO2BottomRatio = 1.0, highPassCutoffLFO2TopRatio = 1.0,
  highPassResLFO1Bottom = 0, highPassResLFO1Top = 0, highPassResLFO2Bottom = 0,
  highPassResLFO2Top = 0,

  lowPassCutoff = 3500, lowPassRes = 0.0,
  lowPassEnvLoop = 0,
  lowPassEnvAttackRatio = 1.0, lowPassEnvPeakRatio = 1.0, lowPassEnvSustainRatio = 1.0,
  lowPassEnvReleaseRatio = 1.0,
  lowPassEnvAttackTime = 0.0, lowPassEnvDecayTime = 0.0, lowPassEnvReleaseTime = 0.0,
  lowPassCutoffLFO1BottomRatio = 1.0, lowPassCutoffLFO1TopRatio = 1.0,
  lowPassCutoffLFO2BottomRatio = 1.0, lowPassCutoffLFO2TopRatio = 1.0,
  lowPassResLFO1Bottom = 0, lowPassResLFO1Top = 0,
  lowPassResLFO2Bottom = 0, lowPassResLFO2Top = 0,

  attackTime = 0.05, decayTime = 0, sustainLevel = 1, releaseTime = 0.05,
  ampLFO1Bottom = 1, ampLFO1Top = 1, ampLFO2Bottom = 1, ampLFO2Top = 1,

  panLFO1Bottom = 0, panLFO1Top = 0, panLFO2Bottom = 0, panLFO2Top = 0,
  pan = 0

  |

  var lfo2;
  var lfo1, lfo1FreqLFO, thisLFO1Freq, lfo1Sine, lfo1Saw, lfo1RevSaw, lfo1Rect, lfo1SampleAndHold, lfo1Noise;
  var lfo1AttackEnv, lfo1ReleaseEnv, lfo1AttackAndReleaseEnv;

  var thisFreq, freqEnv, freqLFO1, freqLFO2;
  var gendyAmpDist, gendyAmpDistLFO1, gendyAmpDistLFO2;
  var gendyDurDist, gendyDurDistLFO1, gendyDurDistLFO2;
  var gendyAdParam, gendyAdParamLFO1, gendyAdParamLFO2;
  var gendyDdParam, gendyDdParamLFO1, gendyDdParamLFO2;
  var gendyAmpScale, gendyAmpScaleLFO1, gendyAmpScaleLFO2;
  var gendyDurScale, gendyDurScaleLFO1, gendyDurScaleLFO2;
  var gendyKnum, gendyKnumLFO1, gendyKnumLFO2;
  var gendy;
  var gendySub, gendySum;

  var highPassEnv;
  var highPass, thishighPassCutoff, thishighPassRes;
  var highPassCutoffLFO1, highPassCutoffLFO2, highPassResLFO1, highPassResLFO2;
  var lowPass, thislowPassCutoff, thislowPassRes;
  var lowPassCutoffLFO1, lowPassCutoffLFO2, lowPassResLFO1, lowPassResLFO2;
  var lowPassEnv;

  var ampEnv, ampLFO1, ampLFO2;
  var panning, panningLFO1, panningLFO2;

  var sig;

  // lfo 2 input:
  lfo2 = In.kr(lfo2InBus);

  // lfo 1 (per-voice lfo):
  lfo1FreqLFO = lfo2.linlin(-1, 1, lfo1FreqLFO2BottomRatio, lfo1FreqLFO2TopRatio);
  thisLFO1Freq = lfo1Freq * lfo1FreqLFO;
  lfo1Sine = SinOsc.kr(thisLFO1Freq);
  lfo1Saw = LFSaw.kr(thisLFO1Freq);
  lfo1RevSaw = LFSaw.kr(thisLFO1Freq, 1) * -1;
  lfo1Rect = (LFPulse.kr(thisLFO1Freq, width: lfo1PulseWidth) - 0.5) * 2;
  lfo1SampleAndHold = LFNoise0.kr(thisLFO1Freq);
  lfo1Noise = LFNoise2.kr(thisLFO1Freq);
  lfo1AttackEnv = EnvGen.kr(Env.new([0, 0, 1], [0, lfo1AttackTime, ], 'cubed', 1), gate);
  lfo1ReleaseEnv = EnvGen.kr(Env.cutoff(lfo1ReleaseTime, 1, 'cubed'), gate);
  lfo1AttackAndReleaseEnv = EnvGen.kr(Env.asr(lfo1AttackTime, 1, lfo1ReleaseTime, curve: 'cubed'), gate);
  lfo1 = SelectX.kr(lfo1Waveform, [lfo1Sine, lfo1Saw, lfo1RevSaw, lfo1Rect, lfo1SampleAndHold, lfo1Noise]);
  lfo1 = Select.kr(lfo1EnvType, [lfo1, lfo1 * lfo1AttackEnv, lfo1 * lfo1ReleaseEnv, lfo1 * lfo1AttackAndReleaseEnv]);

  // GENDYN Oscillator:
  freqEnv = EnvGen.kr(Env.new([freqEnvStartRatio, freqEnvStartRatio, freqEnvEndRatio],
        [0, freqEnvTime], 'exp'), gate);
  freqLFO1 = lfo1.linlin(-1, 1, freqLFO1BottomRatio, freqLFO1TopRatio);
  freqLFO2 = lfo2.linlin(-1, 1, freqLFO2BottomRatio, freqLFO2TopRatio);
  thisFreq = freq * freqLFO1 * freqLFO2 * freqEnv;
  // amp dist:
  gendyAmpDistLFO1 = lfo1.linlin(-1, 1, ampDistLFO1Bottom, ampDistLFO1Top);
  gendyAmpDistLFO2 = lfo2.linlin(-1, 1, ampDistLFO2Bottom, ampDistLFO2Top);
  gendyAmpDist = (ampDist + gendyAmpDistLFO1 + gendyAmpDistLFO2).clip(0, 6);
  // dur dist:
  gendyDurDistLFO1 = lfo1.linlin(-1, 1, durDistLFO1Bottom, durDistLFO1Top);
  gendyDurDistLFO2 = lfo1.linlin(-1, 1, durDistLFO2Bottom, durDistLFO2Top);
  gendyDurDist = (durDist + gendyDurDistLFO1 + gendyDurDistLFO2).clip(0, 6);
  gendyDurDist.poll;
  // ADParam:
  gendyAdParamLFO1 = lfo1.linlin(-1, 1, adParamLFO1Bottom, adParamLFO1Top);
  gendyAdParamLFO2 = lfo2.linlin(-1, 1, adParamLFO2Bottom, adParamLFO2Top);
  gendyAdParam = adParam + gendyAdParamLFO1 + gendyAdParamLFO2;
  // DDParam:
  gendyDdParamLFO1 = lfo1.linlin(-1, 1, ddParamLFO1Bottom, ddParamLFO1Top);
  gendyDdParamLFO2 = lfo2.linlin(-1, 1, ddParamLFO2Bottom, ddParamLFO2Top);
  gendyDdParam = ddParam + gendyDdParamLFO1 + gendyDdParamLFO2;
  // ampScale:
  gendyAmpScaleLFO1 = lfo1.linlin(-1, 1, ampScaleLFO1Bottom, ampScaleLFO1Top);
  gendyAmpScaleLFO2 = lfo2.linlin(-1, 1, ampScaleLFO2Bottom, ampScaleLFO2Top);
  gendyAmpScale = ampScale + gendyAmpScaleLFO1 + gendyAmpScaleLFO2;
  // durScale:
  gendyDurScaleLFO1 = lfo1.linlin(-1, 1, durScaleLFO1Bottom, durScaleLFO1Top);
  gendyDurScaleLFO2 = lfo2.linlin(-1, 1, durScaleLFO2Bottom, durScaleLFO2Top);
  gendyDurScale = durScale + gendyDurScaleLFO1 + gendyDurScaleLFO2;
  // knum:
  gendyKnumLFO1 = lfo1.linlin(-1, 1, knumLFO1Bottom, knumLFO1Top);
  gendyKnumLFO2 = lfo2.linlin(-1, 1, knumLFO2Bottom, knumLFO2Top);
  gendyKnum = knum + gendyKnumLFO1 + gendyKnumLFO2.clip(2, initCPs);

  gendy = Gendy3.ar(gendyAmpDist, gendyDurDist, gendyAdParam, gendyDdParam, thisFreq,
    gendyAmpScale, gendyDurScale, initCPs, gendyKnum);
  gendySub = SinOsc.ar(thisFreq/2) * gendySubAmp;
  gendySum = gendy + gendySub;

  // high pass filter:
  if(highPassEnvLoop == 0, { highPassEnvLoop = nil }, { highPassEnvLoop = highPassEnvLoop - 1 });
  highPassEnv = EnvGen.kr(
    Env.new([highPassEnvAttackRatio, highPassEnvAttackRatio, highPassEnvPeakRatio,
      highPassEnvSustainRatio, highPassEnvReleaseRatio],
      [0, highPassEnvAttackTime, highPassEnvDecayTime, highPassEnvReleaseTime], -4, 3,
      highPassEnvLoop), gate);
  highPassCutoffLFO1 = lfo1.linlin(-1, 1, highPassCutoffLFO1BottomRatio, highPassCutoffLFO1TopRatio);
  highPassCutoffLFO2 = lfo2.linlin(-1, 1, highPassCutoffLFO2BottomRatio, highPassCutoffLFO2TopRatio);
  thishighPassCutoff = highPassCutoff * highPassEnv * highPassCutoffLFO1 * highPassCutoffLFO2;

  highPassResLFO1 = lfo1.linlin(-1, 1, highPassResLFO1Bottom, highPassResLFO1Top);
  highPassResLFO2 = lfo2.linlin(-1, 1, highPassResLFO2Bottom, highPassResLFO2Top);
  thishighPassRes = highPassRes + highPassResLFO1 + highPassResLFO2;

  highPass = DFM1.ar(gendySum, thishighPassCutoff, thishighPassRes, 1, 1);

  // low pass filter:
  if(lowPassEnvLoop == 0, { lowPassEnvLoop = nil }, { lowPassEnvLoop = lowPassEnvLoop - 1 });
  lowPassEnv = EnvGen.kr(
    Env.new([lowPassEnvAttackRatio, lowPassEnvAttackRatio, lowPassEnvPeakRatio,
      lowPassEnvSustainRatio, lowPassEnvReleaseRatio], [0, lowPassEnvAttackTime,
        lowPassEnvDecayTime, lowPassEnvReleaseTime], -4, 3, lowPassEnvLoop), gate);

  lowPassCutoffLFO1 = lfo1.linlin(-1, 1, lowPassCutoffLFO1BottomRatio, lowPassCutoffLFO1TopRatio);
  lowPassCutoffLFO2 = lfo2.linlin(-1, 1, lowPassCutoffLFO2BottomRatio, lowPassCutoffLFO2TopRatio);
  thislowPassCutoff = lowPassCutoff * lowPassEnv *
  lowPassCutoffLFO1 * lowPassCutoffLFO2;

  lowPassResLFO1 = lfo1.linlin(-1, 1, lowPassResLFO1Bottom, lowPassResLFO1Top);
  lowPassResLFO2 = lfo2.linlin(-1, 1, lowPassResLFO2Bottom, lowPassResLFO2Top);
  thislowPassRes = lowPassRes + lowPassResLFO1 + lowPassResLFO2;

  lowPass = DFM1.ar(highPass, thislowPassCutoff, thislowPassRes, 1, 0);

  ampLFO1 = lfo1.linlin(-1, 1, ampLFO1Bottom, ampLFO1Top);
  ampLFO2 = lfo2.linlin(-1, 1, ampLFO2Bottom, ampLFO2Top);
  ampEnv = EnvGen.kr(Env.new(
    [0, 0, 1, sustainLevel, 0], [0, attackTime, decayTime, releaseTime], 4, 3), gate + Impulse.kr(0),
    doneAction: 2);
  ampEnv = ampEnv * ampLFO1 * ampLFO2;

  panningLFO1 = lfo1.linlin(-1, 1, panLFO1Bottom, panLFO1Top);
  panningLFO2 = lfo2.linlin(-1, 1, panLFO2Bottom, panLFO2Top);
  panning = Pan2.ar(lowPass * ampEnv, (pan + panningLFO1 + panningLFO2).clip(-1, 1));

  sig = panning * amp;

  Out.ar(outBus, sig);
}).add;

*/