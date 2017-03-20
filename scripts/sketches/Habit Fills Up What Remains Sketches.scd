~prm.moog.vol

x = { Out.ar(~prm.submixB, SoundIn.ar(3)) }.play
x.free;


~moog = Habit_Moog.new(3, ~prm.submixB, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, nil,
  ~prm.procGroup, \addToHead, "iConnectAudio4+", "DIN");

~part1 = Pseq([13, 13, 8, 6, \r, 1, -4, -6, -8, -3, -11, -1, 6, 1], 1);
~part2 = Pseq([13, \r, 13, \r, 8, 6, 1, \r, -4, -6, -8, \r, -3, -11, -1, 6, 1], 1);
~part3 = Pseq([1, -4, -6], 1);
~part4 = Pseq([Pseq([1], 4), Pseq([-4], 8), 6], 1);


~moog.synth.addKey(\sequence1, \octave, 4);
~moog.synth.addKey(\sequence1, \legato, 0.99);
~moog.synth.addKey(\sequence1, \dur, Prand([1.5, 3, 1, 2.5, 2, 2, 0.5, 1.5, 1.5, 0.75, 0.25, 0.5, 0.5, 0.25, 1.5, 3, 2, 1, 4], inf));
~moog.synth.addKey(\sequence1, \note, Prand([~part1, ~part1, ~part1, ~part2, ~part2, ~part3, ~part3, ~part3, ~part3, ~part4], inf));



~moog.synth.addKey(\sequence2, \octave, 4);
~moog.synth.addKey(\sequence2, \legato, 0.99);
~moog.synth.addKey(\sequence2, \dur, Pxrand([4.75, 2, 10.75, 5.25, 10.25, 6.25, 8, 2.5, 4.75, 0.75, 7.5, 8.25, 5.25, 10.5, 2.5, 2.25, 9.25, 1.5, 9.5, 2.25, 5.25, 2.5, 5, 3.25, 4, 2.5, 3.75, 4.25, 2.5, 5.25, 1.25, 1.25, 4.5, 0.75, 4.75, 16, 16, 32, 4, 4, 8, Pseq([ Pseq([1], 4), Pseq([0.5], 8)], 6), Pseq([Pseq([0.25], 16), Pseq([0.5], 8)], 1),  Pseq([Pseq([1], 4), Pseq([0.5], 8)], 1)], inf));
~moog.synth.addKey(\sequence2, \note, Prand([~part1, ~part1, ~part1, ~part2, ~part2, ~part3, ~part3, ~part3, ~part3, ~part4], inf));


~moog.synth.playSequence(\sequence2);


//// sequence 3:
(
var part1, part2, part3, part4, part5;
part1 = Pseq([13, \r, 13, 13, \r, 13, \r, 13, 13, 13, 13, \r, 13, 13, \r, 13, \r, 13], 1);
part2 = Pseq([8, \r, 8, \r, 8, \r, 8, \r, -8, 8, \r, 8, \r, 8, 8], 1);
part3 = Pseq([-6], 2);
part4 = Pseq([4], 4);
part5 = Pseq([-3], 3);
~moog.synth.addKey(\sequence3, \octave, 4);
~moog.synth.addKey(\sequence3, \legato, 0.99);
~moog.synth.addKey(\sequence3, \dur, Pxrand([1.25, 0.5, 2.5, 1.25, 0.25, 2.5, 1.5, 2, 0.75, 1.25, 2, 2, 1.25, 0.25, 2.5, 0.5, 0.75, 2.25, 0.5, 2.25, 0.5, 0.25, 1.25, 0.75, 1.25, 1, 1, 0.25, 0.5, 0.25, 0.75, 1, 0.25, 0.5, 1.25, 0.25, 0.25, 0.25, 1.25, 1.25, 4, 4, 8, 2, 2, 4, Pseq([Pseq([0.5], 4), Pseq([0.25], 8), 4], 1)], inf));
~moog.synth.addKey(\sequence3, \note, Prand([part1, part1, part1, part1, part2, part2, part2, part3, part3, part4, part4, part5, part5], inf));
);

