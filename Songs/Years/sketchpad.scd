y = Years_Drone.new(~prm.audioIn, ~prm.reverb.inBus, send3Bus: a.inBus, addAction: \addToHead);
y.isLoaded;


y.playDrone
y.releaseDrone
y.mixer.setSendVol(3, -30);

z = Years_Drums.new(~prm.audioIn, ~prm.reverb.inBus, addAction: \addToHead);

z.drums.readPreset('years1');

z.drums.playSample(0);
z.granulator.setGrainEnvelope('revPerc');
GranularDelay2
z.distortion.makeGUI

VST

a = Years_Delay.new(~prm.audioIn);
a.isLoaded;



a = DelayNetwork.newStereo(~prm.audioIn, 6, addAction: \addToHead);
a.setDelayTime(0, 1.7);
a.setDelayTime(1, 2);
a.setDelayTime(2, 0.3);
a.setDelayTime(3, 0.4);
a.setDelayTime(4, 1);
a.setDelayTime(5, 2);

a.setFeedback(0, 0.75);
a.setFeedback(1, 0.75);
a.setFeedback(2, 0.71);
a.setFeedback(3, 0.71);
a.setFeedback(4, 0.42);
a.setFeedback(5, 0.42);

a.setPan(0, -1);
a.setPan(1, 1);
a.setPan(2, -1);
a.setPan(3, 1);
a.setPan(4, -1);
a.setPan(5, 1);

a.setFilterType(0, 2);
a.setFilterType(1, 2);
a.setFilterType(2, 0);
a.setFilterType(3, 0);
a.setFilterType(4, 1);
a.setFilterType(5, 1);

a.setCutoff(0, 1260);
a.setCutoff(1, 1260);
a.setCutoff(4, 250);
a.setCutoff(5, 250);




b = Subtractive.new(a.inBus);

b.setSustainLevel(0);
b.setDecayTime(0.05);

b.playNote(440);

b.free;

z.eq.setLowPassCutoff(3500);

z.granulator.setMix(0.8);

z.granulator.setGrainDur(1, 2);
z.granulator.setTrigRate(15);

z.granulator.setDelayLevel(0);


z = SampleGrid.new(~prm.audioIn);
z.loadSample(1)


z.writePreset('years1');

z.playSample(9)
x.isLoaded
y.playDrone
y.mixer.setSendVol(0, -24);

y.tremolo.setVolLFOFreq(8.5);
y.releaseDrone


~cutoffRout = {
	loop {
		var cutoff = exprand(400, 1100).lag(2);
		y.eq.setLowPassCutoff(cutoff);
		rrand(2, 5).wait;
	};
}.fork;

~cutoffRout.stop;
y.eq.setLowPassCutoff(500);

x = MidBells.new(~prm.audioIn);

x.mixer.setSendVol(0, -18);


x = Years_Glock.new(~prm.audioIn, ~prm.reverb.inBus);

x.isLoaded;


~noteRout  = {
	loop{
		var note = [0, 2, 4, 5, 7, 9].choose;
		note = note + 73;
		x.glock.playNote(note.midicps);
		rrand(1, 5).wait;
	};

}.fork;

~noteRout.play
~noteRout.stop;

x.glock.playNote(73.midicps);

x.mixer.setVol(-6);

s.meter


x.glock.mixer.setPreVol(-12);
x.mixer.setPreVol(-12);