/*
Tuesday, July 7th 2015
Connections.sc
prm
*/

Connections : Song {

  var <isLoaded, server;
  var <noteRecord, <inlet;

  var <airSputters, <droner, <bass;
  var <trumpetGran, <chords;

  var airSputtersInput, dronerInput;
  var noteRecordInput, <trumpetGranInput;

  var <clock;

  *new {
    | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(7, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      clock = TempoClock.new;
      server.sync;
      clock.tempo = 75/60;

      noteRecord = Connections_NoteRecord.new(group, \addToHead);
      while({ try { noteRecord.isLoaded } != true }, { 0.001.wait; });
      noteRecordInput = IM_HardwareIn.new(0, noteRecord.inBus, group, \addToHead);
      while({ try { noteRecordInput.isLoaded } != true }, { 0.001.wait; });

      server.sync;
      mixer.setMasterVol(-9);
      isLoaded = true;
    }
  }

  /////// public functions:

  free {
    noteRecord.free;
    try { inlet.free; };
    try { airSputters.free; };
    try { droner.free; };
    try { bass.free; };
    try { trumpetGran.free; };
    try { chords.free; };
    this.freeSong;
  }

  toggleLoadAirSputters {
    if( try { airSputters.isLoaded } != true,
      {
       r {
          airSputters = Connections_AirSputters.new(mixer.chanStereo(0), clock, group, \addToHead);
          while({ try { airSputters.isLoaded } != true }, { 0.001.wait; });
          airSputtersInput = IM_HardwareIn.new(0, airSputters.inBus, group, \addToHead);
        }.play;
      },
        {
          airSputtersInput.free;
          airSputters.free;
        }
    );
  }

  toggleLoadDroner {
    if( try { droner.isLoaded } != true,
      {
        r{
          droner = Droner.newMono(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
          while({ try { droner.isLoaded } != true }, { 0.001.wait; });
          dronerInput = IM_HardwareIn.new(0, droner.inBus, group, \addToHead);
        }.play;
      },
      {
        dronerInput.free;
        droner.free;
      }
    );
  }

  toggleLoadBass {
    if( try { bass.isLoaded } != true,
      { bass = Connections_Bassline.new(mixer.chanStereo(2), noteRecord.noteBufferArray, group, \addToHead); },
      { bass.free; }
    );
  }

  toggleLoadTrumpetGran {
    if( try { trumpetGran.isLoaded } != true,
      {
        r{
          trumpetGran = Connections_TrumpetGran.new(mixer.chanStereo(3), group, \addToHead);
          while({ try { trumpetGran.isLoaded } != true }, { 0.001.wait; });
          trumpetGranInput = IM_HardwareIn.new(0, trumpetGran.inBus, group, \addToHead);
        }.play;
      },
      {
        trumpetGranInput.free;
        trumpetGran.free;
      }
    );
  }

  toggleLoadInlet {
    if( try { inlet.isLoaded } != true,
      {
        r {
          inlet = Connections_Inlet.new(mixer.chanStereo(4), noteRecord.noteBufferArray,
            noteRecord.cascadeBufferArray,  group, \addToHead);
          while({ try { inlet.isLoaded } != true }, { 0.001.wait; });
          mixer.setSendVol(4, 0, -6);
          "Inlet Finally Loaded".postln;
        }.play;
      },
      { inlet.free; }
    );
  }

  toggleLoadChords {
    if( try { chords.isLoaded } != true,
      {
        chords = Connections_Chords.new(mixer.chanStereo(5), noteRecord.chordBufferArray,
        group, \addToHead);
      },
      { chords.free;}
    );
  }
}