//// sequence 4:
(
var part1, part2, part3, part4, part5;
part1 = Pseq([1, \r, 1, -4, -6, \r, -11, \r, -16, -18, -20, -15, \r, -23, -13, \r, -6, \r, -11], 1);
part2 = Pseq([1, \r, 1, \r, -4, -6, \r, -11, -16, \r, -18, -20, \r, -15, -23, \r, -13, \r, -6, -11], 1);
part3 = Pseq([-11, -16, -18], 1);
part4 = Pseq([-11, -16, -18], 1);
part5 = Pseq([Pseq([-11], 4), Pseq([-16], 8), -6], 1);
~moog.synth.addKey(\sequence4, \octave, 4);
~moog.synth.addKey(\sequence4, \legato, 0.99);
~moog.synth.addKey(\sequence4, \dur, Pxrand([
  Pseq([1.25, 0.5, 2.5, 1.5, 2.5, 1.5, 2, 0.75, 1.25, 2, 2, 1.25, 0.25, 2.5, 0.5, 0.75, 2.25, 0.5, 2.25], 1),
  Pseq([0.5, 0.25, 1.25, 0.25, 1.25, 1, 1, 0.25, 0.5, 0.25, 0.75, 1, 0.25, 0.5, 1.25, 0.25, 0.25, 0.25, 1.25, 1.25], 1),
  Pseq([4, 4, 8], 1),
  Pseq([2, 2, 4], 1),
  Pseq([Pseq([0.5], 4), Pseq([0.25], 8), 4], 1)
], inf));
~moog.synth.addKey(\sequence4, \note, Prand([part1, part1, part1, part1, part2, part2, part2, part2, part3, part4], inf));
);

~moog.synth.addKey(\sectionEnd, \octave, 4);
~moog.synth.addKey(\sectionEnd, \legato, 0.99);
~moog.synth.addKey(\sectionEnd, \dur, Pseq([1.25, 0.5, 2.5, 1.25, 0.25, 2.5, 1.5, 2, 0.75, 1.25, 2, 2, 1.25, 0.25, 2.5, 0.5, 0.75, 2.25, 0.5, 2.25, 1.25, 0.5, 2.5, 1.25, 0.25, 2.5, 1.5, 2, 0.75, 2.5, 0.25, 3.75, 4.25, 2, 5.25, 1.25, 1.25, 4.5, 0.75, 4.75, Pseq([0.25], 23), 2.75, 2.25, 1.25, 0.5, 2.75, 1.25, 2.5, 1.75, 1.75, 0.5, 2.5, 0.25, 3.5, 4.75, 2.25, 1.25, 5.25, 2.75, 5, 3.25, 4, 1.25, 4.75,  0.75, 7.5, 5.75, 4, 6.25, 3, 9.75, 0.25, 6.25, 6.5, 12, 4, 1, 4.75, 0.75, 7.5, 5.75, 4, 6.25, 0.25, 2.75, 9.75, 0.25, 6.25, 6.5, 11.75, 12, 4, 1.5, 4.75, 0.5, 7.5, 0.25, 5.25, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.5, 12, 4, 3.5, 3.5, 1.5, 4.75, 0.5, 7.5, 0.25, 5.5, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.5, 12, 4, 3.5, 4, 1.5, 4.75, 0.5, 7.5, 5.5, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.25, 12, 4], 1));
~moog.synth.addKey(\sectionEnd, \note, Pseq([1, \r, 1, 1, \r, 1, \r, 1, \r, 1, 1, 1, 1, \r, 1, 1, \r, 1, \r, 1, 1, \r, 1, 1, \r, 1, \r, 1, \r, -11, \r, -23, -11, -11, -11, 1, \r, -23, \r, 1, 1, 1, 1, 1, 1, 1, \r, 1, 1, 1, 1, 1, \r, 1, \r, 1, 1, 1, 1, 1, 1, 1, 1, \r, -23, -23, \r, -23, -23, -23, \r, 1, \r, 1, \r, -23, -23, 1, \r, 1, 1, 1, \r, 1, \r, 1, \r, 1, 1, 1, 1, 1, 1, \r, 1, 1, -11, -11, \r, -11, \r, -11, -11, -11, -11, \r, -11, -11, \r, -11, -11, -11, -11, -11, \r, -11, \r, -11, \r, -11, -11, \r, -11, -11, -11, -11, \r, -11, -11, -11, \r, -23, \r, -23, \r, -23, \r, -23, -23, \r, -23, -23, -23, -23, \r, -23, -23, -23, \r, -23, \r, -23, \r, -23, \r, -23, -23, \r, -23, -23, -23, -23, \r, -23, -23, -23], 1));

