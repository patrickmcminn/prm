// Updated as IM_Song subclass 5/21/2014 JAB
IM_BandDirect : IM_Module {
  var <isLoaded;
  var trumpetMute, trumpet, flugel, guitar;

  *new { |inBusses = #[0, 1, 2, 3], outBus = 0, send0Bus = nil, send1Bus = nil,
    send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = \addToHead|

    ^super.new(4, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(inBusses);
  }

  prInit { |inBusses = #[0, 1, 2, 3]|
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      while( { try { mixer.isLoaded } != true }, { 0.001.wait });

      trumpetMute = IM_HardwareIn(inBusses[0], mixer.chanMono(0), group, \addToHead);
      trumpet = IM_HardwareIn(inBusses[1], mixer.chanMono(1), group, \addToHead);
      flugel = IM_HardwareIn(inBusses[2], mixer.chanMono(2), group, \addToHead);
      guitar = IM_HardwareIn(inBusses[3], mixer.chanMono(3), group, \addToHead);

      while({ try { guitar.isLoaded } != true }, { 0.001.wait });
      isLoaded = true;
    };
  }

  free {
    isLoaded = false;
    mixer.masterChan.mute;

    trumpetMute.free;
    trumpet.free;
    flugel.free;
    guitar.free;

    trumpetMute = nil;
    trumpet = nil;
    flugel = nil;
    guitar = nil;

    super.freeModule;
  }
}