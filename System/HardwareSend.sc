/*
Monday, June 29th 2015
HardwareSend.sc
prm
*/

// good for sending signals to external audio effects
// sums stereo down to mono

MonoHardwareSend {

  var <mixer,  group, out, <inBus, synth;
  var <isLoaded, isMuted;

  *new {
    | externalOut = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil,
    send3Bus = nil, feedback = false, relGroup = nil, addAction = \addToHead  |

    ^super.new.prInit(externalOut, send0Bus, send1Bus, send2Bus, send3Bus,
      feedback, relGroup, addAction);
  }

  prInit {
    | externalOut = 0, send0Bus, send1Bus, send2Bus, send3Bus,
    feedback, relGroup, addAction |

     var server = Server.default;

    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDef;
      group = Group(relGroup, addAction);
      inBus = Bus.audio(server, 2);
      server.sync;

      //out = IM_HardwareOut.new(1);
      server.sync;

      synth = Synth(\prm_MonoMixer, [\inBus, inBus, \outBus, externalOut,
        \send0Bus, send0Bus, \send1Bus, send1Bus, \send2Bus, send2Bus, \send3Bus, send3Bus], group, \addToHead);
      isMuted = false;

      server.sync;

      isLoaded  = true;
    };
  }

  prAddSynthDef {
    SynthDef(\prm_MonoMixer, { |preAmp = 1, inBus = 1, outBus = 0, amp = 1, mute = 0,
      preOrPost = 1, panBal = 0,
      send0Bus, send0Amp = 0, send1Bus, send1Amp = 0,
      send2Bus, send2Amp = 0, send3Bus, send3Amp = 0,
      ampLagTime = 0.05|

      var sig, input,  monoInput,  sendSig, lagTime = 0.05;

      amp = amp.lag(ampLagTime);

      input = In.ar(inBus, 2);
      input = input * preAmp;

      monoInput = Mix.ar([input[0], input[1]]) * 0.5;
      //stereoInput = Balance2.ar(input[0], input[1], panBal);

      sig = monoInput * amp * (1 - mute);

      // Choose pre or post fx sends
      sendSig = (input * (1 - preOrPost)) + (sig * preOrPost);

      // Fx sends
      Out.ar(send0Bus, sendSig * send0Amp.lag(lagTime) );
      Out.ar(send1Bus, sendSig * send1Amp.lag(lagTime) );
      Out.ar(send2Bus, sendSig * send2Amp.lag(lagTime) );
      Out.ar(send3Bus, sendSig * send3Amp.lag(lagTime) );

      Out.ar(outBus, sig);
    }).add;
  }

  /////// public functions:
  free {
    synth.free;
    out.free;
    inBus.free;
    inBus = nil;
    group.free;
    group = nil;
  }

  setPreVol { |db = 0| synth.set(\preAmp, db.dbamp) }
  setVol { |db = 0, lagTime = 0.05| synth.set(\amp, db.dbamp, \ampLagTime, lagTime) }
  fadeOut { |lagTime = 2| synth.set(\amp, -999.dbamp, \ampLagTime, lagTime) }
  fade{ | targetdb = 0, time = 2 | synth.set(\amp, targetdb.dbamp, \ampLagTime, time); }

  mute { synth.set(\mute, 1); isMuted = true; }
  unMute { synth.set(\mute, 0); isMuted = false; }
  tglMute { if(isMuted, { this.unMute }, { this.mute }) }

  setSendVol { |sendNum = 0, db = 0|
    var sendSymbol = ("send" ++ sendNum ++ "Amp").asSymbol;
    synth.set(sendSymbol, db.dbamp);
  }

  setSendPre { synth.set(\preOrPost, 0) }
  setSendPost { synth.set(\preOrPost, 1) }

  setPanBal { |panBal = 0| synth.set(\panBal, panBal) }
  setOutBus { |out = 0| synth.set(\outBus, out) }
}