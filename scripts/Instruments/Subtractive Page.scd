/*
Tuesday, November 3rd 2015
Subtractive Page.scd
prm
*/

var noteOnFuncArray, noteOffFuncArray, inPort;

// make sure to load up the subtractive:
/*
~subtractive = Subtractive.new(~prm.submixB, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend,
  relGroup: ~prm.procGroup, addAction: \addToHead);
*/

// keyboard:
inPort = MIDIIn.findPort("iConnectAudio4+", "DIN");
noteOnFuncArray = 127.do({ | note |
  MIDIFunc.noteOn({| vel |
    ~subtractive.playNote(note.midicps, vel.ccdbfs(-20, 0));
  }, note, nil, inPort.uid);
});
noteOffFuncArray = 127.do({ | note |
  MIDIFunc.noteOff({ ~subtractive.releaseNote(note.midicps);
  }, note, nil, inPort.uid);
});

/*
MIDIIn.connectAll;
~inPort = MIDIIn.findPort("LPK25", "LPK25");
127.do({ | note |
  MIDIFunc.noteOn({ | vel |
    var amp = vel.linlin(0, 127, 0, 1);
    ~sub.playNote(note.midicps);
  }, note, nil, ~inPort.uid);
  MIDIFunc.noteOff({
    ~sub.releaseNote(note.midicps);
  }, note, nil, ~inPort.uid);
});
*/

