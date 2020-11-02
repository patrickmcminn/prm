~d = DelayNetwork.newMono(~prm.audioIn, 5, maxDelay: 15, relGroup: ~prm.procGroup, addAction: \addToHead);
~d.free;

~d = DelayNetwork2.newMono(~prm.audioIn, 10, maxDelay: 10, relGroup: ~prm.procGroup, addAction: \addToHead);
~d.free;
~e.free;


5.do({ | i | ~d.setFilterType(i, 2); });

5.do({ | i | ~d.setCutoff(i, 500); });

~d.setDelayTime(0, (60/132)*8);
~d.setDelayTime(1, (60/132)*13);
~d.setDelayTime(2, (60/132)*6);
~d.setDelayTime(3, (60/132)*15.5);
~d.setDelayTime(4, (60/132)*10);
~d.setDelayTime(5, (60/132)*7);
~d.setDelayTime(6, (60/132)*11);
~d.setDelayTime(7, (60/132)*12);
~d.setDelayTime(8, (60/132)*16);
~d.setDelayTime(9, (60/132)*18.5);

5.do({ | i | ~d.setFeedback(i, 0.5); });
	(60/132*18)

~d.randomizeDecayTime(30, 35);
~d.randomizePan(-1, 1);


~d.randomizeDelayTime(5, 5);

//~f = IM_Mixer.new(1, ~d.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);
~e = IM_HardwareIn.new(1, ~d.inBus, ~prm.procGroup, \addToHead);

~d.free
~e.free;


~d.free;
~e.free;

~d.dry.isMuted;
~d.dry.tglMute;

s.meter

~e.unMute

~d.mixer.tglMute;

~d.dry.free;

10.do({ | i | ~d.subMixer.tglMute(i); });

~d.subMixer.tglMute(1)


x = IM_Mixer.new(12);

x.numChans




~shift = PitchShifter.newMono(~prm.audioIn, 12);
~in = IM_HardwareIn.new(0, ~shift.inBus);