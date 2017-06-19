/*
Friday, June 16th 2017
Foundation_TrumpetSection.sc
prm
*/

Foundation_TrumpetSection : IM_Module {

  var <isLoaded, server;
  var <trumpet1, <trumpet2, <trumpet3;
  var buffer1, buffer2, buffer3;

  *new { | outBus = 0, lowDBuffer, aBuffer, highDBuffer, relGroup, addAction = 'addToHead' |
    ^super.new(3, outBus, relGroup: relGroup, addAction: addAction).prInit(lowDBuffer, aBuffer, highDBuffer);
  }

  prInit { | lowDBuffer, aBuffer, highDBuffer |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      buffer1 = highDBuffer;
      buffer2 = aBuffer;
      buffer3 = lowDBuffer;

      trumpet1 = SamplePlayer.newMono(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { trumpet1.isLoaded } != true }, { 0.001.wait; });

      trumpet2 = SamplePlayer.newMono(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { trumpet2.isLoaded } != true }, { 0.001.wait; });

      trumpet3 = SamplePlayer.newMono(mixer.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { trumpet3.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      trumpet1.setAttackTime(0.1);
      trumpet1.setReleaseTime(0.15);
      trumpet1.setBuffer(buffer1);

      trumpet2.setAttackTime(0.1);
      trumpet2.setReleaseTime(0.15);
      trumpet2.setBuffer(buffer2);

      trumpet3.setAttackTime(0.1);
      trumpet3.setReleaseTime(0.15);
      trumpet3.setBuffer(buffer3);

      mixer.setPanBal(1, -0.5);
      mixer.setPanBal(1, 0.5);


      this.prCreateSequences;
      server.sync;

      this.prMakeIntroSequences;
      this.prMakeArrivalSequences;
      this.prMakeMainSequences;
      server.sync;

      isLoaded = true;
    }
  }

  prCreateSequences {
    /////// intro:
    trumpet1.makeSequence(\intro, 'sustaining');
    trumpet2.makeSequence(\intro, 'sustaining');
    trumpet3.makeSequence(\intro, 'sustaining');

    ////// arrival:
    trumpet1.makeSequence(\arrival, 'sustaining');
    trumpet2.makeSequence(\arrival, 'sustaining');
    trumpet3.makeSequence(\arrival, 'sustaining');

    ////// main:
    trumpet1.makeSequence(\main, 'sustaining');
    trumpet2.makeSequence(\main, 'sustaining');
    trumpet3.makeSequence(\main, 'sustaining');

    trumpet1.makeSequence(\test);
  }

  prMakeIntroSequences {
    this.prMakeTrumpet1IntroSequence;
    this.prMakeTrumpet2IntroSequence;
    this.prMakeTrumpet3IntroSequence;
  }

  prMakeArrivalSequences {
    this.prMakeTrumpet1ArrivalSequence;
    this.prMakeTrumpet2ArrivalSequence;
    this.prMakeTrumpet3ArrivalSequence;
  }

  prMakeMainSequences {
    this.prMakeTrumpet1MainSequence;
    this.prMakeTrumpet2MainSequence;
    this.prMakeTrumpet3MainSequence;
  }

  prMakeTrumpet1IntroSequence { }
  prMakeTrumpet1ArrivalSequence {
    var noteLoop = [-2, -3, -10, -2, -3, -12];
    trumpet1.addKey(\arrival, \legato, 1);
    trumpet1.addKey(\arrival, \rate, Pseq([
      Rest,
      Pseq(noteLoop.midiratio, 4),
      Pseq([-2, -3, -10].midiratio, 1)
    ], 1));
    trumpet1.addKey(\arrival, \dur, Pseq([
      4,
      2, 2, 2, 2, 2, 5,
      2, 2, 2, 2, 2, 3,
      1, 1, 1, 1, 1, 2,
      1, 1, 1, 1, 1, 1, 1, 1, 1
    ], 1));
  }
  prMakeTrumpet1MainSequence {
    var pattern1, pattern2, pattern3, pattern4;
    pattern1 = Pseq([-2, -3, -10, -2, -3, -12].midiratio, 30);
    pattern2 = Pseq([-2, -3, -5, -2, -3, -5].midiratio, 18);
    pattern3 = Pseq([-2, -3, -2, 0].midiratio, 10);
    pattern4 = Pseq([0, -2, 0, 2, 3, 2].midiratio, 16);
    trumpet1.addKey(\main, \legato, 1);
    trumpet1.addKey(\main, \dur, 0.25);
    trumpet1.addKey(\main, \rate, Pseq([pattern1, pattern2, pattern3, pattern4], 1));
    //trumpet1.addKey(\main, \rate, Pseq([0, -2, 0, 3, 2].midiratio, inf));
  }

  prMakeTrumpet2IntroSequence { }

  prMakeTrumpet2ArrivalSequence {
    var noteLoop = [0, -4, -9, -7];
    trumpet2.addKey(\arrival, \legato, 1);
    trumpet2.addKey(\arrival, \rate, Pseq([
      Rest, Rest,
      Pseq(noteLoop.midiratio, 5),
      0.midiratio
    ], 1));
    trumpet2.addKey(\arrival, \dur, Pseq([4, Pseq([2], 22)], 1));
  }

  prMakeTrumpet2MainSequence {
    var pattern1, pattern2, pattern3, pattern4;
    pattern1 = Pseq([-7, -5, -4, -5, -7, -9].midiratio, 24);
    pattern2 = Pseq([-7, -5, -4, -5, -7, -9, -10, -9].midiratio, 13);
    pattern3 = Pseq([-10, -9, -7, -5, -4, -5, -7, -9, -10, -12].midiratio, 8);
    pattern4 = Pseq([0, 2, 3, 2, 0, -2].midiratio, 16);
    trumpet2.addKey(\main, \legato, 1);
    trumpet2.addKey(\main, \dur, 0.25);
    trumpet2.addKey(\main, \rate, Pseq([pattern1, pattern2, pattern3, pattern4], 1));
  }

  prMakeTrumpet3IntroSequence { }

  prMakeTrumpet3ArrivalSequence {
    var note = [0, 5, 3, 2, 0, 5, 3, 2, -2, 0, 5, 3, 2, 0, 5, 3];
    var dur = [4, 4, 2, 2, 4, 4, 2, 2, 2, 2, 4, 2, 2, 2, 4, 2, 2, 2];
    trumpet3.addKey(\arrival, \legato, 1);
    trumpet3.addKey(\arrival, \rate, Pseq([Rest, Rest, Pseq(note.midiratio, 1)], 1));
    trumpet3.addKey(\arrival, \dur, Pseq(dur, 1));
  }

  prMakeTrumpet3MainSequence {
    var pattern1, pattern2, pattern3, pattern4;
    pattern1 = Pseq([7, 5, 7, 10, 9, 10].midiratio, 20);
    pattern2 = Pseq([7, 5, 7, 10, 9, 10, 12].midiratio, 16);
    pattern3 = Pseq([7, 5, 7, 10, 9, 10, 12, 5].midiratio, 12);
    pattern4 = Pseq([3, 5, 3, 5, 3, 2].midiratio, 16);
    trumpet3.addKey(\main, \legato, 1);
    trumpet3.addKey(\main, \dur, 0.25);
    trumpet3.addKey(\main, \rate, Pseq([pattern1, pattern2, pattern3, pattern4], 1));
  }

  //////// public functions:

  free {
    trumpet1.free;
    trumpet2.free;
    trumpet3.free;
    this.freeModule;
    isLoaded = false;
  }

  playIntroSequence { | clock |
    trumpet1.playSequence(\intro, clock);
    trumpet2.playSequence(\intro, clock);
    trumpet3.playSequence(\intro, clock);
  }

  playArrivalSequence { | clock |
    trumpet1.playSequence(\arrival, clock);
    trumpet2.playSequence(\arrival, clock);
    trumpet3.playSequence(\arrival, clock);
  }

  playMainSequence { | clock |
    trumpet1.playSequence(\main, clock);
    trumpet2.playSequence(\main, clock);
    trumpet3.playSequence(\main, clock);
  }

}