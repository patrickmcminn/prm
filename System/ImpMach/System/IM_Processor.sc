IM_Processor : IM_Module {
  var <inBus;

  *new { |numInBusses = 1, numChans = 1, outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil,
    send3Bus = nil, feedback = false, relGroup = nil, addAction = \addToHead|

    ^super.new(numChans, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prProcessorInit(numInBusses);
  }

	*newNoSends {
		|numInBusses = 1, numChans = 1, outBus = 0, feedback = false, relGroup = nil, addAction = \addToHead|

    ^super.newNoSends(numChans, outBus,feedback, relGroup, addAction).prProcessorInit(numInBusses);
	}

  prProcessorInit { |numInBusses|
    inBus = Bus.audio(Server.default, numInBusses);
  }

  freeProcessor {
    inBus.free;
    inBus = nil;

    super.freeModule;
  }
}