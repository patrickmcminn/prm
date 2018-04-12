/*
Tuesday, May 6th

JAB 7/23/2014
*/
IM_DopplerShift : IM_Processor {
  var synth;
  var <isLoaded;

  *new { |outBus = 0, dopplerMix = 0.5, xRate = 0.05, yRate = 0.1,
    relGroup = nil, addAction = 'addToTail'|

    ^super.new(1, 1, outBus,  relGroup: relGroup, addAction: addAction).prInit(dopplerMix, xRate, yRate);
  }

  prInit { |dopplerMix = 0.5, xRate = 0.05, yRate = 0.1|
    var server = Server.default;
    isLoaded = false;

    server.waitForBoot {
      this.prAddSynthDef;
      server.sync;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait });

      synth = Synth(\im_dopplerShift, [\inBus, inBus, \outBus, mixer.chanMono(0),
        \dopplerMix, dopplerMix, \xRate, xRate, \yRate, yRate], group, \addToHead);

      while( { synth == nil }, { 0.001.wait });
      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\im_dopplerShift, { |inBus = 0, outBus = 0, amp = 1, freq = 220, dopplerMix = 0.5,
      xRate = 0.3, yRate = 0.1, xRangeLow = 0.01, xRangeHigh = 0.3, yRangeLow = 0.05, yRangeHigh = 0.2|

      var input, x, y, delayTime, delay, sig;

      input = In.ar(inBus);
      x = SinOsc.ar(xRate).range(xRangeLow.lag(0.05), xRangeHigh.lag(0.05));
      y = SinOsc.ar(yRate).range(yRangeLow.lag(0.05), yRangeHigh.lag(0.05));
      delayTime = sqrt(x.pow(2) + y.pow(2));
      delay = DelayC.ar(input, 10, delayTime);
      sig = (input * (1-dopplerMix)) + (delay * dopplerMix);
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:
  setDopplerMix { |mix = 1|
    synth.set(\dopplerMix, mix);
  }

  setXRate { | xRate = 0.05 |
    synth.set(\xRate, xRate);
  }

  setYRate { | yRate = 0.1 |
    synth.set(\yRate, yRate);
  }

  setXRange { | xRangeLow = 0.01, xRangeHigh = 0.3 |
    this.setXRangeLow(xRangeLow);
    this.setXRangeHigh(xRangeHigh);
  }

  setXRangeLow { | xRangeLow = 0.01 |
    synth.set(\xRangeLow, xRangeLow);
  }

  setXRangeHigh { | xRangeHigh = 0.3 |
    synth.set(\xRangeHigh, xRangeHigh);
  }

  setYRange { | yRangeLow = 0.05, yRangeHigh = 0.2 |
    this.setYRangeLow(yRangeLow);
    this.setYRangeHigh(yRangeHigh);
  }

  setYRangeLow { | yRangeLow = 0.05 |
    synth.set(\yRangeLow, yRangeLow)
  }

  setYRangeHigh { | yRangeHigh = 0.2 |
    synth.set(\yRangeHigh, yRangeHigh);
  }

  free {
    isLoaded = false;
    mixer.mute;

    synth.free;
    synth = nil;

    group.free;
    group = nil;

    this.freeProcessor;
  }
}