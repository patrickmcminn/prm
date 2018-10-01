/*
Saturday, April 15th 2017
FalseSelf.sc
prm
*/

FalseSelf : IM_Module {

  var <server, <isLoaded;

  var <clock, <bar, relBar;

  var <fakeGuitar, <bellSection, <melodySynth;
  var <bassSection, <modular, <modularInput;
  var <drums, <mainTrumpet, <mainTrumpetInput;
	var <trumpetCanon, <trumpetCanonInput, <drones, <sixteenthDrones;
  var <orchestra, <planeNoise, <midBuzz, <flute;
  var <trumpetMelody, <trumpetMelodyInput, <freezeGuitar;
  var <endTrumpet, <endTrumpetInput;

  var <metronome;

  var <modularRoutine;

  var <section1IsPlaying, <chorus1IsPlaying, <chorus2IsPlaying, <canonIsPlaying;
  var <limboIsPlaying, <melodyIsPlaying, <endIsPlaying;

  *new { | outBus, moogDevice, moogPort, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(16, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(moogDevice, moogPort);
  }

  prInit { | moogDevice, moogPort |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      this.prSetInitialPlayingConditions;

      // make sure trumpet doesn't take your head off when the song loads:
      mixer.mute(4);

      clock = TempoClock.new;
      server.sync;
      clock.tempo = 160/60;


      //// mixer:
      bellSection = FalseSelf_BellSection.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { bellSection.isLoaded } != true }, { 0.001.wait; });

      fakeGuitar = FalseSelf_FakeGuitar.new(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { fakeGuitar.isLoaded } != true }, { 0.001.wait; });

      melodySynth = FalseSelf_MelodySynth.new(mixer.chanStereo(2), group, \addToHead);
      while({ try { melodySynth.isLoaded } != true }, { 0.001.wait; });

      bassSection = FalseSelf_BassSection.new(mixer.chanStereo(3), relGroup: group, addAction: \addToHead,
        moogDeviceName: moogDevice, moogPortName: moogPort);
      while({ try { bassSection.isLoaded } != true }, { 0.001.wait; });

      mainTrumpet = FalseSelf_MainTrumpet.new(mixer.chanStereo(4), relGroup: group, addAction: \addToHead);
      while({ try { mainTrumpet.isLoaded } != true }, { 0.001.wait; });
      mainTrumpetInput = IM_HardwareIn.new(0, mainTrumpet.inBus, group, 'addToHead');
      while({ try { mainTrumpetInput.isLoaded } != true }, { 0.001.wait; });

      drums = FalseSelf_Kick.new(mixer.chanStereo(5), relGroup: group, addAction: \addToHead);
      while({ try { drums.isLoaded } != true }, { 0.001.wait; });

      trumpetCanon = FalseSelf_TrumpetCanon.new(mixer.chanStereo(6), relGroup: group, addAction: \addToHead);
      while({ try { trumpetCanon.isLoaded } != true }, { 0.001.wait; });
      trumpetCanonInput = IM_HardwareIn.new(0, trumpetCanon.inBus, group, \addToHead);
      while({ try { trumpetCanonInput.isLoaded } != true }, { 0.001.wait; });

      orchestra = FalseSelf_Orchestra.new(mixer.chanStereo(7), relGroup: group, addAction: \addToHead);
      while({ try { orchestra.isLoaded } != true }, { 0.001.wait; });

      trumpetMelody = FalseSelf_TrumpetMelody.new(mixer.chanStereo(8), relGroup: group, addAction: \addToHead);
      while({ try { trumpetMelody.isLoaded } != true }, { 0.001.wait; });
      trumpetMelodyInput = IM_HardwareIn.new(0, trumpetMelody.inBus, group, \addToHead);
      while({ try { trumpetMelodyInput.isLoaded } != true }, { 0.001.wait; });

      drones = FalseSelf_CrudeDrones.new(mixer.chanStereo(9), relGroup: group, addAction: \addToHead);
      while({ try { drones.isLoaded } != true }, { 0.001.wait; });

      sixteenthDrones = FalseSelf_16thDrones.new(mixer.chanStereo(10), relGroup: group, addAction: \addToHead);
      while({ try { sixteenthDrones.isLoaded } != true }, { 0.001.wait; });

      planeNoise = FalseSelf_PlaneNoise.new(mixer.chanStereo(11), relGroup: group, addAction: \addToHead);
      while({ try { planeNoise.isLoaded } != true }, { 0.001.wait; });

      midBuzz = FalseSelf_MidBuzz.new(mixer.chanStereo(12), relGroup: group, addAction: \addToHead);
      while({ try { midBuzz.isLoaded } != true }, { 0.001.wait; });

      freezeGuitar = FalseSelf_FreezeGtr.new(mixer.chanStereo(13), relGroup: group, addAction: \addToHead);
      while({ try { freezeGuitar.isLoaded } != true }, { 0.001.wait; });

      modular = IM_Mixer_1Ch.new(mixer.chanStereo(14), relGroup: group, addAction: \addToHead);
      while({ try { modular.isLoaded } != true }, { 0.001.wait; });
      modularInput = IM_HardwareIn.new(2, modular.chanMono, group, \addToHead);
      while({ try { modularInput.isLoaded } != true }, { 0.001.wait; });

      endTrumpet = FalseSelf_EndTrumpet.new(mixer.chanStereo(15), relGroup: group, addAction: \addToHead);
      while({ try { endTrumpet.isLoaded } != true }, { 0.001.wait; });
      endTrumpetInput = IM_HardwareIn.new(1, endTrumpet.inBus, group, \addToHead);
      while({ try { endTrumpetInput.isLoaded } != true }, { 0.001.wait; });


      metronome = Metronome.new(0, group, \addToHead);
      while({ try { metronome.isLoaded } != true }, { 0.001.wait;});


      server.sync;

      this.prMakeModularRoutine;
      this.prSetInitialParameters;

      relBar = 0;
      clock.schedAbs(clock.beats.ceil, { | beat | bar = (clock.bar - relBar) + 1; 1 });

      isLoaded = true;
    }
  }

  prSetInitialPlayingConditions {
    section1IsPlaying = false;
    chorus1IsPlaying = false;
    chorus2IsPlaying = false;
    canonIsPlaying = false;
    limboIsPlaying = false;
    melodyIsPlaying = false;
    endIsPlaying = false;

  }

  prMakeModularRoutine {
    modularRoutine = r {
      {
        bassSection.moog.assignableOut.triggerEnvelopeOneShot;
        ((1/clock.tempo)/4).wait;
      }.loop;
    };
  }

  prSetInitialParameters {
    this.prSetInitialMixerLevels;
    this.prSetAssignableOutParameters;
  }

  prSetInitialMixerLevels {
    mixer.setMasterVol(-6);

    // fake guitar:
    mixer.setVol(1, 0);

    // bells:
    mixer.setVol(0, -6);
    // trumpet:
    mixer.setVol(4, -6);
    mainTrumpet.mixer.setVol(-25);

    // melodySynth:
    mixer.setVol(2, -3);

    // basses:
    mixer.setVol(3, -3);
    bassSection.satur.mixer.setVol(-inf);
    bassSection.feedback.mixer.setVol(-inf);
    bassSection.moog.mixer.setVol(-inf);

    // drums:
    mixer.setVol(5, -6);
    mixer.setSendVol(5, 0, -21);


    // modular:
    //mixerC.setVol(0, -inf);
    mixer.setSendVol(14, 0, -15);

    // Trumpet Canon:
    mixer.setVol(6, -6);
    mixer.setSendVol(6, 0, -12);

    // plane noise:
    mixer.setVol(11, -18);
    mixer.setSendVol(11, 0, -10);

    // mid buzz:
    mixer.setVol(12, -12);
    mixer.setSendVol(12, 0, -10);

    // crude drones:
    mixer.setVol(9, -19);
    mixer.setSendVol(9, 0, 0);
    mixer.setSendVol(9, 2, 0);

    // 16th drones:
    mixer.setVol(10, -21);
    mixer.setSendVol(10, 0, -6);
    mixer.setSendVol(10, 2, 0);

    // orchestra:
    mixer.setVol(7, 0);
    mixer.setSendVol(7, 0, -18);

    // trumpet melody:
    mixer.setSendVol(8, 0, -12);
    mixer.mute(8);

    // end trumpet:
    mixer.setSendVol(15, 0, -6);
    mixer.setSendVol(15, 2, 0);
    endTrumpet.input.mute;
  }

  prSetAssignableOutParameters {
    bassSection.moog.assignableOut.setStaticValue(0);
    bassSection.moog.assignableOut.setAttackTime(0);
    bassSection.moog.assignableOut.setSustainTime(0);
    bassSection.moog.assignableOut.setReleaseTime((1/clock.tempo)/4);
    bassSection.moog.assignableOut.triggerEnvelopeOneShot;
  }

  prFadeModular { | start = -inf, end = 0, time = 10 |
    {
      var bus = Bus.control;
      server.sync;
      { Out.kr(bus, Line.kr(start.dbamp, end.dbamp, time, doneAction: 2)) }.play;
      modular.mapAmp(bus);
      { bus.free }.defer(time);
      { modular.setVol(end) }.defer(time);
    }.fork;
  }



  //////// public functions:

  free {

    fakeGuitar.free; bellSection.free; melodySynth.free;
    bassSection.free; modular.free; modularInput.free;
    drums.free; mainTrumpet.free; mainTrumpetInput.free;
    trumpetCanon.free; trumpetCanonInput.free; drones.free; sixteenthDrones.free;
    orchestra.free; planeNoise.free; midBuzz.free; flute.free;
    trumpetMelody.free; trumpetMelodyInput.free; freezeGuitar.free;
    endTrumpet.free; endTrumpetInput.free;
    metronome.free;
  }

  playModularRoutine { modularRoutine.play; }
  stopModularRoutine { modularRoutine.stop; }

  //////// song sequencing:

  startSong {
    clock.playNextBar({

      relBar = clock.bar;

      ///////////////////////////
      //// Clock Management: ////
      //////////////////////////

      //// tempo:
      // chorus tempo:
      clock.sched((55-1)*4, { clock.tempo = 106.66/60 });
      // chorus pt.2 tempo:
      clock.sched(268, { clock.tempo = 160/60 });

      // meter:
      clock.sched(224, { clock.beatsPerBar_(3) });
      clock.sched(242, { clock.beatsPerBar_(4) });
      clock.sched(250, { clock.beatsPerBar_(3) });
      clock.sched(268, { clock.beatsPerBar_(4) });
      clock.sched(276, { clock.beatsPerBar_(3) });
      clock.sched(294, { clock.beatsPerBar_(4) });
      clock.sched(302, { clock.beatsPerBar_(3) });
      clock.sched(412, { clock.beatsPerBar_(4) });

      ////////////////////
      //// Beginning: ////
      ///////////////////

      section1IsPlaying = true;
      //////// start FakeGuitar:
      fakeGuitar.section1.playSampleOneShot;

      ///////// Fake Guitar Modular:
      clock.sched((21-1)*4, {
        this.prFadeModular(-inf, 0, 4.5);
        this.playModularRoutine;
      });
      clock.sched((33-1)*4, { mixer.mute(14); });
      clock.sched((35-1)*4, { mixer.unMute(14); });
      clock.sched((43-1)*4, { mixer.mute(14); });
      clock.sched((47-1)*4, { mixer.unMute(14); });
      clock.sched((55-1)*4, { this.stopModularRoutine; mixer.setSendVol(1, 2, -inf); });

      //////// basses:
      clock.sched((41-1)*4, {
        bassSection.fadeMoog(0, 1, 15);
        bassSection.fadeSaturSynth(0, 1, 21);
        bassSection.fadeFeedbackSynth(0, 1, 21);
        bassSection.feedback.sweepFilter(30, 1880, 21);
      });
      clock.sched((41-1)*4, { bassSection.playPreChorus(clock); });

      //////// trumpet:
      clock.sched((25-1)*4, { mainTrumpet.fadeVolume(-25, -12, 12); });
      clock.sched((25-1)*4, { mixer.unMute(4); });


      //////// Melody Synth:
      clock.sched((23-1)*4, { melodySynth.fadeVolume(-inf, 0, 20) });
      clock.sched((23-1)*4, { melodySynth.playIntroSequence(clock) });

      /////// Drums:
      clock.sched((21-1)*4, { drums.fadeVolume(-inf, -3, 24); });
      clock.sched((21-1)*4, { drums.playSection1(clock); });

      // drums swell:
      clock.sched((53-1)*4, { drums.fadeVolume(-3, 0, 3); });

      ////////////////////
      //// Chorus: ////
      ///////////////////

      clock.sched((55-1)*4, { chorus1IsPlaying = true; });
      clock.sched(268, { chorus2IsPlaying = true; });

			// main trumpet:
      clock.sched((55-1)*4, { mainTrumpet.mixer.setVol(-6); });
			clock.sched((55-1)*4, { mainTrumpet.recordLoop });
      // chorus pt. 2:
			clock.sched(268, { mainTrumpet.playWarpedLoop(19.5); });

			// bass:
      clock.sched((55-1)*4, { bassSection.feedback.setFilterCutoff(5720); });
			clock.sched((55-1)*4, { bassSection.playChorus(clock); });

			// melody synth:
			clock.sched((55-1)*4, { melodySynth.playChorus(clock); });

			// drums:
			clock.sched((55-1)*4, { drums.playChorus1(clock); });
      // reverb send up:
      clock.sched((55-1)*4, { mixer.setSendVol(5, 0, -12) });
      clock.sched(268, { mixer.setSendVol(5, 0, -21); });
      clock.sched(268, { drums.playChorus2(clock) });

      // reverb send up at the end of Chorus 2:
      //clock.sched(316-1, { this.fadeMixerBSend(2, 0, -21, -11, 3); });

      ///////////////////////////
      //// post-chorus/canon ////
      //////////////////////////

      clock.sched(320, { canonIsPlaying = true });
      clock.sched(416, { limboIsPlaying = true });

      //// trumpet:
      // mute:
      clock.sched(320, { mixer.mute(4); });
      // free:
      clock.sched(323, { mainTrumpet.free; });

      //// bells:
      clock.sched(323, { bellSection.free; });

      //// modular:
      //clock.sched(327-1, { this.stopModularRoutine });

      //// trumpet canon:
      // unmute input (causes pop)
      //clock.sched(320, { trumpetCanon.unMuteInput; });
      // fade in input:
      clock.sched(320, { trumpetCanon.fadeInputAmp(0, 1, 0.5); });
      // mute delays:
      clock.sched(416, { trumpetCanon.muteDelays; });
      // high pass sweep:
      clock.sched(344, { trumpetCanon.postEQ.sweepHighPassFilter(30, 1200, 31.5); });
      clock.sched(440, { trumpetCanon.muteInput });


      //// drums:
      // play post chorus:
      clock.sched(320, {
        drums.playCanon(clock);
        drums.mixer.setVol(-6);
      });
      // sweep filter:
      //clock.sched(344, { drums.sweepFilter(30, 500, 36); });
      // send to granulator:
      //clock.sched(368, { this.fadeMixerBSend(2, 1, -inf, -6, 11.25); });
      clock.sched(376, { mixer.setSendVol(5, 1, -6) });


      //// fake guitar:
      // send to granulator:
      //clock.sched(320, { this.fadeMixerASend(0, 1, -inf, 0, 9); });
      clock.sched(320, { mixer.setSendVol(1, 1, -12) });
      // fade out:
      //clock.sched(320, { fakeGuitar.fadeVolume(0, -inf, 30); });


      //// basses:
      // feedback filter sweep:
      // CAUSES AWFUL CLICK
      //clock.sched(324-1, { bassSection.feedback.setFilterCutoff(3000); });
      // play sequence:
      clock.sched(320, { bassSection.playPostChorus; });
      // fade out satur:
      clock.sched(320, { bassSection.fadeSaturSynth(1, 0, 75); });
      // fade out moog:
      clock.sched(320, { bassSection.fadeMoog(1, 0, 50); });
      // fade out feedback:
      clock.sched(320, { bassSection.fadeFeedbackSynth(1, 0, 35); });


      clock.sched(520, { bassSection.stopPostChorus; });

      //// plane noise:
      clock.sched(320, { planeNoise.playSample; });

      //// mid buzz:
      clock.sched(320, { midBuzz.playSequence(clock) });
      // YOU ARE RESPONSIBLE FOR FADING OUT THE MID BUZZ
      //// Crude Drones:
      // play:
      clock.sched(320, {
        drones.playVoice1Sequence(clock);
        drones.playVoice2Sequence(clock);
        drones.playVoice3Sequence(clock);
      });

      //// orchestra:
      clock.sched(344, { orchestra.playMahlerPhrase });

    });
  }

  startMelody {
    clock.playNextBar({
      ///////////////////////////
      //// Clock Management: ////
      //////////////////////////

      //clock.tempo = 142.20/60;

      /////// time signature changes:
      clock.sched(20, { clock.beatsPerBar_(8); });
      //clock.sched(16, { clock.beatsPerBar_(12) });
      //clock.sched(28, { clock.beatsPerBar_(8) });
      //clock.sched(60, { clock.beatsPerBar_(12) });
      //clock.sched(72, { clock.beatsPerBar_(8) });
      //clock.sched(96, { clock.beatsPerBar_(12) });
      //clock.sched(108, { clock.beatsPerBar_(8) });
      clock.sched(48+20, { clock.beatsPerBar_(12) });
      clock.sched(60+20, { clock.beatsPerBar_(8) });
      clock.sched(66+20, { clock.beatsPerBar_(6) });
      clock.sched(72+20, { clock.beatsPerBar_(8) });

      //////////////////////////
      //// Trumpet Melody: ////
      ////////////////////////

      melodyIsPlaying = true;
      clock.sched(82+20, { endIsPlaying = true; });

      //////// trumpet melody:
      //clock.sched(20, { trumpetMelody.playPattern(clock); });
      clock.sched(20, { trumpetMelody.dry.unMute; });
      clock.sched(20, { trumpetMelody.shift2.unMute; });
      //clock.sched(20, { trumpetMelody.staticShift.mixer.mute; });

      //////// freeze guitar:
      // swell:
      freezeGuitar.mixer.setVol(-inf);
      freezeGuitar.fadeVolume(-inf, 0, 10);
      // chord progression:
      freezeGuitar.playChordProgression(clock);
      // end progression:
      clock.sched(82+20, { freezeGuitar.playEndProgression(clock); });

      //////// bass:
      bassSection.satur.mixer.setVol(0);
      bassSection.moog.mixer.setVol(0);
      bassSection.feedback.mixer.setVol(0);
      // end melody:
      clock.sched(20, { bassSection.playEnd(clock); });
      // coda:
      clock.sched(82+20, { bassSection.playCoda(clock); });

      ///// fake guitar:
      clock.sched(16+20, { mixer.setSendVol(1, 1, 0); });
      clock.sched(16+20, { fakeGuitar.mixer.setVol(1, -9); });
      clock.sched(16+20, { fakeGuitar.section2.setFilterCutoff(33); });
      clock.sched(16+20, { fakeGuitar.section2.playSampleOneShot; });
      clock.sched(16+20, { fakeGuitar.section2.sweepFilter(33, 539, 37.1); });
      clock.sched(264+20, { fakeGuitar.section2.sweepFilter(539, 33, 5.1) });

      /*
      //// 16th drones:
      clock.sched(68, {
        sixteenthDrones.playVoice1Sequence(clock);
        sixteenthDrones.playVoice2Sequence(clock);
        sixteenthDrones.playVoice3Sequence(clock);
      });
      */

      //// end drums:
      drums.setHighPassCutoff(60);
      clock.sched(82+20, { drums.mixer.setVol(-inf); });
      clock.sched(82+20, {
        drums.playEnding(clock);
        drums.fadeVolume(-inf, 0, 20);
        mixer.setSendVol(5, 1, -17);
        mixer.setSendVol(5, 0, -30);
      });
    });
  }

}