var page = 'subtractive';

var mainGridBank = 0;
var keyboardBank = 6;
//var keyboardHoldBank = 7;

var mainMixerBank = 0;
var mainDeviceEncodersBank = 0;
var mainMixerEncodersBank = 0;
var mainSceneLaunchBank = 0;

var mainBank = 0;
var osc1Bank = 1;
var osc2Bank = 2;
var filterBank = 3;
var envBank = 4;
var lfoBank = 5;
var keyboard = 6;
var presetLoad = 7;
var presetSave = 8;

var makeMIDIFuncs, freeMIDIFuncs;
var onArray, offArray;
var midiFuncsLoaded = false;

var ccDict = Dictionary.new;
var makeCCFuncs, freeCCFuncs;

~apc.setSceneLaunchMonitorFunc(\midi, {
	if( midiFuncsLoaded == true, { ~apc.turnSceneLaunchButtonGreen(4); }, { ~apc.turnSceneLaunchButtonYellow(4) });
}, mainSceneLaunchBank, page);
~apc.setSceneLaunchFunc(4, {
	if( midiFuncsLoaded == true, { freeMIDIFuncs.value; }, { makeMIDIFuncs.value(~pyr.uid, 13); });
}, bank: mainSceneLaunchBank, page: page);


makeMIDIFuncs = { | device = nil, channel = 13 |
	onArray = Array.fill(128, { | i |
		MIDIFunc.noteOn({ | vel | ~prm.subtractive.playNote(i.midicps, vel.ccdbfs);
	}, i, channel, device); });
	offArray = Array.fill(128, { | i |
		MIDIFunc.noteOff({ ~prm.subtractive.releaseNote(i.midicps); }, i, channel, device); });
	makeCCFuncs.value(device, channel);
	midiFuncsLoaded = true;
};

freeMIDIFuncs = {
	onArray.do({ | i | i.free; });
	offArray.do({ | i | i.free; });
	freeCCFuncs.value;
	midiFuncsLoaded = false;
};

