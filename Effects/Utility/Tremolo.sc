/*
Tuesday, September 18th 2018
Tremolo.sc
prm
*/

Tremolo : IM_Module {

	var <isLoaded;
	var server;

	var input, <volLFO, <panLFO;

	var <volLFOFreq, <volLFODepth, <volLFOWaveform, <volLFOPulseWidth;
	var <panLFOFreq, <panLFODepth, <panLFOWaveform, <panLFOPulseWidth;

	var <monoOrStereo;

	*newMono {
		|
		outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead'
		|
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitMono;
	}

	*newStereo { | outBus = 0, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
		^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo;
	}

	prInitMono {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			monoOrStereo = 'mono';

			this.prSetInitialParameters;

			input = IM_Mixer_1Ch.new(mixer.chanStereo, relGroup: group, addAction: \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			volLFO = LFO.new(volLFOFreq, volLFOWaveform, (1-volLFODepth), 1, group, \addToHead);
			while({ try { volLFO.isLoaded } != true }, { 0.001.wait; });
			panLFO = LFO.new(panLFOFreq, panLFOWaveform, (0-panLFODepth), panLFODepth, group, \addToHead);
			while({ try { panLFO.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			input.mapAmp(volLFO.outBus);
			input.mapPan(panLFO.outBus);

			isLoaded = true;
		}
	}

	prInitStereo {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			monoOrStereo = 'stereo';

			this.prSetInitialParameters;

			input = IM_Mixer_1Ch.new(mixer.chanStereo, relGroup: group, addAction: \addToHead);
			while({ try { input.isLoaded } != true }, { 0.001.wait; });

			volLFO = LFO.new(volLFOFreq, volLFOWaveform, (1-volLFODepth), 1, group, \addToHead);
			while({ try { volLFO.isLoaded } != true }, { 0.001.wait; });
			panLFO = LFO.new(panLFOFreq, panLFOWaveform, (0-panLFODepth), panLFODepth, group, \addToHead);
			while({ try { panLFO.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			input.mapAmp(volLFO.outBus);
			input.mapPan(panLFO.outBus);

			isLoaded = true;
		}
	}


	prSetInitialParameters {
		volLFOFreq = 1; volLFODepth = 1; volLFOWaveform = 'sine'; volLFOPulseWidth = 0.5;
		panLFOFreq = 1; panLFODepth =  0; panLFOWaveform = 'sine'; panLFOPulseWidth = 0.5;
	}

	//////// public functions:

	free {
		volLFO.free;
		panLFO.free;
		input.free;
		this.freeModule;
	}

	inBus { if( monoOrStereo == 'mono',
		{ ^input.chanMono }, { ^input.chanStereo });
	}

	setVolLFOFreq { | freq = 1 |
		volLFOFreq = freq;
		volLFO.setFrequency(volLFOFreq);
	}
	setVolLFOWaveform { | waveform = 'sine' |
		volLFOWaveform = waveform;
		volLFO.setWaveform(volLFOWaveform);
	}
	setVolLFOPulseWidth { | width = 0.5 |
		volLFOPulseWidth = width;
		volLFO.setPulseWidth(volLFOPulseWidth);
	}
	setVolLFODepth { | depth = 1 |
		volLFODepth = depth;
		volLFO.setRangeLow(1-depth);
	}

	setPanLFOFreq { | freq = 1 |
		panLFOFreq = freq;
		panLFO.setFrequency(panLFOFreq);
	}
	setPanLFOWaveform { | waveform = 'sine' |
		panLFOWaveform = waveform;
		panLFO.setWaveform(panLFOWaveform);
	}
	setPanLFOPulseWidth { | width = 0.5 |
		panLFOPulseWidth = width;
		panLFO.setPulseWidth(panLFOPulseWidth);
	}
	setPanLFODepth { | depth = 0 |
		panLFODepth = depth;
		panLFO.setRangeLow(0-depth);
		panLFO.setRangeHigh(depth);
	}


}