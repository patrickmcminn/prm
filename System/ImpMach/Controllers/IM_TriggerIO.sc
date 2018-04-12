// This class does not cover everything the Trigger iO can do,
// it's just a quick and dirty thing for the 10 triggers.
// CONVERT THIS CLASS TO A GENERIC MIDI NOTE IN CLASS (so it can be used on zones in a keyboard, etc)
IM_TriggerIO {
  var midiPort;
  var midiFuncArray;


  // Pass the note numbers of each trigger, in order, as an array when creating an instance
  *new { |notes = #[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]|
    ^super.new.prInit(notes);
  }


  //////// PRIVATE FUNCTIONS
  prInit { |notes = #[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]|
    this.prInitMIDI;
    this.prMakeMIDIResponders(notes);
  }

  prInitMIDI {
    MIDIIn.connectAll;
    midiPort = MIDIIn.findPort("TriggerIO", "MIDI Out");
  }

  prMakeMIDIResponders { |notes = #[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]|
    midiFuncArray = this.prMakeMIDIFuncArray(notes);
  }

  // Flash TriggerIO to initialize setting
  prSysex {
  }

  // Create an array to store MIDIFuncs
  prMakeMIDIFuncArray { |notes = #[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]|
    ^Array.fill(notes.size, { |index|
      MIDIFunc(
        { |vel, note, chan, src| ("Trigger" + index + "velocity:" + vel).postln; },
        notes[index], nil, \noteOn, midiPort.uid
        ).fix;
    } );
  }

  /////// PUBLIC FUNCTIONS
  setFunc { |triggerNum = 0, func = nil|
    midiFuncArray[triggerNum].prFunc_(func);
  }

  reset {
    midiFuncArray.do { |item, index|
      this.setFunc(index, { |vel, note, chan, src|
        ("Trigger" + index + "velocity:" + vel).postln;
      });
    };
  }

  // NOT FINISHED
  free {
    midiFuncArray.do { |item, index|
      item.free;
    };
  }
}