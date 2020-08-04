/*
Monday, November 10th 2014
adapted from IM_AudioSystem in ImpMach
by Jonah Beram and Patrick McMinn
prm

2/26/2020
moving over to Valhalla VST
*/

AudioSystem {

	var <isLoaded;
	var <procGroup, systemGroup;
	var hardwareOut, <systemMixer, <cmix;
	var <irLibrary, <splitter;

	var <sampler, <cv;

	var <reverb, <granulator, <modularSend, <delay;
	var <splitter;
	var <microphone, <micInput, <pickup, <pickupInput, <modular, modularInput,  <moog, <moogInput;
	var <modular2, <modularInput2, <modular3, <modularInput3;
	var <beauty, <beautyIn;
	var <subtractive;
	var <songBook;
	var <server;

	var <masterEQ;

	var micIn, pickupIn, moogIn, morphageneIn, modularOut;

	*new {
		|
		numOutputs = 2, micInBus, pickupInBus, moogInBus,
		morphageneInBus, plaitsInBus, noiseInBus, modularOutBus
		|
		^super.new.prInit(numOutputs,micInBus, pickupInBus, moogInBus, morphageneInBus,
			plaitsInBus, noiseInBus, modularOutBus);
	}

	prInit {
		|
		numOutputs,micInBus, pickupInBus, moogInBus, morphageneInBus,
		plaitsInBus, noiseInBus, modularOutBus
		|
		server = Server.default;

		this.prSetServerOptions(server, 64, 131072, 1024, nil);

		micIn = micInBus; pickupIn = pickupInBus; moogIn = moogInBus; morphageneIn = morphageneInBus;
		modularOut = modularOutBus;

		server.waitForBoot {
			var masterOutArray;
			//var tempTime = SystemClock.seconds;

			isLoaded = false;

			server.sync;

			server.latency = 0.05;

			hardwareOut = IM_HardwareOut(numOutputs);
			procGroup = Group(server, \addToHead);
			systemGroup = Group(procGroup, \addAfter);
			server.sync;

			// Fix input checking: a numOutputs of 1 will result 0.5 passed to Array.fill
			masterOutArray = Array.fill(numOutputs / 2, { |index| index * 2 });

			// trying to create copies for a headphone mix.
			// taking it out. no good!
			//monitorMixer = IM_MasterMixer.new([0, 1], systemGroup);
			//while({ try { monitorMixer.isLoaded } != true }, { 0.001.wait; });

			systemMixer = IM_MasterMixer.new([0, 1], systemGroup);
			// while( { try { systemMixer.inBus(0) } == nil }, { 0.001.wait });
			server.sync;
			while ( { try { systemMixer.isLoaded} != true }, { 0.001.wait } );

			splitter = Splitter.newStereo(2, [systemMixer.inBus(0), nil], false, systemGroup, \addToHead);
			while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

			masterEQ = Equalizer.newStereo(splitter.inBus, systemGroup, \addToHead);
			while({ try { masterEQ.isLoaded } != true }, { 0.001.wait; });

			irLibrary = IM_IRLibrary.new("~/Library/Application Support/SuperCollider/Extensions/prm/Effects/Reverb/ImpulseResponses");
			server.sync;
			while( { try { irLibrary.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			///////// RETURN FX:

			//

			reverb = Valhalla.newStereo(masterEQ.inBus, relGroup: systemGroup, addAction: \addToHead);
			/*reverb = IM_Reverb.newConvolution(masterEQ.inBus, bufName: irLibrary.irDict['3.0LongReverb'],
				relGroup: systemGroup, addAction: \addToHead);*/
			server.sync;
			while( { try { reverb.isLoaded } != true }, { 0.001.wait; });

			//reverb.setPreAmp(-6.dbamp);

			// pre eq:
			reverb.preEQ.setHighPassCutoff(180);
			//reverb.preEQ.setLowPassCutoff(15000);
			// post eq:
			reverb.postEQ.setLowGain(-9);
			//reverb.postEQ.setHighGain(3);
			reverb.postEQ.setLowFreq(250);
			reverb.postEQ.setPeak1Freq(496.6);
			reverb.postEQ.setPeak1RQ(3);
			reverb.postEQ.setPeak1Gain(-5);

			reverb.loadPreset('longTrumpet');

			granulator = GranularDelay.new(masterEQ.inBus, relGroup: systemGroup, addAction: \addToHead);
			server.sync;
			while( {  try { granulator.isLoaded } != true }, { 0.001.wait; });
			granulator.granulator.setCrossfade(1);
			granulator.delay.setMix(0);

			// send out to modular system
			modularSend = MonoHardwareSend.new(modularOut, relGroup: systemGroup, addAction: \addToHead);
			while({ try { modularSend.isLoaded } != true }, { 0.001.wait; });

			// delay:
			delay = SimpleDelay.newStereo(masterEQ.inBus, 1.5, 0.35, 10, relGroup: systemGroup, addAction: \addToHead);
			while({ try { delay.isLoaded } != true }, { 0.001.wait; });
			delay.setMix(1);


			/////////// DEFAULT INPUTS:

			cmix = IM_Mixer.new(8, this.audioIn,
				reverb.inBus, granulator.inBus, modularSend.inBus, delay.inBus, false, procGroup, \addToHead);
			while({ try { cmix.isLoaded } != true }, { 0.001.wait; });

			// utilities come in muted:
			cmix.mute(0); cmix.mute(1); cmix.mute(2); cmix.mute(3); cmix.mute(4); cmix.mute(5); cmix.mute(6);

			microphone = IM_Mixer_1Ch.new(cmix.chanStereo(0), relGroup: procGroup, addAction: \addToHead);
			while({ try { microphone.isLoaded } != true }, { 0.001.wait; });
			micInput = IM_HardwareIn.new(micInBus, microphone.chanMono(0), procGroup, \addToHead);
			while({ try { micInput.isLoaded } != true }, { 0.001.wait; });

			pickup = IM_Mixer_1Ch.new(cmix.chanStereo(1), relGroup: procGroup, addAction: \addToHead);
			while({ try { pickup.isLoaded } != true }, { 0.001.wait; });
			pickupInput = IM_HardwareIn.new(pickupInBus, pickup.chanMono, procGroup, \addToHead);
			while({ try { pickupInput.isLoaded } != true }, { 0.001.wait; });

			modular = IM_Mixer_1Ch.new(cmix.chanStereo(2), relGroup: procGroup, addAction: \addToHead);
			while( { try { modular.isLoaded } != true }, { 0.001.wait; });
			modularInput = IM_HardwareIn.new(morphageneInBus, modular.chanMono, procGroup, \addToHead);
			while({ try { modularInput.isLoaded } != true }, { 0.001.wait; });

			moog = IM_Mixer_1Ch.new(cmix.chanStereo(3), relGroup: procGroup, addAction: \addToHead);
			while({ try { moog.isLoaded } != true }, { 0.001.wait; });
			moogInput = IM_HardwareIn.new(moogInBus, moog.chanMono(0), procGroup, \addToHead);
			while({ try { moogInput.isLoaded } != true }, { 0.001.wait; });


			sampler = SampleGrid.new(cmix.chanStereo(6), relGroup: procGroup, addAction: \addToHead);
			while({ try { sampler.isLoaded } != true }, { 0.001.wait; });
			/*
			beauty = Beauty.newMono(cmix.chanStereo(6), relGroup: procGroup, addAction: \addToHead);
			while({ try { beauty.isLoaded } != true }, { 0.001.wait; });
			beautyIn = IM_HardwareIn.new(pickupInBus, beauty.inBus, procGroup, \addToHead);
			while({ try { beautyIn.isLoaded } != true }, { 0.001.wait; });
			*/

			modular2 = IM_HardwareIn.new(plaitsInBus, cmix.chanMono(4), procGroup, \addToHead);
			while({ try { modular2.isLoaded } != true }, { 0.001.wait; });

			modular3 = IM_HardwareIn.new(noiseInBus, cmix.chanMono(5), procGroup, \addToHead);
			while({ try { modular3.isLoaded } != true }, { 0.001.wait; });

			subtractive = Subtractive.new(cmix.chanStereo(7), relGroup: procGroup, addAction: \addToHead);
			while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });

			cv = CV_Suite.new(procGroup, \addToHead);
			while({ try { cv.isLoaded } != true }, { 0.001.wait; });


			// reverb!
			cmix.setSendVol(0, 0, -3);
			cmix.setSendVol(1, 0, -3);
			cmix.setSendVol(2, 0, -6);
			cmix.setSendVol(3, 0, -15);
			cmix.setSendVol(4, 0, -12);
			cmix.setSendVol(5, 0, -9);
			cmix.setSendVol(6, 0, -12);

			cmix.setSendVol(7, 0, -6);

			splitter.setOutBus(1, sampler.inBus);


			songBook = IdentityDictionary.new;

			isLoaded = true;

		};
	}

	// Define audio device, block size, and total memory reserved for SCLang
	// Default memory size = 2 ** 17
	prSetServerOptions { |server, blockSize = 64, memSize = 131072, numAudioBusChannels = 1024, devName|
		server.options.blockSize = blockSize;
		server.options.memSize = memSize;
		server.options.numAudioBusChannels = numAudioBusChannels;
		server.options.hardwareBufferSize = 256;
		server.options.numInputBusChannels = 44;
		server.options.numOutputBusChannels = 48;
		// comment out for verbosity:
		//server.options.verbosity = -1;
		// server.options.device = (devName);
	}

	// Still some problems here with nodes trying to be freed after their groups have been
	free {
		fork {
			systemMixer.muteAll;

			hardwareOut.free;
			reverb.free;
			granulator.free;
			modularSend.free;
			masterEQ.free;
			systemMixer.free;

			while( { systemMixer.group != nil }, { 0.001.wait } );

			systemGroup.free;
			procGroup.free;
			//songBook.free;

			hardwareOut = nil;

			modularInput = nil;
			modular = nil;

			reverb = nil;
			granulator = nil;
			modularSend = nil;

			systemMixer = nil;

			systemGroup = nil;
			procGroup = nil;
			//songBook = nil;

			// Server.default.quit;
		};
	}
}

// Convenience functions
+ AudioSystem {
	setVol { |chan = 0, db = 0, lagTime = 0| systemMixer.setVol(chan, db, lagTime) }
	mute { |chan = 0| systemMixer.mute(chan) }
	unMute { |chan = 0| systemMixer.unMute(chan) }
	tglMute { |chan = 0| systemMixer.tglMute(chan) }
	fadeOut { |chan = 0, dur = 5| systemMixer.fadeOut(chan, dur) }

	makeSong { |songName = nil| ^songBook.makeSong(songName) }
	getSong { |songName = nil| ^songBook.getSong(songName) }
	freeSong { |songName = nil| ^songBook.freeSong(songName) }

	audioIn { ^masterEQ.inBus }
}