x = { | amp = 0, dur = 1, ad = 1  | HPF.ar(LPF.ar(Gendy3.ar(amp, dur, ad, 1, [220, 110, 55], 0.5, 0.5), 3500), 20) ! 2 }.play

x.set(\amp, 6);
x.set(\dur, 6);
x.set(\ad, 0.05);

SynthDef(\gendSynth, {

  var lfo2;
  var lfo1, lfo1FreqLFO, thisLFO1Freq, lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoSampleAndHold, lfoNoise;
  var lfo1AttackEnv, lfo1ReleaseEnv, lfo1AttackAndReleaseEnv;

  var freq, freqEnv, freqLFO1, freqLFO2;
  var gendyAmpLFO1, gendyAmpLFO2;
  var gendyAmpDist, gendyAmpDistLFO1, gendyAmpDistLFO2;
  var gendyDurDist, gendyDurDistLFO1, gendyDurDistLFO2;
  var gendyAmpDist, gendyAmpDistLFO1, gendyAmpDistLFO2;
  var gendyAdparam, gendyAdparamLFO1, gendyAdparamLFO2;
  var gendyDdparam, gendyDdparamLFO1, gendyDdparamLFO2;
  var gendyAmpScale, gendyAmpScaleLFO1, gendyAmpScaleLFO2;
  var gendyDurScale, gendyDurScaleLFO1, gendyDurScaleLFO2;
  var gendyKnum, gendyKnumLFO1, gendyKnumLFO2;
  var gendy;
  var lowPassFilter, highPassFilter;
  var envelope;
  var pan;
  var sig;

}).add;