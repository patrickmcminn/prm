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
  var spec = [0.05, 10, 2].asSpec;
  var attack = spec.map(val.linlin(0, 127, 0, 1));
  ~subtractive.setAttackTime(attack);
}, mainBank, page);
// Decay Time:
~ohm.setLeftSliderFunc(1, { | val |
  var spec = [0, 3, 2].asSpec;
  var decay = spec.map(val.linlin(0, 127, 0, 1));
  ~subtractive.setDecayTime(decay);
}, mainBank, page);
// Sustain Level:
~ohm.setLeftSliderFunc(2, { | val |
  var spec = [0, 1, 1.5].asSpec;
  var sustain = spec.map(val.linlin(0, 127, 0, 1));
  ~subtractive.setSustainLevel(sustain);
}, mainBank, page);
// Release Time:
~ohm.setLeftSliderFunc(3, { | val |
  var spec = [0.05, 7, 2].asSpec;
  var release = spec.map(val.linlin(0, 127, 0, 1));
  ~subtractive.setReleaseTime(release);
}, mainBank, page);

//////// Left Knobs:


///// row 2:
// Filter Cutoff:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var cutoff = val.linexp(0, 127, 200, 18000);
  ~subtractive.setFilterCutoff(cutoff);
}, mainBank, page);
// Filter Resonance:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var res = val.linlin(0, 127, 0, 1.2);
  ~subtractive.setFilterRes(res);
}, mainBank, page);
// Filter Cutoff LFO1:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var bottom = val.linlin(0, 127, 1, 0.125);
  var top = val.linlin(0, 127, 1, 2);
  ~subtractive.setFilterCutoffLFO1BottomRatio(bottom);
  ~subtractive.setFilterCutoffLFO1TopRatio(top);
}, mainBank, page);
// Filter CutoffLFO2:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var bottom = val.linlin(0, 127, 1, 0.125);
  var top = val.linlin(0, 127, 1, 2);
  ~subtractive.setFilterCutoffLFO2BottomRatio(bottom);
  ~subtractive.setFilterCutoffLFO2TopRatio(top);
}, mainBank, page);

///// row 1:
// Osc 1 Waveform:
~ohm.setLeftKnobFunc(0, 1, { | val |
  var wave = val.linlin(0, 127, 0, 3);
  ~subtractive.setOsc1Waveform(wave);
}, mainBank, page);
// Osc 2 Waveform:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var wave = val.linlin(0, 127, 0, 3);
  ~subtractive.setOsc2Waveform(wave);
}, mainBank, page);
// LFO 1 Amp Mod:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var modNeg = val.linlin(0, 63, 0, 1);
  var modPos = val.linlin(64, 127, 1, 0);
  if( val < 64,
    {
      ~subtractive.setAmplitudeLFO1Top(modNeg);
      ~subtractive.setAmplitudeLFO1Bottom(1);
    },
    {
      ~subtractive.setAmplitudeLFO1Bottom(modPos);
      ~subtractive.setAmplitudeLFO1Top(1);
  });
}, mainBank, page);
// LFO 2 Amp Mod:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var modNeg = val.linlin(0, 63, 0, 1);
  var modPos = val.linlin(64, 127, 1, 0);
  if( val < 64,
    {
      ~subtractive.setAmplitudeLFO2Top(modNeg);
      ~subtractive.setAmplitudeLFO2Bottom(1);
    },
    {
      ~subtractive.setAmplitudeLFO2Bottom(modPos);
      ~subtractive.setAmplitudeLFO2Top(1);
  });
}, mainBank, page);


///// row 0:
// LFO 1 freq:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var spec = [0.1, 20, 2].asSpec;
  var freq = spec.map(val.linlin(0, 127, 0, 1));
  ~subtractive.setLFO1Freq(freq);
}, mainBank, page);
// LFO 1 Waveform:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var wave = val.linlin(0, 127, 0, 5);
  ~subtractive.setLFO1Waveform(wave);
}, mainBank, page);
// LFO 2 freq:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var spec = [0.1, 20, 2].asSpec;
  var freq = spec.map(val.linlin(0, 127, 0, 1));
  ~subtractive.setLFO2Freq(freq);
}, mainBank, page);
// LFO 2 Waveform:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var wave = val.linlin(0, 127, 0, 5);
  ~subtractive.setLFO2Waveform(wave);
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

