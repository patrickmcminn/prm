x = TempoClock.new;
x.tempo = 92/60;

~prm = AudioSystem.new(2);
y = Subtractive.new(0, ~prm.reverb.inBus, ~prm.granulator.inBus);


y.playNote(110);
y.mixer.setSendVol(0, -21);
y.mixer.setSendVol(1, -6);
y.releaseNote(110);

~prm.granulator.granulator.setCrossfade(-1);
~prm.granulator.setDelayMix(1)


y.makeSequence(\ok);

y.addKey(\ok, \dur, 0.25);
y.addKey(\ok, \note, Pseq([0, -2, 0, 2, 3, 5, 7], inf));
y.addKey(\ok, \legato, 1);
y.addKey(\ok, \amp, 0.2);
y.addKey(\ok, \pan, Pbrown(-1, 1));

y.setReleaseTime(0.05);
y.setSustainLevel(0.5);
y.setDecayTime(0.15);
y.setAttackTime(0);



y.playSequence(\ok, x, 1);


y.makeSequence(\nok);

y.addKey(\nok, \dur, 0.25);
y.addKey(\nok, \note, Pseq([7, 9, 7, 3, 2, \r, \r], inf));
y.addKey(\nok, \legato, 1);
y.addKey(\nok, \octave, 6);
y.addKey(\nok, \amp, 0.25);
y.addKey(\nok, \note, Pseq([7, 9, 7, 3, 2, \r], inf));
y.addKey(\nok, \note, Pseq([7, 9, 7, 3, 2], inf));

y.addKey(\nok, \note, Pseq([7, 9, 7, 9, 7, 3, 2, 0], inf));
y.addKey(\nok, \pan, Pbrown(-1, 1));

y.playSequence(\nok, x, 1);



y.setOsc1Waveform('rect');
y.setOsc2Waveform('tri');
y.setOsc1Octave(3);
y.setOsc2Octave(2);
y.setOsc2DetuneCents(0);
y.setOsc1PulseWidth(0.3);
y.setOsc1PulseWidthLFO1Bottom(0.3);
y.setOsc1PulseWidthLFO2Bottom(-0.5);

y.setFilterType('lowPass');

y.setOsc1SubVol(-70);
y.setOsc2SubVol(-70);


y.setLFO1Freq(1.5)


y.stopSequence(\ok);



y.makeSequence(\mok);
y.addKey(\mok, \dur, 0.75);

y.addKey(\mok, \note, Pseq([0, -5, 2, 3, 2], inf));
y.addKey(\mok, \legato, 1);
y.addKey(\mok, \octave, 4);
y.addKey(\mok, \amp, 0.5);
y.addKey(\mok, \osc1SubAmp, 0.5);
y.addKey(\mok, \pan, 0);


y.playSequence(\mok, x, 1);
y.stopSequence(\mok);




y.makeSequence(\lok);
y.addKey(\lok, \dur, 4);
y.addKey(\lok, \note, Pseq([[0, 7, 15], \r, [-2, 7, 14], \r, [0, 5, 9], \r], inf));
y.addKey(\lok, \octave, Prand([5, 6], inf));
y.addKey(\lok, \legato, 1);
y.addKey(\lok, \releaseTime, 7);
y.addKey(\lok, \amp, 0.2);
y.playSequence(\lok, x, 1);
y.stopSequence(\lok);



y.makeSequence(\sok);
y.addKey(\sok, \dur, 0.5);
y.addKey(\sok, \note, Pseq([0, -5, 3, 5, -2], inf));
y.addKey(\sok, \legato, 1);
y.addKey(\sok, \amp, Pwhite(0.4, 0.5));
y.addKey(\sok, \octave, 4);
y.a

y.playSequence(\sok, x, 1);

s.meter;



y.makeSequence(\bok);
y.addKey(\bok, \dur, 0.125);
y.addKey(\bok, \note, Pseq([Pseq([0], 16), Pseq([2], 16)], inf));
y.addKey(\bok, \legato, 1);
y.addKey(\bok, \amp, 0.3);
y.addKey(\bok, \octave, 7);
y.addKey(\bok, \attackTime, 0);
y.addKey(\bok, \decayTime, 0.05);
y.addKey(\bok, \sustainLevel, 0);
y.addKey(\bok, \pan, Pwhite(-1, 1));

y.playSequence(\bok, x, 1);
y.setFilterDrive(10);



y.setFilterCutoffLFO2BottomRatio(0.8);
y.setFilterCutoffLFO2TopRatio(1.5);

y.setFilterCutoffLFO1TopRatio(1.3);
y.setFilterCutoffLFO1BottomRatio(0.7);

y.setLFO2Freq(0.3);
y.setLFO2Waveform('noise');
y.setFilterRes(0.3);


y.stopSequence(\ok);
y.stopSequence(\nok);
y.stopSequence(\lok);
y.stopSequence(\mok);
y.stopSequence(\sok);
y.stopSequence(\bok);

y.mixer.setVol(-12);
s.meter;