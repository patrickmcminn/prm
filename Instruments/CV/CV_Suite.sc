/*
Tuesday, July 28th
CV_Suite.sc
prm

a buncha CV stuff!
*/

CV_Suite {

	var server;

	var <isLoaded;

	var <lfo1, <lfo2, <lfo3, <lfo4;
	var <trigEnv1, <trigEnv2, <gateEnv1, <gateEnv2;
	var <gate1, <gate2, <constant1, <constant2;
	var <freq1, <freq2, <pitchGate1, <pitchGate2;

	var <nilBus;
	var <group;

	*new {
		| relGroup = nil, addAction = 'addToHead' |
		^super.new.prInit(relGroup, addAction);
	}

	prInit { | relGroup, addAction |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;

			nilBus = Bus.audio;

			server.sync;

			lfo1 = CV_LFO.new(nilBus, 1, 'sine', -0.5, 0.5, group, \addToHead);
			while({ try { lfo1.isLoaded } != true }, { 0.001.wait; });

			lfo2 = CV_LFO.new(nilBus, 1, 'saw', -0.5, 0.5, group, \addToHead);
			while({ try { lfo2.isLoaded } != true }, { 0.001.wait; });

			lfo3 = CV_LFO.new(nilBus, 1, 'sampleAndHold', -0.5, 0.5, group, \addToHead);
			while({ try { lfo3.isLoaded } != true }, { 0.001.wait; });

			lfo4 = CV_LFO.new(nilBus, 1, 'noise', -0.5, 0.5, group, \addToHead);
			while({ try { lfo4.isLoaded } != true }, { 0.001.wait; });

			trigEnv1 = CV_EnvPerc.new(nilBus, group, \addToHead);
			while( { try { trigEnv1.isLoaded } != true }, { 0.001.wait; });

			trigEnv2 = CV_EnvPerc.new(nilBus, group, \addToHead);
			while( { try { trigEnv2.isLoaded } != true }, { 0.001.wait; });

			gateEnv1 = CV_EnvADSR.new(nilBus, group, \addToHead);
			while( { try { gateEnv1.isLoaded } != true }, { 0.001.wait; });

			gateEnv2 = CV_EnvADSR.new(nilBus, group, \addToHead);
			while( { try { gateEnv2.isLoaded } != true }, { 0.001.wait; });

			gate1 = CV_Gate.new(nilBus, group, \addToHead);
			while( { try { gate1.isLoaded } != true }, { 0.001.wait; });

			gate2 = CV_Gate.new(nilBus, group, \addToHead);
			while( { try { gate2.isLoaded } != true }, { 0.001.wait; });

			constant1 = CV_Constant.new(nilBus, 0, group, \addToHead);
			while( { try { constant1.isLoaded } != true},  { 0.001.wait; });

			constant2 = CV_Constant.new(nilBus, 0, group, \addToHead);
			while( { try { constant2.isLoaded } != true},  { 0.001.wait; });

			freq1 = CV_Freq.new(nilBus, 0, group, \addToHead);
			while( { try { freq1.isLoaded } != true }, { 0.001.wait; });

			freq2 = CV_Freq.new(nilBus, 0, group, \addToHead);
			while( { try { freq2.isLoaded } != true }, { 0.001.wait; });

			pitchGate1 = CV_PitchGate.new(nilBus, nilBus, group, \addToHead);
			while({ try { pitchGate1.isLoaded } != true }, { 0.001.wait; });

			pitchGate2 = CV_PitchGate.new(nilBus, nilBus, group, \addToHead);
			while({ try { pitchGate2.isLoaded } != true }, { 0.001.wait; });

			isLoaded = true;
		};
	}

	//////// public functions:

	free {
		lfo1.free; lfo2.free; lfo3.free; lfo4.free;
		trigEnv1; trigEnv2; gateEnv1; gateEnv2;
		gate1; gate2; constant1; constant2;
		freq1; freq2; pitchGate1; pitchGate2;
	}

} 