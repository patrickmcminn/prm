/*
Monday, January 12th 2015
Song.sc
prm
*/

Song {

  var server, <isLoaded;
  var group;
  var <mixerA, <mixerB, <mixerC;

  *new {
    |
    mixAOutBus, mixANumChannels = 1, mixBOutBus, mixBNumChannels = 1, mixCOutBus, mixCNumChannels = 1,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, feedback = false,
    relGroup = nil, addAction = 'addToHead'
    |
    ^super.new.prSongInit(mixAOutBus, mixANumChannels, mixBOutBus, mixBNumChannels, mixCOutBus, mixCNumChannels,
      send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction);
  }

  prSongInit {
    |
    mixAOutBus, mixANumChannels = 1, mixBOutBus, mixBNumChannels = 1, mixCOutBus, mixCNumChannels = 1,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, feedback = false,
    relGroup = nil, addAction = 'addToHead'
    |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      group = Group.new(relGroup, addAction);
      server.sync;

      mixerA = IM_Mixer.new(mixANumChannels, mixAOutBus, send0Bus, send1Bus, send2Bus, send3Bus,
        feedback, group, 'addToHead');
      while({ try { mixerA.isLoaded } != true }, { 0.001.wait; });

      mixerB = IM_Mixer.new(mixBNumChannels, mixBOutBus, send0Bus, send1Bus, send2Bus, send3Bus,
        feedback, group, 'addToHead');
      while({ try { mixerB.isLoaded } != true }, { 0.001.wait; });

      mixerC = IM_Mixer.new(mixCNumChannels, mixCOutBus, send0Bus, send1Bus, send2Bus, send3Bus,
        feedback, group, 'addToHead');
      while({ try { mixerC.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    }
  }

  //////// public functions:

  freeSong {

    mixerA.masterChan.mute;
    mixerB.masterChan.mute;
    mixerC.masterChan.mute;

    mixerA.free;
    mixerB.free;
    mixerC.free;
    group.free;

    mixerA = nil;
    mixerB = nil;
    mixerC = nil;
    group = nil;

  }

}