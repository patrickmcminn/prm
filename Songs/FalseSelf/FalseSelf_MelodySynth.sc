/*
Saturday, April 15th 2017
FalseSelf_MelodySynth.sc
prm
*/

FalseSelf_MelodySynth : IM_Module {

  var server, <isLoaded;
  var <synth;

  *new { | outBus = 0, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      mixer.tglMute;

      synth = GlitchySynth.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { synth.isLoaded } != true }, { 0.001.wait; });


      mixer.tglMute;

      synth.setAttackTime(15);
      synth.setReleaseTime(0.05);

      this.prMakeSequence;

      isLoaded = true;
    }
  }

  prMakeSequence {
    {
      synth.makeSequence(\chorusC);
      synth.makeSequence(\chorusCSharp);
      synth.makeSequence(\chorusGSharp);
      synth.makeSequence(\chorusE);
      synth.makeSequence(\chorusDSharp);
      server.sync;

      synth.addKey(\chorusC, \legato, 1);
      synth.addKey(\chorusC, \dur, Pseq([8, 6, 6, 6], 4));
      synth.addKey(\chorusC, \note, Pseq([0, \r, 0, \r], 4));
      synth.addKey(\chorusC, \octave, 6);

      synth.addKey(\chorusCSharp, \legato, 1);
      synth.addKey(\chorusCSharp, \dur, Pseq([2, 6, 8, 4, 6], 4));
      synth.addKey(\chorusCSharp, \note, Pseq([\r, 1, \r, 1, \r], 4));
      synth.addKey(\chorusCSharp, \octave, 6);

      synth.addKey(\chorusGSharp, \legato, 1);
      synth.addKey(\chorusGSharp, \dur, Pseq([4, 7, 1, 2, 4, 5, 1, 2], 4));
      synth.addKey(\chorusGSharp, \note, Pseq([\r, -4, \r, -4, \r, -4, \r, -4], 4));
      synth.addKey(\chorusGSharp, \octave, 6);

      synth.addKey(\chorusE, \legato, 1);
      synth.addKey(\chorusE, \dur, Pseq([8, 4, 8, 4, 2], 4));
      synth.addKey(\chorusE, \note, Pseq([\r, 4, \r, 4, \r], 4));
      synth.addKey(\chorusE, \octave, 6);

      synth.addKey(\chorusDSharp, \legato, 1);
      synth.addKey(\chorusDSharp, \dur, Pseq([10, 2, 10, 2, 2], 4));
      synth.addKey(\chorusDSharp, \note, Pseq([\r, 3, \r, 3, \r], 4));
      synth.addKey(\chorusDSharp, \octave, 6);

    }.fork;
  }

  //////// public functions:

  free {
    synth.free;
    this.freeModule;
    isLoaded = false;
  }

  playNote { | note = 220 | synth.playNote(note); }
  releaseNote { | note = 220 | synth.releaseNote(note); }

  playIntroSequence { | clock |
    clock.playNextBar({
      synth.setAttackTime(15);
      synth.playNote(72.midicps);
    });
    clock.sched(32, {
      synth.setAttackTime(12);
      synth.playNote(73.midicps);
    });
    clock.sched(80, {
      synth.setAttackTime(4);
      synth.playNote(68.midicps);
    });
    clock.sched(112, {
      synth.setAttackTime(1);
      synth.playNote(75.midicps);
    });
    clock.sched(129, {
      synth.releaseNote(75.midicps);
      synth.releaseNote(68.midicps);
      synth.releaseNote(73.midicps);
      synth.releaseNote(72.midicps);
    });
  }

  playChorus { | clock |
    synth.setAttackTime(0.1);
    synth.setReleaseTime(0.5);
    synth.playSequence(\chorusC, clock);
    synth.playSequence(\chorusCSharp, clock);
    synth.playSequence(\chorusGSharp, clock);
    synth.playSequence(\chorusDSharp, clock);
    synth.playSequence(\chorusE, clock);
  }

  fadeVolume {  | start = -inf, end = 0, time = 10 |
    {
      var bus = Bus.control;
      server.sync;
      { Out.kr(bus, Line.kr(start.dbamp, end.dbamp, time, doneAction: 2)) }.play;
      mixer.mapAmp(bus);
      { bus.free }.defer(time);
      { mixer.setVol(end) }.defer(time);
    }.fork;
  }
}