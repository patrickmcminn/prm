
~prm = AudioSystem.new(2);
~path = "/Users/patrickmcminn/Documents/Music/Patrick McMinn/sketches/SuperCollider Saw Loops Project/Hymn .aif";
~buf = Buffer.read(s, ~path);

x = { Out.ar(~prm.submixA, PlayBuf.ar(2, ~buf, 1, 1, 0, 1)) }.play;
x.free;

~prm.submixerA.isRecording;
~prm.submixerA.playLoop;
~prm.submixerA.loop;
~prm.submixerA.stopLoop;
~prm.submixerA.clearLoop(30);
~prm.submixerA.setLoopDivison(13);
~prm.submixerA.setLoopPosition(0.4);
~prm.submixerA.setLoopRate(1);

~prm.submixerA.eq.setLowGain(6);
~prm.submixerA.eq.setLowRQ(0.5);
~prm.submixerA.eq.setHighGain(-12);
~prm.submixerA.eq.setHighFreq(1500);
~prm.submixerA.eq.setPeak2Gain(-6);
~prm.submixerA.eq.setPeak1Freq(900);
s.meter;
s.makeWindow;

~prm.submixerA.mixer.setVol(0);
~prm.submixerA.mixer.setSendVol(1, -70);
~prm.submixerA.mixer.tglMute;
~prm.granulator.setGrainDur(0.5, 4);

~prm.granulator.setGrainEnvelope('rexpodec');