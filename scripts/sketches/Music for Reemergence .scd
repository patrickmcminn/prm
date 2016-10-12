~prm = AudioSystem.new(2);

(
~prm.subtractive.setAllParameters([ 1, 1, 5, 0.1, 0.5, 0, 0.05, 0.05, 0.11, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0.4, 1, 1, 1, -0.5, 1.3, 0, 0, 0, 0.5, 0.5, 0, 2, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0.4, 1, 1, 0.3, -1, 0, 0, 1, 0.5, 0.25, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 1, 1, 1, 1, 0.05, 0, 0.05, false, 0.7, 1.3, 1, 1, 0, 0, 0, 0, 1, 1500, 0.3, 0, 1, 1, 1, 1, 10, 0.05, 1, 10, 0, -1, 1, 0, 0 ]);

~prm.subtractive.mixer.setSendVol(0, -12);
~prm.subtractive.mixer.setSendVol(1, -100);

~prm.subtractive.makeSequence(\D);
~prm.subtractive.makeSequence(\E);
~prm.subtractive.makeSequence(\F);
~prm.subtractive.makeSequence(\G);
~prm.subtractive.makeSequence(\A);
~prm.subtractive.makeSequence(\B);
~prm.subtractive.makeSequence(\C);
~prm.subtractive.makeSequence(\D2);
~prm.subtractive.makeSequence(\D0);

);

(
~prm.subtractive.addKey(\D, \note, Pseq([2, \r], inf));
~prm.subtractive.addKey(\D, \dur, Pwhite(15, 25));
~prm.subtractive.addKey(\D, \amp, Pwhite(0.2, 0.32));
~prm.subtractive.addKey(\D, \legato, 1);
~prm.subtractive.addKey(\D, \osc2OctaveMul, Prand([0.5, 1, 1, 2, 4], inf));

~prm.subtractive.addKey(\E, \note, Pseq([4, \r], inf));
~prm.subtractive.addKey(\E, \dur, Pwhite(17, 27));
~prm.subtractive.addKey(\E, \amp, Pwhite(0.2, 0.32));
~prm.subtractive.addKey(\E, \legato, 1);

~prm.subtractive.addKey(\F, \note, Pxrand([5, \r], inf));
~prm.subtractive.addKey(\F, \dur, Pwhite(21, 30));
~prm.subtractive.addKey(\F, \amp, Pwhite(0.2, 0.32));
~prm.subtractive.addKey(\F, \legato, 1);

~prm.subtractive.addKey(\G, \note, Prand([7, \r], inf));
~prm.subtractive.addKey(\G, \dur, Pwhite(11, 20));
~prm.subtractive.addKey(\G, \amp, Pwhite(0.2, 0.32));
~prm.subtractive.addKey(\G, \legato, 1);
~prm.subtractive.addKey(\G, \osc2DetuneCents, Pwhite(0, 20));

~prm.subtractive.addKey(\A, \note, Pxrand([9, \r], inf));
~prm.subtractive.addKey(\A, \dur, Pwhite(15, 25));
~prm.subtractive.addKey(\A, \amp, Pwhite(0.2, 0.32));
~prm.subtractive.addKey(\A, \legato, 1);
~prm.subtractive.addKey(\A, \osc1SubAmp, Prand([0, 0, Pwhite(0.1, 0.6)], inf));

~prm.subtractive.addKey(\B, \note, Prand([11, \r, \r], inf));
~prm.subtractive.addKey(\B, \dur, Pwhite(15, 25));
~prm.subtractive.addKey(\B, \amp, Pwhite(0.2, 0.32));
~prm.subtractive.addKey(\B, \legato, 1);

~prm.subtractive.addKey(\C, \note, Prand([12, \r, \r, 0], inf));
~prm.subtractive.addKey(\C, \dur, Pwhite(15, 25));
~prm.subtractive.addKey(\C, \amp, Pwhite(0.2, 0.32));
~prm.subtractive.addKey(\C, \legato, 1);

~prm.subtractive.addKey(\D2, \note, Pseq([14, \r], inf));
~prm.subtractive.addKey(\D2, \dur, Pwhite(15, 25));
~prm.subtractive.addKey(\D2, \amp, Pwhite(0.2, 0.32));
~prm.subtractive.addKey(\D2, \legato, 1);

~prm.subtractive.addKey(\D0, \note, Pseq([-10, \r, \r, \r], inf));
~prm.subtractive.addKey(\D0, \dur, Pwhite(15, 25));
~prm.subtractive.addKey(\D0, \amp, Pwhite(0.2, 0.32));
~prm.subtractive.addKey(\D0, \legato, 1);

~prm.subtractive.addKey(\D, \note, Pseq([2, \r], inf));
~prm.subtractive.addKey(\D, \dur, Pwhite(15, 25));
~prm.subtractive.addKey(\D, \amp, Pwhite(0.2, 0.32));
~prm.subtractive.addKey(\D, \legato, 1);

);

