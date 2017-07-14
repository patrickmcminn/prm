/*
Monday, November 10th 2014
adapted from IM_AudioSystem in ImpMach
by Jonah Beram and Patrick McMinn
prm
*/

AudioSystem {

  var <isLoaded;
  var <procGroup, systemGroup;
  var hardwareOut, <systemMixer, <monitorMixer;
  var <irLibrary;

  var <reverb, <granulator, <modularSend, <delay;

  var <submixerA, <submixerB, <submixerC;
  var <splitter;

  var <modular, modularIn, <microphone, micIn, <moog, <moogIn;

  var <subtractive;

  var <songBook;

  var <server;

  *new { | numOutputs = 2 |
    ^super.new.prInit(numOutputs);
  }

  prInit { |numOutputs|
    server = Server.default;

    this.prSetServerOptions(server, 64, 131072, 1024, nil);

    server.waitForBoot {
      var masterOutArray;
      //var tempTime = SystemClock.seconds;

      isLoaded = false;

      server.sync;

      server.latency = 0.05;

      hardwareOut = IM_HardwareOut(numOutputs);
      procGroup = Group(server, \addToHead);
      systemGroup = Group(procGroup, \addAfter);
      server.sync;

      // Fix input checking: a numOutputs of 1 will result 0.5 passed to Array.fill
      masterOutArray = Array.fill(numOutputs / 2, { |index| index * 2 });
      monitorMixer = IM_MasterMixer.new([0, 1], systemGroup);
      while({ try { monitorMixer.isLoaded } != true }, { 0.001.wait; });
      systemMixer = IM_MasterMixer.new([2, 3], systemGroup);
      // while( { try { systemMixer.inBus(0) } == nil }, { 0.001.wait });
      server.sync;
      while ( { try { systemMixer.isLoaded} != true }, { 0.001.wait } );



      irLibrary = IM_IRLibrary.new("~/Library/Application Support/SuperCollider/Extensions/prm/Effects/Reverb/ImpulseResponses");
      server.sync;
      while( { try { irLibrary.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newStereo(2, [systemMixer.inBus, monitorMixer.inBus], relGroup: procGroup, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      // delay:
      delay = SimpleDelay.newStereo(splitter.inBus, 1.5, 0.35, 10, relGroup: systemGroup, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      // send out to modular system
      modularSend = MonoHardwareSend.new(4, relGroup: systemGroup, addAction: \addToHead);
      while({ try { modularSend.isLoaded } != true }, { 0.001.wait; });

      //granulator = IM_Granulator(systemMixer.inBus(0),
        //relGroup: systemGroup, addAction: \addToHead);
      granulator = GranularDelay.new(splitter.inBus, relGroup: systemGroup, addAction: \addToHead);
      server.sync;
      while( {  try { granulator.isLoaded } != true }, { 0.001.wait; });
      granulator.granulator.setCrossfade(1);
      granulator.delay.setMix(0);

      //reverb = Wash.newStereo(systemMixer.inBus(0), relGroup: systemGroup, addAction: \addToHead);
      reverb = IM_Reverb.newConvolution(splitter.inBus, bufName: irLibrary.irDict['3.2EmptyChurch'],
        relGroup: systemGroup, addAction: \addToHead);
      server.sync;
      while( { try { reverb.isLoaded } != true }, { 0.001.wait; });

      //reverb.setMix(1);

      submixerA = Looper.newStereo(splitter.inBus, 30, 0, reverb.inBus, granulator.inBus, modularSend.inBus, nil,
        procGroup, \addToHead);
      while( { try { submixerA.isLoaded } != true }, { 0.001.wait; });
      submixerB = Looper.newStereo(splitter.inBus, 30, 0, reverb.inBus, granulator.inBus, modularSend.inBus, nil,
        procGroup, \addToHead);
      while( { try { submixerB.isLoaded } != true }, { 0.001.wait; });
      submixerC = Looper.newStereo(splitter.inBus, 30, 0, reverb.inBus, granulator.inBus, modularSend.inBus, nil,
        procGroup, \addToHead);
      while( { try { submixerC.isLoaded } != true }, { 0.001.wait; });



      submixerA.mixer.setPreVol(3);
      submixerB.mixer.setPreVol(3);
      submixerC.mixer.setPreVol(3);

      modular = IM_Mixer_1Ch.new(this.submixB, reverb.inBus, granulator.inBus, modularSend.inBus,
        nil, false, procGroup, \addToHead);
      while( { try { modular.isLoaded } != true }, { 0.001.wait; });
      modularIn = IM_HardwareIn.new(2, modular.chanMono, procGroup, \addToHead);
      while({ try { modularIn.isLoaded } != true }, { 0.001.wait; });

      microphone = IM_Mixer_1Ch.new(this.submixC, reverb.inBus, granulator.inBus, modularSend.inBus, nil, false,
        procGroup, \addToHead);
      while({ try { microphone.isLoaded } != true }, { 0.001.wait; });
      micIn = IM_HardwareIn.new(1, microphone.chanMono(0), procGroup, \addToHead);
      while({ try { micIn.isLoaded } != true }, { 0.001.wait; });

      moog = IM_Mixer_1Ch.new(this.submixA, reverb.inBus, granulator.inBus, modularSend.inBus, nil, false,
        procGroup, \addToHead);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });
      moogIn = IM_HardwareIn.new(3, moog.chanMono(0), procGroup, \addToHead);
      while({ try { moogIn.isLoaded } != true }, { 0.001.wait; });

      // modular + mic come in muted
      modular.setVol(-70);
      moog.setVol(-70);
      microphone.setVol(-70);
      microphone.setPreVol(3);

      /*
      subtractive = Subtractive.new(this.submixB, reverb.inBus, granulator.inBus, modularSend.inBus, nil, procGroup,
        \addToHead);
      while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });
      */

      songBook = IdentityDictionary.new;


      isLoaded = true;

    };
  }

  // Define audio device, block size, and total memory reserved for SCLang
  // Default memory size = 2 ** 17
  prSetServerOptions { |server, blockSize = 64, memSize = 131072, numAudioBusChannels = 512, devName|
    server.options.blockSize = blockSize;
    server.options.memSize = memSize;
    server.options.numAudioBusChannels = numAudioBusChannels;
    server.options.hardwareBufferSize = 256;
    // comment out for verbosity:
    //server.options.verbosity = -1;
    // server.options.device = (devName);
  }

  // Still some problems here with nodes trying to be freed after their groups have been
  free {
    fork {
      systemMixer.muteAll;

      modularIn.free;
      modular.free;

      submixerA.free;
      submixerB.free;
      submixerC.free;

      hardwareOut.free;
      reverb.free;
      granulator.free;
      modularSend.free;
      systemMixer.free;

      while( { systemMixer.group != nil }, { 0.001.wait } );

      systemGroup.free;
      procGroup.free;
      //songBook.free;

      hardwareOut = nil;

      modularIn = nil;
      modular = nil;

      submixerA = nil;
      submixerB = nil;
      submixerC = nil;

      reverb = nil;
      granulator = nil;
      modularSend = nil;

      systemMixer = nil;

      systemGroup = nil;
      procGroup = nil;
      //songBook = nil;

      // Server.default.quit;
    };
  }
}

// Convenience functions
+ AudioSystem {
  setVol { |chan = 0, db = 0, lagTime = 0| systemMixer.setVol(chan, db, lagTime) }
  mute { |chan = 0| systemMixer.mute(chan) }
  unMute { |chan = 0| systemMixer.unMute(chan) }
  tglMute { |chan = 0| systemMixer.tglMute(chan) }
  fadeOut { |chan = 0, dur = 5| systemMixer.fadeOut(chan, dur) }

  makeSong { |songName = nil| ^songBook.makeSong(songName) }
  getSong { |songName = nil| ^songBook.getSong(songName) }
  freeSong { |songName = nil| ^songBook.freeSong(songName) }

  submixA { ^submixerA.inBus; }
  submixB { ^submixerB.inBus; }
  submixC { ^submixerC.inBus; }
}