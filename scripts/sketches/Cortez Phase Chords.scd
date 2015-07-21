

x = Cortez.new(~prm.submixA, ~prm.submixB, ~prm.submixC, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, nil,
  ~prm.procGroup, \addToHead);


// interesting that base phase is q = 100

x.phaseSynth.mixer.setSendVol(0, -24);

x.phaseSynth.playSequence(\cortezPhase);
x.phaseSynth.playSequence(\cortezPhase2);
x.phaseSynth.playSequence(\cortezPhase3);
x.phaseSynth.playSequence(\cortezPhase4);
x.phaseSynth.playSequence(\cortezPhase5);

// 2
x.phaseSynth.addKey(\cortezPhase, \note, Pseq([-2], inf));
x.phaseSynth.addKey(\cortezPhase5, \note, Pseq([8], inf));
x.phaseSynth.addKey(\cortezPhase4, \note, Pseq([7], inf));
x.phaseSynth.addKey(\cortezPhase3, \note, Pseq([12], inf));
x.phaseSynth.addKey(\cortezPhase2, \note, Pseq([3], inf));


// 3
x.phaseSynth.addKey(\cortezPhase3, \note, Pseq([2], inf));
x.phaseSynth.addKey(\cortezPhase2, \note, Pseq([0], inf));
x.phaseSynth.addKey(\cortezPhase, \note, Pseq([-4], inf));
x.phaseSynth.addKey(\cortezPhase5, \note, Pseq([12], inf));
x.phaseSynth.addKey(\cortezPhase4, \note, Pseq([10], inf));

// 4:
x.phaseSynth.addKey(\cortezPhase, \note, Pseq([-5], inf));
x.phaseSynth.addKey(\cortezPhase4, \note, Pseq([8], inf));
x.phaseSynth.addKey(\cortezPhase3, \note, Pseq([5], inf));
x.phaseSynth.addKey(\cortezPhase5, \note, Pseq([10], inf));
x.phaseSynth.addKey(\cortezPhase2, \note, Pseq([2], inf));

// 1:
x.phaseSynth.addKey(\cortezPhase3, \note, Pseq([3], inf));
x.phaseSynth.addKey(\cortezPhase2, \note, Pseq([2], inf));
x.phaseSynth.addKey(\cortezPhase, \note, Pseq([0], inf));
x.phaseSynth.addKey(\cortezPhase4, \note, Pseq([5], inf));
x.phaseSynth.addKey(\cortezPhase5, \note, Pseq([7], inf));









x.phaseSynth.mixer.setSendVol(1, -70);