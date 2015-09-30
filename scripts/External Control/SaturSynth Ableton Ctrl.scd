/*
Wednesday, September 30th 2015
SaturSynth Ableton Ctrl.scd
script for piping MIDI into the SaturSynth to control it from Ableton
*/

var midiArray;

 loads synth:
//~saturSynth = SaturSynth.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend,
 // relGroup: ~prm.procGroup, addAction: \addToHead);

~saturSynth = SaturSynth.new(0);

MIDIIn.connectAll;

// MIDI Control:

~midiArray = Array.fill(127, { | i |
  MIDIFunc.noteOn({ | vel, note |
    var freq = note.midicps;
    var db = vel.ccdbfs(-27, 0);
    [vel, note].postln;
    ~saturSynth.playNote(freq, db);
  }, i, 0, MIDIEndPoint("iConnectAudio4+", "USB1"));
  MIDIFunc.noteOff({ | vel, note |
    var freq = note.midicps;
    ~saturSynth.releaseNote(freq);
  }, i, 0, MIDIEndPoint("iConnectAudio4+", "USB1"));
});