~prm.subtractive.playSequence(\D);
~prm.subtractive.playSequence(\E);
~prm.subtractive.playSequence(\F);
~prm.subtractive.playSequence(\G);
~prm.subtractive.playSequence(\A);
~prm.subtractive.playSequence(\B);
~prm.subtractive.playSequence(\C);
~prm.subtractive.playSequence(\D2);
~prm.subtractive.playSequence(\D0);



~prm.subtractive.stopSequence(\D);
~prm.subtractive.stopSequence(\E);
~prm.subtractive.stopSequence(\F);
~prm.subtractive.stopSequence(\G);
~prm.subtractive.stopSequence(\A);
~prm.subtractive.stopSequence(\B);
~prm.subtractive.stopSequence(\C);
~prm.subtractive.stopSequence(\D2);
~prm.subtractive.stopSequence(\D0);

s.meter;
~prm.subtractive.setOsc1SubVol(-3);
~prm.subtractive.setOsc2SubVol(-6);


/*
//~prm.subtractive = Subtractive.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus);
(
~prm.subtractive.mixer.setSendVol(0, -12);
~prm.subtractive.mixer.setSendVol(1, -100);

~prm.subtractive.setOsc1Waveform('sine');
~prm.subtractive.setOsc2Waveform('tri');
~prm.subtractive.setOsc2Octave(4);


~prm.subtractive.setAttackTime(10);
~prm.subtractive.setReleaseTime(10);

~prm.subtractive.setLFO1Waveform('noise');
~prm.subtractive.setLFO1Freq(0.1);

~prm.subtractive.setPanLFO1Bottom(-1);
~prm.subtractive.setPanLFO1Top(1);

~prm.subtractive.setFilterCutoff(1500);
~prm.subtractive.setFilterCutoffLFO1BottomRatio(0.7);
~prm.subtractive.setFilterCutoffLFO1TopRatio(1.3);
~prm.subtractive.setLFO2Freq(0.11);
~prm.subtractive.setLFO2Waveform('noise');

~prm.subtractive.setFilterDrive(1);
~prm.subtractive.setFilterRes(0.3);

~prm.subtractive.setOsc1AmpLFO1Bottom(0.4);
~prm.subtractive.setOsc2AmpLFO1Top(0.4);

~prm.subtractive.setOsc1WaveformLFO1Bottom(-0.5);
~prm.subtractive.setOsc1WaveformLFO1Top(1.3);

~prm.subtractive.setOsc2WaveformLFO1Bottom(0.3);
~prm.subtractive.setOsc2WaveformLFO1Top(-1);


~prm.subtractive.setOsc1PulseWidthLFO2Bottom(-1);
~prm.subtractive.setOsc1PulseWidthLFO2Bottom(1);
~prm.subtractive.setOsc1PulseWidthLFO1Bottom(-1);
~prm.subtractive.setOsc1PulseWidthLFO1Bottom(1);

~prm.subtractive.setOsc2PulseWidthLFO2Bottom(-1);
~prm.subtractive.setOsc2PulseWidthLFO2Bottom(1);
~prm.subtractive.setOsc2PulseWidthLFO1Bottom(-1);
~prm.subtractive.setOsc2PulseWidthLFO1Bottom(1);

~prm.subtractive.setFilterType('lowPass');
//~prm.subtractive.setFilterType('highPass');
);
*/
