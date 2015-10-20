/*
Wednesday, September 30th 2015
SaturSynth Ableton Ctrl.scd
script for piping MIDI into the SaturSynth to control it from Ableton

THIS SYNTH RESPONDS ON MIDI CHANNEL 1 (0 IN SC-TALK)
*/

var midiArray, midiInPort;
var vol, cutoff, attack, decay, sustain, release;
var tremRate, tremDepth;

// loads synth:
//~saturSynth = SaturSynth.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend,
  //relGroup: ~prm.procGroup, addAction: \addToHead);

~saturSynth = SaturSynth.new(0);

MIDIIn.connectAll;
//midiInPort = MIDIIn.findPort("iConnectAudio4+", "USB2");
//midiInPort = MIDIIn.findPort("IAC Driver", "Bus 1");
midiInPort = nil;

// MIDI Control:

~midiArray = Array.fill(127, { | i |
  MIDIFunc.noteOn({ | vel, note |
    var freq = note.midicps;
    var db = vel.ccdbfs(-27, 0);
    ~saturSynth.playNote(freq, db);
  }, i, 0, midiInPort);
  MIDIFunc.noteOff({ | vel, note |
    var freq = note.midicps;
    ~saturSynth.releaseNote(freq);
  }, i, 0, midiInPort);
});

// filterCutoff, attackTime, decayTime, sustainLevel, releaseTime;
// trem rate, trem depth

// Mixer Vol:
vol = MIDIFunc.cc({ | val |
  var volume = val.ccdbfs(-70, 0);
  ~saturSynth.mixer.setVol(volume);
}, 120, 0, midiInPort);

// filter cutoff (1):
cutoff = MIDIFunc.cc({ | val |
  var cutoff = val.linlin(0, 127, 20, 1000);
  ~saturSynth.setFilterCutoff(cutoff);
}, 127, 0, midiInPort);

// attack time (2):
attack = MIDIFunc.cc({ | val |
  var attack = val.linlin(0, 127, 0.05, 5);
  ~saturSynth.setAttackTime(attack);
}, 126, 0, midiInPort);

// decay time (3):
decay = MIDIFunc.cc({ | val |
  var decay = val.linlin(0, 127, 0, 3);
  ~saturSynth.setDecayTime(decay);
}, 125, 0, midiInPort);

// sustain level (4):
sustain = MIDIFunc.cc({ | val |
  var sustain = val.linlin(0, 127, 0, 1);
  ~saturSynth.setSustainLevel(sustain);
}, 124, 0, midiInPort);

// release time (5):
release = MIDIFunc.cc({ | val |
  var release = val.linlin(0, 127, 0.0, 10.0);
  release.postln;
  val.postln;
  ~saturSynth.setReleaseTime(release);
}, 119, 0, midiInPort);

// trem rate (6):
tremRate = MIDIFunc.cc({ | val |
  var rate = val.linlin(0, 127, 0, 18);
  ~saturSynth.setTremoloRate(rate);
}, 122, 0, midiInPort);

// trem depth (7):
tremDepth = MIDIFunc.cc({ | val |
  var depth = val.linlin(0, 127, 0, 1);
  ~saturSynth.setTremoloDepth(depth);
}, 121, 0, midiInPort);