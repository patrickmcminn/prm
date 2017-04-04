/*
Thursday, March 16th 2017
FalseSelf_TrumpetCanon.sc
prm
*/

FalseSelf_TrumpetCanon : IM_Processor {

  var <isLoaded;
  var server;
  var buffer;
  var <submixer, <processor, <postEQ, <delays;
  var <inputIsMuted;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      postEQ = Equalizer.newStereo(mixer.chanStereo(0), group, 'addToHead');
      while({ try { postEQ.isLoaded } != true }, { 0.001.wait; });

      processor = FalseSelf_MainTrumpet.new(postEQ.inBus, relGroup: group, addAction: \addToHead);
      while({ try { processor.isLoaded } != true }, { 0.001.wait; });

      submixer = IM_Mixer.new(3, processor.inBus, relGroup: group, addAction: 'addToHead');
      while({ try { submixer.isLoaded } != true }, { 0.001.wait; });

      delays = Synth(\prm_FalseSelf_TrumpetCanon,
        [\inBus, inBus, \outBus1, submixer.chanMono(0), \outBus2, submixer.chanMono(1), \outBus3, submixer.chanMono(2)],
        group, \addToHead);

      server.sync;

      postEQ.setLowPassCutoff(5350);
      postEQ.setHighPassCutoff(30);

      isLoaded = true;
    };
  }


  prMakeSynthDefs {

    SynthDef(\prm_FalseSelf_TrumpetCanon, {
      | inBus = 0, outBus1 = 0, outBus2 = 1, outBus3 = 2, buffer, inMute = 0, mute = 1, amp1 = 1, amp2 = 1, amp3 = 1 |

      var input, write, tap1, tap2, tap3;

      // mono input:
      input = In.ar(inBus);
      input = input * inMute;
      write = DelTapWr.ar(buffer, input);

      // delay taps:
      tap1 = DelTapRd.ar(buffer, write, 9);
      tap1 = tap1 * mute * amp1;
      tap2 = DelTapRd.ar(buffer, write, 18);
      tap2 = tap2 * mute * amp2;
      tap3 = DelTapRd.ar(buffer, write, 27);
      tap3 = tap3 * mute * amp3;

      // outputs:
      Out.ar(outBus1, tap1);
      Out.ar(outBus2, tap2);
      Out.ar(outBus3, tap3);
    }).add;

  }

  //////// public functions:

  free {
    postEQ.free;
    processor.free;
    submixer.free;
    delays.free;
    this.freeProcessor;
  }

  //////// mutes:

  muteInput {
    synth.set(\inMute, 0);
    inputIsMuted = true;
  }
  unMuteInput {
    synth.set(\inMute, 1);
    inputIsMuted = false;
  }
  tglMuteInput {
    if(inputIsMuted == false,
      {
        synth.set(\inMute, 0);
        inputIsMuted = true;
      },
      {
        synth.set(\inMute, 1);
        inputIsMuted = false;
    });
  }


}

