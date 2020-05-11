IM_Mixer_1Ch {
  var myOutBus, myNilBus;
  var mySend0Bus, mySend1Bus, mySend2Bus, mySend3Bus;

  var <group;
  var <masterChan;

  var <isFeedback;
  var <isLoaded;

	var <sends;

  *new { |outBus = 0,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, feedback = false,
    relGroup = nil, addAction = \addToHead|

    ^super.new.prInit(outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback,
      relGroup, addAction);
  }

	*newNoSends { |outBus = 0, feedback = false, relGroup = nil, addAction = \addToHead|
		^super.new.prInitNoSends(outBus, feedback, relGroup, addAction);
  }

  prInit { |outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback,
    relGroup, addAction|

    var server = Server.default;

    isLoaded = false;

		sends = true;

    myOutBus = outBus;
    mySend0Bus = send0Bus;
    mySend1Bus = send1Bus;
    mySend2Bus = send2Bus;
    mySend3Bus = send3Bus;

    isFeedback = feedback;

    server.waitForBoot {
      group = Group(relGroup, addAction);
      myNilBus = Bus.audio(server, 2);
      server.sync;

      masterChan = IM_ChannelStrip(myOutBus, mySend0Bus, mySend1Bus, mySend2Bus,
        mySend3Bus, myNilBus, isFeedback, group, \addToTail);

      while ( { try { masterChan.isLoaded } != true }, { 0.001.wait } );

      isLoaded = true;
    };
  }

	prInitNoSends {
		|outBus,  feedback, relGroup, addAction|

    var server = Server.default;

    isLoaded = false;

		sends = false;

    myOutBus = outBus;

    isFeedback = feedback;

    server.waitForBoot {
      group = Group(relGroup, addAction);
      myNilBus = Bus.audio(server, 2);
      server.sync;

      masterChan = IM_ChannelStrip(myOutBus, myNilBus, isFeedback, group, \addToTail);

      while ( { try { masterChan.isLoaded } != true }, { 0.001.wait } );

      isLoaded = true;
    };
	}

  // Add error handling to this
  chan { ^masterChan }

  muteAll { masterChan.mute }
  unMuteAll { masterChan.unMute }

  solo { }

  free {
    fork {
      masterChan.mute;
      masterChan.free;
      while( { try { masterChan.synth } != nil }, { 0.001.wait });

      group.free;
      myNilBus.free;

      group = nil;
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
+ IM_Mixer_1Ch {
  inBus { ^masterChan.inBus }
  chanMono { ^masterChan.chanMono }
  chanStereo { ^masterChan.chanStereo }

  mute { masterChan.mute }
  unMute { masterChan.unMute }
  tglMute { masterChan.tglMute }
  isMuted { ^masterChan.isMuted }

  setPreVol { |db = 0| masterChan.setPreVol(db) }
  setVol { |db = 0, lagTime = 0| masterChan.setVol(db, lagTime) }
  fadeOut { |dur = 1| masterChan.fadeOut(dur) }
  fade { | targetdb = 0, dur = 1 | masterChan.fade(targetdb, dur); }

	setSendVol { |sendNum = 0, db = 0| if(sends == true, { masterChan.setSendVol(sendNum, db) });}
	setSendPre { if(sends == true, {  masterChan.setSendPre });}
	setSendPost {if(sends == true, {  masterChan.setSendPost });}

  setPanBal { |panBal = 0| masterChan.setPanBal(panBal) }

  muteMaster { masterChan.mute }
  unMuteMaster { masterChan.unMute }
  tglMuteMaster { masterChan.tglMute }

  setMasterVol { |db = 0, lagTime = 0| masterChan.setVol(db, lagTime) }
  fadeOutMaster { |dur = 1| masterChan.fadeOut(dur) }

  mapAmp { | bus | masterChan.mapAmp(bus); }
  mapPan { | bus | masterChan.mapPan(bus); }

	mapSend0Amp { | bus | if(sends == true, { masterChan.mapSend0Amp(bus) });}
	mapSend1Amp { | bus | if(sends == true, {  masterChan.mapSend1Amp(bus) }); }
	mapSend2Amp { | bus | if(sends == true, {  masterChan.mapSend2Amp(bus) }); }
	mapSend3Amp { | bus | if(sends == true, {  masterChan.mapSend3Amp(bus) }); }

  vol {  ^masterChan.vol; }
  sendVol { | sendNum = 0 |
		if(sends == true, {
			switch(sendNum,
				0, { ^masterChan.send0Vol },
				1, { ^masterChan.send1Vol },
				2, { ^masterChan.send2Vol },
				3, { ^masterChan.send3Vol }
			);
		});
  }
  panBal { ^masterChan.panBal; }
  preOrPost { ^masterChan.preOrPost; }
}