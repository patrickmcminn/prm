/*
Friday, June 7th 2019
WhereTheBirds_NoiseSynth.sc
prm


lightly adapted from noiseSynth.scd
Friday, November 2nd, 2012
*/

WhereTheBirds_NoiseSynth : IM_Module {

  var <isLoaded, server;
  var <granulator, <lfo;

  var path, bufferArray;

  *new { | outBus, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      this.prAddSynthDef;

      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Instruments/NoiseSynth/";
      bufferArray = Array.fill(53, { | i |
        Buffer.read(server, path ++ "/Noise Synth/" ++ (i + 32) ++ ".wav"); });

      granulator = GranularDelay.new(mixer.chanStereo, group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitializeParameters;

      isLoaded = true;
    }
  }

  prAddSynthDef {

    SynthDef(\prm_birds_playBufStereo, {
      | bufName, out, rate = 1, loop = 0, startPos = 0, amp = 1,
      attack = 0.05, sustain = 1, release = 0.05, pan = 0 |
      var play, sig, env;
      play = PlayBuf.ar(1, bufName, rate, 1, startPos, loop);
      env = EnvGen.kr(Env.linen(attack, sustain, release), 1, doneAction: 2);
      sig =  env * play;
      sig = Pan2.ar(sig, pan);
      sig = sig * amp;
      sig = Out.ar(out, sig);
    }).add;
  }

  prInitializeParameters {
    granulator.setGranulatorCrossfade(1);
    granulator.setGrainDur(0.4, 0.6);
    granulator.setTrigRate(45);
    granulator.setDelayTime(1);
    granulator.setFeedback(0.6);
    granulator.setDelayMix(0.3);
  }
}