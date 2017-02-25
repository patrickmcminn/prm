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
      this.prSequenceSequences;

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
    drums.makeSequence(\section1_long, 'oneShot');
    drums.makeSequence(\chorus1, 'oneShot');
    drums.makeSequence(\chorus2, 'oneShot');
    drums.makeSequence(\canon, 'oneShot');
    drums.makeSequence(\end, 'oneShot');
  }

  prSequenceSequences {
    drums.addKey(\section1, \dur, 1);
    drums.addKey(\section1, \buffer, [drums.bufferArray[0], drums.bufferArray[1], drums.bufferArray[2]]);
    drums.addKey(\section1, \amp, 0.3);

    drums.addKey(\section1_long, \dur, 4);
    drums.addKey(\section1_long, \buffer, drums.bufferArray[3]);
    drums.addKey(\section1_long, \amp, 0.4);



  }

  //////// public functions:

  setHighPassCutoff { | cutoff = 20 |
    highPass.set(\cutoff, cutoff);
    highPassCutoff = cutoff;
  }

}