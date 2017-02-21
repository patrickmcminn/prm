z = SimpleDelay.newStereo(0);
x = Subtractive.new(z.inBus);
y = SaturSynth.new;

y.playNote(36.midicps);
y.setTremoloRate(3.25);
y.setTremoloDepth(1);

y.releaseNote(36.midicps);

z.setDelayTime(1.5);
z.setFeedback(0.7);
z.setMix(0.3);


x.savePreset('goodDrone', [ 1, 1, 4, 5, 0.5, 0, 0.05, 0.05, 0.1, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0.1, 0.9, 0, 1, 1, 1, 0, 0, -3, 3, 3, 0.3, 0.5, 0, 0.5, -5, 1, 1, 0, 1, 1, 1, 1, 0.1, 0.9, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 3, 0.7, 0.25, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0.063095734448019, 10000, 1, 1, 1, 1, 0.05, 0, 0.05, false, 0.4, 1.7, 0.7, 1.7, 0, 0, 0, 0, 10, 1200, 0.8, 0, 1, 1, 1, 1, 7, 0.05, 1, 5, 0, -1, 1, 0, 0 ]);

x.loadPreset('goodDrone');


x.makeSequence('drone1');
x.makeSequence('drone2');
x.makeSequence('drone3');

(
x.addKey(\drone1, \note, Prand([0, 0, 0, 0, 7, 7, 7, 7, 12, 12, 12, 3, 3, 3, 3, 2, 2, 2, 5, 8, 8, 10, 10], inf));
x.addKey(\drone1, \octave, Prand([3, 4, 4, 5, 5, 5, 4, 6, 6], inf));
x.addKey(\drone1, \legato, 1);
x.addKey(\drone1, \dur, Prand([2, 3, 4], inf));

x.addKey(\drone2, \note, Prand([0, 0, 0, 0, 7, 7, 7, 7, 12, 12, 12, 3, 3, 3, 3, 2, 2, 2, 5, 8, 8, 10, 10], inf));
x.addKey(\drone2, \octave, Prand([3, 4, 4, 5, 5, 5, 4, 6, 6], inf));
x.addKey(\drone2, \legato, 1);
x.addKey(\drone2, \dur, Prand([2, 3, 4], inf));

x.addKey(\drone3, \note, Prand([0, 0, 0, 0, 7, 7, 7, 7, 12, 12, 12, 3, 3, 3, 3, 2, 2, 2, 5, 8, 8, 10, 10], inf));
x.addKey(\drone3, \octave, Prand([3, 4, 4, 5, 5, 5, 4, 6, 6], inf));
x.addKey(\drone3, \legato, 1);
x.addKey(\drone3, \dur, Prand([2, 3, 4], inf));
);

(
x.playSequence(\drone1);
x.playSequence(\drone2);
x.playSequence(\drone3);
)

x.addKey(\drone, \legato, 1);
x.addKey(\drone, \dur, Prand([1, 2, 4], inf));
x.addKey(\drone, \note, Prand([
  Prand([0, 0, 0, 7, 7, 7, -5, -5, 3, 3, 2, 8, 10], inf),
  Prand([0, 0, 0, 7, 7, 7, -5, -5, 3, 3, 2, 8, 10], inf),
  Prand([0, 0, 0, 7, 7, 7, -5, -5, 3, 3, 2, 8, 10], inf),
  Prand([0, 0, 0, 7, 7, 7, -5, -5, 3, 3, 2, 8, 10], inf),
], inf));

x.addKey(\drone, \note, Prand([[0, 3, 7], [0, -12, 12], 0, [7, 8, 9], 7, 7, -5, -5, 3, 3, 2, 8, 10, [0, 3, 7], [0, 2, 3], [0, -2, 5]], inf));
x.addKey(\drone, \octave, Prand([3, 4, 4, 5, 5, 5, 4, 6, 6, 7, 7], inf));

