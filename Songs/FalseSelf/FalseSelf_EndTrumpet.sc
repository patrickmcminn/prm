/*
Wednesday, April 19th 2017
FalseSelf_EndTrumpet.sc
prm
*/

FalseSelf_EndTrumpet : IM_Module {

  var <isLoaded, server;
  var splitter;
  var <input, <dry, <delay1, <delay2, <delay3, <delay4;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: \addToHead).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      delay4 = SimpleDelay.newMono(mixer.chanStereo, 1.68776371308016, 0, relGroup: group, addAction: \addToHead);
      while({ try { delay4.isLoaded } != true }, { 0.001.wait; });

      delay3 = SimpleDelay.newMono(mixer.chanStereo, 5.06329113924048, 0, 6, relGroup: group, addAction: \addToHead);
      while({ try { delay3.isLoaded } != true }, { 0.001.wait; });

      delay2 = SimpleDelay.newMono(mixer.chanStereo, 10.97046413502104, 0, 11, relGroup: group, addAction: \addToHead);
      while({ try { delay2.isLoaded } != true }, { 0.001.wait; });

      delay1 = SimpleDelay.newMono(mixer.chanStereo, 6.75105485232064, 0, 7, relGroup: group, addAction: \addToHead);
      while({ try { delay1.isLoaded } != true }, { 0.001.wait; });

      dry = IM_Mixer_1Ch.new(mixer.chanStereo, relGroup: group, addAction: \addToHead);
      while({ try { dry.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newMono(5, [dry.chanMono, delay1.inBus, delay2.inBus, delay3.inBus, delay4.inBus],
        relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      input = IM_Mixer_1Ch.new(splitter.inBus, relGroup: group, addAction: \addToHead);
      while({ try { input.isLoaded } != true }, { 0.001.wait; });

      delay1.setMix(1);
      delay2.setMix(1);
      delay3.setMix(1);
      delay4.setMix(1);
      /*
      delay1.mixer.setVol(-3);
      delay2.mixer.setVol(-6);
      delay3.mixer.setVol(-6);
      delay4.mixer.setVol(-6);
      */

      isLoaded = true;
    }
  }

  //////// public functions:

  free {
    input.free;
    splitter.free;
    dry.free;
    delay1.free;
    delay2.free;
    delay3.free;
    delay4.free;
    this.freeModule;
    isLoaded = false;
  }

  inBus { ^input.inBus }
}