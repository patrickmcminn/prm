IM_Module {
  var <group;
  var <mixer;

  *new { |numChans = 1, outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil,
    send3Bus = nil, feedback = false, relGroup = nil, addAction = \addToHead|

    ^super.new.prModuleInit(numChans, outBus, send0Bus, send1Bus, send2Bus, send3Bus,
      feedback, relGroup, addAction);
  }

  prModuleInit { |numChans, outBus, send0Bus, send1Bus, send2Bus, send3Bus,
    feedback, relGroup, addAction|

    var server = Server.default;

    server.waitForBoot {
      group = Group(relGroup, addAction);
      server.sync;

      if( numChans == 1,
        { mixer = IM_Mixer_1Ch(outBus, send0Bus, send1Bus, send2Bus, send3Bus,
          feedback, group, \addToTail) },
        { mixer = IM_Mixer(numChans, outBus, send0Bus, send1Bus, send2Bus, send3Bus,
            feedback, group, \addToTail) }
      );

    };
  }

  freeModule {
    fork {
      mixer.mute;
      mixer.free;

      // Wait until the mixer is cleaned up before freeing the group
      while( { try { mixer.group } != nil }, { 0.01.wait } );
      group.free;

      mixer = nil;
      group = nil;
    };
  }
}


+ IM_Module {
  setVol { |chan = 0, db = 0, lagTime = 0.1|
    if( mixer.class == IM_Mixer_1Ch,
      { mixer.setVol(db, lagTime) },
      { mixer.setVol(chan, db, lagTime) }
    );
  }

  isMuted { | chan = 0 |
    if( mixer.class == IM_Mixer_1Ch,
      { ^mixer.isMuted },
      { ^mixer.isMuted(chan) }
    );
  }

  mute { |chan = 0|
    if( mixer.class == IM_Mixer_1Ch,
      { mixer.mute },
      { mixer.mute(chan) }
    );
  }

  unMute { |chan = 0|
    if( mixer.class == IM_Mixer_1Ch,
      { mixer.unMute },
      { mixer.unMute(chan) }
    );
  }

  tglMute { |chan = 0|
    if( mixer.class == IM_Mixer_1Ch,
      { mixer.tglMute },
      { mixer.tglMute(chan) }
    );
  }

  fadeOut { |chan = 0, dur = 5|
    if( mixer.class == IM_Mixer_1Ch,
      { mixer.fadeOut(dur) },
      { mixer.fadeOut(chan, dur) }
    );
  }
}