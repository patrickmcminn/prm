/*
Tuesday, April 24th 2018
Docile_Moog.sc
prm

to compress the world to a docile state
*/

Docile_Moog : IM_Module {

  var <isLoaded;
  var server;

  var arpMIDI, revMIDI, midiOut;

  var <synth, <granulator;

  var arpIsPlaying, arpRevIsPlaying;

  *new { | inBus = 3, outBus = 0, moogDeviceName, moogPortName, relGroup = nil, addAction = 'addToHead' |
     ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(inBus, moogDeviceName, moogPortName);
  }

  prInit { | inBus, moogDeviceName, moogPortName |
    server = Server.default;
    server.waitForBoot {
      var path1, path2;

      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      path1 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/to compress the world to a docile state/samples/MOOG/Arp.mid";
      path2 = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/to compress the world to a docile state/samples/MOOG/ArpRev.mid";

      arpIsPlaying = false;
      arpRevIsPlaying = false;

      arpMIDI = SimpleMIDIFile.read(path1);
      revMIDI = SimpleMIDIFile.read(path2);

      server.sync;

      arpMIDI.tempo = 142;
      revMIDI.tempo = 142;

      server.sync;

      granulator = GranularDelay.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      synth = Mother32.new(inBus, granulator.inBus, moogDeviceName, moogPortName, relGroup: group, addAction: \addToHead);
      while({ try { synth.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      midiOut = MIDIOut.newByName(moogDeviceName, moogPortName);

      server.sync;

      this.prInitializeParameters;
    }
  }

  prInitializeParameters {
    granulator.setGranulatorCrossfade(0.9);
    granulator.setDelayMix(0.9);
    granulator.setDelayTime(0.635);
    granulator.setFeedback(0.8);
    granulator.setGrainDur(0.477962, 0.477962);
    granulator.setTrigRate(60);
  }

  //////// public functions:

  free {
    synth.free;
    granulator.free;
    this.freeModule;
    isLoaded = false;
  }

  playArp {
    var pattern;
    pattern = Pmul(\sustain, 0.9, arpMIDI.p <> (type: \midi, midiout: midiOut, chan: 0)).play;
    arpIsPlaying = true;
  }

  playArpRev {
    var pattern;
    pattern = Pmul(\sustain, 0.9, revMIDI.p <> (type: \midi, midiout: midiOut, chan: 0)).play;
    arpRevIsPlaying = true;
  }
}