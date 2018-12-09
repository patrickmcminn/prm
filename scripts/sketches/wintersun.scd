~prm = AudioSystem.new(2);

~mix = IM_Mixer.new(8, ~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus,
relGroup: ~prm.procGroup, addAction: \addToHead);

~kurz = IM_HardwareIn.new(4, ~mix.chanMono(0), ~prm.procGroup, \addToHead);
~kurz.free;

~mix.free;


x = MIDISequencer.new("mio10", "DIN 1", 0);

x.makeSequence(\test);

x.setSequencerClockTempo(440);

x.addKey(\test, \dur, Pseq([0.25, 0.25, 0.5], inf));
x.addKey(\test, \note, Pseq([[0, 2, 4, 5, 7], [0, 12], [7, 12, -2], \], inf));
x.addKey(\test, \octave, Prand([5, 6, 7, 6], inf));


x.playSequence(\test);
x.stopSequence(\test);

z.free;

~mix.tglMute(0);

~mix.setSendVol(0, 0, -inf);
~mix.setSendVol(0, 1, -inf);

~mix.setSendVol(0, 0, -9);
~mix.setSendVol(0, 1, -3);

~mix.setVol(0, 0);
~mix.setVol(0, -24);

~mix.setSendPre(0);

~prm.granulator.setGrainEnvelope(\gabWide);

~prm.granulator.setTrigRate(24);
~prm.granulator.setGrainDur(0.0001, 0.3);


GranularDelay

IM_Granulator


z.free;
z = SaturSynth.new(~mix.chanStereo(1), ~prm.reverb.inBus, ~prm.granulator.inBus,
relGroup: ~prm.procGroup, addAction: \addToHead);


z.setSequencerClockTempo(120);

z.makeSequence(\bass);

z.addKey(\bass, \dur, Pseq([4, 4, 12, 8, 4], inf));
z.addKey(\bass, \note, Pseq([[0, 12], [-2, 10], [2, 14], \, [7, 19], \, \], inf));
z.addKey(\bass, \octave, 3);

z.playSequence(\bass);

z.setReleaseTime(4);
z.setAttackTime(1);

~mix.setVol(1, -12);
~mix.setSendVol(1, 0, -21);

~mix.setVol(2, -18);

~mix.setSendVol(2, 0, -6);

a = Subtractive.new(~mix.chanStereo(2), ~prm.reverb.inBus, ~prm.granulator.inBus,
relGroup: ~prm.procGroup, addAction: \addToHead);


a.setSequencerClockTempo(120);


a.makeSequence(\cD);

a.addKey(\cD, \dur, Pwhite(12, 16, inf));
a.addKey(\cD, \note, Prand(
  [
    [0, 2, 4],
    [0, 2, 7],
    [0, -2, 2],
    [5, 9, 12, 4],
    \,
    \,
], inf));

a.addKey(\cD, \ocatve, 5);

a.setAttackTime(3);
a.setReleaseTime(6);

a.setFilterCutoff(2500);

a.setFilterEnvAttackRatio(0.3);
a.setFilterEnvPeakRatio(1);
a.setFilterEnvAttackTime(5);

a.setFilterRes(0.4);


a.setFilterDrive(10);
a.playSequence(\cD);

a.setOsc1Waveform('rect');

a.setOsc1PulseWidthLFO2Bottom(0);
a.setOsc1PulseWidthLFO2Top(1);

a.setOsc1PulseWidthLFO1Bottom(0);
a.setOsc1PulseWidthLFO1Top(1);

a.setLFO2Freq(0.6);
a.setLFO1Freq(0.9);

~mix.tglMute(1);

a.setPanLFO1Bottom(-1);
a.setPanLFO1Top(1);

a.setLFO1Waveform('noise');

s.makeWindow;






~mix.setSendVol(0, 0, -36);
~mix.setSendVol(0, 1, -40);

~mix.setVol(0, -6);





x.playSequence(\test);

z.playSequence(\bass);

a.playSequence(\cD);



z.stopSequence(\bass);
a.stopSequence(\cD);

x.stopSequence(\test);

// s.makeWindow



