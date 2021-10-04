

~chain = Chain.new(~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
	~prm.procGroup, \addToHead);
~chInput = IM_HardwareIn.new(~pickupIn, ~chain.inBus, ~prm.procGroup, \addToHead);
~chMic = IM_HardwareIn.new(~micIn, ~chain.inBus, ~prm.procGroup, \addToHead);

~apc.setPage('chain');
