/*
Tuesday, July 7th 2015
Connections_Inlet.sc
prm
*/

Connections_Inlet : IM_Module {

  var server, <isLoaded;
  var <cascade, <attackRandomizer;
  var <reverb, <granulator;

  *new { | outBus, noteBufferArray, cascadeBufferArray, ir, relGroup = nil, addAction = 'addToTail' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(noteBufferArray, cascadeBufferArray, ir);
  }

  prInit { | noteBufferArray, cascadeBufferArray, ir |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      reverb = IM_Reverb.new(granulator.inBus, amp: 1, mix: 0.6, roomSize: 0.8, damp: 0.85,
        relGroup: group, addAction: \addToHead);
      //reverb = IM_Reverb.newConvolution(granulator.inBus, nil, nil, nil, nil, false, 1, ir, 2, group, \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      cascade = Connections_Cascade.new(reverb.inBus, noteBufferArray, cascadeBufferArray, group, \addToHead);
      while({ try { cascade.isLoaded } != true }, { 0.001.wait; });

      attackRandomizer = Connections_AttackRandomizer.new(reverb.inBus, noteBufferArray, group, \addToHead);
      while({ try { attackRandomizer.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      reverb.mixer.setVol(-12);
      granulator.setGranulatorCrossfade(1);
      granulator.setDelayMix(0);
      granulator.setTrigRate(40);
      granulator.setGrainDur(0.1, 0.3);

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    cascade.free;
    attackRandomizer.free;
    reverb.free;
    granulator.free;
    this.freeModule;
    isLoaded = false;

    cascade = nil;
    attackRandomizer = nil;
    reverb = nil;
    granulator = nil;
  }
}