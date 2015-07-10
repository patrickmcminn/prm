/*
Monday, September 15th 2014
prm
*/

TrumpetSequencer : IM_Processor {

  var <isLoaded;
  var server;
  var <shiftSequencer, trumpetProcessor;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true}, { 0.01.wait; });

      this.prAddSynthDef;
      server.sync;
      shiftSequencer = ShiftSequencer.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { shiftSequencer.isLoaded } != true }, { 0.01.wait; });

      trumpetProcessor = Synth(\prm_trumpetSequencer_processor, [\inBus, inBus, \outBus, shiftSequencer.inBus, \amp, 1], group, \addToHead);
      server.sync;

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_trumpetSequencer_processor, {
      |
      inBus = 0, outBus = 0, amp = 1,
      cutoff = 2500, dist = 50, postDistortionCutoff = 2500
      highBoost = -6
      |
      var input, filter, distortion, postFilter, eq, sig;
      input = In.ar(inBus);
      filter = LPF.ar(input, cutoff);
      distortion = (filter * dist).distort;
      postFilter = LPF.ar(distortion, postDistortionCutoff);
      eq = BHiShelf.ar(postFilter, 2500, 1, highBoost);
      sig = eq * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:

  free {
    trumpetProcessor.free;
    trumpetProcessor = nil;
    shiftSequencer.free;
    shiftSequencer = nil;
    this.freeProcessor;
  }

  setDistortion { | dist = 50 | trumpetProcessor.set(\dist, dist); }
  setCutoff { | cutoff = 2500 | trumpetProcessor.set(\cutoff, cutoff); }
  setPostDistortionCutoff { | cutoff = 2500 | trumpetProcessor.set(\postDistortionCutoff, cutoff); }
  setHighBoost { | db = -6 | trumpetProcessor.set(\highBoost, db); }

}