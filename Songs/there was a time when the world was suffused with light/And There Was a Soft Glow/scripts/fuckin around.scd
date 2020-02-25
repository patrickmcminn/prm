/*
February 20th 2020

sketches for a performance system for
And There Was a Warm Glow

prm
*/
(
/*
0 - sampler
1 - synth 1
2 - synth 2
3 - moog 1
4 - moog 2
5 - subtractive bass
6 - watershift 1
7 - watershift 2
*/

MIDIIn.connectAll;
(
~mixer = IM_Mixer.new(8, ~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus,
	~prm.modularSend.inBus, ~prm.delay.inBus,
	relGroup: ~prm.procGroup, addAction: \addToHead);
);


~samplerReturn = IM_HardwareIn.newStereo(34, ~mixer.chanStereo(0), ~prm.procGroup, \addToHead);

/////// Creation:

(
// 0:
var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/And There Was a Soft Glow/Samples/gurgler/";
~sampleArray = (path ++ "*").pathMatch;


~sampler = Sampler.newStereo(~modOutput[0], ~sampleArray, relGroup: ~prm.procGroup, addAction: \addToHead);


// 1:
~sub1 = Subtractive.new(~mixer.chanStereo(1), relGroup: ~prm.procGroup, addAction: \addToHead);

// 2:
~sub2 = Subtractive.new(~mixer.chanStereo(2), relGroup: ~prm.procGroup, addAction: \addToHead);

// 3:
~moog1 = Mother32.new(~modInput[0], ~mixer.chanStereo(3), ~moogDevice, ~moogPort,
	relGroup: ~prm.procGroup, addAction: \addToHead);
/*
// 4:
// (distorted moog)
~verb = IM_Reverb.new(~mixer.chanStereo(4), damp: 0.8, relGroup: ~prm.procGroup, addAction: \addToHead);
~dist = Distortion2.newMono(~verb.inBus, 0.5, 0.5, relGroup: ~prm.procGroup, addAction: \addToHead);
~moog2 = IM_HardwareIn(~modInput[0], ~dist.inBus, ~prm.procGroup, \addToHead);
*/

// 5:
// (bass subtractive) should be maybe a little growly and FM-y? Maybe?
~subBass = Subtractive.new(~mixer.chanStereo(5), relGroup: ~prm.procGroup, addAction: \addToHead);
);

////// Settings:

(

//// Mixer:
~mixer.setSendVol(0, 0, -inf);

~mixer.setVol(0, 0);
~mixer.setPreVol(0, 3);
~mixer.setVol(3, -6);
~mixer.setVol(5, -3);


~mixer.setVol(1, -24);
~mixer.setPanBal(0, -0.25);
~mixer.setSendVol(1, 0, -6);
~mixer.setSendVol(1, 1, -9);


~mixer.setVol(2, -15);
~mixer.setSendVol(2, 0, -6);
~mixer.setSendVol(2, 1, -6);
~mixer.setPanBal(2, 0.5);

~subBass.mixer.setPreVol(6);


//// sub 1:
~sub1.setReleaseTime(0.3);
~sub1.setDecayTime(0.4);
~sub1.setAttackTime(0.45);
~sub1.setSustainLevel(0.3);
~sub1.setFilterDrive(10);
~sub1.setFilterCutoff(8000);
~sub1.setLFO1Waveform('sampleAndHold');
~sub1.setLFO1Freq(1.9);
~sub1.setFilterCutoffLFO1BottomRatio(0.5);
~sub1.setFilterCutoffLFO1TopRatio(1.2);
~sub1.setFilterEnvSustainRatio(0.57);
~sub1.setFilterEnvAttackTime(0.16);
~sub1.setFilterEnvPeakRatio(1.2);
~sub1.setFilterEnvDecayTime(0.835);


// sub 2:

~sub2.setOsc1Waveform('sine');
~sub2.setOsc1Octave(4);
~sub2.setOsc2Waveform('saw');
~sub2.setOsc2Octave(2);
~sub2.setNoiseOscVol(-21);
~sub2.setFilterCutoff(900);
~sub2.setFilterRes(0.25);
~sub2.setLFO1Freq(12);
~sub2.setLFO1Waveform('sampleAndHold');
~sub2.setLFO2Freq(16);
~sub2.setLFO2Waveform('sampleAndHold');
~sub2.setFilterCutoffLFO1TopRatio(4);
~sub2.setFilterCutoffLFO2TopRatio(4);
~sub2.setFilterCutoffLFO1BottomRatio(0.25);
~sub2.setFilterCutoffLFO2BottomRatio(0.25);
~sub2.setFilterDrive(1.5);
~sub2.setFilterEnvAttackTime(1.29);
~sub2.setFilterEnvAttackRatio(0.5);
~sub2.setFilterEnvDecayTime(0.626);
~sub2.setFilterEnvPeakRatio(2.0);
~sub2.setFilterEnvSustainRatio(0.5);
~sub2.setFilterEnvReleaseTime(0.25);
~sub2.setAttackTime(0.65);
~sub2.setDecayTime(0.626);
~sub2.setSustainLevel(0.35);
~sub2.setReleaseTime(0.3);
~sub2.setPanLFO1Bottom(-1);
~sub2.setPanLFO1Top(1);
~sub2.setPanLFO2Bottom(-1);
~sub2.setPanLFO2Top(1);

// subtractive bass:
~subBass.setFilterCutoff(400);
~subBass.setFilterRes(0.35);
~subBass.setOsc1Waveform('saw');
~subBass.setOsc1Octave(3);
~subBass.setOsc2Waveform('tri');
~subBass.setOsc2Octave(4);
~subBass.setFilterEnvAttackTime(2.65);
~subBass.setFilterEnvAttackRatio(1);
~subBass.setFilterEnvDecayTime(0.35);
~subBass.setFilterEnvSustainRatio(0.31);
~subBass.setFilterEnvReleaseRatio(0);
~subBass.setFilterEnvReleaseTime(0.35);
~subBass.setFilterDrive(10);
~subBass.setFilterEnvPeakRatio(2.5);
~subBass.setLFO1Freq(0.9);
~subBass.setLFO1Waveform('sine');
~subBass.setLFO2Waveform('noise');
~subBass.setLFO2Freq(2.8);
~subBass.setFilterCutoffLFO2TopRatio(2);
~subBass.setAttackTime(0.05);
~subBass.setReleaseTime(0.5);
~subBass.setDecayTime(0.626);
~subBass.setSustainLevel(0.5);
~subBass.setAmplitudeLFO1Bottom(0.3);
);

//// MIDI:
(

//// mixer:

//// Sampler:
~snder = MIDIFunc.cc({ | i | ~mixer.setSendVol(0, 0, i.ccdbfs); }, 20, 11);

MIDIFunc.noteOn({ ~sampler.playSampleSustaining(\loop1, 0, -6); }, 60, 11);
MIDIFunc.noteOff({ ~sampler.releaseSampleSustaining(\loop1); }, 60, 11);
MIDIFunc.noteOn({ ~sampler.playSampleSustaining(\loop2, 1, -6); }, 61, 11);
MIDIFunc.noteOff({ ~sampler.releaseSampleSustaining(\loop2); }, 61, 11);
MIDIFunc.noteOn({ ~sampler.playSampleSustaining(\loop3, 2, -6); }, 62, 11);
MIDIFunc.noteOff({ ~sampler.releaseSampleSustaining(\loop3); }, 62, 11);

// sub 1:
~sub1On = Array.fill(127, { | i | MIDIFunc.noteOn({ ~sub1.playNote(i.midicps) }, i, 8); });
~sub1Off = Array.fill(127, { | i | MIDIFunc.noteOff({ ~sub1.releaseNote(i.midicps) }, i, 8); });

/*
~sub1On = Array.fill(127, { | note, vel | MIDIFunc.noteOn({ ~sub1.playNote(note.midicps, vel.ccdbfs) },
note, 8); });
~sub1Off = Array.fill(127, { | note, vel | MIDIFunc.noteOn({ ~sub1.releaseNote(note.midicps) },
note, 8); });
*/

// sub 2:
~sub2On = Array.fill(127, { | i | MIDIFunc.noteOn({ ~sub2.playNote(i.midicps) }, i, 9); });
~sub2Off = Array.fill(127, { | i | MIDIFunc.noteOff({ ~sub2.releaseNote(i.midicps) }, i, 9); });

// moog:
~moogOn = Array.fill(127, { | i | MIDIFunc.noteOn({ ~moog1.playNote(i.midicps) }, i, 0); });
~moogOff = Array.fill(127, { | i | MIDIFunc.noteOff({ ~moog1.releaseNote(i.midicps) }, i, 0); });

// subtractive bass:
~subBassOn = Array.fill(127, { | i | MIDIFunc.noteOn({ ~subBass.playNote(i.midicps) }, i, 1); });
~subBassOff = Array.fill(127, { | i | MIDIFunc.noteOff({ ~subBass.releaseNote(i.midicps) }, i, 1); });
~subVol = MIDIFunc.cc({ | i | ~subBass.mixer.setVol(i.ccdbfs); }, 7, 1);

);

