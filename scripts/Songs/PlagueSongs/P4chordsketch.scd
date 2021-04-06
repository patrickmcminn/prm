

~prm = AudioSystem.new(2);

z = IM_Mixer.new(1, ~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus, nil, nil, false, ~prm.procGroup, \addToHead);

y = Decapitator.newStereo(z.chanStereo, relGroup: ~prm.procGroup, addAction: \addToHead);

y.savePreset('plague4');

x = SubJuno.new(y.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);


x.releaseAllNotes;
x.panic


(
x.playNote(60.midicps);
x.playNote(65.midicps);
x.playNote(67.midicps);
x.playNote(76.midicps);
)

({

x.releaseNote(60.midicps);
x.releaseNote(65.midicps);
x.releaseNote(67.midicps);
x.releaseNote(76.midicps);

	0.5.wait;

x.playNote(64.midicps);
x.playNote(68.midicps);
x.playNote(71.midicps);
x.playNote(75.midicps);
}.fork;
)


(
{
x.releaseNote(64.midicps);
x.releaseNote(68.midicps);
x.releaseNote(71.midicps);
x.releaseNote(75.midicps);

	0.75.wait;

x.playNote(61.midicps);
x.playNote(68.midicps);
x.releaseNote(71.midicps);
x.playNote(76.midicps);
}.fork;
)

(
{
	x.releaseNote(61.midicps);
	x.releaseNote(68.midicps);
	x.releaseNote(71.midicps);
	x.releaseNote(76.midicps);

	0.5.wait;

	x.playNote(60.midicps);
	x.playNote(69.midicps);
	x.playNote(71.midicps);
	x.playNote(76.midicps);
}.fork;
)



x.playNote(330);
x.playNote(110);


x.setOsc1SubVol(-70);
x.setOsc2SubVol(-70);

x.releaseNote(220);

x.setPanLFO2Bottom(0.3);
x.setPanLFO2Top(-0.3);

x.setOsc1Waveform('saw');
x.setOsc2Waveform('rect');

x.setLFO2Waveform('noise');
x.setOsc1WaveformLFO2Bottom(-0.5);
x.setOsc1WaveformLFO2Top(0.5);

x.setLFO2Freq(0.3);

x.setFilterCutoffLFO2BottomRatio(0.8);
x.setFilterCutoffLFO2TopRatio(1.5);


x.setAttackTime(0);
x.setDecayTime(0.3);
x.setSustainLevel(0.4);
x.setReleaseTime(0.05);


x.playNote(110);
x.playNote(110*(3/2)*2);

y.makeGUI;
~prm.reverb.makeGUI;
x.panic;

z.setPreVol(0, -12);
z.setVol(0, -6);

x.setPanLFO1Top(1);

z.setSendVol(0, 0, -21);

s.meter

x.setLFO1Waveform('revSaw');
x.setLFO1AttackTime(2);
x.setLFO1EnvType('attackAndRelease');
x.setAmplitudeLFO1Bottom(0);

x.setLFO1Freq(11);


