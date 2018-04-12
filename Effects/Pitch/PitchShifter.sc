/*
Wednesday, April 19th 2017
PitchShifter.sc
prm
*/

PitchShifter : IM_Processor {

  var server, <isLoaded;
  var <shiftAmount, shifter, <eq;

  *newMono { | outBus = 0, shift = -12, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono(shift);
  }

  *newStereo { | outBus = 0, shift = -12, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo(shift);
  }

  prInitMono { | shift = -12 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      shiftAmount = shift;

      this.prAddSynthDef;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newMono(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      shifter = Synth(\prm_SimplePitchShift_Mono, [\inBus, inBus, \outBus, eq.inBus, \shiftAmount, shiftAmount],
        group, \addToHead);
      server.sync;

      mixer.setPreVol(3);

      isLoaded = true;
    }
  }

  prInitStereo { | shift = -12 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      shiftAmount = shift;

      this.prAddSynthDef;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      shifter = Synth(\prm_SimplePitchShift_Stereo, [\inBus, inBus, \outBus, eq.inBus, \shiftAmount, shiftAmount],
        group, \addToHead);
      server.sync;

      mixer.setPreVol(3);

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_SimplePitchShift_Mono, {
      | inBus, outBus, shiftAmount = -12, amp = 1 |
      var input, shifter, sig;
      input = In.ar(inBus, 1);
      shifter = PitchShift.ar(input, 0.1, shiftAmount.midiratio, 0, 0.05);
      sig = shifter * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_SimplePitchShift_Stereo, {
      | inBus, outBus, shiftAmount = -12, amp = 1 |
      var input, shifter, sig;
      input = In.ar(inBus, 2);
      shifter = PitchShift.ar(input, 0.1, shiftAmount.midiratio, 0, 0.05);
      sig = shifter * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  /////// public functions:

  free {
    isLoaded = false;
    shifter.free;
    eq.free;
    this.freeProcessor;
  }

  setShiftAmount { | shift = -12 | shifter.set(\shiftAmount, shift); }

}

