
Base {

  var midiInPort, midiOutPort;
  var noteOnFuncArray, noteOffFuncArray, controlFuncArray;
  var <pageDict, <activePage, activePageKey;
  var colorArray;

  *new { ^super.new.prInit }

  prInit {
    this.prInitMIDI;
    this.prMakeResponders;
    this.prMakeColorArray;
    this.prMakePageDictionary;
  }

  prInitMIDI {
    MIDIIn.connectAll;
    midiInPort = MIDIIn.findPort("Base", "Controls");
    midiOutPort = MIDIOut.newByName("Base", "Controls");
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

}