~moog.synth.addKey(\pieceEnd, \octave, 4);
~moog.synth.addKey(\pieceEnd, \legato, 0.99);
~moog.synth.addKey(\pieceEnd, \dur, Pseq([1.25, 0.5, 2.5, 1.25, 0.25, 2.5, 1.5, 2, 0.75, 1.25, 2, 2, 1.25, 0.25, 2.5, 0.5, 0.75, 2.25, 0.5, 2.25, 1.25, 0.5, 2.5, 1.25, 0.25, 2.5, 1.5, 2, 0.75, 2.5, 0.25, 3.75, 4.25, 2, 5.25, 1.25, 1.25, 4.5, 0.75, 4.75, Pseq([0.25], 23), 2.75, 2.25, 1.25, 0.5, 2.75, 1.25, 2.5, 1.75, 1.75, 0.5, 2.5, 0.25, 3.5, 4.75, 2.25, 1.25, 5.25, 2.75, 5, 3.25, 4, 1.25, 4.75,  0.75, 7.5, 5.75, 4, 6.25, 3, 9.75, 0.25, 6.25, 6.5, 12, 4, 1, 4.75, 0.75, 7.5, 5.75, 4, 6.25, 0.25, 2.75, 9.75, 0.25, 6.25, 6.5, 11.75, 12, 4, 1.5, 4.75, 0.5, 7.5, 0.25, 5.25, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.5, 12, 4, 3.5, 3.5, 1.5, 4.75, 0.5, 7.5, 0.25, 5.5, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.5, 12, 4, 3.5, 4, 1.5, 4.75, 0.5, 7.5, 5.5, 4, 0.25, 6.25, 3.25, 9.75, 6.25, 0.25, 6.25, 12, 4], 1));
~moog.synth.addKey(\pieceEnd, \note, Pseq([1, \r, 1, 1, \r, 1, \r, 1, \r, 1, 1, 1, 1, \r, 1, 1, \r, 1, \r, 1, 1, \r, 1, 1, \r, 1, \r, 1, \r, -11, \r, -23, -11, -11, -11, 1, \r, -23, \r, 1, 1, 1, 1, 1, 1, 1, \r, 1, 1, 1, 1, 1, \r, 1, \r, 1, 1, 1, 1, 1, 1, 1, 1, \r, -23, -23, \r, -23, -23, -23, \r, 1, \r, 1, \r, -23, -23, 1, \r, 1, 1, 1, \r, 1, \r, 1, \r, 1, 1, 1, 1, 1, 1, \r, 1, 1, -11, -11, \r, -11, \r, -11, -11, -11, -11, \r, -11, -11, \r, -11, -11, -11, -11, -11, \r, -11, \r, -11, \r, -11, -11, \r, -11, -11, -11, -11, \r, -11, -11, -11, \r, -23, \r, -23, \r, -23, \r, -23, -23, \r, -23, -23, -23, -23, \r, -23, -23, -23, \r, -23, \r, -23, \r, -23, \r, -23, -23, \r, -23, -23, -23, -23, \r, -23, -23, -23], 1));

