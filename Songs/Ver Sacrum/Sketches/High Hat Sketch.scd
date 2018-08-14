/*
High Hat Sketch
*/


~prm = AudioSystem.new(2);

(
~hat = Subtractive.new(~prm.submixA,
  ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modular.inBus, ~prm.delay.inBus,
  ~prm.procGroup, \addToHead);
)

~hat.playNote(59.midicps);
~hat.releaseNote(59.midicps);

~hat.setFilterCutoff(900);
~hat.setFilterType('highPass');

~hat.setAttackTime(0.01);
~hat.setReleaseTime(0);
~hat.setSustainLevel(0);
~hat.setDecayTime(0.05);
~hat.setFilterDrive(50);
~hat.setNoiseOscVol(0);

~hat.mixer.setSendVol(0, -21);
~hat.mixer.setSendVol(1, -18);
~hat.mixer.setSendVol(3, -inf);

//~prm.delay.mixer.setSendVol(0, -12);

~prm.delay.setMix(1);
~prm.delay.setDelayTime((60/160)*2);

~hat.sequencerClock.tempo = 160/60;

~hat.makeSequence(\seq1);
~hat.makeSequence(\seq2);

(
var p = Prand([-1, 1, 2], 1);
~hat.addKey(\seq1, \dur, 0.25);
~hat.addKey(\seq1, \octave, 4);
~hat.addKey(\seq1, \note, Pseq([p, p, p, p, p, Rest, p, p, p, Rest, p], inf));
~hat.addKey(\seq1, \pan, -1);
//~hat.addKey(\seq1, \freq, 220)
)

(
var p = Prand([6, 7, 9], 1);
~hat.addKey(\seq2, \dur, 0.25);
~hat.addKey(\seq2, \octave, 4);
~hat.addKey(\seq2, \note, Pseq([p, p, p, p, p, p, p, Rest, p, p, p, Rest], inf));
~hat.addKey(\seq2, \pan, 1);
//~hat.addKey(\seq1, \freq, 220)
)



~hat.playSequence(\seq1);
~hat.playSequence(\seq2);


~hat.stopSequence(\seq1);

~hat.playSequence(\test);

~hat.makeSequence(\test);
~hat.addKey(\test, \dur, 0.25);
~hat.addKey(\test, \note, Pseq([0, 2, 4, 5], inf));
