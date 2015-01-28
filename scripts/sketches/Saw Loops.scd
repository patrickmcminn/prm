SynthDef(\filterSaw, {
 |
  freq = 220, cutoff = 10000, outBus = 0, amp = 1, waveLossAmount = 0,
  gate = 1, attackTime = 0.05, decayTime = 1, sustainLevel = 0.8, releaseTime = 1,
  pan = 0
  |
  var saw, filter, envelope, sig;
  saw = Saw.ar(freq);
  filter = RLPF.ar(saw, cutoff, 1);
  envelope = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4), gate, doneAction: 2);
  sig = filter * envelope;
  sig = WaveLoss.ar(sig, waveLossAmount, 100, 2);
  sig = sig * amp;
  sig = Pan2.ar(sig, pan);
  Out.ar(outBus, sig);
}, [0.05, 0.1]).add;

~prm = AudioSystem.new(2);

Pbindef(\saws,
  \instrument, \filterSaw,
  \group, ~prm.procGroup,
  \addAction, \addToHead,
  \outBus, ~prm.submixA,
  \legato, 1,
  \note, Prand([0, 2, 4, 5, 7, 9, 11], inf),
  \octave, Prand([4, 5, 6], inf),
  \dur, Pbrown(0.05, 0.3, 0.125, inf),
  \attackTime, Pwhite(1, 3, inf),
  \decayTime, 0,
  \sustainLevel, 0,
  \releaseTime, 0,
  \amp, 0.8,
  \cutoff, Pwhite(500, 6000),
  \waveLossAmount, Pwhite(0, 30, inf)
);

Pbindef(\saws).play;
Pbindef(\saws).stop;
~prm.submixerA.playLoop;
~prm.submixerA.loop;
~prm.submixerA.stopLoop;
~prm.submixerA.clearLoop;
~prm.submixerA.setLoopDivison(6);
~prm.submixerA.setLoopPosition(0.1);
~prm.submixerA.setLoopRate(0.75);

~prm.submixerA.eq.setLowGain(0);
~prm.submixerA.eq.setLowRQ(0.7);
~prm.submixerA.eq.setHighGain(0);
~prm.submixerA.eq.setHighFreq(1800);
~prm.submixerA.eq.setPeak1Gain(0);
~prm.submixerA.eq.setPeak1Freq(900);
s.meter;
s.makeWindow;

~prm.submixerA.mixer.setVol(0);
~prm.submixerA.mixer.setSendVol(1, 6);
~prm.submixerA.mixer.tglMute;