makeCCFuncs = { | device, channel |

	// volume:
	ccDict[\volume] = MIDIFunc.cc({ | val |
		~prm.subtractive.mixer.setVol(val.ccdbfs);
	}, 1, channel, device);
	// pan:
	ccDict[\pan] = MIDIFunc.cc({ | val |
		var pan = val.linlin(0, 127, -1, 1);
		~prm.subtractive.setPan(pan);
	}, 2, channel, device);


	// Basic Filter:
	ccDict[\cutoff] = MIDIFunc.cc({ | val |
		var cutoff = val.linexp(0, 127, 50, 20000);
		~prm.subtractive.setFilterCutoff(cutoff);
	}, 3, channel, device);
	// resonance:
	ccDict[\res] = MIDIFunc.cc({ | val |
		var res = val.linlin(0, 127, 0, 1.3);
		~prm.subtractive.setFilterRes(res);
	}, 4, channel, device);
	// drive:
	ccDict[\drive] = MIDIFunc.cc({ | val |
		var drive = val.linexp(0, 127, 1, 100);
		~prm.subtractive.setFilterDrive(drive);
	}, 5, channel, device);


	//////// Amplitude Envelope:
	// amplitude env attack time:
	ccDict[\attack] = MIDIFunc.cc({ | val |
		var attack = val.linexp(0, 127, 0.001, 10);
		~prm.subtractive.setAttackTime(attack);
	}, 6, channel, device);
	// amplitude env decay time:
	ccDict[\decay] = MIDIFunc.cc({ | val |
		var decay = val.linexp(0, 127, 0.001, 7);
		~prm.subtractive.setDecayTime(decay);
	}, 7, channel, device);
	// amplitude env sustain level:
	ccDict[\sustain] = MIDIFunc.cc({ | val |
		var sustain = val.linlin(0, 127, 0, 1);
		~prm.subtractive.setSustainLevel(sustain);
	}, 8, channel, device);
	// amplitude env release time:
	ccDict[\release] = MIDIFunc.cc({ | val |
		var release = val.linexp(0, 127, 0.001, 15);
		~prm.subrtractive.setReleaseTime(release);
	}, 9, channel, device);

	////// Basic LFO:
	// LFO 1 Freq:
	ccDict[\lfo1Freq] = MIDIFunc.cc({ | val |
		var freq = val.linexp(0, 127, 0.01, 55);
		~prm.subtractive.setLFO1Freq(freq);
	}, 10, channel, device);
	// LFO 1 Waveform:
	ccDict[\lfo1Waveform] = MIDIFunc.cc({ | val |
		var waveform = val.linlin(0, 127, 0, 5);
		~prm.subtractive.setLFO1Waveform(waveform);
	}, 11, channel, device);
	// LFO 2 Freq:
	ccDict[\lfo2Freq] = MIDIFunc.cc({ | val |
		var freq = val.linexp(0, 127, 0.01, 55);
		~prm.subtractive.setLFO2Freq(freq);
	}, 12, channel, device);
	// LFO 2 Waveform:
	ccDict[\lfo2Waveform] = MIDIFunc.cc({ |val |
		var waveform = val.linlin(0, 127, 0, 5);
		~prm.subtractive.setLFO2Waveform(waveform);
	}, 13, channel, device);

	//////// Oscillator 1:
	// osc 1 vol:
	ccDict[\osc1Vol] = MIDIFunc.cc({ | val |
		~prm.subtractive.setOsc1Vol(val.ccdbfs);
	}, 14, channel, device);
	// osc 1 sub vol:
	ccDict[\osc1SubVol] = MIDIFunc.cc({ | val |
		~prm.subtractive.setOsc1SubVol(val.ccdbfs);
	}, 15, channel, device);
	// osc 1 waveform:
	ccDict[\osc1Waveform] = MIDIFunc.cc({ | val |
		var waveform = val.linlin(0, 127, 0, 3);
		~prm.subtractive.setOsc1Waveform(waveform);
	}, 16, channel, device);
	ccDict[\osc1PulseWidth] = MIDIFunc.cc({ | val |
		var width = val.linlin(0, 127, 0, 1);
		~prm.subtractive.setOsc1PulseWidth(width);
	}, 17, channel, device);
	ccDict[\osc1Octave] = MIDIFunc.cc({ | val |
		var oct = val.linlin(0, 127, 0, 6).asInt;
		~prm.subtractive.setOsc1Octave(oct);
	}, 18, channel, device);

	//// osc 1 lfo:
	ccDict[\osc1WaveformLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -1.5);
		var top = val.linlin(0, 127, 0, 1.5);
		~prm.setOsc1WaveformLFO1Bottom(bottom);
		~prm.setOsc1WaveformLFO1Top(top);
	}, 19, channel, device);
	ccDict[\osc1WaveformLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -1.5);
		var top = val.linlin(0, 127, 0, 1.5);
		~prm.subtractive.setOsc1WaveformLFO2Bottom(bottom);
		~prm.subtractive.setOsc1WaveformLFO2Top(top);
	}, 20, channel, device);

	ccDict[\osc1PulseWidthLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -0.4);
		var top = val.linlin(0, 127, 0, 0.4);
		~prm.subtractive.setOsc1PulseWidthLFO1Bottom(bottom);
		~prm.subtractive.setOsc1PulseWidthLFO1Top(top);
	}, 21, channel, device);
	ccDict[\osc1PulseWidthLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -0.4);
		var top = val.linlin(0, 127, 0, 0.4);
		~prm.subtractive.setOsc1PulseWidthLFO2Bottom(bottom);
		~prm.subtractive.setOsc1PulseWidthLFO2Top(top);
	}, 22, channel, device);

	ccDict[\osc1AmpLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0);
		~prm.subtractive.setOsc1AmpLFO1Bottom(bottom);
		~prm.subtractive.setOsc1AmpLFO1Top(1);
	}, 23, channel, device);
	ccDict[\osc1AmpLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0);
		~prm.subtractive.setOsc1AmpLFO2Bottom(bottom);
		~prm.subtractive.setOsc1AmpLFO2Top(1);
	}, 24, channel, device);

	ccDict[\osc1FreqLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0.25);
		var top = val.linlin(0, 127, 1, 4);
		~prm.subtractive.setOsc1FreqLFO1BottomRatio(bottom);
		~prm.subtractive.setOsc1FreqLFO1TopRatio(top);
	}, 25, channel, device);
	ccDict[\osc1FreqLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0.25);
		var top = val.linlin(0, 127, 1, 4);
		~prm.subtractive.setOsc1FreqLFO2BottomRatio(bottom);
		~prm.subtractive.setOsc1FreqLFO2TopRatio(top);
	}, 26, channel, device);

	ccDict[\osc1FreqEnvStart] = MIDIFunc.cc({ | val |
		var start = val.linlin(0, 127, 0.25, 4);
		~prm.subtractive.setOsc1FreqEnvStartRatio(start);
	}, 27, channel, device);
	ccDict[\osc1FreqEnvEnd] = MIDIFunc.cc({ | val |
		var end = val.linlin(0, 127, 0.25, 4);
		~prm.subtractive.setOsc1FreqEnvEndRatio(end);
	}, 28, channel, device);
	ccDict[\osc1FreqEnvTime] = MIDIFunc.cc({ | val |
		var time = val.linexp(0, 127, 0.1, 30);
	}, 29, channel, device);


	//////// Oscillator 2:
	// osc 2 vol:
	ccDict[\osc2Vol] = MIDIFunc.cc({ | val |
		~prm.subtractive.setOsc2Vol(val.ccdbfs);
	}, 30, channel, device);
	// osc 2 sub vol:
	ccDict[\osc2SubVol] = MIDIFunc.cc({ | val |
		~prm.subtractive.setOsc2SubVol(val.ccdbfs);
	}, 31, channel, device);
	// osc 2 waveform:
	ccDict[\osc2Waveform] = MIDIFunc.cc({ | val |
		var waveform = val.linlin(0, 127, 0, 3);
		~prm.subtractive.setOsc2Waveform(waveform);
	}, 32, channel, device);
	ccDict[\osc2PulseWidth] = MIDIFunc.cc({ | val |
		var width = val.linlin(0, 127, 0, 1);
		~prm.subtractive.setOsc2PulseWidth(width);
	}, 33, channel, device);
	ccDict[\osc2Octave] = MIDIFunc.cc({ | val |
		var oct = val.linlin(0, 127, 0, 6).asInt;
		~prm.subtractive.setOsc2Octave(oct);
	}, 34, channel, device);

	//// osc 2 lfo:
	ccDict[\osc2WaveformLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -1.5);
		var top = val.linlin(0, 127, 0, 1.5);
		~prm.setOsc2WaveformLFO1Bottom(bottom);
		~prm.setOsc2WaveformLFO1Top(top);
	}, 35, channel, device);
	ccDict[\osc2WaveformLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -1.5);
		var top = val.linlin(0, 127, 0, 1.5);
		~prm.subtractive.setOsc2WaveformLFO2Bottom(bottom);
		~prm.subtractive.setOsc2WaveformLFO2Top(top);
	}, 36, channel, device);

	ccDict[\osc2PulseWidthLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -0.4);
		var top = val.linlin(0, 127, 0, 0.4);
		~prm.subtractive.setOsc2PulseWidthLFO1Bottom(bottom);
		~prm.subtractive.setOsc2PulseWidthLFO1Top(top);
	}, 37, channel, device);
	ccDict[\osc2PulseWidthLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -0.4);
		var top = val.linlin(0, 127, 0, 0.4);
		~prm.subtractive.setOsc2PulseWidthLFO2Bottom(bottom);
		~prm.subtractive.setOsc2PulseWidthLFO2Top(top);
	}, 38, channel, device);

	ccDict[\osc2AmpLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0);
		~prm.subtractive.setOsc2AmpLFO1Bottom(bottom);
		~prm.subtractive.setOsc2AmpLFO1Top(1);
	}, 39, channel, device);
	ccDict[\osc2AmpLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0);
		~prm.subtractive.setOsc2AmpLFO2Bottom(bottom);
		~prm.subtractive.setOsc2AmpLFO2Top(1);
	}, 40, channel, device);

	ccDict[\osc2FreqLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0.25);
		var top = val.linlin(0, 127, 1, 4);
		~prm.subtractive.setOsc2FreqLFO1BottomRatio(bottom);
		~prm.subtractive.setOsc2FreqLFO1TopRatio(top);
	}, 41, channel, device);
	ccDict[\osc2FreqLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0.25);
		var top = val.linlin(0, 127, 1, 4);
		~prm.subtractive.setOsc2FreqLFO2BottomRatio(bottom);
		~prm.subtractive.setOsc2FreqLFO2TopRatio(top);
	}, 42, channel, device);

	ccDict[\osc2FreqEnvStart] = MIDIFunc.cc({ | val |
		var start = val.linlin(0, 127, 0.25, 4);
		~prm.subtractive.setOsc2FreqEnvStartRatio(start);
	}, 43, channel, device);
	ccDict[\osc2FreqEnvEnd] = MIDIFunc.cc({ | val |
		var end = val.linlin(0, 127, 0.25, 4);
		~prm.subtractive.setOsc2FreqEnvEndRatio(end);
	}, 44, channel, device);
	ccDict[\osc2FreqEnvTime] = MIDIFunc.cc({ | val |
		var time = val.linexp(0, 127, 0.1, 30);
	}, 45, channel, device);

	ccDict[\osc2Detune] = MIDIFunc.cc({ | val |
		var detune = val.linlin(0, 127, -50, 50);
		~prm.subtractive.setOsc2DetuneCents(detune);
	}, 46, channel, device);

	////// noise oscillator lfo:
	ccDict[\noiseOscVol] = MIDIFunc.cc({ | val |
		~prm.subtractive.setNoiseOscVol(val.ccdbfs);
	}, 47, channel, device);
	ccDict[\noiseOscCutoff] = MIDIFunc.cc({ | val |
		var cutoff = val.linexp(0, 127, 50, 20000);
		~prm.subtractive.setNoiseOscCutoff(cutoff);
	}, 48, channel, device);

	ccDict[\noiseOscAmpLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0);
		~prm.subtractive.setNoiseOscAmpLFO1Bottom(bottom);
	}, 49, channel, device);
	ccDict[\noiseOscAmpLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0);
		~prm.subtractive.setNoiseOscAmpLFO2Bottom(bottom);
	}, 50, channel, device);

	ccDict[\noiseOscCutoffLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linexp(0, 127, 1, 0.25);
		var top = val.linexp(0, 127, 1, 4);
		~prm.subtractive.setNoiseOscFilterLFO1BottomRatio(bottom);
		~prm.subtractive.setNoiseOscFilterLFO1TopRatio(top);
	}, 51, channel, device);
	ccDict[\noiseOscCutoffLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linexp(0, 127, 1, 0.25);
		var top = val.linexp(0, 127, 1, 4);
		~prm.subtractive.setNoiseOscFilterLFO2BottomRatio(bottom);
		~prm.subtractive.setNoiseOscFilterLFO2TopRatio(top);
	}, 52, channel, device);

	//////// Filter:

	// Filter Type:
	ccDict[\filterType] = MIDIFunc.cc({ | val |
		var type = val.linlin(0, 127, 0, 2).asInt;
		~prm.subtractive.setFilterType(type);
	}, 53, channel, device);

	ccDict[\filterCutoffLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0.25);
		var top = val.linlin(0, 127, 1, 4);
		~prm.subtractive.setFilterCutoffLFO1BottomRatio(bottom);
		~prm.subtractive.setFilterCutoffLFO1TopRatio(top);
	}, 54, channel, device);
	ccDict[\filterCutoffLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0.25);
		var top = val.linlin(0, 127, 1, 4);
		~prm.subtractive.setFilterCutoffLFO2BottomRatio(bottom);
		~prm.subtractive.setFilterCutoffLFO2TopRatio(top);
	}, 55, channel, device);

	ccDict[\filterResLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -1);
		var top = val.linlin(0, 127, 0, 1);
		~prm.subtractive.setFilterResLFO1Bottom(bottom);
		~prm.subtractive.setFilterResLFO1Top(top);
	}, 56, channel, device);
	ccDict[\filterResLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -1);
		var top = val.linlin(0, 127, 0, 1);
		~prm.subtractive.setFilterResLFO2Bottom(bottom);
		~prm.subtractive.setFilterResLFO2Top(top);
	}, 57, channel, device);

	ccDict[\filterEnvAttackTime] = MIDIFunc.cc({ | val |
		var time = val.linexp(0, 127, 0.001, 7);
		~prm.subtractive.setFilterEnvAttackTime(time);
	}, 58, channel, device);
	ccDict[\filterEnvAttackRatio] = MIDIFunc.cc({ | val |
		var ratio = val.linlin(0, 127, 0.25, 4);
		~prm.subtractive.setFilterEnvAttackRatio(ratio);
	}, 59, channel, device);
	ccDict[\filterEnvDecayTime] = MIDIFunc.cc({ | val |
		var time = val.linexp(0, 127, 0.001, 7);
		~prm.subtractive.setFilterEnvDecayTime(time);
	}, 60, channel, device);
	ccDict[\filterEnvPeakRatio] = MIDIFunc.cc({ | val |
		var ratio = val.linlin(0, 127, 0.25, 4);
		~prm.subtractive.setFilterEnvPeakRatio(ratio);
	}, 61, channel, device);
	ccDict[\filterEnvSustainRatio] = MIDIFunc.cc({ | val |
		var ratio = val.linlin(0, 127, 0.25, 4);
		~prm.subtractive.setFilterEnvSustainRatio(ratio);
	}, 62, channel, device);
	ccDict[\filterEnvReleaseTime] = MIDIFunc.cc({ | val |
		var time = val.linexp(0.001, 7);
		~prm.subtractive.setFilterEnvReleaseTime(time);
	}, 63, channel, device);
	ccDict[\filterEnvReleaseRatio] = MIDIFunc.cc({ | val |
		var ratio = val.linlin(0, 127, 0.25, 4);
		~prm.subtractive.setFilterEnvReleaseRatio(ratio);
	}, 64, channel, device);

	//////// Envelope:

	ccDict[\ampLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0);
		~prm.subtractive.setAmplitudeLFO1Bottom(bottom);
	}, 65, channel, device);
	ccDict[\ampLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0);
		~prm.subtractive.setAmplitudeLFO2Bottom(bottom);
	}, 66, channel, device);

	ccDict[\panLFO1] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -1);
		var top = val.linlin(0, 127, 0, 1 );
		~prm.subtractive.setPanLFO1Bottom(bottom);
		~prm.subtractive.setPanLFO1Top(top);
	}, 67, channel, device);
	ccDict[\panLFO2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 0, -1);
		var top = val.linlin(0, 127, 0, 1 );
		~prm.subtractive.setPanLFO2Bottom(bottom);
		~prm.subtractive.setPanLFO2Top(top);
	}, 68, channel, device);

	//////// other LFO stuff:

	// LFO 1 Pulse Width:
	ccDict[\lfo1PulseWidth] = MIDIFunc.cc({ | val |
		var width = val.linlin(0, 127, 0, 1);
		~prm.subtractive.setLFO1PulseWidth(width);
	}, 69, channel, device);
	ccDict[\lfo1EnvType] = MIDIFunc.cc({ |val |
		var type = val.linlin(0, 127, 0, 3).asInt;
		~prm.subtractive.setLFO1EnvType(type);
	}, 70, channel, device);
	ccDict[\lfo1AttackTime] = MIDIFunc.cc({ | val |
		var time = val.linexp(0, 127, 0, 7);
		~prm.subtractive.setLFO1AttackTime(time);
	}, 71, channel, device);
	ccDict[\lfo1ReleaseTime] = MIDIFunc.cc({ | val |
		var time = val.linexp(0, 127, 0, 7);
		~prm.subtractive.setLFO1ReleaseTime(time);
	}, 72, channel, device);
	ccDict[\lfo1lfo2] = MIDIFunc.cc({ | val |
		var bottom = val.linlin(0, 127, 1, 0.25);
		var top = val.linlin(0, 127, 1, 4);
	}, 73, channel, device);
	// LFO 2 Pulse Width:
	ccDict[\lfo2PulseWidth] = MIDIFunc.cc({ | val |
		var width = val.linlin(0, 127, 0, 1);
		~prm.subtractive.setLFO2PulseWidth(width);
	}, 74, channel, device);

};

freeCCFuncs = { ccDict.do({ |i | i.free; }); };