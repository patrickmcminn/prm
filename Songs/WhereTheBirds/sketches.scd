~cool = IM_HardwareIn.new(~modInput[1], 0, ~prm.procGroup, \addToHead);

~cool.free



~cool.free;

~dist = Distortion2.newMono(~prm.audioIn, 0.5, 0.5, ~prm.reverb.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);

~dist.free;

~dist.setDistAmp(1);
~dist.setDistSmooth(1)

~dist.postEQ.setHighPassCutoff(100);

~dist.postEQ.setLowPassCutoff(4500)

~dist.mixer.setSendVol(0, -inf);

~dist.mixer.setPreVol(0);

~dist.free;

~dist.lfo.setFrequency(5);
~dist.lfo.setRangeLow(0.25);

Pmono

~birdChords = WhereTheBirds_Chords.new(~prm.audioIn, ~modOutput[2],~modOutput[3], ~modOutput[4], ~modInput[2],
  ~prm.procGroup, \addToHead);

~dist = WhereTheBirds_DistChords.new(~prm.audioIn, ~modInput[2], ~prm.procGroup, \addToHead);
~dist.mixer.tglMute;

~birdChords.playSequence(\drone);
~birdChords.stopSequence(\drone);
~birdChords.stopSequence(\section1);

~birdChords.addKey(\section1, \freq, Pseq([66.midicps, 69.midicps, 61.midicps, 64.midicps], inf));
~birdChords.addKey(\section1, \chordValue, Pseq([0.13, 0.5, 0.13, 0.5], inf));
~birdChords.addKey(\section1, \inversionValue, Pseq([0.05, 0.03, 0.05, 0], inf));

~birdChords.playSequence(\section1, ~clock);

~birdChords.setPitchValue(0.3);
~birdChords.setInversionValue(0.05);
~birdChords.setChordValue(0.05)

~birdChords.free;

~birdChords.setFreq(61.midicps);
~birdChords.setChordValue(0);
~birdChords.setInversionValue(0);


~birdChords.makeSequence(\test);
~birdChords.addKey(\test, \note, Pseq([0, 2, 4, 5], inf));


~birdChords.addKey(\test, \freq, Pseq([66.midicps, 61.midicps], inf));
~birdChords.addKey(\test, \chordValue, Pseq([0.13, 0.5], inf));
~birdChords.addKey(\test, \dur, 1);
~birdChords.addKey(\test, \legato, 1);

~birdChords.playSequence(\test, ~clock);
~birdChords.stopSequence(\test);

~clock = TempoClock.new;
~birdChords

~clock.tempo = 50/60





~mix = IM_Mixer.new(8, ~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
  false, ~prm.procGroup, \addToHead);


~mix.setSendVol(0, 0, 0);


~bed = WhereTheBirds_Bed.new(~mix.chanStereo(0), ~prm.procGroup, \addToHead);
~bed = WhereTheBirds_Bed.new;

~bed.prSetSequenceParameters;

~bed.isLoaded;

~bed.synth.playSequence(\a);
~bed.synth.playSequence(\gSharp);
~bed.synth.playSequence(\b);
~bed.synth.playSequence(\e)

~bed.synth.stopSequence(\a);
~bed.synth.stopSequence(\fSharp);
~bed.synth.stopSequence(\b);
~bed.synth.stopSequence(\e);

~bed.synth.playSequence(\cSharp);

~bed.mixer.setPreVol(-12)

~bed.granulator.setGranulatorCrossfade(-0.75);
~bed.granulator.setDelayMix(0);

~bed.synth.isLoaded;
~bed.synth.makeSequence(\);


~eq = Equalizer.newStereo(~mix.chanStereo(0), ~prm.procGroup, \addToHead);

~sub = Subtractive.new(~eq.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);

~mix.setVol(0, -9);

~mix.setPreVol(0, -12);

~mix.setSendPre(0);

~eq.setHighPassCutoff(500);
~eq.setPeak1Freq(350);
~eq.setPeak1Gain(-6);

~eq.setPeak2Freq(650);
~eq.setPeak2Gain(-6);

~eq.mixer.unMute


~sub.makeSequence(\cSharp);
~sub.makeSequence(\gSharp);
~sub.makeSequence(\a);
~sub.makeSequence(\fSharp);

~sub.addKey(\cSharp, \dur, Pseq([1.576, Rest(5.424)], inf));
~sub.addKey(\cSharp, \note, 1);
~sub.addKey(\cSharp, \octave, 6);
~sub.addKey(\cSharp, \amp, Pwhite(0.3, 0.55, inf));
~sub.addKey(\cSharp, \pan, Pwhite(-1, 1, inf));


~sub.addKey(\gSharp, \dur, Pseq([1.576, Rest(6)], inf));
~sub.addKey(\gSharp, \note, 8);
~sub.addKey(\gSharp, \octave, 5);
~sub.addKey(\gSharp, \amp, Pwhite(0.25, 0.3, inf));
~sub.addKey(\gSharp, \pan, Pwhite(-1, 1, inf));


~sub.addKey(\a, \dur, Pseq([1.576, Rest(5.9)], inf));
~sub.addKey(\a, \note, 9);
~sub.addKey(\a, \octave, 5);
~sub.addKey(\a, \amp, Pwhite(0.2, 0.29, inf));
~sub.addKey(\a, \pan, Pwhite(-1, 1, inf));

~sub.addKey(\fSharp, \dur, Pseq([1.576, Rest(5.5)], inf));
~sub.addKey(\fSharp, \note, 6);
~sub.addKey(\fSharp, \octave, 5);
~sub.addKey(\fSharp, \amp, Pwhite(0.25, 0.35, inf));
~sub.addKey(\fSharp, \pan, Pwhite(-1, 1, inf));



~sub.playSequence(\cSharp);
~sub.playSequence(\gSharp);
~sub.playSequence(\a);
~sub.playSequence(\fSharp);

~sub.stopSequence(\cSharp);
~sub.stopSequence(\gSharp);
~sub.stopSequence(\a);

~sub.setNoiseOscVol(-40);

attack = 0.262;
sustain = 1.576;
release = 0.162;

~sub.setAttackTime(2);
~sub.setReleaseTime(1.5);

~sub.setFilterDrive(2);



~sub.setFilterCutoff(1100);
~sub.setOsc1Waveform(1);
~sub.setOsc2Waveform(1.5);
~sub.setOsc1Octave(3);
~sub.setOsc2Octave(3);

~sub.playNote(61.midicps);
~sub.releaseNote(61.midicps);







  