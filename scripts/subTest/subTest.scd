x = Subtractive.new;

x.playNoteControlSurface(220);
x.releaseNoteControlSurface(0);
x.freeNoteControlSurface(0);
x.orderNum;
x.setReleaseTime(3);


(
var noteArray = [0, 2, 4, 5, 7, 9, 11, 12];
y = Base.new;
y.turnAllGridBlue;

4.do({ | index | y.turnGridRed(0, index); });
4.do({ | row |
  8.do({ | column |
    //note.postln;
    //column.postln;
    //row.postln;
    var midiNote = noteArray[column] + (12 * row) + 36;
    y.setGridFunc(column, row, {
      var voiceNumber = x.playNoteControlSurface(midiNote.midicps);
      y.setGridFunc(column, row, { x.releaseNoteControlSurface(voiceNumber) }, 'noteOff');
      }, 'noteOn');
    //y.setGridFunc(column, row, { x.releaseNote(midiNote.midicps); }, 'noteOff');
    //note = note + 1;
  });
});
)


x.numVoices;
x.freqArray;

x.synthDict[440].isPlaying;

x.synthDict;
x.playNote(440);
x.releaseNote(440);


y.setGridFunc(0, 0, { x.playNote(36.midicps); }, 'noteOn');
y.setGridFunc(0, 0, { x.releaseNote(36.midicps); }, 'noteOff');

Synth

x.setReleaseTime(7);
x.setFilterCutoffLFOTopRatio(0.3);
x.setFilterType('lowPass');
x.setFilterRes(0);
x.setFilterCutoffLFOBottomRatio(0.1);
x.setFilterEnvDecayTime(2);
x.setFilterEnvSustainRatio(0.5);

x.setFilterEnvPeakRatio(3.0);
x.setFilterEnvAttackTime(4);
x.setOsc2WaveformLFOBottom(-0.6);
x.setOsc2WaveformLFOTop(0.4);
x.setOsc1SubVol(0);
x.setOsc2Octave(2);
x.setOsc2OctaveMul(0.5);
x.setOsc2Vol(0);
x.setLFO1Freq(17);
x.setLFO1Waveform('sampleAndHold');
x.setLFOFreqLFO2TopRatio(1.7);
x.setLFOFreqLFO2BottomRatio(0.5);
x.setLFO2Waveform('saw');
x.setAmplitudeLFOBottom(0);

x.setOsc1FreqLFOTopRatio(1.5);
x.setOsc2FreqLFOTopRatio(0.79);

x.setPanL

x.setOsc1FreqEnvStartRatio(12);
x.setOsc1FreqEnvEndRatio(0.5);
x.setOsc1FreqEnvTime(4)

x.setOsc2FreqEnvStartRatio(0.25);
x.setOsc2FreqEnvEndRatio(4);
x.setOsc2FreqEnvTime(6);
Server.killAll;