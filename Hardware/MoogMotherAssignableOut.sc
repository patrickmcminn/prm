/*
Sunday, January 17th 2016
MotherAssignableOut.sc
prm
*/


MoogMotherAssignableOut {

  var <isLoaded, server;
  var midiOutPort;
  var <ccOutput, <midiChannel, oscFunc;
  var <controlInBus, synth, <lfo;
  var <nilBus;
  var group;
  var <attackTime, <decayTime, <sustainLevel, <sustainTime, <releaseTime, <peakLevel;

  *new { | deviceName = nil, portName = nil, inBus = nil, ccOut = 1, midiChan = 1, relGroup = nil, addAction = 'addToTail' |
    ^super.new.prInit(deviceName, portName, ccOut);
  }

  prInit {  | deviceName = nil, portName = nil, inBus = nil, ccOut = 1, midiChan = 1, relGroup, addAction = 'addToTail' |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      // create groups and buses:
      group = Group.new(relGroup, addAction);
      nilBus = Bus.control;
      server.sync;
      nilBus.set(64);
      // initialize midi parameters:
      ccOutput = ccOut;
      midiChannel = (midiChan - 1);

      // set controlInBus variable:
      if( inBus != nil, { controlInBus = nilBus   }, { controlInBus = inBus });
      //controlInBus = nilBus;

      // initialize envelope parameters:
      attackTime = 0.05;
      decayTime = 0;
      sustainLevel = 1;
      sustainTime = 1;
      releaseTime = 0.05;
      peakLevel = 64;

      this.prAddSynthDefs;
      this.prInitMIDI(deviceName, portName);
      server.sync;

      lfo = LFO.new(rangeLow: 0, rangeHigh: 0, relGroup: group, addAction: \addToHead);
      while({ try { lfo.isLoaded != true } }, { 0.001.wait; });

      server.sync;

      synth = Synth(\prm_mother_krToOsc, [\inBus, controlInBus, \lfoInBus, lfo.outBus,
        \attackTime, attackTime, \decayTime, decayTime, \sustainLevel, sustainLevel,
        \sustainTime, sustainTime, \releaseTime, releaseTime],
        group, \addToHead);

      oscFunc = OSCFunc({ | msg |
        midiOutPort.control(midiChannel, ccOut, msg[3]);
      }, '/motherAssign', server.addr);

      server.sync;

      isLoaded = true;
    };
  }

  prAddSynthDefs {
    SynthDef(\prm_mother_krToOsc, {
      |
      inBus, lfoInBus, sampleRate = 60,
      attackTime = 0.05, decayTime = 0, sustainLevel = 1, sustainTime = 1, releaseTime = 0, peakLevel = 127,
      gate = 0, t_gate = 0
      |
      var input, lfoInput, sustainingEnvelope, envelope, sig;
      input = In.kr(inBus);
      lfoInput = In.kr(lfoInBus);
      sustainingEnvelope = EnvGen.kr(Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, peakLevel, -4), gate);
      envelope = EnvGen.kr(Env.linen(attackTime, sustainTime, releaseTime, peakLevel), t_gate);
      sig = input.lag(005) + lfoInput + sustainingEnvelope + envelope;
      SendReply.kr(Impulse.kr(sampleRate), '/motherAssign', sig);
    }).add;
  }

  prInitMIDI { | deviceName, portName |
    MIDIIn.connectAll;
    server.sync;
    midiOutPort = MIDIOut.newByName(deviceName, portName);
    midiOutPort.latency = 0;
  }

  //////// public functions:

  free {
    oscFunc.free;
    synth.free;
    group.free;
    nilBus.free;
  }

  setInBus { | inBus = nil |
    if( inBus != nil, { controlInBus = inBus }, { controlInBus = nilBus });
    synth.set(\inBus, controlInBus);
  }

  setStaticValue { | val = 0 |
    controlInBus.set(val);
  }

  triggerEnvelopeSustaining { synth.set(\gate, 1); }
  releaseEnvelopeSustaining { synth.set(\gate, 0); }
  triggerEnvelopeOneShot { synth.set(\t_gate, 1); }

  setAttackTime { | attack = 0.05 |
    attackTime = attack;
    synth.set(\attackTime, attackTime);
  }
  setDecayTime { | decay = 0 |
    decayTime = decay;
    synth.set(\decayTime, decayTime);
  }
  setSustainLevel { | sustain = 1.0 |
    sustainLevel = sustain;
    synth.set(\sustainLevel, sustainLevel);
  }
  setSustainTime { | sustain = 1 |
    sustainTime = sustain;
    synth.set(\sustainTime, sustainTime);
  }
  setReleaseTime { | release = 0.05 |
    releaseTime = release;
    synth.set(\releaseTime, releaseTime);
  }
  setPeakLevel { | peak = 127 |
    peakLevel = peak;
    synth.set(\peakLevel, peakLevel);
  }


}