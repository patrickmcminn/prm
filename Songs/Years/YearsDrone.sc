/*
Wednesday, March 30th 2022
YearsDrone.sc
prm
*/

Years_Drone : IM_Module {

	var server, <isLoaded;
	var <grainBuf1,  <grainBuf2;
	var <tremolo, <eq;
	var buffer1, buffer2;
	var <droneIsPlaying;

	*new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
	}

	prInit {
		var path;
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;

			path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/Years/samples/Years guitar.aif";

			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			buffer1 = Buffer.readChannel(server, path, channels: 0);
			buffer2 = Buffer.readChannel(server, path, channels: 1);

			eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
			while({ try { eq.isLoaded } != true }, { 0.001.wait; });

			tremolo = Tremolo.newStereo(eq.inBus, relGroup: group, addAction: \addToHead);
			while({ try { tremolo.isLoaded } != true }, { 0.001.wait; });

			grainBuf1 = BufferGranulator.newMono(tremolo.inBus, buffer1, relGroup: group, addAction: \addToHead);
			while({ try { grainBuf1.isLoaded } != true }, { 0.001.wait; });
			grainBuf2 = BufferGranulator.newMono(tremolo.inBus, buffer2, relGroup: group, addAction: \addToHead);
			while({ try { grainBuf2.isLoaded } != true }, { 0.001.wait; });

			this.prSetInitialParameters;

			isLoaded = true;

		}
	}

	prSetInitialParameters {
		droneIsPlaying = false;

		grainBuf1.mixer.setPreVol(-12);
		grainBuf1.mixer.setPanBal(-0.75);
		grainBuf2.mixer.setPreVol(-12);
		grainBuf2.mixer.setPanBal(0.75);

		grainBuf1.setAttackTime(5);
		grainBuf1.setReleaseTime(8);
		grainBuf2.setAttackTime(5.5);
		grainBuf2.setReleaseTime(7.5);

		tremolo.setPanLFODepth(0.2);
		tremolo.setPanLFOWaveform('noise');

		tremolo.setVolLFOFreq(9);
		tremolo.mixer.setPreVol(-12);
		eq.setLowPassCutoff(650);
		eq.setHighPassCutoff(60);
		eq.setLowFreq(110);
		eq.setLowGain(4.5);
		eq.setPeak1Freq(300);
		eq.setPeak1Gain(-3);
	}

	/////// public functions:
	free {
		grainBuf1.free; grainBuf2.free;
		tremolo.free; eq.free;
		buffer1.free; buffer2.free;
		this.freeModule;
	}

	toggleDrone { if( droneIsPlaying == false, { this.playDrone }, { this.releaseDrone }); }

	playDrone {
		grainBuf1.playNote(\yearsDrone);
		grainBuf2.playNote(\yearsDrone);
		droneIsPlaying = true;
	}

	releaseDrone {
		grainBuf1.releaseNote(\yearsDrone);
		grainBuf2.releaseNote(\yearsDrone);
		droneIsPlaying = false;
	}
}

