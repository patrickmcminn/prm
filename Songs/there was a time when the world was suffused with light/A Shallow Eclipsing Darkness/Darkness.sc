/*
Thursday, April 12th 2018
Darkness.sc
A Shallow Eclipsing Darkness

eschewing the Song class for now, to get away from using
submixes

UPDATED
4/22/2020
pandemic times
*/

Darkness : IM_Module {

	var <isLoaded;
	var server;

	var <introSample, <bass, <trumpet, <trumpetInput, <drones, <spuds, <noise;

	var <basslineIsPlaying, <introIsPlaying;

	var <drone1IsPlaying, <drone2IsPlaying, <drone3IsPlaying, <drone4IsPlaying;
	var <drone5IsPlaying, <drone6IsPlaying, <drone7IsPlaying, <drone8IsPlaying;
	var <drone9IsPlaying, <drone10IsPlaying;

	var spudsOnArray, spudsOffArray, introVol, moogVol, trumpetVol, droneVol, spudsVol, noiseVol, highPass;
	var <midiFuncsLoaded = false;

	*new {
		|
		outBus, send0Bus, send1Bus, send2Bus, send3Bus,
		pickupInBus, modularOutBus, moogInBus, modularInBus, noiseInBus, moogDeviceName, moogPortName,
		relGroup, addAction = 'addToHead'
		|
		^super.new(6, outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup: relGroup, addAction: addAction).prInit(
			pickupInBus, modularOutBus, moogInBus, modularInBus, noiseInBus, moogDeviceName, moogPortName);
	}

	prInit { | pickupInBus, modularOutBus, moogInBus, modularInBus, noiseInBus, moogDeviceName, moogPortName |
		server = Server.default;
		server.waitForBoot {
			var path = "~/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/A Shallow Eclipsing Darkness/samples/introLoop.wav".standardizePath;

			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			mixer.mute(2);
			mixer.mute(5);

			// 1:
			introSample = SamplePlayer.newStereo(mixer.chanStereo(0), path, relGroup: group, addAction: \addToHead);
			while({ try { introSample.isLoaded } != true }, { 0.001.wait; });

			// 2:
			bass = Darkness_Bass.new(moogInBus, mixer.chanStereo(1), moogDeviceName, moogPortName, group, \addToHead);
			while({ try { bass.isLoaded } != true }, { 0.001.wait; });

			// 3:
			trumpet = Darkness_Trumpet.new(mixer.chanStereo(2), modularOutBus, modularInBus, group, \addToHead);
			while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });
			trumpetInput = IM_HardwareIn.new(pickupInBus, trumpet.inBus, group, \addToHead);
			while({ try { trumpetInput.isLoaded } != true }, { 0.001.wait; });

			// 4:
			drones = Darkness_Drone.new(mixer.chanStereo(3), group, \addToHead);
			while({ try { drones.isLoaded } != true }, { 0.001.wait; });

			// 5:
			spuds = Darkness_Spuds.new(mixer.chanStereo(4), group, \addToHead);
			while({ try { spuds.isLoaded } != true }, { 0.001.wait; });


			noise = Darkness_Noise.new(mixer.chanStereo(5), noiseInBus, group, \addToHead);
			while({ try { noise.isLoaded } != true }, { 0.001.wait; });
			/*
			noise = IM_HardwareIn.new(noiseInBus, mixer.chanStereo(5), group, \addToHead);
			while({ try { noise.isLoaded } != true }, { 0.001.wait; });
			*/

			server.sync;

			this.prSetInitialMixerLevels;
			this.prSetInitialParameters;

			isLoaded = true;
		}
	}

	prSetInitialMixerLevels {
		// Intro Sample:
		//introSample.mixer.setPreVol(12);
		//introSample.mixer.setVol(-3);
		mixer.setPreVol(0, 9);
		mixer.setSendVol(0, 0, -6);
		mixer.setSendVol(0, 2, -24);
		mixer.setSendVol(0, 3, 0);


		// Bass:
		mixer.setPreVol(1, 3);
		mixer.setSendVol(1, 0, -9);
		//mixer.setSendVol(1, 2, -24);
		//mixer.setSendVol(1, 3, 0);


		// Trumpet:
		mixer.setPreVol(2, 3);
		//trumpet.mixer.setVol(0, -9);
		trumpet.mixer.setVol(1, -9);
		mixer.setSendVol(2, 0, -12);
		//mixer.setSendVol(2, 2, -8);
		//mixer.setSendVol(2, 3, 0);

		// Drones:
		//drones.mixer.setVol(-3);
		mixer.setPreVol(3, -3);
		mixer.setSendVol(3, 0, 0);
		//mixer.setSendVol(3, 2, -30);
		//mixer.setSendVol(3, 3, 0);

		// spuds:
		mixer.setPreVol(4, -3);
		mixer.setSendVol(4, 0, -9);

		// noise:
		mixer.setPreVol(5, -9);
		mixer.setVol(5, -18);
		mixer.setSendVol(5, 0, -6);

	}

	prSetInitialParameters {

		introIsPlaying = false;
		basslineIsPlaying = false;

		this.setBasslineTempo(100);

		drone1IsPlaying = false; drone2IsPlaying = false; drone3IsPlaying = false;
		drone4IsPlaying = false; drone5IsPlaying = false; drone6IsPlaying = false;
		drone7IsPlaying = false; drone8IsPlaying = false;
		drone9IsPlaying = false; drone10IsPlaying = false;


		introSample.setAttackTime(10);
		introSample.setReleaseTime(10);

	}

	//////// public functions:
	free {
		introIsPlaying = false;
		basslineIsPlaying = false;
		drones.free;
		trumpet.free;
		trumpetInput.free;
		bass.free;
		introSample.free;
		spuds.free;
		noise.free;
		this.freeMIDIFuncs;
		this.freeModule;
		isLoaded = false;
	}

	setReverb { | reverbInstance | reverbInstance.loadPreset('darkness'); }

	makeMIDIFuncs { | sequencer |
		var seq = sequencer.uid;
		var spudsChan = 13;
		var volChan = 6;
		spudsOnArray = Array.fill(128, { | i |
			MIDIFunc.noteOn({ | vel | spuds.synth.playNote(i.midicps, vel.ccdbfs); }, i, spudsChan, seq); });
		spudsOffArray = Array.fill(128, { | i |
			MIDIFunc.noteOff({ spuds.synth.releaseNote(i.midicps); }, i, spudsChan, seq); });
		midiFuncsLoaded = true;
		introVol = MIDIFunc.cc({ | val | introSample.mixer.setVol(val.ccdbfs) }, 2, volChan, seq);
		moogVol = MIDIFunc.cc({ | val | bass.mixer.setVol(val.ccdbfs) }, 3, volChan, seq);
		droneVol = MIDIFunc.cc({ | val | drones.mixer.setVol(val.ccdbfs) }, 4, volChan, seq);
		spudsVol = MIDIFunc.cc({ | val | spuds.mixer.setVol(val.ccdbfs) }, 5, volChan, seq);
		noiseVol = MIDIFunc.cc({ | val | noise.mixer.setVol(val.ccdbfs) }, 6, volChan, seq);
	}

	freeMIDIFuncs {
		spudsOnArray.do({ | i | i.free; });
		spudsOffArray.do({ | i | i.free; });
		introVol.free; moogVol.free; droneVol.free; spudsVol.free; noiseVol.free;
		midiFuncsLoaded = false;
	}

	playBassline {
		bass.moog.playSequence(\darkness);
		basslineIsPlaying = true;
	}
	stopBassline {
		bass.moog.stopSequence(\darkness);
		basslineIsPlaying = false;
	}
	tglBassline {
		if( basslineIsPlaying == false, { this.playBassline }, { this.stopBassline });
	}

	setBasslineTempo { | tempo = 100 |
		bass.moog.setSequencerClockTempo(tempo);
	}

	playIntro {
		introSample.playSampleSustaining('intro');
		introIsPlaying = true;
	}
	stopIntro {
		introSample.releaseSampleSustaining('intro');
		introIsPlaying = false;
	}
	tglIntro {
		if(introIsPlaying == false, { this.playIntro }, { this.stopIntro });
	}

	playDrone1 {
		drones.synth.playSequence(\cSharp);
		drone1IsPlaying = true;
	}
	stopDrone1 {
		drones.synth.stopSequence(\cSharp);
		drone1IsPlaying = false;
	}
	tglDrone1 {
		if(drone1IsPlaying == false, { this.playDrone1 }, { this.stopDrone1 });
	}

	playDrone2 {
		drones.synth.playSequence(\dSharp);
		drone2IsPlaying = true;
	}
	stopDrone2 {
		drones.synth.stopSequence(\dSharp);
		drone2IsPlaying = false;
	}
	tglDrone2 {
		if(drone2IsPlaying == false, { this.playDrone2 }, { this.stopDrone2 });
	}

	playDrone3 {
		drones.synth.playSequence(\E);
		drone3IsPlaying = true;
	}
	stopDrone3 {
		drones.synth.stopSequence(\E);
		drone3IsPlaying = false;
	}
	tglDrone3 {
		if(drone3IsPlaying == false, { this.playDrone3 }, { this.stopDrone3 });
	}

	playDrone4 {
		drones.synth.playSequence(\fSharp);
		drone4IsPlaying = true;
	}
	stopDrone4 {
		drones.synth.stopSequence(\fSharp);
		drone4IsPlaying = false;
	}
	tglDrone4 {
		if(drone4IsPlaying == false, { this.playDrone4 }, { this.stopDrone4 });
	}

	playDrone5 {
		drones.synth.playSequence(\gSharp);
		drone5IsPlaying = true;
	}
	stopDrone5 {
		drones.synth.stopSequence(\gSharp);
		drone5IsPlaying = false;
	}
	tglDrone5 {
		if(drone5IsPlaying == false, { this.playDrone5 }, { this.stopDrone5 });
	}

	playDrone6 {
		drones.synth.playSequence(\A);
		drone6IsPlaying = true;
	}
	stopDrone6 {
		drones.synth.stopSequence(\A);
		drone6IsPlaying = false;
	}
	tglDrone6 {
		if(drone6IsPlaying == false, { this.playDrone6 }, { this.stopDrone6 });
	}

	playDrone7 {
		drones.synth.playSequence(\B);
		drone7IsPlaying = true;
	}
	stopDrone7 {
		drones.synth.stopSequence(\B);
		drone7IsPlaying = false;
	}
	tglDrone7 {
		if(drone7IsPlaying == false, { this.playDrone7 }, { this.stopDrone7 });
	}

	playDrone8 {
		drones.synth.playSequence(\cSharp2);
		drone8IsPlaying = true;
	}
	stopDrone8 {
		drones.synth.stopSequence(\cSharp2);
		drone8IsPlaying = false;
	}
	tglDrone8 {
		if(drone8IsPlaying == false, { this.playDrone8 }, { this.stopDrone8 });
	}

	playDrone9 {
		drones.synth.playSequence(\cSharp);
		drone9IsPlaying = true;
	}
	stopDrone9 {
		drones.synth.stopSequence(\cSharp);
		drone9IsPlaying = false;
	}
	tglDrone9 {
		if(drone9IsPlaying == false, { this.playDrone9 }, { this.stopDrone9 });
	}

	playDrone10 {
		drones.synth.playSequence(\cSharp0);
		drone10IsPlaying = true;
	}
	stopDrone10 {
		drones.synth.stopSequence(\cSharp0);
		drone10IsPlaying = false;
	}
	tglDrone10 {
		if(drone10IsPlaying == false, { this.playDrone10 }, { this.stopDrone10 });
	}

}