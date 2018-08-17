/*
Saturday, April 22nd 2017
Light_Sends.sc
prm
*/

Light_Sends : Song {

  var server, <isLoaded;
  var <reverb, <delays, <multiShift;

  *new {
    | outBus, reverbBuf, send0Bus send1Bus, send2Bus, send3Bus,
    relGroup, addAction = 'addToHead' |
    ^super.new(5, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(reverbBuf);
  }

  prInit { | reverbBuf |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      reverb = IM_Reverb.newConvolution(mixer.chanStereo(0), bufName: reverbBuf,
        relGroup: group, addAction: \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      delays = Light_Delays.new(mixer.chanStereo(1), group, \addToHead);
      while({ try { delays.isLoaded } != true }, { 0.001.wait; });

      multiShift = Light_MultiShift.new(mixer.chanStereo(2), group, \addToHead);
      while({ try { multiShift.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      // reverb to reverb!
      mixer.setSendVol(0, 0, -17);
      mixer.setVol(0, -6);

      // delays:
      mixer.setVol(1, -3);

      // multiShift:
      mixer.setVol(2, -9);

      mixer.masterChan.setVol(-9);

      isLoaded = true;
    }
  }

}