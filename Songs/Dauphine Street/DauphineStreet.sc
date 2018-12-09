/*
Sunday, July 5th 2015
DauphineStreet.sc
prm
*/


DauphineStreet : Song {

  var <isLoaded, server;
  var <trumpet, <synths, <accomp, <bass;
  var trumpetInput;
  var <tempoClock;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |

    ^super.new(4, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
}

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      tempoClock = TempoClock.new;
      server.sync;
      tempoClock.tempo = 2;

      trumpet = Dauphine_Wash.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

      trumpetInput = IM_HardwareIn.new(0, trumpet.inBus, group, 'addToHead');
      while({ try { trumpetInput.isLoaded } != true }, { 0.001.wait; });


      synths = Dauphine_Synths.new(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { synths.isLoaded } !=true }, { 0.001.wait; });


      accomp = Dauphine_SequenceSynth.new(mixer.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { accomp.isLoaded } != true }, { 0.001.wait; });


      bass = Dauphine_Bass.new(mixer.chanStereo(3), relGroup: group, addAction: \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });


      mixer.setVol(0, -15);

      mixer.setVol(1, -70);
      mixer.setSendVol(1, 0, -12);

      mixer.setVol(2, -70);
      mixer.setSendVol(2, 0, -16);

      mixer.setPreVol(3, 6);
      mixer.setSendVol(3, 0, -24);

      isLoaded = true;
    };
  }

  /////// public functions:

  free {
    trumpet.free;
    synths.free;
    accomp.free;
    bass.free;

    this.freeSong;
  }

}
