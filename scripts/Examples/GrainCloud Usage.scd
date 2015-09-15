x = GrainCloud.new

x.noteArray
x.octaveArray
x.durLow

x.faderSynth.set(\amp, 6)

z.stream = Pbind(\freq, 800).asStream

x.setOctaveArray([4, 3, 6, 5])
x.setSustain(0.05, 0.19)
x.faderSynth.set(\amp, 2)
x.setNoteArray([0, 3, 7, 10, 12])
x.setNoteArray([\r])
x.setNoteArray([0, 0.3, -0.3, 0.6, -0.6, 1, -1, 0, 0, 0, 0, 0, 0, 0])
x.setTrigRate(10, 70)

x.setOctaveArray([3])

x.setInstArray(['rexpodecSaw'])

x.setTrigRate(700, 1000)
x.setTrigRate(500, 700)
x.setTrigRate(150, 500)
x.setTrigRate(50, 250)
x.setTrigRate(25, 75)
x.setTrigRate(7, 50)

x.setSustain(0.05, 0.5)
x.setPatternAmp(0.1, 0.9)



// even triggers:
x.setInstArray(['expodecSaw'])
x.setTrigRate(250, 100)
x.setSustain(1, 0.1)
x.setPan(0, 0)
x.setPatternAmp(1, 0.2)
x.setCutoff(1200, 10)



x.setRQ(0.5, 1)
x.setRQ(0.7, 0.05)

x.setCutoff(1200, 1200)

x.setVol(-6)

x.setNoteArray([0, 3, 7, 10, 12])
x.setNoteArray([-2, 2, 5, 9, 10])

x.toggleNote(2)



x.noteArray

x.noteArray.add(24)

MIDIIn.connectAll;
x = GrainCloud.new;
x.setOctaveArray([2]);
x.setNoteArray([\r])

(
72.do({ | i |
  MIDIFunc.noteOn({
    x.toggleNote(i);
}, 24 + i);
  //MIDIFunc.noteOff({
    //x.toggleNote(i);
//}, 24 + i);
});
)

MIDIdef.noteOn(\test, { "tester".postln; }, 60)
s.makeWindow
s.meter

