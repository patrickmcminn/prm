/*
Friday, February 24th, 2017
FalseSelf_Kick.sc
prm
driving from Atlanta, GA to New Orleans, LA
for Mardi Gras
on the bus
*/

FalseSelf_Kick : IM_Module {

  var server, <isLoaded;
  var <drums, <eq;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    var path, sampleArray;

    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      //this.prAddSynthDefs;

      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/FalseSelf/samples/kick/";
      sampleArray = (path ++ "*").pathMatch;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      drums = Sampler.newStereo(eq.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { drums.isLoaded } != true }, { 0.001.wait; });
      server.sync;

      this.prSetInitialParameters;
      //this.prMakeSequences;
      server.sync;
      /*
      this.prSequenceSection1;
      this.prSequenceChorus1;
      this.prSequenceChorus2;
      this.prSequenceCanon;
      this.prSequenceEnding;
      */

      server.sync;

      mixer.setPreVol(-3);

      isLoaded = true;
    }
  }

  prSetInitialParameters {
    eq.setLowFreq(112);
    eq.setLowGain(3.35.dbamp);
    eq.setLowRQ(1.41);

    eq.setPeak1Freq(776);
    eq.setPeak1Gain(-6.57.dbamp);
    eq.setPeak1RQ(1.41);

    eq.setLowPassCutoff(2200);
    //highPassCutoff = 20;
  }



  //////// public functions:

  free {
    eq.free;
    drums.free;
    this.freeModule;
    isLoaded = false;
  }

  /*
  setHighPassCutoff { | cutoff = 20 |
  highPass.set(\cutoff, cutoff);
  highPassCutoff = cutoff;
  }
  */

  playDrum0 { | vol = -6 | drums.playSampleOneShot(0, vol+3); }
  playDrum1 { | vol = -6 | drums.playSampleOneShot(1, vol+3); }
  playDrum2 { | vol = -6 | drums.playSampleOneShot(2, vol-1.5); }
  playDrum3 { | vol = -6 | drums.playSampleOneShot(3, vol-1.5); }

  playMDrum { | vol = -15, pitch = 0 | drums.playSampleOneShot(4, vol, pitch.midiratio); }

  /*
  playSection1 { | clock = 'internal', quant = nil | drums.playSequence(\section1, clock, quant); }
  stopSection1 { drums.stopSequence(\section1); }

  playChorus1 { | clock = 'internal', quant = nil | drums.playSequence(\chorus1, clock, quant); }
  stopChorus1 { drums.stopSequence(\chorus1); }

  playChorus2 { | clock = 'internal', quant = nil | drums.playSequence(\chorus2, clock, quant); }
  stopChorus2 { drums.stopSequence(\chorus2); }

  playCanon { | clock = 'internal', quant = nil | drums.playSequence(\canon, clock, quant); }
  stopCanon { drums.stopSequence(\canon); }

  playEnding { | clock = 'internal', quant = nil | drums.playSequence(\ending, clock, quant); }
  stopEnding { drums.stopSequence(\ending); }

  setSequencerClockTempo { | tempo = 160 | drums.setSequencerClockTempo(tempo); }

  fadeVolume { | start = -inf, end = 0, time = 10 |
  {
  var bus = Bus.control;
  server.sync;
  { Out.kr(bus, Line.kr(start.dbamp, end.dbamp, time, doneAction: 2)) }.play;
  mixer.mapAmp(bus);
  { bus.free }.defer(time);
  { mixer.setVol(end) }.defer(time);
  }.fork;
  }

  sweepFilter { | startFreq = 20, endFreq = 3500, time = 10 |
  {
  var bus = Bus.control;
  server.sync;
  { Out.kr(bus, XLine.kr(startFreq, endFreq, time, doneAction: 2)); }.play;
  highPass.set(\cutoff, bus.asMap);
  { bus.free }.defer(time);
  { highPassCutoff = endFreq; }.defer(time);
  }.fork;
  }
  */
  prMakeSequences {
    drums.makeSequence(\section1, 'oneShot');
    drums.makeSequence(\chorus1, 'oneShot');
    drums.makeSequence(\chorus2, 'oneShot');
    drums.makeSequence(\canon, 'oneShot');
    drums.makeSequence(\ending, 'oneShot');
  }

  prSequenceSection1 {
    var part1, part2, part3, part4;
    var drum0 = drums.bufferArray[0];
    var drum1 = drums.bufferArray[1];
    var drum2 = drums.bufferArray[2];
    var drum3 = drums.bufferArray[3];

    part1 = Pseq([
      [drum0, drum1, drum2, drum3],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2]
    ], 8);
    part2 = Pseq([
      [drum0, drum1, drum2, drum3],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2]
    ], 6);
    part3 = Pseq([
      [drum0, drum1, drum2],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2]
    ], 4);
    part4 = Pseq([
      [drum0, drum1, drum2, drum3], drum1, drum2, drum0,
      drum1, drum0, drum2, drum0,
      [drum0, drum1, drum2], drum1, drum0, drum2,
      drum1, drum0, drum0, drum2,
      [drum0, drum1, drum2], drum2, drum1, drum0,
      drum1, drum2, drum1, drum0,
      [drum0, drum1, drum2], drum1, drum2, drum1,
      [drum0, drum1, drum2], [drum0, drum1], [drum0, drum1, drum2], [drum0, drum1, drum2]
    ], 1);


    drums.addKey(\section1, \dur, Pseq([Pseq([1], 64), Pseq([2], 24), Pseq([1], 16), Pseq([0.25], inf)], inf));
    drums.addKey(\section1, \buffer, Pseq([part1, part2, part3, part4], 1));
    drums.addKey(\section1, \amp, 0.65);
  }

  prSequenceChorus1 {
    var part1, part2, part3;
    var drum0 = drums.bufferArray[0];
    var drum1 = drums.bufferArray[1];
    var drum2 = drums.bufferArray[2];
    var drum3 = drums.bufferArray[3];

    part1 = Pseq([
      [drum0, drum1, drum2, drum3], Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,

      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      [drum0, drum1, drum2, drum3], Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,

      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
    ], 1);

    part2 = Pseq([
      [drum0, drum1, drum2, drum3], Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      [drum0, drum1, drum2, drum3], Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,

      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, [drum1, drum2], [drum1, drum2],

      [drum0, drum1, drum2, drum3], Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
    ], 1);

    part3 = Pseq([
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      [drum0, drum1, drum2, drum3], Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,

      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,

      [drum0, drum1, drum2, drum3], Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,

      Rest, Rest, Rest, Rest,
      [drum1, drum2], Rest, [drum1, drum2], Rest,
      [drum0, drum1, drum2, drum3], Rest, Rest, Rest,
      Rest, Rest, Rest, Rest,

      [drum1, drum3], Rest, Rest, Rest,
      [drum1, drum2], Rest, Rest, Rest,
      [drum2, drum3], Rest, Rest, Rest,
      [drum1, drum2], Rest, Rest, Rest,

      [drum0, drum3], Rest, Rest, Rest,
      [drum1, drum2], [drum1, drum2], [drum1, drum2], [drum1, drum2],
      [drum0, drum1, drum2, drum3], Rest, Rest, Rest,
      [drum1, drum2], Rest, Rest, Rest,

      Pseq([[drum1, drum2], Rest, Rest, Rest], 4);
    ], 1);

    drums.addKey(\chorus1, \dur, 0.25);
    drums.addKey(\chorus1, \buffer, Pseq([part1, part2, part3], 1));
    drums.addKey(\chorus1, \amp, 1);
  }

  prSequenceChorus2 {
    var part1, part2;
    var drum0 = drums.bufferArray[0];
    var drum1 = drums.bufferArray[1];
    var drum2 = drums.bufferArray[2];
    var drum3 = drums.bufferArray[3];

    part1 = Pseq([[drum0, drum1, drum2], Rest, Rest, Rest], 11*4);
    part2 = Pseq([
      [drum0, drum1, drum2, drum3], drum1, drum2, drum0,
      drum1, drum0, drum2, drum0,
      [drum0, drum1, drum2], drum1, drum0, drum2,
      drum1, drum0, drum0, drum2,

      [drum0, drum1, drum2], drum2, drum1, drum0,
      drum1, drum2, drum1, drum0,
      [drum0, drum1, drum2], drum1, drum2, drum1,
      [drum0, drum1, drum2, drum3],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2]
    ], 1);

    drums.addKey(\chorus2, \dur, 0.25);
    drums.addKey(\chorus2, \buffer, Pseq([part1, part2], 1));
    drums.addKey(\chorus2, \amp, 1);
  }


  prSequenceCanon {
    var introPart;
    var mainPart;
    var drum0 = drums.bufferArray[0];
    var drum1 = drums.bufferArray[1];
    var drum2 = drums.bufferArray[2];
    var drum3 = drums.bufferArray[3];

    introPart = Pseq([
      [drum0, drum1, drum2, drum3], Rest, Rest, Rest,
      [drum0, drum1, drum2], drum1, drum2, drum1,
      [drum0, drum1, drum2, drum3],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2]
    ], 1);
    mainPart = Pseq([
      [drum0, drum1, drum2, drum3], drum1, drum2, drum0,
      drum1, drum0, drum2, drum0,
      [drum0, drum1, drum2], drum1, drum0, drum2,
      drum1, drum0, drum0, drum2,

      [drum0, drum1, drum2], drum2, drum1, drum0,
      drum1, drum2, drum1, drum0,
      [drum0, drum1, drum2], drum1, drum2, drum1,
      [drum0, drum1, drum2, drum3],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2],
      [drum0, drum1, drum2]
    ], inf);

    drums.addKey(\canon, \dur, 0.25);
    drums.addKey(\canon, \buffer, Pseq([introPart, mainPart], 1));
    drums.addKey(\canon, \amp, 0.8);
  }

  prSequenceEnding {
    var drum2 = drums.bufferArray[2];

    drums.addKey(\ending, \dur, Pseq([1], inf));
    drums.addKey(\ending, \buffer, Pseq([drum2], inf));
    drums.addKey(\ending, \amp, 0.8);

  }

}