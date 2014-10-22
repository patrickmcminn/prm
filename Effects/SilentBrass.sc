/*
SilentBrass.sc
Wednesday, October 22nd 2014
prm
*/

SilentBrass {

  var <isLoaded;
  var synth;

  *new { | inBus = 0, outBus = 0, relGroup = nil, addAction = \addToHead |
    ^super.new.prInit(inBus, outBus, relGroup, addAction);
  }

  prInit { | inBus = 0, outBus = 0, relGroup = nil, addAction = \addToHead |
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      this.prAddSynthDef;
      server.sync;

      synth = Synth(\prm_SilentBrass, [\inBus, inBus, \outBus, outBus], relGroup, addAction);

      while({ synth == nil }, { 0.001.wait; });
      isLoaded = true;
    };
  }

  prAddSynthDef {

    SynthDef(\prm_SilentBrass, {
      |
      inBus = 0, outBus = 0, amp = 1,
      highPassCutoff = 160, lowPassCutoff = 11200,
      notch1Freq = 5000, notch2Freq = 8760, bandPassGain = -6
      |
      var input, highPass, notch1, notch2, lowPass, sig;
      input = SoundIn.ar(inBus);
      highPass = HPF.ar(input, 160, 1);
      notch1 = BPeakEQ.ar(highPass, notch1Freq, 0.3597, -6);
      notch2 = BBandStop.ar(notch1, notch2Freq, 0.6803);
      lowPass = BLowPass4.ar(notch2, lowPassCutoff, 1.4286);
      sig = lowPass * amp;
      Out.ar(outBus, sig);
    }).add;

  }

  //////// public functions:
  free {
    isLoaded = false;
    synth.free;
    synth = nil;
  }
}

  