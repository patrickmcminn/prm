/*
Saturday, April 22nd 2017
Light_Sends.sc
prm
*/

Light_Sends : Song {

  var server, <isLoaded;
  var <reverb, <delays, <multiShift;

  *new {
    | mixAOutBus, mixBOutBus, mixCOutBus, reverbBuf, send0Bus send1Bus, send2Bus, send3Bus,
    relGroup, addAction = 'addToHead' |
    ^super.new(mixAOutBus, 1, mixBOutBus, 1, mixCOutBus, 3, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(reverbBuf);
  }

  prInit { | reverbBuf |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      reverb = IM_Reverb.newConvolution(mixerC.chanStereo(0), bufName: reverbBuf,
        relGroup: group, addAction: \addToTail);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      delays = Light_Delays.new(mixerC.chanStereo(1), group, \addToHead);
      while({ try { delays.isLoaded } != true }, { 0.001.wait; });

      multiShift = Light_MultiShift.new(mixerC.chanStereo(2), group, \addToHead);
      while({ try { multiShift.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      // reverb to reverb!
      mixerC.setSendVol(0, 0, -17);
      mixerC.setVol(0, -6);

      // delays:
      mixerC.setVol(1, -3);

      // multiShift:
      mixerC.setVol(2, -9);

      mixerC.masterChan.setVol(-9);

      isLoaded = true;
    }
  }

}