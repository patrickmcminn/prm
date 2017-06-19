/*
Friday, June 16th 2017
FreezeSequencer.sc
prm
*/

FreezeSequencer : IM_Module {

  var <isLoaded, server;
  var <freeze;
  var <sequencerDict, <sequencerClock;
  var <tempo;
  var <envBus;

  *new { |outBus = 0,  send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      this.prAddSynthDef;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      freeze = IM_GrainFreeze.new(envBus, group, \addToHead);
      while({ try { freeze.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_FreezeSequencer_Envelope_Sustaining, {
      |
      inBus = 0, outBus = 0, amp = 1, gate = 1,
      attackTime = 0.01, decayTime = 0, sustainLevel = 1, releaseTime = 0.01
      |
      var input, env, sig;
      input = In.ar(inBus, 2);
      env = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, 1, -4, 0), gate, doneAction: 2);
      sig = input * env;
      Out.ar(outBus, sig);
    }).add;

  }

}