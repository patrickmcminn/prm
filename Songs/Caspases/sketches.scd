x = SubJuno.new(0);

x.playNote(220);

x.releaseNote(220);

x.setAttackTime(0.5);
x.setReleaseTime(1);





x.setOsc1Waveform('rect');
x.setOsc2Waveform('rect');
x.setOsc1SubVol(-12);
x.setOsc2SubVol(-inf);



x.setOsc2Octave(3);

x.setOsc1PulseWidth(0.2);
x.setOsc2PulseWidth(0.7);


x.setLFO1Waveform('noise');
x.setLFO2Waveform('noise');
x.setLFO2Freq(0.8);

x.setAmplitudeLFO1Bottom(0.3);
x.setAmplitudeLFO1Top(1.5);
x.setFilterCutoffLFO1BottomRatio(0.8);
x.setFilterCutoffLFO1TopRatio(1.2);

x.setPanLFO1Bottom(-0.7);
x.setPanLFO1Top(0.7);

x.setPanLFO2Bottom(0.3);
x.setPanLFO2Top(0.3);


x.setFilterDrive(3);

x.chorus.setDepth(0.35);
x.chorus.setTimeMul(0.05);


x.mixer.setVol(-9);

x.setFilterCutoff(4000);


x.mixer.tglMute;

s.meter


x.writePreset('caspasesJuno');
x.readPreset('caspasesJuno');

x.playNote(69.midicps);
x.playNote(66.midicps);
x.playNote(64.midicps);
x.playNote(61.midicps);
x.playNote(59.midicps);
x.playNote(52.midicps);


x.releaseNote(69.midicps);
x.releaseNote(66.midicps);
x.releaseNote(64.midicps);
x.releaseNote(61.midicps);
x.releaseNote(59.midicps);
x.releaseNote(52.midicps);

x.playNote(64.midicps);
x.playNote(52.midicps);
x.playNote(59.midicps);

x.releaseNote(64.midicps);
x.releaseNote(52.midicps);
x.releaseNote(59.midicps);

x..releaseAllNotes;



x = Subtractive.new();

x.readPreset('midnightSong');

x.writePreset('caspasesPrickles');


x.playNote(12);
x.releaseNote(440);

10.do({ | i | x.playNote(rrand(880, 1220)) });

x.setFilterCutoff(330)


~caspases = Caspases.new(~prm.audioIn, ~micIn, ~pickupIn, ~modInput, ~digiTrackArray, ~pyr,
	~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
	~prm.procGroup, \addToHead);

~caspases.isLoaded;

~caspases.texture.playSample(0);
~caspases.texture.releaseSample(0);


~prm.reverb.loadPreset('caspases');


s.meter

MIDIIn.connectAll;