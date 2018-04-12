// 5/9/2014 JAB
// A collection of stereo master faders, commonly set up to output to hardware outs
IM_MasterMixer {
  var <group;
  var channelArray;

  var <isLoaded;

  *new { |outBusArray, relGroup = nil, addAction = \addToTail|
    ^super.new.prInit(outBusArray, relGroup, addAction);
  }

  *newEightDirectOuts { |relGroup = nil, addAction = \addToTail|
    ^super.new.prInit([0, 2, 4, 6], relGroup, addAction);
  }

  prInit { |outBusArray, relGroup, addAction|
    var server = Server.default;

    isLoaded = false;

    if(outBusArray == nil, { outBusArray = [0] });

    server.waitForBoot {
      group = Group(relGroup, addAction);
      server.sync;

      channelArray = Array.fill(outBusArray.size, { |index|
        IM_MasterStrip(outBusArray[index], group)
      });

      channelArray.do { |chan|
        while ( { try { chan.isLoaded } != true }, { 0.001.wait } );
      };

      isLoaded = true;
    };
  }

  chan { |index = 0| ^channelArray[index] }
  inBus { |index = 0| ^channelArray[index].inBus }

  muteAll { channelArray.do { |chan| chan.mute } }
  unMuteAll { channelArray.do { |chan| chan.unMute } }

  free {
    channelArray.do { |masterStrip| masterStrip.free };
    channelArray = nil;

    group.free;
    group = nil;
  }
}


// Convenience functions: access to individual master strips
+ IM_MasterMixer {
  chanMono { "Use .inBus for access to master channel strips. Master channel strips are always stereo.".postln }
  chanStereo { "Use .inBus for access to master channel strips. Master channel strips are always stereo.".postln }

  setVol { |index = 0, db = 0, lagTime = 0| this.chan(index).setVol(db, lagTime) }
  fadeOut { |index = 0, dur = 1| this.chan(index).fadeOut(dur) }

  mute { |index = 0| this.chan(index).mute }
  unMute { |index = 0| this.chan(index).unMute }
  tglMute { |index = 0| this.chan(index).tglMute }

  setBal { |index = 0, bal = 0| this.chan(index).setBal(bal) }

  inBusLeft { |index = 0| ^this.chan(index).inBusLeft }
  inBusRight { |index = 0| ^this.chan(index).inBusRight }

  vol { | index = 0 | ^this.chan(index).vol; }
  sendVol { | index = 0, sendNum = 0 |
    switch(sendNum,
      0, { ^this.chan(index).send0Vol },
      1, { ^this.chan(index).send1Vol },
      2, { ^this.chan(index).send2Vol },
      3, { ^this.chan(index).send3Vol }
    );
  }
  panBal { | index = 0 | ^this.chan(index).panBal; }
}