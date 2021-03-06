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

  var <pageDict, <activePage, <activePageKey, <storageDict, <previousPage;

  var <activeSequencer1Bank = 0, <activeSequencer2Bank = 0, <activeDrumBank = 0, <activeControlBank = 0;

  var <sequencer1Dict, <sequencer2Dict, <drumSequencerDict, <syncSequencerDict;
  var <sequencerClock, <tempo, <beats;

  var sequencer1ButtonFuncArray, sequencer2ButtonFuncArray, drumButtonFuncArray, controlButtonFuncArray;
  var <controlEncoderFuncArray;

  *new {
    |
    deviceName = "Arturia BeatStep Pro", portName = "Arturia BeatStepPro",
    sequencer1Chan = 1, sequencer2Chan = 2, drumChan = 10, controlChan = 3
    |
    ^super.new.prInit(deviceName, portName, sequencer1Chan, sequencer2Chan, drumChan, controlChan);
  }

  prInit {
    |
    deviceName = "Arturia BeatStep Pro", portName = "Arturia BeatStepPro",
    sequencer1Chan = 1, sequencer2Chan = 2, drumChan = 10, controlChan = 3
    |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      this.prInitMIDI(deviceName, portName);
      //// MIDI Section:
      //this.prSetSequencerChannels(sequencer1Chan, sequencer2Chan, drumChan, controlChan);
      sequencer1Channel = 0;
      sequencer2Channel = 1;
      controlChannel = 2;
      drumChannel = 9;

      this.prMakeResponders;
      this.prMakePageDictionary;

      //// Sequences:
      sequencer1Dict = IdentityDictionary.new;
      sequencer2Dict = IdentityDictionary.new;
      drumSequencerDict = IdentityDictionary.new;
      syncSequencerDict = IdentityDictionary.new;
      sequencerClock = TempoClock.new;
      server.sync;

      //this.prMakeSyncSequence;

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
    sequencer1ButtonFuncArray = Array.fill2D(2, 84, { nil });
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
    sequencer2ButtonFuncArray = Array.fill2D(2, 84, { nil });
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
    controlEncoderFuncArray = Array.fill(16, { nil });
    16.do({ | num |
      controlEncoderFuncArray[num] = MIDIFunc({ }, num + 16, controlChannel, \control, midiInPort.uid).fix;
    });
  }

  prFreeControlResponders { controlEncoderFuncArray.do({ | f | f.free; }); }


  ///// this won't work until it also re-writes all of the MIDI funcs
  /*
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

  */

  /////// pages:
  prMakePageDictionary {
    pageDict = IdentityDictionary.new;
    this.makePage('main');
    activePage = pageDict['main'];
    this.setPage('main');
  }

  makePage { | name = 'newPage' |
    pageDict[name] = BeatstepPro_Page.new;
  }


  setPage { | name = 'page' |
    activePage.offLoadFunctionDict.do({ | func | func.value; });

    previousPage = activePageKey;
    activePageKey = name;
    activePage = pageDict[activePageKey];

    ///// load all functions onto page:
    this.prSetAllSequencer1ButtonFuncs;
    this.prSetAllSequencer2ButtonFuncs;
    this.prSetAllDrumButtonFuncs;
    this.prSetAllControlEncoderFuncs;

    // page load function:
    activePage.loadFunctionDict.do({ | func | func.value; });
  }
}

