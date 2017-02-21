/*
Monday, January 25th 2016
Mother32.sc
prm
class to interface with the Mother32 hardware
*/

Mother32 : IM_Module {

  // assumes assignable out is in a CC transmit mode

  var <isLoaded, server;
  var <assignableOut;
  var <hardwareIn;
  var midiOutPort;
  var <midiChannel;

  *new { | hardwareInBus = 0, outBus = 0, deviceName = nil, portName = nil, midiChan = 1, assignableCC = 1,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(
      hardwareInBus, deviceName, portName, midiChan, assignableCC);
  }

  prInit { | hardwareInBus = 0, deviceName = nil, portName = nil, midiChan = 1, assignableCC = 1 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      midiChannel = (midiChan-1);
      this.prInitMIDI(deviceName, portName);
      server.sync;

      assignableOut = MoogMotherAssignableOut.new(deviceName, portName, nil, assignableCC, midiChannel, group, \addToTail);
      while({ try { assignableOut.isLoaded } != true }, { 0.001.wait; });

      hardwareIn = IM_HardwareIn.new(hardwareInBus, mixer.chanMono(0), group, \addToHead);
      while({ try { hardwareIn.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    }
  }

  prInitMIDI { | deviceName = nil, portName = nil |
    MIDIIn.connectAll;
    server.sync;
    midiOutPort = MIDIOut.newByName(deviceName, portName);
    midiOutPort.latency = 0;
  }

  //////// public functions:
  free {
    hardwareIn.free;
    assignableOut.free;
    this.freeModule;
  }

  playNote { | freq = 220 |
    midiOutPort.noteOn(midiChannel, freq.cpsmidi);
  }

  releaseNote { | freq = 220 |
    midiOutPort.noteOff(midiChannel, freq.cpsmidi);
  }
}