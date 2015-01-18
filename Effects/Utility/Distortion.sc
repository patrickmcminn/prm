/*
Monday, January 19th 2015
Distortion.sc
prm
Hobart, TAS
Australia
*/

Distortion : IM_Module {

  var server;
  var <isLoaded;
  var <preEQ, synth, <postEQ;
  var <inBus, distBus;

  *newMono {
    |
    outBus, distortionGain = 10, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    feedback = false, relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitMono(distortionGain);
  }

  *newStereo {
    |
    outBus, distortionGain = 10, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    feedback = false, relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(2,  outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitStereo(distortionGain);
  }

  prInitMono { | distortionGain = 10 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      server.sync;
      distBus = Bus.audio(server, 1);
      while( { try { mixer.isLoaded } != true }, { 0.001.wait } );

      postEQ = Equalizer.newMono(mixer.chanMono(0), group, 'addToHead');
      server.sync;
      while( { try { postEQ.isLoaded } != true }, { 0.001.wait; });

      synth = Synth(\prm_DistortionMono, [\inBus, distBus, \outBus, postEQ.inBus, \distortionGain, distortionGain],
        group, \addToHead);
      while( { try { synth } == nil }, { 0.001.wait; });

      preEQ = Equalizer.newMono(distBus, group, \addToHead);
      while( { try { preEQ.isLoaded } != true } , { 0.001.wait; });

      inBus = preEQ.inBus;
      server.sync;

      isLoaded = true;
    }
  }

  prInitStereo { | distortionGain = 10 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      distBus = Bus.audio(server, 2);

      server.sync;
      while( { try { mixer.isLoaded } != true }, { 0.001.wait } );


      postEQ = Equalizer.newStereo(mixer.chanMono(0), group, 'addToHead');
      server.sync;
      while( { try { postEQ.isLoaded } != true }, { 0.001.wait; });


      synth = Synth(\prm_DistortionStereo, [\inBus, distBus, \outBus, postEQ.inBus, \distortionGain, distortionGain],
        group, \addToHead);
      while( { try { synth } == nil }, { 0.001.wait; });

      preEQ = Equalizer.newStereo(distBus, group, \addToHead);
      while( { try { preEQ.isLoaded } != true } , { 0.001.wait; });

      inBus = preEQ.inBus;
      server.sync;

      isLoaded = true;
    }
  }

  prAddSynthDefs {

    SynthDef(\prm_DistortionMono, {
      | inBus = 0, outBus = 0, amp = 1, distortionGain = 10 |
      var input, distortion, sig;
      input = In.ar(inBus, 1);
      distortion = (input * distortionGain).distort;
      sig = distortion * amp;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_DistortionStereo, {
      | inBus = 0, outBus = 0, amp = 1, distortionGain = 10 |
      var input, distortion, sig;
      input = In.ar(inBus, 2);
      distortion = (input * distortionGain).distort;
      sig = distortion * amp;
      Out.ar(outBus, sig);
    }).add;

  }

  //////// public functions:

  free {
    postEQ.free;
    synth.free;
    preEQ.free;
    distBus.free;

    postEQ = nil;
    synth = nil;
    preEQ = nil;
    distBus = nil;

    this.freeModule;
  }

  setDistortionGain { | gain = 10 |
    synth.set(\distortionGain, gain);
  }

}