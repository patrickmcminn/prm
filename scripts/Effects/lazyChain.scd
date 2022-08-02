(
~chain = Chain.new(~prm.audioIn,
	~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
	~prm.procGroup, \addToHead);

~digitone = IM_Mixer_1Ch.new(~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,false, ~prm.procGroup, \addToHead);
)

(
~chInput = IM_HardwareIn.new(~pickupIn, ~chain.inBus);
~chMic = IM_HardwareIn.new(~micIn, ~chain.inBus);
~digitoneInput = IM_HardwareIn.newStereo(2, ~digitone.chanStereo, ~prm.procGroup, \addToHead);
)



~apc.setPage('chain');


~apc.setActiveMixerBank(1);
~apc.setActiveMixerEncodersBank(1);

~apc.turnTrackSelectButtonOff(1);

s.meter