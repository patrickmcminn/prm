/*
Tuesday, August 29th 2017
StupidNoiseGate.sc
prm
*/

StupidNoiseGate : IM_Processor {

  var server, <isLoaded;

  var synth;
  var <thresh;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      thresh = 0.01;

      this.prAddSynthDef;

      server.sync;

      synth = Synth(\prm_stupidNoiseGate, [\inBus, inBus, \outBus, mixer.chanStereo(0), \thresh, thresh],
        group, \addToHead);
      while({ synth == nil }, { 0.001.wait; });

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_stupidNoiseGate, {
      | inBus = 0, outBus = 0, thresh = 0.01 |
      var input, comp;
      input = In.ar(inBus, 2);
      comp = Compander.ar(input, input, thresh, 10, 1, 0.002, 0.05);
      Out.ar(outBus, comp);
    }).add;
  }

  free {
    synth.free;
    this.freeProcessor;
    isLoaded = false;
  }

  setThresh { | thresh = 0.01 |
    synth.set(\thresh, thresh);
  }

}