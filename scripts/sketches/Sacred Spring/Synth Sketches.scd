x = GlitchySynth.new(0);

x.playNote(220);
x.releaseNote(220);

x.setFilterCutoff(14000);

x.free;

x = FeedbackSynth.new(0);

x.playNote(220);
x.releaseNote(220);

x.free;

x = Subtractive.new(0)

x = Subtractive.new(32)

s.meter


x.playNote(220);
x.releaseNote(220);


x.setAttackTime(0.01);
x.setDecayTime(0.1);
x.setSustainLevel(0);
x.setReleaseTime(0.01);

x.setLFO1Freq(5);
x.setLFO1Waveform('noise');

x.setPanLFO1Bottom(-1);
x.setPanLFO1Top(1);

x.setOsc1Waveform('rect');
x.setOsc1PulseWidth(0.5);
x.setOsc1PulseWidthLFO2Bottom(-0.3);
x.setOsc1PulseWidthLFO2Top(0.4);

x.setOsc2PulseWidth(0.3);
x.setOsc2PulseWidthLFO2Top(0.6);
x.setOsc2PulseWidthLFO2Bottom(-0.2);

x.setOsc1Octave(3);
x.setOsc2Octave(3);

x.setOsc1SubVol(-inf);

x.setFilterCutoff(4500);



x.setLFO2Freq(0.3);


MIDIIn.connectAll;


y = Array.fill(127, { | i | MIDIFunc.noteOn({ x.playNote(i.midicps) }, i, 0); });
z = Array.fill(127, { | i | MIDIFunc.noteOff({ x.releaseNote(i.midicps) }, i, 0); });

y.free;
z.free;


a = IM_HardwareIn.new(32, 0);
b = IM_HardwareIn.new(33, 1);

a.free;
b.free;