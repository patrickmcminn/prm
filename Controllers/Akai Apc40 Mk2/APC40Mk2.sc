/*
Monday, May 21st 2018
APC40Mk2.sc
prm
*/

APC40Mk2 {

  var midiInPort, midiOutPort;
  var noteOnFuncArray, noteOffFuncArray, controlFuncArray;
  var <pageDict, <activePage, <activePageKey, <storageDict, <previousPage;
  var colorArray;




  *new { | deviceName = "APC40 mkII", portName = "APC40 mkII" |
    ^super.new.prInit(deviceName, portName);
  }

  prInit { | deviceName = "APC40 mkII", portName = "APC40 mkII" |
    this.prInitMIDI(deviceName, portName);
    this.prMakeResponders;

  }

  prInitMIDI { | deviceName = "APC40 mkII", portName = "APC40 mkII" |
    MIDIIn.connectAll;
    midiInPort = MIDIIn.findPort(deviceName, portName);
    midiOutPort = MIDIOut.newByName(deviceName, portName);
    midiOutPort.latency = 0;

    /////// set to control mode:
    midiOutPort.sysex(Int8Array[0xf0, 0x47, 0x01, 0x29, 0x06, 0x60, 0x00, 0x04, 0x42, 0x01, 0x01, 0x01, 0xf7]);
  }

  prMakeResponders {
    this.prMakeNoteResponders;
    this.prMakeControlResponders;
  }

  prFreeResponders {
    this.prFreeNoteResponders;
    this.prFreeControlResponders;
  }


}