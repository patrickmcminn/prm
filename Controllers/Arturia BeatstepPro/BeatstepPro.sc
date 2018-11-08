/*
Tuesday, July 31st 2018
BeatstepPro.sc
prm

class for interfacing with the
Arturia Beatstep Pro
*/

BeatstepPro  {

  var <isLoaded, server;
  var midiInPort, midiOutPort;
  var <sequencer1Channel, <sequencer2Channel, <drumChannel, <controlChannel;

  var <sequencer1Dict, <sequencer2Dict, <drumSequencerDict;
  var <sequencerClock, <tempo, <beats;

  var sequencer1ButtonFuncArray, sequencer2ButtonFuncArray, drumButtonFuncArray, controlButtonFuncArray;
  var sequencer1EncoderFuncArray, sequencer2EncoderFuncArray, drumEncoderFuncArray, controlEncoderFuncArray;

  *new {
    |
    deviceName = "Arturia BeatStep Pro", portName = "Arturia BeatStep Pro",
    sequencer1Chan = 1, sequencer2Chan = 2, drumChan = 10, controlChan = 3
    |
    ^super.new.prInit(deviceName, portName, sequencer1Chan, sequencer2Chan, drumChan, controlChan);
  }

  prInit {
    |
    deviceName = "Arturia BeatStep Pro", portName = "Arturia BeatStep Pro",
    sequencer1Chan = 1, sequencer2Chan = 2, drumChan = 10, controlChan = 3
    |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      this.prInitMIDI(deviceName, portName);
      //// MIDI Section:
      this.prSetSequencerChannels(sequencer1Chan, sequencer2Chan, drumChan, controlChan);
      this.prMakeResponders;
      this.prMakePageDictionary;

      //// Sequences:
      sequencer1Dict = IdentityDictionary.new;
      sequencer2Dict = IdentityDictionary.new;
      drumSequencerDict = IdentityDictionary.new;
      sequencerClock = TempoClock.new;
      server.sync;

      this.prMakeSyncSequence;

      isLoaded = true;
    }
  }

  prInitMIDI { | deviceName, portName |
    MIDIIn.connectAll;
    midiInPort = MIDIIn.findPort(deviceName, portName);
    midiOutPort = MIDIOut.newByName(deviceName, portName);
    midiOutPort.latency = 0;
  }

  prMakeResponders {
    this.prMakeNoteResponders;
    this.prMakeControlResponders;
  }

  prFreeResponders {
    this.prFreeNoteResponders;
    this.prFreeControlResponders;
  }

  prMakeNoteResponders {
    this.prMakeSequencer1ButtonFuncArray;
    this.prMakeSequencer2ButtonFuncArray;
    this.prMakeDrumButtonFuncArray;
    this.prMakeControlButtonFuncArray;
  }

  prFreeNoteResponders {
    this.prFreeSequencer1ButtonFuncArray;
    this.prFreeSequencer2ButtonFuncArray;
    this.prFreeDrumButtonFuncArray;
    this.prFreeControlButtonFuncArray;
  }

  prMakeSequencer1ButtonFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    sequencer1ButtonFuncArray = Array.fill2D(2, 128, { nil });
    84.do({ | num |
      sequencer1ButtonFuncArray[0][num] = MIDIFunc({ }, num + 24, sequencer1Channel, \noteOn, midiInPort.uid).fix;
    });
    84.do({ | num |
      sequencer1ButtonFuncArray[1][num] = MIDIFunc({ }, num + 24, sequencer1Channel, \noteOff, midiInPort.uid).fix;
    });
  }

  prFreeSequencer1ButtonFuncArray { sequencer1ButtonFuncArray.do({ | f | f.free; }); }

  prMakeSequencer2ButtonFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    sequencer2ButtonFuncArray = Array.fill2D(2, 128, { nil });
    84.do({ | num |
      sequencer2ButtonFuncArray[0][num] = MIDIFunc({ }, num + 24, sequencer2Channel, \noteOn, midiInPort.uid).fix;
    });
    84.do({ | num |
      sequencer2ButtonFuncArray[1][num] = MIDIFunc({ }, num + 24, sequencer2Channel, \noteOff, midiInPort.uid).fix;
    });
  }

  prFreeSequencer2ButtonFuncArray { sequencer2ButtonFuncArray.do({ | f | f.free; }); }

  prMakeDrumButtonFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    drumButtonFuncArray = Array.fill2D(2, 16, { nil });
    16.do({ | num |
      drumButtonFuncArray[0][num] = MIDIFunc({ }, num+36, drumChannel, \noteOn, midiInPort.uid).fix;
    });
    16.do({ | num |
      drumButtonFuncArray[1][num] = MIDIFunc({ }, num+36, drumChannel, \noteOff, midiInPort.uid).fix;
    });
  }

  prFreeDrumButtonFuncArray { drumButtonFuncArray.do({ | f | f.free; }); }

  prMakeControlButtonFuncArray {
    // slot 0 is for note on funcs:
    // slot 1 is for note off funcs:
    controlButtonFuncArray = Array.fill2D(2, 16, { nil });
    16.do({ | num |
      controlButtonFuncArray[0][num] = MIDIFunc({ }, num+36, controlChannel, \noteOn, midiInPort.uid).fix;
    });
    16.do({ | num |
      controlButtonFuncArray[1][num] = MIDIFunc({ }, num+36, controlChannel, \noteOff, midiInPort.uid).fix;
    });
  }

  prFreeControlButtonFuncArray { controlButtonFuncArray.do({ | f | f.free; }); }

  prMakeControlResponders {
    //this.prMakeSequencer1EncoderFuncArray;
    //this.prMakeSequencer2EncoderFuncArray;
    //this.prMakeDrumEncoderFuncArray;
    this.prMakeControlEncoderFuncArray;
  }

  prFreeControlResponders {
    //this.prFreeSequencer1EncoderFuncArray;
    //this.prFreeSequencer2EncoderFuncArray;
    //this.prFreeDrumEncoderFuncArray;
    this.prFreeControlEncoderFuncArray;
  }

  prMakeControlEncoderFuncArray {
    controlEncoderFuncArray = Array.fill(16, { nil });
    controlEncoderFuncArray.do({ | num |
      controlEncoderFuncArray[num] = MIDIFunc({ }, num + 16, controlChannel, \control, midiInPort.uid).fix;
    });
  }

  prFreeControlEncoderFuncArray { controlEncoderFuncArray.do({ | f | f.free; }); }

  prSetSequencerChannels { | seq1Chan = 1, seq2Chan = 2, drumChan = 10, controlChan = 3 |
    this.setSequencer1Channel(seq1Chan);
    this.setSequencer2Channel(seq2Chan);
    this.setDrumChannel(drumChan);
    this.setControlChannel(controlChan);
  }

  setSequencer1Channel { | channel = 1 |
    var midiChan = channel-1;
    sequencer1Channel = midiChan;
  }
  setSequencer2Channel { | channel = 2 |
    var midiChan = channel-1;
    sequencer2Channel = midiChan;
  }
  setDrumChannel  {| channel = 10 |
    var midiChan = channel-1;
    drumChannel = midiChan;
  }
  setControlChannel { | channel = 3 |
    var midiChan = channel-1;
    controlChannel = midiChan;
  }



}

