~prm = AudioSystem.new(2);

(
x = Subtractive.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus,
  relGroup: ~prm.procGroup, addAction: \addToHead);
);

y = TempoClock.new;
y.tempo = 125/60;


x.makeSequence(\end);

~chord1 = [-12, -5, 7, 10, 12, 14, 15];
~chord2 = [-18, -6, 6, 10, -1, 13, 15];
~chord3 = [-12, -5, 7, 10, 12, 14, 15];
~chord401 = [-17, -5, 7, 9, 11, 14, 17];
~chord402 = [-17, -5, 7, 9, 12, 14, 19];
~chord5 = [-16, -8, 8, 12, 13, 15, 19];
~chord6 = [-12, -9, 7, 10, 12, 14, 17];
~chord7 = [-16, -8, 8, 12, 13, 15, 19];
~chord8 = [-17, -5, 7, 10, 12, 14, 19];
~chord9 = [-19, -7, 9, 10, 14, 15, 17];
~chord10 = [-21, -14, 7, 10, 14, 15, 19];
~chord11 = [-22, -14, 9, 10, 12, 15, 17];
~chord12 = [-17, -10, 2, 7, 10, 14, 19];

//x.addKey(\end, \dur, 1/3);
//x.addKey(\end, \note, Pseq([Pseq([~chord1], 16), Pseq([~chord2], 16), Pseq([~chord3], 16), Pseq([~chord401], 16), Pseq([~chord402], 16), Pseq([~chord5], 16), Pseq([~chord6], 16), Pseq([~chord7], 16), Pseq([~chord8], 16), Pseq([~chord9], 16), Pseq([~chord10], 16), Pseq([~chord11], 16), Pseq([~chord12], 16)], inf));
x.addKey(\end, \octave, 5);
x.addKey(\end, \amp, 0.2);
x.addKey(\end, \legato, 1);
x.addKey(\end, \root, 1);
x.addKey(\end, \lfo1Freq, Pseq([6, 16/3, 6, 4, 16/3, 6, 16/3, 4, 16/3, 4, 6, 16/3, 6], inf));

(
x.addKey(\end, \note, Pseq([~chord1, ~chord2, ~chord3, ~chord401, ~chord401, ~chord5, ~chord6, ~chord7, ~chord8,
  ~chord9, ~chord10, ~chord11, ~chord12], inf));
x.addKey(\end, \dur, Pseq([8, 8, 6, 4, 6, 8, 8, 6, 6, 8, 6, 8, 6], inf));
)





x.setOsc2Octave(4);
x.setNoiseOscCutoff(1500);
x.setNoiseOscVol(0);
x.setNoiseOscAmpLFO2Top(0);
x.setNoiseOscAmpLFO2Bottom(0);
x.setLFO2Freq(60/125);
x.setLFO2Waveform('sine');
x.setLFO1Waveform('sine');
x.setNoiseOscAmpLFO1Bottom(0);
x.setLFO1Freq((60/125)*2);
x.setFilterDrive(12);
x.setFilterType('lowPass');

x.setOsc1AmpLFO1Bottom(0);
x.setLFO1Waveform('sine');


x.setOsc1SubVol(-inf);
x.setOsc2SubVol(-inf);

x.setOsc1Waveform('saw');
x.setOsc2Waveform('saw');


x.setFilterCutoff(850);

x.setFilterCutoffLFO2BottomRatio(0.5);
x.setFilterCutoffLFO2TopRatio(1.5);
x.setLFO2Waveform('noise');

x.setFilterResLFO1Bottom(0.5);
x.setFilterResLFO1Top(0);
x.setFilterResLFO2Top(0.4);
x.setFilterResLFO2Bottom(0);

x.stopSequence(\end);

x.setOsc2Vol(-inf);

x.setReleaseTime(0.2);




x.playSequence(\end, y);
x.mixer.setSendVol(0, -inf);

s.makeWindow
