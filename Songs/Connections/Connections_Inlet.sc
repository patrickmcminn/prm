/*
Tuesday, July 7th 2015
Connections_Inlet.sc
prm
*/

Connections_Inlet : IM_Module {

  var server, <isLoaded;
  var <cascade, <attackRandomizer;
  var <reverb, <granulator;

  *new { | outBus, noteBufferArray, cascadeBufferArray, relGroup = nil, addAction = 'addToTail' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(noteBufferArray, cascadeBufferArray);
  }

  prInit { | noteBufferArray, cascadeBufferArray |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay2.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

			/*
      reverb = IM_Reverb.new(granulator.inBus, amp: 1, mix: 0.6, roomSize: 0.8, damp: 0.9,
        relGroup: group, addAction: \addToHead);
      //reverb = IM_Reverb.newConvolution(granulator.inBus, nil, nil, nil, nil, false, 1, ir, 2, group, \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });
			*/

      cascade = Connections_Cascade.new(granulator.inBus, noteBufferArray, cascadeBufferArray, group, \addToHead);
      while({ try { cascade.isLoaded } != true }, { 0.001.wait; });

      attackRandomizer = Connections_AttackRandomizer.new(granulator.inBus, noteBufferArray, group, \addToHead);
      while({ try { attackRandomizer.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      //reverb.mixer.setVol(-9);
      granulator.setMix(1);
      granulator.setDelayLevel(0);
      granulator.setTrigRate(40);
      granulator.setGrainDur(0.1, 0.3);

      //mixer.setPreVol(3);

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    cascade.free;
    attackRandomizer.free;
    //reverb.free;
    granulator.free;
    this.freeModule;
    isLoaded = false;

    cascade = nil;
    attackRandomizer = nil;
    reverb = nil;
    granulator = nil;
  }
}