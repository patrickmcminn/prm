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
	var <trumpetCanon, <drones, <sixteenthDrones, <orchestra, <planeBuzz, <flute;

  var <modularRoutine;

  *new { | mixAOutBus, mixBOutBus, mixCOutBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(mixAOutBus, 8, mixBOutBus, 8, mixCOutBus, 8, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixerC.isLoaded } != true }, { 0.001.wait; });

      mixerA.tglMute(2);

      clock = TempoClock.new;
      server.sync;
      clock.tempo = 160/60;

      fakeGuitar = FalseSelf_FakeGuitar.new(mixerA.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { fakeGuitar.isLoaded } != true }, { 0.001.wait; });

      bellSection = FalseSelf_BellSection.new(mixerA.chanStereo(1), relGroup: group, addAction: \addToHead);
      while({ try { bellSection.isLoaded } != true }, { 0.001.wait; });

      mainTrumpet = FalseSelf_MainTrumpet.new(mixerA.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { mainTrumpet.isLoaded } != true }, { 0.001.wait; });
      mainTrumpetInput = IM_HardwareIn.new(0, mainTrumpet.inBus, group, 'addToHead');
      while({ try { mainTrumpetInput.isLoaded } != true }, { 0.001.wait; });

      melodySynth = FalseSelf_MelodySynth.new(mixerB.chanStereo(0), group, \addToHead);
      while({ try { melodySynth.isLoaded } != true }, { 0.001.wait; });

      bassSection = FalseSelf_BassSection.new(mixerB.chanStereo(1), relGroup: group, addAction: \addToHead,
        moogDeviceName: "iConnectAudio4+", moogPortName: "DIN");
      while({ try { bassSection.isLoaded } != true }, { 0.001.wait; });

      drums = FalseSelf_Kick.new(mixerB.chanStereo(2), relGroup: group, addAction: \addToHead);
      while({ try { drums.isLoaded } != true }, { 0.001.wait; });


      modular = IM_Mixer_1Ch.new(mixerC.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { modular.isLoaded } != true }, { 0.001.wait; });
      modularInput = IM_HardwareIn.new(2, modular.chanMono, group, \addToHead);
      while({ try { modularInput.isLoaded } != true }, { 0.001.wait; });


      server.sync;

      this.prMakeModularRoutine;
      this.prSetInitialParameters;

      relBar = 0;
      clock.schedAbs(clock.beats.ceil, { | beat | bar = clock.bar - relBar; 1 });

      isLoaded = true;
    }
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
    // fake guitar:
    mixerA.setVol(0, -3);
    mixerA.setSendVol(0, 2, 0);
    // bells:
    mixerA.setVol(1, -6);
    // trumpet:
    mixerA.setVol(2, 0);

    // melodySynth:
    mixerA.setVol(3, -6);

    // basses:
    mixerB.setVol(1, -6);
    bassSection.satur.mixer.setVol(-inf);
    bassSection.feedback.mixer.setVol(-inf);
    bassSection.moog.mixer.setVol(-inf);

    // drums:

    // modular:
    //mixerC.setVol(0, -inf);
    mixerC.setSendVol(0, 0, -10);

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

      ////////////////////
      //// Chorus: ////
      ///////////////////

			// main trumpet:
			clock.sched((55*4)-1, { mainTrumpet.recordLoop });
			clock.sched(272-1, { mainTrumpet.playWarpedLoop(19.5); });

			// bass:
			clock.sched((55*4) -1, { bassSection.playChorus(clock); });

			// melody synth:
			clock.sched((55*4)-1, { melodySynth.playChorus(clock); });

			// drums:
			clock.sched((55*4) -1, { drums.playChorus1(clock); });

			//// loaders:

			// trumpet canon:
			clock.sched((55*4)-1, {
				trumpetCanon = FalseSelf_TrumpetCanon.new(mixerA.chanStereo(3), relGroup: group, addAction: \addToHead);
			});

			// drones:
			clock.sched((55*4)-1, {
				drones = FalseSelf_CrudeDrones.new(mixerC.chanStereo(1), relGroup: group, addAction: \addToHead);
			});

			// 16th drones:
			clock.sched((55*4)-1, {
				sixteenthDrones = FalseSelf_16thDrones.new(mixerC.chanStereo(2), relGroup: group, addAction: \addToHead);
			});

			// plane buzz:
			clock.sched((55*4)-1, {
				planeBuzz = FalseSelf_PlaneNoise.new(mixerC.chanStereo(3), relGroup: group, addAction: \addToHead);
			});

			// orchestra:
			clock.sched((55*4) -1, {
				orchestra = FalseSelf_Orchestra.new(mixerB.chanStereo(3), relGroup: group, addAction: \addToHead);
			});

			// flute:
			clock.sched((55*4)-1, {
				flute = FalseSelf_Flute.new(mixerB.chanStereo(4), relGroup: group, addAction: \addToHead);
			});
    });
  }

}