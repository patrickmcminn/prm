/*
Monday, July 10th 2017
Hammond.cd
prm

shit emulation of a hammond organ, ok?
*/

Hammond : IM_Module {

  var <isLoaded;
  var server;

  var <subVol, <bassVol, <fundamentalVol;
  var <partial2Vol, <partial3Vol, <partial4Vol, <partial5Vol;
  var <partial6Vol, <partial7Vol, <partial8Vol;
  var <filterCutoff, <filterRes, <distortionAmount;
  var <attackTime, sustainLevel, <releaseTime, <pan;

  var <tremoloSpeed, <tremoloDepth;

  var noteDict;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 0, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      this.prAddSynthDef;

      server.sync;
      isLoaded = true;
    }
  }

  prAddSynthDef {

    SynthDef(\hammond, {
      | out = 0, freq = 220, amp = 0.5,
      subAmp = 0.1, bassAmp = 0.1, fundAmp = 1,
      partial2Amp = 0.25, partial3Amp = 0.125, partial4Amp = 0.1, partial5Amp = 0.09, partial6Amp = 0.08, partial8Amp = 0.05,
      cutoff = 20000, res = 0.1, dist = 0.1, noise = 0.0003,
      attackTime = 0.01, sustainLevel = 1, releaseTime = 0.01, gate = 1
      pan = 0, tremoloSpeed = 0, tremoloDepth = 0
      |

      var subOctave, bass, fund, octave, octaveFifth, twoOct, twoOctThird, twoOctFifth, threeOct;
      var sum, env, filter, tremolo, sig;

      subOctave = SinOsc.ar(freq/2) * subAmp;
      bass = SinOsc.ar(freq * (3/4)) * bassAmp;
      fund = SinOsc.ar(freq) * fundAmp;
      octave = SinOsc.ar(freq * 2) * partial2Amp;
      octaveFifth = SinOsc.ar(freq * 3) * partial3Amp;
      twoOct = SinOsc.ar(freq * 4) * partial4Amp;
      twoOctThird = SinOsc.ar(freq * 5) * partial5Amp;
      twoOctFifth = SinOsc.ar(freq * 6) * partial6Amp;
      threeOct = SinOsc.ar(freq * 8) * partial8Amp;

      sum = Mix.new([ subOctave, bass, fund, octave, octaveFifth, twoOct, twoOctThird, twoOctFifth, threeOct]);
      filter = DFM1.ar(sum, cutoff, res, dist, 0, noise);
      env = EnvGen.kr(Env.asr(attackTime, sustainLevel, releaseTime), gate, doneAction: 2);
      sig = filter * env;
      sig = sig * amp;
      sig = Pan2.ar(sig, pan);
      Out.ar(out, sig);
    }).add;

  }

}