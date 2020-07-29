/*
Thursday, May 30th 2019
CV_DCOut.sc
prm

class for routing a static value to an analog output
for DC Coupled audio interfaces
*/

CV_Constant {

  var <isLoaded;
  var server;
  var synth;
  var <value;

  var out, rG, action;

  *new { | outBus = 0, initialValue = 0, relGroup, addAction = 'addToHead' |
    ^super.new.prInit(outBus, initialValue, relGroup, addAction);
  }

  prInit { | outBus = 0, initialValue = 0, relGroup, addAction |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      value = initialValue;
      out = outBus;
      rG = relGroup;
      action = addAction;
      this.prAddSynthDef;

      server.sync;

      synth = Synth(\prm_dcOut, [\outBus, outBus, \value, value], relGroup, addAction);

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_dcOut, {
      | outBus = 0, value = 0, gate = 1, lag = 0.001 |
      var sig;
      sig = EnvGen.ar(Env.cutoff(0, 1.0), gate, doneAction: 2);
      sig = sig * value;
      sig = Lag2.ar(sig, lag);
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:
  free {
    synth.free;
  }

  setValue { | value = 0, lag = 0.001 |
    synth.set(\lag, lag);
    synth.set(\value, value);
  }

	setOutBus { | outBus | out = outBus; synth.set(\outBus, outBus); }

}