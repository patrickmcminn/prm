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
  var <delaysMuted;
  var <inputBus;

  *new { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      this.prAddSynthDef;
      buffer = Buffer.alloc(server, server.sampleRate * 30, 1);

      inputBus = Bus.control;

      server.sync;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      postEQ = Equalizer.newStereo(mixer.chanStereo(0), group, 'addToHead');
      while({ try { postEQ.isLoaded } != true }, { 0.001.wait; });

      processor = FalseSelf_MainTrumpet.new(postEQ.inBus, relGroup: group, addAction: \addToHead);
      while({ try { processor.isLoaded } != true }, { 0.001.wait; });

      submixer = IM_Mixer.new(4, processor.inBus, relGroup: group, addAction: 'addToHead');
      while({ try { submixer.isLoaded } != true }, { 0.001.wait; });

      delays = Synth(\prm_FalseSelf_TrumpetCanon,
        [\inBus, inBus, \outBus1, submixer.chanMono(0), \buffer, buffer,
          \outBus2, submixer.chanMono(1), \outBus3, submixer.chanMono(2), \outBus4, submixer.chanMono(3)],
        group, \addToHead);

      server.sync;

      postEQ.setLowPassCutoff(5500);
      postEQ.setHighPassCutoff(30);
      processor.delay.setMix(0);
      processor.distortion.setDistortionGain(1.75);
      processor.distortion.postEQ.setLowPassCutoff(7500);
      processor.distortion.mixer.setPreVol(-3);

      delays.set(\inAmp, inputBus.asMap);

      inputIsMuted = false;
      delaysMuted = false;

      isLoaded = true;
    };
  }


  prAddSynthDef {

    SynthDef(\prm_FalseSelf_TrumpetCanon, {
      | inBus = 0, outBus1 = 0, outBus2 = 1, outBus3 = 2, outBus4,
      buffer, inMute = 1, inAmp = 0, mute = 1, amp1 = 1, amp2 = 1, amp3 = 1 |

      var input, write, tap1, tap2, tap3;

      // mono input:
      input = In.ar(inBus);
      input = input * inMute * inAmp;
      write = DelTapWr.ar(buffer, input);

      // delay taps:
      tap1 = DelTapRd.ar(buffer, write, 9);
      tap1 = tap1 * mute * amp1;
      tap2 = DelTapRd.ar(buffer, write, 18);
      tap2 = tap2 * mute * amp2;
      tap3 = DelTapRd.ar(buffer, write, 27);
      tap3 = tap3 * mute * amp3;

      // outputs:
      Out.ar(outBus1, input);
      Out.ar(outBus2, tap1);
      Out.ar(outBus3, tap2);
      Out.ar(outBus4, tap3);
    }).add;

  }

  //inBus { ^processor.inBus }

  //////// public functions:

  free {
    postEQ.free;
    processor.free;
    submixer.free;
    delays.free;
    buffer.free;
    this.freeProcessor;
    isLoaded = false;
  }

  //////// mutes:

  muteInput {
    delays.set(\inMute, 0);
    inputIsMuted = true;
  }
  unMuteInput {
    delays.set(\inMute, 1);
    inputIsMuted = false;
  }
  tglMuteInput {
    if(inputIsMuted == false,
      {
        delays.set(\inMute, 0);
        inputIsMuted = true;
      },
      {
        delays.set(\inMute, 1);
        inputIsMuted = false;
    });
  }

  muteDelays {
    delays.set(\mute, 0);
    delaysMuted = true;
  }
  unMuteDelays {
    delays.set(\mute, 1);
    delaysMuted = false;
  }
  tglMuteDelays {
    if( delaysMuted == false, { this.muteDelays }, { this.unMuteDelays });
  }

  fadeInputAmp { | start = 0, end = 1, time = 0.5 |
    { Out.kr(inputBus, Line.kr(start, end, time, doneAction: 2)) }.play;
  }


}

