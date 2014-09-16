/*
Wednesday, September 3rd
Lucky.sc
prm
temp title for a new song
*/

Lucky : IM_Module {

  var server;
  var <isLoaded;
  var <granulator, <reverb, <poppy;
  var <melodySubtractive, <basslineSubtractive, <fakeGuitar, <tptIn, hardwareIn, <nebula;
  var <tempoClock;
  var <drone, <trumpetSequencer, <arpFreeze;

  var muteSplitter;

  var <isIntroPlaying, <isSection1Playing, <isBreakPlaying, <isSection2Playing, <isOutPlaying, <introVersion;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(8, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(outBus, relGroup, addAction);
  }

  prInit { | outBus = 0  |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait });

      this.prAddSynthDef;

      //irLib = IM_IRLibrary.new;
      //while({ try { irLib.isLoaded } != true }, { 0.001.wait; });
      granulator = IM_Granulator.new(mixer.chanStereo(5), relGroup: group, addAction: \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });
      reverb = IM_Reverb.new(mixer.chanStereo(5), amp: 1, mix: 1, roomSize: 0.9, damp: 1, relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });
      poppy = PoppyNoSynth.new(mixer.chanStereo(5), granulator.inBus, reverb.inBus,  relGroup: group, addAction: \addToHead);
      while({ try { poppy.isLoaded } != true }, { 0.001.wait; });

      ////synths:
      melodySubtractive = Subtractive.new(mixer.chanStereo(0), granulator.inBus, reverb.inBus, poppy.inBus,
        relGroup: group, addAction: \addToHead);
      while({ try { melodySubtractive.isLoaded } != true }, { 0.001.wait; });
      basslineSubtractive = Subtractive.new(mixer.chanStereo(1), granulator.inBus, reverb.inBus, poppy.inBus,
        relGroup: group, addAction: \addToHead);
      while({ try { basslineSubtractive.isLoaded } != true }, { 0.001.wait; });
      fakeGuitar = FakeGuitar.new(mixer.chanStereo(2), granulator.inBus, reverb.inBus, poppy.inBus, relGroup: group, addAction: \addToHead);
      while({ try { fakeGuitar.isLoaded } != true }, { 0.001.wait; });

      //// trumpet:
      tptIn = IM_Mixer_1Ch.new(mixer.chanStereo(3), granulator.inBus, reverb.inBus, poppy.inBus, nil, false, group, \addToHead);
      while({ try { tptIn.isLoaded } != true }, { 0.001.wait; });
      tptIn.chanMono;
      hardwareIn = IM_HardwareIn.new(1, tptIn.inBus, group, \addToHead);
      while({ try { hardwareIn.isLoaded } !=true }, { 0.001.wait; });
      nebula = TrumpetNebula.new(mixer.chanStereo(4), granulator.inBus, reverb.inBus, poppy.inBus, relGroup: group, addAction: \addToHead);
      while({ try { nebula.isLoaded } != true }, { 0.001.wait; });
      //muteIn = IM_HardwareIn.new(0, nebula.inBus, group, \addToHead);
      //while({ try { muteIn.isLoaded } != true }, { 0.001.wait; });

      // intro horns:
      drone = FreezeDrone.new(mixer.chanStereo(6), granulator.inBus, reverb.inBus, poppy.inBus, nil,  group, \addToHead);
      while({ try { drone.isLoaded } != true}, { 0.001.wait; });
      trumpetSequencer = TrumpetSequencer.new(mixer.chanStereo(7), granulator.inBus, reverb.inBus, poppy.inBus, nil,  group, \addToHead);
      while({ try { trumpetSequencer.isLoaded } != true}, { 0.001.wait; });
      arpFreeze = IM_GrainFreeze.new(trumpetSequencer.inBus, relGroup: group, addAction: \addToHead);
      while({ try { arpFreeze.isLoaded } != true }, { 0.001.wait; });

      muteSplitter = Synth(\prm_lucky_muteSplitter, [\inBus, 0, \outBus1, nebula.inBus, \outBus2, drone.inBus,
        \outBus3, trumpetSequencer.inBus, \outBus4, arpFreeze.inBus], group, \addToHead);

      server.sync;

      tempoClock = TempoClock.new(1.6);

      // setting synth parameters:
      this.prSetMelodySubtractiveParameters;
      this.prSetFakeGuitarParameters;
      this.prSetBasslineSubtractiveParameters;
      this.prSetNebulaParameters;

      this.prMakeMelodySubtractiveSequences;
      this.prMakeFakeGuitarSequences;


      this.prSetTrumpetParameters;

      this.prSetIntroTrumpetParameters;

      isIntroPlaying = false;
      isSection1Playing = false;
      isBreakPlaying = false;
      isSection2Playing = false;
      isOutPlaying = false;
      introVersion = "short";

      isLoaded = true;
    }
  }

  prAddSynthDef {
   SynthDef(\prm_lucky_muteSplitter, {
      | inBus = 0, outBus1 = 0, outBus2 = 1, outBus3 = 2, outBus4 = 3 |
      var sig = SoundIn.ar(inBus);
      Out.ar(outBus1, sig);
      Out.ar(outBus2, sig);
      Out.ar(outBus3, sig);
      Out.ar(outBus4, sig);
    }).add;
  }

  prSetTrumpetParameters {
    //trumpet parameters:
    tptIn.setSendVol(1, 0);
    tptIn.setSendVol(2, 0);
    //granulator.setRate(0.5, 0.5);
    poppy.setTrigRate(0.7);
    poppy.setSustainTime(0.1, 1.7);
    poppy.setAttackTime(2);
    poppy.setReleaseTime(2);
    poppy.setPlayRate(0.5);
    tptIn.setVol(-70);
    nebula.mixer.setVol(-70);
  }

  prSetIntroTrumpetParameters {
    fork {
      var seq = trumpetSequencer.shiftSequencer;
      seq.makeSequence(\luckyFirstArp);
      seq.makeSequence(\luckySecondArp);
      seq.makeSequence(\luckyThirdArp);
      seq.makeSequence(\luckyFourthArp);
      server.sync;

      seq.setSequenceQuant(\luckyFirstArp, 0);
      seq.setSequenceQuant(\luckySecondArp, 0);
      seq.setSequenceQuant(\luckyThirdArp, 0);
      seq.setSequenceQuant(\luckyFourthArp, 0);
      server.sync;

      seq.addKey(\luckyFirstArp, \dur, 1/6);
      seq.addKey(\luckyFirstArp, \shiftAmount, Pseq([-12, -5, 0, 4, 0, -5, -12], inf));
      seq.addKey(\luckyFirstArp, \legato, 1);

      seq.addKey(\luckySecondArp, \dur, 1/6);
      seq.addKey(\luckySecondArp, \shiftAmount, Pseq([-12, -5, 0, 4, 5, 4, 0, -5, -12], inf));
      seq.addKey(\luckySecondArp, \legato, 1);
      seq.addKey(\luckySecondArp, \amp, 0);

      seq.addKey(\luckyThirdArp, \dur, 1/6);
      seq.addKey(\luckyThirdArp, \shiftAmount, Pseq([-12, -8, -5, -1, 0, -1, -5, -8], inf));
      seq.addKey(\luckyThirdArp, \legato, 1);
      seq.addKey(\luckyThirdArp, \amp, 0);

      seq.addKey(\luckyFourthArp, \dur, 1/6);
      seq.addKey(\luckyFourthArp, \shiftAmount, Prand([11, 12, 14, 9, 7, 5], inf));
      seq.addKey(\luckyFourthArp, \legato, 1);
      seq.addKey(\luckyFourthArp, \amp, 0);

      seq.setAttackTime(0.3);
      seq.setReleaseTime(0.3);

      arpFreeze.setAttack(3);
      arpFreeze.setRelease(3);
    }
  }

  prSetMelodySubtractiveParameters {
    melodySubtractive.setLFO1Freq(12.632);
    melodySubtractive.setLFO1Waveform('sampleAndHold');
    melodySubtractive.setAmplitudeLFOBottom(0);
    melodySubtractive.setAmplitudeLFOTop(1);
    melodySubtractive.setOsc2Waveform('tri');
    melodySubtractive.setOsc1Octave(6);
    melodySubtractive.setOsc2Octave(6);
    melodySubtractive.setOsc2Vol(0);
    melodySubtractive.setNoiseOscCutoff(7000)
  }

  prSetFakeGuitarParameters {
    fakeGuitar.setVol(0);
    fakeGuitar.setCutoff(7500);
    fakeGuitar.setVibratoDepth(0);
    fakeGuitar.setFlangerMix(0.5);
    fakeGuitar.setFlangerSpeed(0.05);
    fakeGuitar.setFlangerFeedback(0.3);
    fakeGuitar.setWaveLossAmount(0);
    fakeGuitar.setFlangerWaveform('noise');
  }

  prSetBasslineSubtractiveParameters {
    basslineSubtractive.setAttackTime(2.5);
    basslineSubtractive.setReleaseTime(2.5);
    basslineSubtractive.setFilterCutoff(750);
    //basslineSubtractive.setAmplitudeLFO2Bottom(0);
    basslineSubtractive.setOsc2Waveform('tri');
    basslineSubtractive.setOsc2Vol(0);
    basslineSubtractive.setOsc1SubVol(0.2);
    basslineSubtractive.setOsc2SubVol(0.2);
    basslineSubtractive.setOsc1Octave(3);
    basslineSubtractive.setOsc2Octave(2);
    basslineSubtractive.setLFO2Waveform('saw');
    //basslineSubtractive.setLFO2Freq(5.8649353972248);
  }

  prSetNebulaParameters {
    nebula.setPitchShiftAmount(-12);
    nebula.setDelayVol(-70);
    nebula.setReverbDamp(1);
    nebula.setReverbRoom(0.85);
    nebula.setCutoff(5000);
  }

  prMakeMelodySubtractiveSequences {
    {
      melodySubtractive.makeSequence(\luckyIntro);
      melodySubtractive.makeSequence('\luckySection1');
      melodySubtractive.makeSequence(\luckyBreak);
      melodySubtractive.makeSequence(\luckySection2);
      melodySubtractive.makeSequence(\luckyOut);

      server.sync;

      melodySubtractive.setSequenceQuant(\luckyIntro, 0);
      melodySubtractive.setSequenceQuant(\luckySection1, 0);
      melodySubtractive.setSequenceQuant(\luckyBreak, 0);
      melodySubtractive.setSequenceQuant(\luckySection2, 0);
      melodySubtractive.setSequenceQuant(\luckyOut, 0);

      server.sync;

      melodySubtractive.addKey(\luckyIntro, \legato, 1);
      melodySubtractive.addKey(\luckyIntro, \note, Pseq([ [3, 5, 7]], inf));
      melodySubtractive.addKey(\luckyIntro, \dur, Pseq([8, 8], inf));

      melodySubtractive.addKey(\luckySection1, \legato, Pwhite(0.85, 1.05, inf));
      melodySubtractive.addKey(\luckySection1, \dur, Pseq([
        Pwhite(0.2, 0.27, 16), Pwhite(0.33, 0.38, 16), Pwhite(0.17, 0.22, 12), Pwhite(0.19, 0.29, 20)], inf));
      melodySubtractive.addKey(\luckySection1, \note, Pseq([
        Pseq([[3, 5, 7]], 16), Pseq([[1, 6, 8]], 16), Pseq([[-2, 0, 5]], 12), Pseq([[3, 5, 10]], 20)], inf));
      melodySubtractive.addKey(\luckySection1, \amp, Pwhite(0.09, 0.15, inf));
      melodySubtractive.addKey(\luckySection1, \filterCutoff, Pwhite(1200, 2500, inf));
      melodySubtractive.addKey(\luckySection1, \osc1Amp, Pwhite(0.7, 1, inf));
      melodySubtractive.addKey(\luckySection1, \osc2Amp, Pwhite(0.7, 1, inf));

      melodySubtractive.addKey(\luckyBreak, \legato, Pwhite(0.8, 0.9, inf));
      melodySubtractive.addKey(\luckyBreak, \note, Pseq([ Pseq([8], 16), Pseq([[8, 10]], 18), Pseq([[7, 8, 10]], 32) ], inf));
      melodySubtractive.addKey(\luckyBreak, \dur, Pwhite(0.155, 0.167, inf));
      melodySubtractive.addKey(\luckyBreak, \osc1OctaveMul, 2);
      melodySubtractive.addKey(\luckyBreak, \osc2OctaveMul, 4);
      melodySubtractive.addKey(\luckyBreak, \osc1SubAmp, Prand([0, 0, 0, 0, 0, 0, 0, Pstutter(5, 0.1), 0.15], inf));
      melodySubtractive.addKey(\luckyBreak, \filterType, Pwhite(0, 0.7, inf));
      melodySubtractive.addKey(\luckyBreak, \amp, 0.09);

      melodySubtractive.addKey(\luckySection2, \legato, 1);
      melodySubtractive.addKey(\luckySection2, \dur,
        Pseq([ Pseq([0.25], 4), Pseq([0.23], 5), Pseq([0.21], 7), Pseq([0.17], 6), Pseq([0.19], 4), Pseq([0.21], 5), Pseq([0.22], 7), Pseq([0.25], 6)], inf));
      melodySubtractive.addKey(\luckySection2, \note, Pseq([
        Pseq([[-21, -9,  3, 5, 7] ], 4), Pseq([[ -23, -11, 1, 6, 8]], 5), Pseq([[ -26, -14, -2, 0, 5]], 7), Pseq([[-31, -19, 3, 5, 10]], 6)], inf));
      melodySubtractive.addKey(\luckySection2, \amp, 0.15);
      melodySubtractive.addKey(\luckySection2, \filterCutoff, Pbrown(2500, 3000, 100, inf));
      melodySubtractive.addKey(\luckySection2, \osc1OctaveMul, 1);
      melodySubtractive.addKey(\luckySection2, \osc2Waveform, 2);
      melodySubtractive.addKey(\luckySection2, \osc1SubAmp, 0.2);
      melodySubtractive.addKey(\luckySection2, \osc2SubAmp, 0.2);

      melodySubtractive.addKey(\luckyOut, \dur, 0.2);
      melodySubtractive.addKey(\luckyOut, \note, [1, 13]);
      melodySubtractive.addKey(\luckyOut, \legato, 0.4);
      melodySubtractive.addKey(\luckyOut, \drive, 1000);
      melodySubtractive.addKey(\luckyOut, \filterCutoff, Pbrown(1000, 3000, 100, inf));
      melodySubtractive.addKey(\luckyOut, \osc1SubAmp, 1);
      melodySubtractive.addKey(\luckyOut, \osc2SubAmp, 1);
      melodySubtractive.addKey(\luckyOut, \amp, 0.3);
      melodySubtractive.addKey(\luckyOut, \osc1OctaveMul, 0.5);
      melodySubtractive.addKey(\luckyOut, \osc2OctaveMul, 0.5);
    }.fork
  }

  prMakeFakeGuitarSequences {
    {
      fakeGuitar.makeSequence('luckyFGBassline');
      fakeGuitar.makeSequence('luckyFGOut');

      server.sync;

      fakeGuitar.setSequenceQuant('luckyFGBassline', 0);
      fakeGuitar.setSequenceQuant('luckyFGOut', 0);

      fakeGuitar.addKey(\luckyFGBassline, \legato, 1);
      fakeGuitar.addKey(\luckyFGBassline, \note, Pseq([3, 1, -2, -7], inf));
      fakeGuitar.addKey(\luckyFGBassline, \dur, Pseq([1, 1.15, 1.47, 1.02, 0.76, 1.05, 1.54, 1.5], inf));
      fakeGuitar.addKey(\luckyFGBassline, \octave, 3);

      fakeGuitar.addKey(\luckyFGOut, \dur, 0.2);
      fakeGuitar.addKey(\luckyFGOut, \note, 1);
      fakeGuitar.addKey(\luckyFGOut, \octave, 3);
      fakeGuitar.addKey(\luckyFGOut, \legato, 0.6);
    }.fork;
  }


  //////// public methods:

  free {
    granulator.free;
    granulator = nil;
    reverb.free;
    reverb = nil;
    //irLib.free;
    //irLib = nil;
    poppy.free;
    poppy = nil;
    tptIn.free;
    tptIn = nil;
    hardwareIn.free;
    hardwareIn = nil;
    melodySubtractive.free;
    melodySubtractive = nil;
    basslineSubtractive.free;
    basslineSubtractive = nil;
    fakeGuitar.free;
    fakeGuitar = nil;
    nebula.free;
    nebula = nil;
    //muteIn.free;
   // muteIn = nil;
    drone.free;
    drone = nil;
    trumpetSequencer.free;
    trumpetSequencer = nil;
    arpFreeze.free;
    arpFreeze = nil;
    muteSplitter.free;
    muteSplitter = nil;
    tempoClock.free;
    tempoClock = nil;
    this.freeModule;
  }

  setMelodySubtractiveIntroPreset { this.prSetMelodySubtractiveParameters }
  setMelodySubtractiveSection1Preset { melodySubtractive.setAmplitudeLFOBottom(1); }
  setMelodySubtractiveBreakPreset {
    melodySubtractive.setFilterCutoffLFO2BottomRatio(0.85);
    melodySubtractive.setFilterCutoffLFO2TopRatio(1.5);
    melodySubtractive.setLFO2Freq(0.1);
    melodySubtractive.setFilterCutoff(1200);
  }
  setMelodySubtractiveSection2Preset { melodySubtractive.setFilterDrive(10); }

  playIntroSequence {
    melodySubtractive.playSequence(\luckyIntro, tempoClock);
    isIntroPlaying = true;
  }
  setIntroSequenceLong {
    melodySubtractive.addKey(\luckyIntro, \note, Pseq([ [3, 5, 7], [1, 6, 8]], inf));
    introVersion = "long";
  }
  setIntroSequenceShort {
    melodySubtractive.addKey(\luckyIntro, \note, Pseq([ [3, 5, 7]], inf));
    introVersion = "short";
  }
  playSection1Sequence {
    melodySubtractive.playSequence(\luckySection1, tempoClock);
    isSection1Playing = true;
  }
  playBreakSequence {
    melodySubtractive.playSequence(\luckyBreak, tempoClock);
    isBreakPlaying = true;
  }
  playSection2Sequence {
    melodySubtractive.playSequence(\luckySection2, tempoClock);
    fakeGuitar.playSequence(\luckyFGBassline, tempoClock);
    isSection2Playing = true;
  }
  playOutSection {
    melodySubtractive.playSequence(\luckyOut, tempoClock);
    fakeGuitar.playSequence(\luckyFGOut, tempoClock);
    isOutPlaying = true;
  }

  stopIntroSequence {
    melodySubtractive.stopSequence(\luckyIntro);
    isIntroPlaying = false;
  }
  stopSection1Sequence {
    melodySubtractive.stopSequence(\luckySection1);
    isSection1Playing = false;
  }
  stopBreakSequence {
    melodySubtractive.stopSequence(\luckyBreak);
    isBreakPlaying = false;
  }
  stopSection2Sequence {
    melodySubtractive.stopSequence(\luckySection2);
    fakeGuitar.stopSequence(\luckyFGBassline);
    isSection2Playing = false;
  }
  stopOutSection {
    melodySubtractive.stopSequence(\luckyOut);
    fakeGuitar.stopSequence(\luckyFGOut);
    isOutPlaying = false;
  }

}