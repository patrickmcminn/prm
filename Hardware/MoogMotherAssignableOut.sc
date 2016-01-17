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
      //if( inBus != nil, { controlInBus = inBus }, { controlInBus = nilBus });
      controlInBus = nilBus;

      this.prAddSynthDefs;
      this.prInitMIDI(deviceName, portName);
      server.sync;

      lfo = LFO.new(rangeLow: 0, rangeHigh: 0, relGroup: group, addAction: \addToHead);
      while({ try { lfo.isLoaded != true } }, { 0.001.wait; });

      server.sync;

      synth = Synth(\prm_mother_krToOsc, [\inBus, controlInBus, \lfoInBus, lfo.outBus], group, \addToHead);

      oscFunc = OSCFunc({ | msg |
        midiOutPort.control(midiChannel, ccOut, msg[3]);
      }, '/motherAssign', server.addr);

      server.sync;

      isLoaded = true;
    };
  }

  prAddSynthDefs {
    SynthDef(\prm_mother_krToOsc, {
      | inBus, lfoInBus, sampleRate = 60 |
      var input, lfoInput;
      input = In.kr(inBus);
      lfoInput = In.kr(lfoInBus);
      SendReply.kr(Impulse.kr(sampleRate), '/motherAssign', input.lag(0.05) + lfoInput);
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

  setValue { | val = 0 |
    controlInBus.set(val);
  }

}