/*
Tuesday, November 3rd 2015
Subtractive Page.scd
prm
*/

var page = 'subtractive';
var noteOnFuncArray, noteOffFuncArray, inPort;

var mainBank = 0;

// make sure to load up the subtractive:
/*
~subtractive = Subtractive.new(~prm.submixB, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend,
  relGroup: ~prm.procGroup, addAction: \addToHead);
*/

/*
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
*/

////////////////////
//////// Ohm: /////
///////////////////

/////// global:
~ohm.makePage(page);

~ohm.setPageLoadFunction({
  ~ohm.storageDict[\loadGlobalSubmixerFunc].value;
}, page);

// bring base on over:
~ohm.turnControlButtonPurple(2, 0, bank: mainBank, page: page);
~ohm.setControlButtonFunc(2, 0, { ~base.setPage(page); }, bank: mainBank, page: page);



/////////////////
// Main Bank: ///
/////////////////

//////// Left Sliders:

// Attack Time:
~ohm.setLeftSliderFunc(0, { | val |
  var spec = [0.05, 10, 4].asSpec;
  var attack = spec.map(val.linlin(0, 127, 0, 1));
  ~subtractive.setAttackTime(attack);
}, mainBank, page);
// Decay Time:
~ohm.setLeftSliderFunc(1, { | val |
  var spec = [0, 3, 4].asSpec;
  var decay = spec.map(val.linlin(0, 127, 0, 1));
  ~subtractive.setDecayTime(decay);
}, mainBank, page);
// Sustain Level:
~ohm.setLeftSliderFunc(2, { | val |
  var spec = [0, 1, 1].asSpec;
  var sustain = spec.map(val.linlin(0, 127, 0, 1));
  ~subtractive.setSustainLevel(sustain);
}, mainBank, page);
// Release Time:
~ohm.setLeftSliderFunc(3, { | val |
  var spec = [0.05, 9, 4].asSpec;
  var release = spec.map(val.linlin(0, 127, 0, 1));
  ~subtractive.setReleaseTime(release);
}, mainBank, page);




///////////////////////
//////// Base: ///////
//////////////////////


~base.makePage(page);



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

