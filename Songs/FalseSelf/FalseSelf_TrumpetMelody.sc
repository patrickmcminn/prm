/*
Monday, April 10th 2017
FalseSelf_TrumpetMelody.sc
prm
*/


FalseSelf_TrumpetMelody : IM_Processor {

  var server;
  var <isLoaded;
  var <shift1, <shift2, <dry;
  var <eq;
  var <splitter;
  var shiftAmount1, shiftAmount2;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      shift1 = GrainFreeze2.newMono(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { shift1.isLoaded } != true }, { 0.001.wait; });

      shift2 = GrainFreeze2.newMono(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { shift2.isLoaded } != true }, { 0.001.wait; });

      /*
      shift1 = PitchShifter.newMono(eq.inBus, -12, relGroup: group, addAction: \addToHead);
      while({ try { shift1.isLoaded } != true }, { 0.001.wait; });

      shift2 = PitchShifter.newMono(eq.inBus, -15, relGroup: group, addAction: \addToHead);
      while({ try { shift2.isLoaded } != true }, { 0.001.wait; });
      */

      dry = IM_Mixer_1Ch.new(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { dry.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newMono(3, [dry.chanMono, shift1.inBus, shift2.inBus],
        relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      this.prInitializeParameters;
      //this.prMakePatterns;

      server.sync;

      isLoaded = true;
    }
  }


  prInitializeParameters {
    mixer.setPreVol(-6);
    dry.setVol(0);
    shift1.mixer.setPreVol(-5);
    shift2.mixer.setPreVol(-5);
    //dry.mute;
    //shift2.mixer.mute;
    // eq:
    eq.setHighPassCutoff(175);
    eq.setPeak3Freq(360);
    eq.setPeak3Gain(-6);
    eq.setPeak1Freq(150);
    eq.setPeak1Gain(1.5);
    eq.setPeak1RQ(0.3);
    eq.setPeak2Freq(1200);
    eq.setPeak2Gain(-1);
  }

  //////// free:

  free {
    splitter.free;
    dry.free;
    shift1.free;
    shift2.free;
    eq.free;
    this.freeProcessor;
    isLoaded = false;
  }

  inBus { ^splitter.inBus }

  recordBuffer {
    shift1.recordBuffer;
    shift2.recordBuffer;
  }

}
