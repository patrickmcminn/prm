~yearsDelay = Years_Delay.new(~prm.audioIn, relGroup: ~prm.procGroup, addAction: \addToHead);



~years.glockNoteRout1.play
~years.glockNoteRout1.stop;
~years.glockNoteRout1.reset;

~years.playGlockNoteRout1;
~years.stopGlockNoteRout1;


(
~years = Years.new(~prm.audioIn, ~micIn, ~modInput, ~pyr,
	~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~yearsDelay.inBus,
	~prm.procGroup, \addToHead);
);

s.meter
~years.isLoaded;

~years.ensemble.granulator.setFeedback(0.3);

~apc.setPage('years');

~years.ensemble.distortion.savePreset('yearsEns');


GranularDelay2

~years.ensemble.synths.mixer

~years.ensemble.delayNetwork1.mixer.setPreVol(-9);
~years.ensemble.delayNetwork2.mixer.setPreVol(-9);

s.meter

~years.ensemble.mixer.setPreVol(0);
~years.ensemble.granulator.mixer.setPreVol(-12);


x = IM_HardwareIn.new(~modInput[2], ~prm.audioIn);

x.free;

x;

~years.mixer.setVol(3, -6);

s.meter

SaturSynth
~years.ensemble.distortion.makeGUI
Years_BlockChords




~years.bass.setReleaseTime(0.75);

~years.ensemble.granulator.mix(0.5);

~years.ensemble.subtractive.readPreset('ver sacrum');
~years.ensemble.subtractive.writePreset('yearsStrings');
~yearsDelay.isLoaded
~years.isLoaded

~years.ensemble.delayNetwork1.mixer.setVol(-3);
~years.ensemble.delayNetwork2.mixer.setVol(-3);

~years.makeMIDIFuncs;
~years.toggleMIDIFuncs
~years.drone.playDrone

~years.toggleDroneCutoffRout
~years.toggleDroneTremRout

~years.toggleGlockNoteRout1;

~years.drone.releaseDrone;

~years.glock.glock.playNote(61.midicps);

~years.ensemble.delayNetwork1.mixer.setVol(-6);
~years.ensemble.delayNetwork2.mixer.setVol(-6);

~years.ensemble.subtractive.mixer.setPreVol(6);
~pyr.uid
Routine

~years.ensemble.subtractive.releaseTime
~years.ensemble.delayNetwork1.mixer.setVol(-6);
~years.ensemble.delayNetwork2.mixer.setVol(-6);

~years.stopGlockNoteRout1;

~years.glock.free;
~apc.turnGridGreen(0, 0);

s.meter

x = Years_BlockChords.new(~prm.audioIn, ~prm.reverb.inBus);
x.isLoaded

z = Years_EndHorns.new(~prm.audioIn, ~prm.reverb.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);

z.isLoaded;

z.eq

z.mixer.vol

DelayNetwork

~years.ensemble.eq.setPeak1Freq(350);
~years.ensemble.eq.setPeak1Gain(-4);
~years.ensemble.eq.setPeak3Freq(1100);
~years.ensemble.eq.setPeak3Gain(-3);

~years.bass.setReleaseTime(1);

z

~in = IM_HardwareIn.new(0, z.inBus, ~prm.procGroup, \addToHead);

z.granulator.setTrigRate(3);
~in.free;
);

s.meter

~years.ensemble.distortion

~years.ensemble.distortion.mix

Years_Ensemble
~years.ensemble.distortion.makeGUI
~years.ensemble.granulator.mixer.setPreVol(-12);
~years.ensemble.granulator.setMix(0);

a = Years_Delay.new(~prm.audioIn);

y = Years_Drone.new(~prm.audioIn, ~prm.reverb.inBus, send3Bus: a.inBus, addAction: \addToHead);

x = Years_Glock.new(~prm.audioIn, ~prm.reverb.inBus, send3Bus: a.inBus);

x.subtractive.playNote(220);
x.subtractive.releaseNote;

x.subtractive.playNote(56.midicps);
x.subtractive.playNote(61.midicps);
x.subtractive.playNote(65.midicps);

x.subtractive.releaseNote(56.midicps);
x.subtractive.releaseNote(61.midicps);
x.subtractive.releaseNote(65.midicps);

SampleGrid

x.subtractive.setAttackTime(0.8);
x.subtractive.setReleaseTime(0.8);

x.subtractive.writePreset('yearsBlockChords');

x.subtractive.readPreset('yearsBlockChords');

Decapitator
x.distortion.savePreset('yearsBlockChords');
x.distortion.loadPreset('yearsBlockChords');

x.subtractive.setOsc1SubVol(-70);
x.subtractive.setOsc2SubVol(-70);

x.subtractive.setOsc1Waveform('rect');
x.subtractive.setOsc1PulseWidthLFO1Bottom(-0.4);
x.subtractive.setLFO2Waveform('noise');
x.subtractive.setOsc2PulseWidthLFO2Top(0.5);
x.subtractive.setOsc2PulseWidthLFO2Bottom(-0.3);

x.subtractive.setFilterCutoffLFO2BottomRatio(0.8);
x.subtractive.setFilterCutoffLFO2TopRatio(1.2);
x.subtractive.setFilterDrive(10);

x.filter.setCutoff(800);

LowPassFilter
x.filter.lfo.setWaveform('sampleAndHold');
x.filter.lfo.setFrequency(4.5);
x.filter.setCutoffLFOBottomRatio(0.8);
x.filter.setCutoffLFOTopRatio(3);

x.distortion.makeGUI
s.meter

x.subtractive.releaseAllNotes;

x.delay.setFeedback(1, 0);

x.filter.setCutoff(15000);

x.subtractive.releaseNote(66.midicps);

x.delay.mixer.setVol(-3);

x.delay.dry

DelayNetwork

y.playDrone;


x = Years_FreezeTpts.new(~prm.audioIn, ~prm.reverb.inBus);
x.isLoaded;

y = IM_HardwareIn.new(0, x.inBus, addAction: \addToHead);

x.recordBuffer;

x.playCluster
x.releaseCluster
s.meter

x.freeze.playNote('test', -7);
x.freeze.releaseNote('test');

x.free;
y.mixer.setSendVol(3, -48);
y.mixer.setSendVol(0, -24);

y.tremolo.setVolLFOFreq(12);

x.mixer.setSendVol(0, -24);

x.mixer.setSendVol(3, -18);

~noteRout.stop;
y.releaseDrone

~noteRout  = {
	loop{
		var note = [0, 2, 4, 5, 7, 9].choose;
		note = note + 73;
		x.glock.playNote(note.midicps);
		rrand(1, 5).wait;
	};

}.fork;

~cutoffRout = {
	loop {
		var cutoff = exprand(400, 1100).lag(2);
		y.eq.setLowPassCutoff(cutoff);
		rrand(2, 5).wait;
	};
}.fork;

~tremRout = {
	loop {
		var freq = rrand(6, 12).lag(2);
		y.tremolo.setVolLFOFreq(freq);
		rrand(2, 5).wait;
	};
}.fork;


Years_Drums

x = Subtractive.new;

x.readPreset('ver sacrum');

x.attackTime

x.playNote(220);

x.releaseNote(220);
~years.ensemble.mixer.setPreVol(-12);

Years_Ensemble

Years_Delay

s.meter