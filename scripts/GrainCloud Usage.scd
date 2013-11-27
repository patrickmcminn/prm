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
x.setNoteArray([0])
x.setNoteArray([0, 0.3, -0.3, 0.6, -0.6, 1, -1, 0, 0, 0, 0, 0, 0, 0])
x.setTrigRate(10, 70)

x.setOctaveArray([2])

x.setInstArray(['percRevSaw', 'gaborSaw', 'rexpodecSaw'])

x.setTrigRate(700, 1000)
x.setTrigRate(500, 700)
x.setTrigRate(300, 500)
x.setTrigRate(50, 250)
x.setTrigRate(25, 75)
x.setTrigRate(1, 12)

x.setSustain(0.005, 0.05)
x.setPatternAmp(0.1, 0.15)

x.setRQ(1, 0.01)
x.setCutoff(1200, 5600)

x.setVol(0)

x.setNoteArray([0, 3, 7, 10, 12])
x.setNoteArray([-2, 2, 5, 9, 10])