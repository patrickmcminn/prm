/*
Monday, July 6th 2015
Connections_TrumpetGran.sc
prm
*/

Connections_TrumpetGran : IM_Module {

  var <isLoaded, server;
  var <input, <eq, <granulator, <reverb;
  var <inBus;

  *new { | outBus, relGroup = nil, addAction = 'addToTail' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      reverb = IM_Reverb.new(mixer.chanStereo(0), nil, nil, nil, nil, false, 1, 0.55, 0.75, 0.3, group, \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(reverb.inBus, group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(granulator.inBus, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      input = IM_Mixer_1Ch.new(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { input.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      inBus = input.chanMono(0);

      //input.mute;
      eq.setHighFreq(2500);
      eq.setHighGain(-6);
      granulator.setGrainDur(0.04, 0.06);
      granulator.setTrigRate(40);
      granulator.mixer.setPreVol(3);
      granulator.setDelayTime(0.8);
      granulator.setGranulatorCrossfade(1);
      granulator.setFeedback(0.3);

      mixer.setPreVol(9);

      server.sync;


      isLoaded = true;
    }
  }

  /////// public functions:

  free {
    input.free;
    eq.free;
    granulator.free;
    reverb.free;
    this.freeModule;
  }
}