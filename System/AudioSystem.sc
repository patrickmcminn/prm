/*
Monday, November 10th 2014
adapted from IM_AudioSystem in ImpMach
by Jonah Beram and Patrick McMinn
prm
*/

AudioSystem {
  var <isLoaded;
  var <procGroup, systemGroup;
  var hardwareOut, <systemMixer;
  var <irLibrary;

  var <reverb, <granulator;

  var <submixerA, <submixerB, <submixerC;

  var <songBook;

  *new { | numOutputs = 2 |
    ^super.new.prInit(numOutputs);
  }

  prInit { |numOutputs|
    var server = Server.default;

    this.prSetServerOptions(server, 64, 131702, 512, nil);

    server.waitForBoot {
      var masterOutArray;
      var tempTime = SystemClock.seconds;

      isLoaded = false;

      hardwareOut = IM_HardwareOut(numOutputs);
      procGroup = Group(server, \addToHead);
      systemGroup = Group(procGroup, \addAfter);
      server.sync;

      // Fix input checking: a numOutputs of 1 will result 0.5 passed to Array.fill
      masterOutArray = Array.fill(numOutputs / 2, { |index| index * 2 });
      systemMixer = IM_MasterMixer.new(masterOutArray, systemGroup);
      // while( { try { systemMixer.inBus(0) } == nil }, { 0.001.wait });
      server.sync;
      while ( { try { systemMixer.isLoaded} != true }, { 0.001.wait } );

      irLibrary = IM_IRLibrary.new("~/Library/Application Support/SuperCollider/Extensions/prm/Effects/Reverb/ImpulseResponses");
      server.sync;
      while( { try { irLibrary.isLoaded } != true }, { 0.001.wait; });

      //granulator = IM_Granulator(systemMixer.inBus(0),
        //relGroup: systemGroup, addAction: \addToHead);
      granulator = GranularDelay.new(systemMixer.inBus, relGroup: systemGroup, addAction: \addToHead);
      server.sync;
      while( {  try { granulator.isLoaded } != true }, { 0.001.wait; });
      granulator.granulator.setCrossfade(1);
      granulator.delay.setMix(0);

      reverb = IM_Reverb.newConvolution(systemMixer.inBus(0), bufName: irLibrary.irDict['3.4Cathedral'],
        relGroup: systemGroup, addAction: \addToHead);
      server.sync;
      while( { try { reverb.isLoaded } != true }, { 0.001.wait; });

      submixerA = Looper.newStereo(systemMixer.inBus, 30, 0, reverb.inBus, granulator.inBus, nil, nil,
        procGroup, \addToHead);
      submixerB = Looper.newStereo(systemMixer.inBus, 30, 0, reverb.inBus, granulator.inBus, nil, nil,
        procGroup, \addToHead);
      submixerC = Looper.newStereo(systemMixer.inBus, 30, 0, reverb.inBus, granulator.inBus, nil, nil,
        procGroup, \addToHead);

      //songBook = IM_SongBook(systemMixer.inBus, reverb.inBus, granulator.inBus, nil, nil, procGroup);

      isLoaded = true;

    };
  }

  // Define audio device, block size, and total memory reserved for SCLang
  // Default memory size = 2 ** 17
  prSetServerOptions { |server, blockSize = 64, memSize = 131072, numAudioBusChannels = 512, devName|
    server.options.blockSize = blockSize;
    server.options.memSize = memSize;
    server.options.numAudioBusChannels = numAudioBusChannels;
    // server.options.device = (devName);
  }

  // Still some problems here with nodes trying to be freed after their groups have been
  free {
    fork {
      systemMixer.muteAll;

      submixerA.free;
      submixerB.free;
      submixerC.free;

      hardwareOut.free;
      reverb.free;
      granulator.free;
      systemMixer.free;

      while( { systemMixer.group != nil }, { 0.001.wait } );

      systemGroup.free;
      procGroup.free;
      songBook.free;

      hardwareOut = nil;

      submixerA = nil;
      submixerB = nil;
      submixerC = nil;

      reverb = nil;
      granulator = nil;
      systemMixer = nil;

      systemGroup = nil;
      procGroup = nil;
      songBook = nil;

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