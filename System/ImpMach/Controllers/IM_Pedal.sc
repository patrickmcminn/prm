IM_Pedal {
  var <group;
  var synth;
  var controlBus;

  *new { |hardwareIn = 3, hardwareOut = 3, relGroup = nil, addAction = \addToHead|
    ^super.new.prInit(hardwareIn, hardwareOut, relGroup, addAction);
  }

  prInit { |hardwareIn, hardwareOut, relGroup, addAction|
    var server = Server.default;

    fork {
      controlBus = Bus.audio(server, 1);
      this.prAddSynthDef;
      server.sync;

      this.prMakeSynth;
    };
  }

  prAddSynthDef {
    // should the tone and amp follower both be in the same synthDef?
    // lag the output
    SynthDef(\IM_Pedal, { |outBus, toneBus, inBus|
    }).add;
  }

  prMakeSynth { |hardwareOutBus, hardwareInBus|
    synth = Synth(\IM_Pedal, [\outBus, controlBus, \toneBus, hardwareOutBus, \inBus, hardwareInBus], group);
  }

  free {
  }
}