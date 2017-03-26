/*
Friday, March 18th 2017
Habit_Moog.sc
prm
*/

Habit_Moog : IM_Module {

  var <isLoaded;
  var <server;
  var <synth;
  var <sequencePlaying;
  var <notePlaying;

  *new {
    |
    inBus = 3, outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead',
    deviceName, portName
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(inBus, deviceName, portName);
  }

  prInit { | inBus = 2, deviceName, portName|
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;

      synth = Mother32.new(inBus, mixer.chanStereo(0), deviceName, portName, relGroup: group, addAction: \addToHead);
      while({ try { synth.isLoaded } != true }, { 0.001.wait; });
      server.sync;

      synth.makeSequence('habit_sequence1');
      synth.makeSequence('habit_sequence2');
      synth.makeSequence('habit_sequence3');
      synth.makeSequence('habit_sequence4');
      synth.makeSequence('habit_sectionEndLoop');
      synth.makeSequence('habit_sectionEnd');
      synth.makeSequence('habit_pieceEnd');

      server.sync;

      this.prMakeSequence1;
      this.prMakeSequence2;
      this.prMakeSequence3;
      this.prMakeSequence4;
      this.prMakeSectionEndLoop;
      this.prMakeSectionEnd;
      this.prMakePieceEnd;

      isLoaded = true;
    }
  }

  prMakeSequence1 {
    var part1, part2, part3, part4, part5;
    part1 = Pseq([13, 13, 8, 6, \r, 1, -4, -6, -8, -3, -11, -1, 6, 1], 1);
    part2 = Pseq([13, \r, 13, \r, 8, 6, 1, \r, -4, -6, -8, \r, -3, -11, -1, 6, 1], 1);
    part3 = Pseq([1, -4, -6], 1);
    part4 = Pseq([Pseq([1], 4), Pseq([-4], 8), 6], 1);

    synth.addKey(\habit_sequence1, \octave, 4);
    synth.addKey(\habit_sequence1, \legato, 0.99);
    synth.addKey(\habit_sequence1, \dur, Pxrand([
      Pseq([1.5, 3, 1, 2.5, 2, 2, 0.5, 1.5, 1.5, 2, 0.5, 1, 2.5, 0.5, 1, 2.5, 2.5], 1),
      Pseq([0.5, 0.25, 1.25, 0.25, 0.5, 1, 1, 0.25, 0.75, 0.75, 1.25, 0.25, 0.5, 1.5, 0.5, 1.25, 1.25]*2, 1),
      Pseq([3, 3, 6], 1),
      Pseq([2, 2, 4], 1),
      Pseq([Pseq([0.5], 4), Pseq([0.25], 8), 4]),
    ], inf));
    synth.addKey(\habit_sequence1, \note, Prand([part1, part1, part1, part2, part2, part3, part3, part3, part3, part4], inf));
    synth.addKey(\habit_sequence1, \notePlaying, Pfunc({ |ev| notePlaying = ev[\note]%12; }));
  }

  prMakeSequence2 {
    var part1, part2, part3, part4, part5;
    part1 = Pseq([13, 13, 8, 6, \r, 1, -4, -6, -8, -3, -11, -1, 6, 1], 1);
    part2 = Pseq([13, \r, 13, \r, 8, 6, 1, \r, -4, -6, -8, \r, -3, -11, -1, 6, 1], 1);
    part3 = Pseq([1, -4, -6], 1);
    part4 = Pseq([Pseq([1], 4), Pseq([-4], 8), 6], 1);
    synth.addKey(\habit_sequence2, \octave, 4);
    synth.addKey(\habit_sequence2, \legato, 0.99);
    synth.addKey(\habit_sequence2, \dur, Pxrand([
      Pseq([4.75, 2, 10.75, 5.25, 10.25, 6.25, 8, 2.5, 4.75, 0.75, 7.5, 0.25, 8.25, 5.25, 10.5, 2.5, 2.25, 9.25, 1.5, 9.5], 1),
      Pseq([2.25, 1.25, 5.25, 2.5, 5, 3.25, 4, 1.5, 2.5, 3.75, 0.25, 4.25, 2.5, 5.25, 1.25, 1.25, 4.5, 0.75, 4.75], 1),
      Pseq([8, 8, 16],1),
      Pseq([4, 4, 8], 1),
      Pseq([Pseq([ Pseq([ Pseq([1], 4), Pseq([0.5], 8)], 6), Pseq([0.25], 16), Pseq([0.5], 8),  Pseq([1], 4), Pseq([0.5], 8)], 1), 16], 1)
    ], inf));
    synth.addKey(\habit_sequence2, \note, Prand([part1, part1, part1, part2, part2, part3, part3, part3, part3, part4], inf));
    synth.addKey(\habit_sequence2, \notePlaying, Pfunc({ |ev| notePlaying = ev[\note]%12; }));
  }

  prMakeSequence3 {
    var part1, part2, part3, part4, part5;
    part1 = Pseq([13, \r, 13, 13, \r, 13, \r, 13, 13, 13, 13, \r, 13, 13, \r, 13, \r, 13], 1);
    part2 = Pseq([8, \r, 8, \r, 8, \r, 8, \r, -8, 8, \r, 8, \r, 8, 8], 1);
    part3 = Pseq([-6], 2);
    part4 = Pseq([4], 4);
    part5 = Pseq([-3], 3);
    synth.addKey(\habit_sequence3, \octave, 4);
    synth.addKey(\habit_sequence3, \legato, 0.99);
    synth.addKey(\habit_sequence3, \dur, Pxrand([1.25, 0.5, 2.5, 1.25, 0.25, 2.5, 1.5, 2, 0.75, 1.25, 2, 2, 1.25, 0.25, 2.5, 0.5, 0.75, 2.25, 0.5, 2.25, 0.5, 0.25, 1.25, 0.75, 1.25, 1, 1, 0.25, 0.5, 0.25, 0.75, 1, 0.25, 0.5, 1.25, 0.25, 0.25, 0.25, 1.25, 1.25, 4, 4, 8, 2, 2, 4, Pseq([Pseq([0.5], 4), Pseq([0.25], 8), 4], 1)], inf));
    synth.addKey(\habit_sequence3, \note, Prand([part1, part1, part1, part1, part2, part2, part2, part3, part3, part4, part4, part5, part5], inf));
    synth.addKey(\habit_sequence3, \notePlaying, Pfunc({ |ev| notePlaying = ev[\note]%12; }));
  }

  prMakeSequence4 {
    var part1, part2, part3, part4, part5;
    part1 = Pseq([1, \r, 1, -4, -6, \r, -11, \r, -16, -18, -20, -15, \r, -23, -13, \r, -6, \r, -11], 1);
    part2 = Pseq([1, \r, 1, \r, -4, -6, \r, -11, -16, \r, -18, -20, \r, -15, -23, \r, -13, \r, -6, -11], 1);
    part3 = Pseq([-11, -16, -18], 1);
    part4 = Pseq([-11, -16, -18], 1);
    part5 = Pseq([Pseq([-11], 4), Pseq([-16], 8), -6], 1);
    synth.addKey(\habit_sequence4, \octave, 4);
    synth.addKey(\habit_sequence4, \legato, 0.99);
    synth.addKey(\habit_sequence4, \dur, Pxrand([
      Pseq([1.25, 0.5, 2.5, 1.5, 2.5, 1.5, 2, 0.75, 1.25, 2, 2, 1.25, 0.25, 2.5, 0.5, 0.75, 2.25, 0.5, 2.25], 1),
      Pseq([0.5, 0.25, 1.25, 0.25, 1.25, 1, 1, 0.25, 0.5, 0.25, 0.75, 1, 0.25, 0.5, 1.25, 0.25, 0.25, 0.25, 1.25, 1.25], 1),
      Pseq([4, 4, 8], 1),
      Pseq([2, 2, 4], 1),
      Pseq([Pseq([0.5], 4), Pseq([0.25], 8), 4], 1)
    ], inf));
    synth.addKey(\habit_sequence4, \note, Prand([part1, part1, part1, part1, part2, part2, part2, part2, part3, part4], inf));
    synth.addKey(\habit_sequence4, \notePlaying, Pfunc({ |ev| notePlaying = ev[\note]%12; }));
  }

  prMakeSectionEndLoop {
    synth.addKey(\habit_sectionEndLoop, \octave, 4);
    synth.addKey(\habit_sectionEndLoop, \legato, 0.99);
    synth.addKey(\habit_sectionEndLoop, \dur, Pseq([Pseq([0.25], 22), 0.5, 0.5, 0.25, 0.75, 0.25, 0.5, 0.5, Pseq([0.25], 14), 0.5, 0.5, 0.25, 0.25, 0.5, 0.25, 0.25, 0.75, 0.25, 0.5, 0.5, 0.25, 1, 0.75, 0.5, 0.75, 0.5, 1.25, 0.75, 0.75, 1.5, 0.5, 0.25, 0.5, 1, 0.75, 0.5, 0.75, 0.5, 1.25, 0.75, 0.75, 1.5, 1.5, 0.5, 0.25, 0.5, 0.25, 0.75, 0.75, 0.5, 0.75, 0.5, 1.25, 0.75, 0.75, 1.5, 0.5, 0.5, 0.5, 0.25, 0.5, 0.25, 0.75, 0.5], inf));
    synth.addKey(\habit_sectionEndLoop, \note, Pseq([1, 1, 1, 1, \r, 1, 1, 1, 1, 1, 1, 1, \r, 1, 1, 1, 1, 1, \r, 1, -11, \r, -23, -11, -11, -11, 1, -23, 1, \r, 1, 1, 1, 1, -23, -23, -23, -23, -23, \r, 1, 1, \r, -23, -23, 1, \r, 1, 1, \r, 1, \r, 1, 1, \r, 1, 1, 1, 1, 1, 1, 1, 1, -11, -11, \r, Pseq([-11], 12), \r, -11, \r, Pseq([-11], 10), \r, -23, \r, -23, \r, -23, -23], inf));
    synth.addKey(\habit_sectionEndLoop, \notePlaying, Pfunc({ |ev| notePlaying = ev[\note]%12; }));
  }

  prMakeSectionEnd {
    synth.addKey(\habit_sectionEnd, \octave, 4);
    synth.addKey(\habit_sectionEnd, \legato, 0.99);
    synth.addKey(\habit_sectionEnd, \dur, Pseq([1.25, 0.5, 2.5, 1.25, 0.25, 2.5, 1.5, 2, 0.75, 1.25, 2, 2, 1.25, 0.25, 2.5, 0.5, 0.75, 2.25, 0.5, 2.25, 1.25, 0.5, 2.5, 1.25, 0.25, 2.5, 1.5, 2, 0.75, 2.5, 0.25, 3.75, 4.25, 2, 5.25, 1.25, 1.25, 4.5, 0.75, 4.75, Pseq([0.25], 23), 2.75, 2.25, 1.25, 0.5, 2.75, 1.25, 2.5, 1.75, 1.75, 0.5, 2.5, 0.25, 3.5, 4.75, 2.25, 1.25, 5.25, 2.75, 5, 3.25, 4, 1.25, 4.75,  0.75, 7.5, 5.75, 4, 6.25, 3, 9.75, 0.25, 6.25, 6.5, 12, 4, 1, 4.75, 0.75, 7.5, 5.75, 4, 6.25, 0.25, 2.75, 9.75, 0.25, 6.25, 6.5, 11.75, 12, 4, 1.5, 4.75, 0.5, 7.5, 0.25, 5.25, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.5, 12, 4, 3.5, 3.5, 1.5, 4.75, 0.5, 7.5, 0.25, 5.5, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.5, 12, 4, 3.5, 4, 1.5, 4.75, 0.5, 7.5, 5.5, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.25, 12, 4], 1));
    synth.addKey(\habit_sectionEnd, \note, Pseq([1, \r, 1, 1, \r, 1, \r, 1, \r, 1, 1, 1, 1, \r, 1, 1, \r, 1, \r, 1, 1, \r, 1, 1, \r, 1, \r, 1, \r, -11, \r, -23, -11, -11, -11, 1, \r, -23, \r, 1, 1, 1, 1, 1, 1, 1, \r, 1, 1, 1, 1, 1, \r, 1, \r, 1, 1, 1, 1, 1, 1, 1, 1, \r, -23, -23, \r, -23, -23, -23, \r, 1, \r, 1, \r, -23, -23, 1, \r, 1, 1, 1, \r, 1, \r, 1, \r, 1, 1, 1, 1, 1, 1, \r, 1, 1, -11, -11, \r, -11, \r, -11, -11, -11, -11, \r, -11, -11, \r, -11, -11, -11, -11, -11, \r, -11, \r, -11, \r, -11, -11, \r, -11, -11, -11, -11, \r, -11, -11, -11, \r, -23, \r, -23, \r, -23, \r, -23, -23, \r, -23, -23, -23, -23, \r, -23, -23, -23, \r, -23, \r, -23, \r, -23, \r, -23, -23, \r, -23, -23, -23, -23, \r, -23, -23, -23], 1));
    synth.addKey(\habit_sectionEnd, \notePlaying, Pfunc({ |ev| notePlaying = ev[\note]%12; }));
  }

  prMakePieceEnd {
    synth.addKey(\habit_pieceEnd, \octave, 4);
    synth.addKey(\habit_pieceEnd, \legato, 0.99);
    synth.addKey(\habit_pieceEnd, \dur, Pseq([1.25, 0.5, 2.5, 1.25, 0.25, 2.5, 1.5, 2, 0.75, 1.25, 2, 2, 1.25, 0.25, 2.5, 0.5, 0.75, 2.25, 0.5, 2.25, 1.25, 0.5, 2.5, 1.25, 0.25, 2.5, 1.5, 2, 0.75, 2.5, 0.25, 3.75, 4.25, 2, 5.25, 1.25, 1.25, 4.5, 0.75, 4.75, Pseq([0.25], 23), 2.75, 2.25, 1.25, 0.5, 2.75, 1.25, 2.5, 1.75, 1.75, 0.5, 2.5, 0.25, 3.5, 4.75, 2.25, 1.25, 5.25, 2.75, 5, 3.25, 4, 1.25, 4.75,  0.75, 7.5, 5.75, 4, 6.25, 3, 9.75, 0.25, 6.25, 6.5, 12, 4, 1, 4.75, 0.75, 7.5, 5.75, 4, 6.25, 0.25, 2.75, 9.75, 0.25, 6.25, 6.5, 11.75, 12, 4, 1.5, 4.75, 0.5, 7.5, 0.25, 5.25, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.5, 12, 4, 3.5, 3.5, 1.5, 4.75, 0.5, 7.5, 0.25, 5.5, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.5, 12, 4, 3.5, 4, 1.5, 4.75, 0.5, 7.5, 5.5, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.25, 12, 4], 1));
    synth.addKey(\habit_pieceEnd, \note, Pseq([1, \r, 1, 1, \r, 1, \r, 1, \r, 1, 1, 1, 1, \r, 1, 1, \r, 1, \r, 1, 1, \r, 1, 1, \r, 1, \r, 1, \r, -11, \r, -23, -11, -11, -11, 1, \r, -23, \r, 1, 1, 1, 1, 1, 1, 1, \r, 1, 1, 1, 1, 1, \r, 1, \r, 1, 1, 1, 1, 1, 1, 1, 1, \r, -23, -23, \r, -23, -23, -23, \r, 1, \r, 1, \r, -23, -23, 1, \r, 1, 1, 1, \r, 1, \r, 1, \r, 1, 1, 1, 1, 1, 1, \r, 1, 1, -11, -11, \r, -11, \r, -11, -11, -11, -11, \r, -11, -11, \r, -11, -11, -11, -11, -11, \r, -11, \r, -11, \r, -11, -11, \r, -11, -11, -11, -11, \r, -11, -11, -11, \r, -23, \r, -23, \r, -23, \r, -23, -23, \r, -23, -23, -23, -23, \r, -23, -23, -23, \r, -23, \r, -23, \r, -23, \r, -23, -23, \r, -23, -23, -23, -23, \r, -23, -23, -23], 1));
    synth.addKey(\habit_pieceEnd, \notePlaying, Pfunc({ |ev| notePlaying = ev[\note]%12; }));
  }



  //////// public functions:

  free {
    synth.free;
    this.freeModule;
  }

  playSequence1 { | clock |
    synth.stopSequence(\habit_sequence2);
    synth.stopSequence(\habit_sequence3);
    synth.stopSequence(\habit_sequence4);
    synth.stopSequence(\habit_sectionEndLoop);
    synth.stopSequence(\habit_sectionEnd);
    synth.stopSequence(\habit_pieceEnd);
    synth.playSequence(\habit_sequence1, clock);
    sequencePlaying = 'sequence1';
  }

  playSequence2 { | clock |
    synth.stopSequence(\habit_sequence1);
    synth.stopSequence(\habit_sequence3);
    synth.stopSequence(\habit_sequence4);
    synth.stopSequence(\habit_sectionEndLoop);
    synth.stopSequence(\habit_sectionEnd);
    synth.stopSequence(\habit_pieceEnd);
    synth.playSequence(\habit_sequence2, clock);
    sequencePlaying = 'sequence2';
  }

  playSequence3 { | clock |
    synth.stopSequence(\habit_sequence1);
    synth.stopSequence(\habit_sequence2);
    synth.stopSequence(\habit_sequence4);
    synth.stopSequence(\habit_sectionEndLoop);
    synth.stopSequence(\habit_sectionEnd);
    synth.stopSequence(\habit_pieceEnd);
    synth.playSequence(\habit_sequence3, clock);
    sequencePlaying = 'sequence3';
  }

  playSequence4 { | clock |
    synth.stopSequence(\habit_sequence1);
    synth.stopSequence(\habit_sequence2);
    synth.stopSequence(\habit_sequence3);
    synth.stopSequence(\habit_sectionEndLoop);
    synth.stopSequence(\habit_sectionEnd);
    synth.stopSequence(\habit_pieceEnd);
    synth.playSequence(\habit_sequence4, clock);
    sequencePlaying = 'sequence4';
  }

  playSectionEndLoop { | clock |
    synth.stopSequence(\habit_sequence1);
    synth.stopSequence(\habit_sequence2);
    synth.stopSequence(\habit_sequence3);
    synth.stopSequence(\habit_sequence4);
    synth.stopSequence(\habit_sectionEnd);
    synth.stopSequence(\habit_pieceEnd);
    synth.playSequence(\habit_sectionEndLoop, clock);
    sequencePlaying = 'sectionEndLoop';
  }

  playSectionEnd { | clock |
    synth.stopSequence(\habit_sequence1);
    synth.stopSequence(\habit_sequence2);
    synth.stopSequence(\habit_sequence3);
    synth.stopSequence(\habit_sequence4);
    synth.stopSequence(\habit_sectionEndLoop);
    synth.stopSequence(\habit_pieceEnd);
    synth.playSequence(\habit_sectionEnd, clock);
    sequencePlaying = 'sectionEnd';
  }

  playPieceEnd { | clock |
    synth.stopSequence(\habit_sequence1);
    synth.stopSequence(\habit_sequence2);
    synth.stopSequence(\habit_sequence3);
    synth.stopSequence(\habit_sequence4);
    synth.stopSequence(\habit_sectionEndLoop);
    synth.stopSequence(\habit_sectionEnd);
    synth.playSequence(\habit_pieceEnd, clock);
    sequencePlaying = 'pieceEnd';
  }

  stopAllSequences {
    synth.stopSequence(\habit_sequence1);
    synth.stopSequence(\habit_sequence2);
    synth.stopSequence(\habit_sequence3);
    synth.stopSequence(\habit_sequence4);
    synth.stopSequence(\habit_sectionEndLoop);
    synth.stopSequence(\habit_sectionEnd);
    synth.stopSequence(\habit_pieceEnd);
    sequencePlaying = 'none';
  }

}