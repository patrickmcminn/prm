~prm = AudioSystem.new(2);

~path = "/Users/patrickmcminn/Music/Patrick McMinn/_Sketches/cutting/Audio Files/cutsSplit.aif";

y = Distortion.newStereo(~prm.submixA, 10, ~prm.reverb.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);
(
x = SamplePlayer.newMono(~prm.submixA, ~path, ~prm.reverb.inBus, ~prm.granulator.inBus,
  relGroup: ~prm.procGroup, addAction: \addToHead);
);

x.playSampleSustaining('ok');
x.releaseSampleSustaining('ok');

y.setDistortionGain(1);

x.makeSequence('aut');

(
x.addKey(\aut, \dur, Pseq([0.25, Pseq([0.05], 16), 0.125, 0.125, 0.125, 0.125, 0.5/3, 0.5/3, 0.5/3, 0.5/3], inf));
x.addKey(\aut, \rate, Pseq([0.75, Prand([1, 2], 16), 2, 2, 2, 2, 2/3, 2/3, 2/3, 2/3], inf));
x.addKey(\aut, \legato, 1);
x.addKey(\aut, \freq, Prand([1, 1], inf));
x.addKey(\aut, \startPos, Pwhite(0, 0.3));
x.addKey(\aut, \endPos, Pwhite(0.5, 1));
x.addKey(\aut, \pan, Pseq([0, Pseq([-1, 1], 16), 0, 0, -0.3, 0.3, -0.5, 0.5, -0.5, 0.5], inf));

x.mixer.setVol(12);
x.mixer.setSendVol(0, -inf);
);

x.playSequence(\aut);
x.stop

x.setReleaseTime(3);

~prm.submixerA.eq.setLowPassCutoff(15500);
~prm.submixerA.eq.setLowFreq(0);
~prm.submixerA.eq.setLowGain(1);


x.mixer.setSendVol(1, -18);

~prm.granulator.setGranulatorCrossfade(-1);
~prm.granulator.setFeedback(0.75);
~prm.granulator.setDelayMix(1);
~prm.granulator.setDelayTime(0.1);