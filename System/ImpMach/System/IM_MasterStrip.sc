// Should master strip do automation? Naw, let's keep it stripped down
IM_MasterStrip {
  var <inBus;
  var synth, <isMuted;
  var <isLoaded;

  *new { |outBus = 0, relGroup = nil, addAction = \addToTail|
    ^super.new.prInit(outBus, relGroup, addAction);
  }

  // put inside server timing
  prInit { |outBus, relGroup, addAction|
    var server = Server.default;

    isLoaded = false;
    isMuted = false;

    server.waitForBoot {
      inBus = Bus.audio(server, 2);

      this.prAddSynthDef;
      server.sync;

      synth = Synth(\IM_MasterStrip, [\inBus, inBus, \outBus, outBus], relGroup, addAction);

      while( { synth == nil }, { 0.0001.wait; });
      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\IM_MasterStrip, { |inBus = 1, outBus = 0, amp = 1, mute = 0, balance = 0,
      ampLagTime = 0.05|

      var input, sig;

      amp = amp.lag(ampLagTime);
      input = In.ar(inBus, 2);
      sig = Balance2.ar(input[0], input[1], balance, amp * (1 - mute));

      Out.ar(outBus, sig);
    }).add;
  }

  inBusLeft { ^inBus.subBus(0) }
  inBusRight { ^inBus.subBus(1) }

  setVol { |db = 0, lagTime = 0| synth.set(\amp, db.dbamp, \ampLagTime, lagTime) }
  fadeOut { |lagTime = 2| synth.set(\amp, -999.dbamp, \ampLagTime, lagTime) }

  mute { synth.set(\mute, 1); isMuted = true; }
  unMute { synth.set(\mute, 0); isMuted = false; }
  tglMute { if(isMuted, { this.unMute }, { this.mute }) }

  setBal { |bal = 0| synth.set(\balance, bal) }

  free {
    this.mute;
    synth.free;

    isMuted = nil;
    synth = nil;

    inBus.free;
    inBus = nil;
  }
}