/*
Wednesday, April 18th 2018
Meaning_Reverb.sc
prm

everything was pregnant with meaning
*/

Meaning_Reverb : IM_Processor {

  var <isLoaded;
  var server;

  var <delay, <eq, <reverb;

  *new { | outBus = 0, buffer, relGroup = nil, addAction = \addToHead |
    ^super.new(1, 1, relGroup: relGroup, addAction: addAction).prInit(buffer);
  }

  prInit { | buffer |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      reverb = IM_Reverb.newConvolution(mixer.chanStereo(0), bufName: buffer,
        relGroup: group, addAction: \addToHead);
      /*
      reverb = IM_Reverb.new(mixer.chanStereo(0), mix: 1, roomSize: 0.6, damp: 0,
        relGroup: group, addAction: \addToHead);
      */
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(reverb.inBus, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      delay = SimpleDelay.newStereo(eq.inBus, 0.25, 0, 0.26, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prInitializeParameters;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    eq.setHighPassCutoff(947);
    delay.setMix(1);
    reverb.setHighPassFreq(500);
  }

  inBus { ^delay.inBus }
}