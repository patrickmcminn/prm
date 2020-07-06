/*
Friday, July 3rd 2020
PlateCylinder.sc
prm
*/

PlateCylinder : IM_Module {

	var server, <isLoaded;

	var input, splitter, <equalizer;
	var <multiShift, <delayNetwork;
	var <sampler, <subtractive;

	*new {
		|
		outBus, contactMicIn,
		send0Bus, send1Bus, send2Bus, send3Bus,
		relGroup, addAction = 'addToHead'
		|
		^super.new(5, outBus, send0Bus, send1Bus, send2Bus, send3Bus,  false, relGroup, addAction).prInit(contactMicIn);
	}

	prInit { | contactMicIn |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			equalizer = Equalizer.newMono(mixer.chanStereo(0), group, \addToHead);
			while( { try { equalizer.isLoaded } != true }, { 0.001.wait; });

			multiShift = IM_MultiShift.new(mixer.chanStereo(1), [7, -5, 12, -12], 0, group, \addToHead);
			while({ try { multiShift.isLoaded } != true }, { 0.001.wait; });

			delayNetwork = DelayNetwork2.newMono(mixer.chanStereo(2), 10, relGroup: group, addAction: \addToHead);
			while({ try { delayNetwork.isLoaded } != true }, { 0.001.wait; });

			splitter = Splitter.newMono(3, [equalizer.inBus, multiShift.inBus, delayNetwork.inBus], false, group, \addToHead);
			while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

			sampler = SampleGrid.new(mixer.chanStereo(3), relGroup: group, addAction: \addToHead);
			while( { try { sampler.isLoaded } != true }, { 0.001.wait; });

			subtractive = Subtractive.new(mixer.chanStereo(4), relGroup: group, addAction: \addToHead);
			while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });

			input = IM_HardwareIn.new(contactMicIn, splitter.inBus, group, \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			isLoaded = true;
		}
	}

	//////// public functions:

	free {
		input.free;
		subtractive.free;
		sampler.free;
		splitter.free;
		delayNetwork.free;
		multiShift.free;
		equalizer.free;
		this.freeModule;
	}

} 