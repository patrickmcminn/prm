x = Subtractive.new;
x.free;
x.setReleaseTime(0.5);

z.play;

x.makeSequence('seq1');
x.makeSequence('seq3');
x.setSequencerClockTempo(60);


x.addKey('seq3', \dur, Pseq([3, 5, 3], inf));
x.addKey('seq3', \note, Pseq([[24, 19], [23, 19], [19, 16]], inf));
x.playSequence('seq3');
x.addKey('seq3', \legato, 0.3);
x.addKey('seq3', \releaseTime, 6);
x.addKey('seq3', \sustainLevel, 0.6);
x.addKey('seq3', \decayTime, 0.2);
x.addKey('seq3', \attackTime, 0.15);




x.addKey('seq1', \legato, 1);
x.addKey('seq1', \dur, Pseq([0.1, 0.1, 0.08, 0.1], inf));
x.addKey('seq1', \note, Pseq([0, 2, 4, 2], inf));
x.addKey('seq1', \octave, 4);
x.addKey('seq1', \filterCutoffLFO2BottomRatio, 1);
x.addKey('seq1', \filterCutoffLFO2TopRatio, 1);
x.addKey('seq1', \filterCutoff, 1500);
x.addKey('seq1', \osc1SubAmp, 1);
x.addKey('seq1', \amp, 0.25);


x.stopSequence('seq3');

(
x.playSequence('seq2');
x.playSequence('seq1');
)
x.makeSequence('seq2');
x.setReleaseTime(0.05);
x.addKey('seq2', \legato, 1);
x.addKey('seq2', \dur, Pseq([0.1, 0.1, 0.08, 0.1], inf));
x.addKey('seq2', \note, Pseq([ [0, -3], [2, -5],  [4, -12]], inf));
x.addKey('seq2', \octave, 5);
x.stopSequence('seq2');


z.addKey(\dur, Pseq(0.4 * [0.25, 0.25, 0.25, 0.25, 0.3, 0.3, 0.3, 0.3, 0.4, 0.4, 0.75, 0.75, 0.75, 0.75, 0.75, 0.75], inf));
z.addKey(\note, Pseq([0, 0, 0, 0, -3, -3, -3, -3, -5, -5, 2, 2, 2, 2, 2, 2], inf));
z.addKey(\octave, Pfunc({ 3}));
z.addKey(\legato, 1);
z.addKey(\filterCutoff, Pfunc({ 500 }));
z.addKey(\osc2Vol, Pseq([ -70], inf));
z.addKey(\filterCutoffLFO2BottomRatio, Pfunc({ 1 }));
z.addKey(\filterCutoffLFO2TopRatio, Pfunc({ 1 }));
z.addKey(\osc1SubAmp, Pfunc({ 1 }));
z.addKey(\osc2SubVol, Pfunc({ 0 }));
z.addKey(\amp, Pseq([0.6], inf));

x.makeSequencer('seq');
x.sequencerDict['seq'].play

y = x.sequencerDict['seq']
y.pause;
y.resume;

y.addKey(\dur, Pseq(7 * [0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.27, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2,  0.1, 0.1, 0.1, 0.1, 0.1, 0.1 ], inf));
y.addKey(\legato, 1);
y.addKey(\note, Pxrand([[0, 7], [7, 4], [0, 4],  [-5, 2], [0, 9], [4, 7], [4, 2], [7, 19], [0, 4, 7], [0, 4, 7, 11], [0, 2, 4, 5], -12], inf));
y.addKey(\octave, Prand([7, 5, 6, 6, 5, 5, 5], inf));
y.addKey(\osc2DetuneCents, Pwhite(-10, 10, inf));
y.addKey(\releaseTime, Pseq([ 2], inf));
y.addKey(\attackTime, Pseq([ 1], inf));


x.setFilterCutoffLFO2BottomRatio(0.8);
x.setFilterCutoffLFO2TopRatio(1.7);
x.setLFO2Freq(0.25);
x.setLFO2PulseWidth(0.8);
x.setLFO2Waveform(0.25);
x.setFilterCutoff(5700);
x.setFilterRes(0);

x.setOsc1Waveform('saw');
x.setOsc2Waveform('tri');
x.setOsc1SubVol(-70);
x.setOsc2SubVol(-18);
x.setOsc2WaveformLFO2Bottom(0);
x.setOsc2WaveformLFO2Top(0);
x.setOsc2PulseWidthLFO2Bottom(-0.5);
x.setOsc2PulseWidthLFO2Top(0.5);

x.setLFO1Waveform('sampleAndHold');
x.setLFO1Freq(12);
x.setLFOFreqLFO2TopRatio(1.5);
x.setLFOFreqLFO2BottomRatio(0.1);

x.setAmplitudeLFOBottom(1.1);
x.setAmplitudeLFOTop(1);

x.setOsc2PulseWidthLFOBottom(-0.5);
x.setOsc2PulseWidthLFOTop(0.5);
x.setOsc1AmpLFOBottom(0.2);


x.setNoiseOscAmpLFO2Bottom(0.3);
x.setNoiseOscVol(-70);
x.setNoiseOscFilterLFO2BottomRatio(1);
x.setNoiseOscFilterLFO2TopRatio(5);
x.setNoiseOscCutoff(1700);


x.playNote(72.midicps);
x.releaseNote(72.midicps);

s.makeWindow;
s.meter;


x.setOsc1WaveformLFO2Bottom(1);
x.setOsc1WaveformLFO2Top(0);

x.mixer.fadeOut(0, 30);

y.stop;
z.stop;

x.free;