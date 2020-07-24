// 5/9/2014 JAB

// A mixer is simply a list of audio strips of a given type, a memory of the type of strips
// it holds, and group storage

// Also: add a special case for a mixer with a single channel strip
// that doesn't have the overhead of a single channel strip and a
// single master strip?:  // var isSingle;  // *newSingleChan { }
IM_Mixer {
  var myOutBus, myNilBus;
  var mySend0Bus, mySend1Bus, mySend2Bus, mySend3Bus;

  var <group;

  var channelList;
  var <masterChan;

  var <isFeedback;
  var <isLoaded;

  *new { |numChans = 1, outBus = 0,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, feedback = false,
    relGroup = nil, addAction = \addToHead|

    ^super.new.prInit(numChans, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback,
      relGroup, addAction);
  }

	*newNoSends { | numChans = 1, outBus = 0, feedback = false, relGroup = nil, addAction = \addToHead|
		^super.new.prInitNoSends(numChans, outBus, feedback, relGroup, addAction);
	}

  prInit { |numChans, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback,
    relGroup, addAction|

    var server = Server.default;

    isLoaded = false;
    myOutBus = outBus;
    mySend0Bus = send0Bus;
    mySend1Bus = send1Bus;
    mySend2Bus = send2Bus;
    mySend3Bus = send3Bus;

    isFeedback = feedback;
    channelList = List.newClear(0);

    server.waitForBoot {
      group = Group(relGroup, addAction);
      myNilBus = Bus.audio(server, 2);
      server.sync;

      masterChan = IM_ChannelStrip(myOutBus, mySend0Bus, mySend1Bus, mySend2Bus,
        mySend3Bus, myNilBus, isFeedback, group, \addToTail);
      while ( { try { masterChan.isLoaded } != true }, { 0.001.wait } );

      numChans.do { this.addStrip };

      channelList.do { |chan|
        while ( { try { chan.isLoaded } != true }, { 0.001.wait } );
      };

      isLoaded = true;
    };
  }

	prInitNoSends { | numChans = 1, outBus = 0, feedback = false, relGroup = nil, addAction = \addToHead|
		var server = Server.default;

    isLoaded = false;
    myOutBus = outBus;

    isFeedback = feedback;
    channelList = List.newClear(0);

    server.waitForBoot {
      group = Group(relGroup, addAction);
      myNilBus = Bus.audio(server, 2);
      server.sync;

      masterChan = IM_ChannelStrip.newNoSends(myOutBus, myNilBus, isFeedback, group, \addToTail);
      while ( { try { masterChan.isLoaded } != true }, { 0.001.wait } );

      numChans.do { this.addStripNoSends };

      channelList.do { |chan|
        while ( { try { chan.isLoaded } != true }, { 0.001.wait } );
      };

      isLoaded = true;
    };

	}

  addStrip {
    channelList.add(
      IM_ChannelStrip.new(masterChan.inBusStereo, mySend0Bus, mySend1Bus, mySend2Bus,
        mySend3Bus, myNilBus, isFeedback, group)
    );
  }

	addStripNoSends {
		channelList.add(IM_ChannelStrip.newNoSends(masterChan.inBusStereo, myNilBus, isFeedback, group));
	}

  // NOT WORKING: when you remove a channel and add it back, messages to it stop working
  // Is the list not shrinking?
  removeStrip { |index|
    if( (index >= 0) && (index < channelList.size),
      {
        channelList[index].free;
        channelList[index] = nil;
        channelList.remove(index);
      },
      { "Index out of bounds.".postln }
    );
  }

  // Add error handling to this
  chan { |index = 0| ^channelList[index] }

  muteAll { channelList.do { |chan| chan.mute } }
  unMuteAll { channelList.do { |chan| chan.unMute } }

  // Not sure if this works yet. What about un-soloing? mute states won't be stored
  solo { |index = 0| channelList.do { |chan, chanIndex| if(chanIndex != index, { chan.mute }) } }

  free {
    fork {
      masterChan.mute;

      channelList.do { |item| item.free; item = nil; };
      masterChan.free;

      // NON WORKING attempt to wait a dynamic amount of time before attempting to free the group
      // while({ masterChan.isFeedback != nil }, { 0.01.wait });

      // Arbitrary wait time to allow all the channels to free themselves before the group frees
      0.5.wait;
      group.free;
      myNilBus.free;

      group = nil;
      channelList = nil;
      masterChan = nil;

      myOutBus = nil;
      myNilBus = nil;
      mySend0Bus = nil;
      mySend1Bus = nil;
      mySend2Bus = nil;
      mySend3Bus = nil;

      isFeedback = nil;
      isLoaded = nil;
    };
  }
}


// Convenience function wrappers into the ChannelStrip class
+ IM_Mixer {
  inBus { |index = 0| ^this.chan(index).inBus }
  chanMono { |index = 0| ^this.chan(index).chanMono(index) }
  chanStereo { |index = 0| ^this.chan(index).chanStereo(index) }

  mute { |index = 0| this.chan(index).mute }
  unMute { |index = 0| this.chan(index).unMute }
  tglMute { |index = 0| this.chan(index).tglMute }
  isMuted { | index = 0 | ^this.chan(index).isMuted }

  setPreVol { |index = 0, db = 0| this.chan(index).setPreVol(db) }
  setVol { |index = 0, db = 0, lagTime = 0| this.chan(index).setVol(db, lagTime) }
  fadeOut { |index = 0, dur = 1| this.chan(index).fadeOut(dur) }
  fade { | index = 0, targetdb = 0, dur = 1 | this.chan(index).fade(targetdb, dur); }

  setSendVol { |index = 0, sendNum = 0, db = 0| this.chan(index).setSendVol(sendNum, db) }
  setSendPre { |index = 0| this.chan(index).setSendPre }
  setSendPost { |index = 0| this.chan(index).setSendPost }

  setPanBal { |index = 0, panBal = 0| this.chan(index).setPanBal(panBal) }

  muteMaster { masterChan.mute }
  unMuteMaster { masterChan.unMute }
  tglMuteMaster { masterChan.tglMute }
  isMutedMaster { ^masterChan.isMuted }

  setMasterVol { |db = 0, lagTime = 0| masterChan.setVol(db, lagTime) }
  fadeOutMaster { |dur = 1| masterChan.fadeOut(dur) }

  vol { | index = 0 | ^this.chan(index).vol; }
  masterVol { ^this.masterChan.vol; }
  sendVol { | index = 0, sendNum = 0 |
    switch(sendNum,
      0, { ^this.chan(index).send0Vol },
      1, { ^this.chan(index).send1Vol },
      2, { ^this.chan(index).send2Vol },
      3, { ^this.chan(index).send3Vol }
    );
  }

  mapSend0Amp { | index, bus | this.chan(index).mapSend0Amp(bus) }
  mapSend1Amp {  | index, bus | this.chan(index).mapSend1Amp(bus) }
  mapSend2Amp { | index, bus | this.chan(index).mapSend2Amp(bus) }
  mapSend3Amp { | index, bus | this.chan(index).mapSend3Amp(bus) }

  panBal { | index = 0 | ^this.chan(index).panBal; }
  preOrPost { | index = 0 | ^this.chan(index).preOrPost; }

  mapAmp { | index, bus | this.chan(index).mapAmp(bus) }
  mapPan { | index, bus | this.chan(index).mapPan(bus); }
}