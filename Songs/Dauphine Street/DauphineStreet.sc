/*
Sunday, July 5th 2015
DauphineStreet.sc
prm
*/


DauphineStreet : Song {

  var <isLoaded, server;
  var <trumpet, <synths, <accomp, <bass;
  var trumpetInput;

  *new { | mixAOutBus, mixBOutBus, mixCOutBus, send0Bus, send1Bus, send2Bus, send3Bus,
    relGroup = nil, addAction = 'addToHead' |

    ^super.new(mixAOutBus, 1, mixBOutBus, 2, mixCOutBus, 1, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixerC.isLoaded } != true }, { 0.001.wait; });

      trumpet = Dauphine_Wash.new(mixerA.chanStereo(0), group, \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

      trumpetInput = IM_HardwareIn.new(0, trumpet.inBus, group, 'addToHead');
      while({ try { trumpetInput.isLoaded } != true }, { 0.001.wait; });

      synths = Dauphine_Synths.new(mixerB.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { synths.isLoaded } !=true }, { 0.001.wait; });

      accomp = Dauphine_SequenceSynth.new(mixerB.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { accomp.isLoaded } != true }, { 0.001.wait; });

      bass = Dauphine_Bass.new(mixerC.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });

      mixerA.setVol(0, -18);

      mixerB.setSendVol(0, 0, -12);
      mixerB.setSendVol(1, 0, -16);
      mixerB.setVol(1, -70);

      mixerC.setSendVol(0, 0, -24);

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
