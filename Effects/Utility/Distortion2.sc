/*
Monday, January 19th 2015
Distortion.sc
prm
Hobart, TAS
Australia

Friday, June 7th 2019
Distortion2.sc
prm
*/

Distortion2 : IM_Module {

  var server;
  var <isLoaded;
  var <preEQ, synth, <postEQ;
  var <inBus, distBus;
  var <distAmp, <distSmooth;

  *newMono {
    |
    outBus, distortionAmp = 0.5, distortionSmooth = 0.5, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    feedback = false, relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitMono(distortionAmp, distortionSmooth);
  }

  *newStereo {
    |
    outBus, distortionAmp = 0.5, distortionSmooth = 0.5, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    feedback = false, relGroup = nil, addAction = 'addToHead'
    |
    ^super.new(1,  outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInitStereo(distortionAmp, distortionSmooth);
  }

  prInitMono { | distortionAmp, distortionSmooth |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      server.sync;
      distBus = Bus.audio(server, 1);
      distAmp = distortionAmp;
      distSmooth = distortionSmooth;
      while( { try { mixer.isLoaded } != true }, { 0.001.wait } );
      mixer.mute;

      postEQ = Equalizer.newMono(mixer.chanMono(0), group, 'addToHead');
      server.sync;
      while( { try { postEQ.isLoaded } != true }, { 0.001.wait; });

      synth = Synth(\prm_Distortion2Mono, [\inBus, distBus, \outBus, postEQ.inBus, \distSmooth, distSmooth, \distAmp, distAmp],
        group, \addToHead);
      while( { try { synth } == nil }, { 0.001.wait; });

      preEQ = Equalizer.newMono(distBus, group, \addToHead);
      while( { try { preEQ.isLoaded } != true } , { 0.001.wait; });

      inBus = preEQ.inBus;
      server.sync;

      mixer.unMute;

      isLoaded = true;
    }
  }

  prInitStereo { | distortionAmp, distortionSmooth |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      distBus = Bus.audio(server, 2);
      distAmp = distortionAmp;
      distSmooth = distortionSmooth;
      server.sync;
      while( { try { mixer.isLoaded } != true }, { 0.001.wait } );


      postEQ = Equalizer.newStereo(mixer.chanStereo(0), group, 'addToHead');
      server.sync;
      while( { try { postEQ.isLoaded } != true }, { 0.001.wait; });


      synth = Synth(\prm_Distortion2Stereo, [\inBus, distBus, \outBus, postEQ.inBus, \distSmooth, distSmooth, \distAmp, distAmp],
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

    SynthDef(\prm_Distortion2Mono, {
      | distAmp = 0.5, smooth = 0.5,  inBus = 0, outBus = 0, amp = 1  |
      var input, distortion, sig;
      input = In.ar(inBus, 1);
      distortion = CrossoverDistortion.ar(input, distAmp, smooth);
      //distortion = (input * distortionGain).distort;
      sig = distortion * amp;
      Out.ar(outBus, sig);
    }, [0.05] ).add;

    SynthDef(\prm_Distortion2Stereo, {
      | distAmp = 0.5, smooth = 0.5,  inBus = 0, outBus = 0, amp = 1  |
      var input, distortion, sig;
      input = In.ar(inBus, 2);
      distortion = CrossoverDistortion.ar(input, distAmp, smooth);
      //distortion = (input * distortionGain).distort;
      sig = distortion * amp;
      Out.ar(outBus, sig);
    }, [0.05]).add;

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

  setDistAmp { | amp = 0.5 |
    distAmp = amp;
    synth.set(\distAmp, distAmp);
  }

  setDistSmooth { | smooth = 0.5 |
    distSmooth = smooth;
    synth.set(\smooth, distSmooth);
  }

}