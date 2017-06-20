/*
Monday, June 19th 2017
Foundation_Moog.sc
prm
*/

Foundation_Moog : IM_Module {

  var server, <isLoaded;
  var <moog, <eq;
  var mainMidiFile, endMidiFile, midiOut;
  var <mainSequenceIsPlaying = false;
  var <endSequenceIsPlaying = false;

  *new { | inBus = 3, outBus = 0, moogDeviceName, moogPortName, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(inBus, moogDeviceName, moogPortName);
  }

  prInit { | inBus, moogDeviceName, moogPortName |
    server = Server.default;
    server.waitForBoot {
      var path1 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/BuiltOnAFlawedFoundation/samples/MoogMIDI.mid";
      var path2 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/BuiltOnAFlawedFoundation/samples/EndMelody.mid";

      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      mainMidiFile = SimpleMIDIFile.read(path1);
      endMidiFile = SimpleMIDIFile.read(path2);

      server.sync;
      mainMidiFile.tempo = 96;
      endMidiFile.tempo = 96;

      server.sync;

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      moog = Mother32.new(inBus, eq.inBus, moogDeviceName, moogPortName, relGroup: group, addAction: \addToHead);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      midiOut = MIDIOut.newByName(moogDeviceName, moogPortName);
      eq.setHighPassCutoff(1300);
      eq.setLowPassCutoff(3010);

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    midiOut.free;
    moog.free;
    this.freeModule;
    isLoaded = false;
  }

  playMainSection {
    var pattern;
    pattern = Pmul(\sustain, 0.9, mainMidiFile.p <> (type: \midi, midiout: midiOut, chan: 0)).play;
    mainSequenceIsPlaying = true;
  }

  playEndSection {
    var pattern;
    pattern = Pmul(\sustain, 0.9, endMidiFile.p <> (type: \midi, midiout: midiOut, chan: 0)).play;
    endSequenceIsPlaying = true;
  }

}