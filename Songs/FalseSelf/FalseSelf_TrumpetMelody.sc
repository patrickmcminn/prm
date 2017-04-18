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

      shift1 = ShiftSequencer.new(eq.inBus, group, \addToHead);
      while({ try { shift1.isLoaded } != true }, { 0.001.wait; });

      shift2 = ShiftSequencer.new(eq.inBus, group, \addToHead);
      while({ try { shift2.isLoaded } != true }, { 0.001.wait; });

      dry = IM_Mixer_1Ch.new(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { dry.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newMono(3, [dry.chanMono, shift1.inBus, shift2.inBus], relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      this.prInitParameters;
      this.prMakePatterns;

      server.sync;

      isLoaded = true;
    }
  }

  prInitParameters {
    mixer.setPreVol(0);
    dry.mute;
    // eq:
    eq.setHighPassCutoff(100);
    eq.setPeak1Freq(350);
    eq.setPeak1Gain(-3);
    eq.setPeak1RQ(0.3);
    eq.setPeak2Freq(1200);
    eq.setPeak2Gain(3);
  }

  prMakePatterns {
    {

      var shift1NoteArray, shift2NoteArray, durArray;

      shift1NoteArray =
      [
        Pseq([-12], 25),
        -8, -7, -9, -8, -8, -7, -9, -9, -5, -9, -7, -8, -7, -9, -7, -8, -8, -5, -3, -2, 3, -7, -5, -4, -9
      ];

      shift2NoteArray =
      [
        -3, -2, -5, -4, -3, -2, -5, -4, -4, -5, -3, -1, -4, -5, -3, -1, -4, -2, -3, -2, 8, -2, -3, -2, -8,
        -3, -2, -5, -5, -3, -2, -5, -4, 4, -4, -2, -5, -4, -4, -2, 0, 0, 3, 5, 6, 8, 0, 2, 3, -5
      ] -12;

      durArray =
      [
        2, 4, 2, 10, 2, 4, 4, 8, 8, 2, 2, 4, 6, 2, 2, 2, 8, 2, 4, 2, 8, 2, 4, 4, 10,
        2, 4, 2, 8, 2, 2, 2, 4, 6, 2, 2, 4, 8, 2, 2, 2, 6, 2, 4, 2, 6, 2, 2, 4, 8
      ];


      shift1.makeSequence(\falseSelf_Shift1);
      shift2.makeSequence(\falseSelf_Shift2);

      server.sync;

      shift1.addKey(\falseSelf_Shift1, \shiftAmount, Pseq(shift1NoteArray, 1));
      shift1.addKey(\falseSelf_Shift1, \dur, Pseq(durArray, 1));
      shift1.addKey(\falseSelf_Shift1, \legato, 1);

      shift2.addKey(\falseSelf_Shift2, \shiftAmount, Pseq(shift2NoteArray, 1));
      shift2.addKey(\falseSelf_Shift2, \dur, Pseq(durArray, 1));
      shift2.addKey(\falseSelf_Shift2, \legato, 1);

    }.fork;
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

  playPattern { | clock |
    shift1.playSequence(\falseSelf_Shift1, clock);
    shift2.playSequence(\falseSelf_Shift2, clock);
  }
}
