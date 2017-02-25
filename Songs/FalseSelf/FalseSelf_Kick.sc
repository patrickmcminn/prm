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
  var <drums, eq, highPass;
  var highPassBus;
  var <highPassCutoff;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    var path, sampleArray;

    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      this.prAddSynthDefs;

      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/FalseSelf/samples/kick/";
      sampleArray = (path ++ "*").pathMatch;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      highPassBus = Bus.audio(server, 1);
      server.sync;
      highPass = Synth(\prm_falseSelf_hpf, [\inBus, highPassBus, \outBus, mixer.chanMono(0)], group, \addToHead);
      server.sync;

      eq = Equalizer.newMono(highPassBus, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      drums = Sampler.newMono(eq.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { drums.isLoaded } != true }, { 0.001.wait; });
      server.sync;

      this.prInitializeEQ;
      this.prMakeSequences;
      server.sync;
      this.prSequenceSection1;

      server.sync;
      isLoaded = true;
    }
  }

  prAddSynthDefs {
    SynthDef(\prm_falseSelf_hpf, {
      | inBus = 0, outBus = 1, amp = 1, cutoff = 15 |
      var input, filter, sig;
      input = In.ar(inBus);
      filter = RHPF.ar(input, cutoff, 1);
      sig = Out.ar(outBus, filter);
    }).add;
  }

  prInitializeEQ {
    eq.setLowFreq(112);
    eq.setLowGain(3.35.dbamp);
    eq.setLowRQ(1.41);

    eq.setPeak1Freq(776);
    eq.setPeak1Gain(-6.57.dbamp);
    eq.setPeak1RQ(1.41);

    eq.setLowPassCutoff(2200);
    highPassCutoff = 20;
  }

  prMakeSequences {
    drums.makeSequence(\section1, 'oneShot');
    drums.makeSequence(\chorus1, 'oneShot');
    drums.makeSequence(\chorus2, 'oneShot');
    drums.makeSequence(\canon, 'oneShot');
    drums.makeSequence(\end, 'oneShot');
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
      [drum0, drum1, drum2, drum3],
      [drum0, drum1, drum2, drum3],
      [drum0, drum1, drum2, drum3],
      [drum0, drum1, drum2, drum3]
    ], 4);
    part4 = Pseq([
      [drum0, drum1, drum2, drum3], drum1, drum2, drum0,
      drum1, drum0, drum2, drum0,
      [drum0, drum1, drum2], drum1, drum0, drum2,
      drum1, drum0, drum0, drum2,
      [drum0, drum1, drum2], drum2, drum1, drum0,
      drum1, drum2, drum1, drum0,
      [drum0, drum1, drum2], drum1, drum2, drum1,
      [drum0, drum1, drum2, drum3], [drum0, drum1, drum2], [drum0, drum1, drum2], [drum0, drum1, drum2]
    ], 1);


    drums.addKey(\section1, \dur, Pseq([Pseq([1], 64), Pseq([2], 24), Pseq([1], 16), Pseq([0.25], inf)], inf));
    drums.addKey(\section1, \buffer, Pseq([part1, part2, part3, part4], 1));
    drums.addKey(\section1, \amp, 0.3);
  }

  //////// public functions:

  setHighPassCutoff { | cutoff = 20 |
    highPass.set(\cutoff, cutoff);
    highPassCutoff = cutoff;
  }

}