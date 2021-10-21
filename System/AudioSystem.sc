/*
Monday, November 10th 2014
adapted from IM_AudioSystem in ImpMach
by Jonah Beram and Patrick McMinn
prm

2/26/2020
moving over to Valhalla VST

8/4/2020
cleaning up
making cMixer a bit more generic
*/

AudioSystem {

	var <isLoaded;
	var <procGroup, systemGroup;
	var hardwareOut, <systemMixer, <cmix;
	var <irLibrary, <splitter;

	var modIn1, modIn2, modIn3, modIn4;
	var modOut1;

	var <reverb, <granulator, <modularSend, <delay;
	var <splitter;
	var <beauty, <beautyIn;
	var <songBook;
	var <server;

	var <masterEQ;

	var <microphone, <pickup;
	var <mod1, <mod2, <mod3, <mod4;
	var <sampler, <subtractive, <cv;

	var <three, <four, <fiveAndSix, <sevenAndEight;

	*new {
		| numOutputs = 2, micInput, pickupInput, modInArray, modOutArray |
		^super.new.prInit(numOutputs = 2, micInput, pickupInput, modInArray, modOutArray);
	}

	prInit {
		| numOutputs = 2, micInput, pickupInput, modInArray, modOutArray |

		server = Server.default;

		this.prSetServerOptions(server, 64, 131072, 1024, nil);

		modIn1 = modInArray[0];
		modIn2 = modInArray[1];
		modIn3 = modInArray[2];
		modIn4 = modInArray[3];

		modOut1 = modOutArray[0];

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

			reverb.loadPreset('prmDefault');

			//granulator = GranularDelay.new(masterEQ.inBus, relGroup: systemGroup, addAction: \addToHead);
			granulator = GranularDelay2.new(masterEQ.inBus, reverb.inBus, nil, nil, nil,
				relGroup: systemGroup, addAction: \addToHead);
			server.sync;
			while( {  try { granulator.isLoaded } != true }, { 0.001.wait; });
			granulator.setPosMod(0.5);
			granulator.setDelayTime(1);
			granulator.setDelayLevel(0.75);
			granulator.setFeedback(0.3);
			granulator.mixer.setSendVol(0, -12);

			// send out to modular system
			modularSend = MonoHardwareSend.new(modOut1, relGroup: systemGroup, addAction: \addToHead);
      modularSend.setPreVol(9);
			while({ try { modularSend.isLoaded } != true }, { 0.001.wait; });

			// delay:
			delay = SimpleDelay.newStereo(masterEQ.inBus, 1.5, 0.35, 10,
				send0Bus: reverb.inBus, send1Bus: granulator.inBus, send2Bus: modularSend.inBus,
				relGroup: systemGroup, addAction: \addToHead);
			while({ try { delay.isLoaded } != true }, { 0.001.wait; });
			delay.setMix(1);


			/////////// DEFAULT INPUTS:

			cmix = IM_Mixer.new(12, this.audioIn,
				reverb.inBus, granulator.inBus, modularSend.inBus, delay.inBus, false, procGroup, \addToHead);
			while({ try { cmix.isLoaded } != true }, { 0.001.wait; });

			// utilities come in muted:
			12.do({ | chan | cmix.mute(chan); });

			microphone = IM_HardwareIn.new(micInput, cmix.chanMono(0), procGroup, \addToHead);
			while({ try { microphone.isLoaded } != true }, { 0.001.wait; });

			pickup = IM_HardwareIn.new(pickupInput, cmix.chanMono(1), procGroup, \addToHead);
			while({ try { pickup.isLoaded } != true }, { 0.001.wait; });

			mod1 = IM_HardwareIn.new(modIn1, cmix.chanMono(2), procGroup, \addToHead);
			while({ try { mod1.isLoaded } != true }, { 0.001.wait; });

			mod2 = IM_HardwareIn.new(modIn2, cmix.chanMono(3), procGroup, \addToHead);
			while({ try { mod2.isLoaded } != true }, { 0.001.wait; });

			mod3 = IM_HardwareIn.new(modIn3, cmix.chanMono(4), procGroup, \addToHead);
			while({ try { mod3.isLoaded } != true }, { 0.001.wait; });

			mod4 = IM_HardwareIn.new(modIn4, cmix.chanMono(5), procGroup, \addToHead);
			while({ try { mod4.isLoaded } != true }, { 0.001.wait; });

			sampler = SampleGrid.new(cmix.chanStereo(6), relGroup: procGroup, addAction: \addToHead);
			while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

			subtractive = Subtractive.new(cmix.chanStereo(7), relGroup: procGroup, addAction: \addToHead);
			while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });

			three = IM_HardwareIn.new(2, cmix.chanMono(8), procGroup, \addToHead);
			while({ try { three.isLoaded } != true }, { 0.001.wait; });

			four = IM_HardwareIn.new(3, cmix.chanMono(9), procGroup, \addToHead);
			while({ try { four.isLoaded } != true }, { 0.001.wait; });

			fiveAndSix = IM_HardwareIn.newStereo(4, cmix.chanStereo(10), procGroup, \addToHead);
			while({ try { fiveAndSix.isLoaded } != true }, { 0.001.wait; });

			sevenAndEight = IM_HardwareIn.newStereo(6, cmix.chanStereo(11), procGroup, \addToHead);
			while({ try { sevenAndEight.isLoaded } != true }, { 0.001.wait; });

			cv = CV_Suite.new(~prm.procGroup, \addToHead);
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
			procGroup.deepFreeMsg;
			procGroup.free;
			//songBook.free;

			hardwareOut = nil;

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