~moog.synth.addKey(\sectionEndLoop, \octave, 4);
~moog.synth.addKey(\sectionEndLoop, \legato, 0.99);
~moog.synth.addKey(\sectionEndLoop, \dur, Pseq([Pseq([0.25], 22), 0.5, 0.5, 0.25, 0.75, 0.25, 0.5, 0.5, Pseq([0.25], 14), 0.5, 0.5, 0.25, 0.25, 0.5, 0.25, 0.25, 0.75, 0.25, 0.5, 0.5, 0.25, 1, 0.75, 0.5, 0.75, 0.5, 1.25, 0.75, 0.75, 1.5, 0.5, 0.25, 0.5, 1, 0.75, 0.5, 0.75, 0.5, 1.25, 0.75, 0.75, 1.5, 1.5, 0.5, 0.25, 0.5, 0.25, 0.75, 0.75, 0.5, 0.75, 0.5, 1.25, 0.75, 0.75, 1.5, 0.5, 0.5, 0.5, 0.25, 0.5, 0.25, 0.75, 0.5], inf));
~moog.synth.addKey(\sectionEndLoop, \note, Pseq([1, 1, 1, 1, \r, 1, 1, 1, 1, 1, 1, 1, \r, 1, 1, 1, 1, 1, \r, 1, -11, \r, -23, -11, -11, -11, 1, -23, 1, \r, 1, 1, 1, 1, -23, -23, -23, -23, -23, \r, 1, 1, \r, -23, -23, 1, \r, 1, 1, \r, 1, \r, 1, 1, \r, 1, 1, 1, 1, 1, 1, 1, 1, -11, -11, \r, Pseq([-11], 12), \r, -11, \r, Pseq([-11], 10), \r, -23, \r, -23, \r, -23, -23], inf));


~moog.synth.stopSequence(\sequence2);


~moog.synth.playSequence(\sectionEnd);
~moog.synth.makeSequence(\pieceEnd);
~moog.synth.stopSequence(\sectionEnd);

~moog.synth.playSequence(\sequence3);
~moog.synth.stopSequence(\sequence3);

~moog.synth.playSequence(\sequence4);

~moog.synth.stopSequence(\sequence4);
~moog.synth.playSequnece(\sequence1);






~moog.synth.playSequence(\sequence1);
~moog.synth.stopSequence(\sequence1);

~moog.synth.playSequence(\sectionEnd);

~moog.synth.playSequence(\pieceEnd);

~prm.granulator.setGranulatorCrossfade(-1);
~prm.granulator.setDelayMix(1);

~moog.mixer.setSendVol(0, -12);
~moog.mixer.setSendVol(1, -12);
~moog.mixer.setSendVol(2, 0);

~prm.granulator.setDelayTime(1);
~prm.granulator.setFeedback(0.35);

~moog.synth.setSequencerClockTempo(2500);

s.meter;

~moog.mixer.setVol(-6);
~moog.mixer.tglMute;

y = { Out.ar(~prm.submixC, [SoundIn.ar(2), SoundIn.ar(2)]); }.play;

~prm.modular.setSendVol(0, -15);
~prm.modular.setSendVol(1, -3);


~moog.playSequence1;

~moog.playSequence2;

~moog.playSectionEnd
~moog.playPieceEnd;


~habit = Habit.new(1, 2, 3, "iConnectAudio4+", "DIN",
  ~prm.submixA, ~prm.submixB, ~prm.submixC,
  ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus,
  relGroup: ~prm.procGroup, addAction: \addToHead);

~habit.trumpet.mixer.tglMute;
~habit.trumpet.mixer.setSendVol(2, 0);

~habit.modular.tglMute;
~habit.moog;

~habit.mixerB.chanMono(0);

~habit.delay.mixer.tglMute;

~habit.moog.playSequence1(~habit.clock);
~habit.moog.playSectionEndLoop(~habit.clock);
~habit.moog.playSectionEnd(~habit.clock);
~habit.moog.playPieceEnd(~habit.clock);

~habit.clock.tempo = 2;

~habit.moog.stopAllSequences;

IM_Mixer_1Ch
s.meter


z = IM_HardwareIn.new(2, ~habit.mixerB.chanMono(0));

~habit.mixerB.setSendVol(0, 0, -6);
z.free;

~habit.modular.setSendVol(1, 0);
~habit.moog.mixer.setSendVol(1, -6);
~prm.granulator.setGrainEnvelope('rexpodec');
~prm.granulator.setGrainDur(0.01, 0.5);
~prm.granulator.setSync(0);


~habit.moog.mixer.setSendVol(0, -9);
~habit.modular.setSendVol(0, -9);

~habit.moog.mixer.setVol(-9);




