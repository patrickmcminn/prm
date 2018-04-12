IM_StepSequencer {

  var <isLoaded;
  var <clock, seqRout;
  var <>seq;
  var <controlBus;

  var <>valLow, <>valHigh;
  var <>timingJitter;

  *new { ^super.new.prInit }

  prInit {
    isLoaded = false;
    valLow = 0.0;
    valHigh = 1.0;
    timingJitter = 0;
    clock = TempoClock(120/60);

    Server.default.waitForBoot {
      controlBus = Bus.control(Server.default, 1);
      while({ controlBus == nil } , { 0.001.wait; });
      isLoaded = true;
    };

  }

  playSeq {
    var count = 0, myQuant = 0;

    try { seqRout.stop };

    seqRout = r {
      var val = try { seq.wrapAt(count).digit.clip(0, 15) };

      if( val != nil, { controlBus.set(val.linlin(0, 15, valLow, valHigh)) });
      (timingJitter.rand + 0.25).wait;

      count = count + 1;
    }.loop.play(clock, quant: myQuant);
  }

  stopSeq { seqRout.stop }

  getBPM { ^clock.tempo * 60 }
  setBPM { |bpm| clock.tempo_(bpm / 60) }

  setRange { |low = 0.0, high = 1.0|
    valLow = low;
    valHigh = high;
  }

  free {
    try { seqRout.stop };
    clock.free;
    controlBus.free;

    clock = nil;
    seqRout = nil;
    seq = nil;

    controlBus = nil;
    valLow = nil;
    valHigh = nil;

    timingJitter = nil;
  }
}