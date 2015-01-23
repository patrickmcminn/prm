/*
Friday, January 23rd 2015
EnvelopeTrigger.sc
prm
plane from Sydney to Melbourne
*/

EnvelopeTrigger : IM_Processor {

  var <isLoaded;
  var server;
  var nilBus;
  var synth;

  *newMono {
    |
    outBus = 0, attackTime = 0.05, decayTime = 1, sustainLevel = 1, releaseTime = 0.05, threshold = 0.05,
    triggerBus = nil, envelopeBus = nil, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    feedback = 'false', relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitMono(
      attackTime, decayTime, sustainLevel, releaseTime, threshold, triggerBus, envelopeBus);
  }


  *newStereo {
    |
    outBus = 0, attackTime = 0.05, decayTime = 1, sustainLevel = 1, releaseTime = 0.05, threshold = 0.05,
    triggerBus = nil, envelopeBus = nil, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    feedback = 'false', relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(2, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitStereo(
      attackTime, decayTime, sustainLevel, releaseTime, threshold, triggerBus, envelopeBus);}

  prInitMono {
     |
    attackTime = 0.05, decayTime = 1, sustainLevel = 1, releaseTime = 0.05, threshold = 0.05,
    triggerBus = nil, envelopeBus = nil
    |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      nilBus = Bus.audio(server, 1);
      server.sync;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      if( triggerBus == nil, { triggerBus = nilBus });
      if( envelopeBus == nil, { envelopeBus = nilBus });

      synth = Synth(\prm_EnvelopeTrigger_mono,
        [\attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel, \releaseTime, releaseTime,
          \triggerBus, triggerBus, \envelopeBus, envelopeBus],
        group, 'addToHead');

      while({ try { synth } == nil }, { 0.001.wait; });
      isLoaded = true;
    };
  }

  prInitStereo {
    |
    attackTime = 0.05, decayTime = 1, sustainLevel = 1, releaseTime = 0.05, threshold = 0.05,
    triggerBus = nil, envelopeBus = nil
    |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      nilBus = Bus.audio(server, 1);
      server.sync;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      if( triggerBus == nil, { triggerBus = nilBus });
      if( envelopeBus == nil, { envelopeBus = nilBus });

      synth = Synth(\prm_EnvelopeTrigger_Stereo,
        [\attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel, \releaseTime, releaseTime,
          \triggerBus, triggerBus, \envelopeBus, envelopeBus],
        group, 'addToHead');

      while({ try { synth } == nil }, { 0.001.wait; });
      isLoaded = true;
    };
  }

  prAddSynthDefs {
    SynthDef(\prm_EnvelopeTrigger_mono, {
      |
      attackTime = 0.05, decayTime = 1, sustainLevel = 1, releaseTime = 0.05,
      inBus = 0, outBus = 0, amp = 1, trigBus = 0, envBus = 0,
      curve = -4,
      trackFall = 0.2, slowLag = 0.2, fastLag = 0.01, fastMul = 0.5, thresh = 0.05, minDur = 0.1
      |

      var input, onsetDetect, envelope, sig;

      input = In.ar(inBus, 1);
      onsetDetect = Coyote.kr(input, trackFall, slowLag, fastLag, fastMul, thresh, minDur);

      envelope = EnvGen.kr(Env.new([0, 0, 1, sustainLevel, 0], [0, attackTime, decayTime, releaseTime], curve), onsetDetect);
      sig =  input * onsetDetect;
      sig = sig * amp;
      Out.ar(outBus, sig);
      Out.kr(trigBus, onsetDetect);
      Out.kr(envBus, envelope);
    }, [0.05, 0.05, 0.05, 0.50]).add;

    SynthDef(\prm_EnvelopeTrigger_stereo, {
      |
      attackTime = 0.05, decayTime = 1, sustainLevel = 1, releaseTime = 0.05,
      inBus = 0, outBus = 0, amp = 1, trigBus = 0, envBus = 0,
      curve = -4,
      trackFall = 0.2, slowLag = 0.2, fastLag = 0.01, fastMul = 0.5, thresh = 0.05, minDur = 0.1
      |

      var input, onsetDetect, envelope, sig;

      input = In.ar(inBus, 2);
      onsetDetect = Coyote.kr(input, trackFall, slowLag, fastLag, fastMul, thresh, minDur);

      envelope = EnvGen.kr(Env.new([0, 0, 1, sustainLevel, 0], [0, attackTime, decayTime, releaseTime], curve), onsetDetect);
      sig =  input * onsetDetect;
      sig = sig * amp;
      Out.ar(outBus, sig);
      Out.kr(trigBus, onsetDetect);
      Out.kr(envBus, envelope);
    }, [0.05, 0.05, 0.05, 0.50]).add;
  }

  //////// public methods:

  free {
    synth.free;
    nilBus.free;

    synth = nil;
    nilBus = nil;

    this.freeProcessor;
  }

  setAttackTime { | time = 0.05 | synth.set(\attackTime, time); }
  setDecayTime { | time = 1 | synth.set(\decayTime, time); }
  setSustainLevel { | level = 1 | synth.set(\sustainLevel, level); }
  setReleaseTime { | time = 0.05 | synth.set(\releaseTime, time); }
  setEnvelopeCurve { | curve = -4 | synth.set(\curve, curve); }

  setThreshold { | thresh = 0.05 | synth.set(\thresh, thresh); }

  //// deeper paramaters:
  setTrackFall { | trackFall = 0.2 | synth.set(\trackFall, trackFall); }
  setSlowLag { | slowLag = 0.2 | synth.set(\slowLag, slowLag); }
  setFastLag { | fastLag = 0.01 | synth.set(\fastLag, fastLag); }
  setFastMul { | fastMul = 0.5 | synth.set(\fastMul, fastMul); }
  setMinDur { | minDur = 0.1 | synth.set(\minDur, minDur); }

}