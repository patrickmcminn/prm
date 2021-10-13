/*
Monday, June 3rd 2019
Connections_Mic.sc
prm

finally a class to replace a
bad hack from long ago

well.
I guess one hack begets another.
fuck it for now.

not in the mood to create a 'mono' version of GranularDelay, so using a 1-channel
IM_Mixer to act as a Mono => Stereo conversion
*/

Connections_Mic : IM_Module {

  var <isLoaded, server;
  var <granulator, <eq;
  var inBusHack;

  * new { | outBus, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay2.new(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      inBusHack = IM_Mixer.new(1, granulator.inBus, relGroup: group, addAction: \addToHead);
      while({ try { inBusHack.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitializeParameters;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    eq.setHighPassCutoff(200);
    eq.setPeak1Freq(400);
    eq.setPeak1Gain(3);
    eq.setPeak2Freq(650);
    eq.setPeak2Gain(-6);

    granulator.setMix(0.3);
    granulator.setRate(0.5, 0.5);
    granulator.setGrainEnvelope('gabWide');
    granulator.setTrigRate(16);
    granulator.setGrainDur(0.1, 0.3);
    granulator.setDelayLevel(0.5);

  }

  ///////// public functions:

  free {
    isLoaded = false;
    this.freeModule;
    inBusHack.free;
    granulator.free;
    eq.free;
  }

  inBus { ^inBusHack.chanMono; }



}