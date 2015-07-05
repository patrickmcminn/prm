/*
Sunday, July 5th 2015
Dauphine_Wash.sc
prm
*/

Dauphine_Wash : IM_Module {

  var <isLoaded, server;
  var <granulator, <wash;
  var <inputMixer;
  var <inBus;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixer.isLoaded } !=true }, { 0.001.wait; });

      wash = Wash.newStereo(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { wash.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(wash.inBus, group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      inputMixer = IM_Mixer_1Ch.new(granulator.inBus, relGroup: group, addAction: \addToHead);
      while({ try { inputMixer.isLoaded } !=true }, { 0.001.wait; });

      inBus = inputMixer.chanMono(0);
      wash.setDelayCoefficient(0.8);
      wash.setFeedbackCoefficient(0.3);
      wash.setMix(1);
      wash.setLowPassCutoff(3500);
      wash.setHighPassCutoff(250);
      granulator.granulator.setCrossfade(0.5);
      granulator.setGrainDur(0.3, 0.7);
      granulator.setTrigRate(16);
      granulator.setFeedback(0.5);


      isLoaded = true;
    }
  }


    //////// Public Functions:

    free {
      inputMixer.free;
      granulator.free;
      wash.free;
      this.freeModule;

      inputMixer = nil;
      granulator = nil;
      wash = nil;
    }
}

