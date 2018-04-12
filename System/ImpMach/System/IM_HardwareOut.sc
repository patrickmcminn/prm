// 3/25/2014 JAB
IM_HardwareOut {
  var inBusArray;
  var <group;
  var <outputSynthArray;

  *new { |numOutputs = 8|
    ^super.new.prInit(numOutputs);
  }

  //////// PRIVATE FUNCTIONS

  prInit { | numOutputs = 8 |
    var server = Server.default;

    server.waitForBoot {
      this.prAddSynthDef;
      server.sync;

      this.prMakeBusses(numOutputs);
      group = Group.tail(server);
      server.sync;

      this.prMakeOutputs(numOutputs);
    };
  }

  prAddSynthDef {
    SynthDef(\IM_extOut, { |inBus = 0, outBus = 0|
      var sig, out;
      sig = In.ar(inBus, 1);
      out = Out.ar(outBus, Limiter.ar(sig));
    }).add;
  }

  prMakeBusses { |numOutputs = 8| inBusArray = Array.fill(numOutputs, { Bus.audio }) }

  prMakeOutputs { |numOutputs = 8|
    outputSynthArray = Array.fill(numOutputs, { | index |
      Synth(\IM_extOut, [\inBus, inBusArray.at(index), \outBus, index], group);
    });

    // outputSynthArray.do { |synth, index| CmdPeriod.remove(synth); };
  }


  //////// PUBLIC FUNCTIONS

  // Returns the bus to which a given audio hardware input is routed
  inBus { |index = 0|
    ^inBusArray[index];
  }

  numOutputs {
    ^outputSynthArray.size;
  }

  free {
    var server = Server.default;

    fork {
      outputSynthArray.do { |synth| synth.free };
      outputSynthArray = nil;
      server.sync;

      group.free;
      group = nil;

      inBusArray.do({ |bus| bus.free });
      inBusArray = nil;
    };
  }
}