/*
Saturday, April 15th 2017
FalseSelf.sc
prm
*/

FalseSelf : Song {

  var <server, <isLoaded;

  var <clock, <bar, relBar;

  var <fakeGuitar, <bellSection, <melodySynth;
  var <bassSection, <modular, <modularInput;
  var <drums, <mainTrumpet, <mainTrumpetInput;
	var <trumpetCanon, <trumpetCanonInput, <drones, <sixteenthDrones;
  var <orchestra, <planeNoise, <midBuzz, <flute;
  var <trumpetMelody, <trumpetMelodyInput, <freezeGuitar;

  var <modularRoutine;

  var <section1IsPlaying, <chorus1IsPlaying, <chorus2IsPlaying, <canonIsPlaying;
  var <limboIsPlaying, <melodyIsPlaying, <endIsPlaying;

  *new { | mixAOutBus, mixBOutBus, mixCOutBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(mixAOutBus, 8, mixBOutBus, 8, mixCOutBus, 8, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixerC.isLoaded } != true }, { 0.001.wait; });

      this.prSetInitialPlayingConditions;

      // make sure trumpet doesn't take your head off when the song loads:
      mixerA.tglMute(2);

      clock = TempoClock.new;
      server.sync;
      clock.tempo = 160/60;

      //// Mixer A:

      fakeGuitar = FalseSelf_FakeGuitar.new(mixerA.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { fakeGuitar.isLoaded } != true }, { 0.001.wait; });

      bellSection = FalseSelf_BellSection.new(mixerA.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { bellSection.isLoaded } != true }, { 0.001.wait; });

      mainTrumpet = FalseSelf_MainTrumpet.new(mixerA.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { mainTrumpet.isLoaded } != true }, { 0.001.wait; });
      mainTrumpetInput = IM_HardwareIn.new(0, mainTrumpet.inBus, group, 'addToHead');
      while({ try { mainTrumpetInput.isLoaded } != true }, { 0.001.wait; });

      trumpetCanon = FalseSelf_TrumpetCanon.new(mixerA.chanStereo(3), relGroup: group, addAction: \addToHead);
      while({ try { trumpetCanon.isLoaded } != true }, { 0.001.wait; });
      trumpetCanonInput = IM_HardwareIn.new(0, trumpetCanon.inBus, group, \addToHead);
      while({ try { trumpetCanonInput.isLoaded } != true }, { 0.001.wait; });

      trumpetMelody = FalseSelf_TrumpetMelody.new(mixerA.chanStereo(4), relGroup: group, addAction: \addToHead);
      while({ try { trumpetMelody.isLoaded } != true }, { 0.001.wait; });
      trumpetMelodyInput = IM_HardwareIn.new(0, trumpetMelody.inBus, group, \addToHead);
      while({ try { trumpetMelodyInput.isLoaded } != true }, { 0.001.wait; });


      //// Mixer B:

      melodySynth = FalseSelf_MelodySynth.new(mixerB.chanStereo(0), group, \addToHead);
      while({ try { melodySynth.isLoaded } != true }, { 0.001.wait; });

      bassSection = FalseSelf_BassSection.new(mixerB.chanStereo(1), relGroup: group, addAction: \addToHead,
        moogDeviceName: "iConnectAudio4+", moogPortName: "DIN");
      while({ try { bassSection.isLoaded } != true }, { 0.001.wait; });

      drums = FalseSelf_Kick.new(mixerB.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { drums.isLoaded } != true }, { 0.001.wait; });

      orchestra = FalseSelf_Orchestra.new(mixerB.chanStereo(3), relGroup: group, addAction: \addToHead);
      while({ try { orchestra.isLoaded } != true }, { 0.001.wait; });

      freezeGuitar = FalseSelf_FreezeGtr.new(mixerB.chanStereo(4), relGroup: group, addAction: \addToHead);
      while({ try { freezeGuitar.isLoaded } != true }, { 0.001.wait; });

      //// Mixer C:

      modular = IM_Mixer_1Ch.new(mixerC.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { modular.isLoaded } != true }, { 0.001.wait; });
      modularInput = IM_HardwareIn.new(2, modular.chanMono, group, \addToHead);
      while({ try { modularInput.isLoaded } != true }, { 0.001.wait; });

      drones = FalseSelf_CrudeDrones.new(mixerC.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { drones.isLoaded } != true }, { 0.001.wait; });

      sixteenthDrones = FalseSelf_16thDrones.new(mixerC.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { sixteenthDrones.isLoaded } != true }, { 0.001.wait; });

      planeNoise = FalseSelf_PlaneNoise.new(mixerC.chanStereo(3), relGroup: group, addAction: \addToHead);
      while({ try { planeNoise.isLoaded } != true }, { 0.001.wait; });

      midBuzz = FalseSelf_MidBuzz.new(mixerC.chanStereo(4), relGroup: group, addAction: \addToHead);
      while({ try { midBuzz.isLoaded } != true }, { 0.001.wait; });


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
    mixerA.setMasterVol(-6);
    mixerB.setMasterVol(-6);
    mixerC.setMasterVol(-6);

    // fake guitar:
    mixerA.setVol(0, -6);
    mixerA.setSendVol(0, 2, 0);
    // bells:
    mixerA.setVol(1, -6);
    // trumpet:
    mixerA.setVol(2, -6);
    mainTrumpet.mixer.setVol(-25);

    // melodySynth:
    mixerB.setVol(0, -12);

    // basses:
    mixerB.setVol(1, -3);
    bassSection.satur.mixer.setVol(-inf);
    bassSection.feedback.mixer.setVol(-inf);
    bassSection.moog.mixer.setVol(-inf);

    // drums:
    mixerB.setVol(2, -6);
    mixerB.setSendVol(2, 0, -21);


    // modular:
    //mixerC.setVol(0, -inf);
    mixerC.setSendVol(0, 0, -15);

    // Trumpet Canon:
    mixerA.setVol(3, -5);
    mixerA.setSendVol(3, 0, -12);

    // plane noise:
    mixerC.setVol(3, -24);
    mixerC.setSendVol(3, 0, -10);

    // mid buzz:
    mixerC.setVol(4, -12);
    mixerC.setSendVol(4, 0, -10);

    // crude drones:
    mixerC.setVol(1, -12);
    mixerC.setSendVol(1, 0, 0);
    mixerC.setSendVol(1, 2, 0);

    // 16th drones:
    mixerC.setVol(2, -18);
    mixerC.setSendVol(2, 0, -6);
    mixerC.setSendVol(2, 2, 0);

    // orchestra:
    mixerB.setVol(3, -9);
    mixerB.setSendVol(3, 0, -10);

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

  fadeMixerASend { | chan = 0, send = 0, start = -inf, end = 0, time = 10 |
    {
      var bus = Bus.control;
      server.sync;
      { Out.kr(bus, Line.kr(start.dbamp, end.dbamp, time, doneAction: 2)) }.play;
      switch(send,
        0, { mixerA.mapSend0Amp(chan, bus); },
        1, { mixerA.mapSend1Amp(chan, bus); },
        2, { mixerA.mapSend2Amp(chan, bus); },
        3, { mixerA.mapSend3Amp(chan, bus); }
      );
      { mixerA.setSendVol(chan, send, end); }.defer(time);
      { bus.free; }.defer(time);
    }.fork;
  }

  fadeMixerBSend { | chan = 0, send = 0, start = -inf, end = 0, time = 10 |
    {
      var bus = Bus.control;
      server.sync;
      { Out.kr(bus, Line.kr(start.dbamp, end.dbamp, time, doneAction: 2)) }.play;
      switch(send,
        0, { mixerB.mapSend0Amp(chan, bus); },
        1, { mixerB.mapSend1Amp(chan, bus); },
        2, { mixerB.mapSend2Amp(chan, bus); },
        3, { mixerB.mapSend3Amp(chan, bus); }
      );
      { mixerB.setSendVol(chan, send, end); }.defer(time);
      { bus.free; }.defer(time);
    }.fork;
  }

  fadeMixerCSend { | chan = 0, send = 0, start = -inf, end = 0, time = 10 |
    {
      var bus = Bus.control;
      server.sync;
      { Out.kr(bus, Line.kr(start.dbamp, end.dbamp, time, doneAction: 2)) }.play;
      switch(send,
        0, { mixerC.mapSend0Amp(chan, bus); },
        1, { mixerC.mapSend1Amp(chan, bus); },
        2, { mixerC.mapSend2Amp(chan, bus); },
        3, { mixerC.mapSend3Amp(chan, bus); }
      );
      { mixerC.setSendVol(chan, send, end); }.defer(time);
      { bus.free; }.defer(time);
    }.fork;
  }


  //////// public functions:

  free {

  }

  playModularRoutine { modularRoutine.play; }
  stopModularRoutine { modularRoutine.stop.reset; }

  //////// song sequencing:

  startSong {
    clock.playNextBar({

      relBar = clock.bar;

      ///////////////////////////
      //// Clock Management: ////
      //////////////////////////

      //// tempo:
      // chorus tempo:
      clock.sched((55*4)-1, { clock.tempo = 106.66/60 });
      // chorus pt.2 tempo:
      clock.sched(272-1, { clock.tempo = 160/60 });

      // meter:
      clock.sched(228-1, { clock.beatsPerBar_(3) });
      clock.sched(246-1, { clock.beatsPerBar_(4) });
      clock.sched(254-1, { clock.beatsPerBar_(3) });
      clock.sched(272-1, { clock.beatsPerBar_(4) });
      clock.sched(280-1, { clock.beatsPerBar_(3) });
      clock.sched(298-1, { clock.beatsPerBar_(4) });
      clock.sched(306-1, { clock.beatsPerBar_(3) });
      clock.sched(416-1, { clock.beatsPerBar_(4) });

      ////////////////////
      //// Beginning: ////
      ///////////////////

      section1IsPlaying = true;
      //////// start FakeGuitar:
      fakeGuitar.section1.playSampleOneShot;

      ///////// Fake Guitar Modular:
      clock.sched((21*4)-1, {
        this.prFadeModular(-inf, 0, 4.5);
        this.playModularRoutine;
      });
      clock.sched((33*4)-1, { mixerC.mute(0); });
      clock.sched((35*4)-1, { mixerC.unMute(0); });
      clock.sched((43*4)-1, { mixerC.mute(0); });
      clock.sched((47*4)-1, { mixerC.unMute(0); });
      clock.sched((55*4)-1, {
        mixerC.mute(0);
        this.stopModularRoutine;
      });

      //////// basses:
      clock.sched((41*4)-1, {
        bassSection.fadeMoog(0, 1, 15);
        bassSection.fadeSaturSynth(0, 1, 21);
        bassSection.fadeFeedbackSynth(0, 1, 21);
        bassSection.feedback.sweepFilter(30, 1880, 21);
      });
      clock.sched((41*4)-1, { bassSection.playPreChorus(clock); });

      //////// trumpet:
      clock.sched((25*4)-1, { mainTrumpet.fadeVolume(-25, -12, 12); });
      clock.sched((25*4)-1, { mixerA.unMute(2); });


      //////// Melody Synth:
      clock.sched((23*4)-1, { melodySynth.fadeVolume(-inf, 0, 20) });
      clock.sched((23*4)-1, { melodySynth.playIntroSequence(clock) });

      /////// Drums:
      clock.sched((21*4)-1, { drums.fadeVolume(-inf, -3, 24); });
      clock.sched((21*4)-1, { drums.playSection1(clock); });

      // drums swell:
      clock.sched((53*4)-1, { drums.fadeVolume(-3, 0, 3); });

      ////////////////////
      //// Chorus: ////
      ///////////////////

      clock.sched((55*4)-1, { chorus1IsPlaying = true; });
      clock.sched(272-1, { chorus2IsPlaying = true; });

			// main trumpet:
      clock.sched((55*4)-1, { mainTrumpet.mixer.setVol(-6); });
			clock.sched((55*4)-1, { mainTrumpet.recordLoop });
      // chorus pt. 2:
			clock.sched(272-1, { mainTrumpet.playWarpedLoop(19.5); });

			// bass:
      clock.sched((55*4)-1, { bassSection.feedback.setFilterCutoff(5720); });
			clock.sched((55*4) -1, { bassSection.playChorus(clock); });

			// melody synth:
			clock.sched((55*4)-1, { melodySynth.playChorus(clock); });

			// drums:
			clock.sched((55*4) -1, { drums.playChorus1(clock); });
      // reverb send up:
      clock.sched((55*4) -1, { mixerB.setSendVol(2, 0, -12) });
      clock.sched(272-1, { mixerB.setSendVol(2, 0, -21); });
      clock.sched(272-1, { drums.playChorus2(clock) });

      // reverb send up at the end of Chorus 2:
      //clock.sched(316-1, { this.fadeMixerBSend(2, 0, -21, -11, 3); });

      ///////////////////////////
      //// post-chorus/canon ////
      //////////////////////////

      clock.sched(324-1, { canonIsPlaying = true });
      clock.sched(420-1, { limboIsPlaying = true });

      //// trumpet:
      // mute:
      clock.sched(324-1, { mixerA.mute(2); });
      // free:
      clock.sched(327-1, { mainTrumpet.free; });

      //// bells:
      clock.sched(327-1, { bellSection.free; });

      //// modular:
      clock.sched(327-1, { this.stopModularRoutine });

      //// trumpet canon:
      // unmute input:
      clock.sched(324-1, { trumpetCanon.unMuteInput; });
      // mute delays:
      clock.sched(420-1, { trumpetCanon.muteDelays; });
      // high pass sweep:
      clock.sched(348-1, { trumpetCanon.postEQ.sweepHighPassFilter(30, 1200, 31.5); });

      //// drums:
      // play post chorus:
      clock.sched(324-1, { drums.playCanon(clock); });
      // sweep filter:
      clock.sched(348-1, { drums.sweepFilter(30, 500, 36); });
      // send to granulator:
      clock.sched(372-1, { this.fadeMixerBSend(2, 1, -inf, 0, 11.25); });

      //// fake guitar:
      // send to granulator:
      clock.sched(324-1, { this.fadeMixerASend(0, 1, -inf, 0, 9); });
      // fade out:
      clock.sched(324-1, { fakeGuitar.fadeVolume(0, -inf, 30); });


      //// basses:
      // feedback filter sweep:
      // CAUSES AWFUL CLICK
      //clock.sched(324-1, { bassSection.feedback.setFilterCutoff(3000); });
      // play sequence:
      clock.sched(324-1, { bassSection.playPostChorus; });
      // fade out satur:
      clock.sched(324-1, { bassSection.fadeSaturSynth(1, 0, 75); });
      // fade out moog:
      clock.sched(324-1, { bassSection.fadeMoog(1, 0, 50); });
      // fade out feedback:
      clock.sched(324-1, { bassSection.fadeFeedbackSynth(1, 0, 35); });


      clock.sched(524-1, { bassSection.stopPostChorus; });

      //// plane noise:
      clock.sched(324-1, { planeNoise.playSample; });

      //// mid buzz:
      clock.sched(324-1, { midBuzz.playSequence(clock) });
      // YOU ARE RESPONSIBLE FOR FADING OUT THE MID BUZZ
      //// Crude Drones:
      // play:
      clock.sched(324-1, {
        drones.playVoice1Sequence(clock);
        drones.playVoice2Sequence(clock);
        drones.playVoice3Sequence(clock);
      });

      //// orchestra:
      clock.sched(324-1, { orchestra.playMahlerSample });
    });
  }

  startMelody {
    clock.playNextBar({
      ///////////////////////////
      //// Clock Management: ////
      //////////////////////////

      clock.tempo = 142.20;

      /////// time signature changes:
      clock.beatsPerBar_(8);
      clock.sched(3-1, { clock.beatsPerBar_(12) });
      clock.sched(4-1, { clock.beatsPerBar_(8) });
      clock.sched(8-1, { clock.beatsPerBar_(12) });
      clock.sched(9-1, { clock.beatsPerBar_(8) });
      clock.sched(12-1, { clock.beatsPerBar_(12) });
      clock.sched(13-1, { clock.beatsPerBar_(8) });
      clock.sched(19-1, { clock.beatsPerBar_(12) });
      clock.sched(20-1, { clock.beatsPerBar_(8) });
      clock.sched(21-1, { clock.beatsPerBar_(6) });
      clock.sched(22-1, { clock.beatsPerBar_(8) });

      //////////////////////////
      //// Trumpet Melody: ////
      ////////////////////////

      //////// trumpet melody:
      trumpetMelody.playPattern(clock);
      clock.sched(108-1, { trumpetMelody.dry.unMute; });

      //////// freeze guitar:
      // chord progression:
      clock.sched(88-1, { freezeGuitar.playChordProgression(clock); });
      // end progression:
      clock.sched(194-1, { freezeGuitar.playEndProgression(clock); });

      //////// bass:
      // end melody:
      clock.sched(108-1, { bassSection.playEnd });
      // coda:
      clock.sched(194-1, { bassSection.playCoda });

      ///// fake guitar:
      clock.sched(48, { mixerA.setSendVol(0, 1, 0); });
      clock.sched(48, { fakeGuitar.section2.setFilterCutoff(33); });
      clock.sched(124, { fakeGuitar.section2.playSampleOneShot; });
      clock.sched(124, { fakeGuitar.section2.sweepFilter(33, 539, 37.1); });
      clock.sched(388, { fakeGuitar.section2.sweepFilter(539, 33, 5.1) });
    });
  }

}