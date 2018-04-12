/*
Wednesday, April 11th 2018
Darkness_Gran.sc
A Shallow Eclipsing Darkness
prm

granulator at absurd rate to emulate K2000 through modular improvisation
*/

Darkness_Gran : IM_Processor {

  var <isLoaded;
  var server;

  var <eq, <granulator, <input;

  *new { | outBus = 0, relGroup, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }


  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(eq.inBus, group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      input = IM_Mixer_1Ch.new(granulator.inBus, relGroup: group, addAction: \addToHead);
      while({ try { input.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

      isLoaded = true;
    }
  }

  prSetInitialParameters {
    granulator.setGrainDur(0.01, 0.09);
    granulator.setRate(100, 1000);
    granulator.setGrainEnvelope('expodec');
    granulator.setTrigRate(19);
    granulator.setDelayTime(1);
    granulator.setFeedback(0.35);
    granulator.setGranulatorCrossfade(1);
  }

  //////// public functions:

  inBus { ^input.chanStereo(0) }

}