(
x.addKey(\drone1, \dur, Pseq([4, 4, 4, 8], inf));
x.addKey(\drone1, \note, Pseq([ [-12, 0, 3, 7], [-15, -2, 3, 7], [-19, -2, 2, 7], [-14, -2, 2, 5] ], inf));
x.addKey(\drone1, \octave, 5);
x.stopSequence(\drone2);
x.stopSequence(\drone3);
);

x.addKey(\drone1, \note, Pseq([~chord1, ~chord2, ~chord3, ~chord4], inf));

~chord1 = [0, 3, 7];
~chord2 = [-2, 2, 7];
~chord3 = [-2, 2, 5];
~chord4 = [-4, 0, 5];



x.setReleaseTime(6);
x.setFilterCutoff(5500);
x.setFilterType('lowPass');
x.playSequence(\drone);

x.setLFO1Freq(5);
x.setLFO2Freq(3);

x.setPanLFO1Bottom(-1);
x.setPanLFO1Top(1);
x.setReleaseTime(12);

x.setPan(0);

x.setFilterCutoff(5500);


x.playNote(220);
x.playNote(55);
x.playNote(110);
x.playNote(330);
x.releaseNote(220);
x.playNote(440);
x.playNote(528);
x.playNote(495);
x.playNote(352, -15);
x.playNote(660, -18);

x.releaseNote(660);
x.releaseNote(220);
x.releaseNote(330);
x.releaseNote(440);
x.releaseNote(110);
x.releaseNote(55);

x.releaseNote(352);
x.releaseNote(528);
x.releaseNote(495);

x.releaseAllNotes;

x.setOsc1AmpLFO1Bottom(1);
x.setOsc2AmpLFO1Bottom(1);


x.setOsc2Octave(2);

x.setOsc1Waveform('rect');

x.setOsc1PulseWidth(0.3);
x.setOsc1PulseWidthLFO2Bottom(0.1);
x.setOsc1PulseWidthLFO2Top(0.9);

x.setOsc2PulseWidth(0.7);
x.setOsc2PulseWidthLFO1Bottom(0.1);
x.setOsc2PulseWidthLFO1Top(0.9);

x.setLFO1Freq(50);

x.setOsc2DetuneCents(-10);


x.setLFO2Freq(11);

x.setReleaseTime(1.5);
x.setAttackTime(0.3);

x.setLFO2Waveform('noise');
x.setLFO1Waveform('sampleAndHold');

x.setFilterCutoff(1200);

x.setFilterCutoffLFO1BottomRatio(0.4);
x.setFilterCutoffLFO1TopRatio(1.7);

x.setFilterCutoffLFO2BottomRatio(0.7);
x.setFilterCutoffLFO2TopRatio(1.7);

x.setFilterRes(0);

x.setFilterDrive(10);


x.setPanLFO1Bottom(-1);
x.setPanLFO1Top(1);

x.mixer.setVol(-6);

x.setOsc1SubVol(-70);
x.setOsc2SubVol(-70);



x.setOsc1WaveformLFO2Bottom(-3);
x.setOsc1WaveformLFO2Top(3);



x.setNoiseOscAmpLFO1Top(1);
x.setNoiseOscAmpLFO1Bottom(1);
x.setNoiseOscVol(-70);

x.printAllParameters;
x.saveCurrentAsPreset('goodDrone');
x.saveCurrentAsPreset('noiseDrone');



Subtractive


x.makeSequence(\a);
x.addKey(\a, \dur, Pseq([1, 1, 2, 1, 1, 2, 1, 1, 3]/2, inf));
x.addKey(\a, \note, Pseq([[0, -5], [2, -5], [3, -7], [0, -5], [2, -5], [-2, -5], [0, -5], [2, -5], [7, -4]], inf));
x.addKey(\a, \legato, 1);
x.playSequence(\a);

x.playNote(220);

x.setAttackTime(0.1);
x.setReleaseTime(0.5);

x.setFilterType('lowPass');

