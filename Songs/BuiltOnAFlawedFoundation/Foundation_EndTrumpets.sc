/*
Monday, June 19th 2017
Foundation_EndTrumpets.sc
prm
*/

Foundation_EndTrumpets :IM_Module {

  var server, <isLoaded;
  var <trumpet;

  *new { |outBus, aBuffer, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit(aBuffer);
  }

  prInit { | aBuffer |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      trumpet = SamplePlayer.newMono(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      trumpet.setAttackTime(1);
      trumpet.setReleaseTime(5);

      trumpet.makeSequence(\trumpet1);
      trumpet.makeSequence(\trumpet2);
      trumpet.makeSequence(\trumpet3);
      trumpet.setFilterCutoff(12000);

      trumpet.setBuffer(aBuffer);

      server.sync;

      this.prMakePatterns;

      isLoaded = true;
    }
  }

  prMakePatterns {
    this.prMakeTrumpet1Patterns;
    this.prMakeTrumpet2Patterns;
    this.prMakeTrumpet3Patterns;
  }

  prMakeTrumpet1Patterns {
    var squareNoteArray, squareDurArray;
    var driftNoteArray, driftDurArray;
    squareNoteArray = Pseq([
      -7.midiratio, -7.midiratio, Rest, -10.midiratio, Rest, -4.midiratio, Rest, -12.midiratio, -12.midiratio,
      Rest, -5.midiratio, Rest, -4.midiratio, Rest, -9.midiratio, Rest
    ], 1);
    squareDurArray = Pseq([6, 3, 4, 1.5, 5.5, 1.5, 2.5, 6, 3, 4, 2, 7, 1.5, 2.5, 1.5, 4.5], 1);
    driftNoteArray = Pseq([
      -7.midiratio, -7.midiratio, Rest, -10.midiratio, Rest, -4.midiratio, Rest, -12.midiratio, -12.midiratio,
      Rest, -5.midiratio, Rest, -4.midiratio, Rest, -9.midiratio, Rest
    ], inf);
    driftDurArray = Pseq([6, 3, 4, 1.5, 5.5, 1.5, 2.5, 6, 3, 4, 2, 7, 1.5, 2.5, 1.5, 3.5], inf);

    trumpet.addKey(\trumpet1, \legato, 1);
    trumpet.addKey(\trumpet1, \dur, Pseq([squareDurArray, driftDurArray], 1));
    trumpet.addKey(\trumpet1, \rate, Pseq([squareNoteArray, driftNoteArray], 1));
    trumpet.addKey(\trumpet1, \pan, Pwhite(-1, 1));
  }

  prMakeTrumpet2Patterns {
    var squareNoteArray, squareDurArray;
    var driftNoteArray, driftDurArray;

    squareNoteArray = Pseq([
      Rest, -4.midiratio, Rest, -12.midiratio, -12.midiratio, Rest, -5.midiratio, Rest,
      -9.midiratio, Rest, -7.midiratio, 0.midiratio, Rest, -5.midiratio, Rest, -10.midiratio, Rest
    ], 1);
    squareDurArray = Pseq([4, 1.5, 2.5, 6, 3, 4, 1.5, 5.5, 1.5, 2.5, 6, 6, 3, 1.5, 2.5, 1.5, 3.5], 1);
    driftNoteArray = Pseq([
      Rest, -4.midiratio, Rest, -12.midiratio, -12.midiratio, Rest, -5.midiratio, Rest,
      -9.midiratio, Rest, -7.midiratio, 0.midiratio, Rest, -5.midiratio, Rest, -10.midiratio, Rest
    ], inf);
    driftDurArray = Pseq([4, 1.5, 2.5, 6, 3, 4, 1.5, 5.5, 1.5, 2.5, 6, 6, 3, 1.5, 2.5, 1.5, 4], inf);

    trumpet.addKey(\trumpet2, \legato, 1);
    trumpet.addKey(\trumpet2, \dur, Pseq([squareDurArray, driftDurArray], 1));
    trumpet.addKey(\trumpet2, \rate, Pseq([squareNoteArray, driftNoteArray], 1));
    trumpet.addKey(\trumpet2, \pan, Pwhite(-1, 1));
  }

  prMakeTrumpet3Patterns {
    var squareNoteArray, squareDurArray;
    var driftNoteArray, driftDurArray;

    squareNoteArray = Pseq([
      Rest, -5.midiratio, Rest, -9.midiratio, Rest, -7.midiratio, -7.midiratio, Rest,
      -10.midiratio, Rest, -4.midiratio, Rest, -12.midiratio, -7.midiratio, -12.midiratio
    ], 1);
    squareDurArray = Pseq([5, 1.5, 5.5, 1.5, 2.5, 6, 3, 4, 1.5, 5.5, 1.5, 4.5, 6, 4, 4], 1);
    driftNoteArray = Pseq([
      Rest, -5.midiratio, Rest, -9.midiratio, Rest, -7.midiratio, -7.midiratio, Rest,
      -10.midiratio, Rest, -4.midiratio, Rest, -12.midiratio, -7.midiratio, -12.midiratio
    ], inf);
    driftDurArray = Pseq([5, 1.5, 5.5, 1.5, 2.5, 6, 3, 4, 1.5, 5.5, 1.5, 4.5, 6, 4, 2], inf);

    trumpet.addKey(\trumpet3, \legato, 1);
    trumpet.addKey(\trumpet3, \dur, Pseq([squareDurArray, driftDurArray], 1));
    trumpet.addKey(\trumpet3, \rate, Pseq([squareNoteArray, driftNoteArray], 1));
    trumpet.addKey(\trumpet3, \pan, Pwhite(-1, 1));
  }

  //////// public functions:
  free {
    trumpet.free;
    this.freeModule;
    isLoaded = false;
  }

  playEndSequence { | clock |
    trumpet.playSequence(\trumpet1, clock);
    trumpet.playSequence(\trumpet2, clock);
    trumpet.playSequence(\trumpet3, clock);
  }

  stopEndSequence {
    trumpet.stopSequence(\trumpet1);
    trumpet.stopSequence(\trumpet2);
    trumpet.stopSequence(\trumpet3);
  }

}