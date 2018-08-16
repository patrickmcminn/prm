/*
Monday, January 12th 2015
Song.sc
prm
*/

Song {

  var isLoaded;
  var server;
  var <group;
  var <mixer;

  *new {
    |
    numChannels = 1, outBus = 0,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, feedback = false,
    relGroup = nil, addAction = 'addToHead'
    |
    ^super.new.prSongInit(numChannels, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction);
  }

  prSongInit {
    |
    numChannels = 1, outBus = 0,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, feedback = false,
    relGroup = nil, addAction = 'addToHead'
    |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      group = Group.new(relGroup, addAction);
      server.sync;

      mixer = IM_Mixer.new(numChannels, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback,
        relGroup, addAction);
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      isLoaded = true;
    }
  }

  //////// public functions:

  freeSong {

    mixer.masterChan.mute;

    mixer.free;

    group.free;

    mixer = nil;
    group = nil;

  }

}